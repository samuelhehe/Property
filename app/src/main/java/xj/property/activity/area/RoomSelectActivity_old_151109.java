package xj.property.activity.area;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import xj.property.Constant;
import xj.property.R;
import xj.property.XjApplication;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.CommonPostResultBean;
import xj.property.beans.FixUserInfoRequest;
import xj.property.beans.Floor;
import xj.property.beans.TouristResult;
import xj.property.beans.UserBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.beans.UserLoginRequest;
import xj.property.beans.XJUserInfoBean;
import xj.property.domain.User;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.HomeOwnerUtil;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;
import xj.property.widget.wheelview.ArrayWheelAdapter;
import xj.property.widget.wheelview.OnWheelChangedListener;
import xj.property.widget.wheelview.WheelView;

public class RoomSelectActivity_old_151109 extends HXBaseActivity {
    /**
     * logger
     */

    /**
     * number picker for building ,unit ,room
     */
//    private NumberPicker np_room_0;
//    private NumberPicker np_room_1;
//    private NumberPicker np_room_2;
    /**
     * ok ,cancel button for a dialogue
     */
    private Button btn_room_select;

    private String[] item_floors;
    private String[] item_unit;
    private String[] item_data_rooms;
    private boolean progressShow;
    private ProgressDialog pd;
    List<Floor> floors = new ArrayList<>();
    private String emobId;
    private TextView tv_select_room_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_select);
        initTitle(null, "选择真实住址", "注册完成");
        initView();
        pd = new ProgressDialog(RoomSelectActivity_old_151109.this);
        mLdDialog.show();
        HomeOwnerUtil.getHomeList(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mLdDialog.dismiss();
                if (msg.what == Config.TASKERROR) {
                    showToast("获取小区结构失败");
                } else {
                    floors = (List<Floor>) msg.obj;
                    item_floors = HomeOwnerUtil.getFloor(floors);
                    item_unit = HomeOwnerUtil.getuserUnit(floors.get(0));
                    item_data_rooms = HomeOwnerUtil.getuserRoom(floors.get(0).getList().get(0));
                    initWheelCity(null, null, null);
                }
            }
        });

    }

    WheelView mWheelViewPro;
    WheelView mWheelViewCity;
    WheelView mWheelViewTwon;

    private void initWheelCity(String proindex,
                               String cityindex, String townindex) {
        mWheelViewPro.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
//                int proCurrentPosition = v.getCurrentItem();

                item_unit = HomeOwnerUtil.getuserUnit(floors.get(newValue));
                mWheelViewCity.setAdapter(new ArrayWheelAdapter<String>(
                        item_unit));
                mWheelViewCity.setCurrentItem(item_unit.length / 2);
                item_data_rooms = HomeOwnerUtil.getuserRoom(floors.get(newValue).getList().get(0));
                mWheelViewTwon.setAdapter(new ArrayWheelAdapter<String>(
                        item_data_rooms));
                mWheelViewTwon.setCurrentItem(item_data_rooms.length / 2);
            }
        });
        mWheelViewCity.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int proCurrentPosition = mWheelViewPro.getCurrentItem();
                item_data_rooms = HomeOwnerUtil.getuserRoom(floors.get(proCurrentPosition).getList().get(newValue));
                mWheelViewTwon.setAdapter(new ArrayWheelAdapter<String>(
                        item_data_rooms));
                mWheelViewTwon.setCurrentItem(item_data_rooms.length / 2);
            }
        });
