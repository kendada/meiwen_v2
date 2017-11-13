package cc.meiwen.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.koudai.kbase.widget.dialog.KTipDialog;

import cc.meiwen.R;
import cc.meiwen.model.Feedback;
import cc.meiwen.model.User;
import cc.meiwen.util.OnTextChangeListener;
import cc.meiwen.view.TitleBar;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-06
 * Time: 14:42
 * Version 1.0
 */

public class FeedbackActivity extends BaseActivity{

    private EditText edit_post;
    private TextView post_btn;
    private TitleBar title_bar;
    private TextView words_number_view;

    private User user;

    private KTipDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_layout);

        title_bar = (TitleBar) findViewById(R.id.title_bar);
        title_bar.setTitleText("反馈");
        edit_post = (EditText)findViewById(R.id.edit_post);
        edit_post.setHint("感谢您的反馈");
        post_btn = (TextView) findViewById(R.id.post_btn);
        post_btn.setText("提交反馈");
        words_number_view = (TextView) findViewById(R.id.words_number_view);

        user = BmobUser.getCurrentUser(User.class);

        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postFeedback();
            }
        });

        edit_post.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                words_number_view.setText(s.length() + "/200");
            }
        });

        loadingDialog = new KTipDialog.Builder(this)
                .setIconType(KTipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在提交反馈")
                .create();
    }

    private void postFeedback(){
        String content = edit_post.getText().toString();

        if(TextUtils.isEmpty(content)) {
            Toast.makeText(getContext(), "请输入反馈内容", Toast.LENGTH_SHORT).show();
            return;
        }
        loadingDialog.show();
        Feedback feedback = new Feedback(content, user);
        feedback.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                loadingDialog.dismiss();
                if(e == null){
                    Toast.makeText(getContext(), "感谢您的反馈", Toast.LENGTH_SHORT).show();
                    FeedbackActivity.this.finish();
                } else {
                    Toast.makeText(getContext(), "反馈出现错误，请添加小编微信进行反馈！", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}
