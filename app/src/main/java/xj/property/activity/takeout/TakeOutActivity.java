package xj.property.activity.takeout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import xj.property.adapter.TakeoutAdapter;
import xj.property.beans.TakeoutBean;
import xj.property.beans.TakeoutListBean;
import xj.property.utils.other.Config;

public class TakeOutActivity extends HXBaseActivity {
    /**
     * logger
     */
    /**
     * widgets activities page
     */
    private ListView lv_takeouts;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TakeoutAdapter takeoutAdapter;
    /**
     * if load nest
     */
    private boolean isLoadNext = false;

    /**
     * activities data to show
     */
    List<TakeoutBean> takeoutBeanList = new ArrayList<TakeoutBean>();
    /**
     * page index
     */
    private int pageIndex = 1;

    private static Map<String, String> options = new HashMap<String, String>();

    static {
        options.put("pageNum","1");
        options.put("pageSize","10");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_out);
        initView();
        getTakeoutList();
    }



    public void initView(){
        //init swipeRefreshLayout
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeResources(R.color.slateblue);
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

        //init listview
        lv_takeouts = (ListView)findViewById(R.id.lv_takeouts);
        takeoutAdapter = new TakeoutAdapter(this, takeoutBeanList);
        lv_takeouts.setAdapter(takeoutAdapter);
        lv_takeouts.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intentPush = new Intent();
                intentPush.setClass(TakeOutActivity.this,TakeoutDetailActivity.class);
//                intentPush.putExtra("rid", resourceDataToShowModelList.get(i).getId());
                startActivity(intentPush);
            }
        });

        lv_takeouts.setOnScrollListener(new AbsListView.OnScrollListener()
        {

            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState)
            {

            }

            @Override
            public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                if (firstVisibleItem + visibleItemCount == totalItemCount)
                {

                    if (isLoadNext == true)
                    {
                        isLoadNext = false;

                        // 到了底部
                        if (takeoutBeanList.size() == 0)
                        {
                            return;
                        }
                        // weather reach the max number of this page
                        // if (resourceDataToShowModelList.size() == 60)
                        if (pageIndex % 6 == 0)
                        {
                            // btn_load_next_page.setVisibility(View.VISIBLE);
                            ImageView iv_refresh = (ImageView) lv_takeouts.findViewById(R.id.iv_refresh);
                            iv_refresh.setVisibility(View.GONE);
                            TextView tv_refresh = (TextView) (lv_takeouts.findViewById(R.id.tv_loading));
                            tv_refresh.setText("点击加载更多");
                            tv_refresh.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View arg0)
                                {
                                    // btn_load_next_page
                                    // .setVisibility(View.GONE);
//                                    isPullDown = false;
//                                    resourcePageIndex++;
//                                    map_param_search.put("page", String.valueOf(resourcePageIndex));
//                                    // clear data for this page
//                                    resourceDataToShowModelList.clear();
//                                    getNewResource(1);
                                }
                            });
                        } else
                        {
//                            isPullDown = false;
//                            resourcePageIndex++;
//                            map_param_search.put("page", String.valueOf(resourcePageIndex));
//                            getNewResource(0);
                        }
                    }
                } else
                {
                    isLoadNext = true;
                }
            }
        });
    }

    interface TakeoutListService {
        @GET("/api/v1/communities/{communityId}/shops/{shopId}?pageNum=1&pageSize=10")
        void listShops(@Path("communityId") int communityId, @Path("shopId") int shopId, @QueryMap Map<String, String> options, Callback<TakeoutListBean> cb);
    }

    private void getTakeoutList(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Config.NET_BASE)
                .build();
        TakeoutListService takeoutListService = restAdapter.create(TakeoutListService.class);

        Callback<TakeoutListBean> callback = new Callback<TakeoutListBean>() {
            @Override
            public void success(TakeoutListBean takeoutListBean, Response response) {
                takeoutBeanList.clear();
                takeoutBeanList.addAll(takeoutListBean.getPageData());
                takeoutAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                showNetErrorToast();
            }
        };
        takeoutListService.listShops(1,1,options, callback);
    }

    @Override
    public void onClick(View v) {

    }
}
