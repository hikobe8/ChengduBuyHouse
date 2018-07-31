package com.ray.chengdubuyhouse;

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

import com.ray.chengdubuyhouse.adapter.BannerDetailContentAdapter;
import com.ray.chengdubuyhouse.bean.BannerDetailBean;
import com.ray.chengdubuyhouse.network.HtmlParser;
import com.ray.chengdubuyhouse.network.processor.BannerDetailParseProcessor;
import com.ray.lib.loading.LoadingViewController;
import com.ray.lib.loading.LoadingViewManager;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class BannerDetailActivity extends BaseActivity {

    private Toolbar toolbar;

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
        final RecyclerView recyclerContent = findViewById(R.id.recycler_content);
        final LoadingViewController loadingViewController = LoadingViewManager.register(recyclerContent);
        recyclerContent.setLayoutManager(new LinearLayoutManager(this));
        recyclerContent.addItemDecoration(new BannerDetailDividerItemDecoration(this));
        setupToolBar(toolbar);
        loadingViewController.switchLoading();
        HtmlParser.getInstance().parseHtml(getIntent().getStringExtra("url"), new BannerDetailParseProcessor(), new HtmlParser.HtmlDataCallback<List<BannerDetailBean>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(List<BannerDetailBean> data) {
                if (data.size() > 0) {
                    toolbar.setTitle(data.get(0).getText());
                    recyclerContent.setAdapter(new BannerDetailContentAdapter(data.subList(1, data.size())));
                    loadingViewController.switchSuccess();
                } else {
                    loadingViewController.switchError();
                }
            }

            @Override
            public void onError(Throwable e) {
                loadingViewController.switchError();
            }
        });
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
