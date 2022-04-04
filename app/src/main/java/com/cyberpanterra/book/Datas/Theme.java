package com.cyberpanterra.book.Datas;

/* 
    The creator of the Theme class is Asadjon Xusanjonov
    Created on 8:43, 23.03.2022
*/

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Theme {
    private Chapter mChapter;
    private final int mId;
    private final String mName;
    private final String mIndex;
    private final Pages mPages;
    private final boolean mIsSerialized;

    public Theme(int id, String index, String name, boolean isSerialized, Pages pages) {
        this(null, id, index, name, isSerialized, pages);
    }

    public Theme(Chapter chapter, int id, String index, String name, boolean isSerialized, Pages pages) {
        mChapter = chapter;
        mId = id;
        mName = name;
        mIndex = index;
        mPages = pages;
        mIsSerialized = isSerialized;
    }

    public Chapter getChapter() {
        return mChapter;
    }

    public Theme setChapter(Chapter chapter) {
        this.mChapter = chapter;
        return this;
    }

    public int getId() { return mId; }

    public String getName() { return mName; }

    public String getIndex() { return mIndex; }

    public Pages getPages() { return mPages; }

    public boolean isSerialized() { return mIsSerialized; }

    @NotNull
    public Theme clone(){
        return new Theme(mId, mIndex, mName, mIsSerialized, mPages).setChapter(mChapter.clone());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Theme theme = (Theme) o;
        return mId == theme.mId &&
                mName.equals(theme.mName) &&
                mIndex.equals(theme.mIndex) &&
                mPages.equals(theme.mPages);
    }
}
