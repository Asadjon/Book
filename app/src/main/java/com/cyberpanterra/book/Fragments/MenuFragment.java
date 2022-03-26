package com.cyberpanterra.book.Fragments;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
    private boolean mIsExpandSearchView = false;

    public MenuFragment() { super(R.layout.fragment_home); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolbar(view.findViewById(R.id.menuToolbar));

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        List<Chapter> chapters = ChaptersData.getInstance(requireContext(), MainActivity.DATABASE_NAME).getChapterList();
        RecyclerView recyclerView = view.findViewById(R.id.chapters);
        mAdapter = new ChaptersDataAdapter(chapters, this::OnClick, this::OnClick);
        recyclerView.setAdapter(mAdapter);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
    }

    private void setToolbar(@NotNull Toolbar toolbar){
        setHasOptionsMenu(true);
        toolbar.inflateMenu(R.menu.menu_search);
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if(activity != null) activity.setSupportActionBar(toolbar);
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
                mAdapter.getFilter().filter(newText);
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