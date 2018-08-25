package com.ray.lib.bean;

import com.ray.lib.base.IKeepProguard;

/**
 * Author : hikobe8@github.com
 * Time : 2018/8/25 下午7:22
 * Description :
 */
public class PageableResponseBean<T> implements IKeepProguard {

    public PageableData pageableData;
    public T data;

    public PageableResponseBean() {
        pageableData = new PageableData();
    }
}
