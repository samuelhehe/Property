package xj.property.activity.contactphone;

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

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.adapter.XJBaseAdapter;
import xj.property.beans.UserInfoDetailBean;
import xj.property.cache.SearchHistoryCache;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

/**
 * Created by n on 2015/4/14.
 */
public class FastShopSearchListActivity extends HXBaseActivity {

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
    int searchtype=110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_fastshop);
        userbean = PreferencesUtil.getLoginInfo(FastShopSearchListActivity.this);
        initView();
        initData();
    }

    private void initData() {
        searchHis = (ArrayList) new Select().from(SearchHistoryCache.class).where("emobid =? and searchtype =?",userbean==null?"": userbean.getEmobId(), searchtype).orderBy("ID DESC").execute();
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

    private void initView() {
        query = (EditText) findViewById(R.id.query);
        //query.requestFocus();
        ll_neterror=(LinearLayout)findViewById(R.id.ll_neterror);
        tv_getagain=(TextView)findViewById(R.id.tv_getagain);
        ll_nomessage=(LinearLayout)findViewById(R.id.ll_nomessage);
        ll_errorpage=(LinearLayout)findViewById(R.id.ll_errorpage);


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager = (InputMethodManager) query.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(query, 0);
            }

        }, 500);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        search_clear = (ImageButton) findViewById(R.id.search_clear);


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
                Intent it = new Intent(FastShopSearchListActivity.this, FastShopIndexActivity.class);
                it.putExtra(Config.SearchName, searchHis.get(position).searchcontent);
                startActivity(it);

            }
        });
        tv_clear_search_his.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Delete().from(SearchHistoryCache.class).execute();
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
                        SearchHistoryCache searchHistoryCache = new SearchHistoryCache(userbean==null?"":userbean.getEmobId(),searchtype,searchName);
                        searchHistoryCache.save();
                        Intent intent = new Intent(FastShopSearchListActivity.this, FastShopIndexActivity.class);
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


    @Override
    public void onClick(View v) {


    }

}
