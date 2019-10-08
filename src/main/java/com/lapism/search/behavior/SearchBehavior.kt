package com.lapism.search.behavior

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.AppBarLayout
import com.lapism.search.internal.SearchLayout


class SearchBehavior<S : SearchLayout> : CoordinatorLayout.Behavior<S>() {

    // *********************************************************************************************
    var scroll: Boolean = false

    // *********************************************************************************************
    init {
        scroll = true
    }

    // *********************************************************************************************
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: S,
        dependency: View
    ): Boolean {
        if (dependency is AppBarLayout && scroll) {
            ViewCompat.setZ(child, ViewCompat.getZ(dependency) + 1)
            return true
        }
        return super.layoutDependsOn(parent, child, dependency)
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: S,
        dependency: View
    ): Boolean {
        if (dependency is AppBarLayout && scroll) {
            child.translationY = dependency.getY()
            return true
        }
        return super.onDependentViewChanged(parent, child, dependency)
    }

}
