package xj.property.activity.user;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.utils.CommonUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;


/**
 * 帮帮币使用说明
 */
public class BangBiExplanationActivity extends HXBaseActivity {
    private LinearLayout ll_errorpage;
    private LinearLayout ll_nomessage;
    private LinearLayout ll_neterror;
    private TextView tv_getagain;
    private TextView tv_nomessage;
    private WebView webView;
    private WebSettings mWebSettings;
    private TextView tv_yuan;
    private TextView tv_bangbi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bangbangbi_explanation);
        initView();
        initData();
        initWebView(webView);
    }

    private void initData() {
        if (!CommonUtils.isNetWorkConnected(this)) {
            ll_errorpage.setVisibility(View.VISIBLE);
            ll_neterror.setVisibility(View.VISIBLE);
            ll_nomessage.setVisibility(View.GONE);
            tv_getagain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!CommonUtils.isNetWorkConnected(BangBiExplanationActivity.this)) {
                        return;
                    } else {
                        ll_errorpage.setVisibility(View.GONE);
                        webView.loadUrl(Config.NET_BASE+ "/jsp/app/bangbang_coin.jsp");
//                        webView.loadUrl("http://www.baidu.com");
                        tv_bangbi.setText(Integer.parseInt(PreferencesUtil.getPrefBangbangbiCount(getApplicationContext())) > 0 ? PreferencesUtil.getPrefBangbangbiCount(getApplicationContext()) + "" : "100");
                        tv_yuan.setText(Integer.parseInt(PreferencesUtil.getPrefBangbangbiExchange(getApplicationContext())) > 0 ? PreferencesUtil.getPrefBangbangbiExchange(getApplicationContext()) + "" : "1");
                    }
                }
            });
        } else {
            ll_errorpage.setVisibility(View.GONE);
            webView.loadUrl(Config.NET_BASE+ "/jsp/app/bangbang_coin.jsp");
            tv_bangbi.setText(Integer.parseInt(PreferencesUtil.getPrefBangbangbiCount(this)) > 0 ? PreferencesUtil.getPrefBangbangbiCount(this) + "" : "100");
            tv_yuan.setText(Integer.parseInt(PreferencesUtil.getPrefBangbangbiExchange(this)) > 0 ? PreferencesUtil.getPrefBangbangbiExchange(this) + "" : "1");
        }
    }

    private void initView() {

        initTitle();

        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        tv_getagain = (TextView) findViewById(R.id.tv_getagain);
        ll_nomessage = (LinearLayout) findViewById(R.id.ll_nomessage);
        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);
        tv_nomessage = (TextView) findViewById(R.id.tv_nomessage);
        tv_nomessage.setText("");
        tv_bangbi = (TextView) findViewById(R.id.tv_bangbi);
        tv_yuan = (TextView) findViewById(R.id.tv_yuan);
        webView = (WebView) findViewById(R.id.wv_bangbi_explanntion);
//        webView.getSettings().setJavaScriptEnabled(true);
    }

    private void initTitle() {

        TextView tv = (TextView) this.findViewById(R.id.tv_title);
        tv.setText("使用说明");
        tv.setVisibility(View.VISIBLE);
        this.findViewById(R.id.iv_back).setOnClickListener(this);

    }

    public void initWebView(WebView webView) {
        // 设置支持JavaScript等
        mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setDefaultTextEncodingName("UTF -8");
        mWebSettings.setLightTouchEnabled(true);
        webView.setHapticFeedbackEnabled(false); //
        webView.setInitialScale(0); // 改变这个值可以设定初始大小 //重要,用于与页面交互!
        mLdDialog.show();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // return super.shouldOverrideUrlLoading(view, url);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // super.onPageFinished(view, url);
                mLdDialog.dismiss();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                ll_errorpage.setVisibility(View.VISIBLE);
                ll_neterror.setVisibility(View.GONE);
                ll_nomessage.setVisibility(View.VISIBLE);
                mLdDialog.dismiss();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_back:
                finish();
                break;
        }

    }
}
