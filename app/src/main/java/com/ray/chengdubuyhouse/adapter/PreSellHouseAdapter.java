package com.ray.chengdubuyhouse.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ray.chengdubuyhouse.activity.PreSellDetailActivity;
import com.ray.chengdubuyhouse.R;
import com.ray.lib.bean.BannerBean;
import com.ray.lib.bean.PreSellHouseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : hikobe8@github.com
 * Time : 2018/7/27 上午12:28
 * Description :
 */
public class PreSellHouseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PreSellHouseBean> mPreSellHouseBeanList = new ArrayList<>();

    private List<BannerBean> mBannerBeanList = new ArrayList<>();

    public void setData(List<PreSellHouseBean> data) {
        if (data != null) {
            mPreSellHouseBeanList.clear();
            mPreSellHouseBeanList.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void setBannerData( List<BannerBean> bannerBeans) {
        if (bannerBeans != null) {
            mBannerBeanList.clear();
            mBannerBeanList.addAll(bannerBeans);
            notifyItemInserted(0);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new BannerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner_holder, parent, false));
        }
        return new PreSellHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_presell_house, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BannerHolder) {
            ((BannerHolder)holder).bindBanner(mBannerBeanList);
        } else {
            ((PreSellHolder)holder).bind(mPreSellHouseBeanList.get(getItemPosition(position)));
        }
    }

    private int getItemPosition(int position) {
        return mBannerBeanList.size() > 0 ? position - 1 : position ;
    }

    @Override
    public int getItemCount() {
        return mBannerBeanList.size() > 0 ? mPreSellHouseBeanList.size() + 1 : mPreSellHouseBeanList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mBannerBeanList.size() > 0 ? position == 0 ? 0 : 1 : 1;
    }

    static class BannerHolder extends RecyclerView.ViewHolder {

        RecyclerView mBannerRecycler;
        BannerAdapter mBannerAdapter;
        List<BannerBean> mBannerBeanList = new ArrayList<>();


        public BannerHolder(View itemView) {
            super(itemView);
            mBannerRecycler = itemView.findViewById(R.id.recycler_banner);
            mBannerRecycler.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            mBannerRecycler.addItemDecoration(new BannerItemDecoration(itemView.getContext()));
            mBannerAdapter = new BannerAdapter();
        }

        public void bindBanner(List<BannerBean> bannerBeanList) {
            if (!mBannerBeanList.equals(bannerBeanList)) {
                mBannerBeanList.clear();
                mBannerBeanList.addAll(bannerBeanList);
                mBannerAdapter.refreshData(mBannerBeanList);
                mBannerRecycler.setAdapter(mBannerAdapter);
            }
        }
    }

    static class PreSellHolder extends RecyclerView.ViewHolder {

        TextView tvAddress;
        TextView tvName;
        TextView tvDate;
        private PreSellHouseBean mSellHouseBean;

        PreSellHolder(final View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDate = itemView.findViewById(R.id.tv_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSellHouseBean != null)
                        PreSellDetailActivity.launch(itemView.getContext(), mSellHouseBean.getLink());
                }
            });
        }

        public void bind(PreSellHouseBean houseBean) {
            if (houseBean != null && !houseBean.equals(mSellHouseBean)) {
                tvAddress.setText(houseBean.getAddress());
                tvName.setText(houseBean.getName());
                tvDate.setText(houseBean.getDate());
                mSellHouseBean = houseBean;
            }
        }

    }



}
