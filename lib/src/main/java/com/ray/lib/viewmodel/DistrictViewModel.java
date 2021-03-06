package com.ray.lib.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ray.lib.db.DistrictRepository;
import com.ray.lib.db.entity.DistrictEntity;

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
        DistrictEntity cachedDistrictEntity = mDistrictRepository.getCachedDistrictEntity();
        if (cachedDistrictEntity == null) {
            cachedDistrictEntity = new DistrictEntity("00", "所有区域");
        } else {
            if (TextUtils.isEmpty(cachedDistrictEntity.getName()) || TextUtils.isEmpty(cachedDistrictEntity.getRegionCode())) {
                cachedDistrictEntity.setName("所有区域");
                cachedDistrictEntity.setRegionCode("00");
            }
        }
        return cachedDistrictEntity;
    }

}
