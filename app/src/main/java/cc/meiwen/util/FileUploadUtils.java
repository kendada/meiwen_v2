package cc.meiwen.util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Date: 2017-01-07
 * Time: 16:11
 * Version 1.0
 */

public class FileUploadUtils {
    /**
     * 获取剪裁图片的意图
     */
    public static Intent createCropImageIntent(Uri src, Uri dest, int w, int h) {
        Intent intent = null;
        try {
            intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(src, "image/*");
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop", "true");

            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);

            // outputX,outputY 是剪裁图片的宽高
            intent.putExtra("outputX", w);
            intent.putExtra("outputY", h);
            intent.putExtra("return-data", false);
            //保存图片
            intent.putExtra(MediaStore.EXTRA_OUTPUT, dest);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            //去黑边
            intent.putExtra("noFaceDetection", true);
            intent.putExtra("scale", true);// 去黑边
            intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        } catch (Exception e){
            e.printStackTrace();
        }
        return intent;
    }



}
