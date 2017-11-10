package cc.meiwen.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cc.meiwen.R;
import cc.meiwen.adapter.NoPassAdapter;
import cc.meiwen.model.Post;
import cc.meiwen.model.User;
import cc.meiwen.util.CopyUtil;
import cc.meiwen.util.task.AsyncTask;
import cc.meiwen.util.task.ThreadPoolManager;
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

public class NoPassActivity extends BaseActivity {

    private Toolbar toolbar;
    private StateFrameLayout state_layout;
    private SwipeRefreshLayout refresh_layout;
    private ListView list_view;

    private ProgressBar progressBar;
    private TextView text;

    private NoPassAdapter adapter;
    private List<Post> mList;

    private User bmobUser = null;

    private int limit = 20; //每页20条数据
    private int page = 1; //页数
    private boolean isLoading = false; //是否正在加载
    private boolean isFinish = true; //是否加载完成

    private List<Post> favoPosts;

    private ThreadPoolManager threadPoolManager;

    private String tag = NoPassActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_pass_layout);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();

        threadPoolManager = new ThreadPoolManager(ThreadPoolManager.TYPE_FIFO, 5);
        bmobUser = BmobUser.getCurrentUser(getContext(), User.class);
        initData();
        getDBFavo();
    }

    public void initViews() {
        state_layout = (StateFrameLayout)findViewById(R.id.state_layout);
        refresh_layout = (SwipeRefreshLayout)findViewById(R.id.refresh_layout);
        refresh_layout.setColorSchemeResources(R.color.darkPrimaryColor, R.color.primaryColor, R.color.lightPrimaryColor);
        list_view = (ListView)findViewById(R.id.list_view);

        View footerView = LayoutInflater.from(getContext()).inflate(R.layout.footer_view, null);
        progressBar = (ProgressBar)footerView.findViewById(R.id.progressBar);
        text = (TextView)footerView.findViewById(R.id.text);

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

        list_view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), "请登录享受更多服务！", Toast.LENGTH_SHORT).show();
            }
        });

        //长按复制
        list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Post post = mList.get(i);
                new CopyUtil(getContext()).copy(post.getContent());
                return true;
            }
        });
    }

    /**
     * 获取本地缓存已经收藏的帖子
     * */
    private void getDBFavo(){
        AsyncTask task = new AsyncTask() {
            @Override
            public void updata(Object obj) {
                getPostData();
            }

            @Override
            public Object loadData() {
                return null;
            }
        };
        threadPoolManager.addAsyncTask(task);
        threadPoolManager.start();
    }

    /**
     * 获取已经发布帖子:刷新
     * */
    private void refreshData(){
        BmobQuery<Post> query = new BmobQuery<>();
        query.order("-createdAt");
        query.include("user,postType");
        query.addWhereEqualTo("isShow", true);
        query.setLimit(limit);
        query.findObjects(getContext(), new FindListener<Post>() {
            @Override
            public void onSuccess(List<Post> list) {
                mList = list;
                adapter = new NoPassAdapter(getContext(), mList);
                list_view.setAdapter(adapter);

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
        BmobQuery<Post> query = new BmobQuery<>();
        query.order("-createdAt");
        query.include("user,postType");
        query.addWhereEqualTo("isShow", true);
        query.setLimit(limit);
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findObjects(getContext(), new FindListener<Post>() {
            @Override
            public void onStart() {
                loadingDialog.setText("正在获取数据");
                dialog.show();
                loadingDialog.startAnim();
            }

            @Override
            public void onSuccess(List<Post> list) {
                if(mList!=null && adapter!=null){
                    mList.addAll(list);
                    adapter.notifyDataSetChanged();
                } else {
                    mList = list;
                    adapter = new NoPassAdapter(getContext(), mList);
                    list_view.setAdapter(adapter);
                }

            }

            @Override
            public void onError(int i, String s) {
                state_layout.setViewState(StateFrameLayout.VIEW_STATE_ERROR); //加载错误
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
        BmobQuery<Post> query = new BmobQuery<>();
        query.order("-createdAt");
        query.include("user,postType");
        query.addWhereEqualTo("isShow", true); //只显示通过审核的
        query.setLimit(limit);
        query.setSkip(limit * page); // 忽略前20*page条数据（即第一页数据结果）
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findObjects(getContext(), new FindListener<Post>() {
            @Override
            public void onStart() {
                isLoading = true;
                progressBar.setVisibility(View.VISIBLE);
                text.setText("正在加载");
            }

            @Override
            public void onSuccess(List<Post> list) {
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
