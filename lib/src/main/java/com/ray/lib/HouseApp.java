package com.ray.lib;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Author : hikobe8@github.com
 * Time : 2018/7/30 下午10:59
 * Description :
 */
public class HouseApp extends Application{

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
    }
}
