package com.lapism.searchview;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;


@SuppressWarnings({"WeakerAccess", "SameParameterValue", "unused"})
public class SearchItem implements Parcelable {

    public static final Creator<SearchItem> CREATOR = new Creator<SearchItem>() {
        @Override
        public SearchItem createFromParcel(Parcel in) {
            return new SearchItem(in);
        }

        @Override
        public SearchItem[] newArray(int size) {
            return new SearchItem[size];
        }
    };
    private Drawable drawable;
    private int resource;
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

    public SearchItem(int resource, CharSequence text) {
        this(resource, text, null);
    }

    public SearchItem(int resource, CharSequence text, String tag) {
        this.resource = resource;
        this.text = text;
        this.tag = tag;
    }

    public SearchItem(Drawable icon, CharSequence text) {
        this(icon, text, null);
    }

    public SearchItem(Drawable icon, CharSequence text, String tag) {
        this.drawable = icon;
        this.text = text;
        this.tag = tag;
    }

    public SearchItem(Parcel in) {
        this.resource = in.readInt();
        this.text = in.readParcelable(CharSequence.class.getClassLoader());
        this.tag = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        dest.writeParcelable(bitmap, flags);
        dest.writeString(this.tag);
        dest.writeString(this.text.toString());
        dest.writeInt(this.resource);
        // TextUtils.writeToParcel(this.text, dest, flags);
        // writeValue
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getIconResource() {
        return this.resource;
    }

    public void setIconResource(int resource) {
        this.resource = resource;
    }

    public Drawable getIconDrawable() {
        return this.drawable;
    }

    public void setIconDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public CharSequence getText() {
        return this.text;
    }

    public void setText(CharSequence text) {
        this.text = text;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}
