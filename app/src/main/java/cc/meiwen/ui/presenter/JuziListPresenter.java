package cc.meiwen.ui.presenter;

import android.util.Log;


import java.util.List;

import cc.meiwen.model.Artcile;
import cc.meiwen.model.Post;
import cc.meiwen.model.PostType;
import cc.meiwen.model.RecommendPost;
import cn.bmob.v3.BmobQuery;
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
        mBmobQuery.findObjects(mvpView.getContext(), new FindListener<Artcile>() {
            @Override
            public void onSuccess(List<Artcile> list) {
                Log.d(TAG, "posts = " + list);
                if(mvpView != null){
                    mvpView.toResult(list);
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "s = " + s);
            }
        });
    }

    public void getRecommendPost(){
        BmobQuery<RecommendPost> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(mvpView.getContext(), new FindListener<RecommendPost>() {
            @Override
            public void onSuccess(List<RecommendPost> list) {
                if(mvpView != null){
                    mvpView.toResultAd(list);
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "s = " + s);
            }
        });
    }

    public void getPostType(){
        BmobQuery<PostType> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(mvpView.getContext(), new FindListener<PostType>() {
            @Override
            public void onSuccess(List<PostType> list) {
                if(mvpView != null){
                    mvpView.toResultPostType(list);
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "s = " + s);
            }
        });
    }

    @Override
    public void detachView() {
        mvpView = null;
    }
}
