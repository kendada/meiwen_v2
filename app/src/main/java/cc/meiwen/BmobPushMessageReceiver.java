package cc.meiwen;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONObject;

import cc.meiwen.ui.activity.BaseActivity;
import cc.meiwen.ui.activity.MainActivity;
import cc.meiwen.ui.activity.MainActivityV2;


/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-03
 * Time: 10:57
 * Version 1.0
 */

public class BmobPushMessageReceiver extends BroadcastReceiver{

    private String tag = BmobPushMessageReceiver.class.getSimpleName();

    private static int NOTIFY_ID = 1000;

    private String action = "cn.bmob.push.action.MESSAGE";

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences spf = context.getSharedPreferences(BaseActivity.APP_SETTING_NAME, Context.MODE_PRIVATE);
        boolean isPush = spf.getBoolean(BaseActivity.APP_SETTING_PUSH, true);
        if(isPush) {
            if(intent.getAction().equals(action)){
                String msg = intent.getStringExtra("msg");
                Log.i(tag, "客户端收到推送内容：" + msg);
                try {
                    JSONObject object = new JSONObject(msg);
                    String alert = object.optString("alert");
//                    sendNotification(context, alert);
                    sendNotification(context, alert, NOTIFY_ID);
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

    private void sendNotification(Context context, String message, int pushId){
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        Intent notificationIntent = new Intent(context, MainActivityV2.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        mBuilder.setContentTitle("天天美文")//设置通知栏标题
                .setContentText(message)
                .setContentIntent(intent) //设置通知栏点击意图
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                .setSmallIcon(R.mipmap.ic_launcher);//设置通知小ICON
        Notification notify = mBuilder.build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(pushId, notify);
    }

}
