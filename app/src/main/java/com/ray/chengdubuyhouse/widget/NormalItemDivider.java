package com.ray.chengdubuyhouse.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-08-09 10:49
 *  description : 
 */
public class NormalItemDivider extends DividerItemDecoration {

    private int mOrientation;
    private int mSize;

    public NormalItemDivider(Context context) {
        this(context, LinearLayoutManager.VERTICAL);
    }

    public NormalItemDivider(Context context, int orientation) {
        this(context, orientation, (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, context.getResources().getDisplayMetrics()) + 0.5f));
    }

    public NormalItemDivider(Context context, int orientation, int size) {
        super(context, orientation);
        mOrientation = LinearLayoutManager.VERTICAL;
        mSize = size;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.set(mSize, 0, (int) (mSize / 2f + 0.5f), 0);
            } else if (parent.getChildAdapterPosition(view) == parent.getLayoutManager().getItemCount() - 1) {
                outRect.set((int) (mSize / 2f + 0.5f), 0, mSize, 0);
            } else {
                outRect.set((int) (mSize / 2f + 0.5f), 0, (int) (mSize / 2f + 0.5f), 0);
            }
        } else {
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.set(0, mSize, 0, (int) (mSize / 2f + 0.5f));
            } else if (parent.getChildAdapterPosition(view) == parent.getLayoutManager().getItemCount() - 1) {
                outRect.set(0, (int) (mSize / 2f + 0.5f), 0, mSize);
            } else {
                outRect.set(0, (int) (mSize / 2f + 0.5f), 0, (int) (mSize / 2f + 0.5f));
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
    }
}
