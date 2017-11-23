package cc.meiwen.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.koudai.kbase.widget.dialog.KTipDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cc.meiwen.R;
import cc.meiwen.event.SignUpEvent;
import cc.meiwen.model.User;
import cc.meiwen.util.AppUtils;
import cc.meiwen.util.weibo.AccessTokenKeeper;
import cc.meiwen.util.weibo.Oauth2AccessToken;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-30
 * Time: 14:52
 * Version 1.0
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private String tag = LoginActivity.class.getSimpleName();

    private TextView login_btn;
    private Button regis_btn, no_pass_btn;
    private EditText edit_name, edit_pass;

    //第三方登录
    private TextView weibo_btn;

    private KTipDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        EventBus.getDefault().register(this);

        loadingDialog = new KTipDialog.Builder(this)
                .setIconType(KTipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在登录")
                .create();

        initViews();

        initData();
    }

    public void initViews(){
        no_pass_btn = (Button)findViewById(R.id.no_pass_btn);
        regis_btn = (Button)findViewById(R.id.regis_btn);
        login_btn = (TextView) findViewById(R.id.login_btn);
        edit_name = (EditText)findViewById(R.id.edit_name);
        edit_pass = (EditText)findViewById(R.id.edit_pass);
        weibo_btn = (TextView) findViewById(R.id.weibo_btn);

    }

    public void initData(){
        regis_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        no_pass_btn.setOnClickListener(this);
        weibo_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.regis_btn:
                regisUser(); //游客注册
                break;
            case R.id.login_btn:
                loginUser(); //用户登录
                break;
            case R.id.no_pass_btn: //游客模式
                noPassGo();
                break;
            case R.id.weibo_btn: // 微博 登录
                weiboLogin();
                break;
        }
    }

    private void weiboLogin(){
        loadingDialog.show();
        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        weibo.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Log.d(tag, "arg0 = " + arg0 + ", arg1 = " + arg1 + ", arg2 = " + arg2);
                dissmisDialog(1);
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                //输出所有授权信息
                String result = arg0.getDb().exportData();
                Log.d(tag, "result = " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String nickname = jsonObject.optString("nickname");
                    String userID = jsonObject.optString("userID");
                    String resume = jsonObject.optString("resume");
                    String icon = jsonObject.optString("icon");
                    String snsUserUrl = jsonObject.optString("snsUserUrl");

                    String token = jsonObject.optString("token");
                    String expiresTime = jsonObject.optString("expiresTime");

                    Oauth2AccessToken oauth2AccessToken = new Oauth2AccessToken();
                    Bundle mBundle = new Bundle();
                    mBundle.putString("uid", userID);
                    mBundle.putString("access_token", token);
                    mBundle.putString("expires_in", expiresTime);
                    oauth2AccessToken = Oauth2AccessToken.parseAccessToken(mBundle);
                    AccessTokenKeeper.writeAccessToken(AppUtils.getCtx(), oauth2AccessToken);

                    weiboUserLogin(nickname, userID, resume, icon, snsUserUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                Log.d(tag, "arg0 = " + arg0 + ", arg1 = " + arg1);
                dissmisDialog(2);
            }
        });
        weibo.authorize();
    }

    private void dissmisDialog(final int type){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();
                String text = "";
                switch (type){
                    case 1:
                        text = "登录微博发生异常，请重试！";
                        break;
                    case 2:
                        text = "你已取消微博登录";
                        break;
                }
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void weiboUserLogin(String nickname, String userId, String resume, String iconUrl, String snsUserUrl){
        final User user = new User();
        user.setUsername(nickname);
        user.setPassword(userId);
        user.setUserInfo(resume);
        user.setIconUrl(iconUrl);
        user.setSnsUserUrl(snsUserUrl);
        user.setLoginType("WEIBO");

        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user1, BmobException e) {
                Log.d(tag, "e = " + e);
                doLogin(user);
            }
        });
    }

    /**
     * 注册用户
     * */
    private void regisUser(){
        Intent intent = new Intent(this, RegisUserActivity.class);
        startActivity(intent);
    }

    /**
     * 游客访问模式
     * */
    private void noPassGo(){
        Intent intent = new Intent(this, NoPassActivity.class);
        startActivity(intent);
    }

    /**
     *  用户登录
     * */
    private void loginUser(){
        String name = edit_name.getText().toString();
        String pass = edit_pass.getText().toString();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(pass)){
            Toast.makeText(getContext(), "用户名或密码为空", Toast.LENGTH_SHORT).show();
            return;
        }

        loadingDialog.show();
        BmobUser user = new BmobUser();
        user.setUsername(name);
        user.setPassword(pass);

        doLogin(user);
    }

    private void doLogin(BmobUser user){
        user.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e != null){
                    Log.i(tag, "****110****onFailure()***e=" + e);
                    Toast.makeText(getContext(), "登录失败，请重试！", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(tag, "****76****onSuccess()");
                    Toast.makeText(getContext(), "登录成功！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivityV2.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                }
                loadingDialog.dismiss();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSignUpEvent(SignUpEvent event){
        if(event.isSuccess) finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
