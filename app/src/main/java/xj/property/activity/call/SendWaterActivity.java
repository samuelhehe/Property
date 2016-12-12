package xj.property.activity.call;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
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
import xj.property.adapter.SendWaterAdapter;
import xj.property.beans.SendWaterListBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.CommonUtils;
import xj.property.utils.other.PreferencesUtil;

/**
 * Created by Administrator on 2015/3/25.
 * 送水
 * v3 2016/03/21
 */
public class SendWaterActivity extends HXBaseActivity implements PullToRefreshLayout.OnRefreshListener {
    private int pageIndex = 1;
    private SendWaterAdapter adapter;

    private FrameLayout fl_error;
    private LinearLayout ll_errorpage;
    private LinearLayout ll_nomessage;
    private LinearLayout ll_neterror;
    private TextView tv_getagain;

    private ArrayList<SendWaterListBean.DataEntity> waterSattions = new ArrayList<SendWaterListBean.DataEntity>();
    private PullToRefreshLayout pull_to_refreshlayout;
    private PullListView lv_water_stations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_water);
        initView();
        initTitle(null, "送水", "");
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
                    if (!CommonUtils.isNetWorkConnected(SendWaterActivity.this)) {
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

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pageIndex = 1;
        new GetDataTask().execute();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        if (pageIndex == 1) {
            lv_water_stations.setSelection(lv_water_stations.getCount());
        }
        pageIndex++;
        new GetDataTask().execute();
    }

    private void initView() {
        fl_error = (FrameLayout) findViewById(R.id.fl_error);
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        tv_getagain = (TextView) findViewById(R.id.tv_getagain);
        ll_nomessage = (LinearLayout) findViewById(R.id.ll_nomessage);
        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);

        pull_to_refreshlayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refreshlayout);
        pull_to_refreshlayout.setOnRefreshListener(this);
        lv_water_stations = (PullListView) findViewById(R.id.lv_water_stations);
        lv_water_stations.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (lv_water_stations.getLastVisiblePosition() == (lv_water_stations.getCount() - 1)) {
                            pull_to_refreshlayout.autoLoad();
                        }
                        // 判断滚动到顶部
                        if (lv_water_stations.getFirstVisiblePosition() == 0) {

                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        adapter = new SendWaterAdapter(this, waterSattions);
        lv_water_stations.setAdapter(adapter);


        lv_water_stations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent phoneIntent = new Intent(
                        "android.intent.action.CALL", Uri.parse("tel:"
                        + waterSattions.get(position).getPhone()));
                startActivity(phoneIntent);
            }
        });
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
    interface CallListService {
        ///api/v1/communities/1/shops/4?pageNum=1&pageSize=10
        //http://114.215.105.202:8080/api/v1/communities/{communityId}/shops?q={catId}&pageNum=1&pageSize=10
        //http://114.215.114.56:8080/api/v1/communities/1/shops?q=4&pageNum=1&pageSize=10
//        @GET("/api/v1/communities/{communityId}/shops")
//        void getCallList(@Path("communityId") long communityId, @QueryMap Map<String, String> map, Callback<SendWaterListBean> cb);
//        @GET("/api/v1/communities/{communityId}/shops")
//        /api/v3/communities/{小区ID}/waterShops?page={页码}&limit={页面大小}
        @GET("/api/v3/communities/{communityId}/waterShops")
        void getCallList(@Path("communityId") int communityId, @QueryMap Map<String, String> map, Callback<CommonRespBean<SendWaterListBean>> cb);
    }

    private void getResumeActivities() {
        HashMap<String, String> option = new HashMap<String, String>();
        option.put("page", "1");
        option.put("limit", "10");
        CallListService service = RetrofitFactory.getInstance().create(getmContext(), option, CallListService.class);
        Callback<CommonRespBean<SendWaterListBean>> callback = new Callback<CommonRespBean<SendWaterListBean>>() {
            @Override
            public void success(CommonRespBean<SendWaterListBean> bean, retrofit.client.Response response) {
                if (null == bean || !"yes".equals(bean.getStatus())) return;
                waterSattions.clear();
                pageIndex = 1;
                waterSattions.addAll(bean.getData().getData());
                if (waterSattions != null && waterSattions.size() != 0) {
                    ll_errorpage.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                } else {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_neterror.setVisibility(View.GONE);
                    ll_nomessage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
//                error.printStackTrace();
//                showToast(error.toString());
                showNetErrorToast();
            }
        };

        service.getCallList(PreferencesUtil.getCommityId(this), option, callback);
    }

    protected void getData(final int pageIndex) {
        HashMap<String, String> option = new HashMap<String, String>();
        option.put("page", ""+pageIndex);
        option.put("limit", "10");

        CallListService service = RetrofitFactory.getInstance().create(getmContext(),option,CallListService.class);
        Callback<CommonRespBean<SendWaterListBean>> callback = new Callback<CommonRespBean<SendWaterListBean>>() {
            @Override
            public void success(CommonRespBean<SendWaterListBean> bean, retrofit.client.Response response) {
                if (bean != null && TextUtils.equals("yes", bean.getStatus())) {

                    List<SendWaterListBean.DataEntity> pageData = bean.getData().getData();
                    if (waterSattions.size() > 0) {
                        if (pageData == null || pageData.size() <= 0) {
                            showNoMoreToast();
                        }
                    }
                    if (pageIndex == 1) {
                        waterSattions.clear();
                        waterSattions.addAll(bean.getData().getData());
                        adapter.notifyDataSetChanged();
                    } else if (bean.getData().getPage() >= pageIndex) {
                        if (waterSattions != null) {
                            waterSattions.addAll(bean.getData().getData());
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
            }
        };
        service.getCallList(PreferencesUtil.getCommityId(this), option, callback);
    }


}
