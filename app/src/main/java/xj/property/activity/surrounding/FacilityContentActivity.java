package xj.property.activity.surrounding;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.utils.CoordinateConverter;

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.FacilitiesBean;
import xj.property.utils.other.Config;

/**
 * Created by Administrator on 2015/4/2.
 */
public class FacilityContentActivity extends HXBaseActivity {
    MapView mMapView = null;
    BaiduMap mBaiduMap;
    // 定位相关声明
    public LocationClient locationClient = null;
    // 自定义图标
    BitmapDescriptor mCurrentMarker = null;
    boolean isFirstLoc = true;// 是否首次定位
    TextView tv_info;
    //PoiSearch mPoiSearch;// POI检索实例
    FacilitiesBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
        // 注意该方法要再setContentView方法之前实现
        // 获取地图控件引用
        setContentView(R.layout.activity_faci_content);
        mMapView = (MapView) findViewById(R.id.bmapsView);
        mBaiduMap = mMapView.getMap();
        bean = (FacilitiesBean) getIntent().getSerializableExtra(Config.INTENT_PARMAS1);
        TextView tvAddress = (TextView) findViewById(R.id.tv_faci_address);
        TextView tvPhone = (TextView) findViewById(R.id.tv_faci_phone);
        TextView tv_faci_time = (TextView) findViewById(R.id.tv_faci_time);
        tv_faci_time.setText(bean.getBusinessStartTime() + "-" + bean.getBusinessEndTime());
        ImageView iv_call_phone = (ImageView) findViewById(R.id.iv_call_phone);

        tvAddress.setText(bean.getAddress());
        iv_call_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneIntent = new Intent(
                        "android.intent.action.CALL", Uri.parse("tel:"
                        + bean.getPhone()));
                startActivity(phoneIntent);
            }
        });
        tvPhone.setText(bean.getPhone());
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneIntent = new Intent(
                        "android.intent.action.CALL", Uri.parse("tel:"
                        + bean.getPhone()));
                startActivity(phoneIntent);
            }
        });
        //mPoiSearch = PoiSearch.newInstance();
       // initLoaction();
        initMap();
        if (bean != null)
            initTitle(null, bean.getFacilityName(), "");

    }


    //	private void initDialog() {
