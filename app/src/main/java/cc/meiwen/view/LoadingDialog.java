package cc.meiwen.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cc.meiwen.R;

public class LoadingDialog {

	private Context context;
	private TextView tipTextView;
	private Animation anim = null;
	private ImageView spaceshipImage = null;
	
	public LoadingDialog(Context context){
		this.context = context;
	}

	/**
	 * 得到自定义的progressDialog
	 * @param msg
	 * @return
	 */
	public Dialog createLoadingDialog(String msg) {
		
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		// main.xml中的ImageView
		spaceshipImage = (ImageView) v.findViewById(R.id.img);
		tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
		// 加载动画
		anim = AnimationUtils.loadAnimation(
				context, R.anim.loading_animation);
		// 使用ImageView显示动画
		startAnim();
		tipTextView.setText(msg);// 设置加载信息

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

//		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));// 设置布局
		
		return loadingDialog;
	}
	
	public void startAnim(){
		if(spaceshipImage!=null && anim!=null){
			spaceshipImage.startAnimation(anim);
		}
	}
	
	/**
	 * 设置显示文字*/
	public void setText(String text){
		if(tipTextView != null)
		tipTextView.setText(text);
	}
	
}
