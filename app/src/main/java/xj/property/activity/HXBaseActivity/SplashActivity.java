package xj.property.activity.HXBaseActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.query.Update;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.igexin.sdk.PushManager;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebView;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.PUT;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.user.GuideActivity;
import xj.property.activity.user.LocationActivity;
import xj.property.beans.AppLoginInfoBean;
import xj.property.beans.CommonPostResultBean;
import xj.property.beans.CurrentAppVersionReqBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.cache.ActivityReaded;
import xj.property.cache.GroupHeader;
import xj.property.cache.OrderDetailModel;
import xj.property.cache.OrderModel;
import xj.property.cache.ServiceModel;
import xj.property.cache.ShopContact;
import xj.property.cache.XJNotify;
import xj.property.service.DaemonService;
import xj.property.service.FirstLoadingX5Service;
import xj.property.syncadapter.SyncUtils;
import xj.property.ums.UmsAgent;
import xj.property.utils.XJPushManager;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.message.XJMessageHelper;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

/**
 * 开屏页
 */
public class SplashActivity extends HXBaseActivity {
    private RelativeLayout rootLayout;
    private TextView versionText;

    private static final int sleepTime = 2500;
    private boolean isFirst = true;


    private Handler handler = new Handler();


    private long oncreatetimestart;
    private long oncreatetimeend;
    private long enterGuideTime;

    @Override
    protected void onCreate(Bundle arg0) {
        setContentView(R.layout.activity_splash);
        super.onCreate(arg0);

        oncreatetimestart = System.currentTimeMillis();

//        if(FileUtils.isExist("/data/data/xj.property/app_bin/daemon")){

        if (!Config.PHONETYPE.equals(Build.MANUFACTURER)) {
            startService(new Intent(this, DaemonService.class));
            SyncUtils.CreateSyncAccount(getApplicationContext());
        }
        //// 预加载 X5 webview 内核
        preinitX5WithService();

//        }
        Log.i("SplashActivity", "oncreatetimestart " + oncreatetimestart + "afterdaemonService " + System.currentTimeMillis() + " used: " + (System.currentTimeMillis() - oncreatetimestart) + " ms");
        rootLayout = (RelativeLayout) findViewById(R.id.splash_root);
        versionText = (TextView) findViewById(R.id.tv_version);
        versionText.setText(getVersion());
        isFirst = PreferencesUtil.isFirstOpen(this);

        int notifyDelete = PreferencesUtil.isNotifyDelete(this);
        if (notifyDelete == 0) {
            // new Delete().from(XJNotify.class).execute();
            new Update(XJNotify.class).set("read_status = ?", "no").execute();
            PreferencesUtil.saveNotifyDelete(this);
        }
        // reocderActive();
        ///  2016/1/5 start
        if (isFirst) {
            new ShopContact().save();
//            new OrderRepair().save();
            new OrderModel().save();
            new OrderDetailModel().save();
            new GroupHeader().save();
            new ServiceModel("2313454").save();
//            new ActivityModel().save();
//            new ActivityPhoneModel().save();
//            new Delete().from(ActivityModel.class).execute();
            Log.i("onion", "第一次安装，插入数据库");
            new ActivityReaded().save();
            PreferencesUtil.saveLogin(this, false);
            PreferencesUtil.saveUnReadCount(this, 0);
        } else if (PreferencesUtil.isFirstUpdate(this, UserUtils.getVersion(this)) == 0) {
            //// 标记首次升级之后，将首页请求的时间设置0
            PreferencesUtil.saveFirstUpdate(this, UserUtils.getVersion(this), (int) (System.currentTimeMillis() / 1000));
            PreferencesUtil.saveIndexTime(this,0);
        }
        //// 2016/1/5 end
        /// 加载本地信息
//        new Thread(enterLoadNativeConversation).start();
//        String savedVersionName = PreferencesUtil.getSavedVersionName(getmContext());
//        Log.i("SplashActivity ", "savedVersionName is: "+ savedVersionName);
//        Log.i("SplashActivity ", "getVersion is: "+ UserUtils.getVersion(getmContext()));
//        if(TextUtils.isEmpty(savedVersionName)){
//            Log.i("SplashActivity ", "savedVersionName is empty ");
//            putCurrentAppVersionCall();
//        }else  if(!equalsVersion(UserUtils.getVersion(getmContext()),savedVersionName)){
//            Log.i("SplashActivity ", "savedVersionName no equals  ");
//            putCurrentAppVersionCall();
//        }

        loadNativeConversation();
        oncreatetimeend = System.currentTimeMillis();
        Log.i("SplashActivity", "oncreatetimestart " + oncreatetimestart + " oncreatetimeend   " + oncreatetimeend + " oncreateTime used " + (oncreatetimeend - oncreatetimestart) + "ms");
    }

