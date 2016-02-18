package cc.meiwen.ui.activity;

import android.os.Bundle;
import android.webkit.WebView;

import cc.meiwen.R;
import cc.meiwen.model.Artcile;


/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-18
 * Time: 09:35
 * Version 1.0
 */

public class MostBeautifulActivity extends BaseActivity {

    private WebView web_view;

    private Artcile artcile;

    private String tag = MostBeautifulActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_most_bra_layout);
        initViews();
        initData();
    }

    public void initViews() {
        web_view = (WebView)findViewById(R.id.web_view);
    }

    public void initData() {
        artcile = (Artcile) getIntent().getSerializableExtra("art");
        if(artcile != null){
            web_view.loadDataWithBaseURL(null, artcile.getContent(), "text/html", "UTF-8", null);
        }
    }

}
