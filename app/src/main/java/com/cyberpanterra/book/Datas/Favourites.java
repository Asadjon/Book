package com.cyberpanterra.book.Datas;

/* 
    The creator of the FakeChapder class is Asadjon Xusanjonov
    Created on 10:00, 26.03.2022
*/

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cyberpanterra.book.Interactions.StaticClass;

import java.util.Collections;
import java.util.List;

public class Favourites {
    private final FavouriteDatabaseController database;
    private final MutableLiveData<List<SimpleChapter>> chapters = new MutableLiveData<>();

    public Favourites(Context context) {
        database = FavouriteDatabaseController.Companion.init(context);
        chapters.setValue(database.getChapterList());
    }

    public void addChapter(SimpleChapter chapter) {
        List<SimpleChapter> chapters = database.getChapterList();
        boolean changed = addSimpleChapter(chapter, chapters);

        if (chapter.getClass() == Chapter.class && ((Chapter) chapter).clone().fullList.retainAll(((Chapter) chapters.get(chapters.indexOf(chapter))).fullList)) {
            ((Chapter) StaticClass.first(chapters, ch -> ch.equals(chapter))).set(((Chapter) chapter).fullList);
            changed = true;
        }

        if(changed) {
            this.chapters.setValue(chapters);
            database.setChapterList(chapters);
        }
    }

    private boolean addSimpleChapter(SimpleChapter chapter, List<SimpleChapter> chapters) {
        if (chapters.contains(chapter)) return false;

        chapters.add(chapter.getClass() == Chapter.class ?
                new Chapter(chapter.id, chapter.name, chapter.value, chapter.pages) :
                new SimpleChapter(chapter.id, chapter.name, chapter.value, chapter.pages));
        Collections.sort(chapters, (ch, ch1) -> ch.getId() - ch1.getId());
        return true;
    }

    public void addTheme(Theme favouriteTheme) {
        List<SimpleChapter> chapters = database.getChapterList();
        addSimpleChapter(favouriteTheme.getChapter(), chapters);

        ((Chapter) StaticClass.first(chapters, ch -> ch instanceof Chapter && ch.equals(favouriteTheme.getChapter()))).add(favouriteTheme);

        this.chapters.setValue(chapters);
        database.setChapterList(chapters);
    }

    public void removeTheme(Theme favouriteTheme) {
        List<SimpleChapter> chapters = database.getChapterList();
        Chapter chapter = (Chapter) StaticClass.first(chapters, ch -> ch instanceof Chapter && ch.equals(favouriteTheme.getChapter()));
        if (chapter != null) {
            chapter.remove(favouriteTheme);

            if (chapter.isEmpty()) chapters.remove(chapter);

            this.chapters.setValue(chapters);
            database.setChapterList(chapters);
        }
    }

    public void removeChapter(SimpleChapter favouriteChapter) {
        List<SimpleChapter> chapters = database.getChapterList();
        if (!chapters.contains(favouriteChapter)) return;
        chapters.remove(favouriteChapter);

        this.chapters.setValue(chapters);
        database.setChapterList(chapters);
    }

    public LiveData<List<SimpleChapter>> getFavouriteChapters() { return chapters; }

    public boolean isContains(Data data) {
        List<SimpleChapter> chapters = database.getChapterList();

        if (data.getClass() == Theme.class) {
            Chapter chapter = (Chapter) StaticClass.first(StaticClass.whereAll(chapters, ch -> ch.getClass() == Chapter.class), ch -> ch.equals(((Theme) data).getChapter()));
            return chapter != null && chapter.themeList.contains(data);
        } else return chapters.contains(data);
    }
}