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

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.easemob.EMCallBack;
import com.easemob.EMChatRoomChangeListener;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatDB;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.EMLog;
import com.easemob.util.EasyUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import xj.property.activity.HXBaseActivity.ChatActivity;
import xj.property.activity.HXBaseActivity.MainActivity;
import xj.property.activity.HXBaseActivity.NewFriendsMsgActivity;
import xj.property.activity.chat.VoteDetailsChatActivity;
import xj.property.activity.chat.WelfareChatActivity;
import xj.property.activity.repair.RepairChatActivity;
import xj.property.activity.takeout.SuperMarketChatActivity;
import xj.property.cache.GroupHeader;
import xj.property.domain.User;
import xj.property.hxlib.model.HXNotifier;
import xj.property.receiver.CallReceiver;
import xj.property.utils.CommonUtils;
import xj.property.utils.message.XJContactHelper;
import xj.property.utils.message.XJMessageHelper;
import xj.property.utils.other.Config;
import xj.property.utils.other.GroupUtils;
import xj.property.utils.other.PreferencesUtil;

/**
 * Demo UI HX SDK helper class which subclass HXSDKHelper
 *
 * @author easemob
 */
public class HXSDKHelper extends xj.property.hxlib.controller.HXSDKHelper {

    private static final String TAG = "HXSDKHelper";

    /**
     * EMEventListener
     */
    protected EMEventListener eventListener = null;

    /**
     * contact list in cache
     */
    private Map<String, User> contactList;
    private CallReceiver callReceiver;

    /**
     * 用来记录foreground Activity
     */
    private List<Activity> activityList = new ArrayList<>();

    public void pushActivity(Activity activity) {
        if (!activityList.contains(activity)) {
            activityList.add(0, activity);
        }
    }

    public void popActivity(Activity activity) {
        activityList.remove(activity);
    }

    @Override
    protected void initHXOptions() {
        super.initHXOptions();

        // you can also get EMChatOptions to set related SDK options
        //  EMChatOptions options = EMChatManager.getInstance().getChatOptions();
        // options.allowChatroomOwnerLeave(getModel().isChatroomOwnerLeaveAllowed());
    }

    @Override
    protected void initListener() {
        super.initListener();
        IntentFilter callFilter = new IntentFilter(EMChatManager.getInstance().getIncomingCallBroadcastAction());
        if (callReceiver == null) {
            callReceiver = new CallReceiver();
        }

        //注册通话广播接收者
        appContext.registerReceiver(callReceiver, callFilter);
        //注册消息事件监听
        initEventListener();
    }

