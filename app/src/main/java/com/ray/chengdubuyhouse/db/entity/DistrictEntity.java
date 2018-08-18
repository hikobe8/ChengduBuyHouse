package com.ray.chengdubuyhouse.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.ray.chengdubuyhouse.IKeepProguard;

/**
 * Author : hikobe8@github.com
 * Time : 2018/8/18 下午11:09
 * Description :
 */
@Entity(tableName = DistrictEntity.TABLE_NAME)
public class DistrictEntity implements IKeepProguard {

    static final String TABLE_NAME = "district";

    @PrimaryKey
    @ColumnInfo(name = "code")
    private int mRegionCode;
    @ColumnInfo(name = "name")
    private String name;

    public DistrictEntity(int regionCode, String name) {
        this.mRegionCode = regionCode;
        this.name = name;
    }

    public int getRegionCode() {
        return mRegionCode;
    }

    public void setRegionCode(int regionCode) {
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

        DistrictEntity that = (DistrictEntity) o;

        if (mRegionCode != that.mRegionCode) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = mRegionCode;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DistrictEntity{" +
                "mRegionCode=" + mRegionCode +
                ", name='" + name + '\'' +
                '}';
    }
}
