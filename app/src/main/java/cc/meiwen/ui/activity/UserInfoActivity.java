package cc.meiwen.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cc.meiwen.R;
import cc.meiwen.adapter.MainFragmentAdapter;
import cc.meiwen.model.Post;
import cc.meiwen.model.User;
import cc.meiwen.util.task.AsyncTask;
import cc.meiwen.util.task.ThreadPoolManager;
import cc.meiwen.view.SelectableRoundedImageView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.CountListener;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-31
 * Time: 12:14
 * Version 1.0
 */

public class UserInfoActivity extends BaseActivity implements View.OnClickListener{

    private SwipeRefreshLayout refresh_layout;
    private Toolbar toolbar;
    private SelectableRoundedImageView user_icon; //用户头像
    private TextView name, post_count, friends_count, message_count;
    private Button sign_out_btn;

    private User bmobUser;

    private List<Post> mList;
    private MainFragmentAdapter adapter;

    private ThreadPoolManager threadPoolManager;

    private String tag = UserInfoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_layout);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("个人主页");
        setSupportActionBar(toolbar);
        //设置点击事件
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        threadPoolManager = new ThreadPoolManager(ThreadPoolManager.TYPE_FIFO, 10);

        //初始化控件
        initViews();
        //初始化数据
        initData();
    }

    public void initViews(){
        refresh_layout = (SwipeRefreshLayout)findViewById(R.id.refresh_layout);
        refresh_layout.setColorSchemeResources(R.color.darkPrimaryColor, R.color.primaryColor, R.color.lightPrimaryColor);

        user_icon = (SelectableRoundedImageView)findViewById(R.id.user_icon);
        name = (TextView)findViewById(R.id.name);
        post_count = (TextView)findViewById(R.id.post_count);
        friends_count = (TextView)findViewById(R.id.friends_count);
        message_count = (TextView)findViewById(R.id.message_count);

        sign_out_btn = (Button)findViewById(R.id.sign_out_btn);
        sign_out_btn.setOnClickListener(this);

    }

    public void initData(){
        //用户信息
        getInfo();
    }


    /**
     * 获取用户信息
     * */
    private void getInfo(){
        bmobUser = BmobUser.getCurrentUser(getContext(), User.class);
        if(bmobUser!=null){
            name.setText(bmobUser.getUsername());
            //获取登录用户发布帖子的数量
            getPostCount();
        }
    }

    /**
     * 获取发布帖子的数量
     * */
    private void getPostCount(){
        BmobQuery<Post> query = new BmobQuery<>();
        query.addWhereEqualTo("user", bmobUser.getObjectId());
        query.count(getContext(), Post.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                post_count.setText(String.valueOf(i));
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                UserInfoActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign_out_btn:
                signOut();
                break;
        }
    }

    /**
     * 退出登录
     * */
    private void signOut(){
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            public void updata(Object obj) {
                Toast.makeText(getContext(), "已经退出", Toast.LENGTH_SHORT).show();
                UserInfoActivity.this.finish();
            }

            @Override
            public Object loadData() {
                BmobUser.logOut(getContext());
                application.finishAllActivity(); //退出app
                clearSetting(); //清除配置信息
                return null;
            }
        };
        threadPoolManager.addAsyncTask(asyncTask);
        threadPoolManager.start();
    }
}
