package cc.meiwen.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cc.meiwen.R;
import cc.meiwen.model.Artcile;
import cc.meiwen.util.MnAppUtil;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by abc on 2017/11/2.
 */

public class JuziListAdapter extends BaseQuickAdapter<Artcile, BaseViewHolder> {

    private String TAG = JuziListAdapter.class.getSimpleName();

    private int pw = 480;

    private Context context;

    public JuziListAdapter(@Nullable List<Artcile> data, Context context) {
        super(R.layout.adapter_most_artcile_layout, data);
        pw = MnAppUtil.getPhoneW(context)/4;
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Artcile artcile) {
        ImageView conImageView = helper.getView(R.id.thum_img);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(pw, pw);
        conImageView.setLayoutParams(rlp);
        TextView titleTextView = helper.getView(R.id.title);
        TextView contentTextView = helper.getView(R.id.content);

        if(artcile != null){
            titleTextView.setText(artcile.getTitle());
            contentTextView.setText(artcile.getAbstrac());

            BmobFile bmobFile = artcile.getThumb();
            if(bmobFile != null){
                conImageView.setVisibility(View.VISIBLE);
                Glide.with(context).load(bmobFile.getFileUrl()).asBitmap().into(conImageView);
            } else {
                conImageView.setVisibility(View.GONE);
            }
        }
    }
}
