package com.lapism.search.widget

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
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
    private var radius: Float = 0f
    //todo get() = 1f

    // *********************************************************************************************
    init {
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

        // TODO - MORE ATTRIBUTTES IN THE FUTURE RELEASE. Any
        setClearIconImageResource(R.drawable.search_ic_outline_clear_24px)
        binding.searchViewShadow.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.search_shadow
            )
        )

        setBackgroundRadius(resources.getDimensionPixelSize(R.dimen.search_radius).toFloat())
        elevation = context.resources.getDimensionPixelSize(R.dimen.search_elevation).toFloat()

        binding.searchViewDivider.visibility = View.GONE
        binding.searchViewShadow.visibility = View.GONE
        binding.searchRecyclerView.visibility = View.GONE

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
                    setBackgroundRadius(
                        resources.getDimensionPixelSize(R.dimen.search_radius_focus).toFloat()
                    )
                    showKeyboard()
                } else {
                    mOnFocusChangeListener?.onFocusChange(false)
                }
            }
        })

        binding.searchRoot.layoutTransition = transition
    }

    // *********************************************************************************************
    override fun addFocus() {
        mStrokeWidth = getBackgroundStrokeWidth()
        mElevation = elevation
        radius = getBackgroundRadius()

        val paddingLeftRight = context.resources.getDimensionPixelSize(R.dimen.search_dp_16)
        binding.searchSearchEditText.setPadding(paddingLeftRight, 0, paddingLeftRight, 0)

        mOnFocusChangeListener?.onFocusChange(true)

        elevation =
            context.resources.getDimensionPixelSize(R.dimen.search_elevation_focus).toFloat()
        setBackgroundStrokeWidth(context.resources.getDimensionPixelSize(R.dimen.search_stroke_width_focus))

        margins = Margins.FOCUS
        setLayoutHeight(context.resources.getDimensionPixelSize(R.dimen.search_layout_height_focus))

        binding.searchViewShadow.visibility = View.VISIBLE
        binding.searchViewDivider.visibility = View.VISIBLE
        binding.searchRecyclerView.visibility = View.VISIBLE
    }

    override fun removeFocus() {
        binding.searchSearchEditText.setPadding(0, 0, 0, 0)

        binding.searchViewDivider.visibility = View.GONE
        binding.searchViewShadow.visibility = View.GONE
        binding.searchRecyclerView.visibility = View.GONE

        margins = Margins.NO_FOCUS
        setLayoutHeight(context.resources.getDimensionPixelSize(R.dimen.search_layout_height))

        setBackgroundRadius(radius)
        elevation = mElevation
        setBackgroundStrokeWidth(mStrokeWidth)

        hideKeyboard()
    }

    override fun getBehavior(): CoordinatorLayout.Behavior<*> {
        return mBehavior
    }

    fun setBehavior(behavior: CoordinatorLayout.Behavior<*>) {
        mBehavior = behavior
    }

}