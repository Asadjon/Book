package com.cyberpanterra.book.Datas;

/* 
    The creator of the FakeChapder class is Asadjon Xusanjonov
    Created on 10:00, 26.03.2022
*/

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavouriteThemes {
    private final FavouriteDatabaseController database;
    private final MutableLiveData<List<Chapter>> chapters = new MutableLiveData<>();

    public FavouriteThemes(Context context) {
        database = FavouriteDatabaseController.Companion.init(context);
        chapters.setValue(database.getChapters());
    }

    public void addTheme(Theme favouriteTheme){
        if(!database.getChapters().contains(favouriteTheme.getChapter())) {
            database.getChapters().add(favouriteTheme.getChapter().clone()
                    .setThemes(new ArrayList<>(Collections.singletonList(favouriteTheme))));
            Collections.sort(database.getChapters(), (chapter, chapter1) -> chapter.getId() - chapter1.getId());

        } else for(Chapter chapter : database.getChapters())
            if(chapter.equals(favouriteTheme.getChapter())){
                chapter.addTheme(favouriteTheme);
                break;
            }

        chapters.setValue(database.getChapters());
        database.setChapterList(database.getChapters());
    }

    public void removeTheme(Theme favouriteTheme){
        for(Chapter chapter : database.getChapters())
            if(chapter.equals(favouriteTheme.getChapter())){
                chapter.removeTheme(favouriteTheme);
                if(chapter.getFullThemes().size() == 0) database.getChapters().remove(chapter);

                chapters.setValue(database.getChapters());
                database.setChapterList(database.getChapters());
                break;
            }
    }

    public LiveData<List<Chapter>> getFavouriteChapters() { return chapters; }

    public boolean isContains(Theme theme){
        return database.getChapters().contains(theme.getChapter()) &&
                database.getChapters().get(database.getChapters().indexOf(theme.getChapter())).getFullThemes().contains(theme);
    }
}
