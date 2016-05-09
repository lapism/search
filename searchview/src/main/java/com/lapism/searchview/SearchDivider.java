package com.lapism.searchview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


class SearchDivider extends RecyclerView.ItemDecoration {

    private Drawable divider;
    private int dividerHeight;
    private int dividerWidth;

    public SearchDivider(Context context) {
        final TypedArray a = context.obtainStyledAttributes(null, new int[]{android.R.attr.listDivider});
        setDivider(a.getDrawable(0));
        a.recycle();
    }

    private void setDivider(Drawable divider) {
        this.divider = divider;
        this.dividerHeight = divider == null ? 0 : divider.getIntrinsicHeight();
        this.dividerWidth = divider == null ? 0 : divider.getIntrinsicWidth();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (divider == null) {
            super.getItemOffsets(outRect, view, parent, state);
            return;
        }

        final int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        final boolean firstItem = position == 0;
        final boolean dividerBefore = !firstItem;

        if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
            outRect.top = dividerBefore ? dividerHeight : 0;
            outRect.bottom = 0;
        } else {
            outRect.left = dividerBefore ? dividerWidth : 0;
            outRect.right = 0;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (divider == null) {
            super.onDraw(c, parent, state);
            return;
        }

        int left = 0;
        int right = 0;
        int top = 0;
        int bottom = 0;

        final int orientation = getOrientation(parent);
        final int childCount = parent.getChildCount();

        final boolean vertical = orientation == LinearLayoutManager.VERTICAL;
        final int size;
        if (vertical) {
            size = dividerHeight;
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
        } else {
            size = dividerWidth;
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
        }

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int position = params.getViewLayoutPosition();
            if (position == 0) {
                continue;
            }
            if (vertical) {
                top = child.getTop() - params.topMargin - size;
                bottom = top + size;
            } else {
                left = child.getLeft() - params.leftMargin - size;
                right = left + size;
            }
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

    private int getOrientation(RecyclerView parent) {
        final RecyclerView.LayoutManager lm = parent.getLayoutManager();
        if (lm instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) lm).getOrientation();
        } else {
            throw new IllegalStateException("Use only with a LinearLayoutManager!");
        }
    }

}