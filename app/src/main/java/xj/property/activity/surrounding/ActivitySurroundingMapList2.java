package xj.property.activity.surrounding;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.utils.CommonUtils;


/**
 * 根据Key进行搜索显示的百度周边页面
 * 1. 主要加载一个WebView
 * 2. 加载在各种情况下的页面显示处理
 */
public class ActivitySurroundingMapList2 extends HXBaseActivity {

    private WebView surrounding_map_wv;

    private View ll_errorpage, ll_neterror;

    private WebSettings mWebSettings;

    private String loadingurl;

    private float latitude,longitude ;

    private String mKeys ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srrounding_map);
        mKeys =   getIntent().getStringExtra("searchmKeys");

        latitude = getIntent().getFloatExtra("latitude",0.0f);

        longitude = getIntent().getFloatExtra("longitude",0.0f);

//        Log.d("onCreate --->>> ",""+ latitude + ""+ longitude);

        initView();
        initData();
        initWebView();


    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        this.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.tv_title)).setText("周边的"+mKeys);
    }


    private void initData() {

        mLdDialog.show();
//        loadingurl = getIntent().getStringExtra("loadingurl");

//        loadingurl = Config.NET_BASE+"/jsp/app/privilege.jsp";

        loadingurl = "file:///android_asset/index.html";


        if (TextUtils.isEmpty(loadingurl)) {

            showMiddleToast("数据出错");
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


    private void initView() {
        initTitle();
        surrounding_map_wv = (WebView) this.findViewById(R.id.surrounding_map_wv);

        ll_errorpage = this.findViewById(R.id.ll_errorpage);

        ll_neterror = this.findViewById(R.id.ll_neterror);

        this.findViewById(R.id.tv_getagain).setOnClickListener(this);
    }


    private Handler  mHandler = new Handler();
    /**
     * 初始化WebView
     *

     */
//    @SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
//    @JavascriptInterface
    public void initWebView() {
        // 设置支持JavaScript等

        mWebSettings = surrounding_map_wv.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setDefaultTextEncodingName("UTF-8");
        mWebSettings.setLightTouchEnabled(true);

        surrounding_map_wv.setHapticFeedbackEnabled(false);

        surrounding_map_wv.addJavascriptInterface(new MyObject(mHandler), "MyObject");


        //加载assets目录下的文件
        String url = "file:///android_asset/index.html";

        surrounding_map_wv.loadUrl(url);
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



    class MyObject {

        private Handler handler = null;


        public MyObject(Handler handler) {
            this.handler = handler;
        }

        @JavascriptInterface
        public void init(){
            //通过handler来确保init方法的执行在主线程中
            handler.post(new Runnable() {

                public void run() {
                    //调用客户端setContactInfo方法
                    surrounding_map_wv.loadUrl("javascript:setContactInfo('" + getJsonStr() + "')");
                }
            });
        }

        public  String getJsonStr(){

            try {
                JSONObject object1 = new JSONObject();
                object1.put("key", mKeys);
                object1.put("latitude",latitude );
                object1.put("longitude", longitude);
                Log.d("JSONObject ","JSONObject  "+object1.toString());
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(object1);
                return jsonArray.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
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
