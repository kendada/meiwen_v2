package cc.meiwen.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Date: 2015-10-22
 * Time: 11:49
 * Version 1.0
 */

public class FileUtils {

    public static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();

    private static String mDataRootPath = null;

    private final static String APP_PACKAGE_FOLDER = "/Android/data";

    private final static String F_PICTURE = getSDCardPath() + "/jz/cache/picture";//下载目录
    private final static String SDCARD_PATH = getSDCardPath();

    private final static String FOLDER_NAME = "/image";

    private String appPackage = null;

    private Context mContext;

    private String tag = FileUtils.class.getSimpleName();

    public FileUtils(Context context){
        appPackage = context.getPackageName();
        mDataRootPath = context.getCacheDir().getPath();
        mContext = context;
        Log.i(tag, "----38-appPackage:"+appPackage);
        Log.i(tag, "----39-mDataRootPath:"+mDataRootPath);
    }

    public static File getCachePictureDirectory() {
        return getFile(F_PICTURE);
    }

    public static String createCacheImagePath() {
        long dateTaken = System.currentTimeMillis();
        String title = CommonConstants.RequestCode.FILE_CACHE_START_NAME + dateTaken;
        String filename = title + CommonConstants.RequestCode.IMAGE_EXTENSION;
        return getCachePictureDirectory().getAbsolutePath() + "/" + filename;
    }

    public static File getFile(String path) {
        if (null == path) {
            return null;
        }

        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        return f;

    }

    public static String getSDCardPath() {
        String sdCardPathString = "";
        if (checkSDCard()) {
            sdCardPathString = Environment.getExternalStorageDirectory()
                    .getPath();
        } else {
            sdCardPathString = Environment.getExternalStorageDirectory()
                    .getParentFile().getPath();
        }

        return sdCardPathString;
    }

    /**
     * 检查sdcard是否存在
     *
     * @return
     */
    public static boolean checkSDCard() {
        return TextUtils.equals(android.os.Environment.MEDIA_MOUNTED, android.os.Environment.getExternalStorageState());
    }

    /**
     * 获取图片保存路径：优先保存在SD卡
     * */
    public String getStorageDirectory(){
        String path = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
                mSdRootPath + APP_PACKAGE_FOLDER + File.separator + appPackage + FOLDER_NAME : mDataRootPath + FOLDER_NAME;
        File folderFile = new File(path);
        if(!folderFile.exists()){ //目录不存在时，进行创建
            folderFile.mkdirs();
        }
        return path;
    }

    /**
     * 保存图片到SD卡，或者手机目录
     * */
    public void saveBitmap(String fileName, Bitmap bitmap) throws IOException {
        if(bitmap == null){
            return;
        }
        String path = getStorageDirectory();
        File folderFile = new File(path);
        if(!folderFile.exists()){ //目录不存在时，进行创建
            folderFile.mkdirs();
        }

        File file = new File(path + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        if(fileName.indexOf("jpg")!=-1|| fileName.indexOf("jpeg")!=-1){
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } else if(fileName.indexOf("png")!=-1){
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } else {
            bitmap.compress(Bitmap.CompressFormat.WEBP, 100, fos);
        }


        fos.flush();
        fos.close();
    }

    /**
     * 从SD卡或者手机目录获取图片
     * */
    public Bitmap getBitmap(String fileName){
        return BitmapFactory.decodeFile(getStorageDirectory() + File.separator + fileName);
    }

    /**
     * 判断文件是否存在
     * */
    public boolean isFileExists(String fileName){
        return new File(getStorageDirectory() + File.separator + fileName).exists();
    }

    /**
     * 获取文件的大小
     * */
    public long getFileSize(String fileName){
        return new File(getStorageDirectory() + File.separator + fileName).length();
    }

    /**
     * 删除图片
     * */
    public void deleteFile(){
        String path = getStorageDirectory();
        File dirFile = new File(path);
        if(!dirFile.exists()){
            return;
        }
        if(dirFile.isDirectory()){
            String[] children = dirFile.list();
            for(String p:children){
                new File(path+ File.separator+p).delete();
            }
        }
        dirFile.delete();
    }

}
