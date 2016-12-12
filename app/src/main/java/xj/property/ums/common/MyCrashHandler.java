/**
 * Cobub Razor
 *
 * An open source analytics android sdk for mobile applications
 *
 * @package		Cobub Razor
 * @author		WBTECH Dev Team
 * @copyright	Copyright (c) 2011 - 2012, NanJing Western Bridge Co.,Ltd.
 * @license		http://www.cobub.com/products/cobub-razor/license
 * @link		http://www.cobub.com/products/cobub-razor/
 * @since		Version 0.1
 * @filesource
 */
package xj.property.ums.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import xj.property.activity.HXBaseActivity.SplashActivity;
import xj.property.beans.ErrorBean;
import xj.property.beans.MyMessage;
import xj.property.ums.UmsAgent;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

public class MyCrashHandler implements UncaughtExceptionHandler {
    private static MyCrashHandler myCrashHandler;
    private Context context;
    private Object stacktrace;
    private String activities;
    private String time;
    private String appkey;
    private String os_version;

    private MyCrashHandler() {

    }

    public static synchronized MyCrashHandler getInstance() {
        if (myCrashHandler != null) {
            return myCrashHandler;
        } else {
            myCrashHandler = new MyCrashHandler();
            return myCrashHandler;
        }
    }

    public void init(Context context) {
        this.context = context;
        // this.service = service;
    }

    public void uncaughtException(Thread thread, final Throwable arg1) {
        Log.d("ums-threadname", thread.getName());
        new Thread() {
            @Override
            public void run() {
                super.run();

                Looper.prepare();
                String errorinfo = getErrorInfo(arg1);

                String[] ss = errorinfo.split("\n\t");
                String headstring = ss[0] + "\n\t" + ss[1] + "\n\t" + ss[2]
                        + "\n\t";
                String newErrorInfoString = headstring + errorinfo;

                stacktrace = newErrorInfoString;
                activities = CommonUtil.getActivityName(context);
                time = CommonUtil.getTime();
                appkey = CommonUtil.getAppKey(context);
                os_version = CommonUtil.getOsVersion(context);
//				JSONObject errorInfo =
                ErrorBean bean=    getErrorInfoString(context);

                //CommonUtil.printLog("UmsAgent", errorInfo.toString());
                // ErrorUpLoad.onError(PreferencesUtil.getCommityId(context),bean);
                new ErrorSave(bean).start();
                MyMessage message = NetworkUitlity.post(
//                        "http://192.168.1.66:9095" + "/api/v1/communities/"+PreferencesUtil.getCommityId(context)+"/crash/",
                        Config. NET_AGENT+ "/api/v1/communities/"+PreferencesUtil.getCommityId(context)+"/crash/",
//                        info);
                        getErrorInfoJSONString(context).toString());
//                context.startActivity(new Intent(context, SplashActivity.class));
                //ErrorUpLoad.getUserBlacklist(context);
			/*	if (1 == CommonUtil.getReportPolicyMode(context)
						&& CommonUtil.isNetworkAvailable(context)) {
					if (!stacktrace.equals("")) {
						MyMessage message = NetworkUitlity.post(
								UmsConstants.preUrl + UmsConstants.errorUrl,
                                bean.toString());
						CommonUtil.printLog("UmsAgent", message.getMsg());
//						if (!message.isFlag()) {
//							UmsAgent.saveInfoToFile("errorInfo", errorInfo,
//                                    context);
//							CommonUtil.printLog("error", message.getMsg());
//						}
					}
				} else {
//					UmsAgent.saveInfoToFile("errorInfo", errorInfo, context);
				}*/
                android.os.Process.killProcess(android.os.Process.myPid());
                Looper.loop();
            }

        }.start();
    }
    //String info="{\"appkey\":\"123\",\"time\":\"2015-05-04 18:31:10\",\"activity\":\".activity.HXBaseActivity.MainActivity\",\"deviceId\":\"Meizu MX4\",\"osVersion\":\"4.4.2\",\"appVersion\":\"1.0.0\",\"stacktrace\":\"stacktrace msg\"}";
    private ErrorBean getErrorInfoString(Context context) {
        return new ErrorBean(stacktrace.toString(),time,CommonUtil.getVersion(context),activities,appkey, os_version,CommonUtil.getDeviceName());
//		JSONObject errorInfo = new JSONObject();
//		try {
//			errorInfo.put("stacktrace", stacktrace);
//			errorInfo.put("time", time);
//			errorInfo.put("appVersion", );
//			errorInfo.put("activity", activities);
//			errorInfo.put("appkey", appkey);
//			errorInfo.put("osVersion", os_version);
//			errorInfo.put("deviceId", CommonUtil.getDeviceName());
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return errorInfo;
    }
    private JSONObject getErrorInfoJSONString(Context context) {
        JSONObject errorInfo = new JSONObject();
        try {
            errorInfo.put("stacktrace", stacktrace);
            errorInfo.put("time", time);
            errorInfo.put("appVersion", CommonUtil.getVersion(context));
            errorInfo.put("activity", activities);
            errorInfo.put("appkey", appkey);
            errorInfo.put("osVersion", os_version);
            errorInfo.put("deviceId", CommonUtil.getDeviceName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return errorInfo;
    }
    private String getErrorInfo(Throwable arg1) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        arg1.printStackTrace(pw);
        pw.close();
        String error = writer.toString();
        return error;
    }

}
