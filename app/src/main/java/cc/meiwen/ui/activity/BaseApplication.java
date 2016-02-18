package cc.meiwen.ui.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cc.emm.AppConnect;


public class BaseApplication extends Application {

	public static BaseApplication instance;

	public List<Activity> activities; //安全退出Finish

	@Override
	public void onCreate() {
		super.onCreate();
		activities = new ArrayList<>();
		instance = this;
		initImageLoader(getApplicationContext());
	}

	public static void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "Android/data/"+context.getPackageName()+"/cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024)
				// 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.diskCache(new UnlimitedDiskCache(cacheDir)) //自定义缓存路径
				.writeDebugLogs() // Remove for release app
				.build();
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 添加activity
	 * */
	public void addActivity(Activity activity){
		if(activities==null){
			activities = new ArrayList<>();
		}
		activities.add(activity);
	}

	public List<Activity> getActivities(){
		return activities;
	}

	/**
	 * finish所有的Activity
	 * */
	public void finishAllActivity(){
		AppConnect.getInstance(this).close();
		for(Activity activity:activities){
			if(activity!=null){
				activity.finish();
			}
		}
	}

}
