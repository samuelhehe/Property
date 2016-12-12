package xj.property.activity.membership;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
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
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.beans.MspCardShopBuyedUsers;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.other.Config;

/**
 * 查看更多已购买用户
 */
public class ActivityMSPBuyedMoreUsers extends HXBaseActivity implements PullToRefreshLayout.OnRefreshListener {


    private static final String TAG = "ActivityMSPBuyedMoreUsers";
    private String shopemobid;

    private MyAdapter adapter = new MyAdapter();

    private int pageNum = 1;
    private int count;


    private String pageSize = "10";

    private List<MspCardShopBuyedUsers.PageDataEntity> pageData = new ArrayList<>();


    private View headView;

    private LinearLayout ll_errorpage;
    private LinearLayout ll_nomessage;
    private LinearLayout ll_neterror;
    private ImageView iv_nomessage_image;
    private PullToRefreshLayout pull_to_refreshlayout;
    private PullListView buyed_usrs_lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msp_buyed_more_users);
        shopemobid = getIntent().getStringExtra("shopvipcardid");

        initView();
        initData();
    }

    private void initData() {
        pull_to_refreshlayout.autoRefresh();
    }

    private void initView() {
        TextView tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("查看已购用户");
        tv_title.setVisibility(View.VISIBLE);
        this.findViewById(R.id.iv_back).setOnClickListener(this);


        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        ll_nomessage = (LinearLayout) findViewById(R.id.ll_nomessage);
        iv_nomessage_image = (ImageView) findViewById(R.id.iv_nomessage_image);
        iv_nomessage_image.setImageResource(R.drawable.tikuanjilu_people);
        ll_neterror.setOnClickListener(this);

        pull_to_refreshlayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refreshlayout);
        pull_to_refreshlayout.setOnRefreshListener(this);

        buyed_usrs_lv = (PullListView) findViewById(R.id.buyed_usrs_lv);
        buyed_usrs_lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (buyed_usrs_lv.getLastVisiblePosition() == (buyed_usrs_lv.getCount() - 1)) {
                            pull_to_refreshlayout.autoLoad();
                        }
                        // 判断滚动到顶部
                        if (buyed_usrs_lv.getFirstVisiblePosition() == 0) {

                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });


        if (headView == null) {
            headView = View.inflate(this, R.layout.circyle_headview, null);
            buyed_usrs_lv.addHeaderView(headView);
        }

        buyed_usrs_lv.setAdapter(adapter);

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pageNum = 1;
        getShopVipcardsOrders(shopemobid);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        pageNum++;
        getShopVipcardsOrders(shopemobid);
    }

    interface WelfareBuyedMoreUsrsService {

//        @GET("/api/v1/shopVipcards/orders/{shopEmobId}/users")
//        void getShopVipcardsOrders(@Path("shopEmobId") String shopEmobId, @QueryMap Map<String, String> map, Callback<MspCardShopBuyedUsers> cb);
//        @GET("/api/v1/shopVipcards/orders/{shopEmobId}/users")

        ///api/v3/nearbyVipcards/{店家环信ID}/users?page={页码}&limit={页面大小}
        @GET("/api/v3/nearbyVipcards/{shopEmobId}/users")
        void getShopVipcardsOrders(@Path("shopEmobId") String shopEmobId, @QueryMap Map<String, String> map, Callback<CommonRespBean<MspCardShopBuyedUsers>> cb);
    }

    private void getShopVipcardsOrders(String shopEmobId) {

        HashMap<String, String> option = new HashMap<String, String>();
        option.put("page", "" + pageNum);
        option.put("limit", pageSize);
//        page={页码}&limit={页面大小}

        WelfareBuyedMoreUsrsService service = RetrofitFactory.getInstance().create(getmContext(),option,WelfareBuyedMoreUsrsService.class);
        Callback<CommonRespBean<MspCardShopBuyedUsers>> callback = new Callback<CommonRespBean<MspCardShopBuyedUsers>>() {
            @Override
            public void success(CommonRespBean<MspCardShopBuyedUsers> bean, retrofit.client.Response response) {

                if (bean != null) {
                    if ("yes".equals(bean.getStatus())) {

                        ll_errorpage.setVisibility(View.GONE);
                        ll_nomessage.setVisibility(View.GONE);
                        ll_neterror.setVisibility(View.GONE);

                        List<MspCardShopBuyedUsers.PageDataEntity> pageDatanew = bean.getData().getPageData();
                        if (pageData.size() > 0) {
                            if (pageDatanew == null || pageDatanew.size() < 1) {
                                showNoMoreToast();
                            }
                        }

                        if (pageNum == 1) {
                            pageData.clear();
                            pageData.addAll(pageDatanew);
                        } else {
                            pageData.addAll(pageDatanew);
                        }
                        count = adapter.getCount();
                        adapter.notifyDataSetChanged();

                    } else {
                        showToast(bean.getMessage());
                    }
                } else {
                    showToast("数据异常");
                    if (count == 0) {
                        ll_errorpage.setVisibility(View.VISIBLE);
                        ll_nomessage.setVisibility(View.VISIBLE);
                    }
                }

                if (pageNum == 1) {
                    pull_to_refreshlayout.refreshFinish(true);
                } else {
                    pull_to_refreshlayout.loadMoreFinish(true);
                }

            }

            @Override
            public void failure(RetrofitError error) {
                if (count == 0) {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_neterror.setVisibility(View.VISIBLE);
                }

                if (pageNum == 1) {
                    pull_to_refreshlayout.refreshFinish(true);
                } else {
                    pull_to_refreshlayout.loadMoreFinish(true);
                }
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        service.getShopVipcardsOrders(shopEmobId, option, callback);
    }


    private class MyAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return pageData.size();
        }

        @Override
        public Object getItem(int position) {
            return pageData;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(ActivityMSPBuyedMoreUsers.this, R.layout.common_mspcard_purchase_moreusr_item, null);
                viewHolder = new ViewHolder(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Log.i("debbug", "size=" + pageData.size());
            Log.i("debbug", "viewHolder=" + viewHolder);
            Log.i("debbug", "viewholder.tvname=" + viewHolder.welfare_purchase_hasgoturs_name_tv);

            viewHolder.welfare_purchase_hasgoturs_name_tv.setText("" + pageData.get(position).getNickname());
            ImageLoader.getInstance().displayImage(pageData.get(position).getAvatar(), viewHolder.iv_avatar, options);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("debug ", "adapter.getCount" + adapter.getCount() + " pageData.size(" + pageData.size() + "position " + position);

                    startActivity(new Intent(ActivityMSPBuyedMoreUsers.this, UserGroupInfoActivity.class).putExtra(Config.INTENT_PARMAS2, pageData.get(position).getEmobId()));
                }
            });


            return convertView;
        }


        class ViewHolder {
            ImageView iv_avatar;
            TextView welfare_purchase_hasgoturs_name_tv;

            ViewHolder(View v) {
                iv_avatar = (ImageView) v.findViewById(R.id.iv_avatar);
                welfare_purchase_hasgoturs_name_tv = (TextView) v.findViewById(R.id.welfare_purchase_hasgoturs_name_tv);
                v.setTag(this);
            }
        }

        private DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.head_portrait_personage)
                .showImageForEmptyUri(R.drawable.head_portrait_personage)
                .showImageOnFail(R.drawable.head_portrait_personage)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_neterror:

                getShopVipcardsOrders(shopemobid);

                break;
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_activity_welfare_buyed_more_users, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


}
