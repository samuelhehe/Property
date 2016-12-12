package xj.property.activity.activities;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.adapter.HomeIndexSearchAdapterForLifecircle;
import xj.property.adapter.HomeIndexSearchAdapterForNeighbor;
import xj.property.beans.HomeSearchResultRespBean;
import xj.property.beans.SearchLifeCircleResultRespBean;
import xj.property.beans.SearchUserResultRespBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.CommonUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.ViewUtils;
import xj.property.widget.MyListView;

/**
 * 2015/12/18
 */
public class HomeIndexSearchResultActivity extends HXBaseActivity {

    private UserInfoDetailBean userbean;
    private EditText query;

    /// 搜索关键字
    private String searchName;

    private static final String stype_all = "all";
    private static final String stype_life = "lifeCircle";
    private static final String stype_neighbor = "user";

    ///type={搜索类型：user->搜索邻居，lifeCircle->搜索生活圈，不传type则全部都搜索}

    private TextView search_cancel_tv;
    private ImageButton search_clear;

    private HomeIndexSearchAdapterForLifecircle lifecircleadapter;

    private HomeIndexSearchAdapterForNeighbor neighboradapter;

    /// 生活圈搜索结果
    private List<SearchLifeCircleResultRespBean> lifecircles = new ArrayList<>();
    /// 用户搜索结果
    private List<SearchUserResultRespBean> userresults = new ArrayList<>();

    private int lifepageIndex = 1;

    private int neighborpageIndex = 1;

    private LinearLayout ll_errorpage;
    private LinearLayout ll_nomessage;
    private LinearLayout ll_neterror;

    private TextView tv_getagain;
    /// 初始化页面
    private LinearLayout home_index_search_init_llay;
    //// 搜索结果
    private LinearLayout home_index_search_result_llay;
    /// 搜索邻居结果
    private LinearLayout search_neighbor_result_llay;
    /// 搜索邻居列表结果
    private MyListView search_neighbor_result_lv;
    /// 搜索邻居结果 加载更多
    private LinearLayout search_neighbor_bottom_llay;
    /// 正在刷新邻居
    private RelativeLayout search_neighbor_refreshing_rlay;

