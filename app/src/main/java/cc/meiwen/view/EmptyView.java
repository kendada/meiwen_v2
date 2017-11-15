package cc.meiwen.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.meiwen.R;
import cc.meiwen.util.CopyUtil;

/**
 * Created by abc on 2017/11/13.
 */

public class EmptyView extends LinearLayout {

    @BindView(R.id.title_view)
    TextView titleView;
    @BindView(R.id.content_layout)
    LinearLayout contentLayout;

    public EmptyView(Context context) {
        this(context, null);
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(getContext()).inflate(R.layout.empty_layout, this, true);
        ButterKnife.bind(this);
    }

    public void setTitle(String text){
        if(titleView != null && !TextUtils.isEmpty(text)){
            titleView.setText(text);
        }
    }

    public void setTitle(int resId){
        setTitle(getResources().getString(resId));
    }

    @OnClick({R.id.title_view, R.id.content_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_view:

                break;
            case R.id.content_layout:
                new CopyUtil(getContext()).copy("图说微情话");
                break;
        }
    }
}
