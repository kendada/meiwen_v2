package cc.meiwen.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.koudai.kbase.imageselector.utils.ScreenUtils;
import com.koudai.kbase.widget.dialog.KTipDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.meiwen.R;
import cc.meiwen.adapter.HistorySignAdapter;
import cc.meiwen.model.calendarSign;
import cc.meiwen.ui.fragment.TipsDialog;
import cc.meiwen.view.TitleBar;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by abc on 2017/11/17.
 */

public class HistorySignActivity extends BaseActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, HistorySignActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private List<calendarSign> mList = new ArrayList<>();
    private HistorySignAdapter mAdapter;

    private KTipDialog loadingDialog;

    private int mW; // 屏幕分辨率

    private String TAG = HistorySignActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_sign_layout);
        ButterKnife.bind(this);

        initViews();

        getData();
    }

    private void initViews(){
        mW = ScreenUtils.getWidthPx(this);

        loadingDialog = new KTipDialog.Builder(this)
                .setIconType(KTipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        mAdapter = new HistorySignAdapter(mList, this, mW);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                onShow(mList.get(position));
            }
        });
    }

    private void onShow(calendarSign sign){
        if(sign == null) return;
        TipsDialog mTipsDialog = new TipsDialog();
        mTipsDialog.setCalendarSign(sign);
        mTipsDialog.setCache(true);
        mTipsDialog.show(getSupportFragmentManager());
    }

    private void getData() {
        loadingDialog.show();
        BmobQuery<calendarSign> query = new BmobQuery<>();
        query.order("-createdAt");
        query.findObjects(new FindListener<calendarSign>() {
            @Override
            public void done(List<calendarSign> list, BmobException e) {
                Log.d(TAG, "e = " + e);
                if(list != null && list.size() > 0){
                    mList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                }
                loadingDialog.dismiss();
            }
        });
    }
}
