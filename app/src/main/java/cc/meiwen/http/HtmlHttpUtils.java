package cc.meiwen.http;

import android.text.TextUtils;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import cc.meiwen.model.HtmlMwBO;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by abc on 2017/11/24.
 * 解析HTML
 */

public class HtmlHttpUtils extends HttpUtils{

    private final static String TAG = HtmlHttpUtils.class.getSimpleName();

    public static Observable<List<HtmlMwBO>> createObservableHtml(final OkBasicParamsInterceptor.Builder paramsBuilder) {
        return Observable.create(new Observable.OnSubscribe<List<HtmlMwBO>>(){
            @Override
            public void call(Subscriber<? super List<HtmlMwBO>> subscriber) {
                subscriber.onStart();
                if (paramsBuilder == null) {
                    subscriber.onError(new OkHttpException("params can't be null",-1,null));
                    return;
                }
                try {
                    OkHttpClient okHttpClient = createOkHttpClient(paramsBuilder);
                    Request request = new Request.Builder()
                            .url(paramsBuilder.getUrl())
                            .build();
                    Response response = okHttpClient.newCall(request).execute();
                    String result = response.body().string();
                    List<HtmlMwBO> t = createHtmlMw(result);
                    if(t != null){
                        subscriber.onNext(t);
                        subscriber.onCompleted();
                    } else {
                        onRequestError(subscriber, new RuntimeException("获取数据失败！"));
                    }
                } catch (Throwable e){
                    onRequestError(subscriber, e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static List<HtmlMwBO> createHtmlMw(String text){
        List<HtmlMwBO> list = new ArrayList<>();
        Document doc = Jsoup.parse(text);
        Elements elements = doc.getElementsByClass("pic_text1");
        for (Element element : elements){
            String t = element.text();
            Log.d(TAG, "element = " + t);
            HtmlMwBO htmlMwBO = new HtmlMwBO();
            htmlMwBO.text = t;

            Elements imgElements = element.getElementsByTag("img");
            for (Element imgElement : imgElements){
                String imgSrc = imgElement.attr("src"); //获取src属性的值
                imgSrc = formatImgUrl(imgSrc);
                Log.d(TAG, "imgSrc = " + imgSrc);
                htmlMwBO.original_pic = imgSrc;
            }

            list.add(htmlMwBO);
        }
        return list;
    }

    private static String formatImgUrl(String imgUrl){
        if(TextUtils.isEmpty(imgUrl)) return null;
        imgUrl = "http://www.59xihuan.cn" + imgUrl;
        if(imgUrl.contains("-lp")){
            imgUrl = imgUrl.replaceAll("-lp", "");
        }
        return imgUrl;
    }

}
