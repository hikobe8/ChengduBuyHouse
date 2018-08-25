package com.ray.lib.network.processor;

import com.ray.lib.bean.PageableResponseBean;
import com.ray.lib.bean.PreSellHouseBean;
import com.ray.lib.network.NetworkConstant;
import com.ray.lib.util.PageDataUtil;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author : hikobe8@github.com
 * Time : 2018/7/31 上午12:31
 * Description :
 */
public class PreSellParseProcessor implements IHtmlParseProcessor<PageableResponseBean<List<PreSellHouseBean>>> {

    @Override
    public PageableResponseBean<List<PreSellHouseBean>> processParse(Document doc) {
        PageableResponseBean<List<PreSellHouseBean>> pageableResponseBean = new PageableResponseBean<>();
        Element pageElement = doc.select("div.pages2 > b").first();
        String pageText = pageElement.text();
        PageDataUtil.fillPageableData(pageText, pageableResponseBean);
        Elements select = doc.select("div.main > div.right_cont > ul.ul_list > li");
        List<PreSellHouseBean> datas = new ArrayList<>();
        for (Element next : select) {
            if (!next.hasClass("line")) {
                Element first = next.select("span.sp_name > a").first();
                String title = first.attr("title");
                String link = first.attr("href");
                PreSellHouseBean preSellHouseBean = new PreSellHouseBean();
                String[] split = title.split("\\|");
                preSellHouseBean.setAddress(split[0]);
                preSellHouseBean.setName(split[1]);
                preSellHouseBean.setLink(NetworkConstant.HOST + link);
                Element span = next.select("span").last();
                if (span.hasText()) {
                    preSellHouseBean.setDate(span.text());
                }
                datas.add(preSellHouseBean);
            }
        }
        pageableResponseBean.data = datas;
        return pageableResponseBean;
    }

}
