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
package xj.property.activity.user;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.util.HanziToPinyin;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import xj.property.Constant;
import xj.property.R;
import xj.property.XjApplication;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.UserLoginRequest;
import xj.property.beans.XJUserInfoBean;
import xj.property.domain.User;
import xj.property.utils.CommonUtils;
import xj.property.utils.ToastUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

/**
 * 登陆页面
 */
public class LoginActivity extends HXBaseActivity {
    public static final int REQUEST_CODE_SETNICK = 1;
    private EditText usernameEditText;
    private EditText passwordEditText;

    private boolean progressShow;
    private boolean autoLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 如果用户名密码都有，直接进入主页面
//		if (HXSDKHelper.getInstance().isLogined()) {
//			autoLogin = true;
//			startActivity(new Intent(LoginActivity.this, MainActivity.class));
//
//			return;
//		}
        setContentView(R.layout.activity_login);
        ImageView iv = (ImageView) findViewById(R.id.iv_login_pic);
       // ImageLoader.getInstance().displayImage("drawable://" + R.drawable.login_bg, iv, new ImgLoadListener());
        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);

        // 如果用户名改变，清空密码
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordEditText.setText(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /**
     * 登陆
     *
     * @param view
     */
    public void login(View view) {
        if (!CommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }
//        XjApplication.currentUserNick = data.getStringExtra("edittext");

        final String username = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            progressShow = true;
            final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
            pd.setCanceledOnTouchOutside(false);
            pd.setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    progressShow = false;
                }
            });
            pd.setMessage("正在登陆...");
            pd.show();

            if(!CommonUtils.isNetWorkConnected(this)){
                pd.dismiss();
                showNetErrorToast();
            }
            UserUtils.loginUser(this,username,password,null);

