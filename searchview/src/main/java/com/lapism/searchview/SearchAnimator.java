package com.lapism.searchview;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;


class SearchAnimator {

    private final Context mContext;
    private final SearchEditText mEditText;
    private final SearchView mSearchView;
    private final SearchView.OnOpenCloseListener mOnOpenCloseListener;

    SearchAnimator(Context context, SearchEditText editText, SearchView searchView, SearchView.OnOpenCloseListener listener) {
        mContext = context;
        mEditText = editText;
        mSearchView = searchView;
        mOnOpenCloseListener = listener;
    }

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
    void revealOpen(View view, int duration) {

        int cx = view.getWidth() - mContext.getResources().getDimensionPixelSize(R.dimen.search_reveal);
        int cy = mContext.getResources().getDimensionPixelSize(R.dimen.search_height) / 2;

        if (cx != 0 && cy != 0) {
            float finalRadius = (float) Math.hypot(cx, cy);

            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0.0f, finalRadius);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setDuration(duration);
            anim.addListener(new Animator.AnimatorListener() { // new AnimatorListenerAdapter()
                @Override
                public void onAnimationStart(Animator animation) {
                    if (mOnOpenCloseListener != null) {
                        mOnOpenCloseListener.onOpen();
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (mEditText.length() > 0) {
                        mEditText.getText().clear();
                    }
                    mEditText.requestFocus();
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
    void revealClose(final View view, int duration) {

        int cx = view.getWidth() - mContext.getResources().getDimensionPixelSize(R.dimen.search_reveal);
        int cy = mContext.getResources().getDimensionPixelSize(R.dimen.search_height) / 2;

        if (cx != 0 && cy != 0) {
            float initialRadius = (float) Math.hypot(cx, cy);

            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0.0f);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setDuration(duration);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (mEditText.length() > 0) {
                        mEditText.getText().clear();
                    }
                    mEditText.clearFocus();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.GONE);
                    mSearchView.setVisibility(View.GONE);
                    if (mOnOpenCloseListener != null) {
                        mOnOpenCloseListener.onClose();
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

    void fadeOpen(View view, int duration) {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(duration);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (mOnOpenCloseListener != null) {
                    mOnOpenCloseListener.onOpen();
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mEditText.length() > 0) {
                    mEditText.getText().clear();
                }
                mEditText.requestFocus();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        view.setAnimation(anim);
        view.setVisibility(View.VISIBLE);
    }

    void fadeClose(final View view, int duration) {
        Animation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(duration);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (mEditText.length() > 0) {
                    mEditText.getText().clear();
                }
                mEditText.clearFocus();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
                mSearchView.setVisibility(View.GONE);
                if (mOnOpenCloseListener != null) {
                    mOnOpenCloseListener.onClose();
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
