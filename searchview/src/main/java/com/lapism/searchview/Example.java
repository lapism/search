package com.lapism.searchview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import static android.view.View.VISIBLE;

// static
public class Example extends CoordinatorLayout.Behavior<FloatingActionButton> {

    private static final boolean AUTO_HIDE_DEFAULT = true;

    private Rect mTmpRect;
    private FloatingActionButton.OnVisibilityChangedListener mInternalAutoHideListener;
    private boolean mAutoHideEnabled;

    public Example() {
        super();
        mAutoHideEnabled = AUTO_HIDE_DEFAULT;
    }

    public Example(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, android.support.design.R.styleable.FloatingActionButton_Behavior_Layout);
        mAutoHideEnabled = a.getBoolean(android.support.design.R.styleable.FloatingActionButton_Behavior_Layout_behavior_autoHide, AUTO_HIDE_DEFAULT);
        a.recycle();
    }

    private static boolean isBottomSheet(@NonNull View view) {
        final ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp instanceof CoordinatorLayout.LayoutParams) {
            return ((CoordinatorLayout.LayoutParams) lp).getBehavior() instanceof BottomSheetBehavior;
        }
        return false;
    }

    public boolean isAutoHideEnabled() {
        return mAutoHideEnabled;
    }

    public void setAutoHideEnabled(boolean autoHide) {
        mAutoHideEnabled = autoHide;
    }

    @Override
    public void onAttachedToLayoutParams(@NonNull CoordinatorLayout.LayoutParams lp) {
        if (lp.dodgeInsetEdges == Gravity.NO_GRAVITY) {
            lp.dodgeInsetEdges = Gravity.BOTTOM;
        }
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        if (dependency instanceof AppBarLayout) {
            updateFabVisibilityForAppBarLayout(parent, (AppBarLayout) dependency, child);
        } else if (isBottomSheet(dependency)) {
            updateFabVisibilityForBottomSheet(dependency, child);
        }
        return false;
    }

    @VisibleForTesting
    void setInternalAutoHideListener(FloatingActionButton.OnVisibilityChangedListener listener) {
        mInternalAutoHideListener = listener;
    }

    private boolean shouldUpdateVisibility(View dependency, FloatingActionButton child) {
        final CoordinatorLayout.LayoutParams lp =
                (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (!mAutoHideEnabled) {
            return false;
        }

        if (lp.getAnchorId() != dependency.getId()) {
            return false;
        }

        return true;
    }

    private boolean updateFabVisibilityForAppBarLayout(CoordinatorLayout parent,
                                                       AppBarLayout appBarLayout, FloatingActionButton child) {
        if (!shouldUpdateVisibility(appBarLayout, child)) {
            return false;
        }
        if (mTmpRect == null) {
            mTmpRect = new Rect();
        }
        final Rect rect = mTmpRect;
        if (rect.bottom <= 0) {
            //
        } else {
            //
        }
        return true;
    }

    private boolean updateFabVisibilityForBottomSheet(View bottomSheet,
                                                      FloatingActionButton child) {
        if (!shouldUpdateVisibility(bottomSheet, child)) {
            return false;
        }
        CoordinatorLayout.LayoutParams lp =
                (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (bottomSheet.getTop() < child.getHeight() / 2 + lp.topMargin) {
            //
        } else {
            //
        }
        return true;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, FloatingActionButton child, int layoutDirection) {
        final List<View> dependencies = parent.getDependencies(child);
        for (int i = 0, count = dependencies.size(); i < count; i++) {
            final View dependency = dependencies.get(i);
            if (dependency instanceof AppBarLayout) {
                if (updateFabVisibilityForAppBarLayout(
                        parent, (AppBarLayout) dependency, child)) {
                    break;
                }
            } else if (isBottomSheet(dependency)) {
                if (updateFabVisibilityForBottomSheet(dependency, child)) {
                    break;
                }
            }
        }
        parent.onLayoutChild(child, layoutDirection);
        offsetIfNeeded(parent, child);
        return true;
    }

    @Override
    public boolean getInsetDodgeRect(@NonNull CoordinatorLayout parent, @NonNull FloatingActionButton child, @NonNull Rect rect) {
        final Rect shadowPadding = new Rect();
        rect.set(child.getLeft() + shadowPadding.left,
                child.getTop() + shadowPadding.top,
                child.getRight() - shadowPadding.right,
                child.getBottom() - shadowPadding.bottom);
        return true;
    }

    private void offsetIfNeeded(CoordinatorLayout parent, FloatingActionButton fab) {
        final Rect padding = new Rect();

        if (padding != null && padding.centerX() > 0 && padding.centerY() > 0) {
            final CoordinatorLayout.LayoutParams lp =
                    (CoordinatorLayout.LayoutParams) fab.getLayoutParams();

            int offsetTB = 0, offsetLR = 0;

            if (fab.getRight() >= parent.getWidth() - lp.rightMargin) {
                // If we're on the right edge, shift it the right
                offsetLR = padding.right;
            } else if (fab.getLeft() <= lp.leftMargin) {
                // If we're on the left edge, shift it the left
                offsetLR = -padding.left;
            }
            if (fab.getBottom() >= parent.getHeight() - lp.bottomMargin) {
                // If we're on the bottom edge, shift it down
                offsetTB = padding.bottom;
            } else if (fab.getTop() <= lp.topMargin) {
                // If we're on the top edge, shift it up
                offsetTB = -padding.top;
            }

            if (offsetTB != 0) {
                ViewCompat.offsetTopAndBottom(fab, offsetTB);
            }
            if (offsetLR != 0) {
                ViewCompat.offsetLeftAndRight(fab, offsetLR);
            }
        }
    }

}

