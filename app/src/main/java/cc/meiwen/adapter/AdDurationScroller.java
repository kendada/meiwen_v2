package cc.meiwen.adapter;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Date: 2016-05-09
 * Time: 12:04
 * Version 1.0
 */

public class AdDurationScroller extends Scroller {

    private double scrollFactor = 1;

    public AdDurationScroller(Context context) {
        super(context);
    }

    public AdDurationScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    /**
     * not exist in android 2.3
     *
     * @param context
     * @param interpolator
     * @param flywheel
     */
    // @SuppressLint("NewApi")
    // public CustomDurationScroller(Context context, Interpolator interpolator, boolean flywheel){
    // super(context, interpolator, flywheel);
    // }

    /**
     * Set the factor by which the duration will change
     */
    public void setScrollDurationFactor(double scrollFactor) {
        this.scrollFactor = scrollFactor;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, (int)(duration * scrollFactor));
    }

}
