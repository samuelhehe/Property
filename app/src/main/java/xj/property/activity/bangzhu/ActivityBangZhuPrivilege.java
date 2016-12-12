package xj.property.activity.bangzhu;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.utils.CommonUtils;
import xj.property.utils.other.Config;


/**
 * 帮助特权页面
 * 1. 主要加载一个WebView
 * 2. 加载在各种情况下的页面显示处理
 */
public class ActivityBangZhuPrivilege extends HXBaseActivity {

    private WebView bangzhu_privilege_introduction_wv;

    private View ll_errorpage, ll_neterror;

    private WebSettings mWebSettings;

    private String loadingurl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bang_zhu_privilege);
        initTitle();
        initView();
        initData();
        initWebView(bangzhu_privilege_introduction_wv);

    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        this.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.tv_title)).setText("特权/规则");
    }


    private void initData() {
        mLdDialog.show();
//        loadingurl = getIntent().getStringExtra("loadingurl");
        loadingurl = Config.NET_BASE+"/jsp/app/privilege.jsp";
        if (TextUtils.isEmpty(loadingurl)) {
            showMiddleToast("数据出错");
            if (bangzhu_privilege_introduction_wv != null) {
                bangzhu_privilege_introduction_wv.setVisibility(View.GONE);
            }
        } else {
            if (CommonUtils.isNetWorkConnected(this)) {
                if (bangzhu_privilege_introduction_wv != null) {
                    bangzhu_privilege_introduction_wv.loadUrl(loadingurl);
                }
            } else {
                setNetworkErrorView();
            }
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
        bangzhu_privilege_introduction_wv = (WebView) this.findViewById(R.id.bangzhu_privilege_introduction_wv);

        ll_errorpage = this.findViewById(R.id.ll_errorpage);

        ll_neterror = this.findViewById(R.id.ll_neterror);

        this.findViewById(R.id.tv_getagain).setOnClickListener(this);
    }

    /**
     * 初始化WebView
     *
     * @param webView
     */
    public void initWebView(WebView webView) {
        // 设置支持JavaScript等
        mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setDefaultTextEncodingName("UTF-8");
        mWebSettings.setLightTouchEnabled(true);
        webView.setHapticFeedbackEnabled(false); //
        webView.setInitialScale(0); // 改变这个值可以设定初始大小 //重要,用于与页面交互!
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (ll_errorpage != null) {
                    ll_errorpage.setVisibility(View.GONE);
                }
                bangzhu_privilege_introduction_wv.setVisibility(View.VISIBLE);
                if (mLdDialog != null) {
                    mLdDialog.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                ll_errorpage.setVisibility(View.VISIBLE);
                ll_neterror.setVisibility(View.VISIBLE);
                if (mLdDialog != null) {
                    mLdDialog.dismiss();
                }
//                ll_nomessage.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_getagain:

                initData();
                /// 重试
                break;

            case R.id.iv_back:
                finish();
                break;
        }


    }
}
