package cc.meiwen.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.koudai.kbase.widget.dialog.KTipDialog;

import java.util.List;

import cc.meiwen.R;
import cc.meiwen.adapter.PostTypeActivityAdapter;
import cc.meiwen.model.Post;
import cc.meiwen.model.PostType;
import cc.meiwen.model.User;
import cc.meiwen.util.CopyUtil;
import cc.meiwen.view.TitleBar;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-30
 * Time: 13:44
 * Version 1.0
 */

public class PostTypeActivity extends BaseActivity {


    private ImageView mViewCover;
    private TextView mViewType;
    private TextView mViewTitle;

    public static void start(Context context, User user) {
        start(context, null, user);
    }

    public static void start(Context context, PostType postType) {
        start(context, postType, null);
    }

    public static void start(Context context, PostType postType, User user) {
        Intent intent = new Intent(context, PostTypeActivity.class);
        intent.putExtra("pt", postType);
        intent.putExtra("user", user);
        context.startActivity(intent);
    }

    private SwipeRefreshLayout refresh_layout;
    private ListView list_view;

    private ProgressBar progressBar;
    private TextView text;
    private TitleBar title_bar;

    private PostTypeActivityAdapter adapter;
    private List<Post> mList;

    private User bmobUser = null;

    private PostType postType;

    private int limit = 20; //每页20条数据
    private int page = 1; //页数
    private boolean isLoading = false; //是否正在加载
    private boolean isFinish = true; //是否加载完成

    private KTipDialog loadingDialog;

    private String tag = PostTypeActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_type_layout);
        getIntentData();

        initViews();
        initData();
        getPostData();

        if (postType != null) {
            title_bar.setTitleText(postType.getType());
        } else if (bmobUser != null) {
            title_bar.setTitleText(bmobUser.getUsername() + "的美文");
        }
    }

    private void getIntentData() {
        try {
            Intent intent = getIntent();
            postType = (PostType) intent.getSerializableExtra("pt");
            bmobUser = (User) intent.getSerializableExtra("user");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initViews() {
        loadingDialog = new KTipDialog.Builder(getContext())
                .setIconType(KTipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在刷新")
                .create();

        refresh_layout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refresh_layout.setColorSchemeResources(R.color.darkPrimaryColor, R.color.primaryColor, R.color.lightPrimaryColor);
        list_view = (ListView) findViewById(R.id.list_view);
        title_bar = (TitleBar) findViewById(R.id.title_bar);

        View footerView = LayoutInflater.from(getContext()).inflate(R.layout.footer_view, null);
        progressBar = (ProgressBar) footerView.findViewById(R.id.progressBar);
        text = (TextView) footerView.findViewById(R.id.text);

        list_view.addFooterView(footerView);
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLoading) {
                    loadMoreData();
                }
            }
        });

        if(postType != null){
            addHeadView();
        }
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
                if(i == 0){
                    return;
                }
                if(i>0){
                    i = i- 1;
                }
                PostCommentActivity.start(getContext(), mList.get(i));
            }
        });

        //长按复制
        list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    return true;
                }
                if(i>0){
                    i = i- 1;
                }
                new CopyUtil(getContext()).copy(mList.get(i).getContent());
                return true;
            }
        });
    }

    private void addHeadView() {
        View mHeadView = LayoutInflater.from(this).inflate(R.layout.header_post_type_layout, null);
        initHeadView(mHeadView);
        list_view.addHeaderView(mHeadView);
    }

    private void initHeadView(View itemView) {
        mViewCover = (ImageView) itemView.findViewById(R.id.cover_view);
        mViewType = (TextView) itemView.findViewById(R.id.type_view);
        mViewTitle = (TextView) itemView.findViewById(R.id.title_view);

        if(postType != null){
            mViewType.setText(postType.getType());
            mViewTitle.setText(postType.getTitle());
            Glide.with(this).load(postType.getIconUrl()).asBitmap().into(mViewCover);
        }
    }

    /**
     * 获取已经发布帖子:刷新
     */
    private void refreshData() {
        BmobQuery<Post> query = createPostBmobQuery();
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null) {
                    mList = list;
                    adapter = new PostTypeActivityAdapter(getContext(), mList);
                    list_view.setAdapter(adapter);
                }

                refresh_layout.setRefreshing(false);
            }
        });
    }

    private BmobQuery<Post> createPostBmobQuery() {
        BmobQuery<Post> query = new BmobQuery<>();
        query.order("-createdAt");
        if (postType != null) {
            query.addWhereEqualTo("postType", postType);
        }
        if (bmobUser != null) {
            query.addWhereEqualTo("user", bmobUser);
        }
        query.include("user,postType");
        query.addWhereEqualTo("isShow", true);
        query.setLimit(limit);
        return query;
    }


    /**
     * 获取已经发布帖子
     */
    private void getPostData() {
        loadingDialog.show();

        BmobQuery<Post> query = createPostBmobQuery();
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null) {
                    if (mList != null && adapter != null) {
                        mList.addAll(list);
                        adapter.notifyDataSetChanged();
                    } else {
                        mList = list;
                        adapter = new PostTypeActivityAdapter(getContext(), mList);
                        list_view.setAdapter(adapter);
                    }
                }

                loadingDialog.dismiss();
            }
        });
    }

    private void loadMoreData() {
        if (!isFinish) return;
        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);
        text.setText("正在加载");

        BmobQuery<Post> query = createPostBmobQuery();
        query.setSkip(limit * page); // 忽略前20*page条数据（即第一页数据结果）
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e != null) {
                    isFinish = true;
                } else {
                    if (list != null && list.size() > 0) {
                        mList.addAll(list);
                        adapter.notifyDataSetChanged();
                        page++;
                        isFinish = true;
                    } else {
                        isFinish = false;
                    }
                }
                isLoading = false;
                progressBar.setVisibility(View.INVISIBLE);
                if (isFinish) {
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
        if (adapter != null) {
            adapter.refreshData();
        }
    }

}
