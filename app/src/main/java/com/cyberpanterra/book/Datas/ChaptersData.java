package com.cyberpanterra.book.Datas;

/* 
    The creator of the ChaptersData class is Asadjon Xusanjonov
    Created on 14:27, 23.03.2022
*/

import android.annotation.SuppressLint;
import android.content.Context;

import com.cyberpanterra.book.Interactions.StaticClass;

import org.json.JSONObject;

import java.util.List;

public class ChaptersData extends JsonDataLoader {

    @SuppressLint("StaticFieldLeak")
    private static ChaptersData instance = null;

    public static ChaptersData getInstance(Context context, String fileName) {
        return instance == null ? (instance = new ChaptersData(context, fileName)) : instance;
    }

    private ChaptersData(Context context, String fileName) { super(context, fileName); }

    public List<SimpleChapter> getChapterList() {
        return StaticClass.<JSONObject, SimpleChapter> getJsonListAt(getValue(getMainJsonObject(), "Contents"), chapterJson -> {
            Chapter chapter = new Chapter(
                            getValue(chapterJson, "Chapter"),
                            getValue(chapterJson, "Name"),
                            getValue(chapterJson, "Value"),
                            new Pages(StaticClass.<Integer, Integer> getJsonListAt(getValue(chapterJson, "Pages"), target -> target).toArray()));

            return chapterJson.has("Themes") ?
                    chapter.set(StaticClass.<JSONObject, Theme> getJsonListAt(getValue(chapterJson, "Themes"), themeJson -> new Theme(
                                    getValue(themeJson, "Id"),
                                    getValue(themeJson, "Index"),
                                    getValue(themeJson, "Name"),
                                    new Pages(StaticClass.<Integer, Integer> getJsonListAt(getValue(themeJson, "Pages"), target -> target).toArray())))) :
                    new SimpleChapter(chapter);
        });
    }
}
