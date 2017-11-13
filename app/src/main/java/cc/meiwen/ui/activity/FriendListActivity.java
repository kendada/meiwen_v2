package cc.meiwen.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.koudai.kbase.widget.dialog.KTipDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.meiwen.R;
import cc.meiwen.adapter.UserItemAdapter;
import cc.meiwen.model.User;
import cc.meiwen.ui.presenter.FriendListMvpView;
import cc.meiwen.ui.presenter.FriendListPresenter;
import cc.meiwen.view.TitleBar;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by abc on 2017/11/11.
 * 好友列表
 */

public class FriendListActivity extends BaseActivity implements FriendListMvpView {

    public static void start(Context context, int type) {
        Intent intent = new Intent(context, FriendListActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private List<User> mList;
    private UserItemAdapter mAdapter;
    private FriendListPresenter mFriendListPresenter;

    private KTipDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list_layout);
        ButterKnife.bind(this);

        initViews();
        initData();
    }

    private void initViews() {
        loadingDialog = new KTipDialog.Builder(getContext())
                .setIconType(KTipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在刷新")
                .create();

        mFriendListPresenter = new FriendListPresenter();
        mFriendListPresenter.attachView(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mList = new ArrayList<>();
        mAdapter = new UserItemAdapter(mList, this);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mFriendListPresenter.getUserList();
            }
        }, recyclerView);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PostTypeActivity.start(getContext(), mList.get(position));
            }
        });
    }

    private void initData() {
        mFriendListPresenter.getUserList();
    }

    @Override
    public void toResult(List<User> list) {
        if(list == null || list.size() == 0){
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
            mList.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public int count() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public void onError(BmobException be) {
        mAdapter.loadMoreFail();

    }

    @Override
    public void showDialog() {
        if(loadingDialog != null){
            loadingDialog.show();
        }
    }

    @Override
    public void dismissDialog() {
        if(loadingDialog != null){
            loadingDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFriendListPresenter.detachView();
    }
}
