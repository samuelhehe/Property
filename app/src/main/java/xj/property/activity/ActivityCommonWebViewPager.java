package xj.property.activity;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;

import java.net.MalformedURLException;
import java.net.URL;

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.IndexBean;
import xj.property.widget.X5WebView;


/**
 * 首页模块类型如果为URL的情况下跳转的页面
 * 1. 主要加载一个WebView
 * 2. 加载在各种情况下的页面显示处理
 */
public class ActivityCommonWebViewPager extends HXBaseActivity {

    private static final int MSG_INIT_UI = 10;
    private X5WebView mWebView;
    private View ll_errorpage, ll_neterror;

    private WebSettings mWebSettings;

    private String loadingurl;

    private IndexBean clickedIndexBean;
    private URL mIntentUrl;
    private String mHomeUrl;
    private LinearLayout common_x5webview_llay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        try {
            if (Integer.parseInt(Build.VERSION.SDK) >= 11) {
                getWindow().setFlags(
                        android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                        android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_common_webviewpager);
        clickedIndexBean = (IndexBean) getIntent().getSerializableExtra("clickedIndexBean");
        if (clickedIndexBean == null) {
            showToast("数据异常");
            return;
        }
        initTitle();
        initView();
        QbSdk.preInit(this);
        initData();
        mTestHandler.sendEmptyMessage(MSG_INIT_UI);
    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        this.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.tv_title)).setText(clickedIndexBean.getServiceName());
    }

    private void initData() {
        mLdDialog.show();
//        loadingurl = getIntent().getStringExtra("loadingurl");
        loadingurl = clickedIndexBean.getUrl();
        Intent intent = getIntent();
        if (intent != null) {
            try {
                mIntentUrl = new URL(loadingurl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mWebView = new X5WebView(this);
        common_x5webview_llay.addView(mWebView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        if (TextUtils.isEmpty(loadingurl)) {
            showMiddleToast("数据出错");
            if (mWebView != null) {
                mWebView.setVisibility(View.GONE);
            }
            setNetworkErrorView();
        }
    }

    private void setNetworkErrorView() {
        if (ll_errorpage != null && ll_neterror != null) {
            ll_neterror.setVisibility(View.VISIBLE);
            ll_errorpage.setVisibility(View.VISIBLE);
        }
        if (mLdDialog != null) {
            mLdDialog.dismiss();
        }
    }

    private void initView() {
        common_x5webview_llay = (LinearLayout) this.findViewById(R.id.common_x5webview_llay);
        ll_errorpage = this.findViewById(R.id.ll_errorpage);
        ll_neterror = this.findViewById(R.id.ll_neterror);
        this.findViewById(R.id.tv_getagain).setOnClickListener(this);
    }

    private Handler mTestHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_INIT_UI:
                    init();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void init() {
        Log.w("grass", "Current SDK_INT:" + Build.VERSION.SDK_INT);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                // TODO Auto-generated method stub

                Log.e("should", "request.getUrl().toString() is " + request.getUrl().toString());

                return super.shouldInterceptRequest(view, request);
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (ll_errorpage != null) {
                    ll_errorpage.setVisibility(View.GONE);
                }
                mWebView.setVisibility(View.VISIBLE);
                if (mLdDialog != null) {
                    mLdDialog.dismiss();
                }

            }

        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
            }


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
            }
        });


        WebSettings webSetting = mWebView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        long time = System.currentTimeMillis();
        if (mIntentUrl == null) {
            mWebView.loadUrl(mHomeUrl);
        } else {
            mWebView.loadUrl(mIntentUrl.toString());
        }
        TbsLog.d("time-cost", "cost time: "
                + (System.currentTimeMillis() - time));
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_getagain:
                mWebView.reload();
                /// 重试
                break;
            case R.id.iv_back:
                finish();
                break;
        }


    }
}
