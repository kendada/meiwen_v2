package cc.meiwen;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import org.json.JSONObject;

import cc.meiwen.ui.activity.BaseActivity;
import cc.meiwen.ui.activity.MainActivity;


/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-03
 * Time: 10:57
 * Version 1.0
 */

public class MyPushMessageReceiver extends BroadcastReceiver{

    private String tag = MyPushMessageReceiver.class.getSimpleName();

    private static int NOTIFY_ID = 1000;

    private String action = "cn.bmob.push.action.MESSAGE";

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences spf = context.getSharedPreferences(BaseActivity.APP_SETTING_NAME, Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
        boolean isPush = spf.getBoolean(BaseActivity.APP_SETTING_PUSH, true);
        if(isPush) {
            if(intent.getAction().equals(action)){
                String msg = intent.getStringExtra("msg");
                Log.i(tag, "客户端收到推送内容：" + msg);
                try {
                    JSONObject object = new JSONObject(msg);
                    String alert = object.optString("alert");
                    sendNotification(context, alert);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 发送通知
     * */
    private void sendNotification(Context context, String message){
        NotificationManager notifyMgr= (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("来自美文吧的新消息")
                .setContentTitle("天天美文")
                .setContentText(message)
                .setContentIntent(pi)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notifyMgr.notify(NOTIFY_ID, notification);
    }

}
