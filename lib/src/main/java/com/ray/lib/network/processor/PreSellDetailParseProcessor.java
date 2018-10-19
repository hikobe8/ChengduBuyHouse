package com.ray.lib.network.processor;

import android.text.TextUtils;

import com.ray.lib.bean.PreSellHouseDetailBean;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : hikobe8@github.com
 * Time : 2018/7/31 上午12:40
 * Description :
 */
public class PreSellDetailParseProcessor implements IHtmlParseProcessor<List<PreSellHouseDetailBean>>{
    @Override
    public List<PreSellHouseDetailBean> processParse(Document doc) {
        Elements select = doc.select("p.MsoNormal");
        List<Element> elements = select.subList(2, select.size());
        List<PreSellHouseDetailBean> result = new ArrayList<>();
        for (Element pElement:elements) {
            Element spanElement = pElement.select("span").first();
            PreSellHouseDetailBean detailBean = new PreSellHouseDetailBean();
            final Element parentSpan = spanElement.selectFirst("span");
            if (parentSpan != null) {
                Element a = parentSpan.selectFirst("a");
                if (a != null) {
                    String href;
                    if (a.hasAttr("href") && !TextUtils.isEmpty((href = a.attr("href")))) {
                        detailBean.setDownloadUrl(href);
                    }
                }
            }
            String string = spanElement.text();
            detailBean.setName(string);
            result.add(detailBean);
        }
        return result;
    }
}
