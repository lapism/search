package com.lapism.searchview;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;


@SuppressWarnings("WeakerAccess")
public class SearchItem implements Parcelable {

    public static final Creator<SearchItem> CREATOR = new Creator<SearchItem>() {
        public SearchItem createFromParcel(Parcel source) {
            return new SearchItem(source);
        }

        public SearchItem[] newArray(int size) {
            return new SearchItem[size];
        }
    };


    private int icon;
    private CharSequence text;
    private String tag;

    public SearchItem() {
    }

    public SearchItem(CharSequence text) {
        this(R.drawable.ic_search_black_24dp, text, null);
    }

    public SearchItem(CharSequence text, String tag) {
        this(R.drawable.ic_search_black_24dp, text, tag);
    }

    public SearchItem(int icon, CharSequence text) {
        this(icon, text, null);
    }

    public SearchItem(int icon, CharSequence text, String tag) {
        this.icon = icon;
        this.text = text;
        this.tag = tag;
    }

    public SearchItem(Parcel in) {
        this.icon = in.readInt();
        this.text = in.readParcelable(CharSequence.class.getClassLoader());
        this.tag = in.readString();
    }

    public int get_icon() {
        return this.icon;
    }

    public void set_icon(int icon) {
        this.icon = icon;
    }

    public CharSequence get_text() {
        return this.text;
    }

    public void set_text(CharSequence text) {
        this.text = text;
    }

    public String get_tag() {
        return this.tag;
    }

    public void set_tag(String tag) {
        this.tag = tag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.icon);
        TextUtils.writeToParcel(this.text, dest, flags); // dest.writeValue(this.text);
        dest.writeString(this.tag);
    }

}