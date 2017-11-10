package cc.meiwen.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.koudai.kbase.widget.dialog.KTipDialog;

import java.io.File;
import java.util.List;

import cc.meiwen.R;
import cc.meiwen.model.JZBmobFile;
import cc.meiwen.model.Post;
import cc.meiwen.model.PostType;
import cc.meiwen.model.User;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-03
 * Time: 14:26
 * Version 1.0
 * Info 发布帖子
 */

public class SavePostActivity extends BaseImageSelectActivity implements View.OnClickListener{

    private PostType postType;
    private User bmobUser;

    private TextView post_btn;
    private EditText edit_post;
    private ImageView image_picker_view;
    private LinearLayout image_picker_layout;

    private JZBmobFile bmobFile;

    private KTipDialog loadingDialog;

    private String tag = SavePostActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_post_layout);

        loadingDialog = new KTipDialog.Builder(this)
                .setIconType(KTipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在上传")
                .create();

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
        post_btn = (TextView) findViewById(R.id.post_btn);
        image_picker_view = (ImageView) findViewById(R.id.image_picker_view);
        image_picker_layout = (LinearLayout) findViewById(R.id.image_picker_layout);
    }

    /**
     * 初始化数据
     * */
    public void initData(){
        post_btn.setOnClickListener(this);
        image_picker_view.setOnClickListener(this);
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
        if(TextUtils.isEmpty(edit_post.getText().toString())) {
            Toast.makeText(this, "美文内容为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //创建帖子信息
        Post post = new Post();
        post.setContent(edit_post.getText().toString());
        post.setUser(bmobUser);
        post.setPostType(postType);
        if(bmobFile != null){
            post.setConImg(bmobFile);
        }

        post.save(getContext(), new SaveListener() {
            @Override
            public void onStart() {
                loadingDialog.show();
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
                Toast.makeText(getContext(), "上传失败：" + s, Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    @Override
    public int getCropHeight() {
        return (int) (super.getCropHeight()*0.7f);
    }

    @Override
    protected void onImageSelected(String path) {
        Log.i(tag, "path = " + path);

        File file = new File(path);
        bmobFile = new JZBmobFile(file);
        bmobFile.setFilename(file.getName());
        bmobFile.uploadblock(this, new UploadFileListener() {
            @Override
            public void onProgress(Integer value) {
                Log.i(tag, "progress = " + value);
            }

            @Override
            public void onSuccess() {
                Log.i(tag, "onSuccess()");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i(tag, "onFailure , s = " + s + ", i = " + i);
            }
        });

        Glide.with(this).load(path).asBitmap().into(image_picker_view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.post_btn:
                postData();
                break;
            case R.id.image_picker_view:
            case R.id.image_picker_layout:
                openImageAlbum(true);
                break;
        }
    }
}
