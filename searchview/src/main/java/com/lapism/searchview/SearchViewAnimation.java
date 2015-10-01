package com.lapism.searchview;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;


public class SearchViewAnimation {

    public static final int ANIMATION_DURATION_SHORT = 200;
    public static final int ANIMATION_DURATION_LONG = 800;

    public interface AnimationListener {
        boolean onAnimationStart(View view);

        boolean onAnimationEnd(View view);

        boolean onAnimationCancel(View view);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void revealIn(final View view, int animationDuration, final AnimationListener listener) {

        int cx = view.getRight();
        int cy = view.getBottom();

        int initialRadius = 0;
        int finalRadius = Math.max(view.getWidth(), view.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, finalRadius);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(animationDuration);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                listener.onAnimationStart(view);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                listener.onAnimationEnd(view);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                listener.onAnimationCancel(view);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        view.setVisibility(View.VISIBLE);
        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void revealOut(final View view, int animationDuration, final AnimationListener listener) {

        int cx = view.getRight();
        int cy = view.getBottom();

        int initialRadius = view.getWidth();
        int finalRadius = 0;

        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, finalRadius);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(animationDuration);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                listener.onAnimationStart(view);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                listener.onAnimationEnd(view);
                view.setVisibility(View.GONE); //INVISIBLE
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                listener.onAnimationCancel(view);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
    }

    public static void fadeInView(View view, int duration, final AnimationListener listener) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0f);
        ViewPropertyAnimatorListener vpListener = null;

        if (listener != null) {
            vpListener = new ViewPropertyAnimatorListener() {
                @Override
                public void onAnimationStart(View view) {
                    if (!listener.onAnimationStart(view)) {
                        view.setDrawingCacheEnabled(true);
                    }
                }

                @Override
                public void onAnimationEnd(View view) {
                    if (!listener.onAnimationEnd(view)) {
                        view.setDrawingCacheEnabled(false);
                    }
                }

                @Override
                public void onAnimationCancel(View view) {
                    /*if (!listener.onAnimationCancel(view)) {
                    }*/
                }
            };
        }
        ViewCompat.animate(view).alpha(1f).setDuration(duration).setListener(vpListener);
    }

    public static void fadeOutView(View view, int duration, final AnimationListener listener) {
        ViewCompat.animate(view).alpha(0f).setDuration(duration).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                if (listener == null || !listener.onAnimationStart(view)) {
                    view.setDrawingCacheEnabled(true);
                }
            }

            @Override
            public void onAnimationEnd(View view) {
                if (listener == null || !listener.onAnimationEnd(view)) {
                    view.setVisibility(View.GONE);
                    //view.setAlpha(1f);
                    view.setDrawingCacheEnabled(false);
                }
            }

            @Override
            public void onAnimationCancel(View view) {
                /*if (listener == null || !listener.onAnimationCancel(view)) {
                }*/
            }
        });
    }

}