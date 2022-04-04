package com.cyberpanterra.book;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.cyberpanterra.book.Datas.FavouriteDatabaseController;
import com.cyberpanterra.book.Interfaces.IOnBackPressed;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    public static final String DATABASE_NAME = "Mundarija.json";

    private BottomNavigationView mNavView;
    private NavHostFragment mNavHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FavouriteDatabaseController.init(this);
        setContentView(R.layout.activity_main);

        mNavView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_menu, R.id.navigation_saved, R.id.navigation_open_data)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(mNavView, navController);

        mNavHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
    }

    public void setNavViewVisibility(int visibility) { mNavView.setVisibility(visibility); }

    @Override public void onBackPressed() {
        Fragment fragment = null;
        if (mNavHostFragment != null) fragment = mNavHostFragment.getChildFragmentManager().getFragments().get(0);

        if (fragment instanceof IOnBackPressed ) { if(((IOnBackPressed) fragment).onBackPressed()) super.onBackPressed(); }
        else super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_about)
            startActivity(new Intent(this, AuthorActivity.class));

        return super.onOptionsItemSelected(item);
    }
}