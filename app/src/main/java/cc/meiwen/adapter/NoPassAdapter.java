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
import cc.meiwen.model.Post;
import cc.meiwen.model.PostType;
import cc.meiwen.model.User;
import cc.meiwen.util.ImageConfigBuilder;
import cc.meiwen.util.MnAppUtil;
import cc.meiwen.util.MnDateUtil;
import cc.meiwen.view.SelectableRoundedImageView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-31
 * Time: 16:40
 * Version 1.0
 */

public class NoPassAdapter extends MnBaseAdapter<Post>{

    private User user = null;

    private int ph;

    private String tag = NoPassAdapter.class.getSimpleName();

    public NoPassAdapter(Context context, List<Post> datas) {
        super(context, datas);
        user = BmobUser.getCurrentUser(User.class);
        ph = MnAppUtil.getPhoneH(mContext)/3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        AdapterHolder adapterHolder = AdapterHolder.get(position, convertView, viewGroup, R.layout.adapter_user_info_post_layout);
        TextView post_type_txt = adapterHolder.getView(R.id.post_type_txt);
        TextView post_content_txt = adapterHolder.getView(R.id.post_content_txt);
        TextView time_txt = adapterHolder.getView(R.id.time_txt);
        TextView favo_btn = adapterHolder.getView(R.id.favo_btn);
        favo_btn.setVisibility(View.GONE);
        TextView share_btn = adapterHolder.getView(R.id.share_btn);
        share_btn.setVisibility(View.GONE);
        SelectableRoundedImageView type_icon = adapterHolder.getView(R.id.type_icon);
        ImageView content_img = adapterHolder.getView(R.id.content_img);
        final Post post = mDatas.get(position);
        if(post!=null){
            post_content_txt.setText(post.getContent());

            time_txt.setText(MnDateUtil.stringByFormat(post.getCreatedAt(), "MM月dd日 HH:mm"));

            if(isLoadImg){
                //获取帖子图片
                BmobFile bmobFile = post.getConImg();
                if(bmobFile!=null){
                    content_img.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ph);
                    content_img.setLayoutParams(llp);
                    ImageLoader.getInstance().displayImage(bmobFile.getFileUrl(), content_img, ImageConfigBuilder.USER_HEAD_HD_OPTIONS);
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
        }
        return adapterHolder.getConvertView();
    }

}