//		View v1 = View.inflate(this, R.layout.dialog_normal, null);
//		tv_info = (TextView) v1.findViewById(R.id.tv_dialog);
//		Button btn = (Button) v1.findViewById(R.id.btn_dialog);
//		dialog = new Dialog(this, R.style.MyDialog);
//		dialog.setCanceledOnTouchOutside(true);
//		dialog.setContentView(v1);
//		btn.setOnClickListener(new DialogDismiss(dialog));
    //}

    /**
     * 初始化，定位
     */
    private void initLoaction() {
        mBaiduMap.setMyLocationEnabled(true);
        locationClient = new LocationClient(getApplicationContext()); // 实例化LocationClient类
        locationClient.registerLocationListener(myListener); // 注册监听函数
        this.setLocationOption(); // 设置定位参数
        locationClient.start();

    }

    /* private void addFugaiWu(ShopList  shop,int position) {
         LatLng location = new LatLng(shop.getmLatitude(), shop.getmLongitude());
         // LatLng point = new LatLng(39.963175, 116.400244);
         String imgname = "qipao0" +position ;
         int imgid = getResources().getIdentifier(imgname, "drawable", "com.panpass.feihe");
         BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(imgid);
         Bundle bundle = new Bundle();
         bundle.putSerializable(Config.SHOP, shop);

         OverlayOptions option = new MarkerOptions().position(location)
                 .icon(bitmap).extraInfo(bundle);

         mBaiduMap.addOverlay(option);

         TextOptions optionText = new TextOptions().bgColor(0xAAFFFF00)
                 .fontSize(24).fontColor(0xFFFF00FF).text("飞鹤" + shop.getAgencyName())
                 .rotate(0)// -30
                 .position(location); // 在地图上添加该文字对象并显示
         // mBaiduMap.addOverlay(option);
         mBaiduMap.addOverlay(optionText);
     }*/
    private void addFugaiWu(PoiInfo info) {
//        Log.i("onion", info.toString());
//        LatLng location = info.location;
//        Log.i("onion", location.latitude + "/" + location.longitude);
//        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
//        Bundle bundle = new Bundle();
//        bundle.putString("info", info.name + info.address);
//
//        OverlayOptions option = new MarkerOptions().position(location)
//                .icon(bitmap).extraInfo(bundle);
//
//        mBaiduMap.addOverlay(option);
        LatLng llA =info.location;
                //new LatLng(latitude, longtitude);
        CoordinateConverter converter= new CoordinateConverter();
        converter.coord(llA);
        converter.from(CoordinateConverter.CoordType.COMMON);
        LatLng convertLatLng = converter.convert();
        OverlayOptions ooA = new MarkerOptions().position(convertLatLng).icon(BitmapDescriptorFactory
                .fromResource(R.drawable.icon_marka))
                .zIndex(4).draggable(true);
        mBaiduMap.addOverlay(ooA);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 17.0f);
        mBaiduMap.animateMapStatus(u);
        /*TextOptions optionText = new TextOptions().bgColor(0xAAFFFF00)
                .fontSize(24).fontColor(0xFFFF00FF).text("飞鹤" + info.name)
                .rotate(0)// -30
                .position(location); // 在地图上添加该文字对象并显示
        // mBaiduMap.addOverlay(option);
        mBaiduMap.addOverlay(optionText);*/
        // OnInfoWindowClickListener listener = new OnInfoWindowClickListener()
        // {
        //
        // @Override
        // public void onInfoWindowClick() {
        // System.out.println("点击了标示");
        //
        // }
        // };

        // InfoWindow window = new InfoWindow(btn, location, listener);
        // mBaiduMap.showInfoWindow(window);

    }

    //初始化地图
    private void initMap() {

        // 普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        PoiInfo info = new PoiInfo();
        info.location = new LatLng(bean.getLatitude(), bean.getLongitude());
        info.name = bean.getFacilityName();
        addFugaiWu(info);

        // 卫星地图
        // mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);

        // mBaiduMap.setTrafficEnabled(true);//交通路况
       /* mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

            public boolean onMapPoiClick(MapPoi arg0) {
                return false;
            }

            public void onMapClick(LatLng arg0) {
                mBaiduMap.hideInfoWindow();
            }
        } );*/
        /*OnMarkerClickListener listener = new OnMarkerClickListener() {
            *//**
         * 地图 Marker 覆盖物点击事件监听函数
         *
         * @param marker
         *            被点击的 marker
         *//*
            public boolean onMarkerClick(Marker marker) {
                final LatLng ll = marker.getPosition();
                MapStatusUpdate m = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.setMapStatus(m);
                Bundle  bundle=marker.getExtraInfo();
                ShopList  shop=(ShopList) bundle.getSerializable(Config.SHOP);
                tv_info =(TextView) View.inflate(ShopMapLineActivity.this, R.layout.mapwindow, null);
                tv_info.setText("飞鹤奶粉专卖店\n"
                        + "门店名称:"+StrUtils.subString(shop.getAgencyName(),12)+"\n地址:"+StrUtils.subString(shop.getAddress(), 12)+"\n联系电话:"+shop.getPhone());
//				tv_info.setBackgroundResource(R.id.);
                //dialog.show();
                InfoWindow  info=new InfoWindow(tv_info, new LatLng(shop.getmLatitude(), shop.getmLongitude()), null);
                mBaiduMap.showInfoWindow(info);
                return true;
            }
        };
        mBaiduMap.setOnMarkerClickListener(listener);*/
    }

//	OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
//		public void onGetPoiResult(PoiResult result) {
//
//			try {
//				BDLocation location = locationClient.getLastKnownLocation();
//				Toast.makeText(ShopMapLineActivity.this, location.getAddrStr(), 1)
//						.show();
//				// 获取POI检索结果
//				List<PoiInfo> infos = result.getAllPoi();
//				for (int i = 0; i < infos.size(); i++) {
//					PoiInfo info = infos.get(i);
//					// System.out.println(info.name);
//					// System.out.println(info.location);
//					addFugaiWu(info, i);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//		public void onGetPoiDetailResult(PoiDetailResult result) {
//			// 获取Place详情页检索结果
//		}
//	};

    public BDLocationListener myListener = new BDLocationListener() {
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();

            try {
                mBaiduMap.setMyLocationData(locData); // 设置定位数据
                if (isFirstLoc) {
                    isFirstLoc = false;

                    LatLng ll = new LatLng(location.getLatitude(),
                            location.getLongitude());
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(
                            ll, 16); // 设置地图中心点以及缩放级别11,16
                    // MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                    mBaiduMap.animateMapStatus(u);
                }
//				mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
//				mPoiSearch.searchInCity((new PoiCitySearchOption()).city("北京")
//						.keyword("饭馆").pageNum(10));
                // PoiOverlay poi = new PoiOverlay(mBaiduMap);
            } catch (Exception e) {
                e.printStackTrace();
                myListener = null;
            }
            locationClient.stop();
        }

        public void onReceivePoi(BDLocation arg0) {

        }

    };

    /**
     * 设置定位参数
     */
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开GPS
        option.setScanSpan(30000);
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll"); // 返回的定位结果是百度经纬度,默认值gcj02
        // option.setScanSpan(5000); // 设置发起定位请求的间隔时间为5000ms
//        option.setIsNeedAddress(true); // 返回的定位结果包含地址信息
//        option.setNeedDeviceDirect(true); // 返回的定位结果包含手机机头的方向
        option.setAddrType("all");
        locationClient.setLocOption(option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理

        mMapView.onDestroy();
        //mPoiSearch.destroy();
        myListener = null;
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


    public void onClick(View v) {

    }

}
