package xj.property.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.easemob.chat.EMChatManager;
import com.igexin.sdk.PushManager;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

import xj.property.XjApplication;
import xj.property.beans.UserInfoDetailBean;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

/**
 * Created by Administrator on 2016/1/20.
 * =====================================
 * 这里是推送整体的初始化,绑定/解绑管理类
 * <p/>
 * 初始化策略:
 * 1. 如果是小米机型, 仅仅初始化小米推送, 关闭个推/极光推送
 * 2. 如果不是小米机型, 全部初始化. （由于小米推送后台没有使用，暂时注释掉，根据后台进行是否进行注册 2016/01/29 ）
 *
 * <p/>
 * 绑定策略:
 * <p/>
 * 1) 在登陆状态下绑定:
 * 1. 如果是小米机型, 仅仅绑定小米推送,解绑个推/极光推送
 * 2. 如果不是小米机型, 全部绑定.
 * 2) 在注册定位小区下绑定:
 * 1. 绑定游客emobid.
 * <p/>
 * 解绑策略:
 * 1. 在退出登录的状态下,全部解绑,保证用户不再收到推送
 * NOTE: 退出登录包含, 1, 用户手动退出, 2, 账号冲突被挤掉的退出.均要处理.
 */
public class XJPushManager {

    private static final String TAG = "XJPushManager";

    private String emobid;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private Context context;

    private UserInfoDetailBean userInfoDetailBean;

    public XJPushManager(Context context) {
        this.context = context;
        if (context == null) {
            throw new RuntimeException("xjpushmanager context is null !!!");
        }
        userInfoDetailBean = PreferencesUtil.getLoginInfo(context);
        if (userInfoDetailBean != null) {
            this.emobid = userInfoDetailBean.getEmobId();
        } else {
            this.emobid = PreferencesUtil.getTourist(context);
        }
    }

    /**
     * 初始化推送模块
     */
    public void initPushService() {

        Log.d(TAG, "initPushService start ");
        Log.d(TAG, "initPushService PHONETYPE " + Build.MANUFACTURER);

        if (TextUtils.equals(Config.PHONETYPE, Build.MANUFACTURER)) {

            /// 应用处于进程运行时注册.
            if (shouldInit()) {
                Log.d(TAG, "initPushService  MiPushClient.registerPush ");
                MiPushClient.registerPush(context.getApplicationContext(), XjApplication.APP_ID, XjApplication.APP_KEY);
            }
            //// 检查其他是否已经开启,如果开启则关闭
            /// 个推
            if (PushManager.getInstance().isPushTurnedOn(context.getApplicationContext())) {
                PushManager.getInstance().turnOffPush(context.getApplicationContext());
                Log.d(TAG, "initPushService  turnOffPush.gexin  ");
            }

        } else {
            /// 应用处于进程运行时注册.
//            if (shouldInit()) {
//                Log.d(TAG, "initPushService  MiPushClient.registerPush ");
//                MiPushClient.registerPush(context.getApplicationContext(), XjApplication.APP_ID, XjApplication.APP_KEY);
//            }

            /// 环信与小米推送关联设定. 2.2.5 // 160120
//            EMChatManager.getInstance().setMipushConfig(XjApplication.APP_ID, XjApplication.APP_KEY);

            /// 个推
            PushManager.getInstance().initialize(context.getApplicationContext());
            PushManager.getInstance().turnOnPush(context.getApplicationContext());
            Log.d(TAG, "initPushService  PushManager.getui  ");

            /// 信鸽
            XGPushManager.enableService(context.getApplicationContext(),true);
            XGPushConfig.enableDebug(context.getApplicationContext(),true);
            Log.d(TAG, "initPushService  PushManager.xinge  ");
        }
        Log.d(TAG, "initPushService end ");
    }


