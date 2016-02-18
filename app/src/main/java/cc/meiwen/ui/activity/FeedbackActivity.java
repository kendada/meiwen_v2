package cc.meiwen.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cc.meiwen.R;
import cc.meiwen.model.Feedback;
import cc.meiwen.model.User;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-06
 * Time: 14:42
 * Version 1.0
 */

public class FeedbackActivity extends BaseActivity{

    private Toolbar toolbar;
    private EditText edit_post;
    private Button post_btn;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_post_layout);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("反馈");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edit_post = (EditText)findViewById(R.id.edit_post);
        edit_post.setHint("感谢您的反馈");
        post_btn = (Button)findViewById(R.id.post_btn);
        post_btn.setText("提交反馈");

        user = BmobUser.getCurrentUser(getContext(), User.class);

        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postFeedback();
            }
        });

    }

    private void postFeedback(){
        String content = edit_post.getText().toString();

        if(TextUtils.isEmpty(content)) {
            Toast.makeText(getContext(), "请输入反馈内容", Toast.LENGTH_SHORT).show();
            return;
        }

        Feedback feedback = new Feedback(content, user);
        feedback.save(getContext(), new SaveListener() {
            @Override
            public void onStart() {
                loadingDialog.setText("正在提交反馈");
                dialog.show();
                loadingDialog.startAnim();
            }

            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "感谢您的反馈", Toast.LENGTH_SHORT).show();
                FeedbackActivity.this.finish();
            }

            @Override
            public void onFailure(int i, String s) {

            }

            @Override
            public void onFinish() {
                dialog.dismiss();
            }
        });

    }


}
