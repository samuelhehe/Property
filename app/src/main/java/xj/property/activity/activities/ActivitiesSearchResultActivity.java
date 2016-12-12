package xj.property.activity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.repo.xw.library.views.PullListView;
import com.repo.xw.library.views.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.adapter.ActivitiesAdapter;
import xj.property.beans.ActivitiesBean;
import xj.property.beans.ActivitiesSearchBean;
import xj.property.beans.ActivityBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.cache.ActivityReaded;
import xj.property.utils.CommonUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

import static com.repo.xw.library.views.PullToRefreshLayout.OnRefreshListener;

/**
 * Created by n on 2015/4/14.
 */
public class ActivitiesSearchResultActivity extends HXBaseActivity implements OnRefreshListener {

    /**
     * logger
     */
    //  private SwipeRefreshLayout swipeRefreshLayout;

    private TextView tv_search_activities;
    private ImageView iv_search;
    private ActivitiesAdapter activitiesAdapter;
    /**
     * if load next
     */
    private boolean isLoadNext = false;
    /**
     * last item index
     */
    private int lastItemIndex;


    /**
     * activities data to show
     */
    List<ActivityBean> activityBeanList = new ArrayList<ActivityBean>();
    ActivitiesBean activitiesBean = new ActivitiesBean();
    /**
     * page index
     */
    private int pageIndex = 1;
    /**
     * load moreview
     */
    private View loadMoreView;

    /**
     * button new activity
     */
    private RelativeLayout btn_new_activity;

    private String searchName;
    private UserInfoDetailBean bean;

    private LinearLayout ll_errorpage;
    private LinearLayout ll_nomessage;
    private LinearLayout ll_neterror;
    private TextView tv_getagain;
    private PullToRefreshLayout pull_to_refreshlayout;
    private PullListView lv_activities;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_search_layout);
        initTitle(null, "约局", "");
        Intent intent = getIntent();
        searchName = intent.getStringExtra("searchName");
        activityBeanList.clear();
        initView();
        bean = PreferencesUtil.getLoginInfo(this);
        initData();


    }

    private void initData() {
        if (!CommonUtils.isNetWorkConnected(this)) {
            ll_errorpage.setVisibility(View.VISIBLE);
            ll_neterror.setVisibility(View.VISIBLE);
            ll_nomessage.setVisibility(View.GONE);
            tv_getagain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!CommonUtils.isNetWorkConnected(ActivitiesSearchResultActivity.this)) {
                        return;
                    } else {
                        ll_errorpage.setVisibility(View.GONE);
                        getActivities(searchName);
                    }
                }
            });
        } else {
            ll_errorpage.setVisibility(View.GONE);
            getActivities(searchName);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
//        activityBeanList.clear();
//        getActivities(searchName);
//        checkIsReade(activityBeanList);
//        getResumeActivities();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkIsReade(activityBeanList);
        activitiesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

    }

    public void initView() {
        //init swipeRefreshLayout
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        tv_getagain = (TextView) findViewById(R.id.tv_getagain);
        ll_nomessage = (LinearLayout) findViewById(R.id.ll_nomessage);
        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);
        tv_search_activities = (TextView) findViewById(R.id.query);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        tv_search_activities.setText(searchName);
        tv_search_activities.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                /*隐藏软键盘*/
//                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    if (inputMethodManager.isActive()) {
//                        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
//                    }
                    if (tv_search_activities.getText().toString() != null && tv_search_activities.getText().toString().length() != 0) {
                        //activitiesAdapter=null;
                        getActivities(tv_search_activities.getText().toString());
                        return true;
                    } else {

                    }
                }
                return false;
            }
        });


        pull_to_refreshlayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refreshlayout);
        pull_to_refreshlayout.setOnRefreshListener(this);

        lv_activities = (PullListView) findViewById(R.id.lv_activities);
