package cc.meiwen.ui.presenter;

import java.util.List;

import cc.meiwen.model.Artcile;
import cc.meiwen.model.RecommendPost;
import cc.meiwen.model.calendarSign;

/**
 * Created by abc on 2017/11/2.
 */

public interface JuziListMvpView extends MvpView{

    void toResult(List<Artcile> list);

    void toResultAd(List<RecommendPost> list);

    void toResultPostType(List<calendarSign> list);

    int getCount();


}
