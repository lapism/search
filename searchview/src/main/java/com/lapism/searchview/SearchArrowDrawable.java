package com.lapism.searchview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.util.Property;
import android.view.animation.AccelerateDecelerateInterpolator;


class SearchArrowDrawable extends DrawerArrowDrawable {

    static final float STATE_ARROW = 0.0f;
    static final float STATE_HAMBURGER = 1.0f;
    private static final Property<SearchArrowDrawable, Float> PROGRESS = new Property<SearchArrowDrawable, Float>(Float.class, "progress") {
        @Override
        public void set(SearchArrowDrawable object, Float value) {
            object.setProgress(value);
        }

        @Override
        public Float get(SearchArrowDrawable object) {
            return object.getProgress();
        }
    };

    SearchArrowDrawable(Context context) {
        super(context);
        // mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    void animate(float state, int duration) {
        ObjectAnimator anim;
        if (state == STATE_ARROW) {
            anim = ObjectAnimator.ofFloat(this, PROGRESS, state, STATE_HAMBURGER);
        } else {
            anim = ObjectAnimator.ofFloat(this, PROGRESS, state, STATE_ARROW);
        }
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(duration);
        anim.start();
    }

}