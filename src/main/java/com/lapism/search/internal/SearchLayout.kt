package com.lapism.search.internal

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.ColorFilter
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.lapism.search.R
import com.lapism.search.SearchUtils

/**
 * @hide
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
abstract class SearchLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes), View.OnClickListener,
    Filter.FilterListener {

    // *********************************************************************************************
    protected var mLinearLayout: LinearLayout? = null
    protected var mCardView: CardView? = null
    protected var mSearchEditText: SearchEditText? = null
    protected var mViewShadow: View? = null
    protected var mViewDivider: View? = null
    protected var mOnFocusChangeListener: OnFocusChangeListener? = null

    private var mAnimationDuration: Long = 0
    private var mImageViewNavigation: ImageView? = null
    private var mImageViewMic: ImageView? = null
    private var mImageViewClear: ImageView? = null
    private var mImageViewMenu: ImageView? = null
    private var mRecyclerView: RecyclerView? = null
    private var mSearchArrowDrawable: SearchArrowDrawable? = null
    private var mOnQueryTextListener: OnQueryTextListener? = null
    private var mOnNavigationClickListener: OnNavigationClickListener? = null
    private var mOnMicClickListener: OnMicClickListener? = null
    private var mOnClearClickListener: OnClearClickListener? = null
    private var mOnMenuClickListener: OnMenuClickListener? = null

    // *********************************************************************************************
    @SearchUtils.NavigationIconSupport
    @get:SearchUtils.NavigationIconSupport
    var navigationIconSupport: Int = 0
        set(@SearchUtils.NavigationIconSupport navigationIconSupport) {
            field = navigationIconSupport

            when (navigationIconSupport) {
                SearchUtils.NavigationIconSupport.HAMBURGER -> setNavigationIconImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.search_ic_outline_menu_24px
                    )
                )
                SearchUtils.NavigationIconSupport.ARROW -> setNavigationIconImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.search_ic_outline_arrow_back_24px
                    )
                )
                SearchUtils.NavigationIconSupport.ANIMATION -> {
                    mSearchArrowDrawable = SearchArrowDrawable(context)
                    setNavigationIconImageDrawable(mSearchArrowDrawable)
                }
            }
        }

    @SearchUtils.Margins
    @get:SearchUtils.Margins
    protected var margins: Int = 0
        set(@SearchUtils.Margins margins) {
            field = margins

            val left: Int
            val top: Int
            val right: Int
            val bottom: Int
            val params: LayoutParams

            when (margins) {
                SearchUtils.Margins.NONE_TOOLBAR -> {
                    left =
                        context.resources.getDimensionPixelSize(R.dimen.search_margins_toolbar_none)
                    top =
                        context.resources.getDimensionPixelSize(R.dimen.search_margins_toolbar_none)
                    right =
                        context.resources.getDimensionPixelSize(R.dimen.search_margins_toolbar_none)
                    bottom =
                        context.resources.getDimensionPixelSize(R.dimen.search_margins_toolbar_none)

                    params =
                        LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    params.setMargins(left, top, right, bottom)
                    mCardView?.layoutParams = params
                }
                SearchUtils.Margins.NONE_MENU_ITEM -> {
                    left =
                        context.resources.getDimensionPixelSize(R.dimen.search_margins_menu_item_none)
                    top =
                        context.resources.getDimensionPixelSize(R.dimen.search_margins_menu_item_none)
                    right =
                        context.resources.getDimensionPixelSize(R.dimen.search_margins_menu_item_none)
                    bottom =
                        context.resources.getDimensionPixelSize(R.dimen.search_margins_menu_item_none)

                    params =
                        LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    params.setMargins(left, top, right, bottom)
                    mCardView?.layoutParams = params
                }
                SearchUtils.Margins.TOOLBAR -> {
                    left =
                        context.resources.getDimensionPixelSize(R.dimen.search_margins_toolbar_left_right)
                    top =
                        context.resources.getDimensionPixelSize(R.dimen.search_margins_toolbar_top_bottom)
                    right =
                        context.resources.getDimensionPixelSize(R.dimen.search_margins_toolbar_left_right)
                    bottom =
                        context.resources.getDimensionPixelSize(R.dimen.search_margins_toolbar_top_bottom)

                    params =
                        LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    params.setMargins(left, top, right, bottom)
                    mCardView?.layoutParams = params
                }
                SearchUtils.Margins.MENU_ITEM -> {
                    left =
                        context.resources.getDimensionPixelSize(R.dimen.search_margins_menu_item)
                    top =
                        context.resources.getDimensionPixelSize(R.dimen.search_margins_menu_item)
                    right =
                        context.resources.getDimensionPixelSize(R.dimen.search_margins_menu_item)
                    bottom =
                        context.resources.getDimensionPixelSize(R.dimen.search_margins_menu_item)

                    params = LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    params.setMargins(left, top, right, bottom)
                    mCardView?.layoutParams = params
                }
            }
        }

    // *********************************************************************************************
    protected abstract fun addFocus()

    protected abstract fun removeFocus()

    // *********************************************************************************************
    protected fun init() {
        setAnimationDuration(context.resources.getInteger(R.integer.search_animation_duration).toLong())

        mLinearLayout = findViewById(R.id.search_linearLayout)

        mImageViewNavigation = findViewById(R.id.search_imageView_navigation)
        mImageViewNavigation?.setOnClickListener(this)

        mImageViewMic = findViewById(R.id.search_imageView_mic)
        mImageViewMic?.setOnClickListener(this)

        mImageViewClear = findViewById(R.id.search_imageView_clear)
        mImageViewClear?.visibility = View.GONE
        mImageViewClear?.setOnClickListener(this)

        mImageViewMenu = findViewById(R.id.search_imageView_menu)
        mImageViewMenu?.visibility = View.GONE
        mImageViewMenu?.setOnClickListener(this)

        mSearchEditText = findViewById(R.id.search_searchEditText)
        mSearchEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                this@SearchLayout.onTextChanged(s)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        mSearchEditText?.setOnEditorActionListener { _, _, _ ->
            onSubmitQuery()
            return@setOnEditorActionListener true // true
        }
        mSearchEditText?.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                addFocus()
            } else {
                removeFocus()
            }
        }

        mRecyclerView = findViewById(R.id.search_recyclerView)
        mRecyclerView?.layoutManager = LinearLayoutManager(context)
        mRecyclerView?.visibility = View.GONE
        mRecyclerView?.isNestedScrollingEnabled = false
        mRecyclerView?.itemAnimator = DefaultItemAnimator()
        mRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    hideKeyboard()
                }
            }
        })

        mViewDivider = findViewById(R.id.search_view_divider)
        mViewDivider?.visibility = View.GONE

        mViewShadow = findViewById(R.id.search_view_shadow)
        mViewShadow?.visibility = View.GONE

        mCardView = findViewById<MaterialCardView>(R.id.search_materialCardView)

        isFocusable = true
        isFocusableInTouchMode = true
        //isClickable = true
        setOnClickListener(this)
    }

    // *********************************************************************************************
    fun setNavigationIconImageResource(@DrawableRes resId: Int) {
        mImageViewNavigation?.setImageResource(resId)
    }

    fun setNavigationIconImageDrawable(@Nullable drawable: Drawable?) {
        mImageViewNavigation?.setImageDrawable(drawable)
    }

    fun setNavigationIconColorFilter(color: Int) {
        mImageViewNavigation?.setColorFilter(color)
    }

    fun setNavigationIconColorFilter(cf: ColorFilter?) {
        mImageViewNavigation?.colorFilter = cf
    }

    fun clearNavigationIconColorFilter() {
        mImageViewNavigation?.clearColorFilter()
    }

    fun setNavigationIconContentDescription(contentDescription: CharSequence) {
        mImageViewNavigation?.contentDescription = contentDescription
    }

    // *********************************************************************************************
    fun setMicIconImageResource(@DrawableRes resId: Int) {
        mImageViewMic?.setImageResource(resId)
    }

    fun setMicIconImageDrawable(@Nullable drawable: Drawable?) {
        mImageViewMic?.setImageDrawable(drawable)
    }

    fun setMicIconColorFilter(color: Int) {
        mImageViewMic?.setColorFilter(color)
    }

    fun setMicIconColorFilter(cf: ColorFilter?) {
        mImageViewMic?.colorFilter = cf
    }

    fun clearMicIconColorFilter() {
        mImageViewMic?.clearColorFilter()
    }

    fun setMicIconContentDescription(contentDescription: CharSequence) {
        mImageViewMic?.contentDescription = contentDescription
    }

    // *********************************************************************************************
    fun setClearIconImageResource(@DrawableRes resId: Int) {
        mImageViewClear?.setImageResource(resId)
    }

    fun setClearIconImageDrawable(@Nullable drawable: Drawable?) {
        mImageViewClear?.setImageDrawable(drawable)
    }

    fun setClearIconColorFilter(color: Int) {
        mImageViewClear?.setColorFilter(color)
    }

    fun setClearIconColorFilter(cf: ColorFilter?) {
        mImageViewClear?.colorFilter = cf
    }

    fun clearClearIconColorFilter() {
        mImageViewClear?.clearColorFilter()
    }

    fun setClearIconContentDescription(contentDescription: CharSequence) {
        mImageViewClear?.contentDescription = contentDescription
    }

    // *********************************************************************************************
    fun setMenuIconImageResource(@DrawableRes resId: Int) {
        mImageViewMenu?.setImageResource(resId)
    }

    fun setMenuIconImageDrawable(@Nullable drawable: Drawable?) {
        mImageViewMenu?.setImageDrawable(drawable)
    }

    fun setMenuIconColorFilter(color: Int) {
        mImageViewMenu?.setColorFilter(color)
    }

    fun setMenuIconColorFilter(cf: ColorFilter?) {
        mImageViewMenu?.colorFilter = cf
    }

    fun clearMenuIconColorFilter() {
        mImageViewMenu?.clearColorFilter()
    }

    fun setMenuIconContentDescription(contentDescription: CharSequence) {
        mImageViewMenu?.contentDescription = contentDescription
    }

    // *********************************************************************************************
    fun setAdapterLayoutManager(@Nullable layout: RecyclerView.LayoutManager?) {
        mRecyclerView?.layoutManager = layout
    }

    fun setAdapterHasFixedSize(hasFixedSize: Boolean) {
        mRecyclerView?.setHasFixedSize(hasFixedSize)
    }

    /**
     * DividerItemDecoration class
     */
    fun addAdapterItemDecoration(@NonNull decor: RecyclerView.ItemDecoration) {
        mRecyclerView?.addItemDecoration(decor)
    }

    fun removeAdapterItemDecoration(@NonNull decor: RecyclerView.ItemDecoration) {
        mRecyclerView?.removeItemDecoration(decor)
    }

    fun setAdapter(@Nullable adapter: RecyclerView.Adapter<*>?) {
        mRecyclerView?.adapter = adapter
    }

    @Nullable
    fun getAdapter(): RecyclerView.Adapter<*>? {
        return mRecyclerView?.adapter
    }

    // *********************************************************************************************
    /**
     * Typeface.NORMAL
     * Typeface.BOLD
     * Typeface.ITALIC
     * Typeface.BOLD_ITALIC
     *
     * Typeface.DEFAULT
     * Typeface.DEFAULT_BOLD
     * Typeface.SANS_SERIF
     * Typeface.SERIF
     * Typeface.MONOSPACE
     *
     * Typeface.create(Typeface.NORMAL, Typeface.DEFAULT)
     *
     * TODO PARAMETERS NAME
     */
    fun setTextTypeface(@Nullable typeface: Typeface?) {
        mSearchEditText?.typeface = typeface
    }

    @Nullable
    fun getTextTypeface(): Typeface? {
        return mSearchEditText?.typeface
    }

    fun setTextInputType(inputType: Int) {
        mSearchEditText?.inputType = inputType
    }

    fun getTextInputType(): Int? {
        return mSearchEditText?.inputType
    }

    fun setTextImeOptions(imeOptions: Int) {
        mSearchEditText?.imeOptions = imeOptions
    }

    fun getTextImeOptions(): Int? {
        return mSearchEditText?.imeOptions
    }

    fun setTextQuery(@Nullable query: CharSequence?, submit: Boolean) {
        mSearchEditText?.setText(query)
        if (query != null) {
            mSearchEditText?.setSelection(mSearchEditText?.length()!!)
        }
        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery()
        }
    }

    @Nullable
    fun getTextQuery(): CharSequence? {
        return mSearchEditText?.text
    }

    fun setTextHint(hint: CharSequence?) {
        mSearchEditText?.hint = hint
    }

    fun getTextHint(): CharSequence? {
        return mSearchEditText?.hint
    }

    fun setTextColor(@ColorInt color: Int) {
        mSearchEditText?.setTextColor(color)
    }

    fun setTextSize(size: Float) {
        mSearchEditText?.textSize = size
    }

    fun setTextGravity(gravity: Int) {
        mSearchEditText?.gravity = gravity
    }

    fun setTextHint(@StringRes hint: Int) {
        mSearchEditText?.setHint(hint)
    }

    fun setTextHintColor(@ColorInt color: Int) {
        mSearchEditText?.setHintTextColor(color)
    }

    // *********************************************************************************************
    override fun setBackgroundColor(@ColorInt color: Int) {
        mCardView?.setCardBackgroundColor(color)
    }

    fun setBackgroundColor(@Nullable color: ColorStateList?) {
        mCardView?.setCardBackgroundColor(color)
    }

    // TODO PUBLIC
    override fun setElevation(elevation: Float) {
        setElevationCompat(elevation)
    }

    // TODO PUBLIC
    protected fun setElevationCompat(elevation: Float) {
        mCardView?.cardElevation = elevation
    }

    // TODO PUBLIC
    protected fun setMaxElevation(maxElevation: Float) {
        mCardView?.maxCardElevation = maxElevation
    }

    // TODO PUBLIC
    protected fun setBackgroundRadius(radius: Float) {
        mCardView?.radius = radius
    }

    fun setBackgroundRippleColor(@ColorRes rippleColorResourceId: Int) {
        if (mCardView is MaterialCardView) {
            (mCardView as MaterialCardView).setRippleColorResource(rippleColorResourceId)
        }
    }

    fun setBackgroundRippleColorResource(@Nullable rippleColor: ColorStateList?) {
        if (mCardView is MaterialCardView) {
            (mCardView as MaterialCardView).rippleColor = rippleColor
        }
    }

    fun setBackgroundStrokeColor(@ColorInt strokeColor: Int) {
        if (mCardView is MaterialCardView) {
            (mCardView as MaterialCardView).strokeColor = strokeColor
        }
    }

    fun setBackgroundStrokeColor(strokeColor: ColorStateList) {
        if (mCardView is MaterialCardView) {
            (mCardView as MaterialCardView).setStrokeColor(strokeColor)
        }
    }

    fun setBackgroundStrokeWidth(@Dimension strokeWidth: Int) {
        if (mCardView is MaterialCardView) {
            (mCardView as MaterialCardView).strokeWidth = strokeWidth
        }
    }

    // *********************************************************************************************
    fun setBackgroundColorViewOnly(@ColorInt color: Int) {
        mLinearLayout?.setBackgroundColor(color)
    }

    fun setDividerColor(@ColorInt color: Int) {
        mViewDivider?.setBackgroundColor(color)
    }

    fun setShadowColor(@ColorInt color: Int) {
        mViewShadow?.setBackgroundColor(color)
    }

    // *********************************************************************************************
    fun setOnFocusChangeListener(listener: OnFocusChangeListener) {
        mOnFocusChangeListener = listener
    }

    fun setOnQueryTextListener(listener: OnQueryTextListener) {
        mOnQueryTextListener = listener
    }

    fun setOnNavigationClickListener(listener: OnNavigationClickListener) {
        mOnNavigationClickListener = listener
    }

    fun setOnMicClickListener(listener: OnMicClickListener) {
        mOnMicClickListener = listener
    }

    fun setOnClearClickListener(listener: OnClearClickListener) {
        mOnClearClickListener = listener
    }

    fun setOnMenuClickListener(listener: OnMenuClickListener) {
        mOnMenuClickListener = listener
        mImageViewMenu?.visibility = View.VISIBLE
    }

    // *********************************************************************************************
    fun showKeyboard() {
        if (!isInEditMode) {
            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(
                mSearchEditText,
                InputMethodManager.RESULT_UNCHANGED_SHOWN
            )
        }
    }

    fun hideKeyboard() {
        if (!isInEditMode) {
            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                windowToken,
                InputMethodManager.RESULT_UNCHANGED_SHOWN
            )
        }
    }

    // *********************************************************************************************
    protected fun getAnimationDuration(): Long {
        return mAnimationDuration
    }

    protected fun filter(constraint: CharSequence?) {
        if (mRecyclerView?.adapter != null && mRecyclerView?.adapter is Filterable) {
            (mRecyclerView?.adapter as Filterable).filter.filter(constraint, this)
        }
    }

    protected fun getLayoutHeight(): Int {
        val params = mLinearLayout?.layoutParams
        return params?.height!!
    }

    protected fun setLayoutHeight(height: Int) {
        val params = mLinearLayout?.layoutParams
        params?.height = height
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        mLinearLayout?.layoutParams = params
    }

    protected fun showAdapter() {
        if (mRecyclerView?.adapter != null) {
            mRecyclerView?.visibility = View.VISIBLE
        }
    }

    protected fun hideAdapter() {
        if (mRecyclerView?.adapter != null) {
            mRecyclerView?.visibility = View.GONE
        }
    }

    protected fun animateHamburgerToArrow(verticalMirror: Boolean) {
        if (verticalMirror) {
            mSearchArrowDrawable?.setVerticalMirror(false)
        }
        mSearchArrowDrawable?.animate(
            SearchArrowDrawable.STATE_ARROW,
            getAnimationDuration()
        )
    }

    protected fun animateArrowToHamburger(verticalMirror: Boolean) {
        if (verticalMirror) {
            mSearchArrowDrawable?.setVerticalMirror(true)
        }
        mSearchArrowDrawable?.animate(
            SearchArrowDrawable.STATE_HAMBURGER,
            getAnimationDuration()
        )
    }

    // *********************************************************************************************
    // TODO - SET AS PUBLIC IN THE FUTURE RELEASE
    private fun setAnimationDuration(animationDuration: Long) {
        mAnimationDuration = animationDuration
    }

    private fun onTextChanged(newText: CharSequence) {
        filter(newText)
        setMicOrClearIcon(newText)

        if (mOnQueryTextListener != null) {
            mOnQueryTextListener?.onQueryTextChange(newText)
        }
    }

    private fun setMicOrClearIcon(s: CharSequence) {
        if (!TextUtils.isEmpty(s)) {
            mImageViewMic?.visibility = View.GONE
            mImageViewClear?.visibility = View.VISIBLE
        } else {
            mImageViewClear?.visibility = View.GONE
            mImageViewMic?.visibility = View.VISIBLE
        }
    }

    private fun onSubmitQuery() {
        val query = mSearchEditText?.text
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            if (mOnQueryTextListener == null || !mOnQueryTextListener!!.onQueryTextSubmit(query.toString())) {
                mSearchEditText?.text = query
            }
        }
    }

    // *********************************************************************************************
    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val ss = SearchViewSavedState(superState!!)
        ss.query = mSearchEditText?.text
        ss.hasFocus = mSearchEditText?.hasFocus()!!
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is SearchViewSavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)
        if (state.hasFocus) {
            mSearchEditText?.requestFocus()
        }
        if (state.query != null) {
            setTextQuery(state.query, false)
        }
        requestLayout()
    }

    override fun requestFocus(direction: Int, previouslyFocusedRect: Rect?): Boolean {
        return if (!isFocusable) {
            false
        } else {
            mSearchEditText?.requestFocus(direction, previouslyFocusedRect)!!
        }
    }

    override fun clearFocus() {
        super.clearFocus()
        mSearchEditText?.clearFocus()
    }

    override fun onClick(view: View?) {
        if (view === mImageViewNavigation) {
            if (mSearchEditText?.hasFocus()!!) {
                mSearchEditText?.clearFocus()
            } else {
                if (mOnNavigationClickListener != null) {
                    mOnNavigationClickListener?.onNavigationClick()
                }
            }
        } else if (view === mImageViewMic) {
            if (mOnMicClickListener != null) {
                mOnMicClickListener?.onMicClick()
            }
        } else if (view === mImageViewClear) {
            if (mSearchEditText?.text!!.isNotEmpty()) {
                mSearchEditText?.text!!.clear()
            }
            if (mOnClearClickListener != null) {
                mOnClearClickListener?.onClearClick()
            }
        } else if (view === mImageViewMenu) {
            if (mOnMenuClickListener != null) {
                mOnMenuClickListener?.onMenuClick()
            }
        } else if (view === mViewShadow) {
            mSearchEditText?.clearFocus()
        }
    }

    override fun onFilterComplete(count: Int) {

    }

    // *********************************************************************************************
    interface OnFocusChangeListener {

        fun onFocusChange(hasFocus: Boolean)
    }

    interface OnQueryTextListener {

        fun onQueryTextChange(newText: CharSequence)

        fun onQueryTextSubmit(query: CharSequence): Boolean
    }

    interface OnNavigationClickListener {

        fun onNavigationClick()
    }

    interface OnMicClickListener {

        fun onMicClick()
    }

    interface OnClearClickListener {

        fun onClearClick()
    }

    interface OnMenuClickListener {

        fun onMenuClick()
    }

}
