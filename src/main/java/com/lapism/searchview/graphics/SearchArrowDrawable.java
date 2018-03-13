package com.lapism.searchview.graphics;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.util.Property;
import android.view.animation.AccelerateDecelerateInterpolator;


public class SearchArrowDrawable extends DrawerArrowDrawable {

    public static final float STATE_HAMBURGER = 0.0f;
    public static final float STATE_ARROW = 1.0f;

    private static final Property<SearchArrowDrawable, Float> PROGRESS = new Property<SearchArrowDrawable, Float>(Float.class, "progress") {
        @Override
        public void set(SearchArrowDrawable object, Float value) {
            object.setProgress(value);
        }

        @NonNull
        @Override
        public Float get(SearchArrowDrawable object) {
            return object.getProgress();
        }
    };

    public SearchArrowDrawable(Context context) {
        super(context);
        setColor(ContextCompat.getColor(context, android.R.color.black));
    }

    public void animate(float state, long duration) {
        ObjectAnimator anim;
        if (state == STATE_ARROW) {
            anim = ObjectAnimator.ofFloat(this, PROGRESS, STATE_HAMBURGER, state);
        } else {
            anim = ObjectAnimator.ofFloat(this, PROGRESS, STATE_ARROW, state);
        }
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(duration);
        anim.start();
    }

    public float getPosition() {
        return super.getProgress();
    }

    public void setPosition(float position) {
        if (position == 1f) {
            setVerticalMirror(true);
        } else if (position == 0f) {
            setVerticalMirror(false);
        }
        super.setProgress(position);
    }

}
