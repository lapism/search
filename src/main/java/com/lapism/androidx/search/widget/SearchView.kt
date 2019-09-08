package com.lapism.androidx.search.widget

import android.content.Context
import android.util.AttributeSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.lapism.androidx.search.R
import com.lapism.androidx.search.SearchUtils
import com.lapism.androidx.search.behavior.SearchBehavior
import com.lapism.androidx.search.internal.SearchLayout


class SearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0, // TODO
    defStyleRes: Int = 0   // TODO
) : SearchLayout(context, attrs, defStyleAttr, defStyleRes), CoordinatorLayout.AttachedBehavior {

    // *********************************************************************************************
    private var mBehavior: CoordinatorLayout.Behavior<*> = SearchBehavior()

    // *********************************************************************************************
    init {
        inflate(context, R.layout.search_view, this)
        init()
        mCardView = findViewById<MaterialCardView>(R.id.search_materialCardView)

        val typedArray = context.obtainStyledAttributes(
            attrs, R.styleable.SearchView, defStyleAttr, defStyleRes
        )
        navigationIconSupport =
            typedArray.getInteger(
                R.styleable.SearchView_search_navigation_icon_support,
                SearchUtils.NavigationIconSupport.ANIMATION
            )
        theme = typedArray.getInteger(
            R.styleable.SearchView_search_theme,
            SearchUtils.Theme.LIGHT
        )
        typedArray.recycle()

        // TODO MORE ATTRIBUTTES
        setClearIconImageResource(R.drawable.search_ic_outline_clear_24px)
        mViewShadow?.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.search_shadow
            )
        )

        setDefault()
    }

    // *********************************************************************************************
    fun setBehavior(behavior: CoordinatorLayout.Behavior<*>) {
        mBehavior = behavior
    }

    // *********************************************************************************************
    private fun setDefault() {
        elevation =
            context.resources.getDimensionPixelSize(R.dimen.search_elevation).toFloat()
        margins = SearchUtils.Margins.TOOLBAR
        setBackgroundRadius(resources.getDimensionPixelSize(R.dimen.search_shape_rounded).toFloat())
        setLayoutHeight(context.resources.getDimensionPixelSize(R.dimen.search_layout_height))
        mSearchEditText?.setPadding(0, 0, 0, 0)
    }

    // *********************************************************************************************
    override fun addFocus() {
        filter("")

        mOnFocusChangeListener?.onFocusChange(true)

        SearchUtils.fadeAddFocus(mViewShadow, getAnimationDuration())
        animateHamburgerToArrow(false)
        showAdapter()
        showKeyboard()

        val paddingLeftRight =
            context.resources.getDimensionPixelSize(R.dimen.search_key_line_8)
        mSearchEditText?.setPadding(paddingLeftRight, 0, paddingLeftRight, 0)
        setLayoutHeight(context.resources.getDimensionPixelSize(R.dimen.search_layout_height_focus))
        setBackgroundRadius(resources.getDimensionPixelSize(R.dimen.search_shape_none).toFloat())
        margins = SearchUtils.Margins.NONE_TOOLBAR
        elevation = context.resources.getDimensionPixelSize(R.dimen.search_elevation_focus)
            .toFloat()
    }

    override fun removeFocus() {
        SearchUtils.fadeRemoveFocus(mViewShadow, getAnimationDuration())
        animateArrowToHamburger(false)
        hideKeyboard()
        hideAdapter()

        setDefault()

        mOnFocusChangeListener?.onFocusChange(false)
    }

    override fun getBehavior(): CoordinatorLayout.Behavior<*> {
        return mBehavior
    }

}
