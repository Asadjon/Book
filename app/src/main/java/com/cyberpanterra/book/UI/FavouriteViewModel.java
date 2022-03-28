package com.cyberpanterra.book.UI;

/* 
    The creator of the FavouriteViewModel class is Asadjon Xusanjonov
    Created on 10:50, 26.03.2022
*/

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cyberpanterra.book.Datas.Chapter;
import com.cyberpanterra.book.Datas.Theme;
import com.cyberpanterra.book.Datas.FavouriteRepository;

import java.util.List;

public class FavouriteViewModel extends ViewModel {

    private final FavouriteRepository favouriteRepository;

    public FavouriteViewModel(FavouriteRepository favouriteRepository) {
        this.favouriteRepository = favouriteRepository;
    }

    public LiveData<List<Chapter>> getFavourites() { return favouriteRepository.getFavourites(); }

    public void addFavourite(Theme theme) { favouriteRepository.addTheme(theme); }

    public void removeFavourite(Theme theme) { favouriteRepository.removeTheme(theme); }

    public void removeChapter(Chapter chapter) { favouriteRepository.removeChapter(chapter); }

    public boolean isContainsFavourite(Theme theme) { return favouriteRepository.isContains(theme); }
}
