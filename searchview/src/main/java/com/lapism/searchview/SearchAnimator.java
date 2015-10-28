package com.lapism.searchview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;


public class SearchAnimator {

    public static final int ANIMATION_DURATION = 360;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void revealInAnimation(final Context mContext, final View view, final int duration) {

        int cx = view.getWidth() - mContext.getResources().getDimensionPixelSize(R.dimen.reveal);
        int cy = view.getHeight() / 2;

        if (cx != 0 && cy != 0) {
            float initialRadius = 0.0f;
            float finalRadius = Math.max(view.getWidth(), view.getHeight());

            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, finalRadius);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setDuration(duration);
            view.setVisibility(View.VISIBLE);
            anim.start();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void revealOutAnimation(final Context mContext, final View view, final int duration) {

        int cx = view.getWidth() - mContext.getResources().getDimensionPixelSize(R.dimen.reveal);
        int cy = view.getHeight() / 2;

        if (cx != 0 && cy != 0) {
            float initialRadius = view.getWidth();
            float finalRadius = 0.0f;

            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, finalRadius);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setDuration(duration);
            anim.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.GONE);
                }
            });
            anim.start();
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void fadeInAnimation(final View view, int duration) {

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(duration);

        view.setAnimation(anim);
        view.setVisibility(View.VISIBLE);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void fadeOutAnimation(final View view, int duration) {

        Animation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(duration);

        view.setAnimation(anim);
        view.setVisibility(View.GONE);
    }

}