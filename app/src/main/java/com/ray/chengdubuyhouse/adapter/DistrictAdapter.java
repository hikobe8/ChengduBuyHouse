package com.ray.chengdubuyhouse.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ray.chengdubuyhouse.R;
import com.ray.chengdubuyhouse.db.entity.DistrictEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : hikobe8@github.com
 * Time : 2018/8/19 上午12:13
 * Description :
 */
public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.DistrictHolder> {

    private List<DistrictEntity> mDistrictEntityList = new ArrayList<>();

    public void setData(List<DistrictEntity> districtEntityList) {
        mDistrictEntityList.clear();
        mDistrictEntityList.addAll(districtEntityList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DistrictHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DistrictHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_district, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DistrictHolder holder, int position) {
        holder.bindData(mDistrictEntityList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDistrictEntityList.size();
    }

    static class DistrictHolder extends RecyclerView.ViewHolder {

        private DistrictEntity mDistrictEntity;
        private TextView mTvName;

        DistrictHolder(View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
        }

        void bindData(DistrictEntity districtEntity) {
            if (districtEntity != null && !districtEntity.equals(mDistrictEntity)) {
                mDistrictEntity = districtEntity;
                mTvName.setText(mDistrictEntity.getName());
            }
        }

    }

}
