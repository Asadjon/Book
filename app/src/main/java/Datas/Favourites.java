package Datas;

/* 
    The creator of the Favourites class is Asadjon Xusanjonov
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
    private final MutableLiveData<List<Data>> favourites = new MutableLiveData<>();

    public Favourites(Context context) {
        database = FavouriteDatabaseController.Companion.init(context);
        favourites.setValue(database.getChapterList());
    }

    public void add(Data favouriteData) {
        if (favouriteData instanceof Chapter) addChapter((Chapter) favouriteData);
        else if (favouriteData instanceof Theme) addTheme((Theme) favouriteData);
        else addData(favouriteData);
    }

    public void remove(Data favouriteData) {
        if (favouriteData instanceof Chapter) removeChapter((Chapter) favouriteData);
        else if (favouriteData instanceof Theme)  removeTheme((Theme) favouriteData);
        else removeData(favouriteData);
    }

    private void addData(Data data) {
        List<Data> favouriteList = database.getChapterList();

        if (!favouriteList.contains(data)) {
            favouriteList.add(data);
            Collections.sort(favouriteList, (ch, ch1) -> ch.getId() - ch1.getId());
            favourites.setValue(favouriteList);
            database.setChapterList(favouriteList);
        }
    }

    private void addChapter(Chapter chapter) {
        List<Data> favouriteList = database.getChapterList();
        boolean changed = false;

        if (!favouriteList.contains(chapter)) {
            favouriteList.add(new Chapter(chapter.id, chapter.name, chapter.value, chapter.pages));
            Collections.sort(favouriteList, (ch, ch1) -> ch.getId() - ch1.getId());
            changed = true;
        }

        List<Chapter> chapters = StaticClass.getListAt(StaticClass.whereAll(favouriteList, data -> data instanceof Chapter), data -> (Chapter) data);
        if (chapter.clone().themeList.retainAll(chapters.get(chapters.indexOf(chapter)).themeList)) {
            StaticClass.first(chapters, ch -> ch.equals(chapter)).set(chapter.themeList);
            changed = true;
        }

        if (changed) {
            favourites.setValue(favouriteList);
            database.setChapterList(favouriteList);
        }
    }

    private void addTheme(Theme theme) {
        List<Data> favouriteList = database.getChapterList();
        List<Chapter> chapters = StaticClass.getListAt(StaticClass.whereAll(favouriteList, data -> data instanceof Chapter), data -> (Chapter) data);

        Chapter chapter;
        if (chapters.isEmpty() || !chapters.contains(theme.chapter)) {
            chapter = new Chapter(theme.chapter);
            favouriteList.add(chapter);
            Collections.sort(favouriteList, (ch, ch1) -> ch.getId() - ch1.getId());
        } else chapter = chapters.get(chapters.indexOf(theme.chapter));

        if (!chapter.themeList.contains(theme)) {
            chapter.add(theme);

            favourites.setValue(favouriteList);
            database.setChapterList(favouriteList);
        }
    }

    private void removeData(Data data) {
        List<Data> favouriteList = database.getChapterList();

        if (!favouriteList.isEmpty()) {
            favouriteList.remove(data);

            favourites.setValue(favouriteList);
            database.setChapterList(favouriteList);
        }
    }

    private void removeChapter(Chapter chapter) {
        List<Data> chapters = database.getChapterList();

        if (chapters.isEmpty() || !chapters.contains(chapter)) return;

        chapters.remove(chapter);
        favourites.setValue(chapters);
        database.setChapterList(chapters);
    }

    private void removeTheme(Theme theme) {
        List<Data> favouriteList = database.getChapterList();
        List<Chapter> chapters = StaticClass.getListAt(StaticClass.whereAll(favouriteList, data -> data instanceof Chapter), data -> (Chapter) data);

        if (!chapters.isEmpty() && chapters.contains(theme.getChapter())) {
            Chapter chapter = chapters.get(chapters.indexOf(theme.getChapter()));

            if (!chapter.isEmpty() && chapter.themeList.contains(theme)) {
                chapter.remove(theme);

                if (chapter.isEmpty()) favouriteList.remove(chapter);

                this.favourites.setValue(favouriteList);
                database.setChapterList(favouriteList);
            }
        }
    }

    public LiveData<List<Data>> getFavouriteChapters() { return favourites; }

    public boolean isContains(Data data) {
        List<Data> chapters = database.getChapterList();

        if (data.getClass() == Theme.class) {
            Chapter chapter = (Chapter) StaticClass.first(StaticClass.whereAll(chapters, ch -> ch.getClass() == Chapter.class), ch -> ch.equals(((Theme) data).getChapter()));
            return chapter != null && chapter.themeList.contains(data);
        } else return chapters.contains(data);
    }
}