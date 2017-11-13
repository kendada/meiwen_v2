package cc.meiwen.ui.presenter;

import java.util.List;

import cc.meiwen.model.User;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by abc on 2017/11/13.
 */

public class FriendListPresenter implements Presenter<FriendListMvpView>{

    private FriendListMvpView mvpView;

    @Override
    public void attachView(FriendListMvpView view) {
        mvpView = view;
    }

    public void getUserList(){
        if(mvpView.count() <= 0){
            mvpView.showDialog();
        }
        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        userBmobQuery.addWhereEqualTo("isAdmin", true);
        userBmobQuery.setLimit(20);
        userBmobQuery.setSkip(mvpView.count());
        userBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(mvpView.count() <= 0){
                    mvpView.dismissDialog();
                }
                if(e == null){
                    if(mvpView != null){
                        mvpView.toResult(list);
                    }
                } else {
                    if (mvpView != null){
                        mvpView.onError(e);
                    }
                }
            }
        });
    }

    @Override
    public void detachView() {
        mvpView = null;
    }
}
