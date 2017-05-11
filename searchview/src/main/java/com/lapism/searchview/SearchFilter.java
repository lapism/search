package com.lapism.searchview;

import android.os.Parcel;
import android.os.Parcelable;


public class SearchFilter implements Parcelable {

    public static final Creator<SearchFilter> CREATOR = new Creator<SearchFilter>() {
        @Override
        public SearchFilter createFromParcel(Parcel in) {
            return new SearchFilter(in);
        }

        @Override
        public SearchFilter[] newArray(int size) {
            return new SearchFilter[size];
        }
    };

    private final String mTitle;
    private boolean mIsChecked;
    private String mTagId;

    public SearchFilter(String title, boolean checked) {
        this(title, checked, null);
    }

    public SearchFilter(String title, boolean checked, String tagId) {
        mTitle = title;
        mIsChecked = checked;
        mTagId = tagId;
    }

    SearchFilter(Parcel source) {
        mTitle = source.readString();
        mIsChecked = source.readByte() != 0;
        mTagId = source.readString();
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean isChecked() {
        return mIsChecked;
    }

    public void setChecked(boolean checked) {
        mIsChecked = checked;
    }

    public String getTagId() {
        return mTagId;
    }

    public void setTagId(String tagId) {
        mTagId = tagId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeByte((byte) (mIsChecked ? 1 : 0));
        parcel.writeString(mTagId);
    }

}