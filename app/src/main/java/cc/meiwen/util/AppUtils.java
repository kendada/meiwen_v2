package cc.meiwen.util;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * Created by abc on 2017/11/16.
 */

public class AppUtils {

    private AppUtils() {
    }

    public static Application application = null;

    /**
     * 获取应用程序的上下文
     *
     * @return 应用程序的上下文
     */
    public static Context getCtx() {
        return application;
    }

    public static Resources getResources(){
        if(application != null){
            return application.getResources();
        }
        return null;
    }

    public static boolean isZh(){
        return true;
    }
}
