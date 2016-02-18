package cc.meiwen.adapter.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import cc.meiwen.ui.activity.BaseActivity;
import cc.meiwen.util.FileUtils;
import cc.meiwen.util.MnDateUtil;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-30
 * Time: 10:51
 * Version 1.0
 */

public abstract class MnBaseAdapter<T> extends BaseAdapter {

    protected List<T> mDatas;
    protected Context mContext;

    public boolean isLoadImg = true;

    private SharedPreferences spf;

    private FileUtils fileUtils;

    private String tag = MnBaseAdapter.class.getSimpleName();

    public MnBaseAdapter(Context context, List<T> datas){
        mDatas = datas;
        mContext = context;
        fileUtils = new FileUtils(mContext);
        spf = mContext.getSharedPreferences(BaseActivity.APP_SETTING_NAME, Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
        isLoadImg = spf.getBoolean(BaseActivity.APP_SETTING_ISLOADIMG, true);
    }

    @Override
    public int getCount() {
        return mDatas!=null ? mDatas.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return mDatas!=null ? mDatas.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 刷新数据
     * */
    public void refreshData(){
        isLoadImg = spf.getBoolean(BaseActivity.APP_SETTING_ISLOADIMG, true);
        Log.i(tag, "---54---"+isLoadImg);
        this.notifyDataSetChanged();
    }



    /**
     * 保存图片到SD卡
     * */
    public void saveImageToSDCard(final String url){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("操作提示");
        builder.setMessage("你确定下载这张图片吗？\r\n" +
                "保存路径：SD卡下Android/data/cc.meiwen/image目录下");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ImageLoader.getInstance().loadImage(url, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        try {
                            fileUtils.saveBitmap(MnDateUtil.stringByFormat(new Date(),
                                    MnDateUtil.dateFormatFileName)+".jpg", bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(mContext, "图片已保存在SD卡下Android/data/cc.meiwen/image目录下", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

}
