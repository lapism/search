package com.lapism.searchview.widget;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;


public class SearchFilter implements Parcelable {

    public static final Creator<SearchFilter> CREATOR = new Creator<SearchFilter>() {
        @NonNull
        @Override
        public SearchFilter createFromParcel(Parcel in) {
            return new SearchFilter(in);
        }

        @NonNull
        @Override
        public SearchFilter[] newArray(int size) {
            return new SearchFilter[size];
        }
    };
    private String title;
    private boolean isChecked;
    private String tag;

    public SearchFilter(String title, boolean checked) {
        this(title, checked, null);
    }

    public SearchFilter(String title, boolean checked, String tagId) {
        this.title = title;
        this.isChecked = checked;
        this.tag = tagId;
    }

    protected SearchFilter(Parcel in) {
        this.title = in.readString();
        this.isChecked = in.readByte() != 0;
        this.tag = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeByte((byte) (this.isChecked ? 1 : 0));
        dest.writeString(this.tag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }

    public String getTagId() {
        return this.tag;
    }

    public void setTagId(String tag) {
        this.tag = tag;
    }

}
