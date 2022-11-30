package com.lapism.search.internal

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import androidx.appcompat.widget.AppCompatEditText


class FocusEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    // *********************************************************************************************
    private var clear: Boolean = false
    private var clearFocus: Boolean = false

    // *********************************************************************************************
    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_UP && clear) {
            if (hasFocus()) {
                clearFocus()
                return true
            }
        }
        return super.onKeyPreIme(keyCode, event)
    }

    override fun clearFocus() {
        super.clearFocus()
        if (clearFocus) {
            text?.clear()
        }
    }

    // *********************************************************************************************
    fun setTextClearOnBackPressed(clear: Boolean) {
        this.clear = clear
    }

    fun setTextClearOnClearFocus(clearFocus: Boolean) {
        this.clearFocus = clearFocus
    }

}