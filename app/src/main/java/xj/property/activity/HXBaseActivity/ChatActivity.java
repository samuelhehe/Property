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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatDB;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.GroupReomveListener;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.LocationMessageBody;
import com.easemob.chat.NormalFileMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VideoMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.PathUtil;
import com.easemob.util.VoiceRecorder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import xj.property.HXSDKHelper;
import xj.property.R;
import xj.property.XjApplication;
import xj.property.activity.activities.AlbumActivity;
import xj.property.activity.activities.BitmapHelper;
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.adapter.ExpressionAdapter;
import xj.property.adapter.ExpressionPagerAdapter;
import xj.property.adapter.MessageAdapter;
import xj.property.adapter.VoicePlayClickListener;
import xj.property.beans.GroupStatusBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.cache.GroupHeader;
import xj.property.cache.OrderModel;
import xj.property.domain.User;
import xj.property.event.ButtonOnClickEvent;
import xj.property.utils.CommonUtils;
import xj.property.utils.ImageUtils;
import xj.property.utils.SmileUtils;
import xj.property.utils.message.XJContactHelper;
import xj.property.utils.message.XJMessageHelper;
import xj.property.utils.other.Config;
import xj.property.utils.other.GroupUtils;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.ExpandGridView;
import xj.property.widget.PasteEditText;
import xj.property.widget.com.viewpagerindicator.CirclePageIndicator;

/**
 * 聊天页面
 */
public class ChatActivity extends HXBaseActivity implements OnClickListener, EMEventListener {


    private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
    public static final int REQUEST_CODE_CONTEXT_MENU = 3;
    private static final int REQUEST_CODE_MAP = 4;
    public static final int REQUEST_CODE_TEXT = 5;
    public static final int REQUEST_CODE_VOICE = 6;
    public static final int REQUEST_CODE_PICTURE = 7;
    public static final int REQUEST_CODE_LOCATION = 8;
    public static final int REQUEST_CODE_NET_DISK = 9;
    public static final int REQUEST_CODE_FILE = 10;
    public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
    public static final int REQUEST_CODE_PICK_VIDEO = 12;
    public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 13;
    public static final int REQUEST_CODE_VIDEO = 14;
    public static final int REQUEST_CODE_DOWNLOAD_VOICE = 15;
    public static final int REQUEST_CODE_SELECT_USER_CARD = 16;
    public static final int REQUEST_CODE_SEND_USER_CARD = 17;
    public static final int REQUEST_CODE_CAMERA = 18;
    public static final int REQUEST_CODE_LOCAL = 19;
    public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 20;
    public static final int REQUEST_CODE_GROUP_DETAIL = 21;
    public static final int REQUEST_CODE_SELECT_VIDEO = 23;
    public static final int REQUEST_CODE_SELECT_FILE = 24;
    public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;
    //    public static final int REQUEST_CODE_GROUP_DETAIL = 21;
    public static final int RESULT_CODE_COPY = 1;
    public static final int RESULT_CODE_DELETE = 2;
    public static final int RESULT_CODE_FORWARD = 3;
    public static final int RESULT_CODE_OPEN = 4;
    public static final int RESULT_CODE_DWONLOAD = 5;
    public static final int RESULT_CODE_TO_CLOUD = 6;
    public static final int RESULT_CODE_EXIT_GROUP = 7;

    public static final int CHATTYPE_SINGLE = 1;
    public static final int CHATTYPE_GROUP = 2;

    public static final String COPY_IMAGE = "EASEMOBIMG";
    private View recordingContainer;
    private ImageView micImage;
    private TextView recordingHint;
    protected ListView listView;
    protected PasteEditText mEditTextContent;
    private View buttonSetModeKeyboard;
    private View buttonSetModeVoice;
    private View buttonSend;
    private View buttonPressToSpeak;
    // private ViewPager expressionViewpager;
    private LinearLayout emojiIconContainer;
    private LinearLayout btnContainer;
    private ImageView locationImgview;
    private View more;
    private int position;
    private ClipboardManager clipboard;
    private ViewPager expressionViewpager;
    private InputMethodManager manager;
    private List<String> reslist;
    private Drawable[] micImages;
    protected int chatType;
    protected EMConversation conversation;
    private NewMessageBroadcastReceiver receiver;
    public static ChatActivity activityInstance = null;
    // 给谁发送消息
    protected String toChatUsername;
    protected String toChatUserNick;
    protected String toChatUserAvatr;
    private VoiceRecorder voiceRecorder;
    protected TextView tv_ChatName;
    protected MessageAdapter adapter;
    private File cameraFile;
    static int resendPos;

    private GroupListener groupListener;

    private ImageView iv_emoticons_normal;
    private ImageView iv_emoticons_checked;
    private RelativeLayout edittext_layout;
    private ProgressBar loadmorePB;
    private boolean isloading;
    private final int pagesize = 20;
    private boolean haveMoreData = true;
    private Button btnMore;
    protected User toChatUser;
    public String playMsgId;
    private TextView tv_unservicetime;
    private LinearLayout ll_pop;
    private int testFrom;
    private int testTo;
    private String groupName;
    private String communityName;
    private String communityId;

    public String getServantType() {
        return servantType;
    }

    public void setServantType(String servantType) {
        this.servantType = servantType;
    }

    private String servantType; /// 投诉服务类型

    public int getCmdCode() {
        return cmdCode;
    }

    public void setCmdCode(int cmdCode) {
        this.cmdCode = cmdCode;
    }

    /**
     * 命令码
     */
    public  int cmdCode;
    /**
     * 命令具体内容
     */
    protected String cmdDetail;

    private Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 切换msg切换图片
            micImage.setImageDrawable(micImages[msg.what]);
        }
    };
    private EMGroup group;
    private UserInfoDetailBean bean;

    private CirclePageIndicator vpager_indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EMChatManager.getInstance().getChatOptions().setShowNotificationInBackgroud(false);
        if (!forChild()) {
            setContentView(R.layout.activity_chat);
        }
        initView();
        bean = PreferencesUtil.getLoginInfo(this);

        setUpView();
        ///// 投诉消息,投诉内容
        String content = getIntent().getStringExtra(Config.ComplainContent);
        if (!TextUtils.isEmpty(content)) {
            sendFeedBackOrComplainText(content);
        }
//        if (bean != null && !TextUtils.isEmpty(toChatUsername)) {
//            //// 群聊
//            if (chatType != CHATTYPE_SINGLE) {
//                String checkKey = (toChatUsername + bean.getEmobId());
//                boolean isNeedSend = PreferencesUtil.getIsNeedSendFirstEnterGroupMsg(getmContext(), checkKey);
//                if (isNeedSend) {
//                    String sendText = "大家好，我是你们的邻居" + bean.getNickname() + "，很开心认识大家";
//                    sendText(sendText);
//                    PreferencesUtil.setIsNeedSendFirstEnterGroupMsg(getmContext(), checkKey, false);
//                }
//            }
//        }
        if (!getIntent().getBooleanExtra(Config.InServiceTime, true)) {
            ll_pop.setVisibility(View.VISIBLE);
            /// 是否是用户反馈
            if (TextUtils.equals(servantType,Config.SERVANT_TYPE_BANGBANG)) {
                showCallBackPopWindow();
            } else {
                showPopWindow();
            }

            //不在营业时间范围内
//      View v=   findViewById(R.id.remind_top);
//         v.setVisibility(View.VISIBLE);
//    Animation animation= new TranslateAnimation(0,0,0,300);
//         animation.setDuration(1000);
//         animation.setFillAfter(true);
//     v.startAnimation(animation);
        }

    }

    private void showPopWindow() {
        tv_unservicetime.setText("客服的工作时间为" + MainActivity.startTime + "-" + MainActivity.endTime + "，非工作时间无法及时回复请您留言，我们会在次日第一时间与您联系");
        Animation animation = AnimationUtils.loadAnimation(ChatActivity.this, R.anim.push_top_in3);
        animation.setFillAfter(true);
        tv_unservicetime.startAnimation(animation);
    }

    private void showCallBackPopWindow() {
        tv_unservicetime.setText("您有什么问题请留言，我们会尽快回复您");
        Animation animation = AnimationUtils.loadAnimation(ChatActivity.this, R.anim.push_top_in3);
        animation.setFillAfter(true);
        tv_unservicetime.startAnimation(animation);
    }

    private void showPopWindow2() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mMenuView = inflater.inflate(R.layout.unservice_time, null);
        ((TextView) mMenuView.findViewById(R.id.tv_unservicetime)).setText("客服的工作时间为" + MainActivity.startTime + "-" + MainActivity.endTime + "，非工作时间无法及时回复请您留言，我们会在次日第一时间与您联系");
        ((TextView) mMenuView.findViewById(R.id.tv_unservicetime)).setTextSize(getResources().getDimension(R.dimen.tv_unservicetime));
        final PopupWindow popupWindow = new PopupWindow();
        // 设置按钮监听
        // 设置SelectPicPopupWindow的View
        popupWindow.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
