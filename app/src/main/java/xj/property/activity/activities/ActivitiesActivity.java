package xj.property.activity.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.repo.xw.library.views.PullListView;
import com.repo.xw.library.views.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.adapter.ActivitiesAdapter;
import xj.property.beans.ActivitiesBean;
import xj.property.beans.ActivitiesSearchBean;
import xj.property.beans.ActivityBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.cache.ActivityReaded;
import xj.property.event.FriendsChoicedBackEvent;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.CommonUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

public class ActivitiesActivity extends HXBaseActivity implements PullToRefreshLayout.OnRefreshListener {

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

    ActivitiesSearchBean ActivitiesSearchBean = new ActivitiesSearchBean();
    /**
     * page index
     */
    private int pageIndex = 1;

    /**
     * button new activity
     */
    private RelativeLayout btn_new_activity;
    private UserInfoDetailBean bean;

    private LinearLayout ll_errorpage;
    private LinearLayout ll_index_empty;
    private LinearLayout ll_neterror;
    private TextView tv_getagain;

    private int tempPostion;

    private PopupWindow popupWindow;//搜索
    private ImageView bannerImg;
    private ImageView ll_index_empty_iv;
    private PullToRefreshLayout pull_to_refreshlayout;
    private PullListView lv_activities;
    private int limit = 10;
    private int currentPagerSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);
        initTitle(null, "约局", "说明");

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        createHeaderView();
        initView();
        initPopupWindow();
        bean = PreferencesUtil.getLoginInfo(ActivitiesActivity.this);
        PreferencesUtil.saveUnReadCount(this, 0);
        initData();

        boolean isFirstUseActivities = PreferencesUtil.isFirstUseActivities(getmContext());
        if (isFirstUseActivities) {
            /// 是否首次使用活动
            startActivity(new Intent(getmContext(), ActivitiesNoticesPagerActivity.class));
        }
    }

    private void initData() {
        if (!CommonUtils.isNetWorkConnected(this)) {
            ll_errorpage.setVisibility(View.VISIBLE);
            ll_neterror.setVisibility(View.VISIBLE);
            ll_index_empty.setVisibility(View.GONE);
            tv_getagain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!CommonUtils.isNetWorkConnected(ActivitiesActivity.this)) {
                        return;
                    } else {
                        ll_errorpage.setVisibility(View.GONE);
                        getResumeActivities();
                        pull_to_refreshlayout.autoRefresh();
                    }
                }
            });
        } else {
            ll_errorpage.setVisibility(View.GONE);
            getResumeActivities();
            pull_to_refreshlayout.autoRefresh();
        }
    }


    public void onEvent(FriendsChoicedBackEvent choicededbackevent) {
//        if (choicededbackevent != null) {
//        }
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkIsReade(activityBeanList);
        activitiesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right_text:
                startActivity(new Intent(this, ActivityExplain.class));
                break;
            case R.id.iv_right:

                showPopupWindow(v);
                break;
        }
    }

    public void initView() {

        iv_search = (ImageView) findViewById(R.id.iv_search);
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        tv_getagain = (TextView) findViewById(R.id.tv_getagain);
        ll_index_empty = (LinearLayout) findViewById(R.id.ll_index_empty);
        ll_index_empty_iv = (ImageView) findViewById(R.id.ll_index_empty_iv);
        ll_index_empty_iv.setImageResource(R.drawable.activities_index_none);
        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);

        ivRight = (ImageView) findViewById(R.id.iv_right);
        ivRight.setVisibility(View.GONE);
