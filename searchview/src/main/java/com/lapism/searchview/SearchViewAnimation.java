package com.lapism.searchview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;


public class SearchViewAnimation {

    public static final int ANIMATION_DURATION = 380;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void revealInAnimation(final View view, int duration) {

        int cx = view.getWidth() - dpToPx(24);
        int cy = view.getHeight() / 2;
        //int cx2 = view.getWidth() - (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());

        int initialRadius = 0;
        int finalRadius = Math.max(view.getWidth(), view.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, finalRadius);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(duration);
        view.setVisibility(View.VISIBLE);
        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void revealOutAnimation(final View view, int duration) {

        int cx = view.getWidth() - dpToPx(24);
        int cy = view.getHeight() / 2;

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

        //view.animate().alpha(1.0f).setDuration(duration);
        //ViewCompat.animate(view).alpha(1.0f).setDuration(duration);
       /* view.animate()
                .alpha(0.0f)
                .setDuration(duration)
                .setListener(new ViewProperty...AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                        //view.setDrawingCacheEnabled(false);
                    }
                });
*/
        Animation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration(duration);

        view.setAnimation(anim);
        view.setVisibility(View.GONE);
    }

    public static int dpToPx(final float dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPxContext(final Context context, final float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (dp * metrics.density);
    }

    public static int pxToDp(final float px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDpContext(final Context context, final float px) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (px / metrics.density);
    }

    // dp = (width in pixels * 160) /  screen density ,return Math.round((float)dp * density);
}