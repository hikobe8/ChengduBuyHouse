package com.ray.chengdubuyhouse.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ray.lib.base.BaseActivity;
import com.ray.chengdubuyhouse.R;
import com.ray.lib.base.BaseNetworkObserver;
import com.ray.lib.network.HtmlParser;
import com.ray.lib.network.processor.PreSellDetailParseProcessor;
import com.ray.lib.loading.LoadingViewController;
import com.ray.lib.loading.LoadingViewManager;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class PreSellDetailActivity extends BaseActivity {

    private LoadingViewController mLoadingViewController;
    private RecyclerView mRecyclerDetail;

    public static void launch(Context context, String url) {
        Intent startIntent = new Intent(context, PreSellDetailActivity.class);
        startIntent.putExtra("url", url);
        context.startActivity(startIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_sell_detail);
        setupToolBar(R.id.toolbar);
        mRecyclerDetail = findViewById(R.id.recycler_detail);
        mLoadingViewController = LoadingViewManager.register(mRecyclerDetail);
        mRecyclerDetail.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerDetail.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        String url = getIntent().getStringExtra("url");
        mLoadingViewController.switchLoading();
        HtmlParser.getInstance().parseHtml(url, new PreSellDetailParseProcessor()).subscribe(new DetailObserver(this));
    }

    static class DetailObserver extends BaseNetworkObserver<PreSellDetailActivity, List<String>> {

        public DetailObserver(PreSellDetailActivity preSellDetailActivity) {
            super(preSellDetailActivity);
        }

        @Override
        public void onNetworkSubscribe(PreSellDetailActivity preSellDetailActivity, Disposable d) {
            preSellDetailActivity.addDisposable(d);
        }

        @Override
        public void onNetworkNext(PreSellDetailActivity preSellDetailActivity, List<String> list) {
            if (list == null || list.size() < 1) {
                preSellDetailActivity.mLoadingViewController.switchEmpty();
            } else {
                preSellDetailActivity.mRecyclerDetail.setAdapter(new DetailAdapter(list));
                preSellDetailActivity.mLoadingViewController.switchSuccess();
            }
        }

        @Override
        public void onNetworkError(PreSellDetailActivity preSellDetailActivity, Throwable e) {
            preSellDetailActivity.mLoadingViewController.switchError();
        }
    }

    static class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailHolder> {

        private List<String> mInfoList;

        DetailAdapter(List<String> infoList) {
            mInfoList = infoList;
        }

        @NonNull
        @Override
        public DetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView textView = new TextView(parent.getContext());
            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, parent.getContext().getResources().getDisplayMetrics());
            marginLayoutParams.leftMargin = margin;
            marginLayoutParams.rightMargin = margin;
            marginLayoutParams.topMargin = (int) (margin / 2f + 0.5f);
            marginLayoutParams.bottomMargin = (int) (margin / 2f + 0.5f);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
            textView.setLayoutParams(marginLayoutParams);
            return new DetailHolder(textView);
        }

        @Override
        public void onBindViewHolder(@NonNull DetailHolder holder, int position) {
            holder.bindData(mInfoList.get(position));
        }

        @Override
        public int getItemCount() {
            return mInfoList.size();
        }

        class DetailHolder extends RecyclerView.ViewHolder {

            DetailHolder(View itemView) {
                super(itemView);
            }

            void bindData(String text) {
                ((TextView) itemView).setText(text);
            }

        }

    }

}
