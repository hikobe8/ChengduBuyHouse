package com.ray.lib.bean;

import com.ray.lib.base.IKeepProguard;

/**
 * Author : hikobe8@github.com
 * Time : 2018/8/25 下午7:50
 * Description :
 */
public class PageableData implements IKeepProguard {

    public int page = 1;
    public int total;

    public void reset(){
        page = 1;
        total = 0;
    }

    public boolean isLastPage(){
        return page >= total;
    }

}
