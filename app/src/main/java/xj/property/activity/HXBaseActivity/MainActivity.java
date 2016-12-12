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
package xj.property.activity.HXBaseActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatDB;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMNotifier;
import com.easemob.chat.GroupChangeListener;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.igexin.sdk.PushManager;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.Constant;
import xj.property.HXSDKHelper;
import xj.property.R;
import xj.property.XjApplication;
import xj.property.activity.tags.MyCommunityCategoryDialog;
import xj.property.beans.AppLoginInfoBean;
import xj.property.beans.CommunityCategoryDataRespBean;
import xj.property.beans.GroupMsgInfoBean;
import xj.property.beans.UpDateApp;
import xj.property.beans.UserInfoDetailBean;
import xj.property.beans.XJUserInfoBean;
import xj.property.cache.GroupHeader;
import xj.property.cache.XJNotify;
import xj.property.db.InviteMessgeDao;
import xj.property.domain.InviteMessage;
import xj.property.domain.InviteMessage.InviteMesageStatus;
import xj.property.domain.User;
import xj.property.event.NewNotifyEvent;
import xj.property.event.NewRefreshIndexMenuEvent;
import xj.property.fragment.ChatAllHistoryFragment;
import xj.property.fragment.IndexFragment;
import xj.property.fragment.MeFragment;
import xj.property.fragment.NewSurroundingFrg;
import xj.property.fragment.SettingsFragment;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseUtils;
import xj.property.netbase.RetrofitFactory;
import xj.property.provider.ChatAllHistoryFProvider;
import xj.property.service.PushPullReqService;
import xj.property.statistic.StatisticAsyncService;
import xj.property.ums.UpdateManager;
import xj.property.utils.CommonUtils;
import xj.property.utils.XJPushManager;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.message.XJContactHelper;
import xj.property.utils.message.XJMessageHelper;
import xj.property.utils.other.Config;
import xj.property.utils.other.GroupUtils;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

public class MainActivity extends HXBaseActivity implements EMEventListener {
    /**
     * logger
     */

    protected static final String TAG = "MainActivity";
    /// 对话框的显示时间
    private static final long DIALOG_SHOW_TIME = 8 * 1000 + 1250;
    /**
     * unread label
     */
    private TextView unreadLabel;
    private TextView tv_newbangbi;


    public static String startTime;
    public static String endTime;
    /**
     * unread address label
     */
    private TextView unreadAddressLabel;
    /**
     * button for tabs
     */
    private Button[] mTabs;
    /**
     * contactListFragment
     */
//    private ContactlistFragment contactListFragment;
    /**
     * chat history fragment
     */
    public ChatAllHistoryFragment chatHistoryFragment;
    /**
     * setting fragment
     */
    private SettingsFragment settingFragment;
    /**
     * index fragment
     */
    private IndexFragment indexFragment;

//    private SurroundingsFragment surroundingsFragment;
//    private PeripheryFragment peripheryFragment;

    private NewSurroundingFrg newSurroundingFrg;

    /**
     * me fragment
     */
    private MeFragment meFragment;// 我的
    /**
     * array for fragment
     */
    private Fragment[] fragments;
    // private List<Fragment> fragments;

    public static int index;

    private RelativeLayout[] tab_containers;

    private TextView tv_notifycount;

    /**
     * tab index
     */
    private int currentTabIndex;

    //private NewMessageBroadcastReceiver msgReceiver;
    /**
     * login other where
     */
    public boolean isConflict = false;
    private boolean isNetwork = false;
    private boolean isVersionUpdate = false;

    public boolean isX5WebViewEnabled = false;




    /**
     * view pager
     */
    private ViewPager viewPager;

    private String username;
    private String password;

    private String groupId;

    private ChatAllHistoryFProvider mChatAllHistoryFProvider;
    private UserInfoDetailBean userInfoBean;

    /**
     * viewpager adapter
     */
    class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragments[arg0];
        }
    }


    public void onEvent(NewRefreshIndexMenuEvent event) {
        if (event != null) {
            Log.d(TAG + " onEvent", "NewRefreshIndexMenuEvent is not null  ");
            int refreshCode = event.refreshCode;
            if (handler != null) {
//                if(indexFragment!=null){
//                    indexFragment.getLifeCircleCount(handler);
//                    Log.d(TAG+" onEvent","NewRefreshIndexMenuEvent  indexFragment getLifeCircleCount ");
//
//                    indexFragment.refreshIndexMenu();
//                    Log.d(TAG+" onEvent","NewRefreshIndexMenuEvent  indexFragment refreshIndexMenu ");
//
//                }else{
//                    Log.d(TAG+" onEvent","NewRefreshIndexMenuEvent  indexFragment is null ");
//                }
            } else {
                Log.d(TAG + " onEvent", "NewRefreshIndexMenuEvent handler  is null ");
            }
        } else {
            Log.d(TAG + " onEvent", "NewRefreshIndexMenuEvent is null ");
        }
    }

    /**
     * 监听事件
     */
    @Override
    public void onEvent(EMNotifierEvent event) {
        switch (event.getEvent()) {
            case EventNewMessage: // 普通消息
            {
                EMMessage message = (EMMessage) event.getData();
                String from = message.getFrom();
                Log.i("onion", "mainActivity收到新消息" + message.getFrom());
                // 消息id
//                if (offlineUser.contains(from)) {
//                    XJMessageHelper.loadOffLineMessage(EMChatManager.getInstance().getConversation(from));
//                    offlineUser.remove(from);
//                    Log.i("onion", "离线消息");
//                } else
                if (XJMessageHelper.getOrderModel(message.getStringAttribute(Config.EXPKey_serial, ""), 201) != null ||
                        XJMessageHelper.getOrderModel(message.getStringAttribute("welfareId", ""), 601) != null ||
                        XJMessageHelper.getOrderModel(message.getStringAttribute("welfareId", ""), 602) != null) {
                    Log.i("debbug", "已经有message了");
                    EMConversation conversation = EMChatManager.getInstance().getConversation(message.getFrom());
                    conversation.removeMessage(message.getMsgId());
                    EMChatDB.getInstance().deleteMessage(message.getMsgId());
                    return;
                }
                if (!XJMessageHelper.operatNewMessage(getmContext(),message)) {
                    Log.i("onion", "Main处理消息" + message + message.isUnread());
                    XJContactHelper.saveContact(message);
                } else {
                    Log.i("onion", "是个通知");
                    refreshUI();
                    refreshNewBangBiUI();
                    updateUnreadLabel();
                    return;
                }
                if (message.getChatType() == ChatType.GroupChat) {
                    GroupHeader header = new Select().from(GroupHeader.class).where("group_id = ?", message.getTo()).executeSingle();
                    if (header == null || header.getNum() < 10)
                        GroupUtils.getGroupInfo(message.getTo());
                }

                // 2014-10-22 修复在某些机器上，在聊天页面对方发消息过来时不立即显示内容的bug
                if (ChatActivity.activityInstance != null) {
                    if (message.getChatType() == ChatType.GroupChat) {
                        if (message.getTo().equals(
                                ChatActivity.activityInstance.getToChatUsername()))
                            return;
                    } else {
                        if (from.equals(ChatActivity.activityInstance.getToChatUsername()))
                            return;
                    }
                }
                //过滤掉通知公告
                //todo 自定义显示位置在首页顶部
                // 刷新bottom bar消息未读数
                // 提示新消息
                if (message.getChatType() == ChatType.Chat || !PreferencesUtil.getUnNotifyGroupS(XjApplication.getInstance()).contains(message.getTo()))
                    HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
                //TODO 修改refreshUI刷新图表
                groupId = message.getTo();
                refreshUI(groupId);
                break;
            }

            case EventOfflineMessage: {
                List<EMMessage> offlineMessages = (List<EMMessage>) event.getData();
                Log.i("onion", "offlineMEssage" + offlineMessages.toString());
                XJMessageHelper.loadOffLineMessages(getmContext(),offlineMessages);
                refreshUI();
                break;
            }
            case EventDeliveryAck: {
                EMMessage message = (EMMessage) event.getData();
                Log.i("onion", "送达监听:" + message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) + message.getStringAttribute(Config.EXPKey_serial, "") + message.getBody());
//    if(message.getIntAttribute(Config.EXPKey_CMD_CODE,0)!=0&&!message.getStringAttribute(Config.EXPKey_serial,"").isEmpty()){
//        XJMessageHelper.saveMessage2DB(message.getMsgId(),message.getStringAttribute(Config.EXPKey_serial,""),message.getIntAttribute(Config.EXPKey_CMD_CODE,0));
//    }
                break;
            }
            case EventConversationListChanged: {
                refreshUI();
                break;
            }

            default:
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        EMChatManager.getInstance().unregisterEventListener(this);
        HXSDKHelper sdkHelper = (HXSDKHelper) HXSDKHelper.getInstance();
        sdkHelper.popActivity(this);

//        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private int selectNewNotifyCount() {
        return new Select().from(XJNotify.class).where("emobid = ? and read_status = ?", PreferencesUtil.getLogin(this) ? PreferencesUtil.getLoginInfo(this).getEmobId() : "-1", "no").execute().size();
    }

    //获取最新通知
    private XJNotify selectNewNotify() {
        return new Select().from(XJNotify.class).orderBy("timestamp DESC").executeSingle();
    }

    // This method will be called when a MessageEvent is posted
    public void onEvent(NewNotifyEvent event) {
        initNotifyCount();
    }

    private void initNotifyCount() {
        int count = selectNewNotifyCount();
        if (count != 0)//
        {
            tv_notifycount.setVisibility(View.VISIBLE);
            tv_notifycount.setText("" + count);
        } else {
            tv_notifycount.setVisibility(View.GONE);
        }
    }

    /**
     * onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            finish();
            // startActivity(new Intent(this, LoginActivity.class));
            startActivity(new Intent(this, LoginDialogActivity.class));
            return;
        }
        setContentView(R.layout.activity_main);
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myNetReceiver, mFilter);
        initView();
//        MobclickAgent.setDebugMode(true);
//        MobclickAgent.updateOnlineConfig(this);
//        EMChatManager.getInstance().getChatOptions().setShowNotificationInBackgroud(false);
        if (getIntent().getBooleanExtra("conflict", false) && !isConflictDialogShow)
            showConflictDialog();

        inviteMessgeDao = new InviteMessgeDao(this);
        //  userDao = new UserDao(this);

        // 这个fragment只显示好友和群组的聊天记录
        // chatHistoryFragment = new ChatHistoryFragment();
        // 显示所有人消息记录的fragment
        chatHistoryFragment = new ChatAllHistoryFragment();
//        contactListFragment = new ContactlistFragment();
        settingFragment = new SettingsFragment();
        indexFragment = new IndexFragment();
        mChatAllHistoryFProvider = new ChatAllHistoryFProvider(getApplicationContext());
//        surroundingsFragment = new SurroundingsFragment();
//        peripheryFragment = new PeripheryFragment();
        newSurroundingFrg = new NewSurroundingFrg();

        meFragment = new MeFragment();

//        fragments = new Fragment[]{indexFragment, chatHistoryFragment,
//                surroundingsFragment, meFragment};

        fragments = new Fragment[]{indexFragment, chatHistoryFragment,
                newSurroundingFrg, meFragment};

        tv_notifycount = (TextView) findViewById(R.id.tv_newnotify_count);

        // 左右滑动
        viewPager = (ViewPager) findViewById(R.id.vp_index);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(3);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                mTabs[currentTabIndex].setSelected(false);
                // 把当前tab设为选中状态
                mTabs[arg0].setSelected(true);
                currentTabIndex = arg0;

                switch (arg0) {
                    case 0:
                        indexFragment.getLifeCircleCount(handler);
                        break;
                    case 2:
                        Log.i("debbug", "置为0");
                        PreferencesUtil.saveCrazySalesCount(MainActivity.this, 0);
                        unreadAddressLabel.setVisibility(View.INVISIBLE);

//                        indexFragment.eventService("" + 18, "周边店家");

                        newSurroundingFrg.updateUI();
//                        peripheryFragment.updateUI();

                    case 3:
                        Log.i("meFragement tags ", "置为已阅读");
                        PreferencesUtil.saveIsUnReadTagsChange(MainActivity.this, false);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

       /* IntentFilter offlineMessageIntentFilter = new
                IntentFilter(EMChatManager.getInstance()
                .getOfflineMessageBroadcastAction());
        offlineMessageIntentFilter.setPriority(6);
        registerReceiver(offlineMessageReceiver, offlineMessageIntentFilter);
        // 注册一个接收消息的BroadcastReceiver
        msgReceiver = new NewMessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(EMChatManager
                .getInstance().getNewMessageBroadcastAction());

        intentFilter.setPriority(3);
        registerReceiver(msgReceiver, intentFilter);

        // 注册一个ack回执消息的BroadcastReceiver
        IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager
                .getInstance().getAckMessageBroadcastAction());
        ackMessageIntentFilter.setPriority(3);
        registerReceiver(ackMessageReceiver, ackMessageIntentFilter);

        // 注册一个透传消息的BroadcastReceiver
        IntentFilter cmdMessageIntentFilter = new IntentFilter(EMChatManager
                .getInstance().getCmdMessageBroadcastAction());
        cmdMessageIntentFilter.setPriority(3);
        registerReceiver(cmdMessageReceiver, cmdMessageIntentFilter);*/

        // 注册一个离线消息的BroadcastReceiver

