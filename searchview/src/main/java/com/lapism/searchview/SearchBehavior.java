package com.lapism.searchview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;


public class SearchBehavior extends CoordinatorLayout.Behavior<SearchView> {

    public SearchBehavior() {
        super();
    }

    public SearchBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, SearchView child, View dependency) {
        if (dependency instanceof AppBarLayout) {
            //AppBarLayout mAppBarLayout = (AppBarLayout) dependency;
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dependency.setElevation(0);
            }*/
            return true;
        }
        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, SearchView child, View dependency) {
        if (dependency instanceof AppBarLayout) {
            child.setTranslationY(dependency.getY());
            // ViewCompat.setElevation(mSearchView, ViewCompat.getElevation(dependency));
            ViewCompat.setElevation(dependency, ViewCompat.getElevation(dependency));
            return true;
        }
        return super.onDependentViewChanged(parent, child, dependency);
    }

}

/*
    Toolbar mToolBar = (Toolbar) findViewById(R.id.imagePreviewToolbar);
    setSupportActionBar(mToolBar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    getSupportActionBar().setHomeButtonEnabled(true);

                    // CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
                /*StateListAnimator stateListAnimator = new StateListAnimator();
                stateListAnimator.addState(new int[1], new Animator() {
                    @Override
                    public long getStartDelay() {
                        return 1;
                    }

                    @Override
                    public void setStartDelay(long startDelay) {

                    }

                    @Override
                    public Animator setDuration(long duration) {
                        return null;
                    }

                    @Override
                    public long getDuration() {
                        return 1;
                    }

                    @Override
                    public void setInterpolator(TimeInterpolator value) {

                    }

                    @Override
                    public boolean isRunning() {
                        return false;
                    }
                });
                mAppBarLayout.setStateListAnimator(null);*/
// AppBarLayout.Behavior
                /*StateListAnimator stateListAnimator = new StateListAnimator();
                stateListAnimator.addState(new int[0], ObjectAnimator.ofFloatmAppBarLayout, "elevation", 0.1f));
                mAppBarLayout.setStateListAnimator(stateListAnimator);*/