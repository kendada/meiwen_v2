package cc.meiwen.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.List;

import cc.meiwen.R;
import cc.meiwen.model.PostType;

/**
 * Date: 2017-11-03
 * Time: 10:45
 * Version 1.0
 */

public class FindSearchUserLiveLayout extends RelativeLayout {

    private Context mContext;

    private int mCount = 3;

    private int mViewWidth = 50;
    private int mViewHeight = 50;

    public FindSearchUserLiveLayout(Context context) {
        this(context, null);
    }

    public FindSearchUserLiveLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FindSearchUserLiveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FindSearchUserLiveLayout);
        mCount = ta.getInteger(R.styleable.FindSearchUserLiveLayout_user_count, 3);
        ta.recycle();

        mViewWidth = getResources().getDimensionPixelSize(R.dimen.live_find_liveing_user_icon_w_h_b_2);
        mViewHeight = mViewWidth;

        init();
    }

    private void init(){
        for(int i=0; i<mCount; i++){
            View view = LayoutInflater.from(mContext).inflate(R.layout.live_adapter_search_user_liveing_layout, null);
            LayoutParams params = new LayoutParams(mViewWidth, mViewHeight);
            params.leftMargin = mViewWidth*2*i/3;
            view.setLayoutParams(params);
            this.addView(view);
        }
    }

    /**
     * 设置数据：直播数据3.0版
     * */
    public void setData(List<PostType> mLiveList){
        if(mLiveList == null || mLiveList.size() == 0){
            this.setVisibility(View.GONE);
        } else {
            this.setVisibility(View.VISIBLE);
            int size = mLiveList.size();
            int childCount = this.getChildCount();
            if(size<mCount){
                for(int i=0; i<childCount; i++){
                    this.getChildAt(i).setVisibility(View.GONE);
                }
            }
            if(size>mCount){
                size = mCount;
            }
            for(int i=0; i<size; i++){
                View childView = this.getChildAt(i);
                ImageView user_icon = (ImageView) childView.findViewById(R.id.user_icon);
                if(childView != null){
                    childView.setVisibility(View.VISIBLE);
                    Glide.with(mContext)
                            .load(mLiveList.get(i).getIconUrl())
                            .asBitmap()
                            .into(new CirculImageViewTarget(user_icon));
                }
            }
        }
    }

}
