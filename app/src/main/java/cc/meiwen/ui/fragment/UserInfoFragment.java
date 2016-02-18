package cc.meiwen.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cc.meiwen.R;
import cc.meiwen.adapter.MainFragmentAdapter;
import cc.meiwen.model.Post;
import cc.meiwen.model.User;
import cc.meiwen.ui.activity.AppSettingActivity;
import cc.meiwen.ui.activity.BaseActivity;
import cc.meiwen.ui.activity.FavoActivity;
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

public class UserInfoFragment extends BaseFragment implements View.OnClickListener{

    private SwipeRefreshLayout refresh_layout;
    private SelectableRoundedImageView user_icon; //用户头像
    private TextView name, post_count, friends_count, message_count;
    private Button sign_out_btn;
    private LinearLayout favo_btn, more_setting_btn;

    private User bmobUser;

    private List<Post> mList;
    private MainFragmentAdapter adapter;

    private ThreadPoolManager threadPoolManager;

    private String tag = UserInfoFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_info_layout, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        threadPoolManager = new ThreadPoolManager(ThreadPoolManager.TYPE_FIFO, 10);
        //初始化控件
        initViews(view);
        //初始化数据
        initData();
    }

    public void initViews(View view){
        refresh_layout = (SwipeRefreshLayout)view.findViewById(R.id.refresh_layout);
        refresh_layout.setColorSchemeResources(R.color.darkPrimaryColor, R.color.primaryColor, R.color.lightPrimaryColor);

        user_icon = (SelectableRoundedImageView)view.findViewById(R.id.user_icon);
        name = (TextView)view.findViewById(R.id.name);
        post_count = (TextView)view.findViewById(R.id.post_count);
        friends_count = (TextView)view.findViewById(R.id.friends_count);
        message_count = (TextView)view.findViewById(R.id.message_count);

        sign_out_btn = (Button)view.findViewById(R.id.sign_out_btn);
        sign_out_btn.setOnClickListener(this);

        favo_btn = (LinearLayout)view.findViewById(R.id.favo_btn);
        favo_btn.setOnClickListener(this);
        more_setting_btn = (LinearLayout)view.findViewById(R.id.more_setting_btn);
        more_setting_btn.setOnClickListener(this);

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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign_out_btn: //退出
                signOut();
                break;
            case R.id.favo_btn:  //收藏
                Intent fIntent = new Intent(getContext(), FavoActivity.class);
                startActivity(fIntent);
                break;
            case R.id.more_setting_btn:
                Intent mIntent = new Intent(getContext(), AppSettingActivity.class);
                startActivity(mIntent);
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
            }

            @Override
            public Object loadData() {
                BmobUser.logOut(getContext());
                return null;
            }
        };
        threadPoolManager.addAsyncTask(asyncTask);
        threadPoolManager.start();
    }
}
