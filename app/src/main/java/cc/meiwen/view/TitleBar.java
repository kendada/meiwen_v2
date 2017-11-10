package cc.meiwen.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.meiwen.R;
import cc.meiwen.ui.activity.BaseActivity;

/**
 * Created by hightkwt on 2016/1/19.
 * 标题栏的自定义控件
 */
public class TitleBar extends RelativeLayout {
    @BindView(R.id.titlebar_iv_return)
    ImageView mIvReturn;        //返回图标
    @BindView(R.id.titlebar_tv_title)
    TextView mTvTitle;           //标题
    @BindView(R.id.titlebar_tv_event_right)
    TextView mTvEventRight;         //文字事件1(右)
    @BindView(R.id.titlebar_tv_event_right2)
    TextView mTvEventRight2;         //文字事件2(右)
    @BindView(R.id.titlebar_tv_event_left)
    TextView mTvEventLeft;          //文字事件(左)
    @BindView(R.id.titlebar_rl_root)
    RelativeLayout mRlRoot;
    @BindView(R.id.titlebar_iv_right)
    ImageView mIvRight;         //图片事件（右）
    private onRightEventClickListener mRightEventListener;      //第一次点击文字事件
    private onRightEventClickListener2 mRightEventListener2;    //文字变更后第二次点击文字事件
    private onLeftEventClickListener mLeftEventListener;
    private onRightImgEventClickListener mRightImgEventListener;
    private OnLeftFinishClickListener onLeftFinishClickListener;
    private boolean mShowReturn = true;     //默认显示返回键
    private boolean mShowEvent = false;      //默认不显示文字事件点击
    private boolean mShowImgEvent = false;  //默认不显示图片事件点击

