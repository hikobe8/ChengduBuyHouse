package com.ray.lib.loading;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.FrameLayout;

import com.ray.lib.R;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-05-09 18:46
 *  description : loading view logic controller
 */
public class LoadingViewController {

    private ViewGroup mLoadingMainLayout;

    private View mTargetView;

    private View mLoadingLayout, mEmptyLayout, mErrorLayout;

    private Button mBtnReload;

    private OnReloadClickListener mOnReloadClickListener;

    public void setOnReloadClickListener(OnReloadClickListener onReloadClickListener) {
        mOnReloadClickListener = onReloadClickListener;
    }

    public interface OnReloadClickListener {
        void onReloadClick();
    }

    /**
     * for activity use
     *
     * @param context
     */
    public LoadingViewController(Context context) {

        ViewGroup parentView = ((Activity) context).findViewById(android.R.id.content);
        mLoadingMainLayout = getLoadingMainLayout(context);
        parentView.addView(mLoadingMainLayout);

    }

    public LoadingViewController(View targetView) {

        mLoadingMainLayout = getLoadingMainLayout(targetView.getContext());
        ViewGroup parentLayout = (ViewGroup) targetView.getParent();
        ViewGroup.LayoutParams layoutParams = targetView.getLayoutParams();
        FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(layoutParams);
        layoutParams1.topMargin = 0;
        layoutParams1.bottomMargin = 0;
        layoutParams1.leftMargin = 0;
        layoutParams1.rightMargin = 0;
        int index = parentLayout.indexOfChild(targetView);
        parentLayout.removeView(targetView);
        mLoadingMainLayout.addView(targetView, 0, layoutParams1);
        parentLayout.addView(mLoadingMainLayout, index, layoutParams);
        mTargetView = targetView;

    }

    private ViewGroup getLoadingMainLayout(Context context) {

        return (ViewGroup) LayoutInflater.from(context).inflate(R.layout.load_main_layout, null, false);

    }


    public void switchLoading() {

        if (mLoadingMainLayout != null && mLoadingLayout == null) {
            ViewStub viewStub = mLoadingMainLayout.findViewById(R.id.vs_loading);
            mLoadingLayout = viewStub.inflate();
        }
        internalHideView(mTargetView);
        internalHideView(mEmptyLayout);
        internalHideView(mErrorLayout);
        mLoadingLayout.setVisibility(View.VISIBLE);

    }

    public void switchEmpty() {

        if (mLoadingMainLayout != null && mEmptyLayout == null) {
            ViewStub viewStub = mLoadingMainLayout.findViewById(R.id.vs_empty);
            mEmptyLayout = viewStub.inflate();
        }
        internalHideView(mTargetView);
        internalHideView(mLoadingLayout);
        internalHideView(mErrorLayout);
        mErrorLayout.setEnabled(false);
        mEmptyLayout.setVisibility(View.VISIBLE);

    }

    public void switchError() {

        if (mLoadingMainLayout != null && mErrorLayout == null) {
            ViewStub viewStub = mLoadingMainLayout.findViewById(R.id.vs_error);
            mErrorLayout = viewStub.inflate();
            mBtnReload = mErrorLayout.findViewById(R.id.btn_reload);
            mBtnReload.setVisibility(mOnReloadClickListener == null ? View.INVISIBLE : View.VISIBLE);
            if (mBtnReload.getVisibility() == View.VISIBLE) {
                mBtnReload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnReloadClickListener != null) {
                            switchLoading();
                            mOnReloadClickListener.onReloadClick();
                        }
                    }
                });
            }

        }
        internalHideView(mTargetView);
        internalHideView(mLoadingLayout);
        internalHideView(mEmptyLayout);
        mErrorLayout.setVisibility(View.VISIBLE);
        mErrorLayout.setEnabled(true);

    }

    public void switchSuccess() {

        internalHideView(mLoadingLayout);
        internalHideView(mEmptyLayout);
        internalHideView(mErrorLayout);
        if (mTargetView != null)
            mTargetView.setVisibility(View.VISIBLE);

    }

    private void internalHideView(View view) {

        if (view != null)
            view.setVisibility(View.INVISIBLE);

    }

}
