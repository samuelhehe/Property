package xj.property.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import xj.property.utils.other.Config;


/**
 * 微信支付注册
 */
public class AppRegister extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final IWXAPI api = WXAPIFactory.createWXAPI(context, null);

		//
		api.registerApp(Config.APP_ID);
        Log.i("debbug", "weixinpay registerApp ");
    }
}
