package com.ray.chengdubuyhouse.network;

import com.ray.chengdubuyhouse.network.processor.IHtmlParseProcessor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

    /**
     * post 请求
     * @param url 请求地址
     * @return 返回数据
     * @throws IOException
     */
    private String post(String url, Map<String, String> paramsMap) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramsMap.keySet()) {
            builder.add(key, paramsMap.get(key));
        }
        RequestBody formBody=builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public <T> void parseHtmlByOkHttp(final String url, final IHtmlParseProcessor<T> htmlParseProcess, final HtmlDataCallback<T> callback) {
        Observable
                .create(new ObservableOnSubscribe<Document>() {
                    @Override
                    public void subscribe(ObservableEmitter<Document> emitter) throws Exception {
                        Map<String,String> paramsMap=new HashMap<>();
                        paramsMap.put("regioncode","00");
                        paramsMap.put("pageNo","1");
                        String responseData = post(url, paramsMap);
                        Document doc = Jsoup.parse(responseData);
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
