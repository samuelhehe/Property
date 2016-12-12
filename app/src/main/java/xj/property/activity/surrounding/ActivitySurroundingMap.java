package xj.property.activity.surrounding;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.utils.CommonUtils;


/**
 * 根据Key进行搜索显示的百度周边页面
 * 1. 主要加载一个WebView
 * 2. 加载在各种情况下的页面显示处理
 */
public class ActivitySurroundingMap extends HXBaseActivity {

    private static final String MAPBAIDU_PREFIX_URL = "http://map.baidu.com";
    private WebView surrounding_map_wv;

    private View ll_errorpage, ll_neterror,ll_nocontent;

    private WebSettings mWebSettings;

    private String loadingurl;

    private String maplistitemuid;

    private PoiSearch mPoiSearch;

    private TextView ll_nocontent_msg_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srrounding_map);
        maplistitemuid = getIntent().getStringExtra("maplistitemuid");

        if (TextUtils.isEmpty(maplistitemuid)) {
            Log.d("onCreate --->>>  maplistitemuid  ", "" + maplistitemuid);
            showToast("查看详情失败");
            finish();
        }
        SDKInitializer.initialize(this.getApplicationContext());
        loadWebViewURL();
        initView();

    }

    private void loadWebViewURL() {

        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(new MyOnGetPoiSearchResultListener());
        mPoiSearch.searchPoiDetail(new PoiDetailSearchOption().poiUid(maplistitemuid));
    }

    private class MyOnGetPoiSearchResultListener implements OnGetPoiSearchResultListener {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {

        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
            if (poiDetailResult != null) {
                loadingurl = poiDetailResult.getDetailUrl();
                if(TextUtils.isEmpty(loadingurl)||loadingurl.startsWith(MAPBAIDU_PREFIX_URL)){
                    Log.d("onCreate --->>>  onGetPoiDetailResult  ", " loadingurl " + loadingurl);
                    setnoContentView();

                }else{
                    initData();
                    initWebView();
                }
            } else {
                setnoContentView();
            }
        }
    }


    /**
     * 初始化标题
     */
    private void initTitle() {
        this.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.tv_title)).setText("详情");
    }


    private void initData() {
        mLdDialog.show();

        if (TextUtils.isEmpty(loadingurl)) {

            if (surrounding_map_wv != null) {
                surrounding_map_wv.setVisibility(View.GONE);
            }
        } else {
            if (CommonUtils.isNetWorkConnected(this)) {
                if (surrounding_map_wv != null) {
                    surrounding_map_wv.loadUrl(loadingurl);
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
    private void setnoContentView() {
        if (ll_errorpage != null && ll_nocontent != null) {
            ll_nocontent.setVisibility(View.VISIBLE);
            ll_errorpage.setVisibility(View.VISIBLE);
        }
        if (mLdDialog != null) {
            mLdDialog.dismiss();
        }
    }

    private void initView() {
        initTitle();
        surrounding_map_wv = (WebView) this.findViewById(R.id.surrounding_map_wv);

        ll_errorpage = this.findViewById(R.id.ll_errorpage);

        ll_neterror = this.findViewById(R.id.ll_neterror);

        ll_nocontent = this.findViewById(R.id.ll_nocontent);
        ll_nocontent_msg_tv = (TextView) findViewById(R.id.ll_nocontent_msg_tv);
        ll_nocontent_msg_tv.setText("无法请求到数据");

        this.findViewById(R.id.tv_getagain).setOnClickListener(this);
    }

    /**
     * 初始化WebView
     */
//    @SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
//    @JavascriptInterface
    public void initWebView() {
        // 设置支持JavaScript等

        mWebSettings = surrounding_map_wv.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setDefaultTextEncodingName("UTF-8");
        mWebSettings.setLightTouchEnabled(true);
        //// "Uncaught TypeError: Cannot call method 'getItem' of null", source: http://map.baidu.com/mobile/weba
        //// http://wazai.net/2969/android-webview-error-uncaught-typeerror-cannot-call-method-getitem-of-null-at
        mWebSettings.setDomStorageEnabled(true);

        surrounding_map_wv.setHapticFeedbackEnabled(false);

//        surrounding_map_wv.addJavascriptInterface(new MyObject(mHandler), "MyObject");

        surrounding_map_wv.loadUrl(loadingurl);

//        surrounding_map_wv.evaluateJavascript();

        surrounding_map_wv.setInitialScale(0); // 改变这个值可以设定初始大小 //重要,用于与页面交互!
        surrounding_map_wv.setWebViewClient(new WebViewClient() {
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
                surrounding_map_wv.setVisibility(View.VISIBLE);

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
