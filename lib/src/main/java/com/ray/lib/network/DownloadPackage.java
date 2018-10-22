package com.ray.lib.network;

import com.ray.lib.base.IKeepProguard;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-10-22 14:14
 *  description : 
 */
public class DownloadPackage implements IKeepProguard{

    public long contentLength;
    public float finishedRate;
    public boolean downloadFinished;
    public String filePath;

}
