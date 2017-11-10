package cc.meiwen.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cc.meiwen.R;
import cc.meiwen.adapter.base.AdapterHolder;
import cc.meiwen.adapter.base.MnBaseAdapter;
import cc.meiwen.model.Favo;
import cc.meiwen.model.Post;
import cc.meiwen.model.PostType;
import cc.meiwen.model.User;
import cc.meiwen.util.ImageConfigBuilder;
import cc.meiwen.util.MnAppUtil;
import cc.meiwen.util.MnDateUtil;
import cc.meiwen.view.SelectableRoundedImageView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-31
 * Time: 16:40
 * Version 1.0
 */

public class MeFragmentAdapter extends MnBaseAdapter<Post>{

    private User user = null;

    private int ph;

    private String url = "http://file.bmob.cn/";

    private String tag = MeFragmentAdapter.class.getSimpleName();

    public MeFragmentAdapter(Context context, List<Post> datas) {
        super(context, datas);
        user = BmobUser.getCurrentUser(context, User.class);
        ph = MnAppUtil.getPhoneH(mContext)/3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        AdapterHolder adapterHolder = AdapterHolder.get(position, convertView, viewGroup, R.layout.adapter_me_layout);
        TextView post_type_txt = adapterHolder.getView(R.id.post_type_txt);
        TextView post_content_txt = adapterHolder.getView(R.id.post_content_txt);
        TextView time_txt = adapterHolder.getView(R.id.time_txt);
        TextView is_show_txt = adapterHolder.getView(R.id.is_show_txt);
        TextView share_btn = adapterHolder.getView(R.id.share_btn);
        final TextView favo_btn = adapterHolder.getView(R.id.favo_btn);
        SelectableRoundedImageView type_icon = adapterHolder.getView(R.id.type_icon);
        ImageView content_img = adapterHolder.getView(R.id.content_img);
        final Post post = mDatas.get(position);
        if(post!=null){

            if(post.isShow()){
                is_show_txt.setTextColor(mContext.getResources().getColor(R.color.isShow));
                is_show_txt.setText("已通过审核");
                favo_btn.setVisibility(View.VISIBLE);
                share_btn.setVisibility(View.VISIBLE);
            } else {
                is_show_txt.setTextColor(mContext.getResources().getColor(R.color.isNotShow));
                is_show_txt.setText("正在审核");
                favo_btn.setVisibility(View.GONE);
                share_btn.setVisibility(View.GONE);
            }

            post_content_txt.setText(post.getContent());

            time_txt.setText(MnDateUtil.stringByFormat(post.getCreatedAt(), "MM月dd日 HH:mm"));

            if(isLoadImg){
                //获取帖子图片
                final BmobFile bmobFile = post.getConImg();
                if(bmobFile!=null){
                    content_img.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ph);
                    content_img.setLayoutParams(llp);
                    ImageLoader.getInstance().displayImage(url + bmobFile.getUrl(), content_img, ImageConfigBuilder.USER_HEAD_HD_OPTIONS);
                    //长按下载图片
                    content_img.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            saveImageToSDCard(url + bmobFile.getUrl());
                            return true;
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

        }
        return adapterHolder.getConvertView();
    }

    /**
     * 收藏帖子
     * */
    private void favoPost(final Post post){
        Favo favo = new Favo(user, post);
        favo.save(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                getSaveFavo(post);
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    /**
     * 取消收藏帖子
     * */
    private void deletePost(Post post){
        final Favo favo = new Favo(user, post);
        favo.setObjectId(post.getFavoId());
        favo.delete(mContext, favo.getObjectId(), new DeleteListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    private void getSaveFavo(Post post){
        BmobQuery<Favo> query = new BmobQuery<>();
        query.addWhereEqualTo("user", user);
        query.addWhereEqualTo("post", post);
        query.include("user,post,post.postType,post.user");
        query.findObjects(mContext, new FindListener<Favo>() {
            @Override
            public void onSuccess(List<Favo> list) {
                if(list!=null && list.size()>0){
                    Favo favo = list.get(0);
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

}
