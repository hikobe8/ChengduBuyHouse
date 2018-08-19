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

    public interface OnItemClickListener {
        void onItemClick(DistrictEntity districtEntity);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

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
    public void onBindViewHolder(@NonNull final DistrictHolder holder, final int position) {
        holder.bindData(mDistrictEntityList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(holder.mDistrictEntity);
                }
            }
        });
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
