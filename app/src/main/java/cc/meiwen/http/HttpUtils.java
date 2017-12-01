package cc.meiwen.http;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cc.meiwen.BuildConfig;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by abc on 2017/11/22.
 */

public class HttpUtils {

    private final static String TAG = HttpUtils.class.getSimpleName();

    public static <T> Observable<T> createObservable(final OkBasicParamsInterceptor.Builder paramsBuilder, final Class<T> clazz) {
        return Observable.create(new Observable.OnSubscribe<T>(){
            @Override
            public void call(Subscriber<? super T> subscriber) {
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
                    T t = JSONObject.parseObject(result, clazz);
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

    protected static <T> void onRequestError(Subscriber<? super T> subscriber, Throwable ex) {
        try {
            subscriber.onError(ex);
            subscriber.onCompleted();
        } catch (Throwable ignored) {
            Log.e(TAG, "e = " + ignored);
        }
    }

    protected static OkHttpClient createOkHttpClient(OkBasicParamsInterceptor.Builder paramsBuilder){

        OkBasicParamsInterceptor paramsInterceptor = paramsBuilder.build();

        OkHttpClient.Builder builder = null;
        try {
            builder = getOkHttpClientBuilder();
            setOkHttpClientBuilder(builder, paramsInterceptor);
        } catch (Exception e) {
            builder = new OkHttpClient.Builder();
            setOkHttpClientBuilder(builder, paramsInterceptor);
        }

        // 判断是否为debug
        if (BuildConfig.DEBUG) {
            // 如果为 debug 模式，则添加日志拦截器
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        return builder.build();
    }

    public static void setOkHttpClientBuilder(OkHttpClient.Builder builder, Interceptor interceptor){
        if(builder != null && interceptor != null){
            long connectTimeout = 15;
            long readTimeout = 15;
            if(interceptor instanceof OkBasicParamsInterceptor){
                OkBasicParamsInterceptor paramsInterceptor = (OkBasicParamsInterceptor) interceptor;
                connectTimeout = paramsInterceptor.getConnectTimeout();
                readTimeout = paramsInterceptor.getReadTimeout();
            }

            builder.addInterceptor(interceptor)
                    .hostnameVerifier(DEFAULT_HOSTNAME_VERIFIER)
                    .connectTimeout(connectTimeout, TimeUnit.SECONDS) // 设置连接超时，单位s
                    .readTimeout(readTimeout, TimeUnit.SECONDS); // 设置读取超时，单位s
        }
    }

    private final static HostnameVerifier DEFAULT_HOSTNAME_VERIFIER = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * 设置https 访问的时候对所有证书都进行信任
     *
     * @throws Exception
     */
    public static OkHttpClient.Builder getOkHttpClientBuilder() throws Exception {
        final X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        return new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustManager)
                .hostnameVerifier(DEFAULT_HOSTNAME_VERIFIER);

    }
}
