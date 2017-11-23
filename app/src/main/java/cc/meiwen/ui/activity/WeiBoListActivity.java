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
import cc.meiwen.adapter.WeiBoListAdapter;
import cc.meiwen.api.BaseApi;
import cc.meiwen.model.FriedsTimelineBO;
import cc.meiwen.model.FriendsTimelineStatusesBO;
import cc.meiwen.model.PostType;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import rx.Observer;

/**
 * Created by abc on 2017/11/22.
 *
 */

public class WeiBoListActivity extends AppCompatActivity {

    private String TAG = WeiBoListActivity.class.getSimpleName();

    private List<FriendsTimelineStatusesBO> mList;

    private RecyclerView mViewRecycle;
    private WeiBoListAdapter mAdapter;

    private KTipDialog loadingDialog;

    private BaseApi mBaseApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weibo_list_layout);

        initView();
        initData();

        getData();
    }

    private void initView() {
        mBaseApi = new BaseApi();

        loadingDialog = new KTipDialog.Builder(this)
                .setIconType(KTipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在刷新")
                .create();

        mList = new ArrayList<>();

        mViewRecycle = (RecyclerView) findViewById(R.id.recycle_view);
        mViewRecycle.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new WeiBoListAdapter(mList, this);
        mViewRecycle.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData();
            }
        }, mViewRecycle);
    }

    private void initData() {
        getPostType();
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

    private void getData() {
        if(isDataEmpty()){
            loadingDialog.show();
        }

        mBaseApi.friedsTimeline(getPage())
                .subscribe(new Observer<FriedsTimelineBO>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "e = " + e);
                mAdapter.loadMoreFail();
            }

            @Override
            public void onNext(FriedsTimelineBO friedsTimelineBO) {
                Log.d(TAG, "friedsTimelineBO = " + friedsTimelineBO);
                toResult(friedsTimelineBO);
            }
        });
    }

    private boolean isDataEmpty(){
        return mList == null || mList.size() == 0;
    }

    /**
     * 页数
     * */
    int page = 1;
    public int getPage(){
        return page;
    }

    /**
     * 每页最大值
     * */
    public int getCount(){
        return 20;
    }

    public void toResult(FriedsTimelineBO friedsTimelineBO) {
        if(isDataEmpty()){
            loadingDialog.dismiss();
        }
        if (friedsTimelineBO != null) {
            List<FriendsTimelineStatusesBO> list = friedsTimelineBO.getStatuses();
            if(list == null || list.size() == 0){
                mAdapter.loadMoreEnd();
            } else {
                if(list.size() < getCount()){
                    mAdapter.loadMoreEnd();
                } else {
                    mAdapter.loadMoreComplete();
                    page++;
                }
                mList.addAll(list);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

}
