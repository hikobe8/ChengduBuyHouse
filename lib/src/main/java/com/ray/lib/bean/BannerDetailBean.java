package com.ray.lib.bean;

import android.os.Parcel;
import android.os.Parcelable;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-07-31 10:10
 *  description : 
 */
public class BannerDetailBean implements Parcelable {

    private String text;
    private boolean isImage;

    public BannerDetailBean() {
    }

    protected BannerDetailBean(Parcel in) {
        text = in.readString();
        isImage = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeByte((byte) (isImage ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BannerDetailBean> CREATOR = new Creator<BannerDetailBean>() {
        @Override
        public BannerDetailBean createFromParcel(Parcel in) {
            return new BannerDetailBean(in);
        }

        @Override
        public BannerDetailBean[] newArray(int size) {
            return new BannerDetailBean[size];
        }
    };

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

}
