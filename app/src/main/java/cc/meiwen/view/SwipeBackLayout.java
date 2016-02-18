package cc.meiwen.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-04
 * Time: 09:22
 * Version 1.0
 */

public class SwipeBackLayout extends LinearLayout implements View.OnTouchListener{

    //最外层父控件
    private ViewGroup mParentView;

    //滑动帮助类
    private Scroller mScroller;

    //屏幕宽度
    private int mScreenWidth;

    //需要处理滑动的子控件
    private View touchView;

    //是否正在滑动
    private boolean isSilding;

    //按下的坐标点
    private int downX, downY;

    //滑动的最小距离
    private int mTouchSlop;

    //x坐标点
    private int tempX;

    //是否滑动完毕
    private boolean isFinish;

    private  OnSwipeBackListener onSwipeBackListener;

    private String tag = SwipeBackLayout.class.getSimpleName();

    public SwipeBackLayout(Context context) {
        this(context, null);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed){
            mParentView = (ViewGroup)this.getParent();
            mScreenWidth = this.getWidth();
        }
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            mParentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();

            if(mScroller.isFinished()){ //回调方法，滑动完毕
                if(onSwipeBackListener!=null && isFinish){
                    onSwipeBackListener.onFinishActivity();
                }
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = tempX =  (int)event.getRawX();
                downY = (int)event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int)event.getRawX();
                int deltaX = tempX - moveX;
                tempX = moveX;
                if(Math.abs(moveX-downX)>mTouchSlop && Math.abs((int)event.getRawY()-downY)<mTouchSlop){
                    isSilding = true;
                }
                if(moveX-downX>=0 && isSilding){
                    mParentView.scrollBy(deltaX, 0);
                    if(isTouchAbsListView() || isTouchScrollView()){
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isSilding = false;
                if(mParentView.getScrollX()<=-mScreenWidth/2){
                    isFinish = true;
                    scrollRight();
                } else {
                    scrollLeft();
                    isFinish = false;
                }
                break;
        }
        if(isTouchScrollView()|| isTouchAbsListView()){
            return v.onTouchEvent(event);
        }
        return true;
    }

    private void scrollRight(){
        int rightX = mScreenWidth + mParentView.getScrollX();
        mScroller.startScroll(mParentView.getScrollX(), 0, -rightX+1, 0, Math.abs(rightX));
        postInvalidate();
    }

    private void scrollLeft(){
        int leftX = mParentView.getScrollX();
        mScroller.startScroll(mParentView.getScrollX(), 0, -leftX, 0, Math.abs(leftX));
        postInvalidate();
    }

    public View getTouchView() {
        return touchView;
    }

    public void setTouchView(View touchView) {
        this.touchView = touchView;
        touchView.setOnTouchListener(this);
    }

    /**
     * 判断是否是ScrollView，及其子类
     * */
    public boolean isTouchScrollView(){
        return touchView instanceof ScrollView;
    }

    /**
     * 判断是否是AbsListView，及其子类
     * */
    public boolean isTouchAbsListView(){
        return  touchView instanceof AbsListView;
    }

    public void setOnSwipeBackListener(OnSwipeBackListener onSwipeBackListener) {
        this.onSwipeBackListener = onSwipeBackListener;
    }

    public interface OnSwipeBackListener{
        void onFinishActivity();
    }


}