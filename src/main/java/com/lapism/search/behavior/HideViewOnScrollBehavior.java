package com.lapism.search.behavior;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.animation.AnimationUtils;


public class HideViewOnScrollBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    protected static final int ENTER_ANIMATION_DURATION = 225;
    protected static final int EXIT_ANIMATION_DURATION = 175;

    private static final int STATE_SCROLLED_DOWN = 1;
    private static final int STATE_SCROLLED_UP = 2;

    private int height = 0;
    private int currentState = STATE_SCROLLED_UP;
    private int additionalHiddenOffsetY = 0;
    @Nullable
    private ViewPropertyAnimator currentAnimator;

    public HideViewOnScrollBehavior() {
    }

    public HideViewOnScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(
            @NonNull CoordinatorLayout parent, @NonNull V child, int layoutDirection) {
        ViewGroup.MarginLayoutParams paramsCompat =
                (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        height = child.getMeasuredHeight() + paramsCompat.bottomMargin;
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    public void setAdditionalHiddenOffsetY(@NonNull V child, @Dimension int offset) {
        additionalHiddenOffsetY = offset;

        if (currentState == STATE_SCROLLED_DOWN) {
            child.setTranslationY(height + additionalHiddenOffsetY);
        }
    }

    @Override
    public boolean onStartNestedScroll(
            CoordinatorLayout coordinatorLayout,
            V child,
            View directTargetChild,
            View target,
            int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(
            CoordinatorLayout coordinatorLayout,
            @NonNull V child,
            View target,
            int dxConsumed,
            int dyConsumed,
            int dxUnconsumed,
            int dyUnconsumed) {
        if (dyConsumed > 0) {
            slideUp(child);
        } else if (dyConsumed < 0) {
            slideDown(child);
        }
    }

    public void slideUp(@NonNull V child) {
        if (currentState == STATE_SCROLLED_UP) {
            return;
        }

        if (currentAnimator != null) {
            currentAnimator.cancel();
            child.clearAnimation();
        }
        currentState = STATE_SCROLLED_UP;
        animateChildTo(
                child, 0, ENTER_ANIMATION_DURATION, AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
    }

    public void slideDown(@NonNull V child) {
        if (currentState == STATE_SCROLLED_DOWN) {
            return;
        }

        if (currentAnimator != null) {
            currentAnimator.cancel();
            child.clearAnimation();
        }
        currentState = STATE_SCROLLED_DOWN;
        animateChildTo(
                child,
                height + additionalHiddenOffsetY,
                EXIT_ANIMATION_DURATION,
                AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR);
    }

    private void animateChildTo(
            @NonNull V child, int targetY, long duration, TimeInterpolator interpolator) {
        currentAnimator =
                child
                        .animate()
                        .translationY(targetY)
                        .setInterpolator(interpolator)
                        .setDuration(duration)
                        .setListener(
                                new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        currentAnimator = null;
                                    }
                                });
    }
}
