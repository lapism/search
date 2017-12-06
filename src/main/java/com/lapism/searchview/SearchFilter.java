package com.lapism.searchview;

import android.os.Parcel;
import android.os.Parcelable;


@SuppressWarnings({"WeakerAccess", "unused"})
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
    private String mTitle;
    private boolean mIsChecked;
    private String mTag;

    public SearchFilter(String title, boolean checked) {
        this(title, checked, null);
    }

    public SearchFilter(String title, boolean checked, String tagId) {
        mTitle = title;
        mIsChecked = checked;
        mTag = tagId;
    }

    protected SearchFilter(Parcel in) {
        mTitle = in.readString();
        mIsChecked = in.readByte() != 0;
        mTag = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeByte((byte) (mIsChecked ? 1 : 0));
        dest.writeString(mTag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public boolean isChecked() {
        return mIsChecked;
    }

    public void setChecked(boolean checked) {
        mIsChecked = checked;
    }

    public String getTagId() {
        return mTag;
    }// todo remove ID

    public void setTagId(String tag) {
        mTag = tag;
    }

}
