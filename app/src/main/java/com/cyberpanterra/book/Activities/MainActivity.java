package com.cyberpanterra.book.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import Datas.FavouriteDatabaseController;
import com.cyberpanterra.book.Interactions.StaticClass;
import Interfaces.IOnBackPressed;
import com.cyberpanterra.book.R;
import com.cyberpanterra.book.databinding.ActivityMainBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    public static final String DATABASE_NAME = "Mundarija.json";

    private NavHostFragment mNavHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FavouriteDatabaseController.init(this);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_menu, R.id.navigation_saved)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        mNavHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem_search = menu.findItem(R.id.action_search);

        SearchView mSearchView = (SearchView) menuItem_search.getActionView();
        mSearchView.setQueryHint(getResources().getString(R.string.title_search));

        EditText eSearchView = mSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
        eSearchView.setTextColor(getResources().getColor(R.color.white));
        eSearchView.getViewTreeObserver().addOnGlobalLayoutListener(() -> StaticClass.keyboardShown(eSearchView.getRootView()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_about)
            startActivity(new Intent(this, AuthorActivity.class));

        return super.onOptionsItemSelected(item);
    }

    @Override public void onBackPressed() {
        Fragment fragment = null;
        if (mNavHostFragment != null) fragment = mNavHostFragment.getChildFragmentManager().getFragments().get(0);

        if (fragment instanceof IOnBackPressed ) { if(((IOnBackPressed) fragment).onBackPressed()) super.onBackPressed(); }
        else super.onBackPressed();
    }
}