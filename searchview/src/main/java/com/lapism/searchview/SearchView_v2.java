package com.lapism.searchview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;


public class SearchView_v2 extends FrameLayout{

    private final Context mContext;

    public SearchView_v2(Context context) {
        this(context, null);
    }

    public SearchView_v2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView_v2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

}
