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

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cc.meiwen.R;
import cc.meiwen.constant.Constant;
import cc.meiwen.model.calendarSign;
import cc.meiwen.util.FileUtils;
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

    private int imageW = 480;
    private int imageH = 680;

    private calendarSign mSign;
    private boolean isCache;

    public void setCalendarSign(calendarSign sign) {
        mSign = sign;
    }

    public void setCache(boolean isCache){
        this.isCache = isCache;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog);
        super.onCreate(savedInstanceState);
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

            if(mSign.isShowWX()){
                wxIconView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(!isCache){
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
                if(mSign != null){
//                    ShareUtil.shareMsg(getContext(), "", mSign.getTitle(), mSign.getMessage(), saveBitmap(false));
                }
                dismiss();
                break;
            case R.id.download_p_btn:
                saveBitmap(true);
                dismiss();
                break;
            case R.id.history_p_btn:
                dismiss();
                break;
        }
    }

    /**
     * 保存图片
     * */
    private String saveBitmap(boolean isShowToast){
        String path = null;
        try {
            contentLayout.setDrawingCacheEnabled(true);
            Bitmap bitmap = contentLayout.getDrawingCache();
            FileUtils fileUtils = new FileUtils(getContext());
            path = fileUtils.saveBitmap(mSign.getObjectId()+".jpg", bitmap);
            contentLayout.setDrawingCacheEnabled(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(isShowToast){
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
