package com.lapism.search.internal

import android.animation.ObjectAnimator
import android.content.Context
import android.util.Property
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.RestrictTo
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.core.content.ContextCompat

/**
 * @hide
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class SearchArrowDrawable internal constructor(context: Context) :
    DrawerArrowDrawable(context) {

    // *********************************************************************************************
    var position: Float
        get() = progress
        set(position) {
            if (position == STATE_ARROW) {
                setVerticalMirror(true)
            } else if (position == STATE_HAMBURGER) {
                setVerticalMirror(false)
            }
            progress = position
        }

    // *********************************************************************************************
    init {
        color = ContextCompat.getColor(context, android.R.color.black)
    }

    // *********************************************************************************************
    fun animate(state: Float, duration: Long) {
        val anim: ObjectAnimator = if (state == STATE_ARROW) {
            ObjectAnimator.ofFloat(
                this,
                PROGRESS,
                STATE_HAMBURGER,
                state
            )
        } else {
            ObjectAnimator.ofFloat(
                this,
                PROGRESS,
                STATE_ARROW,
                state
            )
        }
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.duration = duration
        anim.start()
    }

    // *********************************************************************************************
    companion object {

        const val STATE_HAMBURGER = 0.0f
        const val STATE_ARROW = 1.0f

        private val PROGRESS =
            object : Property<SearchArrowDrawable, Float>(Float::class.java, "progress") {
                override fun set(obj: SearchArrowDrawable, value: Float?) {
                    obj.progress = value!!
                }

                override fun get(obj: SearchArrowDrawable): Float {
                    return obj.progress
                }
            }
    }

}
