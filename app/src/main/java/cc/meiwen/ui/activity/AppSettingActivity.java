package cc.meiwen.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import cc.meiwen.R;
import cc.meiwen.constant.Constant;
import cc.meiwen.model.User;
import cc.meiwen.util.SharedPreferencesUtils;
import cc.meiwen.util.task.AsyncTask;
import cc.meiwen.util.task.ThreadPoolManager;
import cc.meiwen.util.weibo.AccessTokenKeeper;
import cc.meiwen.view.SwipeBackLayout;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.update.BmobUpdateAgent;


/**
 * APP设置界面
 * */
public class AppSettingActivity extends BaseActivity implements OnClickListener {

	private String fontSize[] = null;
	
	private RelativeLayout user_share_layout = null; //分享绑定
	private RelativeLayout push_news_layout = null; //新闻推送
	private CheckBox check_box_push_btn = null;
	private RelativeLayout delete_icon_layout = null; //清除缓存
	private RelativeLayout loaded_layout = null;//是否加载图片
	private CheckBox check_box_btn = null;
	private RelativeLayout font_size_layout = null; //字体大小
	private TextView text_font_size = null;
	private RelativeLayout about_layout = null; //关于
	private RelativeLayout refresh_layout = null; //检查更新
	private RelativeLayout opinion_layout = null; //意见反馈
	private RelativeLayout app_recommed_layout = null; //应用推荐
	
	private AlertDialog deleteDialog = null; //清除缓存对话框
	private AlertDialog fontSizeDialog = null; //选择字体大小
	
	private Editor editor;

	private SwipeBackLayout swip_back_layout;
	private ScrollView scrollView;

	private ThreadPoolManager threadPoolManager;

