package com.lapism.searchview.widget;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;


public class SearchViewSavedState extends View.BaseSavedState {

    public static final Parcelable.Creator<SearchViewSavedState> CREATOR = new Parcelable.Creator<SearchViewSavedState>() {
        @NonNull
        @Override
        public SearchViewSavedState createFromParcel(@NonNull Parcel source) {
            return new SearchViewSavedState(source);
        }

        @NonNull
        @Override
        public SearchViewSavedState[] newArray(int size) {
            return new SearchViewSavedState[size];
        }
    };

    @Nullable
    public String query;
    boolean hasFocus;
    boolean shadow;

    private SearchViewSavedState(@NonNull Parcel source) {
        super(source);
        query = source.readString();
        hasFocus = source.readInt() == 1;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    public SearchViewSavedState(@NonNull Parcel source, ClassLoader loader) {
        super(source, loader);
        query = source.readString();
        hasFocus = source.readInt() == 1;
    }

    SearchViewSavedState(Parcelable superState) {
        super(superState);
    }

    @Override
    public void writeToParcel(@NonNull Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeString(query);
        out.writeInt(hasFocus ? 1 : 0);
    }

}
