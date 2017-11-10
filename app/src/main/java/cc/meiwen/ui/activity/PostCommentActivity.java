package cc.meiwen.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cc.meiwen.R;
import cc.meiwen.adapter.PostCommentAdapter;
import cc.meiwen.model.Comment;
import cc.meiwen.model.Post;
import cc.meiwen.model.PostType;
import cc.meiwen.model.User;
import cc.meiwen.util.CopyUtil;
import cc.meiwen.util.ImageConfigBuilder;
import cc.meiwen.util.MnAppUtil;
import cc.meiwen.util.MnDateUtil;
import cc.meiwen.view.MnProgressBar;
import cc.meiwen.view.SelectableRoundedImageView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-05
 * Time: 10:15
 * Version 1.0
 */

public class PostCommentActivity extends BaseActivity{

    private Toolbar toolbar;
    private SwipeRefreshLayout refresh_layout;
    private ListView list_view;
    private EditText edit_comment;
    private Button comment_btn;

    private View headView;
    private TextView post_type_txt, post_content_txt, time_txt;
    private SelectableRoundedImageView type_icon;
    private ImageView content_img;

    private View footerView;
    private MnProgressBar progressBar;
    private TextView text;

    private List<Comment> mList;
    private PostCommentAdapter adapter;

    public boolean isLoadImg = true;

    private int ph;

    private String url = "http://file.bmob.cn/";

    private Post post;

    private User user;

    private int limit = 10; //每页10条数据
    private int page = 1; //页数
    private boolean isLoading = false; //是否正在加载
    private boolean isFinish = true; //是否加载完成

    private String tag = PostCommentActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comment_layout);

        user = BmobUser.getCurrentUser(this, User.class);

        isLoadImg = getAppSettingSharedPre().getBoolean(APP_SETTING_ISLOADIMG, true);

        ph = MnAppUtil.getPhoneH(this)/3;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("帖子正文");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getIntentData();
        initViews();
        initData();


        getComment();
    }

    private void initViews(){
        refresh_layout = (SwipeRefreshLayout)findViewById(R.id.refresh_layout);
        refresh_layout.setColorSchemeResources(R.color.darkPrimaryColor, R.color.primaryColor, R.color.lightPrimaryColor);
        list_view = (ListView)findViewById(R.id.list_view);
        comment_btn = (Button)findViewById(R.id.comment_btn);
        edit_comment = (EditText)findViewById(R.id.edit_comment);

        addListViewHeadView();

        addListViewFooterView();
    }

    private void postComment(){
        String cc = edit_comment.getText().toString();
        if(!TextUtils.isEmpty(cc)){
            final Comment comment = new Comment(cc ,user, post, post.getObjectId());
            comment.save(this, new SaveListener() {
                @Override
                public void onSuccess() {
                    Log.i(tag, "----onSuccess()----");
                    edit_comment.setText(""); //清除评论内容
                    mList.add(0, comment);
                    adapter.refreshData();
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.i(tag, "----onFailure()----"+s);
                }
            });
        } else {
            Toast.makeText(this, "请输入评论内容", Toast.LENGTH_SHORT).show();
        }
    }

    private void addListViewHeadView(){
        headView = LayoutInflater.from(getContext()).inflate(R.layout.hearder_post_comment_layout, null);

        post_type_txt = (TextView)headView.findViewById(R.id.post_type_txt);
        post_content_txt = (TextView)headView.findViewById(R.id.post_content_txt);
        time_txt = (TextView)headView.findViewById(R.id.time_txt);
        type_icon = (SelectableRoundedImageView)headView.findViewById(R.id.type_icon);
        content_img = (ImageView)headView.findViewById(R.id.content_img);

        list_view.addHeaderView(headView);

    }

    private void addListViewFooterView(){
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.footer_view, null);

        progressBar = (MnProgressBar)footerView.findViewById(R.id.progressBar);
        text = (TextView)footerView.findViewById(R.id.text);
        //万普广告
        LinearLayout adlayout =(LinearLayout)footerView.findViewById(R.id.AdLinearLayout);

        list_view.addFooterView(footerView);


    }

    private void initData(){
        if(post!=null){
            post_content_txt.setText(post.getContent());

            time_txt.setText(MnDateUtil.stringByFormat(post.getCreatedAt(), "MM月dd日 HH:mm"));

            if(isLoadImg){
                //获取帖子图片
                BmobFile bmobFile = post.getConImg();
                if(bmobFile!=null){
                    content_img.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ph);
                    content_img.setLayoutParams(llp);
                    ImageLoader.getInstance().displayImage(url+bmobFile.getUrl(), content_img, ImageConfigBuilder.USER_HEAD_HD_OPTIONS);
                } else {
                    content_img.setVisibility(View.GONE);
                }
            } else {
                content_img.setVisibility(View.GONE);
            }

            //分类
            PostType postType = post.getPostType();
            if (postType!=null){
                post_type_txt.setText(postType.getType());
                if(isLoadImg){
                    type_icon.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(postType.getIconUrl(), type_icon, ImageConfigBuilder.USER_HEAD_HD_OPTIONS);
                } else {
                    type_icon.setVisibility(View.GONE);
                }
            }
        }

        //评论
        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postComment();
            }
        });

        //刷新
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getComment();
            }
        });

        //加载更多
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLoading = true;
                loadMoreComment();
            }
        });

        //长按复制评论
        list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){
                    Comment comment = mList.get(i-1);
                    new CopyUtil(getContext()).copy(comment.getContent()); //复制
                }
                return true;
            }
        });
    }

    /**
     * 获取帖子的评论
     * */
    private void getComment(){
        BmobQuery<Comment> query = new BmobQuery<>();
        query.order("-createdAt");
        query.include("user");
        query.setLimit(limit);
        query.addWhereEqualTo("postId", post.getObjectId());
        query.findObjects(getContext(), new FindListener<Comment>() {
            @Override
            public void onStart() {
                loadingDialog.setText("正在获取数据");
                dialog.show();
                loadingDialog.startAnim();
            }

            @Override
            public void onSuccess(List<Comment> list) {
                mList = list;
                adapter = new PostCommentAdapter(getContext(), mList);
                list_view.setAdapter(adapter);
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onFinish() {
                dialog.dismiss();
                refresh_layout.setRefreshing(false);
            }
        });
    }

    /**
     * 获取帖子的评论:加载更多
     * */
    private void loadMoreComment(){
        if(!isFinish) return;
        BmobQuery<Comment> query = new BmobQuery<>();
        query.order("-createdAt");
        query.include("user");
        query.addWhereEqualTo("postId", post.getObjectId());
        query.setLimit(limit);
        query.setSkip(limit * page); // 忽略前10*page条数据（即第一页数据结果）
        query.findObjects(getContext(), new FindListener<Comment>() {
            @Override
            public void onStart() {
                text.setText("正在加载更多");
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(List<Comment> list) {
                if(list!=null && list.size()>0){
                    page++;
                    mList.addAll(list);
                    adapter.refreshData();
                    isFinish = true;
                } else {
                    isFinish = false;
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.i(tag, "---onError()----"+s);
            }

            @Override
            public void onFinish() {
                progressBar.setVisibility(View.GONE);
                if(isFinish){
                    text.setText("加载更多");
                } else {
                    text.setText("没有更多了");
                }
            }
        });
    }

    private void getIntentData(){
        try{
            Intent intent = getIntent();
            post = (Post)intent.getSerializableExtra("post");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