//        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        //  popupWindow.setAnimationStyle(R.style.AnimTop);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0.0f, 0, 200f);
        translateAnimation.setDuration(2000);
        translateAnimation.setFillAfter(true);
        ((TextView) mMenuView.findViewById(R.id.tv_unservicetime)).setAnimation(translateAnimation);
        translateAnimation.start();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // ,Animation.RELATIVE_TO_SELF,10f,Animation.RELATIVE_TO_SELF,10f
                popupWindow.showAsDropDown(findViewById(R.id.top_bar));
                //popupWindow.showAtLocation(findViewById(R.id.list),Gravity.NO_GRAVITY,0,0);
            }
        }, 200);

    }


    protected boolean forChild() {

        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EMChatManager.getInstance().unregisterEventListener(this);
        EventBus.getDefault().unregister(this);
        HXSDKHelper sdkHelper = (HXSDKHelper) HXSDKHelper.getInstance();

        // 把此activity 从foreground activity 列表里移除
        sdkHelper.popActivity(this);
        try {
            unregisterReceiver(receiver);
            receiver = null;
        } catch (Exception e) {
        }
        super.onStop();
    }

    // This method will be called when a MessageEvent is posted
    public void onEvent(ButtonOnClickEvent event) {
        if (event.CMD_CODE == Config.HeaderClcikEvent && !bean.getEmobId().equals(event.message)) {//群聊点击了对方头像
            event.view.setClickable(true);
            Intent intent = new Intent(ChatActivity.this, UserGroupInfoActivity.class);
            intent.putExtra(Config.INTENT_PARMAS2, event.message);
            startActivity(intent);
//            UserUtils.callUser(this, event.message);
            return;
        }
        sendTextWithExt("", event.CMD_CODE);
    }


    /**
     * initView
     */
    protected void initView() {
        tv_unservicetime = (TextView) findViewById(R.id.tv_unservicetime);
        ll_pop = (LinearLayout) findViewById(R.id.ll_pop);
        recordingContainer = findViewById(R.id.recording_container);
        micImage = (ImageView) findViewById(R.id.mic_image);
        recordingHint = (TextView) findViewById(R.id.recording_hint);
        listView = (ListView) findViewById(R.id.list);
        mEditTextContent = (PasteEditText) findViewById(R.id.et_sendmessage);
        buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
        edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
        buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);
        buttonSend = findViewById(R.id.btn_send);
        buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
        expressionViewpager = (ViewPager) findViewById(R.id.vPager);

        vpager_indicator = (xj.property.widget.com.viewpagerindicator.CirclePageIndicator) findViewById(R.id.vpager_indicator);

        emojiIconContainer = (LinearLayout) findViewById(R.id.ll_face_container);
        btnContainer = (LinearLayout) findViewById(R.id.ll_btn_container);
        locationImgview = (ImageView) findViewById(R.id.btn_location);
        iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
        iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);
        loadmorePB = (ProgressBar) findViewById(R.id.pb_load_more);
        tv_ChatName = (TextView) findViewById(R.id.name);
        btnMore = (Button) findViewById(R.id.btn_more);
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.INVISIBLE);
        more = findViewById(R.id.more);

        edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);

        // 动画资源文件,用于录制语音时
        micImages = new Drawable[]{getResources().getDrawable(R.drawable.record_animate_01),
                getResources().getDrawable(R.drawable.record_animate_02), getResources().getDrawable(R.drawable.record_animate_03),
                getResources().getDrawable(R.drawable.record_animate_04), getResources().getDrawable(R.drawable.record_animate_05),
                getResources().getDrawable(R.drawable.record_animate_06), getResources().getDrawable(R.drawable.record_animate_07),
                getResources().getDrawable(R.drawable.record_animate_08), getResources().getDrawable(R.drawable.record_animate_09),
                getResources().getDrawable(R.drawable.record_animate_10), getResources().getDrawable(R.drawable.record_animate_11),
                getResources().getDrawable(R.drawable.record_animate_12), getResources().getDrawable(R.drawable.record_animate_13),
                getResources().getDrawable(R.drawable.record_animate_14),};


        // 表情list 目前表情数量为99个
        reslist = getExpressionRes(99);

        // 初始化表情viewpager
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        View gv3 = getGridChildView(3);
        View gv4 = getGridChildView(4);
        View gv5 = getGridChildView(5);

        views.add(gv1);
        views.add(gv2);
        views.add(gv3);
        views.add(gv4);
        views.add(gv5);

        expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
        vpager_indicator.setViewPager(expressionViewpager);

        edittext_layout.requestFocus();
        voiceRecorder = new VoiceRecorder(micImageHandler);
        buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
        mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //login_line  input_bar_bg_active
                if (hasFocus) {
                    edittext_layout.setBackgroundResource(R.drawable.login_line);
                } else {
                    edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
                }

            }
        });
        mEditTextContent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                edittext_layout.setBackgroundResource(R.drawable.login_line);
                more.setVisibility(View.GONE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.GONE);
            }
        });
        // 监听文字框
        mEditTextContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    btnMore.setVisibility(View.GONE);
                    buttonSend.setVisibility(View.VISIBLE);
                } else {
                    btnMore.setVisibility(View.VISIBLE);
                    buttonSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private void setUpView() {
        activityInstance = this;
        iv_emoticons_normal.setOnClickListener(this);
        iv_emoticons_checked.setOnClickListener(this);
        // position = getIntent().getIntExtra("position", -1);
        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        wakeLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
        // 判断单聊还是群聊
        chatType = getIntent().getIntExtra("chatType", CHATTYPE_SINGLE);

        cmdCode = getIntent().getIntExtra(Config.EXPKey_CMD_CODE, 0);

        servantType = getIntent().getStringExtra(Config.SERVANT_TYPE);//帮帮反馈 ///物业客服// 维修投诉客服 // 快店投诉客服 /// 店家客服 帮帮serverapp中使用...

        if (TextUtils.equals(servantType,Config.SERVANT_TYPE_BANGBANG)||
                TextUtils.equals(servantType,Config.SERVANT_TYPE_WEIXIUTOUSU) ||
                TextUtils.equals(servantType,Config.SERVANT_TYPE_SHOPTOUSU)||
                TextUtils.equals(servantType,Config.SERVANT_TYPE_WUYE)) {

            locationImgview.setVisibility(View.GONE);
            findViewById(R.id.btn_video).setVisibility(View.GONE);
            findViewById(R.id.tv_location).setVisibility(View.GONE);
            findViewById(R.id.tv_video).setVisibility(View.GONE);

        }
        cmdDetail = getIntent().getStringExtra("CMD_DETAIL");

        if (chatType == CHATTYPE_SINGLE) { // 单聊

            toChatUsername = getIntent().getStringExtra("userId");
            toChatUserNick = getIntent().getStringExtra(Config.EXPKey_nickname);
            toChatUserAvatr = getIntent().getStringExtra(Config.EXPKey_avatar);

            if (toChatUserNick == null) {
                onError();
                return;
            }
            tv_ChatName.setText(toChatUserNick);
            findViewById(R.id.container_remove).setVisibility(View.GONE);
            toChatUser = XJContactHelper.selectContact(toChatUsername);
//            conversation =
//            EMChatManager.getInstance().getConversation(toChatUsername,false);

            testFrom = PreferencesUtil.getUserInfoTest(getmContext());
            testTo = PreferencesUtil.getGroupUserInfoTest(getmContext(),toChatUsername);

            Log.d("ChatActivity: ","testFrom  "+ testFrom + "testTo "+ testTo );

        } else {

            // 群聊 活动第一次进入应用发送一条消息////TODO 创建一条群消息 大家好，我是你们的邻居XXX，很开心认识大家
            findViewById(R.id.container_to_group).setVisibility(View.VISIBLE);
            findViewById(R.id.container_remove).setVisibility(View.GONE);
            findViewById(R.id.container_voice_call).setVisibility(View.GONE);
            toChatUsername = getIntent().getStringExtra("groupId");
            //get group from local
            group = EMGroupManager.getInstance().getGroup(toChatUsername);
            getGroupInfo();
            if (group == null) {


                fetchGroupInfoFromServer();

            }
            testFrom = PreferencesUtil.getUserInfoTest(getmContext());

            groupName =group.getGroupName();

            communityName = PreferencesUtil.getCommityName(getmContext());

            communityId = ""+bean.getCommunityId();

            Log.d("ChatActivity: "," testFrom  "+ testFrom + " groupName "+ groupName + " communityName  "+ communityName+ " communityId "+ communityId);

            //join group
           /* try {
                EMGroupManager.getInstance().joinGroup(toChatUsername);
            } catch (EaseMobException e) {
                e.printStackTrace();
            }*/
//            String groupName = getIntent().getStringExtra(Config.EXPKey_GROUP);
//            ((TextView) findViewById(R.id.name)).setText(groupName != null ? groupName : group.getGroupName());
            // conversation =
            // EMChatManager.getInstance().getConversation(toChatUsername,true);
        }

        conversation = EMChatManager.getInstance().getConversation(toChatUsername);
        // 把此会话的未读数置为0
//        conversation.resetUnreadMsgCount();
        conversation.markAllMessagesAsRead();

        Log.d("ChatActivity", "markAllMessagesAsRead is complete toChatUsername "+ toChatUsername+ " toChatUserNick "+toChatUserNick);

        // 初始化db时，每个conversation加载数目是getChatOptions().getNumberOfMessagesLoaded
        // 这个数目如果比用户期望进入会话界面时显示的个数不一样，就多加载一些
        final List<EMMessage> msgs = conversation.getAllMessages();
        int msgCount = msgs != null ? msgs.size() : 0;
        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
            String msgId = null;
            if (msgs != null && msgs.size() > 0) {
                msgId = msgs.get(0).getMsgId();
            }
            if (chatType == CHATTYPE_SINGLE) {
                conversation.loadMoreMsgFromDB(msgId, pagesize);
            } else {
                conversation.loadMoreGroupMsgFromDB(msgId, pagesize);
            }
        }
        adapter = createMessageAdapter(this, toChatUsername, chatType);
        // 显示消息
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new ListScrollListener());
        adapter.refreshSelectLast();
        int count = listView.getCount();
        if (count > 0) {
            listView.setSelection(count - 1);
        }

        listView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                more.setVisibility(View.GONE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.GONE);
                return false;
            }
        });


        // 注册一个ack回执消息的BroadcastReceiver
       /* IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager.getInstance().getAckMessageBroadcastAction());
        ackMessageIntentFilter.setPriority(5);
        registerReceiver(ackMessageReceiver, ackMessageIntentFilter);

        // 注册一个消息送达的BroadcastReceiver
        IntentFilter deliveryAckMessageIntentFilter = new IntentFilter(EMChatManager.getInstance().getDeliveryAckMessageBroadcastAction());
        deliveryAckMessageIntentFilter.setPriority(5);
        registerReceiver(deliveryAckMessageReceiver, deliveryAckMessageIntentFilter);*/

        // 监听当前会话的群聊解散被T事件
        groupListener = new GroupListener();
        EMGroupManager.getInstance().addGroupChangeListener(groupListener);

        // show forward message if the message is not null
        String forward_msg_id = getIntent().getStringExtra("forward_msg_id");
        if (forward_msg_id != null) {
            // 显示发送要转发的消息
            forwardMessage(forward_msg_id);
        }
        //标明是携带命令的消息，用sendTextWithExt方法发消息
