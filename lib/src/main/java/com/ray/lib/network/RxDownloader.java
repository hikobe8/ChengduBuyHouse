package com.ray.lib.network;

import android.util.Log;

import com.ray.lib.util.StorageUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-10-22 10:27
 *  description : 下载管理
 */
public class RxDownloader {

    public static final String TAG = "RxDownloader";

    public RxDownloader() {
        if (RxJavaPlugins.getErrorHandler() == null)
            RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) {
                    Log.e("ErrorHandler", throwable.getMessage());
                }
            });
    }

    public static class SingletonHolder {
        static RxDownloader sInstance = new RxDownloader();
    }

    public static RxDownloader getInstance() {
        return SingletonHolder.sInstance;
    }

    private Call internalDownloadByOkHttp(String downloadUrl) {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .get()
                .build();
        return client.newCall(request);
    }

    public Observable<DownloadPackage> download(final String downloadUrl) {
        return Observable.create(new ObservableOnSubscribe<DownloadPackage>() {
            @Override
            public void subscribe(ObservableEmitter<DownloadPackage> emitter) throws Exception {
                Call downloadByOkHttp = internalDownloadByOkHttp(downloadUrl);
                Response response = downloadByOkHttp.execute();
                if (response != null) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        long contentLength = body.contentLength();
                        DownloadPackage downloadPackage = new DownloadPackage();
                        downloadPackage.contentLength = contentLength;
                        InputStream inputStream = body.byteStream();
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                        byte[] buffer = new byte[1024];
                        String downloadDir = StorageUtil.getDownloadDir();
                        File downloadFile = new File(downloadDir, downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1));
                        if (downloadFile.exists()) {
                            downloadFile.delete();
                        }
                        FileOutputStream fileOutputStream = new FileOutputStream(downloadFile);
                        downloadPackage.filePath = downloadFile.getAbsolutePath();
                        int len;
                        while ((len = bufferedInputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, len);
                            downloadPackage.finishedRate += (len * 1f)/downloadPackage.contentLength;
                            emitter.onNext(downloadPackage);
                        }
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        bufferedInputStream.close();
                        downloadPackage.downloadFinished = true;
                        emitter.onNext(downloadPackage);
                        emitter.onComplete();
                    }
                    return;
                }
                emitter.onError(new Throwable("download data error!"));
            }
        })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
