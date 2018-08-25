package com.ray.lib.base;

import java.lang.ref.WeakReference;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Author : hikobe8@github.com
 * Time : 2018/8/25 下午4:57
 * Description : 网络请求的观察者，使用弱引用解决页面需要销毁时还在请求的内存泄漏问题
 */
public abstract class BaseNetworkObserver<Context, Data> implements Observer<Data> {

    private WeakReference<Context> mActivityWeakReference;

    public BaseNetworkObserver(Context context) {
        mActivityWeakReference = new WeakReference<>(context);
    }

    @Override
    public void onSubscribe(Disposable d) {
        Context context = mActivityWeakReference.get();
        if (context != null) {
            onNetworkSubscribe(context, d);
        }
    }

    @Override
    public void onNext(Data data) {
        Context context = mActivityWeakReference.get();
        if (context != null) {
            onNetworkNext(context, data);
        }
    }

    @Override
    public void onError(Throwable e) {
        Context context = mActivityWeakReference.get();
        if (context != null) {
            onNetworkError(context, e);
        }
    }

    @Override
    public void onComplete() {
        Context context = mActivityWeakReference.get();
        if (context != null) {
            onNetworkComplete(context);
        }
    }

    public abstract void onNetworkSubscribe(Context context, Disposable d);

    public abstract void onNetworkNext(Context context, Data data);

    public abstract void onNetworkError(Context context, Throwable e);

    public void onNetworkComplete(Context context) {}
}
