package com.lapism.search.widget

import android.content.Context
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.customview.view.AbsSavedState
import androidx.transition.*
import com.lapism.search.R
import com.lapism.search.databinding.MaterialSearchViewBinding


//@Suppress("MemberVisibilityCanBePrivate", "unused")
class MaterialSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : MaterialSearchLayout(context, attrs, defStyleAttr, defStyleRes) {

    // *********************************************************************************************
    private var binding: MaterialSearchViewBinding
    private var focusListener: OnFocusChangeListener? = null
    private var queryListener: OnQueryTextListener? = null
    private var clearClickListener: OnClearClickListener? = null

    // *********************************************************************************************
    init {
        val inflater = LayoutInflater.from(getContext())
        binding = MaterialSearchViewBinding.inflate(inflater, this)

        binding.searchViewEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                this@MaterialSearchView.onTextChanged(s)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        binding.searchViewEditText.setOnEditorActionListener { _, _, _ ->
            onSubmitQuery()
            return@setOnEditorActionListener true // same as ,,true" :-)
        }
        binding.searchViewEditText.setOnFocusChangeListener { _, hasFocus ->
            visibility = if (hasFocus) {
                showKeyboard()
                View.VISIBLE
            } else {
                hideKeyboard()
                View.GONE
            }

            focusListener?.onFocusChange(hasFocus)
        }

        val left = context.resources.getDimensionPixelSize(R.dimen.search_dp_8)
        val params = binding.searchViewEditText.layoutParams as? MarginLayoutParams
        params?.setMargins(left, 0, 0, 0)
        binding.searchViewEditText.layoutParams = params

        binding.searchViewEditText.isFocusable = true
        binding.searchViewEditText.isFocusableInTouchMode = true

        binding.searchViewClearButton.visibility = View.GONE
        binding.searchViewClearButton.setOnClickListener {
            clearClickListener?.onClearClick()
            binding.searchViewEditText.text?.clear()
        }

        val a = context.obtainStyledAttributes(
            attrs, R.styleable.MaterialSearchView, defStyleAttr, defStyleRes
        )

        when {
            a.hasValue(R.styleable.MaterialSearchView_search_navigationIconCompat) -> {
                navigationIconCompat = a.getInt(
                    R.styleable.MaterialSearchView_search_navigationIconCompat,
                    NavigationIconCompat.NONE
                )
            }
            a.hasValue(R.styleable.MaterialSearchView_search_navigationIcon) -> {
                setNavigationIcon(a.getDrawable(R.styleable.MaterialSearchView_search_navigationIcon))
            }
        }

        if (a.hasValue(R.styleable.MaterialSearchView_search_navigationContentDescription)) {
            val description =
                a.getText(R.styleable.MaterialSearchView_search_navigationContentDescription)
            setNavigationContentDescription(description)
        }

        if (a.hasValue(R.styleable.MaterialSearchView_search_backgroundColor)) {
            val color = a.getInt(R.styleable.MaterialSearchView_search_backgroundColor, 0)
            setBackgroundColor(color)
        }

        if (a.hasValue(R.styleable.MaterialSearchView_search_clearIcon)) {
            setClearIcon(a.getDrawable(R.styleable.MaterialSearchView_search_clearIcon))
        } else {
            setClearIcon(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.search_ic_outline_clear_24
                )
            )
        }

        if (a.hasValue(R.styleable.MaterialSearchView_search_dividerColor)) {
            setDividerColor(a.getInt(R.styleable.MaterialSearchView_search_dividerColor, 0))
        }

        if (a.hasValue(R.styleable.MaterialSearchView_search_scrimColor)) {
            setScrimColor(
                a.getInt(
                    R.styleable.MaterialSearchView_search_scrimColor,
                    0
                )
            )
        }

        if (a.hasValue(R.styleable.MaterialSearchView_android_hint)) {
            val hint = a.getString(R.styleable.MaterialSearchView_android_hint)
            setHint(hint)
        }

        val imeOptions = a.getInt(R.styleable.MaterialSearchView_android_imeOptions, -1)
        if (imeOptions != -1) {
            setImeOptions(imeOptions)
        }

        val inputType = a.getInt(R.styleable.MaterialSearchView_android_inputType, -1)
        if (inputType != -1) {
            setInputType(inputType)
        }

        a.recycle()

