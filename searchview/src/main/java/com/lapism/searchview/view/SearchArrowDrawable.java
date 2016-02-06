package com.lapism.searchview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;

import com.lapism.searchview.R;
//import android.support.v7.graphics.drawable.SearchArrowDrawable;

class SearchArrowDrawable extends Drawable {

    private static final float ARROW_HEAD_ANGLE = (float) Math.toRadians(45.0);//d,D
    private final Paint mPaint = new Paint();
    private final Path mPath = new Path();
    private final float mBarGap;
    private final int mSize;
    private final float mBarLength;
    private final float mArrowShaftLength;
    private final float mArrowHeadLength;
    private final float mMaxCutForBarSize;
    private float mProgress;
    private boolean mSpin;
    private boolean mVerticalMirror = false;

    public SearchArrowDrawable(Context context) {
        float mBarThickness = context.getResources().getDimension(R.dimen.search_arrow_thickness);
        mMaxCutForBarSize = (float) (mBarThickness / 2 * Math.cos(ARROW_HEAD_ANGLE));

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.MITER);
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        mPaint.setAntiAlias(true);
        mPaint.setColor(ContextCompat.getColor(context, R.color.search_arrow_color));
        mPaint.setStrokeWidth(mBarThickness);

        setSpinEnabled(true);

        mBarGap = Math.round(context.getResources().getDimension(R.dimen.search_arrow_gapBetweenBars));
        mSize = context.getResources().getDimensionPixelSize(R.dimen.search_arrow_drawableSize);
        mBarLength = Math.round(context.getResources().getDimension(R.dimen.search_arrow_barLength));

        mArrowHeadLength = Math.round(context.getResources().getDimension(R.dimen.search_arrow_arrowHeadLength));
        mArrowShaftLength = context.getResources().getDimension(R.dimen.search_arrow_arrowShaftLength);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        final boolean flipToPointRight = DrawableCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL;
        float arrowHeadBarLength = (float) Math.sqrt(mArrowHeadLength * mArrowHeadLength * 2);
        arrowHeadBarLength = match(mBarLength, arrowHeadBarLength, mProgress);
        final float arrowShaftLength = match(mBarLength, mArrowShaftLength, mProgress);
        final float arrowShaftCut = Math.round(match(0, mMaxCutForBarSize, mProgress));
        final float rotation = match(0, ARROW_HEAD_ANGLE, mProgress);
        final float canvasRotate = match(flipToPointRight ? 0 : -180, flipToPointRight ? 180 : 0, mProgress);
        final float arrowWidth = Math.round(arrowHeadBarLength * Math.cos(rotation));
        final float arrowHeight = Math.round(arrowHeadBarLength * Math.sin(rotation));
        mPath.rewind();
        final float topBottomBarOffset = match(mBarGap + mPaint.getStrokeWidth(), -mMaxCutForBarSize, mProgress);
        final float arrowEdge = -arrowShaftLength / 2;
        mPath.moveTo(arrowEdge + arrowShaftCut, 0);
        mPath.rLineTo(arrowShaftLength - arrowShaftCut * 2, 0);
        mPath.moveTo(arrowEdge, topBottomBarOffset);
        mPath.rLineTo(arrowWidth, arrowHeight);
        mPath.moveTo(arrowEdge, -topBottomBarOffset);
        mPath.rLineTo(arrowWidth, -arrowHeight);
        mPath.close();
        canvas.save();
        final float barThickness = mPaint.getStrokeWidth();
        final int remainingSpace = (int) (bounds.height() - barThickness * 3 - mBarGap * 2);
        float yOffset = (remainingSpace / 4) * 2;
        yOffset += barThickness * 1.5 + mBarGap;
        canvas.translate(bounds.centerX(), yOffset);
        if (mSpin) {
            canvas.rotate(canvasRotate * ((mVerticalMirror ^ flipToPointRight) ? -1 : 1));
        } else if (flipToPointRight) {
            canvas.rotate(180);
        }
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {
        if (alpha != mPaint.getAlpha()) {
            mPaint.setAlpha(alpha);
            invalidateSelf();
        }
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return mSize;
    }

    @Override
    public int getIntrinsicHeight() {
        return mSize;
    }

    public void setVerticalMirror(boolean verticalMirror) {
        if (mVerticalMirror != verticalMirror) {
            mVerticalMirror = verticalMirror;
            invalidateSelf();
        }
    }

    public void setProgress(@FloatRange(from = 0.0, to = 1.0) float progress) {
        if (mProgress != progress) {
            mProgress = progress;
            invalidateSelf();
        }
    }

    public void setColor(@ColorInt int color) {
        if (color != mPaint.getColor()) {
            mPaint.setColor(color);
            invalidateSelf();
        }
    }

    private float match(float a, float b, float t) {
        return a + (b - a) * t;
    }

    private void setSpinEnabled(boolean enabled) {
        if (mSpin != enabled) {
            mSpin = enabled;
            invalidateSelf();
        }
    }

}