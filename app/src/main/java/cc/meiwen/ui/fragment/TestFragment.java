package cc.meiwen.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import cc.meiwen.R;
import cc.meiwen.model.Post;
import cc.meiwen.model.User;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-30
 * Time: 13:44
 * Version 1.0
 */

public class TestFragment extends BaseFragment implements View.OnClickListener{

    private String tag = TestFragment.class.getSimpleName();

    private TextView text;
    private Button btn, post_btn;

    private User bmobUser = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(tag, "****23****onCreateView()");
        View rootView = inflater.inflate(R.layout.fragment_main_layout, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(tag, "****30****onViewCreated()");
        initViews(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(tag, "****36****onActivityCreated()");
        initData();

        getPostData();
    }

    @Override
    public void initViews(View view) {
        text = (TextView)view.findViewById(R.id.text);
        btn = (Button)view.findViewById(R.id.btn);
        post_btn = (Button)view.findViewById(R.id.post_btn);
    }

    @Override
    public void initData() {
        //获取用户信息
        getInfo();

        btn.setOnClickListener(this);
        post_btn.setOnClickListener(this);

    }

    /**
     * 上传头像
     * */
    private void uploadIcon(){
        String fileName = "/storage/emulated/0/Download/img01.jpg";
//        BTPFileResponse response = BmobProFile.getInstance(getContext()).upload(fileName, new UploadListener() {
//            @Override
//            public void onSuccess(String fileName,String url,BmobFile file) {
//                Log.i(tag, "文件上传成功：" + fileName + ",可访问的文件地址：" + file.getUrl());
//                updateUser(file.getUrl());  //更新用户信息
//            }
//
//            @Override
//            public void onProgress(int progress) {
//                Log.i(tag,"onProgress :"+progress);
//            }
//
//            @Override
//            public void onError(int statuscode, String errormsg) {
//                Log.i(tag,"文件上传失败："+errormsg);
//            }
//        });
    }

    /**
     * 更新用户信息
     * */
    private void updateUser(String iconUrl){
        if(TextUtils.isEmpty(iconUrl)) return;

        //官方推荐
        User user = new User();
        user.setIconUrl(iconUrl); //设置用户头像

        user.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e != null){
                    Log.i("bmob","***done()*** e = " + e);
                } else {
                    Log.i("bmob","***done()***");
                }
            }
        });
    }

    /**
     * 获取用户信息
     * */
    private void getInfo(){
        bmobUser = BmobUser.getCurrentUser(User.class);
        if(bmobUser!=null){
            StringBuilder sb = new StringBuilder();
            sb.append("用户信息："+"\r\n");
            sb.append(bmobUser.getUsername()+"\r\n");
            sb.append(bmobUser.getEmail()+"\r\n");
            sb.append(bmobUser.getSessionToken()+"\r\n");
            sb.append(bmobUser.getAge()+"\r\n");
            sb.append(bmobUser.getSex()+"\r\n");
            sb.append(bmobUser.getIconUrl()+"\r\n");

            text.setText(sb.toString());
        }
    }

    /**
     * 发表帖子
     * */
    private void postData(){
        //创建帖子信息
        Post post = new Post();
        post.setTitle("爱的分别");
        post.setContent(getActivity().getResources().getString(R.string.test));
        post.setUser(bmobUser);

        post.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e != null){
                    Log.i(tag,"***done()*** e = "+e);
                } else {
                    Log.i(tag,"***done()*** s = " + s);
                }
            }
        });
    }

    /**
     * 获取已经发布帖子
     * */
    private void getPostData(){
        BmobQuery<Post> query = new BmobQuery<>();
        query.addWhereEqualTo("author", bmobUser);
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if(list != null){
                    Log.i(tag, "---180----"+list);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn:
                uploadIcon();
                break;
            case R.id.post_btn:
                postData();
                break;
        }
    }
}
