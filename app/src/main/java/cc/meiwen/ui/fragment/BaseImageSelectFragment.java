package cc.meiwen.ui.fragment;

import android.app.Activity;
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
 * Created by abc on 2017/11/11.
 */

public abstract class BaseImageSelectFragment extends BaseFragment{

    private String mPicPath;

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
        MultiImageSelector selector = MultiImageSelector.create(getActivity());
        selector.showCamera(false);
        selector.single();
        selector.startImage(this, CommonConstants.RequestCode.PHOTO_ALBUM, false);
    }

    /**
     * 打开相册选取图片
     */
    protected void openImageAlbum(boolean crop) {
        MultiImageSelector selector = MultiImageSelector.create(getActivity());
        selector.showCamera(false);
        selector.single();
        selector.startImage(this, CommonConstants.RequestCode.PHOTO_ALBUM, crop);
    }

    protected void cropImage(String imagePath) {
        if(!TextUtils.isEmpty(imagePath)){
            File file = new File(imagePath);
            if(file.exists() && file.exists()){
                destination = new File(FileUtils.getCachePictureDirectory(), "crop_" + System.currentTimeMillis() + ".jpg");
                Uri from = Uri.fromFile(new File(imagePath));
                Uri dest = Uri.fromFile(destination);

                try {
                    Intent intent = FileUploadUtils.createCropImageIntent(from, dest,  getCropWidth(), getScreenHeight());
                    startActivityForResult(intent, CommonConstants.RequestCode.CROP_PHOTO);
                } catch (Exception e){}
            } else {
                onImageSelected(null);
            }
        } else {
            onImageSelected(null);
        }
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

    public abstract void onImageSelected(String path);

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CommonConstants.RequestCode.CAMERA:
                if (mCrop) {
                    cropImage(mPicPath);
                } else {
                    onImageSelected(mPicPath);
                }
                break;
            case CommonConstants.RequestCode.PHOTO_ALBUM:
                if (resultCode == Activity.RESULT_OK) {
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
                if (resultCode == Activity.RESULT_OK) {
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
