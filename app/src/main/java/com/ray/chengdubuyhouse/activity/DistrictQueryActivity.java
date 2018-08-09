package com.ray.chengdubuyhouse.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ray.chengdubuyhouse.BaseActivity;
import com.ray.chengdubuyhouse.R;
import com.ray.chengdubuyhouse.adapter.QueryAdapter;
import com.ray.chengdubuyhouse.bean.QueryResultBean;
import com.ray.chengdubuyhouse.network.HtmlParser;
import com.ray.chengdubuyhouse.network.NetworkConstant;
import com.ray.chengdubuyhouse.network.processor.QueryParseProcessor;
import com.ray.chengdubuyhouse.widget.NormalItemDivider;
import com.ray.lib.loading.LoadingViewController;
import com.ray.lib.loading.LoadingViewManager;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class DistrictQueryActivity extends BaseActivity implements Observer<List<QueryResultBean>> {

    private LoadingViewController mLoadingViewController;

    public static void launch(Context context) {
        Intent startIntent = new Intent(context, DistrictQueryActivity.class);
        context.startActivity(startIntent);
    }

    private QueryAdapter mQueryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district_query);
        setupToolBar(R.id.toolbar);
        RecyclerView recyclerResult = findViewById(R.id.recycler_result);
        mLoadingViewController = LoadingViewManager.register(recyclerResult);
        recyclerResult.setLayoutManager(new LinearLayoutManager(this));
        mQueryAdapter = new QueryAdapter();
        recyclerResult.setAdapter(mQueryAdapter);
        mLoadingViewController.switchLoading();
        recyclerResult.addItemDecoration(new NormalItemDivider(this));
        HtmlParser.getInstance().parseHtmlByOkHttp(NetworkConstant.QUERY_LIST, new QueryParseProcessor()).subscribe(this);
    }

    @Override
    public void onSubscribe(Disposable d) {
        addDisposable(d);
    }

    @Override
    public void onNext(List<QueryResultBean> data) {
        if (data != null && data.size() > 0) {
            mQueryAdapter.addData(data);
            mLoadingViewController.switchSuccess();
        } else {
            mLoadingViewController.switchEmpty();
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
