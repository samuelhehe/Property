package xj.property.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.activeandroid.util.Log;

import de.greenrobot.event.EventBus;
import xj.property.event.NetworkStateChangeEvent;
import xj.property.event.WXPayRequestEvent;
import xj.property.statistic.EventServiceUtils;
import xj.property.statistic.StatisticAsyncService;

/**
 * Created by Administrator on 2015/6/19.
 */
public class NetWorkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("onion", "监听到");
        NetworkInfo.State wifiState = null;
        NetworkInfo.State mobileState = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (wifiState != null && mobileState != null
                && NetworkInfo.State.CONNECTED != wifiState
                && NetworkInfo.State.CONNECTED == mobileState) {
            // 手机网络连接成功
            Log.i("onion", "4g");
            EventBus.getDefault().post( new NetworkStateChangeEvent(true));

        } else if (wifiState != null && mobileState != null
                && NetworkInfo.State.CONNECTED != wifiState
                && NetworkInfo.State.CONNECTED != mobileState) {
            Log.i("onion", "没网");
            EventBus.getDefault().post( new NetworkStateChangeEvent(false));

            // 手机没有任何的网络
        } else if (wifiState != null && NetworkInfo.State.CONNECTED == wifiState) {

            Log.i("onion", "start StatisticAsyncService");
            context.startService(new Intent(context,StatisticAsyncService.class));
            // 无线网络连接成功
            Log.i("onion", "wifi");
            EventBus.getDefault().post(new NetworkStateChangeEvent(true));
        }

    }

}
