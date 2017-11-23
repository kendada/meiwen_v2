package cc.meiwen.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.koudai.kbase.widget.dialog.KTipDialog;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import cc.meiwen.R;
import cc.meiwen.model.FriendsTimelineStatusesBO;
import cc.meiwen.model.Post;
import cc.meiwen.model.PostType;
import cc.meiwen.model.User;
import cc.meiwen.model.WeiBoUserBO;
import cc.meiwen.util.FileUtils;
import cc.meiwen.util.MnDateUtil;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by abc on 2017/11/22.
 *
 */

public class WeiBoListAdapter extends BaseQuickAdapter<FriendsTimelineStatusesBO, BaseViewHolder> {

    private String TAG = WeiBoListAdapter.class.getSimpleName();

    private Context context;
    private FileUtils fileUtils;
    private KTipDialog loadingDialog;

    private PostType mPostType;

    public WeiBoListAdapter(@Nullable List<FriendsTimelineStatusesBO> data, Context context) {
        super(R.layout.adapter_weibo_list_item_layout, data);
        this.context = context;
        fileUtils = new FileUtils(context);
        loadingDialog = new KTipDialog.Builder(context)
                .setIconType(KTipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在发布")
                .create();
    }

    @Override
    protected void convert(BaseViewHolder helper, final FriendsTimelineStatusesBO item) {
        WeiBoUserBO user = item.user;
        if(user != null){
            helper.setText(R.id.user_info_view, user.screen_name);
            ImageView iconView = helper.getView(R.id.icon_view);
            Glide.with(context).load(user.profile_image_url).into(iconView);
        }
        helper.setText(R.id.text_view, item.text);
        ImageView contentView = helper.getView(R.id.content_view);
        if(!TextUtils.isEmpty(item.original_pic)){
            contentView.setVisibility(View.VISIBLE);
            Glide.with(context).load(item.original_pic).asBitmap().into(contentView);
        } else {
            contentView.setVisibility(View.GONE);
        }
        TextView releaseBtn = helper.getView(R.id.release_btn);
        releaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releaseMW(item);
            }
        });
    }

    /**
     * 设置美文分类
     * */
    public void setPostType(PostType postType){
        mPostType = postType;
    }

    private void releaseMW(final FriendsTimelineStatusesBO item){
        loadingDialog.show();
        if(TextUtils.isEmpty(item.original_pic)){
            upload(null, item.text);
        } else {
            // 获取缓存图片
            Glide.with(context).load(item.original_pic).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    String path = saveBitmap(resource);
                    upload(path, item.text);
                }
            });
        }
    }

    /**
     * 上传图片
     * @param path  图片地址
     * @param text  美文内容
     * */
    private void upload(String path, final String text){
        if(TextUtils.isEmpty(path)){
            postJz(null, text);
        } else {
            File file = new File(path);
            if(file.exists()){
                final BmobFile bmobFile = new BmobFile(file);
                bmobFile.uploadblock(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e != null){
                            loadingDialog.dismiss();
                            Log.d(TAG, "上传文章配图，异常e = " + e);
                        } else {
                            Log.d(TAG, "上传文章配图，成功 = " + bmobFile.getFileUrl());
                            postJz(bmobFile, text);
                        }
                    }
                });
            } else {
                postJz(null, text);
            }
        }
    }

    /**
     * 同步美文
     * @param bmobFile  已上传的图片内容
     * @param text 美文内容
     * */
    private void postJz(BmobFile bmobFile, String text){
        Post mPost = new Post();
        mPost.setUser(BmobUser.getCurrentUser(User.class));
        mPost.setContent(text);
        mPost.setPostType(mPostType);
        mPost.setConImg(bmobFile);

        mPost.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                Log.d(TAG, "e = " + e);
                loadingDialog.dismiss();
            }
        });
    }

    /**
     * 保存图片
     * */
    private String saveBitmap(Bitmap bitmap){
        try {
            String path = fileUtils.saveBitmap(MnDateUtil.stringByFormat(new Date(),
                    MnDateUtil.dateFormatFileName)+".jpg", bitmap);
            return path;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
