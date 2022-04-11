package com.cyberpanterra.book.Datas;

/* 
    The creator of the Chapter class is Asadjon Xusanjonov
    Created on 8:38, 23.03.2022
*/

import com.cyberpanterra.book.Interactions.StaticClass;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Chapter extends SimpleChapter {
    final List<Theme> themeList = new ArrayList<>();
    final List<Theme> fullList = new ArrayList<>();

    public Chapter(int id, String name, String value, Pages pages) {
        super(id, name, value, pages);
        setName(name).setValue(value);
    }

    public boolean isEmpty() { return themeList.isEmpty(); }

    public int size() { return themeList.size(); }

    public int indexOf(Theme theme) { return themeList.indexOf(theme); }

    public Theme get(int index) { return themeList.get(index); }

    public Chapter set(List<Theme> themes) {
        StaticClass.forEach(themes, theme -> theme.setChapter(this));
        themeList.clear();
        fullList.clear();
        themeList.addAll(themes);
        fullList.addAll(themeList);
        return this;
    }

    public void add(Theme newTheme){
        if(!themeList.contains(newTheme)) {
            themeList.add(newTheme);
            Collections.sort(themeList, (theme, theme1) -> theme.getId() - theme1.getId());
            set(new ArrayList<>(themeList));
        }
    }

    public void remove(Theme theme){
        if(themeList.contains(theme)) {
            themeList.remove(theme);
            set(new ArrayList<>(themeList));
        }
    }

    public void resetList(){
        themeList.clear();
        themeList.addAll(fullList);
    }

    public boolean isSearchResult(String searchingText){
        themeList.clear();
        themeList.addAll(StaticClass.whereAll(fullList, theme ->
                theme.getName().toUpperCase().contains(searchingText) ||
                        theme.getValue().toUpperCase().contains(searchingText)));

        return !themeList.isEmpty();
    }

    @NotNull
    public Chapter clone() { return new Chapter(id, name, value, pages).set(themeList); }
}

