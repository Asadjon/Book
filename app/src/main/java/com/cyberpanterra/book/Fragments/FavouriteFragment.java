package com.cyberpanterra.book.Fragments;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cyberpanterra.book.Adapters.ChaptersDataAdapter;
import com.cyberpanterra.book.Datas.FavouriteDatabase;
import com.cyberpanterra.book.Datas.FavouriteDatabaseController;
import com.cyberpanterra.book.Datas.FavouriteRepository;
import com.cyberpanterra.book.Datas.Theme;
import com.cyberpanterra.book.Interactions.StaticClass;
import com.cyberpanterra.book.Interfaces.IOnBackPressed;
import com.cyberpanterra.book.R;
import com.cyberpanterra.book.UI.FavouriteViewModel;
import com.cyberpanterra.book.UI.FavouriteViewModelFactory;
import com.cyberpanterra.book.UI.SharedViewModel;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FavouriteFragment extends Fragment implements IOnBackPressed{

    private SharedViewModel mSharedViewModel;
    private FavouriteViewModel mFavouriteViewModel;
    private ChaptersDataAdapter mAdapter;
    private SearchView mSearchView;
    private TextView mEmptyText;
    private RecyclerView mRecyclerView;
    private boolean mIsExpandSearchView = false;

    public FavouriteFragment() { super(R.layout.fragment_favourite); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        mSharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        mFavouriteViewModel = new ViewModelProvider(requireActivity(), provideFavouriteViewModelFactory()).get(FavouriteViewModel.class);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mAdapter = new ChaptersDataAdapter(true, new ArrayList<>(FavouriteDatabaseController.Companion.init(requireContext()).getChapters()), this::OnClick, this::RemoveOnClick);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);

        mEmptyText = view.findViewById(R.id.emptyText);

        favouriteEmpty();
    }

    private void favouriteEmpty(){
        if(mAdapter.getChapters().isEmpty()){
            mRecyclerView.setVisibility(View.GONE);
            mEmptyText.setVisibility(View.VISIBLE);
            mAdapter.setTurnOnDelete(false);
        }else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyText.setVisibility(View.GONE);
        }
    }

    public void OnClick(@NotNull Theme theme) {
        if(mIsExpandSearchView) StaticClass.setShowKeyboard(requireContext(), requireActivity().getCurrentFocus(), false);

        mSharedViewModel.setData(theme);
        NavHostFragment.findNavController(this).navigate(R.id.action_navigation_saved_to_navigation_open_data);
    }

    public void RemoveOnClick(@NotNull Theme theme, Boolean isChapter) {
        if(mIsExpandSearchView) StaticClass.setShowKeyboard(requireContext(), requireActivity().getCurrentFocus(), false);

        if(isChapter) mFavouriteViewModel.removeChapter(theme.getChapter());
        else mFavouriteViewModel.removeFavourite(theme);

        favouriteEmpty();
    }

    @NotNull
    @Contract(" -> new")
    private FavouriteViewModelFactory provideFavouriteViewModelFactory(){
        return new FavouriteViewModelFactory(FavouriteRepository.getInstance(FavouriteDatabase.getInstance(requireContext()).getFavouriteThemes()));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_favourite, menu);

        MenuItem menuItem_search = menu.findItem(R.id.action_search);
        menuItem_search.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) { mIsExpandSearchView = true; return true; }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) { mIsExpandSearchView = false; return true; }
        });

        mSearchView = (SearchView) menuItem_search.getActionView();
        mSearchView.setQueryHint(requireContext().getResources().getString(android.R.string.search_go));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {  return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.filter(newText);
                favouriteEmpty();
                return false;
            }
        });

        EditText eSearchView = mSearchView.findViewById( androidx.appcompat.R.id.search_src_text);
        eSearchView.setHintTextColor(requireContext().getResources().getColor(R.color.search_query_text));
        eSearchView.setTextColor(requireContext().getResources().getColor(R.color.white));
        eSearchView.getViewTreeObserver().addOnGlobalLayoutListener(() -> StaticClass.keyboardShown(eSearchView.getRootView()));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete && mAdapter.getItemCount() > 0) {
            mAdapter.setTurnOnDelete(!mAdapter.getTurnOnDelete());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onBackPressed() {
        if(mIsExpandSearchView) mSearchView.onActionViewCollapsed();
        return !mIsExpandSearchView;
    }
}