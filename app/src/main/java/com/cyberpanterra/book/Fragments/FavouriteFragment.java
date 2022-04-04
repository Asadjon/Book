package com.cyberpanterra.book.Fragments;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.cyberpanterra.book.Adapters.ChaptersDataAdapter;
import com.cyberpanterra.book.Datas.FavouriteDatabase;
import com.cyberpanterra.book.Datas.FavouriteRepository;
import com.cyberpanterra.book.Datas.Theme;
import com.cyberpanterra.book.Interactions.StaticClass;
import com.cyberpanterra.book.Interfaces.ForAdapters;
import com.cyberpanterra.book.Interfaces.IOnBackPressed;
import com.cyberpanterra.book.R;
import com.cyberpanterra.book.UI.FavouriteViewModel;
import com.cyberpanterra.book.UI.FavouriteViewModelFactory;
import com.cyberpanterra.book.UI.SharedViewModel;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class FavouriteFragment extends Fragment implements IOnBackPressed, ForAdapters {

    private SharedViewModel mSharedViewModel;
    private FavouriteViewModel mFavouriteViewModel;
    private ChaptersDataAdapter mAdapter;
    private SearchView mSearchView;
    private TextView mEmptyText;
    private boolean mIsExpandSearchView = false;

    public FavouriteFragment() { super(R.layout.fragment_favourite); }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        mSharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        mFavouriteViewModel = new ViewModelProvider(requireActivity(), provideFavouriteViewModelFactory()).get(FavouriteViewModel.class);

        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        new ItemTouchHelper(chapterCallback).attachToRecyclerView(mRecyclerView);

        mAdapter = new ChaptersDataAdapter(this, this::OnClick);
        mRecyclerView.setAdapter(mAdapter);

        mFavouriteViewModel.getFavourites().observe(requireActivity(), chapters -> mAdapter.setChapters(chapters));

        mEmptyText = view.findViewById(R.id.emptyText);

        checkFavourites();
    }

    public void OnClick(@NotNull Theme theme) {
        if (mIsExpandSearchView)
            StaticClass.setShowKeyboard(requireContext(), requireActivity().getCurrentFocus(), false);

        mSharedViewModel.setData(theme);
        NavHostFragment.findNavController(this).navigate(R.id.action_navigation_saved_to_navigation_open_data);
    }

    private final ItemTouchHelper.SimpleCallback chapterCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean isItemViewSwipeEnabled() { return mAdapter.isSelected(); }

        @Override
        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState == 0) mAdapter.setSelected(false);
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) { return false; }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            mFavouriteViewModel.removeChapter(mAdapter.getChapterAt(viewHolder.getAdapterPosition()));
            checkFavourites();
        }
    };

    @NotNull
    @Contract(" -> new")
    private FavouriteViewModelFactory provideFavouriteViewModelFactory() {
        return new FavouriteViewModelFactory(FavouriteRepository.getInstance(FavouriteDatabase.getInstance(requireContext()).getFavouriteThemes()));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_favourite, menu);

        MenuItem menuItem_search = menu.findItem(R.id.action_search);
        menuItem_search.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                mIsExpandSearchView = true;
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                mIsExpandSearchView = false;
                return true;
            }
        });

        mSearchView = (SearchView) menuItem_search.getActionView();
        mSearchView.setQueryHint(requireContext().getResources().getString(android.R.string.search_go));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                favouriteEmpty(mAdapter.filter(newText));
                return false;
            }
        });

        EditText eSearchView = mSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
        eSearchView.setHintTextColor(requireContext().getResources().getColor(R.color.search_query_text));
        eSearchView.setTextColor(requireContext().getResources().getColor(R.color.white));
        eSearchView.getViewTreeObserver().addOnGlobalLayoutListener(() -> StaticClass.keyboardShown(eSearchView.getRootView()));

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void favouriteEmpty(boolean isEmpty){
        mEmptyText.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onBackPressed() {
        if (mIsExpandSearchView) mSearchView.onActionViewCollapsed();
        return !mIsExpandSearchView;
    }

    @Override
    public FavouriteViewModel getFavouriteViewModel() { return mFavouriteViewModel; }

    @Override
    public void checkFavourites() {
//        if(mIsExpandSearchView) StaticClass.setShowKeyboard(requireContext(), requireActivity().getCurrentFocus(), false);
        favouriteEmpty(mAdapter.getCurrentList().isEmpty());
    }
}