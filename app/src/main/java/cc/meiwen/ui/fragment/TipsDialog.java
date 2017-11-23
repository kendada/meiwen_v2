package cc.meiwen.ui.fragment;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.koudai.kbase.widget.dialog.KBottomSheet;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cc.meiwen.R;
import cc.meiwen.constant.Constant;
import cc.meiwen.model.calendarSign;
import cc.meiwen.ui.activity.HistorySignActivity;
import cc.meiwen.util.FileUtils;
import cc.meiwen.util.MWShare;
import cc.meiwen.util.MnAppUtil;
import cc.meiwen.util.SharedPreferencesUtils;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by abc on 2017/11/15.
 */

public class TipsDialog extends DialogFragment {

    @BindView(R.id.content_image_view)
    ImageView contentImageView;
    @BindView(R.id.content_layout)
    RelativeLayout contentLayout;
    @BindView(R.id.root_content_layout)
    RelativeLayout rootContentLayout;

    Unbinder unbinder;
    @BindView(R.id.share_p_btn)
    ImageView sharePBtn;
    @BindView(R.id.download_p_btn)
    ImageView downloadPBtn;
    @BindView(R.id.title_view)
    TextView titleView;
    @BindView(R.id.message_view)
    TextView messageView;
    @BindView(R.id.content_view)
    TextView contentView;
    @BindView(R.id.wx_icon_view)
    ImageView wxIconView;
    @BindView(R.id.history_p_btn)
    ImageView historyPBtn;
    @BindView(R.id.tool_layout)
    RelativeLayout toolLayout;
    @BindView(R.id.root_layout)
    LinearLayout rootLayout;

    private int imageW = 480;
    private int imageH = 680;

    private calendarSign mSign;
    private boolean isCache; // 是否缓存id，是否显示历史按钮

    public void setCalendarSign(calendarSign sign) {
        mSign = sign;
    }

    public void setCache(boolean isCache) {
        this.isCache = isCache;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog);
        super.onCreate(savedInstanceState);
        mwShare = new MWShare();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_tips_layout, container);
        unbinder = ButterKnife.bind(this, view);

        int w = MnAppUtil.getPhoneW(getContext()) - 50;

        RelativeLayout.LayoutParams imageViewParams = new RelativeLayout.LayoutParams(w, imageH * w / imageW);
        contentImageView.setLayoutParams(imageViewParams);

        int toolHeight = getResources().getDimensionPixelSize(R.dimen.tool_layout_hw);

        LinearLayout.LayoutParams rootContentLayoutParams = new LinearLayout.LayoutParams(w, imageH * w / imageW + toolHeight);
        rootContentLayout.setLayoutParams(rootContentLayoutParams);

        if (isCache) {
            historyPBtn.setVisibility(View.GONE);
        } else {
            historyPBtn.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mSign != null) {
            titleView.setText(mSign.getTitle());
            titleView.setTextColor(mSign.getTextColor());
            messageView.setText(mSign.getMessage());
            messageView.setTextColor(mSign.getTextColor());
            contentView.setText(mSign.getContent());
            contentView.setTextColor(mSign.getTextColor());

            BmobFile bmobFile = mSign.getContentImage();
            if (bmobFile != null) {
                Glide.with(this).load(bmobFile.getFileUrl()).asBitmap().into(contentImageView);
            }

            if (mSign.isShowWX()) {
                wxIconView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (!isCache) {
            SharedPreferencesUtils.putString(Constant.ShareKey.OBJECT_ID, mSign.getObjectId());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @OnClick({R.id.share_p_btn, R.id.download_p_btn, R.id.history_p_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.share_p_btn:
                dismiss();
                if (mSign != null) {
                    showShare(saveBitmap(false));
                }
                dismiss();
                break;
            case R.id.download_p_btn:
                saveBitmap(true);
                dismiss();
                break;
            case R.id.history_p_btn:
                dismiss();
                HistorySignActivity.start(getContext());
                break;
        }
    }

    final int TAG_SHARE_WECHAT_FRIEND = 0;
    final int TAG_SHARE_WECHAT_MOMENT = 1;
    final int TAG_SHARE_WEIBO = 2;
    final int TAG_SHARE_CHAT = 3;

    public void showShare(final String t) {
        KBottomSheet.BottomGridSheetBuilder builder = new KBottomSheet.BottomGridSheetBuilder(getContext());
        builder.addItem(R.mipmap.icon_more_operation_share_friend, "分享到微信", TAG_SHARE_WECHAT_FRIEND, KBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_share_moment, "分享到朋友圈", TAG_SHARE_WECHAT_MOMENT, KBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_share_weibo, "分享到微博", TAG_SHARE_WEIBO, KBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_share_chat, "分享到短信", TAG_SHARE_CHAT, KBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
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
                        }
                    }
                }).build().show();
    }

    private MWShare mwShare;

    /**
     * 分享到微博
     */
    public void shareWeiBo(String t) {
        if (mwShare != null) {
            mwShare.setImagePath(t);
            mwShare.onShare(MWShare.SHARE_TYPE_0);
        }
    }

    /**
     * 分享到朋友圈
     */
    public void shareWeChatMoment(String t) {
        if (mwShare != null) {
            mwShare.setImagePath(t);
            mwShare.onShare(MWShare.SHARE_TYPE_2);
        }
    }

    /**
     * 分享到微信
     */
    public void shareWeChatFriend(String t) {
        if (mwShare != null) {
            mwShare.setImagePath(t);
            mwShare.onShare(MWShare.SHARE_TYPE_1);
        }
    }

    /**
     * 分析到短信
     */
    public void shareShortMessage(String t) {
        if (mwShare != null) {
            mwShare.setImagePath(t);
            mwShare.onShare(MWShare.SHARE_TYPE_5);
        }
    }

    /**
     * 保存图片
     */
    private String saveBitmap(boolean isShowToast) {
        String path = null;
        try {
            contentLayout.setDrawingCacheEnabled(true);
            Bitmap bitmap = contentLayout.getDrawingCache();
            FileUtils fileUtils = new FileUtils(getContext());
            path = fileUtils.saveBitmap(mSign.getObjectId() + ".jpg", bitmap);
            contentLayout.setDrawingCacheEnabled(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (isShowToast) {
            Toast.makeText(getContext(), "图片已保存在SD卡下Android/data/cc.meiwen/image目录下", Toast.LENGTH_SHORT).show();
        }
        return path;
    }

    @Override
    public void onStart() {
        try {
            super.onStart();
        } catch (Throwable ignored) {
        }
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Throwable ignored) {
        }
    }

    public void show(FragmentManager manager) {
        show(manager, getClass().getSimpleName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
