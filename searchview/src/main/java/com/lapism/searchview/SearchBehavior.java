package com.lapism.searchview;

import android.content.Context;
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

            /*mSearchView = child;
            mAppBarLayout = (AppBarLayout) dependency;
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
            mAppBarLayoutBehavior = (AppBarLayout.Behavior) params.getBehavior();*/

            return true;
        }
        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, SearchView child, View dependency) {
        if (dependency instanceof AppBarLayout) {

            /*child.setTranslationY(dependency.getY());
            // ViewCompat.setElevation(mSearchView, ViewCompat.getElevation(dependency));
            ViewCompat.setElevation(dependency, ViewCompat.getElevation(dependency));

            mSearchView.setTranslationY(dependency.getY());
            ViewCompat.setElevation(mSearchView, ViewCompat.getElevation(dependency));*/

            return true;
        }
        return super.onDependentViewChanged(parent, child, dependency);
    }

}