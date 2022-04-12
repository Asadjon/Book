package com.cyberpanterra.book.Datas;

/* 
    The creator of the Pages class is Asadjon Xusanjonov
    Created on 18:04, 23.03.2022
*/

public class Pages{
    public int fromPage = 0;
    public int toPage = 0;

    public Pages() {}

    public Pages(int fromPage, int toPage) {
        this.fromPage = fromPage;
        this.toPage = toPage;
    }

    public Pages(Object[] pages) {
        if(pages != null && pages.length > 1) {
            if(pages[0] instanceof Integer) this.fromPage = (int) pages[0];
            if(pages[1] instanceof Integer) this.toPage = (int) pages[1];
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pages pages = (Pages) o;
        return fromPage == pages.fromPage &&
                toPage == pages.toPage;
    }
}
