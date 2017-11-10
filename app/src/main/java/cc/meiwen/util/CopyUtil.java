package cc.meiwen.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.ClipboardManager;
import android.widget.Toast;

/**
 * 复制操作<br />
 * API 11之前： android.text.ClipboardManager<br />
 * API 11之后： android.content.ClipboardManager<br />
 * */
public class CopyUtil {

	private Context mContext;
	private SharedPreferences spf;
	private int copyCount = -1; //复制美文的次数

	@SuppressLint("WrongConstant")
	public CopyUtil(Context context){
		mContext = context;
		spf = mContext.getSharedPreferences("ad", Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
		copyCount = spf.getInt("c_count", -1);
	}

	public void copy(String text){
		ClipboardManager cm = (ClipboardManager) mContext.getSystemService(
				Context.CLIPBOARD_SERVICE);
		cm.setText(text.trim());
		Toast.makeText(mContext, "文字已复制到剪切板上", Toast.LENGTH_SHORT).show();
		SharedPreferences.Editor ed = spf.edit();
		if(copyCount>100){
			ed.putInt("c_count", -1);
			ed.commit();
		} else {
			copyCount += 1;
			ed.putInt("c_count", copyCount);
			ed.commit();
		}
	}

	public String Paste(Context context){
		ClipboardManager clipboard =
				(ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		return  String.valueOf(clipboard.getText());
	}


}
