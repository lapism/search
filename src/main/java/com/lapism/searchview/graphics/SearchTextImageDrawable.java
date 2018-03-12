package com.lapism.searchview.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.TypedValue;

import com.lapism.searchview.R;
import com.lapism.searchview.Search;


public class SearchTextImageDrawable extends Drawable {

    private final String mText;
    private final Paint mPaint;
    private int mIntrinsicWidth;
    private int mIntrinsicHeight;

    public SearchTextImageDrawable(Context context, String text) {
        mText = text;

        float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10f, context.getResources().getDisplayMetrics());

        //AssetManager am = context.getApplicationContext().getAssets();
        Typeface typeface;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            typeface = context.getResources().getFont(R.font.roboto_medium);
        } else {
            typeface = ResourcesCompat.getFont(context, R.font.roboto_regular);
        }
        /*
        android:fontFamily="sans-serif"           // Roboto-Regular.ttf
        android:fontFamily="sans-serif-medium"    // Roboto-Medium.ttf    21+
        */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // typeface = Typeface.createFromAsset(am, String.format(Locale.getDefault(), "fonts/%s", "Roboto-Medium.ttf"));
        } else {
            // typeface = Typeface.createFromAsset(am, String.format(Locale.getDefault(), "fonts/%s", "Roboto-Regular.ttf"));
        }

        mPaint = new Paint();
        mPaint.setTextSize(size);
        mPaint.setAntiAlias(true);
        mPaint.setTypeface(typeface);
        mPaint.setFakeBoldText(false);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);

        Rect bounds = new Rect();
        mPaint.getTextBounds(mText, 0, mText.length(), bounds);
        mIntrinsicWidth = bounds.width();
        mIntrinsicHeight = bounds.height();

        setTheme(Search.Theme.PLAY, context.getResources());//todo upravit + vycentrovat
    }

    public void setTheme(@Search.Theme int theme, Resources resources) {
        switch (theme) {
            case Search.Theme.PLAY:
                mPaint.setColor(ResourcesCompat.getColor(resources, R.color.search_play_text_image, null)); // theme
                break;
            case Search.Theme.COLOR:
                mPaint.setColor(ResourcesCompat.getColor(resources, R.color.search_color_text_image, null));
                break;
            case Search.Theme.LIGHT:
                mPaint.setColor(ResourcesCompat.getColor(resources, R.color.search_light_text_image, null));
                break;
            case Search.Theme.DARK:
                mPaint.setColor(ResourcesCompat.getColor(resources, R.color.search_dark_text_image, null));
                break;
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bounds = new Rect();
        mPaint.getTextBounds(mText, 0, mText.length(), bounds);
        int x = (canvas.getWidth() / 2) - (bounds.width() / 2);
        int y = (canvas.getHeight() / 2) - (bounds.height() / 2);
        canvas.drawText(mText, x, y, mPaint);
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
