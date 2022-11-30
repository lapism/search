package com.lapism.search.internal

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout


class ClippableRoundedCornerLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    // *********************************************************************************************
    private var radius: Float = 0.0f

    // *********************************************************************************************
    init {
        setWillNotDraw(false)
    }

    // *********************************************************************************************
    override fun dispatchDraw(canvas: Canvas?) {
        canvas?.let {
            val rect = RectF(0F, 0F, canvas.width.toFloat(), canvas.height.toFloat())
            val path = Path()
            path.addRoundRect(rect, radius, radius, Path.Direction.CW)

            val save = canvas.save()
            canvas.clipPath(path)
            super.dispatchDraw(canvas)
            canvas.restoreToCount(save)
        }
    }

    fun setRadius(radius: Float) {
        this.radius = radius
        invalidate()
    }

}