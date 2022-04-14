package Datas;

/* 
    The creator of the Theme class is Asadjon Xusanjonov
    Created on 8:43, 23.03.2022
*/

import org.jetbrains.annotations.NotNull;

public class Theme extends Data {
    Chapter chapter;

    public Theme(int id, String index, String name, Pages pages) {
        this(null, id, index, name, pages);
    }

    public Theme(Chapter chapter, int id, String name, String value, Pages pages) {
        super(id, name, value, pages);
        this.chapter = chapter;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public Theme setChapter(Chapter chapter) {
        this.chapter = chapter;
        return this;
    }

    @NotNull
    @Override
    public Theme clone(){ return ((Theme) super.clone()).setChapter(chapter.clone()); }

    @Override
    public boolean equals(Object o) { return super.equals(o) && chapter.equals(((Theme) o).getChapter()); }
}
