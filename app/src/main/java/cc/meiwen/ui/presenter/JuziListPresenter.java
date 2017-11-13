package cc.meiwen.ui.presenter;

import android.util.Log;


import java.util.List;

import cc.meiwen.model.Artcile;
import cc.meiwen.model.Post;
import cc.meiwen.model.PostType;
import cc.meiwen.model.RecommendPost;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by abc on 2017/11/2.
 */

public class JuziListPresenter implements Presenter<JuziListMvpView> {

    private String TAG = JuziListPresenter.class.getSimpleName();

    private JuziListMvpView mvpView;

    private BmobQuery<Artcile> mBmobQuery;

    @Override
    public void attachView(JuziListMvpView view) {
        mvpView = view;

        mBmobQuery = new BmobQuery<>();
        mBmobQuery.setLimit(20);
    }

    public void getData(){
        mBmobQuery.setSkip(mvpView.getCount());
        mBmobQuery.findObjects(new FindListener<Artcile>() {
            @Override
            public void done(List<Artcile> list, BmobException e) {
                if(e == null){
                    Log.d(TAG, "posts = " + list);
                    if(mvpView != null){
                        mvpView.toResult(list);
                    }
                } else {
                    Log.d(TAG, "e = " + e);
                }
            }
        });
    }

    public void getRecommendPost(){
        BmobQuery<RecommendPost> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<RecommendPost>() {
            @Override
            public void done(List<RecommendPost> list, BmobException e) {
                if(mvpView != null){
                    mvpView.toResultAd(list);
                }
                Log.d(TAG, "e = " + e);
            }
        });
    }

    public void getPostType(){
        BmobQuery<PostType> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<PostType>() {
            @Override
            public void done(List<PostType> list, BmobException e) {
                if(mvpView != null){
                    mvpView.toResultPostType(list);
                }
                Log.d(TAG, "e = " + e);
            }
        });
    }

    @Override
    public void detachView() {
        mvpView = null;
    }
}
