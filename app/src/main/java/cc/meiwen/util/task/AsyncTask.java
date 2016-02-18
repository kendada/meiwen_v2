package cc.meiwen.util.task;

import android.os.Handler;
import android.os.Message;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-29
 * Time: 14:52
 * Version 1.0
 */

public abstract class AsyncTask extends ThreadPoolTask {

    private String tag = AsyncTask.class.getSimpleName();

    public AsyncTask(){

    }

    @Override
    public void run() {
        Object obj = loadData();
        Message msg = handler.obtainMessage(0, obj);
        handler.sendMessage(msg);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    updata(msg.obj);
                    break;
            }
        }
    };

}
