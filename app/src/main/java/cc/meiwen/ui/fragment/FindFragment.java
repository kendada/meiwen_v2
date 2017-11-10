package cc.meiwen.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cc.meiwen.R;
import cc.meiwen.adapter.AdViewPager;
import cc.meiwen.adapter.FindAdAdapter;
import cc.meiwen.adapter.JuziListAdapter;
import cc.meiwen.model.Artcile;
import cc.meiwen.model.Post;
import cc.meiwen.model.PostType;
import cc.meiwen.model.RecommendPost;
import cc.meiwen.ui.activity.MostBeautifulActivity;
import cc.meiwen.ui.presenter.JuziListMvpView;
import cc.meiwen.ui.presenter.JuziListPresenter;
import cc.meiwen.view.FindSearchUserLiveLayout;

/**
 * Created by abc on 2017/11/8.
 */

public class FindFragment extends BaseFragment implements JuziListMvpView, View.OnClickListener{

    private List<Artcile> mList;

    private JuziListPresenter mJuziListPresenter;

    private RecyclerView mViewRecycler;
    private JuziListAdapter mAdapter;

    private AdViewPager mViewPagerAd;
    private TextView titleView;
    private FindSearchUserLiveLayout mSearchUserLiveLayoutFind;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_jz_juzi_list_layout, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initData();
    }

    public void initViews(View view){
        mViewRecycler = (RecyclerView) view.findViewById(R.id.recycler_view);
        mViewRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void initData(){
        mJuziListPresenter = new JuziListPresenter();
        mJuziListPresenter.attachView(this);

        mList = new ArrayList<>();
        mAdapter = new JuziListAdapter(mList, getActivity());
        mViewRecycler.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mJuziListPresenter.getData(); // 加载下一页数据
            }
        }, mViewRecycler);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(), MostBeautifulActivity.class);
                if(intent == null) return;
                intent.putExtra("art", mList.get(position));
                startActivity(intent);
            }
        });
        addHeadView();
        mJuziListPresenter.getRecommendPost();
        mJuziListPresenter.getPostType();
        mJuziListPresenter.getData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    @Override
    public void toResult(List<Artcile> list) {
        if(list == null || list.size() == 0){
            mAdapter.loadMoreEnd();
        } else {
            mList.addAll(list);
            mAdapter.notifyDataSetChanged();
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void toResultAd(final List<RecommendPost> list) {
        FindAdAdapter mAdAdapter = new FindAdAdapter(getContext(), list);
        mViewPagerAd.setAdapter(mAdAdapter);

        mViewPagerAd.setCurrentItem((list.size()) * 100);

        // 设置自动滚动的间隔时间，单位为毫秒
        mViewPagerAd.setInterval(2000);
        // 设置循环滚动时滑动到从边缘滚动到下一个是否需要动画，默认为true
        mViewPagerAd.setBorderAnimation(false);
        // 设置ViewPager滑动动画间隔时间的倍率，达到减慢动画或改变动画速度的效果
        mViewPagerAd.setAutoScrollDurationFactor(6);
        mViewPagerAd.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int index = position % list.size();
                titleView.setText((index + 1) + "/" + list.size() + "    " + list.get(index).title);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void toResultPostType(List<PostType> list) {
        mSearchUserLiveLayoutFind.setData(list);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    private void addHeadView() {
        View headView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_find_item_head_layout, null);

        mViewPagerAd = (AdViewPager) headView.findViewById(R.id.ad_view_pager);
        titleView = (TextView) headView.findViewById(R.id.title_view);
        mSearchUserLiveLayoutFind = (FindSearchUserLiveLayout) headView.findViewById(R.id.find_search_user_live_layout);

        mAdapter.addHeaderView(headView);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mViewPagerAd != null) {
            mViewPagerAd.startAutoScroll();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mViewPagerAd != null) {
            mViewPagerAd.stopAutoScroll();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mJuziListPresenter != null) {
            mJuziListPresenter.detachView();
        }
    }

}
