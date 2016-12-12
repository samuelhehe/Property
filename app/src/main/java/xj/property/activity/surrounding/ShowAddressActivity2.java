package xj.property.activity.surrounding;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.CoordinateConverter;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.UserLoginRequest;
import xj.property.beans.XJUserInfoBean;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

public class ShowAddressActivity2 extends HXBaseActivity implements OnGetGeoCoderResultListener {

    GeoCoder mSearch ,mSearch2 = null;
    MapView mMapView = null;
    BaiduMap mBaiduMap;
    // 定位相关声明
    public LocationClient locationClient = null;
    // 自定义图标
    BitmapDescriptor mCurrentMarker = null;
    boolean isFirstLoc = true;// 是否首次定位

    private String shopAddress;
    private String businessStartTime;
    private String businessEndTime;
    private String shopName;
    private double longitude;
    private double latitude;
    private double commlongitude,commlatitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_show_address);
        shopAddress=getIntent().getStringExtra("address");
        businessStartTime=getIntent().getStringExtra("businessStartTime");
        businessEndTime=getIntent().getStringExtra("businessEndTime");
        longitude=getIntent().getDoubleExtra("longitude", 0);
        latitude=getIntent().getDoubleExtra("latitude", 0);
        shopName=getIntent().getStringExtra("shopName");
        commlongitude = getIntent().getDoubleExtra("commlongitude", 0);
        commlatitude = getIntent().getDoubleExtra("commlatitude",0);
        Log.i("debbug",""+longitude);
        Log.i("debbug",""+latitude);
        Log.i("debbug",""+shopAddress);
        Log.i("-----",""+businessStartTime);
        Log.i("-----",""+businessEndTime);
        Log.i("-----",""+shopName);
        initTitle(null,shopName,""); /// 标题为店名称
        mMapView = (MapView) findViewById(R.id.bmapsView);
        mBaiduMap = mMapView.getMap();
        TextView tvAddress = (TextView) findViewById(R.id.tv_faci_address);
        tvAddress.setText(""+shopAddress);
        TextView tv_faci_time = (TextView) findViewById(R.id.tv_faci_time);
        tv_faci_time.setText(businessStartTime+"-"+businessEndTime);
        mSearch = GeoCoder.newInstance();
        mSearch2 = GeoCoder.newInstance();
        mSearch2.setOnGetGeoCodeResultListener(new myOnGetGeoCoderResultListener());
        mSearch.setOnGetGeoCodeResultListener(this);
        LatLng ptCenter = new LatLng(latitude, longitude);
        // 反Geo搜索
        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(ptCenter));
        LatLng ptCenter2 = new LatLng(commlatitude,commlongitude);
        // 反Geo搜索
        mSearch2.reverseGeoCode(new ReverseGeoCodeOption()
                .location(ptCenter2));
        initMap(latitude,longitude);
//        if("".equals(PreferencesUtil.getLongitude(this))){
//            getUserInfo(this,PreferencesUtil.getLoginInfo(this).getUsername(),PreferencesUtil.getLoginInfo(this).getPassword());
//        }else {
//            Log.i("debbug", "splat=" +Double.parseDouble(PreferencesUtil.getLatitude(this)));
//            Log.i("debbug", "splong=" + Double.parseDouble(PreferencesUtil.getLongitude(this)));
//
//        }
    }
    //初始化地图
    private void initMap(double latitude,double longitude ) {

        // 普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        PoiInfo info = new PoiInfo();
        info.location = new LatLng(latitude,longitude);
        info.name = shopName;
        addFugaiWu(info);
    }
    private void addFugaiWu(PoiInfo info) {
        LatLng llA =info.location;
        CoordinateConverter converter= new CoordinateConverter();
        converter.coord(llA);
        converter.from(CoordinateConverter.CoordType.COMMON);
        LatLng convertLatLng = converter.convert();

        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 17.0f);
        mBaiduMap.animateMapStatus(u);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理

        mMapView.onDestroy();
        mSearch.destroy();
        mSearch2.destroy();
        //mPoiSearch.destroy();
//        myListener = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
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
            Toast.makeText(this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
//        mBaiduMap.clear();
//        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
//                .icon(BitmapDescriptorFactory
//                        .fromResource(R.drawable.icon_marka)));
        Log.i("debbug", "result="+result.getLocation().toString());
        Log.i("debbug", "address="+result.getAddress());
        View frameLayout = View.inflate(this, R.layout.map_marker_view, null);
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


    interface XJUserService {
        @POST("/api/v1/communities/{communityId}/users/{username}")
        void getUserInfo(@Header("signature") String signature, @Body UserLoginRequest request, @Path("communityId") int communityId, @Path("username") String username, Callback<XJUserInfoBean> cb);
    }

    private void getUserInfo(final Context context, final String username, final String password) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        XJUserService service = restAdapter.create(XJUserService.class);
        final UserLoginRequest request = new UserLoginRequest();
        request.setPassword(StrUtils.string2md5(password));
        request.setRole("owner");
        request.setEquipmentVersion(UserUtils.getVersion(context));
        if(Config.PHONETYPE.equals(android.os.Build.MANUFACTURER)){
            request.setEquipment("mi");
        }else {
            request.setEquipment("android");
        }

        Callback<XJUserInfoBean> callback = new Callback<XJUserInfoBean>() {
            @Override
            public void success(final XJUserInfoBean bean, retrofit.client.Response response) {
                if (bean != null && "yes".equals(bean.getStatus())) {
                    PreferencesUtil.saveLatitude(context, "" + bean.getLatitude());
                    PreferencesUtil.saveLongitude(context, "" + bean.getLongitude());
                    LatLng ptCenter = new LatLng(Double.parseDouble(bean.getLatitude()), Double.parseDouble(bean.getLongitude()));
                    // 反Geo搜索
                    mSearch2.reverseGeoCode(new ReverseGeoCodeOption()
                            .location(ptCenter));
                }

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        };
        service.getUserInfo(StrUtils.string2md5(Config.BANGBANG_TAG+StrUtils.dataHeader(request)),request, PreferencesUtil.getCommityId(context), username, callback);
    }

    private class myOnGetGeoCoderResultListener implements OnGetGeoCoderResultListener{

        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(ShowAddressActivity2.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                        .show();
                return;
            }
//        mBaiduMap.clear();
//        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
//                .icon(BitmapDescriptorFactory
//                        .fromResource(R.drawable.icon_marka)));
            Log.i("debbug", "result="+result.getLocation().toString());
            Log.i("debbug", "address="+result.getAddress());
            View frameLayout = View.inflate(ShowAddressActivity2.this, R.layout.map_marker_view, null);
            TextView t1 = (TextView) frameLayout.findViewById(R.id.tv_txt);
            t1.setText(PreferencesUtil.getCommityName(ShowAddressActivity2.this));

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

}
