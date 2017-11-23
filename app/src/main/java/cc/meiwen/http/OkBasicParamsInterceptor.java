package cc.meiwen.http;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by abc on 2017/7/20.
 * Mail to jinshikun@gmail.com
 * OkHttp 拦截器，主要功能：
 * 1. 请求添加公共参数
 * 2. 拦截HTTP请求异常，拦截请求异常代码如下：
 * Response response = chain.proceed(request);
 * if(response != null){
 * if(!response.isSuccessful()){ // 请求失败时，进行拦截访问的URL
 * Request errorRequest = response.request();
 * throw new OkHttpException(response.message(), response.code(), errorRequest!=null? errorRequest.url().toString() : null);
 * }
 * }
 * 3. 设置重试机制，例【只是实例代码，需要根据异常类型进行判断是否需要重试】：
 * public int maxRetry;//最大重试次数
 * private int retryNum = 0;
 * Response response = chain.proceed(request);
 * while (!response.isSuccessful() && retryNum < maxRetry) {
 * retryNum++;
 * Log.d(OkBaseObservable.TAG, "retryNum=" + retryNum);
 * response = chain.proceed(request);
 * }
 */

public class OkBasicParamsInterceptor implements Interceptor{

    private int connectTimeout = 15; // 连接超时时间：单位s
    private int readTimeout = 15; // 读取超时时间：单位是s

    private String url;

    List<OkKeyValue> queryParams = new ArrayList<>();
    List<OkKeyValue> params = new ArrayList<>();
    List<OkKeyValue> headerParams = new ArrayList<>();
    List<String> headerLinesList = new ArrayList<>();

    private OkBasicParamsInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();

        // 添加头部参数，方式【1】
        Headers.Builder headersBuilder = request.headers().newBuilder();
        if(headerParams.size() > 0){
            for (OkKeyValue okKeyValue : headerParams){
                headersBuilder.add(okKeyValue.key, okKeyValue.value);
            }
        }
        // 添加头部参数，方式【2】
        if(headerLinesList.size() > 0){
            for (String line : headerLinesList){
                headersBuilder.add(line);
            }
        }
        // 将Header添加到请求中
        requestBuilder.headers(headersBuilder.build());

        // GET 请求添加参数，直接将参数放在URL中
        if(queryParams.size() > 0){
            request = injectParamsIntoUrl(request.url().newBuilder(), requestBuilder, queryParams);
        }

        // POST 请求添加参数，from表单方式
        if(params != null && params.size() > 0 && request.method().equals("POST")){
            if(request.body() instanceof FormBody) {
                FormBody.Builder newFormBodyBuilder = new FormBody.Builder();
                if(params.size() > 0){
                    for (OkKeyValue okKeyValue : params){
                        newFormBodyBuilder.add(okKeyValue.key, okKeyValue.value);
                    }
                }

                FormBody oldFormBody = (FormBody) request.body();
                int paramSize = oldFormBody.size();
                if(paramSize > 0){
                    for (int i=0; i<paramSize; i++){
                        newFormBodyBuilder.add(oldFormBody.name(i), oldFormBody.value(i));
                    }
                }

                requestBuilder.post(newFormBodyBuilder.build());
                request = requestBuilder.build();
            }
            else if(request.body() instanceof MultipartBody){
                MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

                for (OkKeyValue okKeyValue : params){
                    multipartBuilder.addFormDataPart(okKeyValue.key, okKeyValue.value);
                }

                List<MultipartBody.Part> oldParts = ((MultipartBody)request.body()).parts();
                if(oldParts != null && oldParts.size() > 0){
                    for (MultipartBody.Part part : oldParts){
                        multipartBuilder.addPart(part);
                    }
                }

                requestBuilder.post(multipartBuilder.build());
                request = requestBuilder.build();
            } else {
                MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

                for (OkKeyValue okKeyValue : params){
                    multipartBuilder.addFormDataPart(okKeyValue.key, okKeyValue.value);
                }

                requestBuilder.post(multipartBuilder.build());
                request = requestBuilder.build();
            }
        }
        return chain.proceed(request);
    }

    /**
     * 格式化GET请求参数
     * */
    private Request injectParamsIntoUrl(HttpUrl.Builder httpUrlBuilder, Request.Builder requestBuilder, List<OkKeyValue> params){
        if(params.size() > 0){
            for (OkKeyValue okKeyValue : params){
                httpUrlBuilder.addQueryParameter(okKeyValue.key, okKeyValue.value);
            }

            requestBuilder.url(httpUrlBuilder.build());
            return requestBuilder.build();
        }
        return null;
    }

    public int getReadTimeout(){
        return readTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public List<OkKeyValue> getParams(){
        return params;
    }

    public List<OkKeyValue> getQueryParams(){
        return queryParams;
    }

    public List<OkKeyValue> getAllParams(){
        List<OkKeyValue> allParamsMap = new ArrayList<>();
        allParamsMap.addAll(params);
        allParamsMap.addAll(queryParams);
        return allParamsMap;
    }

    public static class Builder {

        OkBasicParamsInterceptor interceptor;

        public Builder () {
            interceptor = new OkBasicParamsInterceptor();
        }

        public Builder addParams(String key, String value){
            interceptor.params.add(new OkKeyValue(key, value));
            return this;
        }
        // 用键值对 Map 作为参数批量插
        public Builder addParamsMap(List<OkKeyValue> params){
            interceptor.params.addAll(params);
            return this;
        }

        public Builder addHeaderParams(String key, String value){
            interceptor.headerParams.add(new OkKeyValue(key, value));
            return this;
        }

        public Builder addHeaderParamsMap(List<OkKeyValue> headerParams){
            interceptor.headerParams.addAll(headerParams);
            return this;
        }

        public Builder addHeaderLine(String headerLine){
            int index = headerLine.indexOf(":");
            if(index == -1){
                throw new IllegalArgumentException("Unexpected header: " + headerLine);
            }
            interceptor.headerLinesList.add(headerLine);
            return this;
        }

        public Builder addHeaderLineList(List<String> headerLineList){
            for(String headerLine : headerLineList){
                int index = headerLine.indexOf(":");
                if(index == -1){
                    throw new IllegalArgumentException("Unexpected header: " + headerLine);
                }
                interceptor.headerLinesList.add(headerLine);
            }
            return this;
        }

        public Builder addQueryParam(String key, String value){
            interceptor.queryParams.add(new OkKeyValue(key, value));
            return this;
        }

        public Builder addQueryParamsMap(List<OkKeyValue> queryParams){
            interceptor.queryParams.addAll(queryParams);
            return this;
        }

        public Builder setUrl(String url){
            interceptor.url = url;
            return this;
        }

        public String getUrl(){
            return interceptor.url;
        }

        public void setConnectTimeout(int connectTimeout) {
            if (connectTimeout > 0) {
                interceptor.connectTimeout = connectTimeout;
            }
        }

        public void setReadTimeout(int readTimeout){
            interceptor.readTimeout = readTimeout;
        }

        public OkBasicParamsInterceptor build() {
            return interceptor;
        }

    }

}
