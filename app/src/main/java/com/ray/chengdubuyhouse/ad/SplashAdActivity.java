package com.ray.chengdubuyhouse.ad;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.miui.zeus.mimo.sdk.ad.AdWorkerFactory;
import com.miui.zeus.mimo.sdk.ad.IAdWorker;
import com.miui.zeus.mimo.sdk.listener.MimoAdListener;
import com.ray.chengdubuyhouse.R;
import com.ray.chengdubuyhouse.activity.MainActivity;
import com.xiaomi.ad.common.pojo.AdType;


/**
 * 您可以参考本类中的代码来接入小米广告SDK中的开屏广告。在接入过程中，有如下事项需要注意：
 * 1.请将POSITION_ID值替换成您在小米开发者网站上申请的开屏广告位。
 */
public class SplashAdActivity extends Activity {
    private static final String TAG = "SplashAdActivity";
    //以下的POSITION_ID 需要使用您申请的值替换下面内容
    private static final String POSITION_ID = "b373ee903da0c6fc9c9da202df95a500";
    private ViewGroup mContainer;
    private IAdWorker mWorker;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mHandler.postDelayed(mRunnable, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashad);
        mRunnable = new Runnable() {
            @Override
            public void run() {
                jump2Home();
            }
        };
        checkPermission();
        mContainer = findViewById(R.id.splash_ad_container);
        try {
            mWorker = AdWorkerFactory.getAdWorker(this, mContainer, new MimoAdListener() {
                @Override
                public void onAdPresent() {
                    // 开屏广告展示
                    Log.d(TAG, "onAdPresent");
                    mHandler.removeCallbacks(mRunnable);
                }

                @Override
                public void onAdClick() {
                    //用户点击了开屏广告
                    Log.d(TAG, "onAdClick");
                }

                @Override
                public void onAdDismissed() {
                    //这个方法被调用时，表示从开屏广告消失。
                    Log.d(TAG, "onAdDismissed");
                    jump2Home();
                }

                @Override
                public void onAdFailed(String s) {
                    Log.e(TAG, "ad fail message : " + s);
                }

                @Override
                public void onAdLoaded(int size) {
                }

                @Override
                public void onStimulateSuccess() {
                }
            }, AdType.AD_SPLASH);

            mWorker.loadAndShow(POSITION_ID);
        } catch (Exception e) {
            e.printStackTrace();
            mContainer.setVisibility(View.GONE);
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE}, 0);
            } else {
                mHandler.postDelayed(mRunnable, 2000);
            }
        } else {
            mHandler.postDelayed(mRunnable, 2000);
        }
    }

    private void jump2Home() {
        MainActivity.launch(SplashAdActivity.this);
        finish();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            // 捕获back键，在展示广告期间按back键，不跳过广告
            if (mContainer.getVisibility() == View.VISIBLE) {
                return true;
            }
            jump2Home();
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            mWorker.recycle();
            mHandler.removeCallbacks(mRunnable);
        } catch (Exception e) {
        }
    }
}