//        mWheelViewCity.addScrollingListener(new OnWheelScrollListener() {
//
//            public void onScrollingStarted(WheelView wheel) {
//
//            }
//
//            public void onScrollingFinished(WheelView v) {
//                int proCurrentPosition=     mWheelViewPro.getCurrentItem();
//                int cityPosition=v.getCurrentItem();
//                item_data_rooms = HomeOwnerUtil.getuserRoom(floors.get(proCurrentPosition).getList().get(cityPosition));
//                mWheelViewTwon.setAdapter(new ArrayWheelAdapter<String>(
//                        item_data_rooms));
//                mWheelViewTwon.setCurrentItem(item_data_rooms.length/2);
//
//            }
//        });

        // province
        // 初始化的位置--------------------------------------------------------------
        int proPosition = 0;
        if (proindex != null) {
            proPosition = getArrayPosition(item_floors, proindex);
        }
        // city
        // 初始化的位置--------------------------------------------------------------
        int cityPosition = 0;
        if (cityindex != null) {
            cityPosition = getArrayPosition(item_unit, cityindex);
        }


        // town
        // 初始化的位置--------------------------------------------------------------
        int townPosition = 0;
        if (townindex != null) {
            townPosition = getArrayPosition(item_data_rooms, townindex);
        }

        ArrayWheelAdapter<String> proAdapter = new ArrayWheelAdapter<String>(
                item_floors);
