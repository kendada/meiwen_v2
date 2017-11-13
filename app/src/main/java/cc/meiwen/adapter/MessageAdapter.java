package cc.meiwen.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cc.meiwen.R;
import cc.meiwen.model.Comment;
import cc.meiwen.model.User;
import cc.meiwen.util.MnDateUtil;
import cc.meiwen.view.SelectableRoundedImageView;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by abc on 2017/11/13.
 */

public class MessageAdapter extends BaseQuickAdapter<Comment, BaseViewHolder>{

    private Context context;

    public MessageAdapter(@Nullable List<Comment> data, Context context) {
        super(R.layout.adapter_post_comment_layout, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Comment comment) {
        TextView post_type_txt = helper.getView(R.id.post_type_txt);
        TextView post_content_txt = helper.getView(R.id.post_content_txt);
        TextView time_txt = helper.getView(R.id.time_txt);
        SelectableRoundedImageView user_icon = helper.getView(R.id.user_icon);
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
    }
}