    private boolean isShowing = false; // TitleBar 是否正在显示

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.bank_view_titlebar, this, true);
        ButterKnife.bind(this);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        CharSequence titleText = ta.getText(R.styleable.TitleBar_tbTitleText);
        CharSequence rightEventText = ta.getText(R.styleable.TitleBar_tbRightEventText);
        CharSequence rightEventText2 = ta.getText(R.styleable.TitleBar_tbRightEventText2);
        int tbBg = ta.getColor(R.styleable.TitleBar_tbBackground, -1);
        Drawable drawable = ta.getDrawable(R.styleable.TitleBar_tbRightEventSrc);
        if (drawable != null)
            setRightSrc(drawable);
        if (tbBg != -1) {
            mRlRoot.setBackgroundColor(tbBg);
        }
        boolean showReturn = ta.getBoolean(R.styleable.TitleBar_tbShowReturn, mShowReturn);
        mShowEvent = ta.getBoolean(R.styleable.TitleBar_tbShowEvent, mShowEvent);
        mShowImgEvent = ta.getBoolean(R.styleable.TitleBar_tbShowImgEvent, mShowImgEvent);
        if (titleText != null)
            setTitleText(titleText);
        if (rightEventText != null)
            setRightEventText(rightEventText);
        if (rightEventText2 != null)
            setRight2EventText(rightEventText2);
        setShowReturn(showReturn);
        setShowRightEvent(mShowEvent);
        setShowImgEvent(mShowImgEvent);
        ta.recycle();
    }

    @OnClick({R.id.titlebar_iv_return, R.id.titlebar_tv_event_right, R.id.titlebar_tv_event_right2
            , R.id.titlebar_tv_event_left, R.id.titlebar_iv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titlebar_iv_return:
                returnLast();
                break;
            case R.id.titlebar_tv_event_right:
                if (mRightEventListener != null && mShowEvent) {
                    if (mLeftEventListener != null) {
                        changeToSecond();
                        mRightEventListener.onRightEventClick();
                    } else {
                        mRightEventListener.onRightEventClick();
                    }
                }
                break;
            case R.id.titlebar_tv_event_right2:
                if (mRightEventListener2 != null) {
                    changeToFirst();
                    mRightEventListener2.onRightEventClick2();
                }
                break;
            case R.id.titlebar_tv_event_left:
                if (mLeftEventListener != null) {
                    changeToFirst();
                    mLeftEventListener.onLeftEventClick();
                }
                break;
            case R.id.titlebar_iv_right:
                if (mRightImgEventListener != null) {
                    mRightImgEventListener.onRightImgEventClick();
                }
        }
    }

    /**
     * 返回上一页
     */
    public void returnLast() {
//        Runtime runtime = Runtime.getRuntime();
//        try {
//            runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        if (onLeftFinishClickListener != null) {
            onLeftFinishClickListener.onLeftFinishClick();
        } else {
            ((BaseActivity) getContext()).finish();
        }

    }


    /**
     * 设置是否需要返回键
     *
     * @param showReturn
     */
    public void setShowReturn(boolean showReturn) {
        if (showReturn) {
            mIvReturn.setVisibility(VISIBLE);
        } else {
            mIvReturn.setVisibility(GONE);
        }

    }

    /**
     * 设置是否需要文字事件(右)点击
     *
     * @param showEvent
     */
    public void setShowRightEvent(boolean showEvent) {
        mShowEvent = showEvent;
        if (showEvent) {
            mTvEventRight.setVisibility(VISIBLE);
        } else {
            mTvEventRight.setVisibility(GONE);
        }
    }

    /**
     * 设置是否需要图片事件（右）点击
     *
     * @param showImgEvent
     */
    public void setShowImgEvent(boolean showImgEvent) {
        if (showImgEvent)
            mIvRight.setVisibility(VISIBLE);
        else
            mIvRight.setVisibility(GONE);
    }

    /**
     * 点击左边事件
     */
    public void clickLeft() {
        onClick(mTvEventLeft);
    }

    /**
     * 设置标题文本
     *
     * @param text
     */
    public void setTitleText(CharSequence text) {
        mTvTitle.setText(text);
    }

    public void setTitleMaxEms(int maxems) {
        if (mTvTitle != null) {
            mTvTitle.setMaxEms(maxems);
        }
    }

    /**
     * 设置事件1(右)文本
     *
     * @param text
     */
    public void setRightEventText(CharSequence text) {
        mTvEventRight.setText(text);
    }

    /**
     * 是否可点击
     * */
    public void setRightEventTextEnabled(boolean enabled){
        mTvEventRight.setEnabled(enabled);
    }

    /**
     * 设置事件2(右)文本
     *
     * @param text
     */
    public void setRight2EventText(CharSequence text) {
        mTvEventRight2.setText(text);
    }

    /**
     * 设置右边事件图片
     *
     * @param drawable
     */
    private void setRightSrc(Drawable drawable) {
        if (drawable != null)
            mIvRight.setImageDrawable(drawable);
    }

    /**
     * 事件(右)的第一次点击事件
     */
    public void setOnRightEventClickListener(onRightEventClickListener listener) {
        mRightEventListener = listener;
    }

    /**
     * 事件(右)的第二次点击事件(文字变更后)
     */
    public void setOnRightEventClickListener2(onRightEventClickListener2 listener) {
        mRightEventListener2 = listener;
    }

    /**
     * 如果有需要实现的事件(左)，用这个方法
     */
    public void setOnLeftEventClickListener(onLeftEventClickListener listener) {
        mLeftEventListener = listener;
    }

    /**
     * 图片事件（右）的点击事件
     *
     * @param listener
     */
    public void setOnRightImgEventClickListener(onRightImgEventClickListener listener) {
        mRightImgEventListener = listener;
    }

    /**
     * 切换到第二种按钮
     */
    public void changeToSecond() {
        mIvReturn.setVisibility(GONE);
        mTvEventLeft.setVisibility(VISIBLE);
        mTvEventRight.setVisibility(GONE);
        mTvEventRight2.setVisibility(VISIBLE);
    }

    /**
     * 切换到第一种按钮
     */
    public void changeToFirst() {
        mTvEventLeft.setVisibility(GONE);
        mIvReturn.setVisibility(VISIBLE);
        mTvEventRight2.setVisibility(GONE);
        if (mRightEventListener != null) {
            mTvEventRight.setVisibility(VISIBLE);
        }
    }

    public void toggle(){
        if (isShowing){
            hideTitleBar();
        } else {
            showTitleBar();
        }
    }

    /**
     * 显示titleBar,有动画效果
     * */
    private void showTitleBar(){
        setVisibility(View.VISIBLE);
        if(isShowing){
            return;
        }
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.top_in);
        anim.setFillAfter(true);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isShowing = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(anim);
    }

    /**
     * 隐藏TitleBar, 有动画效果
     * */
    private void hideTitleBar(){
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.top_out);
        anim.setFillAfter(true);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isShowing = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(anim);
    }

    public void hideAss() {

    }

    public void showAss() {

    }

    @Override
    public Drawable getBackground() {
        return mRlRoot.getBackground();
    }

    @Override
    public void setBackgroundResource(int resid) {
        mRlRoot.setBackgroundResource(resid);
    }

    public OnLeftFinishClickListener getOnLeftFinishClickListener() {
        return onLeftFinishClickListener;
    }

    public void setOnLeftFinishClickListener(OnLeftFinishClickListener onLeftFinishClickListener) {
        this.onLeftFinishClickListener = onLeftFinishClickListener;
    }

    public interface onRightEventClickListener {

        void onRightEventClick();
    }

    public interface onRightEventClickListener2 {

        void onRightEventClick2();
    }

    public interface onLeftEventClickListener {

        void onLeftEventClick();
    }

    public interface onRightImgEventClickListener {

        void onRightImgEventClick();
    }

    public interface OnLeftFinishClickListener {
        void onLeftFinishClick();
    }
}
