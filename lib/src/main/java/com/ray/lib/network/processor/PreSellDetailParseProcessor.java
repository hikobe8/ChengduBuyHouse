package com.ray.lib.network.processor;

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
public class PreSellDetailParseProcessor implements IHtmlParseProcessor<List<String>>{
    @Override
    public List<String> processParse(Document doc) {
        Elements select = doc.select("p.MsoNormal");
        List<Element> elements = select.subList(2, select.size());
        List<String> result = new ArrayList<>();
        for (Element pElement:elements) {
            Element spanElement = pElement.select("span").first();
            String string = spanElement.text();
            result.add(string);
        }
        return result;
    }
}
