package com.lapism.search.behavior

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.AppBarLayout


class SearchBehavior<V : View> : CoordinatorLayout.Behavior<V>() {

    // *********************************************************************************************
    var scroll: Boolean = false

    // *********************************************************************************************
    init {
        scroll = true
    }

    // *********************************************************************************************
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: V,
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
        child: V,
        dependency: View
    ): Boolean {
        if (dependency is AppBarLayout && scroll) {
            child.translationY = dependency.getY()
            return true
        }
        return super.onDependentViewChanged(parent, child, dependency)
    }

}
