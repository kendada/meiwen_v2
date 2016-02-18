package cc.meiwen.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cc.meiwen.R;

/**
 * User: 靳世坤(1203596603@qq.com)
 * Date: 2015-08-17
 * Time: 15:38
 * Version 1.0
 */

public class StateFrameLayout extends FrameLayout {

    public static final int VIEW_STATE_CONTENT = 0; //显示内容

    public static final int VIEW_STATE_ERROR = 1;  //显示错误内容

    public static final int VIEW_STATE_EMPTY = 2;  //加载数据为空

    public static final int VIEW_STATE_LOADING = 3; //正在加载时

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({VIEW_STATE_CONTENT, VIEW_STATE_ERROR, VIEW_STATE_EMPTY, VIEW_STATE_LOADING})
    public @interface ViewState{

    }

    private LayoutInflater mInflater ;

    private View mContentView;

    private View mLoadingView;

    private View mEmptyView;

    private View mErrorView;

    @StateFrameLayout.ViewState
    private int mViewState = VIEW_STATE_CONTENT;

    public StateFrameLayout(Context context) {
        this(context, null);
    }

    public StateFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * 初始化
     * */
    private void init(AttributeSet attrs){
        mInflater = LayoutInflater.from(getContext());

        //获取自定义属性
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.StateFrameLayout);

        int loadingViewResId = a.getResourceId(R.styleable.StateFrameLayout_msv_loadingView, -1);
        if(loadingViewResId > -1){
            mLoadingView = mInflater.inflate(loadingViewResId, this, false);
            addView(mLoadingView, mLoadingView.getLayoutParams());
        }

        int emptyViewResId = a.getResourceId(R.styleable.StateFrameLayout_msv_emptyView, -1);
        if(emptyViewResId > -1){
            mEmptyView = mInflater.inflate(emptyViewResId, this, false);
            addView(mEmptyView, mEmptyView.getLayoutParams());
        }

        int errorViewResId = a.getResourceId(R.styleable.StateFrameLayout_msv_errorView, -1);
        if(errorViewResId>-1){
            mErrorView = mInflater.inflate(errorViewResId, this, false);
            addView(mErrorView, mErrorView.getLayoutParams());
        }

        int viewState = a.getInt(R.styleable.StateFrameLayout_msv_viewState, VIEW_STATE_CONTENT);

        switch (viewState){
            case VIEW_STATE_CONTENT:
                mViewState = VIEW_STATE_CONTENT;
                break;
            case VIEW_STATE_ERROR:
                mViewState = VIEW_STATE_ERROR;
                break;
            case VIEW_STATE_EMPTY:
                mViewState = VIEW_STATE_EMPTY;
                break;
            case VIEW_STATE_LOADING:
                mViewState = VIEW_STATE_LOADING;
                break;
        }
        a.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(mContentView == null) throw new IllegalArgumentException("Content view is not defined");
        setView();
    }

    @Override
    public void addView(View child) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, index, params);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int width, int height) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, width, height);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) mContentView = child;
        return super.addViewInLayout(child, index, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        if (isValidContentView(child)) mContentView = child;
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }

    /**
     * Checks if the given {@link View} is valid for the Content View
     *
     * @param view The {@link View} to check
     * @return
     */
    private boolean isValidContentView(View view) {
        if (mContentView != null && mContentView != view) {
            return false;
        }

        return view != mLoadingView && view != mErrorView && view != mEmptyView;
    }

    public View getView(@ViewState int state){
        switch (state){
            case VIEW_STATE_LOADING:
                return mLoadingView;

            case VIEW_STATE_EMPTY:
                return mEmptyView;

            case VIEW_STATE_ERROR:
                return mErrorView;

            case VIEW_STATE_CONTENT:
                return mContentView;

            default: return null;
        }
    }

    @ViewState
    public int getViewState() {
        return mViewState;
    }

    public void setViewState(@ViewState int state){
        if(state != mViewState){
            mViewState = state;
            setView();
        }
    }

    private void setView(){
        switch (mViewState){
            case VIEW_STATE_LOADING:
                if(mLoadingView==null){
                    throw new NullPointerException("Loading View is null");
                }

                mLoadingView.setVisibility(View.VISIBLE);
                if(mContentView!=null) mContentView.setVisibility(View.GONE);
                if(mErrorView!=null) mErrorView.setVisibility(View.GONE);
                if(mEmptyView!=null) mEmptyView.setVisibility(View.GONE);
                break;

            case VIEW_STATE_EMPTY:
                if(mEmptyView==null){
                    throw  new NullPointerException("Empty View is null");
                }

                mEmptyView.setVisibility(View.VISIBLE);
                if(mLoadingView!=null) mLoadingView.setVisibility(View.GONE);
                if(mContentView!=null) mContentView.setVisibility(View.GONE);
                if(mErrorView!=null) mErrorView.setVisibility(View.GONE);
                break;

            case VIEW_STATE_ERROR:
                if(mErrorView==null) {
                    throw  new NullPointerException("Error View is null");
                }

                mErrorView.setVisibility(View.VISIBLE);
                if(mLoadingView!=null) mLoadingView.setVisibility(View.GONE);
                if(mContentView!=null) mContentView.setVisibility(View.GONE);
                if(mEmptyView!=null) mEmptyView.setVisibility(View.GONE);
                break;

            case VIEW_STATE_CONTENT:
                default:
                    if(mContentView==null){
                        throw new NullPointerException("Content View is null");
                    }

                    mContentView.setVisibility(View.VISIBLE);
                    if(mLoadingView!=null) mLoadingView.setVisibility(View.GONE);
                    if(mErrorView!=null) mErrorView.setVisibility(View.GONE);
                    if(mEmptyView!=null) mEmptyView.setVisibility(View.GONE);
                    break;
        }
    }

    public void setViewForState(View view, @ViewState int state, boolean switchToState){
        switch (state){
            case VIEW_STATE_LOADING:
                if(mLoadingView != null) removeView(mLoadingView);
                mLoadingView = view;
                addView(mLoadingView);
                break;

            case VIEW_STATE_EMPTY:
                if(mEmptyView != null) removeView(mEmptyView);
                mEmptyView = view;
                addView(mEmptyView);
                break;

            case VIEW_STATE_ERROR:
                if(mErrorView != null) removeView(mErrorView);
                mErrorView = view;
                addView(mErrorView);
                break;

            case VIEW_STATE_CONTENT:
                if(mContentView != null) removeView(mContentView);
                mContentView = view;
                addView(mContentView);
                break;
        }

        if (switchToState) setViewState(state);
    }

    public void setViewForState(View view, @ViewState int state){
        setViewForState(view, state, false);
    }

    public void  setViewForState(@LayoutRes int layoutRes, @ViewState int state, boolean switchToState){
        if(mInflater == null) mInflater = LayoutInflater.from(getContext());
        View view = mInflater.inflate(layoutRes, this, false);
        setViewForState(view, state, switchToState);
    }

    public void setViewForState(@LayoutRes int layoutRes, @ViewState int state){
        setViewForState(layoutRes, state, false);
    }

}
