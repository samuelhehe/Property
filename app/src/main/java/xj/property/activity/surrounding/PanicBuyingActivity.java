package xj.property.activity.surrounding;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.nostra13.universalimageloader.core.ImageLoader;

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
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.beans.SinglePanicBuyingBean;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;
import xj.property.widget.LoadingDialog;

public class PanicBuyingActivity extends HXBaseActivity {
    private int crazySalesId;
    private LoadingDialog loadingDialog;

    private ImageView iv_back, img;
    private TextView title, description, rest_time, viewed_count, limit_count, rest_count, num, ongoing_time, phone, address,tv_distance;
    private Button panic_buying;
    private ImageButton deliver, add;
    private LinearLayout ll_phone, ll_address;
    private int screenWidth ;

    GeoCoder mSearch ,mSearch2 = null;
    MapView mMapView = null;
    BaiduMap mBaiduMap;
    private String shopName;
    private double longitude;
    private double latitude;
    private double commlongitude,commlatitude;
    //// 查看已购买用户
    private LinearLayout panic_has_purchase_llay;
    /// 查看已购买用户subview
    private LinearLayout welfare_purchase_hasgoturs_lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_panic_buying);
        crazySalesId = getIntent().getIntExtra("crazySalesId", -1);
        Log.i("crazySalesId", crazySalesId + "");
        initView();
        initData();
    }

    private void initData() {
        mSearch = GeoCoder.newInstance();
        mSearch2 = GeoCoder.newInstance();
        mSearch2.setOnGetGeoCodeResultListener(new myOnGetGeoCoderResultListener());
        mSearch.setOnGetGeoCodeResultListener(new myOnGetGeoCoderResultListenerHome());
        getSinglePanicBuyingDetail(crazySalesId);

    }

    private void initView() {

        initTitle();

        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        loadingDialog = new LoadingDialog(PanicBuyingActivity.this);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        img = (ImageView) findViewById(R.id.img);
        title = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);
        rest_time = (TextView) findViewById(R.id.rest_time);
        viewed_count = (TextView) findViewById(R.id.viewed_count);
        limit_count = (TextView) findViewById(R.id.limit_count);
        rest_count = (TextView) findViewById(R.id.rest_count);
        num = (TextView) findViewById(R.id.num);
        ongoing_time = (TextView) findViewById(R.id.ongoing_time);
        phone = (TextView) findViewById(R.id.phone);
        address = (TextView) findViewById(R.id.address);
        deliver = (ImageButton) findViewById(R.id.deliver);
        add = (ImageButton) findViewById(R.id.add);
        panic_buying = (Button) findViewById(R.id.panic_buying);
        ll_phone = (LinearLayout) findViewById(R.id.ll_phone);
        ll_address = (LinearLayout) findViewById(R.id.ll_address);
        tv_distance = (TextView) findViewById(R.id.tv_distance);

        panic_has_purchase_llay = (LinearLayout) findViewById(R.id.panic_has_purchase_llay);
        welfare_purchase_hasgoturs_lv = (LinearLayout) findViewById(R.id.welfare_purchase_hasgoturs_lv);

        mMapView = (MapView) findViewById(R.id.bmapsView);
        mBaiduMap = mMapView.getMap();

        RelativeLayout.LayoutParams rlparams = (RelativeLayout.LayoutParams) img.getLayoutParams();
        rlparams.width = screenWidth;
        rlparams.height = screenWidth*3/4;
        img.setLayoutParams(rlparams);

        TextPaint tp = title.getPaint();
        tp.setFakeBoldText(true);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initTitle() {
        TextView   tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("活动详情");
        tv_title.setTextColor(Color.parseColor("#48D382"));
        tv_title.setVisibility(View.VISIBLE);
        this.findViewById(R.id.iv_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.iv_back:

                finish();

                break;
        }

    }

    interface SinglePanicBuyingDetailService {
        //         http://bangbang.ixiaojian.com/api/v1/crazysales/detail/user/{crazySalesId}?communityId={communityId}&emobId={emobId}
//        HTTP Method : GET
        @GET("/api/v1/crazysales/detail/user/{crazySalesId}")
//        ?communityId={communityId}&emobId={emobId}
        void getInfo(@Path("crazySalesId") int crazySalesId, @QueryMap Map<String, String> map, Callback<SinglePanicBuyingBean> cb);
    }

    private void getSinglePanicBuyingDetail(final int crazySalesId) {

        loadingDialog.show();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        SinglePanicBuyingDetailService service = restAdapter.create(SinglePanicBuyingDetailService.class);
        Callback<SinglePanicBuyingBean> callback = new Callback<SinglePanicBuyingBean>() {
            @Override
            public void success(final SinglePanicBuyingBean singlePanicBuyingBean, Response response) {
                Log.i("SinglePanicBuyingBean", "请求成功！");
                loadingDialog.dismiss();
                if (singlePanicBuyingBean != null && "yes".equals(singlePanicBuyingBean.getStatus())){
                    try {

                        //给布局中的控件赋值
                        title.setText("" + singlePanicBuyingBean.getInfo().getTitle());
                        description.setText("" + singlePanicBuyingBean.getInfo().getDescr());
                        rest_time.setText("" + formatDuring((singlePanicBuyingBean.getInfo().getEndTime() - (System.currentTimeMillis() / 1000)) * 1000));
                        Log.i("rest_time", "" + (singlePanicBuyingBean.getInfo().getEndTime() * 1000 - System.currentTimeMillis()));
                        limit_count.setText("" + singlePanicBuyingBean.getInfo().getPerLimit());
                        rest_count.setText("" + singlePanicBuyingBean.getInfo().getRemain() );

                        viewed_count.setText("" + singlePanicBuyingBean.getInfo().getViewCount() );

                        Log.i("1111111", (singlePanicBuyingBean.getInfo().getShop().getShopName() + ""));
                        Log.i("1111111", (singlePanicBuyingBean.getInfo().getShop().getBusinessStartTime() + ""));
                        Log.i("1111111", (singlePanicBuyingBean.getInfo().getShop().getBusinessEndTime() + ""));
                        ongoing_time.setText("" + singlePanicBuyingBean.getInfo().getShop().getShopName() + "营业时间" + singlePanicBuyingBean.getInfo().getShop().getBusinessStartTime() + "-" + singlePanicBuyingBean.getInfo().getShop().getBusinessEndTime());
                        phone.setText("" + singlePanicBuyingBean.getInfo().getShop().getPhone());
                        address.setText("" + singlePanicBuyingBean.getInfo().getShop().getAddress());
                        tv_distance.setText("距离小区："+singlePanicBuyingBean.getInfo().getDistance()+"m");




                        for (int i = 0; i < singlePanicBuyingBean.getInfo().getCrazySalesImg().size(); i++) {
                            ImageLoader.getInstance().displayImage(singlePanicBuyingBean.getInfo().getCrazySalesImg().get(i).getImgUrl(), img);
                        }


                        //// 加载查看已购买更多用户
                        loadingGoodsHasGotursHeadImgs4(singlePanicBuyingBean);

                        add.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                String string = num.getText().toString();
                                int n = Integer.valueOf(string);
                                num.setText(++n + "");
                                if (n > singlePanicBuyingBean.getInfo().getPerLimit()) {
                                    num.setText(singlePanicBuyingBean.getInfo().getPerLimit() + "");
                                    final Dialog dialog = new Dialog(PanicBuyingActivity.this, R.style.MyDialogStyle);
                                    dialog.setContentView(R.layout.dialog_warn_user_limit);
                                    ((TextView) dialog.findViewById(R.id.dialog_limit_count)).setText("抢购活动，每人仅限抢" + singlePanicBuyingBean.getInfo().getPerLimit() + "份");
                                    dialog.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();
                                }
                            }
                        });

                        deliver.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                String string = num.getText().toString();
                                int n = Integer.valueOf(string);
                                num.setText(--n + "");
                                if (--n < 1) {
                                    num.setText(1 + "");
                                }
                            }
                        });

                        panic_buying.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!PreferencesUtil.getLogin(PanicBuyingActivity.this)) {
                                    Intent intent = new Intent(PanicBuyingActivity.this, RegisterLoginActivity.class);
                                    startActivity(intent);
                                    return;
                                }
                                Intent intent = new Intent(PanicBuyingActivity.this, PanicBuyingDetailActivity.class);
                                intent.putExtra("crazySalesId", crazySalesId);
                                intent.putExtra("emobId", PreferencesUtil.getLoginInfo(PanicBuyingActivity.this).getEmobId());
                                intent.putExtra("count", Integer.parseInt(num.getText().toString()));
                                intent.putExtra("logo", ""+singlePanicBuyingBean.getInfo().getShop().getLogo());
                                startActivity(intent);
                            }
                        });

                        ll_phone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent phoneIntent = new Intent(
                                        "android.intent.action.CALL", Uri.parse("tel:"
                                        + singlePanicBuyingBean.getInfo().getShop().getPhone()));
                                startActivity(phoneIntent);
                            }
                        });

                        shopName = singlePanicBuyingBean.getInfo().getShop().getShopName();
                        longitude = singlePanicBuyingBean.getInfo().getShop().getLongitude();
                        latitude = singlePanicBuyingBean.getInfo().getShop().getLatitude();
                        commlongitude = singlePanicBuyingBean.getInfo().getCommunityInfo().getLongitude();
                        commlatitude = singlePanicBuyingBean.getInfo().getCommunityInfo().getLatitude();



                        initMap(latitude,longitude);

                        LatLng ptCenter = new LatLng(latitude, longitude);
                        // 反Geo搜索
                        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                                .location(ptCenter));

                        LatLng ptCenter2 = new LatLng(commlatitude,commlongitude);
                        // 反Geo搜索
                        mSearch2.reverseGeoCode(new ReverseGeoCodeOption()
                                .location(ptCenter2));


                        ll_address.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent addRessIntent = new Intent(PanicBuyingActivity.this, ShowAddressActivity.class);
                                addRessIntent.putExtra("longitude", singlePanicBuyingBean.getInfo().getShop().getLongitude());
                                addRessIntent.putExtra("latitude", singlePanicBuyingBean.getInfo().getShop().getLatitude());
                                addRessIntent.putExtra("address", singlePanicBuyingBean.getInfo().getShop().getAddress());
                                addRessIntent.putExtra("businessStartTime", singlePanicBuyingBean.getInfo().getShop().getBusinessStartTime());
                                addRessIntent.putExtra("businessEndTime", singlePanicBuyingBean.getInfo().getShop().getBusinessEndTime());
                                addRessIntent.putExtra("shopName", singlePanicBuyingBean.getInfo().getShop().getShopName());
                                addRessIntent.putExtra("commlongitude", singlePanicBuyingBean.getInfo().getCommunityInfo().getLongitude());
                                addRessIntent.putExtra("commlatitude", singlePanicBuyingBean.getInfo().getCommunityInfo().getLatitude());
