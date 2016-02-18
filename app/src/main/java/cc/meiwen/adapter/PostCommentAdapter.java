package cc.meiwen.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cc.meiwen.R;
import cc.meiwen.adapter.base.AdapterHolder;
import cc.meiwen.adapter.base.MnBaseAdapter;
import cc.meiwen.model.Comment;
import cc.meiwen.model.User;
import cc.meiwen.util.ImageConfigBuilder;
import cc.meiwen.util.MnDateUtil;
import cc.meiwen.view.SelectableRoundedImageView;
import cn.bmob.v3.datatype.BmobFile;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-05
 * Time: 10:56
 * Version 1.0
 */

public class PostCommentAdapter extends MnBaseAdapter<Comment>{

    private String url = "http://file.bmob.cn/";

    private String tag = PostCommentAdapter.class.getSimpleName();

    public PostCommentAdapter(Context context, List<Comment> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        AdapterHolder adapterHolder = AdapterHolder.get(i, view, viewGroup, R.layout.adapter_post_comment_layout);
        TextView post_type_txt = adapterHolder.getView(R.id.post_type_txt);
        TextView post_content_txt = adapterHolder.getView(R.id.post_content_txt);
        TextView time_txt = adapterHolder.getView(R.id.time_txt);
        SelectableRoundedImageView type_icon = adapterHolder.getView(R.id.type_icon);
        Comment comment = mDatas.get(i);
        if(comment!=null){
            post_content_txt.setText(comment.getContent());
            time_txt.setText(MnDateUtil.stringByFormat(comment.getCreatedAt(), "MM月dd日 HH:mm"));

            User user = comment.getUser();
            Log.i(tag, "---47---"+user);
            if(user!=null){
                post_type_txt.setText(user.getUsername());

                BmobFile iconBmobFile = user.getIcon();
                if(iconBmobFile!=null){
                    ImageLoader.getInstance().displayImage(url+iconBmobFile.getUrl(), type_icon, ImageConfigBuilder.USER_HEAD_HD_OPTIONS);
                }
            }

        }
        return adapterHolder.getConvertView();
    }
}
