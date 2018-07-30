package com.ray.chengdubuyhouse.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ray.chengdubuyhouse.R;

/**
 * Author : hikobe8@github.com
 * Time : 2018/7/30 上午12:41
 * Description :
 */
public class BannerItemDecoration extends DividerItemDecoration {

    private int mDividerSize;

    public BannerItemDecoration(Context context) {
        super(context, HORIZONTAL);
        mDividerSize = context.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int itemCount = parent.getLayoutManager().getItemCount();
        if (position == 0) {
            outRect.set(mDividerSize,0, mDividerSize, 0);
        } else if (position == itemCount - 1) {
            outRect.set(0 ,0, mDividerSize, 0);
        } else {
            outRect.set(0,0, mDividerSize, 0);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
    }
}
