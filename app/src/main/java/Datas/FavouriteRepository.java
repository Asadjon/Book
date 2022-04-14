package Datas;

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

    public void addData(Data data) { favourites.add(data); }

    public void removeData(Data data) { favourites.remove(data); }

    public boolean isContains(Data data) { return favourites.isContains(data); }

    public LiveData<List<Data>> getFavourites() { return favourites.getFavouriteChapters(); }
}
