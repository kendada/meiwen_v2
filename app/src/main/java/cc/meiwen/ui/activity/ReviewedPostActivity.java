package cc.meiwen.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.koudai.kbase.widget.dialog.KTipDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.meiwen.R;
import cc.meiwen.model.Post;
import cc.meiwen.model.PostType;
import cc.meiwen.model.User;
import cc.meiwen.util.MnAppUtil;
import cc.meiwen.util.MnDateUtil;
import cc.meiwen.view.SelectableRoundedImageView;
import cc.meiwen.view.TitleBar;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by abc on 2017/11/17.
 * 管理员审核帖子
 */

public class ReviewedPostActivity extends BaseActivity {

    @BindView(R.id.title_view)
    TitleBar titleView;
    @BindView(R.id.type_icon)
    SelectableRoundedImageView typeIcon;
    @BindView(R.id.post_type_txt)
    TextView postTypeTxt;
    @BindView(R.id.post_content_txt)
    TextView postContentTxt;
    @BindView(R.id.content_img)
    ImageView contentImg;
    @BindView(R.id.time_txt)
    TextView timeTxt;
    @BindView(R.id.do_not_btn)
    TextView doNotBtn;
    @BindView(R.id.do_btn)
    TextView doBtn;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    private int ph;

    private int mPosition = 0;

    private List<Post> mList;

    private KTipDialog loadingDialog;
    private boolean isMore = true; // 是否还有更多

    private User mUser;

    private String tag = ReviewedPostActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviewed_post_layout);
        ButterKnife.bind(this);

        ph = MnAppUtil.getPhoneH(this) / 3;

        loadingDialog = new KTipDialog.Builder(getContext())
                .setIconType(KTipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在刷新")
                .create();

        getPostData();
    }


    private void setData(Post post) {
        if (post != null) {
            postContentTxt.setText(post.getContent());

            timeTxt.setText(MnDateUtil.stringByFormat(post.getCreatedAt(), "MM月dd日 HH:mm"));

            BmobFile bmobFile = post.getConImg();
            if (bmobFile != null) {
                contentImg.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ph);
                contentImg.setLayoutParams(llp);
                Glide.with(this).load(bmobFile.getFileUrl()).asBitmap().into(contentImg);
            } else {
                contentImg.setVisibility(View.GONE);
            }

            //分类
            PostType postType = post.getPostType();
            if (postType != null) {
                postTypeTxt.setText(postType.getType());
                typeIcon.setVisibility(View.VISIBLE);
                Glide.with(this).load(postType.getIconUrl()).asBitmap().into(typeIcon);
            }
        }
    }

    @OnClick({R.id.do_not_btn, R.id.do_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.do_not_btn:
                getData(false);
                break;
            case R.id.do_btn:
                getData(true);
                break;
        }
    }

    private void updatePost(Post post, boolean isShow) {
        post.setIsShow(isShow);
        post.setReviewed(true);
        post.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                Log.d(tag, "e = " + e);
            }
        });
    }

    private void getData(boolean isShow) {
        mUser = BmobUser.getCurrentUser(User.class);
        if(mUser == null || !mUser.isAdmin()) {
            Toast.makeText(getContext(), "你还不是管理员，没有权限审核美文！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mList.size() > mPosition) {
            Post post = mList.get(mPosition);
            setData(post);
            mPosition++;
            updatePost(post, isShow);
        } else {
            if (isMore) {
                getPostData();
            } else {
                showToast();
            }
        }
    }

    private int getCount() {
        return mList != null ? mList.size() : 0;
    }

    private void getPostData() {
        loadingDialog.show();

        BmobQuery<Post> query = new BmobQuery<>();
        query.order("-createdAt");
        query.include("user,postType");
        query.addWhereEqualTo("isShow", false);
        query.addWhereEqualTo("isReviewed", false);
        query.setSkip(getCount());
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                loadingDialog.dismiss();
                if (e == null) {
                    if (mList == null) {
                        mList = new ArrayList<>();
                    }
                    if (list != null && list.size() > 0) {
                        mList.addAll(list);
                        if (mList.size() > mPosition) {
                            Post post = mList.get(mPosition);
                            setData(post);
                        }
                        isMore = true;
                    } else {
                        isMore = false;
                        showToast();
                    }
                }
                if(mList != null && mList.size() == 0){
                    scrollView.setVisibility(View.GONE);
                } else {
                    scrollView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void showToast() {
        Toast.makeText(getContext(), "辛苦了，目前没有更多了！", Toast.LENGTH_SHORT).show();
    }
}
