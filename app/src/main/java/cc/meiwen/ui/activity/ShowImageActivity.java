package cc.meiwen.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.IOException;
import java.util.Date;

import cc.meiwen.R;
import cc.meiwen.util.FileUtils;
import cc.meiwen.util.MnDateUtil;

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

    private String TAG = ShowImageActivity.class.getSimpleName();

    private ImageView image_view;
    private Bitmap mResource;

    private String imgUrl;

    private FileUtils fileUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image_layout);
        getIntentData();

        fileUtils = new FileUtils(getContext());

        image_view = (ImageView)findViewById(R.id.image_view);

        setImageToBitmap();

        image_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                saveImageToSDCard();
                return true;
            }
        });
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
        Glide.with(getContext()).load(imgUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                mResource = resource;
                if(resource != null){
                    image_view.setImageBitmap(resource);
                    Log.d(TAG,"w = " + resource.getWidth() + ", h = " + resource.getHeight());
                }
            }
        });
    }

    /**
     * 保存图片到SD卡
     * */
    public void saveImageToSDCard(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("操作提示");
        builder.setMessage("你确定下载这张图片吗？\r\n" +
                "保存路径：SD卡下Android/data/cc.meiwen/image目录下");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveBitmap(mResource);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    public void saveBitmap(Bitmap bitmap){
        try {
            fileUtils.saveBitmap(MnDateUtil.stringByFormat(new Date(),
                    MnDateUtil.dateFormatFileName)+".jpg", bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getContext(), "图片已保存在SD卡下Android/data/cc.meiwen/image目录下", Toast.LENGTH_SHORT).show();
    }

}
