package com.ray.lib.download;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.ray.lib.BuildConfig;
import com.ray.lib.network.DownloadPackage;
import com.ray.lib.util.RxDisposableManager;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class DownloadService extends Service {

    private RxDisposableManager mRxDisposableManager;
    public static final String TAG = "DownloadService";

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    private Map<String, DownloadPackage> mDownloadingPacks = new LinkedHashMap<>();
    private Map<String, ProgressListener> mProgressListener = new HashMap<>();

    public DownloadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mRxDisposableManager = new RxDisposableManager();
    }

    public class LocalBinder extends Binder {
        public DownloadService getService() {
            // Return this instance of LocalService so clients can call public methods
            return DownloadService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void startDownload(final String downloadUrl) {
        Toast.makeText(this,"开始下载！", Toast.LENGTH_SHORT).show();
        RxDownloader.getInstance().download(downloadUrl).subscribe(new Observer<DownloadPackage>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxDisposableManager.addDisposable(d);
            }

            @Override
            public void onNext(DownloadPackage downloadPackage) {
                mDownloadingPacks.put(downloadUrl, downloadPackage);
                publishProgress();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                mDownloadingPacks.remove(downloadUrl);
            }
        });
    }

    private void publishProgress(){
        Set<String> keySet = mDownloadingPacks.keySet();
        for (String s : keySet) {
            ProgressListener progressListener = mProgressListener.get(s);
            if (progressListener != null) {
                progressListener.onProgressUpdated(s, mDownloadingPacks.get(s));
            }
        }
    }

    public interface ProgressListener {

        void onProgressUpdated(String downloadUrl, DownloadPackage downloadPackage);

    }

    public void addProgressListener(String downloadUrl, ProgressListener progressListener){
        mProgressListener.put(downloadUrl, progressListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "下载服务已启动！");
        }
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDownloadingPacks.clear();
        mProgressListener.clear();
        mRxDisposableManager.dispose();
    }
}
