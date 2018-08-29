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

    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_NORMAL = 1;

    public static final int STATE_LOADING = 1;
    public static final int STATE_LOADED = 2;
    public static final int STATE_LAST_PAGE = 3;
    public static final int STATE_LOAD_ERROR = 4;
    public static final int STATE_REFRESH = 5;

    private int mLoadState = STATE_LOADED;

    //脚部加载栏状态
    private int mFooterState = LoadingMoreType.TYPE_LOADING;

    private LoadingHolder mLoadingHolder;

    public int getLoadState() {
        return mLoadState;
    }

    private void internalSetLoadState(int loadState) {
        mLoadState = loadState;
    }

    public void setDataLoading(){
        internalSetLoadState(STATE_LOADING);
        setFooterState(LoadingMoreType.TYPE_LOADING);
    }

    public void setDataLoaded () {
        internalSetLoadState(STATE_LOADED);
        setFooterState(LoadingMoreType.TYPE_LOADING);
    }

    public void setLastPage() {
        internalSetLoadState(STATE_LAST_PAGE);
        setFooterState(LoadingMoreType.TYPE_LAST);
    }

    public void setDataLoadError(){
        mLoadState = STATE_LOAD_ERROR;
        setFooterState(LoadingMoreType.TYPE_ERROR);
    }

    public void setDataRefreshing(){
        mLoadState = STATE_REFRESH;
    }

    public boolean isLoaded() {
        return mLoadState == STATE_LOADED;
    }

    public boolean isRefreshing() {
        return mLoadState == STATE_REFRESH;
    }

    public interface OnLoadInErrorStateListener {
        void onLoadInErrorState();
    }

    private OnLoadInErrorStateListener mOnLoadInErrorStateListener;

    public void setOnLoadInErrorStateListener(OnLoadInErrorStateListener onLoadInErrorStateListener) {
        mOnLoadInErrorStateListener = onLoadInErrorStateListener;
    }

    public void setFooterState(int footerState) {
        mFooterState = footerState;
        if (mLoadingHolder != null) {
            mLoadingHolder.switchState(footerState);
            return;
        }
        notifyItemChanged(getNormalItemCount());
    }

    @NonNull
    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            mLoadingHolder = new LoadingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_loading_item, parent, false));
            mLoadingHolder.setOnLoadInErrorStateListener(mOnLoadInErrorStateListener);
            return mLoadingHolder;
        }
        return onCreateNormalViewHolder(parent, viewType);
    }

    protected abstract RecyclerView.ViewHolder onCreateNormalViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public final void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position < getNormalItemCount()) {
            onBindNormalViewHolder(holder, position);
        } else {
            if (holder instanceof LoadingHolder) {
                LoadingHolder loadingHolder = (LoadingHolder) holder;
                loadingHolder.switchState(mFooterState);
            }
        }
    }

    protected abstract void onBindNormalViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public final int getItemCount() {
        return getNormalItemCount() == 0 ? 0 : getNormalItemCount() + 1;
    }

    public abstract int getNormalItemCount();

    @Override
    public final int getItemViewType(int position) {
        int normalItemCount = getNormalItemCount();
        if (position == normalItemCount) {
            return TYPE_FOOTER;
        }
        return getNormalItemViewType(position);
    }

    protected int getNormalItemViewType(int position) {
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
                    if (mErrorView.getVisibility() != View.VISIBLE)
                        return;
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
