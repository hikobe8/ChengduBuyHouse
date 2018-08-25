package com.ray.lib.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Author : hikobe8@github.com
 * Time : 2018/8/20 上午12:11
 * Description :
 */
public class SPUtil {

    private static final String SP_NAME = "house_sp";
    private static SPUtil sInstance;
    private static SharedPreferences sSharedPreferences;

    public static SPUtil getInstance(Context context) {
        if (sInstance == null) {
            synchronized (SPUtil.class) {
                if (sInstance == null) {
                    sInstance = new SPUtil(context);
                }
            }
        }
        return sInstance;
    }

    private SPUtil(Context context) {
        sSharedPreferences = context.getApplicationContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public interface KEY{
        String KEY_SELECTED_DISTRICT = "key_selected_district";
    }

    public void putString(String key, String value) {
        sSharedPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key) {
        return sSharedPreferences.getString(key, "");
    }

}
