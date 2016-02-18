package cc.meiwen.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

import cc.meiwen.R;

/**
 * User: 靳世坤(1203596603@qq.com)
 * Date: 2015-09-17
 * Time: 22:48
 * Version 1.0
 */

public class MnProgressBar extends ProgressBar {

    public MnProgressBar(Context context) {
        this(context, null);
    }

    public MnProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MnProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setInterpolatorDrawable(R.drawable.rotate_loading_github);
        Interpolator interpolator = new LinearInterpolator();
        this.setInterpolator(interpolator);
    }

    /**
     * 设置旋转图片
     * */
    public void setInterpolatorDrawable(int resId){
        this.setIndeterminateDrawable(getResources().getDrawable(resId));
    }
}
