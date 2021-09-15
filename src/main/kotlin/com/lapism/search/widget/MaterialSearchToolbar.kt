package com.lapism.search.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.accessibility.AccessibilityNodeInfo
import androidx.annotation.AttrRes
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView
import com.lapism.search.R


class MaterialSearchToolbar : MaterialToolbar {

    private var textView: MaterialTextView? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        View.inflate(context, R.layout.material_search_toolbar, this)

        textView = findViewById(R.id.search_toolbar_text_view)
    }

    override fun setTitle(title: CharSequence?) {

    }

    override fun setTitle(resId: Int) {

    }

    override fun setSubtitle(subtitle: CharSequence?) {

    }

    override fun setSubtitle(resId: Int) {

    }

    override fun setElevation(elevation: Float) {

    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo?) {
        super.onInitializeAccessibilityNodeInfo(info)
        info?.className = MaterialTextView::class.java.canonicalName
        var text: CharSequence? = getText()
        val isEmpty = TextUtils.isEmpty(text)
        info?.hintText = getHint()
        info?.isShowingHintText = isEmpty
        if (isEmpty) {
            text = getHint()
        }
        info?.text = text
    }

    fun setText(text: CharSequence?) {
        textView?.text = text
    }

    private fun getText(): CharSequence? {
        return textView?.text
    }

    private fun getHint(): CharSequence? {
        return textView?.hint
    }

    fun setHint(hint: CharSequence?) {
        textView?.hint = hint
    }

}