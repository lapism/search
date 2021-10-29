package com.lapism.search.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.TextView
import androidx.annotation.Nullable
import com.google.android.material.appbar.MaterialToolbar
import com.lapism.search.databinding.MaterialSearchToolbarBinding


class MaterialSearchToolbar : MaterialToolbar {

    // *********************************************************************************************
    private var binding: MaterialSearchToolbarBinding

    // *********************************************************************************************
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    // *********************************************************************************************
    init {
        val inflater = LayoutInflater.from(context)
        binding = MaterialSearchToolbarBinding.inflate(inflater, this, true)
    }

    // *********************************************************************************************
    override fun setTitle(resId: Int) {

    }

    override fun setTitle(title: CharSequence?) {

    }

    override fun setSubtitle(resId: Int) {

    }

    override fun setSubtitle(subtitle: CharSequence?) {

    }

    override fun setElevation(elevation: Float) {

    }

    override fun setNavigationIcon(drawable: Drawable?) {
        // Fix for Navigation Library
        if (drawable != null) {
            super.setNavigationIcon(drawable)
        }
    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)
        info.className = TextView::class.java.canonicalName
        var text: CharSequence? = getText()
        val isEmpty = TextUtils.isEmpty(text)
        info.hintText = getHint()
        info.isShowingHintText = isEmpty
        if (isEmpty) {
            text = getHint()
        }
        info.text = text
    }

    // *********************************************************************************************
    fun setText(@Nullable text: CharSequence?) {
        binding.searchToolbarTextView.text = text
    }

    @Nullable
    private fun getText(): CharSequence? {
        return binding.searchToolbarTextView.text
    }

    fun setHint(@Nullable hint: CharSequence?) {
        binding.searchToolbarTextView.hint = hint
    }

    @Nullable
    private fun getHint(): CharSequence? {
        return binding.searchToolbarTextView.hint
    }

}