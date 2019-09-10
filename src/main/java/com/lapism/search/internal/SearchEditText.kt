package com.lapism.search.internal

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import androidx.annotation.AttrRes
import androidx.annotation.RestrictTo
import androidx.appcompat.widget.AppCompatEditText

/**
 * @hide
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
class SearchEditText : AppCompatEditText {

    // *********************************************************************************************
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    // *********************************************************************************************
    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
            if (hasFocus()) {
                clearFocus()
                return true
            }
        }
        return super.onKeyPreIme(keyCode, event)
    }

    override fun clearFocus() {
        text?.clear()
        super.clearFocus()
    }

}