//                                startActivity(addRessIntent);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(PanicBuyingActivity.this,"数据异常！",Toast.LENGTH_SHORT).show();
                    }
                }else {

                }

            }

            @Override
            public void failure(RetrofitError error) {
                loadingDialog.dismiss();
                Log.e("debbug", error.toString());
                showNetErrorToast();
            }
        };
        Map<String, String> map = new HashMap<>();

        String emobId = null, communityId = null;
        if(PreferencesUtil.getLogin(this)){
            emobId = PreferencesUtil.getLoginInfo(this).getEmobId();
            communityId = ""+PreferencesUtil.getLoginInfo(this).getCommunityId();
        }else {
            communityId = "" + PreferencesUtil.getCommityId(this);
            if(PreferencesUtil.getTouristLogin(this)){
                emobId = PreferencesUtil.getTourist(this);
            }else {
                emobId = PreferencesUtil.getlogoutEmobId(this);
            }
        }

            map.put("emobId", ""+emobId);
            map.put("communityId",""+communityId);
        service.getInfo(crazySalesId, map, callback);
    }

    //倒计时
    public static String formatDuring(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        return days + " 天 " + hours + " 小时 ";
    }





    /**
     * 加载已经购买过的用户
     *
     * @param info
     */
    private void loadingGoodsHasGotursHeadImgs4(final SinglePanicBuyingBean info) {

        final List<SinglePanicBuyingBean.InfoEntity.UsersEntity> users = info.getInfo().getUsers();
        Log.i("debbug", "info.size" + info.getInfo().getUsers().size());
        if (users.size() <= 0) {
            if (panic_has_purchase_llay != null) {
                panic_has_purchase_llay.setVisibility(View.GONE);

            }
        }

        if (users.size() > 5) {
            for (int i = 0; i < 4; i++) {
                LinearLayout usrHeadView = (LinearLayout) View.inflate(this, R.layout.common_panic_purchase_hasgoturs_headlay, null);
                ImageView img = (ImageView) usrHeadView.findViewById(R.id.iv_avatar);
                img.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(users.get(i).getAvatar(), img, UserUtils.options);
                usrHeadView.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams rlparams = (LinearLayout.LayoutParams) img.getLayoutParams();
                rlparams.width = screenWidth * 123 / 1080;
                rlparams.height = screenWidth * 123 / 1080;

                img.setLayoutParams(rlparams);

                final int finalI = i;
                usrHeadView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getmContext(), UserGroupInfoActivity.class).putExtra(Config.INTENT_PARMAS2, users.get(finalI).getEmobId()));
                    }
                });

