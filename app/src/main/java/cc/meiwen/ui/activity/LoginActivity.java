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
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cc.meiwen.R;
import cc.meiwen.event.SignUpEvent;
import cc.meiwen.model.User;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

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
    private TextView qq_login_btn;

    private Tencent mTencent;

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

        initTencent(); //腾讯

    }

    public void initViews(){
        no_pass_btn = (Button)findViewById(R.id.no_pass_btn);
        regis_btn = (Button)findViewById(R.id.regis_btn);
        login_btn = (TextView) findViewById(R.id.login_btn);
        edit_name = (EditText)findViewById(R.id.edit_name);
        edit_pass = (EditText)findViewById(R.id.edit_pass);
        qq_login_btn = (TextView)findViewById(R.id.qq_login_btn);

    }

    public void initData(){
        regis_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        no_pass_btn.setOnClickListener(this);
        qq_login_btn.setOnClickListener(this);
    }

    /**
     * 初始化第三方登录
     * */
    private void initTencent(){
        mTencent = Tencent.createInstance("1104956192", this);
    }

    /**
     * 腾讯登录
     * */
    private void tencentLogin(){
        //判断qq用户是否已经登录
        if(mTencent.isSessionValid() && mTencent.getOpenId() != null){
            Log.i(tag, "已经登录。。。");
        } else {
            Log.i(tag, "没有登录。。。");
            mTencent.login(this, "all", new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    Log.i(tag, "---83----" + o);
                }

                @Override
                public void onError(UiError uiError) {
                    Log.i(tag, "----88---" + uiError);
                }

                @Override
                public void onCancel() {
                    Log.i(tag, "---93----onCancel()");
                }
            });
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    Log.i(tag, "---112----" + o);


                    getTencentUserInfo();
                }

                @Override
                public void onError(UiError uiError) {
                    Log.i(tag, "----117---" + uiError);
                }

                @Override
                public void onCancel() {
                    Log.i(tag, "---122----onCancel()");
                }
            });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getTencentUserInfo(){
        UserInfo info = new UserInfo(this, mTencent.getQQToken());
        info.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Log.i(tag, "----138---"+o);
            }

            @Override
            public void onError(UiError uiError) {
                Log.i(tag, "----143---"+uiError);
            }

            @Override
            public void onCancel() {
                Log.i(tag, "---148---onCancel()---");
            }
        });
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
            case R.id.qq_login_btn: //QQ 登录
                tencentLogin();
                break;
        }
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

        BmobUser user = new BmobUser();
        user.setUsername(name);
        user.setPassword(pass);

        user.login(new SaveListener<User>() {
            @Override
            public void onStart() {
                loadingDialog.show();
            }

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
            }

            @Override
            public void onFinish() {
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
