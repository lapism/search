package com.lapism.searchview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.lapism.searchview.R;


public class SearchItem implements Parcelable {

    public static final Creator<SearchItem> CREATOR = new Creator<SearchItem>() {
        @NonNull
        @Override
        public SearchItem createFromParcel(@NonNull Parcel in) {
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

    private SearchItem(@NonNull Parcel in) {
        Bitmap bitmap_1 = in.readParcelable(getClass().getClassLoader());
        this.icon_1_drawable = new BitmapDrawable(context.getResources(), bitmap_1);
        Bitmap bitmap_2 = in.readParcelable(getClass().getClassLoader());
        this.icon_2_drawable = new BitmapDrawable(context.getResources(), bitmap_2);

        this.icon_1_resource = in.readInt();
        this.icon_2_resource = in.readInt();

        this.title = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        this.subtitle = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
    }

    public Drawable getIcon1Drawable() {
        return this.icon_1_drawable;
    }

    public void setIcon1Drawable(Drawable icon_1_drawable) {
        this.icon_1_drawable = icon_1_drawable;
    }

    public Drawable getIcon2Drawable() {
        return this.icon_2_drawable;
    }

    public void setIcon2Drawable(Drawable icon_2_drawable) {
        this.icon_2_drawable = icon_2_drawable;
    }

    public int getIcon1Resource() {
        return this.icon_1_resource;
    }

    public void setIcon1Resource(int icon_1_resource) {
        this.icon_1_resource = icon_1_resource;
    }

    public void setIcon1ResourcePlay() {
        this.icon_1_resource = R.drawable.search_ic_search_black_24dp;
    }

    public void setIcon1ResourceGoogle() {
        this.icon_1_resource = R.drawable.search_ic_trending_up_black_24dp;
    }

    public void setIcon2ResourceGoogle() {
        this.icon_2_resource = R.drawable.search_ic_arrow_back_rotated_black_24dp;
    }

    public int getIcon2Resource() {
        return this.icon_2_resource;
    }

    public void setIcon2Resource(int icon_2_resource) {
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

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        Bitmap bitmap_1 = ((BitmapDrawable) this.icon_1_drawable).getBitmap();
        dest.writeParcelable(bitmap_1, flags);
        Bitmap bitmap_2 = ((BitmapDrawable) this.icon_2_drawable).getBitmap();
        dest.writeParcelable(bitmap_2, flags);

        dest.writeInt(this.icon_1_resource);
        dest.writeInt(this.icon_2_resource);

        TextUtils.writeToParcel(this.title, dest, flags);
        TextUtils.writeToParcel(this.subtitle, dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
