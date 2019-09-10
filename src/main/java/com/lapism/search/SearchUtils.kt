package com.lapism.search

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.annotation.IntDef


object SearchUtils {

    // *********************************************************************************************
    private const val SPEECH_REQUEST_CODE = 100

    // *********************************************************************************************
    @IntDef(
        NavigationIconSupport.HAMBURGER,
        NavigationIconSupport.ARROW,
        NavigationIconSupport.ANIMATION
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class NavigationIconSupport {
        companion object {
            const val HAMBURGER = 100
            const val ARROW = 101
            const val ANIMATION = 102
        }
    }

    @IntDef(
        Margins.NONE_TOOLBAR,
        Margins.NONE_MENU_ITEM,
        Margins.TOOLBAR,
        Margins.MENU_ITEM
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class Margins {
        companion object {
            const val NONE_TOOLBAR = 300
            const val NONE_MENU_ITEM = 301
            const val TOOLBAR = 302
            const val MENU_ITEM = 303
        }
    }

    // *********************************************************************************************
    @JvmStatic
    fun setVoiceSearch(activity: Activity, text: String) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, text)
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)

        activity.startActivityForResult(intent, SPEECH_REQUEST_CODE)
    }

    @JvmStatic
    fun isVoiceSearchAvailable(context: Context): Boolean {
        val pm = context.packageManager
        val activities =
            pm.queryIntentActivities(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0)
        return activities.size != 0
    }

    @JvmStatic
    fun fadeAddFocus(view: View?, duration: Long) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.duration = duration
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                view?.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation) {

            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        view?.startAnimation(anim)
    }

    @JvmStatic
    fun fadeRemoveFocus(view: View?, duration: Long) {
        val anim = AlphaAnimation(1.0f, 0.0f)
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.duration = duration
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                view?.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        view?.startAnimation(anim)
    }

}
