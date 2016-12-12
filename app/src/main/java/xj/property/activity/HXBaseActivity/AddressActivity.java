package xj.property.activity.HXBaseActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.XjApplication;
import xj.property.activity.ABaseActivity;
import xj.property.adapter.AddressAdapter;
import xj.property.beans.ActivitiesSearchBean;
import xj.property.beans.DeviceInfo;
import xj.property.beans.LocationVerficationRespBean;
import xj.property.beans.MoveAllBean;
import xj.property.beans.MoveInfoBean;
import xj.property.beans.MovePostBean;
import xj.property.beans.TouristBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.ums.UmsAgent;
import xj.property.utils.XJPushManager;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.LocationUtil;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

/**
 * 作者：asia on 2016/1/13 13:58
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：定位地址页面
 */
public class AddressActivity extends ABaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private CommonRespBean<List<LocationVerficationRespBean.InfoEntity>> locationVerficationRespBean;
    private List<LocationVerficationRespBean.InfoEntity> searchResultList = new ArrayList<LocationVerficationRespBean.InfoEntity>();
    private LocationVerficationRespBean.InfoEntity mInfoEntity;

    private LinearLayout mLl_back;
    private LinearLayout mLl_empty;
    private EditText mTv_address;
    private Button mGo_verfication_btn;
    private ListView mLv_address;
    private BDLocation location;
    private boolean isSubmit = false;

    private AddressAdapter mAddressAdapter;
    private boolean hasMove;

    private boolean isItemclickActivation = false;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (LocationUtil.LOCATIONSUCCESS == msg.what) {
                location = (BDLocation) msg.obj;
            }
        }
    };

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.activity_address);
        hasMove = PreferencesUtil.getHasMove(getApplicationContext());
    }

    @Override
    protected void initView() {
        mLl_back = (LinearLayout) findViewById(R.id.ll_back);
        mLl_empty = (LinearLayout) findViewById(R.id.ll_empty);
        mTv_address = (EditText) findViewById(R.id.tv_address);
        mGo_verfication_btn = (Button) findViewById(R.id.go_verfication_btn);
        mLv_address = (ListView) findViewById(R.id.lv_address);
    }

    @Override
    protected void initDate() {
        LocationUtil.getLocation(this, mHandler);
        List<LocationVerficationRespBean.InfoEntity> mlist = new ArrayList<LocationVerficationRespBean.InfoEntity>();
        mAddressAdapter = new AddressAdapter(AddressActivity.this, mlist);
        mLv_address.setAdapter(mAddressAdapter);
    }

    @Override
    protected void initListenner() {
        mLl_back.setOnClickListener(this);
        mGo_verfication_btn.setOnClickListener(this);
        mLv_address.setOnItemClickListener(this);
        mTv_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchKey = s.toString();
                if (!TextUtils.isEmpty(searchKey) && searchKey.length() >= 2 && !isItemclickActivation) {
                    getCommunitid(searchKey);
                } else {
                    searchResultList.clear();
                    mAddressAdapter.ChangeRefresh(searchResultList);
                    mLl_empty.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.go_verfication_btn:
                String s = mTv_address.getText().toString();
                PreferencesUtil.setRefreshSame(getApplicationContext(), true);
                mLdDialog.show();
                if (!hasMove) {
                    if (!TextUtils.isEmpty(s) && location != null && mInfoEntity != null && locationVerficationRespBean != null) {
                        if (mInfoEntity.getDistance() <= locationVerficationRespBean.getField("maxDistance", Double.class)) {
                            enterMain();
                        } else {
                            showLocation("您没在自己的小区吧？", "请回到小区后再尝试以上步骤，认 识您的真实邻居一定会给你带来很 多惊喜。");
                        }
                    } else if (!TextUtils.isEmpty(s) && mInfoEntity == null) {
                        if (searchResultList != null && searchResultList.size() > 0) {
                            mInfoEntity = searchInfoEntity(searchResultList, s);
                            if (mInfoEntity.getDistance() <= locationVerficationRespBean.getField("maxDistance", Double.class)) {
                                enterMain();
                            } else {
                                showLocation("您没在自己的小区吧？", "请回到小区后再尝试以上步骤，认 识您的真实邻居一定会给你带来很 多惊喜。");
                            }
                        } else {
                            mLdDialog.dismiss();
                            isSubmit = true;
                            getCommunitid(s);
                        }
                    } else if (location == null) {
                        Toast.makeText(getApplicationContext(), "亲，我们正在努力定位您的小区，请隔5秒钟再试,或者检查一下手机定位是否开启\n(〜￣▽￣)〜", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "请输入小区", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    PreferencesUtil.setRefreshIndexCache(getApplicationContext(), true);
                    if (!TextUtils.isEmpty(s) && location != null && mInfoEntity != null && locationVerficationRespBean != null) {
                        if (mInfoEntity.getDistance() <= locationVerficationRespBean.getField("maxDistance", Double.class)) {
                            String emobid = "";
                            UserInfoDetailBean info = PreferencesUtil.getLoginInfo(getApplicationContext());
                            if (info != null) {
                                emobid = info.getEmobId();
                            } else {
                                emobid = PreferencesUtil.getTouristEmob(getApplicationContext());
                            }
                            if ("".equals(emobid)) {
                                mLdDialog.show();
//                                loadDeviceInfo();
                                switchActivity();
                            } else {
                                getMove();
                            }
                        } else {
                            showLocation("您没在自己的小区吧？", "请回到小区后再尝试以上步骤，认 识您的真实邻居一定会给你带来很 多惊喜。");
                        }
                    } else if (!TextUtils.isEmpty(s) && mInfoEntity == null) {
                        if (searchResultList != null && searchResultList.size() > 0) {
                            mInfoEntity = searchInfoEntity(searchResultList, s);
                            if (mInfoEntity.getDistance() <= locationVerficationRespBean.getField("maxDistance", Double.class)) {
                                enterMain();
                            } else {
                                showLocation("您没在自己的小区吧？", "请回到小区后再尝试以上步骤，认 识您的真实邻居一定会给你带来很 多惊喜。");
                            }
                        } else {
                            mLdDialog.dismiss();
                            isSubmit = true;
                            getCommunitid(s);
                        }
                    } else if (location == null) {
                        Toast.makeText(getApplicationContext(), "亲，我们正在努力定位您的小区，请隔5秒钟再试,或者检查一下手机定位是否开启\n(〜￣▽￣)〜", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "请输入小区", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private LocationVerficationRespBean.InfoEntity searchInfoEntity(List<LocationVerficationRespBean.InfoEntity> searchResultList, String address) {
        for (int i = 0; i < searchResultList.size(); i++) {
            LocationVerficationRespBean.InfoEntity info = searchResultList.get(i);
            if (info.getCommunityName().equals(address)) {
                return info;
            }
        }
        return searchResultList.get(0);
    }

    private void enterMain() {
//        loadDeviceInfo();
        switchActivity();

        Log.i("debbug", "getLatitude=" + mInfoEntity.getLatitude());
        Log.i("debbug", "getLongitude=" + mInfoEntity.getLongitude());
        PreferencesUtil.saveCommity(AddressActivity.this,mInfoEntity.getCommunityId(),mInfoEntity.getCommunityName());
        PreferencesUtil.saveLatitude(AddressActivity.this, "" + mInfoEntity.getLatitude());
        PreferencesUtil.saveLongitude(AddressActivity.this, "" + mInfoEntity.getLongitude());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mInfoEntity = searchResultList.get(position);
        isItemclickActivation = true;
        String choiceds = mInfoEntity.getCommunityName();
        if (!TextUtils.isEmpty(choiceds)) {
            mTv_address.setText(choiceds);
            mTv_address.setSelection(choiceds.length());
        }
        isItemclickActivation = false;
        mLv_address.setVisibility(View.GONE);
        mLl_empty.setVisibility(View.VISIBLE);
    }

    interface getAddressService {
//        @GET("/api/v1/communities/latitude")
//        void getCommId(@QueryMap HashMap<String, String> map, Callback<LocationVerficationRespBean> cb);

        @GET("/api/v3/communities/location")
        void getCommIdV3(@QueryMap HashMap<String, String> map, Callback<CommonRespBean<List<LocationVerficationRespBean.InfoEntity>>> cb);


//        @POST("/api/v1/communities/{communityId}/devices/")
//        void getDeviceInfo(@Header("signature") String signature, @Body DeviceInfo deviceInfo, @Path("communityId") int communityId, Callback<Object> callback);

//        @GET("/api/v1/communities/{communityId}/users/{emobIdUser}/activities")
//        void getActList(@Path("communityId") int communityId, @Path("emobIdUser") String emobIdUser, @QueryMap Map<String, String> map, Callback<ActivitiesSearchBean> cb);

//        @POST("/api/v1/communities/{communityId}/users/{emobId}/move")
//        void getMove(@Header("signature") String signature, @Body MovePostBean bean, @Path("communityId") int communityId, @Path("emobId") String emobId, Callback<MoveAllBean> cb);


        @POST("/api/v3/communities/{communityId}/users/{emobId}/move")
        void getMoveV3(@Body MovePostBean bean, @Path("communityId") int communityId, @Path("emobId") String emobId, Callback<CommonRespBean<MoveInfoBean>> cb);

    }

    public void getMove() {
        mLdDialog.show();

        UserInfoDetailBean info = PreferencesUtil.getLoginInfo(getApplicationContext());
        MovePostBean movePostBean = new MovePostBean();
        String emobid = "";
        if (info != null) {
            movePostBean.setPassword(info.getPassword());
            emobid = info.getEmobId();
        } else {
            movePostBean.setPassword("123");
            emobid = PreferencesUtil.getTouristEmob(getApplicationContext());
        }


        getAddressService service = RetrofitFactory.getInstance().create(getApplicationContext(),movePostBean,getAddressService.class);
        Callback<CommonRespBean<MoveInfoBean>> callback = new Callback<CommonRespBean<MoveInfoBean>>() {
            public void success(CommonRespBean<MoveInfoBean> bean, retrofit.client.Response response) {
                mLdDialog.dismiss();
                if (bean != null) {
                    if ("yes".equals(bean.getStatus()) && null != bean.getData()) {
                        UserInfoDetailBean userBean = PreferencesUtil.getLoginInfo(getApplicationContext());
                        MoveInfoBean bean1 = bean.getData();
                        if (userBean != null) {
                            userBean.setEmobId(bean1.getEmobId());
                            userBean.setUserId(bean1.getUserId());
                            userBean.setNickname(bean1.getNickname());
                            userBean.setAge(bean1.getAge());
                            userBean.setAvatar(bean1.getAvatar());
                            userBean.setGender(bean1.getGender());
                            userBean.setStatus(bean1.getStatus());
                            userBean.setSignature(bean1.getSignature());
                            userBean.setRole(bean1.getRole());
                            userBean.setCommunityId(bean1.getCommunityId());
                            userBean.setIdentity(bean1.getIdentity());
                            userBean.setGrade(bean1.getGrade());
                            PreferencesUtil.saveLogin(getApplicationContext(), userBean);
                            PreferencesUtil.saveHasMove(getApplicationContext(), false);
                            moveActivity();
                        } else {
//                            loadDeviceInfo();
                            switchActivity();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "" + (bean != null ? bean.getMessage() : "亲，服务器出错了，我们会尽快抢修！"), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "亲，服务器出错了，我们会尽快抢修！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                Toast.makeText(getApplicationContext(), "亲，请检查您的网络！", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        };
        service.getMoveV3( movePostBean, mInfoEntity.getCommunityId(), "".equals(emobid) ? "-1" : emobid, callback);
    }


    public void getCommunitid(String searchKey) {
        HashMap<String, String> map = new HashMap<>();
        map.put("latitude", String.valueOf(location.getLatitude()));
        map.put("longitude", String.valueOf(location.getLongitude()));
        map.put("name", searchKey);
        getAddressService service = RetrofitFactory.getInstance().create(getApplicationContext(),map,getAddressService.class);
        Callback<CommonRespBean<List<LocationVerficationRespBean.InfoEntity>>> callback = new Callback<CommonRespBean<List<LocationVerficationRespBean.InfoEntity>>>() {
            @Override
            public void success(CommonRespBean<List<LocationVerficationRespBean.InfoEntity>> bean, retrofit.client.Response response) {
                if (bean != null && "yes".equals(bean.getStatus())) {
                    locationVerficationRespBean = bean;
                    List<LocationVerficationRespBean.InfoEntity> info = bean.getData();
                    if (info != null) {
                        if (info.size() <= 0) {
                            searchResultList.clear();
                            mLl_empty.setVisibility(View.VISIBLE);
                            mAddressAdapter.ChangeRefresh(searchResultList);
                            if (isSubmit) {
                                showLocation("邻居帮帮没有发现你输入的小区。请检查输入小区名称是否正确！", "认识你的真实邻居一定会给你带来很多惊喜");
                            }
                        } else if (!"".equals(mTv_address.getText().toString())) {
                            mLl_empty.setVisibility(View.GONE);
                            searchResultList.clear();
                            searchResultList.addAll(info);
                            mAddressAdapter.ChangeRefresh(searchResultList);
                        }
                    } else {
                        mLl_empty.setVisibility(View.GONE);
                        searchResultList.clear();
                        mAddressAdapter.ChangeRefresh(searchResultList);
                    }
                    isSubmit = false;
                    mLv_address.setVisibility(View.VISIBLE);
                    mLl_empty.setVisibility(View.GONE);

                } else {
                    Toast.makeText(getApplicationContext(), "亲，服务器出错了，我们会尽快抢修！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                if (isSubmit) {
                    showLocation("请检查你的手机网络是否开启！", "");
                }
                isSubmit = false;
//                ToastUtils.showToast(AddressActivity.this,"网络异常,请稍后重试");
            }
        };

        if (location == null) {
            Toast.makeText(getApplicationContext(), "亲，我们正在努力定位您的小区，请隔5秒钟再试,或者检查一下手机定位是否开启\n(〜￣▽￣)〜", Toast.LENGTH_SHORT).show();
        } else {
            service.getCommIdV3(map, callback);
        }
    }

//    public void loadDeviceInfo() {
//        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_AGENT).build();
//        getAddressService service = restAdapter.create(getAddressService.class);
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
//        JSONObject jsonObject = UmsAgent.getClientDataJSONObject(this);
        DeviceInfo info = new DeviceInfo();
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
//        service.getDeviceInfo(StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(info)), info, mInfoEntity.getCommunityId(), callback);
//    }

    public void switchActivity() {
        UserUtils.getTourist(this, mInfoEntity.getCommunityId(), new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Config.TouristGETEMOBID:
                        mLdDialog.dismiss();
                        CommonRespBean<TouristBean> result = (CommonRespBean<TouristBean>) msg.obj;
                        Log.i("onion", "游客emobId" + result.getData().getEmobId() + " 游客id： " + result.getData().getUserId());
                        touristLogin(result.getData().getEmobId(), result.getData().getUserId());
                        PreferencesUtil.saveHasMove(getApplicationContext(), false);
                        startActivity(new Intent(AddressActivity.this, MainActivity.class));
                        finish();
                        break;
                    case Config.TouristLoginError:
                        mLdDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "网络异常，稍后重试", Toast.LENGTH_SHORT).show();
                        break;
                    case 12:
                        mLdDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "获取失败", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
    }

    public void moveActivity() {
//        moveMiPush();
//        if(xjpushManager!=null){
//            xjpushManager.unregisterLoginedPushService();
//        }else{
//            xjpushManager = new XJPushManager(AddressActivity.this);
//            xjpushManager.unregisterLoginedPushService();
//        }
        moveUser();
    }

    private void moveUser() {
        UserInfoDetailBean userBean = PreferencesUtil.getLoginInfo(getApplicationContext());
        final ProgressDialog pd = new ProgressDialog(AddressActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        UserUtils.moveUser(AddressActivity.this, userBean.getUsername(), userBean.getPassword(), new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Config.LoginUserComplete:
                        setResult(RESULT_OK);
                        if ("0".equals(PreferencesUtil.getCommityId(XjApplication.getInstance()))) {
                            PreferencesUtil.saveLifeCircleCountTime(XjApplication.getInstance(), "" + System.currentTimeMillis() / 1000);
                        }
                        startActivity(new Intent(AddressActivity.this, MainActivity.class));
                        finish();
                        break;
                    case Config.LoginUserFailure:
                        if (!AddressActivity.this.isFinishing()) {
                            pd.dismiss();
                            Toast.makeText(AddressActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case Config.LoginUserSuccess:
                        pd.setMessage("正在登录帮帮..");
                        break;
                    case Config.LogoutTourist:
                        pd.setMessage("正在登出游客..");
                        break;
                    case Config.LoginUserUPDATENATIVE:
                        pd.setMessage("正在加载群，联系人信息..");
                        break;
                }
            }
        });
    }

    private void touristLogin(String emobId, int userId) {
        PreferencesUtil.saveTourist(AddressActivity.this, emobId, userId);
        PreferencesUtil.saveTourist(AddressActivity.this, true);
        if (xjpushManager == null) {
            xjpushManager = new XJPushManager(AddressActivity.this);
        }
        xjpushManager.initPushService();
        xjpushManager.registerPushService(emobId);
    }

    private void showLocation(String line1, String line2) {
        mLdDialog.dismiss();
        final Dialog dialog = new Dialog(AddressActivity.this, R.style.MyDialogStyle);
        dialog.setContentView(R.layout.dialog_location_verfication_notice);
        dialog.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.ll_play_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "01087551266"));
                startActivity(intent);
                dialog.dismiss();
            }
        });
        ((TextView) dialog.findViewById(R.id.lv_notice_line1_tv)).setText("" + line1);
        ((TextView) dialog.findViewById(R.id.lv_notice_line2_tv)).setText("" + line2);
        dialog.show();
    }

}
