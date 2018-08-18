package com.ray.chengdubuyhouse.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.ray.chengdubuyhouse.db.entity.DistrictEntity;

import java.util.List;

/**
 * Author : hikobe8@github.com
 * Time : 2018/8/18 下午11:20
 * Description :
 */
@Dao
public interface DistrictDao {

    @Insert
    public void insertDistrict(DistrictEntity entity);

    @Query("DELETE FROM district")
    void deleteAll();

    @Query("SELECT * FROM district ORDER BY code ASC")
    LiveData<List<DistrictEntity>> getAllDistricts();

}
