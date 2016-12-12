package xj.property.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.Date;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import xj.property.beans.UpDateApp;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseUtils;
import xj.property.ums.UpdateManager;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

/**
 * Created by n on 2015/6/17.
 * /// v3 2016/03/18
 */
public class UpdateUtils {

    //版本更新
    /// v3 2016/03/18
    public static void initUpdate(final Context context) {

        String emobId = null;
        int communityId;
        if (PreferencesUtil.getLogin(context)) {
            emobId = PreferencesUtil.getLoginInfo(context).getEmobId();
            communityId = PreferencesUtil.getLoginInfo(context).getCommunityId();
        } else {
            if (PreferencesUtil.getTouristLogin(context)) {
                emobId = PreferencesUtil.getTourist(context);
            } else {
                emobId = PreferencesUtil.getlogoutEmobId(context);
            }
            communityId = PreferencesUtil.getCommityId(context);
        }
        NetBaseUtils.getAppUpdateInfoV3(context, communityId, emobId, new NetBaseUtils.NetRespListener<CommonRespBean<UpDateApp.Info>>() {
            @Override
            public void successYes(CommonRespBean<UpDateApp.Info> commonRespBean, Response response) {
                PreferencesUtil.saveCheckTime(context, System.currentTimeMillis());
                String version = "";
                try {
                    version = getVersionName(context);
                } catch (Exception e) {
                }
                if (equalsVersion(commonRespBean.getData().getVersion(), version)) {
//                if(true){
                    new UpdateManager(context, commonRespBean.getData().version, "false", commonRespBean.getData().url, commonRespBean.getData().detail);
//                    new UpdateManager(MainActivity.this, Object.getInfo().version, "false", "http://7d9lcl.com2.z0.glb.qiniucdn.com/bangbang_c.apk", Object.getInfo().detail);
                    UpdateManager.showNoticeDialog(context);
                }
            }

            @Override
            public void successNo(CommonRespBean<UpDateApp.Info> commonRespBean, Response response) {
                PreferencesUtil.saveCheckTime(context, System.currentTimeMillis());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("onion", "失败" + error.toString());
            }
        });
    }

    private static String getVersionName(Context context) throws Exception {
// 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
// getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }

    private static boolean equalsVersion(String serviceVersion, String localVersion) {
//        Log.i("equalsVersion","serviceVersion"+serviceVersion);
//        Log.i("equalsVersion","localVersion"+localVersion);
        boolean b = false;
        int serviceint, localint;
        String[] serviceVersions = serviceVersion.split("\\.");
        String[] loaclVersions = localVersion.split("\\.");
        for (int i = 0; i < serviceVersions.length; i++) {
            serviceint = Integer.parseInt(serviceVersions[i]);
            localint = Integer.parseInt(loaclVersions[i]);
//            Log.i("equalsVersion","serviceint"+serviceint);
            if (serviceint > localint) {
                b = true;
                break;
            } else if (serviceint < localint) {
                b = false;
                break;
            }
        }

        return b;
    }
}
