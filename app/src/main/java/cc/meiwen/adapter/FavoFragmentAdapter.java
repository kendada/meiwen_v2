package cc.meiwen.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import cc.meiwen.R;
import cc.meiwen.adapter.base.AdapterHolder;
import cc.meiwen.adapter.base.MnFavoAdapter;
import cc.meiwen.model.Favo;
import cc.meiwen.model.Post;
import cc.meiwen.model.PostType;
import cc.meiwen.ui.activity.PostCommentActivity;
import cc.meiwen.util.ImageConfigBuilder;
import cc.meiwen.util.MnAppUtil;
import cc.meiwen.util.MnDateUtil;
import cc.meiwen.view.SelectableRoundedImageView;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-31
 * Time: 16:40
 * Version 1.0
 */

public class FavoFragmentAdapter extends MnFavoAdapter{

    private int ph;

    private String tag = FavoFragmentAdapter.class.getSimpleName();

    public FavoFragmentAdapter(Context context, List<Favo> datas) {
        super(context, datas);
        ph = MnAppUtil.getPhoneH(mContext)/3;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        AdapterHolder adapterHolder = AdapterHolder.get(position, convertView, viewGroup, R.layout.adapter_favo_fragment_layout);
        TextView post_type_txt = adapterHolder.getView(R.id.post_type_txt);
        TextView post_content_txt = adapterHolder.getView(R.id.post_content_txt);
        TextView time_txt = adapterHolder.getView(R.id.time_txt);
        TextView favo_btn = adapterHolder.getView(R.id.favo_btn);
        TextView share_btn = adapterHolder.getView(R.id.share_btn);
        SelectableRoundedImageView type_icon = adapterHolder.getView(R.id.type_icon);
        ImageView content_img = adapterHolder.getView(R.id.content_img);
        final Favo favo = mDatas.get(position);
        final Post post = favo.getPost();
        if(post!=null){
            post_content_txt.setText(post.getContent());

            time_txt.setText(MnDateUtil.stringByFormat(post.getCreatedAt(), "MM月dd日 HH:mm"));

            share_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showShare(favo);
                }
            });

            if(isLoadImg){
                //获取帖子图片
                final BmobFile bmobFile = post.getConImg();
                if(bmobFile!=null){
                    content_img.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ph);
                    content_img.setLayoutParams(llp);
                    Glide.with(mContext).load(bmobFile.getFileUrl()).asBitmap().into(content_img);
                    //长按下载图片
                    content_img.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            saveImageToSDCard(bmobFile.getFileUrl());
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
                if(isLoadImg){
                    type_icon.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(postType.getIconUrl()).asBitmap().into(type_icon);
                } else {
                    type_icon.setVisibility(View.GONE);
                }
            }

            favo_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteFavo(favo);
                }
            });

        }
        return adapterHolder.getConvertView();
    }

    /**
     * 取消收藏
     * */
    private void deleteFavo(Favo favo){
        mDatas.remove(favo);
        refreshData();
        favo.delete(favo.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e != null){
                    Log.i(tag, "----done()-------"+e);
                } else {
                    Toast.makeText(mContext, "取消收藏", Toast.LENGTH_SHORT).show();
                    Log.i(tag, "----done()----成功----");
                }
            }
        });
    }
}
