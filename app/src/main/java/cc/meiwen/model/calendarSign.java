package cc.meiwen.model;

import android.graphics.Color;
import android.text.TextUtils;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by abc on 2017/11/16.
 *
 * 首页弹框-历史签名
 */

public class calendarSign extends BmobObject{

    private String title;
    private String message;
    private String content;
    private boolean isShow;
    private boolean isShowWX;

    private BmobFile contentImage;

    private String textColor; // 字体颜色

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public BmobFile getContentImage() {
        return contentImage;
    }

    public void setContentImage(BmobFile contentImage) {
        this.contentImage = contentImage;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public boolean isShowWX() {
        return isShowWX;
    }

    public void setShowWX(boolean showWX) {
        isShowWX = showWX;
    }

    /**
     * 字体颜色，默认白色
     * */
    public int getTextColor(){
        if(TextUtils.isEmpty(textColor)){
            textColor = "#FFFFFF";
        }
        return Color.parseColor(textColor);
    }

}
