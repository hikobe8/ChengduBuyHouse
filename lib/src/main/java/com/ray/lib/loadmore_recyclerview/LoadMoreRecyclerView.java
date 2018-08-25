package com.ray.lib.loadmore_recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.ray.lib.BuildConfig;

/**
 * Author : hikobe8@github.com
 * Time : 2018/8/23 下午11:52
 * Description :
 */
public class LoadMoreRecyclerView extends RecyclerView{

    public LoadMoreRecyclerView(Context context) {
        this(context, null, 0);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    private OnLoadMoreListener mOnLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    private void init() {
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
                LoadMoreAdapter adapter = (LoadMoreAdapter) getAdapter();
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition == adapter.getNormalItemCount() && adapter.isCanLoadMore()) {
                    adapter.setFooterState(LoadingMoreType.TYPE_LOADING);
                    if (mOnLoadMoreListener != null) {
                        if (BuildConfig.DEBUG) {
                            Log.i("LoadMoreRecyclerView", "onLoadMore");
                        }
                        mOnLoadMoreListener.onLoadMore();
                    }
                }
            }
        });
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (!(adapter instanceof LoadMoreAdapter)) {
            throw new RuntimeException("only support LoadMoreAdapter");
        }
        super.setAdapter(adapter);
    }


}
