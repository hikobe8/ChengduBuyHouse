package com.ray.lib.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Author : hikobe8@github.com
 * Time : 2018/8/18 下午11:09
 * Description :
 */
@Entity(tableName = DistrictEntity.TABLE_NAME)
public class DistrictEntity implements Parcelable {

    static final String TABLE_NAME = "district";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "code")
    private String mRegionCode;
    @ColumnInfo(name = "name")
    private String name;

    public DistrictEntity(String regionCode, String name) {
        this.mRegionCode = regionCode;
        this.name = name;
    }

    protected DistrictEntity(Parcel in) {
        mRegionCode = in.readString();
        name = in.readString();
    }

    public static final Creator<DistrictEntity> CREATOR = new Creator<DistrictEntity>() {
        @Override
        public DistrictEntity createFromParcel(Parcel in) {
            return new DistrictEntity(in);
        }

        @Override
        public DistrictEntity[] newArray(int size) {
            return new DistrictEntity[size];
        }
    };

    public String getRegionCode() {
        return mRegionCode;
    }

    public void setRegionCode(String regionCode) {
        mRegionCode = regionCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DistrictEntity entity = (DistrictEntity) o;

        if (mRegionCode != null ? !mRegionCode.equals(entity.mRegionCode) : entity.mRegionCode != null)
            return false;
        return name != null ? name.equals(entity.name) : entity.name == null;
    }

    @Override
    public int hashCode() {
        int result = mRegionCode != null ? mRegionCode.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "mRegionCode=" + mRegionCode +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mRegionCode);
        dest.writeString(name);
    }
}