    /**
     * 全局事件监听
     * 因为可能会有UI页面先处理到这个消息，所以一般如果UI页面已经处理，这里就不需要再次处理
     * activityList.size() <= 0 意味着所有页面都已经在后台运行，或者已经离开Activity Stack
     */
    protected void initEventListener() {
        eventListener = new EMEventListener() {
            private BroadcastReceiver broadCastReceiver = null;

            @Override
            public void onEvent(EMNotifierEvent event) {
                EMMessage message = null;
                if (event.getData() instanceof EMMessage) {
                    message = (EMMessage) event.getData();
                    EMLog.d(TAG, "receive the event : " + event.getEvent() + ",id : " + message.getMsgId());
                }

                switch (event.getEvent()) {
                    case EventNewMessage:
                        //应用在后台，不需要刷新UI,通知栏提示新消息
                        if (activityList.size() <= 0) {
                            if (message != null && (XJMessageHelper.getOrderModel(message.getStringAttribute(Config.EXPKey_serial, ""), 201) != null ||
                                    XJMessageHelper.getOrderModel(message.getStringAttribute("welfareId", ""), 601) != null ||
                                    XJMessageHelper.getOrderModel(message.getStringAttribute("welfareId", ""), 602) != null)) {
                                Log.i("debbug", "已经有message了");
                                EMConversation conversation = EMChatManager.getInstance().getConversation(message.getFrom());
                                conversation.removeMessage(message.getMsgId());
                                EMChatDB.getInstance().deleteMessage(message.getMsgId());
                                return;
                            }
                            if (!XJMessageHelper.operatNewMessage(appContext,message)) {
                                Log.i("onion", "后台处理消息" + message);
                                XJContactHelper.saveContact(message);
                            } else {
                                Log.i("onion", "是个通知");
                            }
                            if (message.getChatType() == ChatType.Chat || !PreferencesUtil.getUnNotifyGroupS(XjApplication.getInstance()).contains(message.getTo()))
                                HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
                        }
                        break;
                    case EventOfflineMessage:
                        if (activityList.size() <= 0) {
                            EMLog.d(TAG, "received offline messages");
                            List<EMMessage> messages = (List<EMMessage>) event.getData();
                            HXSDKHelper.getInstance().getNotifier().onNewMesg(messages);
                        }
                        break;
                    // below is just giving a example to show a cmd toast, the app should not follow this
                    // so be careful of this
//                    case EventNewCMDMessage:
//                    {
//
//                        EMLog.d(TAG, "收到透传消息");
//                        //获取消息body
//                        CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
//                        final String action = cmdMsgBody.action;//获取自定义action
//
//                        //获取扩展属性 此处省略
//                        //message.getStringAttribute("");
//                        EMLog.d(TAG, String.format("透传消息：action:%s,message:%s", action,message.toString()));
//                        final String str = appContext.getString(R.string.receive_the_passthrough);
//
//                        final String CMD_TOAST_BROADCAST = "easemob.demo.cmd.toast";
//                        IntentFilter cmdFilter = new IntentFilter(CMD_TOAST_BROADCAST);
//
//                        if(broadCastReceiver == null){
//                            broadCastReceiver = new BroadcastReceiver(){
//
//                                @Override
//                                public void onReceive(Context context, Intent intent) {
//                                    // TODO Auto-generated method stub
//                                    Toast.makeText(appContext, intent.getStringExtra("cmd_value"), Toast.LENGTH_SHORT).show();
//                                }
//                            };
//
//                            //注册通话广播接收者
//                            appContext.registerReceiver(broadCastReceiver,cmdFilter);
//                        }
//
//                        Intent broadcastIntent = new Intent(CMD_TOAST_BROADCAST);
//                        broadcastIntent.putExtra("cmd_value", str+action);
//                        appContext.sendBroadcast(broadcastIntent, null);
//
//                        break;
//                    }
                    case EventDeliveryAck:
                        message.setDelivered(true);
                        break;
                    case EventReadAck:
                        message.setAcked(true);
                        break;
                    // add other events in case you are interested in
                    default:
                        break;
                }

            }
        };

        EMChatManager.getInstance().registerEventListener(eventListener);

        EMChatManager.getInstance().addChatRoomChangeListener(new EMChatRoomChangeListener() {
            private final static String ROOM_CHANGE_BROADCAST = "easemob.demo.chatroom.changeevent.toast";
            private final IntentFilter filter = new IntentFilter(ROOM_CHANGE_BROADCAST);
            private boolean registered = false;

            private void showToast(String value) {
                if (!registered) {
                    //注册通话广播接收者
                    appContext.registerReceiver(new BroadcastReceiver() {

                        @Override
                        public void onReceive(Context context, Intent intent) {
                            Toast.makeText(appContext, intent.getStringExtra("value"), Toast.LENGTH_SHORT).show();
                        }

                    }, filter);

                    registered = true;
                }

                Intent broadcastIntent = new Intent(ROOM_CHANGE_BROADCAST);
                broadcastIntent.putExtra("value", value);
                appContext.sendBroadcast(broadcastIntent, null);
            }

            @Override
            public void onChatRoomDestroyed(String roomId, String roomName) {
                showToast(" room : " + roomId + " with room name : " + roomName + " was destroyed");
                Log.i("info", "onChatRoomDestroyed=" + roomName);
            }

            @Override
            public void onMemberJoined(String roomId, String participant) {
                showToast("member : " + participant + " join the room : " + roomId);
                Log.i("info", "onmemberjoined=" + participant);

            }

            @Override
            public void onMemberExited(String roomId, String roomName,
                                       String participant) {
                showToast("member : " + participant + " leave the room : " + roomId + " room name : " + roomName);
                Log.i("info", "onMemberExited=" + participant);

            }

            @Override
            public void onMemberKicked(String roomId, String roomName,
                                       String participant) {
                showToast("member : " + participant + " was kicked from the room : " + roomId + " room name : " + roomName);
                Log.i("info", "onMemberKicked=" + participant);

            }

        });
    }

