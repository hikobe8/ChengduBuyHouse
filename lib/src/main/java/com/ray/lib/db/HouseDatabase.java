package com.ray.lib.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.ray.lib.db.dao.DistrictDao;
import com.ray.lib.db.entity.DistrictEntity;

/**
 * Author : hikobe8@github.com
 * Time : 2018/8/18 下午11:24
 * Description :
 */
@Database(entities = {DistrictEntity.class}, version = HouseDatabase.DB_VERSION, exportSchema = false)
public abstract class HouseDatabase extends RoomDatabase {

    static final String DB_NAME = "house_db";
    static final int DB_VERSION = 1;
    private static HouseDatabase sInstance;

    public static HouseDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (HouseDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room
                            .databaseBuilder(
                                    context.getApplicationContext(),
                                    HouseDatabase.class,
                                    HouseDatabase.DB_NAME)
                            .build();
                }
            }
        }
        return sInstance;
    }

    public abstract DistrictDao districtDao();

}