//		proAdapter.setItemTextResource(Typeface.BOLD);
        mWheelViewPro.setSoundEffectsEnabled(true);
        mWheelViewPro.setCurrentItem(proPosition); // 初始化province的位置----------------------------
        mWheelViewPro.setAdapter(proAdapter);
        mWheelViewPro.setCyclic(false);
        ArrayWheelAdapter<String> cityAdapter = new ArrayWheelAdapter<String>(
                item_unit);
        mWheelViewCity.setAdapter(cityAdapter);

        mWheelViewCity.setCurrentItem(cityPosition); // 初始化city的位置-------------------------------------------

        ArrayWheelAdapter<String> twonAdapter = new ArrayWheelAdapter<String>(
                item_data_rooms);
        mWheelViewTwon.setAdapter(twonAdapter);

        mWheelViewTwon.setCurrentItem(townPosition); // 初始化town的位置------------------------------------------

       /* okBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String proName1 = proNameStr[mWheelViewPro.getCurrentItem()];
//                String cityIndexStr1 = cityIndexStr[mWheelViewCity
//                        .getCurrentItem()];
                String cityName1 = cityNameStr[mWheelViewCity.getCurrentItem()];
//                String twonIndexStr1 = twonIndexStr[mWheelViewTwon
//                        .getCurrentItem()];
                String twonName1 = twonNameStr[mWheelViewTwon.getCurrentItem()];
                tv_fix_address.setText(proName1 + "  " + cityName1 + "  " + twonName1);
                dismissPw();

            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dismissPw();
            }
        });*/
    }

    private int getArrayPosition(String[] data, String dataIndex) {

        for (int i = 0; i < data.length; i++) {

            if (data[i].equals(dataIndex)) {

                return i;
            }
        }

        return 0;
    }

    private void initView() {
        tv_select_room_info = (TextView) findViewById(R.id.tv_select_room_info);
        tvRight.setOnClickListener(this);
        Log.i("debbug", "screen=" + getWindowManager().getDefaultDisplay().getHeight());
        findViewById(R.id.ll_btns).setVisibility(View.GONE);
        mWheelViewPro = (WheelView) findViewById(R.id.provice);
        mWheelViewCity = (WheelView) findViewById(R.id.city);
        mWheelViewTwon = (WheelView) findViewById(R.id.area);
        //init number picker
//        np_room_0 = (NumberPicker) findViewById(R.id.np_0);
//        np_room_1 = (NumberPicker) findViewById(R.id.np_1);
//        np_room_2 = (NumberPicker) findViewById(R.id.np_2);
//        np_room_2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                item_unit=HomeOwnerUtil.getuserUnit(floors.get(newVal));
//                np_room_1.setDisplayedValues(item_unit);
//            }
//        });

        btn_room_select = (Button) findViewById(R.id.btn_room_select);

        btn_room_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setClickable(false);
                storeLocation();
                // userRegist();
//                pd.setCanceledOnTouchOutside(false);
//                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        progressShow = false;
//                    }
//                });
//                pd.setMessage("正在注册...");
//                if (pd != null && !RoomSelectActivity.this.isFinishing()) {
//                    pd.show();
//                }
                if (PreferencesUtil.getTouristLogin(RoomSelectActivity_old_151109.this)) {
                    emobId = PreferencesUtil.getTourist(RoomSelectActivity_old_151109.this);
                    userRegistByFix();
                    v.setClickable(true);
                } else
                    UserUtils.getTourist(RoomSelectActivity_old_151109.this, PreferencesUtil.getCommityId(RoomSelectActivity_old_151109.this), new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            switch (msg.what) {
                                case Config.TouristGETEMOBID:
                                    // emobId=(String)msg.obj;
                                    TouristResult result = (TouristResult) msg.obj;
                                    emobId = result.getInfo().getEmobId();
                                    Log.i("onion", "emobId: " + emobId);
                                    userRegistByFix();
                                    v.setClickable(true);
                                    break;
                            }

                        }
                    });


            }
        });


    }


    /**
     * service to judge if a user exist
     */
    interface FixUserInfoService {
        @PUT("/api/v1/communities/{communityId}/users/{emobId}")
        void userRegist(@Header("signature") String signature, @Body UserBean userBean, @Path("communityId") long communityId, @Path("emobId") String emobId, Callback<CommonPostResultBean> cb);
    }

    /**
     * check if user valid
     */
    private void userRegistByFix() {
        mLdDialog.show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Config.NET_BASE2)
                .build();
        FixUserInfoService isUserExistService = restAdapter.create(FixUserInfoService.class);
        final UserBean userBean = new UserBean();
        userBean.setUsername(getIntent().getStringExtra(Config.LoginUserName));
        userBean.setNickname(getIntent().getStringExtra(Config.LoginUserNickName));
        userBean.setPassword(StrUtils.string2md5(getIntent().getStringExtra(Config.LoginUserPwd)));
        userBean.setUserFloor(item_floors[mWheelViewPro.getCurrentItem()]);
        userBean.setUserUnit(item_unit[mWheelViewCity.getCurrentItem()]);
        userBean.setRoom(item_data_rooms[mWheelViewTwon.getCurrentItem()]);
       // userBean.setClientId(PreferencesUtil.getCid(RoomSelectActivity.this));
        userBean.setMethod("PUT");
        Callback<CommonPostResultBean> callback = new Callback<CommonPostResultBean>() {
            @Override
            public void success(CommonPostResultBean commonPostResultBean, Response response) {

                mLdDialog.dismiss();
                if ("yes".equals(commonPostResultBean.getStatus())) {
                    Log.i("onion","fix success");
                    final UserInfoDetailBean userInfoDetailBean = new UserInfoDetailBean(userBean);
                    userInfoDetailBean.setEmobId(emobId);
                    userInfoDetailBean.setCommunityId(PreferencesUtil.getCommityId(RoomSelectActivity_old_151109.this));
                    showToast("注册成功");
                    pd.show();
                    UserUtils.loginUser(RoomSelectActivity_old_151109.this, getIntent().getStringExtra(Config.LoginUserName), getIntent().getStringExtra(Config.LoginUserPwd), new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            Log.i("onion","handler running......");
                            switch (msg.what) {
                                case Config.LoginUserComplete:
//                                    PreferencesUtil.saveLogin(RoomSelectActivity.this, userInfoDetailBean);
                                    PreferencesUtil.saveFirstLogin(RoomSelectActivity_old_151109.this);
                                    boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(userBean.getNickname());
                                    if (!updatenick) {
                                        EMLog.e("LoginActivity", "update current user nick fail");
                                    }
                                    setResult(RESULT_OK);
                                    finish();
                                    if (progressShow) pd.dismiss();
                                    break;
                                case Config.LoginUserFailure:
                                    if (progressShow && !RoomSelectActivity_old_151109.this.isFinishing())
                                        pd.dismiss();
                                    break;
                                case Config.LoginUserSuccess:
                                    pd.setMessage("正在登录帮帮..");
                                    break;
                                case Config.LogoutTourist:
                                    pd.setMessage("正在登录帮帮..");
                                    break;
                                case Config.LoginUserUPDATENATIVE:
                                    pd.setMessage("正在加载群，联系人信息..");
                                    break;

                            }
                        }
                    });
                }else {
                    Log.i("onion","fix error");
                }
            }
            @Override
            public void failure(RetrofitError error) {
                Log.i("onion","RetrofitError error"+error.toString());
                showNetErrorToast();
                pd.dismiss();
            }
        };
        isUserExistService.userRegist(StrUtils.string2md5(Config.BANGBANG_TAG+StrUtils.dataHeader(userBean)),userBean, PreferencesUtil.getCommityId(RoomSelectActivity_old_151109.this), emobId, callback);
    }


    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.tv_right_text:
                v.setClickable(false);
                storeLocation();
                // userRegist();
