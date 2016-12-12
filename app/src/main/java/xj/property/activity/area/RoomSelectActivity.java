package xj.property.activity.area;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import xj.property.activity.user.UserHeaderIconActivity;
import xj.property.beans.FixUserInfoRequest;
import xj.property.beans.Floor;
import xj.property.beans.UserBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.beans.UserLoginRequest;
import xj.property.beans.XJUserInfoBean;
import xj.property.domain.User;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

public class RoomSelectActivity extends HXBaseActivity {
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

    private boolean progressShow;
    private ProgressDialog pd;

    List<Floor> floors = new ArrayList<>();
    private String emobId;
    private TextView tv_select_room_info;


    private EditText select_floor_et, select_unit_et,select_room_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_select);
        initTitle(null, "输入真实住址", "");
        initView();
        pd = new ProgressDialog(RoomSelectActivity.this);

//        mLdDialog.show();
//        HomeOwnerUtil.getHomeList(new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                mLdDialog.dismiss();
//                if (msg.what == Config.TASKERROR) {
//                    showToast("获取小区结构失败");
//                } else {
//                    floors = (List<Floor>) msg.obj;
//                    item_floors = HomeOwnerUtil.getFloor(floors);
//                    item_unit = HomeOwnerUtil.getuserUnit(floors.get(0));
//                    item_data_rooms = HomeOwnerUtil.getuserRoom(floors.get(0).getList().get(0));
//                   // initWheelCity(null, null, null);
//                }
//            }
//        });

    }


    private void initView() {
        tv_select_room_info = (TextView) findViewById(R.id.tv_select_room_info);
        tvRight.setOnClickListener(this);
        Log.i("debbug", "screen=" + getWindowManager().getDefaultDisplay().getHeight());

        select_floor_et = (EditText) findViewById(R.id.select_floor_et);
        select_unit_et = (EditText) findViewById(R.id.select_unit_et);
        select_room_et = (EditText) findViewById(R.id.select_room_et);

        btn_room_select = (Button) findViewById(R.id.btn_room_select);
        btn_room_select.setOnClickListener(this);

    }





    @Override
    public void onClick(final View v) {

        switch (v.getId()) {

            case R.id.btn_room_select:
            case R.id.tv_right_text:
                btn_room_select.setClickable(false);

//                storeLocation();
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

                final String item_floor = select_floor_et.getText().toString().trim();
                final String item_unit = select_unit_et.getText().toString().trim();
                final String item_room = select_room_et.getText().toString().trim();


                if (PreferencesUtil.getTouristLogin(RoomSelectActivity.this)) {
                    emobId = PreferencesUtil.getTourist(RoomSelectActivity.this);
                    userRegistByFix(item_floor,item_unit,item_room,emobId);
                    btn_room_select.setClickable(true);

                } else{

                    btn_room_select.setClickable(true);
                    userRegistByFix(item_floor,item_unit,item_room,"");

//                    UserUtils.getTourist(RoomSelectActivity.this, PreferencesUtil.getCommityId(RoomSelectActivity.this), new Handler() {
//                        @Override
//                        public void handleMessage(Message msg) {
//                            btn_room_select.setClickable(true);
//                            switch (msg.what) {
//                                case Config.TouristGETEMOBID:
//                                    // emobId=(String)msg.obj;
//                                    TouristResult result = (TouristResult) msg.obj;
//                                    emobId = result.getInfo().getEmobId();
//                                    Log.i("onion", "emobId: " + emobId);
//                                    userRegistByFix(item_floor,item_unit,item_room);
//                                    break;
//                            }
//                        }
//                    });
                }
                break;
        }
    }



    /**
     * check if user valid
     */
    private void userRegistByFix(String item_floors,String item_unit, String item_data_rooms, String emobId) {
        final UserBean userBean = new UserBean();
        userBean.setUsername(getIntent().getStringExtra(Config.LoginUserName));
        userBean.setNickname(getIntent().getStringExtra(Config.LoginUserNickName));
        userBean.setPassword(getIntent().getStringExtra(Config.LoginUserPwd));
        userBean.setUserFloor(item_floors);
        userBean.setUserUnit(item_unit);
        userBean.setRoom(item_data_rooms);
        // userBean.setClientId(PreferencesUtil.getCid(RoomSelectActivity.this));
        userBean.setMethod("PUT");

        UserInfoDetailBean userInfoDetailBean = new UserInfoDetailBean(userBean);
        userInfoDetailBean.setEmobId(emobId);
        userInfoDetailBean.setCommunityId(PreferencesUtil.getCommityId(RoomSelectActivity.this));

        Intent intent = new Intent (getmContext(), UserHeaderIconActivity.class);
        intent.putExtra("userInfoDetailBean",userInfoDetailBean );
        intent.putExtra("authcode",getIntent().getStringExtra("authcode"));
        startActivity(intent);
        finish();

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
                    Toast.makeText(RoomSelectActivity.this, "帮帮帮登录失败:用户名或密码错误", Toast.LENGTH_LONG).show();
                    return;
                }
                bean.getInfo().setPassword(request.getPassword());
                Log.i("onion", "登录成功" + bean.toString() + "密码" + request.getPassword());
                PreferencesUtil.saveLogin(RoomSelectActivity.this, bean.getInfo());

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
                PreferencesUtil.Logout(RoomSelectActivity.this);
                PreferencesUtil.saveLogin(RoomSelectActivity.this, bean.getInfo());
                Map<String, User> map = new HashMap<String, User>();
                XjApplication.getInstance().setContactList(map);
            }

            @Override
            public void failure(RetrofitError error) {
            }
        };
        isUserExistService.getuser(PreferencesUtil.getCommityId(RoomSelectActivity.this), emobId, callback);
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
                    PreferencesUtil.Logout(RoomSelectActivity.this);
                    PreferencesUtil.saveLogin(RoomSelectActivity.this, bean.getInfo());
                    UserInfoDetailBean userbean = PreferencesUtil.getLoginInfo(RoomSelectActivity.this);
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

        service.getUserInfo(StrUtils.string2md5(Config.BANGBANG_TAG+StrUtils.dataHeader(request)),request, PreferencesUtil.getCommityId(RoomSelectActivity.this), username, callback);
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