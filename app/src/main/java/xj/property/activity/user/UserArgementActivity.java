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


/**
 * Created by Administrator on 2015/3/25.
 */
public class UserArgementActivity extends HXBaseActivity {
    private WebView webview;

    private LinearLayout ll_errorpage;
    private LinearLayout ll_nomessage;
    private LinearLayout ll_neterror;
    private TextView tv_getagain;
    private WebSettings mWebSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bonus_instructions);
        initTitle(null, "帮帮用户服务协议", null);
        ll_neterror=(LinearLayout)findViewById(R.id.ll_neterror);
        tv_getagain=(TextView)findViewById(R.id.tv_getagain);
        ll_nomessage=(LinearLayout)findViewById(R.id.ll_nomessage);
        ll_errorpage=(LinearLayout)findViewById(R.id.ll_errorpage);
        webview = (WebView) findViewById(R.id.wv_content);
        if (!CommonUtils.isNetWorkConnected(this)) {
            ll_errorpage.setVisibility(View.VISIBLE);
            ll_neterror.setVisibility(View.VISIBLE);
            ll_nomessage.setVisibility(View.GONE);
            tv_getagain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!CommonUtils.isNetWorkConnected(UserArgementActivity.this)){
                        return;
                    }else {
                        ll_errorpage.setVisibility(View.GONE);

                    }
                }
            });
        }else {
            ll_errorpage.setVisibility(View.GONE);
        }
        initWebView(webview);
        webview.loadUrl("http://114.215.114.56:8080/jsp/app/agreement.jsp");

    }


    @Override
    public void onClick(View v) {
    }

    private void initWebView(WebView wView) {
        // 设置支持JavaScript等
        mWebSettings = webview.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setDefaultTextEncodingName("UTF -8");
      //  mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setLightTouchEnabled(true);
        //mWebSettings.setSupportZoom(true);
        webview.setHapticFeedbackEnabled(false); //
        webview.setInitialScale(0); // 改变这个值可以设定初始大小 //重要,用于与页面交互!
        mLdDialog.show();
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
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
            }
        });

    }




}
