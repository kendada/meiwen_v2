package cc.meiwen.util.weibo;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by abc on 2017/11/22.
 */

public class AccessTokenKeeper {

    private static final String PREFERENCES_NAME = "com_weibo_sdk_android";
    private static final String KEY_UID = "uid";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_EXPIRES_IN = "expires_in";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";

    public AccessTokenKeeper() {
    }

    public static void writeAccessToken(Context context, Oauth2AccessToken token) {
        if(null != context && null != token) {
            SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("uid", token.getUid());
            editor.putString("access_token", token.getToken());
            editor.putString("refresh_token", token.getRefreshToken());
            editor.putLong("expires_in", token.getExpiresTime());
            editor.commit();
        }
    }

    public static Oauth2AccessToken readAccessToken(Context context) {
        if(null == context) {
            return null;
        } else {
            Oauth2AccessToken token = new Oauth2AccessToken();
            SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
            token.setUid(pref.getString("uid", ""));
            token.setToken(pref.getString("access_token", ""));
            token.setRefreshToken(pref.getString("refresh_token", ""));
            token.setExpiresTime(pref.getLong("expires_in", 0L));
            return token;
        }
    }

    public static void clear(Context context) {
        if(null != context) {
            SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
        }
    }
}
