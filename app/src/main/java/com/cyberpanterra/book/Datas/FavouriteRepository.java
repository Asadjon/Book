package com.cyberpanterra.book.Datas;

/* 
    The creator of the FavouriteRepository class is Asadjon Xusanjonov
    Created on 10:43, 26.03.2022
*/

import androidx.lifecycle.LiveData;

import java.util.List;

public class FavouriteRepository {
    private static FavouriteRepository repository = null;

    public static FavouriteRepository getInstance(FavouriteThemes favouriteThemes){
        return repository != null ? repository : (repository = new FavouriteRepository(favouriteThemes));
    }

    private FavouriteRepository(FavouriteThemes favouriteThemes) {
        this.favouriteThemes = favouriteThemes;
    }

    private final FavouriteThemes favouriteThemes;

    public void addTheme(Theme theme) { favouriteThemes.addTheme(theme); }

    public void removeTheme(Theme theme) { favouriteThemes.removeTheme(theme); }

    public void removeChapter(Chapter chapter) { favouriteThemes.removeChapter(chapter); }

    public boolean isContains(Theme theme) { return favouriteThemes.isContains(theme); }

    public LiveData<List<Chapter>> getFavourites() { return favouriteThemes.getFavouriteChapters(); }
}