    /**
     *  检查推送服务是否正常注册运行
     *  不包含绑定注册部分。
     */
    public void checkPushService(){
        Log.d(TAG, "checkPushService start ");
        Log.d(TAG, "checkPushService PHONETYPE " + Build.MANUFACTURER );

           if(!isExistPushserviceProcess(getContext())){
               Log.d(TAG, "checkPushService pushservice is not exist  start init pushservice" );
               initPushService();
           }

        Log.d(TAG, "checkPushService end ");
    }
    public static Boolean  isExistPushserviceProcess(Context context) {
        try {
//            int pid = android.os.Process.myPid();
            ActivityManager mActivityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                    .getRunningAppProcesses()) {
                if(TextUtils.equals(appProcess.processName, ":pushservice")){
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            // ignore
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 解推送
     */
    public void unInitPushService() {

        Log.d(TAG, "unInitPushService start ");
        Log.d(TAG, "unInitPushService PHONETYPE " + Build.MANUFACTURER);

        if (TextUtils.equals(Config.PHONETYPE, Build.MANUFACTURER)) {
            MiPushClient.unregisterPush(context.getApplicationContext());
            Log.d(TAG, "unInitPushService  unregisterPush ");

        }else{
//            MiPushClient.unregisterPush(context.getApplicationContext());
//            Log.d(TAG, "unInitPushService  unregisterPush ");

            //// 检查其他是否已经开启,如果开启则关闭
            /// 个推
            if (PushManager.getInstance().isPushTurnedOn(context.getApplicationContext())) {
                PushManager.getInstance().turnOffPush(context.getApplicationContext());
                Log.d(TAG, "unInitPushService  turnOffPush.gexin  ");
            }
            XGPushManager.unregisterPush(context.getApplicationContext());
            Log.d(TAG, "unInitPushService  turnOffPush.xinge  ");

        }
    }

    /**
     *
     * 绑定制定的emobid
     * @param emobidInner
     */
    public void registerPushService(String emobidInner){

        Log.d(TAG, "registerLoginedPushService PHONETYPE " + Build.MANUFACTURER +" emobidInner "+ emobidInner);
        if(TextUtils.isEmpty(emobidInner)&&!PreferencesUtil.getLogin(context.getApplicationContext())){
            emobidInner = PreferencesUtil.getTourist(context.getApplicationContext());
            Log.d(TAG, "registerLoginedPushService emobid is null  emobidInner "+ emobidInner);

        }
        //// 小米机型
        if (Config.PHONETYPE.equals(Build.MANUFACTURER)) {

            MiPushClient.setAlias(context.getApplicationContext(), emobidInner, null);
            Log.d(TAG, "registerLoginedPushService  MiPushClient.setAlias "+emobidInner+"  success ");


            /// 个推
            if (PushManager.getInstance().isPushTurnedOn(context.getApplicationContext())) {
                PushManager.getInstance().unBindAlias(context.getApplicationContext(), emobidInner, false);
                PushManager.getInstance().turnOffPush(context.getApplicationContext());
                Log.d(TAG, "registerLoginedPushService  turnOffPush.gexin  ");
            }
            /// 信鸽退出
            XGPushManager.unregisterPush(context.getApplicationContext());
            Log.d(TAG, "registerLoginedPushService  unregisterPush.xinge  ");

            //// 极光推送如果开启则关闭
        } else {

//            MiPushClient.setAlias(context.getApplicationContext(), emobidInner, null);
//            Log.d(TAG, "registerLoginedPushService  MiPushClient.setAlias   success ");


            boolean flag = PushManager.getInstance().bindAlias(context.getApplicationContext(), emobidInner);
            Log.d(TAG, "registerLoginedPushService  getui .bindAlias:  " + flag);

            XGPushManager.registerPush(context.getApplicationContext(), emobidInner,
                    new XGIOperateCallback() {
                        @Override
                        public void onSuccess(Object data, int flag) {
                            Log.d(TAG, "xinge注册成功，设备token为：" + data);
                        }

                        @Override
                        public void onFail(Object data, int errCode, String msg) {
                            Log.d(TAG, "xinge注册失败，错误码：" + errCode + ",错误信息：" + msg);
                        }
                    });

            Log.d(TAG, "registerLoginedPushService  xinge .bindAlias:  " + flag);
        }
    }

    /**
     * 注册已登录状态的推送模块
     */
    public void registerLoginedPushService() {
        registerPushService(getEmobid());
    }

    /**
     * 解绑推送模块
     */
    public void unregisterLoginedPushService() {
        Log.d(TAG, "unregisterLoginedPushService PHONETYPE " + Build.MANUFACTURER+ " unbind getEmobid "+ getEmobid());
        if (Config.PHONETYPE.equals(Build.MANUFACTURER)) {
            MiPushClient.unsetAlias(context.getApplicationContext(), getEmobid(), null);
            Log.d(TAG, "unregisterLoginedPushService   MiPushClient  clear alias success ");
        } else {
//            MiPushClient.unsetAlias(context.getApplicationContext(), getEmobid(), null);
//            Log.d(TAG, "unregisterLoginedPushService   MiPushClient  clear alias success ");

            boolean flag = PushManager.getInstance().unBindAlias(context.getApplicationContext(), getEmobid(), false);
            Log.d(TAG, "unregisterLoginedPushService   getui  clear alias success " + flag);

            /// 信鸽
            XGPushManager.registerPush(context, "*", new XGIOperateCallback() {
                @Override
                public void onSuccess(Object o, int i) {
                    Log.d(TAG, "unregisterLoginedPushService xinge onSuccess  "+ i);
                }

                @Override
                public void onFail(Object o, int i, String s) {
                    Log.d(TAG, "unregisterLoginedPushService xinge onFail  "+ i +" s "+ s);
                }
            } );

        }
    }

    /**
     * 注册游客状态的推送
     */
    public void registerTouristPushService() {

        registerLoginedPushService();
    }

    public String getEmobid() {
        return emobid;
    }

    public void setEmobid(String emobid) {
        this.emobid = emobid;
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = context.getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

}
