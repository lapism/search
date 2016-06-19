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
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.util.Property;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// from AppCompat
class SearchArrowDrawable extends Drawable {

    static final float STATE_ARROW = 0.0f;
    static final float STATE_HAMBURGER = 1.0f;
    private static final int ARROW_DIRECTION_LEFT = 0;
    private static final int ARROW_DIRECTION_RIGHT = 1;
    private static final int ARROW_DIRECTION_START = 2;
    private static final int ARROW_DIRECTION_END = 3;
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
    private static final float ARROW_HEAD_ANGLE = (float) Math.toRadians(45);
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path mPath = new Path();
    private final int mSize;
    private float mArrowHeadLength;
    private float mBarLength;
    private float mArrowShaftLength;
    private float mBarGap;
    private boolean mSpin;
    private boolean mVerticalMirror = false;
    private float mProgress;
    private float mMaxCutForBarSize;
    private int mDirection = ARROW_DIRECTION_START;

    SearchArrowDrawable(Context context) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.MITER);
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        mPaint.setAntiAlias(true);
        setColor(ContextCompat.getColor(context, android.R.color.black));
        setBarThickness(context.getResources().getDimension(com.lapism.searchview.R.dimen.arrow_thickness));
        setSpinEnabled(true);
        setGapSize(Math.round(context.getResources().getDimension(com.lapism.searchview.R.dimen.arrow_gapBetweenBars)));
        mSize = context.getResources().getDimensionPixelSize(com.lapism.searchview.R.dimen.arrow_drawableSize);
        mBarLength = Math.round(context.getResources().getDimension(com.lapism.searchview.R.dimen.arrow_barLength));
        mArrowHeadLength = Math.round(context.getResources().getDimension(com.lapism.searchview.R.dimen.arrow_arrowHeadLength));
        mArrowShaftLength = context.getResources().getDimension(com.lapism.searchview.R.dimen.arrow_arrowShaftLength);
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    public float getArrowHeadLength() {
        return mArrowHeadLength;
    }

    public void setArrowHeadLength(float length) {
        if (mArrowHeadLength != length) {
            mArrowHeadLength = length;
            invalidateSelf();
        }
    }

    public float getArrowShaftLength() {
        return mArrowShaftLength;
    }

    public void setArrowShaftLength(float length) {
        if (mArrowShaftLength != length) {
            mArrowShaftLength = length;
            invalidateSelf();
        }
    }

    public float getBarLength() {
        return mBarLength;
    }

    public void setBarLength(float length) {
        if (mBarLength != length) {
            mBarLength = length;
            invalidateSelf();
        }
    }

    @ColorInt
    public int getColor() {
        return mPaint.getColor();
    }

    private void setColor(@ColorInt int color) {
        if (color != mPaint.getColor()) {
            mPaint.setColor(color);
            invalidateSelf();
        }
    }

    public float getBarThickness() {
        return mPaint.getStrokeWidth();
    }

    private void setBarThickness(float width) {
        if (mPaint.getStrokeWidth() != width) {
            mPaint.setStrokeWidth(width);
            mMaxCutForBarSize = (float) (width / 2 * Math.cos(ARROW_HEAD_ANGLE));
            invalidateSelf();
        }
    }

    public float getGapSize() {
        return mBarGap;
    }

    private void setGapSize(float gap) {
        if (gap != mBarGap) {
            mBarGap = gap;
            invalidateSelf();
        }
    }

    public boolean isSpinEnabled() {
        return mSpin;
    }

    @SuppressWarnings("SameParameterValue")
    private void setSpinEnabled(boolean enabled) {
        if (mSpin != enabled) {
            mSpin = enabled;
            invalidateSelf();
        }
    }

    @ArrowDirection
    public int getDirection() {
        return mDirection;
    }

    void setDirection(@ArrowDirection int direction) {
        if (direction != mDirection) {
            mDirection = direction;
            invalidateSelf();
        }
    }

    void setVerticalMirror(boolean verticalMirror) {
        if (mVerticalMirror != verticalMirror) {
            mVerticalMirror = verticalMirror;
            invalidateSelf();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();

        final boolean flipToPointRight;
        switch (mDirection) {
            case ARROW_DIRECTION_LEFT:
                flipToPointRight = false;
                break;
            case ARROW_DIRECTION_RIGHT:
                flipToPointRight = true;
                break;
            case ARROW_DIRECTION_END:
                flipToPointRight = DrawableCompat.getLayoutDirection(this)
                        == ViewCompat.LAYOUT_DIRECTION_LTR;
                break;
            case ARROW_DIRECTION_START:
            default:
                flipToPointRight = DrawableCompat.getLayoutDirection(this)
                        == ViewCompat.LAYOUT_DIRECTION_RTL;
                break;
        }

        float arrowHeadBarLength = (float) Math.sqrt(mArrowHeadLength * mArrowHeadLength * 2);
        arrowHeadBarLength = lerp(mBarLength, arrowHeadBarLength, mProgress);
        final float arrowShaftLength = lerp(mBarLength, mArrowShaftLength, mProgress);
        final float arrowShaftCut = Math.round(lerp(0, mMaxCutForBarSize, mProgress));
        final float rotation = lerp(0, ARROW_HEAD_ANGLE, mProgress);
        final float canvasRotate = lerp(flipToPointRight ? 0 : -180, flipToPointRight ? 180 : 0, mProgress);
        final float arrowWidth = Math.round(arrowHeadBarLength * Math.cos(rotation));
        final float arrowHeight = Math.round(arrowHeadBarLength * Math.sin(rotation));
        mPath.rewind();
        final float topBottomBarOffset = lerp(mBarGap + mPaint.getStrokeWidth(), -mMaxCutForBarSize, mProgress);
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
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override
    public int getIntrinsicHeight() {
        return mSize;
    }

    @Override
    public int getIntrinsicWidth() {
        return mSize;
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @FloatRange(from = STATE_ARROW, to = STATE_HAMBURGER)
    private float getProgress() {
        return mProgress;
    }

    void setProgress(@FloatRange(from = STATE_ARROW, to = STATE_HAMBURGER) float progress) {
        if (mProgress != progress) {
            mProgress = progress;
            invalidateSelf();
        }
    }

    public final Paint getPaint() {
        return mPaint;
    }

    void animate(float state, int duration) {
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

    @IntDef({ARROW_DIRECTION_LEFT, ARROW_DIRECTION_RIGHT,
            ARROW_DIRECTION_START, ARROW_DIRECTION_END})
    @Retention(RetentionPolicy.SOURCE)
    @interface ArrowDirection {
    }

}