//                LinearLayout.LayoutParams llayparams = (LinearLayout.LayoutParams) welfare_purchase_hasgoturs_lv.getLayoutParams();
//                welfare_purchase_hasgoturs_lv.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                welfare_purchase_hasgoturs_lv.addView(usrHeadView);

            }

            /// 添加一个查看更多用户按钮

            LinearLayout usrHeadView = (LinearLayout) View.inflate(this, R.layout.common_panic_purchase_hasgoturs_headlay, null);
            ImageView img = (ImageView) usrHeadView.findViewById(R.id.iv_avatar);
            img.setVisibility(View.VISIBLE);

            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.msp_card_buyed_see_more, img);
            usrHeadView.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams rlparams = (LinearLayout.LayoutParams) img.getLayoutParams();

            rlparams.width = screenWidth * 123 / 1080;
            rlparams.height = screenWidth * 123 / 1080;

            img.setLayoutParams(rlparams);

            usrHeadView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ///
                    startActivity(new Intent(getmContext(), ActivitySrroundingBuyedMoreUsers.class).putExtra("crazySalesId", ""+info.getInfo().getCrazySalesId()));

                }
            });
//
//            LinearLayout.LayoutParams llayparams = (LinearLayout.LayoutParams) welfare_purchase_hasgoturs_lv.getLayoutParams();
//            welfare_purchase_hasgoturs_lv.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            welfare_purchase_hasgoturs_lv.addView(usrHeadView);
        }

        if (users.size() > 0 && users.size() <= 5) {

            for (int i = 0; i < users.size(); i++) {

                LinearLayout usrHeadView = (LinearLayout) View.inflate(this, R.layout.common_panic_purchase_hasgoturs_headlay, null);
                ImageView img = (ImageView) usrHeadView.findViewById(R.id.iv_avatar);
                img.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(users.get(i).getAvatar(), img, UserUtils.options);
                usrHeadView.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams rlparams = (LinearLayout.LayoutParams) img.getLayoutParams();

                rlparams.width = screenWidth * 123 / 1080;
                rlparams.height = screenWidth * 123 / 1080;

                img.setLayoutParams(rlparams);


                final int finalI = i;
                usrHeadView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getmContext(), UserGroupInfoActivity.class).putExtra(Config.INTENT_PARMAS2, users.get(finalI).getEmobId()));
                    }
                });

