package com.ray.chengdubuyhouse;

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

import com.ray.chengdubuyhouse.parser.HtmlParser;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class PreSellDetailActivity extends BaseActivity {

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
        final RecyclerView recyclerDetail = findViewById(R.id.recycler_detail);
        recyclerDetail.setLayoutManager(new LinearLayoutManager(this));
        recyclerDetail.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        String url = getIntent().getStringExtra("url");
        HtmlParser.getInstance().parsePreSellDetailHtml(url, new HtmlParser.DetailCallback() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<String> datas) {
                recyclerDetail.setAdapter(new DetailAdapter(datas));
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    static class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailHolder> {

        private List<String> mInfos;

        public DetailAdapter(List<String> infos) {
            mInfos = infos;
        }

        @NonNull
        @Override
        public DetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView textView = new TextView(parent.getContext());
            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, parent.getContext().getResources().getDisplayMetrics());
            marginLayoutParams.leftMargin = margin;
            marginLayoutParams.rightMargin = margin;
            marginLayoutParams.topMargin = (int) (margin/2f + 0.5f);
            marginLayoutParams.bottomMargin = (int) (margin/2f + 0.5f);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
            textView.setLayoutParams(marginLayoutParams);
            return new DetailHolder(textView);
        }

        @Override
        public void onBindViewHolder(@NonNull DetailHolder holder, int position) {
            holder.bindData(mInfos.get(position));
        }

        @Override
        public int getItemCount() {
            return mInfos.size();
        }

        class DetailHolder extends RecyclerView.ViewHolder {

            DetailHolder(View itemView) {
                super(itemView);
            }

            void bindData(String text) {
                ((TextView)itemView).setText(text);
            }

        }

    }

}