package xj.property.activity.LifeCircle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.umeng.socialize.sso.UMSsoHandler;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.adapter.XJBaseAdapter;
import xj.property.beans.UserInfoDetailBean;
import xj.property.cache.SearchHistoryCache;
import xj.property.provider.ShareProvider;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

public class SearchLifeCircle extends HXBaseActivity {

    private UserInfoDetailBean userbean;
    private EditText query;
    //    private PullToRefreshListView lv_index_search_result;
    private String searchName;
    private TextView tv_cancle;
    private ImageButton search_clear;

    //    IndexSearchAdapter adapter;
    private XJBaseAdapter searchAdapter;

    private int pageIndex = 1;

    private LinearLayout ll_errorpage;
    private LinearLayout ll_nomessage;
    private LinearLayout ll_neterror;
    private TextView tv_getagain;

    private int searchtype = 100;
    private String emobid ;

    /**
     * 搜索历史
     */
    private ArrayList<SearchHistoryCache> searchHis;

    private TextView tv_searchhis_header;
    private View tv_searchhis_view;
    private xj.property.widget.MyListView  lv_index_search_his;
    private TextView tv_clear_search_his;
    private LinearLayout ll_search_his;
    //    private LinearLayout ll_search_result;
    private LinearLayout ll_search_his_empty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_life_circle);
        ShareProvider.getInitShareProvider(SearchLifeCircle.this);
        userbean = PreferencesUtil.getLoginInfo(this);
        initView();
        initData();
    }

    private void initView() {
        query = (EditText) findViewById(R.id.query);
        //query.requestFocus();
        ll_neterror=(LinearLayout)findViewById(R.id.ll_neterror);
        tv_getagain=(TextView)findViewById(R.id.tv_getagain);
        ll_nomessage=(LinearLayout)findViewById(R.id.ll_nomessage);
        ll_errorpage=(LinearLayout)findViewById(R.id.ll_errorpage);

        //  query.setFocusable(true);
        //  InputMethodManager m = (InputMethodManager) query.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        // m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        //m.showSoftInput(query, 0);
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                InputMethodManager m = (InputMethodManager) query.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        }, 30);


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager = (InputMethodManager) query.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(query, 0);
            }

        }, 500);

        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        search_clear = (ImageButton) findViewById(R.id.search_clear);

//        lv_index_search_result = (PullToRefreshListView) findViewById(R.id.lv_index_search_result);

        /***his***/
        tv_searchhis_header = (TextView) findViewById(R.id.tv_searchhis_header);
        tv_searchhis_view = (View) findViewById(R.id.tv_searchhis_view);
        lv_index_search_his = (xj.property.widget.MyListView) findViewById(R.id.lv_index_search_his);
        tv_clear_search_his = (TextView) findViewById(R.id.tv_clear_search_his);

        ll_search_his = (LinearLayout) findViewById(R.id.ll_search_his);
        ll_search_his_empty = (LinearLayout) findViewById(R.id.ll_search_his_empty);

        lv_index_search_his.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                query.setText(searchHis.get(position).searchcontent);
                Intent it = new Intent(SearchLifeCircle.this, LifeSearchActivity.class);
                it.putExtra(Config.SearchName, searchHis.get(position).searchcontent);
                startActivity(it);

            }
        });
        tv_clear_search_his.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Delete().from(SearchHistoryCache.class).where("emobid =? and searchtype =?",emobid,searchtype).execute();
                initData();
            }
        });
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        query.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    /*隐藏软键盘*/
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    String searchName = query.getText().toString();
                    if (searchName != null && searchName.length() != 0) {
                        SearchHistoryCache searchHistoryCache = new SearchHistoryCache(emobid,searchtype,searchName);
                        searchHistoryCache.save();
                        Intent intent = new Intent(SearchLifeCircle.this, LifeSearchActivity.class);
                        intent.putExtra(Config.SearchName, searchName);
                        startActivity(intent);
                        return true;
                    }
                }
                return false;
            }
        });

        search_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.setText("");
            }
        });
    }

    private void initData() {
        if(userbean == null){
            emobid = PreferencesUtil.getTourist(this);
        }else {
            emobid = userbean .getEmobId();
        }
        searchHis = (ArrayList) new Select().from(SearchHistoryCache.class).where("emobid =? and searchtype =?",emobid,searchtype).orderBy("ID DESC").execute();
        if (searchHis.size() == 0) {
            ll_search_his_empty.setVisibility(View.VISIBLE);
            ll_search_his.setVisibility(View.GONE);
        } else {
            ll_search_his_empty.setVisibility(View.GONE);
            ll_search_his.setVisibility(View.VISIBLE);
            searchAdapter = new XJBaseAdapter(this, R.layout.item_surrounding_search_his, searchHis, new String[]{"searchcontent"});
            lv_index_search_his.setAdapter(searchAdapter);

        }
    }

    public void shareShow(String url,String message ){
        ShareProvider.getShareProvider(SearchLifeCircle.this).showShareActivity(url, message, "邻居帮帮", ShareProvider.CODE_LIFECRILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMSsoHandler ssoHandler = ShareProvider.mController.getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {


    }


}
