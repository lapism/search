package com.lapism.searchview.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;


public class SearchBehavior extends CoordinatorLayout.Behavior<SearchView> {

    public SearchBehavior() {
    }

    public SearchBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, SearchView child, View dependency) {
        if (dependency instanceof AppBarLayout) {
            ViewCompat.setElevation(child, ViewCompat.getElevation(dependency));
            ViewCompat.setZ(child, ViewCompat.getZ(dependency) + 1); // todo no click background
            return true;
        }
        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, @NonNull SearchView child, View dependency) {
        if (dependency instanceof AppBarLayout) {
            child.setTranslationY(dependency.getY());
            return true;
        }
        return super.onDependentViewChanged(parent, child, dependency);
    }

}
