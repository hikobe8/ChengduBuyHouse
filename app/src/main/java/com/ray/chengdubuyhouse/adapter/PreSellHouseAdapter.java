package com.ray.chengdubuyhouse.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ray.chengdubuyhouse.R;
import com.ray.chengdubuyhouse.bean.PreSellHouseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : hikobe8@github.com
 * Time : 2018/7/27 上午12:28
 * Description :
 */
public class PreSellHouseAdapter extends RecyclerView.Adapter<PreSellHouseAdapter.PreSellHolder> {

    private List<PreSellHouseBean> mPreSellHouseBeanList = new ArrayList<>();

    public void setData(List<PreSellHouseBean> data) {
        if (data != null) {
            mPreSellHouseBeanList.clear();
            mPreSellHouseBeanList.addAll(data);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public PreSellHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PreSellHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_presell_house, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PreSellHolder holder, int position) {
        holder.bind(mPreSellHouseBeanList.get(position));
    }

    @Override
    public int getItemCount() {
        return mPreSellHouseBeanList.size();
    }

    class PreSellHolder extends RecyclerView.ViewHolder {

        TextView tvAddress;
        TextView tvName;
        TextView tvDate;
        private PreSellHouseBean mSellHouseBean;

        PreSellHolder(View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDate = itemView.findViewById(R.id.tv_date);
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
