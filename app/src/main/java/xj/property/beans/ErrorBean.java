package xj.property.beans;

import xj.property.ums.common.CommonUtil;

/**
 * Created by Administrator on 2015/5/12.
 */
public class ErrorBean {

  // {"appkey":"123","time":"2015-05-04 18:31:10","activity":".activity.HXBaseActivity.MainActivity","deviceId":"Meizu MX4","osVersion":"4.4.2","appVersion":"1.0.0","stacktrace":"stacktrace msg"}

    private  String appkey;
    private  String time;
    private  String activity;
    private  String deviceId;
    private  String osVersion;
    private  String appVersion;
    private  String stacktrace;

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public ErrorBean() {
//        this.stacktrace = "";
//        this.time = "";
//        this.appVersion = "";
//        this.activity = "";
//        this.appkey = "";
//        this.osVersion = "";
//        this.deviceId = "";
    }

    public ErrorBean(String stacktrace, String time, String appVersion, String activity, String appkey, String osVersion, String deviceId) {
        this.stacktrace = stacktrace;
        this.time = time;
        this.appVersion = appVersion;
        this.activity = activity;
        this.appkey = appkey;
        this.osVersion = osVersion;
        this.deviceId = deviceId;
    }


    @Override
    public String toString() {
        return "ErrorBean{" +
                "stacktrace='" + stacktrace + '\'' +
                ", time='" + time + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", activity='" + activity + '\'' +
                ", appkey='" + appkey + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
