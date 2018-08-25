package com.ray.lib.network.processor;

import android.text.TextUtils;


import com.ray.lib.bean.BannerDetailBean;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-07-31 10:07
 *  description : 
 */
public class BannerDetailParseProcessor implements IHtmlParseProcessor<List<BannerDetailBean>> {

    private static final String IMG_STYLE_VALUE = "text-align:center;";

    @Override
    public List<BannerDetailBean> processParse(Document doc) {
        List<BannerDetailBean> result = new ArrayList<>();
        final Element divContent = doc.select("div.main > div.neirong").first();
        Elements children = divContent.children();
        for (Element child : children) {
            if (TextUtils.isEmpty(child.text()) && child.children().size() == 0) {
                continue;
            }
            BannerDetailBean bannerDetailBean = new BannerDetailBean();
            if (TextUtils.equals(child.attr("style"), IMG_STYLE_VALUE)) {
                //image data
                Elements imgElements = child.children();
                for (Element img : imgElements) {
                    bannerDetailBean.setText(img.attr("abs:src"));
                    bannerDetailBean.setImage(true);
                    result.add(bannerDetailBean);
                }
            } else {
                //normal text data
                bannerDetailBean.setText(child.text());
                result.add(bannerDetailBean);
            }
        }
        return result;
    }
}
