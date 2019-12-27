package com.lapism.search.internal

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.Point
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import androidx.annotation.RestrictTo
import androidx.cardview.widget.CardView
import com.lapism.search.R
import com.lapism.search.SearchUtils
import kotlin.math.hypot
import kotlin.math.max

/**
 * @hide
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class SearchAnimation {

    // *********************************************************************************************
    private var mAnimationListener: OnAnimationListener? = null

    // *********************************************************************************************
    fun start(
        context: Context,
        linearLayout: LinearLayout?,
        cardView: CardView?,
        x: Int,
        duration: Long,
        back: Boolean
    ) {
        var cx = x
        if (cx <= 0) {
            val padding = context.resources.getDimensionPixelSize(R.dimen.search_reveal)
            cx = if (SearchUtils.isLayoutRtl(context)) {
                padding
            } else {
                // cardView?.width!! - padding TODO Test
                cardView?.measuredWidth!! - padding
            }
        }
        val cy = linearLayout?.height!! / 2

        if (cx != 0 && cy != 0) {
            val displaySize = Point()
            val radius = hypot(max(cx, displaySize.x - cx).toDouble(), cy.toDouble()).toFloat()

            val animator: Animator
            animator = if (back) {
                ViewAnimationUtils.createCircularReveal(cardView, cx, cy, 0.0f, radius)
            } else {
                ViewAnimationUtils.createCircularReveal(cardView, cx, cy, radius, 0.0f)
            }
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.duration = duration
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)
                    mAnimationListener?.onAnimationStart()
                }

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    mAnimationListener?.onAnimationEnd()
                }
            })
            animator.start()
        }
    }

    // *********************************************************************************************
    fun setOnAnimationListener(listener: OnAnimationListener) {
        mAnimationListener = listener
    }

    // *********************************************************************************************
    interface OnAnimationListener {

        fun onAnimationStart()

        fun onAnimationEnd()
    }

}
