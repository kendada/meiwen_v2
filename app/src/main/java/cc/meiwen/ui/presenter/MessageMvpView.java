package cc.meiwen.ui.presenter;

import java.util.List;

import cc.meiwen.model.Comment;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by abc on 2017/11/13.
 */

public interface MessageMvpView extends MvpView{

    void toResult(List<Comment> list);

    void onError(BmobException be);

    int getCount();

    void showDialog();

    void dismissDialog();

    String getUserId();

}
