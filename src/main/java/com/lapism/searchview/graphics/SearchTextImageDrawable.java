package com.lapism.searchview.graphics;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.TypedValue;

import com.lapism.searchview.R;


public class SearchTextImageDrawable extends Drawable {

    private final String mText;
    private final Paint mPaint;
    private int mIntrinsicWidth;
    private int mIntrinsicHeight;

    public SearchTextImageDrawable(Resources resources, String text) {
        mText = text;

        float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 24f, resources.getDisplayMetrics());//23dp

        mPaint = new Paint();
        //mPaint.setColor(ResourcesCompat.getColor(resources, R.color.search_text_image, null));
        mPaint.setTextSize(mTextSize);
        mPaint.setAntiAlias(true);
        mPaint.setFakeBoldText(false);
        mPaint.setShadowLayer(2f, 0, 0, Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.LEFT);

        Rect bounds = new Rect();
        mPaint.getTextBounds(mText, 0, mText.length(), bounds);
        mIntrinsicWidth = bounds.width();
        mIntrinsicHeight = bounds.height();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect r = getBounds();

        int count = canvas.save();
        canvas.translate(r.left, r.top);
        int height = canvas.getHeight() < 0 ? r.height() : canvas.getHeight();
        canvas.drawText(mText, 0, height / 2 - ((mPaint.descent() + mPaint.ascent()) / 2), mPaint);
        canvas.restoreToCount(count);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return mIntrinsicWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mIntrinsicHeight;
    }

}
