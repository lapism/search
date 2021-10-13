package com.lapism.search.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.lapism.search.R


abstract class MaterialSearchLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    @NavigationIconCompat
    @get:NavigationIconCompat
    var navigationIconCompat: Int = 0
        set(@NavigationIconCompat navigationIconCompat) {
            field = navigationIconCompat

            when (navigationIconCompat) {
                NavigationIconCompat.NONE -> {
                    setNavigationIcon(0)
                }
                NavigationIconCompat.ARROW -> {
                    setNavigationIcon(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.search_ic_outline_arrow_back_24
                        )
                    )
                }
                NavigationIconCompat.SEARCH -> {
                    setNavigationIcon(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.search_ic_outline_search_24
                        )
                    )
                }
            }
        }

    abstract fun setNavigationIcon(@DrawableRes resId: Int)

    abstract fun setNavigationIcon(@Nullable drawable: Drawable?)

    abstract fun setNavigationContentDescription(@StringRes resId: Int)

    abstract fun setNavigationContentDescription(@Nullable description: CharSequence?)

    abstract fun setNavigationOnClickListener(listener: OnClickListener)

}