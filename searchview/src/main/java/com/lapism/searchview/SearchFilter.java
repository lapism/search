package com.lapism.searchview;

public class SearchFilter
{
    private boolean mIsChecked;
    private String mTitle;

    public SearchFilter(String title, boolean checked)
    {
        mTitle = title;
        mIsChecked = checked;
    }

    public String getTitle()
    {
        return mTitle;
    }

    public boolean isChecked()
    {
        return mIsChecked;
    }

    public void setChecked(boolean checked)
    {
        mIsChecked = checked;
    }
}