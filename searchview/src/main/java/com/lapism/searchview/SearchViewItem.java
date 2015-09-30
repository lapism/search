package com.lapism.searchview;


public class SearchViewItem {

    private int icon;
    private String text;

    public SearchViewItem(int icon, String text) {
        this.icon = icon;
        this.text = text;
    }

    public int get_icon() {
        return this.icon;
    }

    public void set_icon(int icon) {
        this.icon = icon;
    }

    public String get_text() {
        return this.text;
    }

    public void set_text(String text) {
        this.text = text;
    }

}