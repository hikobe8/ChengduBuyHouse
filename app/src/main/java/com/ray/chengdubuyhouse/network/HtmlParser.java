package com.ray.chengdubuyhouse.network;

import com.ray.chengdubuyhouse.network.processor.IHtmlParseProcessor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Author : hikobe8@github.com
 * Time : 2018/7/26 下午11:05
 * Description : 解析html
 */
public class HtmlParser {

    private static final String TAG = "HtmlParser";

    public static HtmlParser getInstance() {
        return SingletonHolder.sInstance;
    }

    public static class SingletonHolder {
        static HtmlParser sInstance = new HtmlParser();
    }

    public interface HtmlDataCallback<T>{
        void onSubscribe(Disposable d);
        void onNext(T data);
        void onError(Throwable e);
    }

    public <T> void parseHtml(final String url, final IHtmlParseProcessor<T> htmlParseProcess, final HtmlDataCallback<T> callback) {
        Observable
                .create(new ObservableOnSubscribe<Document>() {
                    @Override
                    public void subscribe(ObservableEmitter<Document> emitter) throws Exception {
                        Document doc = Jsoup.connect(url).get();
                        emitter.onNext(doc);
                        emitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Document, T>() {
                    @Override
                    public T apply(Document doc) {
                        return htmlParseProcess.processParse(doc);
                    }
                })
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (callback != null)
                            callback.onSubscribe(d);
                    }

                    @Override
                    public void onNext(T list) {
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
