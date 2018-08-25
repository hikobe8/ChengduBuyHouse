package com.ray.chengdubuyhouse.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ray.chengdubuyhouse.R;
import com.ray.lib.bean.BannerDetailBean;

import java.util.List;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-07-31 11:51
 *  description : 
 */
public class BannerDetailContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BannerDetailBean> mDetailBeanList;

    public BannerDetailContentAdapter(List<BannerDetailBean> detailBeanList) {
        mDetailBeanList = detailBeanList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new NormalHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner_detail_text, parent, false));
        } else {
            return new ImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner_detail_image, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalHolder) {
            ((NormalHolder)holder).bind(mDetailBeanList.get(position).getText());
        } else if (holder instanceof ImageHolder) {
            ((ImageHolder)holder).bind(mDetailBeanList.get(position).getText());
        }
    }

    @Override
    public int getItemCount() {
        return mDetailBeanList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDetailBeanList.get(position).isImage() ? 1 : 0;
    }

    static class NormalHolder extends RecyclerView.ViewHolder {

        NormalHolder(View itemView) {
            super(itemView);
        }

        void bind(String text) {
            ((TextView)itemView).setText(text);
        }

    }

    static class ImageHolder extends RecyclerView.ViewHolder {

        ImageHolder(View itemView) {
            super(itemView);
        }

        void bind(String text) {
            Glide.with(itemView.getContext()).load(text).into((ImageView) itemView);
        }
    }

}
