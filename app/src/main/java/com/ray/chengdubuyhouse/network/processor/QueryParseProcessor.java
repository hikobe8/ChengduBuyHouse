package com.ray.chengdubuyhouse.network.processor;

import com.ray.chengdubuyhouse.bean.QueryResultBean;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : hikobe8@github.com
 * Time : 2018/7/31 下午10:26
 * Description :
 */
public class QueryParseProcessor implements IHtmlParseProcessor<List<QueryResultBean>> {

    @Override
    public List<QueryResultBean> processParse(Document doc) {
        Elements trElements = doc.select("tbody#_projectInfo > tr");
        List<QueryResultBean> resultBeanList = new ArrayList<>();
        for (int i = 0; i < trElements.size(); i ++) {
            Element trElement = trElements.get(i);
            QueryResultBean queryResultBean = new QueryResultBean();
            queryResultBean.setDistrict(trElement.child(2).text());
            queryResultBean.setProjectName(trElement.select("td.projectName").first().text());
            queryResultBean.setSellNo(trElement.child(4).text());
            queryResultBean.setRange(trElement.child(5).text());
            queryResultBean.setHouseCount(trElement.child(6).text());
            queryResultBean.setPhone(trElement.child(7).text());
            queryResultBean.setStartTime(trElement.child(8).text());
            queryResultBean.setEndTime(trElement.child(8).text());
            queryResultBean.setSelectEndTime(trElement.select("td.terminalTime").first().text());
            queryResultBean.setStatus(trElement.child(10).text());
            resultBeanList.add(queryResultBean);
        }
        return resultBeanList;
    }

}
