package com.lapism.search.widget

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.customview.view.AbsSavedState
import androidx.transition.*
import com.google.android.material.appbar.MaterialToolbar
import com.lapism.search.R
import com.lapism.search.internal.FocusEditText


@Suppress("MemberVisibilityCanBePrivate")
class MaterialSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : MaterialSearchLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var scrim: View? = null
    private var background: LinearLayout? = null
    private var toolbar: MaterialToolbar? = null
    private var editText: FocusEditText? = null
    private var clear: ImageButton? = null
    private var divider: View? = null
    private var container: FrameLayout? = null

    private var focusListener: OnFocusChangeListener? = null
    private var queryListener: OnQueryTextListener? = null
    private var clearClickListener: OnClearClickListener? = null

    init {
        View.inflate(context, R.layout.material_search_view, this)

        scrim = findViewById(R.id.search_view_scrim)

        background = findViewById(R.id.search_view_background)

        toolbar = findViewById(R.id.search_view_toolbar)

        editText = findViewById(R.id.search_view_edit_text)
        editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                this@MaterialSearchView.onTextChanged(s)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        editText?.setOnEditorActionListener { _, _, _ ->
            onSubmitQuery()
            return@setOnEditorActionListener true // true
        }
        editText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showAnimation()
                showKeyboard()
            } else {
                hideKeyboard()
                hideAnimation()
            }

            focusListener?.onFocusChange(hasFocus)
        }

        val left = context.resources.getDimensionPixelSize(R.dimen.search_dp_8)
        val params = editText?.layoutParams as? LinearLayout.LayoutParams
        params?.setMargins(left, 0, 0, 0)
        editText?.layoutParams = params
        editText?.isFocusable = true
        editText?.isFocusableInTouchMode = true

        clear = findViewById(R.id.search_view_clear_button)
        clear?.visibility = View.GONE
        clear?.setOnClickListener {
            clearClickListener?.onClearClick()
            editText?.text?.clear()
        }

        divider = findViewById(R.id.search_view_divider)
        container = findViewById(R.id.search_view_content_container)

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
    }

    // TODO ANOTACE A NAZVY PROMENNCYH + @Nullable
    override fun addView(child: View?) {
        container?.addView(child)
    }

    fun setTextQuery(query: CharSequence?, submit: Boolean) {
        editText?.setText(query)
        if (query != null) {
            editText?.setSelection(editText?.length()!!)
        }
        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery()
        }
    }

    private fun onSubmitQuery() {
        val query = editText?.text
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            if (queryListener == null || !queryListener!!.onQueryTextSubmit(query.toString())) {
                editText?.text = query
            }
        }
    }

    override fun setNavigationOnClickListener(listener: OnClickListener) {
        toolbar?.setNavigationOnClickListener(listener)
    }

    override fun setNavigationIcon(drawable: Drawable?) {
        toolbar?.navigationIcon = drawable
    }

    override fun setNavigationIcon(resId: Int) {
        toolbar?.setNavigationIcon(resId)
    }

    fun setClearIcon(drawable: Drawable?) {
        clear?.setImageDrawable(drawable)
    }

    fun setImeOptions(imeOptions: Int) {
        editText?.imeOptions = imeOptions
    }

    fun setInputType(type: Int) {
        editText?.inputType = type
    }

    fun setTextClearOnBackPressed(clear: Boolean) {
        editText?.setTextClearOnBackPressed(clear)
    }

    fun getTextQuery(): Editable? {
        return editText?.text
    }

    fun setHint(hint: CharSequence?) {
        editText?.hint = hint
    }

    fun setOnFocusChangeListener(listener: OnFocusChangeListener) {
        focusListener = listener
    }

    fun setOnQueryTextListener(listener: OnQueryTextListener) {
        queryListener = listener
    }

    private fun showAnimation() {
        // TODO
    }

    private fun hideAnimation() {
        // TODO
    }

    fun showKeyboard() {
        if (!isInEditMode) {
            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(
                editText,
                InputMethodManager.RESULT_UNCHANGED_SHOWN
            )
        }
    }

    fun hideKeyboard() {
        if (!isInEditMode) {
            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                editText?.windowToken,
                InputMethodManager.RESULT_UNCHANGED_SHOWN
            )
        }
    }

    private fun onTextChanged(s: CharSequence) {
        if (s.isNotEmpty()) {
            clear?.visibility = View.VISIBLE
        } else {
            clear?.visibility = View.GONE
        }

        queryListener?.onQueryTextChange(s)
    }

    override fun setBackgroundColor(@ColorInt color: Int) {
        background?.setBackgroundColor(color)
    }

    fun setDividerColor(@ColorInt color: Int) {
        divider?.setBackgroundColor(color)
    }

    fun setScrimColor(@ColorInt color: Int) {
        scrim?.setBackgroundColor(color)
    }

    override fun requestFocus(direction: Int, previouslyFocusedRect: Rect?): Boolean {
        return editText?.requestFocus(direction, previouslyFocusedRect)!!
    }

    override fun clearFocus() {
        editText?.clearFocus()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState: Parcelable? = super.onSaveInstanceState()
        superState?.let {
            val state = SavedState(it)
            state.text = getTextQuery().toString()
            return state
        } ?: run {
            return superState
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            setTextQuery(state.text, false)
        } else {
            super.onRestoreInstanceState(state)
        }
    }

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

    @Suppress("unused")
    class SavedState : AbsSavedState {

        var text: String? = null

        constructor(superState: Parcelable) : super(superState)

        constructor(source: Parcel, loader: ClassLoader? = null) : super(source, loader) {
            text = source.readString()
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeString(text)
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