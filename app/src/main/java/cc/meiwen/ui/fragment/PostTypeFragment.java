package cc.meiwen.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.koudai.kbase.widget.dialog.KTipDialog;

import java.util.List;

import cc.meiwen.R;
import cc.meiwen.adapter.PostTypeAdapter;
import cc.meiwen.model.Datas;
import cc.meiwen.model.PostType;
import cc.meiwen.ui.activity.PostTypeActivity;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-03
 * Time: 16:14
 * Version 1.0
 */

public class PostTypeFragment extends BaseFragment {

    private SwipeRefreshLayout refresh_layout;
    private ListView list_view;

    private PostTypeAdapter adapter;

    private List<PostType> mList;

    private KTipDialog loadingDialog;

    private boolean isVisibleToUI = true; //默认可见

    private String tag = PostTypeFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loadingDialog = new KTipDialog.Builder(getActivity())
                .setIconType(KTipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在刷新")
                .create();
        View rootView = inflater.inflate(R.layout.fragment_post_type__layout, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initData();
    }

    @Override
    public void onFirstTimeLaunched() {
        if(isVisibleToUI){
            //获取分类
            getType();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        isVisibleToUI = isVisibleToUser;
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUI){
            if(mList==null || mList.size()<=0){
                //获取分类
                getType();
            }
        }
    }

    @Override
    public void onRestoreState(Bundle saveInstanceState) {
        Datas datas = saveInstanceState.getParcelable("data");
        if(datas!=null && datas.getTypeList()!=null){
            mList = datas.getTypeList();
            adapter = new PostTypeAdapter(getContext(), mList);
            list_view.setAdapter(adapter);
        } else {
            if(isVisibleToUI){
                //获取分类
                getType();
            }
        }
    }

    @Override
    public void onSaveState(Bundle outState) {
        outState.putParcelable("data", new Datas(mList, null));
    }

    @Override
    public void initViews(View view) {
        refresh_layout = (SwipeRefreshLayout)view.findViewById(R.id.refresh_layout);
        refresh_layout.setColorSchemeResources(R.color.darkPrimaryColor, R.color.primaryColor, R.color.lightPrimaryColor);
        list_view = (ListView) view.findViewById(R.id.list_view);
    }

    @Override
    public void initData() {
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getType();
            }
        });

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), PostTypeActivity.class);
                intent.putExtra("pt", mList.get(i));
                startActivity(intent);
            }
        });
    }

    /**
     * 获取分类列表
     * */
    private void getType(){
        BmobQuery<PostType> query = new BmobQuery<>();
        query.order("-createdAt");
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.findObjects(getContext(), new FindListener<PostType>() {
            @Override
            public void onStart() {
                loadingDialog.show();
            }

            @Override
            public void onSuccess(List<PostType> list) {
                mList = list;
                adapter = new PostTypeAdapter(getContext(), mList);
                list_view.setAdapter(adapter);
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onFinish() {
                loadingDialog.dismiss();
                refresh_layout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null){
            adapter.refreshData();
        }
    }
}
