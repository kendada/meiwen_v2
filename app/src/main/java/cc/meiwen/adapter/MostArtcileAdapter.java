package cc.meiwen.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cc.meiwen.R;
import cc.meiwen.adapter.base.AdapterHolder;
import cc.meiwen.adapter.base.MnBaseAdapter;
import cc.meiwen.model.Artcile;
import cc.meiwen.util.ImageConfigBuilder;
import cc.meiwen.util.MnAppUtil;
import cn.bmob.v3.datatype.BmobFile;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-18
 * Time: 10:53
 * Version 1.0
 */

public class MostArtcileAdapter extends MnBaseAdapter<Artcile>{

    private int pw = 480;

    public MostArtcileAdapter(Context context, List<Artcile> datas) {
        super(context, datas);

        pw = MnAppUtil.getPhoneW(context)/4;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterHolder adapterHolder = AdapterHolder.get(position, convertView, parent, R.layout.adapter_most_artcile_layout);

        ImageView conImageView = adapterHolder.getView(R.id.thum_img);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(pw, pw);
        conImageView.setLayoutParams(rlp);
        TextView titleTextView = adapterHolder.getView(R.id.title);
        TextView contentTextView = adapterHolder.getView(R.id.content);

        Artcile artcile = mDatas.get(position);
        if(artcile != null){
            titleTextView.setText(artcile.getTitle());
            contentTextView.setText(artcile.getAbstrac());

            BmobFile bmobFile = artcile.getThumb();
            if(bmobFile != null){
                conImageView.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(bmobFile.getFileUrl()).asBitmap().into(conImageView);
            } else {
                conImageView.setVisibility(View.GONE);
            }
        }

        return adapterHolder.getConvertView();
    }
}
