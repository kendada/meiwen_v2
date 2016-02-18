package cc.meiwen.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import cc.meiwen.R;
import cc.meiwen.model.Post;
import cc.meiwen.model.PostType;
import cc.meiwen.model.User;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-03
 * Time: 14:26
 * Version 1.0
 * Info 发布帖子
 */

public class SavePostActivity extends BaseActivity implements View.OnClickListener{

    private PostType postType;
    private User bmobUser;

    private Button post_btn;
    private EditText edit_post;
    private Toolbar toolbar;

    private String tag = SavePostActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_post_layout);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("发布帖子");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //设置点击返回事件

        //初始化控件
        initViews();
        //初始化数据
        initData();

        //获取用户信息
        getUser();
        //获取分类
        getPostType();
    }

    /**
     * 初始化控件
     * */
    public void initViews(){
        edit_post = (EditText)findViewById(R.id.edit_post);
        post_btn = (Button)findViewById(R.id.post_btn);
    }

    /**
     * 初始化数据
     * */
    public void initData(){
        post_btn.setOnClickListener(this);
    }

    /**
     * 获取用户信息
     * */
    private void getUser(){
        bmobUser = BmobUser.getCurrentUser(this, User.class);
    }

    /**
     * 获取分类
     * */
    private void getPostType(){
        BmobQuery<PostType> query = new BmobQuery<>();
        query.findObjects(this, new FindListener<PostType>() {
            @Override
            public void onSuccess(List<PostType> list) {
                if(list!=null && list.size()>0){
                    postType = list.get(0);
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.i(tag, "----"+s);
            }
        });
    }

    /**
     * 发表帖子
     * */
    private void postData(){
        if(TextUtils.isEmpty(edit_post.getText().toString())) return;
        //创建帖子信息
        Post post = new Post();
        post.setContent(edit_post.getText().toString());
        post.setUser(bmobUser);
        post.setPostType(postType);

        post.save(getContext(), new SaveListener() {
            @Override
            public void onStart() {
                loadingDialog.setText("正在发布帖子");
                dialog.show();
                loadingDialog.startAnim();
            }

            @Override
            public void onSuccess() {
                Log.i(tag, "***onSuccess()***");
                Toast.makeText(getContext(), "帖子发布成功，管理员会尽快审核！", Toast.LENGTH_SHORT).show();
                SavePostActivity.this.finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i(tag, "***onFailure()***i=" + i + "****s=" + s);
                dialog.dismiss();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.post_btn:
                postData();
                break;
        }
    }
}
