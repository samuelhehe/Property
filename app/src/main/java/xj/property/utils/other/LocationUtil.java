package xj.property.utils.other;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class LocationUtil {
	public static LocationClient locationClient = null;
	public static final int LOCATIONSUCCESS = 66;
	public static final int LOCATIONSUCCESS1 = 61;
	public static final int LOCATIONSUCCESS2 = 161;
	public static final int LOCATIONSUCCESS3 = 65;

	public static void getLocation(Activity activity,final Handler handler) {
		locationClient = new LocationClient(activity.getApplicationContext());

		locationClient.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation bdLocation) {

				Log.i("onion",
						"badutype"+bdLocation.getLocType()+"onUtil" + bdLocation.getAddrStr() + "\n" + bdLocation.getLatitude()
								+ "/" + bdLocation.getLongitude());
				Message msg = Message.obtain();
				msg.obj = bdLocation;

				if ( bdLocation.getLocType() == LOCATIONSUCCESS||bdLocation.getLocType() == LOCATIONSUCCESS1
						||bdLocation.getLocType() == LOCATIONSUCCESS2||bdLocation.getLocType() == LOCATIONSUCCESS3) {
					locationClient.stop();
					msg.what = LOCATIONSUCCESS;
				} else {
					locationClient.start();
				}
				handler.sendMessage(msg);
			}
		}); // 注册监听函数
		setLocationOption(); // 设置定位参数
		locationClient.start();
	}

	/**
	 * 设置定位参数
	 */
	private static void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开GPS
		option.setAddrType("all");
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//
		// 设置定位模式
		option.setCoorType("bd09ll"); // 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(2000); // 设置发起定位请求的间隔时间为5000ms
		option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//		option.setIsNeedAddress(true); // 返回的定位结果包含地址信息
//		option.setNeedDeviceDirect(true); // 返回的定位结果包含手机机头的方向
		locationClient.setLocOption(option);
	}
}
