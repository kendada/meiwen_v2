package cc.meiwen.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import cc.meiwen.R;
import cc.meiwen.adapter.FavoFragmentAdapter;
import cc.meiwen.model.Datas;
import cc.meiwen.model.Favo;
import cc.meiwen.model.User;
import cc.meiwen.ui.fragment.BaseFragment;
import cc.meiwen.util.CopyUtil;
import cc.meiwen.view.LoadingDialog;
import cc.meiwen.view.StateFrameLayout;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-30
 * Time: 13:44
 * Version 1.0
 */

public class FavoActivity extends BaseActivity {

    private StateFrameLayout state_layout;
    private SwipeRefreshLayout refresh_layout;
    private ListView list_view;
    private Toolbar toolbar;

    private ProgressBar progressBar;
    private TextView text;

    private FavoFragmentAdapter adapter;
    private List<Favo> mList;

    private User bmobUser = null;

    private int limit = 20; //每页20条数据
    private int page = 1; //页数
    private boolean isLoading = false; //是否正在加载
    private boolean isFinish = true; //是否加载完成

    private LoadingDialog loadingDialog;
    private Dialog dialog;

    private boolean isVisibleToUI = true; //默认可见

    private String tag = FavoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadingDialog = new LoadingDialog(getContext());
        dialog = loadingDialog.createLoadingDialog("正在获取数据");

        bmobUser = BmobUser.getCurrentUser(getContext(), User.class);
        initViews();
        initData();
        getPostData();
    }

    public void initViews() {
        state_layout = (StateFrameLayout)findViewById(R.id.state_layout);
        refresh_layout = (SwipeRefreshLayout)findViewById(R.id.refresh_layout);
        refresh_layout.setColorSchemeResources(R.color.darkPrimaryColor, R.color.primaryColor, R.color.lightPrimaryColor);
        list_view = (ListView)findViewById(R.id.list_view);

        View footerView = LayoutInflater.from(getContext()).inflate(R.layout.footer_view, null);
        progressBar = (ProgressBar)footerView.findViewById(R.id.progressBar);
        text = (TextView)footerView.findViewById(R.id.text);
        //万普广告
        LinearLayout adlayout =(LinearLayout)footerView.findViewById(R.id.AdLinearLayout);

        list_view.addFooterView(footerView);
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLoading) {
                    loadMoreData();
                }
            }
        });

    }

    public void initData() {
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), PostCommentActivity.class);
                intent.putExtra("post", mList.get(i).getPost());
                startActivity(intent);
            }
        });

        list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                new CopyUtil(getContext()).copy(mList.get(i).getPost().getContent());
                return true;
            }
        });
    }

    /**
     * 获取已经发布帖子:刷新
     * */
    private void refreshData(){
        BmobQuery<Favo> query = new BmobQuery<>();
        query.addWhereEqualTo("user", bmobUser);
        query.order("-createdAt");
        query.include("user,post,post.postType,post.user");
        query.setLimit(limit);
        query.findObjects(getContext(), new FindListener<Favo>() {
            @Override
            public void onSuccess(List<Favo> list) {
                mList = list;

            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onFinish() {
                refresh_layout.setRefreshing(false);
            }
        });
    }


    /**
     * 获取已经发布帖子
     * */
    private void getPostData(){
        BmobQuery<Favo> query = new BmobQuery<>();
        query.addWhereEqualTo("user", bmobUser);
        query.order("-createdAt");
        query.include("user,post,post.postType,post.user");
        query.setLimit(limit);
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findObjects(getContext(), new FindListener<Favo>() {
            @Override
            public void onStart() {
                loadingDialog.setText("正在获取数据");
                dialog.show();
                loadingDialog.startAnim();
            }

            @Override
            public void onSuccess(List<Favo> list) {
                if(mList!=null && adapter!=null){
                    mList.addAll(list);
                    adapter.notifyDataSetChanged();
                } else {
                    mList = list;
                    adapter = new FavoFragmentAdapter(getContext(), mList);
                    list_view.setAdapter(adapter);
                }

            }

            @Override
            public void onError(int i, String s) {
                state_layout.setViewState(StateFrameLayout.VIEW_STATE_ERROR);
            }

            @Override
            public void onFinish() {
                dialog.dismiss();
                state_layout.setViewState(StateFrameLayout.VIEW_STATE_CONTENT);
            }
        });
    }

    private void loadMoreData(){
        if(!isFinish) return;
        BmobQuery<Favo> query = new BmobQuery<>();
        query.addWhereEqualTo("user", bmobUser);
        query.order("-createdAt");
        query.include("user,post,post.postType,post.user");
        query.setLimit(limit);
        query.setSkip(limit * page); // 忽略前20*page条数据（即第一页数据结果）
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findObjects(getContext(), new FindListener<Favo>() {
            @Override
            public void onStart() {
                isLoading = true;
                progressBar.setVisibility(View.VISIBLE);
                text.setText("正在加载");
            }

            @Override
            public void onSuccess(List<Favo> list) {
                if(list!=null && list.size()>0){
                    mList.addAll(list);
                    adapter.notifyDataSetChanged();
                    page++;
                    isFinish = true;
                } else {
                    isFinish = false;
                }
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onFinish() {
                isLoading = false;
                progressBar.setVisibility(View.INVISIBLE);
                if(isFinish){
                    text.setText("加载更多");
                } else {
                    text.setText("没有更多了");
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null){ //测试
            adapter.refreshData();
        }
    }
}
