package com.cyberpanterra.book.Datas;

/* 
    The creator of the Chapter class is Asadjon Xusanjonov
    Created on 8:38, 23.03.2022
*/

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Chapter {
    private final int mId;
    private final String mName;
    private final String mValue;
    private List<Theme> mSerializedThemes = new ArrayList<>();
    private final List<Theme> mNonSerializedThemes = new ArrayList<>();
    private final List<Theme> mSerializedFullThemes = new ArrayList<>();
    private final List<Theme> mFullThemes = new ArrayList<>();

    public Chapter(int id, String name, String value, List<Theme> themes) {
        mId = id;
        mName = name;
        mValue = value;

        setThemes(themes);
    }

    public int getId() { return mId; }

    public String getName() { return mName; }

    public String getValue() { return mValue; }

    public List<Theme> getSerializedThemes() { return mSerializedThemes; }

    public List<Theme> getFullThemes() { return mFullThemes; }

    public Chapter setThemes(List<Theme> themes) {
        mFullThemes.clear();
        mSerializedThemes.clear();
        mNonSerializedThemes.clear();
        mSerializedFullThemes.clear();

        for (Theme theme : themes) {
            if (theme.isSerialized()) mSerializedThemes.add(theme);
            else mNonSerializedThemes.add(theme);
            theme.setChapter(this);
        }

        mFullThemes.addAll(themes);
        mSerializedFullThemes.addAll(mSerializedThemes);
        return this;
    }

    public void addTheme(Theme newTheme){
        if(!mFullThemes.contains(newTheme)) {
            mFullThemes.add(newTheme);
            Collections.sort(mFullThemes, (theme, theme1) -> theme.getId() - theme1.getId());
            setThemes(new ArrayList<>(mFullThemes));
        }
    }

    public void removeTheme(Theme theme){
        if(mFullThemes.contains(theme)) {
            mFullThemes.remove(theme);
            setThemes(new ArrayList<>(mFullThemes));
        }
    }

    public void resetData() { mSerializedThemes = new ArrayList<>(mSerializedFullThemes); }

    public boolean isSearchResult(String searchingText){
        for (Theme data : mSerializedFullThemes)
            if (data.getName().toUpperCase().contains(searchingText) || data.getIndex().toUpperCase().contains(searchingText))
                return true;

        return false;
    }

    @NotNull
    public Chapter clone() { return new Chapter(mId, mName, mValue, mFullThemes); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chapter chapter = (Chapter) o;
        return mId == chapter.mId &&
                Objects.equals(mName, chapter.mName) &&
                Objects.equals(mValue, chapter.mValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mName, mValue, mSerializedThemes, mNonSerializedThemes, mSerializedFullThemes, mFullThemes);
    }
}

