package cc.meiwen.util.weibo;

import android.os.Bundle;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by abc on 2017/11/22.
 */

public class Oauth2AccessToken {

    public static final String KEY_UID = "uid";
    public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String KEY_EXPIRES_IN = "expires_in";
    public static final String KEY_REFRESH_TOKEN = "refresh_token";
    public static final String KEY_PHONE_NUM = "phone_num";
    private String mUid = "";
    private String mAccessToken = "";
    private String mRefreshToken = "";
    private long mExpiresTime = 0L;
    private String mPhoneNum = "";
    Bundle bundle;

    public Oauth2AccessToken() {
    }

    /** @deprecated */
    @Deprecated
    public Oauth2AccessToken(String responseText) {
        if(responseText != null && responseText.indexOf("{") >= 0) {
            try {
                JSONObject json = new JSONObject(responseText);
                this.setUid(json.optString("uid"));
                this.setToken(json.optString("access_token"));
                this.setExpiresIn(json.optString("expires_in"));
                this.setRefreshToken(json.optString("refresh_token"));
                this.setPhoneNum(json.optString("phone_num"));
            } catch (JSONException var3) {
                var3.printStackTrace();
            }
        }

    }

    public Oauth2AccessToken(String accessToken, String expiresIn) {
        this.mAccessToken = accessToken;
        this.mExpiresTime = System.currentTimeMillis();
        if(expiresIn != null) {
            this.mExpiresTime += Long.parseLong(expiresIn) * 1000L;
        }

    }

    public static Oauth2AccessToken parseAccessToken(String responseJsonText) {
        if(!TextUtils.isEmpty(responseJsonText) && responseJsonText.indexOf("{") >= 0) {
            try {
                JSONObject json = new JSONObject(responseJsonText);
                Oauth2AccessToken token = new Oauth2AccessToken();
                token.setUid(json.optString("uid"));
                token.setToken(json.optString("access_token"));
                token.setExpiresIn(json.optString("expires_in"));
                token.setRefreshToken(json.optString("refresh_token"));
                token.setPhoneNum(json.optString("phone_num"));
                return token;
            } catch (JSONException var3) {
                var3.printStackTrace();
            }
        }

        return null;
    }

    public static Oauth2AccessToken parseAccessToken(Bundle bundle) {
        if(null != bundle) {
            Oauth2AccessToken accessToken = new Oauth2AccessToken();
            accessToken.setUid(getString(bundle, "uid", ""));
            accessToken.setToken(getString(bundle, "access_token", ""));
            accessToken.setExpiresIn(getString(bundle, "expires_in", ""));
            accessToken.setRefreshToken(getString(bundle, "refresh_token", ""));
            accessToken.setPhoneNum(getString(bundle, "phone_num", ""));
            accessToken.setBundle(bundle);
            return accessToken;
        } else {
            return null;
        }
    }

    public boolean isSessionValid() {
        return !TextUtils.isEmpty(this.mAccessToken);
    }

    public String toString() {
        return "uid: " + this.mUid + ", " + "access_token" + ": " + this.mAccessToken + ", " + "refresh_token" + ": " + this.mRefreshToken + ", " + "phone_num" + ": " + this.mPhoneNum + ", " + "expires_in" + ": " + Long.toString(this.mExpiresTime);
    }

    public String getUid() {
        return this.mUid;
    }

    public void setUid(String uid) {
        this.mUid = uid;
    }

    public String getToken() {
        return this.mAccessToken;
    }

    public void setToken(String mToken) {
        this.mAccessToken = mToken;
    }

    public String getRefreshToken() {
        return this.mRefreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.mRefreshToken = refreshToken;
    }

    public long getExpiresTime() {
        return this.mExpiresTime;
    }

    public void setExpiresTime(long mExpiresTime) {
        this.mExpiresTime = mExpiresTime;
    }

    public void setExpiresIn(String expiresIn) {
        if(!TextUtils.isEmpty(expiresIn) && !expiresIn.equals("0")) {
            this.setExpiresTime(System.currentTimeMillis() + Long.parseLong(expiresIn) * 1000L);
        }

    }

    private static String getString(Bundle bundle, String key, String defaultValue) {
        if(bundle != null) {
            String value = bundle.getString(key);
            return value != null?value:defaultValue;
        } else {
            return defaultValue;
        }
    }

    public String getPhoneNum() {
        return this.mPhoneNum;
    }

    private void setPhoneNum(String mPhoneNum) {
        this.mPhoneNum = mPhoneNum;
    }

    public Bundle getBundle() {
        return this.bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
