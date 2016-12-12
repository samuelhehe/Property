package xj.property.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.Date;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import xj.property.beans.ActiveUserInfo;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

/**
 * Created by n on 2015/6/3.
 */
public class ReocderActiveUtils {

    interface ReocderActiveUserService {
        ///api/v1/communities/{communityId}/users/
        @POST("/api/v1/communities/{communityId}/users/")
        void getUpdateInfo(@Header("signature") String signature, @Body ActiveUserInfo userInfo, @Path("communityId") int communityId, Callback<Object> callback);
    }

    public static   void reocderActive(Context context) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_AGENT).build();
        ReocderActiveUserService service = restAdapter.create(ReocderActiveUserService.class);
        Callback<Object> callback = new Callback<Object>() {
            @Override
            public void success(Object Object, retrofit.client.Response response) {
               Log.i("onion", "Record成功" + Object);
            }

            @Override
            public void failure(RetrofitError error) {
                //Log.i("onion","失败"+error.toString());
            }
        };
        ActiveUserInfo info=new ActiveUserInfo();
        info.day=(int)(new Date().getTime()/1000);
       // Log.i("onion","登录状态: "+PreferencesUtil.getLogin(context)+PreferencesUtil.getTouristid(context));

        info.setEmobId( PreferencesUtil.getLogin(context)?PreferencesUtil.getLoginInfo(context).getEmobId():PreferencesUtil.getTourist(context));
        info.setUserId(PreferencesUtil.getLogin(context)?PreferencesUtil.getUserId(context):PreferencesUtil.getTouristid(context));
       // Log.i("onion","info1: "+info.toString());
        if (info.getEmobId().trim().length()==0){
            info.setUserId(PreferencesUtil.getlogoutUserId(context));
            info.setEmobId(PreferencesUtil.getlogoutEmobId(context));
        }
        try {
            info.setAppVersion( getVersionName(context));
        }catch (Exception e){
            e.printStackTrace();
            info.appVersion="";
        }
        Log.i("onion","Record成功info2: "+info.toString()+"小区id="+PreferencesUtil.getCommityId(context));

        service.getUpdateInfo(StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(info)),info,PreferencesUtil.getCommityId(context),callback);
    }

    private static String getVersionName(Context context) throws Exception {
// 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
// getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }
}
