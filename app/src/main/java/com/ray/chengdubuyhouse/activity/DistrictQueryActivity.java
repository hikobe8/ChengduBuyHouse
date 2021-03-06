package com.ray.chengdubuyhouse.activity;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.ray.lib.base.BaseActivity;
import com.ray.chengdubuyhouse.R;
import com.ray.chengdubuyhouse.adapter.DistrictAdapter;
import com.ray.chengdubuyhouse.adapter.QueryAdapter;
import com.ray.lib.base.BaseNetworkObserver;
import com.ray.lib.bean.PageableData;
import com.ray.lib.bean.PageableResponseBean;
import com.ray.lib.bean.QueryResultBean;
import com.ray.lib.db.entity.DistrictEntity;
import com.ray.lib.loadmore_recyclerview.LoadMoreAdapter;
import com.ray.lib.loadmore_recyclerview.LoadMoreRecyclerView;
import com.ray.lib.network.HtmlParser;
import com.ray.lib.network.NetworkConstant;
import com.ray.lib.network.processor.QueryParseProcessor;
import com.ray.lib.util.SPUtil;
import com.ray.lib.viewmodel.DistrictViewModel;
import com.ray.chengdubuyhouse.widget.NormalItemDivider;
import com.ray.lib.loading.LoadingViewController;
import com.ray.lib.loading.LoadingViewManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

public class DistrictQueryActivity extends BaseActivity {

    private LoadingViewController mLoadingViewController;
    private LoadingViewController mDistrictLoadingViewController;
    private QueryAdapter mQueryAdapter;
    private DistrictAdapter mDistrictAdapter;
    private DistrictViewModel mDistrictViewModel;
    private DrawerLayout mDrawerLayout;
    private Disposable mRequestDisposable;
    private QueryObserver mQueryObserver;
    private Toolbar mToolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LoadMoreRecyclerView mRecyclerResult;
    private String mRegionCode;
    private PageableData mPageableData = new PageableData();
    private String mPhoneNumber;

