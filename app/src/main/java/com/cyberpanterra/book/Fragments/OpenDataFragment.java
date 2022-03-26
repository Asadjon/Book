package com.cyberpanterra.book.Fragments;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.cyberpanterra.book.Datas.Theme;
import com.cyberpanterra.book.Interfaces.IOnBackPressed;
import com.cyberpanterra.book.MainActivity;
import com.cyberpanterra.book.R;
import com.cyberpanterra.book.UI.FavouriteViewModel;
import com.cyberpanterra.book.UI.FavouriteViewModelFactory;
import com.cyberpanterra.book.UI.SharedViewModel;
import com.cyberpanterra.book.Datas.FavouriteDatabase;
import com.cyberpanterra.book.Datas.FavouriteRepository;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FileUtils;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import org.jetbrains.annotations.NotNull;

public class OpenDataFragment extends Fragment implements IOnBackPressed {

    private PDFView mPdfView;
    private FavouriteViewModel favouriteViewModel;
    private Theme mTheme;
    private boolean isFavourite = false;

    public OpenDataFragment() { super(R.layout.fragment_open_data); }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolbar(view.findViewById(R.id.openDataToolbar));

        mPdfView = view.findViewById(R.id.pdfViewer);

        favouriteViewModel = new ViewModelProvider(requireActivity(), provideFavouriteViewModelFactory()).get(FavouriteViewModel.class);

        SharedViewModel mViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        mViewModel.getData().observe(requireActivity(), data -> {
            mTheme = data;
            isFavourite = favouriteViewModel.isContainsFavourite(data);

            int[] pages = new int[mTheme.getPages().toPage - mTheme.getPages().fromPage + 1];
            for (int i = 0; i < pages.length; i++) pages[i] = mTheme.getPages().fromPage + i - 1;

            PDFView.Configurator configurator = null;
            try { configurator = mPdfView.fromFile(FileUtils.fileFromAsset(requireContext(), "Book.pdf"));
            } catch (Exception e) { e.printStackTrace(); }

            if(configurator != null)
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
        });
    }

    private void setToolbar(@NotNull Toolbar toolbar){
        toolbar.inflateMenu(R.menu.menu_open_data);
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if(activity != null) {
            activity.setSupportActionBar(toolbar);
            ActionBar ab = activity.getSupportActionBar();
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
            setHasOptionsMenu(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_open_data, menu);
        MenuItem itemFavourite = menu.findItem(R.id.action_favourite);

        favouriteViewModel.getFavourites().observe(requireActivity(), chaptersList ->
                itemFavourite.setIcon(isFavourite ? R.drawable.ic_baseline_turned_in_24_light : R.drawable.ic_baseline_turned_in_not_24_light));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) requireActivity().onBackPressed();
        else if (item.getItemId() == R.id.action_favourite) changeFavourite();

        return super.onOptionsItemSelected(item);
    }

    private void changeFavourite(){
        isFavourite = !isFavourite;
        if(isFavourite) favouriteViewModel.addFavourite(mTheme.clone());
        else favouriteViewModel.removeFavourite(mTheme.clone());
    }

    private FavouriteViewModelFactory provideFavouriteViewModelFactory(){
        return new FavouriteViewModelFactory(FavouriteRepository.getInstance(FavouriteDatabase.getInstance(requireContext()).getFavouriteThemes()));
    }

    @Override
    public void onResume() {
        super.onResume();
        hide();
    }

    @Override
    public void onPause() {
        super.onPause();
        show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPdfView = null;
    }

    private void hide() {
        Activity activity = getActivity();
        if (activity != null) ((MainActivity) activity).setNavViewVisibility(View.GONE);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        Activity activity = getActivity();
        if (activity != null) ((MainActivity) activity).setNavViewVisibility(View.VISIBLE);
    }

    @Override
    public boolean onBackPressed() {
        show();
        return true;
    }
}