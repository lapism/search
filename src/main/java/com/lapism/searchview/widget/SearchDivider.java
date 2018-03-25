package com.lapism.searchview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lapism.searchview.R;


class SearchDivider extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = {android.R.attr.listDivider};
    private final Context mContext;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerWidth;

    public SearchDivider(Context context) {
        TypedArray a = context.obtainStyledAttributes(ATTRS);
        setDivider(a.getDrawable(0));
        a.recycle();

        mContext = context;
    }

    private static int getOrientation(RecyclerView parent) {
        RecyclerView.LayoutManager lm = parent.getLayoutManager();
        if (lm instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) lm).getOrientation();
        } else {
            throw new IllegalStateException("Use only with a LinearLayoutManager!");
        }
    }

    // todo  barva a sirka
    private void setDivider(Drawable divider) {
        mDivider = divider;
        mDividerHeight = divider == null ? 0 : divider.getIntrinsicHeight();
        mDividerWidth = divider == null ? 0 : divider.getIntrinsicWidth();
        mDividerWidth -= mContext.getResources().getDimensionPixelSize(R.dimen.search_icon_56);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mDivider == null) {
            super.getItemOffsets(outRect, view, parent, state);
            return;
        }

        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        boolean firstItem = position == 0;
        boolean dividerBefore = !firstItem;

        if (SearchDivider.getOrientation(parent) == LinearLayoutManager.VERTICAL) {
            outRect.top = dividerBefore ? this.mDividerHeight : 0;
            outRect.bottom = 0;
        } else {
            outRect.left = dividerBefore ? this.mDividerWidth : 0;
            outRect.right = 0;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (this.mDivider == null) {
            super.onDraw(c, parent, state);
            return;
        }

        int left = 0;
        int right = 0;
        int top = 0;
        int bottom = 0;

        int orientation = SearchDivider.getOrientation(parent);
        int childCount = parent.getChildCount();

        boolean vertical = orientation == LinearLayoutManager.VERTICAL;
        int size;
        if (vertical) {
            size = this.mDividerHeight;
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
        } else {
            size = this.mDividerWidth;
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
        }

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int position = params.getViewLayoutPosition();
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
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

}
