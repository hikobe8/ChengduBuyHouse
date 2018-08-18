package com.ray.chengdubuyhouse.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ray.chengdubuyhouse.BaseActivity;
import com.ray.chengdubuyhouse.R;
import com.ray.chengdubuyhouse.adapter.QueryAdapter;
import com.ray.chengdubuyhouse.adapter.DistrictAdapter;
import com.ray.chengdubuyhouse.bean.QueryResultBean;
import com.ray.chengdubuyhouse.db.HouseDatabase;
import com.ray.chengdubuyhouse.db.entity.DistrictEntity;
import com.ray.chengdubuyhouse.network.HtmlParser;
import com.ray.chengdubuyhouse.network.NetworkConstant;
import com.ray.chengdubuyhouse.network.processor.QueryParseProcessor;
import com.ray.chengdubuyhouse.widget.NormalItemDivider;
import com.ray.lib.loading.LoadingViewController;
import com.ray.lib.loading.LoadingViewManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class DistrictQueryActivity extends BaseActivity implements Observer<List<QueryResultBean>> {

    private LoadingViewController mLoadingViewController;

    public static void launch(Context context) {
        Intent startIntent = new Intent(context, DistrictQueryActivity.class);
        context.startActivity(startIntent);
    }

    private QueryAdapter mQueryAdapter;
    private DistrictAdapter mDistrictAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district_query);
        setupToolBar(R.id.toolbar);
        RecyclerView recyclerResult = findViewById(R.id.recycler_result);
        RecyclerView rvDistrict = findViewById(R.id.rv_district);
        initRegionRv(rvDistrict);
        mLoadingViewController = LoadingViewManager.register(recyclerResult);
        recyclerResult.setLayoutManager(new LinearLayoutManager(this));
        mQueryAdapter = new QueryAdapter();
        recyclerResult.setAdapter(mQueryAdapter);
        mLoadingViewController.switchLoading();
        recyclerResult.addItemDecoration(new NormalItemDivider(this));
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("regioncode", "00");
        paramsMap.put("pageNo", "1");
        HtmlParser.getInstance().parseHtmlByOkHttp(NetworkConstant.QUERY_LIST, paramsMap, new QueryParseProcessor()).subscribe(this);
        HouseDatabase.getInstance(this).districtDao().getAllDistricts().observe(this, new android.arch.lifecycle.Observer<List<DistrictEntity>>() {
            @Override
            public void onChanged(@Nullable List<DistrictEntity> districtEntities) {
                mDistrictAdapter.setData(districtEntities);
            }
        });
    }

    private void initRegionRv(RecyclerView rvDistrict) {
        rvDistrict.setLayoutManager(new LinearLayoutManager(this));
        rvDistrict.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mDistrictAdapter = new DistrictAdapter();
        rvDistrict.setAdapter(mDistrictAdapter);
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
