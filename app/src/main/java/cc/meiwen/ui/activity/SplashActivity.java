package cc.meiwen.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import cc.emm.AppConnect;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-30
 * Time: 15:01
 * Version 1.0
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //万普广告初始化
        AppConnect.getInstance("14aa41cabb9c95d6cad976dbc3b4de43", "xiaomi", this);

        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation(this).save();
        // 启动推送服务
        BmobPush.startWork(this, "你的Application Id");

        //初始化Bmob
        Bmob.initialize(this, "aee6e001a4a2cdd86a45363b771755e8");
        //判断用户是否登录
        isLogined();
    }

    /**
     * 判断用户是否已登录
     * */
    private void isLogined(){
        BmobUser user = BmobUser.getCurrentUser(getContext());
        if(user != null){
            //已经登录
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            //未登录
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        SplashActivity.this.finish();
    }

}
