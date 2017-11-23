package cc.meiwen.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cc.meiwen.R;
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
        Glide.with(getContext()).load(imgUrl).asBitmap().into(image_view);
    }

}
