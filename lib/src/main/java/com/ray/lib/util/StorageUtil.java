package com.ray.lib.util;

import android.os.Environment;

import com.ray.lib.BuildConfig;

import java.io.File;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-10-22 11:28
 *  description : 
 */
public class StorageUtil {

    public static String getDownloadDir(){
        String downloadDir = Environment.getDownloadCacheDirectory().getAbsolutePath();
        boolean extStorageExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (extStorageExist) {
            downloadDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        File dirFile = new File(downloadDir, BuildConfig.APPLICATION_ID);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        return dirFile.getAbsolutePath();
    }

}
