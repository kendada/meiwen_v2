package cc.meiwen.ui.activity;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * User: 靳世坤(1203596603@qq.com)
 * Date: 2015-08-17
 * Time: 20:29
 * Version 1.0
 */

public interface IBaseActivity {
    /**
     * 获取Application对象
     * */
    public abstract Application getApplication();

    /**
     * 开启服务
     * */
    public abstract void startService();

    /**
     * 停止服务
     * */
    public abstract void stopService();

    /**
     * 判断是否有网络连接，若没有，则弹出设置对话框，返回false
     * */
    public abstract boolean validateInternet();

    /**
     * 判断是否有网络连接
     * */
    public abstract boolean hasInternetConnect();

    /**
     * 退出应用
     * */
    public abstract void isExit();

    /**
     * 检查内存卡
     * */
    public abstract void checkMemoryCard();

    /**
     * 获取当前Activity的上下文
     * */
    public abstract Context getContext();

    /**
     * 获取app的设置
     * */
    public abstract SharedPreferences getAppSettingSharedPre();

    /**
     * 获取app主题配置
     * */
    public abstract int getThemeSetting();

}
