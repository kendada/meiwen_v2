package cc.meiwen.view;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.request.target.BitmapImageViewTarget;

/**
 * Created by abc on 2017/11/3.
 */

public class CirculImageViewTarget extends BitmapImageViewTarget {

    public CirculImageViewTarget(ImageView view) {
        super(view);
    }

    @Override
    protected void setResource(Bitmap resource) {
        RoundedBitmapDrawable circularBitmapDrawable =
                RoundedBitmapDrawableFactory.create(view.getResources(), resource);
        circularBitmapDrawable.setCircular(true);
        view.setImageDrawable(circularBitmapDrawable);
    }
}
