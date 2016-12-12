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

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import xj.property.Constant;
import xj.property.R;
import xj.property.beans.AddGroupMessageReqBean;
import xj.property.beans.AddGroupMessageRespBean;
import xj.property.beans.AddMemberToGroupReqBean;
import xj.property.beans.AddMemberToGroupRespBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.domain.InviteMessage;
import xj.property.event.NewFriendsProcessedBackEvent;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseUtils;
import xj.property.utils.ToastUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;


/**
 * 申请与通知处理界面
 */
public class NewFriendsMsgProcessActivity extends HXBaseActivity {

    private static final String FLAG_REPLY = "REAPPLY";
    private static final String FLAG_NORMAL = "NORMAL";

    private static final String IOS_CONVERSATION_EMOBID_FLAG = "g_groupConversionChatter";
    private static final String IOS_CONVERSATION_EMOBID_FLAG_VALUE = "847670c1b0374f28922553649c2c8671";
    private EMMessage message;
    private ImageView avatar_msg_civ;
    private TextView msg_nickname_tv;
    private TextView msg_reason_tv;

    private LinearLayout msg_apply_llay;
    private EditText msg_decline_reason_et;
    private Button msg_decline_btn;
    private Button msg_agree_btn;


    private LinearLayout msg_reapply_llay;
    private EditText msg_reapply_reason_et;
    private Button msg_reapply_btn;


    private LinearLayout msg_apply_result_llay;
    private ImageView msg_apply_result_iv;
    private TextView msg_apply_result_tv;
    private TextView msg_apply_reason_tv;
    private UserInfoDetailBean userBean;
    //// 为了解决 Android-->IOS 拒绝消息不通的问题.  标记,发送透传到IOS
    private boolean isFromIosBeApply= false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friends_msg_process);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("验证消息");
        message = getIntent().getParcelableExtra("EMmessage");
        userBean = PreferencesUtil.getLoginInfo(getmContext());
        if (message != null) {
            initView();
            initDataLoadingView();

        } else {
            showToast("该消息不存在或已处理");
            finish();
        }
    }

    private void initDataLoadingView() {
        userBean = PreferencesUtil.getLoginInfo(getmContext());
        try {
            int msgStauts = message.getIntAttribute(Config.EXPKey_msgstatus);
            String ishandle = message.getStringAttribute(Config.EXPKey_ishandle);

            TextMessageBody txtBody = (TextMessageBody) message.getBody();
            final String content = txtBody.getMessage();

            //// 申请入群消息处理
            if (InviteMessage.InviteMesageStatus.BEAPPLYED.ordinal() == msgStauts) {

                msg_apply_llay.setVisibility(View.VISIBLE);
                msg_reapply_llay.setVisibility(View.GONE);
                msg_apply_result_llay.setVisibility(View.GONE);

                JSONObject obj = new JSONObject(content);


                if(!TextUtils.isEmpty(content)&&content.contains(IOS_CONVERSATION_EMOBID_FLAG)){

                 String iosConversationEmobid =  obj.getString(IOS_CONVERSATION_EMOBID_FLAG);
                    if(TextUtils.equals(iosConversationEmobid,IOS_CONVERSATION_EMOBID_FLAG_VALUE)){
                        isFromIosBeApply = true;
                    }
                }
                final String groupName = obj.getString("groupName");
                final String groupId = obj.getString("groupId");

                String joinReason = obj.getString("n_reason");
                final String nickname = obj.getString("u_userName");
                final String userAvatar = obj.getString("u_userAvatar");
                final String g_groupOwerEmobId = obj.getString("g_groupOwerEmobId");
                String n_messageId = obj.getString("n_messageId");
                final String applyerId = obj.getString("applyerId");


                ImageLoader.getInstance().displayImage(userAvatar, avatar_msg_civ, UserUtils.options);
                msg_nickname_tv.setText(nickname);
                msg_reason_tv.setText(joinReason);/// 申请理由
                // 设置点击事件
                msg_agree_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // 同意别人发的好友请求
                        ///  final String emobId,  String groupOwnerEmobid ,
                        acceptInvitation(applyerId, g_groupOwerEmobId, groupId, groupName);
                    }
                });
                msg_decline_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String declineReason = msg_decline_reason_et.getText().toString().trim();

