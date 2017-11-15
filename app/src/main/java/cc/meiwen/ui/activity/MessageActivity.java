package cc.meiwen.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.koudai.kbase.widget.dialog.KTipDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.meiwen.R;
import cc.meiwen.adapter.MessageAdapter;
import cc.meiwen.model.Comment;
import cc.meiwen.model.User;
import cc.meiwen.ui.presenter.MessageMvpView;
import cc.meiwen.ui.presenter.MessagePresenter;
import cc.meiwen.view.EmptyView;
import cc.meiwen.view.TitleBar;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by abc on 2017/11/13.
 */

public class MessageActivity extends BaseActivity implements MessageMvpView{

    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;



    public static void start(Context context) {
        Intent intent = new Intent(context, MessageActivity.class);
        context.startActivity(intent);
    }

    private User mUser;

    private List<Comment> mList;
    private MessageAdapter mAdapter;

    private MessagePresenter mMessagePresenter;

    private KTipDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_layout);
        ButterKnife.bind(this);

        mUser = BmobUser.getCurrentUser(User.class);

        initViews();
        initData();
    }

    private void initViews(){
        loadingDialog = new KTipDialog.Builder(getContext())
                .setIconType(KTipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在刷新")
                .create();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mMessagePresenter = new MessagePresenter();
        mMessagePresenter.attachView(this);

        mList = new ArrayList<>();
        mAdapter = new MessageAdapter(mList, getContext());
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mMessagePresenter.getData();
            }
        }, recyclerView);
        mAdapter.setEmptyView(new EmptyView(this));
    }

    private void initData(){
        mMessagePresenter.getData();
    }

    @Override
    public void toResult(List<Comment> list) {
        if(list == null || list.size() == 0){
            mAdapter.loadMoreEnd();
        } else {
            mList.addAll(list);
            mAdapter.notifyDataSetChanged();
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void onError(BmobException be) {
        mAdapter.loadMoreFail();
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
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
    public String getUserId() {
        return mUser != null ? mUser.getObjectId() : null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMessagePresenter.detachView();
    }
}
