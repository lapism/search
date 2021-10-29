package com.lapism.search.widget

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.AppBarLayout
import com.lapism.search.R
import com.lapism.search.databinding.MaterialSearchBarBinding


@Suppress("unused", "MemberVisibilityCanBePrivate")
class MaterialSearchBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : MaterialSearchLayout(context, attrs, defStyleAttr, defStyleRes) {

    // *********************************************************************************************
    private var binding: MaterialSearchBarBinding
    private var a: TypedArray? = null

    // *********************************************************************************************
    init {
        val inflater = LayoutInflater.from(getContext())
        binding = MaterialSearchBarBinding.inflate(inflater, this)

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

        val defaultRadius = context.resources.getDimensionPixelSize(R.dimen.search_dp_24)
        val customRadius = a?.getInt(R.styleable.MaterialSearchBar_search_radius, defaultRadius)
        setRadius(customRadius?.toFloat()!!)

        val defaultElevation = context.resources.getDimensionPixelSize(R.dimen.search_dp_0)
        val customElevation =
            a?.getInt(R.styleable.MaterialSearchBar_android_elevation, defaultElevation)
        elevation = customElevation?.toFloat()!!

        if (a?.hasValue(R.styleable.MaterialSearchBar_android_hint)!!) {
            val hint = a?.getString(R.styleable.MaterialSearchBar_android_hint)
            setHint(hint)
        }
    }

    // *********************************************************************************************
    override fun setNavigationIcon(@DrawableRes resId: Int) {
        binding.searchBarToolbar.setNavigationIcon(resId)
    }

    override fun setNavigationIcon(@Nullable drawable: Drawable?) {
        binding.searchBarToolbar.navigationIcon = drawable
    }

    override fun setNavigationContentDescription(@StringRes resId: Int) {
        binding.searchBarToolbar.setNavigationContentDescription(resId)
    }

    override fun setNavigationContentDescription(@Nullable description: CharSequence?) {
        binding.searchBarToolbar.navigationContentDescription = description
    }

    override fun setNavigationOnClickListener(listener: OnClickListener) {
        binding.searchBarToolbar.setNavigationOnClickListener(listener)
    }

    // *********************************************************************************************
    override fun setBackgroundColor(@ColorInt color: Int) {
        binding.searchBarCard.setCardBackgroundColor(color)
    }

    override fun setOnClickListener(@Nullable l: OnClickListener?) {
        binding.searchBarToolbar.setOnClickListener(l)
    }

    override fun setElevation(elevation: Float) {
        binding.searchBarCard.cardElevation = elevation
    }

    override fun getElevation(): Float {
        return binding.searchBarCard.elevation
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        val defaultMarginsStartEnd =
            context.resources.getDimensionPixelSize(R.dimen.search_dp_16)
        val defaultMarginsTopBottom =
            context.resources.getDimensionPixelSize(R.dimen.search_dp_8)

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

    // *********************************************************************************************
    fun getToolbar(): MaterialSearchToolbar {
        return binding.searchBarToolbar
    }

    fun setText(@Nullable text: CharSequence?) {
        binding.searchBarToolbar.setText(text)
    }

    fun setHint(@Nullable hint: CharSequence?) {
        binding.searchBarToolbar.setHint(hint)
    }

    fun setForegroundColor(@Nullable foregroundColor: ColorStateList?) {
        binding.searchBarCard.setCardForegroundColor(foregroundColor)
    }

    fun setStrokeWidth(@Dimension strokeWidth: Int) {
        binding.searchBarCard.strokeWidth = strokeWidth
    }

    fun setStrokeColor(strokeColor: ColorStateList) {
        binding.searchBarCard.setStrokeColor(strokeColor)
    }

    fun setRadius(radius: Float) {
        binding.searchBarCard.radius = radius
    }

    // *********************************************************************************************
    // TODO PUBLIC ? and requestLayout()
    private fun setMargins(left: Int, top: Int, right: Int, bottom: Int) {
        if (binding.searchBarCard.layoutParams is MarginLayoutParams) {
            val params = binding.searchBarCard.layoutParams as? MarginLayoutParams
            params?.setMargins(left, top, right, bottom)
            binding.searchBarCard.layoutParams = params
        }
    }

    // *********************************************************************************************
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
                dependency.stateListAnimator = null
                ViewCompat.setElevation(dependency, 0.0f)
            }
            return false
        }

    }

}