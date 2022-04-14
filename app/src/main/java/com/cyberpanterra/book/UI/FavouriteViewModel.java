package com.cyberpanterra.book.UI;

/* 
    The creator of the FavouriteViewModel class is Asadjon Xusanjonov
    Created on 10:50, 26.03.2022
*/

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import Datas.Data;
import Datas.FavouriteRepository;

import java.util.List;

public class FavouriteViewModel extends ViewModel {

    private final FavouriteRepository favouriteRepository;

    public FavouriteViewModel(FavouriteRepository favouriteRepository) {
        this.favouriteRepository = favouriteRepository;
    }

    public LiveData<List<Data>> getFavourites() { return favouriteRepository.getFavourites(); }

    public void addData(Data data) { favouriteRepository.addData(data); }

    public void removeData(Data data) { favouriteRepository.removeData(data); }

    public boolean isContainsFavourite(Data data) { return favouriteRepository.isContains(data); }
}
