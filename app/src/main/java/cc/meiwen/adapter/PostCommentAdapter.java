package cc.meiwen.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cc.meiwen.R;
import cc.meiwen.adapter.base.AdapterHolder;
import cc.meiwen.adapter.base.MnBaseAdapter;
import cc.meiwen.model.Comment;
import cc.meiwen.model.User;
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
        SelectableRoundedImageView user_icon = adapterHolder.getView(R.id.user_icon);
        Comment comment = mDatas.get(i);
        if(comment!=null){
            post_content_txt.setText(comment.getContent());
            time_txt.setText(MnDateUtil.stringByFormat(comment.getCreatedAt(), "MM月dd日 HH:mm"));

            User user = comment.getUser();
            if(user!=null){
                post_type_txt.setText(user.getUsername());

                BmobFile iconBmobFile = user.getIcon();
                if(iconBmobFile!=null){
                    Glide.with(mContext).load(iconBmobFile.getFileUrl()).asBitmap().into(user_icon);
                }
            }

        }
        return adapterHolder.getConvertView();
    }
}