//EMChatManager.getInstance().getChatOptions().setNotifyText(new OnMessageNotifyListener() {
//    @Override
//    public String onNewMessageNotify(EMMessage message) {
//        operatNewMessage(message);
//        return message.getBody().toString();
//    }
//
//    @Override
//    public String onLatestMessageNotify(EMMessage message, int i, int i2) {
//        return null;
//    }
//
//    @Override
//    public String onSetNotificationTitle(EMMessage message) {
//        return message.getStringAttribute(Config.EXPKey_nickname,"帮帮帮用户");
//    }
//
//    @Override
//    public int onSetSmallIcon(EMMessage message) {
//        return R.drawable.me_arrow;
//    }
//});
        // setContactListener监听联系人的变化等
        EMContactManager.getInstance().setContactListener(new MyContactListener());
        // 注册一个监听连接状态的listener
        EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
        // 注册群聊相关的listener
        EMGroupManager.getInstance().addGroupChangeListener(new MyGroupChangeListener());

        // 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
        EMChat.getInstance().setAppInited();

        AppLoginInfoBean.Info info = (AppLoginInfoBean.Info) getIntent().getSerializableExtra(Config.IsLoginOtherApp);
        if (null != info) {
            showLogoutDialog("上次登录" + StrUtils.getTime4Millions(info.getLoginTime() * 1000L) + "\n设备id:" + info.getEquipment());
        }

        checkPushUpdate();

//        UpdateUtils.initUpdate(getmContext());


//        getRecords(getContentResolver());

        //// 2015/11/11 删除上周排行榜
//        if(TimeUtils.isNeedShowRPTopList(this)){
//            startActivity(new Intent(this, LastWeekRPValueTopListActivity.class));
//        }


        ////延时检查更新
//        handler.postDelayed(checkAppUpdate, new Random().nextInt(5) * 1000);
        initUpdate();


        //// 判断是否是首次安装 , 几个医生, 几个护士 , 几个家庭教师弹窗
//        PreferencesUtil.setFirstOpen(getmContext(), true);
        if (PreferencesUtil.isFirstOpen(getmContext())) {
            fetchCommunityCategoryData();
        }
//// 群组申请处理消息去重  2016/1/6
        if (PreferencesUtil.getLogin(this)) {
            long communityId = PreferencesUtil.getLoginInfo(this).getCommunityId();
            String emobId = PreferencesUtil.getLoginInfo(this).getEmobId();
            getGroupMsgsCall((int) communityId, emobId);
        }
        /// 预加载X5WebCore
