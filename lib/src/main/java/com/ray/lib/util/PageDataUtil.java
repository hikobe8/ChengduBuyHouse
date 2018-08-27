package com.ray.lib.util;

import com.ray.lib.bean.PageableResponseBean;
import com.ray.lib.bean.PreSellHouseBean;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author : hikobe8@github.com
 * Time : 2018/8/25 下午7:48
 * Description :
 */
public class PageDataUtil {

    public static void fillPageableData(String pageText, PageableResponseBean pageableResponseBean) {
        String[] split = pageText.split("/");
        pageableResponseBean.pageableData.page = Integer.valueOf(split[0]);
        String totalText = split[1];
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(totalText);
        totalText = matcher.replaceAll("");
        pageableResponseBean.pageableData.total = Integer.valueOf(totalText);
    }

    public static int getSingleIngeterValue(String pageText) {
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(pageText);
        try {
            return Integer.valueOf(matcher.replaceAll(""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
