package xj.property.activity.surrounding;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.utils.DistanceUtil;
import com.repo.xw.library.views.PullListView;
import com.repo.xw.library.views.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.utils.other.Arith;

/**
 *
 */
public class ActivitySrroundingMapList extends HXBaseActivity implements OnGetPoiSearchResultListener, PullToRefreshLayout.OnRefreshListener {


    private static final String TAG = "ActivityMSPBuyedMoreUsers";
    private static final int SEARCH_RADIUS = 10000;

    private MyAdapter adapter = new MyAdapter();


    private int pageNum = 1;

    private int count;

    private List<PoiInfo> pageData = new ArrayList<PoiInfo>();


    private View headView;

    private LinearLayout ll_errorpage;
    private LinearLayout ll_nocontent;
    private LinearLayout ll_neterror;

    //// 搜索关键字
    private String mKeys;

    private double latitude;

    private double longitude;

    private PoiSearch mPoiSearch;

    private TextView ll_nocontent_msg_tv;
    private boolean isLoadComplete = false;
    private PullToRefreshLayout pull_to_refreshlayout;
    private PullListView buyed_usrs_lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srroundmaplist);
        SDKInitializer.initialize(this.getApplicationContext());
        mKeys = getIntent().getStringExtra("searchmKeys");

        latitude = getIntent().getDoubleExtra("latitude", 0.0f);
        longitude = getIntent().getDoubleExtra("longitude", 0.0f);

        if (TextUtils.isEmpty(mKeys) || latitude <= 0.0f || longitude <= 0.0f) {
            showToast("检索信息异常");
            finish();
        }
        initView();
        searchData();
//        pull_to_refreshlayout.autoRefresh();
    }


    private void searchData() {
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        LatLng latLng = new LatLng(latitude, longitude);
        mPoiSearch.searchNearby(new PoiNearbySearchOption().keyword(mKeys).location(latLng).radius(SEARCH_RADIUS).sortType(PoiSortType.distance_from_near_to_far).pageCapacity(10).pageNum(pageNum));
    }

    private void initView() {

        TextView tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("" + mKeys);
        tv_title.setVisibility(View.VISIBLE);
        this.findViewById(R.id.iv_back).setOnClickListener(this);

        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        ll_nocontent = (LinearLayout) findViewById(R.id.ll_nocontent);
        ll_nocontent_msg_tv = (TextView) findViewById(R.id.ll_nocontent_msg_tv);
        ll_nocontent_msg_tv.setText("无法请求到数据");
        ll_neterror.setOnClickListener(this);

        pull_to_refreshlayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refreshlayout);
        pull_to_refreshlayout.setOnRefreshListener(this);

        buyed_usrs_lv = (PullListView) findViewById(R.id.buyed_usrs_lv);
//        pull_listview.setPullUpEnable(false);

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
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult != null) {
            List<PoiInfo> poiInfoList = poiResult.getAllPoi();

            if (pageData != null && pageData.size() > 0) {
                if (poiInfoList == null || poiInfoList.size() <= 0) {
                    showNoMoreToast();
                }
            }

            if (poiInfoList == null || poiInfoList.size() <= 0) {
//                showToast(R.string.no_more);
                isLoadComplete = false;
            } else {
                setNetworkContentOkView();

                if (pageNum == 1) {
                    pageData.clear();
                    pageData.addAll(poiInfoList);
                } else {
                    pageData.addAll(poiInfoList);
                }
                isLoadComplete = true;

                count = adapter.getCount();
                adapter.notifyDataSetChanged();
            }


        } else {
            isLoadComplete = false;
            setNetworkErrorView();
            showToast("数据异常");
            if (count == 0) {
                setnoContentView();
            }
        }

        if (pageNum == 1) {
            pull_to_refreshlayout.refreshFinish(true);
        } else {
            pull_to_refreshlayout.loadMoreFinish(true);
        }

        /// 百度地图周边首次加载没数据
        if (!isLoadComplete) {
            searchData();
        }

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {


    }


    private void setNetworkErrorView() {
        if (ll_errorpage != null && ll_neterror != null) {
            ll_neterror.setVisibility(View.VISIBLE);
            ll_errorpage.setVisibility(View.VISIBLE);
        }
    }

    private void setNetworkContentOkView() {
        if (ll_errorpage != null && ll_neterror != null) {
            ll_errorpage.setVisibility(View.GONE);
        }
    }

    private void setnoContentView() {
        if (ll_errorpage != null && ll_nocontent != null) {
            ll_nocontent.setVisibility(View.VISIBLE);
            ll_errorpage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pageNum = 1;
        searchData();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        pageNum++;
        searchData();
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
                convertView = View.inflate(ActivitySrroundingMapList.this, R.layout.common_srrounding_maplist_item, null);
                viewHolder = new ViewHolder(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Log.i("debbug", "size=" + pageData.size());

            final PoiInfo poiInfo = pageData.get(position);

            Log.i("debbug", "poiInfo=" + poiInfo);

            if (poiInfo != null) {

                viewHolder.maplist_keyname_tv.setText("" + (position + 1) + "." + pageData.get(position).name + "(" + poiInfo.address + ")");

                viewHolder.maplist_address_tv.setText(pageData.get(position).address);
                if (TextUtils.isEmpty(poiInfo.phoneNum)) {
                    viewHolder.maplist_phonenum_btn.setVisibility(View.GONE);
                } else {
                    String phoneNum = poiInfo.phoneNum;
                    /// 如果有两个或者两个以上,默认拨打第一个
                    if (phoneNum.contains(",")) {
                        String[] phoneNums = phoneNum.split(",");
                        if (phoneNums != null && phoneNums.length > 0) {
                            phoneNum = phoneNums[0];
                        }
                    }
                    viewHolder.maplist_phonenum_btn.setText(phoneNum);
                    final String finalPhoneNum = phoneNum;
                    viewHolder.maplist_phonenum_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

//                            String phoneNum = poiInfo.phoneNum;
//                            /// 如果有两个或者两个以上,默认拨打第一个
//                            if(phoneNum.contains(",")){
//                              String[] phoneNums=      phoneNum.split(",");
//                                if(phoneNums!=null&&phoneNums.length>0){
//                                    phoneNum = phoneNums[0];
//                                }
//                            }
                            Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + finalPhoneNum));
                            startActivity(phoneIntent);
                        }
                    });
                }

                final LatLng latLng1 = new LatLng(latitude, longitude);

                double distance = DistanceUtil.getDistance(latLng1, poiInfo.location);
                if (distance < 1000) {
                    viewHolder.maplist_distance_tv.setText("<" + Arith.round(distance, 1) + "m");
                } else if (distance >= 1000) {
                    viewHolder.maplist_distance_tv.setText("<" + Arith.round(distance / 1000, 1) + "km");
                }

                viewHolder.maplist_seedetail_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        startActivity(new Intent(getmContext(), ActivitySurroundingMap.class).putExtra("maplistitemuid", poiInfo.uid));

                        Intent goMapIntent = new Intent(getmContext(), ShowAddressActivity.class);
                        goMapIntent.putExtra("address", poiInfo.address);