//        if(!isX5WebViewEnabled){
//            preinitX5WebCore();
//        }

    }

    /**
     * X5内核在使用preinit接口之后，对于首次安装首次加载没有效果
     * 实际上，X5webview的preinit接口只是降低了webview的冷启动时间；
     * 因此，现阶段要想做到首次安装首次加载X5内核，必须要让X5内核提前获取到内核的加载条件
     */
    private void preinitX5WebCore() {
        if (!QbSdk.isTbsCoreInited()) {// preinit只需要调用一次，如果已经完成了初始化，那么就直接构造view
            QbSdk.preInit(MainActivity.this, myCallback);// 设置X5初始化完成的回调接口
            // 第三个参数为true：如果首次加载失败则继续尝试加载；
        }
    }

    private QbSdk.PreInitCallback myCallback = new QbSdk.PreInitCallback() {

        @Override
        public void onViewInitFinished() {// 当X5webview 初始化结束后的回调
            new WebView(MainActivity.this);
            MainActivity.this.isX5WebViewEnabled = true;
        }

        @Override
        public void onCoreInitFinished() {
        }
    };
    private void checkPushUpdate() {
        String emobid = PreferencesUtil.getLogin(this) ? PreferencesUtil.getLoginInfo(this).getEmobId() : PreferencesUtil.getTourist(this);

        if (xjpushManager != null) {
            xjpushManager.registerPushService(emobid);
        } else {
            xjpushManager = new XJPushManager(this);
            xjpushManager.registerPushService(emobid);
        }
    }

    private android.app.AlertDialog otherAppLogin;

    public void showLogoutDialog(String content) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.otherapplogin, null);
        final TextView cancle = (TextView) view.findViewById(R.id.cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherAppLogin.dismiss();
            }
        });
        ((TextView) view.findViewById(R.id.tv_content)).setText(content);
        otherAppLogin = builder.create();
        otherAppLogin.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        otherAppLogin.setView(view);
        otherAppLogin.setCanceledOnTouchOutside(true);
        otherAppLogin.show();

    }

    //// 检查更新 oncreate 时调用
    public void initUpdate() {

        String emobId = null;
        int communityId;
        if (PreferencesUtil.getLogin(this)) {
            emobId = PreferencesUtil.getLoginInfo(this).getEmobId();
            communityId = PreferencesUtil.getLoginInfo(this).getCommunityId();
        } else {
            communityId = PreferencesUtil.getCommityId(this);
            if (PreferencesUtil.getTouristLogin(this)) {
                emobId = PreferencesUtil.getTourist(this);
            } else {
                emobId = PreferencesUtil.getlogoutEmobId(this);
            }
        }
        Log.d("checkAppUpdate", "checkAppUpdate  is running ...  ");
        NetBaseUtils.getAppUpdateInfoV3(getmContext(),communityId, emobId, new NetBaseUtils.NetRespListener<CommonRespBean<UpDateApp.Info>>() {
            @Override
            public void successYes(CommonRespBean<UpDateApp.Info> commonRespBean, Response response) {
                PreferencesUtil.saveCheckTime(getmContext(), new Date().getTime());
                String version = "";
                try {
                    version = UserUtils.getVersion(getmContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (equalsVersion(commonRespBean.getData().getVersion(), version)) {
                    tv_newbangbi.setVisibility(View.VISIBLE);
                    Log.d("isVersionUpdate", "isVersionUpdate  VISIBLE ");
                    new UpdateManager(MainActivity.this, commonRespBean.getData().version, "false", commonRespBean.getData().url, commonRespBean.getData().detail);
//                    new UpdateManager(MainActivity.this, Object.getInfo().version, "false", "http://7d9lcl.com2.z0.glb.qiniucdn.com/bangbang_c.apk", Object.getInfo().detail);
                    UpdateManager.showNoticeDialog(MainActivity.this);
                    isVersionUpdate = true;
                } else {
                    /// 如果没有新版本  ,并且没有其他消息 消失
                    updateBangZhuNews();
                }
            }

            @Override
            public void successNo(CommonRespBean<UpDateApp.Info> commonRespBean, Response response) {
                PreferencesUtil.saveCheckTime(MainActivity.this, System.currentTimeMillis());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("onion", "失败" + error.toString());
            }
        });

    }

    /**
     * 初始化组件
     */
    private void initView() {
        unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
        unreadAddressLabel = (TextView) findViewById(R.id.unread_address_number);
        tv_newbangbi = (TextView) findViewById(R.id.tv_newbangbi);

        mTabs = new Button[4];
        mTabs[0] = (Button) findViewById(R.id.btn_index);
        mTabs[1] = (Button) findViewById(R.id.btn_conversation);
        mTabs[2] = (Button) findViewById(R.id.btn_address_list);
        mTabs[3] = (Button) findViewById(R.id.btn_setting);
        /**
         * set first label selected
         */
        mTabs[0].setSelected(true);

    }

    /**
     * button点击事件
     *
     * @param view
     */
    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_index:
                index = 0;
                refreshIndexUI();
                break;
            case R.id.btn_conversation:
                if (PreferencesUtil.getLogin(MainActivity.this)) {
                    refreshUI();
                    index = 1;
                } else {
                    Intent intent = new Intent(MainActivity.this, RegisterLoginRelationActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.btn_address_list:
                index = 2;
                PreferencesUtil.saveCrazySalesCount(MainActivity.this, 0);
                unreadAddressLabel.setVisibility(View.INVISIBLE);
                // indexFragment.eventService(""+17,"周边");

                //// 周边
//                eventServiceUtils.postClickEvent(currentPagerUUID,"17");
                eventServiceUtils.postScheduleClickEvent(currentPagerUUID, "17");

                break;
            case R.id.btn_setting:
                refreshNewBangBiUI();
                index = 3;
                break;
        }
        if (currentTabIndex != index) {
            viewPager.setCurrentItem(index);
        }
        mTabs[currentTabIndex].setSelected(false);
        /**
         * set current lab selected
         */
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }

    public void refreshIndexUI() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (indexFragment != null) {
                    indexFragment.refreshIndexMenu();
                }
            }
        });
    }

    public void refreshUI() {
        Log.i("onion", "refreshUI");
        runOnUiThread(new Runnable() {
            public void run() {
                //mChatAllHistoryFProvider.getGroupInfo();
                // 刷新bottom bar消息未读数
                updateUnreadLabel();
                // 当前页面如果为聊天历史页面，刷新此页面
                if (chatHistoryFragment != null) {
                    chatHistoryFragment.refresh();
                }
            }
        });
    }

    public void refreshUI(final String groupId) {
//        Log.i("onion","refreshUI");
        runOnUiThread(new Runnable() {
            public void run() {
                mChatAllHistoryFProvider.getGroupInfo(groupId);
                // 刷新bottom bar消息未读数
                updateUnreadLabel();
                // 当前页面如果为聊天历史页面，刷新此页面
                if (chatHistoryFragment != null) {
                    chatHistoryFragment.refresh();
                }
            }
        });
    }


    public void refreshNewBangBiUI() {
        runOnUiThread(new Runnable() {
            public void run() {
                // 刷新是否有新获取的帮帮币
                updateNewbangBi();
            }
        });
    }


    ConnectivityManager mConnectivityManager = null;
    NetworkInfo netInfo = null;
    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {

        @Override
        public synchronized void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isAvailable()) {
                    if (!isNetwork) {
                        isNetwork = true;

//                        PushUtil.isActive(MainActivity.this);

//                        AdminUtils.askAdmin(MainActivity.this, "service", null, null);

                        //TODO
                    }
                    Log.i("onion", "有网");
                    startService(new Intent(context, StatisticAsyncService.class));
                    /////////////网络连接
                    String name = netInfo.getTypeName();

                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        /////WiFi网络
                    } else if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                        /////有线网络 303933808@qq.com

                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        /////////3g网络

                    }
                } else {
                    isNetwork = false;
                    ////////网络断开
                    Log.i("onion", "没网");
                }
            }

        }
    };


    class HomeWatcherReceiver extends BroadcastReceiver {
        private static final String LOG_TAG = TAG;
        private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
        private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        private static final String SYSTEM_DIALOG_REASON_LOCK = "lock";
        private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(LOG_TAG, "onReceive: action: " + action);
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                // android.intent.action.CLOSE_SYSTEM_DIALOGS
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                Log.i(LOG_TAG, "reason: " + reason);

                if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
                    // 短按Home键
                    Log.i(LOG_TAG, "homekey");
                    startService(new Intent(getmContext(), StatisticAsyncService.class));
                } else if (SYSTEM_DIALOG_REASON_RECENT_APPS.equals(reason)) {
                    // 长按Home键 或者 activity切换键
                    Log.i(LOG_TAG, "long press home key or activity switch");
                } else if (SYSTEM_DIALOG_REASON_LOCK.equals(reason)) {
                    // 锁屏
                    Log.i(LOG_TAG, "lock");
                } else if (SYSTEM_DIALOG_REASON_ASSIST.equals(reason)) {
                    // samsung 长按Home键
                    Log.i(LOG_TAG, "assist");
                }

            }
        }

    }

    private HomeWatcherReceiver mHomeKeyReceiver = null;

    private void registerHomeKeyReceiver(Context context) {
        Log.i(TAG, "registerHomeKeyReceiver");
        mHomeKeyReceiver = new HomeWatcherReceiver();
        final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.registerReceiver(mHomeKeyReceiver, homeFilter);
    }

    private void unregisterHomeKeyReceiver(Context context) {
        Log.i(TAG, "unregisterHomeKeyReceiver");
        if (null != mHomeKeyReceiver) {
            context.unregisterReceiver(mHomeKeyReceiver);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterHomeKeyReceiver(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed : " + this.getClass().getName());
        if (!TextUtils.isEmpty(currentPagerUUID)) {
            Log.d(TAG, "onBackPressed : " + this.getClass().getName() + " currentPagerUUID : " + currentPagerUUID);
            if (eventServiceUtils != null) {
                //// 退出事件
                eventServiceUtils.postScheduleExitEvent(currentPagerUUID, this.getClass().getSimpleName());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i("MainActivity", "onDestroy");

        if (myNetReceiver != null) {
            unregisterReceiver(myNetReceiver);
        }
        if (conflictBuilder != null) {
            conflictBuilder.create().dismiss();
            conflictBuilder = null;
        }

        if (homeDialogTimer != null) {
            homeDialogTimer.cancel();
        }

        if (myCommunityCategoryDialog != null) {
            myCommunityCategoryDialog.dismiss();
            myCommunityCategoryDialog = null;
        }


    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销广播接收者
        try {
            unregisterReceiver(msgReceiver);
        } catch (Exception e) {
        }
        try {
            unregisterReceiver(cmdMessageReceiver);
            unregisterReceiver(ackMessageReceiver);
        } catch (Exception e) {
        }
        try {
            unregisterReceiver(offlineMessageReceiver);
        } catch (Exception e) {
        }
        if (conflictBuilder != null) {
            conflictBuilder.create().dismiss();
            conflictBuilder = null;
        }
    }*/

    /**
     * 刷新未读消息数
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            unreadLabel.setText(String.valueOf(count));
            unreadLabel.setVisibility(View.VISIBLE);
        } else {
            unreadLabel.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 刷新是否有新的帮帮币以及新的版本
     */
    public void updateNewbangBi() {

        boolean newBangBi = PreferencesUtil.getNewBangBangBi(this);
        if ((!newBangBi) && (!isVersionUpdate)) {

            tv_newbangbi.setVisibility(View.INVISIBLE);
        } else {

            tv_newbangbi.setVisibility(View.VISIBLE);
        }
        Log.d("updateNewbangBi", "newBangBi) &&(!isVersionUpdate" + ((!newBangBi) && (!isVersionUpdate)));

        //// 如果不可见则帮主群内消息公用.
        if (tv_newbangbi.getVisibility() != View.VISIBLE) {
            updateBangZhuNews();
        }

        //// 如果不可见则我的页面,标签更改信息刷新
        if (tv_newbangbi.getVisibility() != View.VISIBLE) {
            updateMyTagsChanges();
        }

    }


    /**
     * 标签更改信息显示
     */
    private void updateMyTagsChanges() {

        boolean isUnreadTagsChange = PreferencesUtil.getisUnReadTagsChange(this);
        if (isUnreadTagsChange) {
            Log.d("updateMyTagsChanges", "没有读过,则显示为红点可见.  " + isUnreadTagsChange);
            if (tv_newbangbi != null) {
                tv_newbangbi.setVisibility(View.VISIBLE);
            }
        }

    }

    /**
     * 刷新是否有新的帮主内信息
     */
    public void updateBangZhuNews() {

        String currentTimeStamp = PreferencesUtil.getCurrentBangZhuNewsTimeStamp(this);

        ////是否读过邀请邻居和竞选帮主
        boolean bangzhuNews = PreferencesUtil.getCurrentBangzhuNewsReadStatus(this, currentTimeStamp);

        //// 只要邀请邻居和竞选帮主有一个没有点击则一直显示红点
        if (PreferencesUtil.getBangzhuUnReadStatus(this) || PreferencesUtil.getInvitedNeighborsUnReadStatus(this)) {

            Log.d("updateBangZhuNews", "getBangzhuUnReadStatus " + PreferencesUtil.getBangzhuUnReadStatus(this));
            Log.d("updateBangZhuNews", "getInvitedNeighborsUnReadStatus " + PreferencesUtil.getInvitedNeighborsUnReadStatus(this));
            Log.d("updateBangZhuNews", "只要邀请邻居和竞选帮主有一个没有点击则一直显示红点");

            if (tv_newbangbi != null) {
                tv_newbangbi.setVisibility(View.VISIBLE);
            }
            return;
        }

        Log.d("currentTimeStamp", "" + currentTimeStamp);
        if (TextUtils.isEmpty(currentTimeStamp) || TextUtils.equals(currentTimeStamp, "-1")) {/// new状态,但是还需要显示为红点
            if (tv_newbangbi != null) {
                tv_newbangbi.setVisibility(View.INVISIBLE);
            }
            Log.d("currentTimeStamp", "tv_newbangbi.setVisibility(View.INVISIBLE) ");
        } else if (bangzhuNews) { //// 如果读过则红点不可见
            Log.d("bangzhuNews", "如果读过则红点不可见  " + bangzhuNews);

            if (tv_newbangbi != null) {
                tv_newbangbi.setVisibility(View.INVISIBLE);
            }
        } else {
            /// 没有读过,则显示为红点可见.
            Log.d("bangzhuNews", "没有读过,则显示为红点可见.  " + bangzhuNews);
            if (tv_newbangbi != null) {
                tv_newbangbi.setVisibility(View.VISIBLE);
            }
        }


    }


    /**
     * 刷新申请与通知消息数
     */
    public void updateUnreadAddressLable() {
        runOnUiThread(new Runnable() {
            public void run() {
                int count = getUnreadAddressCountTotal();
                if (count > 0) {
                    unreadAddressLabel.setText(String.valueOf(count));
                    unreadAddressLabel.setVisibility(View.VISIBLE);
                } else {
                    unreadAddressLabel.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    /**
     * 刷新未读消息数
     */
    public void updateUnreadLabel(final int count) {
        if (count > 0) {
            unreadLabel.setText(String.valueOf(count));
            unreadLabel.setVisibility(View.VISIBLE);
        } else {
            unreadLabel.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 获取未读申请与通知消息
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;

        if (PreferencesUtil.getLogin(this) && XjApplication.getInstance().getContactList() != null && XjApplication.getInstance().getContactList()
                .get(Constant.NEW_FRIENDS_USERNAME) != null)
            unreadAddressCountTotal = XjApplication.getInstance()
                    .getContactList().get(Constant.NEW_FRIENDS_USERNAME)
                    .getUnreadMsgCount();
        return unreadAddressCountTotal;
    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;

        if (PreferencesUtil.getLogin(getmContext())) {
            unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
            //// 所有通知申请消息中未处理消息数
            unreadMsgCountTotal += getAllUnHandleNumsNewFriends();

            Log.d("getUnreadMsgCountTotal ", "unreadMsgCountTotal added  " + unreadMsgCountTotal);
        }

        unreadMsgCountTotal += PreferencesUtil.getWelfareSuccessMsgCount(getmContext());

        Log.d("getUnreadMsgCountTotal ", "unreadMsgCountTotal " + unreadMsgCountTotal + "welfaremsg count :  " + PreferencesUtil.getWelfareSuccessMsgCount(getmContext()));


        return unreadMsgCountTotal;
    }

    ArrayList<String> offlineUser = new ArrayList<>();
    // return true 为小区通知 false为一般消息

    /**
     * 新消息广播接收者
     */
    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看
            String from = intent.getStringExtra("from");
            // 消息id
            String msgId = intent.getStringExtra("msgid");
            EMMessage message = EMChatManager.getInstance().getMessage(msgId);
            if (offlineUser.contains(from)) {
                XJMessageHelper.loadOffLineMessage(getmContext(),EMChatManager.getInstance().getConversation(from));
                offlineUser.remove(from);
                updateUnreadLabel();
                Log.i("onion", "离线消息");
                return;

            } else if (!XJMessageHelper.operatNewMessage(getmContext(),message)) {
                XJContactHelper.saveContact(message);
                Log.i("onion", "新消息");
            } else {
                Log.i("onion", "是个通知");
                chatHistoryFragment.refresh();
                updateUnreadLabel();
                return;
            }
            if (message.getChatType() == ChatType.GroupChat) {
                GroupHeader header = new Select().from(GroupHeader.class).where("group_id = ?", message.getTo()).executeSingle();
                if (header == null || header.getNum() < 10 || !new File(header.getHeader_id()).exists())
                    GroupUtils.getGroupInfo(message.getTo());
            }
            chatHistoryFragment.refresh();
//            if(XJMessageHelper.operatNewMessage(message)){abortBroadcast();return;}
          /*  try {
                String strUserInfo = message.getStringAttribute("xj");
                logger.info("xj is :" + message.getStringAttribute("xj"));
                Gson gson = new Gson();
                UserInfoBean userInfoBean = gson.fromJson(strUserInfo, UserInfoBean.class);
                logger.info("nickname is :" + userInfoBean.getUserInfo().getNickname());
                logger.info("avatar is :" + userInfoBean.getUserInfo().getAvatar());

            } catch (EaseMobException e) {
                e.printStackTrace();
            }*/

            // 2014-10-22 修复在某些机器上，在聊天页面对方发消息过来时不立即显示内容的bug
            if (ChatActivity.activityInstance != null) {
                if (message.getChatType() == ChatType.GroupChat) {
                    if (message.getTo().equals(
                            ChatActivity.activityInstance.getToChatUsername()))
                        return;
                } else {
                    if (from.equals(ChatActivity.activityInstance
                            .getToChatUsername()))
                        return;
                }
            }

            // 注销广播接收者，否则在ChatActivity中会收到这个广播
            abortBroadcast();
            //过滤掉通知公告
            //todo 自定义显示位置在首页顶部
//            if(!from.equals("admin")){
            notifyNewMessage(message);
            // 刷新bottom bar消息未读数
            updateUnreadLabel();
//            }

//            if (currentTabIndex == 1) {
//                // 当前页面如果为聊天历史页面，刷新此页面
//                if (chatHistoryFragment != null) {
//                    chatHistoryFragment.refresh();
//                }
//            }

        }

    }

    /**
     * 消息回执BroadcastReceiver
     */
    private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();

            String msgid = intent.getStringExtra("msgid");
            String from = intent.getStringExtra("from");

            EMConversation conversation = EMChatManager.getInstance()
                    .getConversation(from);
            if (conversation != null) {
                // 把message设为已读
                EMMessage msg = conversation.getMessage(msgid);

                if (msg != null) {

                    // 2014-11-5 修复在某些机器上，在聊天页面对方发送已读回执时不立即显示已读的bug
                    if (ChatActivity.activityInstance != null) {
                        if (msg.getChatType() == ChatType.Chat) {
                            if (from.equals(ChatActivity.activityInstance
                                    .getToChatUsername()))
                                return;
                        }
                    }

                    msg.isAcked = true;
                }
            }

        }
    };

    private static String title = "";//接收透传消息
    private String content = "";
    private int timestamp = 0;
    /**
     * 透传消息BroadcastReceiver
     */
    private BroadcastReceiver cmdMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();
//            EMLog.d(TAG, "收到透传消息");
            //获取cmd message对象
            String msgId = intent.getStringExtra("msgid");
            EMMessage message = intent.getParcelableExtra("message");
            //获取消息body
            CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
            String action = cmdMsgBody.action;// 获取自定义action

            //获取扩展属性 此处省略
            try {
                title = message.getStringAttribute("title");
                content = message.getStringAttribute("content");
                timestamp = message.getIntAttribute("timestamp");
            } catch (EaseMobException e) {
                e.printStackTrace();
            }

//            if("200".equals(code)){
//                Intent it=new Intent(MainActivity.this,CommentActivity.class);
//                startActivity(it);
//                code="";
//            }
            Log.i(TAG, "form--" + message.getFrom() + "    to--" + message.getTo());
        }
    };

    /**
     * 离线消息BroadcastReceiver sdk 登录后，服务器会推送离线消息到client，这个receiver，是通知UI
     * 有哪些人发来了离线消息 UI 可以做相应的操作，比如下载用户信息
     */
    private BroadcastReceiver offlineMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            XJMessageHelper.loadNativeMessage();
//            abortBroadcast();
            String[] users = intent.getStringArrayExtra("fromuser");
            String[] groups = intent.getStringArrayExtra("fromgroup");
            Log.i("onion", "users" + users);
            if (users != null) {
                for (String user : users) {
//                   XJMessageHelper.loadOffLineMessage(EMChatManager.getInstance().getConversation(user));
                    offlineUser.add(user);
                }
            }
            Log.i("onion", "groups" + groups);
            if (groups != null) {
                for (String group : groups) {
                    GroupHeader header = new Select().from(GroupHeader.class).where("group_id = ?", group).executeSingle();
                    if (header == null || header.getNum() < 10 || !new File(header.getHeader_id()).exists())
                        GroupUtils.getGroupInfo(group);
                }
            }
//            chatHistoryFragment.refresh();
//            updateUnreadLabel();
//            if (groups != null) {
//                for (String group : groups) {
//                    System.out.println("收到group离线消息：" + group);
//                }
//            }
        }
    };

    private InviteMessgeDao inviteMessgeDao;
    //private UserDao userDao;

    /**
     * 好友变化listener
     */
    private class MyContactListener implements EMContactListener {

        @Override
        public void onContactAdded(List<String> usernameList) {
            // 保存增加的联系人
            Map<String, User> localUsers = XjApplication.getInstance()
                    .getContactList();
            Map<String, User> toAddUsers = new HashMap<String, User>();
            for (String username : usernameList) {
                User user = setUserHead(username);
                // 添加好友时可能会回调added方法两次
                if (!localUsers.containsKey(username)) {
                    //  userDao.saveContact(user);
                }
                toAddUsers.put(username, user);
            }
            localUsers.putAll(toAddUsers);
            // 刷新ui
//            if (currentTabIndex == 2)
//                contactListFragment.refresh();
        }

        @Override
        public void onContactDeleted(final List<String> usernameList) {
            // 被删除
            Map<String, User> localUsers = XjApplication.getInstance()
                    .getContactList();
            for (String username : usernameList) {
                localUsers.remove(username);
                //  userDao.deleteContact(username);
                inviteMessgeDao.deleteMessage(username);
            }
            runOnUiThread(new Runnable() {
                public void run() {
                    // 如果正在与此用户的聊天页面
                    if (ChatActivity.activityInstance != null
                            && usernameList
                            .contains(ChatActivity.activityInstance
                                    .getToChatUsername())) {
                        Toast.makeText(
                                MainActivity.this,
                                ChatActivity.activityInstance.getToChatUsername() + "已把你从他好友列表里移除",
                                Toast.LENGTH_SHORT).show();

                        ChatActivity.activityInstance.finish();
                    }
                    updateUnreadLabel();
                    // 刷新ui
                    if (currentTabIndex == 2) {

//                        contactListFragment.refresh();
                    } else if (currentTabIndex == 1)
                        refreshUI();
                }
            });

        }

        @Override
        public void onContactInvited(String username, String reason) {
            // 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不需要重复提醒
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();

            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getGroupId() == null
                        && inviteMessage.getFrom().equals(username)) {
                    inviteMessgeDao.deleteMessage(username);
                }
            }
            // 自己封装的javabean
            InviteMessage msg = new InviteMessage();
            msg.setFrom(username);
            msg.setTime(System.currentTimeMillis());
            msg.setReason(reason);
            Log.d(TAG, username + "请求加你为好友,reason: " + reason);
            // 设置相应status
            msg.setStatus(InviteMesageStatus.BEINVITEED);
            notifyNewIviteMessage(msg);

        }

        @Override
        public void onContactAgreed(String username) {
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getFrom().equals(username)) {
                    return;
                }
            }
            // 自己封装的javabean
            InviteMessage msg = new InviteMessage();
            msg.setFrom(username);
            msg.setTime(System.currentTimeMillis());
            Log.d(TAG, username + "同意了你的好友请求");
            msg.setStatus(InviteMesageStatus.BEAGREED);
            notifyNewIviteMessage(msg);

        }

        @Override
        public void onContactRefused(String username) {
            // 参考同意，被邀请实现此功能,demo未实现
            Log.d(username, username + "拒绝了你的好友请求");
        }

    }

    /**
     * 保存提示新消息
     *
     * @param msg
     */
    private void notifyNewIviteMessage(InviteMessage msg) {
        saveInviteMsg(msg);
        // 提示有新消息
        EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

        // 刷新bottom bar消息未读数
//        updateUnreadAddressLable();
        // 刷新好友页面ui
//        if (currentTabIndex == 2)
//            contactListFragment.refresh();
    }

    /**
     * 保存邀请等msg
     *
     * @param msg
     */
    private void saveInviteMsg(InviteMessage msg) {
        // 保存msg
        inviteMessgeDao.saveMessage(msg);
        // 未读数加1
        User user = XjApplication.getInstance().getContactList()
                .get(Constant.NEW_FRIENDS_USERNAME);
        if (user.getUnreadMsgCount() == 0)
            user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
    }

    /**
     * set head
     *
     * @param username
     * @return
     */
    User setUserHead(String username) {
        User user = new User();
        user.setUsername(username);
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
            user.setHeader(HanziToPinyin.getInstance()
                    .get(headerName.substring(0, 1)).get(0).target.substring(0,
                            1).toUpperCase());
            char header = user.getHeader().toLowerCase().charAt(0);
            if (header < 'a' || header > 'z') {
                user.setHeader("#");
            }
        }
        return user;
    }

    /**
     * 连接监听listener
     */
    private class MyConnectionListener implements EMConnectionListener {

        @Override
        public void onConnected() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    // ---------------------------------------------本来无注释

                    // chatHistoryFragment.errorItem.setVisibility(View.GONE);
                }

            });
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (error == EMError.CONNECTION_CONFLICT) {
                        // 显示帐号在其他设备登陆dialog
                        showConflictDialog();
//                     boolean flag= PushManager.getInstance().unBindAlias(MainActivity.this,   PreferencesUtil.getLoginInfo(MainActivity.this).getEmobId());
//                        PreferencesUtil.Logout(MainActivity.this);
                    } else {
                        Log.d("test", "login error");
//                        chatHistoryFragment.errorItem.setVisibility(View.VISIBLE);
//                        if (NetUtils.hasNetwork(MainActivity.this))
//                            chatHistoryFragment.errorText.setText("连接不到聊天服务器");
//                        else
//                            chatHistoryFragment.errorText
//                                    .setText("当前网络不可用，请检查网络设置");
                    }
                }

            });
        }
    }

    ////api/v1/communities/{communityId}/users/{emobId}/activities/messages?time={time}


    /**
     * 群组成员变动监听, 包含申请加入,
     */
    private class MyGroupChangeListener implements GroupChangeListener {

        @Override
        public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
            //收到加入群聊的邀请
            Log.i("MyGroupChangeListener ", "onInvitationReceived " + groupId + groupName + reason);
            for (EMGroup group : EMGroupManager.getInstance().getAllGroups()) {
                if (group.getGroupId().equals(groupId)) {
                    /// 说明我已经在该群中，不需要接受邀请
                    return;
                }
            }

//            // 被邀请
//            EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
//            msg.setFrom(inviter);
//            msg.setTo(groupId);
//            msg.setMsgId(UUID.randomUUID().toString());
//            //群主 inviter
//            msg.addBody(new TextMessageBody("" + groupName + "邀请你加入了群聊"));
//            // 保存邀请消息
//            EMChatManager.getInstance().saveMessage(msg);
//            // 提醒新消息
//            EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();


            try {

                String agreetxt = groupName + "邀请你加入群聊";
                JSONObject obj = new JSONObject();
                obj.put("groupName", groupName);
                obj.put("groupId", groupId);
                obj.put("inviter", inviter);
                obj.put("reason", agreetxt);

//                String n_messageId = obj.getString("n_messageId");


//                String n_messageId = emobIdGroup+"_"+emobGroupOwner+"_"+userBean.getEmobId();
                userInfoBean = PreferencesUtil.getLoginInfo(getmContext());
                String n_messageId = null;
                if (userInfoBean != null) {
                    n_messageId = groupId + "_" + inviter + "_" + userInfoBean.getEmobId();
                    //// 被邀请
                    GroupUtils.createEMConversationMessage(getmContext(), obj.toString(), n_messageId, InviteMesageStatus.JOINED);
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(TAG, e.getMessage());
            }
            runOnUiThread(new Runnable() {
                public void run() {
                    updateUnreadLabel();
                    if (currentTabIndex == 1) {
                        refreshUI();
                    }
                }
            });
        }


        /**
         * @param groupId
         * @param inviter
         * @param reason
         * @"u_userName" : [XJAccountTool account].nickname,
         * @"u_userAvatar" : [XJAccountTool account].avatar,
         * @"n_reason" : alertView.textView.text,
         * @"n_messageId" : message.messageId,
         * @"g_groupOwerEmobId"
         */


        @Override
        public void onInvitationAccpted(String groupId, String inviter, String reason) {
            //群聊邀请被接受
            Log.i("MyGroupChangeListener", "onInvitationAccpted " + groupId + inviter + reason);
//
//            try {
//                JSONObject obj = new JSONObject();
//                obj.put("reason",reason);
//                obj.put("groupId",groupId);
//                obj.put("inviter",inviter);
//                obj.put("reason",reason);
//
//                String emobid = PreferencesUtil.getLoginInfo(getmContext()).getEmobId();
//                GroupUtils.createEMConversationMessage(getmContext(),obj.toString(), emobid,InviteMesageStatus.JOINED);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Log.d(TAG, e.getMessage());
//            }

        }

        @Override
        public void onInvitationDeclined(String groupId, String invitee, String reason) {
            //群聊邀请被拒绝
//            showToast("onInvitationDeclined "+ reason);

//            Log.i("MyGroupChangeListener","onInvitationDeclined "+ groupId + invitee + reason);
//
//            try {
//                JSONObject obj = new JSONObject();
//                obj.put("reason",reason);
//                obj.put("groupId",groupId);
//                obj.put("invitee",invitee);
//
//                Log.d(TAG, invitee + " 拒绝加入"  + groupId+ "的邀请, reason "+ reason );
//
//                String emobid = PreferencesUtil.getLoginInfo(getmContext()).getEmobId();
//                GroupUtils.createEMConversationMessage(getmContext(),obj.toString(), emobid,InviteMesageStatus.BEREFUSED);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Log.d(TAG, e.getMessage());
//            }
        }

        @Override
        public void onUserRemoved(String groupId, String groupName) {
            //当前用户被管理员移除出群聊
            // 刷新ui

            runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        updateUnreadLabel();
                        if (currentTabIndex == 1)
                            refreshUI();
                        if (CommonUtils.getTopActivity(MainActivity.this)
                                .equals(GroupsActivity.class.getName())) {
                            GroupsActivity.instance.onResume();
                        }
                    } catch (Exception e) {
                        EMLog.e(TAG, "refresh exception " + e.getMessage());
                    }
                }
            });
        }

        @Override
        public void onGroupDestroy(String groupId, String groupName) {
            //群聊被创建者解散

//            showToast("onGroupDestroy "+ groupName);

            try {
                JSONObject obj = new JSONObject();
                obj.put("groupName", groupName);
                obj.put("groupId", groupId);

                Log.d(TAG, groupName + " 被解散" + " groupId " + groupId);
//
//                String emobid = PreferencesUtil.getLoginInfo(getmContext()).getEmobId();
//                GroupUtils.createEMConversationMessage(getmContext(),obj.toString(), emobid,InviteMesageStatus.AGREED);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(TAG, e.getMessage());
            }

            // 提示用户群被解散,demo省略
            // 刷新ui
            runOnUiThread(new Runnable() {
                public void run() {
                    updateUnreadLabel();
                    if (currentTabIndex == 1)
                        refreshUI();
                    if (CommonUtils.getTopActivity(MainActivity.this).equals(
                            GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                }
            });
        }

        /**
         * @param groupId
         * @param groupName
         * @param applyer
         * @param reason
         */
        @Override
        public void onApplicationReceived(String groupId, String groupName, String
                applyer, String reason) {
            //收到加群申请
            try {
                JSONObject obj = new JSONObject(reason);

//                    jsonObject = jsonObject.put("u_userName", userBean.getNickname());
//                    jsonObject = jsonObject.put("u_userAvatar", userBean.getAvatar());
//                    jsonObject = jsonObject.put("n_reason", joinReason);
//                    jsonObject = jsonObject.put("g_groupOwerEmobId", emobGroupOwner);
//                    jsonObject = jsonObject.put("n_messageId", n_messageId);


                String joinReason = obj.getString("n_reason");
                String nickname = obj.getString("u_userName");
                String userAvatar = obj.getString("u_userAvatar");
                String g_groupOwerEmobId = obj.getString("g_groupOwerEmobId");
                String n_messageId = obj.getString("n_messageId");

                obj.put("groupName", groupName);
                obj.put("groupId", groupId);
                obj.put("applyerId", applyer);

                Log.d(TAG, nickname + " 申请加入群聊：" + groupName + " groupId " + groupId + " reason " + reason);

                //// 对方申请消息
                GroupUtils.createEMConversationMessage(getmContext(), obj.toString(), n_messageId, InviteMesageStatus.BEAPPLYED);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(TAG, e.getMessage());
            }
            runOnUiThread(new Runnable() {
                public void run() {
                    updateUnreadLabel();
                    if (currentTabIndex == 1) {
                        refreshUI();
                    }
                }
            });


//            // 用户申请加入群聊
//            InviteMessage msg = new InviteMessage();
//            msg.setFrom(applyer);
//            msg.setTime(System.currentTimeMillis());
//            msg.setGroupId(groupId);
//            msg.setGroupName(groupName);
//            msg.setReason(reason);

//            msg.setStatus(InviteMesageStatus.BEAPPLYED);
//            notifyNewIviteMessage(msg);
        }


        @Override
        public void onApplicationAccept(String groupId, String groupName, String accepter) {
            //加群申请被同意
            Log.i("MyGroupChangeListener", "onApplicationAccept " + groupId + groupName + accepter);

//            aff offline group application accetpt received event for group

            try {
                //// 创建msgId
//                String n_messageId = UUID.randomUUID().toString().replaceAll("-", "");

                userInfoBean = PreferencesUtil.getLoginInfo(getmContext());
                if (userInfoBean != null) {
                    /////
                    String n_messageId = groupId + "_" + accepter + "_" + userInfoBean.getEmobId();

                    JSONObject obj = new JSONObject();
                    obj.put("groupName", groupName);
                    obj.put("groupId", groupId);
                    obj.put("accepter", accepter);
                    Log.d(TAG, groupName + " 同意了你的群聊申请" + " groupId " + groupId);

                    GroupUtils.createEMConversationMessage(getmContext(), obj.toString(), n_messageId, InviteMesageStatus.BEAGREED);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            updateUnreadLabel();
                            // 刷新ui
                            if (currentTabIndex == 1)
                                refreshUI();
                        }
                    });

                    try {
                        EMGroupManager.getInstance().getGroupsFromServer();
                    } catch (EaseMobException e) {
                        e.printStackTrace();
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(TAG, e.getMessage());
            }

        }

        @Override
        public void onApplicationDeclined(String groupId, String groupName, String
                decliner, String reason) {
            // 加群申请被拒绝
            Log.i("onApplicationDeclined", groupId + groupName + reason + decliner);
            if (userInfoBean != null && TextUtils.equals(decliner, userInfoBean.getEmobId())) {
                return;
            }

            try {

                JSONObject jsonObject = new JSONObject(reason);

//                jsonObject.put("u_userName", nickname);
//                jsonObject.put("u_userAvatar", userAvatar);
//                jsonObject.put("n_reason", declineReason);
//                jsonObject.put("n_messageId", n_messageId);
//                jsonObject.put("g_groupId", groupId);
//                jsonObject.put("g_groupname", groupName);
//                jsonObject.put("g_groupOwerEmobId", g_groupOwerEmobId);

                jsonObject.put("groupName", groupName);
                jsonObject.put("groupId", groupId);
                jsonObject.put("decliner", decliner);

                String n_messageId = jsonObject.getString("n_messageId");


                Log.d(TAG, decliner + " 拒绝加入：" + groupName + " groupId " + groupId + " reason " + reason);

                GroupUtils.createEMConversationMessage(getmContext(), jsonObject.toString(), n_messageId, InviteMesageStatus.BEREFUSED);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(TAG, e.getMessage());
            }
            runOnUiThread(new Runnable() {
                public void run() {
                    updateUnreadLabel();
                    // 刷新ui
                    if (currentTabIndex == 1)
                        refreshUI();
                }
            });
        }

    }


    public int getAllUnHandleNumsNewFriends() {

        if (EMChatManager.getInstance() != null && PreferencesUtil.getLogin(getmContext())) {
            EMConversation conversation = EMChatManager.getInstance().getConversation(Constant.NEW_FRIENDS_USERNAME);
            List<EMMessage> allMessages = conversation.getAllMessages();
            int unhandleNums = 0;

            for (EMMessage message : allMessages) {
                try {
                    String isHandle = message.getStringAttribute(Config.EXPKey_ishandle);
                    if ("n".equals(isHandle)) {
                        unhandleNums++;
                    }
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }

            Log.d("getAllUnHandleNumsNewFriends ", "unhandleNums " + unhandleNums);
            return unhandleNums;
        }
        return 0;
    }

    /**
     * indexFragment.refreshIndexMenu();
     * indexFragment.getLifeCircleCount(handler);
     */

    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        if (!isConflict) {
//            updateUnreadLabel();
//            updateUnreadAddressLable();
            EMChatManager.getInstance().activityResumed();
            refreshUI();
            refreshNewBangBiUI();
        }
        // unregister this event listener when this activity enters the
        // background
        HXSDKHelper sdkHelper = (HXSDKHelper) HXSDKHelper.getInstance();
        sdkHelper.pushActivity(this);
        EMChatManager.getInstance().registerEventListener(this, new EMNotifierEvent.Event[]{
                EMNotifierEvent.Event.EventNewMessage,
                EMNotifierEvent.Event.EventDeliveryAck,
                EMNotifierEvent.Event.EventOfflineMessage,
                EMNotifierEvent.Event.EventConversationListChanged});

        initNotifyCount();

        viewPager.setCurrentItem(index);

//        if (Config.PHONETYPE.equals(android.os.Build.MANUFACTURER)) {
        //// 2015/12/2 未读推送拉取 去除单一对小米手机进行消息拉取处理
        XjApplication.getInstance().pool.submit(new Runnable() {
            @Override
            public void run() {
                startService(new Intent(getmContext(), PushPullReqService.class));
            }
        });
//        }

        registerHomeKeyReceiver(this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private android.app.AlertDialog.Builder conflictBuilder;
    android.app.AlertDialog conflictDialog;
    private boolean isConflictDialogShow;
    boolean progressShow;

    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;
        final UserInfoDetailBean detailBean = PreferencesUtil.getLoginInfo(getApplication());
        username = detailBean.getUsername();
        password = detailBean.getPassword();
        final XJUserInfoBean bean = new XJUserInfoBean();
        bean.setInfo(detailBean);

        if (xjpushManager != null) {
            xjpushManager.unregisterLoginedPushService();
        } else {
            xjpushManager = new XJPushManager(this);
            xjpushManager.unregisterLoginedPushService();
        }

//        boolean flag= PushManager.getInstance().unBindAlias(MainActivity.this,   PreferencesUtil.getLoginInfo(MainActivity.this).getEmobId());
//        Log.i("onion","解绑flag："+flag);
        XjApplication.getInstance().logout(new EMCallBack() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshUI();
                        refreshNewBangBiUI();
                    }
                });
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        PreferencesUtil.Logout(MainActivity.this);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                final Dialog dialog = new Dialog(MainActivity.this, R.style.MyDialogStyle);
                dialog.setContentView(R.layout.dialog_conflict);
                TextView tv_cancle = (TextView) dialog.findViewById(R.id.tv_cancle);
                TextView tv_relogin = (TextView) dialog.findViewById(R.id.tv_relogin);
                tv_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        conflictBuilder = null;
                        dialog.dismiss();
                        index = 0;
                        updateUnreadLabel();
                        startActivity(new Intent(MainActivity.this,
                                MainActivity.class));
