package com.ray.chengdubuyhouse.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ray.lib.base.BaseActivity;
import com.ray.chengdubuyhouse.R;
import com.ray.chengdubuyhouse.adapter.BannerDetailContentAdapter;
import com.ray.lib.base.BaseNetworkObserver;
import com.ray.lib.bean.BannerDetailBean;
import com.ray.lib.network.HtmlParser;
import com.ray.lib.network.processor.BannerDetailParseProcessor;
import com.ray.lib.loading.LoadingViewController;
import com.ray.lib.loading.LoadingViewManager;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class BannerDetailActivity extends BaseActivity {

    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private LoadingViewController mLoadingViewController;

    public static void launch(Context context, String url) {
        Intent intent = new Intent(context, BannerDetailActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_detail);
        toolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.recycler_content);
        mLoadingViewController = LoadingViewManager.register(mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new BannerDetailDividerItemDecoration(this));
        setupToolBar(toolbar);
        mLoadingViewController.switchLoading();
        mLoadingViewController.setOnReloadClickListener(new LoadingViewController.OnReloadClickListener() {
            @Override
            public void onReloadClick() {
                HtmlParser.getInstance().parseHtml(getIntent().getStringExtra("url"), new BannerDetailParseProcessor()).subscribe(new DetailObserver(BannerDetailActivity.this));
            }
        });
        HtmlParser.getInstance().parseHtml(getIntent().getStringExtra("url"), new BannerDetailParseProcessor()).subscribe(new DetailObserver(this));
    }

    static class DetailObserver extends BaseNetworkObserver<BannerDetailActivity, List<BannerDetailBean>> {

        public DetailObserver(BannerDetailActivity bannerDetailActivity) {
            super(bannerDetailActivity);
        }

        @Override
        public void onNetworkSubscribe(BannerDetailActivity bannerDetailActivity, Disposable d) {
            bannerDetailActivity.addDisposable(d);
        }

        @Override
        public void onNetworkNext(BannerDetailActivity bannerDetailActivity, List<BannerDetailBean> bannerDetailBeans) {
            if (bannerDetailBeans.size() > 0) {
                bannerDetailActivity.toolbar.setTitle(bannerDetailBeans.get(0).getText());
                bannerDetailActivity.mRecyclerView.setAdapter(new BannerDetailContentAdapter(bannerDetailBeans.subList(1, bannerDetailBeans.size())));
                bannerDetailActivity.mLoadingViewController.switchSuccess();
            } else {
                bannerDetailActivity.mLoadingViewController.switchError();
            }
        }

        @Override
        public void onNetworkError(BannerDetailActivity bannerDetailActivity, Throwable e) {
            bannerDetailActivity.mLoadingViewController.switchError();
        }
    }

    private static class BannerDetailDividerItemDecoration extends DividerItemDecoration {

        public BannerDetailDividerItemDecoration(Context context) {
            super(context, VERTICAL);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0,0,0,parent.getResources().getDimensionPixelSize(R.dimen.nav_header_vertical_spacing));
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        }
    }

}