//                        goMapIntent.putExtra("businessStartTime",poiInfo);
//                        goMapIntent.putExtra("businessEndTime",poiInfo);

                        goMapIntent.putExtra("commlongitude", latLng1.longitude);
                        goMapIntent.putExtra("commlatitude", latLng1.latitude);

                        goMapIntent.putExtra("shopName", poiInfo.name);

                        goMapIntent.putExtra("longitude", poiInfo.location.longitude);
                        goMapIntent.putExtra("latitude", poiInfo.location.latitude);

                        startActivity(goMapIntent);

//                        shopAddress=getIntent().getStringExtra("address");
//                        businessStartTime=getIntent().getStringExtra("businessStartTime");
//                        businessEndTime=getIntent().getStringExtra("businessEndTime");
//                        longitude=getIntent().getDoubleExtra("longitude", 0);
//                        latitude=getIntent().getDoubleExtra("latitude", 0);
//                        shopName=getIntent().getStringExtra("shopName");
//                        commlongitude = getIntent().getDoubleExtra("commlongitude", 0);
//                        commlatitude = getIntent().getDoubleExtra("commlatitude",0);

                    }
                });

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent goMapIntent = new Intent(getmContext(), ShowAddressActivity.class);
                        goMapIntent.putExtra("address", poiInfo.address);
//                        goMapIntent.putExtra("businessStartTime",poiInfo);
//                        goMapIntent.putExtra("businessEndTime",poiInfo);

                        goMapIntent.putExtra("commlongitude", latLng1.longitude);
                        goMapIntent.putExtra("commlatitude", latLng1.latitude);

                        goMapIntent.putExtra("shopName", poiInfo.name);

                        goMapIntent.putExtra("longitude", poiInfo.location.longitude);
                        goMapIntent.putExtra("latitude", poiInfo.location.latitude);

                        startActivity(goMapIntent);

//                        startActivity(new Intent(getmContext(), ActivitySurroundingMap.class).putExtra("maplistitemuid", poiInfo.uid));
                    }
                });

            }
            return convertView;
        }


        class ViewHolder {
            TextView maplist_keyname_tv;
            TextView maplist_address_tv;
            TextView maplist_distance_tv;
            Button maplist_phonenum_btn;
            Button maplist_seedetail_btn;

            ViewHolder(View v) {
                maplist_keyname_tv = (TextView) v.findViewById(R.id.maplist_keyname_tv);
                maplist_address_tv = (TextView) v.findViewById(R.id.maplist_address_tv);
                maplist_distance_tv = (TextView) v.findViewById(R.id.maplist_distance_tv);
                maplist_phonenum_btn = (Button) v.findViewById(R.id.maplist_phonenum_btn);
                maplist_seedetail_btn = (Button) v.findViewById(R.id.maplist_seedetail_btn);
                v.setTag(this);
            }
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_neterror:
                searchData();
                break;
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPoiSearch.destroy();
    }

}
