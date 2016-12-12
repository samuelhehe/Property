/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xj.property.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.DateUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import xj.property.Constant;
import xj.property.R;
import xj.property.XjApplication;
import xj.property.activity.HXBaseActivity.ChatActivity;
import xj.property.activity.HXBaseActivity.GroupDetailsActivity;
import xj.property.activity.HXBaseActivity.MainActivity;
import xj.property.activity.HXBaseActivity.NewFriendsMsgActivity;
import xj.property.activity.chat.VoteDetailsChatActivity;
import xj.property.activity.chat.WelfareChatActivity;
import xj.property.activity.dialog.DialogActivity;
import xj.property.activity.repair.RepairChatActivity;
import xj.property.activity.surrounding.PanicBuyingChatActivity;
import xj.property.activity.takeout.SuperMarketChatActivity;
import xj.property.cache.GroupHeader;
import xj.property.cache.GroupInfo;
import xj.property.domain.InviteMessage;
import xj.property.domain.User;
import xj.property.utils.SmileUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.message.XJContactHelper;
import xj.property.utils.other.AdminUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;


/**
 * 显示所有聊天记录adpater
 */
public class ChatAllHistoryAdapter extends ArrayAdapter<EMConversation> {


    private LayoutInflater inflater;
    private List<EMConversation> conversationList;
    private List<EMConversation> copyConversationList;
    private ConversationFilter conversationFilter;
    private ViewHolder holder;
    private DisplayImageOptions options;
    private Context mContext;

