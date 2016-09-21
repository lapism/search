package com.lapism.searchview;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;


class SearchAnimator {

    static void fadeIn(View view, int duration) {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(duration);

        view.setAnimation(anim);
        view.setVisibility(View.VISIBLE);
    }

    static void fadeOut(View view, int duration) {
        Animation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(duration);

        view.setAnimation(anim);
        view.setVisibility(View.GONE);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    static void revealOpen(View view, int cx, int duration, Context context, final SearchEditText editText, final boolean shouldClearOnOpen, final SearchView.OnOpenCloseListener listener) {

        if (cx <= 0) {
            int padding = context.getResources().getDimensionPixelSize(R.dimen.search_reveal);
            if (SearchUtils.isRtlLayout(context))
                cx = padding;
            else
                cx = view.getWidth() - padding;
        }

        int cy = context.getResources().getDimensionPixelSize(R.dimen.search_height) / 2;

        if (cx != 0 && cy != 0) {
            Point displaySize = new Point();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(displaySize);
            float finalRadius = (float) Math.hypot(Math.max(cx, displaySize.x - cx), cy);

            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0.0f, finalRadius);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setDuration(duration);
            anim.addListener(new Animator.AnimatorListener() { // new AnimatorListenerAdapter()
                @Override
                public void onAnimationStart(Animator animation) {
                    if (listener != null) {
                        listener.onOpen();
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (shouldClearOnOpen && editText.length() > 0) {
                        editText.getText().clear();
                    }
                    editText.requestFocus();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            view.setVisibility(View.VISIBLE);
            anim.start();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    static void revealClose(final View view, int cx, int duration, Context context, final SearchEditText editText, final boolean shouldClearOnClose, final SearchView searchView, final SearchView.OnOpenCloseListener listener) {

        if (cx <= 0) {
            int padding = context.getResources().getDimensionPixelSize(R.dimen.search_reveal);
            if (SearchUtils.isRtlLayout(context))
                cx = padding;
            else
                cx = view.getWidth() - padding;
        }

        int cy = context.getResources().getDimensionPixelSize(R.dimen.search_height) / 2;

        if (cx != 0 && cy != 0) {
            Point displaySize = new Point();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(displaySize);
            float initialRadius = (float) Math.hypot(Math.max(cx, displaySize.x - cx), cy);

            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0.0f);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setDuration(duration);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (shouldClearOnClose && editText.length() > 0) {
                        editText.getText().clear();
                    }
                    editText.clearFocus();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.GONE);
                    searchView.setVisibility(View.GONE);
                    if (listener != null) {
                        listener.onClose();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            anim.start();
        }
    }

    static void fadeOpen(View view, int duration, final SearchEditText editText, final boolean shouldClearOnOpen, final SearchView.OnOpenCloseListener listener) {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(duration);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (listener != null) {
                    listener.onOpen();
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (shouldClearOnOpen && editText.length() > 0) {
                    editText.getText().clear();
                }
                editText.requestFocus();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        view.setAnimation(anim);
        view.setVisibility(View.VISIBLE);
    }

    static void fadeClose(final View view, int duration, final SearchEditText editText, final boolean shouldClearOnClose, final SearchView searchView, final SearchView.OnOpenCloseListener listener) {
        Animation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(duration);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (shouldClearOnClose && editText.length() > 0) {
                    editText.getText().clear();
                }
                editText.clearFocus();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
                searchView.setVisibility(View.GONE);
                if (listener != null) {
                    listener.onClose();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        view.setAnimation(anim);
        view.setVisibility(View.GONE);
    }

}
