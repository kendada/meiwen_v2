package cc.meiwen.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.koudai.kbase.widget.dialog.KBottomSheet;

import cc.meiwen.R;
import cc.meiwen.util.CopyUtil;
import cc.meiwen.util.MWShare;
import cc.meiwen.view.SwipeBackLayout;
import cc.meiwen.view.TitleBar;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-06
 * Time: 11:09
 * Version 1.0
 */

public class AboutActivity extends BaseActivity{

    private TitleBar mTitleBar;
    private SwipeBackLayout swip_back_layout;
    private ScrollView scrollView;
    private TextView qq_btn, how_btn, wx_btn, wx_user_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_layout);

        mwShare = new MWShare();
        mwShare.setUrl(url);
        mwShare.setTitleUrl(url);
        mwShare.setImageData();

        scrollView = (ScrollView)findViewById(R.id.scrollView);
        swip_back_layout = (SwipeBackLayout)findViewById(R.id.swip_back_layout);
        swip_back_layout.setTouchView(scrollView);
        swip_back_layout.setOnSwipeBackListener(new SwipeBackLayout.OnSwipeBackListener() {
            @Override
            public void onFinishActivity() {
                AboutActivity.this.finish();
            }
        });

        mTitleBar = (TitleBar) findViewById(R.id.title_bar);
        mTitleBar.setOnRightEventClickListener(new TitleBar.onRightEventClickListener() {
            @Override
            public void onRightEventClick() {
                showShare("我发现一个感人的美文分享APP，一起来下载吧！");
            }
        });

        qq_btn = (TextView)findViewById(R.id.qq_btn);
        qq_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CopyUtil(getContext()).copy("287292031");
            }
        });
        wx_btn = (TextView) findViewById(R.id.wx_btn);
        wx_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CopyUtil(getContext()).copy("图说微情话");
            }
        });
        wx_user_btn = (TextView) findViewById(R.id.wx_user_btn);
        wx_user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CopyUtil(getContext()).copy("vqh1314");
            }
        });
        how_btn = (TextView)findViewById(R.id.how_btn);
        how_btn.setText("使用说明：\r\n" +
                "1. 每条美文长按可复制；\r\n" +
                "2. 用户可收藏美文，同步到云端，永不丢失；\r\n" +
                "3. 用户发布美文，将会通过管理员审核；【为了保证美文的质量】\r\n" +
                "4. 用户可申请做管理员，请添加QQ群联系群主；");

    }

    final int TAG_SHARE_WECHAT_FRIEND = 0;
    final int TAG_SHARE_WECHAT_MOMENT = 1;
    final int TAG_SHARE_WEIBO = 2;
    final int TAG_SHARE_CHAT = 3;
    final int TAG_SHARE_LOCAL = 4;

    public void showShare(final String text) {
        KBottomSheet.BottomGridSheetBuilder builder = new KBottomSheet.BottomGridSheetBuilder(this);
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
                                shareWeChatFriend(text);
                                break;
                            case TAG_SHARE_WECHAT_MOMENT: //分享到朋友圈
                                shareWeChatMoment(text);
                                break;
                            case TAG_SHARE_WEIBO: // 分享到微博
                                shareWeiBo(text);
                                break;
                            case TAG_SHARE_CHAT: // 分享到私信
                                shareShortMessage(text);
                                break;
                            case TAG_SHARE_LOCAL: // 保存到本地
                                saveImage(text);
                                break;
                        }
                    }
                }).build().show();
    }

    private MWShare mwShare;
    private String url = "http://app.mi.com/details?id=cc.meiwen";

    /**
     * 分享到微博
     * */
    public void shareWeiBo(String text){
        if(mwShare != null){
            mwShare.setText(text);
            mwShare.onShare(MWShare.SHARE_TYPE_0);
        }
    }

    /**
     * 分享到朋友圈
     * */
    public void shareWeChatMoment(String text){
        if(mwShare != null){
            mwShare.setTitle(text);
            mwShare.setText(text);
            mwShare.onShare(MWShare.SHARE_TYPE_2);
        }
    }

    /**
     * 分享到微信
     * */
    public void shareWeChatFriend(String text){
        if(mwShare != null){
            mwShare.setText(text);
            mwShare.setTitle(text);
            mwShare.onShare(MWShare.SHARE_TYPE_1);
        }
    }

    /**
     * 分析到短信
     * */
    public void shareShortMessage(String text){
        if(mwShare != null){
            mwShare.setText(text);
            mwShare.onShare(MWShare.SHARE_TYPE_5);
        }
    }

    /**
     * 保存图片到SD卡
     * */
    public void saveImage(String text){}


}
