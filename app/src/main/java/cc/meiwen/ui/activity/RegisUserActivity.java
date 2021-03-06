package cc.meiwen.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.koudai.kbase.widget.dialog.KTipDialog;

import cc.meiwen.R;
import cc.meiwen.model.User;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-30
 * Time: 15:26
 * Version 1.0
 */

public class RegisUserActivity extends BaseActivity implements View.OnClickListener{

    private String tag = RegisUserActivity.class.getSimpleName();

    private TextView regis_btn, login_btn;
    private EditText edit_name, edit_email, edit_pass;

    private KTipDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis_layout);

        //初始化控件
        initViews();
        //初始化数据
        initData();

    }

    public void initViews(){
        loadingDialog = new KTipDialog.Builder(getContext())
                .setIconType(KTipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在注册")
                .create();

        regis_btn = (TextView)findViewById(R.id.regis_btn);
        login_btn = (TextView)findViewById(R.id.login_btn);
        edit_name = (EditText)findViewById(R.id.edit_name);
        edit_email = (EditText)findViewById(R.id.edit_email);
        edit_pass = (EditText)findViewById(R.id.edit_pass);
    }

    public void initData(){
        regis_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);
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
        }
    }

    /**
     * 注册用户
     * */
    private void regisUser(){
        String name = edit_name.getText().toString();
        String email = edit_email.getText().toString();
        String pass = edit_pass.getText().toString();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
            Toast.makeText(getContext(), "用户名，邮箱或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        loadingDialog.show();

        User user = new User();
        user.setUsername(name);
        user.setPassword(pass);
        user.setEmail(email);

        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                loadingDialog.dismiss();
                if(e == null){
                    Log.i(tag, "****76****onSuccess()");
                    loginUser();
                } else {
                    Log.i(tag, "****81****onFailure()***e=" + e);
                }
            }
        });
    }

    public void loginUser(){
        RegisUserActivity.this.finish();
    }
}
