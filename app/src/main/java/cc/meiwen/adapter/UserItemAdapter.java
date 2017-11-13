package cc.meiwen.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cc.meiwen.R;
import cc.meiwen.model.User;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by abc on 2017/11/13.
 */

public class UserItemAdapter extends BaseQuickAdapter<User, BaseViewHolder>{


    private Context context;

    public UserItemAdapter(@Nullable List<User> data, Context context) {
        super(R.layout.adapter_user_item_layout, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, User item) {
        helper.setText(R.id.name, item.getUsername());
        if(!TextUtils.isEmpty(item.getUserInfo())){
            helper.setText(R.id.user_explain_view, item.getUserInfo());
        }
        ImageView iconView = helper.getView(R.id.user_icon);
        BmobFile bmobFile = item.getIcon();
        if(bmobFile != null){
            Glide.with(context).load(bmobFile.getFileUrl()).asBitmap().into(iconView);
        }
    }
}
