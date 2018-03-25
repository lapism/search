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

    public static final Parcelable.Creator<SearchItem> CREATOR = new Parcelable.Creator<SearchItem>() {
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
    private int icon_1_resource;
    private int icon_2_resource;
    private CharSequence title;
    private CharSequence subtitle;
    private Context context;

    public SearchItem(Context context) {
        this.context = context;
    }

    private SearchItem(Parcel in) {
        Bitmap bitmap_1 = in.readParcelable(this.getClass().getClassLoader());
        icon_1_drawable = new BitmapDrawable(context.getResources(), bitmap_1);
        Bitmap bitmap_2 = in.readParcelable(this.getClass().getClassLoader());
        icon_2_drawable = new BitmapDrawable(context.getResources(), bitmap_2);

        icon_1_resource = in.readInt();
        icon_2_resource = in.readInt();

        title = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        subtitle = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
    }

    Drawable getIcon1Drawable() {
        return icon_1_drawable;
    }

    public void setIcon1Drawable(Drawable icon_1_drawable) {
        this.icon_1_drawable = icon_1_drawable;
    }

    Drawable getIcon2Drawable() {
        return icon_2_drawable;
    }

    public void setIcon2Drawable(Drawable icon_2_drawable) {
        this.icon_2_drawable = icon_2_drawable;
    }

    int getIcon1Resource() {
        return icon_1_resource;
    }

    public void setIcon1Resource(int icon_1_resource) {
        this.icon_1_resource = icon_1_resource;
    }

    int getIcon2Resource() {
        return icon_2_resource;
    }

    public void setIcon2Resource(int icon_2_resource) {
        this.icon_2_resource = icon_2_resource;
    }

    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public CharSequence getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(CharSequence subtitle) {
        this.subtitle = subtitle;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bitmap bitmap_1 = ((BitmapDrawable) icon_1_drawable).getBitmap();
        dest.writeParcelable(bitmap_1, flags);
        Bitmap bitmap_2 = ((BitmapDrawable) icon_2_drawable).getBitmap();
        dest.writeParcelable(bitmap_2, flags);

        dest.writeInt(icon_1_resource);
        dest.writeInt(icon_2_resource);

        TextUtils.writeToParcel(title, dest, flags);
        TextUtils.writeToParcel(subtitle, dest, flags);
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

To read the Drawable from the Parcelable:

Bitmap bitmap = (Bitmap) in.readParcelable(getClass().getClassLoader());
if ( bitmap != null ) {
    drawable = new BitmapDrawable(bitmap);
}
else {
    drawable = null;
}
*/