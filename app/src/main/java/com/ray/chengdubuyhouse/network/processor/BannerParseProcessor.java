package com.ray.chengdubuyhouse.network.processor;

import com.ray.chengdubuyhouse.bean.BannerBean;
import com.ray.chengdubuyhouse.network.NetworkConstant;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : hikobe8@github.com
 * Time : 2018/7/31 上午12:42
 * Description :
 */
public class BannerParseProcessor implements IHtmlParseProcessor<List<BannerBean>>{
    @Override
    public List<BannerBean> processParse(Document doc) {
        Elements bannerWrapperList = doc.select("div.scrcont > div > a");
        List<BannerBean> result = new ArrayList<>();
        for (Element bannerWrapper : bannerWrapperList) {
            BannerBean bannerBean = new BannerBean();
            bannerBean.setName(bannerWrapper.selectFirst("span").text());
            bannerBean.setLink(NetworkConstant.HOST+bannerWrapper.attr("href"));
            bannerBean.setImageUrl(NetworkConstant.HOST+bannerWrapper.selectFirst("img").attr("src"));
            result.add(bannerBean);
        }
        return result;
    }
}
