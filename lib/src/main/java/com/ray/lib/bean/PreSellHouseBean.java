package com.ray.lib.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author : hikobe8@github.com
 * Time : 2018/7/27 上午12:11
 * Description :
 */
public class PreSellHouseBean implements Parcelable{

    private String address;
    private String name;
    private String link;
    private String date;

    public PreSellHouseBean() {
    }

    protected PreSellHouseBean(Parcel in) {
        address = in.readString();
        name = in.readString();
        link = in.readString();
        date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(name);
        dest.writeString(link);
        dest.writeString(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PreSellHouseBean> CREATOR = new Creator<PreSellHouseBean>() {
        @Override
        public PreSellHouseBean createFromParcel(Parcel in) {
            return new PreSellHouseBean(in);
        }

        @Override
        public PreSellHouseBean[] newArray(int size) {
            return new PreSellHouseBean[size];
        }
    };

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PreSellHouseBean that = (PreSellHouseBean) o;

        return (address != null ? address.equals(that.address) : that.address == null) && (name != null ? name.equals(that.name) : that.name == null) && (link != null ? link.equals(that.link) : that.link == null);
    }

    @Override
    public int hashCode() {
        int result = address != null ? address.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (link != null ? link.hashCode() : 0);
        return result;
    }
}
