package com.cyberpanterra.book.Datas;

/* 
    The creator of the Data class is Asadjon Xusanjonov
    Created on 12:00, 07.04.2022
*/

import android.os.Build;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Data {
    final int id;
    String name = "";
    String value = "";
    Pages pages = new Pages();

    public Data(int id) { this.id = id; }

    public Data(int id, String name, String value, Pages pages) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.pages = pages;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Data setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public Data setValue(String value) {
        this.value = value;
        return this;
    }

    public Pages getPages() {
        return pages;
    }

    public Data setPages(Pages pages) {
        this.pages = pages;
        return this;
    }

    @NotNull
    public Data clone() { return new Data(getId(), getName(), getValue(), getPages()); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Data data = (Data) o;
        return id == data.id &&
                name.equals(data.name) &&
                value.equals(data.value) &&
                pages.equals(data.pages);
    }

    @Override
    public int hashCode() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ?
                Objects.hash(id, name, value, pages) :
                super.hashCode();
    }
}
