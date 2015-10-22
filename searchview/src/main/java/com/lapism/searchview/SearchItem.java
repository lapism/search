package com.lapism.searchview;


public class SearchItem {

    private int icon;
    private String text;

    public SearchItem(int icon, String text) {
        this.icon = icon;
        this.text = text;
    }

    public int get_icon() {
        return this.icon;
    }

    public String get_text() {
        return this.text;
    }

}