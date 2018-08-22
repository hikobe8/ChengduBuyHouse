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
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.ray.chengdubuyhouse.BaseActivity;
import com.ray.chengdubuyhouse.R;
import com.ray.chengdubuyhouse.adapter.DistrictAdapter;
import com.ray.chengdubuyhouse.adapter.QueryAdapter;
import com.ray.chengdubuyhouse.bean.QueryResultBean;
import com.ray.chengdubuyhouse.db.entity.DistrictEntity;
import com.ray.chengdubuyhouse.network.HtmlParser;
import com.ray.chengdubuyhouse.network.NetworkConstant;
import com.ray.chengdubuyhouse.network.processor.QueryParseProcessor;
import com.ray.chengdubuyhouse.util.SPUtil;
import com.ray.chengdubuyhouse.viewmodel.DistrictViewModel;
import com.ray.chengdubuyhouse.widget.NormalItemDivider;
import com.ray.lib.loading.LoadingViewController;
import com.ray.lib.loading.LoadingViewManager;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class DistrictQueryActivity extends BaseActivity{

    private LoadingViewController mLoadingViewController;
    private QueryAdapter mQueryAdapter;
    private DistrictAdapter mDistrictAdapter;
    private DistrictViewModel mDistrictViewModel;
    private DrawerLayout mDrawerLayout;
    private Disposable mRequestDisposable;
    private QueryObserver mQueryObserver;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerResult;

    public static void launch(Context context) {
        Intent startIntent = new Intent(context, DistrictQueryActivity.class);
        context.startActivity(startIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district_query);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToolbar = findViewById(R.id.toolbar);
        setupToolBar(mToolbar);
        mRecyclerResult = findViewById(R.id.recycler_result);
        RecyclerView rvDistrict = findViewById(R.id.rv_district);
        mLoadingViewController = LoadingViewManager.register(mRecyclerResult);
        mRecyclerResult.setLayoutManager(new LinearLayoutManager(this));
        mQueryAdapter = new QueryAdapter();
        mRecyclerResult.setAdapter(mQueryAdapter);
        mRecyclerResult.addItemDecoration(new NormalItemDivider(this));
        mDistrictViewModel = ViewModelProviders.of(this).get(DistrictViewModel.class);
        mDistrictViewModel.getListLiveData().observe(this, new android.arch.lifecycle.Observer<List<DistrictEntity>>() {
            @Override
            public void onChanged(@Nullable List<DistrictEntity> districtEntities) {
                mDistrictAdapter.setData(districtEntities);
            }
        });
        String cachedCode = "00";
        DistrictEntity cachedDistrict = mDistrictViewModel.getCachedDistrictEntity();
        if (cachedDistrict != null) {
            mToolbar.setTitle(cachedDistrict.getName());
            cachedCode = cachedDistrict.getRegionCode();
        } else {
            mToolbar.setTitle("所有区域");
        }
        requestData(cachedCode);
        initRegionRv(rvDistrict, cachedCode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_district_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            mDrawerLayout.openDrawer(Gravity.END);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        if (mQueryObserver == null) {
            mQueryObserver = new QueryObserver(this);
        }
        HtmlParser.getInstance()
                .parseHtmlByOkHttp(
                        NetworkConstant.QUERY_LIST,
                        paramsMap,
                        new QueryParseProcessor(mDistrictViewModel)).subscribe(mQueryObserver);
    }

    private static class QueryObserver implements Observer<List<QueryResultBean>>{

        private WeakReference<DistrictQueryActivity> mDistrictQueryActivityWeakReference;

        public QueryObserver(DistrictQueryActivity districtQueryActivity) {
            mDistrictQueryActivityWeakReference = new WeakReference<>(districtQueryActivity);
        }

        @Override
        public void onSubscribe(Disposable d) {
            DistrictQueryActivity districtQueryActivity = mDistrictQueryActivityWeakReference.get();
            if (districtQueryActivity != null) {
                districtQueryActivity.mRequestDisposable = d;
                districtQueryActivity.addDisposable(d);
            }

        }

        @Override
        public void onNext(List<QueryResultBean> data) {
            DistrictQueryActivity districtQueryActivity = mDistrictQueryActivityWeakReference.get();
            if (districtQueryActivity != null) {
                if (data != null && data.size() > 0) {
                    districtQueryActivity.mQueryAdapter.refreshData(data);
                    districtQueryActivity.mRecyclerResult.scrollToPosition(0);
                    districtQueryActivity.mLoadingViewController.switchSuccess();
                } else {
                    districtQueryActivity.mLoadingViewController.switchEmpty();
                }
            }
        }

        @Override
        public void onError(Throwable e) {
            DistrictQueryActivity districtQueryActivity = mDistrictQueryActivityWeakReference.get();
            if (districtQueryActivity != null) {
                districtQueryActivity.mLoadingViewController.switchError();
                districtQueryActivity.mRequestDisposable.dispose();
            }
        }

        @Override
        public void onComplete() {

        }
    }

    private void initRegionRv(RecyclerView rvDistrict, String cachedCode) {
        rvDistrict.setLayoutManager(new LinearLayoutManager(this));
        rvDistrict.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mDistrictAdapter = new DistrictAdapter(this, cachedCode);
        mDistrictAdapter.setOnItemClickListener(new DistrictAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DistrictEntity districtEntity) {
                String regionCode = districtEntity.getRegionCode();
                requestData(regionCode);
                SPUtil.getInstance(mActivity).putString(SPUtil.KEY.KEY_SELECTED_DISTRICT, districtEntity.toString());
                mDrawerLayout.closeDrawer(Gravity.END);
                mToolbar.setTitle(districtEntity.getName());
            }
        });
        rvDistrict.setAdapter(mDistrictAdapter);
    }
}
