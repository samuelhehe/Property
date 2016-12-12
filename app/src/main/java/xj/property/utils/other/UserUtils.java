package xj.property.utils.other;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.igexin.sdk.PushManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import xj.property.Constant;
import xj.property.R;
import xj.property.XjApplication;
import xj.property.beans.AppLoginRequest;
import xj.property.beans.BlackListBean;
import xj.property.beans.BlackUserInfo;
import xj.property.beans.SelectUserRequest;
import xj.property.beans.StatusBean;
import xj.property.beans.TouristBean;
import xj.property.beans.TouristRequest;
import xj.property.beans.TouristResult;
import xj.property.beans.UserGroupV3Bean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.beans.UserLoginRequest;
import xj.property.db.DbOpenHelper;
import xj.property.domain.User;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.CommonUtils;
import xj.property.utils.XJPushManager;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.message.XJMessageHelper;

/**
 * Created by Administrator on 2015/4/9.
 */
public class UserUtils {



    //// 默认头像
    public static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .bitmapConfig(Bitmap.Config.RGB_565)
            .showImageOnLoading(R.drawable.head_portrait_personage)
            .showImageForEmptyUri(R.drawable.head_portrait_personage)
            .showImageOnFail(R.drawable.head_portrait_personage)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
//            .displayer(new RoundedBitmapDisplayer(20))
            .build();
    /// 帮主竞选默认头像图
    public static DisplayImageOptions bangzhu_election_options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.campaign_default_photos)
            .showImageForEmptyUri(R.drawable.campaign_default_photos)
            .showImageOnFail(R.drawable.campaign_default_photos)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();
    //// 帮主竞选我里边的默认头像图
    public static DisplayImageOptions bangzhu_election_me_options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.head_portrait_personage)
            .showImageForEmptyUri(R.drawable.head_portrait_personage)
            .showImageOnFail(R.drawable.head_portrait_personage)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();

    /// 会员卡里边默认头像图
    public static DisplayImageOptions msp_card_iv_options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.head_portrait_personage)
            .showImageForEmptyUri(R.drawable.head_portrait_personage)
            .showImageOnFail(R.drawable.head_portrait_personage)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();
    //// 活动默认背景图
    public static DisplayImageOptions activity_options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.default_picture)
            .showImageForEmptyUri(R.drawable.default_picture)
            .showImageOnFail(R.drawable.default_picture)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .considerExifParams(true)
            .build();
    //// 首页默认模块图
    public static DisplayImageOptions home_item_loading_options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.home_item_loading)
            .showImageForEmptyUri(R.drawable.home_item_loading)
            .showImageOnFail(R.drawable.home_item_loading)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .considerExifParams(true)
            .build();
    /// 上周人品排行榜顶部背景图片
    public static DisplayImageOptions lastweek_toplist_defimg_options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.default_picture)
            .showImageForEmptyUri(R.drawable.default_picture)
            .showImageOnFail(R.drawable.default_picture)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .considerExifParams(true)
            .build();





    interface getUserService {
        @GET("/api/v1/communities/{communityId}/users/{emobid}")
        void getUserInfo(@Path("communityId") long communityId, @Path("emobid") String emobid, Callback<SelectUserRequest> cb);

        @GET("/api/v3/communities/{communityId}/users/{emobid}")
        void getUserInfoV3(@Path("communityId") int communityId, @Path("emobid") String emobid, Callback<CommonRespBean<UserGroupV3Bean>> cb);
    }

    public static void callUser(final Context context, final String emobid, final Handler handler) {
        getUserService service = RetrofitFactory.getInstance().create(context,getUserService.class);
        Callback<CommonRespBean<UserGroupV3Bean>> callback = new Callback<CommonRespBean<UserGroupV3Bean>>() {
            @Override
            public void success(CommonRespBean<UserGroupV3Bean> bean, Response response) {
                if ("yes".equals(bean.getStatus())) {
//                Intent intent = new Intent(context, UserGroupInfoActivity.class);
//                intent.putExtra(Config.INTENT_PARMAS1, bean.info);
//                context.startActivity(intent);
                    Message message = Message.obtain();
                    message.what = Config.TASKCOMPLETE;
                    message.obj = bean.getData();
                    Log.d("SelectUserRequest", "SelectUserRequest getTest -->> " + bean.getData().getTest());
                    PreferencesUtil.setGroupUserInfoTest(context,emobid,bean.getData().getTest());
                    handler.sendMessage(message);

                } else {
                    Message message = Message.obtain();
                    message.what = Config.TASKCOMPLETE;
                    message.obj = "无法查看成员信息" + bean.getMessage();
                    handler.sendMessage(message);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Message message = Message.obtain();
                message.what = Config.TASKCOMPLETE;
                message.obj = "系统出错";
                handler.sendMessage(message);
                error.printStackTrace();
            }
        };
        service.getUserInfoV3(PreferencesUtil.getCommityId(context), emobid, callback);
    }


    interface getTouristService {
        ///api/v1/communities/1/users
        @POST("/api/v1/communities/{communities}/users")
        void getTouristInfo(@Header("signature") String signature, @Body TouristRequest request, @Path("communities") long communityId, Callback<TouristResult> callback);

        @POST("/api/v3/communities/{communities}/users")
        void getTouristInfoV3(@Body TouristRequest request, @Path("communities") long communityId, Callback<CommonRespBean<TouristBean>> callback);

    }


    /**
     * 注册分两种情况:
     * 用户第一次使用没有账号的情况下:
     * 1. 在本地没有emobid的情况下创建游客emobid,
     * 2. 绑定本地的游客的emobid进行注册
     *
     * 用户已经有账号的情况下注册:
     * 1.调用创建游客emobid.
     * 2.绑定创建的额游客emobid进行注册.
     * @param context
     * @param communityId
     * @param handler
     */
    public static void getTourist(final Context context, long communityId, final Handler handler) {
        TouristRequest request=new TouristRequest();
        request.equipmentVersion=getVersion(context);
        getTouristService service = RetrofitFactory.getInstance().create(context,request,getTouristService.class);
        Callback<CommonRespBean<TouristBean>> callback = new Callback<CommonRespBean<TouristBean>>() {
            @Override
            public void success(CommonRespBean<TouristBean> result, Response response) {
                if (result==null){
                    Log.i("onion","result is null ");
                    handler.sendEmptyMessage(12);
                    return;
                }
                //获取未登录游客id
                if ("yes".equals(result.getStatus())) {
                    Log.i("onion", "未登录游客Emoid" + result.getData().getEmobId()+"  未登录游客id:"+result.getData().getUserId());
                    Message message = Message.obtain();
                    message.what = Config.TouristGETEMOBID;
                    message.obj = result;
                    handler.sendMessage(message);
                } else {
                    handler.sendEmptyMessage(Config.TouristLoginError);
                }
            }
            @Override
            public void failure(RetrofitError error) {
                handler.sendEmptyMessage(Config.TouristLoginError);
                error.printStackTrace();
            }
        };
        service.getTouristInfoV3( request,communityId, callback);
    }

    public static void loginTourist(final Context context, final String touristName, final Handler handler) {
        Log.i("onion", "登录的环信游客名:" + touristName);
        EMChatManager.getInstance().login(touristName, StrUtils.string2md5("123"), new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.i("onion", "游客登录成功");
                PreferencesUtil.saveTourist(context, touristName, 0);
                PreferencesUtil.saveTourist(context, true);
                handler.sendEmptyMessage(Config.TouristLogin);
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(final int code, final String message) {
                Log.i("onion", "登录失败");
                handler.sendEmptyMessage(Config.TouristLoginError);
            }
        });
    }


    /**
     * 验证请在之前
     */
    public static void loginUser(Context context, String username, String password, Handler handler) {
        if (!CommonUtils.isNetWorkConnected(context)) {
            Toast.makeText(context, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            PreferencesUtil.clearBlackList(context);
            PreferencesUtil.setRefreshIndexCache(context, true);
            PreferencesUtil.setRefreshSame(context, true);
            getUserInfo(context, username, password, handler);
        }
    }

    /**
     * 验证请在之前
     */
    public static void moveUser(Context context, String username, String password, Handler handler) {
        if (!CommonUtils.isNetWorkConnected(context)) {
            Toast.makeText(context, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            PreferencesUtil.clearBlackList(context);
            moveUserInfo(context, username, password, handler);
        }
    }

    /**
     * @param context
     * @param username
     * @param password  这里的password 是没有md5加密
     * @param handler
     */
    private static void moveUserInfo(final Context context, final String username, final String password, final Handler handler) {

        final UserLoginRequest request = new UserLoginRequest();
        request.setPassword(password);
        request.setRole("owner");
        request.setEquipmentVersion(getVersion(context));

        if(Config.PHONETYPE.equals(android.os.Build.MANUFACTURER)){
            request.setEquipment("mi");
        }else {
            request.setEquipment("android");
        }
        XJUserService service = RetrofitFactory.getInstance().create(context,request,XJUserService.class);
        Callback<CommonRespBean<UserInfoDetailBean>> callback = new Callback<CommonRespBean<UserInfoDetailBean>>() {
            @Override
            public void success(final CommonRespBean<UserInfoDetailBean> bean, Response response) {
                Log.i("onion",bean+"");
                if (bean.getData() == null || bean.getStatus().equals("no")) {
                    handler.sendEmptyMessage(Config.LoginUserFailure);
                    Toast.makeText(context, "帮帮登录失败:用户名或密码错误", Toast.LENGTH_LONG).show();
                    return;
                }
                handler.sendEmptyMessage(Config.LoginUserSuccess);

                bean.getData().setPassword(request.getPassword());
//                PreferencesUtil.saveLatitude(context, "" + bean.getLatitude());
//                PreferencesUtil.saveLongitude(context, "" + bean.getLongitude());

//                Log.i("debbug","loginlat="+bean.getLatitude());
//                Log.i("debbug","loginlong="+bean.getLongitude());
                Log.i("onion", "登录成功" + bean.toString() + "密码" + request.getPassword());
                // 调用sdk登陆方法登陆聊天服务器
                Log.i("onion", "环信登录名" + bean.getData().getEmobId());
                if (!PreferencesUtil.getTourist(context).equals("")||false) {//先登出
                    XJPushManager xjPushManager = new XJPushManager(context);
                    xjPushManager.unregisterLoginedPushService();
                    xjPushManager.unInitPushService();
                    xjPushManager.initPushService();
                    xjPushManager.registerPushService(bean.getData().getEmobId());
//                    xjPushManager.registerLoginedPushService();
                    PreferencesUtil.saveTourist(context, "",0);
                    PreferencesUtil.saveTourist(context, false);
                    loginEMChat((Activity) context, username, bean.getData(), handler);//再登录
                    if (handler != null)
                        handler.sendEmptyMessage(Config.LogoutTourist);
                } else {
                    loginEMChat((Activity) context, username, bean.getData(), handler);//直接登录
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                handler.sendEmptyMessage(Config.LoginUserFailure);
            }
        };
        service.getUserInfo(request, PreferencesUtil.getCommityId(context),  callback);
    }


    /**
     * 登录app信息
     */
    interface AppLoginService {
        @POST("/api/v1/communities/{communityId}/applogin")
        void appLogin(@Header("signature") String signature, @Body AppLoginRequest request, @Path("communityId") long communityId, Callback<StatusBean> cb);
    }

    public static void appLogin(final Context context, final String equipment, final String phonenumnber) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        AppLoginService service = restAdapter.create(AppLoginService.class);
        final AppLoginRequest request = new AppLoginRequest();
        request.setEmobId(PreferencesUtil.getLoginInfo(context).getEmobId());
        request.setEquipment(equipment);
        request.setKey(phonenumnber);
        Callback<StatusBean> callback = new Callback<StatusBean>() {
            @Override
            public void success(StatusBean statusBean, Response response) {
                Log.i("onion", statusBean.getStatus() + "上传的设备id" + equipment);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("onion", "上传设备失败" + error.toString());
            }
        };

        service.appLogin(StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(request)), request, PreferencesUtil.getCommityId(context), callback);
    }


    /**
     * 获取XJuser部分
     */
    interface XJUserService {
        ///api/v1/communities/{communityId}/users/{username}

        //// /api/v3/communities/1/users/login
        @POST("/api/v3/communities/{communityId}/users/login")
        void getUserInfo(@Body UserLoginRequest request, @Path("communityId") long communityId, Callback<CommonRespBean<UserInfoDetailBean>> cb);
    }

    /**
     * @param context
     * @param username
     * @param password  这里的password 是没有md5加密
     * @param handler
     */
    private static void getUserInfo(final Context context, final String username, final String password, final Handler handler) {

        final UserLoginRequest request = new UserLoginRequest();
        request.setUsername(username);
        request.setPassword(StrUtils.string2md5(password));
        request.setRole("owner");
        request.setEquipmentVersion(getVersion(context));
        if(Config.PHONETYPE.equals(android.os.Build.MANUFACTURER)){
            request.setEquipment("mi");
        }else {
            request.setEquipment("android");
        }
        XJUserService service = RetrofitFactory.getInstance().create(context,request,XJUserService.class);
        Callback<CommonRespBean<UserInfoDetailBean>> callback = new Callback<CommonRespBean<UserInfoDetailBean>>() {
            @Override
            public void success(final CommonRespBean<UserInfoDetailBean> bean, Response response) {
                Log.i("onion",bean+"");
                if (bean.getData() == null || bean.getStatus().equals("no")) {
                    handler.sendEmptyMessage(Config.LoginUserFailure);
                    Toast.makeText(context, "帮帮登录失败:用户名或密码错误", Toast.LENGTH_LONG).show();
                    return;
                }
                handler.sendEmptyMessage(Config.LoginUserSuccess);
                bean.getData().setPassword(request.getPassword());

//                PreferencesUtil.saveLatitude(context, "" + bean.getLatitude());
//                PreferencesUtil.saveLongitude(context, "" + bean.getLongitude());

//                Log.i("debbug","loginlat="+bean.getLatitude());
//                Log.i("debbug","loginlong="+bean.getLongitude());

                Log.i("onion", "登录成功" + bean.getData().toString() + "密码 " + request.getPassword());
                // 调用sdk登陆方法登陆聊天服务器
                Log.i("onion", "环信登录名" + bean.getData().getEmobId());
                if (!PreferencesUtil.getTourist(context).equals("")||false) {//先登出
//                    MiPushClient.getAllAlias(context);

//                    MiPushClient.unsetAlias(context,PreferencesUtil.getTourist(context),null);
//                    MiPushClient.unregisterPush(context);
//                    XjApplication.getInstance().registerMPUSH();

                    XJPushManager xjPushManager = new XJPushManager(context);
                    xjPushManager.unInitPushService();
                    xjPushManager.initPushService();
                    xjPushManager.registerLoginedPushService();

                    PreferencesUtil.saveTourist(context, "",0);
                    PreferencesUtil.saveTourist(context, false);

                    loginEMChat((Activity) context, username, bean.getData(), handler);//再登录
                    if (handler != null)
                        handler.sendEmptyMessage(Config.LogoutTourist);
                } else {
                    loginEMChat((Activity) context, username, bean.getData(), handler);//直接登录
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                handler.sendEmptyMessage(Config.LoginUserFailure);
            }
        };
        service.getUserInfo(request, PreferencesUtil.getCommityId(context),callback);
    }


    /**
     *
     * 环信登陆
     *
     * @param context
     * @param username
     * @param bean
     * @param handler
     */
    public static void loginEMChat(final Activity context, final String username, final UserInfoDetailBean bean, final Handler handler) {
        EMChatManager.getInstance().login(bean.getEmobId(), StrUtils.string2md5("&bang#bang@" + bean.getPassword()), new EMCallBack() {

            @Override
            public void onSuccess() {
//                getUser(context,bean.getEmobId()); /// 这个过程是拉取人品值信息， 已合并，删除。 0224 v3

                // 登陆成功，保存用户名密码
                XjApplication.getInstance().setUserName(username);
                XjApplication.getInstance().setPassword(bean.getPassword());
                // 重新创建以当前手机号_demo.db 库
                DbOpenHelper.reSetInstance(context);

                Log.i("UserUtils ", "loginEMChat  is executed  ");

                XJPushManager xjPushManager = new XJPushManager(context);
                xjPushManager.setEmobid(bean.getEmobId());
                xjPushManager.registerPushService(bean.getEmobId());

                //保存人品值等等
               // saveUser(context,bean.getEmobId());
                if (handler != null)
                    handler.sendEmptyMessage(Config.LoginUserUPDATENATIVE);
                PreferencesUtil.saveTourist(context, false);
                PreferencesUtil.saveLogin(context, bean);
                PreferencesUtil.saveCommity(context, (int) bean.getCommunityId(), bean.getCommunityName());

//                UserUtils.appLogin(context,PushManager.getInstance().getClientid(context), username);

                XJMessageHelper.loadNativeMessage();

                new Thread(){
                    @Override
                    public void run() {
                        try {
                            // ** 第一次登录或者之前logout后，加载所有本地群和回话
                            // ** manually load all local groups and
                            // conversations in case we are auto login
                            // 获取群聊列表(群聊里只有groupid和groupname等简单信息，不包含members),sdk会把群组存入到内存和db中
                            EMGroupManager.getInstance().getGroupsFromServer();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                getUserBlacklist(context);
                handler.sendEmptyMessage(Config.LoginUserComplete);
                boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(PreferencesUtil.getLoginInfo(context).getNickname());
                if (!updatenick) {
                    EMLog.e("LoginActivity", "update current user nick fail");
                    int i=3;
                   while (i>0){
                       updatenick = EMChatManager.getInstance().updateCurrentUserNick(PreferencesUtil.getLoginInfo(context).getNickname());
                       if(updatenick)break;
                   }
                }

//                Intent back = new Intent();
//                back.putExtra(Config.INTENT_PARMAS1, bean);
//                context.setResult(-1, back);
//                context.finish();
              /*  //更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(XjApplication.currentUserNick);
                if (!updatenick) {
                    EMLog.e("LoginActivity", "update current user nick fail");
                }

                if (!context.isFinishing())
                    pd.dismiss();
                // 进入主页面
                context.    finish();*/
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(final int code, final String message) {
                try {
                    handler.sendEmptyMessage(Config.LoginUserFailure);
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "登录失败: " + message, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    Log.i("onion", e.toString());
                    Log.i("onion", "code" + code);
                    Log.i("onion", "message" + message);
                    e.printStackTrace();
                }

            }
        });
    }

    public static void setUserHearder(String username, User user) {
        String headerName = null;
        if (!TextUtils.isEmpty(user.getNick())) {
            headerName = user.getNick();
        } else {
            headerName = user.getUsername();
        }
        if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
            user.setHeader("");
        } else if (Character.isDigit(headerName.charAt(0))) {
            user.setHeader("#");
        } else {
            user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase());
            char header = user.getHeader().toLowerCase().charAt(0);
            if (header < 'a' || header > 'z') {
                user.setHeader("#");
            }
        }
    }


    /**
     * 验证请在之前
     */
    public static void reLoginUser(Context context, String username, String password, Handler handler) {
        if (!CommonUtils.isNetWorkConnected(context)) {
            Toast.makeText(context, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            getReUserInfo(context, username, password, handler);
            PreferencesUtil.clearBlackList(context);
        }
    }

    private static void getReUserInfo(final Context context, final String username, final String password, final Handler handler) {
        final UserLoginRequest request = new UserLoginRequest();
        request.setPassword(password);
        request.setRole("owner");
        request.setEquipmentVersion(getVersion(context));
        if(Config.PHONETYPE.equals(android.os.Build.MANUFACTURER)){
            request.setEquipment("mi");
        }else {
            request.setEquipment("android");
        }
        XJUserService service = RetrofitFactory.getInstance().create(context ,request,XJUserService.class);
        Callback<CommonRespBean<UserInfoDetailBean>> callback = new Callback<CommonRespBean<UserInfoDetailBean>>() {
            @Override
            public void success(final CommonRespBean<UserInfoDetailBean> bean, Response response) {
                if (bean.getData() == null || bean.getStatus().equals("no")) {
                    handler.sendEmptyMessage(Config.LoginUserFailure);
                    // Toast.makeText(context, "帮帮帮登录失败:用户名或密码错误", Toast.LENGTH_LONG).show();
                    return;
                }
                handler.sendEmptyMessage(Config.LoginUserSuccess);
                bean.getData().setPassword(request.getPassword());
                PreferencesUtil.saveTourist(context, "",0);
                PreferencesUtil.saveTourist(context, false);
                Log.i("onion", "登录成功" + bean.toString() + "密码" + request.getPassword());
//                PreferencesUtil.saveLatitude(context,""+bean.getLatitude());
//                PreferencesUtil.saveLongitude(context,""+bean.getLongitude());
                // 调用sdk登陆方法登陆聊天服务器
                Log.i("onion", "环信登录名" + bean.getData().getEmobId());
                if (!PreferencesUtil.getTourist(context).equals("")||false) {//先登出
                    if (handler != null)
                        handler.sendEmptyMessage(Config.LogoutTourist);
                    XjApplication.getInstance().logout(new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            loginEMChat((Activity) context, username, bean.getData(), handler);//再登录
                            PreferencesUtil.saveTourist(context, "",0);
                            PreferencesUtil.saveTourist(context, false);
                        }

                        @Override
                        public void onError(int i, String s) {
                            Log.i("onion", "登出失败" + s);
                        }

                        @Override
                        public void onProgress(int i, String s) {

                        }
                    });
                } else {
                    loginEMChat((Activity) context, username, bean.getData(), handler);//直接登录
                }
//                loginEMChat(username, bean, pd);//再登录
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                handler.sendEmptyMessage(Config.LoginUserFailure);
            }
        };
        service.getUserInfo(request, PreferencesUtil.getCommityId(context),  callback);






    }


    /**
     * 获取XJuser部分
     */
    interface XJUserBlackService {
        ///http://114.215.105.202:8080/api/v1/communities/{communityId}/blacklist/{emobId}
        @GET("/api/v1/communities/{communityId}/blacklist/{emobId}")
        void getUserBlacklist(@Path("communityId") long communityId, @Path("emobId") String emobId, Callback<BlackListBean> cb);
    }

    private  static void getUserBlacklist(final Context context) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE2).build();
        XJUserBlackService service = restAdapter.create(XJUserBlackService.class);
        Callback<BlackListBean> callback = new Callback<BlackListBean>() {
            @Override
            public void success(BlackListBean bean, Response response) {
                if ("yes".equals(bean.getStatus())){
                    ArrayList<String> blackItems=new ArrayList<>();
                    ArrayList<String> groupItems=new ArrayList<>();
                    for(int i=0;i<bean.getInfo().size();i++){
                        BlackUserInfo userInfo=  bean.getInfo().get(i);
                        if(userInfo!=null){
                            if(userInfo.getNickname()!=null&&!userInfo.getNickname().isEmpty()){//黑名单
                                blackItems.add(userInfo.getEmobIdTo());
                            }else {
                                groupItems.add(userInfo.getEmobIdTo());
                            }
                        }
                    }
                    PreferencesUtil.saveBlackList(context, blackItems);
//                    if(!groupItems.isEmpty()) {
                        PreferencesUtil.saveUnNotifyGroupS(context, groupItems);
                        EMChatManager.getInstance().getChatOptions().setReceiveNotNoifyGroup(groupItems);
//                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        };
        service.getUserBlacklist( PreferencesUtil.getCommityId(context), PreferencesUtil.getLoginInfo(context).getEmobId(), callback);
    }

    public static String getVersion(Context context){
        PackageManager packageManager = context.getPackageManager();
        String version="";
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
            Log.d("UserUtils: ","getVersion : "+ version);
        }catch (Exception e){
            e.printStackTrace();
        }

        return version;
    }


//    interface getUserService {
//        @GET("/api/v1/communities/{communityId}/users/{emobid}")
//        void getUserInfo(@Path("communityId") long communityId, @Path("emobid") String emobid, Callback<SelectUserRequest> cb);
//    }

    public static void getUser(final Context context, String emobid) {
//        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE2).build();
//        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
//        getUserService service = restAdapter.create(getUserService.class);
        getUserService service = RetrofitFactory.getInstance().create(context,getUserService.class);
        Callback<CommonRespBean<UserGroupV3Bean>> callback = new Callback<CommonRespBean<UserGroupV3Bean>>() {
            @Override
            public void success(CommonRespBean<UserGroupV3Bean> bean, Response response) {
                if ("yes".equals(bean.getStatus())) {
//                    PreferencesUtil.saveRp(context,bean.getInfo().getCharacterValues()+"",(int)bean.getInfo().getCharacterPercent()+"",bean.getInfo().getLifeCircleSum()+"");
                    PreferencesUtil.saveRPValue(XjApplication.getInstance(), "" + bean.getData().getCharacterValues());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        };
        service.getUserInfoV3(PreferencesUtil.getCommityId(context), emobid, callback);
    }
}