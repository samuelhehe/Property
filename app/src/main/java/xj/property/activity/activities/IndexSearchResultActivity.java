package xj.property.activity.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.repo.xw.library.views.PullListView;
import com.repo.xw.library.views.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.surrounding.FacilityContentActivity;
import xj.property.adapter.IndexSearchAdapter;
import xj.property.adapter.XJBaseAdapter;
import xj.property.beans.FacilitiesBean;
import xj.property.beans.IndexSearchDataListBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.cache.SuroundingSearchModel;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

import static com.repo.xw.library.views.PullToRefreshLayout.OnRefreshListener;

/**
 * Created by n on 2015/4/14.
 */
public class IndexSearchResultActivity extends HXBaseActivity implements OnRefreshListener {

    private UserInfoDetailBean userbean;
    private EditText query;
    private String searchName;
    private TextView tv_cancle;
    private ImageButton search_clear;

    IndexSearchAdapter adapter;
    XJBaseAdapter searchAdapter;

    private int pageIndex = 1;

    private LinearLayout ll_errorpage;
    private LinearLayout ll_nomessage;
    private LinearLayout ll_neterror;
    private TextView tv_getagain;

    /**
     * 搜索历史
     */
    private ArrayList<SuroundingSearchModel> searchHis;

    private TextView tv_searchhis_header;
    private View tv_searchhis_view;
    private xj.property.widget.MyListView lv_index_search_his;
    private TextView tv_clear_search_his;
    private LinearLayout ll_search_his;
    private LinearLayout ll_search_result;
    private LinearLayout ll_search_his_empty;
    private PullToRefreshLayout pull_to_refreshlayout;
    private PullListView lv_index_search_result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indexsearch);
        userbean = PreferencesUtil.getLoginInfo(IndexSearchResultActivity.this);
        SharedPreferences sp = getSharedPreferences("searchhis", MODE_PRIVATE);
        String flag = sp.getString("flag", "");
        if (TextUtils.isEmpty(flag) || flag.trim().length() == 0) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("flag", "true");
            editor.commit();
            SuroundingSearchModel model = new SuroundingSearchModel("test");
            model.save();
            new Delete().from(SuroundingSearchModel.class).execute();
        }
        initView();
        initData();
    }

    private void initData() {
        searchHis = (ArrayList) new Select().from(SuroundingSearchModel.class).orderBy("ID DESC").execute();
        if (searchHis.size() == 0) {
            ll_search_his_empty.setVisibility(View.VISIBLE);
            ll_search_his.setVisibility(View.GONE);
            ll_search_result.setVisibility(View.GONE);
        } else {
            ll_search_his_empty.setVisibility(View.GONE);
            ll_search_his.setVisibility(View.VISIBLE);
            ll_search_result.setVisibility(View.GONE);
            searchAdapter = new XJBaseAdapter(this, R.layout.item_surrounding_search_his, searchHis, new String[]{"facilityName"});
            lv_index_search_his.setAdapter(searchAdapter);

        }

    }

    private void initView() {
        query = (EditText) findViewById(R.id.query);
        //query.requestFocus();
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        tv_getagain = (TextView) findViewById(R.id.tv_getagain);
        ll_nomessage = (LinearLayout) findViewById(R.id.ll_nomessage);
        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);
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

        pull_to_refreshlayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refreshlayout);
        pull_to_refreshlayout.setOnRefreshListener(this);

        lv_index_search_result = (PullListView) findViewById(R.id.lv_index_search_result);
