package cc.meiwen.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.koudai.kbase.widget.dialog.KTipDialog;

import java.util.List;

import cc.meiwen.R;
import cc.meiwen.adapter.MainFragmentAdapter;
import cc.meiwen.model.Datas;
import cc.meiwen.model.Post;
import cc.meiwen.model.User;
import cc.meiwen.ui.activity.PostCommentActivity;
import cc.meiwen.util.CopyUtil;
import cc.meiwen.util.task.AsyncTask;
import cc.meiwen.util.task.ThreadPoolManager;
import cc.meiwen.view.StateFrameLayout;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-30
 * Time: 13:44
 * Version 1.0
 */

public class MainFragment extends BaseFragment {

    private StateFrameLayout state_layout;
    private SwipeRefreshLayout refresh_layout;
    private ListView list_view;

    private ProgressBar progressBar;
    private TextView text;

    private MainFragmentAdapter adapter;
    private List<Post> mList;

    private User bmobUser = null;

    private int limit = 20; //每页20条数据
    private int page = 1;   //页数
    private boolean isLoading = false; //是否正在加载
    private boolean isFinish = true; //是否加载完成

    private List<Post> favoPosts;

    private ThreadPoolManager threadPoolManager;

    private KTipDialog loadingDialog;

    private boolean isVisibleToUI = true; //默认可见
    private boolean isFirstLaunched = false; //是否第一次加载

    private String tag = MainFragment.class.getSimpleName();

    public MainFragment(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loadingDialog = new KTipDialog.Builder(getActivity())
                .setIconType(KTipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在刷新")
                .create();
        View rootView = inflater.inflate(R.layout.fragment_main_layout, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        threadPoolManager = new ThreadPoolManager(ThreadPoolManager.TYPE_FIFO, 5);
        bmobUser = BmobUser.getCurrentUser(User.class);
        initData();
    }

    @Override
    public void onFirstTimeLaunched() {
        isFirstLaunched = true;
        if(isVisibleToUI){
            getDBFavo();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        isVisibleToUI = isVisibleToUser;
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUI && isFirstLaunched){
            if(mList==null || mList.size()<=0){
                getDBFavo();
            }
        }
    }

    //恢复状态
    @Override
    public void onRestoreState(Bundle saveInstanceState) {
        page = saveInstanceState.getInt("page");
        Datas datas = saveInstanceState.getParcelable("data");
        if(datas!=null && datas.getList()!=null){
            mList = datas.getList();
            adapter = new MainFragmentAdapter(getContext(), mList);
            list_view.setAdapter(adapter);
        } else {
            if(isVisibleToUI){
                getDBFavo();
            }
        }
    }

    //保存状态
    @Override
    public void onSaveState(Bundle outState) {
        outState.putInt("page", page); //页数
        outState.putParcelable("data", new Datas(mList));
    }

    @Override
    public void initViews(View view) {
        state_layout = (StateFrameLayout)view.findViewById(R.id.state_layout);
        refresh_layout = (SwipeRefreshLayout)view.findViewById(R.id.refresh_layout);
        refresh_layout.setColorSchemeResources(R.color.darkPrimaryColor, R.color.primaryColor, R.color.lightPrimaryColor);
        list_view = (ListView)view.findViewById(R.id.list_view);

        View footerView = LayoutInflater.from(getActivity()).inflate(R.layout.footer_view, null);
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

    @Override
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
                PostCommentActivity.start(getContext(), mList.get(i));
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
     * 获取本地缓存已经收藏的帖子：只有Id
     * */
    private void getDBFavo(){
        AsyncTask task = new AsyncTask() {
            @Override
            public void updata(Object obj) {
            //    getPostData();
                //获取本地数据库缓存
                getDBCachePost();
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
     * 获取本地缓存的Post
     * */
    private void getDBCachePost(){
        if(mList != null && mList.size() > 0){
            mList = getNewList(mList, favoPosts);
            adapter = new MainFragmentAdapter(getContext(), mList);
            list_view.setAdapter(adapter);
        } else {
            getPostData();
        }
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
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                mList = getNewList(list, favoPosts);
                adapter = new MainFragmentAdapter(getContext(), mList);
                list_view.setAdapter(adapter);

                refresh_layout.setRefreshing(false);
            }
        });
    }


    /**
     * 获取已经发布帖子
     * */
    private void getPostData(){
        loadingDialog.show();
        BmobQuery<Post> query = new BmobQuery<>();
        query.order("-createdAt");
        query.include("user,postType");
        query.addWhereEqualTo("isShow", true);
        query.setLimit(limit);
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if(e == null){
                    if(mList!=null && adapter!=null){
                        mList.addAll(getNewList(list, favoPosts));
                        adapter.notifyDataSetChanged();
                    } else {
                        mList = getNewList(list, favoPosts);
                        adapter = new MainFragmentAdapter(getContext(), mList);
                        list_view.setAdapter(adapter);
                    }
                } else {
                    state_layout.setViewState(StateFrameLayout.VIEW_STATE_ERROR); //加载错误
                }

                state_layout.setViewState(StateFrameLayout.VIEW_STATE_CONTENT);
                loadingDialog.dismiss();
            }
        });
    }

    private void loadMoreData(){
        if(!isFinish) return;
        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);
        text.setText("正在加载");
        BmobQuery<Post> query = new BmobQuery<>();
        query.order("-createdAt");
        query.include("user,postType");
        query.addWhereEqualTo("isShow", true); //只显示通过审核的
        query.setLimit(limit);
        query.setSkip(limit * page); // 忽略前20*page条数据（即第一页数据结果）
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if(e == null){
                    if(list!=null && list.size()>0){
                        mList.addAll(getNewList(list, favoPosts));
                        adapter.notifyDataSetChanged();
                        page++;
                        isFinish = true;
                    } else {
                        isFinish = false;
                    }
                } else {
                    isFinish = true;
                }

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