//                        @"u_userName"      : [XJAccountTool account].nickname,
//                        @"u_userAvatar"    : [XJAccountTool account].avatar,
//                        @"n_reason"        : self.textView.text,
//                        @"n_messageId"     : acceptMessage.messageId,
//                        @"g_groupId"       : self.messageModel.message.ext[@"g_groupId"],
//                        @"g_groupname"     : self.messageModel.message.ext[@"g_groupname"],
//                        @"g_groupOwerEmobId" : self.messageModel.message.ext[@"g_groupOwerEmobId"]

                        try {


//                            final String n_messageId = groupId+"_"+g_groupOwerEmobId+"_"+userBean.getEmobId();

                            JSONObject jsonObject = new JSONObject();

                            jsonObject.put("u_userName", nickname);
                            jsonObject.put("u_userAvatar", userAvatar);
                            jsonObject.put("n_reason", declineReason);
                            jsonObject.put("n_messageId", message.getMsgId());
                            jsonObject.put("g_groupId", groupId);
                            jsonObject.put("g_groupname", groupName);
                            jsonObject.put("g_groupOwerEmobId", g_groupOwerEmobId);


                            /// 拒绝别人发的群组申请
                            declineInvitation(applyerId, g_groupOwerEmobId, jsonObject.toString(),groupName,userAvatar, nickname, declineReason, groupId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            } else if (InviteMessage.InviteMesageStatus.AGREED.ordinal() == msgStauts) {
                msg_apply_llay.setVisibility(View.GONE);
                msg_reapply_llay.setVisibility(View.GONE);
                msg_apply_result_llay.setVisibility(View.VISIBLE);

                JSONObject obj = new JSONObject(content);

                String groupName = obj.getString("groupName");
                String groupId = obj.getString("groupId");

                String joinReason = obj.getString("n_reason");
                String nickname = obj.getString("u_userName");
                String userAvatar = obj.getString("u_userAvatar");
                String g_groupOwerEmobId = obj.getString("g_groupOwerEmobId");
                String n_messageId = obj.getString("n_messageId");


                ImageLoader.getInstance().displayImage(userAvatar, avatar_msg_civ, UserUtils.options);

                msg_nickname_tv.setText(nickname);
                msg_reason_tv.setText(joinReason);/// 申请理由

                msg_apply_result_iv.setImageResource(R.drawable.grop_process_ok);

                String refuseResult = "您已同意“" + nickname + "”的进群申请";

                msg_apply_result_tv.setText(refuseResult);

                msg_apply_reason_tv.setVisibility(View.GONE);


            } else if (InviteMessage.InviteMesageStatus.REFUSED.ordinal() == msgStauts) {

                msg_apply_llay.setVisibility(View.GONE);
                msg_reapply_llay.setVisibility(View.GONE);
                msg_apply_result_llay.setVisibility(View.VISIBLE);


                JSONObject obj = new JSONObject(content);

                String groupName = obj.getString("groupName");
                String groupId = obj.getString("groupId");

                String joinReason = obj.getString("n_reason");
                String nickname = obj.getString("u_userName");
                String userAvatar = obj.getString("u_userAvatar");
                String g_groupOwerEmobId = obj.getString("g_groupOwerEmobId");
                String n_messageId = obj.getString("n_messageId");

                ImageLoader.getInstance().displayImage(userAvatar, avatar_msg_civ, UserUtils.options);
                msg_nickname_tv.setText(nickname);
                msg_reason_tv.setText(joinReason);/// 申请理由

                msg_apply_result_iv.setImageResource(R.drawable.group_process_delete);

                String refuseResult = "您已拒绝“" + nickname + "”的进群申请";

                msg_apply_result_tv.setText(refuseResult);

                String refuseReason = message.getStringAttribute(Config.EXPKey_declinereason);
                if (TextUtils.isEmpty(refuseReason)) {
                    msg_apply_reason_tv.setText("拒绝理由:" + refuseReason);
                    msg_apply_reason_tv.setVisibility(View.VISIBLE);
                } else {
                    msg_apply_reason_tv.setVisibility(View.INVISIBLE);
                }

                //// 被邀请进入XX群组  /// 废弃
            } else if (InviteMessage.InviteMesageStatus.BEINVITEED.ordinal() == msgStauts) {
                ////被拒绝
            } else if (InviteMessage.InviteMesageStatus.BEREFUSED.ordinal() == msgStauts) {

                msg_apply_llay.setVisibility(View.GONE);
                msg_reapply_llay.setVisibility(View.VISIBLE);
                msg_apply_result_llay.setVisibility(View.GONE);

                JSONObject obj = new JSONObject(content);

//                jsonObject.put("u_userName", nickname);
//                jsonObject.put("u_userAvatar", userAvatar);
//                jsonObject.put("n_reason", declineReason);
//                jsonObject.put("n_messageId", n_messageId);
//                jsonObject.put("g_groupId", groupId);
//                jsonObject.put("g_groupname", groupName);
//                jsonObject.put("g_groupOwerEmobId", g_groupOwerEmobId);
//
//                jsonObject.put("groupName", groupName);
//                jsonObject.put("groupId", groupId);
//                jsonObject.put("decliner", decliner);


                String u_userAvatar = obj.getString("u_userAvatar");
                String u_userName = obj.getString("u_userName");

                String declinedReason =null;
                if(content.contains("n_reason")){
                    declinedReason = obj.getString("n_reason");
                }
                String groupName = obj.getString("groupName");

                final String groupId = obj.getString("groupId");

                final String g_groupOwerEmobId = obj.getString("g_groupOwerEmobId");

                ImageLoader.getInstance().displayImage(u_userAvatar, avatar_msg_civ, UserUtils.options);

                msg_nickname_tv.setText(u_userName);

                String s = "(" + groupName + "）的群主拒绝了您的申请\n";
                if(!TextUtils.isEmpty(declinedReason)){
                    s += "拒绝理由：" + declinedReason;
                }
                msg_reason_tv.setText(s);

                msg_reapply_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String reapply_reason = msg_reapply_reason_et.getText().toString().trim();
                        if(!TextUtils.isEmpty(reapply_reason)){
                            reApply(reapply_reason, groupId, g_groupOwerEmobId);
                        }else{
                            ToastUtils.showToast(getmContext(), "请输入申请理由");
                        }
                    }
                });

                //// 不需要进入详情,直接可以进入聊天界面
            } else if (InviteMessage.InviteMesageStatus.BEAGREED.ordinal() == msgStauts) {
                msg_apply_llay.setVisibility(View.GONE);
                msg_reapply_llay.setVisibility(View.GONE);
                msg_apply_result_llay.setVisibility(View.GONE);

            }
        } catch (EaseMobException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void reApply(final String joinReason, final String groupId, final String g_groupOwerEmobId) {
        userBean = PreferencesUtil.getLoginInfo(getmContext());

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    JSONObject jsonObject = new JSONObject();

                    final String n_messageId = groupId+"_"+g_groupOwerEmobId+"_"+userBean.getEmobId();

                    jsonObject = jsonObject.put("u_userName", userBean.getNickname());
                    jsonObject = jsonObject.put("u_userAvatar", userBean.getAvatar());
                    jsonObject = jsonObject.put("n_reason", joinReason);
                    jsonObject = jsonObject.put("g_groupOwerEmobId", g_groupOwerEmobId);
                    jsonObject = jsonObject.put("n_messageId", n_messageId);
                    final String joooinReason = jsonObject.toString();
                    Log.d("reApply", "apply joinreason  " + joooinReason);

                    EMGroupManager.getInstance().applyJoinToGroup(groupId, joooinReason);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToast("申请成功,等待审核");

                            AddGroupMessageReqBean quaryToken = new AddGroupMessageReqBean();
                            quaryToken.setGroupId(groupId);
                            quaryToken.setMessageId(n_messageId);
                            quaryToken.setType("apply");
                            quaryToken.setEmobIdFrom(userBean.getEmobId());
                            quaryToken.setEmobIdTo(g_groupOwerEmobId);
                            quaryToken.setMessageContent(joinReason);

                            quaryToken.setCommunityId(userBean.getCommunityId());
                            //// v3 2016/03/03

                            addAGroupMessageCall(quaryToken,  FLAG_REPLY);

                            if (msg_reapply_reason_et != null) {
                                msg_reapply_reason_et.setEnabled(false);
                            }
                            if (msg_reapply_btn != null) {
                                msg_reapply_btn.setEnabled(false);
                            }
                        }
                    });

                } catch (EaseMobException | JSONException e) {
                    e.printStackTrace();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToast("申请失败, 请稍后重试");
                        }
                    });
                }
            }
        }).start();

    }


    private void initView() {
        avatar_msg_civ = (ImageView) findViewById(R.id.avatar_msg_civ);
        msg_nickname_tv = (TextView) findViewById(R.id.msg_nickname_tv);
        msg_reason_tv = (TextView) findViewById(R.id.msg_reason_tv);
        //// 处理申请, 同意, 拒绝
        msg_apply_llay = (LinearLayout) findViewById(R.id.msg_apply_llay);
        msg_decline_reason_et = (EditText) findViewById(R.id.msg_decline_reason_et);
        msg_decline_btn = (Button) findViewById(R.id.msg_decline_btn);
        msg_agree_btn = (Button) findViewById(R.id.msg_agree_btn);

        //// 如果被拒绝,再次申请
        msg_reapply_llay = (LinearLayout) findViewById(R.id.msg_reapply_llay);
        msg_reapply_reason_et = (EditText) findViewById(R.id.msg_reapply_reason_et);
        msg_reapply_btn = (Button) findViewById(R.id.msg_reapply_btn);

        //// 申请处理结果展示
        msg_apply_result_llay = (LinearLayout) findViewById(R.id.msg_apply_result_llay);
        msg_apply_result_iv = (ImageView) findViewById(R.id.msg_apply_result_iv);
        msg_apply_result_tv = (TextView) findViewById(R.id.msg_apply_result_tv);
        msg_apply_reason_tv = (TextView) findViewById(R.id.msg_apply_reason_tv);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /// 刷新消息列表
        EventBus.getDefault().post(new NewFriendsProcessedBackEvent(true, message));
    }

    /**
     * 同意好友请求或者群申请
     */
    private void acceptInvitation(final String emobId, final String groupOwnerEmobid, final String groupId, String groupName) {
        userBean = PreferencesUtil.getLoginInfo(getmContext());
        final ProgressDialog pd = new ProgressDialog(getmContext());
        pd.setMessage("正在同意...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        new Thread(new Runnable() {
            public void run() {
                // 调用sdk的同意方法
                try {
                    EMGroupManager.getInstance().acceptApplication(emobId, groupId);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            showToast("已同意");
                            msg_agree_btn.setText("已同意");
                            message.setAttribute(Config.EXPKey_msgstatus, InviteMessage.InviteMesageStatus.AGREED.ordinal());
                            message.setAttribute(Config.EXPKey_ishandle, "y");  //// 设置为消息处理

//                            msg.setStatus(InviteMesageStatus.AGREED);


//                            //// 更新消息内容
//                            boolean b = EMChatManager.getInstance().updateMessageBody(message);


                            EMConversation conversation = EMChatManager.getInstance().getConversation(Constant.NEW_FRIENDS_USERNAME);
                            conversation.removeMessage(message.getMsgId());

                            /// 默认添加到内存中.
                            EMChatManager.getInstance().saveMessage(message,false);

                            EMMessage message1 = EMChatManager.getInstance().getMessage(message.getMsgId());

                            try {
                                Log.d("acceptInvitation ", " instance: " + EMChatManager.getInstance() + "updateMessageBody "+ " msgid: " + message1.getMsgId() + "EXPKey_msgstatus  "
                                        + message1.getIntAttribute(Config.EXPKey_msgstatus) + " EXPKey_ishandle " + message1.getStringAttribute(Config.EXPKey_ishandle));

                            } catch (EaseMobException e) {
                                e.printStackTrace();
                            }


                            msg_agree_btn.setEnabled(false);
                            msg_decline_btn.setEnabled(false);
                            msg_decline_reason_et.setEnabled(false);

                            /// 添加emobid到groupId
//                            addMemberToGroupCall(groupId,reasonUserBean.getEmobId());

                            AddGroupMessageReqBean quaryToken = new AddGroupMessageReqBean();
                            quaryToken.setGroupId(groupId);
                            quaryToken.setMessageId(message.getMsgId());
                            quaryToken.setType("accept");
                            quaryToken.setEmobIdFrom(groupOwnerEmobid);
                            quaryToken.setEmobIdTo(emobId);
                            quaryToken.setMessageContent("accept");

                            /// v3 2016/03/03
                            quaryToken.setCommunityId(userBean.getCommunityId());

                            addAGroupMessageCall(quaryToken,  FLAG_NORMAL);
                            //// 重新刷新页面
                            initDataLoadingView();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getmContext(), "同意失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        }).start();
    }


    /**
     * 拒绝别人发的申请
     *
     * @param declineReason
     */
    private void declineInvitation(final String emobid, final String g_groupOwerEmobId, final String declinereasonAll,

                                    final String groupName  , final String userAvatar , final String userName ,

                                   final String declineReason, final String groupId) {

        //// declineApplication(java.lang.String username, java.lang.String groupId, java.lang.String reason)
//        拒绝加群申请 reject the application of user to join this group
        userBean = PreferencesUtil.getLoginInfo(getmContext());
        final ProgressDialog pd = new ProgressDialog(getmContext());
        pd.setMessage("正在拒绝...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        new Thread(new Runnable() {
            public void run() {
                try {

                    EMGroupManager.getInstance().declineApplication(emobid, groupId, declinereasonAll);

                    if(isFromIosBeApply){
                        Log.d("declineInvitation ", " send cmd msg  updateMessageBody " + " isFromIosBeApply : " + isFromIosBeApply);

                        EMConversation conversation = EMChatManager.getInstance().getConversation(IOS_CONVERSATION_EMOBID_FLAG);

                        EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);

                        //支持单聊和群聊，默认单聊，如果是群聊添加下面这行
                        String action="action1";//action可以自定义，在广播接收时可以收到
                        CmdMessageBody cmdBody=new CmdMessageBody(action);

                        String toUsername=""+ emobid;//发送给某个人
                        cmdMsg.setReceipt(toUsername);
                        cmdMsg.setFrom(PreferencesUtil.getLoginInfo(getmContext()).getEmobId());
//                        cmdMsg.setTo(emobid);
                        cmdMsg.setAttribute("n_avatar", "qunzhuxiaoxi");//支持自定义扩展
                        cmdMsg.setAttribute("n_nickname", "群组通知");//支持自定义扩展
                        cmdMsg.setAttribute("n_isHandle", "no");//支持自定义扩展
                        cmdMsg.setAttribute("n_reason", ""+declineReason);//支持自定义扩展
                        cmdMsg.setAttribute("n_type", "reject");//支持自定义扩展
                        cmdMsg.setAttribute("n_messageId", message.getMsgId());//支持自定义扩展
                        cmdMsg.setAttribute("g_groupname", ""+groupName);//支持自定义扩展
                        cmdMsg.setAttribute("g_groupId", ""+groupId);//支持自定义扩展
                        cmdMsg.setAttribute("u_userAvatar", ""+userAvatar);//支持自定义扩展
                        cmdMsg.setAttribute("u_userName", ""+ userName );//支持自定义扩展
                        cmdMsg.setAttribute("u_userEmobId", ""+emobid);//支持自定义扩展
                        cmdMsg.setAttribute("g_groupOwerEmobId", ""+g_groupOwerEmobId);//支持自定义扩展
                        cmdMsg.setAttribute("version", ""+UserUtils.getVersion(getmContext()));//支持自定义扩展
                        cmdMsg.setAttribute(IOS_CONVERSATION_EMOBID_FLAG, IOS_CONVERSATION_EMOBID_FLAG_VALUE);//支持自定义扩展
                        cmdMsg.addBody(cmdBody);
                        conversation.addMessage(cmdMsg);
                        EMChatManager instance = EMChatManager.getInstance();
                        if(instance==null){
                            Log.d("declineInvitation ", " instance : " + instance);
                        }else{
                            instance.sendMessage(cmdMsg, new EMCallBack() {
                                @Override
                                public void onSuccess() {
                                    Log.d("declineInvitation ", " send cmd msg success instance: " + EMChatManager.getInstance() + "updateMessageBody " + " msgid: " + message.getMsgId());
                                    EMChatManager.getInstance().deleteConversation(IOS_CONVERSATION_EMOBID_FLAG);
                                }

                                @Override
                                public void onError(int i, String s) {
                                    Log.d("declineInvitation ", " send cmd msg error : " + s);
                                }

                                @Override
                                public void onProgress(int i, String s) {

                                }
                            });
                        }

                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();

                            showToast("已拒绝");
                            msg_decline_btn.setText("已拒绝");

                            message.setAttribute(Config.EXPKey_msgstatus, InviteMessage.InviteMesageStatus.REFUSED.ordinal());
                            message.setAttribute(Config.EXPKey_declinereason, declineReason); //// 仅仅的用户拒绝输入显示
                            message.setAttribute(Config.EXPKey_ishandle, "y"); /// 已拒绝, 拒绝也算是处理
                            //// 更新消息内容
//                            boolean b = EMChatManager.getInstance().updateMessageBody(message);

                            EMConversation conversation = EMChatManager.getInstance().getConversation(Constant.NEW_FRIENDS_USERNAME);
                            conversation.removeMessage(message.getMsgId());

                            /// 默认添加到内存中.
                            EMChatManager.getInstance().saveMessage(message,false);

                            EMMessage message1 = EMChatManager.getInstance().getMessage(message.getMsgId());

                            try {
                                Log.d("declineInvitation ", " instance: " + EMChatManager.getInstance() + "updateMessageBody " + " msgid: " + message1.getMsgId() + "EXPKey_msgstatus  " + message1.getIntAttribute(Config.EXPKey_msgstatus) + "EXPKey_ishandle " + message1.getStringAttribute(Config.EXPKey_ishandle));

                            } catch (EaseMobException e) {
                                e.printStackTrace();
                            }

                            msg_decline_btn.setEnabled(false);
                            msg_agree_btn.setEnabled(false);
                            msg_decline_reason_et.setEnabled(false);

                            AddGroupMessageReqBean quaryToken = new AddGroupMessageReqBean();
                            quaryToken.setGroupId(groupId);
                            quaryToken.setMessageId(message.getMsgId());
                            quaryToken.setType("reject");
                            quaryToken.setEmobIdFrom(g_groupOwerEmobId);
                            quaryToken.setEmobIdTo(emobid);
                            quaryToken.setMessageContent(declineReason);

                            //// v3 2016/03/03
                            quaryToken.setCommunityId(userBean.getCommunityId());
                            //// 调用拒绝接口
                            addAGroupMessageCall(quaryToken,  FLAG_NORMAL);
                            //// 重新刷新页面
                            initDataLoadingView();

                        }
                    });
                } catch (final Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getmContext(), "拒绝失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        }).start();


    }


    /**
     * messageContent : 大家一起玩
     * messageId : 1451369301003
     * emobIdFrom : ce04f45b22793b5a2425962b38c74d08
     * emobIdTo : 1e45c249f64eead873aa8a580b30733c
     * groupId : 1429702488658259
     * type : accept  //// reject , apply, invite
     */
    private void addAGroupMessageCall(AddGroupMessageReqBean quaryToken,final String flag) {
        NetBaseUtils.addAGroupMessage(getmContext(),quaryToken,new NetBaseUtils.NetRespListener<CommonRespBean<String>>() {
            @Override
            public void successYes(CommonRespBean<String> commonRespBean, Response response) {
                Log.d("NewFriendsMsgProcessActivity", " addAGroupMessageCall add success  ");

                if (TextUtils.equals(flag, FLAG_REPLY)) {
                    if (message != null) {
                        //// 再次申请成功之后将消息删除
                        EMChatManager.getInstance().getConversation(Constant.NEW_FRIENDS_USERNAME).removeMessage(message.getMsgId());
//                          EMChatManager.getInstance().deleteConversation();
                    }
                }
            }

            @Override
            public void successNo(CommonRespBean<String> commonRespBean, Response response) {
                Log.d("NewFriendsMsgProcessActivity", " addAGroupMessageCall no msg: " + commonRespBean.getMessage());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("NewFriendsMsgProcessActivity", " addAGroupMessageCall add error ");
            }
        });
    }


}
