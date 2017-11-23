package cc.meiwen.util;

import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

import cc.meiwen.R;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import cn.sharesdk.wechat.utils.WechatClientNotExistException;

/**
 * Created by abc on 2017/11/20.
 */

public class MWShare implements PlatformActionListener{

    private static final String TAG = MWShare.class.getSimpleName();

    public final static int SHARE_TYPE_0 = 0; // 微博分享
    public final static int SHARE_TYPE_1 = 1; // 微信分享
    public final static int SHARE_TYPE_2 = 2; // 微信朋友圈分享
    public final static int SHARE_TYPE_3 = 3; // QQ分享
    public final static int SHARE_TYPE_4 = 4; // QQ空间分享
    public final static int SHARE_TYPE_5 = 5; // 短信分享


    private Platform mPlatform;
    private Platform.ShareParams pParams;

    private String mUrl;
    private String mTitle; // 标题
    private String mTitleUrl; // 标题超链接
    private String mText; // 分享文本
    private String mImageUrl; // 图片网络地址
    private String mImagePath; // 图片本地路径

    private OnShareResultListener mOnShareResultListener;

    private int shareType;
    private CopyUtil mCopyUtil;

    public MWShare(){
        pParams = new Platform.ShareParams();
        mCopyUtil = new CopyUtil(AppUtils.getCtx());
    }

    /**
     * @param shareType 分享平台
     * @param title 标题
     * @param titleUrl 标题超链接
     * @param text 分享文本
     * @param imageUrl 图片网络地址
     * @param imagePath 图片本地路径
     * */
    public void onShare(int shareType, String title, String titleUrl, String text, String imageUrl, String imagePath){
        setTitle(title);
        setUrl(titleUrl);
        setText(text);
        setImageUrl(imageUrl);
        setImagePath(imagePath);
        onShare(shareType);
    }

    /**
     * @param shareType 分享平台
     * */
    public void onShare(int shareType){
        createPlatform(shareType);
        if(mPlatform != null && pParams != null){
            mPlatform.share(pParams);
        }
    }

    private void createPlatform(int shareType){
        this.shareType = shareType;
        switch (shareType){
            case SHARE_TYPE_0: // 微博
                mPlatform = ShareSDK.getPlatform(SinaWeibo.NAME);
                break;
            case SHARE_TYPE_1: // 微信
                mPlatform = ShareSDK.getPlatform(Wechat.NAME);
                pParams.setShareType(Platform.SHARE_IMAGE);
                break;
            case SHARE_TYPE_2: // 微信朋友圈
                mPlatform = ShareSDK.getPlatform(WechatMoments.NAME);
                pParams.setShareType(Platform.SHARE_IMAGE);
                break;
            case SHARE_TYPE_3: // QQ分享
                break;
            case SHARE_TYPE_4: // QQ空间分享
                break;
            case SHARE_TYPE_5: // 短信
                mPlatform = ShareSDK.getPlatform(ShortMessage.NAME);
                break;
        }
//        setImageData();
        if(mPlatform != null){
            mPlatform.setPlatformActionListener(this);
        }
    }


    /**
     * 分享标题
     * */
    public void setTitle(String title) {
        this.mTitle = title;
        if(pParams != null && !TextUtils.isEmpty(mTitle)){
            pParams.setTitle(mTitle);
        }
    }

    public void setUrl(String url) {
        this.mUrl = url;
        if(pParams != null && !TextUtils.isEmpty(mUrl)){
            pParams.setUrl(mUrl);
        }
    }

    /**
     * 分享标题地址
     * */
    public void setTitleUrl(String titleUrl) {
        this.mTitleUrl = titleUrl;
        if(pParams != null && !TextUtils.isEmpty(mTitleUrl)){
            pParams.setTitleUrl(mTitleUrl);
        }
    }

    /**
     * 分享文本
     * */
    public void setText(String text) {
        this.mText = text;
        if(pParams != null && !TextUtils.isEmpty(mText)){
            pParams.setText(mText);
        }
        if(!TextUtils.isEmpty(text) && mCopyUtil != null){ // 其他不复制，
            if(shareType == SHARE_TYPE_1 || shareType == SHARE_TYPE_2){
                mCopyUtil.copy(text, "美文已复制到粘贴板，请复制到微信");
            }
        }
    }

    /**
     * 分享图片网络地址
     * */
    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
        if(pParams != null && !TextUtils.isEmpty(mImageUrl)){
            pParams.setImageUrl(mImageUrl);
        }
    }

    /**
     * 分享图片本地路径
     * */
    public void setImagePath(String imagePath) {
        this.mImagePath = imagePath;
        if(pParams != null && !TextUtils.isEmpty(mImagePath)){
            pParams.setImagePath(mImagePath);
        }
    }

    public void setImageData(){
        if(pParams != null){
            pParams.setImageData(BitmapFactory.decodeResource(AppUtils.getResources(), R.mipmap.ic_launcher));
        }
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Log.e(TAG, " 分享成功：" + platform.getName());
        if(mOnShareResultListener != null){
            mOnShareResultListener.onComplete();
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Log.e(TAG, throwable + "分享出错：" + platform.getName());
        if(throwable instanceof WechatClientNotExistException){
        } else {
        }
        if(mOnShareResultListener != null){
            mOnShareResultListener.onComplete();
        }
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Log.d(TAG, " 分享取消: " + platform.getName());
        if(mOnShareResultListener != null){
            mOnShareResultListener.onComplete();
        }
    }

    public void setOnShareResultListener(OnShareResultListener onShareResultListener) {
        this.mOnShareResultListener = onShareResultListener;
    }

    public interface OnShareResultListener{
        void onComplete();
    }

    public void detach(){
        mOnShareResultListener = null;
        if(mPlatform != null){
            mPlatform = null;
        }
        if(pParams != null){
            pParams = null;
        }
    }
}
