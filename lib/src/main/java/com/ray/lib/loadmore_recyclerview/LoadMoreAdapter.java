package com.ray.lib.loadmore_recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ray.lib.R;

/**
 * Author : hikobe8@github.com
 * Time : 2018/8/22 下午11:03
 * Description :
 */
public abstract class LoadMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_FOOTER = 1;

    private LoadingHolder mLoadingHolder;

    private boolean mCanLoadMore = true;

    public boolean isCanLoadMore() {
        return mCanLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        mCanLoadMore = canLoadMore;
    }

    public interface OnLoadInErrorStateListener{
        void onLoadInErrorState();
    }

    private OnLoadInErrorStateListener mOnLoadInErrorStateListener;

    public void setOnLoadInErrorStateListener(OnLoadInErrorStateListener onLoadInErrorStateListener) {
        mOnLoadInErrorStateListener = onLoadInErrorStateListener;
    }

    public void setFooterState(int footerState) {
        if (mLoadingHolder != null) {
            mLoadingHolder.switchState(footerState);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL) {
            return onCreateNormalViewHolder(parent);
        }
        mLoadingHolder = new LoadingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_loading_item, parent, false));
        return mLoadingHolder;
    }

    protected abstract RecyclerView.ViewHolder onCreateNormalViewHolder(@NonNull ViewGroup parent);

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position < getNormalItemCount()) {
            onBindNormalViewHolder(holder, position);
        } else {
            if (holder instanceof LoadingHolder) {
                LoadingHolder loadingHolder = (LoadingHolder) holder;
                loadingHolder.setOnLoadInErrorStateListener(mOnLoadInErrorStateListener);
            }
        }
    }

    protected abstract void onBindNormalViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return getNormalItemCount() == 0 ? 0 : getNormalItemCount() + 1;
    }

    protected abstract int getNormalItemCount();

    @Override
    public int getItemViewType(int position) {
        int normalItemCount = getNormalItemCount();
        if (position == normalItemCount) {
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    public static class LoadingHolder extends RecyclerView.ViewHolder {

        private View mLoadingView;
        private View mLastView;
        private View mErrorView;
        private OnLoadInErrorStateListener mOnLoadInErrorStateListener;

        public void setOnLoadInErrorStateListener(OnLoadInErrorStateListener onLoadInErrorStateListener) {
            mOnLoadInErrorStateListener = onLoadInErrorStateListener;
        }

        public LoadingHolder(View itemView) {
            super(itemView);
            mLoadingView = itemView.findViewById(R.id.layout_loading);
            mLastView = itemView.findViewById(R.id.layout_last);
            mErrorView = itemView.findViewById(R.id.layout_error);
            mErrorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnLoadInErrorStateListener != null) {
                        mOnLoadInErrorStateListener.onLoadInErrorState();
                    }
                }
            });
        }

        public void switchState(@LoadingMoreType int state) {
            if (state == LoadingMoreType.TYPE_LOADING) {
                mLoadingView.setVisibility(View.VISIBLE);
                mLastView.setVisibility(View.INVISIBLE);
                mErrorView.setVisibility(View.INVISIBLE);

            } else if (state == LoadingMoreType.TYPE_LAST) {
                mLastView.setVisibility(View.VISIBLE);
                mLoadingView.setVisibility(View.INVISIBLE);
                mErrorView.setVisibility(View.INVISIBLE);

            } else if (state == LoadingMoreType.TYPE_ERROR) {
                mErrorView.setVisibility(View.VISIBLE);
                mLoadingView.setVisibility(View.INVISIBLE);
                mLastView.setVisibility(View.INVISIBLE);

            }
        }

    }

}
