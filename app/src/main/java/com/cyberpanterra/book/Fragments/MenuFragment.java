package com.cyberpanterra.book.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.cyberpanterra.book.Adapters.ChaptersDataAdapter;
import com.cyberpanterra.book.Datas.ChaptersData;
import com.cyberpanterra.book.Datas.Data;
import com.cyberpanterra.book.Datas.FavouriteDatabase;
import com.cyberpanterra.book.Datas.FavouriteRepository;
import com.cyberpanterra.book.Datas.SimpleChapter;
import com.cyberpanterra.book.Datas.Theme;
import com.cyberpanterra.book.Interfaces.Action;
import com.cyberpanterra.book.Interfaces.IOnBackPressed;
import com.cyberpanterra.book.MainActivity;
import com.cyberpanterra.book.R;
import com.cyberpanterra.book.UI.FavouriteViewModel;
import com.cyberpanterra.book.UI.FavouriteViewModelFactory;
import com.cyberpanterra.book.ViewActivity;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class MenuFragment extends Fragment implements IOnBackPressed {

    private FavouriteViewModel favouriteViewModel;
    private ChaptersDataAdapter adapter;
    private Action.IRAction<Void, Boolean> onSearchViewCollapse;

    public MenuFragment() { super(R.layout.fragment_menu); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        adapter = new ChaptersDataAdapter()
                .setChapterList(ChaptersData.getInstance(requireContext(), MainActivity.DATABASE_NAME).getChapterList())
                .setOnClickListener(this::OnClick)
                .setOnActionListener(this::onFavorite);
        mRecyclerView.setAdapter(adapter);

        favouriteViewModel = new ViewModelProvider(requireActivity(), provideFavouriteViewModelFactory()).get(FavouriteViewModel.class);
    }

    @NotNull
    @Contract(" -> new")
    private FavouriteViewModelFactory provideFavouriteViewModelFactory() {
        return new FavouriteViewModelFactory(FavouriteRepository.getInstance(FavouriteDatabase.getInstance(requireContext()).getFavouriteThemes()));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem menuItem_search = menu.findItem(R.id.action_search);

        onSearchViewCollapse = target -> {
            if (menuItem_search.isActionViewExpanded()) {
                MenuItemCompat.collapseActionView(menuItem_search);
                return false;
            } else {
                return true;
            }
        };
        onSearchViewCollapse();

        SearchView mSearchView = (SearchView) menuItem_search.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    public void OnClick(@NotNull Data data) {
        Intent intent = new Intent(requireContext(), ViewActivity.class);
        int chapterIndex;

        if (data.getClass() == Theme.class) {
            chapterIndex = adapter.getFullChapters().indexOf(((Theme) data).getChapter());
            int themeIndex = ((Theme) data).getChapter().indexOf(((Theme) data));
            intent.putExtra(ViewActivity.THEME_INDEX, themeIndex);
        } else {
            chapterIndex = adapter.getFullChapters().indexOf(data);
            intent.putExtra(ViewActivity.THEME_INDEX, 0);
        }

        intent.putExtra(ViewActivity.IS_FAVOURITE_VIEWER, false);
        intent.putExtra(ViewActivity.CHAPTER_INDEX, chapterIndex);
        startActivity(intent);
    }

    public void onFavorite(@NotNull Data data) {
        if (data instanceof Theme) favouriteViewModel.addTheme((Theme) data);
        else favouriteViewModel.addChapter((SimpleChapter) data);
        adapter.notifyDataSetChanged();
    }

    private boolean onSearchViewCollapse() {
        return onSearchViewCollapse != null ? onSearchViewCollapse.call(null) : true;
    }

    @Override
    public boolean onBackPressed() {
        return onSearchViewCollapse();
    }
}