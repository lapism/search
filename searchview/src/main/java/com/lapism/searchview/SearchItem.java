package com.lapism.searchview;


public class SearchItem {

    private final int icon;
    private final CharSequence text;

    public SearchItem(int icon, CharSequence text) {
        this.icon = icon;
        this.text = text;
    }

    public int get_icon() {
        return this.icon;
    }

    public CharSequence get_text() {
        return this.text;
    }

}