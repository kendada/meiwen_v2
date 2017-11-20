package cc.meiwen.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cc.meiwen.R;
import cc.meiwen.adapter.base.AdapterHolder;
import cc.meiwen.adapter.base.MnBaseAdapter;
import cc.meiwen.model.Favo;
import cc.meiwen.model.Post;
import cc.meiwen.model.PostType;
import cc.meiwen.model.User;
import cc.meiwen.ui.activity.PostCommentActivity;
import cc.meiwen.util.ImageConfigBuilder;
import cc.meiwen.util.MnAppUtil;
import cc.meiwen.util.MnDateUtil;
import cc.meiwen.util.ShareUtil;
import cc.meiwen.view.SelectableRoundedImageView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-31
 * Time: 16:40
 * Version 1.0
 */

public class MainFragmentAdapter extends MnBaseAdapter<Post>{

    private User user = null;

    private int ph;

    private String tag = MainFragmentAdapter.class.getSimpleName();

    public MainFragmentAdapter(Context context, List<Post> datas) {
        super(context, datas);
        user = BmobUser.getCurrentUser(User.class);
        ph = MnAppUtil.getPhoneH(mContext)/3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        AdapterHolder adapterHolder = AdapterHolder.get(position, convertView, viewGroup, R.layout.adapter_user_info_post_layout);
        TextView post_type_txt = adapterHolder.getView(R.id.post_type_txt);
        TextView post_type_title = adapterHolder.getView(R.id.post_type_title);
        TextView post_content_txt = adapterHolder.getView(R.id.post_content_txt);
        TextView time_txt = adapterHolder.getView(R.id.time_txt);
        final TextView favo_btn = adapterHolder.getView(R.id.favo_btn);
        TextView share_btn = adapterHolder.getView(R.id.share_btn);
        SelectableRoundedImageView type_icon = adapterHolder.getView(R.id.type_icon);
        ImageView content_img = adapterHolder.getView(R.id.content_img);
        final Post post = mDatas.get(position);
        if(post!=null){
            post_content_txt.setText(post.getContent());

            if(TextUtils.isEmpty(post.getCreatedAt())){
                time_txt.setText(MnDateUtil.stringByFormat(post.getCreatedAtDate(), "MM月dd日 HH:mm"));
            } else {
                time_txt.setText(MnDateUtil.stringByFormat(post.getCreatedAt(), "MM月dd日 HH:mm"));
            }

            if(isLoadImg){
                //获取帖子图片
                final BmobFile bmobFile = post.getConImg();
                if(bmobFile!=null || post.getConImgUrl()!=null){
                    content_img.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ph);
                    content_img.setLayoutParams(llp);
                    String imgUrl = null;
                    if(bmobFile != null){
                        imgUrl = bmobFile.getFileUrl();
                    } else {
                        imgUrl = post.getConImgUrl();
                    }
                    ImageLoader.getInstance().displayImage(imgUrl, content_img, ImageConfigBuilder.USER_HEAD_HD_OPTIONS);
                    //长按下载图片
                    content_img.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            String imgUrl = null;
                            if(bmobFile != null){
                                imgUrl = bmobFile.getUrl();
                            } else {
                                imgUrl = post.getConImgUrl();
                            }
                            saveImageToSDCard(imgUrl);
                            return true;
                        }
                    });
                    content_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PostCommentActivity.start(mContext, post);
                        }
                    });

                } else {
                    content_img.setVisibility(View.GONE);
                }
            } else {
                content_img.setVisibility(View.GONE);
            }

            //分类
            PostType postType = post.getPostType();
            if (postType!=null){
                post_type_txt.setText(postType.getType());
                post_type_title.setText(postType.getTitle());
                if(isLoadImg){
                    type_icon.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(postType.getIconUrl(), type_icon, ImageConfigBuilder.USER_HEAD_HD_OPTIONS);
                } else {
                    type_icon.setVisibility(View.GONE);
                }
            }

            if(post.isFavo()){
                favo_btn.setText("取消收藏");
            } else {
                favo_btn.setText("收藏");
            }

            favo_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(post.isFavo()){
                        favo_btn.setText("收藏");
                        post.setIsFavo(false);
                        deletePost(post);
                    } else {
                        favo_btn.setText("取消收藏");
                        post.setIsFavo(true);
                        favoPost(post);
                    }
                }
            });

            share_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShareUtil.shareMsg(mContext, "", "天天美文", post.getContent(), "");
                }
            });

        }
        return adapterHolder.getConvertView();
    }

    /**
     * 收藏帖子
     * */
    private void favoPost(final Post post){
        Favo favo = new Favo(user, post);
        favo.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e != null){
                    Log.i(tag, "----收藏帖子。。。失败。。。。"+e);
                } else {
                    getSaveFavo(post);
                }
            }
        });
    }

    /**
     * 取消收藏帖子
     * */
    private void deletePost(final Post post){
        final Favo favo = new Favo(user, post);
        Log.i(tag, "----post.getFavoId()="+post.getFavoId());
        favo.setObjectId(post.getFavoId());
        favo.delete(favo.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e != null){
                    Toast.makeText(mContext, "取消收藏帖子失败。。。", Toast.LENGTH_SHORT).show();
                    Log.i(tag, "----取消收藏帖子。。。失败。。。。"+e);
                } else {
                    Log.i(tag, "---取消收藏帖子。。。--成功。。。。。。");
                }
            }
        });
    }

    private void getSaveFavo(final Post post){
        BmobQuery<Favo> query = new BmobQuery<>();
        query.addWhereEqualTo("user", user);
        query.addWhereEqualTo("post", post);
        query.include("user,post,post.postType,post.user");
        query.findObjects(new FindListener<Favo>() {
            @Override
            public void done(List<Favo> list, BmobException e) {
                if (list != null && list.size() > 0) {
                    Favo favo = list.get(0);
                    Log.i(tag, "-----234----favo=" + favo.getObjectId());
                } else {
                    Log.i(tag, "----245----e="+e);
                }
            }
        });
    }

}
