package xj.property.service;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebView;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class FirstLoadingX5Service extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public FirstLoadingX5Service() {
        super("FirstLoadingX5Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("yuanhaizhou", "service is start");
        QbSdk.preInit(this);
        //这里必须启用非主进程的service来预热X5内核
//        QbSdk.preInit(this, new QbSdk.PreInitCallback() {
//            @Override
//            public void onCoreInitFinished() {
//                System.exit(0);
//            }
//
//            @Override
//            public void onViewInitFinished() {
//                System.exit(0);
//            }
//        });
    }
}
