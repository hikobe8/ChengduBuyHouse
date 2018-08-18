package com.ray.chengdubuyhouse.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ray.chengdubuyhouse.db.dao.DistrictDao;
import com.ray.chengdubuyhouse.db.entity.DistrictEntity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
                            .addCallback(tempCallback)
                            .build();
                }
            }
        }
        return sInstance;
    }

    public abstract DistrictDao districtDao();

    private static RoomDatabase.Callback tempCallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                    DistrictDao districtDao = sInstance.districtDao();
                    for (int i = 0; i < 10; i++) {
                        districtDao.insertDistrict(new DistrictEntity(i, "西虹市"+i));
                    }
                    emitter.onNext(0);
                    emitter.onComplete();
                }
            }).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe(new Observer<Integer>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Integer integer) {
                    Log.w("temp", "数据插入成功");
                }

                @Override
                public void onError(Throwable e) {
                    Log.w("temp", "数据插入失败 : " + e.getMessage());
                }

                @Override
                public void onComplete() {

                }
            });
        }
    };

}
