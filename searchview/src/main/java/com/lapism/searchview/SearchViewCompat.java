package com.lapism.searchview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.widget.FrameLayout;

// Preview of SearchView version 5.0
@CoordinatorLayout.DefaultBehavior(SearchBehavior.class)
public class SearchViewCompat extends FrameLayout {

    public static final int VERSION_TOOLBAR = 1000;
    public static final int VERSION_MENU_ITEM = 1001;

    public static final int VERSION_MARGINS_TOOLBAR_SMALL = 2000;
    public static final int VERSION_MARGINS_TOOLBAR_BIG = 2001;
    public static final int VERSION_MARGINS_MENU_ITEM = 2002;

    public static final int THEME_LIGHT = 3000;
    public static final int THEME_DARK = 3001;
    public static final int THEME_PLAY_STORE = 3002;

    public static final int SPEECH_REQUEST_CODE = 100;
    public static final int LAYOUT_TRANSITION_DURATION = 200;
    public static final int ANIMATION_DURATION = 300;

    public static final int TEXT_STYLE_NORMAL = 0;
    public static final int TEXT_STYLE_BOLD = 1;
    public static final int TEXT_STYLE_ITALIC = 2;
    public static final int TEXT_STYLE_BOLD_ITALIC = 3;

    private final Context mContext;

    static final String LOG_TAG = "SearchView";

    public SearchViewCompat(@NonNull Context context) {
        super(context);
        mContext = context;
        //initView();
        //initStyle(attrs, defStyleAttr);
    }

    public SearchViewCompat(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        //initView();
        //initStyle(attrs, defStyleAttr);
    }

    public SearchViewCompat(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        //initView();
        //initStyle(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SearchViewCompat(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        //initView();
        //initStyle(attrs, defStyleAttr);
    }



}