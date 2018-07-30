package com.ray.chengdubuyhouse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.ray.chengdubuyhouse.util.RxDisposableManager;

import io.reactivex.disposables.Disposable;

/**
 * Author : hikobe8@github.com
 * Time : 2018/7/30 下午10:49
 * Description :
 */
public class BaseFragment extends Fragment {

    private RxDisposableManager mRxDisposableManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxDisposableManager = new RxDisposableManager();
    }

    protected void addDisposable(Disposable disposable) {
        mRxDisposableManager.addDisposable(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRxDisposableManager.dispose();
    }
}
