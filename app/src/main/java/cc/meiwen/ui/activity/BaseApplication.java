package cc.meiwen.ui.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.koudai.kbase.imageselector.ImagePicker;
import com.mob.MobSDK;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cc.meiwen.BuildConfig;
import cc.meiwen.util.AppUtils;
import cc.meiwen.util.GlideImageLoader;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;

public class BaseApplication extends Application {

	private String TAG = BaseApplication.class.getSimpleName();

	public static BaseApplication instance;

	public List<Activity> activities; //安全退出Finish

	@Override
	public void onCreate() {
		super.onCreate();
		activities = new ArrayList<>();
		instance = this;
		initImageLoader(getApplicationContext());

		AppUtils.application = this;

		MobSDK.init(this, "227cfb2575d2b", "bdf016ba828f60d4ebec51b46960cfae");

		//初始化Bmob
		Bmob.initialize(this, "aee6e001a4a2cdd86a45363b771755e8");
		// 使用推送服务时的初始化操作
		BmobInstallationManager.getInstance().initialize(new InstallationListener<BmobInstallation>() {
			@Override
			public void done(BmobInstallation bmobInstallation, BmobException e) {
				if (e == null) {
					Log.i(TAG,bmobInstallation.getObjectId() + "-" + bmobInstallation.getInstallationId());
				} else {
					Log.e(TAG, e.getMessage());
				}
			}
		});
		BmobPush.setDebugMode(BuildConfig.DEBUG);
		// 启动推送服务
		BmobPush.startWork(this);


		ImagePicker.getInstance().setImageLoader(new GlideImageLoader());
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
		for(Activity activity:activities){
			if(activity!=null){
				activity.finish();
			}
		}
	}

}
