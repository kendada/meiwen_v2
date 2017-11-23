package cc.meiwen.util;

import cc.meiwen.R;

/**
 * ListView和GridView使用时必须设置showImageOnLoading(),
 * 否则有可能出现闪烁情况
 * */
public class ImageConfigBuilder {

	private static final int EMPTY_PHOTO = R.mipmap.pic_bg;
	private static final int EMPTY_PHOTO_WIDTH = R.mipmap.empty_photo_by_width;
}
