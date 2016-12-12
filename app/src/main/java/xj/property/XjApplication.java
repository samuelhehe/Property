/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xj.property;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

import xj.property.domain.User;
import xj.property.ums.UmsAgent;
import xj.property.utils.PackageUtil;
import xj.property.utils.XJPushManager;
import xj.property.utils.other.Config;


public class XjApplication extends Application {

    public static final String APP_ID = "2882303761517351678";
    public static final String APP_KEY = "5331735123678";

    public static Context applicationContext;
    private static XjApplication instance;
    public Vector<String> groupIds = new Vector<>();
    public ExecutorService pool;

    /**
     * 记录已经收取到的messageId
     */
    public Vector<Long> callBackIds = new Vector<>();
    // login user name
    public final String PREF_USERNAME = "username";
    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";

    public static HXSDKHelper hxSDKHelper = new HXSDKHelper();
    /**
     * volley request queue
     */
    private RequestQueue mRequestQueue;
    public static final String TAG = "VolleyPatterns";
    public int grideWidth;
    public int grideHeight;


    public static final String KEY_DEX2_SHA1 = "dex2-SHA1-Digest";
    //   =====================================================================================
    long endTime;
    private long remoteInstallStartTime;
    private static XJPushManager xjpushManager;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.d("loadDex", "App attachBaseContext ");
        String currentProcessName = getCurProcessName(this);
        if (!TextUtils.isEmpty(currentProcessName)) {
            if (currentProcessName.contains(":pushservice") || currentProcessName.contains(":daemon")) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    Log.d("loadDex", "App attachBaseContext current process : " + currentProcessName);
                    remoteInstallStartTime = System.currentTimeMillis();
                    MultiDex.install(this);
                    Log.d("loadDex", "App attachBaseContext current install used : " + (System.currentTimeMillis() - remoteInstallStartTime) + "ms");
                    return;
                }
            }
        }
