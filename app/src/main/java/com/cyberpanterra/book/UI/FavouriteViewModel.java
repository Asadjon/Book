package com.cyberpanterra.book.UI;

/* 
    The creator of the FavouriteViewModel class is Asadjon Xusanjonov
    Created on 10:50, 26.03.2022
*/

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cyberpanterra.book.Datas.Chapter;
import com.cyberpanterra.book.Datas.Data;
import com.cyberpanterra.book.Datas.SimpleChapter;
import com.cyberpanterra.book.Datas.Theme;
import com.cyberpanterra.book.Datas.FavouriteRepository;

import java.util.List;

public class FavouriteViewModel extends ViewModel {

    private final FavouriteRepository favouriteRepository;

    public FavouriteViewModel(FavouriteRepository favouriteRepository) {
        this.favouriteRepository = favouriteRepository;
    }

    public LiveData<List<SimpleChapter>> getFavourites() { return favouriteRepository.getFavourites(); }

    public void addChapter(SimpleChapter chapter) { favouriteRepository.addChapter(chapter); }

    public void addTheme(Theme theme) { favouriteRepository.addTheme(theme); }

    public void removeTheme(Theme theme) { favouriteRepository.removeTheme(theme); }

    public void removeChapter(SimpleChapter chapter) { favouriteRepository.removeChapter(chapter); }

    public boolean isContainsFavourite(Data data) { return favouriteRepository.isContains(data); }
}
