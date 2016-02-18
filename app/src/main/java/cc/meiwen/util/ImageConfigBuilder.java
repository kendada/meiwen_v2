package cc.meiwen.util;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import cc.meiwen.R;

/**
 * ListView和GridView使用时必须设置showImageOnLoading(),
 * 否则有可能出现闪烁情况
 * */
public class ImageConfigBuilder {

	private static final int EMPTY_PHOTO = R.mipmap.pic_bg;
	private static final int EMPTY_PHOTO_WIDTH = R.mipmap.empty_photo_by_width;

	/**高清头像模式 - 不使用动画效果*/
	public static final DisplayImageOptions USER_HEAD_HD_OPTIONS = new DisplayImageOptions.Builder()
	.showImageOnLoading(EMPTY_PHOTO)
	.showImageForEmptyUri(EMPTY_PHOTO)
	.showImageOnFail(EMPTY_PHOTO)
	.cacheInMemory(true)
	.cacheOnDisk(true)
	.bitmapConfig(Bitmap.Config.ARGB_8888)
	.build();
	
	
	/**高清头像模式 - 不使用动画效果*/
	public static final DisplayImageOptions NORMAL_IMAGE = new DisplayImageOptions.Builder()
	.showImageOnLoading(EMPTY_PHOTO_WIDTH)
	.showImageForEmptyUri(EMPTY_PHOTO_WIDTH)
	.showImageOnFail(EMPTY_PHOTO_WIDTH)
	.cacheInMemory(true)
	.cacheOnDisk(true)
	.displayer(new RoundedBitmapDisplayer(10))  //圆角
	.bitmapConfig(Bitmap.Config.ARGB_8888)
	.build();
	
	
	/**不显示默认图片模式*/
	public static final DisplayImageOptions TRANSPARENT_IMAGE = new DisplayImageOptions.Builder()
	.cacheInMemory(true)
	.cacheOnDisk(true)
	.imageScaleType(ImageScaleType.EXACTLY_STRETCHED) //设置之后ImageSize才起作用
	.bitmapConfig(Bitmap.Config.RGB_565)
	.build();
}
