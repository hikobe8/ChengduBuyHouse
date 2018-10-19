package com.ray.lib.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author : hikobe8@github.com
 * Time : 2018/7/27 上午12:11
 * Description :
 */
public class PreSellHouseDetailBean implements Parcelable{

    private String name;
    private String downloadUrl;

    public PreSellHouseDetailBean() {
    }

    protected PreSellHouseDetailBean(Parcel in) {
        name = in.readString();
        downloadUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(downloadUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PreSellHouseDetailBean> CREATOR = new Creator<PreSellHouseDetailBean>() {
        @Override
        public PreSellHouseDetailBean createFromParcel(Parcel in) {
            return new PreSellHouseDetailBean(in);
        }

        @Override
        public PreSellHouseDetailBean[] newArray(int size) {
            return new PreSellHouseDetailBean[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
