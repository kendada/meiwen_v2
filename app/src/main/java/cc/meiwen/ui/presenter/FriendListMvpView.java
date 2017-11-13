package cc.meiwen.ui.presenter;

import java.util.List;

import cc.meiwen.model.User;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by abc on 2017/11/13.
 */

public interface FriendListMvpView extends MvpView{

    void toResult(List<User> list);

    int count();

    void onError(BmobException be);

    void showDialog();

    void dismissDialog();
}