    /**
     * 自定义通知栏提示内容
     *
     * @return
     */
    @Override
    protected HXNotifier.HXNotificationInfoProvider getNotificationListener() {
        //可以覆盖默认的设置
        return new HXNotifier.HXNotificationInfoProvider() {

            @Override
            public String getTitle(EMMessage message) {
                try {//只从本地取
                    if (message.getChatType() == ChatType.GroupChat) {

                        return EMGroupManager.getInstance().getGroup(message.getTo()).getGroupName();
                    }
                } catch (Exception e) {
                }
                //修改标题,这里使用默认
                switch (message.getIntAttribute(Config.EXPKey_CMD_CODE, 0)) {
                    case 100:
                    case 101:
                    case 102:
                    case 103:
                    case 104:
                    case 105:
                    case 106:
                        return message.getStringAttribute("title", "通知");
                    case 110:
                        return "新活动";
                    case 401:
                    case 403:
                        return "物业客服";
                }
                if(Constant.NEW_FRIENDS_USERNAME.equals(message.getUserName())){
                    return "群组申请与通知";
                }
                return message.getStringAttribute(Config.EXPKey_nickname, "帮帮用户");
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                //设置小图标
                return R.drawable.status_bar_icn;
            }

            @Override
            public String getDisplayedText(EMMessage message) {
                // 设置状态栏的消息提示，可以根据message的类型做相应提示
                String ticker = CommonUtils.getMessageDigest(message, appContext);
                if (message.getType() == Type.TXT)
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
//                if( !   XJMessageHelper.operatNewMessage(message)) {
//                    Log.i("onion","通知栏处理消息"+message);
//                    XJContactHelper.saveContact(message);
//                }else {
//                    Log.i("onion","是个通知");
//                    return "通知"+message.getStringAttribute("content","");
//                }
                if (message.getChatType() == ChatType.GroupChat) {
                    GroupHeader header = new Select().from(GroupHeader.class).where("group_id = ?", message.getTo()).executeSingle();
                    if (header == null || header.getNum() < 10)
                        GroupUtils.getGroupInfo(message.getTo());
                }
//              operatNewMessage(message);
//              if(message.getIntAttribute(Config.EXPKey_CMD_CODE,0)!=0){
//                  return  ticker;
//              }
                if (message.getType() == Type.LOCATION) {
                    return message.getStringAttribute(Config.EXPKey_nickname, message.getFrom()) + ":[位置]";
                }

                if(Constant.NEW_FRIENDS_USERNAME.equals(message.getUserName())){
                    return "群组申请与通知";
                }

                if ("通知".equals(message.getStringAttribute(Config.EXPKey_nickname, "通知"))) {
                    return "通知" + message.getStringAttribute("content", "");
//                  return null;
                }
                return message.getStringAttribute(Config.EXPKey_nickname, message.getFrom()) + ": " + ticker;
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                switch (message.getIntAttribute(Config.EXPKey_CMD_CODE, 0)) {
                    case 100:
                    case 101:
                    case 102:
                    case 103:
                    case 104:
                    case 105:
                    case 106:
                    case 401:
                    case 403:
                        return message.getStringAttribute("content", "");
                    case 110:
                        return "有人发起了新活动";
                }
                String userName = "";
                if (message.getChatType() == ChatType.GroupChat) {
                    userName = message.getStringAttribute(Config.EXPKey_nickname, "帮帮") + ":";
                }
                String content = "";
                if (message.getType() == Type.TXT) {
                    content = ((TextMessageBody) message.getBody()).getMessage();
                } else if (message.getType() == Type.FILE) {
                    content = "文件";
                } else if (message.getType() == Type.IMAGE) {
                    content = "图片";
                } else if (message.getType() == Type.LOCATION) {
                    content = "位置";
                } else if (message.getType() == Type.VOICE) {
                    content = "语音";
                } else if (message.getType() == Type.VIDEO) {
                    content = "音频";
                }

                if(Constant.NEW_FRIENDS_USERNAME.equals(message.getUserName())){
                    return  "您有一条新消息";
                }

                return userName + content;
                // return fromUsersNum + "个基友，发来了" + messageNum + "条消息";
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {

                //// 申请与通知
                if(Constant.NEW_FRIENDS_USERNAME.equals(message.getUserName())){
                    Intent intent = new Intent(appContext, NewFriendsMsgActivity.class);
                    intent.putExtra(Config.INTENT_BACKMAIN, true);
                    return intent;
                }

                switch (message.getIntAttribute(Config.EXPKey_CMD_CODE, 0)) {
                    case 100:
                    case 101:
                    case 102:
                    case 103:
                    case 104:
                    case 105:
                    case 106:
                    case 110:
                        return new Intent(appContext, MainActivity.class);
                    case 401:
                    case 403:
                        Intent intent = new Intent(appContext, ChatActivity.class);
                        intent.putExtra("userId", message.getFrom());
                        intent.putExtra(Config.EXPKey_avatar, message.getStringAttribute(Config.EXPKey_avatar, ""));
                        intent.putExtra(Config.EXPKey_nickname, message.getStringAttribute(Config.EXPKey_nickname, ""));
                        intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                        intent.putExtra(Config.EXPKey_CMD_CODE, message.getIntAttribute(Config.EXPKey_CMD_CODE, 0));
                        intent.putExtra(Config.INTENT_BACKMAIN, true);
                        return intent;
                    case 701:
                    case 702:
                    case 703:
                        intent = new Intent(appContext, VoteDetailsChatActivity.class);
                        intent.putExtra("userId", message.getFrom());
                        intent.putExtra(Config.EXPKey_avatar, message.getStringAttribute(Config.EXPKey_avatar, ""));
                        intent.putExtra(Config.EXPKey_nickname, message.getStringAttribute(Config.EXPKey_nickname, ""));
                        intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                        intent.putExtra(Config.EXPKey_CMD_CODE, message.getIntAttribute(Config.EXPKey_CMD_CODE, 0));
                        intent.putExtra(Config.INTENT_BACKMAIN, true);
                        return intent;
                }

                ChatType chatType = message.getChatType();
                int sort = -1;
                try {
                    sort = Integer.parseInt(message.getStringAttribute(Config.EXPKey_SORT, "-1"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = null;
                if (chatType == ChatType.Chat) { // 单聊信息
                    intent = new Intent();
                    switch (sort) {
                        case 4:
                        case 5:
                            intent = new Intent(appContext, RepairChatActivity.class);
                            break;
                        case -1:
                            intent = new Intent(appContext, ChatActivity.class);
                            break;
                        case 2:
                            intent = new Intent(appContext, SuperMarketChatActivity.class);
                            break;
                        case 19:
                            intent = new Intent(appContext, WelfareChatActivity.class);
                            break;
                        case 701:
                        case 702:
                        case 703:
                            intent = new Intent(appContext, VoteDetailsChatActivity.class);
                            break;
                    }
                    intent.putExtra("userId", message.getFrom());
                    intent.putExtra(Config.EXPKey_avatar, message.getStringAttribute(Config.EXPKey_avatar, ""));
                    intent.putExtra(Config.EXPKey_nickname, message.getStringAttribute(Config.EXPKey_nickname, ""));
                    intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);

                } else { // 群聊信息
                    // message.getTo()为群聊id
                    intent = new Intent(appContext, ChatActivity.class);
                    intent.putExtra("groupId", message.getTo());
                    intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                }
                intent.putExtra(Config.INTENT_BACKMAIN, true);
                return intent;
            }
        };
    }


    @Override
    protected void onConnectionConflict() {
        Intent intent = new Intent(appContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("conflict", true);
        appContext.startActivity(intent);
    }

    @Override
    protected void onCurrentAccountRemoved() {
        Intent intent = new Intent(appContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constant.ACCOUNT_REMOVED, true);
        appContext.startActivity(intent);
    }


    @Override
    protected HXSDKModel createModel() {
        return new HXSDKModel(appContext);
    }

    @Override
    public HXNotifier createNotifier() {
        return new HXNotifier() {
            public synchronized void onNewMsg(final EMMessage message) {
                if (EMChatManager.getInstance().isSlientMessage(message)) {
                    return;
                }

                String chatUsename = null;
                List<String> notNotifyIds = null;
                // 获取设置的不提示新消息的用户或者群组ids
                if (message.getChatType() == ChatType.Chat) {
                    chatUsename = message.getFrom();
                    //  notNotifyIds =  hxModel.getDisabledGroups();
                } else {
                    chatUsename = message.getTo();
                    // notNotifyIds = ((DemoHXSDKModel) hxModel).getDisabledIds();
                }

                if (notNotifyIds == null || !notNotifyIds.contains(chatUsename)) {
                    // 判断app是否在后台
                    if (!EasyUtils.isAppRunningForeground(appContext)) {
                        EMLog.d(TAG, "app is running in backgroud");
                        sendNotification(message, false);
                    } else {
                        sendNotification(message, true);
                    }

                    viberateAndPlayTone(message);
                }
            }
        };
    }

    /**
     * 获取内存中好友user list
     *
     * @return
     */
    public Map<String, User> getContactList() {
        if (getHXId() != null && contactList == null) {
            contactList = ((HXSDKModel) getModel()).getContactList();
        }

        return contactList;
    }

    /**
     * 设置好友user list到内存中
     *
     * @param contactList
     */
    public void setContactList(Map<String, User> contactList) {
        this.contactList = contactList;
    }


    @Override
    public void logout(final EMCallBack callback) {
        endCall();
        super.logout(new EMCallBack() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                setContactList(null);
                getModel().closeDB();
                if (callback != null) {
                    callback.onSuccess();
                }
            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub
                if (callback != null) {
                    callback.onProgress(progress, status);
                }
            }

        });
    }

    void endCall() {
        try {
            EMChatManager.getInstance().endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