    public static void launch(Context context) {
        Intent startIntent = new Intent(context, DistrictQueryActivity.class);
        context.startActivity(startIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district_query);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mSwipeRefreshLayout = findViewById(R.id.swipe);
        mToolbar = findViewById(R.id.toolbar);
        setupToolBar(mToolbar);
        mRecyclerResult = findViewById(R.id.recycler_result);
        RecyclerView rvDistrict = findViewById(R.id.rv_district);
        mLoadingViewController = LoadingViewManager.register(mRecyclerResult);
        mRecyclerResult.setLayoutManager(new LinearLayoutManager(this));
        mQueryAdapter = new QueryAdapter();
        mRecyclerResult.setAdapter(mQueryAdapter);
        mRecyclerResult.addItemDecoration(new NormalItemDivider(this));
        initRegionRv(rvDistrict);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageableData.reset();
                refreshData();
            }
        });
        mRecyclerResult.setOnLoadMoreListener(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.e("test", "page = " + mPageableData.page + "");
                loadMoreData();
            }
        });
        mQueryAdapter.setOnLoadInErrorStateListener(new LoadMoreAdapter.OnLoadInErrorStateListener() {
            @Override
            public void onLoadInErrorState() {
                requestData();
            }
        });
        mQueryAdapter.setCallPhoneCallback(new QueryAdapter.CallPhoneCallback() {
            @Override
            public void onMakeCall(String phone) {
                mPhoneNumber = phone;
                callPhone();
            }
        });
        mLoadingViewController.setOnReloadClickListener(new LoadingViewController.OnReloadClickListener() {
            @Override
            public void onReloadClick() {
                refreshData();
            }
        });
        mLoadingViewController.switchLoading();
        refreshData();
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

    private void loadMoreData() {
        mPageableData.page++;
        requestData();
    }

    private void refreshData() {
        mQueryAdapter.setDataRefreshing();
        if (mRequestDisposable != null && !mRequestDisposable.isDisposed()) {
            mRequestDisposable.dispose();
            mRequestDisposable = null;
        }
        if (mQueryAdapter.getNormalItemCount() == 0) {
            mLoadingViewController.switchLoading();
        }
        requestData();
    }

    private void requestData() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("regioncode", mRegionCode);
        paramsMap.put("pageNo", String.valueOf(mPageableData.page));
        if (mQueryObserver == null) {
            mQueryObserver = new QueryObserver(this);
        }
        HtmlParser.getInstance()
                .parseHtmlByOkHttp(
                        NetworkConstant.QUERY_LIST,
                        paramsMap,
                        new QueryParseProcessor(mDistrictViewModel)).subscribe(mQueryObserver);
    }

    private static class QueryObserver extends BaseNetworkObserver<DistrictQueryActivity, PageableResponseBean<List<QueryResultBean>>> {

        QueryObserver(DistrictQueryActivity districtQueryActivity) {
            super(districtQueryActivity);
        }

        @Override
        public void onNetworkSubscribe(DistrictQueryActivity districtQueryActivity, Disposable d) {
            districtQueryActivity.mRequestDisposable = d;
            districtQueryActivity.addDisposable(d);
        }

        @Override
        public void onNetworkNext(DistrictQueryActivity districtQueryActivity, PageableResponseBean<List<QueryResultBean>> listPageableResponseBean) {
            setRefreshComplete(districtQueryActivity);
            if (listPageableResponseBean != null && listPageableResponseBean.data != null && listPageableResponseBean.data.size() > 0) {
                List<QueryResultBean> data = listPageableResponseBean.data;
                if (districtQueryActivity.mQueryAdapter.getLoadState() == LoadMoreAdapter.STATE_REFRESH) {
                    //刷新的结果
                    districtQueryActivity.mQueryAdapter.refreshData(data);
                    districtQueryActivity.mRecyclerResult.scrollToPosition(0);
                    districtQueryActivity.mLoadingViewController.switchSuccess();
                } else {
                    //加载更多的结果
                    districtQueryActivity.mQueryAdapter.addData(data);
                }
                districtQueryActivity.mPageableData = listPageableResponseBean.pageableData;
                if (districtQueryActivity.mPageableData.isLastPage()) {
                    districtQueryActivity.mQueryAdapter.setLastPage();
                } else {
                    districtQueryActivity.mQueryAdapter.setDataLoaded();
                }
            } else {
                if (districtQueryActivity.mQueryAdapter.getNormalItemCount() > 0) {
                    districtQueryActivity.mQueryAdapter.setLastPage();
                } else {
                    districtQueryActivity.mLoadingViewController.switchEmpty();
                }
            }
        }

        @Override
        public void onNetworkError(DistrictQueryActivity districtQueryActivity, Throwable e) {
            setRefreshComplete(districtQueryActivity);
            if (districtQueryActivity.mQueryAdapter.getNormalItemCount() > 0) {
                if (districtQueryActivity.mPageableData.isLastPage()) {
                    districtQueryActivity.mQueryAdapter.setLastPage();
                } else {
                    districtQueryActivity.mQueryAdapter.setDataLoadError();
                }
            } else {
                districtQueryActivity.mLoadingViewController.switchError();
            }
        }
    }

    private static void setRefreshComplete(DistrictQueryActivity districtQueryActivity) {
        if (districtQueryActivity.mSwipeRefreshLayout != null && districtQueryActivity.mSwipeRefreshLayout.isRefreshing()) {
            districtQueryActivity.mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void callPhone() {
        if (TextUtils.isEmpty(mPhoneNumber))
            return;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + mPhoneNumber));//change the number
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 0);
            return;
        }
        startActivity(callIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length >= 1) {
            callPhone();
        }
    }

    private void initRegionRv(RecyclerView rvDistrict) {
        mDistrictLoadingViewController = LoadingViewManager.register(rvDistrict);
        mDistrictLoadingViewController.switchLoading();
        mDistrictViewModel = ViewModelProviders.of(this).get(DistrictViewModel.class);
        mDistrictViewModel.getListLiveData().observe(this, new android.arch.lifecycle.Observer<List<DistrictEntity>>() {
            @Override
            public void onChanged(@Nullable List<DistrictEntity> districtEntities) {
                if (districtEntities != null && districtEntities.size() > 0) {
                    mDistrictAdapter.setData(districtEntities);
                    mDistrictLoadingViewController.switchSuccess();
                }
            }
        });
        DistrictEntity cachedDistrict = mDistrictViewModel.getCachedDistrictEntity();
        mToolbar.setTitle(cachedDistrict.getName());
        mRegionCode = cachedDistrict.getRegionCode();
        rvDistrict.setLayoutManager(new LinearLayoutManager(this));
        rvDistrict.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mDistrictAdapter = new DistrictAdapter(this, mRegionCode);
        mDistrictAdapter.setOnItemClickListener(new DistrictAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DistrictEntity districtEntity) {
                setRefreshComplete(DistrictQueryActivity.this);
                mDrawerLayout.closeDrawer(Gravity.END);
                if (mRegionCode.equals(districtEntity.getRegionCode()))
                    return;
                mPageableData.reset();
                mRegionCode = districtEntity.getRegionCode();
                mLoadingViewController.switchLoading();
                mQueryAdapter.clear();
                refreshData();
                SPUtil.getInstance(mActivity).putString(SPUtil.KEY.KEY_SELECTED_DISTRICT, districtEntity.toString());
                mToolbar.setTitle(districtEntity.getName());
            }
        });
        rvDistrict.setAdapter(mDistrictAdapter);
    }
}