    public ChatAllHistoryAdapter(Activity context, int textViewResourceId, List<EMConversation> objects) {
        super(context, textViewResourceId, objects);
        this.mContext = context;
        this.conversationList = objects;
        copyConversationList = new ArrayList<EMConversation>();
        copyConversationList.addAll(objects);
        inflater = LayoutInflater.from(context);
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .showImageOnFail(R.drawable.head_portrait_personage).showImageForEmptyUri(R.drawable.head_portrait_personage).build();

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_chat_history, parent, false);
        }
        holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.unreadLabel = (TextView) convertView.findViewById(R.id.unread_msg_number);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.msgState = convertView.findViewById(R.id.msg_state);
            holder.list_item_layout = (RelativeLayout) convertView.findViewById(R.id.list_item_layout);
            holder.ivsort = (ImageView) convertView.findViewById(R.id.iv_sort);
            convertView.setTag(holder);
        }

        // 获取与此用户/群组的会话
        EMConversation conversation = getItem(position);
        // 获取用户username或者群组groupid
        String username = conversation.getUserName();///91a5946405384657b76790f29dac1dae
        EMGroupManager.getInstance().loadAllGroups();
        List<EMGroup> groups = EMGroupManager.getInstance().getAllGroups();
        EMContact emContact = null;
        boolean isGroup = false;
        for (EMGroup group : groups) {
            if (group.getGroupId().equals(username)) {
                isGroup = true;
                emContact = group;
                break;
            }
        }

        if (isGroup) {
            // 群聊消息，显示群聊头像
            GroupHeader header = new Select().from(GroupHeader.class).where("group_id = ?", ((EMGroup) emContact).getGroupId()).executeSingle();
            if (header == null) {
                holder.avatar.setImageResource(R.drawable.crowd);
            } else {
                ImageLoader.getInstance().displayImage("file:///" + header.getHeader_id(), holder.avatar);
            }
            GroupInfo groupInfo = new Select().from(GroupInfo.class).where("group_id = ?", ((EMGroup) emContact).getGroupId()).executeSingle();
            if (groupInfo != null) {
                holder.name.setText(groupInfo.getGroup_name());
            } else {
                holder.name.setText(emContact.getNick() != null ? emContact.getNick() : username);
            }
            holder.ivsort.setVisibility(View.VISIBLE);
            holder.ivsort.setImageResource(R.drawable.contact_groupchat_icon);
            final EMContact finalEmContact = emContact;
            holder.ivsort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getContext().startActivity(new Intent(getContext(), GroupDetailsActivity.class).putExtra("groupId", ((EMGroup) finalEmContact).getGroupId()));
                }
            });
        } else {
            // 本地或者服务器获取用户详情，以用来显示头像和nick
            if (username.equals(Constant.GROUP_USERNAME)) {
                holder.name.setText("群聊");
            } else if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
                if (conversation.getMsgCount() > 0) {
                    holder.name.setText("群组申请与通知");
                    holder.avatar.setImageResource(R.drawable.group_notice_img);
                    holder.ivsort.setImageBitmap(null);
                    // 把最后一条消息的内容作为item的message内容
                    EMMessage lastMessage = conversation.getLastMessage();
                    String content = getMessageDigest(lastMessage, (this.getContext()));
                    try {
                        int msgStauts = lastMessage.getIntAttribute(Config.EXPKey_msgstatus);
                        ////TODO  遍历会话是否有未处理消息, 如果有则显示红点
                        //// 申请入群消息处理
                        if (InviteMessage.InviteMesageStatus.BEAPPLYED.ordinal() == msgStauts) {

                            JSONObject obj = new JSONObject(content);

                            String groupName = obj.getString("groupName");
                            String groupId = obj.getString("groupId");

                            String joinReason = obj.getString("n_reason");
                            String nickname = obj.getString("u_userName");
                            String userAvatar = obj.getString("u_userAvatar");
                            String g_groupOwerEmobId = obj.getString("g_groupOwerEmobId");
                            String n_messageId = obj.getString("n_messageId");
                            String applyerId = obj.getString("applyerId");

                            String msg = nickname + "申请加入" + groupName + "理由:" + joinReason;

                            Log.d("ChatAllHistoryAdapter", nickname + "applyerId  " + applyerId + " 申请加入群聊：" + groupName + " groupId " + groupId + " reason " + joinReason);

                            holder.message.setText(msg);
                            //// XXX 同意加入XXX群
                        } else if (InviteMessage.InviteMesageStatus.AGREED.ordinal() == msgStauts) {


                            JSONObject obj = new JSONObject(content);

                            String groupName = obj.getString("groupName");
                            String groupId = obj.getString("groupId");

                            String joinReason = obj.getString("n_reason");
                            String nickname = obj.getString("u_userName");
                            String userAvatar = obj.getString("u_userAvatar");
                            String g_groupOwerEmobId = obj.getString("g_groupOwerEmobId");
                            String n_messageId = obj.getString("n_messageId");


                            String applymsg = "您同意了" + nickname + "进入 “" + groupName + "”群组的申请";

                            Log.d("NewFriendsMsgAdapter", nickname + " 申请加入群聊：" + groupName + " groupId " + groupId + " reason " + joinReason);


//                            李梅同意您加入群组“元旦五日游”群组 ， 进入群组。
//                            Log.d("ChatAllHistoryAdapter", reasonUserBean.getNickname() + " 申请加入群聊：" + groupName + " groupId " + groupId + " reason " + joinReason);

                            holder.message.setText(applymsg);

                            //// 拒绝加入XXX群
                        } else if (InviteMessage.InviteMesageStatus.REFUSED.ordinal() == msgStauts) {


                            JSONObject obj = new JSONObject(content);

                            String groupName = obj.getString("groupName");
                            String groupId = obj.getString("groupId");

                            String joinReason = obj.getString("n_reason");
                            String nickname = obj.getString("u_userName");
                            String userAvatar = obj.getString("u_userAvatar");
                            String g_groupOwerEmobId = obj.getString("g_groupOwerEmobId");
                            String n_messageId = obj.getString("n_messageId");

                            String applymsg = "您拒绝了" + nickname + "进入 “" + groupName + "” 群组的申请";

//                            李梅同意您加入群组“元旦五日游”群组 ， 进入群组。
                            holder.message.setText(applymsg);

                            ////被拒绝
                        } else if (InviteMessage.InviteMesageStatus.BEREFUSED.ordinal() == msgStauts) {


                            JSONObject obj = new JSONObject(content);

                            String declinedReason = obj.getString("n_reason");
                            String u_userAvatar = obj.getString("u_userAvatar");
                            String groupName = obj.getString("groupName");

                            ImageLoader.getInstance().displayImage(u_userAvatar, holder.avatar, UserUtils.options);

                            String s = "(" + groupName + ")的群主拒绝了您的申请\n";
                            s += "拒绝理由：" + declinedReason;

//                            李梅同意您加入群组“元旦五日游”群组 ， 进入群组。
                            holder.message.setText(s);

                        } else if (InviteMessage.InviteMesageStatus.BEAGREED.ordinal() == msgStauts) {

//                            JSONObject obj = new JSONObject();
//                            obj.put("groupName", groupName);
//                            obj.put("groupId", groupId);
//                            obj.put("accepter", accepter);


                            JSONObject obj = new JSONObject(content);

                            final String groupName = obj.getString("groupName");
                            final String groupId = obj.getString("groupId");
                            final String accepter = obj.getString("accepter");

                            holder.message.setText(groupName + " 同意您加入群组“" + groupName + "”群组 ， 进入群组。");


//                          李梅同意您加入群组“元旦五日游”群组 ， 进入群组。
                            ///// 被群主邀请,加入群组.
                        } else if (InviteMessage.InviteMesageStatus.JOINED.ordinal() == msgStauts) {

                            JSONObject obj = new JSONObject(content);

                            final String groupName = obj.getString("groupName");
                            final String groupId = obj.getString("groupId");
                            final String reason = obj.getString("reason");
                            final String inviter = obj.getString("inviter");

                            holder.message.setText(groupName + "邀请您加入群组“" + groupName + "”群组 ， 进入群组。");

                        }
                        /// 消息未读状态处理 TODO


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (EaseMobException e) {
                        e.printStackTrace();
                    }

                }

            } else {
                User contact = XJContactHelper.selectContact(username);
                if (contact != null) {
                    String nike = contact.getNick();
                    String header = contact.avatar;
                    ImageLoader.getInstance().displayImage(header, holder.avatar, options);
                    if (TextUtils.equals(contact.sort, Config.SERVANT_TYPE_BANGBANG) ||
                            TextUtils.equals(contact.sort, Config.SERVANT_TYPE_WEIXIUTOUSU) ||
                            TextUtils.equals(contact.sort, Config.SERVANT_TYPE_SHOPTOUSU) ||
                            TextUtils.equals(contact.sort, Config.SERVANT_TYPE_WUYE)) {
                        holder.avatar.setImageResource(R.drawable.service);
                        holder.ivsort.setVisibility(View.GONE);
                    } else {

                        //根据shoptype区别的展示店铺盖章
                        switch (Integer.parseInt(contact.sort)) {
                            case 4:
                                holder.ivsort.setVisibility(View.VISIBLE);
                                holder.ivsort.setImageResource(R.drawable.contact_clean_icon);
                                break;
                            case -1:
                                holder.ivsort.setVisibility(View.GONE);
                                break;

                            case 5:
                                holder.ivsort.setVisibility(View.VISIBLE);
                                holder.ivsort.setImageResource(R.drawable.contact_repair_icon);
                                break;
                            case 2:
                                holder.ivsort.setVisibility(View.VISIBLE);
                                holder.ivsort.setImageResource(R.drawable.contact_shop_icon);
                                break;
                            case 13:
                            case 500:
                            case 501:
                                holder.ivsort.setVisibility(View.VISIBLE);
                                holder.ivsort.setImageResource(R.drawable.contact_surrounding_icon);
                                break;
                            case 19:
                                holder.ivsort.setVisibility(View.VISIBLE);
                                holder.ivsort.setImageResource(R.drawable.contact_fuli_icon);
                                break;
                        }
                    }


//			holder.avatar.setImageResource(R.drawable.default_avatar);

                    holder.name.setText(nike);
                }
            }
            //服务状态
//			if(收到的透传 显示不在服务){
            //显示不在服务 文字
//			   holder.serState.setVisibility(View.VISIBLE);
//		    }
        }


        if (!TextUtils.equals(Constant.NEW_FRIENDS_USERNAME, conversation.getUserName())) {

            if (conversation.getUnreadMsgCount() > 0) {
                // 显示与此用户的消息未读数
                holder.unreadLabel.setText(String.valueOf(conversation.getUnreadMsgCount()));
                holder.unreadLabel.setVisibility(View.VISIBLE);
            } else {
                holder.unreadLabel.setVisibility(View.INVISIBLE);
            }
        }

        if (conversation.getMsgCount() != 0) {
            // 把最后一条消息的内容作为item的message内容
            EMMessage lastMessage = conversation.getLastMessage();
            String content = getMessageDigest(lastMessage, (this.getContext()));
            if (lastMessage.getType() == EMMessage.Type.LOCATION) {
                holder.message.setText(lastMessage.getStringAttribute(Config.EXPKey_nickname, "帮帮") + ":位置");
            } else if (TextUtils.equals(Constant.NEW_FRIENDS_USERNAME, conversation.getUserName())) {

                //// 消息未处理数   2015/12/31
                if (PreferencesUtil.getLogin(getContext())) {
                    int unhandlenums = getAllUnHandleNumsNewFriends();
                    if (unhandlenums > 0) {

                        holder.avatar.setImageResource(R.drawable.group_notice_img);
                        holder.ivsort.setImageBitmap(null);

                        // 显示与此用户的消息未读数
                        holder.unreadLabel.setText(String.valueOf(unhandlenums));
                        holder.unreadLabel.setVisibility(View.VISIBLE);
                    } else {
                        holder.unreadLabel.setVisibility(View.INVISIBLE);
                    }
                }
            } else {
                holder.message.setText(SmileUtils.getSmiledText(getContext(), content), BufferType.SPANNABLE);
            }
            if (content.contains("[订单]已下单")) {
                holder.unreadLabel.setVisibility(View.VISIBLE);
                holder.unreadLabel.setText("1");
            }
            holder.time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
            if (lastMessage.direct == EMMessage.Direct.SEND && lastMessage.status == EMMessage.Status.FAIL) {
                holder.msgState.setVisibility(View.VISIBLE);
            } else {
                holder.msgState.setVisibility(View.GONE);
            }

            if (lastMessage.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 500
                    || lastMessage.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 501) { //周边抢购在联系人列表的区别显示
//                lastMessage.getStringAttribute("title")

                holder.name.setSingleLine(false);
                holder.name.setMaxLines(2);
                holder.message.setVisibility(View.GONE);
                if (lastMessage.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 500) {
                    holder.name.setText("成功抢购 “" + lastMessage.getStringAttribute("title", "") + "”");
                } else if (lastMessage.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 501) {
                    holder.name.setText("成功扫码 “" + lastMessage.getStringAttribute("title", "") + "”");
                }
                SpannableStringBuilder builder = new SpannableStringBuilder(holder.name.getText().toString());
                ForegroundColorSpan color1 = new ForegroundColorSpan(getContext().getResources().getColor(R.color.prvalue_top_list_txt_color_333333));
                ForegroundColorSpan color2 = new ForegroundColorSpan(getContext().getResources().getColor(R.color.color_9b9b9b));
                builder.setSpan(color1, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(color2, 4, holder.name.getText().toString().length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                holder.name.setText(builder);

            } else if (lastMessage.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 600) { //600code为福利的第一条消息,显示抢购成功,
                holder.name.setSingleLine(false);
                holder.name.setMaxLines(2);
                holder.message.setVisibility(View.GONE);
                holder.name.setText("成功抢到福利 “" + lastMessage.getStringAttribute("title", "") + "”");
                SpannableStringBuilder builder = new SpannableStringBuilder(holder.name.getText().toString());
                ForegroundColorSpan color1 = new ForegroundColorSpan(getContext().getResources().getColor(R.color.prvalue_top_list_txt_color_333333));
                ForegroundColorSpan color2 = new ForegroundColorSpan(getContext().getResources().getColor(R.color.color_9b9b9b));
                ForegroundColorSpan color3 = new ForegroundColorSpan(getContext().getResources().getColor(R.color.color_fulijijiangfafang));
                builder.setSpan(color1, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(color3, 4, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(color2, 6, holder.name.getText().toString().length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                holder.name.setText(builder);
            } else {
                holder.name.setMaxLines(1);
                holder.name.setSingleLine(true);
                holder.message.setVisibility(View.VISIBLE);
                holder.name.setTextColor(getContext().getResources().getColor(R.color.prvalue_top_list_txt_color_333333));
            }

        }


        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(mContext, DialogActivity.class);
                intent.putExtra("message", "确认删除这条信息吗？");
                intent.putExtra("position", position);
                mContext.startActivity(intent);
                return true;
            }
        });


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EMConversation conversation = getItem(position);
                String username = conversation.getUserName();
                Log.i("onion", "username" + username);
                if (username.equals(XjApplication.getInstance().getUserName())) {
                    Toast.makeText(getContext(), "不能和自己聊天", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.equals(Constant.NEW_FRIENDS_USERNAME, username)) {
                    //// 群组申请与通知
//                    Toast.makeText(getActivity(), "群组申请与通知", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(mContext, NewFriendsMsgActivity.class);
                    mContext.startActivity(intent);
                } else {
                    // 进入聊天页面
                    Intent intent = new Intent();
                    User contact = XJContactHelper.selectContact(username);
                    Log.i("onion", contact + "");

                    if (contact != null) {
//                        selectContactbyId(contact.getEmobId(),contact.getAvatar(),contact.getNike());
                        intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);

                        if ("4".equals(contact.sort) || "5".equals(contact.sort)) {
                            intent.setClass(mContext, RepairChatActivity.class);
                        } else if ("-1".equals(contact.sort)) {
                            intent.setClass(mContext, ChatActivity.class);
                        } else if (TextUtils.equals(contact.sort, Config.SERVANT_TYPE_WEIXIUTOUSU) ||
                                TextUtils.equals(contact.sort, Config.SERVANT_TYPE_SHOPTOUSU) ||
                                TextUtils.equals(contact.sort, Config.SERVANT_TYPE_WUYE)) {

                            intent.setClass(mContext, ChatActivity.class);
                            if (MainActivity.startTime == null || MainActivity.endTime == null) {
                                AdminUtils.askAdminCallBack(mContext, contact.sort, null);
                                return;
                            } else {
                                boolean flag = true;
                                //判断当前时间
                                try {
                                    flag = StrUtils.isUnServiceTime(MainActivity.startTime, MainActivity.endTime);
                                } catch (Exception e) {
                                    Log.e("onion", e.toString());
                                }
                                intent.putExtra(Config.InServiceTime, flag);
                            }
                            intent.putExtra(Config.SERVANT_TYPE, contact.sort);

                        } else if (TextUtils.equals(contact.sort, Config.SERVANT_TYPE_BANGBANG)) {
                            intent.setClass(mContext, ChatActivity.class);
                            intent.putExtra(Config.InServiceTime, false);
                            intent.putExtra(Config.SERVANT_TYPE, Config.SERVANT_TYPE_BANGBANG);

                        } else if ("13".equals(contact.sort) || "500".equals(contact.sort) || "501".equals(contact.sort)) {
                            intent.setClass(mContext, PanicBuyingChatActivity.class);

                        } else if ("19".equals(contact.sort) || "601".equals(contact.sort) || "602".equals(contact.sort) || "603".equals(contact.sort)) {
                            intent.setClass(mContext, WelfareChatActivity.class);
                            /// 未读福利消息个数清零.
                            PreferencesUtil.saveWelfareSuccessMsgCount(mContext, 0);
                        }
                        /// 投票消息 23
                        else if ("701".equals(contact.sort) || "702".equals(contact.sort) || "703".equals(contact.sort)) {
                            intent.setClass(mContext, VoteDetailsChatActivity.class);
                        } else {
                            intent.setClass(mContext, SuperMarketChatActivity.class);
                        }
                        intent.putExtra(Config.EXPKey_avatar, contact.avatar);
                        intent.putExtra(Config.EXPKey_nickname, contact.getNick());
                        intent.putExtra("userId", username);
                        mContext.startActivity(intent);
                        return;
                    }

                    intent.setClass(mContext, ChatActivity.class);
                    EMContact emContact = null;
                    List<EMGroup> groups = EMGroupManager.getInstance().getAllGroups();
                    for (EMGroup group : groups) {
                        if (group.getGroupId().equals(username)) {
                            emContact = group;
                            break;
                        }
                    }
                    if (emContact != null && emContact instanceof EMGroup) {
                        // it is group chat
                        intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                        intent.putExtra("groupId", ((EMGroup) emContact).getGroupId());
                    } else {
                        // it is single chat
//						intent.putExtra("userId", username);
                    }
                    mContext.startActivity(intent);
                }
            }
        });

        return convertView;
    }

    /**
     * 对外提供改变服务状态的方法
     *
     * @param b
     */
    public void changeServiceState(boolean b) {
        if (b == false)
            holder.serState.setVisibility(View.VISIBLE);
        else
            holder.serState.setVisibility(View.GONE);
    }

    /**
     * 根据消息内容和消息类型获取消息内容提示
     *
     * @param message
     * @param context
     * @return
     */
    private String getMessageDigest(EMMessage message, Context context) {
        String digest = "";
        switch (message.getType()) {
            case LOCATION: // 位置消息
                if (message.direct == EMMessage.Direct.RECEIVE) {
                    // 从sdk中提到了ui中，使用更简单不犯错的获取string的方法
                    // digest = EasyUtils.getAppResourceString(context,
                    // "location_recv");
                    digest = getStrng(context, R.string.location_recv);
                    digest = String.format(digest, message.getFrom());
                    return digest;
                } else {
                    // digest = EasyUtils.getAppResourceString(context,
                    // "location_prefix");
                    digest = getStrng(context, R.string.location_prefix);
                }
                break;
            case IMAGE: // 图片消息
                ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
                digest = getStrng(context, R.string.picture);
                break;
            case VOICE:// 语音消息
                digest = getStrng(context, R.string.voice);
                break;
            case VIDEO: // 视频消息
                digest = getStrng(context, R.string.video);
                break;
            case TXT: // 文本消息
                if (!message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    TextMessageBody txtBody = (TextMessageBody) message.getBody();
                    digest = txtBody.getMessage();
                } else {
                    TextMessageBody txtBody = (TextMessageBody) message.getBody();
                    digest = getStrng(context, R.string.voice_call) + txtBody.getMessage();
                }
                break;
            case FILE: // 普通文件消息
                digest = getStrng(context, R.string.file);
                break;
            default:
                System.err.println("error, unknow type");
                return "";
        }

        return digest;
    }

    private static class ViewHolder {
        /**
         * 和谁的聊天记录
         */
        TextView name;
        /**
         * 消息未读数
         */
        TextView unreadLabel;
        /**
         * 最后一条消息的内容
         */
        TextView message;
        /**
         * 最后一条消息的时间
         */
        TextView time;
        /**
         * 用户头像
         */
        ImageView avatar;
        /**
         * 最后一条消息的发送状态
         */
        View msgState;
        /**
         * 整个list中每一行总布局
         */
        RelativeLayout list_item_layout;
        /**
         * 服务状态
         */
        TextView serState;
        ImageView ivsort;

    }

    String getStrng(Context context, int resId) {
        return context.getResources().getString(resId);
    }


    public int getAllUnHandleNumsNewFriends() {
        if (EMChatManager.getInstance() != null && PreferencesUtil.getLogin(getContext())) {
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


    @Override
    public Filter getFilter() {
        if (conversationFilter == null) {
            conversationFilter = new ConversationFilter(conversationList);
        }
        return conversationFilter;
    }

    public void reSetFilter() {
        copyConversationList.clear();
        copyConversationList.addAll(conversationList);
    }

    private class ConversationFilter extends Filter {
        List<EMConversation> mOriginalValues = null;

        public ConversationFilter(List<EMConversation> mList) {
            mOriginalValues = mList;
        }

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                mOriginalValues = new ArrayList<EMConversation>();
            }
            if (prefix == null || prefix.length() == 0) {
                results.values = copyConversationList;
                results.count = copyConversationList.size();
            } else {
                String prefixString = prefix.toString();
                mOriginalValues.clear();
                mOriginalValues.addAll(copyConversationList);
                final int count = mOriginalValues.size();
                final ArrayList<EMConversation> newValues = new ArrayList<EMConversation>();

                for (int i = 0; i < count; i++) {
                    final EMConversation value = mOriginalValues.get(i);
                    String username = value.getUserName();
                    if (value.isGroup()) {
                        username = EMGroupManager.getInstance().getGroup(username).getGroupName();
                    } else {
                        username = XJContactHelper.selectContact(username).getNick();
                    }

                    // First match against the whole ,non-splitted value
                    if (username.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        final String[] words = username.split(" ");
                        final int wordCount = words.length;

                        // Start at index 0, in case valueText starts with space(s)
                        for (int k = 0; k < wordCount; k++) {
                            if (words[k].startsWith(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if (results.count > 0) {
                conversationList.clear();
                conversationList.addAll((List<EMConversation>) results.values);
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }

        }


    }
}
