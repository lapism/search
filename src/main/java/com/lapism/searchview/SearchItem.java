package com.lapism.searchview;

import android.graphics.drawable.Drawable;
import android.os.Parcelable;


public class SearchItem implements Parcelable {

    private Drawable drawable;
    private int resource;
    private CharSequence title;
    private CharSequence subtitle;
    private String tag;

    public SearchItem() {
    }

    public SearchItem(CharSequence title) {
        this(R.drawable.ic_search_black_24dp, title, null);
    }

    public SearchItem(CharSequence title, String tag) {
        this(R.drawable.ic_search_black_24dp, title, tag);
    }

    public SearchItem(int resource, CharSequence title) {
        this(resource, title, null);
    }

    public SearchItem(int resource, CharSequence title, String tag) {
        this.resource = resource;
        this.title = title;
        this.tag = tag;
    }

    public SearchItem(Drawable icon, CharSequence title) {
        this(icon, title, null);
    }

    public SearchItem(Drawable icon, CharSequence title, String tag) {
        this.drawable = icon;
        this.title = title;
        this.tag = tag;
    }

    // dodelat resources int StringRes a CharSequence
    // TextUtils.writeToParcel(this.text, dest, flags);
    // writeValue

}
