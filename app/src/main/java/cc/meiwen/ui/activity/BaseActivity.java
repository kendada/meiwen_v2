package cc.meiwen.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.umeng.analytics.MobclickAgent;

import cc.meiwen.R;
import cc.meiwen.util.SystemBarTintManager;
import cc.meiwen.view.LoadingDialog;

/**
 * User: 靳世坤(1203596603@qq.com)
 * Date: 2015-08-17
 * Time: 20:31
 * Version 1.0
 */

public class BaseActivity extends AppCompatActivity implements IBaseActivity {
    private Context mContext = null;
    private SharedPreferences preferences = null;

    public static final String APP_SETTING_NAME = "app_setting"; // app配置文件名称
    public static final String APP_SETTING_THEME = "app_theme";  //软件主题
    public static final String APP_SETTING_ISLOADIMG = "isLoadImg"; //是否加载图片
    public static final String APP_SETTING_PUSH = "isPush"; //是否接受推送新闻

    public BaseApplication application = null;

    public LoadingDialog loadingDialog;
    public Dialog dialog;

    public boolean isSystemBar = true; //是否改变状态颜色

    private String tag = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        preferences = getSharedPreferences(APP_SETTING_NAME, MODE_ENABLE_WRITE_AHEAD_LOGGING);
        setAppTheme(); //设置APP主题
        super.onCreate(savedInstanceState);

        setSystemBarColor();

        application = (BaseApplication) getApplication();
        application.addActivity(this);

        loadingDialog = new LoadingDialog(getContext());
        dialog = loadingDialog.createLoadingDialog("正在加载数据");

        validateInternet();

    }

    /**
     * 改变系统状态栏颜色
     * */
    public void setSystemBarColor(){
        if(!isSystemBar) return;
        //设定状态栏的颜色，当版本大于4.4，小于5.0时起作用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //此处可以重新指定状态栏颜色
            tintManager.setStatusBarTintResource(R.color.primaryColor);

            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setNavigationBarTintResource(R.color.primaryColor);

        }
    }

    @Override
    public boolean validateInternet() {
        boolean connect = doCheckInternetConnect();
        if(!connect){ //没有网络连接时，弹出对话框，进行网络设置
            showInternetDialog();
        }
        return connect;
    }

    @Override
    public boolean hasInternetConnect() {
        return doCheckInternetConnect();
    }

    @Override
    public void isExit() {
        //
        stopService();
    }

    @Override
    public void checkMemoryCard() {
        if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            showCardDialog();
        }
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public SharedPreferences getAppSettingSharedPre() {
        return preferences;
    }

    @Override
    public int getThemeSetting() {
        return preferences.getInt(APP_SETTING_THEME, -1);
    }

    /**
     * 设置主题
     * */
    public void setThemeSetting(int resStyle){
        SharedPreferences.Editor ed = preferences.edit();
        ed.putInt(APP_SETTING_THEME, resStyle);
        ed.commit();
        recreate(); //重启
    }

    /**
     * 清除设置信息
     * */
    public void clearSetting(){
        SharedPreferences.Editor ed = preferences.edit();
        ed.putInt(APP_SETTING_THEME, -1);
        ed.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 对话框
     * @param title 对话框标题
     * @param msg	对话框显示的小时内容
     * @param listener 确认的点击事件
     *
     * @return
     * */
    public AlertDialog getDialog(String title, String msg, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setTitle(title);
        if(listener!=null){
            builder.setPositiveButton("确认", listener);
        }
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog aDialog = builder.create();

        return aDialog;
    }

    /**
     * 判断网络是否已经连接
     * */
    private boolean doCheckInternetConnect(){
        ConnectivityManager manager = (ConnectivityManager)mContext.getSystemService(CONNECTIVITY_SERVICE);
        if(manager==null){
            return false;
        } else {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if(info!=null && info.isConnectedOrConnecting()){
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void startService() {
        Log.i(tag, "--111---startService()");
    }

    @Override
    public void stopService() {
        Log.i(tag, "---116---stopService()");
    }

    /**
     * 网络设置对话框
     * */
    private void showInternetDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("网络设置");
        builder.setMessage("世界上最遥远的距离就是没网。");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS); //网络设置界面
                mContext.startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    /**
     * 检查内存卡对话框
     * */
    private void showCardDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("设置");
        builder.setMessage("储存卡无法使用。");
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS); //网络设置界面
                mContext.startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    /**
     * 设置APP主题
     * */
    private void setAppTheme(){
        int theme = getThemeSetting();
        if(theme>0){
            this.setTheme(getThemeSetting()); //设置APP主题
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                BaseActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
