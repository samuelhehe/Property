package xj.property.activity.repair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.activities.ActivityDetailActivity;
import xj.property.adapter.RepairmentClassAdapter;
import xj.property.beans.RepairmentClassBean;
import xj.property.beans.RepairmentClassListBean;
import xj.property.utils.other.Config;

public class RepairmentClassActivity extends HXBaseActivity {
    /**
     * logger
     */
    /**
     * swipe layout
     */
    private SwipeRefreshLayout swipeRefreshLayout;
    /**
     * listview of repair classed
     */
    private ListView lv_repair_classes;

    /**
     * load moreview
     */
    private View loadMoreView;
    /**
     * adapter
     */
    private RepairmentClassAdapter repairmentClassAdapter;
    /**
     * bean list
      */
    private List<RepairmentClassBean> repairmentClassBeanList = new ArrayList<RepairmentClassBean>();

    /**
     * if load next
     */
    private boolean isLoadNext = false;
    /**
     * last item index
     */
    private int lastItemIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);
        initView();
        initDate();
        initListenner();
        getRepairmentClasses();
    }

    private void initListenner() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final Handler handler = new Handler()
                {
                    @Override
                    public void handleMessage(Message msg)
                    {
                        super.handleMessage(msg);
                        // btn_load_next_page.setVisibility(View.GONE);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                };
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            Thread.sleep(1600);
                            handler.sendEmptyMessage(0);
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        lv_repair_classes.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intentPush = new Intent();
                Bundle bundle = new Bundle();
                intentPush.putExtras(bundle);
                intentPush.setClass(RepairmentClassActivity.this,ActivityDetailActivity.class);
                startActivity(intentPush);
            }
        });

        lv_repair_classes.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void initDate() {
        swipeRefreshLayout.setColorSchemeResources(R.color.slateblue);
        loadMoreView.setVisibility(View.GONE);
        Button btn_loadMore = (Button) loadMoreView.findViewById(R.id.bt_load);
        ProgressBar pg = (ProgressBar) loadMoreView.findViewById(R.id.pg);
        lv_repair_classes.addFooterView(loadMoreView);
        repairmentClassAdapter = new RepairmentClassAdapter(this, repairmentClassBeanList);
        lv_repair_classes.setAdapter(repairmentClassAdapter);
    }

    /**
     * init view
     */
    public void initView(){
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        lv_repair_classes = (ListView)findViewById(R.id.lv_repair_classes);
        LayoutInflater inflater = getLayoutInflater();
        loadMoreView = inflater.inflate(R.layout.footview_lv, null);
    }

    /**
     * interface for repairment class service
     */
    interface RepairmentClassListService {
        @GET("/api/v1/communities/{communityId}/users/{userId}/itemCategory/byType/{type}")
        void listRepairmentClasses(@Path("communityId") int communityId, @Path("userId") int userId, @Path("type") int type, Callback<RepairmentClassListBean> cb);
    }

    private void getRepairmentClasses(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Config.NET_BASE)
                .build();

        RepairmentClassListService repairmentClassListService = restAdapter.create(RepairmentClassListService.class);

        Callback<RepairmentClassListBean> callback = new Callback<RepairmentClassListBean>() {
            @Override
            public void success(RepairmentClassListBean repairmentClassListBean, Response response) {

                repairmentClassBeanList.clear();
                repairmentClassBeanList.addAll(repairmentClassListBean.getInfo());
                repairmentClassAdapter.notifyDataSetChanged();
            }
            @Override
            public void failure(RetrofitError error) {
                showNetErrorToast();
            }
        };
        repairmentClassListService.listRepairmentClasses(1,123,5,callback);
    }
}
