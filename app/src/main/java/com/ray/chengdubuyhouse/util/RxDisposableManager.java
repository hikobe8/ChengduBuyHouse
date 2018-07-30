package com.ray.chengdubuyhouse.util;

import java.util.HashSet;
import java.util.Set;

import io.reactivex.disposables.Disposable;

/**
 * Author : hikobe8@github.com
 * Time : 2018/7/30 下午10:35
 * Description :
 */
public class RxDisposableManager {

    //avoid duplicate disposables
    private Set<Disposable> mDisposables = new HashSet<>();

    public void addDisposable (Disposable disposable) {
        if (disposable != null)
            mDisposables.add(disposable);
    }

    public void dispose() {
        for (Disposable disposable : mDisposables) {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
        mDisposables.clear();
    }

}