	private User mUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_seting);

		mUser = BmobUser.getCurrentUser(User.class);

		scrollView = (ScrollView)findViewById(R.id.scrollView);
		swip_back_layout = (SwipeBackLayout)findViewById(R.id.swip_back_layout);
		swip_back_layout.setTouchView(scrollView);
		swip_back_layout.setOnSwipeBackListener(new SwipeBackLayout.OnSwipeBackListener() {
			@Override
			public void onFinishActivity() {
				AppSettingActivity.this.finish();
			}
		});

		threadPoolManager = new ThreadPoolManager(ThreadPoolManager.TYPE_FIFO, 5);
		//初始化控件
		initViews();
		//初始化数据
		initData();
		
	}

	public void initViews() {
		user_share_layout = (RelativeLayout)findViewById(R.id.user_share_layout);
		user_share_layout.setOnClickListener(this);
		delete_icon_layout = (RelativeLayout)findViewById(R.id.delete_icon_layout);
		delete_icon_layout.setOnClickListener(this);
		loaded_layout = (RelativeLayout)findViewById(R.id.loaded_layout);
		loaded_layout.setOnClickListener(this);
		check_box_btn = (CheckBox)findViewById(R.id.check_box_btn);
		check_box_btn.setChecked(getLoadImg()); //是否加载图片
		font_size_layout = (RelativeLayout)findViewById(R.id.font_size_layout);
		font_size_layout.setOnClickListener(this);
		text_font_size = (TextView)findViewById(R.id.text_font_size);
		about_layout = (RelativeLayout)findViewById(R.id.about_layout);
		about_layout.setOnClickListener(this);
		refresh_layout = (RelativeLayout)findViewById(R.id.refresh_layout);
		refresh_layout.setOnClickListener(this);
		refresh_layout.setVisibility(View.GONE); //自动升级
		opinion_layout = (RelativeLayout)findViewById(R.id.opinion_layout);
		opinion_layout.setOnClickListener(this);
		app_recommed_layout = (RelativeLayout)findViewById(R.id.app_recommed_layout);
		app_recommed_layout.setOnClickListener(this);
		app_recommed_layout.setVisibility(View.GONE); //推荐应用
		push_news_layout = (RelativeLayout)findViewById(R.id.push_news_layout);
		push_news_layout.setOnClickListener(this);
		check_box_push_btn = (CheckBox)findViewById(R.id.check_box_push_btn);
		check_box_push_btn.setChecked(getPush()); //是否接受推送新闻
	}

	public void initData() {
		fontSize = getResources().getStringArray(R.array.setting_font_size_str);
		//创建清除缓存对话框
		deleteDialog = getDialog("提示", "缓存文件可以用来帮您节省流量，但较大时会占用较多磁盘空间。"
				+ "确定开始清理吗？", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DoDeleteCache();
			}
		});
		
		//创建字号选择对话框
		fontSizeDialog = getFontSizeDialog();
		
		//为CheckBox设置监听器:自动加载
		check_box_btn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				setIsLoadImg(isChecked);
			}
		});
		
		//为CheckBox设置监听器：新闻推送
		check_box_push_btn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				setIsPush(isChecked);
			}
		});

		user_share_layout.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				postWeibo();
				return true;
			}
		});
	}

	private void postWeibo(){
		if(mUser != null && mUser.isAdmin() && Constant.WeiBo.LOGIN_WEIBO.equals(mUser.getLoginType())){
			startActivity(new Intent(getContext(), WeiBoListActivity.class));
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.user_share_layout: // 退出登录
			logout();
			break;
		case R.id.push_news_layout: //新闻推送
			setIsPush();
			break;
		case R.id.loaded_layout:  //咨询列表自动加载
			setLoaded();
			break;
		case R.id.font_size_layout: //字体大小
			fontSizeDialog.show();
			break;
		case R.id.delete_icon_layout: //清除缓存
			deleteDialog.show();
			break;
		case R.id.about_layout: //关于
			intentToAbout();
			break;
		case R.id.refresh_layout: //检查更新
			refreshApp();
			break;
		case R.id.opinion_layout: //意见反馈
			intentToOpinion();
			break;
		case R.id.app_recommed_layout: //应用推荐
			intentToAppRecommed();
			break;
		}
	}
	
	/**
	 * 设置咨询列表自动加载
	 * */
	private void setLoaded(){
		if(check_box_btn.isChecked()){
			check_box_btn.setChecked(false);
		} else {
			check_box_btn.setChecked(true);
		}
	}
	
	/**
	 * 设置是否接受推送通知
	 * */
	private void setIsPush(){
		if(check_box_push_btn.isChecked()){
			check_box_push_btn.setChecked(false);
		} else {
			check_box_push_btn.setChecked(true);
		}
	}
	
	/**
	 * 选择字体大小对话框
	 * */
	private AlertDialog getFontSizeDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("字号选择");
		builder.setItems(R.array.setting_font_size_str, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				text_font_size.setText(fontSize[which]);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog aDialog = builder.create();
		return aDialog;
	}

	/**
	 * 退出登录
	 * */
	private void logout(){
		AlertDialog.Builder mBuilder = new AlertDialog.Builder(this)
						.setMessage("确定要退出吗？")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								userShare();
							}
						})
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {

							}
						});
		mBuilder.create().show();
	}

	/**
	 * 退出
	 * */
	private void userShare(){
		AsyncTask asyncTask = new AsyncTask() {
			@Override
			public void updata(Object obj) {
				Toast.makeText(getContext(), "已经退出", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(getContext(), LoginActivity.class));
				AppSettingActivity.this.finish();
			}

			@Override
			public Object loadData() {
				BmobUser.logOut();
				AccessTokenKeeper.clear(getContext());
				SharedPreferencesUtils.putString(Constant.ShareKey.OBJECT_ID, "");
				application.finishAllActivity(); //退出app
				clearSetting(); //清除配置信息
				return null;
			}
		};
		threadPoolManager.addAsyncTask(asyncTask);
		threadPoolManager.start();
	}
	
	/**
	 * 清除缓存
	 * */
	private void DoDeleteCache(){ //暂未处理
		Toast.makeText(this, "缓存已清除完成", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 跳转到关于界面
	 * */
	private void intentToAbout(){
		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}
	
	/**
	 * 跳转到检查更新界面
	 * */
	private void refreshApp(){
		BmobUpdateAgent.forceUpdate(getContext());
	}
	
	/**
	 * 跳转到意见反馈界面
	 * */
	private void intentToOpinion(){
		Intent intent = new Intent(this, FeedbackActivity.class);
		startActivity(intent);
	}
	
	/**
	 * 跳转到应用推荐界面
	 * */
	private void intentToAppRecommed(){

	}

	/**
	 * 设置是否加载图片
	 * */
	private void setIsLoadImg(boolean isLoadImg){
		editor = getAppSettingSharedPre().edit();
		editor.putBoolean(APP_SETTING_ISLOADIMG, isLoadImg);
		editor.commit();
	}

	/**
	 *	获取是否加载图片
	 * */
	private boolean getLoadImg(){
		return  getAppSettingSharedPre().getBoolean(APP_SETTING_ISLOADIMG, true);
	}

	/**
	 * 设置是否接受推送服务
	 * */
	private void setIsPush(boolean isPush){
		if(editor==null){
			editor = getAppSettingSharedPre().edit();
		}
		editor.putBoolean(APP_SETTING_PUSH, isPush);
		editor.commit();
	}

	/**
	 * 获取是否接受推送
	 * */
	private boolean getPush(){
		return  getAppSettingSharedPre().getBoolean(APP_SETTING_PUSH, true);
	}

}