//            getUserInfo(username, password, pd);
//		Intent intent = new Intent(LoginActivity.this, AlertDialog.class);
//		intent.putExtra("editTextShow", true);
//		intent.putExtra("titleIsCancel", true);
//		intent.putExtra("msg", "请设置当前用户的昵称\n为了ios离线推送不是userid而是nick，详情见注释");
//		startActivityForResult(intent, REQUEST_CODE_SETNICK);

        }else{
            ToastUtils.showToast(getmContext(),"请输入登陆信息");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SETNICK) {
                XjApplication.currentUserNick = data.getStringExtra("edittext");

                final String username = usernameEditText.getText().toString();
                final String password = passwordEditText.getText().toString();

                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    progressShow = true;
                    final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                    pd.setCanceledOnTouchOutside(false);
                    pd.setOnCancelListener(new OnCancelListener() {

                        @Override
                        public void onCancel(DialogInterface dialog) {
                            progressShow = false;
                        }
                    });
                    pd.setMessage("正在登陆...");
                    pd.show();

                    getUserInfo(username, password, pd);


                }

            }

        }
    }

    /**
     * 注册
     *
     * @param view
     */
    public void btn_register(View view) {
        startActivityForResult(new Intent(this, UsernameActivity.class), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (autoLogin) {
            return;
        }

        if (XjApplication.getInstance().getUserName() != null) {
            usernameEditText.setText(XjApplication.getInstance().getUserName());
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

    private void loginSuccess2Umeng(final long start) {
        runOnUiThread(new Runnable() {
            public void run() {
                long costTime = System.currentTimeMillis() - start;
                Map<String, String> params = new HashMap<String, String>();
                params.put("status", "success");
//				MobclickAgent.onEventValue(LoginActivity.this, "login1", params, (int) costTime);
//				MobclickAgent.onEventDuration(LoginActivity.this, "login1", (int) costTime);
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
//				MobclickAgent.onEventValue(LoginActivity.this, "login1", params, (int) costTime);
//				MobclickAgent.onEventDuration(LoginActivity.this, "login1", (int) costTime);

            }
        });
    }


    /**
     * 获取XJuser部分
     */
    interface XJUserService {
        ///api/v1/communities/{communityId}/users/{username}
        @POST("/api/v1/communities/{communityId}/users/{username}")
        void getUserInfo(@Header("signature") String signature, @Body UserLoginRequest request, @Path("communityId") int communityId, @Path("username") String username, Callback<XJUserInfoBean> cb);
    }

    private void getUserInfo(final String username, final String password, final ProgressDialog pd) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        XJUserService service = restAdapter.create(XJUserService.class);
        final UserLoginRequest request = new UserLoginRequest();
        request.setPassword(StrUtils.string2md5(password));
        request.setRole("owner");
        Callback<XJUserInfoBean> callback = new Callback<XJUserInfoBean>() {
            @Override
            public void success(final XJUserInfoBean bean, retrofit.client.Response response) {
                System.out.println("bean.getInfo():" + bean.getInfo());
                if (bean.getInfo() == null || bean.getStatus().equals("no")) {
                    pd.dismiss();
                    Toast.makeText(LoginActivity.this, "帮帮登录失败:用户名或密码错误", Toast.LENGTH_LONG).show();
                    return;
                }
                bean.getInfo().setPassword(request.getPassword());
                Log.i("onion", "登录成功" + bean.toString() + "密码" + request.getPassword());
                // 调用sdk登陆方法登陆聊天服务器
                Log.i("onion", "环信登录名" + bean.getInfo().getEmobId());
                if (PreferencesUtil.getTourist(LoginActivity.this) != null) {//先登出
                    XjApplication.getInstance().logout(new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            Log.i("onion", "游客登出成功");
                            loginEMChat(username, bean, pd);//再登录
                        }

                        @Override
                        public void onError(int i, String s) {
Log.i("onion","登出失败"+s);
                        }

                        @Override
                        public void onProgress(int i, String s) {

                        }
                    });
                }
//                loginEMChat(username, bean, pd);//再登录
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(LoginActivity.this,getResources().getString(R.string.netError), Toast.LENGTH_LONG).show();
            }
        };

        //18734900045
        //123456
        service.getUserInfo(StrUtils.string2md5(Config.BANGBANG_TAG+StrUtils.dataHeader(request)),request, 1, username, callback);
    }

    private void loginEMChat(final String username, final XJUserInfoBean bean, final ProgressDialog pd) {
        EMChatManager.getInstance().login(bean.getInfo().getEmobId(), StrUtils.string2md5("&bang#bang@" + bean.getInfo().getPassword()), new EMCallBack() {

            @Override
            public void onSuccess() {
                //umeng自定义事件，开发者可以把这个删掉
                PreferencesUtil.saveLogin(LoginActivity.this, bean.getInfo());
                Intent back = new Intent();
                back.putExtra(Config.INTENT_PARMAS1, bean.getInfo());
                setResult(RESULT_OK, back);
                finish();
                if (!progressShow) {
                    return;
                }
                // 登陆成功，保存用户名密码
                XjApplication.getInstance().setUserName(username);
                XjApplication.getInstance().setPassword(bean.getInfo().getPassword());
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.setMessage("正在获取好友和群聊列表...");
                    }
                });

                /*try {
                    // ** 第一次登录或者之前logout后，加载所有本地群和回话
                    // ** manually load all local groups and
                    // conversations in case we are auto login
                    EMGroupManager.getInstance().loadAllGroups();
                    EMChatManager.getInstance().loadAllConversations();
                    Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
                    Map<String, User> userlist = new HashMap<String, User>();
                    // 过滤掉messages seize为0的conversation
                    for (EMConversation conversation : conversations.values()) {
                        //用来过滤掉username 是 admin 的用户发来的消息
//			if ((conversation.getAllMessages().size() != 0)&&(!conversation.getUserName().equals("admin")))
                        EMMessage message = conversation.getLastMessage();
                        String nike = message.getStringAttribute(Config.EXPKey_nickname, null);
                        if (nike != null && message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) != 0) {
                            List<EMMessage> messages = conversation.getAllMessages();
                            for (int i = 0; i < messages.size(); i++) {
                                if(messages.get(i).direct== EMMessage.Direct.RECEIVE)
                                XJMessageHelper.operatNewMessage(messages.get(i));
                            }
                            continue;
                        }
                        User user = new User();
                        user.setEid(message.getFrom());
                        user.setUsername(message.getFrom());
                        user.sort = message.getStringAttribute(Config.EXPKey_SORT, "-1");
                        user.avatar = message.getStringAttribute(Config.EXPKey_avatar, "");
                        user.setNick(nike);
                        setUserHearder(message.getFrom(), user);
                        userlist.put(message.getFrom(), user);
                    }
                    // demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定
//                   List<String> usernames = EMContactManager.getInstance().getContactUserNames();
//
//                   EMLog.d("roster", "contacts size: " + usernames.size());
//                   for (String username : usernames) {
//                       User user = new User();
//                       user.setUsername(username);
//                 EMMessage message=      emChatManager.getConversation(username).getLastMessage();
//                 String nike=   message   .getStringAttribute(Config.EXPKey_nickname,"帮帮用户");
//                      if(nike==null){
//                       List<EMMessage> messages=      emChatManager.getConversation(username).getAllMessages();
//                        for(int i=0;i<messages.size();i++){
//                          XJMessageHelper.operatNewMessage(messages.get(i));
//                        }
//                          continue;
//                      }
//                       user.sort=message.getStringAttribute(Config.EXPKey_SORT,"-1");
//                       user.avatar=message.getStringAttribute(Config.EXPKey_avatar,"");
//                       user.setNick(nike);
//                       setUserHearder(username, user);
//                       userlist.put(username, user);
//                   }
                    // 添加user"申请与通知"
                    User newFriends = new User();
                    newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
                    newFriends.setNick("申请与通知");
                    newFriends.setHeader("");
                    userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
                    // 添加"群聊"
                    User groupUser = new User();
                    groupUser.setUsername(Constant.GROUP_USERNAME);
                    groupUser.setNick("群聊");
                    groupUser.setHeader("");
                    userlist.put(Constant.GROUP_USERNAME, groupUser);

                    // 存入内存
                    XjApplication.getInstance().setContactList(userlist);
                    // 存入db
                    UserDao dao = new UserDao(LoginActivity.this);
                    List<User> users = new ArrayList<User>(userlist.values());
                    dao.saveContactList(users);

                    // 获取群聊列表(群聊里只有groupid和groupname等简单信息，不包含members),sdk会把群组存入到内存和db中
                    EMGroupManager.getInstance().getGroupsFromServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
//                //更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
//                boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(XjApplication.currentUserNick);
//                if (!updatenick) {
//                    EMLog.e("LoginActivity", "update current user nick fail");
//                }

                if (!LoginActivity.this.isFinishing())
                    pd.dismiss();
                // 进入主页面
                finish();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(final int code, final String message) {

                if (!progressShow) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "登录失败: " + message, Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
}