//        if (cmdCode != 0) {
//            logger.info("cmd detail is :" + cmdDetail);
//            sendTextWithExt("txt with ext", 200);
//        }


    }

    private void fetchGroupInfoFromServer() {

        XjApplication.getInstance().pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    group = EMGroupManager.getInstance().getGroupFromServer(toChatUsername);
                    //保存获取下来的群聊信息
                    EMGroupManager.getInstance().createOrUpdateLocalGroup(group);
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }

                if (group == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onError();
                        }
                    });
                } else {
                    GroupUtils.getEaGroupInfo(toChatUsername, new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            if (msg.what == Config.TASKCOMPLETE) {
//                        GroupInfo groupInfo=    new Select().from(GroupInfo.class).where("group_id = ?",group.getGroupId()).executeSingle();
//                        if(groupInfo!=null)
                                group.setGroupName(msg.obj.toString());
                                tv_ChatName.setText(msg.obj.toString());
                                groupName =group.getGroupName();

                            }
                        }
                    });
                }

            }
        });

    }

    //联系人异常
    private void onError() {
        if (toChatUsername != null) {
            EMChatManager.getInstance().deleteConversation(toChatUsername);
        }
        finish();
    }

    interface GruopInfoService {
        ///api/v1/communities/{communityId}/emobGroup/{emobGroupId}
        @GET("/api/v1/communities/{communityId}/emobGroup/{emobGroupId}")
        void getGroupInfo(@Path("communityId") long communityId, @Path("emobGroupId") String emobGroupId, Callback<GroupStatusBean> cb);
    }

    private void getGroupInfo() {
        if (toChatUsername == null) return;
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        GruopInfoService service = restAdapter.create(GruopInfoService.class);
        Callback<GroupStatusBean> callback = new Callback<GroupStatusBean>() {
            @Override
            public void success(GroupStatusBean bean, retrofit.client.Response response) {
                Log.i("onion", "此群状态" + bean.getStatus() + bean.getInfo());
                if ("deleted".equals(bean.getInfo())) {
                    EMChatManager.getInstance().deleteConversation(toChatUsername);
                    Toast.makeText(ChatActivity.this, "本群已经不存在", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();

                Log.i("onion", "判断失败");
            }
        };
        service.getGroupInfo(PreferencesUtil.getCommityId(this), toChatUsername, callback);
    }

    protected MessageAdapter createMessageAdapter(ChatActivity chatActivity, String toChatUsername, int chatType) {
        return new MessageAdapter(this, toChatUsername, chatType);
    }


    /**
     * onActivityResult
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CODE_EXIT_GROUP) {
            setResult(RESULT_OK);
            finish();
            return;
        }
        if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
            switch (resultCode) {
                case RESULT_CODE_COPY: // 复制消息
                    EMMessage copyMsg = ((EMMessage) adapter.getItem(data.getIntExtra("position", -1)));
                    // clipboard.setText(SmileUtils.getSmiledText(ChatActivity.this,
                    // ((TextMessageBody) copyMsg.getBody()).getMessage()));
                    clipboard.setText(((TextMessageBody) copyMsg.getBody()).getMessage());
                    break;
                case RESULT_CODE_DELETE: // 删除消息
                    EMMessage deleteMsg = (EMMessage) adapter.getItem(data.getIntExtra("position", -1));
                    conversation.removeMessage(deleteMsg.getMsgId());
                    adapter.refresh();
                    listView.setSelection(data.getIntExtra("position", adapter.getCount()) - 1);
                    break;

                case RESULT_CODE_FORWARD: // 转发消息
                    EMMessage forwardMsg = (EMMessage) adapter.getItem(data.getIntExtra("position", 0));
                    Intent intent = new Intent(this, ForwardMessageActivity.class);
                    intent.putExtra("forward_msg_id", forwardMsg.getMsgId());
                    startActivity(intent);

                    break;

                default:
                    break;
            }
        }
        if (resultCode == RESULT_OK) { // 清空消息
            if (requestCode == REQUEST_CODE_EMPTY_HISTORY) {
                // 清空会话
                EMChatManager.getInstance().clearConversation(toChatUsername);
                clearDB();
                adapter.refresh();
            } else if (requestCode == REQUEST_CODE_CAMERA) { // 发送照片
                if (cameraFile != null && cameraFile.exists())
                    sendPicture(cameraFile.getAbsolutePath());
            } else if (requestCode == REQUEST_CODE_SELECT_VIDEO) { // 发送本地选择的视频

                int duration = data.getIntExtra("dur", 0);
                String videoPath = data.getStringExtra("path");
                File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
                Bitmap bitmap = null;
                FileOutputStream fos = null;
                try {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                    if (bitmap == null) {
                        EMLog.d("chatactivity", "problem load video thumbnail bitmap,use default icon");
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_panel_video_icon);
                    }
                    fos = new FileOutputStream(file);

                    bitmap.compress(CompressFormat.JPEG, 100, fos);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        fos = null;
                    }
                    if (bitmap != null) {
                        bitmap.recycle();
                        bitmap = null;
                    }

                }
                sendVideo(videoPath, file.getAbsolutePath(), duration / 1000);

            } else if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
                int bitmapChatSize = BitmapHelper.bitmapChatListStorage.size();


                if (bitmapChatSize > 0) {
                    for (int i = 0; i < bitmapChatSize; i++) {
                        Uri selectedImage = Uri.parse(BitmapHelper.bitmapChatListStorage.get(i));
                        if (selectedImage != null) {
                            sendPicByUri(selectedImage);
                        }
                    }
                    BitmapHelper.bitmapChatListStorage.clear();
                }
//                if (data != null) {
//                    Uri selectedImage = data.getData();
//                    if (selectedImage != null) {
//                        sendPicByUri(selectedImage);
//                    }
//                }
            } else if (requestCode == REQUEST_CODE_SELECT_FILE) { // 发送选择的文件
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        sendFile(uri);
                    }
                }

            } else if (requestCode == REQUEST_CODE_MAP) { // 地图
                double latitude = data.getDoubleExtra("latitude", 0);
                double longitude = data.getDoubleExtra("longitude", 0);
                String locationAddress = data.getStringExtra("address");
                if (locationAddress != null && !locationAddress.equals("")) {
                    more(more);
                    sendLocationMsg(latitude, longitude, "", locationAddress);
                } else {
                    showToast("无法获取到您的位置信息");
                }
                // 重发消息
            } else if (requestCode == REQUEST_CODE_TEXT || requestCode == REQUEST_CODE_VOICE
                    || requestCode == REQUEST_CODE_PICTURE || requestCode == REQUEST_CODE_LOCATION
                    || requestCode == REQUEST_CODE_VIDEO || requestCode == REQUEST_CODE_FILE) {
                resendMessage();
            } else if (requestCode == REQUEST_CODE_COPY_AND_PASTE) {
                // 粘贴
                if (!TextUtils.isEmpty(clipboard.getText())) {
                    String pasteText = clipboard.getText().toString();
                    if (pasteText.startsWith(COPY_IMAGE)) {

                        if (TextUtils.equals(servantType,Config.SERVANT_TYPE_BANGBANG)||
                                TextUtils.equals(servantType,Config.SERVANT_TYPE_WEIXIUTOUSU) ||
                                TextUtils.equals(servantType,Config.SERVANT_TYPE_SHOPTOUSU)||
                                TextUtils.equals(servantType,Config.SERVANT_TYPE_WUYE)) {

                            sendPictureServantOrComplain(pasteText.replace(COPY_IMAGE, ""));
                        }else{
                            // 把图片前缀去掉，还原成正常的path
                            sendPicture(pasteText.replace(COPY_IMAGE, ""));

                        }


                    }

                }
            } else if (requestCode == REQUEST_CODE_ADD_TO_BLACKLIST) { // 移入黑名单
                EMMessage deleteMsg = (EMMessage) adapter.getItem(data.getIntExtra("position", -1));
                addUserToBlacklist(deleteMsg.getFrom());
            } else if (conversation.getMsgCount() > 0) {
                adapter.refresh();
                setResult(RESULT_OK);
                //// 从群组请求回来, 另一种可能是,删除群组处理
            } else if (requestCode == REQUEST_CODE_GROUP_DETAIL) {
                if (data != null) {
                    String destoryflag = data.getStringExtra(Config.EXPKey_GROUP_DESTORY);
                    if (TextUtils.equals(destoryflag, Config.EXPKey_GROUP_DESTORY)) {
                        if (conversation != null) {
                            conversation.clear();
                            ////TODO   删除当前会话
//                            EMChatManager.getInstance().deleteConversation(conversation);
                        }
                        showToast("退出聊天");
                        finish();
                    }
                    group.setGroupName(data.getStringExtra(Config.EXPKey_GROUP));
                }
                adapter.refresh();
            }
        }
    }

    protected void clearDB(String serial) {
        new Delete().from(OrderModel.class).where("serial = ?", serial).execute();
    }

    protected void clearDB() {
        new Delete().from(OrderModel.class).execute();
    }

    /**
     * 事件监听
     * <p/>
     * see {@link EMNotifierEvent}
     */
    @Override
    public void onEvent(EMNotifierEvent event) {
        switch (event.getEvent()) {
            case EventNewMessage: {
                //获取到message
                EMMessage message = (EMMessage) event.getData();

                String username = null;
                Log.i("debbug", "   getOrderModel " + XJMessageHelper.getOrderModel(message.getStringAttribute(Config.EXPKey_serial, ""), 201));
// getOrderModel OrderModel{msg_id='129740808470397384', serial='1511172300278188', cmd_code='201'}

                if (message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) != 2072) {
                    if (XJMessageHelper.getOrderModel(message.getStringAttribute(Config.EXPKey_serial, ""), 201) != null ||
                            XJMessageHelper.getOrderModel(message.getStringAttribute("welfareId", ""), 601) != null ||
                            XJMessageHelper.getOrderModel(message.getStringAttribute("welfareId", ""), 602) != null) {
                        EMChatDB.getInstance().deleteMessage(message.getMsgId());
                        Log.i("debbug", " onEvent  已经有message了");
                        conversation.removeMessage(message.getMsgId());
                        return;
                    }
                }
                //群组消息
                if (message.getChatType() == ChatType.GroupChat || message.getChatType() == ChatType.ChatRoom) {
                    username = message.getTo();
                    if (message.getChatType() == ChatType.GroupChat) {
                        GroupHeader header = new Select().from(GroupHeader.class).where("group_id = ?", username).executeSingle();
                        if (header == null || header.getNum() < 10)
                            GroupUtils.getGroupInfo(message.getTo());
                        XJContactHelper.saveContact(message);
                    }
                } else {
                    //单聊消息
                    username = message.getFrom();
                }
                if (message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) != 0) {
                    if (!XJMessageHelper.operatNewMessage(getmContext(),message)) {
                        XJContactHelper.saveContact(message);
                    }
                }

                //如果是当前会话的消息，刷新聊天页面
                if (username.equals(getToChatUsername())) {

                    refreshUIWithNewMessage();
                    //声音和震动提示有新消息
                    HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(message);

                } else {
                    //如果消息不是和当前聊天ID的消息
                    if (message.getChatType() == ChatType.Chat || !PreferencesUtil.getUnNotifyGroupS(XjApplication.getInstance()).contains(message.getTo()))
                        HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
                }

                break;
            }
            case EventDeliveryAck: {
                //获取到message
                EMMessage message = (EMMessage) event.getData();
//                Log.i("onion","chatactivity 收到送达监听");
//                if(message.getIntAttribute(Config.EXPKey_CMD_CODE,0)!=0&&!message.getStringAttribute(Config.EXPKey_serial,"").isEmpty()){
//                    XJMessageHelper.saveMessage2DB(message.getMsgId(),message.getStringAttribute(Config.EXPKey_serial,""),message.getIntAttribute(Config.EXPKey_CMD_CODE,0));
//                }
                refreshUI();
                break;
            }
            case EventReadAck: {
                //获取到message
                EMMessage message = (EMMessage) event.getData();
                refreshUI();
                break;
            }
            case EventOfflineMessage: {
                //a list of offline messages
                //List<EMMessage> offlineMessages = (List<EMMessage>) event.getData();
                refreshUI();
                break;
            }
            default:
                break;
        }

    }

    protected void refreshUI() {
        if (adapter == null) {
            return;
        }

        runOnUiThread(new Runnable() {
            public void run() {
                adapter.refresh();
            }
        });
    }

    protected void refreshUIWithNewMessage() {
        if (adapter == null) {
            return;
        }

        runOnUiThread(new Runnable() {
            public void run() {
                adapter.refreshSelectLast();
            }
        });
    }

    /**
     * 消息图标点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.btn_send) {// 点击发送按钮(发文字和表情)
            String s = mEditTextContent.getText().toString();
//            if (cmdCode != 0) {
//                sendTextWithExt(s, 0);
//            } else {
//            sendText(s);
//            }

            if (    TextUtils.equals(servantType,Config.SERVANT_TYPE_BANGBANG)||
                    TextUtils.equals(servantType,Config.SERVANT_TYPE_WEIXIUTOUSU) ||
                    TextUtils.equals(servantType,Config.SERVANT_TYPE_SHOPTOUSU)||
                    TextUtils.equals(servantType,Config.SERVANT_TYPE_WUYE)) {
                sendFeedBackOrComplainText(s);
            } else {
                sendText(s);
            }

            ////TODO 发送文本消息
        } else if (id == R.id.btn_take_picture) {
            selectPicFromCamera();// 点击照相图标
        } else if (id == R.id.btn_picture) {
            selectPicFromLocal(); // 点击图片图标
        } else if (id == R.id.btn_location) { // 位置
            startActivityForResult(new Intent(this, BaiduMapActivity.class), REQUEST_CODE_MAP);
        } else if (id == R.id.iv_emoticons_normal) { // 点击显示表情框
            more.setVisibility(View.VISIBLE);
            iv_emoticons_normal.setVisibility(View.INVISIBLE);
            iv_emoticons_checked.setVisibility(View.VISIBLE);
            btnContainer.setVisibility(View.GONE);
            emojiIconContainer.setVisibility(View.VISIBLE);
            hideKeyboard();
        } else if (id == R.id.iv_emoticons_checked) { // 点击隐藏表情框
            iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.INVISIBLE);
            btnContainer.setVisibility(View.VISIBLE);
            emojiIconContainer.setVisibility(View.GONE);
            more.setVisibility(View.GONE);

        } else if (id == R.id.btn_video) {
            // 点击摄像图标
            Intent intent = new Intent(ChatActivity.this, ImageGridActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
        } else if (id == R.id.btn_file) { // 点击文件图标
            selectFileFromLocal();
        } else if (id == R.id.btn_voice_call) { // 点击语音电话图标
            if (!EMChatManager.getInstance().isConnected())
                Toast.makeText(this, "尚未连接至服务器，请稍后重试", 0).show();
            else
                startActivity(new Intent(ChatActivity.this, VoiceCallActivity.class).putExtra("username", toChatUsername).putExtra(
                        "isComingCall", false));
        }
    }

    /**
     * 照相获取图片
     */
    public void selectPicFromCamera() {
        if (!CommonUtils.isExitsSdcard()) {
            Toast.makeText(getApplicationContext(), "SD卡不存在，不能拍照", 0).show();
            return;
        }
        cameraFile = new File(PathUtil.getInstance().getImagePath(), XjApplication.getInstance().getUserName()
                + System.currentTimeMillis() + ".jpg");
        cameraFile.getParentFile().mkdirs();
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
                REQUEST_CODE_CAMERA);
    }

    /**
     * 选择文件
     */
    private void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }

    /**
     * 从图库获取图片
     */
    public void selectPicFromLocal() {
        Intent intent = new Intent(this, AlbumActivity.class);
//        if (Build.VERSION.SDK_INT < 19) {
//            intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("image/*");
//
//        } else {
//            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        }
        intent.putExtra(Config.INTENT_PARMAS1, Config.SelectAblum);
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }

    /**
     * 发送文本消息
     *
     * @param content text to send
     */
    protected void sendText(String content) {
        if (content.length() > 0) {
            EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
            // 如果是群聊，设置chattype,默认是单聊
            if (chatType == CHATTYPE_GROUP){
                message.setChatType(ChatType.GroupChat);
                message.setAttribute(Config.EXPKey_testFrom, testFrom);
                message.setAttribute(Config.EXPKey_groupName, groupName);
                message.setAttribute(Config.EXPKey_communityName,communityName);
                message.setAttribute(Config.EXPKey_communityId,communityId);
            }else{
                message.setAttribute(Config.EXPKey_testFrom, testFrom);
                message.setAttribute(Config.EXPKey_testTo,testTo);
            }
            TextMessageBody txtBody = new TextMessageBody(content);
            // 设置消息body
            message.addBody(txtBody);
            // 设置要发给谁,用户username或者群聊groupid
            message.setReceipt(toChatUsername);
            message.setAttribute(Config.EXPKey_nickname, bean.getNickname());
            message.setAttribute(Config.EXPKey_avatar, bean.getAvatar());
            message.setAttribute(Config.EXPKey_username, bean.getUsername());

            if (chatType == CHATTYPE_SINGLE && toChatUser != null && (toChatUser.sort.equals("2") || toChatUser.sort.equals("5"))){
                message.setAttribute(Config.EXPKey_ADDRESS, bean.getUserFloor() + bean.getUserUnit() + bean.getRoom());
            }
            if (cmdCode != 0) {
                message.setAttribute(Config.EXPKey_CMD_CODE, cmdCode);
                message.setAttribute("content", content);
            }// 把messgage加到conversation中
            conversation.addMessage(message);
            // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
            adapter.refresh();
            listView.setSelection(listView.getCount() - 1);
            mEditTextContent.setText("");

            setResult(RESULT_OK);

        }
    }


    /**
     * 发送反馈/投诉/物业客服
     * <p/>
     * <p/>
     * <p/>
     * module
     * nickname
     * avatar
     * username
     * communityId
     * communityName
     * gender
     * room
     * userUnit
     * userFloor
     *
     * @param content text to send
     */
    protected void sendFeedBackOrComplainText(String content) {
        if (content.length() > 0) {
            EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
            // 如果是群聊，设置chattype,默认是单聊
            if (chatType == CHATTYPE_GROUP){
                message.setChatType(ChatType.GroupChat);
                message.setAttribute(Config.EXPKey_testFrom,testFrom);
                message.setAttribute(Config.EXPKey_groupName,groupName);
                message.setAttribute(Config.EXPKey_communityName,communityName);
                message.setAttribute(Config.EXPKey_communityId,communityId);
            }else{
                message.setAttribute(Config.EXPKey_testFrom, testFrom);
                message.setAttribute(Config.EXPKey_testTo,testTo);
            }
            TextMessageBody txtBody = new TextMessageBody(content);
            // 设置消息body
            message.addBody(txtBody);
            // 设置要发给谁,用户username或者群聊groupid
            message.setReceipt(toChatUsername);
            message.setAttribute(Config.EXPKey_nickname, bean.getNickname());
            message.setAttribute(Config.EXPKey_avatar, bean.getAvatar());

            /**
             * module
             nickname
             avatar
             username
             communityId
             communityName
             gender
             room
             userUnit
             userFloor
             module有三个值：bangbang,wuye,shop，帮帮是用户反馈模块，wuye是物业客服模块，shop是店家客服模块, tousu , 投诉
             *
             */
            message.setAttribute(Config.EXPKey_username, bean.getUsername());
            message.setAttribute(Config.EXPKey_communityId, "" + bean.getCommunityId());
            message.setAttribute(Config.EXPKey_communityName, "" + PreferencesUtil.getCommityName(this));
            message.setAttribute(Config.EXPKey_gender, "" + bean.getGender());
            message.setAttribute(Config.EXPKey_room, "" + bean.getRoom());
            message.setAttribute(Config.EXPKey_userunit, "" + bean.getUserUnit());
            message.setAttribute(Config.EXPKey_userFloor, "" + bean.getUserFloor());

            if (TextUtils.equals(servantType,Config.SERVANT_TYPE_BANGBANG)||
                    TextUtils.equals(servantType,Config.SERVANT_TYPE_WEIXIUTOUSU) ||
                    TextUtils.equals(servantType,Config.SERVANT_TYPE_SHOPTOUSU)||
                    TextUtils.equals(servantType,Config.SERVANT_TYPE_WUYE)) {

                message.setAttribute(Config.EXPKey_module, servantType);
            }


//            if (cmdCode == 401) {
//
////            message.setAttribute(Config.EXPKey_module, "wuye");
////            message.setAttribute(Config.EXPKey_module, "shop");
//
//            } else if (cmdCode == 403) {
//                //// 投诉
//                message.setAttribute(Config.EXPKey_module, "tousu");
//            } else if (cmdCode == 404) {
//                //// 用户反馈
//                message.setAttribute(Config.EXPKey_module, "bangbang");
//            }

            if (cmdCode != 0) {
                message.setAttribute(Config.EXPKey_CMD_CODE, cmdCode);
                message.setAttribute("content", content);
            }
            // 把messgage加到conversation中
            conversation.addMessage(message);
            // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
            adapter.refresh();
            listView.setSelection(listView.getCount() - 1);
            mEditTextContent.setText("");
            setResult(RESULT_OK);

        }
    }

    /**
     * send txt with ext
     *
     * @param content  text
     * @param CMD_CODE CMD CODE
     */
    protected void sendTextWithExt(String content, int CMD_CODE) {
        switch (CMD_CODE) {
//            case 200:
//                if (content.length() > 0) {
//                    EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
//                    // 如果是群聊，设置chattype,默认是单聊
//                    if (chatType == CHATTYPE_GROUP)
//                        message.setChatType(ChatType.GroupChat);
//                    TextMessageBody txtBody = new TextMessageBody(content);
//                    // 设置消息body
//                    message.addBody(txtBody);
//                    // 添加ext属性
//                    message.setAttribute("avatar", "http://ltzmaxwell.qiniudn.com/FpGFUH2SvSDU1MYqocv8okqwxaU2");
//                    message.setAttribute("nickname", "kimi");
//                    message.setAttribute("CMD_CODE", CMD_CODE);
//                    message.setAttribute("CMD_DETAIL", cmdDetail);
//                    message.setAttribute("serial", "12345");
//                    message.setAttribute("clickable", 1);
//                    message.setAttribute("isShowAvatar", 1);
//                    message.setAttribute("msgId", message.getMsgId());
//                    logger.info("message id before add to conversation is :" + message.getMsgId());
//                    //to username or groupid
//                    message.setReceipt(toChatUsername);
//
//                    //add message to conversation
//                    conversation.addMessage(message);
//                    logger.info("message id after add to conversation is :" + message.getMsgId());
//
//                    // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
//                    adapter.refresh();
//                    listView.setSelection(listView.getCount() - 1);
//                    mEditTextContent.setText("");
//
//                    setResult(RESULT_OK);
//                }
//                break;
//
//            case 203:
//                EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
//                // 如果是群聊，设置chattype,默认是单聊
//                if (chatType == CHATTYPE_GROUP)
//                    message.setChatType(ChatType.GroupChat);
//                TextMessageBody txtBody = new TextMessageBody(content);
//                // 设置消息body
//                message.addBody(txtBody);
//
//                //get cmd detail
//                cmdDetail = MessageExtBuilder.buildMessageExt(203);
//                logger.info("cmd detail of 203 is :" + cmdDetail);
//                //add ext
//                message.setAttribute("avatar", "http://ltzmaxwell.qiniudn.com/FpGFUH2SvSDU1MYqocv8okqwxaU2");
//                message.setAttribute("nickname", "kimi");
//                message.setAttribute("CMD_CODE", CMD_CODE);
//                message.setAttribute("CMD_DETAIL", cmdDetail);
//                message.setAttribute("clickable", 1);
//                message.setAttribute("isShowAvatar", 0);
//                message.setAttribute("serial", "12345");
//                message.setAttribute("msgId", message.getMsgId());
//                //to whom ,username or groupid
//                message.setReceipt(toChatUsername);
//                //add messgage to conversation
//                conversation.addMessage(message);
//                // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
//                adapter.refresh();
//                listView.setSelection(listView.getCount() - 1);
//                mEditTextContent.setText("");
//
//                setResult(RESULT_OK);
//
//                break;
//
//            case 204:
//                logger.info("send a 204 message");
//                message = EMMessage.createSendMessage(EMMessage.Type.TXT);
//                // 如果是群聊，设置chattype,默认是单聊
//                if (chatType == CHATTYPE_GROUP)
//                    message.setChatType(ChatType.GroupChat);
//                txtBody = new TextMessageBody(content);
//                // 设置消息body
//                message.addBody(txtBody);
//
//                // 添加ext属性
//                message.setAttribute("avatar", "http://ltzmaxwell.qiniudn.com/FpGFUH2SvSDU1MYqocv8okqwxaU2");
//                message.setAttribute("nickname", "kimi");
//                message.setAttribute("CMD_CODE", CMD_CODE);
//                message.setAttribute("CMD_DETAIL", cmdDetail);
//                message.setAttribute("clickable", 1);
//                message.setAttribute("isShowAvatar", 0);
//                message.setAttribute("serial", "12345");
//                message.setAttribute("msgId", message.getMsgId());
//                // 设置要发给谁,用户username或者群聊groupid
//                message.setReceipt(toChatUsername);
//                // 把messgage加到conversation中
//                conversation.addMessage(message);
//                // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
//                adapter.refresh();
//                listView.setSelection(listView.getCount() - 1);
//                mEditTextContent.setText("");
//                setResult(RESULT_OK);
//                break;
//            case 205:
//                logger.info("send a 205 message");
//                message = EMMessage.createSendMessage(EMMessage.Type.TXT);
//                // 如果是群聊，设置chattype,默认是单聊
//                if (chatType == CHATTYPE_GROUP)
//                    message.setChatType(ChatType.GroupChat);
//                txtBody = new TextMessageBody(content);
//                // 设置消息body
//                message.addBody(txtBody);
//
//                // 添加ext属性
//                message.setAttribute("avatar", "http://ltzmaxwell.qiniudn.com/FpGFUH2SvSDU1MYqocv8okqwxaU2");
//                message.setAttribute("nickname", "kimi");
//                message.setAttribute("CMD_CODE", CMD_CODE);
//                message.setAttribute("CMD_DETAIL", cmdDetail);
//                message.setAttribute("clickable", 0);
//                message.setAttribute("isShowAvatar", 0);
//                message.setAttribute("serial", "123");
//                message.setAttribute("msgId", message.getMsgId());
//
//                // to username or groupid
//                message.setReceipt(toChatUsername);
//
//                //add message to conversation
//                conversation.addMessage(message);
//                // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
//                adapter.refresh();
//                listView.setSelection(listView.getCount() - 1);
//                mEditTextContent.setText("");
//                setResult(RESULT_OK);
//                break;
//            default:
//                break;
        }
    }

    /**
     * 发送语音
     *
     * @param filePath
     * @param fileName
     * @param length
     * @param isResend
     */
    private void sendVoiceServantOrComplain(String filePath, String fileName, String length, boolean isResend) {
        if (!(new File(filePath).exists())) {
            return;
        }
        try {
            final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.VOICE);
            // 如果是群聊，设置chattype,默认是单聊
            if (chatType == CHATTYPE_GROUP){
                message.setChatType(ChatType.GroupChat);
                message.setAttribute(Config.EXPKey_testFrom,testFrom);
                message.setAttribute(Config.EXPKey_groupName,groupName);
                message.setAttribute(Config.EXPKey_communityName,communityName);
                message.setAttribute(Config.EXPKey_communityId,communityId);
            }else{
                message.setAttribute(Config.EXPKey_testFrom, testFrom);
                message.setAttribute(Config.EXPKey_testTo,testTo);
            }
            message.setReceipt(toChatUsername);
            int len = Integer.parseInt(length);
            VoiceMessageBody body = new VoiceMessageBody(new File(filePath), len);
            message.addBody(body);
            message.setAttribute(Config.EXPKey_nickname, bean.getNickname());
            message.setAttribute(Config.EXPKey_avatar, bean.getAvatar());

            /**
             * module
             nickname
             avatar
             username
             communityId
             communityName
             gender
             room
             userUnit
             userFloor
             module有三个值：bangbang,wuye,shop，帮帮是用户反馈模块，wuye是物业客服模块，shop是店家客服模块, tousu , 投诉
             *
             */
            message.setAttribute(Config.EXPKey_username, bean.getUsername());
            message.setAttribute(Config.EXPKey_communityId, "" + bean.getCommunityId());
            message.setAttribute(Config.EXPKey_communityName, "" + PreferencesUtil.getCommityName(this));
            message.setAttribute(Config.EXPKey_gender, "" + bean.getGender());
            message.setAttribute(Config.EXPKey_room, "" + bean.getRoom());
            message.setAttribute(Config.EXPKey_userunit, "" + bean.getUserUnit());
            message.setAttribute(Config.EXPKey_userFloor, "" + bean.getUserFloor());
            if (TextUtils.equals(servantType,Config.SERVANT_TYPE_BANGBANG)||
                    TextUtils.equals(servantType,Config.SERVANT_TYPE_WEIXIUTOUSU) ||
                    TextUtils.equals(servantType,Config.SERVANT_TYPE_SHOPTOUSU)||
                    TextUtils.equals(servantType,Config.SERVANT_TYPE_WUYE)) {
                message.setAttribute(Config.EXPKey_module, servantType);
            }


            if (chatType == CHATTYPE_SINGLE && toChatUser != null && (toChatUser.sort.equals("2") || toChatUser.sort.equals("5"))){
                message.setAttribute(Config.EXPKey_ADDRESS, bean.getUserFloor() + bean.getUserUnit() + bean.getRoom());
            }
            if (cmdCode != 0) {
                message.setAttribute(Config.EXPKey_CMD_CODE, cmdCode);
            }
            conversation.addMessage(message);
            adapter.refresh();
            listView.setSelection(listView.getCount() - 1);
            setResult(RESULT_OK);
            // send file
            // sendVoiceSub(filePath, fileName, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 发送语音
     *
     * @param filePath
     * @param fileName
     * @param length
     * @param isResend
     */
    private void sendVoice(String filePath, String fileName, String length, boolean isResend) {
        if (!(new File(filePath).exists())) {
            return;
        }
        try {
            final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.VOICE);
            // 如果是群聊，设置chattype,默认是单聊
            if (chatType == CHATTYPE_GROUP){
                message.setChatType(ChatType.GroupChat);
                message.setAttribute(Config.EXPKey_testFrom,testFrom);
                message.setAttribute(Config.EXPKey_groupName,groupName);
                message.setAttribute(Config.EXPKey_communityName,communityName);
                message.setAttribute(Config.EXPKey_communityId,communityId);
            }else{
                message.setAttribute(Config.EXPKey_testFrom, testFrom);
                message.setAttribute(Config.EXPKey_testTo,testTo);
            }
            message.setReceipt(toChatUsername);
            int len = Integer.parseInt(length);
            VoiceMessageBody body = new VoiceMessageBody(new File(filePath), len);
            message.addBody(body);
            message.setAttribute(Config.EXPKey_nickname, bean.getNickname());
            message.setAttribute(Config.EXPKey_avatar, bean.getAvatar());
            //0218 add username
            message.setAttribute(Config.EXPKey_username, bean.getUsername());

            if (chatType == CHATTYPE_SINGLE && toChatUser != null && (toChatUser.sort.equals("2") || toChatUser.sort.equals("5"))){
                message.setAttribute(Config.EXPKey_ADDRESS, bean.getUserFloor() + bean.getUserUnit() + bean.getRoom());
            }
            if (cmdCode != 0) {
                message.setAttribute(Config.EXPKey_CMD_CODE, cmdCode);
            }
            conversation.addMessage(message);
            adapter.refresh();
            listView.setSelection(listView.getCount() - 1);
            setResult(RESULT_OK);
            // send file
            // sendVoiceSub(filePath, fileName, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送图片
     *
     * @param filePath
     */
    private void sendPictureServantOrComplain(final String filePath) {
        String to = toChatUsername;
        // create and add image message in view
        final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
        // 如果是群聊，设置chattype,默认是单聊
        if (chatType == CHATTYPE_GROUP){
            message.setChatType(ChatType.GroupChat);
            message.setAttribute(Config.EXPKey_testFrom,testFrom);
            message.setAttribute(Config.EXPKey_groupName,groupName);
            message.setAttribute(Config.EXPKey_communityName,communityName);
            message.setAttribute(Config.EXPKey_communityId,communityId);
        }else{
            message.setAttribute(Config.EXPKey_testFrom, testFrom);
            message.setAttribute(Config.EXPKey_testTo,testTo);
        }
        message.setAttribute(Config.EXPKey_nickname, bean.getNickname());
        message.setAttribute(Config.EXPKey_avatar, bean.getAvatar());


        /**
         * module
         nickname
         avatar
         username
         communityId
         communityName
         gender
         room
         userUnit
         userFloor
         module有三个值：bangbang,wuye,shop，帮帮是用户反馈模块，wuye是物业客服模块，shop是店家客服模块, tousu , 投诉
         *
         */
        message.setAttribute(Config.EXPKey_username, bean.getUsername());
        message.setAttribute(Config.EXPKey_communityId, "" + bean.getCommunityId());
        message.setAttribute(Config.EXPKey_communityName, "" + PreferencesUtil.getCommityName(this));
        message.setAttribute(Config.EXPKey_gender, "" + bean.getGender());
        message.setAttribute(Config.EXPKey_room, "" + bean.getRoom());
        message.setAttribute(Config.EXPKey_userunit, "" + bean.getUserUnit());
        message.setAttribute(Config.EXPKey_userFloor, "" + bean.getUserFloor());
        if (TextUtils.equals(servantType,Config.SERVANT_TYPE_BANGBANG)||
                TextUtils.equals(servantType,Config.SERVANT_TYPE_WEIXIUTOUSU) ||
                TextUtils.equals(servantType,Config.SERVANT_TYPE_SHOPTOUSU)||
                TextUtils.equals(servantType,Config.SERVANT_TYPE_WUYE)) {
            message.setAttribute(Config.EXPKey_module, servantType);
        }

        if (chatType == CHATTYPE_SINGLE && toChatUser != null && (toChatUser.sort.equals("2") || toChatUser.sort.equals("5"))){
            message.setAttribute(Config.EXPKey_ADDRESS, bean.getUserFloor() + bean.getUserUnit() + bean.getRoom() + "");
        }

        if (cmdCode != 0) {
            message.setAttribute(Config.EXPKey_CMD_CODE, cmdCode);
        }
        message.setReceipt(to);
        ImageMessageBody body = new ImageMessageBody(new File(filePath));
        // 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
        // body.setSendOriginalImage(true);
        message.addBody(body);
        conversation.addMessage(message);

        listView.setAdapter(adapter);
        adapter.refresh();
        listView.setSelection(listView.getCount() - 1);
        setResult(RESULT_OK);
        // more(more);
    }


    /**
     * 发送图片
     *
     * @param filePath
     */
    private synchronized void sendPicture(final String filePath) {
        String to = toChatUsername;
        // create and add image message in view
        final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
        // 如果是群聊，设置chattype,默认是单聊
        if (chatType == CHATTYPE_GROUP){
            message.setChatType(ChatType.GroupChat);
            message.setAttribute(Config.EXPKey_testFrom,testFrom);
            message.setAttribute(Config.EXPKey_groupName,groupName);
            message.setAttribute(Config.EXPKey_communityName,communityName);
            message.setAttribute(Config.EXPKey_communityId,communityId);
        }else{
            message.setAttribute(Config.EXPKey_testFrom, testFrom);
            message.setAttribute(Config.EXPKey_testTo,testTo);
        }
        //0218 add username
        message.setAttribute(Config.EXPKey_username, bean.getUsername());

        message.setAttribute(Config.EXPKey_nickname, bean.getNickname());
        message.setAttribute(Config.EXPKey_avatar, bean.getAvatar());
        if (chatType == CHATTYPE_SINGLE && toChatUser != null && (toChatUser.sort.equals("2") || toChatUser.sort.equals("5"))){
            message.setAttribute(Config.EXPKey_ADDRESS, bean.getUserFloor() + bean.getUserUnit() + bean.getRoom() + "");
        }
        if (cmdCode != 0) {
            message.setAttribute(Config.EXPKey_CMD_CODE, cmdCode);
        }
        message.setReceipt(to);
        ImageMessageBody body = new ImageMessageBody(new File(filePath));
        // 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
        // body.setSendOriginalImage(true);
        message.addBody(body);
        conversation.addMessage(message);

        listView.setAdapter(adapter);
        adapter.refresh();
        listView.setSelection(listView.getCount() - 1);
        setResult(RESULT_OK);
        // more(more);
    }

    /**
     * 发送视频消息
     */
    private void sendVideo(final String filePath, final String thumbPath, final int length) {
        final File videoFile = new File(filePath);
        if (!videoFile.exists()) {
            return;
        }
        try {
            EMMessage message = EMMessage.createSendMessage(EMMessage.Type.VIDEO);
            // 如果是群聊，设置chattype,默认是单聊
            if (chatType == CHATTYPE_GROUP){
                message.setChatType(ChatType.GroupChat);
                message.setAttribute(Config.EXPKey_testFrom,testFrom);
                message.setAttribute(Config.EXPKey_groupName,groupName);
                message.setAttribute(Config.EXPKey_communityName,communityName);
                message.setAttribute(Config.EXPKey_communityId,communityId);
            }else{
                message.setAttribute(Config.EXPKey_testFrom, testFrom);
                message.setAttribute(Config.EXPKey_testTo,testTo);
            }
            String to = toChatUsername;
            message.setReceipt(to);
            VideoMessageBody body = new VideoMessageBody(videoFile, thumbPath, length, videoFile.length());
            message.addBody(body);
            message.setAttribute(Config.EXPKey_nickname, bean.getNickname());
            message.setAttribute(Config.EXPKey_avatar, bean.getAvatar());
            //0218 add username
            message.setAttribute(Config.EXPKey_username, bean.getUsername());

            if (chatType == CHATTYPE_SINGLE && toChatUser != null && (toChatUser.sort.equals("2") || toChatUser.sort.equals("5"))){
                message.setAttribute(Config.EXPKey_ADDRESS, bean.getUserFloor() + bean.getUserUnit() + bean.getRoom());
            }
            if (cmdCode != 0) {
                message.setAttribute(Config.EXPKey_CMD_CODE, cmdCode);
            }
            conversation.addMessage(message);
            listView.setAdapter(adapter);
            adapter.refresh();
            listView.setSelection(listView.getCount() - 1);
            setResult(RESULT_OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据图库图片uri发送图片
     *
     * @param selectedImage
     */
    private void sendPicByUri(Uri selectedImage) {
        // String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(selectedImage, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex("_data");
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(this, "找不到图片", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            if (TextUtils.equals(servantType,Config.SERVANT_TYPE_BANGBANG)||
                    TextUtils.equals(servantType,Config.SERVANT_TYPE_WEIXIUTOUSU) ||
                    TextUtils.equals(servantType,Config.SERVANT_TYPE_SHOPTOUSU)||
                    TextUtils.equals(servantType,Config.SERVANT_TYPE_WUYE)) {
                sendPictureServantOrComplain(picturePath);
            }else{
                sendPicture(picturePath);
            }
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(this, "找不到图片", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            if (TextUtils.equals(servantType,Config.SERVANT_TYPE_BANGBANG)||
                    TextUtils.equals(servantType,Config.SERVANT_TYPE_WEIXIUTOUSU) ||
                    TextUtils.equals(servantType,Config.SERVANT_TYPE_SHOPTOUSU)||
                    TextUtils.equals(servantType,Config.SERVANT_TYPE_WUYE)) {
                sendPictureServantOrComplain(file.getAbsolutePath());
            }else{
                sendPicture(file.getAbsolutePath());
            }

        }

    }

    /**
     * 发送位置信息
     *
     * @param latitude
     * @param longitude
     * @param imagePath
     * @param locationAddress
     */
    private void sendLocationMsg(double latitude, double longitude, String imagePath, String locationAddress) {
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.LOCATION);
        // 如果是群聊，设置chattype,默认是单聊
        if (chatType == CHATTYPE_GROUP){
            message.setChatType(ChatType.GroupChat);
            message.setAttribute(Config.EXPKey_testFrom,testFrom);
            message.setAttribute(Config.EXPKey_groupName,groupName);
            message.setAttribute(Config.EXPKey_communityName,communityName);
            message.setAttribute(Config.EXPKey_communityId,communityId);
        }else{
            message.setAttribute(Config.EXPKey_testFrom, testFrom);
            message.setAttribute(Config.EXPKey_testTo,testTo);
        }

        LocationMessageBody locBody = new LocationMessageBody(locationAddress, latitude, longitude);
        message.addBody(locBody);
        message.setReceipt(toChatUsername);
        message.setAttribute(Config.EXPKey_nickname, bean.getNickname());
        message.setAttribute(Config.EXPKey_avatar, bean.getAvatar());
        //0218 add username
        message.setAttribute(Config.EXPKey_username, bean.getUsername());

        conversation.addMessage(message);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setSelection(listView.getCount() - 1);
        setResult(RESULT_OK);

    }

    /**
     * 发送文件
     *
     * @param uri
     */
    private void sendFile(Uri uri) {
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            Toast.makeText(getApplicationContext(), "文件不存在", 0).show();
            return;
        }
        if (file.length() > 10 * 1024 * 1024) {
            Toast.makeText(getApplicationContext(), "文件不能大于10M", 0).show();
            return;
        }

        // 创建一个文件消息
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.FILE);
        // 如果是群聊，设置chattype,默认是单聊
        if (chatType == CHATTYPE_GROUP){
            message.setChatType(ChatType.GroupChat);
            message.setAttribute(Config.EXPKey_testFrom,testFrom);
            message.setAttribute(Config.EXPKey_groupName,groupName);
            message.setAttribute(Config.EXPKey_communityName,communityName);
            message.setAttribute(Config.EXPKey_communityId,communityId);
        }else{
            message.setAttribute(Config.EXPKey_testFrom, testFrom);
            message.setAttribute(Config.EXPKey_testTo,testTo);
        }

        message.setReceipt(toChatUsername);
        // add message body
        NormalFileMessageBody body = new NormalFileMessageBody(new File(filePath));
        message.addBody(body);
        message.setAttribute(Config.EXPKey_nickname, bean.getNickname());
        message.setAttribute(Config.EXPKey_avatar, bean.getAvatar());

        //0218 add username
        message.setAttribute(Config.EXPKey_username, bean.getUsername());

        if (chatType == CHATTYPE_SINGLE && toChatUser != null && (toChatUser.sort.equals("2") || toChatUser.sort.equals("5"))){
            message.setAttribute(Config.EXPKey_ADDRESS, bean.getUserFloor() + bean.getUserUnit() + bean.getRoom());
        }

        if (cmdCode != 0) {
            message.setAttribute(Config.EXPKey_CMD_CODE, cmdCode);
        }
        conversation.addMessage(message);
        listView.setAdapter(adapter);
        adapter.refresh();
        listView.setSelection(listView.getCount() - 1);
        setResult(RESULT_OK);
    }

    /**
     * 重发消息
     */
    private void resendMessage() {
        EMMessage msg = null;
        msg = conversation.getMessage(resendPos);
        // msg.setBackSend(true);
        msg.status = EMMessage.Status.CREATE;

        adapter.refresh();
        listView.setSelection(resendPos);
    }

    /**
     * 显示语音图标按钮
     *
     * @param view
     */
    public void setModeVoice(View view) {
        hideKeyboard();
        edittext_layout.setVisibility(View.GONE);
        more.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        buttonSetModeKeyboard.setVisibility(View.VISIBLE);
        buttonSend.setVisibility(View.GONE);
        btnMore.setVisibility(View.VISIBLE);
        buttonPressToSpeak.setVisibility(View.VISIBLE);
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.INVISIBLE);
        btnContainer.setVisibility(View.VISIBLE);
        emojiIconContainer.setVisibility(View.GONE);

    }

    /**
     * 显示键盘图标
     *
     * @param view
     */
    public void setModeKeyboard(View view) {
        // mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener()
        // {
        // @Override
        // public void onFocusChange(View v, boolean hasFocus) {
        // if(hasFocus){
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        // }
        // }
        // });
        edittext_layout.setVisibility(View.VISIBLE);
        more.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        buttonSetModeVoice.setVisibility(View.VISIBLE);
        // mEditTextContent.setVisibility(View.VISIBLE);
        mEditTextContent.requestFocus();
        // buttonSend.setVisibility(View.VISIBLE);
        buttonPressToSpeak.setVisibility(View.GONE);
        if (TextUtils.isEmpty(mEditTextContent.getText())) {
            btnMore.setVisibility(View.VISIBLE);
            buttonSend.setVisibility(View.GONE);
        } else {
            btnMore.setVisibility(View.GONE);
            buttonSend.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 点击清空聊天记录
     *
     * @param view
     */
    public void emptyHistory(View view) {
        startActivityForResult(
                new Intent(this, AlertDialog.class).putExtra("titleIsCancel", true).putExtra("msg", "是否清空所有聊天记录").putExtra("cancel", true),
                REQUEST_CODE_EMPTY_HISTORY);
    }

    /**
     * 点击进入群组详情
     *
     * @param view
     */
    public void toGroupDetails(View view) {
        startActivityForResult((new Intent(this, GroupDetailsActivity.class).putExtra("groupId", toChatUsername)),
                REQUEST_CODE_GROUP_DETAIL);
    }

    /**
     * 显示或隐藏图标按钮页
     *
     * @param view
     */
    public void more(View view) {
        if (more.getVisibility() == View.GONE) {
            System.out.println("more gone");
            hideKeyboard();
            more.setVisibility(View.VISIBLE);
            btnContainer.setVisibility(View.VISIBLE);
            emojiIconContainer.setVisibility(View.GONE);
        } else {
            if (emojiIconContainer.getVisibility() == View.VISIBLE) {
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.VISIBLE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
            } else {
                more.setVisibility(View.GONE);
            }

        }

    }

    /**
     * 点击文字输入框
     *
     * @param v
     */
    public void editClick(View v) {
        listView.setSelection(listView.getCount() - 1);
        if (more.getVisibility() == View.VISIBLE) {
            more.setVisibility(View.GONE);
            iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * 消息广播接收者
     */
    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 记得把广播给终结掉
            abortBroadcast();

            String username = intent.getStringExtra("from");
            String msgid = intent.getStringExtra("msgid");
            // 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
            EMMessage message = EMChatManager.getInstance().getMessage(msgid);
            if (!XJMessageHelper.operatNewMessage(getmContext(),message)) XJContactHelper.saveContact(message);
            else {
                Log.i("onion", "是个通知");
                return;
            }
            // 如果是群聊消息，获取到group id
            if (message.getChatType() == ChatType.GroupChat) {
                username = message.getTo();
                GroupHeader header = new Select().from(GroupHeader.class).where("group_id = ?", message.getTo()).executeSingle();
                if (header == null || header.getNum() < 10 || !new File(header.getHeader_id()).exists())
                    GroupUtils.getGroupInfo(message.getTo());
            }
            if (!username.equals(toChatUsername)) {
                // 消息不是发给当前会话，return
                notifyNewMessage(message);
                return;
            }


            // conversation =
            // EMChatManager.getInstance().getConversation(toChatUsername);
            // 通知adapter有新消息，更新ui
            adapter.refresh();
            listView.setSelection(listView.getCount() - 1);

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
            EMConversation conversation = EMChatManager.getInstance().getConversation(from);
            if (conversation != null) {
                // 把message设为已读
                EMMessage msg = conversation.getMessage(msgid);
                if (msg != null) {
                    msg.isAcked = true;
                }
            }
            adapter.notifyDataSetChanged();

        }
    };

    /**
     * 消息送达BroadcastReceiver
     */
    private BroadcastReceiver deliveryAckMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();

            String msgid = intent.getStringExtra("msgid");
            String from = intent.getStringExtra("from");
            EMConversation conversation = EMChatManager.getInstance().getConversation(from);
            if (conversation != null) {
                // 把message设为已读
                EMMessage msg = conversation.getMessage(msgid);
                if (msg != null) {
                    msg.isDelivered = true;
                }
            }

            adapter.notifyDataSetChanged();
        }
    };
    private PowerManager.WakeLock wakeLock;

    /**
     * 按住说话listener
     */
    class PressToSpeakListen implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!CommonUtils.isExitsSdcard()) {
                        Toast.makeText(ChatActivity.this, "发送语音需要sdcard支持！", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    try {
                        v.setPressed(true);
                        wakeLock.acquire();
                        if (VoicePlayClickListener.isPlaying)
                            VoicePlayClickListener.currentPlayListener.stopPlayVoice();
                        recordingContainer.setVisibility(View.VISIBLE);
                        recordingHint.setText(getString(R.string.move_up_to_cancel));
                        recordingHint.setBackgroundColor(Color.TRANSPARENT);
                        voiceRecorder.startRecording(null, toChatUsername, getApplicationContext());
                    } catch (Exception e) {
                        e.printStackTrace();
                        v.setPressed(false);
                        if (wakeLock.isHeld())
                            wakeLock.release();
                        if (voiceRecorder != null)
                            voiceRecorder.discardRecording();
                        recordingContainer.setVisibility(View.INVISIBLE);
                        Toast.makeText(ChatActivity.this, R.string.recoding_fail, Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    return true;
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        recordingHint.setText(getString(R.string.release_to_cancel));
                        recordingHint.setBackgroundResource(R.drawable.recording_text_hint_bg);
                    } else {
                        recordingHint.setText(getString(R.string.move_up_to_cancel));
                        recordingHint.setBackgroundColor(Color.TRANSPARENT);
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    recordingContainer.setVisibility(View.INVISIBLE);
                    if (wakeLock.isHeld())
                        wakeLock.release();
                    if (event.getY() < 0) {
                        // discard the recorded audio.
                        voiceRecorder.discardRecording();

                    } else {
                        // stop recording and send voice file
                        try {
                            int length = voiceRecorder.stopRecoding();
                            if (length > 0) {


                                if (TextUtils.equals(servantType,Config.SERVANT_TYPE_BANGBANG)||
                                        TextUtils.equals(servantType,Config.SERVANT_TYPE_WEIXIUTOUSU) ||
                                        TextUtils.equals(servantType,Config.SERVANT_TYPE_SHOPTOUSU)||
                                        TextUtils.equals(servantType,Config.SERVANT_TYPE_WUYE)) {
                                    sendVoiceServantOrComplain(voiceRecorder.getVoiceFilePath(), voiceRecorder.getVoiceFileName(toChatUsername),
                                            Integer.toString(length), false);
                                }else{
                                    sendVoice(voiceRecorder.getVoiceFilePath(), voiceRecorder.getVoiceFileName(toChatUsername),
                                            Integer.toString(length), false);

                                }



                            } else if (length == EMError.INVALID_FILE) {
                                Toast.makeText(getApplicationContext(), "无录音权限", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "录音时间太短", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ChatActivity.this, "发送失败，请检测服务器是否连接", Toast.LENGTH_SHORT).show();
                        }

                    }
                    return true;
                default:
                    recordingContainer.setVisibility(View.INVISIBLE);
                    if (voiceRecorder != null)
                        voiceRecorder.discardRecording();
                    return false;
            }
        }
    }

    /**
     * 获取表情的gridview的子view
     *
     * @param i
     * @return
     */
    private View getGridChildView(int i) {
        View view = View.inflate(this, R.layout.expression_gridview, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();
        if (i == 1) {
            List<String> list1 = reslist.subList(0, 20);/// 20/27//34
            list.addAll(list1);
        } else if (i == 2) {
            list.addAll(reslist.subList(20, 40));
        } else if (i == 3) {
            list.addAll(reslist.subList(40, 60));
        } else if (i == 4) {
            list.addAll(reslist.subList(60, 80));
        } else if (i == 5) {
            list.addAll(reslist.subList(80, reslist.size()));
        }
        list.add("delete_expression");
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this, 1, list);
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = expressionAdapter.getItem(position);
                try {
                    // 文字输入框可见时，才可输入表情
                    // 按住说话可见，不让输入表情
                    if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {

                        if (filename != "delete_expression") { // 不是删除键，显示表情
                            // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                            Class clz = Class.forName("xj.property.utils.SmileUtils");

                            Field field = clz.getField(filename);


                            int selectionStart = mEditTextContent.getSelectionStart();// 获取光标的位置
                            Spannable smiledText = SmileUtils.getSmiledText(getmContext(), (String) field.get(null));
                            Editable editableText = mEditTextContent.getEditableText();
                            editableText.insert(selectionStart, smiledText);

//                        mEditTextContent.append(SmileUtils.getSmiledText(getmContext(), (String) field.get(null)));

                        } else {
                            // 删除文字或者表情
                            if (!TextUtils.isEmpty(mEditTextContent.getText())) {

                                int selectionStart = mEditTextContent.getSelectionStart();// 获取光标的位置

                                if (selectionStart > 0) {

                                    String body = mEditTextContent.getText().toString();

                                    String tempStr = body.substring(0, selectionStart);

                                    int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                    if (i != -1) {
                                        /// 截取最后一个表情
                                        CharSequence cs = tempStr.substring(i, selectionStart);

                                        if (SmileUtils.containsKey(cs.toString()))
                                            /// 删除最后一个表情字符串的占位符
                                            mEditTextContent.getEditableText().delete(i, selectionStart);
                                        else
                                            mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
                                    } else {


                                        mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
                                    }
                                }
                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        return view;
    }

    public List<String> getExpressionRes(int getSum) {
        List<String> reslist = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "ee_" + x;

            reslist.add(filename);

        }
        return reslist;

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
        // 注销广播
        try {
            EMGroupManager.getInstance().removeGroupChangeListener(groupListener);
            unregisterReceiver(ackMessageReceiver);
            ackMessageReceiver = null;
            unregisterReceiver(deliveryAckMessageReceiver);
            deliveryAckMessageReceiver = null;
        } catch (Exception e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (group != null)
            tv_ChatName.setText(group.getGroupName());
        if (adapter != null) {
            adapter.refresh();
        }

        HXSDKHelper sdkHelper = (HXSDKHelper) HXSDKHelper.getInstance();
        sdkHelper.pushActivity(this);
        // register the event listener when enter the foreground

        EMChatManager.getInstance().registerEventListener(
                this,
                new EMNotifierEvent.Event[]{EMNotifierEvent.Event.EventNewMessage, EMNotifierEvent.Event.EventOfflineMessage,
                        EMNotifierEvent.Event.EventDeliveryAck, EMNotifierEvent.Event.EventReadAck});
        // 注册接收消息广播
        //receiver = new NewMessageBroadcastReceiver();
        //IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        // 设置广播的优先级别大于Mainacitivity,这样如果消息来的时候正好在chat页面，直接显示消息，而不是提示消息未读
        //intentFilter.setPriority(5);
        //registerReceiver(receiver, intentFilter);
//        adapter.refresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wakeLock.isHeld())
            wakeLock.release();
        if (VoicePlayClickListener.isPlaying && VoicePlayClickListener.currentPlayListener != null) {
            // 停止语音播放
            VoicePlayClickListener.currentPlayListener.stopPlayVoice();
        }

        try {
            // 停止录音
            if (voiceRecorder.isRecording()) {
                voiceRecorder.discardRecording();
                recordingContainer.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 隐藏软键盘
     */
    protected void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 加入到黑名单
     *
     * @param username
     */
    private void addUserToBlacklist(String username) {
        try {
            EMContactManager.getInstance().addUserToBlackList(username, false);
            Toast.makeText(getApplicationContext(), "移入黑名单成功", Toast.LENGTH_SHORT).show();
        } catch (EaseMobException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "移入黑名单失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        if (getIntent().getBooleanExtra(Config.INTENT_BACKMAIN, false)) {
            startActivity(new Intent(this, MainActivity.class));
            return;
        }
        finish();
    }

    /**
     * 覆盖手机返回键
     */
    @Override
    public void onBackPressed() {
        if (more.getVisibility() == View.VISIBLE) {
            more.setVisibility(View.GONE);
            iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.INVISIBLE);
        } else {
            if (getIntent().getBooleanExtra(Config.INTENT_BACKMAIN, false)) {
                startActivity(new Intent(this, MainActivity.class));
                return;
            }
            super.onBackPressed();
        }
    }

    /**
     * listview滑动监听listener
     */
    private class ListScrollListener implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case OnScrollListener.SCROLL_STATE_IDLE:
                    if (view.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
                        loadmorePB.setVisibility(View.VISIBLE);
                        // sdk初始化加载的聊天记录为20条，到顶时去db里获取更多
                        List<EMMessage> messages;
                        try {
                            // 获取更多messges，调用此方法的时候从db获取的messages
                            // sdk会自动存入到此conversation中
                            if (chatType == CHATTYPE_SINGLE)
                                messages = conversation.loadMoreMsgFromDB(adapter.getItem(0).getMsgId(), pagesize);
                            else
                                messages = conversation.loadMoreGroupMsgFromDB(adapter.getItem(0).getMsgId(), pagesize);
                        } catch (Exception e1) {
                            loadmorePB.setVisibility(View.GONE);
                            return;
                        }
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                        }
                        if (messages.size() != 0) {
                            // 刷新ui
                            adapter.notifyDataSetChanged();
                            listView.setSelection(messages.size() - 1);
                            if (messages.size() != pagesize)
                                haveMoreData = false;
                        } else {
                            haveMoreData = false;
                        }
                        loadmorePB.setVisibility(View.GONE);
                        isloading = false;

                    }
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }

    }


    @Override
    protected void onNewIntent(Intent intent) {
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }

    /**
     * 转发消息
     *
     * @param forward_msg_id
     */
    protected void forwardMessage(String forward_msg_id) {
        EMMessage forward_msg = EMChatManager.getInstance().getMessage(forward_msg_id);
        EMMessage.Type type = forward_msg.getType();
        switch (type) {
            case TXT:
                // 获取消息内容，发送消息
                String content = ((TextMessageBody) forward_msg.getBody()).getMessage();
                sendText(content);
                break;
            case IMAGE:
                // 发送图片
                String filePath = ((ImageMessageBody) forward_msg.getBody()).getLocalUrl();
                if (filePath != null) {
                    File file = new File(filePath);
                    if (!file.exists()) {
                        // 不存在大图发送缩略图
                        filePath = ImageUtils.getThumbnailImagePath(filePath);
                    }
                    sendPicture(filePath);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 监测群组解散或者被T事件
     */
    class GroupListener extends GroupReomveListener {
//        @Override
//        public void onApplicationReceived(String groupId, String groupName,
//                                          String inviter, String reason) {
//            boolean hasGroup = false;
//            for (EMGroup group : EMGroupManager.getInstance().getAllGroups()) {
//                if (group.getGroupId().equals(groupId)) {
//                    hasGroup = true;
//                    break;
//                }
//            }
//            if (!hasGroup)
//                return;
//
//            // 被邀请
//            EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
//            msg.setChatType(ChatType.GroupChat);
//            msg.setFrom(inviter);
//            msg.setTo(groupId);
//            msg.setMsgId(UUID.randomUUID().toString());
//            //群主 inviter
//            msg.addBody(new TextMessageBody("群主" + "邀请你加入了群聊"));
//            // 保存邀请消息
//            EMChatManager.getInstance().saveMessage(msg);
//            // 提醒新消息
//            EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();
//
//            runOnUiThread(new Runnable() {
//                public void run() {
//                    if (CommonUtils.getTopActivity(ChatActivity.this).equals(
//                            GroupsActivity.class.getName())) {
//                        GroupsActivity.instance.onResume();
//                    }
//                }
//            });
//            adapter.refresh();
//        }

        @Override
        public void onUserRemoved(final String groupId, String groupName) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (toChatUsername.equals(groupId)) {
//                        Toast.makeText(ChatActivity.this, "你被群创建者从此群中移除", 1).show();
                        if (GroupDetailsActivity.instance != null)
                            GroupDetailsActivity.instance.finish();
                        finish();
                    }
                }
            });
        }

        @Override
        public void onGroupDestroy(final String groupId, String groupName) {
            // 群组解散正好在此页面，提示群组被解散，并finish此页面
            runOnUiThread(new Runnable() {
                public void run() {
                    if (toChatUsername.equals(groupId)) {
                        Toast.makeText(ChatActivity.this, "当前群聊已被群创建者解散", Toast.LENGTH_LONG).show();
                        if (GroupDetailsActivity.instance != null)
                            GroupDetailsActivity.instance.finish();
                        finish();
                    }
                }
            });
        }

    }

    public String getToChatUsername() {
        return toChatUsername;
    }

    public ListView getListView() {
        return listView;
    }
}
