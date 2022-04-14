package com.cyberpanterra.book.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import Adapters.ChaptersDataAdapter;
import Datas.Data;
import Datas.FavouriteDatabase;
import Datas.FavouriteRepository;
import Datas.Theme;
import Interfaces.Action;
import Interfaces.IOnBackPressed;
import com.cyberpanterra.book.R;
import com.cyberpanterra.book.UI.FavouriteViewModel;
import com.cyberpanterra.book.UI.FavouriteViewModelFactory;
import com.cyberpanterra.book.Activities.ViewActivity;
import com.cyberpanterra.book.databinding.FragmentMainBinding;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class FavouriteFragment extends Fragment implements IOnBackPressed {

    private FavouriteViewModel mFavouriteViewModel;
    private FragmentMainBinding binding;
    private Action.IRAction<Void, Boolean> onSearchViewCollapse;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        binding.setAdapter(new ChaptersDataAdapter()
                .setOnClickListener(this::OnClick)
                .setOnActionListener(this::OnRemove));

        mFavouriteViewModel = new ViewModelProvider(requireActivity(), (ViewModelProvider.Factory) provideFavouriteViewModelFactory()).get(FavouriteViewModel.class);
        mFavouriteViewModel.getFavourites().observe(requireActivity(), chapters -> binding.getAdapter().setChapterList(chapters));
    }

    public void OnClick(@NotNull Data data) {
        Intent intent = new Intent(requireContext(), ViewActivity.class);

        if (data.getClass() == Theme.class) {
            intent.putExtra(ViewActivity.THEME_INDEX, ((Theme) data).getChapter().indexOf(((Theme) data)));
            data = ((Theme) data).getChapter();
        }

        intent.putExtra(ViewActivity.IS_FAVOURITE_VIEWER, true);
        intent.putExtra(ViewActivity.CHAPTER_INDEX, binding.getAdapter().getFullChapters().indexOf(data));
        startActivity(intent);
    }

    public void OnRemove(@NotNull Data data) {
        mFavouriteViewModel.removeData(data);
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
                binding.getAdapter().getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onBackPressed() { return searchViewCollapse(); }
}