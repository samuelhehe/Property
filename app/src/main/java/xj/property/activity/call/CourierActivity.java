package xj.property.activity.call;

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
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.adapter.CourierAdapter;
import xj.property.beans.CourierListBean;
import xj.property.beans.ExpressAddressBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.CommonUtils;
import xj.property.utils.DensityUtil;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

/**
 * Created by Administrator on 2015/3/25.
 */
public class CourierActivity extends HXBaseActivity implements PullToRefreshLayout.OnRefreshListener {
    private int pageIndex = 1;
    private int pageSize = 10;
    private CourierAdapter adapter;

    private LinearLayout ll_errorpage;
    private LinearLayout ll_nomessage;
    private LinearLayout ll_neterror;
    private TextView tv_getagain;

    ArrayList<ExpressAddressBean.CourierDetailBean> couriers = new ArrayList<ExpressAddressBean.CourierDetailBean>();
    private PullToRefreshLayout pull_to_refreshlayout;
    private PullListView lv_couriers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);
        initView();
        initTitle(null, "快递", "");
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
                    if (!CommonUtils.isNetWorkConnected(CourierActivity.this)) {
                        return;
                    } else {
                        ll_errorpage.setVisibility(View.GONE);
                        pull_to_refreshlayout.autoRefresh();
                        getExpressAddress();
                    }
                }
            });
        } else {
            ll_errorpage.setVisibility(View.GONE);
            pull_to_refreshlayout.autoRefresh();
            getExpressAddress();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    View headerView;

    private void initView() {
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        tv_getagain = (TextView) findViewById(R.id.tv_getagain);
        ll_nomessage = (LinearLayout) findViewById(R.id.ll_nomessage);
        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);

        pull_to_refreshlayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refreshlayout);
        pull_to_refreshlayout.setOnRefreshListener(this);
        lv_couriers = (PullListView) findViewById(R.id.lv_couriers);
        lv_couriers.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (lv_couriers.getLastVisiblePosition() == (lv_couriers.getCount() - 1)) {
                            pull_to_refreshlayout.autoLoad();
                        }
                        // 判断滚动到顶部
                        if (lv_couriers.getFirstVisiblePosition() == 0) {

                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


            }
        });

        adapter = new CourierAdapter(CourierActivity.this, couriers);

        headerView = View.inflate(this, R.layout.item_courier_one, null);
        headerView.setVisibility(View.GONE);
//        ((TextView)headerView.findViewById(R.id.tv_address)).setText(PreferencesUtil.getCommityName(this)+"物业处");
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, DensityUtil.dip2px(getmContext(), 195f));
        headerView.setLayoutParams(params);
        lv_couriers.addHeaderView(headerView);
        //// add headerView before lv_couriers  2015/12/28
        lv_couriers.setAdapter(adapter);
    }


    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pageIndex = 1;
        getExpressAddress();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        if (pageIndex == 1) {
            lv_couriers.setSelection(lv_couriers.getCount());
        }
        pageIndex++;
        getExpressAddress();
    }


    @Override
    public void onClick(View v) {

    }

    /**
     * 获取ActList部分
     */
    interface ExpressAddressService {
        ///api/v1/communities/{communityId}/communityExpress?q=max
//        @GET("/api/v1/communities/{communityId}/communityExpress?q=max")
//        void getCallList(@Path("communityId") long communityId, Callback<ExpressAddressBean> cb);
//        @GET("/api/v1/communities/{communityId}/communityExpress?q=max")

        ///api/v3/communities/{小区ID}/expresses?page={页码}&limit={页面大小}
        @GET("/api/v3/communities/{communityId}/expresses")
        void getCallList(@Path("communityId") int communityId, @QueryMap HashMap<String, String> map, Callback<CommonRespBean<ExpressAddressBean>> cb);

    }

    private void getExpressAddress() {

        HashMap<String, String> map = new HashMap<>();
        map.put("page", "" + pageIndex);
        map.put("limit", "" + pageSize);
//                page={页码}&limit={页面大小}
        ExpressAddressService service = RetrofitFactory.getInstance().create(getmContext(), map, ExpressAddressService.class);
        Callback<CommonRespBean<ExpressAddressBean>> callback = new Callback<CommonRespBean<ExpressAddressBean>>() {
            @Override
            public void success(CommonRespBean<ExpressAddressBean> bean, retrofit.client.Response response) {
                if (null != bean && "yes".equals(bean.getStatus())) {
                    if (pageIndex == 1) {
                        String expressOffice = bean.getField("expressOffice", String.class);
                        if (TextUtils.isEmpty(expressOffice)) {
                            headerView.setVisibility(View.GONE);
                            lv_couriers.removeHeaderView(headerView);
                        } else {
                            ((TextView) headerView.findViewById(R.id.tv_address)).setText(expressOffice);
                            headerView.setVisibility(View.VISIBLE);
                        }
                    }
                    if (bean.getData() != null && bean.getData().getData() != null) {
                        List<ExpressAddressBean.CourierDetailBean> courierDetailBeans = bean.getData().getData();
                        if (pageIndex == 1) {
                            couriers.clear();
                        } else {
                            if (courierDetailBeans.size() < pageSize) {
                                showNoMoreToast();
                            }
                        }
                        couriers.addAll(courierDetailBeans);
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    showNetErrorToast();
                }
                if(couriers==null||couriers.size()<=0){
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_neterror.setVisibility(View.GONE);
                    ll_nomessage.setVisibility(View.VISIBLE);
                }else{
                    ll_errorpage.setVisibility(View.GONE);
                }
                if (pageIndex == 1) {
                    pull_to_refreshlayout.refreshFinish(true);
                } else {
                    pull_to_refreshlayout.loadMoreFinish(true);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (pageIndex == 1) {
                    pull_to_refreshlayout.refreshFinish(true);
                } else {
                    pull_to_refreshlayout.loadMoreFinish(true);
                }
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        service.getCallList(PreferencesUtil.getCommityId(this), map, callback);
    }


}
