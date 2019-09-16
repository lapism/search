package com.lapism.search.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import com.lapism.search.R
import com.lapism.search.SearchUtils
import com.lapism.search.internal.SearchAnimation
import com.lapism.search.internal.SearchLayout


class SearchMenuItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : SearchLayout(context, attrs, defStyleAttr, defStyleRes) {

    // *********************************************************************************************
    private var mShadowVisibility: Boolean = true
    private var mMenuItemCx = -1

    // *********************************************************************************************
    init {
        inflate(context, R.layout.search_view, this)
        init()

        val a = context.obtainStyledAttributes(
            attrs, R.styleable.SearchMenuItem, defStyleAttr, defStyleRes
        )
        navigationIconSupport =
            a.getInteger(
                R.styleable.SearchMenuItem_search_navigation_icon_support,
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
    }

    // *********************************************************************************************
    fun requestFocus(menuItem: MenuItem) {
        if (!isFocusable) {
            return
        } else {
            getMenuItemPosition(menuItem.itemId)
            mSearchEditText?.requestFocus()!!
        }
    }

    fun setShadowVisibility(visibility: Boolean) {
        if (visibility) {
            mViewShadow?.visibility = View.VISIBLE
        } else {
            mViewShadow?.visibility = View.GONE
        }
        mShadowVisibility = visibility
    }

    // *********************************************************************************************
    private fun setDefault() {
        elevation =
            context.resources.getDimensionPixelSize(R.dimen.search_elevation).toFloat()
        margins = SearchUtils.Margins.MENU_ITEM
        setBackgroundRadius(resources.getDimensionPixelSize(R.dimen.search_shape_rounded).toFloat())
        setLayoutHeight(context.resources.getDimensionPixelSize(R.dimen.search_layout_height))
        val paddingLeftRight =
            context.resources.getDimensionPixelSize(R.dimen.search_key_line_8)
        mSearchEditText?.setPadding(paddingLeftRight, 0, paddingLeftRight, 0)

        mViewShadow?.setOnClickListener(this)
        mCardView?.visibility = View.GONE
        visibility = View.GONE
    }

    private fun getMenuItemPosition(menuItemId: Int) {
        var viewParent = parent
        while (viewParent is View) {
            val parent = viewParent as View
            val view = parent.findViewById<View>(menuItemId)
            if (view != null) {
                mMenuItemCx = getCenterX(view)
                break
            }
            viewParent = viewParent.getParent()
        }
    }

    private fun getCenterX(view: View): Int {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        return location[0] + view.width / 2
    }

    // *********************************************************************************************
    override fun onFilterComplete(count: Int) {
        super.onFilterComplete(count)
        if (getAdapter()?.itemCount!! > 0) {
            mViewDivider?.visibility = View.VISIBLE
        } else {
            mViewDivider?.visibility = View.GONE
        }
    }

    override fun addFocus() {
        visibility = View.VISIBLE
        mCardView?.visibility = View.VISIBLE

        val viewTreeObserver = mCardView?.viewTreeObserver
        if (viewTreeObserver?.isAlive!!) {
            viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val animation = SearchAnimation()
                    animation.setOnAnimationListener(object :
                        SearchAnimation.OnAnimationListener {
                        override fun onAnimationStart() {
                            filter("")
                            mOnFocusChangeListener?.onFocusChange(true)
                            if (mShadowVisibility) {
                                SearchUtils.fadeAddFocus(mViewShadow, getAnimationDuration())
                            }
                            animateHamburgerToArrow(true)
                        }

                        override fun onAnimationEnd() {
                            showAdapter()
                            showKeyboard()
                        }
                    })
                    animation.start(
                        context,
                        mLinearLayout,
                        mCardView,
                        mMenuItemCx,
                        getAnimationDuration(),
                        true
                    )

                    mCardView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                }
            })
        }
    }

    override fun removeFocus() {
        val animation = SearchAnimation()
        animation.setOnAnimationListener(object : SearchAnimation.OnAnimationListener {
            override fun onAnimationStart() {
                if (mShadowVisibility) {
                    SearchUtils.fadeRemoveFocus(mViewShadow, getAnimationDuration())
                }
                animateArrowToHamburger(true)
                hideKeyboard()
                hideAdapter()
                mOnFocusChangeListener?.onFocusChange(false)
            }

            override fun onAnimationEnd() {
                mCardView?.visibility = View.GONE
                visibility = View.GONE
            }
        })
        animation.start(
            context,
            mLinearLayout,
            mCardView,
            mMenuItemCx,
            getAnimationDuration(),
            false
        )
    }

}
