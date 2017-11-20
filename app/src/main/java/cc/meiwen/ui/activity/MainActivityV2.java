package cc.meiwen.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import cc.meiwen.R;
import cc.meiwen.constant.Constant;
import cc.meiwen.model.calendarSign;
import cc.meiwen.ui.fragment.FindFragment;
import cc.meiwen.ui.fragment.MainFragment;
import cc.meiwen.ui.fragment.PostTypeFragment;
import cc.meiwen.ui.fragment.TipsDialog;
import cc.meiwen.ui.fragment.UserInfoFragment;
import cc.meiwen.util.SharedPreferencesUtils;
import cc.meiwen.view.tab.MnTabGroupLayout;
import cc.meiwen.view.tab.MnTabLayout;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-30
 * Time: 11:05
 * Version 1.0
 */

public class MainActivityV2 extends BaseActivity implements MnTabGroupLayout.OnItemClickListener{

    private MnTabGroupLayout group_tab_layout;
    private ViewPager view_pager;

    private List<Fragment> list = new ArrayList<>();

    private int mPosition;

    private String tag = MainActivityV2.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_layout);

        MainFragment mainFragment = new MainFragment(); //首页
        mainFragment.setUserVisibleHint(true);
        list.add(mainFragment);

        FindFragment findFragment = new FindFragment(); // 发现
        findFragment.setUserVisibleHint(true);
        list.add(findFragment);

        PostTypeFragment postTypeFragment = new PostTypeFragment(); //分类
        postTypeFragment.setUserVisibleHint(false);
        list.add(postTypeFragment);
        UserInfoFragment userInfoFragment = new UserInfoFragment();
        list.add(userInfoFragment);

        view_pager = (ViewPager)findViewById(R.id.view_pager);
        view_pager.setOffscreenPageLimit(4);
        group_tab_layout = (MnTabGroupLayout)findViewById(R.id.group_tab_layout);
        group_tab_layout.setOnItemClickListener(this);

        view_pager.setAdapter(pagerAdapter);

        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                group_tab_layout.onPageScrolling(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                group_tab_layout.setCurrentItem(mPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) { //状态
                if (state == 0) { //滑动接受后，设置可见
                    list.get(mPosition).setUserVisibleHint(true);
                }
            }
        });
        getSign();
    }


    //创建适配器
    private FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    };

    @Override
    public void onClick(int position, MnTabLayout tabLayout) {
        view_pager.setCurrentItem(position, false); //取消动画
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    private void getSign(){
        BmobQuery<calendarSign> query = new BmobQuery<>();
        query.addWhereEqualTo("isShow", true);
        query.order("-createdAt");
        query.setLimit(1);
        query.findObjects(new FindListener<calendarSign>() {
            @Override
            public void done(List<calendarSign> list, BmobException e) {
                Log.d(tag, "e = " + e);
                if(list == null || list.size() == 0) return;
                onShow(list.get(0));
            }
        });
    }

    private void onShow(calendarSign sign){
        if(sign == null) return;
        String objectId = SharedPreferencesUtils.getString(Constant.ShareKey.OBJECT_ID);
        if(objectId.equals(sign.getObjectId())){
            return;
        }
        TipsDialog mTipsDialog = new TipsDialog();
        mTipsDialog.setCalendarSign(sign);
        mTipsDialog.show(getSupportFragmentManager());
    }

}
