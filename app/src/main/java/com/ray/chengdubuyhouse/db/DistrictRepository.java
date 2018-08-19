package com.ray.chengdubuyhouse.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.google.gson.Gson;
import com.ray.chengdubuyhouse.db.dao.DistrictDao;
import com.ray.chengdubuyhouse.db.entity.DistrictEntity;
import com.ray.chengdubuyhouse.util.SPUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
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
    private Disposable mSearchDisposable = null;
    private Application mApplication;

    public DistrictRepository(Application application) {
        mApplication = application;
        mDistrictDao = HouseDatabase.getInstance(application).districtDao();
        mAllDistricts = mDistrictDao.getAllDistricts();
    }

    public LiveData<List<DistrictEntity>> getAllDistricts() {
        return mAllDistricts;
    }

    public DistrictEntity getCachedDistrictEntity(){
        String json = SPUtil.getInstance(mApplication).getString(SPUtil.KEY.KEY_SELECTED_DISTRICT);
        Gson gson = new Gson();
        try {
            return gson.fromJson(json, DistrictEntity.class);
        } catch (Exception e){
            return null;
        }
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
