package com.ray.chengdubuyhouse.network;

import com.ray.chengdubuyhouse.network.processor.IHtmlParseProcessor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

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

    /**
     * post 请求
     *
     * @param url 请求地址
     * @return 返回数据
     * @throws IOException
     */
    private Call post(String url, Map<String, String> paramsMap) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramsMap.keySet()) {
            builder.add(key, paramsMap.get(key));
        }
        RequestBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        return client.newCall(request);
    }

    public <T> Observable<T> parseHtmlByOkHttp(final String url, final Map<String, String> paramsMap, final IHtmlParseProcessor<T> htmlParseProcess) {
        return Observable
                .create(new ObservableOnSubscribe<Document>() {
                    @Override
                    public void subscribe(ObservableEmitter<Document> emitter) throws Exception {
                        Call call = null;
                        while (!emitter.isDisposed()) {
                            if (call == null) {
                                call = post(url, paramsMap);
                                try {
                                    Document doc = Jsoup.parse(call.execute().body().string());
                                    emitter.onNext(doc);
                                    emitter.onComplete();
                                } catch (InterruptedIOException e) {
                                    return;
                                }
                            }
                        }
                        if (call != null && !call.isCanceled()) {
                            call.cancel();
                        }
                    }
                })
                .onTerminateDetach()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Document, T>() {
                    @Override
                    public T apply(Document doc) {
                        return htmlParseProcess.processParse(doc);
                    }
                });
    }

    public <T> Observable<T> parseHtml(final String url, final IHtmlParseProcessor<T> htmlParseProcess) {
        return Observable
                .create(new ObservableOnSubscribe<Document>() {
                    @Override
                    public void subscribe(ObservableEmitter<Document> emitter) throws Exception {
                        Document doc = null;
                        while (!emitter.isDisposed()) {
                            if (doc == null) {
                                doc = Jsoup.connect(url).get();
                                emitter.onNext(doc);
                                emitter.onComplete();
                            }
                        }
                    }
                })
                .onTerminateDetach()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Document, T>() {
                    @Override
                    public T apply(Document doc) {
                        return htmlParseProcess.processParse(doc);
                    }
                });
    }

}
