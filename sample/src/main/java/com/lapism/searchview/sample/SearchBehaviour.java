package com.lapism.searchview.sample;

/*
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.lapism.searchview.SearchView;

@SuppressWarnings("unused")
public class SearchBehaviour extends CoordinatorLayout.Behavior<SearchView> {

    private AppBarLayout appBarLayout;
    private AppBarLayout.Behavior appBarBehavior;
    private ValueAnimator valueAnimator;
    private SearchView searchView;
    private boolean isScrolling;

    @SuppressWarnings("unused")
    public SearchBehaviour(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, SearchView child, View dependency) {
        if (dependency instanceof AppBarLayout) {
            this.searchView = child;
            this.appBarLayout = (AppBarLayout) dependency;
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                    appBarLayout.getLayoutParams();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                appBarLayout.setStateListAnimator(null);
            }
            this.appBarBehavior = (AppBarLayout.Behavior) params.getBehavior();
            return true;
        }
        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout parent, SearchView child, View target, int dx, int dy, int[] consumed) {
        if (dy >= 0 || dy > -10 || this.isScrolling) {
            return;
        }
        this.isScrolling = true;
        if (needsToAdjustSearchBar() && !isRunningAnimation()) {
            int offset = getMinExpandHeight();
            this.getValueAnimator(parent, child, -offset).start();
        }
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, SearchView child, View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, SearchView child, View target) {
        this.isScrolling = false;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, SearchView child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, SearchView child, View dependency) {
        if (needsToAdjustSearchBar()) {
            float offset = getMinExpandHeight() + appBarBehavior.getTopAndBottomOffset();
            child.setY(offset);
            return true;
        }
        return super.onDependentViewChanged(parent, child, dependency);
    }

    private int getStatusBarHeight() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            return 0;
        }
        int result = 0;
        int resourceId = searchView.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = searchView.getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private ValueAnimator getValueAnimator(CoordinatorLayout parent, SearchView searchView, int offset) {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofInt();
        } else if (valueAnimator.isRunning()) {
            return valueAnimator;
        }
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        // valueAnimator.addUpdateListener(animation -> HeaderBehaviorHelper.setHeaderTopBottomOffset(parent, appBarLayout, (int) animation.getAnimatedValue()));
        valueAnimator.setIntValues(appBarBehavior.getTopAndBottomOffset(), offset);
        return valueAnimator;
    }

    private boolean isRunningAnimation() {
        return valueAnimator != null && valueAnimator.isRunning();
    }

    private boolean needsToAdjustSearchBar() {
        float y = Math.abs(appBarBehavior.getTopAndBottomOffset());
        return y > getMinExpandHeight();
    }

    private int getMinExpandHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return appBarLayout.getTotalScrollRange() - searchView.getMinimumHeight()
                    - (getStatusBarHeight() / 2);
        } else
            return 0;
    }

}*/