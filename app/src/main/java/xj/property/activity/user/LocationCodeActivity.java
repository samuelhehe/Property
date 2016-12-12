package xj.property.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.MainActivity;
import xj.property.beans.CommunityInfo4Code;
import xj.property.beans.TouristResult;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.ums.UmsAgent;
import xj.property.utils.XJPushManager;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

public class LocationCodeActivity extends HXBaseActivity {
    EditText et_location_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_code);
        et_location_code = (EditText) findViewById(R.id.et_location_code);
        findViewById(R.id.go_in_bangbang).setOnClickListener(this);
        findViewById(R.id.ib_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go_in_bangbang:
                if ("".equals(et_location_code.getText().toString().trim())) {
                    showToast("请输入定位码");
                } else {
                    getCommunityList(et_location_code.getText().toString().trim());
                }
                break;
            case R.id.ib_back:
                finish();
                break;
        }
    }


    interface getCommunityListService {
//        @GET("/api/v1/communities/latitude/{code}")
//        void getCommunityListInfo(@Path("code") String emobId, Callback<CommunityInfo4Code> cb);

        @GET("/api/v3/communities/locationByCode")
        void getCommunityListInfoV3(@QueryMap HashMap<String, String> map, Callback<CommonRespBean<CommunityInfo4Code.InfoEntity>> cb);
    }

    private void getCommunityList(String code) {
        mLdDialog.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("code", code);
        getCommunityListService service = RetrofitFactory.getInstance().create(getmContext(),map,getCommunityListService.class);
        Callback<CommonRespBean<CommunityInfo4Code.InfoEntity>> callback = new Callback<CommonRespBean<CommunityInfo4Code.InfoEntity>>() {
            @Override
            public void success(CommonRespBean<CommunityInfo4Code.InfoEntity> bean, retrofit.client.Response response) {
                if (bean != null) {
                    if ("yes".equals(bean.getStatus())) {
//                        loadDeviceInfo(bean.getData().getCommunityId());
                        switchActivity(bean.getData().getCommunityId(), bean.getData().getCommunityName());
                    } else {
                        showDataErrorToast(bean.getMessage());
                    }
                } else {
                    showDataErrorToast();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                showNetErrorToast();
                error.printStackTrace();
            }
        };
        service.getCommunityListInfoV3(map, callback);
    }

//    interface loadDeviceInfoService {
//        @POST("/api/v1/communities/{communityId}/devices/")
//        void getDeviceInfo(@Header("signature") String signature, @Body DeviceInfo deviceInfo, @Path("communityId") int communityId, Callback<Object> callback);
//    }

//    public void loadDeviceInfo(int commityId) {
//        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_AGENT).build();
//        loadDeviceInfoService service = restAdapter.create(loadDeviceInfoService.class);
//        Callback<Object> callback = new Callback<Object>() {
//            @Override
//            public void success(Object Object, retrofit.client.Response response) {
//                Log.i("onion", "成功" + Object);
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Log.i("onion", "失败" + error.toString());
//            }
//        };
        JSONObject jsonObject = UmsAgent.getClientDataJSONObject(this);
//        DeviceInfo info = new DeviceInfo();
//        info.platform = jsonObject.optString("platform");
//        info.cellId = jsonObject.optString("cellid");
//        info.isMobileDevice = jsonObject.optBoolean("ismobiledevice") ? 1 : 0;
//        info.userId = jsonObject.optString("userid");
//        info.appkey = jsonObject.optString("appkey");
//        info.resolution = jsonObject.optString("resolution");
//        info.lac = jsonObject.optString("lac");
//        info.network = jsonObject.optString("network");
//        info.version = jsonObject.optString("version");
//        info.deviceId = jsonObject.optString("deviceid");
//        info.os_version = jsonObject.optString("os_version");
//        info.havebt = jsonObject.optBoolean("havebt") ? 1 : 0;
//        info.haveGps = jsonObject.optBoolean("havegps") ? 1 : 0;
//        info.phoneType = jsonObject.optInt("phonetype");
//        info.moduleName = jsonObject.optString("modulename");
//        info.time = new Date().getTime() / 1000;
//        info.wifiMac = jsonObject.optString("wifimac");
//        info.deviceName = jsonObject.optString("devicename");
//        info.longitude = jsonObject.optString("longitude");
//        info.mccmnc = jsonObject.optString("mccmnc");
//        info.latitude = jsonObject.optString("latitude");
//        info.language = jsonObject.optString("language");
//        info.haveGravity = jsonObject.optBoolean("havegravity") ? 1 : 0;
//        service.getDeviceInfo(
//                StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(info)), info, commityId, callback);
//    }

    public void switchActivity(int communityId, String communityName) {
//        getHaveNewActivity(communityId, communityName);
        mLdDialog.show();
        mLdDialog.setCancelable(false);
        UserUtils.getTourist(this, communityId, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mLdDialog.dismiss();
                switch (msg.what) {
                    case Config.TouristGETEMOBID:
                        //  String emobId = (String) msg.obj;
                        TouristResult result = (TouristResult) msg.obj;
                        Log.i("onion", "游客emobId" + result.getInfo().getEmobId() + " 游客id： " + result.getInfo().getUserId());

                        if (xjpushManager == null) {
                            xjpushManager = new XJPushManager(getmContext());
                        }
                        xjpushManager.initPushService();
                        xjpushManager.registerTouristPushService();

                        //PreferencesUtil.saveTourist(LocationCodeActivity.this,emobId);
                        PreferencesUtil.saveTourist(LocationCodeActivity.this, result.getInfo().getEmobId(), result.getInfo().getUserId());
                        PreferencesUtil.saveTourist(LocationCodeActivity.this, true);
                        startActivity(new Intent(LocationCodeActivity.this, MainActivity.class));
                        finish();
                        break;
                    case Config.TouristLoginError:
                        showNetErrorToast();
                        break;
                }
            }
        });

    }


//    interface ActListService {
//        @GET("/api/v1/communities/{communityId}/users/{emobIdUser}/activities")
//        void getActList(@Path("communityId") int communityId, @Path("emobIdUser") String emobIdUser, @QueryMap Map<String, String> map, Callback<ActivitiesSearchBean> cb);
//    }
//
//    public void getHaveNewActivity(int communityId, String communityName) {
//
//        final SharedPreferences spf = getSharedPreferences(
//                PreferencesUtil.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
//        spf.edit().putBoolean(PreferencesUtil.FIRST_OPEN, false).commit();
//        PreferencesUtil.saveCommity(LocationCodeActivity.this, communityId, communityName);
//        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
//        ActListService service = restAdapter.create(ActListService.class);
//        Callback<ActivitiesSearchBean> callback = new Callback<ActivitiesSearchBean>() {
//            @Override
//            public void success(ActivitiesSearchBean bean, retrofit.client.Response response) {
//                if (bean != null) {
//                    PreferencesUtil.saveActivityBean(LocationCodeActivity.this, bean.getInfo().getData());
//                    PreferencesUtil.saveUnReadCount(LocationCodeActivity.this, bean.getInfo().getData().size());
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                error.printStackTrace();
//            }
//        };
//        Map<String, String> option = new HashMap<String, String>();
//        option.put("pageNum", "1");
//        option.put("pageSize", "10");
////        "qiNiuId":2,"qiNiuType":"user"
//        service.getActList(communityId, "null", option, callback);
//
//    }
}
