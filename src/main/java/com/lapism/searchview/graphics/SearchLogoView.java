package com.lapism.searchview.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;


public class SearchLogoView extends AppCompatTextView {

    private Drawable mLogoDrawable;

    public SearchLogoView(Context context) {
        super(context);
    }

    public SearchLogoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchLogoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setLogo(String logoText) {
        //setLogo(new SearchTextImageDrawable(getResources(), logoText));
    }

    public void setLogo(@DrawableRes int drawableRes) {
        setLogo(ResourcesCompat.getDrawable(getResources(), drawableRes, null));
    }

    public void setLogo(Drawable drawable) {
        mLogoDrawable = drawable;
        mLogoDrawable.setBounds(0, 0, mLogoDrawable.getIntrinsicWidth(), mLogoDrawable.getIntrinsicHeight());
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!TextUtils.isEmpty(getText()))
            super.onDraw(canvas);
        else {
            if (mLogoDrawable != null) {
                mLogoDrawable.draw(canvas);
            }
        }
    }

}