//                pd.setCanceledOnTouchOutside(false);
//                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        progressShow = false;
//                    }
//                });
//                pd.setMessage("正在注册...");
//                if (pd != null && !RoomSelectActivity.this.isFinishing()) {
//                    pd.show();
//                }
                if (PreferencesUtil.getTouristLogin(RoomSelectActivity_old_151109.this)) {
                    emobId = PreferencesUtil.getTourist(RoomSelectActivity_old_151109.this);
                    userRegistByFix();
                    v.setClickable(true);
                } else
                    UserUtils.getTourist(RoomSelectActivity_old_151109.this, PreferencesUtil.getCommityId(RoomSelectActivity_old_151109.this), new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            switch (msg.what) {
                                case Config.TouristGETEMOBID:
                                    // emobId=(String)msg.obj;
                                    TouristResult result = (TouristResult) msg.obj;
                                    emobId = result.getInfo().getEmobId();
                                    Log.i("onion", "emobId: " + emobId);
                                    userRegistByFix();
                                    v.setClickable(true);
                                    break;
                            }

                        }
                    });


                break;
        }
    }

    /**
     * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
     *
     * @param username
     * @param user
     */
    protected void setUserHearder(String username, User user) {
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

    /*private void loginSuccess2Umeng(final long start) {
        runOnUiThread(new Runnable() {
            public void run() {
                long costTime = System.currentTimeMillis() - start;
                Map<String, String> params = new HashMap<String, String>();
                params.put("status", "success");
                MobclickAgent.onEventValue(RoomSelectActivity.this, "login1", params, (int) costTime);
                MobclickAgent.onEventDuration(RoomSelectActivity.this, "login1", (int) costTime);
            }
        });
    }
    private void loginFailure2Umeng(final long start, final int code, final String message) {
        runOnUiThread(new Runnable() {
            public void run() {
                long costTime = System.currentTimeMillis() - start;
                Map<String, String> params = new HashMap<String, String>();
                params.put("status", "failure");
                params.put("error_code", code + "");
                params.put("error_description", message);
                MobclickAgent.onEventValue(RoomSelectActivity.this, "login1", params, (int) costTime);
                MobclickAgent.onEventDuration(RoomSelectActivity.this, "login1", (int) costTime);

            }
        });
    }*/

//TODO Mark

    /**
     * 绑定手机号
     */
    interface XJUserService {
        ///api/v1/communities/{communityId}/users/{username}
        @PUT("/api/v1/communities/{communityId}/users/{emobId}")
        void getUserInfo(@Header("signature") String signature, @Body FixUserInfoRequest request, @Path("communityId") long communityId, @Path("emobId") String emobId, Callback<XJUserInfoBean> cb);
    }

    private void getUserInfo(final String username, final String password) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE2).build();
        XJUserService service = restAdapter.create(XJUserService.class);
        final FixUserInfoRequest request = new FixUserInfoRequest();
        request.password = password;
        request.setMethod("PUT");
        Callback<XJUserInfoBean> callback = new Callback<XJUserInfoBean>() {
            @Override
            public void success(final XJUserInfoBean bean, Response response) {
                if (bean.getStatus().equals("no")) {
                    pd.dismiss();
                    Toast.makeText(RoomSelectActivity_old_151109.this, "帮帮帮登录失败:用户名或密码错误", Toast.LENGTH_LONG).show();
                    return;
                }
                bean.getInfo().setPassword(request.getPassword());
                Log.i("onion", "登录成功" + bean.toString() + "密码" + request.getPassword());
                PreferencesUtil.saveLogin(RoomSelectActivity_old_151109.this, bean.getInfo());

                final long start = System.currentTimeMillis();
                // 调用sdk登陆方法登陆聊天服务器
                Log.i("onion", "环信登录名" + bean.getInfo().getEmobId());
//                EMChatManager.getInstance().login(bean.getInfo().getEmobId(),StrUtils.string2md5("&bang#bang@"+bean.getInfo().getPassword()), new EMCallBack() {
//
//                    @Override
//                    public void onSuccess() {
//                        //umeng自定义事件，开发者可以把这个删掉
//                       // loginSuccess2Umeng(start);
//                        PreferencesUtil.saveLogin(RoomSelectActivity.this,bean.getInfo());
//                        Intent back=new Intent();
//                        back.putExtra(Config.INTENT_PARMAS1,bean.getInfo());
//                        setResult(RESULT_OK, back);
//                        finish();
//                        if (!progressShow) {
//                            return;
//                        }
//                        // 登陆成功，保存用户名密码
//                        XjApplication.getInstance().setUserName(username);
//                        XjApplication.getInstance().setPassword(bean.getInfo().getPassword());
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                pd.setMessage("正在获取好友和群聊列表...");
//                            }
//                        });
//                        try {
//                            // ** 第一次登录或者之前logout后，加载所有本地群和回话
//                            // ** manually load all local groups and
//                            // conversations in case we are auto login
//                            EMGroupManager.getInstance().loadAllGroups();
//                            EMChatManager.getInstance().loadAllConversations();
//
//                            // demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定
//                            List<String> usernames = EMContactManager.getInstance().getContactUserNames();
//                            EMLog.d("roster", "contacts size: " + usernames.size());
//                            Map<String, User> userlist = new HashMap<String, User>();
//                            for (String username : usernames) {
//                                User user = new User();
//                                user.setUsername(username);
//                                setUserHearder(username, user);
//                                userlist.put(username, user);
//                            }
//                            // 添加user"申请与通知"
//                            User newFriends = new User();
//                            newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
//                            newFriends.setNick("申请与通知");
//                            newFriends.setHeader("");
//                            userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
//                            // 添加"群聊"
//                            User groupUser = new User();
//                            groupUser.setUsername(Constant.GROUP_USERNAME);
//                            groupUser.setNick("群聊");
//                            groupUser.setHeader("");
//                            userlist.put(Constant.GROUP_USERNAME, groupUser);
//
//                            // 存入内存
//                            XjApplication.getInstance().setContactList(userlist);
//                            // 存入db
//                            UserDao dao = new UserDao(RoomSelectActivity.this);
//                            List<User> users = new ArrayList<User>(userlist.values());
//                            dao.saveContactList(users);
//
//                            // 获取群聊列表(群聊里只有groupid和groupname等简单信息，不包含members),sdk会把群组存入到内存和db中
//                            EMGroupManager.getInstance().getGroupsFromServer();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        //更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
//                        boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(XjApplication.currentUserNick);
//                        if (!updatenick) {
//                            EMLog.e("RoomSelectActivity", "update current user nick fail");
//                        }
//
//                        if (!RoomSelectActivity.this.isFinishing())
//                            pd.dismiss();
//                        // 进入主页面
//                        startActivity(new Intent(RoomSelectActivity.this, MainActivity.class));
//                        finish();
//                    }
//
//                    @Override
//                    public void onProgress(int progress, String status) {
//
//                    }
//
//                    @Override
//                    public void onError(final int code, final String message) {
//                      //  loginFailure2Umeng(start,code,message);
//
//                        if (!progressShow) {
//                            return;
//                        }
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                if (!RoomSelectActivity.this.isFinishing())
//                                    pd.dismiss();
//                                Toast.makeText(getApplicationContext(), "环信登录失败: " + message, Toast.LENGTH_SHORT).show();
//
//                            }
//                        });
//                    }
//                });
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                showNetErrorToast();
            }
        };

        //18734900045
        //123456
        service.getUserInfo(StrUtils.string2md5(Config.BANGBANG_TAG+StrUtils.dataHeader(request)),request, PreferencesUtil.getCommityId(this), username, callback);
    }


    //获取楼信息

 /*   interface FixUserPasswordService {
        ///api/v1/communities/{communityId}/homeOwner
        @GET("/api/v1/communities/{communityId}/homeOwner")
        void fixUserPassword(@Path("communityId") long communityId, Callback<FloorResult> cb);
    }

    private void fixUserPassword() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Config.NET_BASE2)
                .build();
        FixUserPasswordService isUserExistService = restAdapter.create(FixUserPasswordService.class);
        final UserBean userBean = new UserBean();
        userBean.setPassword(StrUtils.string2md5(getIntent().getStringExtra(Config.LoginUserPwd)));
        Callback<FloorResult> callback = new Callback<FloorResult>() {
            @Override
            public void success(FloorResult floorResult, Response response) {
            }

            @Override
            public void failure(RetrofitError error) {
                logger.info("retrofit error is :" + error);
            }
        };
        isUserExistService.fixUserPassword(2, callback);
    }
*/

    //重新获取用户信息

    interface GetUserService {
        @GET("/api/v1/communities/{communityId}/users/{emobId}")
        void getuser(@Path("communityId") long communityId, @Path("emobId") String emobId, Callback<XJUserInfoBean> cb);
    }

    private void getuser(String emobId) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Config.NET_BASE2)
                .build();
        GetUserService isUserExistService = restAdapter.create(GetUserService.class);
        Callback<XJUserInfoBean> callback = new Callback<XJUserInfoBean>() {
            @Override
            public void success(XJUserInfoBean bean, Response response) {
                PreferencesUtil.Logout(RoomSelectActivity_old_151109.this);
                PreferencesUtil.saveLogin(RoomSelectActivity_old_151109.this, bean.getInfo());
                Map<String, User> map = new HashMap<String, User>();
                XjApplication.getInstance().setContactList(map);
            }

            @Override
            public void failure(RetrofitError error) {
            }
        };
        isUserExistService.getuser(PreferencesUtil.getCommityId(RoomSelectActivity_old_151109.this), emobId, callback);
    }

    /**
     * 获取XJuser部分
     */
    interface UserService {
        ///api/v1/communities/{communityId}/users/{username}
        @POST("/api/v1/communities/{communityId}/users/{username}")
        void getUserInfo(@Header("signature") String signature, @Body UserLoginRequest request, @Path("communityId") long communityId, @Path("username") String username, Callback<XJUserInfoBean> cb);
    }

    private void getUserInfo2(final String username, final String password) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        UserService service = restAdapter.create(UserService.class);
        final UserLoginRequest request = new UserLoginRequest();
        request.setPassword(StrUtils.string2md5(password));
        request.setRole("owner");
        Callback<XJUserInfoBean> callback = new Callback<XJUserInfoBean>() {
            @Override
            public void success(final XJUserInfoBean bean, Response response) {
                System.out.println("bean.getInfo():" + bean.getInfo());
                if (bean.getInfo() == null || bean.getStatus().equals("no")) {
                    return;
                } else {
                    PreferencesUtil.Logout(RoomSelectActivity_old_151109.this);
                    PreferencesUtil.saveLogin(RoomSelectActivity_old_151109.this, bean.getInfo());
                    UserInfoDetailBean userbean = PreferencesUtil.getLoginInfo(RoomSelectActivity_old_151109.this);
                    System.out.println("old bean" + userbean);
                    Map<String, User> map = new HashMap<String, User>();
                    XjApplication.getInstance().setContactList(map);
                }

            }

            @Override
            public void failure(RetrofitError error) {
                showNetErrorToast();
            }
        };
        service.getUserInfo(StrUtils.string2md5(Config.BANGBANG_TAG+StrUtils.dataHeader(request)),request, PreferencesUtil.getCommityId(RoomSelectActivity_old_151109.this), username, callback);
    }


    private void storeLocation() {
        SharedPreferences sharedPreferences = getSharedPreferences("xj", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("userFloor", userFloor);
//        editor.putString("userUnit ", userUnit);
//        editor.putString("room ", room);
        editor.commit();//提交修改
    }
}