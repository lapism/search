package com.lapism.searchview.graphics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.lapism.searchview.R;
import com.lapism.searchview.Search;
import com.lapism.searchview.widget.SearchEditText;
import com.lapism.searchview.widget.SearchView;


public class SearchAnimator {

    public static void revealOpen(@NonNull Context context, @NonNull final CardView cardView, int cx, long duration, @NonNull final SearchEditText editText, @Nullable final Search.OnOpenCloseListener listener) {
        if (cx <= 0) {
            int padding = context.getResources().getDimensionPixelSize(R.dimen.search_reveal);
            if (isRtlLayout(context)) {
                cx = padding;
            } else {
                cx = cardView.getWidth() - padding;
            }
        }

        int cy = context.getResources().getDimensionPixelSize(R.dimen.search_height_view) / 2;

        if (cx != 0 && cy != 0) {
            Point displaySize = new Point();
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                windowManager.getDefaultDisplay().getSize(displaySize);
            }
            float finalRadius = (float) Math.hypot(Math.max(cx, displaySize.x - cx), cy);

            Animator anim = ViewAnimationUtils.createCircularReveal(cardView, cx, cy, 0.0f, finalRadius);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setDuration(duration);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    editText.requestFocus();
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    cardView.setVisibility(View.VISIBLE);
                    if (listener != null) {
                        listener.onOpen();
                    }
                }
            });
            anim.start();
        }
    }

    public static void revealClose(@NonNull Context context, @NonNull final CardView cardView, int cx, long duration, @NonNull final SearchEditText editText, @NonNull final SearchView searchView, @Nullable final Search.OnOpenCloseListener listener) {
        if (cx <= 0) {
            int padding = context.getResources().getDimensionPixelSize(R.dimen.search_reveal);
            if (isRtlLayout(context)) {
                cx = padding;
            } else {
                cx = cardView.getWidth() - padding;
            }
        }

        int cy = context.getResources().getDimensionPixelSize(R.dimen.search_height_view) / 2;

        if (cx != 0 && cy != 0) {
            Point displaySize = new Point();
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                windowManager.getDefaultDisplay().getSize(displaySize);
            }
            float initialRadius = (float) Math.hypot(Math.max(cx, displaySize.x - cx), cy);

            Animator anim = ViewAnimationUtils.createCircularReveal(cardView, cx, cy, initialRadius, 0.0f);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setDuration(duration);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    cardView.setVisibility(View.GONE);
                    searchView.setVisibility(View.GONE);
                    if (listener != null) {
                        listener.onClose();
                    }
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    editText.clearFocus();
                }
            });
            anim.start();
        }
    }

    public static void fadeOpen(@NonNull final View view, long duration) {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(duration);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(anim);
    }

    public static void fadeClose(@NonNull final View view, long duration) {
        Animation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(duration);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(anim);
    }

    private static boolean isRtlLayout(@NonNull Context context) {
        return context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

}
