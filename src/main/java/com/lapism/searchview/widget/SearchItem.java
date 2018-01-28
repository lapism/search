package com.lapism.searchview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;


public class SearchItem implements Parcelable {

    public static final Creator<SearchItem> CREATOR = new Creator<SearchItem>() {
        @NonNull
        @Override
        public SearchItem createFromParcel(Parcel in) {
            return new SearchItem(in);
        }

        @NonNull
        @Override
        public SearchItem[] newArray(int size) {
            return new SearchItem[size];
        }
    };
    private Drawable icon_1_drawable;
    private Drawable icon_2_drawable;
    private int icon_1_resource = 0;
    private int icon_2_resource = 0;
    private CharSequence title;
    private CharSequence subtitle;
    private String tag;
    private Context context;

    public SearchItem(Context context) {
        this.context = context;
    }

    private SearchItem(Parcel in) {
        Bitmap bitmap_1 = in.readParcelable(getClass().getClassLoader());
        this.icon_1_drawable = new BitmapDrawable(this.context.getResources(), bitmap_1);
        Bitmap bitmap_2 = in.readParcelable(getClass().getClassLoader());
        this.icon_2_drawable = new BitmapDrawable(this.context.getResources(), bitmap_2);

        this.icon_1_resource = in.readInt();
        this.icon_2_resource = in.readInt();

        this.title = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        this.subtitle = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);

        this.tag = in.readString();
    }

    public Drawable getIcon_1_drawable() {
        return this.icon_1_drawable;
    }

    public void setIcon_1_drawable(Drawable icon_1_drawable) {
        this.icon_1_drawable = icon_1_drawable;
    }

    public Drawable getIcon_2_drawable() {
        return this.icon_2_drawable;
    }

    public void setIcon_2_drawable(Drawable icon_2_drawable) {
        this.icon_2_drawable = icon_2_drawable;
    }

    public int getIcon_1_resource() {
        return this.icon_1_resource;
    }

    public void setIcon_1_resource(int icon_1_resource) {
        this.icon_1_resource = icon_1_resource;
    }

    public int getIcon_2_resource() {
        return this.icon_2_resource;
    }

    public void setIcon_2_resource(int icon_2_resource) {
        this.icon_2_resource = icon_2_resource;
    }

    public CharSequence getTitle() {
        return this.title;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public CharSequence getSubtitle() {
        return this.subtitle;
    }

    public void setSubtitle(CharSequence subtitle) {
        this.subtitle = subtitle;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bitmap bitmap_1 = ((BitmapDrawable) this.icon_1_drawable).getBitmap();
        dest.writeParcelable(bitmap_1, flags);
        Bitmap bitmap_2 = ((BitmapDrawable) this.icon_2_drawable).getBitmap();
        dest.writeParcelable(bitmap_2, flags);

        dest.writeInt(this.icon_1_resource);
        dest.writeInt(this.icon_2_resource);

        TextUtils.writeToParcel(this.title, dest, flags);
        TextUtils.writeToParcel(this.subtitle, dest, flags);

        dest.writeString(this.tag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}

/*
if ( drawable != null ) {
    Bitmap bitmap = (Bitmap) ((BitmapDrawable) drawable).getBitmap();
    parcel.writeParcelable(bitmap, flags);
}
else {
    parcel.writeParcelable(null, flags);
}

To read the Drawable from the Parceable:

Bitmap bitmap = (Bitmap) in.readParcelable(getClass().getClassLoader());
if ( bitmap != null ) {
    drawable = new BitmapDrawable(bitmap);
}
else {
    drawable = null;
}
*/