//                LinearLayout.LayoutParams llayparams = (LinearLayout.LayoutParams) welfare_purchase_hasgoturs_lv.getLayoutParams();
//                welfare_purchase_hasgoturs_lv.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                welfare_purchase_hasgoturs_lv.addView(usrHeadView);

            }
        }
    }





    private class myOnGetGeoCoderResultListener implements OnGetGeoCoderResultListener {

        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(PanicBuyingActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                        .show();
                return;
            }
//        mBaiduMap.clear();
//        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
//                .icon(BitmapDescriptorFactory
//                        .fromResource(R.drawable.icon_marka)));
            Log.i("debbug", "result="+result.getLocation().toString());
            Log.i("debbug", "address="+result.getAddress());
            View frameLayout = View.inflate(PanicBuyingActivity.this, R.layout.map_marker_view, null);
            TextView t1 = (TextView) frameLayout.findViewById(R.id.tv_txt);
            t1.setText(PreferencesUtil.getCommityName(PanicBuyingActivity.this));

            OverlayOptions ooA = new MarkerOptions().position(result.getLocation()).icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_marka_green))
                    .zIndex(6).draggable(true);
            OverlayOptions ooA2 = new MarkerOptions().position(result.getLocation()).icon(BitmapDescriptorFactory
                    .fromView(t1))
                    .zIndex(5).draggable(true)
                    .anchor(0f, 0f);
            mBaiduMap.addOverlay(ooA);
            mBaiduMap.addOverlay(ooA2);
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                    .getLocation()));
//        Toast.makeText(this, result.getAddress(),
//                Toast.LENGTH_LONG).show();
        }
    }

    private class myOnGetGeoCoderResultListenerHome implements OnGetGeoCoderResultListener{
        @Override
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(PanicBuyingActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                        .show();
                return;
            }
            mBaiduMap.clear();
            mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_marka)));
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                    .getLocation()));
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(PanicBuyingActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                        .show();
                return;
            }
