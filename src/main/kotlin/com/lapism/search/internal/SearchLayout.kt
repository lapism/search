package com.lapism.search.internal

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lapism.search.R
import com.lapism.search.databinding.SearchViewBinding


abstract class SearchLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes), View.OnClickListener {

    // *********************************************************************************************
    @IntDef(
        NavigationIconSupport.NONE,
        NavigationIconSupport.MENU,
        NavigationIconSupport.ARROW,
        NavigationIconSupport.SEARCH
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class NavigationIconSupport {
        companion object {
            const val NONE = 1000
            const val MENU = 1001
            const val ARROW = 1002
            const val SEARCH = 1003
        }
    }

    @IntDef(
        Margins.NO_FOCUS,
        Margins.FOCUS
    )
    @Retention(AnnotationRetention.SOURCE)
    internal annotation class Margins {
        companion object {
            const val NO_FOCUS = 2000
            const val FOCUS = 2001
        }
    }

    // *********************************************************************************************
    protected val binding: SearchViewBinding =
        SearchViewBinding.inflate(LayoutInflater.from(context))
    protected var mOnFocusChangeListener: OnFocusChangeListener? = null

    private var mAnimationDuration: Long = 0
    private var mOnQueryTextListener: OnQueryTextListener? = null
    private var mOnNavigationClickListener: OnNavigationClickListener? = null
    private var mOnMicClickListener: OnMicClickListener? = null
    private var mOnClearClickListener: OnClearClickListener? = null
    private var mOnMenuClickListener: OnMenuClickListener? = null

    // *********************************************************************************************
    @NavigationIconSupport
    @get:NavigationIconSupport
    var navigationIconSupport: Int = 0
        set(@NavigationIconSupport navigationIconSupport) {
            field = navigationIconSupport

            when (navigationIconSupport) {
                NavigationIconSupport.NONE
                -> {
                    setNavigationIconImageDrawable(null)
                }
                NavigationIconSupport.MENU -> {
                    setNavigationIconImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.search_ic_outline_menu_24px
                        )
                    )
                }
                NavigationIconSupport.ARROW -> {
                    setNavigationIconImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.search_ic_outline_arrow_back_24px
                        )
                    )
                }
                NavigationIconSupport.SEARCH -> {
                    setNavigationIconImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.search_ic_outline_search_24px
                        )
                    )
                }
            }
        }

    @Margins
    @get:Margins
    protected var margins: Int = 0
        set(@Margins margins) {
            field = margins

            val left: Int
            val top: Int
            val right: Int
            val bottom: Int
            val params: LayoutParams

            when (margins) {
                Margins.NO_FOCUS -> {
                    left =
                        context.resources.getDimensionPixelSize(R.dimen.search_margins_left_right)
                    top =
                        context.resources.getDimensionPixelSize(R.dimen.search_margins_top_bottom)
                    right =
                        context.resources.getDimensionPixelSize(R.dimen.search_margins_left_right)
                    bottom =
                        context.resources.getDimensionPixelSize(R.dimen.search_margins_top_bottom)

                    params =
                        LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    params.setMargins(left, top, right, bottom)
                    binding.searchMaterialCardView.layoutParams = params
                }
                Margins.FOCUS -> {
                    left =
                        context.resources.getDimensionPixelSize(R.dimen.search_margins_focus)
                    top =
                        context.resources.getDimensionPixelSize(R.dimen.search_margins_focus)
                    right =
                        context.resources.getDimensionPixelSize(R.dimen.search_margins_focus)
                    bottom =
                        context.resources.getDimensionPixelSize(R.dimen.search_margins_focus)

                    params =
                        LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    params.setMargins(left, top, right, bottom)
                    binding.searchMaterialCardView.layoutParams = params
                }
            }
        }

    // *********************************************************************************************
    protected abstract fun addFocus()

    protected abstract fun removeFocus()

    // *********************************************************************************************
    protected fun init() {
        setAnimationDuration(
            context.resources.getInteger(R.integer.search_animation_duration).toLong()
        )

        binding.searchImageViewNavigation.setOnClickListener(this)
        binding.searchImageViewMic.setOnClickListener(this)

        binding.searchImageViewClear.visibility = View.GONE
        binding.searchImageViewClear.setOnClickListener(this)

        binding.searchImageViewMenu.visibility = View.GONE
        binding.searchImageViewMenu.setOnClickListener(this)

        binding.searchSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                this@SearchLayout.onTextChanged(s)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        binding.searchSearchEditText.setOnEditorActionListener { _, _, _ ->
            onSubmitQuery()
            return@setOnEditorActionListener true // true
        }
        binding.searchSearchEditText.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                addFocus()
            } else {
                removeFocus()
            }
        }

        binding.searchRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.searchRecyclerView.isNestedScrollingEnabled = false
        binding.searchRecyclerView.itemAnimator = DefaultItemAnimator()
        binding.searchRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    hideKeyboard()
                }
            }
        })

        isFocusable = true
        isFocusableInTouchMode = true
        //isClickable = true TODO
        //setOnClickListener(this)
    }

    // *********************************************************************************************
    // set todo background

    fun setNavigationIconVisibility(visibility: Int) {
        binding.searchImageViewNavigation.visibility = visibility
    }

    fun setNavigationIconImageResource(@DrawableRes resId: Int) {
        binding.searchImageViewNavigation.setImageResource(resId)
    }

    fun setNavigationIconImageDrawable(@Nullable drawable: Drawable?) {
        binding.searchImageViewNavigation.setImageDrawable(drawable)
    }

    fun setNavigationIconColorFilter(color: Int) {
        binding.searchImageViewNavigation.setColorFilter(color)
    }

    fun setNavigationIconColorFilter(color: Int, mode: PorterDuff.Mode) {
        binding.searchImageViewNavigation.setColorFilter(color, mode)
    }

    fun setNavigationIconColorFilter(cf: ColorFilter?) {
        binding.searchImageViewNavigation.colorFilter = cf
    }

    fun clearNavigationIconColorFilter() {
        binding.searchImageViewNavigation.clearColorFilter()
    }

    fun setNavigationIconContentDescription(contentDescription: CharSequence) {
        binding.searchImageViewNavigation.contentDescription = contentDescription
    }

    // *********************************************************************************************
    fun setMicIconImageResource(@DrawableRes resId: Int) {
        binding.searchImageViewMic.setImageResource(resId)
    }

    fun setMicIconImageDrawable(@Nullable drawable: Drawable?) {
        binding.searchImageViewMic.setImageDrawable(drawable)
    }

    fun setMicIconColorFilter(color: Int) {
        binding.searchImageViewMic.setColorFilter(color)
    }

    fun setMicIconColorFilter(color: Int, mode: PorterDuff.Mode) {
        binding.searchImageViewMic.setColorFilter(color, mode)
    }

    fun setMicIconColorFilter(cf: ColorFilter?) {
        binding.searchImageViewMic.colorFilter = cf
    }

    fun clearMicIconColorFilter() {
        binding.searchImageViewMic.clearColorFilter()
    }

    fun setMicIconContentDescription(contentDescription: CharSequence) {
        binding.searchImageViewMic.contentDescription = contentDescription
    }

    // *********************************************************************************************
    fun setClearIconImageResource(@DrawableRes resId: Int) {
        binding.searchImageViewClear.setImageResource(resId)
    }

    fun setClearIconImageDrawable(@Nullable drawable: Drawable?) {
        binding.searchImageViewClear.setImageDrawable(drawable)
    }

    fun setClearIconColorFilter(color: Int) {
        binding.searchImageViewClear.setColorFilter(color)
    }

    fun setClearIconColorFilter(color: Int, mode: PorterDuff.Mode) {
        binding.searchImageViewClear.setColorFilter(color, mode)
    }

    fun setClearIconColorFilter(cf: ColorFilter?) {
        binding.searchImageViewClear.colorFilter = cf
    }

    fun clearClearIconColorFilter() {
        binding.searchImageViewClear.clearColorFilter()
    }

    fun setClearIconContentDescription(contentDescription: CharSequence) {
        binding.searchImageViewClear.contentDescription = contentDescription
    }

    // *********************************************************************************************
    fun setMenuIconVisibility(visibility: Int) {
        binding.searchImageViewMenu.visibility = visibility
    }

    fun setMenuIconImageResource(@DrawableRes resId: Int) {
        binding.searchImageViewMenu.setImageResource(resId)
    }

    fun setMenuIconImageDrawable(@Nullable drawable: Drawable?) {
        binding.searchImageViewMenu.setImageDrawable(drawable)
    }

    fun setMenuIconColorFilter(color: Int) {
        binding.searchImageViewMenu.setColorFilter(color)
    }

    fun setMenuIconColorFilter(color: Int, mode: PorterDuff.Mode) {
        binding.searchImageViewMenu.setColorFilter(color, mode)
    }

    fun setMenuIconColorFilter(cf: ColorFilter?) {
        binding.searchImageViewMenu.colorFilter = cf
    }

    fun clearMenuIconColorFilter() {
        binding.searchImageViewMenu.clearColorFilter()
    }

    fun setMenuIconContentDescription(contentDescription: CharSequence) {
        binding.searchImageViewMenu.contentDescription = contentDescription
    }

    // *********************************************************************************************
    fun setAdapterLayoutManager(@Nullable layout: RecyclerView.LayoutManager?) {
        binding.searchRecyclerView.layoutManager = layout
    }

    fun setAdapterHasFixedSize(hasFixedSize: Boolean) {
        binding.searchRecyclerView.setHasFixedSize(hasFixedSize)
    }

    fun addAdapterItemDecoration(@NonNull decor: RecyclerView.ItemDecoration) {
        binding.searchRecyclerView.addItemDecoration(decor)
    }

    fun removeAdapterItemDecoration(@NonNull decor: RecyclerView.ItemDecoration) {
        binding.searchRecyclerView.removeItemDecoration(decor)
    }

    fun setAdapter(@Nullable adapter: RecyclerView.Adapter<*>?) {
        binding.searchRecyclerView.adapter = adapter
    }

    @Nullable
    fun getAdapter(): RecyclerView.Adapter<*>? {
        return binding.searchRecyclerView.adapter
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
        binding.searchSearchEditText.typeface = typeface
    }

    @Nullable
    fun getTextTypeface(): Typeface? {
        return binding.searchSearchEditText.typeface
    }

    fun setTextInputType(inputType: Int) {
        binding.searchSearchEditText.inputType = inputType
    }

    fun getTextInputType(): Int? {
        return binding.searchSearchEditText.inputType
    }

    fun setTextImeOptions(imeOptions: Int) {
        binding.searchSearchEditText.imeOptions = imeOptions
    }

    fun getTextImeOptions(): Int? {
        return binding.searchSearchEditText.imeOptions
    }

    fun setTextQuery(@Nullable query: CharSequence?, submit: Boolean) {
        binding.searchSearchEditText.setText(query)
        if (query != null) {
            binding.searchSearchEditText.setSelection(binding.searchSearchEditText.length())
        }
        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery()
        }
    }

    @Nullable
    fun getTextQuery(): CharSequence? {
        return binding.searchSearchEditText.text
    }

    fun setTextHint(hint: CharSequence?) {
        binding.searchSearchEditText.hint = hint
    }

    fun getTextHint(): CharSequence? {
        return binding.searchSearchEditText.hint
    }

    fun setTextColor(@ColorInt color: Int) {
        binding.searchSearchEditText.setTextColor(color)
    }

    fun setTextSize(size: Float) {
        binding.searchSearchEditText.textSize = size
    }

    fun setTextGravity(gravity: Int) {
        binding.searchSearchEditText.gravity = gravity
    }

    fun setTextHint(@StringRes hint: Int) {
        binding.searchSearchEditText.setHint(hint)
    }

    fun setTextHintColor(@ColorInt color: Int) {
        binding.searchSearchEditText.setHintTextColor(color)
    }

    // *********************************************************************************************
    override fun setBackgroundColor(@ColorInt color: Int) {
        binding.searchMaterialCardView.setCardBackgroundColor(color)
    }

    fun setBackgroundColor(@Nullable color: ColorStateList?) {
        binding.searchMaterialCardView.setCardBackgroundColor(color)
    }

    override fun setElevation(elevation: Float) {
        binding.searchMaterialCardView.cardElevation = elevation
        binding.searchMaterialCardView.maxCardElevation = elevation
    }

    override fun getElevation(): Float {
        return binding.searchMaterialCardView.elevation
    }

    fun setBackgroundRadius(radius: Float) {
        binding.searchMaterialCardView.radius = radius
    }

    fun getBackgroundRadius(): Float {
        return binding.searchMaterialCardView.radius
    }

    fun setBackgroundRippleColor(@ColorRes rippleColorResourceId: Int) {
        binding.searchMaterialCardView.setRippleColorResource(rippleColorResourceId)
    }

    fun setBackgroundRippleColorResource(@Nullable rippleColor: ColorStateList?) {
        binding.searchMaterialCardView.rippleColor = rippleColor
    }

    fun setBackgroundStrokeColor(@ColorInt strokeColor: Int) {
        binding.searchMaterialCardView.strokeColor = strokeColor
    }

    fun setBackgroundStrokeColor(strokeColor: ColorStateList) {
        binding.searchMaterialCardView.setStrokeColor(strokeColor)
    }

    fun setBackgroundStrokeWidth(@Dimension strokeWidth: Int) {
        binding.searchMaterialCardView.strokeWidth = strokeWidth
    }

    fun getBackgroundStrokeWidth(): Int {
        return binding.searchMaterialCardView.strokeWidth
    }

    // *********************************************************************************************
    fun setBackgroundColorViewOnly(@ColorInt color: Int) {
        binding.searchLinearLayout.setBackgroundColor(color)
    }

    fun setDividerColor(@ColorInt color: Int) {
        binding.searchViewDivider.setBackgroundColor(color)
    }

    fun setShadowColor(@ColorInt color: Int) {
        binding.searchViewShadow.setBackgroundColor(color)
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
        binding.searchImageViewMenu.visibility = View.VISIBLE
    }

    // *********************************************************************************************
    fun showKeyboard() {
        if (!isInEditMode) {
            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(
                binding.searchSearchEditText,
                InputMethodManager.RESULT_SHOWN
                //InputMethodManager.RESULT_UNCHANGED_SHOWN todo
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

    fun filter(constraint: CharSequence) {
        if (mOnQueryTextListener != null) {
            mOnQueryTextListener?.onQueryTextChange(constraint)
        }
    }

    protected fun getLayoutHeight(): Int {
        val params = binding.searchLinearLayout.layoutParams
        return params?.height!!
    }

    protected fun setLayoutHeight(height: Int) {
        val params = binding.searchLinearLayout.layoutParams
        params?.height = height
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        binding.searchLinearLayout.layoutParams = params
    }

    // *********************************************************************************************
    // TODO - SET AS PUBLIC IN THE FUTURE RELEASE
    private fun setAnimationDuration(animationDuration: Long) {
        mAnimationDuration = animationDuration
    }

    private fun onTextChanged(newText: CharSequence) {
        filter(newText)
        setMicOrClearIcon(newText)
    }

    private fun setMicOrClearIcon(s: CharSequence) {
        if (!TextUtils.isEmpty(s)) {
            binding.searchImageViewMic.visibility = View.GONE
            binding.searchImageViewClear.visibility = View.VISIBLE
        } else {
            binding.searchImageViewClear.visibility = View.GONE
            binding.searchImageViewMic.visibility = View.VISIBLE
        }
    }

    private fun onSubmitQuery() {
        val query = binding.searchSearchEditText.text
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            if (mOnQueryTextListener == null || !mOnQueryTextListener!!.onQueryTextSubmit(query.toString())) {
                binding.searchSearchEditText.text = query
            }
        }
    }

    // *********************************************************************************************
    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val ss = SearchViewSavedState(superState!!)
        ss.query = binding.searchSearchEditText.text
        ss.hasFocus = binding.searchSearchEditText.hasFocus()
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is SearchViewSavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)
        if (state.hasFocus) {
            binding.searchSearchEditText.requestFocus()
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
            binding.searchSearchEditText.requestFocus(direction, previouslyFocusedRect)
        }
    }

    override fun clearFocus() {
        super.clearFocus()
        binding.searchSearchEditText.clearFocus()
    }

    override fun onClick(view: View?) {
        if (view === binding.searchImageViewNavigation) {
            if (binding.searchSearchEditText.hasFocus()) {
                binding.searchSearchEditText.clearFocus()
            } else {
                if (mOnNavigationClickListener != null) {
                    mOnNavigationClickListener?.onNavigationClick()
                }
            }
        } else if (view === binding.searchImageViewMic) {
            if (mOnMicClickListener != null) {
                mOnMicClickListener?.onMicClick()
            }
        } else if (view === binding.searchImageViewClear) {
            if (binding.searchSearchEditText.text!!.isNotEmpty()) {
                binding.searchSearchEditText.text!!.clear()
            }
            if (mOnClearClickListener != null) {
                mOnClearClickListener?.onClearClick()
            }
        } else if (view === binding.searchImageViewMenu) {
            if (mOnMenuClickListener != null) {
                mOnMenuClickListener?.onMenuClick()
            }
        }
    }

    // *********************************************************************************************
    interface OnFocusChangeListener {

        fun onFocusChange(hasFocus: Boolean)
    }

    interface OnQueryTextListener {

        fun onQueryTextChange(newText: CharSequence): Boolean

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
