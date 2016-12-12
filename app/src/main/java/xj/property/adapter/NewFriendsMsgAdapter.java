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
package xj.property.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import xj.property.Constant;
import xj.property.R;
import xj.property.activity.HXBaseActivity.ChatActivity;
import xj.property.activity.HXBaseActivity.NewFriendsMsgProcessActivity;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.beans.UserInfoDetailBean;
import xj.property.domain.InviteMessage;
import xj.property.domain.InviteMessage.InviteMesageStatus;
import xj.property.utils.ToastUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

public class NewFriendsMsgAdapter extends BaseAdapter {

    private List<EMMessage> allMessage;
    private Context context;
    private AlertDialog complainDialog;

    public NewFriendsMsgAdapter(Context context, List<EMMessage> allMessages) {
        this.context = context;
        this.allMessage = allMessages;
    }

    @Override
    public int getCount() {
        return allMessage.size();
    }

    @Override
    public EMMessage getItem(int position) {
        return allMessage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.common_invite_msg_item, null);

            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.message_status_tv = (TextView) convertView.findViewById(R.id.message_status_tv);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final EMMessage msg = getItem(position);
        if (msg != null) {

            TextMessageBody txtBody = (TextMessageBody) msg.getBody();
            String content = txtBody.getMessage();
            Log.d("NewFriendsMsgAdapter", "content "+ content);

            try {
                int msgStauts = msg.getIntAttribute(Config.EXPKey_msgstatus);
                String ishandle = msg.getStringAttribute(Config.EXPKey_ishandle);

                //// 申请入群消息处理
                if (InviteMesageStatus.BEAPPLYED.ordinal() == msgStauts) {

                    JSONObject obj = new JSONObject(content);

                    String groupName = obj.getString("groupName");
                    String groupId = obj.getString("groupId");

                    String joinReason = obj.getString("n_reason");
                    String nickname = obj.getString("u_userName");
                    String userAvatar = obj.getString("u_userAvatar");
                    String g_groupOwerEmobId = obj.getString("g_groupOwerEmobId");
                    String n_messageId = obj.getString("n_messageId");

                    Log.d("NewFriendsMsgAdapter", nickname + " 申请加入群聊：" + groupName + " groupId " + groupId + " reason " + joinReason);


                    String applymsg = nickname + "申请加入您的 " + groupName + "群组, 是否同意ta的申请加入";

                    holder.message.setText(applymsg);

                    if ("n".equals(ishandle)) {
                        holder.message_status_tv.setVisibility(View.VISIBLE);
                    } else {
                        holder.message_status_tv.setVisibility(View.GONE);
                    }

                    ImageLoader.getInstance().displayImage(userAvatar, holder.avatar, UserUtils.options);

                    convertView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, NewFriendsMsgProcessActivity.class);
                            intent.putExtra("EMmessage", msg);
                            context.startActivity(intent);
                        }
                    });

                } else if (InviteMesageStatus.BEAGREED.ordinal() == msgStauts) {


                    JSONObject obj = new JSONObject(content);

                    final String groupName = obj.getString("groupName");
                    final String groupId = obj.getString("groupId");
                    final String accepter = obj.getString("accepter");

                    UserInfoDetailBean userInfoBean = PreferencesUtil.getLoginInfo(context);

                    ImageLoader.getInstance().displayImage(userInfoBean.getAvatar(), holder.avatar, UserUtils.options);

                    holder.message.setText(groupName + "已同意你的入群申请, 进入群组");

                    if ("n".equals(ishandle)) {
                        holder.message_status_tv.setVisibility(View.VISIBLE);
                    } else {
                        holder.message_status_tv.setVisibility(View.GONE);
                    }
                    convertView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UserInfoDetailBean loginInfo = PreferencesUtil.getLoginInfo(context);
                            /// 登陆状态
                            if (loginInfo != null) {

                                //// 设置消息处理过
                                msg.setAttribute(Config.EXPKey_ishandle, "y");

                                boolean b = EMChatManager.getInstance().updateMessageBody(msg);

                                EMMessage message1 = EMChatManager.getInstance().getMessage(msg.getMsgId());

                                try {
                                    Log.d("NewFriendsMsgAdapter BEAGREED ", " instance: " + EMChatManager.getInstance() + "updateMessageBody " + b + " msgid: " + message1.getMsgId() + "EXPKey_msgstatus  " + message1.getIntAttribute(Config.EXPKey_msgstatus) + "EXPKey_ishandle " + message1.getStringAttribute(Config.EXPKey_ishandle));

                                } catch (EaseMobException e) {
                                    e.printStackTrace();
                                }

                                /// 获取是否是我的群, 如果是, 直接进入群聊
                                if (isMyGroup(loginInfo, groupId)) {

                                    //// 进入群聊
                                    Intent intentPush = new Intent(context, ChatActivity.class);
                                    // it is group chat
                                    intentPush.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                                    intentPush.putExtra("groupId", groupId);
                                    context.startActivity(intentPush);

                                } else {
                                    ToastUtils.showToast(context, "您还不是该群成员,或该群已解散");
                                    notifyDataSetChanged();
                                }
                            } else {
                                Intent intent = new Intent(context, RegisterLoginActivity.class);
                                context.startActivity(intent);
                            }

                        }
                    });

                }else if(InviteMesageStatus.JOINED.ordinal() == msgStauts){

                    JSONObject obj = new JSONObject(content);

                    final String groupName = obj.getString("groupName");
                    final String groupId = obj.getString("groupId");
                    final String reason = obj.getString("reason");
                    final String inviter = obj.getString("inviter");

                    UserInfoDetailBean userInfoBean = PreferencesUtil.getLoginInfo(context);

                    ImageLoader.getInstance().displayImage(userInfoBean.getAvatar(), holder.avatar, UserUtils.options);

                    holder.message.setText(groupName + "邀请您加入群组“" + groupName + "”群组 ， 进入群组。");

                    if ("n".equals(ishandle)) {
                        holder.message_status_tv.setVisibility(View.VISIBLE);
                    } else {
                        holder.message_status_tv.setVisibility(View.GONE);
                    }
                    convertView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UserInfoDetailBean loginInfo = PreferencesUtil.getLoginInfo(context);
                            /// 登陆状态
                            if (loginInfo != null) {

                                //// 设置消息处理过
                                msg.setAttribute(Config.EXPKey_ishandle, "y");

                                boolean b = EMChatManager.getInstance().updateMessageBody(msg);

                                EMMessage message1 = EMChatManager.getInstance().getMessage(msg.getMsgId());

                                try {
                                    Log.d("NewFriendsMsgAdapter BEAGREED ", " instance: " + EMChatManager.getInstance() + "updateMessageBody " + b + " msgid: " + message1.getMsgId() + "EXPKey_msgstatus  " + message1.getIntAttribute(Config.EXPKey_msgstatus) + "EXPKey_ishandle " + message1.getStringAttribute(Config.EXPKey_ishandle));

                                } catch (EaseMobException e) {
                                    e.printStackTrace();
                                }

                                /// 获取是否是我的群, 如果是, 直接进入群聊
                                if (isMyGroup(loginInfo, groupId)) {

                                    //// 进入群聊
                                    Intent intentPush = new Intent(context, ChatActivity.class);
                                    // it is group chat
                                    intentPush.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                                    intentPush.putExtra("groupId", groupId);
                                    context.startActivity(intentPush);

                                } else {
                                    ToastUtils.showToast(context, "您还不是该群成员");
                                }
                            } else {
                                Intent intent = new Intent(context, RegisterLoginActivity.class);
                                context.startActivity(intent);
                            }

                        }
                    });

                }else if (InviteMesageStatus.BEREFUSED.ordinal() == msgStauts) {

                    JSONObject obj = new JSONObject(content);

//                    jsonObject.put("u_userName", nickname);
//                    jsonObject.put("u_userAvatar", userAvatar);
//                    jsonObject.put("n_reason", declineReason);
//                    jsonObject.put("n_messageId", n_messageId);
//                    jsonObject.put("g_groupId", groupId);
//                    jsonObject.put("g_groupname", groupName);
//                    jsonObject.put("g_groupOwerEmobId", g_groupOwerEmobId);
//
//                    jsonObject.put("groupName", groupName);
//                    jsonObject.put("groupId", groupId);
//                    jsonObject.put("decliner", decliner);
                    String declinedReason = null;
                    if(content.contains("n_reason")){
                         declinedReason = obj.getString("n_reason");
                    }
                    String groupName = obj.getString("groupName");
                    String u_userAvatar = obj.getString("u_userAvatar");
                    final String groupId = obj.getString("groupId");

                    ImageLoader.getInstance().displayImage(u_userAvatar, holder.avatar, UserUtils.options);

                    String s = "(" + groupName + ")的群主拒绝了您的申请\n";
                    if(TextUtils.isEmpty(declinedReason)){
                        declinedReason ="";
                    }
                    s += "拒绝理由：" + declinedReason;
                    holder.message.setText(s);

                    if ("n".equals(ishandle)) {
                        holder.message_status_tv.setVisibility(View.VISIBLE);
                    } else {
                        holder.message_status_tv.setVisibility(View.GONE);
                    }

                    convertView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //// 设置消息处理过
                            msg.setAttribute(Config.EXPKey_ishandle, "y");
                            boolean b = EMChatManager.getInstance().updateMessageBody(msg);

                            EMMessage message1 = EMChatManager.getInstance().getMessage(msg.getMsgId());

                            try {
                                Log.d("NewFriendsMsgAdapter BEREFUSED ", " instance: " + EMChatManager.getInstance() + "updateMessageBody " + b + " msgid: " + message1.getMsgId() + "EXPKey_msgstatus  " + message1.getIntAttribute(Config.EXPKey_msgstatus) + "EXPKey_ishandle " + message1.getStringAttribute(Config.EXPKey_ishandle));

                            } catch (EaseMobException e) {
                                e.printStackTrace();
                            }


                            Intent intent = new Intent(context, NewFriendsMsgProcessActivity.class);
                            intent.putExtra("EMmessage", msg);
                            context.startActivity(intent);
                        }
                    });
                }
                //// 我同意了对方的请求
                else if (msgStauts == InviteMesageStatus.AGREED.ordinal()) {

                    JSONObject obj = new JSONObject(content);

                    String groupName = obj.getString("groupName");
                    String groupId = obj.getString("groupId");

                    String joinReason = obj.getString("n_reason");
                    String nickname = obj.getString("u_userName");
                    String userAvatar = obj.getString("u_userAvatar");
                    String g_groupOwerEmobId = obj.getString("g_groupOwerEmobId");
                    String n_messageId = obj.getString("n_messageId");

                    if ("n".equals(ishandle)) {
                        holder.message_status_tv.setVisibility(View.VISIBLE);
                    } else {
                        holder.message_status_tv.setVisibility(View.GONE);
                    }

                    String applymsg = "您同意了" + nickname + "进入 " + groupName + "群组的申请";

                    Log.d("NewFriendsMsgAdapter", nickname + " 申请加入群聊：" + groupName + " groupId " + groupId + " reason " + joinReason);


                    holder.message.setText(applymsg);
                    ImageLoader.getInstance().displayImage(userAvatar, holder.avatar, UserUtils.options);

                    convertView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //// 设置消息处理过
                            msg.setAttribute(Config.EXPKey_ishandle, "y");

                            boolean b = EMChatManager.getInstance().updateMessageBody(msg);

                            EMMessage message1 = EMChatManager.getInstance().getMessage(msg.getMsgId());

                            try {
                                Log.d("NewFriendsMsgAdapter AGREED ", " instance: " + EMChatManager.getInstance() + "updateMessageBody " + b + " msgid: " + message1.getMsgId() + "EXPKey_msgstatus  " + message1.getIntAttribute(Config.EXPKey_msgstatus) + "EXPKey_ishandle " + message1.getStringAttribute(Config.EXPKey_ishandle));

                            } catch (EaseMobException e) {
                                e.printStackTrace();
                            }


                            Intent intent = new Intent(context, NewFriendsMsgProcessActivity.class);
                            intent.putExtra("EMmessage", msg);
                            context.startActivity(intent);
                        }
                    });

                } else if (msgStauts == InviteMesageStatus.REFUSED.ordinal()) {

                    JSONObject obj = new JSONObject(content);

                    String groupName = obj.getString("groupName");
                    String groupId = obj.getString("groupId");

                    String joinReason = obj.getString("n_reason");
                    String nickname = obj.getString("u_userName");
                    String userAvatar = obj.getString("u_userAvatar");
                    String g_groupOwerEmobId = obj.getString("g_groupOwerEmobId");
                    String n_messageId = obj.getString("n_messageId");

                    if ("n".equals(ishandle)) {
                        holder.message_status_tv.setVisibility(View.VISIBLE);
                    } else {
                        holder.message_status_tv.setVisibility(View.GONE);
                    }

                    String applymsg = "您拒绝了" + nickname + "进入 " + groupName + "群组的申请";

                    holder.message.setText(applymsg);

                    ImageLoader.getInstance().displayImage(userAvatar, holder.avatar, UserUtils.options);

                    convertView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //// 设置消息处理过
                            msg.setAttribute(Config.EXPKey_ishandle, "y");

                            boolean b = EMChatManager.getInstance().updateMessageBody(msg);

                            EMMessage message1 = EMChatManager.getInstance().getMessage(msg.getMsgId());

                            try {
                                Log.d("NewFriendsMsgAdapter REFUSED ", " instance: " + EMChatManager.getInstance() + "updateMessageBody " + b + " msgid: " + message1.getMsgId() + "EXPKey_msgstatus  " + message1.getIntAttribute(Config.EXPKey_msgstatus) + "EXPKey_ishandle " + message1.getStringAttribute(Config.EXPKey_ishandle));

                            } catch (EaseMobException e) {
                                e.printStackTrace();
                            }

                            Intent intent = new Intent(context, NewFriendsMsgProcessActivity.class);
                            intent.putExtra("EMmessage", msg);
                            context.startActivity(intent);
                        }
                    });

                }

            } catch (JSONException | EaseMobException e) {
                e.printStackTrace();
                //// 设置消息处理过
                msg.setAttribute(Config.EXPKey_ishandle, "y");
                boolean b = EMChatManager.getInstance().updateMessageBody(msg);
                return null;
            }
            // 设置用户头像

        }
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                InitDialog(context,msg);

                return false;
            }
        });

        return convertView;
    }



    private void InitDialog(Context context, final EMMessage msg) {
        View rootView = View.inflate(context, R.layout.dialog_group_addordelete_confrim_mgr, null);
        complainDialog = new AlertDialog.Builder(context).setView(rootView).create();
        final TextView mytags_del_confirm_tv = (TextView) rootView.findViewById(R.id.mytags_del_confirm_tv);
        mytags_del_confirm_tv.setText("确定删除该条消息");
        rootView.findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                complainDialog.dismiss();
            }
        });
        rootView.findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                complainDialog.dismiss();
                // 删除此消息
                EMChatManager.getInstance().getConversation(Constant.NEW_FRIENDS_USERNAME).removeMessage(msg.getMsgId());
                if(allMessage!=null&&allMessage.size()>0){
                    allMessage.remove(msg);
                }
                notifyDataSetChanged();
            }
        });
        complainDialog.show();
    }

    private boolean isMyGroup(UserInfoDetailBean loginInfo, String emobGroupId) {
        List<EMGroup> groups = EMGroupManager.getInstance().getAllGroups();/// 包含我创建的,我加入的...
        if (groups != null && groups.size() > 0) {

            for (EMGroup group : groups) {
                if (TextUtils.equals(group.getGroupId(), emobGroupId)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    public void remove(EMMessage emMessage){
        if(emMessage!=null){
            allMessage.remove(emMessage);
        }

    }


    private static class ViewHolder {
        ImageView avatar;
        TextView message;
        TextView message_status_tv;
    }

}
