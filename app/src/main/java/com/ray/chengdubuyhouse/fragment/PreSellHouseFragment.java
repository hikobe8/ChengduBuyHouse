package com.ray.chengdubuyhouse.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ray.chengdubuyhouse.R;
import com.ray.chengdubuyhouse.adapter.BannerAdapter;
import com.ray.chengdubuyhouse.adapter.BannerItemDecoration;
import com.ray.chengdubuyhouse.adapter.PreSellHouseAdapter;
import com.ray.chengdubuyhouse.bean.BannerBean;
import com.ray.chengdubuyhouse.bean.PreSellHouseBean;
import com.ray.chengdubuyhouse.parser.HtmlParser;
import com.ray.lib.loading.LoadingViewController;
import com.ray.lib.loading.LoadingViewManager;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Author : hikobe8@github.com
 * Time : 2018/7/27 上午12:22
 * Description :
 */
public class PreSellHouseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, HtmlParser.DataCallback {

    private PreSellHouseAdapter mAdapter;
    private BannerAdapter mBannerAdapter;
    private LoadingViewController mLoadingViewController;

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
        RecyclerView recyclerPreSell = view.findViewById(R.id.recycler_pre_sell);
        RecyclerView recyclerBanner = view.findViewById(R.id.recycler_banner);
        recyclerPreSell.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new PreSellHouseAdapter();
        recyclerPreSell.setAdapter(mAdapter);
        recyclerBanner.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mBannerAdapter = new BannerAdapter();
        recyclerBanner.setAdapter(mBannerAdapter);
        recyclerBanner.addItemDecoration(new BannerItemDecoration(getActivity()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLoadingViewController.switchLoading();
        HtmlParser.getInstance().parseHtml(null, this);
        HtmlParser.getInstance().getBannerList(mBannerCallback);
    }

    private HtmlParser.BannerCallback mBannerCallback = new HtmlParser.BannerCallback() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(List<BannerBean> datas) {
            mBannerAdapter.refreshData(datas);
        }

        @Override
        public void onError(Throwable e) {

        }
    };

    @Override
    public void onRefresh() {
        HtmlParser.getInstance().parseHtml(null, this);
        HtmlParser.getInstance().getBannerList(mBannerCallback);
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(List<PreSellHouseBean> datas) {
        View view = getView();
        if (view != null && view instanceof SwipeRefreshLayout) {
            ((SwipeRefreshLayout)view).setRefreshing(false);
        }
        if (datas == null || datas.size() < 1) {
            mLoadingViewController.switchEmpty();
        } else {
            mAdapter.setData(datas);
            mLoadingViewController.switchSuccess();
        }
    }

    @Override
    public void onError(Throwable e) {
        mLoadingViewController.switchError();
    }
}
