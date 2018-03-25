package com.lapism.searchview.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.util.TypedValue;

import com.lapism.searchview.R;
import com.lapism.searchview.Search;


public class SearchTextImageDrawable extends Drawable {

    private final String mText;
    private final Paint mPaint;

    public SearchTextImageDrawable(Context context, String text) {
        mText = text;

        float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10f, context.getResources().getDisplayMetrics());

        Typeface typeface;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            typeface = context.getResources().getFont(R.font.roboto_medium);
        } else {
            typeface = ResourcesCompat.getFont(context, R.font.roboto_regular);
        }
        // todo compat + fix
        mPaint = new Paint();
        mPaint.setTextSize(size);
        mPaint.setAntiAlias(true);
        mPaint.setTypeface(typeface);
        mPaint.setFakeBoldText(false);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);

        setTheme(Search.Theme.PLAY, context.getResources());
    }

    public void setTheme(@Search.Theme int theme, Resources resources) {
        switch (theme) {
            case Search.Theme.PLAY:
                mPaint.setColor(ResourcesCompat.getColor(resources, R.color.search_play_text_image, null)); // theme
                break;
            case Search.Theme.GOOGLE:
                this.mPaint.setColor(ResourcesCompat.getColor(resources, R.color.search_google_text_image, null));
                break;
            case Search.Theme.LIGHT:
                this.mPaint.setColor(ResourcesCompat.getColor(resources, R.color.search_light_text_image, null));
                break;
            case Search.Theme.DARK:
                this.mPaint.setColor(ResourcesCompat.getColor(resources, R.color.search_dark_text_image, null));
                break;
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawText(mText, 0, 0, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

}