//        pull_listview.setPullUpEnable(false);

        lv_activities.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (lv_activities.getLastVisiblePosition() == (lv_activities.getCount() - 1)) {
                            pull_to_refreshlayout.autoLoad();
                        }
                        // 判断滚动到顶部
                        if (lv_activities.getFirstVisiblePosition() == 0) {

                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        activitiesAdapter = new ActivitiesAdapter(ActivitiesSearchResultActivity.this, lv_activities, activityBeanList);
        lv_activities.setAdapter(activitiesAdapter);

        lv_activities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent(ActivitiesSearchResultActivity.this, ActivityDetailActivity.class);
                it.putExtra(Config.INTENT_PARMAS1, activityBeanList.get(i - 1));
//                intentPush.putExtra("rid", resourceDataToShowModelList.get(i).getId());
                startActivity(it);
            }
        });


        btn_new_activity = (RelativeLayout) findViewById(R.id.btn_new_activity);
        btn_new_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPush = new Intent();
                intentPush.setClass(ActivitiesSearchResultActivity.this, NewActivityActivity.class);
                startActivity(intentPush);
            }
        });

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pageIndex = 1;
        //TODO / 刷新
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        if (pageIndex == 1) {
            lv_activities.setSelection(lv_activities.getCount());
        }
        pageIndex++;
        //TODO  加载更多
    }



    /**
     * 获取ActList部分
     */
    interface ActListService {
        ///api/v1/communities/1/users/123/activities?q=图&pageNum=1&pageSize=10
        @GET("/api/v1/communities/{communityId}/users/{emobIdUser}/activities")
        void getActList(@Path("communityId") long communityId, @Path("emobIdUser") String emobIdUser, @QueryMap Map<String, String> map, Callback<ActivitiesSearchBean> cb);
    }

    private void getActivities(String searchName) {
        mLdDialog.show();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        ActListService service = restAdapter.create(ActListService.class);
        Callback<ActivitiesSearchBean> callback = new Callback<ActivitiesSearchBean>() {
            @Override
            public void success(ActivitiesSearchBean bean, retrofit.client.Response response) {
                mLdDialog.dismiss();
                if (bean.getInfo() != null && bean.getInfo().getData() != null) {
                    activityBeanList.clear();
                    activityBeanList.addAll(bean.getInfo().getData());
                }
                if (activityBeanList.size() != 0) {
                    ll_errorpage.setVisibility(View.GONE);
                    checkIsReade(bean.getInfo().getData());
                    activitiesAdapter.notifyDataSetChanged();
                } else {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_neterror.setVisibility(View.GONE);
                    ll_nomessage.setVisibility(View.VISIBLE);
                }
                if (pageIndex == 1) {
                    pull_to_refreshlayout.refreshFinish(true);
                } else {
                    pull_to_refreshlayout.loadMoreFinish(true);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
                if (pageIndex == 1) {
                    pull_to_refreshlayout.refreshFinish(true);
                } else {
                    pull_to_refreshlayout.loadMoreFinish(true);
                }
            }
        };
        Map<String, String> option = new HashMap<String, String>();
//        option.put("activityName", searchName);
        option.put("q", searchName);
        option.put("pageNum", 1 + "");
        option.put("pageSize", "10");
        service.getActList(PreferencesUtil.getCommityId(this), bean == null ? "null" : bean.getEmobId(), option, callback);
    }


    private void checkIsReade(List<ActivityBean> activityBeanList) {
        for (int i = 0; i < activityBeanList.size(); i++) {
            ActivityReaded activityReaded = new Select().from(ActivityReaded.class).where("activity_id = ?", activityBeanList.get(i).getActivityId()).executeSingle();
            if (activityReaded == null) {
                activityReaded = new ActivityReaded(activityBeanList.get(i).getActivityId(), false);
                activityReaded.save();
            }
            activityBeanList.get(i).setRead(activityReaded.isreaded);
        }
    }
}
