package com.ray.chengdubuyhouse.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ray.chengdubuyhouse.R;
import com.ray.chengdubuyhouse.adapter.PreSellHouseAdapter;
import com.ray.chengdubuyhouse.widget.NormalItemDivider;
import com.ray.lib.base.BaseFragment;
import com.ray.lib.base.BaseNetworkObserver;
import com.ray.lib.bean.BannerBean;
import com.ray.lib.bean.PageableData;
import com.ray.lib.bean.PageableResponseBean;
import com.ray.lib.bean.PreSellHouseBean;
import com.ray.lib.loading.LoadingViewController;
import com.ray.lib.loading.LoadingViewManager;
import com.ray.lib.loadmore_recyclerview.LoadMoreRecyclerView;
import com.ray.lib.loadmore_recyclerview.LoadingMoreType;
import com.ray.lib.network.HtmlParser;
import com.ray.lib.network.NetworkConstant;
import com.ray.lib.network.processor.BannerParseProcessor;
import com.ray.lib.network.processor.PreSellParseProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Author : hikobe8@github.com
 * Time : 2018/7/27 上午12:22
 * Description :
 */
public class PreSellHouseFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private PreSellHouseAdapter mAdapter;
    private LoadingViewController mLoadingViewController;
    private LinearLayoutManager mLinearLayoutManager;
    private BannerObserver mBannerSubscriber = new BannerObserver(this);
    private DataObserver mDataSubscriber = new DataObserver(this);
    private PageableData mPageableData = new PageableData();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_presell, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoadingViewController = LoadingViewManager.register(view);
        ((SwipeRefreshLayout)view).setOnRefreshListener(this);
        LoadMoreRecyclerView recyclerPreSell = view.findViewById(R.id.recycler_pre_sell);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerPreSell.setLayoutManager(mLinearLayoutManager);
        mAdapter = new PreSellHouseAdapter();
        recyclerPreSell.setAdapter(mAdapter);
        recyclerPreSell.addItemDecoration(new NormalItemDivider(getActivity()));
        recyclerPreSell.setOnLoadMoreListener(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMoreData();
            }
        });
    }

    private void loadMoreData() {
        mPageableData.page ++;
        Map<String, String> params = new HashMap<>();
        params.put("p", String.valueOf(mPageableData.page));
        HtmlParser.getInstance().parseHtml(NetworkConstant.PRE_SELL_URL, params, new PreSellParseProcessor()).subscribe(mDataSubscriber);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLoadingViewController.switchLoading();
        requestData();
    }

    private void requestData() {
        mPageableData.reset();
        HtmlParser.getInstance().parseHtml(NetworkConstant.PRE_SELL_URL, new PreSellParseProcessor()).subscribe(mDataSubscriber);
        HtmlParser.getInstance().parseHtml(NetworkConstant.HOME_BANNER, new BannerParseProcessor()).subscribe(mBannerSubscriber);
    }

    private static class BannerObserver extends BaseNetworkObserver<PreSellHouseFragment, List<BannerBean>> {

        BannerObserver(PreSellHouseFragment preSellHouseFragment) {
            super(preSellHouseFragment);
        }

        @Override
        public void onNetworkSubscribe(PreSellHouseFragment preSellHouseFragment, Disposable d) {
            preSellHouseFragment.addDisposable(d);
        }

        @Override
        public void onNetworkNext(PreSellHouseFragment preSellHouseFragment, List<BannerBean> bannerBeans) {
            preSellHouseFragment.mAdapter.setBannerData(bannerBeans);
            if (preSellHouseFragment.mLinearLayoutManager.findFirstVisibleItemPosition() <= 1) {
                preSellHouseFragment.mLinearLayoutManager.scrollToPosition(0);
            }
        }

        @Override
        public void onNetworkError(PreSellHouseFragment preSellHouseFragment, Throwable e) {

        }
    }

    private static class DataObserver extends BaseNetworkObserver<PreSellHouseFragment, PageableResponseBean<List<PreSellHouseBean>>> {

        public DataObserver(PreSellHouseFragment preSellHouseFragment) {
            super(preSellHouseFragment);
        }

        @Override
        public void onNetworkSubscribe(PreSellHouseFragment preSellHouseFragment, Disposable d) {
            preSellHouseFragment.addDisposable(d);
        }

        @Override
        public void onNetworkNext(PreSellHouseFragment preSellHouseFragment, PageableResponseBean<List<PreSellHouseBean>> listPageableResponseBean) {
            View view = preSellHouseFragment.getView();
            if (view != null && view instanceof SwipeRefreshLayout) {
                if (((SwipeRefreshLayout)view).isRefreshing()) {
                    ((SwipeRefreshLayout)view).setRefreshing(false);
                    preSellHouseFragment.mAdapter.setCanLoadMore(true);
                }
            }
            if (!preSellHouseFragment.mAdapter.isCanLoadMore())
                return;

            if (listPageableResponseBean == null) {
                if (preSellHouseFragment.mAdapter.getNormalItemCount() > 0) {
                    preSellHouseFragment.mAdapter.setFooterState(LoadingMoreType.TYPE_ERROR);
                    preSellHouseFragment.mAdapter.setCanLoadMore(false);
                } else {
                    preSellHouseFragment.mLoadingViewController.switchEmpty();
                }
                return;
            }
            List<PreSellHouseBean> preSellHouseBeans = listPageableResponseBean.data;
            if (preSellHouseBeans == null || preSellHouseBeans.size() < 1) {
                if (preSellHouseFragment.mAdapter.getNormalItemCount() > 0) {
                    preSellHouseFragment.mAdapter.setFooterState(LoadingMoreType.TYPE_LAST);
                    preSellHouseFragment.mAdapter.setCanLoadMore(false);
                } else {
                    preSellHouseFragment.mLoadingViewController.switchEmpty();
                }
            } else {
                preSellHouseFragment.mPageableData = listPageableResponseBean.pageableData;
                if (preSellHouseFragment.mPageableData.page <= 1) {
                    preSellHouseFragment.mAdapter.refreshData(preSellHouseBeans);
                } else {
                    preSellHouseFragment.mAdapter.addData(preSellHouseBeans);
                }
                if (preSellHouseFragment.mAdapter.getNormalItemCount() > 0) {
                    if (preSellHouseFragment.mPageableData.isLastPage()) {
                        preSellHouseFragment.mAdapter.setFooterState(LoadingMoreType.TYPE_LAST);
                        preSellHouseFragment.mAdapter.setCanLoadMore(false);
                    } else if (preSellHouseFragment.mPageableData.page <= 1) {
                        preSellHouseFragment.mAdapter.setFooterState(LoadingMoreType.TYPE_LOADING);
                        preSellHouseFragment.mAdapter.setCanLoadMore(true);
                    }
                }
                preSellHouseFragment.mLoadingViewController.switchSuccess();
            }
        }

        @Override
        public void onNetworkError(PreSellHouseFragment preSellHouseFragment, Throwable e) {
            preSellHouseFragment.mLoadingViewController.switchError();
        }
    }

    @Override
    public void onRefresh() {
        mAdapter.setCanLoadMore(false);
        mPageableData.reset();
        HtmlParser.getInstance().parseHtml(NetworkConstant.PRE_SELL_URL, new PreSellParseProcessor()).subscribe(mDataSubscriber);
    }

}
