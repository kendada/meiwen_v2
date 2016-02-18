package cc.meiwen.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import cc.emm.AppConnect;
import cc.meiwen.R;
import cc.meiwen.adapter.MainFragmentAdapter;
import cc.meiwen.model.Post;
import cc.meiwen.model.PostType;
import cc.meiwen.model.User;
import cc.meiwen.util.CopyUtil;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-30
 * Time: 13:44
 * Version 1.0
 */

public class PostTypeActivity extends BaseActivity {

    private SwipeRefreshLayout refresh_layout;
    private ListView list_view;
    private Toolbar toolbar;

    private ProgressBar progressBar;
    private TextView text;

    private MainFragmentAdapter adapter;
    private List<Post> mList;

    private User bmobUser = null;

    private PostType postType;

    private int limit = 20; //每页20条数据
    private int page = 1; //页数
    private boolean isLoading = false; //是否正在加载
    private boolean isFinish = true; //是否加载完成

    private String tag = PostTypeActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_type_layout);
        getIntentData();
        bmobUser = BmobUser.getCurrentUser(getContext(), User.class);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        if(postType!=null){
            toolbar.setTitle(postType.getType());
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        initData();
        getPostData();

    }

    private void getIntentData(){
        try{
            Intent intent = getIntent();
            postType = (PostType) intent.getSerializableExtra("pt");
            toolbar.setSubtitle(postType.getTitle());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void initViews() {
        refresh_layout = (SwipeRefreshLayout)findViewById(R.id.refresh_layout);
        refresh_layout.setColorSchemeResources(R.color.darkPrimaryColor, R.color.primaryColor, R.color.lightPrimaryColor);
        list_view = (ListView)findViewById(R.id.list_view);

        View footerView = LayoutInflater.from(getContext()).inflate(R.layout.footer_view, null);
        progressBar = (ProgressBar)footerView.findViewById(R.id.progressBar);
        text = (TextView)footerView.findViewById(R.id.text);
        //万普广告
        LinearLayout adlayout =(LinearLayout)footerView.findViewById(R.id.AdLinearLayout);
        AppConnect.getInstance(getContext()).showBannerAd(getContext(), adlayout);

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
                intent.putExtra("post", mList.get(i));
                startActivity(intent);
            }
        });

        //长按复制
        list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                new CopyUtil(getContext()).copy(mList.get(i).getContent());
                return true;
            }
        });
    }

    /**
     * 获取已经发布帖子:刷新
     * */
    private void refreshData(){
        BmobQuery<Post> query = new BmobQuery<>();
        query.order("-createdAt");
        query.addWhereEqualTo("postType", postType);
        query.include("user,postType");
        query.setLimit(limit);
        query.findObjects(getContext(), new FindListener<Post>() {
            @Override
            public void onSuccess(List<Post> list) {
                mList = list;
                adapter = new MainFragmentAdapter(getContext(), mList);
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
        query.addWhereEqualTo("postType", postType);
        query.include("user,postType");
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
                    adapter = new MainFragmentAdapter(getContext(), mList);
                    list_view.setAdapter(adapter);
                }

            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onFinish() {
                dialog.dismiss();
            }
        });
    }

    private void loadMoreData(){
        if(!isFinish) return;
        BmobQuery<Post> query = new BmobQuery<>();
        query.order("-createdAt");
        query.addWhereEqualTo("postType", postType);
        query.include("user,postType");
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
        if(adapter!=null){
            adapter.refreshData();
        }
    }

}
