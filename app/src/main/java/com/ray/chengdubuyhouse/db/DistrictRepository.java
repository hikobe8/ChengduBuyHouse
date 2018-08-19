package com.ray.chengdubuyhouse.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.ray.chengdubuyhouse.db.dao.DistrictDao;
import com.ray.chengdubuyhouse.db.entity.DistrictEntity;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Author : hikobe8@github.com
 * Time : 2018/8/19 上午10:50
 * Description :
 */
public class DistrictRepository {

    private DistrictDao mDistrictDao;
    private LiveData<List<DistrictEntity>> mAllDistricts;
    private Disposable mInsertDisposable = null;

    public DistrictRepository(Application application) {
        mDistrictDao = HouseDatabase.getInstance(application).districtDao();
        mAllDistricts = mDistrictDao.getAllDistricts();
    }

    public LiveData<List<DistrictEntity>> getAllDistricts() {
        return mAllDistricts;
    }

    public void insertDistricts(List<DistrictEntity> districtEntities) {
        Observable.just(districtEntities)
                .flatMap(new Function<List<DistrictEntity>, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(List<DistrictEntity> districtEntities) throws Exception {
                        for (DistrictEntity districtEntity : districtEntities) {
                            DistrictEntity entity = mDistrictDao.searchDistrict(districtEntity.getRegionCode());
                            if (entity == null) {
                                mDistrictDao.insertDistrict(districtEntity);
                            } else {
                                mDistrictDao.updateDistrict(districtEntity);
                            }
                        }
                        return Observable.just(0);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mInsertDisposable = d;
                    }

                    @Override
                    public void onNext(Integer result) {
                        if (mInsertDisposable != null)
                            mInsertDisposable.dispose();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
