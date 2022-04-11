package com.cyberpanterra.book.Datas;

/* 
    The creator of the Theme class is Asadjon Xusanjonov
    Created on 8:43, 23.03.2022
*/

import org.jetbrains.annotations.NotNull;

public class Theme extends Data {
    private Chapter mChapter;

    public Theme(int id, String index, String name, Pages pages) {
        this(null, id, index, name, pages);
    }

    public Theme(Chapter chapter, int id, String name, String value, Pages pages) {
        super(id, name, value, pages);
        mChapter = chapter;
    }

    public Chapter getChapter() {
        return mChapter;
    }

    public Theme setChapter(Chapter chapter) {
        this.mChapter = chapter;
        return this;
    }

    @NotNull
    @Override
    public Theme clone(){ return ((Theme) super.clone()).setChapter(mChapter.clone()); }

    @Override
    public boolean equals(Object o) { return super.equals(o) && mChapter.equals(((Theme) o).getChapter()); }
}
