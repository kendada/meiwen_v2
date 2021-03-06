package cc.meiwen.ui.activity;

import android.content.Intent;
import android.os.Bundle;

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

        //判断用户是否登录
        isLogined();
    }

    /**
     * 判断用户是否已登录
     * */
    private void isLogined(){
        BmobUser user = BmobUser.getCurrentUser();
        if(user != null){
            //已经登录
            Intent intent = new Intent(this, MainActivityV2.class);
            startActivity(intent);
        } else {
            //未登录
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        SplashActivity.this.finish();
    }

}