    private boolean equalsVersion(String serviceVersion, String localVersion) {
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


    private void initAgent() {
        UmsAgent.onError(this);
        //  UmsAgent.update(this);
    }


    private String getVersionName() throws Exception {
// 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
// getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }


    private long enterMaintime;


    Runnable enterMainRunnable = new Runnable() {
        @Override
        public void run() {
            enterMain();
        }
    };


    private void enterMain() {
        boolean hasMove = PreferencesUtil.getHasMove(getApplicationContext());
        if (hasMove) {
            Intent intent = new Intent(SplashActivity.this, LocationActivity.class);
            PreferencesUtil.saveHasMove(getApplicationContext(), true);
            intent.putExtra("isMove", true);
            startActivity(intent);
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        enterMaintime = System.currentTimeMillis();
        Log.i("SplashActivity", "load to enterMaintime  used  " + (enterMaintime - loadstart) + " main to create end   " + (enterMaintime - oncreatetimeend) + " main to create start " + (enterMaintime - oncreatetimestart));
        finish();
    }


    private void enterGuide() {

        startActivity(new Intent(SplashActivity.this, GuideActivity.class));
        enterGuideTime = System.currentTimeMillis();

        Log.i("SplashActivity", " load to enterGuide used:   " + (System.currentTimeMillis() - loadstart) + "ms" + " total time : " + (System.currentTimeMillis() - oncreatetimestart) + "ms");
        finish();
    }


    Runnable enterGuidRunnable = new Runnable() {
        @Override
        public void run() {
            enterGuide();
        }
    };


    Runnable enterLoadNativeConversation = new Runnable() {
        @Override
        public void run() {
            loadNativeConversation();
        }
    };

    long loadstart;

    private void loadNativeConversation() {
        loadstart = System.currentTimeMillis();

        if (EMChat.getInstance() != null && EMChat.getInstance().isLoggedIn() && PreferencesUtil.getLogin(SplashActivity.this)) {
            //// 更新首次升级后的时间
            if (PreferencesUtil.isFirstUpdate(getmContext(), UserUtils.getVersion(getmContext())) == 0) {
                PreferencesUtil.saveFirstUpdate(getmContext(), UserUtils.getVersion(getmContext()), (int) (System.currentTimeMillis() / 1000));
                PreferencesUtil.saveIndexTime(this,0);
            }

            // ** 免登陆情况 加载所有本地群和会话

            //不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
            //加上的话保证进了主页面会话和群组都已经load完毕
            XJMessageHelper.loadNativeMessage();

            EMChatManager.getInstance().getChatOptions().setReceiveNotNoifyGroup(PreferencesUtil.getUnNotifyGroupS(SplashActivity.this));
//					EMGroupManager.getInstance().loadAllGroups();
//					EMChatManager.getInstance().loadAllConversations();

//  handler.post(enterMainRunnable);
//
            enterMain();

        } else {

            if (isFirst) {
//                handler.post(enterGuidRunnable);

                enterGuide();

            } else {
                //// 更新首次升级后的时间
                if (PreferencesUtil.isFirstUpdate(getmContext(), UserUtils.getVersion(getmContext())) == 0) {
                    PreferencesUtil.saveFirstUpdate(getmContext(), UserUtils.getVersion(getmContext()), (int) (System.currentTimeMillis() / 1000));
                    PreferencesUtil.saveIndexTime(this,0);
                }
//                handler.post(enterMainRunnable);

                enterMain();

            }
        }
    }

    /**
     * 登录app信息
     */
    interface AppLoginService {
        @PUT("/api/v2/communities/{communityId}/users/{emobId}/updateEquipment")
        void putCurrentAppVersion(@Header("signature") String signature, @Body CurrentAppVersionReqBean appVersionReqBean, @Path("communityId") int communityId, @Path("emobId") String emobId, Callback<CommonPostResultBean> cb);
    }

    /**
     * 将本地版本信息更新到Server.  升级之后调用.
     */
    private void putCurrentAppVersionCall() {

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        if (Config.SHOW_lOG) {
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        }
        AppLoginService isUserExistService = restAdapter.create(AppLoginService.class);
        Callback<CommonPostResultBean> callback = new Callback<CommonPostResultBean>() {
            @Override
            public void success(CommonPostResultBean commonPostResultBean, Response response) {
                if (commonPostResultBean != null && "yes".equals(commonPostResultBean.getStatus())) {
                    Log.i("SplashActivity", "putCurrentAppVersionCall success");
                    /// 更新成功之后, 将最新应用版本保存到sp文件中,以便下次更新使用.
                    PreferencesUtil.saveCurrentVersionName(getmContext(), UserUtils.getVersion(getmContext()));
                } else {

                    Log.i("SplashActivity", "putCurrentAppVersionCall error");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("SplashActivity", "RetrofitError error" + error.toString());
//                showNetErrorToast();
            }
        };

        final CurrentAppVersionReqBean userBean = new CurrentAppVersionReqBean();
        userBean.setMethod("PUT");
        if (Config.PHONETYPE.equals(Build.MANUFACTURER)) {
            userBean.setEquipment("mi");
        } else {
            userBean.setEquipment("android");
        }
        userBean.setEquipmentVersion(UserUtils.getVersion(getmContext()));
        UserInfoDetailBean loginInfo = PreferencesUtil.getLoginInfo(getmContext());
        String emobid;
        if (loginInfo != null) {
            emobid = loginInfo.getEmobId();
        } else {
            emobid = PreferencesUtil.getTourist(getmContext());
        }
//        getSavedVersionName
        isUserExistService.putCurrentAppVersion(StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(userBean)),
                userBean, PreferencesUtil.getCommityId(getmContext()), emobid, callback);
    }



    /**
     * 获取当前应用程序的版本号
     */
    private String getVersion() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packinfo = pm.getPackageInfo(getPackageName(), 0);
            String version = packinfo.versionName;
            return version;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "版本号错误";
        }
    }
    /**
     * 开启额外进程 服务 预加载X5内核， 此操作必须在主进程调起X5内核前进行，否则将不会实现预加载
     */
    private void preinitX5WithService() {
        Intent intent = new Intent(SplashActivity.this, FirstLoadingX5Service.class);
        startService(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
//        AlphaAnimation animation = new AlphaAnimation(0.4f, 1.0f);
//        animation.setAnimationListener(new MyAnimationListener());
//        animation.setDuration(500);
//        rootLayout.startAnimation(animation);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
