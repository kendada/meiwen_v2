package cc.meiwen.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import cc.meiwen.model.RecommendPost;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Date: 2017-11-02
 * Time: 10:29
 * Version 1.0
 */

public class FindAdAdapter extends PagerAdapter {

    private Context mContext;

    private List<RecommendPost> mAdList;

    public FindAdAdapter(Context context, List<RecommendPost> adList){
        mContext = context;
        mAdList = adList;
    }

    @Override
    public int getCount() {
     //   return mAdList != null ? mAdList.size() : 0;
        return Integer.MAX_VALUE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == (View)obj;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewGroup.LayoutParams adParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        ImageView view = new ImageView(mContext);
        view.setAdjustViewBounds(true);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(adParams);

        final RecommendPost item = mAdList.get(position % mAdList.size());
        BmobFile imgFile = item.imgFile;
        if(imgFile != null){
            Glide.with(mContext)
                    .load(item.imgFile.getFileUrl(mContext))
                    .into(view);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        container.addView(view, 0);
        return view;
    }
}
