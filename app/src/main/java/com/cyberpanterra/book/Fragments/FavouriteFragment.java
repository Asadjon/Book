package com.cyberpanterra.book.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.cyberpanterra.book.Adapters.ChaptersDataAdapter;
import com.cyberpanterra.book.Datas.Data;
import com.cyberpanterra.book.Datas.FavouriteDatabase;
import com.cyberpanterra.book.Datas.FavouriteRepository;
import com.cyberpanterra.book.Datas.SimpleChapter;
import com.cyberpanterra.book.Datas.Theme;
import com.cyberpanterra.book.Interfaces.Action;
import com.cyberpanterra.book.Interfaces.IOnBackPressed;
import com.cyberpanterra.book.R;
import com.cyberpanterra.book.UI.FavouriteViewModel;
import com.cyberpanterra.book.UI.FavouriteViewModelFactory;
import com.cyberpanterra.book.ViewActivity;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class FavouriteFragment extends Fragment implements IOnBackPressed {

    private FavouriteViewModel mFavouriteViewModel;
    private ChaptersDataAdapter adapter;
    private TextView mEmptyText;
    private Action.IRAction<Void, Boolean> onSearchViewCollapse;

    public FavouriteFragment() { super(R.layout.fragment_favourite); }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        mEmptyText = view.findViewById(R.id.emptyText);

        adapter = new ChaptersDataAdapter()
                .setOnClickListener(this::OnClick)
                .setOnActionListener(this::OnRemove);

        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(adapter);

        mFavouriteViewModel = new ViewModelProvider(requireActivity(), provideFavouriteViewModelFactory()).get(FavouriteViewModel.class);
        mFavouriteViewModel.getFavourites().observe(requireActivity(), chapters -> {
            adapter.setChapterList(chapters);
            favouriteEmpty(chapters.isEmpty());
        });
    }

    public void OnClick(@NotNull Data data) {
        Intent intent = new Intent(requireContext(), ViewActivity.class);

        if (data.getClass() == Theme.class) {
            intent.putExtra(ViewActivity.THEME_INDEX, ((Theme) data).getChapter().indexOf(((Theme) data)));
            data = ((Theme) data).getChapter();
        }

        intent.putExtra(ViewActivity.IS_FAVOURITE_VIEWER, true);
        intent.putExtra(ViewActivity.CHAPTER_INDEX, adapter.getFullChapters().indexOf(data));
        startActivity(intent);
    }

    public void OnRemove(@NotNull Data data) {
        if (data instanceof Theme) mFavouriteViewModel.removeTheme((Theme) data);
        else mFavouriteViewModel.removeChapter((SimpleChapter) data);
    }

    private boolean searchViewCollapse() {
        try {
            return onSearchViewCollapse != null ? onSearchViewCollapse.call(null) : true;
        } catch (Exception e) { e.printStackTrace(); }
        return true;
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

    private void favouriteEmpty(boolean isEmpty) {
        mEmptyText.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        if (isEmpty) searchViewCollapse();
    }

    @Override
    public boolean onBackPressed() { return searchViewCollapse(); }
}