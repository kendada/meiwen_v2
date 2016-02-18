package cc.meiwen.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import cc.meiwen.R;
import cc.meiwen.ui.fragment.FavoFragment;
import cc.meiwen.ui.fragment.MainFragment;
import cc.meiwen.ui.fragment.MeFragment;
import cc.meiwen.ui.fragment.PostTypeFragment;
import cc.meiwen.ui.fragment.UserInfoFragment;
import cc.meiwen.view.tab.MnTabGroupLayout;
import cc.meiwen.view.tab.MnTabLayout;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-30
 * Time: 11:05
 * Version 1.0
 */

public class Main2Activity extends BaseActivity implements MnTabGroupLayout.OnItemClickListener{

    private MnTabGroupLayout group_tab_layout;
    private ViewPager view_pager;
    private Toolbar toolbar;

    private List<Fragment> list = new ArrayList<>();

    private int mPosition;

    private String tag = Main2Activity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_layout);

        MainFragment mainFragment = new MainFragment(); //首页
        mainFragment.setUserVisibleHint(true);
        list.add(mainFragment);
        PostTypeFragment postTypeFragment = new PostTypeFragment(); //分类
        postTypeFragment.setUserVisibleHint(false);
        list.add(postTypeFragment);
        UserInfoFragment userInfoFragment = new UserInfoFragment();
        list.add(userInfoFragment);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        view_pager = (ViewPager)findViewById(R.id.view_pager);
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

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_post:
                        Intent it = new Intent(getContext(), SavePostActivity.class);
                        startActivity(it);
                        break;
                }
                return true;
            }
        });

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

}
