package com.ray.chengdubuyhouse.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ray.chengdubuyhouse.R;
import com.ray.chengdubuyhouse.bean.QueryResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : hikobe8@github.com
 * Time : 2018/8/7 下午11:49
 * Description :
 */
public class QueryAdapter extends RecyclerView.Adapter<QueryAdapter.QueryHolder> {

    private List<QueryResultBean> mResultBeanList = new ArrayList<>();

    public void addData(List<QueryResultBean> dataList) {
        if (dataList != null) {
            mResultBeanList.addAll(dataList);
            notifyItemRangeInserted(getItemCount(), dataList.size());
        }
    }

    @NonNull
    @Override
    public QueryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QueryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_query, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QueryHolder holder, int position) {
        holder.bindData(mResultBeanList.get(position));
    }

    @Override
    public int getItemCount() {
        return mResultBeanList.size();
    }

    static class QueryHolder extends RecyclerView.ViewHolder {

        TextView mTvName;
        TextView mTvAddress;

        public QueryHolder(View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvAddress = itemView.findViewById(R.id.tv_address);
        }

        public void bindData(QueryResultBean resultBean) {
            mTvName.setText(resultBean.getProjectName());
            mTvAddress.setText(resultBean.getDistrict());
        }
    }

}
