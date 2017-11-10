package cc.meiwen.util;

/**
 * Created by abc on 2017/11/10.
 */

public interface CommonConstants {

    interface RequestCode { //activity请求码
        //相机
        int CAMERA = 1;
        //相册
        int PHOTO_ALBUM = 2;
        //剪裁图片
        int CROP_PHOTO = 3;

        String FILE_CACHE_START_NAME = "CACHE_";

        String IMAGE_EXTENSION = ".jpg";
    }
}
