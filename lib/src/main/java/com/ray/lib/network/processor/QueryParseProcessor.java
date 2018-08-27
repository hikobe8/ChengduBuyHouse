package com.ray.lib.network.processor;


import com.ray.lib.bean.PageableResponseBean;
import com.ray.lib.bean.QueryResultBean;
import com.ray.lib.db.entity.DistrictEntity;
import com.ray.lib.util.PageDataUtil;
import com.ray.lib.viewmodel.DistrictViewModel;

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
public class QueryParseProcessor implements IHtmlParseProcessor<PageableResponseBean<List<QueryResultBean>>> {

    private DistrictViewModel mDistrictViewModel;

    public QueryParseProcessor(DistrictViewModel districtViewModel) {
        mDistrictViewModel = districtViewModel;
    }

    @Override
    public PageableResponseBean<List<QueryResultBean>> processParse(Document doc) {
        PageableResponseBean<List<QueryResultBean>> listPageableResponseBean = new PageableResponseBean<>();
        fillPageData(doc, listPageableResponseBean);
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
            queryResultBean.setStatus(trElement.child(11).text());
            resultBeanList.add(queryResultBean);
        }
        if (!DistrictViewModel.sDataUpdated) {
            Elements options = doc.select("select.sml").first().children();
            List<DistrictEntity> districtEntityList = new ArrayList<>();
            for (Element option : options) {
                String code = option.attr("value");
                String name = option.text();
                DistrictEntity entity = new DistrictEntity(code, name);
                districtEntityList.add(entity);
            }
            mDistrictViewModel.insertDistricts(districtEntityList);
        }
        listPageableResponseBean.data = resultBeanList;
        return listPageableResponseBean;
    }

    private void fillPageData(Document doc, PageableResponseBean<List<QueryResultBean>> listPageableResponseBean) {
        Element pageBox = doc.select("div.pages-box").first();
        Element totalElement = doc.select("div.pages-box > a").last();
        try {
            listPageableResponseBean.pageableData.page = PageDataUtil.getSingleIngeterValue(pageBox.select("a.on").first().attr("onclick"));
            listPageableResponseBean.pageableData.total = PageDataUtil.getSingleIngeterValue(totalElement.attr("onclick"));
        } catch (Exception e) {
            listPageableResponseBean.pageableData.page = 0;
            listPageableResponseBean.pageableData.total = 0;
        }
    }

}
