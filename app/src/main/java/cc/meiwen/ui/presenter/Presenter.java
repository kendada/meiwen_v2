package cc.meiwen.ui.presenter;

/**
 * Created by abc on 2017/11/2.
 */

public interface Presenter<V extends MvpView> {

    void attachView(V view);

    void detachView();

}