//        pull_listview.setPullUpEnable(false);

        lv_index_search_result.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (lv_index_search_result.getLastVisiblePosition() == (lv_index_search_result.getCount() - 1)) {
                            pull_to_refreshlayout.autoLoad();
                        }
                        // 判断滚动到顶部
                        if (lv_index_search_result.getFirstVisiblePosition() == 0) {

                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        /***his***/
        tv_searchhis_header = (TextView) findViewById(R.id.tv_searchhis_header);
        tv_searchhis_view = (View) findViewById(R.id.tv_searchhis_view);
        lv_index_search_his = (xj.property.widget.MyListView) findViewById(R.id.lv_index_search_his);
        tv_clear_search_his = (TextView) findViewById(R.id.tv_clear_search_his);

        ll_search_his = (LinearLayout) findViewById(R.id.ll_search_his);
        ll_search_result = (LinearLayout) findViewById(R.id.ll_search_result);
        ll_search_his_empty = (LinearLayout) findViewById(R.id.ll_search_his_empty);

        lv_index_search_his.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                query.setText(searchHis.get(position).getFacilityName());
                Intent it = new Intent(IndexSearchResultActivity.this, FacilityContentActivity.class);
                SuroundingSearchModel model = searchHis.get(position);
                FacilitiesBean bean = new FacilitiesBean();
                bean.setAddress(model.address);
                bean.setAdminId(model.adminId);
                bean.setBusinessEndTime(model.businessEndTime);
                bean.setBusinessStartTime(model.businessStartTime);
                bean.setCommunityId(model.communityId);
                bean.setCreateTime(model.createTime);
                bean.setDistance(model.distance);
                bean.setFacilitiesClassId(model.facilitiesClassId);
                bean.setFacilitiesDesc(model.facilitiesDesc);
                bean.setFacilityId(model.facilityId);
                bean.setFacilityName(model.facilityName);
                bean.setLatitude(model.latitude);
                bean.setLogo(model.logo);
                bean.setLongitude(model.longitude);
                bean.setPhone(model.phone);
                bean.setStatus(model.status);
                bean.setTypeName(model.typeName);
                it.putExtra(Config.INTENT_PARMAS1, bean);
                startActivity(it);

            }
        });
        tv_clear_search_his.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Delete().from(SuroundingSearchModel.class).execute();
                initData();
            }
        });


        lv_index_search_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FacilitiesBean bean = list.get(position - 1);
                SuroundingSearchModel model = new SuroundingSearchModel();
                model.businessEndTime = bean.getBusinessEndTime();
                model.businessStartTime = bean.getBusinessStartTime();
                model.distance = bean.getDistance();
                model.facilityName = bean.getFacilityName();
                model.logo = bean.getLogo();
                model.adminId = bean.getAdminId();
                model.communityId = bean.getCommunityId();
                model.createTime = bean.getCreateTime();
                model.facilitiesClassId = bean.getFacilitiesClassId();
                model.facilitiesDesc = bean.getFacilitiesDesc();
                model.facilityId = bean.getFacilityId();
                model.latitude = bean.getLatitude();
                model.longitude = bean.getLongitude();
                model.phone = bean.getPhone();
                model.status = bean.getStatus();
                model.typeName = bean.getTypeName();
                model.address = bean.getAddress();
                model.save();
                Intent it = new Intent(IndexSearchResultActivity.this, FacilityContentActivity.class);
                it.putExtra(Config.INTENT_PARMAS1, list.get(position - 1));
                startActivity(it);
            }
        });

        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        query.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (query.getText().toString().length() == 0) {
                    search_clear.setVisibility(View.INVISIBLE);
                    initData();
                } else {
                    searchAdapter = null;
                    ll_search_his.setVisibility(View.GONE);
                    ll_search_his_empty.setVisibility(View.GONE);
                    ll_search_result.setVisibility(View.VISIBLE);
                    search_clear.setVisibility(View.VISIBLE);
                    searchName = s.toString();
                    getIndexSearchDataList(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pageIndex = 1;
        new GetDataTask().execute();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        pageIndex++;
        new GetDataTask().execute();
    }


    private class GetDataTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // Simulates a background job.
            getData(pageIndex);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }
    }

    @Override
    public void onClick(View v) {


    }

    /**
     * 获取ActList部分
     */
    interface IndexSearchService {
        ///http://114.215.114.56:8080/api/v1/communities/1/facilities?keyword=ktv&pageNum=1&pageSize=10
        ///http://114.215.114.56:8080/api/v1/communities/1/facilities?keyword=超市&pageNum=1&pageSize=10
        @GET("/api/v1/communities/{communityId}/facilities")
        void getIndexSearchDataList(@Path("communityId") long communityId, @QueryMap Map<String, String> map, Callback<IndexSearchDataListBean> cb);
    }

    ArrayList<FacilitiesBean> list;
    private void getIndexSearchDataList(String s) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE2).build();
        IndexSearchService service = restAdapter.create(IndexSearchService.class);
        Callback<IndexSearchDataListBean> callback = new Callback<IndexSearchDataListBean>() {
            @Override
            public void success(IndexSearchDataListBean indexSearchDataListBean, Response response) {
                if (indexSearchDataListBean != null && indexSearchDataListBean.getInfo() != null) {
                    list = indexSearchDataListBean.getInfo().getPageData();
                    adapter = null;
                    adapter = new IndexSearchAdapter(IndexSearchResultActivity.this, list);
                    lv_index_search_result.setAdapter(adapter);
                    if (list.size() == 0) {
                        ll_errorpage.setVisibility(View.VISIBLE);
                        ll_neterror.setVisibility(View.GONE);
                        ll_nomessage.setVisibility(View.VISIBLE);
                    } else {
                        ll_errorpage.setVisibility(View.GONE);
                        ll_neterror.setVisibility(View.GONE);
                        ll_nomessage.setVisibility(View.GONE);
                    }

                } else {
                    showNetErrorToast();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        Map<String, String> option = new HashMap<String, String>();
        option.put("keyword", s);
        option.put("pageNum", "1");
        option.put("pageSize", "10");
        service.getIndexSearchDataList(PreferencesUtil.getCommityId(IndexSearchResultActivity.this), option, callback);
    }


    protected void getData(final int pageIndex) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE2).build();
        IndexSearchService service = restAdapter.create(IndexSearchService.class);
        Callback<IndexSearchDataListBean> callback = new Callback<IndexSearchDataListBean>() {
            @Override
            public void success(IndexSearchDataListBean bean, Response response) {
                if (bean != null && bean.getInfo() != null) {
                    ArrayList<FacilitiesBean> pageData = bean.getInfo().getPageData();
                    if (list.size() > 0) {
                        if (pageData == null || pageData.size() <= 0) {
                            showNoMoreToast();
                        }
                    }

                    if (pageIndex == 1) {
                        list.clear();
                        list.addAll(pageData);
                    } else if (Integer.parseInt(bean.getInfo().getPageCount()) >= pageIndex) {
                        if (list != null) {
                            list.addAll(pageData);
                        }
                    }
                    if (list.size() == 0) {
                        ll_errorpage.setVisibility(View.VISIBLE);
                        ll_neterror.setVisibility(View.GONE);
                        ll_nomessage.setVisibility(View.VISIBLE);
                    } else {
                        ll_errorpage.setVisibility(View.GONE);
                        ll_neterror.setVisibility(View.GONE);
                        ll_nomessage.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    showNetErrorToast();
                }

                if (pageIndex == 1) {
                    pull_to_refreshlayout.refreshFinish(true);
                } else {
                    pull_to_refreshlayout.loadMoreFinish(true);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                showNetErrorToast();
                if (pageIndex == 1) {
                    pull_to_refreshlayout.refreshFinish(true);
                } else {
                    pull_to_refreshlayout.loadMoreFinish(true);
                }
                error.printStackTrace();
            }
        };
        Map<String, String> option = new HashMap<String, String>();
        option.put("keyword", searchName);
        option.put("pageNum", pageIndex + "");
        option.put("pageSize", "10");
        service.getIndexSearchDataList(PreferencesUtil.getCommityId(IndexSearchResultActivity.this), option, callback);
    }
}
