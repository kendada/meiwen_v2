package cc.meiwen.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**知识分享类*/

public class ShareUtil {

	 /**分享操作
	   *@author aken
	   *@time 2013-10-07
	   *@version 1.0.1*/
		
		private Context context;
		public ShareUtil(Context context){
			this.context = context;
		}
		
		/**分享App操作*/
		public void shareAppDialog(){
			Intent intent = new Intent();
			//发送附件
	        intent.setAction(Intent.ACTION_SEND);
	        //类型为一般格式的文字
	        intent.setType("text/plain");
	        intent.putExtra(Intent.EXTRA_TEXT,"小伙伴，我发现一个好用的美文，赶快来下载吧！");
	        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			context.startActivity(intent);
		}
		
		private void share(String txt, String con){
			String contentDetails = txt;
	        String contentBrief = con;
	        String shareUrl = "http://meiwen.bmob.cn/";
	        Intent it = new Intent(Intent.ACTION_SEND);
	        it.setType("text/plain");
	        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(it, 0);
	        if (!resInfo.isEmpty()) {
	            List<Intent> targetedShareIntents = new ArrayList<Intent>();
	            for (ResolveInfo info : resInfo) {
	                Intent targeted = new Intent(Intent.ACTION_SEND);
	                targeted.setType("text/plain");
	                ActivityInfo activityInfo = info.activityInfo;
	                
	                if (activityInfo.packageName.contains("bluetooth") ||
	                		activityInfo.name.contains("bluetooth")) {
	                    continue;
	                }
	                if (activityInfo.packageName.contains("gm") ||
	                		activityInfo.name.contains("mail")) {
	                    targeted.putExtra(Intent.EXTRA_TEXT, contentDetails);
	                } else if (activityInfo.packageName.contains("zxing")) {
	                    targeted.putExtra(Intent.EXTRA_TEXT, shareUrl);
	                } else {
	                    targeted.putExtra(Intent.EXTRA_TEXT, contentBrief);
	                }
	                targeted.setPackage(activityInfo.packageName);
	                targetedShareIntents.add(targeted);
	            }

	            Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0),
						"天天美文");
	            if (chooserIntent == null) {
	                return;
	            }

	            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
	            		targetedShareIntents.toArray(new Parcelable[] {}));

	            try {
	                context.startActivity(chooserIntent);
	            } catch (android.content.ActivityNotFoundException ex) {
	                Toast.makeText(context, "Can't find share component to share",
							Toast.LENGTH_SHORT).show();
	            }
	        }
		}
		/**  
		  * 分享功能  
		  * @param context 上下文  
		  * @param activityTitle Activity的名字  
		  * @param msgTitle 消息标题  
		  * @param msgText 消息内容  
		  * @param imgPath 图片路径，不分享图片则传null  
		  */  
		public static void shareMsg(Context context, String activityTitle,
				String msgTitle, String msgText,
		   String imgPath) {
		  Intent intent = new Intent(Intent.ACTION_SEND);
		  if (imgPath == null || imgPath.equals("")) {  
		   intent.setType("text/plain"); // 纯文本  
		  } else {  
		   File f = new File(imgPath);
		   if (f != null && f.exists() && f.isFile()) {  
		    intent.setType("image/png");  
		    Uri u = Uri.fromFile(f);
		    intent.putExtra(Intent.EXTRA_STREAM, u);
		   }  
		  }  
		  intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
		  intent.putExtra(Intent.EXTRA_TEXT, msgText);
		  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		  context.startActivity(Intent.createChooser(intent, activityTitle));
		 }


}
