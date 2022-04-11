package com.cyberpanterra.book.Datas;

/* 
    The creator of the FavouriteRepository class is Asadjon Xusanjonov
    Created on 10:43, 26.03.2022
*/

import androidx.lifecycle.LiveData;

import java.util.List;

public class FavouriteRepository {
    private static FavouriteRepository repository = null;

    public static FavouriteRepository getInstance(Favourites favouriteThemes){
        return repository != null ? repository : (repository = new FavouriteRepository(favouriteThemes));
    }

    private FavouriteRepository(Favourites favouriteThemes) {
        this.favourites = favouriteThemes;
    }

    private final Favourites favourites;

    public void addChapter(SimpleChapter chapter) { favourites.addChapter(chapter); }

    public void addTheme(Theme theme) { favourites.addTheme(theme); }

    public void removeTheme(Theme theme) { favourites.removeTheme(theme); }

    public void removeChapter(SimpleChapter chapter) { favourites.removeChapter(chapter); }

    public boolean isContains(Data data) { return favourites.isContains(data); }

    public LiveData<List<SimpleChapter>> getFavourites() { return favourites.getFavouriteChapters(); }
}
