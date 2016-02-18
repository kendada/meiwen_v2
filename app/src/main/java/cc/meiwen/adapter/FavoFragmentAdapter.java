package cc.meiwen.adapter;

import android.content.Context;
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
import cc.meiwen.util.ImageConfigBuilder;
import cc.meiwen.util.MnAppUtil;
import cc.meiwen.util.MnDateUtil;
import cc.meiwen.util.ShareUtil;
import cc.meiwen.view.SelectableRoundedImageView;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-31
 * Time: 16:40
 * Version 1.0
 */

public class FavoFragmentAdapter extends MnBaseAdapter<Favo>{

    private int ph;

    private String url = "http://file.bmob.cn/";

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
                    ShareUtil.shareMsg(mContext, "", "天天美文", post.getContent(), "");
                }
            });

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
        favo.delete(mContext, favo.getObjectId(), new DeleteListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "取消收藏", Toast.LENGTH_SHORT).show();
                Log.i(tag, "----onSuccess()----成功----");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i(tag, "----onFailure()-------"+s);
            }
        });
    }
}
