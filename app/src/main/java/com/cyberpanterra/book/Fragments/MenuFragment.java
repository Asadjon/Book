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
import com.cyberpanterra.book.Datas.Chapter;
import com.cyberpanterra.book.Datas.ChaptersData;
import com.cyberpanterra.book.Datas.Theme;
import com.cyberpanterra.book.Interfaces.IOnBackPressed;
import com.cyberpanterra.book.Interactions.StaticClass;
import com.cyberpanterra.book.MainActivity;
import com.cyberpanterra.book.R;
import com.cyberpanterra.book.UI.SharedViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MenuFragment extends Fragment implements IOnBackPressed {

    private SharedViewModel sharedViewModel;
    private ChaptersDataAdapter mAdapter;
    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private TextView mEmptyText;
    private boolean mIsExpandSearchView = false;

    public MenuFragment() { super(R.layout.fragment_menu); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        List<Chapter> chapters = ChaptersData.getInstance(requireContext(), MainActivity.DATABASE_NAME).getChapterList();
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mAdapter = new ChaptersDataAdapter(false, chapters, this::OnClick, null);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);

        mEmptyText = view.findViewById(R.id.emptyText);

        favouriteEmpty();
    }

    private void favouriteEmpty(){
        if(mAdapter.getChapters().isEmpty()){
            mRecyclerView.setVisibility(View.GONE);
            mEmptyText.setVisibility(View.VISIBLE);
        }else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

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

        EditText eSearchView = mSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
        eSearchView.setHintTextColor(requireContext().getResources().getColor(R.color.search_query_text));
        eSearchView.setTextColor(requireContext().getResources().getColor(R.color.white));
        eSearchView.getViewTreeObserver().addOnGlobalLayoutListener(() -> StaticClass.keyboardShown(eSearchView.getRootView()));

        super.onCreateOptionsMenu(menu, inflater);
    }

    public void OnClick(@NotNull Theme theme) {
        if(mIsExpandSearchView) StaticClass.setShowKeyboard(requireContext(), requireActivity().getCurrentFocus(), false);

        sharedViewModel.setData(theme);
        NavHostFragment.findNavController(this).navigate(R.id.action_navigation_menu_to_navigation_open_data);
    }

    @Override
    public boolean onBackPressed() {
        if(mIsExpandSearchView) mSearchView.onActionViewCollapsed();

        return !mIsExpandSearchView;
    }
}