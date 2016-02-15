package com.lapism.searchview.view;

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

import com.lapism.searchview.R;


class SearchAnimator {
    // TODO FOCUS ,PERMISSION, FIX EDIT TEXT PROPERTIES
    // TODO fix out animation
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void revealInAnimation(final Context mContext, final View animatedView, final int startCy, int duration) {

        int cx = animatedView.getWidth() - mContext.getResources().getDimensionPixelSize(R.dimen.search_key_line);
        int cy = startCy == -1 ? animatedView.getHeight() / 2 : startCy;

        if (cx != 0 && cy != 0) {
            float initialRadius = 0.0f;
            float finalRadius = Math.max(animatedView.getWidth(), animatedView.getHeight());

            Animator anim = ViewAnimationUtils.createCircularReveal(animatedView, cx, cy, initialRadius, finalRadius);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setDuration(duration);
            animatedView.setVisibility(View.VISIBLE);
            anim.start();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void revealOutAnimation(final Context mContext, final View animatedView, final int endCy, int duration) {

        int cx = animatedView.getWidth() - mContext.getResources().getDimensionPixelSize(R.dimen.search_key_line);
        int cy = endCy == -1 ? animatedView.getHeight() / 2 : endCy;

        if (cx != 0 && cy != 0) {
            float initialRadius = animatedView.getWidth();
            float finalRadius = 0.0f;

            Animator anim = ViewAnimationUtils.createCircularReveal(animatedView, cx, cy, initialRadius, finalRadius);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setDuration(duration);
            anim.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animatedView.setVisibility(View.GONE);
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