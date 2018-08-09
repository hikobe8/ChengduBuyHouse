package com.ray.chengdubuyhouse.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ray.chengdubuyhouse.BaseFragment;
import com.ray.chengdubuyhouse.R;
import com.ray.chengdubuyhouse.adapter.PreSellHouseAdapter;
import com.ray.chengdubuyhouse.bean.BannerBean;
import com.ray.chengdubuyhouse.bean.PreSellHouseBean;
import com.ray.chengdubuyhouse.network.HtmlParser;
import com.ray.chengdubuyhouse.network.NetworkConstant;
import com.ray.chengdubuyhouse.network.processor.BannerParseProcessor;
import com.ray.chengdubuyhouse.network.processor.PreSellParseProcessor;
import com.ray.chengdubuyhouse.widget.NormalItemDivider;
import com.ray.lib.loading.LoadingViewController;
import com.ray.lib.loading.LoadingViewManager;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Author : hikobe8@github.com
 * Time : 2018/7/27 上午12:22
 * Description :
 */
public class PreSellHouseFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, Observer<List<PreSellHouseBean>> {

    private PreSellHouseAdapter mAdapter;
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
        recyclerPreSell.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new PreSellHouseAdapter();
        recyclerPreSell.setAdapter(mAdapter);
        recyclerPreSell.addItemDecoration(new NormalItemDivider(getActivity()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLoadingViewController.switchLoading();
        requestData();
    }

    private void requestData() {
        HtmlParser.getInstance().parseHtml(NetworkConstant.PRE_SELL_URL, new PreSellParseProcessor()).subscribe(this);
        HtmlParser.getInstance().parseHtml(NetworkConstant.HOME_BANNER, new BannerParseProcessor()).subscribe(mBannerSubscriber);
    }

    private Observer<List<BannerBean>> mBannerSubscriber = new Observer<List<BannerBean>>() {
        @Override
        public void onSubscribe(Disposable d) {
            addDisposable(d);
        }

        @Override
        public void onNext(List<BannerBean> datas) {
            mAdapter.setBannerData(datas);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };

    @Override
    public void onRefresh() {
      requestData();
    }

    @Override
    public void onSubscribe(Disposable d) {
        addDisposable(d);
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

    @Override
    public void onComplete() {

    }

}
