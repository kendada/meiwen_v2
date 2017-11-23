package cc.meiwen.ui.activity;

import android.os.Bundle;
import android.text.Editable;
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
import java.util.ArrayList;
import java.util.List;

import cc.meiwen.R;
import cc.meiwen.model.Post;
import cc.meiwen.model.PostType;
import cc.meiwen.model.User;
import cc.meiwen.util.OnTextChangeListener;
import cc.meiwen.view.pickerview.OptionsPickerView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
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

public class SavePostActivity extends BaseImageSelectActivity implements
        View.OnClickListener, OptionsPickerView.OnOptionsSelectListener {

    private PostType postType;
    private User bmobUser;

    private TextView post_btn, words_number_view, chooce_type_btn;
    private EditText edit_post;
    private ImageView image_picker_view, image_view;
    private LinearLayout image_picker_layout;

    private BmobFile bmobFile;

    private KTipDialog loadingDialog;

    private OptionsPickerView mOpv;
    private ArrayList<PostType> postTypeList = new ArrayList<>();

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
        chooce_type_btn = (TextView) findViewById(R.id.chooce_type_btn);
        post_btn = (TextView) findViewById(R.id.post_btn);
        image_picker_view = (ImageView) findViewById(R.id.image_picker_view);
        image_picker_layout = (LinearLayout) findViewById(R.id.image_picker_layout);
        words_number_view = (TextView) findViewById(R.id.words_number_view);
        image_view = (ImageView) findViewById(R.id.image_view);
    }

    /**
     * 初始化数据
     * */
    public void initData(){
        post_btn.setOnClickListener(this);
        chooce_type_btn.setOnClickListener(this);
        image_picker_view.setOnClickListener(this);
        image_view.setOnClickListener(this);

        edit_post.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                words_number_view.setText(s.length() + "/200");
            }
        });
    }

    private boolean isAdmin;

    /**
     * 获取用户信息
     * */
    private void getUser(){
        bmobUser = BmobUser.getCurrentUser(User.class);
        if(bmobUser != null){
            isAdmin = bmobUser.isAdmin();
        }
    }

    /**
     * 获取分类
     * */
    private void getPostType(){
        BmobQuery<PostType> query = new BmobQuery<>();
        query.order("-createdAt");
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.findObjects(new FindListener<PostType>() {
            @Override
            public void done(List<PostType> list, BmobException e) {
                if(list!=null && list.size()>0){
                    postTypeList.addAll(list);
                    postType = list.get(0);
                    createPickerView(postTypeList);
                }
                Log.i(tag, "----|||||||----"+e);
            }
        });
    }

    public void createPickerView(ArrayList<PostType> list){
        mOpv = new OptionsPickerView(this);
        mOpv.setOnoptionsSelectListener(this);
        mOpv.setPicker(list);
        mOpv.setCyclic(false);
        mOpv.setCancelable(true);
    }

    /**
     * 发表帖子
     * */
    private void postData(){
        if(TextUtils.isEmpty(edit_post.getText().toString())) {
            Toast.makeText(this, "美文内容为空", Toast.LENGTH_SHORT).show();
            return;
        }
        loadingDialog.show();
        if(bmobFile != null){
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if(e != null){
                        loadingDialog.dismiss();
                        Log.d(tag, "上传文章配图，异常e = " + e);
                    } else {
                        Log.d(tag, "上传文章配图，成功 = " + bmobFile.getFileUrl());
                        postJz();
                    }
                }
            });
        } else {
            postJz();
        }
    }

    private void postJz(){
        //创建帖子信息
        Post post = new Post();
        post.setContent(edit_post.getText().toString());
        post.setUser(bmobUser);
        post.setPostType(postType);
        post.setIsShow(isAdmin); // 管理员发布美文，则不用审核
        if(bmobFile != null){
            post.setConImg(bmobFile);
        }

        post.save(new SaveListener<String>() {

            @Override
            public void done(String s, BmobException e) {
                loadingDialog.dismiss();
                if(e != null){
                    Log.i(tag, "***onFailure()***e=" + e);
                    Toast.makeText(getContext(), "上传失败：" + s, Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(tag, "***onSuccess()***");
                    String tipsStr = "";
                    if(isAdmin){
                        tipsStr = "帖子发布成功!";
                    } else {
                        tipsStr = "帖子发布成功，管理员会尽快审核！";
                    }
                    Toast.makeText(getContext(), tipsStr, Toast.LENGTH_SHORT).show();
                    SavePostActivity.this.finish();

                }
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
        if (!file.exists()) return;
        bmobFile = new BmobFile(file);
        Glide.with(this).load(path).asBitmap().into(image_view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.post_btn:
                postData();
                break;
            case R.id.chooce_type_btn:
                if(mOpv != null){
                    mOpv.show();
                }
                break;
            case R.id.image_picker_view:
            case R.id.image_picker_layout:
            case R.id.image_view:
                openImageAlbum(true);
                break;
        }
    }

    @Override
    public void onOptionsSelect(int options1, int option2, int options3) {
        if(options1 < postTypeList.size()){
            postType = postTypeList.get(options1);
            chooce_type_btn.setText(postType.getType());
        }
    }
}
