package Datas;

/* 
    The creator of the FavouriteDatabase class is Asadjon Xusanjonov
    Created on 10:39, 26.03.2022
*/

import android.content.Context;

public class FavouriteDatabase {
    private static FavouriteDatabase database = null;
    public static FavouriteDatabase getInstance(Context context){
        return database != null ? database : (database = new FavouriteDatabase(context));
    }

    private FavouriteDatabase(Context context){ favouriteThemes = new Favourites(context); }

    private final Favourites favouriteThemes;

    public Favourites getFavouriteThemes() { return favouriteThemes; }
}
