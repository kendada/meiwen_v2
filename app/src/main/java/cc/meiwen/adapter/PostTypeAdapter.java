package cc.meiwen.adapter;

import android.content.Context;
import android.text.TextUtils;
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
import cc.meiwen.model.PostType;
import cc.meiwen.util.ImageConfigBuilder;
import cc.meiwen.util.MnAppUtil;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-03
 * Time: 16:35
 * Version 1.0
 */

public class PostTypeAdapter extends MnBaseAdapter<PostType>{

    private int pw;

    public PostTypeAdapter(Context context, List<PostType> datas) {
        super(context, datas);

        pw = MnAppUtil.getPhoneW(mContext)/2;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        AdapterHolder adapterHolder = AdapterHolder.get(i, view, viewGroup, R.layout.adapter_post_type_layout);

        ImageView imageView = adapterHolder.getView(R.id.type_bg);
        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(pw, pw);
        imageView.setLayoutParams(rlp);

        TextView type_title = adapterHolder.getView(R.id.type_title);

        PostType pt = mDatas.get(i);
        if(pt!=null){
            type_title.setText(pt.getType());
            if(isLoadImg){
                imageView.setVisibility(View.VISIBLE);
                String iconUrl = pt.getIconUrl(); //分类图片地址
                if(!TextUtils.isEmpty(iconUrl)){
                    ImageLoader.getInstance().displayImage(iconUrl, imageView, ImageConfigBuilder.USER_HEAD_HD_OPTIONS);
                }
            } else {
                imageView.setVisibility(View.GONE);
            }
        }

        return adapterHolder.getConvertView();
    }
}
