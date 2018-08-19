package com.ray.chengdubuyhouse.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;

import com.ray.chengdubuyhouse.BaseActivity;
import com.ray.chengdubuyhouse.R;
import com.ray.chengdubuyhouse.adapter.DistrictAdapter;
import com.ray.chengdubuyhouse.adapter.QueryAdapter;
import com.ray.chengdubuyhouse.bean.QueryResultBean;
import com.ray.chengdubuyhouse.db.entity.DistrictEntity;
import com.ray.chengdubuyhouse.network.HtmlParser;
import com.ray.chengdubuyhouse.network.NetworkConstant;
import com.ray.chengdubuyhouse.network.processor.QueryParseProcessor;
import com.ray.chengdubuyhouse.viewmodel.DistrictViewModel;
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
    private QueryAdapter mQueryAdapter;
    private DistrictAdapter mDistrictAdapter;
    private DistrictViewModel mDistrictViewModel;
    private DrawerLayout mDrawerLayout;
    private Disposable mRequestDisposable;

    public static void launch(Context context) {
        Intent startIntent = new Intent(context, DistrictQueryActivity.class);
        context.startActivity(startIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district_query);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        setupToolBar(R.id.toolbar);
        RecyclerView recyclerResult = findViewById(R.id.recycler_result);
        RecyclerView rvDistrict = findViewById(R.id.rv_district);
        initRegionRv(rvDistrict);
        mLoadingViewController = LoadingViewManager.register(recyclerResult);
        recyclerResult.setLayoutManager(new LinearLayoutManager(this));
        mQueryAdapter = new QueryAdapter();
        recyclerResult.setAdapter(mQueryAdapter);
        recyclerResult.addItemDecoration(new NormalItemDivider(this));
        mDistrictViewModel = ViewModelProviders.of(this).get(DistrictViewModel.class);
        mDistrictViewModel.getListLiveData().observe(this, new android.arch.lifecycle.Observer<List<DistrictEntity>>() {
            @Override
            public void onChanged(@Nullable List<DistrictEntity> districtEntities) {
                mDistrictAdapter.setData(districtEntities);
            }
        });
        requestData("00");
    }

    private void requestData(String code) {
        if (mRequestDisposable != null && !mRequestDisposable.isDisposed()) {
            mRequestDisposable.dispose();
            mRequestDisposable = null;
        }
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("regioncode", code);
        paramsMap.put("pageNo", "1");
        mLoadingViewController.switchLoading();
        HtmlParser.getInstance()
                .parseHtmlByOkHttp(
                        NetworkConstant.QUERY_LIST,
                        paramsMap,
                        new QueryParseProcessor(mDistrictViewModel)).subscribe(this);
    }

    private void initRegionRv(RecyclerView rvDistrict) {
        rvDistrict.setLayoutManager(new LinearLayoutManager(this));
        rvDistrict.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mDistrictAdapter = new DistrictAdapter();
        mDistrictAdapter.setOnItemClickListener(new DistrictAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DistrictEntity districtEntity) {
                requestData(districtEntity.getRegionCode());
                mDrawerLayout.closeDrawer(Gravity.END);
            }
        });
        rvDistrict.setAdapter(mDistrictAdapter);
    }

    @Override
    public void onSubscribe(Disposable d) {
        mRequestDisposable = d;
        addDisposable(d);
    }

    @Override
    public void onNext(List<QueryResultBean> data) {
        if (data != null && data.size() > 0) {
            mQueryAdapter.refreshData(data);
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
