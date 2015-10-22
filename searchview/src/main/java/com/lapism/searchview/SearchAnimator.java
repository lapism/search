package com.lapism.searchview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;


public class SearchAnimator {

    public static final int ANIMATION_DURATION = 350;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void revealInAnimation(final View view, final int duration) {

        int cx = view.getWidth() - dpToPx(24);
        int cy = view.getHeight() / 2;

        if (cx != 0 && cy != 0) {
            int initialRadius = 0;
            int finalRadius = Math.max(view.getWidth(), view.getHeight());

            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, finalRadius);
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(duration);
            view.setVisibility(View.VISIBLE);
            anim.start();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void revealOutAnimation(final View view, final int duration) {

        int cx = view.getWidth() - dpToPx(24);
        int cy = view.getHeight() / 2;

        if (cx != 0 && cy != 0) {
            int initialRadius = view.getWidth();
            int finalRadius = 0;

            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, finalRadius);
            anim.setInterpolator(new AccelerateInterpolator());
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
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(duration);

        view.setAnimation(anim);
        view.setVisibility(View.VISIBLE);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void fadeOutAnimation(final View view, int duration) {

        Animation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration(duration);

        view.setAnimation(anim);
        view.setVisibility(View.GONE);
    }

    public static int dpToPx(final float dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private static int getDp(final float dp) {
        //int dp = (int) (getResources().getDimension(R.dimen.test)
        //getResources().getDisplayMetrics().density) 48dp
        //int valueInPixels = (int) getResources().getDimension(R.dimen.test)  96px
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
        //setTextSize( TypedValue.COMPLEX_UNIT_PX, getDimensionPixelSize( R.dimen.tag_text_size ) );
        //tagButton.setTextSize(c.getResources().getDimensionPixelSize(R.dimen.tag_text_size));
        //textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_medium))
    }

}