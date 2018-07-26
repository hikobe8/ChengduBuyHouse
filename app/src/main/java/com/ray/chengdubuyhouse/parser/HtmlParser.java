package com.ray.chengdubuyhouse.parser;

import com.ray.chengdubuyhouse.bean.PreSellHouseBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Author : hikobe8@github.com
 * Time : 2018/7/26 下午11:05
 * Description : 解析html
 */
public class HtmlParser {

    public static final String TAG = "HtmlParser";

    public static HtmlParser getInstance() {
        return SingletonHolder.sInstance;
    }

    public static class SingletonHolder {
        static HtmlParser sInstance = new HtmlParser();
    }

    public interface DataCallback{
        void onSubscribe(Disposable d);
        void onNext(List<PreSellHouseBean> datas);
        void onError(Throwable e);
    }

    public void parseHtml(String url, final DataCallback callback) {
        Observable
                .create(new ObservableOnSubscribe<List<PreSellHouseBean>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<PreSellHouseBean>> emitter) throws Exception {
                        Document doc = Jsoup.connect("http://www.cdfangxie.com/Infor/type/typeid/36.html").get();
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
                                preSellHouseBean.setLink(link);
                                Element span = next.select("span").last();
                                if (span.hasText()) {
                                    preSellHouseBean.setDate(span.text());
                                }
                                datas.add(preSellHouseBean);
                            }
                        }
                        emitter.onNext(datas);
                        emitter.onComplete();
                    }
                }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<PreSellHouseBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (callback != null)
                            callback.onSubscribe(d);
                    }

                    @Override
                    public void onNext(List<PreSellHouseBean> list) {
                        if (callback != null)
                            callback.onNext(list);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (callback != null)
                            callback.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
