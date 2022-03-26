package com.cyberpanterra.book.Datas;

/* 
    The creator of the Pages class is Asadjon Xusanjonov
    Created on 18:04, 23.03.2022
*/

import java.util.Objects;

public class Pages{
    public int fromPage;
    public int toPage;

    public Pages(int fromPage, int toPage) {
        this.fromPage = fromPage;
        this.toPage = toPage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pages pages = (Pages) o;
        return fromPage == pages.fromPage &&
                toPage == pages.toPage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromPage, toPage);
    }
}
