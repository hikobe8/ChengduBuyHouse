package com.ray.lib.loadmore_recyclerview;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author : hikobe8@github.com
 * Time : 2018/8/22 下午11:18
 * Description :
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({
        LoadingMoreType.TYPE_LAST,
        LoadingMoreType.TYPE_LOADING,
        LoadingMoreType.TYPE_ERROR
})
public @interface LoadingMoreType {

    int TYPE_LAST = 0;
    int TYPE_LOADING = 1;
    int TYPE_ERROR = 2;

}
