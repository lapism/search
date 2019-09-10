package com.lapism.search.widget

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.lapism.search.R
import com.lapism.search.SearchUtils
import com.lapism.search.behavior.SearchBehavior
import com.lapism.search.internal.SearchLayout


class SearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : SearchLayout(context, attrs, defStyleAttr, defStyleRes), CoordinatorLayout.AttachedBehavior {

    // *********************************************************************************************
    private var mBehavior: CoordinatorLayout.Behavior<*> = SearchBehavior()

    // *********************************************************************************************
    init {
        inflate(context, R.layout.search_view, this)
        init()

        val a = context.obtainStyledAttributes(
            attrs, R.styleable.SearchView, defStyleAttr, defStyleRes
        )
        navigationIconSupport =
            a.getInteger(
                R.styleable.SearchView_search_navigation_icon_support,
                SearchUtils.NavigationIconSupport.ANIMATION
            )
        a.recycle()

        // TODO MORE ATTRIBUTTES in future
        setClearIconImageResource(R.drawable.search_ic_outline_clear_24px)
        mViewShadow?.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.search_shadow
            )
        )

        setDefault()

        val transition = LayoutTransition()
        transition.setDuration(getAnimationDuration())

        val linearLayoutMain = findViewById<LinearLayout>(R.id.search_linearLayout_main)
        linearLayoutMain.layoutTransition = transition
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
