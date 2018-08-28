package com.ray.chengdubuyhouse.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ray.chengdubuyhouse.R;
import com.ray.lib.bean.QueryResultBean;
import com.ray.lib.loadmore_recyclerview.LoadMoreAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : hikobe8@github.com
 * Time : 2018/8/7 下午11:49
 * Description :
 */
public class QueryAdapter extends LoadMoreAdapter {

    private List<QueryResultBean> mResultBeanList = new ArrayList<>();

    public interface CallPhoneCallback {
        void onMakeCall(String phone);
    }

    private CallPhoneCallback mCallPhoneCallback;

    public void setCallPhoneCallback(CallPhoneCallback callPhoneCallback) {
        mCallPhoneCallback = callPhoneCallback;
    }

    public void refreshData(List<QueryResultBean> dataList) {
        if (dataList != null) {
            mResultBeanList.clear();
            mResultBeanList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    public void addData(List<QueryResultBean> dataList) {
        if (dataList != null) {
            int size = getNormalItemCount();
            mResultBeanList.addAll(dataList);
            notifyItemRangeInserted(size, dataList.size());
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateNormalViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QueryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_query, parent, false));
    }

    @Override
    protected void onBindNormalViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((QueryHolder)holder).bindData(mResultBeanList.get(position));
        ((QueryHolder)holder).mIvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((QueryHolder)holder).mQueryResultBean != null && !TextUtils.isEmpty(((QueryHolder)holder).mQueryResultBean.getPhone())) {
                    if (mCallPhoneCallback != null) {
                        mCallPhoneCallback.onMakeCall(((QueryHolder)holder).mQueryResultBean.getPhone());
                    }
                }
            }
        });
    }

    @Override
    public int getNormalItemCount() {
        return mResultBeanList.size();
    }

    static class QueryHolder extends RecyclerView.ViewHolder {

        TextView mTvName;
        TextView mTvAddress;
        TextView mTvNo;
        TextView mTvRange;
        TextView mTvCount;
        TextView mTvPhone;
        View mIvPhone;
        TextView mTvStartEndTime;
        TextView mTvSelectTime;
        TextView mTvStatus;
        QueryResultBean mQueryResultBean;

        QueryHolder(View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvAddress = itemView.findViewById(R.id.tv_address);
            mTvNo = itemView.findViewById(R.id.tv_no);
            mTvRange = itemView.findViewById(R.id.tv_range);
            mTvCount = itemView.findViewById(R.id.tv_count);
            mTvPhone = itemView.findViewById(R.id.tv_phone);
            mTvStartEndTime = itemView.findViewById(R.id.tv_start_end_time);
            mTvSelectTime = itemView.findViewById(R.id.tv_select_time);
            mTvStatus = itemView.findViewById(R.id.tv_status);
            mIvPhone = itemView.findViewById(R.id.iv_call);
        }

        public void bindData(final QueryResultBean resultBean) {
            if (resultBean != null && !resultBean.equals(mQueryResultBean)) {
                mTvName.setText(resultBean.getProjectName());
                mTvAddress.setText(resultBean.getDistrict());
                Context context = itemView.getContext();
                mTvNo.setText(context.getString(R.string.text_query_no, resultBean.getSellNo()));
                mTvRange.setText(context.getString(R.string.text_query_range, resultBean.getRange()));
                mTvCount.setText(context.getString(R.string.text_query_count, resultBean.getHouseCount()));
                mTvPhone.setText(context.getString(R.string.text_query_phone, resultBean.getPhone()));
                mTvStartEndTime.setText(context.getString(R.string.text_query_start_end_time, resultBean.getStartTime(), resultBean.getEndTime()));
                mTvSelectTime.setText(context.getString(R.string.text_query_select_time, resultBean.getSelectEndTime()));
                if ("正在报名".equals(resultBean.getStatus())) {
                    mTvStatus.setTextColor(context.getResources().getColor(R.color.colorMain));
                } else if ("未报名".equals(resultBean.getStatus())) {
                    mTvStatus.setTextColor(context.getResources().getColor(R.color.colorWaiting));
                } else if ("报名结束".equals(resultBean.getStatus())) {
                    mTvStatus.setTextColor(context.getResources().getColor(R.color.colorAccent));
                }
                mTvStatus.setText(resultBean.getStatus());
                mQueryResultBean = resultBean;
            }

        }
    }

}