//        mBaiduMap.clear();
//        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
//                .icon(BitmapDescriptorFactory
//                        .fromResource(R.drawable.icon_marka)));
//            initMap(result.getLocation().latitude,result.getLocation().longitude);
            Log.i("debbug", "result="+result.getLocation().toString());
            Log.i("debbug", "address="+result.getAddress());
            View frameLayout = View.inflate(PanicBuyingActivity.this, R.layout.map_marker_view, null);
            TextView t1 = (TextView) frameLayout.findViewById(R.id.tv_txt);
            t1.setText(shopName);

            OverlayOptions ooA = new MarkerOptions().position(result.getLocation()).icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_marka_red))
                    .zIndex(4).draggable(true);
            OverlayOptions ooA2 = new MarkerOptions().position(result.getLocation()).icon(BitmapDescriptorFactory
                    .fromView(t1))
                    .zIndex(3).draggable(true)
                    .anchor(0f, 0f);
            mBaiduMap.addOverlay(ooA);
            mBaiduMap.addOverlay(ooA2);
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                    .getLocation()));
//        Toast.makeText(this, result.getAddress(),
//                Toast.LENGTH_LONG).show();
        }
    }

    //初始化地图
    private void initMap(double latitude,double longitude ) {

        // 普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        LatLng cenpt = new LatLng(latitude,longitude);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);

//        PoiInfo info = new PoiInfo();
//        info.location = new LatLng(latitude,longitude);
//        LatLng llA =info.location;
//        CoordinateConverter converter= new CoordinateConverter();
//        converter.coord(llA);
//        converter.from(CoordinateConverter.CoordType.COMMON);
//        LatLng convertLatLng = converter.convert();
//
//        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 17.0f);
//        mBaiduMap.animateMapStatus(u);
    }
}
