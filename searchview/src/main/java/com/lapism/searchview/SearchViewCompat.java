package com.lapism.searchview;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by martinlapis on 12.4.17.
 */

@CoordinatorLayout.DefaultBehavior(SearchBehavior.class)
public class SearchViewCompat extends FrameLayout {

    public SearchViewCompat(@NonNull Context context) {
        super(context);
    }

    public SearchViewCompat(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchViewCompat(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public SearchViewCompat(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}