package cc.meiwen.ui.presenter;

import android.text.TextUtils;

import java.util.List;

import cc.meiwen.model.Comment;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by abc on 2017/11/13.
 */

public class MessagePresenter implements Presenter<MessageMvpView>{

    private MessageMvpView mvpView;

    public void getData(){
        if(mvpView.getCount() <= 0){
            mvpView.showDialog();
        }
        BmobQuery<Comment> query = new BmobQuery<>();
        query.include("user");
        query.order("-createdAt");
        query.setLimit(20);
        query.setSkip(mvpView.getCount());
        if(!TextUtils.isEmpty(mvpView.getUserId())){
            query.addWhereEqualTo("user", mvpView.getUserId());
        }
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if(mvpView.getCount() <= 0){
                    mvpView.dismissDialog();
                }
                if(e == null){
                    if(mvpView != null){
                        mvpView.toResult(list);
                    }
                } else {
                    if(mvpView != null){
                        mvpView.onError(e);
                    }
                }
            }
        });
    }

    @Override
    public void attachView(MessageMvpView view) {
        mvpView = view;
    }

    @Override
    public void detachView() {
        mvpView = null;
    }
}
