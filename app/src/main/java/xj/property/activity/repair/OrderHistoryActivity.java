package xj.property.activity.repair;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.repo.xw.library.views.PullListView;
import com.repo.xw.library.views.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.ChatActivity;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.adapter.OrderHistoryAdapter;
import xj.property.beans.OrderRepairHistoryBean;
import xj.property.beans.OrderRepairHistoryV3Bean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.CommonUtils;
import xj.property.utils.message.XJContactHelper;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

/**
 * Created by Administrator on 2015/4/9.
 */
public class OrderHistoryActivity extends HXBaseActivity implements PullToRefreshLayout.OnRefreshListener,View.OnClickListener {
    OrderHistoryAdapter adapter;
    List<OrderRepairHistoryV3Bean.OrderRepairHistoryV3DataBean> list;
    private int pageIndex = 1;

    private LinearLayout ll_errorpage;
    private LinearLayout ll_nomessage;
    private LinearLayout ll_neterror;
    private TextView tv_getagain;
    private PullToRefreshLayout pull_to_refreshlayout;
    private PullListView lv_order_history;

    private UserInfoDetailBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        bean = PreferencesUtil.getLoginInfo(getApplicationContext());
        initTitle(null, "历史订单", "");
        initView();
        initListenner();
        initData();
    }

    private void initListenner() {
        pull_to_refreshlayout.setOnRefreshListener(this);
        tv_getagain.setOnClickListener(this);
        lv_order_history.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (lv_order_history.getLastVisiblePosition() == (lv_order_history.getCount() - 1)) {
                            pull_to_refreshlayout.autoLoad();
                        }
                        // 判断滚动到顶部
                        if (lv_order_history.getFirstVisiblePosition() == 0) {

                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    private void initData() {
        adapter = new OrderHistoryAdapter(this, list, new OrderHistoryAdapter.CallBack() {
            @Override
            public void call(OrderRepairHistoryV3Bean.OrderRepairHistoryV3DataBean bean) {
                if (PreferencesUtil.getLogin(OrderHistoryActivity.this)) {
                    Intent intentPush = new Intent();
                    intentPush.setClass(OrderHistoryActivity.this, RepairChatActivity.class);
                    intentPush.putExtra("userId", bean.getEmobIdShop());//tz
                    intentPush.putExtra(Config.EXPKey_nickname, bean.getShopName());
                    intentPush.putExtra(Config.EXPKey_avatar, bean.getShopLogo());
                    intentPush.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                    XJContactHelper.saveContact(bean.getEmobIdShop(), bean.getShopName(), bean.getShopLogo(), "4");
                    startActivity(intentPush);
                } else {
                    Intent intent = new Intent(OrderHistoryActivity.this, RegisterLoginActivity.class);
                    startActivity(intent);
                }

            }
        });
        if (!CommonUtils.isNetWorkConnected(this)) {
            ll_errorpage.setVisibility(View.VISIBLE);
            ll_neterror.setVisibility(View.VISIBLE);
            ll_nomessage.setVisibility(View.GONE);
        } else {
            ll_errorpage.setVisibility(View.GONE);
            getOrderListInfo();
            pull_to_refreshlayout.autoRefresh();
        }
    }

    private void initView() {
        list = new ArrayList<>();
        pull_to_refreshlayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refreshlayout);
        lv_order_history = (PullListView) findViewById(R.id.lv_order_history);
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        tv_getagain = (TextView) findViewById(R.id.tv_getagain);
        ll_nomessage = (LinearLayout) findViewById(R.id.ll_nomessage);
        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pageIndex = 1;
        new GetDataTask().execute();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        if (pageIndex == 1) {
            lv_order_history.setSelection(lv_order_history.getCount());
        }
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
        switch (v.getId()){
            case R.id.tv_getagain:
                if (!CommonUtils.isNetWorkConnected(OrderHistoryActivity.this)) {
                    return;
                } else {
                    ll_errorpage.setVisibility(View.GONE);
                    getOrderListInfo();
                    pull_to_refreshlayout.autoRefresh();
                }
                break;
        }
    }

    interface getOrderListService {
        ///http://114.215.105.202:8080/api/v1/communities/{communityId}/users/{emobId}/orderHistories?q={sortId:模块类别}?pageNum=1&pageSize=10
        ///api/v1/communities/1/shopsOrderHistory/{emobId}/{sort}?pageNum=1&pageSize=10
        // @GET("/api/v1/communities/{communityId}/shopsOrderHistory/{emobId}/{sort}?pageNum=1&pageSize=10")
        @GET("/api/v1/communities/{communityId}/users/{emobId}/orderHistories")
        void getOrderListInfo(@Path("communityId") long communityId, @Path("emobId") String emobId, @QueryMap Map<String, String> map, Callback<OrderRepairHistoryBean> cb);
    // /api/v3/repairs/orders

        @GET("/api/v3/repairs/orders")
        void getOrderListInfoV3(@QueryMap Map<String, String> map, Callback<CommonRespBean<OrderRepairHistoryV3Bean>> cb);

    }

    private void getOrderListInfo() {
        HashMap<String, String> option = new HashMap<String, String>();
        option.put("emobId", bean.getEmobId());
        option.put("page", "1");
        option.put("limit", "10");
        getOrderListService service = RetrofitFactory.getInstance().create(getApplicationContext(), option, getOrderListService.class);
        Callback<CommonRespBean<OrderRepairHistoryV3Bean>> callback = new Callback<CommonRespBean<OrderRepairHistoryV3Bean>>() {
            @Override
            public void success(CommonRespBean<OrderRepairHistoryV3Bean> bean, retrofit.client.Response response) {
                if (bean != null && "yes".equals(bean.getStatus())) {
                    list.clear();
                    list.addAll(bean.getData().getData());
                    if (list.size() != 0) {
                        ll_errorpage.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    } else {
                        ll_errorpage.setVisibility(View.VISIBLE);
                        ll_neterror.setVisibility(View.GONE);
                        ll_nomessage.setVisibility(View.VISIBLE);
                    }
                } else {
                    showNetErrorToast();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                showNetErrorToast();
                error.printStackTrace();
            }
        };
        service.getOrderListInfoV3(option, callback);
    }

    protected void getData(final int pageIndex) {
        HashMap<String, String> option = new HashMap<String, String>();
        option.put("emobId", bean.getEmobId());
        option.put("page", pageIndex+"");
        option.put("limit", "10");
        getOrderListService service = RetrofitFactory.getInstance().create(getApplicationContext(),option,getOrderListService.class);
        Callback<CommonRespBean<OrderRepairHistoryV3Bean>> callback = new Callback<CommonRespBean<OrderRepairHistoryV3Bean>>() {
            @Override
            public void success(CommonRespBean<OrderRepairHistoryV3Bean> bean, retrofit.client.Response response) {

                if (bean != null && TextUtils.equals("yes", bean.getStatus())) {
                    List<OrderRepairHistoryV3Bean.OrderRepairHistoryV3DataBean> pageData = bean.getData().getData();
                    if (list.size() > 0 && pageData == null || pageData.size() < 1) {
                        showNoMoreToast();
                    }
                    if (pageIndex == 1) {
                        list.clear();
                        list.addAll(pageData);
                        adapter.notifyDataSetChanged();

                    } else if (bean.getData().getPage() >= pageIndex) {
                        if (list != null) {
                            list.addAll(pageData);
                            adapter.notifyDataSetChanged();
                        }
                    }

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
        service.getOrderListInfoV3(option, callback);
    }

}
