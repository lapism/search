package com.lapism.searchview;

import android.animation.ObjectAnimator;
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
import android.util.Property;
import android.view.animation.AccelerateDecelerateInterpolator;


public class SearchArrowDrawable extends Drawable {

    public static final float STATE_ARROW = 0.0f;
    public static final float STATE_HAMBURGER = 1.0f;

    private static final float ARROW_HEAD_ANGLE = (float) Math.toRadians(45.0);
    private static final Property<SearchArrowDrawable, Float> PROGRESS = new Property<SearchArrowDrawable, Float>(Float.class, "progress") {
        @Override
        public void set(SearchArrowDrawable object, Float value) {
            object.setProgress(value);
        }

        @Override
        public Float get(SearchArrowDrawable object) {
            return object.getProgress();
        }
    };
    private final Paint mPaint = new Paint();
    private final Path mPath = new Path();
    private final float mBarGap;
    private final int mSize;
    private final float mBarLength;
    private final float mArrowShaftLength;
    private final float mArrowHeadLength;
    private final float mMaxCutForBarSize;
    private final boolean mSpin;
    private float mProgress;
    private boolean mVerticalMirror = false;

    public SearchArrowDrawable(Context context) {
        float mBarThickness = context.getResources().getDimension(R.dimen.arrow_thickness);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.MITER);
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mBarThickness);
        mPaint.setColor(ContextCompat.getColor(context, android.R.color.black));

        mMaxCutForBarSize = (float) (mBarThickness / 2 * Math.cos(ARROW_HEAD_ANGLE));
        mSpin = true;
        mBarGap = Math.round(context.getResources().getDimension(R.dimen.arrow_gapBetweenBars));
        mSize = context.getResources().getDimensionPixelSize(R.dimen.arrow_drawableSize);
        mBarLength = Math.round(context.getResources().getDimension(R.dimen.arrow_barLength));
        mArrowHeadLength = Math.round(context.getResources().getDimension(R.dimen.arrow_arrowHeadLength));
        mArrowShaftLength = context.getResources().getDimension(R.dimen.arrow_arrowShaftLength);
    }

    private static float match(float a, float b, float t) {
        return a + (b - a) * t;
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

    @FloatRange(from = STATE_ARROW, to = STATE_HAMBURGER)
    private float getProgress() {
        return mProgress;
    }

    public void setProgress(@FloatRange(from = STATE_ARROW, to = STATE_HAMBURGER) float progress) {
        if (mProgress != progress) {
            mProgress = progress;
            invalidateSelf();
        }
    }

    public void setVerticalMirror(boolean verticalMirror) {
        if (mVerticalMirror != verticalMirror) {
            mVerticalMirror = verticalMirror;
            invalidateSelf();
        }
    }

    @Override
    public void setAlpha(int alpha) {
        if (alpha != mPaint.getAlpha()) {
            mPaint.setAlpha(alpha);
            invalidateSelf();
        }
    }

    public void setColor(@ColorInt int color) {
        if (color != mPaint.getColor()) {
            mPaint.setColor(color);
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

    public void animate(float state, int duration) {
        ObjectAnimator anim;
        if (state == STATE_ARROW) {
            anim = ObjectAnimator.ofFloat(this, PROGRESS, state, STATE_HAMBURGER);
        } else {
            anim = ObjectAnimator.ofFloat(this, PROGRESS, state, STATE_ARROW);
        }
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(duration);
        anim.start();
    }

}