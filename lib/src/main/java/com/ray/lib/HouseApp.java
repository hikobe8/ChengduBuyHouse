package com.ray.lib;

import android.app.Application;

import com.miui.zeus.mimo.sdk.MimoSdk;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Author : hikobe8@github.com
 * Time : 2018/7/30 下午10:59
 * Description :
 */
public class HouseApp extends Application{

    // 请注意，千万要把以下的 APP_ID 替换成您在小米开发者网站上申请的 AppID。否则，可能会影响你的应用广告收益。
    private static final String APP_ID = "2882303761517411490";
    // 以下两个没有的话就按照以下传入
    private static final String APP_KEY = "fake_app_key";
    private static final String APP_TOKEN = "fake_app_token";

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        CrashReport.initCrashReport(getApplicationContext(), "cfd5a52743", BuildConfig.DEBUG);
        /**
         * new sdk
         */
        MimoSdk.setDebugOn();
        // 正式上线时候务必关闭stage
        MimoSdk.setStageOn();

        // 如需预置插件请在assets目录下添加mimo_assets.apk
        MimoSdk.init(this, APP_ID, APP_KEY, APP_TOKEN);
    }
}
