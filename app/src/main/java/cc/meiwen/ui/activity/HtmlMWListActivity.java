package cc.meiwen.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.koudai.kbase.widget.dialog.KTipDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cc.meiwen.R;
import cc.meiwen.adapter.HtmlMwListAdapter;
import cc.meiwen.api.BaseApi;
import cc.meiwen.model.HtmlMwBO;
import cc.meiwen.model.PostType;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import rx.Observer;

/**
 * Created by abc on 2017/11/22.
 */

public class HtmlMWListActivity extends AppCompatActivity {

    private String TAG = HtmlMWListActivity.class.getSimpleName();

    private List<HtmlMwBO> mList;

    private RecyclerView mViewRecycle;
    private HtmlMwListAdapter mAdapter;

    private BaseApi mBaseApi;

    private KTipDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weibo_list_layout);

        initView();
        initData();
        getPostType();
        getData();
    }

    private void initView() {
        loadingDialog = new KTipDialog.Builder(this)
                .setIconType(KTipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在刷新")
                .create();

        mBaseApi = new BaseApi();

        mList = new ArrayList<>();

        mViewRecycle = (RecyclerView) findViewById(R.id.recycle_view);
        mViewRecycle.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new HtmlMwListAdapter(mList, this);
        mViewRecycle.setAdapter(mAdapter);
    }

    private void initData() {
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData();
            }
        }, mViewRecycle);
    }

    private PostType mPostType;

    private void getPostType(){
        BmobQuery<PostType> query = new BmobQuery<>();
        query.order("-createdAt");
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.findObjects(new FindListener<PostType>() {
            @Override
            public void done(List<PostType> list, BmobException e) {
                if(list!=null && list.size()>0){
                    int number = new Random().nextInt(list.size());
                    mPostType = list.get(number);
                    if(mAdapter != null){
                        mAdapter.setPostType(mPostType);
                    }
                }
            }
        });
    }
    int page = 1;
    public int getPage(){
        return page;
    }

    public int getCount(){
        return 10;
    }

    private void getData() {
        if(isEmpty()){
            loadingDialog.show();
        }
        mBaseApi.mwHtml(getPage())
                .subscribe(new Observer<List<HtmlMwBO>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "e = " + e);
                mAdapter.loadMoreFail();
            }

            @Override
            public void onNext(List<HtmlMwBO> list) {
                Log.d(TAG, "list = " + list);
                if(isEmpty()){
                    loadingDialog.dismiss();
                }
                toResult(list);
            }
        });
    }

    private boolean isEmpty(){
        return mList == null || mList.size() == 0;
    }

    public void toResult(List<HtmlMwBO> list) {
        if(list == null || list.size() ==0){
            mAdapter.loadMoreEnd();
        } else {
            if(list.size() < getCount()){
                mAdapter.loadMoreEnd();
            }
            mList.addAll(list);
            mAdapter.notifyDataSetChanged();
            mAdapter.loadMoreComplete();
            page++;
        }
    }

}
