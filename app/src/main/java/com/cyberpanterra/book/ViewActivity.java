package com.cyberpanterra.book;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cyberpanterra.book.Datas.Chapter;
import com.cyberpanterra.book.Datas.ChaptersData;
import com.cyberpanterra.book.Datas.Data;
import com.cyberpanterra.book.Datas.FavouriteDatabase;
import com.cyberpanterra.book.Datas.FavouriteDatabaseController;
import com.cyberpanterra.book.Datas.FavouriteRepository;
import com.cyberpanterra.book.Datas.SimpleChapter;
import com.cyberpanterra.book.Datas.Theme;
import com.cyberpanterra.book.UI.FavouriteViewModel;
import com.cyberpanterra.book.UI.FavouriteViewModelFactory;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FileUtils;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ViewActivity extends AppCompatActivity {
    public static final String IS_FAVOURITE_VIEWER = "com.cyberpanterra.book.IS_FAVOURITE_VIEWER";
    public static final String CHAPTER_INDEX = "com.cyberpanterra.book.CHAPTER_INDEX";
    public static final String THEME_INDEX = "com.cyberpanterra.book.THEME_INDEX";

    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    private PDFView pdfView;
    private FavouriteViewModel favouriteViewModel;
    private Data data;
    private boolean isFavourite = false;

    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    @SuppressLint("InlinedApi")
    private final Runnable mHidePart2Runnable = () -> {
        pdfView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    };
    private final Runnable mShowPart2Runnable = () -> {
        // Delayed display of UI elements
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.show();
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = this::hide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view);

        mVisible = true;
        pdfView = findViewById(R.id.pdfViewer);

        pdfView.setOnClickListener(view -> toggle());

        favouriteViewModel = new ViewModelProvider(this, provideFavouriteViewModelFactory()).get(FavouriteViewModel.class);

        Intent intent = getIntent();

        List<SimpleChapter> chapters = intent.getBooleanExtra(IS_FAVOURITE_VIEWER, false) ?
                FavouriteDatabaseController.init(this).getChapterList() :
                ChaptersData.getInstance(this, MainActivity.DATABASE_NAME).getChapterList();

        SimpleChapter chapter = chapters.get(intent.getIntExtra(CHAPTER_INDEX, 0));
        if (chapter.getClass() == Chapter.class)
            data = ((Chapter) chapter).get(intent.getIntExtra(THEME_INDEX, 0));
        else data = chapter;

        isFavourite = favouriteViewModel.isContainsFavourite(data);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(data.getName() + (data.getValue().isEmpty() ? "" : " - " + data.getValue()));

        int[] pages = new int[data.getPages().toPage - data.getPages().fromPage + 1];
        for (int i = 0; i < pages.length; i++) pages[i] = data.getPages().fromPage + i - 1;

        PDFView.Configurator configurator = null;
        try {
            configurator = pdfView.fromFile(FileUtils.fileFromAsset(this, "Book.pdf"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (configurator != null)
            configurator.pages(pages)
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .enableAnnotationRendering(true) // render annotations (such as comments, colors or forms)
                    .password(null)
                    .scrollHandle(null)
                    .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                    .spacing(0)
                    .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
                    .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
                    .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
                    .pageSnap(false) // snap pages to screen boundaries
                    .pageFling(false) // make a fling change only a single page like ViewPager
                    .nightMode(false) // toggle night mode
                    .load();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view, menu);
        MenuItem itemFavourite = menu.findItem(R.id.action_favourite);

        favouriteViewModel.getFavourites().observe(this, chaptersList ->
                itemFavourite.setIcon(isFavourite ? R.drawable.ic_baseline_turned_in_24_light : R.drawable.ic_baseline_turned_in_not_24_light));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        if (item.getItemId() == R.id.action_favourite) changeFavourite();

        return super.onOptionsItemSelected(item);
    }

    private void changeFavourite() {
        isFavourite = !isFavourite;
        if (isFavourite)
            if (data.getClass() == SimpleChapter.class)
                favouriteViewModel.addChapter((SimpleChapter) data);
            else favouriteViewModel.addTheme((Theme) data);

        else if (data.getClass() == SimpleChapter.class)
            favouriteViewModel.removeChapter((SimpleChapter) data);
        else if (data.getClass() == Theme.class) favouriteViewModel.removeTheme((Theme) data);

    }

    @NotNull
    private FavouriteViewModelFactory provideFavouriteViewModelFactory() {
        return new FavouriteViewModelFactory(FavouriteRepository.getInstance(FavouriteDatabase.getInstance(this).getFavouriteThemes()));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        show();
    }

    private void toggle() {
        if (mVisible) hide();
        else show();
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
        pdfView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);

        if (AUTO_HIDE) delayedHide(AUTO_HIDE_DELAY_MILLIS);
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}