package com.lapism.search.internal

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import android.widget.FrameLayout


class ClippableRoundedCornerLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    var path: Path? = null

    override fun dispatchDraw(canvas: Canvas?) {
        if (path == null) {
            super.dispatchDraw(canvas)
            return
        }
        val save = canvas?.save()
        canvas?.clipPath(path!!)
        super.dispatchDraw(canvas)
        canvas?.restoreToCount(save!!)
    }

}