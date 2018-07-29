package com.ray.chengdubuyhouse.parser;

import android.util.Log;

import com.ray.chengdubuyhouse.bean.BannerBean;
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

    public static final String HOST="https://www.cdfangxie.com";

    private static final String HOME_BANNER="https://www.cdfangxie.com";

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

    public interface DetailCallback{
        void onSubscribe(Disposable d);
        void onNext(List<String> datas);
        void onError(Throwable e);
    }

    public void parsePreSellDetailHtml(final String url, final DetailCallback callback) {
        Observable
                .create(new ObservableOnSubscribe<List<String>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {
                        Document doc = Jsoup.connect(HOST + url).get();
                        Elements select = doc.select("p.MsoNormal");
                        List<Element> elements = select.subList(2, select.size());
                        List<String> result = new ArrayList<>();
                        for (Element pElement:elements) {
                            Element spanElement = pElement.select("span").first();
                            String string = spanElement.text();
                            result.add(string);
                        }
                        emitter.onNext(result);
                        emitter.onComplete();
                    }
                }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (callback != null)
                            callback.onSubscribe(d);
                    }

                    @Override
                    public void onNext(List<String> list) {
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

    public interface BannerCallback {

        void onSubscribe(Disposable d);
        void onNext(List<BannerBean> datas);
        void onError(Throwable e);

    }

    public void getBannerList(final BannerCallback callback){
        Observable
                .create(new ObservableOnSubscribe<List<BannerBean>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<BannerBean>> emitter) throws Exception {
                        Document doc = Jsoup.connect(HOME_BANNER).get();
                        Elements bannerWrapperList = doc.select("div.scrcont > div > a");
                        List<BannerBean> result = new ArrayList<>();
                        for (Element bannerWrapper : bannerWrapperList) {
                            BannerBean bannerBean = new BannerBean();
                            bannerBean.setName(bannerWrapper.selectFirst("span").text());
                            bannerBean.setLink(HOST+bannerWrapper.attr("href"));
                            bannerBean.setImageUrl(HOST+bannerWrapper.selectFirst("img").attr("src"));
                            result.add(bannerBean);
                        }
                        emitter.onNext(result);
                        emitter.onComplete();
                    }
                }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BannerBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (callback != null)
                            callback.onSubscribe(d);
                    }

                    @Override
                    public void onNext(List<BannerBean> list) {
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
