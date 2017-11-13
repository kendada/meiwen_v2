package cc.meiwen.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

import cc.meiwen.R;
import cc.meiwen.model.Comment;
import cc.meiwen.model.Post;
import cc.meiwen.model.User;
import cc.meiwen.ui.activity.AppSettingActivity;
import cc.meiwen.ui.activity.FavoActivity;
import cc.meiwen.ui.activity.FriendListActivity;
import cc.meiwen.ui.activity.MeActivity;
import cc.meiwen.ui.activity.MessageActivity;
import cc.meiwen.ui.activity.SavePostActivity;
import cc.meiwen.util.task.AsyncTask;
import cc.meiwen.util.task.ThreadPoolManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-31
 * Time: 12:14
 * Version 1.0
 */

public class UserInfoFragment extends BaseImageSelectFragment implements View.OnClickListener{

    private ImageView user_icon; //用户头像
    private TextView name, post_count, friends_count, message_count, user_explain_view;
    private LinearLayout favo_btn, more_setting_btn, publish_layout,
            post_count_layout, user_friend_layout, message_layout;

    private User bmobUser;

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
        user_icon = (ImageView)view.findViewById(R.id.user_icon);
        user_icon.setOnClickListener(this);
        name = (TextView)view.findViewById(R.id.name);
        post_count = (TextView)view.findViewById(R.id.post_count);
        friends_count = (TextView)view.findViewById(R.id.friends_count);
        message_count = (TextView)view.findViewById(R.id.message_count);
        user_explain_view = (TextView) view.findViewById(R.id.user_explain_view);
        post_count_layout = (LinearLayout) view.findViewById(R.id.post_count_layout);
        post_count_layout.setOnClickListener(this);

        user_friend_layout = (LinearLayout) view.findViewById(R.id.user_friend_layout);
        user_friend_layout.setOnClickListener(this);

        message_layout = (LinearLayout) view.findViewById(R.id.message_layout);
        message_layout.setOnClickListener(this);

        favo_btn = (LinearLayout)view.findViewById(R.id.favo_btn);
        favo_btn.setOnClickListener(this);
        more_setting_btn = (LinearLayout)view.findViewById(R.id.more_setting_btn);
        more_setting_btn.setOnClickListener(this);
        publish_layout = (LinearLayout)view.findViewById(R.id.publish_layout);
        publish_layout.setOnClickListener(this);

    }

    public void initData(){
        //用户信息
        getInfo();
    }


    /**
     * 获取用户信息
     * */
    private void getInfo(){
        bmobUser = BmobUser.getCurrentUser(User.class);
        if(bmobUser!=null){
            name.setText(bmobUser.getUsername());
            if(!TextUtils.isEmpty(bmobUser.getUserInfo())){
                user_explain_view.setText(bmobUser.getUserInfo());
            }
            BmobFile iconBmobFile = bmobUser.getIcon();
            if(iconBmobFile != null){
                Glide.with(this).load(iconBmobFile.getFileUrl()).asBitmap().into(user_icon);
            }
            //获取登录用户发布帖子的数量
            getPostCount();
            getCommentCount();
        }
        getFriendCount();
    }

    /**
     * 获取发布帖子的数量
     * */
    private void getPostCount(){
        BmobQuery<Post> query = new BmobQuery<>();
        query.addWhereEqualTo("user", bmobUser.getObjectId());
        query.count(Post.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                post_count.setText(String.valueOf(integer));
            }
        });
    }

    /**
     * 获取好友数量
     * */
    private void getFriendCount(){
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("isAdmin", true);
        query.count(User.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                friends_count.setText(String.valueOf(integer));
            }
        });
    }

    /**
     * 获取评论数量
     * */
    private void getCommentCount(){
        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("user", bmobUser.getObjectId());
        query.count(Comment.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                message_count.setText(String.valueOf(integer));
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.message_layout:
                MessageActivity.start(getContext());
                break;
            case R.id.user_friend_layout: // 好友
                FriendListActivity.start(getContext(), 1);
                break;
            case R.id.post_count_layout: // 登录用户发布的美文
                startActivity(new Intent(getContext(), MeActivity.class));
                break;
            case R.id.user_icon: // 设置头像
                openImageAlbum(true);
                break;
            case R.id.sign_out_btn: //退出
                signOut();
                break;
            case R.id.favo_btn:  //收藏
                Intent fIntent = new Intent(getContext(), FavoActivity.class);
                startActivity(fIntent);
                break;
            case R.id.more_setting_btn: // 更多设置
                Intent mIntent = new Intent(getContext(), AppSettingActivity.class);
                startActivity(mIntent);
                break;
            case R.id.publish_layout: //  发布美文
                startActivity(new Intent(getContext(), SavePostActivity.class));
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
                BmobUser.logOut();
                return null;
            }
        };
        threadPoolManager.addAsyncTask(asyncTask);
        threadPoolManager.start();
    }

    @Override
    public void onImageSelected(String path) {
        File file = new File(path);
        if(!file.exists()) return;
        Glide.with(this).load(path).asBitmap().into(user_icon);
        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e != null){
                    Log.d(tag, "上传用户头像，异常e = " + e);
                } else {
                    Log.d(tag, "上传用户头像，成功 = " + bmobFile.getFileUrl());
                    updateIconUrl(bmobFile);
                }
            }
        });
    }

    /**
     * 更新用户头像
     * */
    private void updateIconUrl(BmobFile bmobFile){
        if(bmobUser == null) return;
        bmobUser.setIcon(bmobFile);
        bmobUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e != null){
                    Log.d(tag, "更新用户头像，异常e = " + e);
                } else {
                    Log.d(tag, "更新用户头像，成功 = ");
                }
            }
        });
    }
}
