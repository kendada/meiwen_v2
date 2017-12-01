package cc.meiwen.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cc.meiwen.R;
import cc.meiwen.model.Artcile;
import cc.meiwen.view.TitleBar;


/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-18
 * Time: 09:35
 * Version 1.0
 * 内置浏览器
 */

public class AppBrowserActivity extends BaseActivity {

    public static void start(Context context, int type, Artcile artcile) {
        start(context, type, artcile, null);
    }

    public static void start(Context context, int type, String url) {
        start(context, type, null, url);
    }

    /**
     * @param context
     * @param type 1. 使用artcile; 2. 使用url
     * @param artcile
     * @param url
     * */
    public static void start(Context context, int type, Artcile artcile, String url) {
        Intent intent = new Intent(context, AppBrowserActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("art", artcile);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }


    private TitleBar mBarTitle;
    private WebView mWebView;

    private int mType;
    private String mUrl;
    private Artcile artcile;

    private String tag = AppBrowserActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_most_bra_layout);

        getIntentData(getIntent());

        initViews();
        initEvents();
        initData();
    }

    private void getIntentData(Intent intent) {
        if (intent == null) return;
        mType = intent.getIntExtra("type", 0);
        mUrl = intent.getStringExtra("url");
        artcile = (Artcile) intent.getSerializableExtra("art");
    }

    public void initViews() {
        mBarTitle = (TitleBar) findViewById(R.id.title_bar);
        mWebView = (WebView) findViewById(R.id.web_view);
    }

    public void initData() {
        if (artcile != null) {
            mWebView.loadDataWithBaseURL(null, artcile.getContent(), "text/html", "UTF-8", null);
        } else if (!TextUtils.isEmpty(mUrl)) {
            mWebView.loadUrl(mUrl);
        }
    }

    private void initEvents() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // 显示进度条
                Log.d(tag, " 进度条进度 - newProgress = " + newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title)) {
                    if (!title.contains("http") && !title.contains(".com")) {
                        // 网页标题
                        mBarTitle.setTitleText(title);
                    }
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

        });

    }


}
