package cc.meiwen.api;

import java.util.List;

import cc.meiwen.constant.Constant;
import cc.meiwen.http.HtmlHttpUtils;
import cc.meiwen.http.HttpUtils;
import cc.meiwen.http.OkBasicParamsInterceptor;
import cc.meiwen.model.FriedsTimelineBO;
import cc.meiwen.model.HtmlMwBO;
import cc.meiwen.util.SelfInfoUtils;
import rx.Observable;

/**
 * Created by abc on 2017/11/22.
 */

public class BaseApi {

    String friends_timeline = "statuses/friends_timeline.json";

    /**
     * 用户时间线
     * */
    public Observable<FriedsTimelineBO> friedsTimeline(int page){
        OkBasicParamsInterceptor.Builder mBuilder = SelfInfoUtils.getDefaultParams(Constant.URL.url+friends_timeline);
        mBuilder.addQueryParam("page", String.valueOf(page));
        return HttpUtils.createObservable(mBuilder, FriedsTimelineBO.class);
    }

    String html_url = "http://www.59xihuan.cn/index_";
    /**
     * 网页列表
     * */
    public Observable<List<HtmlMwBO>> mwHtml(int page){
        OkBasicParamsInterceptor.Builder mBuilder = SelfInfoUtils.getDefaultParams(html_url+page+".html");
        return HtmlHttpUtils.createObservableHtml(mBuilder);
    }
}
