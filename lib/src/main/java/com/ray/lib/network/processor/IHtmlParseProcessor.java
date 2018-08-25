package com.ray.lib.network.processor;

import org.jsoup.nodes.Document;

/**
 * Author : hikobe8@github.com
 * Time : 2018/7/30 下午11:42
 * Description : Html parse process container
 */
public interface IHtmlParseProcessor<T> {

    T processParse(Document doc);

}
