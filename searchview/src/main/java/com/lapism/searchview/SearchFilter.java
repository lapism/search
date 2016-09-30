package com.lapism.searchview;


@SuppressWarnings({"WeakerAccess", "unused"})
public class SearchFilter {

    private final String mTitle;
    private boolean mIsChecked;

    public SearchFilter(String title, boolean checked) {
        mTitle = title;
        mIsChecked = checked;
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

}
