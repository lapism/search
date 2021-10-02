package com.lapism.search.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Nullable
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.card.MaterialCardView
import com.lapism.search.R


class MaterialSearchBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : MaterialSearchLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var card: MaterialCardView? = null
    private var toolbar: MaterialSearchToolbar? = null
    private var a: TypedArray? = null

    init {
        View.inflate(context, R.layout.material_search_bar, this)

        card = findViewById(R.id.search_bar_card)
        toolbar = findViewById(R.id.search_bar_toolbar)

        a = context.obtainStyledAttributes(
            attrs, R.styleable.MaterialSearchBar, defStyleAttr, defStyleRes
        )

        when {
            a?.hasValue(R.styleable.MaterialSearchBar_search_navigationIconCompat)!! -> {
                navigationIconCompat = a?.getInt(
                    R.styleable.MaterialSearchBar_search_navigationIconCompat,
                    NavigationIconCompat.NONE
                )!!
            }
            a?.hasValue(R.styleable.MaterialSearchBar_search_navigationIcon)!! -> {
                setNavigationIcon(a?.getDrawable(R.styleable.MaterialSearchBar_search_navigationIcon))
            }
        }

        if (a?.hasValue(R.styleable.MaterialSearchBar_search_navigationContentDescription)!!) {
            val description =
                a?.getText(R.styleable.MaterialSearchBar_search_navigationContentDescription)
            setNavigationContentDescription(description)
        }

        if (a?.hasValue(R.styleable.MaterialSearchBar_search_backgroundColor)!!) {
            val color = a?.getInt(R.styleable.MaterialSearchBar_search_backgroundColor, 0)
            setBackgroundColor(color!!)
        }

        val defaultRadius = context.resources.getDimensionPixelSize(R.dimen.search_bar_radius)
        val customRadius = a?.getInt(R.styleable.MaterialSearchBar_search_radius, defaultRadius)
        setRadius(customRadius?.toFloat()!!)

        val defaultElevation = context.resources.getDimensionPixelSize(R.dimen.search_bar_elevation)
        val customElevation =
            a?.getInt(R.styleable.MaterialSearchBar_android_elevation, defaultElevation)
        elevation = customElevation?.toFloat()!!

        if (a?.hasValue(R.styleable.MaterialSearchBar_android_hint)!!) {
            val hint = a?.getString(R.styleable.MaterialSearchBar_android_hint)
            setHint(hint)
        }
    }

    fun getToolbar(): MaterialSearchToolbar? {
        return toolbar
    }

    fun setText(text: CharSequence?) {
        toolbar?.setText(text)
    }

    fun setHint(hint: CharSequence?) {
        toolbar?.setHint(hint)
    }

    override fun setNavigationOnClickListener(listener: OnClickListener) {
        toolbar?.setNavigationOnClickListener(listener)
    }

    override fun setNavigationIcon(@Nullable drawable: Drawable?) {
        toolbar?.navigationIcon = drawable
    }

    override fun setNavigationIcon(resId: Int) {
        toolbar?.setNavigationIcon(resId)
    }

    override fun setNavigationContentDescription(resId: Int) {
        toolbar?.setNavigationContentDescription(resId)
    }

    override fun setNavigationContentDescription(description: CharSequence?) {
        toolbar?.navigationContentDescription = description
    }

    override fun setOnClickListener(@Nullable l: OnClickListener?) {
        toolbar?.setOnClickListener(l)
    }

    override fun setElevation(elevation: Float) {
        card?.cardElevation = elevation
    }

    override fun getElevation(): Float {
        return card?.elevation!!
    }

/*
    fun setForegroundColor(){

    }

    fun setBackgroundColor(){

    }

    fun setStrokeWidth(){

    }

    fun setStrokeColor{
        card?.setStrokeColor()
    }
*/

    override fun setBackgroundColor(@ColorInt color: Int) {
        card?.setCardBackgroundColor(color)
    }

    private fun setMargins(left: Int, top: Int, right: Int, bottom: Int) {
        if (card?.layoutParams is MarginLayoutParams) {
            val params = card?.layoutParams as? MarginLayoutParams
            params?.setMargins(left, top, right, bottom)
            card?.layoutParams = params
        }
    }

    private fun setRadius(radius: Float) {
        card?.radius = radius
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        val defaultMarginsStartEnd =
            context.resources.getDimensionPixelSize(R.dimen.search_bar_margins_start_end)
        val defaultMarginsTopBottom =
            context.resources.getDimensionPixelSize(R.dimen.search_bar_margins_top_bottom)

        val customMarginsStart = a?.getDimensionPixelSize(
            R.styleable.MaterialSearchBar_android_layout_marginStart,
            defaultMarginsStartEnd
        )!!
        val customMarginsEnd = a?.getDimensionPixelSize(
            R.styleable.MaterialSearchBar_android_layout_marginEnd,
            defaultMarginsStartEnd
        )!!
        val customMarginsTop = a?.getDimensionPixelSize(
            R.styleable.MaterialSearchBar_android_layout_marginTop,
            defaultMarginsTopBottom
        )!!
        val customMarginsBottom = a?.getDimensionPixelSize(
            R.styleable.MaterialSearchBar_android_layout_marginBottom,
            defaultMarginsTopBottom
        )!!

        setMargins(customMarginsStart, customMarginsTop, customMarginsEnd, customMarginsBottom)

        a?.recycle()
    }

    @Suppress("unused")
    class ScrollingViewBehavior : AppBarLayout.ScrollingViewBehavior {

        constructor()

        constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

        override fun onDependentViewChanged(
            parent: CoordinatorLayout,
            child: View,
            dependency: View
        ): Boolean {
            super.onDependentViewChanged(parent, child, dependency)
            if (dependency is AppBarLayout) {
                dependency.setBackgroundColor(Color.TRANSPARENT)
                // dependency.elevation = 0.0f
                // dependency.stateListAnimator = null
                ViewCompat.setElevation(dependency, 0.0f)
            }
            return false
        }

    }

}
