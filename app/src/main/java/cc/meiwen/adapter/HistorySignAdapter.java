package cc.meiwen.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cc.meiwen.R;
import cc.meiwen.model.calendarSign;
import cc.meiwen.util.MnDateUtil;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by abc on 2017/11/17.
 */

public class HistorySignAdapter extends BaseQuickAdapter<calendarSign, BaseViewHolder>{

    private Context context;


    public HistorySignAdapter(@Nullable List<calendarSign> data, Context context) {
        super(R.layout.adapter_history_sign_layout, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, calendarSign item) {
        helper.setText(R.id.text_view, MnDateUtil.stringByFormat(item.getCreatedAt(), MnDateUtil.dateFormatYMD));
        helper.setText(R.id.title_view, item.getTitle());
        helper.setText(R.id.message_view, item.getMessage());
        helper.setText(R.id.content_view, item.getContent());
        ImageView imageView = helper.getView(R.id.image_view);
        BmobFile bmobFile = item.getContentImage();
        if(bmobFile != null)
        Glide.with(context).load(bmobFile.getFileUrl()).asBitmap().into(imageView);
    }
}
