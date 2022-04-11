package com.cyberpanterra.book.Datas;

/* 
    The creator of the ChaptersData class is Asadjon Xusanjonov
    Created on 14:27, 23.03.2022
*/

import android.annotation.SuppressLint;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChaptersData extends JsonDataLoader {

    @SuppressLint("StaticFieldLeak")
    private static ChaptersData instance = null;

    public static ChaptersData getInstance(Context context, String fileName) {
        return instance == null ? (instance = new ChaptersData(context, fileName)) : instance;
    }

    private ChaptersData(Context context, String fileName) { super(context, fileName); }

    public List<SimpleChapter> getChapterList() {
        JSONArray chaptersJson = getValue(getMainJsonObject(), "Contents");
        List<SimpleChapter> chapters = new ArrayList<>();

        try {
            for (int i = 0; i < chaptersJson.length(); i++) {
                JSONObject chapterJson = chaptersJson.getJSONObject(i);
                JSONArray chapterPageJson = getValue(chapterJson, "Pages");
                Pages pages = new Pages(chapterPageJson.getInt(0), chapterPageJson.getInt(1));

                if (chapterJson.has("Themes")) {
                    JSONArray themesJson = getValue(chapterJson, "Themes");
                    List<Theme> themes = new ArrayList<>();
                    for (int j = 0; j < themesJson.length(); j++) {
                        JSONObject themeJson = themesJson.getJSONObject(j);
                        JSONArray pageJson = getValue(chapterJson, "Pages");
                        themes.add(new Theme(
                                getValue(themeJson, "Id"),
                                getValue(themeJson, "Index"),
                                getValue(themeJson, "Name"),
                                new Pages(pageJson.getInt(0), pageJson.getInt(1))));
                    }
                    chapters.add(new Chapter(
                            getValue(chapterJson, "Chapter"),
                            getValue(chapterJson, "Name"),
                            getValue(chapterJson, "Value"),
                            pages).set(themes));
                } else {
                    chapters.add(new SimpleChapter(
                            getValue(chapterJson, "Chapter"),
                            getValue(chapterJson, "Name"),
                            getValue(chapterJson, "Value"),
                            pages));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return chapters;
    }
}
