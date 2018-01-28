package com.lapism.searchview.widget;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;


public class SearchViewSavedState extends View.BaseSavedState {

    public static final Parcelable.Creator<SearchViewSavedState> CREATOR = new Creator<SearchViewSavedState>() {
        @NonNull
        @Override
        public SearchViewSavedState createFromParcel(Parcel source) {
            return new SearchViewSavedState(source);
        }

        @NonNull
        @Override
        public SearchViewSavedState[] newArray(int size) {
            return new SearchViewSavedState[size];
        }
    };
    public boolean hasFocus;
    public boolean shadow;
    public String query;

    private SearchViewSavedState(Parcel source) {
        super(source);
        this.query = source.readString();
        this.hasFocus = source.readInt() == 1;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    public SearchViewSavedState(Parcel source, ClassLoader loader) {
        super(source, loader);
        this.query = source.readString();
        this.hasFocus = source.readInt() == 1;
    }

    public SearchViewSavedState(Parcelable superState) {
        super(superState);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeString(query);
        out.writeInt(hasFocus ? 1 : 0);
    }

}
