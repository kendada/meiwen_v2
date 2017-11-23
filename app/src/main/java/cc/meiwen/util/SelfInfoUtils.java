package cc.meiwen.util;

import cc.meiwen.http.OkBasicParamsInterceptor;
import cc.meiwen.util.weibo.AccessTokenKeeper;
import cc.meiwen.util.weibo.Oauth2AccessToken;

/**
 * Created by abc on 2017/11/22.
 */

public class SelfInfoUtils {

    /**
     * 公用BasicParamsInterceptor.Builder
     * 获取含有uid和token的requesparams
     */
    public static OkBasicParamsInterceptor.Builder getDefaultParams(String url) {
        OkBasicParamsInterceptor.Builder mBuilder = new OkBasicParamsInterceptor.Builder();
        mBuilder.setUrl(url);
        Oauth2AccessToken oauth2AccessToken = AccessTokenKeeper.readAccessToken(AppUtils.getCtx());
        if(oauth2AccessToken != null){
            mBuilder.addQueryParam("access_token", oauth2AccessToken.getToken());
            mBuilder.addQueryParam("uid", oauth2AccessToken.getUid());
        }
        return mBuilder;
    }

}
