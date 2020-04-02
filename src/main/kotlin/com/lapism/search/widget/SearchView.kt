package com.lapism.search.widget

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.lapism.search.R
import com.lapism.search.internal.SearchLayout


class SearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : SearchLayout(context, attrs, defStyleAttr, defStyleRes), CoordinatorLayout.AttachedBehavior {

    // *********************************************************************************************
    private var mBehavior: CoordinatorLayout.Behavior<*> = SearchBehavior<SearchView>()
    private var mStrokeWidth: Int = 0
    private var mElevation: Float = 0f
    private var mRadius: Float = 0f

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
                NavigationIconSupport.NONE
            )
        a.recycle()

        // TODO - MORE ATTRIBUTTES IN THE FUTURE RELEASE
        setClearIconImageResource(R.drawable.search_ic_outline_clear_24px)
        mViewShadow?.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.search_shadow
            )
        )

        setBackgroundRadius(resources.getDimensionPixelSize(R.dimen.search_radius).toFloat())
        elevation = context.resources.getDimensionPixelSize(R.dimen.search_elevation).toFloat()

        mRecyclerView?.visibility = View.GONE
        mViewShadow?.visibility = View.GONE
        mViewDivider?.visibility = View.GONE
        margins = Margins.NO_FOCUS


        val transition = LayoutTransition()
        transition.enableTransitionType(LayoutTransition.CHANGING)
        transition.setDuration(getAnimationDuration())
        transition.addTransitionListener(object : LayoutTransition.TransitionListener {
            override fun startTransition(
                transition: LayoutTransition?,
                container: ViewGroup?,
                view: View?,
                transitionType: Int
            ) {

            }

            override fun endTransition(
                transition: LayoutTransition?,
                container: ViewGroup?,
                view: View?,
                transitionType: Int
            ) {
                if (hasFocus()) {

                    showKeyboard()
                } else {
                    // mOnFocusChangeListener?.onFocusChange(false)
                    hideKeyboard()
                }
            }
        })

        val frameLayout = findViewById<FrameLayout>(R.id.search_frameLayout)
        frameLayout.layoutTransition = transition
    }

    // *********************************************************************************************
    fun setBehavior(behavior: CoordinatorLayout.Behavior<*>) {
        mBehavior = behavior
    }

    // *********************************************************************************************
    override fun addFocus() {
        mStrokeWidth = getBackgroundStrokeWidth()
        mElevation = elevation
        mRadius = getBackgroundRadius()
        filter("")

        mOnFocusChangeListener?.onFocusChange(true)
        mRecyclerView?.visibility = View.VISIBLE
        mViewShadow?.visibility = View.VISIBLE
        mViewDivider?.visibility = View.VISIBLE
        margins = Margins.FOCUS
        setLayoutHeight(context.resources.getDimensionPixelSize(R.dimen.search_layout_height_focus))
        val paddingLeftRight = context.resources.getDimensionPixelSize(R.dimen.search_dp_16)
        mSearchEditText?.setPadding(paddingLeftRight, 0, paddingLeftRight, 0)
        setBackgroundRadius(resources.getDimensionPixelSize(R.dimen.search_radius_focus).toFloat())
        elevation =
            context.resources.getDimensionPixelSize(R.dimen.search_elevation_focus).toFloat()
        setBackgroundStrokeWidth(context.resources.getDimensionPixelSize(R.dimen.search_stroke_width_focus))
    }

    override fun removeFocus() {
        mOnFocusChangeListener?.onFocusChange(false)
        mRecyclerView?.visibility = View.GONE
        mViewShadow?.visibility = View.GONE
        mViewDivider?.visibility = View.GONE
        margins = Margins.NO_FOCUS
        setLayoutHeight(context.resources.getDimensionPixelSize(R.dimen.search_layout_height))
        mSearchEditText?.setPadding(0, 0, 0, 0)
        setBackgroundRadius(mRadius)
        elevation = mElevation
        setBackgroundStrokeWidth(mStrokeWidth)
    }

    override fun getBehavior(): CoordinatorLayout.Behavior<*> {
        return mBehavior
    }

}