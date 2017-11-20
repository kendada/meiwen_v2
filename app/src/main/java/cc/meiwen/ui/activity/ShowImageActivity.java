package cc.meiwen.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import cc.meiwen.R;
import cc.meiwen.util.ImageConfigBuilder;
import cc.meiwen.util.MnAppUtil;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-02
 * Time: 10:47
 * Version 1.0
 */

public class ShowImageActivity extends BaseActivity {

    public static void start(Context context, String imgUrl){
        Intent intent = new Intent(context, ShowImageActivity.class);
        intent.putExtra("imgUrl", imgUrl);
        context.startActivity(intent);
    }

    private ImageView image_view;

    private String imgUrl;

    private int screenW = 720; //默认分辨率

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image_layout);
        getIntentData();

        screenW = MnAppUtil.getPhoneW(getContext());

        image_view = (ImageView)findViewById(R.id.image_view);

        setImageToBitmap();
    }

    @Override
    protected boolean isSystemBar() {
        return false;
    }

    private void getIntentData(){
        Intent intent = getIntent();
        imgUrl = intent.getStringExtra("imgUrl");
    }

    private void setImageToBitmap(){
        Bitmap bitmap = ImageLoader.getInstance().loadImageSync(imgUrl);
        int w = 1, h = 1;
        if(bitmap!=null){
            w = bitmap.getWidth();
            h = bitmap.getHeight();
            bitmap.recycle();
            bitmap = null;
        }
        int newH = screenW*h/w;
        ImageSize imageSize = new ImageSize(screenW, newH);
        ImageLoader.getInstance().loadImage(imgUrl, imageSize, ImageConfigBuilder.TRANSPARENT_IMAGE, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                image_view.setImageBitmap(bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }

}