//        ivRight.setImageResource(R.drawable.life_circle_search_icon);
//        ivRight.setOnClickListener(this); //// 搜索功能取消 , 16/1/14


        pull_to_refreshlayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refreshlayout);
        pull_to_refreshlayout.setOnRefreshListener(this);

        lv_activities = (PullListView) findViewById(R.id.lv_activities);
        lv_activities.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (lv_activities.getLastVisiblePosition() == (lv_activities.getCount() - 1)) {
                            if (pageIndex > 1 && currentPagerSize < limit) {
                                showNoMoreToast();
                            } else {
                                pull_to_refreshlayout.autoLoad();
                            }
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

        List<ActivityBean> list = PreferencesUtil.getActivityBean(this);
        if (list != null && !list.isEmpty()) {
            activityBeanList.addAll(list);
            checkIsReade(activityBeanList);
        }

        activitiesAdapter = new ActivitiesAdapter(this, lv_activities, activityBeanList);

        lv_activities.addHeaderView(bannerImg);

        lv_activities.setAdapter(activitiesAdapter);
        lv_activities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (position <= 0) {
                    return;
                }
                Intent it = new Intent(ActivitiesActivity.this, ActivityDetailActivity.class);
                it.putExtra(Config.INTENT_PARMAS1, activityBeanList.get(position - 1));
                it.putExtra("postion", position - 1);
                startActivityForResult(it, 1);
                tempPostion = position - 1;
            }
        });

        btn_new_activity = (RelativeLayout) findViewById(R.id.btn_new_activity);
        btn_new_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PreferencesUtil.getLogin(ActivitiesActivity.this)) {
                    Intent intent = new Intent(ActivitiesActivity.this, RegisterLoginActivity.class);
                    startActivity(intent);
                    return;
                }
                Intent intentPush = new Intent();
                intentPush.setClass(ActivitiesActivity.this, NewActivityActivity.class);
                startActivityForResult(intentPush, 1);
            }
        });

        String bannerImgurl = PreferencesUtil.getActivityBannerImg(getmContext());
        if (!TextUtils.isEmpty(bannerImgurl)) {
            ImageLoader.getInstance().displayImage(bannerImgurl, bannerImg, UserUtils.activity_options);
        }

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pageIndex = 1;
        getResumeActivities();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        if (pageIndex == 1) {
            lv_activities.setSelection(lv_activities.getCount());
        }
        pageIndex++;
        getResumeActivities();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            initData();
        }
        //// 发布新活动结束成功返回,刷新页面
        if (requestCode == 0 || requestCode == 1) {
            if (resultCode == RESULT_OK) {
                initData();
            }
        }


        if (resultCode == 2) {
            int zanType = data.getIntExtra("zanType", 0);
            switch (zanType) {
                case 1:
                    activitiesAdapter.changeDataPZZan(tempPostion);
                    break;
                case 2:
                    activitiesAdapter.changeDataSource(tempPostion);
                    break;
            }
        }
    }

    /**
     * 获取ActList部分
     */
    interface ActListService {
        //        @GET("/api/v1/communities/{communityId}/users/{emobIdUser}/activities")
        @GET("/api/v3/activities")
        void getActList(@QueryMap Map<String, String> map, Callback<CommonRespBean<ActivitiesBean>> cb);

    }

    private void getResumeActivities() {
        HashMap<String, String> option = new HashMap<String, String>();
        option.put("page", "" + pageIndex);
        option.put("limit", "" + limit);
        option.put("communityId", "" + PreferencesUtil.getCommityId(getmContext()));
        ActListService service = RetrofitFactory.getInstance().create(getmContext(),option,ActListService.class);
        Callback<CommonRespBean<ActivitiesBean>> callback = new Callback<CommonRespBean<ActivitiesBean>>() {
            @Override
            public void success(CommonRespBean<ActivitiesBean> bean, retrofit.client.Response response) {
                if ("yes".equals(bean.getStatus())) {
                    List<ActivityBean> beanList = bean.getData().getData();
                    currentPagerSize = beanList.size();
                    if (pageIndex == 1) {
                        activityBeanList.clear();
                    }
                    activityBeanList.addAll(beanList);
                    String photo = bean.getField("photo", String.class);
                    if (pageIndex > 1 && currentPagerSize < limit) {
                        showNoMoreToast();
                    }
                    if (!TextUtils.isEmpty(photo)) {
                        PreferencesUtil.saveActivityBannerImg(getmContext(), photo);
                    }
                    String bannerImgurl = PreferencesUtil.getActivityBannerImg(getmContext());
                    ImageLoader.getInstance().displayImage(bannerImgurl, bannerImg, UserUtils.activity_options);
                    PreferencesUtil.saveActivityBean(ActivitiesActivity.this, beanList);
                    checkIsReade(beanList);
                    activitiesAdapter.notifyDataSetChanged();
                } else {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_neterror.setVisibility(View.GONE);
                    ll_index_empty.setVisibility(View.VISIBLE);
                    return;
                }
                if (activityBeanList.size() == 0) {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_neterror.setVisibility(View.GONE);
                    ll_index_empty.setVisibility(View.VISIBLE);
                }
                if (pageIndex == 1) {
                    pull_to_refreshlayout.refreshFinish(true);
                } else {
                    pull_to_refreshlayout.loadMoreFinish(true);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                if (pageIndex == 1) {
                    pull_to_refreshlayout.refreshFinish(true);
                } else {
                    pull_to_refreshlayout.loadMoreFinish(true);
                }
                showNetErrorToast();
            }
        };

        service.getActList(option, callback);
    }

    private void checkIsReade(List<ActivityBean> activityBeanList) {
        for (int i = 0; i < activityBeanList.size(); i++) {
            ActivityReaded activityReaded = new Select().from(ActivityReaded.class).where("activity_id = ?", activityBeanList.get(i).getActivityId()).executeSingle();
            if (activityReaded == null) {
                activityReaded = new ActivityReaded(activityBeanList.get(i).getActivityId(), false);
                activityReaded.save();
//              int count=  PreferencesUtil.getUnReadCount(this)+1;
//             PreferencesUtil.saveUnReadCount(this,count);
            }
            activityBeanList.get(i).setRead(activityReaded.isreaded);
        }
    }


    private void initPopupWindow() {

        View superZan = View.inflate(this, R.layout.search_bar_activity, null);
        popupWindow = new PopupWindow(superZan, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        tv_search_activities = (TextView) superZan.findViewById(R.id.query);
//        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        tv_search_activities.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    /*隐藏软键盘*/
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    String searchName = tv_search_activities.getText().toString();
                    if (searchName != null && searchName.length() != 0) {
                        Intent intent = new Intent(ActivitiesActivity.this, ActivitiesSearchResultActivity.class);
                        intent.putExtra("searchName", searchName);
                        startActivity(intent);
                        return true;
                    }
                }
                return false;
            }
        });


    }

    private void showPopupWindow(View view) {

        popupWindow.showAsDropDown(view);
        if (tv_search_activities != null) {
            tv_search_activities.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(tv_search_activities,InputMethodManager.SHOW_FORCED);
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void createHeaderView() {
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        bannerImg = new ImageView(this);
        AbsListView.LayoutParams imgvwMargin = new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, widthPixels * 5 / 9);
        bannerImg.setScaleType(ImageView.ScaleType.FIT_XY);
        bannerImg.setLayoutParams(imgvwMargin);
//        ImageLoader.getInstance().displayImage(imgurl, bannerImg);
    }

}