//        if (!TextUtils.isEmpty(currentProcessName)) {
//            if (currentProcessName.contains(":x5webviewinit")) {
//                Log.d("x5webviewinit", "App attachBaseContext current process : " + currentProcessName);
//                return;
//            }
//        }
        if (!quickStart() && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {//>=5.0的系统默认对dex进行oat优化
            if (needWait(base)) {
                Log.d("loadDex", "enter needwait ");
                waitForDexopt(base);
            }
            long startTime = System.currentTimeMillis();
            Log.d("loadDex", "App MultiDex.install start  " + startTime);

            MultiDex.install(this);
            endTime = System.currentTimeMillis();
            Log.d("loadDex", "App MultiDex.install used " + (endTime - startTime) + "ms");
        }
        Log.d("loadDex", "attachBaseContext " + System.currentTimeMillis());
    }

    public boolean quickStart() {
        String currentProcessName = getCurProcessName(this);
        if (!TextUtils.isEmpty(currentProcessName)) {
            if (currentProcessName.contains(":mini")) {
                Log.d("loadDex", ":mini start!");
                return true;
            }
        }
        return false;
    }

    //neead wait for dexopt ?
    private boolean needWait(Context context) {
        String flag = get2thDexSHA1(context);
        Log.d("loadDex", "dex2-sha1 " + flag);
        SharedPreferences sp = context.getSharedPreferences(
                PackageUtil.getPackageInfo(context).versionName, Context.MODE_MULTI_PROCESS | Context.MODE_WORLD_READABLE);
        String saveValue = sp.getString(KEY_DEX2_SHA1, "");

        Log.d("loadDex", "dex2-sha1 saveValue " + saveValue);
        //// 根据class2dex签名是否相同
        return !TextUtils.equals(flag, saveValue);
    }

    /**
     * Get classes.dex file signature
     *
     * @param context
     * @return
     */
    private String get2thDexSHA1(Context context) {
        ApplicationInfo ai = context.getApplicationInfo();
        String source = ai.sourceDir;
        try {
            JarFile jar = new JarFile(source);
            java.util.jar.Manifest mf = jar.getManifest();
            Map<String, Attributes> map = mf.getEntries();
            Attributes a = map.get("classes2.dex");
            return a.getValue("SHA1-Digest");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // optDex finish
    public void installFinish(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                PackageUtil.getPackageInfo(context).versionName, Context.MODE_MULTI_PROCESS | Context.MODE_WORLD_READABLE);
        sp.edit().putString(KEY_DEX2_SHA1, get2thDexSHA1(context)).commit();
    }

    public static String getCurProcessName(Context context) {
        try {
            int pid = android.os.Process.myPid();
            ActivityManager mActivityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                    .getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    public void waitForDexopt(Context base) {
        Intent intent = new Intent();
//        ComponentName componentName = new
//                ComponentName( "xj.property.activity", xj.property.activity.LoadResActivity.class.getName());

        ComponentName componentName = new ComponentName(this, xj.property.activity.LoadResActivity.class);
        intent.setComponent(componentName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        base.startActivity(intent);
        long startWait = System.currentTimeMillis();
        long waitTime = 3 * 1000;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            waitTime = 10 * 1000;//实测发现某些场景下有些2.3版本有可能10s都不能完成optdex
        }
        while (needWait(base)) {
            try {
                long nowWait = System.currentTimeMillis() - startWait;
                Log.d("loadDex", "wait ms :" + nowWait);
                if (nowWait >= waitTime) {
                    Log.d("loadDex", "now wait ms :" + nowWait);
                    return;
                }
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    //===========================================================================

    long oncreatestart;

    @Override
    public void onCreate() {
        super.onCreate();

        String currentProcessName = getCurProcessName(this);
        if (!TextUtils.isEmpty(currentProcessName)) {
            if (currentProcessName.contains(":pushservice") || currentProcessName.contains(":daemon")) {
                Log.d("loadDex", "App onCreate current process : " + currentProcessName);
                return;
            }
        }
        if (quickStart()) {
            return;
        }
//        LeakCanary.install(this);
        oncreatestart = System.currentTimeMillis();
        Log.d("loadDex", "onCreate " + oncreatestart + "attach to oncreate : " + (oncreatestart - endTime) + "ms");

        ActiveAndroid.initialize(this);
//        initImageLoader(getApplicationContext());
        applicationContext = this;
        instance = this;
        /// 添加错误反馈
        UmsAgent.onError(this);
        pool = Executors.newCachedThreadPool();

        /**
         * this function will initialize the HuanXin SDK
         *
         * @return boolean true if caller can continue to call HuanXin related APIs after calling onInit, otherwise false.
         *
         * 环信初始化SDK帮助函数
         * 返回true如果正确初始化，否则false，如果返回为false，请在后续的调用中不要调用任何和环信相关的代码
         *
         * for example:
         * 例子：
         *
         * public class DemoHXSDKHelper extends HXSDKHelper
         *
         * HXHelper = new DemoHXSDKHelper();
         * if(HXHelper.onInit(context)){
         *     // do HuanXin related work
         * }
         */

        // configureLog4j();
        hxSDKHelper.onInit(applicationContext);

        if (TextUtils.equals(Config.PHONETYPE, Build.MANUFACTURER)) {
            /// 环信与小米推送关联设定. 2.2.5 // 160120
            EMChatManager.getInstance().setMipushConfig(XjApplication.APP_ID, XjApplication.APP_KEY);
        }


        //  initAgent();
        xjpushManager = new XJPushManager(this);
        xjpushManager.initPushService();

        Log.d("loadDex", "onCreate end " + System.currentTimeMillis() + " oncreate  used : " + (System.currentTimeMillis() - oncreatestart) + "ms");
    }


//   public void initHxSDKHepler(){
//       hxSDKHelper.onInit(applicationContext);
//
//   }

    public void registerMPUSH() {
        if (shouldInit()) {
            MiPushClient.registerPush(this, APP_ID, APP_KEY);
        }
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();

    }

    private void initAgent() {
        UmsAgent.onError(this);
        //  UmsAgent.update(this);
    }


    public static XjApplication getInstance() {
        return instance;
    }

    /**
     * 获取内存中好友user list
     *
     * @return
     */
    public Map<String, User> getContactList() {
        return hxSDKHelper.getContactList();
    }

    /**
     * 设置好友user list到内存中
     *
     * @param contactList
     */
    public void setContactList(Map<String, User> contactList) {
        hxSDKHelper.setContactList(contactList);
    }


    /**
     * 获取当前登陆用户名
     *
     * @return
     */
    public String getUserName() {
        return hxSDKHelper.getHXId();
    }

    /**
     * 获取密码
     *
     * @return
     */
    public String getPassword() {
        return hxSDKHelper.getPassword();
    }

    /**
     * 设置用户名
     */
    public void setUserName(String username) {
        hxSDKHelper.setHXId(username);
    }

    /**
     * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
     * 内部的自动登录需要的密码，已经加密存储了
     *
     * @param pwd
     */
    public void setPassword(String pwd) {
        hxSDKHelper.setPassword(pwd);
    }

    /**
     * 退出登录,清空数据
     */
    public void logout(final EMCallBack emCallBack) {
        // 先调用sdk logout，在清理app中自己的数据
        hxSDKHelper.logout(emCallBack);

    }

    /**
     * configure log4j
     */
    public void configureLog4j() {
        // confiure log4j
//        final LogConfigurator logConfigurator = new LogConfigurator();
//        logConfigurator.setFileName(Environment.getExternalStorageDirectory() + File.separator + "myapp.log");
//        logConfigurator.setRootLevel(Level.DEBUG);
        // logConfigurator.setRootLevel(Level.OFF);
        // Set log level of a specific logger
        // logConfigurator.setLevel("org.apache", Level.ERROR);
//        logConfigurator.setLevel("org.apache", Level.OFF);
//        logConfigurator.configure();
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * init UIL
     * @param context
     */
  /*  public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }*/

    /**
     * if is login
     *
     * @return
     */

    public int getGrideWidth() {
        return grideWidth;
    }

    public void setGrideWidth(int grideWidth) {
        this.grideWidth = grideWidth;
    }

    public int getGrideHeight() {
        return grideHeight;
    }

    public void setGrideHeight(int grideHeight) {
        this.grideHeight = grideHeight;
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
