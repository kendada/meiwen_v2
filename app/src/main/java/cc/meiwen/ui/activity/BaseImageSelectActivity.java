package cc.meiwen.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.koudai.kbase.imageselector.MultiImageSelector;

import java.io.File;
import java.util.ArrayList;

import cc.meiwen.util.CommonConstants;
import cc.meiwen.util.FileUploadUtils;
import cc.meiwen.util.FileUtils;

/**
 * Created by abc on 2017/11/10.
 */

public abstract class BaseImageSelectActivity extends BaseActivity{

    private String mPicPath;

    private File saveDirectory = FileUtils.getCachePictureDirectory();
    private File destination;
    private boolean mCrop;

    /**
     * 打开摄像头拍摄图片
     */
    protected void openCamera() {
        mCrop = false;
        mPicPath = FileUtils.createCacheImagePath();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mPicPath)));
        startActivityForResult(intent, CommonConstants.RequestCode.CAMERA);
    }

    /**
     * 打开摄像头拍摄图片
     */
    protected void openCamera(boolean crop) {
        mCrop = crop;
        mPicPath = FileUtils.createCacheImagePath();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mPicPath)));
        startActivityForResult(intent, CommonConstants.RequestCode.CAMERA);
    }

    /**
     * 打开相册选取图片
     */
    protected void openImageAlbum() {
        MultiImageSelector selector = MultiImageSelector.create(this);
        selector.showCamera(false);
        selector.single();
        selector.startImage(this, CommonConstants.RequestCode.PHOTO_ALBUM, false);
    }

    /**
     * 打开相册选取图片
     */
    protected void openImageAlbum(boolean crop) {
        MultiImageSelector selector = MultiImageSelector.create(this);
        selector.showCamera(false);
        selector.single();
        selector.startImage(this, CommonConstants.RequestCode.PHOTO_ALBUM, crop);
    }

    protected void cropImage(String imagePath) {
        destination = new File(FileUtils.getCachePictureDirectory(), "crop_" + System.currentTimeMillis() + ".jpg");
        Uri from = Uri.fromFile(new File(imagePath));
        Uri dest = Uri.fromFile(destination);

        try {
            Intent intent = FileUploadUtils.createCropImageIntent(from, dest, getCropWidth(), getCropHeight());
            startActivityForResult(intent, CommonConstants.RequestCode.CROP_PHOTO);
        } catch (Exception e){}
    }

    public int getScreenHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    public int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    public int getCropWidth(){
        return getScreenWidth();
    }

    public int getCropHeight(){
        return getScreenHeight();
    }

    protected void onImageSelected(String path) {
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Glide.get(this).clearMemory();
        switch (requestCode) {
            case CommonConstants.RequestCode.CAMERA:
//                String picPath = new File(saveDirectory, mPicPath + ".jpg").getVideoPath();
                if (mCrop) {
                    cropImage(mPicPath);
                } else {
                    onImageSelected(mPicPath);
                }
                break;
            case CommonConstants.RequestCode.PHOTO_ALBUM:
                if (resultCode == RESULT_OK) {
                    if (data == null) {
                        return;
                    }
                    ArrayList<String> paths = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    String path = paths.get(0);
                    if (!TextUtils.isEmpty(path)) {
                        onImageSelected(path);
                    } else {
                        onImageSelected(path);
                    }
                }
                break;
            case CommonConstants.RequestCode.CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (destination != null) {
                        onImageSelected(destination.getAbsolutePath());
                    } else {
                        onImageSelected(null);
                    }
                } else {
                    onImageSelected(null);
                }
                break;
        }
    }

}
