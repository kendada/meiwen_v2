package cc.meiwen.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import cc.meiwen.R;
import cc.meiwen.model.Favo;
import cc.meiwen.model.PostType;
import cc.meiwen.model.User;
import cc.meiwen.ui.fragment.FavoFragment;
import cc.meiwen.ui.fragment.FindFragment;
import cc.meiwen.ui.fragment.MainFragment;
import cc.meiwen.ui.fragment.MeFragment;
import cc.meiwen.ui.fragment.MostArtcileFragment;
import cc.meiwen.ui.fragment.PostTypeFragment;
import cc.meiwen.util.task.AsyncTask;
import cc.meiwen.util.task.ThreadPoolManager;
import cc.meiwen.view.SelectableRoundedImageView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.update.BmobUpdateAgent;

public class MainActivity extends BaseActivity {

    private Toolbar toolbar;
    private DrawerLayout drawer_layout;
    private SelectableRoundedImageView user_icon;
    private NavigationView navigation_view;

    private ActionBarDrawerToggle drawerToggle;

    private int itemId = R.id.nav_home;
    private int tempId= R.id.nav_home;

    private ThreadPoolManager threadPoolManager;

    private PostTypeFragment postTypeFragment;
    private MainFragment mainFragment;
    private MeFragment meFragment;
    private FavoFragment favoFragment;
    private MostArtcileFragment mostArtcileFragment;
    private FindFragment mFindFragment;

    private String tag = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        postTypeFragment = new PostTypeFragment();
        mainFragment = new MainFragment();
        meFragment = new MeFragment();
        favoFragment = new FavoFragment();
        mostArtcileFragment = new MostArtcileFragment();
        mFindFragment = new FindFragment();

        //保存Post分类
        getPostType();

        threadPoolManager = new ThreadPoolManager(ThreadPoolManager.TYPE_FIFO, 5);

        toolbar = (Toolbar)findViewById(R.id.toolbar);

        drawer_layout = (DrawerLayout)findViewById(R.id.drawer_layout);
        user_icon = (SelectableRoundedImageView)findViewById(R.id.user_icon);
        navigation_view = (NavigationView)findViewById(R.id.navigation_view);

        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_post:
                        Intent it = new Intent(getContext(), SavePostActivity.class);
                        startActivity(it);
                        return true;
                }
                return false;
            }
        });

        //设置点击事件
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.app_name, R.string.app_name){
            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if(itemId == tempId) return;
                tempId = itemId;
                switch (itemId) {
                    case R.id.nav_friends: //美文分类
                        onFragmentCommit(postTypeFragment);
                        break;
                    case R.id.nav_home:  //主页
                        onFragmentCommit(mainFragment);
                        break;
                    case R.id.nav_messages: //消息
                        onFragmentCommit(meFragment);
                        break;
                    case R.id.nav_favo: //收藏
                        onFragmentCommit(favoFragment);
                        break;
                    case R.id.nav_bu_artclie:
                        onFragmentCommit(mostArtcileFragment);
                        break;
                    case R.id.nav_find:
                        onFragmentCommit(mFindFragment);
                        break;
                }
            }
        };
        drawerToggle.syncState();
        drawer_layout.setDrawerListener(drawerToggle);

        user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UserInfoActivity.class);
                startActivity(intent);
            }
        });

        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawer_layout.closeDrawers();
                if(menuItem.getItemId()==R.id.night_type){ //切换日间-夜间模式
                    if (getThemeSetting() == R.style.DrakTheme) {
                        setThemeSetting(R.style.AppTheme);
                    } else {
                        setThemeSetting(R.style.DrakTheme);
                    }
                    return true;
                } else if(menuItem.getItemId() == R.id.nav_setting){
                    Intent intent = new Intent(getContext(), AppSettingActivity.class);
                    startActivity(intent);
                    return true;
                }
                itemId = menuItem.getItemId();
                return true;
            }
        });

        getSupportFragmentManager().beginTransaction().add(R.id.container, mainFragment).commit();


        //在任意网络环境下都进行更新自动提醒
        BmobUpdateAgent.setUpdateOnlyWifi(false);
        BmobUpdateAgent.update(this);

        saveFavoData(); //缓存已经收藏的帖子
    }

    private void onFragmentCommit(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    /**
     * 获取Post分类
     * */
    private void getPostType(){
        int count = 0;
        if(count == 11){ //目前11个
            return;
        }
        BmobQuery<PostType> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<PostType>() {
            @Override
            public void done(List<PostType> list, BmobException e) {
                Log.i(tag, "------ss=="+e);
            }
        });

    }

    /**
     * 获取已经收藏的帖子
     * */
    private void saveFavoData(){
        User user = BmobUser.getCurrentUser(User.class);
        BmobQuery<Favo> query = new BmobQuery<>();
        query.addWhereEqualTo("user", user);
        query.findObjects(new FindListener<Favo>() {
            @Override
            public void done(List<Favo> list, BmobException e) {

            }
        });
    }

    /**
     * 异步保存收藏的帖子
     * */
    private void asyncSaveFavoData(final List<Favo> list){
        AsyncTask task = new AsyncTask() {
            @Override
            public void updata(Object obj) {

            }

            @Override
            public Object loadData() {
                return null;
            }
        };
        threadPoolManager.addAsyncTask(task);
        threadPoolManager.start();
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

    /**
     * 点击返回键退出主程序
     * */
    private static boolean isExit = false;
    private Handler mHandler = new Handler();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(isExit == false){
                isExit = true;
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        isExit = false;
                    }
                }, 2000);
            } else {
                application.finishAllActivity();
                System.exit(0);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
