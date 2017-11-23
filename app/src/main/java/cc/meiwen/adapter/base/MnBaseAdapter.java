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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.koudai.kbase.widget.dialog.KBottomSheet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import cc.meiwen.R;
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

    final int TAG_SHARE_WECHAT_FRIEND = 0;
    final int TAG_SHARE_WECHAT_MOMENT = 1;
    final int TAG_SHARE_WEIBO = 2;
    final int TAG_SHARE_CHAT = 3;
    final int TAG_SHARE_LOCAL = 4;

    private String tag = MnBaseAdapter.class.getSimpleName();

    public MnBaseAdapter(Context context, List<T> datas){
        mDatas = datas;
        mContext = context;
        fileUtils = new FileUtils(mContext);
        spf = mContext.getSharedPreferences(BaseActivity.APP_SETTING_NAME, Context.MODE_PRIVATE);
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

    public void showShare(final T t) {
        KBottomSheet.BottomGridSheetBuilder builder = new KBottomSheet.BottomGridSheetBuilder(mContext);
        builder.addItem(R.mipmap.icon_more_operation_share_friend, "分享到微信", TAG_SHARE_WECHAT_FRIEND, KBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_share_moment, "分享到朋友圈", TAG_SHARE_WECHAT_MOMENT, KBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_share_weibo, "分享到微博", TAG_SHARE_WEIBO, KBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_share_chat, "分享到短信", TAG_SHARE_CHAT, KBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_save, "保存到本地", TAG_SHARE_LOCAL, KBottomSheet.BottomGridSheetBuilder.SECOND_LINE)
                .setOnSheetItemClickListener(new KBottomSheet.BottomGridSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(KBottomSheet dialog, View itemView) {
                        dialog.dismiss();
                        int tag = (int) itemView.getTag();
                        switch (tag) {
                            case TAG_SHARE_WECHAT_FRIEND: //分享到微信
                                shareWeChatFriend(t);
                                break;
                            case TAG_SHARE_WECHAT_MOMENT: //分享到朋友圈
                                shareWeChatMoment(t);
                                break;
                            case TAG_SHARE_WEIBO: // 分享到微博
                                shareWeiBo(t);
                                break;
                            case TAG_SHARE_CHAT: // 分享到私信
                                shareShortMessage(t);
                                break;
                            case TAG_SHARE_LOCAL: // 保存到本地
                                saveImage(t);
                                break;
                        }
                    }
                }).build().show();
    }

    /**
     * 分享到微博
     * */
    public void shareWeiBo(T t){}

    /**
     * 分享到朋友圈
     * */
    public void shareWeChatMoment(T t){}

    /**
     * 分享到微信
     * */
    public void shareWeChatFriend(T t){}

    /**
     * 分析到短信
     * */
    public void shareShortMessage(T t){}

    /**
     * 保存图片到SD卡
     * */
    public void saveImage(T t){}

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
                Glide.with(mContext).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        saveBitmap(resource);
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

    public void saveBitmap(Bitmap bitmap){
        try {
            fileUtils.saveBitmap(MnDateUtil.stringByFormat(new Date(),
                    MnDateUtil.dateFormatFileName)+".jpg", bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(mContext, "图片已保存在SD卡下Android/data/cc.meiwen/image目录下", Toast.LENGTH_SHORT).show();
    }
}
