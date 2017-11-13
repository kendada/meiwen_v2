package cc.meiwen.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import cc.meiwen.R;
import cc.meiwen.util.CopyUtil;
import cc.meiwen.view.SwipeBackLayout;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-06
 * Time: 11:09
 * Version 1.0
 */

public class AboutActivity extends BaseActivity{

    private SwipeBackLayout swip_back_layout;
    private ScrollView scrollView;
    private TextView qq_btn, how_btn, wx_btn, wx_user_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_layout);

        scrollView = (ScrollView)findViewById(R.id.scrollView);
        swip_back_layout = (SwipeBackLayout)findViewById(R.id.swip_back_layout);
        swip_back_layout.setTouchView(scrollView);
        swip_back_layout.setOnSwipeBackListener(new SwipeBackLayout.OnSwipeBackListener() {
            @Override
            public void onFinishActivity() {
                AboutActivity.this.finish();
            }
        });

        qq_btn = (TextView)findViewById(R.id.qq_btn);
        qq_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CopyUtil(getContext()).copy("287292031");
            }
        });
        wx_btn = (TextView) findViewById(R.id.wx_btn);
        wx_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CopyUtil(getContext()).copy("图说微情话");
            }
        });
        wx_user_btn = (TextView) findViewById(R.id.wx_user_btn);
        wx_user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CopyUtil(getContext()).copy("vqh1314");
            }
        });
        how_btn = (TextView)findViewById(R.id.how_btn);
        how_btn.setText("使用说明：\r\n" +
                "1. 每条美文长按可复制；\r\n" +
                "2. 用户可收藏美文，同步到云端，永不丢失；\r\n" +
                "3. 用户发布美文，将会通过管理员审核；【为了保证美文的质量】\r\n" +
                "4. 用户可申请做管理员，请添加QQ群联系群主；");

    }

}
