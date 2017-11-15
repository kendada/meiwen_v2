package cc.meiwen.ui.fragment;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.meiwen.R;
import cc.meiwen.util.MnAppUtil;

/**
 * Created by abc on 2017/11/15.
 */

public class TipsDialog extends DialogFragment {

    @BindView(R.id.content_image_view)
    ImageView contentImageView;
    @BindView(R.id.content_layout)
    RelativeLayout contentLayout;

    Unbinder unbinder;
    private int imageW = 480;
    private int imageH = 640;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int w = MnAppUtil.getPhoneW(getContext());

        LinearLayout rootLayout = (LinearLayout) inflater.inflate(R.layout.dialog_tips_layout, container);
        LinearLayout.LayoutParams rootLayoutParams = new LinearLayout.LayoutParams(w, imageH * w / imageW);
        rootLayout.setLayoutParams(rootLayoutParams);

        unbinder = ButterKnife.bind(this, rootLayout);

        RelativeLayout.LayoutParams imageViewParams = new RelativeLayout.LayoutParams(w, imageH * w / imageW);
        contentImageView.setLayoutParams(imageViewParams);

        return rootLayout;
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