//                        finish();
                    }
                });
                tv_relogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ProgressDialog pd = new ProgressDialog(MainActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
                        pd.setCanceledOnTouchOutside(false);
                        pd.setCancelable(false);
                        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                progressShow = false;
                            }
                        });
                        progressShow = true;
                        pd.setMessage("正在登录...");
                        if (pd != null && !MainActivity.this.isFinishing())
                            pd.show();
                        //重新获取用户信息
                        // getuser((int) detailBean.getCommunityId(),detailBean.getEmobId());
                        UserUtils.reLoginUser(MainActivity.this, username, password, new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                switch (msg.what) {
                                    case Config.LoginUserComplete:
                                        if (progressShow) pd.dismiss();
                                        dialog.dismiss();
                                        startActivity(new Intent(MainActivity.this,
                                                MainActivity.class));
                                        isConflict = false;

//                                        boolean flag = PushManager.getInstance().bindAlias(MainActivity.this, PreferencesUtil.getLoginInfo(MainActivity.this).getEmobId());
                                        PushManager.getInstance().turnOnPush(MainActivity.this);

                                        if (xjpushManager == null) {
                                            xjpushManager = new XJPushManager(getmContext());
                                        }
                                        xjpushManager.registerLoginedPushService();
                                        break;
                                    case Config.LoginUserFailure:
                                        if (progressShow && !MainActivity.this.isFinishing()) {
                                            pd.dismiss();
                                            Toast.makeText(MainActivity.this, "登录聊天失败", Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    default:
                                        pd.setMessage("正在登录帮帮..");
                                        break;

                                }
                            }
                        });

                       /* UserUtils.loginEMChat(MainActivity.this, username, bean, new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                switch (msg.what) {
                                    case Config.LoginUserComplete:
                                        if (progressShow) pd.dismiss();
                                        dialog.dismiss();
                                        UserUtils.appLogin(MainActivity.this,PushManager.getInstance().getClientid(MainActivity.this), PreferencesUtil.getLoginInfo(MainActivity.this).getUsername());
                                        startActivity(new Intent(MainActivity.this,
                                            MainActivity.class));
                                        isConflict=false;
                                        boolean flag= PushManager.getInstance().bindAlias(MainActivity.this,   PreferencesUtil.getLoginInfo(MainActivity.this).getEmobId());

                                        PushManager.getInstance().turnOnPush(MainActivity.this);
                                        break;
                                    case Config.LoginUserFailure:
                                        if (progressShow && !MainActivity.this.isFinishing()) {
                                            pd.dismiss();
                                            Toast.makeText(MainActivity.this, "登录聊天失败", Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    default:
                                        pd.setMessage("正在登录帮帮..");
                                        break;

                                }
                            }
                        });*/
                    }
                });


                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialog.setCancelable(false);
                dialog.show();


                isConflict = true;
            } catch (Exception e) {
                EMLog.e(TAG,
                        "---------color conflictBuilder error" + e.getMessage());
            }

        }
    }


    interface GetUserService {
        @GET("/api/v1/communities/{communityId}/users/{emobId}")
        void getuser(@Path("communityId") int communityId, @Path("emobId") String emobId, Callback<XJUserInfoBean> cb);


        @GET("/api/v1/communities/{communityId}/labels/random")
        void getCommunityCategoryData(@Path("communityId") long communityId, Callback<CommunityCategoryDataRespBean> cb);

    }

    MyCommunityCategoryDialog myCommunityCategoryDialog = null;

    private void fetchCommunityCategoryData() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        GetUserService isUserExistService = restAdapter.create(GetUserService.class);
        Callback<CommunityCategoryDataRespBean> callback = new Callback<CommunityCategoryDataRespBean>() {
            @Override
            public void success(CommunityCategoryDataRespBean bean, Response response) {
                if (bean != null && "yes".equals(bean.getStatus())) {
                    List<CommunityCategoryDataRespBean.InfoEntity> info = bean.getInfo();
                    if (info != null && info.size() > 0) {

                        myCommunityCategoryDialog = new MyCommunityCategoryDialog(getmContext(), info);
                        myCommunityCategoryDialog.setCanceledOnTouchOutside(true);
                        myCommunityCategoryDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                PreferencesUtil.setFirstOpen(getmContext(), false);
                            }
                        });

//                        WindowManager windowManager = getWindowManager();
//                        Display display = windowManager.getDefaultDisplay();

//                        2015/12/18 start

//                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//                        Window window = myCommunityCategoryDialog.getWindow();
//                        lp.copyFrom(window.getAttributes());
//
//                        lp.width = DensityUtil.dip2px(getmContext(),360f);
//                        lp.height = DensityUtil.dip2px(getmContext(),641f);
//                        window.setAttributes(lp);
//                        2015/12/18 end

//                        WindowManager manager = (WindowManager) getSystemService(Activity.WINDOW_SERVICE);
//                        int width, height;
//
//                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
//                            width = manager.getDefaultDisplay().getWidth();
//                            height = manager.getDefaultDisplay().getHeight();
//                        } else {
//                            Point point = new Point();
//                            manager.getDefaultDisplay().getSize(point);
//                            width = point.x;
//                            height = point.y;
//                        }
//                        lp.width = width;
//                        lp.height = height;


//                        WindowManager.LayoutParams lp = myCommunityCategoryDialog.getWindow().getAttributes();
//                        lp.width = getmContext().getResources().getDisplayMetrics().widthPixels;
//                        myCommunityCategoryDialog.getWindow().setAttributes(lp);

                        myCommunityCategoryDialog.show();
                        homeDialogTimer = new CountDownTimer(DIALOG_SHOW_TIME, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                            }

                            @Override
                            public void onFinish() {
                                myCommunityCategoryDialog.dismiss();
                            }
                        };
                        homeDialogTimer.start();


                    }
                }
                PreferencesUtil.setFirstOpen(getmContext(), false);
            }

            @Override
            public void failure(RetrofitError error) {
                PreferencesUtil.setFirstOpen(getmContext(), false);
            }
        };

        long communityId;
        if (PreferencesUtil.getLogin(this)) {
            communityId = PreferencesUtil.getLoginInfo(this).getCommunityId();
        } else {
            communityId = PreferencesUtil.getCommityId(this);
        }
        isUserExistService.getCommunityCategoryData(communityId, callback);
    }


    CountDownTimer homeDialogTimer = null;


    private void getuser(int communityId, String emobId) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Config.NET_BASE2)
                .build();
        GetUserService isUserExistService = restAdapter.create(GetUserService.class);
        Callback<XJUserInfoBean> callback = new Callback<XJUserInfoBean>() {
            @Override
            public void success(XJUserInfoBean bean, Response response) {
                PreferencesUtil.Logout(MainActivity.this);
                PreferencesUtil.saveLogin(MainActivity.this, bean.getInfo());
                Map<String, User> map = new HashMap<String, User>();
                XjApplication.getInstance().setContactList(map);
            }

            @Override
            public void failure(RetrofitError error) {

                showNetErrorToast();
            }
        };
        isUserExistService.getuser(communityId, emobId, callback);
    }


    interface GetGroupInfoMsgService {
//        @GET("/api/v1/communities/{communityId}/users/{emobId}/activities/messages")
//        void getgroupMsgs(@Path("communityId") int communityId, @Path("emobId") String emobId, @QueryMap Map<String, String> map, Callback<GroupMsgRespBean> cb);
//        @GET("/api/v1/communities/{communityId}/users/{emobId}/activities/messages")

        ////api/v3/activities/messages?emobIdUser={消息接收人的环信ID}&time={上次拉取消息的时间}
        @GET("/api/v3/activities/messages")
        void getgroupMsgs(@QueryMap Map<String, String> map, Callback<CommonRespBean<List<GroupMsgInfoBean>>> cb);
    }

    private void getGroupMsgsCall(int communityId, String emobId) {


        int lastReqGroupInfosTime = PreferencesUtil.getLastReqGroupInfosTime(getmContext());
        if (lastReqGroupInfosTime == 0) {
            lastReqGroupInfosTime = (int) (System.currentTimeMillis() / 1000);
        }
        PreferencesUtil.setLastReqGroupInfosTime(getmContext(), (int) (System.currentTimeMillis() / 1000));
        HashMap<String, String> option = new HashMap<String, String>();
        option.put("time", "" + lastReqGroupInfosTime);
        option.put("emobIdUser", "" + emobId);

        GetGroupInfoMsgService isUserExistService = RetrofitFactory.getInstance().create(getmContext(),option,GetGroupInfoMsgService.class);
        Callback<CommonRespBean<List<GroupMsgInfoBean>>> callback = new Callback<CommonRespBean<List<GroupMsgInfoBean>>>() {
            @Override
            public void success(final CommonRespBean<List<GroupMsgInfoBean>> bean, Response response) {

                if (bean != null && TextUtils.equals("yes", bean.getStatus()) && bean.getData() != null) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            processingGroupMsgDuplicate(bean.getData());
                        }
                    }).start();
                } else {
                    Log.d("processingGroupMsgDuplicate ", "info is null ");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        };

        isUserExistService.getgroupMsgs(option,callback);
    }

    private void processingGroupMsgDuplicate(List<GroupMsgInfoBean> info) {
        Log.d("processingGroupMsgDuplicate ", "process start ! ");
        if (info == null || info.size() <= 0) {
            Log.d("processingGroupMsgDuplicate ", "info is null ");
            return;
        }

        if (EMChatManager.getInstance() != null && PreferencesUtil.getLogin(getmContext())) {
            EMConversation conversation = EMChatManager.getInstance().getConversation(Constant.NEW_FRIENDS_USERNAME);
            List<EMMessage> allMessages = conversation.getAllMessages();

            if (allMessages != null && allMessages.size() > 0) {
                Log.d("processingGroupMsgDuplicate ", "allMessages size is  " + allMessages.size());
                List<EMMessage> load = conversation.loadMoreMsgFromDB(allMessages.get(0).getMsgId(), 20);
                allMessages.addAll(load);
                processingSubMsgDuplicate(info, allMessages);

            } else {
                processingSubMsgDuplicate(info, allMessages);
            }
        }
        Log.d("processingGroupMsgDuplicate ", "process end ! ");

    }

    private void processingSubMsgDuplicate(List<GroupMsgInfoBean> info, List<EMMessage> allMessages) {

        long communityId = PreferencesUtil.getLoginInfo(this).getCommunityId();
        String emobId = PreferencesUtil.getLoginInfo(this).getEmobId();

        for (GroupMsgInfoBean infoBean : info) {
            ////TODO check  如果当前消息不存在
            if (notExists(infoBean, allMessages)) {
                String messageContent = infoBean.getMessageContent();

                String type = infoBean.getType();
                if (TextUtils.equals(type, "apply")) {
                    try {

                        JSONObject obj = new JSONObject();

//                    jsonObject = jsonObject.put("u_userName", userBean.getNickname());
//                    jsonObject = jsonObject.put("u_userAvatar", userBean.getAvatar());
//                    jsonObject = jsonObject.put("n_reason", joinReason);
//                    jsonObject = jsonObject.put("g_groupOwerEmobId", emobGroupOwner);
//                    jsonObject = jsonObject.put("n_messageId", n_messageId);


                        obj.put("u_userName", infoBean.getFromNickname());
                        obj.put("u_userAvatar", infoBean.getFromAvatar());
                        obj.put("n_reason", infoBean.getMessageContent());
                        obj.put("g_groupOwerEmobId", emobId);
                        obj.put("n_messageId", infoBean.getMessageId());

                        obj.put("groupName", infoBean.getGroupName());
                        obj.put("groupId", infoBean.getGroupId());
                        obj.put("applyerId", infoBean.getEmobIdFrom());

                        String joinReason = obj.optString("n_reason");
                        String nickname = obj.getString("u_userName");
                        String userAvatar = obj.getString("u_userAvatar");
                        String g_groupOwerEmobId = obj.getString("g_groupOwerEmobId");
                        String n_messageId = obj.getString("n_messageId");

                        Log.d(TAG, nickname + " 申请加入群聊：" + infoBean.getGroupName() + " groupId " + infoBean.getGroupId() + " reason " + messageContent);

                        //// 对方申请消息
                        GroupUtils.createEMConversationMessage(getmContext(), obj.toString(), n_messageId, InviteMesageStatus.BEAPPLYED);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, e.getMessage());
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            updateUnreadLabel();
                            if (currentTabIndex == 1) {
                                refreshUI();
                            }
                        }
                    });

                } else if (TextUtils.equals(type, "accept")) {


                    userInfoBean = PreferencesUtil.getLoginInfo(getmContext());
                    if (userInfoBean != null) {
                        try {
                            //// 创建msgId
//                                String n_messageId = UUID.randomUUID().toString().replaceAll("-", "");

//                                    String n_messageId = infoBean.getGroupId()+"_"+infoBean.getEmobIdFrom()+"_"+userInfoBean.getEmobId();

                            JSONObject obj = new JSONObject();
                            obj.put("groupName", infoBean.getGroupName());
                            obj.put("groupId", infoBean.getGroupId());
                            obj.put("accepter", infoBean.getEmobIdFrom());
                            Log.d(TAG, infoBean.getGroupName() + " 同意了你的群聊申请" + " groupId " + info);
                            GroupUtils.createEMConversationMessage(getmContext(), obj.toString(), infoBean.getMessageId(), InviteMesageStatus.BEAGREED);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, e.getMessage());
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                updateUnreadLabel();
                                // 刷新ui
                                if (currentTabIndex == 1)
                                    refreshUI();
                            }
                        });
                    }


                } else if (TextUtils.equals(type, "reject")) {

                    userInfoBean = PreferencesUtil.getLoginInfo(getmContext());
                    if (userInfoBean != null) {

                        try {

                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("groupName", infoBean.getGroupName());
                            jsonObject.put("groupId", infoBean.getGroupId());
                            jsonObject.put("decliner", infoBean.getEmobIdFrom());
                            jsonObject.put("n_reason", infoBean.getMessageContent());
                            jsonObject.put("u_userName", infoBean.getFromNickname());
                            jsonObject.put("u_userAvatar", infoBean.getFromAvatar());
                            ////todo 2016/1/7 groupownerid
                            jsonObject.put("g_groupOwerEmobId", infoBean.getEmobIdFrom());
                            jsonObject.put("n_messageId", infoBean.getMessageId());

                            String n_messageId = infoBean.getMessageId();

//                                String n_messageId = jsonObject.getString("n_messageId");

                            Log.d(TAG, infoBean.getGroupName() + " 拒绝加入：" + infoBean.getGroupName() + " groupId " + infoBean.getGroupId() + " reason " + messageContent);

                            GroupUtils.createEMConversationMessage(getmContext(), jsonObject.toString(), n_messageId, InviteMesageStatus.BEREFUSED);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, e.getMessage());
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                updateUnreadLabel();
                                // 刷新ui
                                if (currentTabIndex == 1)
                                    refreshUI();
                            }
                        });
                    }
                }
            }
        }

    }

    private boolean notExists(GroupMsgInfoBean infoBean, List<EMMessage> allMessages) {
        if (allMessages == null || allMessages.size() <= 0) {
            Log.d("processingGroupMsgDuplicate ", "local allMessages is null notExists  :" + infoBean);
            return true;
        }

        for (EMMessage message : allMessages) {
            if (TextUtils.equals(message.getMsgId(), infoBean.getMessageId())) {
                return false;
            }
        }
        Log.d("processingGroupMsgDuplicate ", "notExists  :" + infoBean);
        return true;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getIntent().getBooleanExtra("conflict", false)
                && !isConflictDialogShow)
            showConflictDialog();
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


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Log.i("debbug", "看个角标" + PreferencesUtil.getCrazySalesCount(MainActivity.this));
                if (PreferencesUtil.getCrazySalesCount(MainActivity.this) == 0) {
                    unreadAddressLabel.setVisibility(View.INVISIBLE);
                } else {
                    unreadAddressLabel.setVisibility(View.VISIBLE);
                    unreadAddressLabel.setText("" + PreferencesUtil.getCrazySalesCount(MainActivity.this));
                }

            }
        }
    };

//    String records = null;
//    StringBuilder recordBuilder = null;
//
//    public void getRecords(ContentResolver contentResolver) {
//        // ContentResolver contentResolver = getContentResolver();
//        Cursor cursor = contentResolver.query(
//                Uri.parse("content://browser/bookmarks"), new String[] {
//                        "title", "url", "date" }, "date!=?",
//                new String[] { "null" }, "date desc");
//        while (cursor != null && cursor.moveToNext()) {
//            String url = null;
//            String title = null;
//            String time = null;
//            String date = null;
//
//            recordBuilder = new StringBuilder();
//            title = cursor.getString(cursor.getColumnIndex("title"));
//            url = cursor.getString(cursor.getColumnIndex("url"));
//
//            date = cursor.getString(cursor.getColumnIndex("date"));
//
//            SimpleDateFormat dateFormat = new SimpleDateFormat(
//                    "yyyy-MM-dd hh:mm;ss");
//            Date d = new Date(Long.parseLong(date));
//            time = dateFormat.format(d);
//
//            System.out.println(title + url + time);
//        }
//    }

}