    /// 加载更多布局
    private TextView search_neighbor_more_tv;
    /// 搜索生活圈结果
    private LinearLayout search_lifecircle_result_llay;
    /// 搜索生活圈结果 列表
    private MyListView search_lifecircle_result_lv;
    /// 搜索生活圈加载更多
    private LinearLayout search_lifecircle_bottom_llay;
    /// 正在刷新生活圈
    private RelativeLayout search_lifecircle_refreshing_rlay;
    /// 更多生活圈
    private TextView search_lifecircle_more_tv;
    /// 计算ListView 高度
    private Handler handler = new Handler();
    //// 邻居正在加载进度条
    private ProgressBar neighbor_progress_bar;
    /// 生活圈正在加载进度条
    private ProgressBar lifecircle_progress_bar;
    private View home_index_search_dilver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_search_index);
        userbean = PreferencesUtil.getLoginInfo(getmContext());
        initView();
    }

    private void initView() {
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        tv_getagain = (TextView) findViewById(R.id.tv_getagain);
        tv_getagain.setOnClickListener(this);

        ll_nomessage = (LinearLayout) findViewById(R.id.ll_nomessage);
        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);

        home_index_search_init_llay = (LinearLayout) findViewById(R.id.home_index_search_init_llay);
        home_index_search_init_llay.setVisibility(View.VISIBLE);

        home_index_search_dilver = findViewById(R.id.home_index_search_dilver);

        home_index_search_result_llay = (LinearLayout) findViewById(R.id.home_index_search_result_llay);
        home_index_search_result_llay.setVisibility(View.GONE);

        search_neighbor_result_llay = (LinearLayout) findViewById(R.id.search_neighbor_result_llay);
        search_neighbor_result_llay.setVisibility(View.VISIBLE);

        search_neighbor_result_lv = (MyListView) findViewById(R.id.search_neighbor_result_lv);
        neighboradapter = new HomeIndexSearchAdapterForNeighbor(getmContext(), userresults);
        search_neighbor_result_lv.setAdapter(neighboradapter);


        search_neighbor_bottom_llay = (LinearLayout) findViewById(R.id.search_neighbor_bottom_llay);
        search_neighbor_bottom_llay.setVisibility(View.VISIBLE);

        search_neighbor_refreshing_rlay = (RelativeLayout) findViewById(R.id.search_neighbor_refreshing_rlay);

        neighbor_progress_bar = (ProgressBar) findViewById(R.id.neighbor_progress_bar);

        lifecircle_progress_bar = (ProgressBar) findViewById(R.id.lifecircle_progress_bar);


        search_neighbor_more_tv = (TextView) findViewById(R.id.search_neighbor_more_tv);
        search_neighbor_more_tv.setOnClickListener(this);

        search_lifecircle_result_llay = (LinearLayout) findViewById(R.id.search_lifecircle_result_llay);
        search_lifecircle_result_llay.setVisibility(View.GONE);

        search_lifecircle_result_lv = (MyListView) findViewById(R.id.search_lifecircle_result_lv);
        lifecircleadapter = new HomeIndexSearchAdapterForLifecircle(getmContext(), lifecircles);
        search_lifecircle_result_lv.setAdapter(lifecircleadapter);


        search_lifecircle_bottom_llay = (LinearLayout) findViewById(R.id.search_lifecircle_bottom_llay);
        search_lifecircle_bottom_llay.setVisibility(View.VISIBLE);

        search_lifecircle_refreshing_rlay = (RelativeLayout) findViewById(R.id.search_lifecircle_refreshing_rlay);

        search_lifecircle_more_tv = (TextView) findViewById(R.id.search_lifecircle_more_tv);
        search_lifecircle_more_tv.setOnClickListener(this);

        search_cancel_tv = (TextView) findViewById(R.id.search_cancel_tv);
        search_cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        search_clear = (ImageButton) findViewById(R.id.search_clear);
        search_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.setText("");
            }
        });

        query = (EditText) findViewById(R.id.query);
        query.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (query.getText().toString().length() == 0) {
                    search_clear.setVisibility(View.INVISIBLE);

                    home_index_search_init_llay.setVisibility(View.VISIBLE);
                    home_index_search_result_llay.setVisibility(View.GONE);
                    resetSearchResult();

                } else {
                    search_clear.setVisibility(View.VISIBLE);
                    searchName = s.toString();
                    home_index_search_init_llay.setVisibility(View.GONE);

//                    home_index_search_init_llay.setVisibility(View.GONE);
                    /// 全局搜索
//                    getIndexSearchDataList(searchName, stype_all, 1, 1);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        query.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if (query.getText().toString().length() == 0) {
                        search_clear.setVisibility(View.INVISIBLE);
                        showToast("请输入搜索内容");
                        home_index_search_init_llay.setVisibility(View.VISIBLE);
                        home_index_search_result_llay.setVisibility(View.GONE);
                        resetSearchResult();

                    } else {
                        search_clear.setVisibility(View.VISIBLE);
                        searchName = query.getText().toString();
                        home_index_search_init_llay.setVisibility(View.GONE);
//                        home_index_search_result_llay.setVisibility(View.VISIBLE);
                        /// 全局搜索
                        getIndexSearchDataList(searchName, stype_all, 1, 1);
                    }

                }
                return false;
            }
        });


    }

    /**
     * 重置搜索数据
     */
    private void resetSearchResult() {
        if (lifecircles != null) {
            lifecircles.clear();
        } else {
            lifecircles = new ArrayList<>();
        }

        if (userresults != null) {
            userresults.clear();
        } else {
            userresults = new ArrayList<>();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_getagain:
                if (CommonUtils.isNetWorkConnected(getmContext())) {
                    if (TextUtils.isEmpty(searchName)) {
                        showToast("搜索内容不能为空");
                        return;
                    }
                    home_index_search_init_llay.setVisibility(View.GONE);
                    /// 全局搜索
                    getIndexSearchDataList(searchName, stype_all, 1, 1);
                } else {
                    showToast("请检查网络状态");
                }
                break;

            /// 更多邻居  加载后根据有没有数据来显示是否显示 加载更多按钮
            case R.id.search_neighbor_more_tv:

                search_neighbor_more_tv.setVisibility(View.GONE);
                search_neighbor_refreshing_rlay.setVisibility(View.VISIBLE);

                neighborpageIndex++;

                if (TextUtils.isEmpty(searchName)) {
                    searchName = query.getText().toString().trim();
                }
                search_neighbor_result_lv.setSelection(search_neighbor_result_lv.getCount());
                getIndexSearchDataList(searchName, stype_neighbor, 1, neighborpageIndex);

                break;
            /// 更多生活圈  加载后根据有没有数据来显示是否显示 加载更多按钮
            case R.id.search_lifecircle_more_tv:

                search_lifecircle_more_tv.setVisibility(View.GONE);
                search_lifecircle_refreshing_rlay.setVisibility(View.VISIBLE);

                lifepageIndex++;
                if (TextUtils.isEmpty(searchName)) {
                    searchName = query.getText().toString().trim();
                }
                search_lifecircle_result_lv.setSelection(search_lifecircle_result_lv.getCount());

                getIndexSearchDataList(searchName, stype_life, lifepageIndex, 1);

                break;
        }


    }

    interface IndexSearchService {

//        @GET("/api/v2/communities/{communityId}/home/user/{emobId}/serachResult")
//        void getIndexSearchDataList(@Path("communityId") long communityId, @Path("emobId") String emobId, @QueryMap Map<String, String> map, Callback<HomeSearchResultRespBean> cb);
//        @GET("/api/v2/communities/{communityId}/home/user/{emobId}/serachResult")


        ///api/v3/home/search?communityId={小区ID}&emobId={用户环信ID}&q={查询关键字}&type={搜索类型：user->搜索邻居，lifeCircle->搜索生活圈，不传type则全部都搜索}&page={页码}&limit={页面大小}
        @GET("/api/v3/home/search")
        void getIndexSearchDataList(@QueryMap HashMap<String, String> map, Callback<CommonRespBean<HomeSearchResultRespBean>> cb);
    }

    private void getIndexSearchDataList(String s, final String type, int mlifepagerIndex, int mneighborpageIndex) {
        userbean = PreferencesUtil.getLoginInfo(getmContext());
        HashMap<String, String> option = new HashMap<>();
        option.put("q", s);
        option.put("emobId", userbean==null? PreferencesUtil.getTourist(getmContext()):userbean.getEmobId());
        option.put("communityId", "" + PreferencesUtil.getCommityId(getmContext()));
        if (TextUtils.equals(type, stype_all)) {
            option.put("page", "1"); /// 全部类型只显示首页 type 不传
            option.put("limit", "3");
            lifepageIndex = 1;
            neighborpageIndex = 1;
//            option.put("type", type);
        } else if (TextUtils.equals(type, stype_life)) {
            option.put("page", "" + mlifepagerIndex);
            option.put("limit", "10");
            option.put("type", stype_life);
        } else if (TextUtils.equals(type, stype_neighbor)) {
            option.put("page", "" + mneighborpageIndex);
            option.put("limit", "10");
            option.put("type", stype_neighbor);
        }
        // communityId={小区ID}&emobId={用户环信ID}&q={查询关键字}&type={搜索类型：user->搜索邻居，lifeCircle->搜索生活圈，不传type则全部都搜索}&page={页码}&limit={页面大小}
        IndexSearchService service = RetrofitFactory.getInstance().create(getmContext(), option, IndexSearchService.class);
        Callback<CommonRespBean<HomeSearchResultRespBean>> callback = new Callback<CommonRespBean<HomeSearchResultRespBean>>() {
            @Override
            public void success(CommonRespBean<HomeSearchResultRespBean> resultBean, Response response) {
                if (resultBean != null && resultBean.getData() != null && "yes".equals(resultBean.getStatus())) {
                    ll_errorpage.setVisibility(View.GONE);
                    ll_neterror.setVisibility(View.GONE);
                    ll_nomessage.setVisibility(View.GONE);

                    final HomeSearchResultRespBean.UsersEntity users = resultBean.getData().getUsers();
                    final HomeSearchResultRespBean.LifeCirclesEntity lifeCircles = resultBean.getData().getLifeCircles();
                    /// 没有搜索结果
                    if (TextUtils.equals(type, stype_all) && users == null && lifeCircles == null) {
                        ll_errorpage.setVisibility(View.VISIBLE);
                        ll_neterror.setVisibility(View.GONE);
                        ll_nomessage.setVisibility(View.VISIBLE);
                        search_lifecircle_result_llay.setVisibility(View.GONE);
                        search_neighbor_result_llay.setVisibility(View.GONE);
                        home_index_search_dilver.setVisibility(View.GONE);
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (TextUtils.equals(type, stype_all)) {
                                    initLifeCircleListData(lifeCircles, lifepageIndex);
                                    initUsersListData(users, neighborpageIndex);
                                } else if (TextUtils.equals(type, stype_life)) {
                                    initLifeCircleListData(lifeCircles, lifepageIndex);
                                } else if (TextUtils.equals(type, stype_neighbor)) {
                                    initUsersListData(users, neighborpageIndex);
                                }
                            }
                        });

                    }

                } else {
//                    showNetErrorToast();
                    ll_errorpage.setVisibility(View.GONE);
                    ll_neterror.setVisibility(View.GONE);
                    ll_nomessage.setVisibility(View.GONE);
                }
                home_index_search_result_llay.setVisibility(View.VISIBLE);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                showNetErrorToast();

                home_index_search_result_llay.setVisibility(View.VISIBLE);

                if (search_lifecircle_result_lv.getCount() > 0 || search_neighbor_result_lv.getCount() > 0) {

                    if (search_neighbor_result_lv.getCount() > 0 && search_lifecircle_result_lv.getCount() > 0) {
                        home_index_search_dilver.setVisibility(View.VISIBLE);
                    } else {
                        home_index_search_dilver.setVisibility(View.GONE);
                    }

                } else {
                    search_lifecircle_result_llay.setVisibility(View.GONE);
                    search_neighbor_result_llay.setVisibility(View.GONE);

                    home_index_search_dilver.setVisibility(View.GONE);

                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_neterror.setVisibility(View.VISIBLE);
                    ll_nomessage.setVisibility(View.GONE);
                }
            }
        };
        service.getIndexSearchDataList(option, callback);
    }

    /**
     * @param indexSearchDataListBean
     * @param neighborpageIndex
     */
    private void initUsersListData(HomeSearchResultRespBean.UsersEntity indexSearchDataListBean, int neighborpageIndex) {

        if (indexSearchDataListBean != null) {
            List<SearchUserResultRespBean> usersbeans = indexSearchDataListBean.getPageData();

            int size = indexSearchDataListBean.getData().size();


            ////当前页码大于1， 当前搜索结果小于页面大小说明没有数据了
            if (neighborpageIndex>1 && size<indexSearchDataListBean.getLimit()) {
                search_neighbor_more_tv.setVisibility(View.GONE);
            } else {
                search_neighbor_more_tv.setVisibility(View.VISIBLE);
            }

            if (usersbeans != null && usersbeans.size() > 0) {

                if (neighborpageIndex == 1) {
                    userresults.clear();
                }

                userresults.addAll(usersbeans);
                neighboradapter.notifyDataSetChanged();

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        ViewUtils.setListViewHeightBasedOnChildren(search_neighbor_result_lv);
                    }
                });
            } else {
                search_neighbor_more_tv.setVisibility(View.GONE);
            }

            if (search_neighbor_result_lv.getCount() > 0 && search_lifecircle_result_lv.getCount() > 0) {
                home_index_search_dilver.setVisibility(View.VISIBLE);
            } else {
                home_index_search_dilver.setVisibility(View.GONE);
            }


            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    search_neighbor_refreshing_rlay.setVisibility(View.GONE);
                    if (search_neighbor_result_llay != null) {
                        search_neighbor_result_llay.setVisibility(View.VISIBLE);
                    }
                }
            }, 500);

        } else if (userresults.size() > 0) {

            search_neighbor_more_tv.setVisibility(View.GONE);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    search_neighbor_refreshing_rlay.setVisibility(View.GONE);
                    if (search_neighbor_result_llay != null) {
                        search_neighbor_result_llay.setVisibility(View.VISIBLE);
                    }
                }
            }, 500);
        } else {
            ////没有用户信息则
            if (search_neighbor_result_llay != null) {
                search_neighbor_result_llay.setVisibility(View.GONE);
            }
            home_index_search_dilver.setVisibility(View.GONE);
        }

    }

    private void initLifeCircleListData(HomeSearchResultRespBean.LifeCirclesEntity indexSearchDataListBean, int lifepageIndex) {

        if (indexSearchDataListBean != null) {
            List<SearchLifeCircleResultRespBean> lifecirclebeans = indexSearchDataListBean.getData();

            int size = indexSearchDataListBean.getData().size();

            ////当前页码大于1， 当前搜索结果小于页面大小说明没有数据了
            if (lifepageIndex>1 && size<indexSearchDataListBean.getLimit()) {
                search_lifecircle_more_tv.setVisibility(View.GONE);
            } else {
                search_lifecircle_more_tv.setVisibility(View.VISIBLE);
            }

            if (lifecirclebeans.size() > 0) {

                if (lifepageIndex == 1) {
                    lifecircles.clear();
                }

                lifecircles.addAll(lifecirclebeans);
                lifecircleadapter.notifyDataSetChanged();

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        ViewUtils.setListViewHeightBasedOnChildren(search_lifecircle_result_lv);
                    }
                });

            } else {
                search_lifecircle_more_tv.setVisibility(View.GONE);
            }

            if (search_neighbor_result_lv.getCount() > 0 && search_lifecircle_result_lv.getCount() > 0) {
                home_index_search_dilver.setVisibility(View.VISIBLE);
            } else {
                home_index_search_dilver.setVisibility(View.GONE);
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    search_lifecircle_refreshing_rlay.setVisibility(View.GONE);

                    if (search_lifecircle_result_llay != null) {
                        search_lifecircle_result_llay.setVisibility(View.VISIBLE);
                    }
                }
            }, 500);

        } else {
            ////没有生活圈内容则
            if (search_lifecircle_result_llay != null) {
                search_lifecircle_result_llay.setVisibility(View.GONE);
            }
            home_index_search_dilver.setVisibility(View.GONE);

        }

    }
}
