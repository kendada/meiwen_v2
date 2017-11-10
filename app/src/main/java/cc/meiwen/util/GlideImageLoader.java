package cc.meiwen.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.koudai.kbase.imageselector.utils.ImageLoader;

/**
 * Created by abc on 2017/11/10.
 */

public class GlideImageLoader implements ImageLoader{

    @Override
    public void displayImage(Context context, String s, ImageView imageView) {
        Glide.with(context).load(s).asBitmap().into(imageView);
    }

    @Override
    public void clearMemoryCache() {

    }
}
