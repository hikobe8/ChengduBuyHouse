package com.ray.chengdubuyhouse.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.ray.chengdubuyhouse.db.DistrictRepository;
import com.ray.chengdubuyhouse.db.entity.DistrictEntity;

import java.util.List;

/**
 * Author : hikobe8@github.com
 * Time : 2018/8/19 上午11:11
 * Description :
 */
public class DistrictViewModel extends AndroidViewModel {

    private DistrictRepository mDistrictRepository;
    private LiveData<List<DistrictEntity>> mListLiveData;
    //只会在第一次启动的时候更新地区数据表
    public static boolean sDataUpdated;

    public DistrictViewModel(@NonNull Application application) {
        super(application);
        mDistrictRepository = new DistrictRepository(application);
        mListLiveData = mDistrictRepository.getAllDistricts();
    }

    public void insertDistricts(List<DistrictEntity> districtEntities){
        if (sDataUpdated)
            return;
        mDistrictRepository.insertDistricts(districtEntities);
        sDataUpdated = true;
    }

    public LiveData<List<DistrictEntity>> getListLiveData() {
        return mListLiveData;
    }

    public DistrictEntity getCachedDistrictEntity(){
        return mDistrictRepository.getCachedDistrictEntity();
    }

}