        visibility = View.GONE
    }

    // *********************************************************************************************
    override fun setNavigationIcon(@DrawableRes resId: Int) {
        binding.searchViewToolbar.setNavigationIcon(resId)
    }

    override fun setNavigationIcon(@Nullable drawable: Drawable?) {
        binding.searchViewToolbar.navigationIcon = drawable
    }

    override fun setNavigationContentDescription(@StringRes resId: Int) {
        binding.searchViewToolbar.setNavigationContentDescription(resId)
    }

    override fun setNavigationContentDescription(@Nullable description: CharSequence?) {
        binding.searchViewToolbar.navigationContentDescription = description
    }

    override fun setNavigationOnClickListener(listener: OnClickListener) {
        binding.searchViewToolbar.setNavigationOnClickListener(listener)
    }

    // *********************************************************************************************
    override fun setBackgroundColor(@ColorInt color: Int) {
        binding.searchViewBackground.setBackgroundColor(color)
    }

    override fun addView(child: View?) {
        binding.searchViewContentContainer.addView(child)
    }

    override fun requestFocus(direction: Int, previouslyFocusedRect: Rect?): Boolean {
        return binding.searchViewEditText.requestFocus(direction, previouslyFocusedRect)
    }

    override fun clearFocus() {
        binding.searchViewEditText.clearFocus()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState: Parcelable? = super.onSaveInstanceState()
        superState?.let {
            val state = SavedState(it)
            state.text = getTextQuery().toString()
            state.focus = binding.searchViewEditText.hasFocus()
            return state
        } ?: run {
            return superState
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            setTextQuery(state.text, false)
            if (state.focus) {
                binding.searchViewEditText.requestFocus()
            }
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    // *********************************************************************************************
    // TODO ANOTACE, NAZVY PROMENNCYH, @Nullable
    fun setClearIcon(@Nullable drawable: Drawable?) {
        binding.searchViewClearButton.setImageDrawable(drawable)
    }

    fun setImeOptions(imeOptions: Int) {
        binding.searchViewEditText.imeOptions = imeOptions
    }

    fun setInputType(type: Int) {
        binding.searchViewEditText.inputType = type
    }

    fun setTextClearOnBackPressed(clear: Boolean) {
        binding.searchViewEditText.setTextClearOnBackPressed(clear)
    }

    @Nullable
    fun getTextQuery(): Editable? {
        return binding.searchViewEditText.text
    }

    fun setTextQuery(@Nullable query: CharSequence?, submit: Boolean) {
        binding.searchViewEditText.setText(query)

        if (query != null) {
            binding.searchViewEditText.setSelection(binding.searchViewEditText.length())
        }

        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery()
        }
    }

    private fun onSubmitQuery() {
        val query = binding.searchViewEditText.text
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            if (queryListener == null || !queryListener!!.onQueryTextSubmit(query.toString())) {
                hideKeyboard()
            }
        }
    }

    fun setTextTypeface(@Nullable typeface: Typeface?) {
        binding.searchViewEditText.typeface = typeface
    }

    @Nullable
    fun getTextTypeface(): Typeface? {
        return binding.searchViewEditText.typeface
    }

    fun setHint(@Nullable hint: CharSequence?) {
        binding.searchViewEditText.hint = hint
    }

    fun setOnFocusChangeListener(@Nullable listener: OnFocusChangeListener?) {
        focusListener = listener
    }

    fun setOnQueryTextListener(@Nullable listener: OnQueryTextListener?) {
        queryListener = listener
    }

    fun showKeyboard() {
        if (!isInEditMode) {
            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(
                binding.searchViewEditText,
                InputMethodManager.RESULT_UNCHANGED_SHOWN
            )
        }
    }

    fun hideKeyboard() {
        if (!isInEditMode) {
            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                binding.searchViewEditText.windowToken,
                InputMethodManager.RESULT_UNCHANGED_SHOWN
            )
        }
    }

    private fun onTextChanged(s: CharSequence) {
        if (s.isNotEmpty()) {
            binding.searchViewClearButton.visibility = View.VISIBLE
        } else {
            binding.searchViewClearButton.visibility = View.GONE
        }

        queryListener?.onQueryTextChange(s)
    }

    fun setDividerColor(@ColorInt color: Int) {
        binding.searchViewDivider.setBackgroundColor(color)
    }

    fun setScrimColor(@ColorInt color: Int) {
        binding.searchViewScrim.setBackgroundColor(color)
    }


    fun setTextHintColor(@ColorInt color: Int) {
        binding.searchViewEditText.setHintTextColor(color)
    }

    fun setTextColor(@ColorInt color: Int) {
        binding.searchViewEditText.setTextColor(color)
    }

    // *********************************************************************************************
    interface OnFocusChangeListener {

        fun onFocusChange(hasFocus: Boolean)
    }

    interface OnQueryTextListener {

        fun onQueryTextChange(newText: CharSequence): Boolean

        fun onQueryTextSubmit(query: CharSequence): Boolean
    }

    interface OnClearClickListener {

        fun onClearClick()
    }

    // *********************************************************************************************
    @Suppress("unused")
    class SavedState : AbsSavedState {

        var text: String? = null
        var focus: Boolean = false

        constructor(superState: Parcelable) : super(superState)

        constructor(source: Parcel, loader: ClassLoader? = null) : super(source, loader) {
            text = source.readString()
            focus = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                source.readBoolean()
            } else {
                source.readInt() == 1
            }
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeString(text)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                dest.writeBoolean(focus)
            } else {
                dest.writeInt(if (focus) 1 else 0)
            }
        }

        companion object {

            @JvmField
            val CREATOR: Parcelable.ClassLoaderCreator<SavedState> = object :
                Parcelable.ClassLoaderCreator<SavedState> {

                override fun createFromParcel(source: Parcel, loader: ClassLoader): SavedState {
                    return SavedState(source, loader)
                }

                override fun createFromParcel(source: Parcel): SavedState {
                    return SavedState(source, null)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return newArray(size)
                }
            }
        }

    }

}