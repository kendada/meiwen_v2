package cc.meiwen.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.meiwen.R;
import cc.meiwen.event.UserInfoEvent;
import cc.meiwen.model.User;
import cc.meiwen.util.OnTextChangeListener;
import cc.meiwen.view.TitleBar;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by abc on 2017/11/13.
 * 编辑用户签名
 */

public class UpdateUserInfoActivity extends BaseActivity {

    private String TAG = UpdateUserInfoActivity.class.getSimpleName();

    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.edit_post)
    EditText editPost;
    @BindView(R.id.words_number_view)
    TextView wordsNumberView;
    @BindView(R.id.post_btn)
    TextView postBtn;

    private User bmobUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_userinfo_layout);
        ButterKnife.bind(this);

        bmobUser = BmobUser.getCurrentUser(User.class);

        editPost.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                wordsNumberView.setText(s.length() + "/200");
            }
        });

    }

    public String getInfo(){
        return editPost.getText().toString();
    }

    @OnClick(R.id.post_btn)
    public void onViewClicked() {
        if (TextUtils.isEmpty(getInfo())) {
            Toast.makeText(getContext(), "请输入签名内容", Toast.LENGTH_SHORT).show();
            return;
        }
        updateInfo();
    }

    private void updateInfo(){
        bmobUser.setUserInfo(getInfo());
        bmobUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                Log.d(TAG, " e = " + e);
                if(e == null){
                    EventBus.getDefault().post(new UserInfoEvent(getInfo()));
                    UpdateUserInfoActivity.this.finish();
                }
            }
        });
    }
}
