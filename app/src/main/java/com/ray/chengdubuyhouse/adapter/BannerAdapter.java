package com.ray.chengdubuyhouse.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ray.chengdubuyhouse.activity.BannerDetailActivity;
import com.ray.chengdubuyhouse.R;
import com.ray.chengdubuyhouse.bean.BannerBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : hikobe8@github.com
 * Time : 2018/7/30 上午12:14
 * Description :
 */
public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerHolder> {

    private List<BannerBean> mBeanList = new ArrayList<>();

    public void refreshData( List<BannerBean> beanList ) {
        mBeanList.clear();
        mBeanList.addAll(beanList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BannerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BannerHolder holder, int position) {
        holder.bindData(mBeanList.get(position));
    }

    @Override
    public int getItemCount() {
        return mBeanList.size();
    }

    class BannerHolder extends RecyclerView.ViewHolder {

        TextView mTvName;
        ImageView mIvBanner;
        private BannerBean mBannerBean;

        BannerHolder(final View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.tv_banner);
            mIvBanner = itemView.findViewById(R.id.iv_banner);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBannerBean != null && !TextUtils.isEmpty(mBannerBean.getLink()))
                        BannerDetailActivity.launch(itemView.getContext(), mBannerBean.getLink());
                }
            });
        }

        void bindData(BannerBean bannerBean) {
            if (mBannerBean == null || !mBannerBean.equals(bannerBean)) {
                mTvName.setText(bannerBean.getName());
                Glide.with(itemView).load(bannerBean.getImageUrl()).into(mIvBanner);
                mBannerBean = bannerBean;
            }
        }
    }

}
