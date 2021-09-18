package com.lapism.search.widget

import androidx.annotation.IntDef


@IntDef(
    NavigationIconCompat.NONE,
    NavigationIconCompat.ARROW,
    NavigationIconCompat.SEARCH
)
@Retention(AnnotationRetention.SOURCE)
annotation class NavigationIconCompat {
    companion object {
        const val NONE = 0
        const val ARROW = 1
        const val SEARCH = 2
    }
}

// TODO ANOTACE, NOVE IKONKY, PORADEK V KNIHOVNE, NEDRZI SI FOCUS