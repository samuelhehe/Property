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

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.repo.xw.library.views.PullListView;
import com.repo.xw.library.views.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.greenrobot.event.EventBus;
import xj.property.Constant;
import xj.property.R;
import xj.property.adapter.NewFriendsMsgAdapter;
import xj.property.event.NewFriendsProcessedBackEvent;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;


/**
 * 申请与通知
 */
public class NewFriendsMsgActivity extends HXBaseActivity implements PullToRefreshLayout.OnRefreshListener {
    private static final int LOAD_COMPLETE = 1;
    private PullListView listView;
    private EMConversation conversation;
    private List<EMMessage> allMessages = new ArrayList<>();

    private NewFriendsMsgAdapter adapter;
    private int pageNum = 10;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message hmsg) {
            super.handleMessage(hmsg);
            switch (hmsg.what) {
                case LOAD_COMPLETE:
                    pull_relay.autoRefresh();
                    break;
            }

        }
    };
    private PullToRefreshLayout pull_relay;
    private AlertDialog complainDialog;


    private void InitDialog(final int postion) {
        View rootView = View.inflate(this, R.layout.dialog_group_addordelete_confrim_mgr, null);
        complainDialog = new AlertDialog.Builder(this).setView(rootView).create();
        final TextView mytags_del_confirm_tv = (TextView) rootView.findViewById(R.id.mytags_del_confirm_tv);
        mytags_del_confirm_tv.setText("确定删除该条消息");
        rootView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complainDialog.dismiss();


            }
        });
        rootView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complainDialog.dismiss();

                EMMessage tobeDeletemsg = adapter.getItem(postion);
                // 删除此消息
                EMChatManager.getInstance().getConversation(Constant.NEW_FRIENDS_USERNAME).removeMessage(tobeDeletemsg.getMsgId());
                adapter.remove(tobeDeletemsg);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }
        });
        complainDialog.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friends_msg);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("验证消息");
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        pull_relay = (com.repo.xw.library.views.PullToRefreshLayout) findViewById(R.id.pull_relay);
        pull_relay.setOnRefreshListener(this);
        listView = (PullListView) findViewById(R.id.list);
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                //// 弹出是否要删除该消息的对话框
//                InitDialog(position);
//                return false;
//            }
//        });
//        registerForContextMenu(listView);
        adapter = new NewFriendsMsgAdapter(this, allMessages);
        listView.setAdapter(adapter);
        initData();

//
//        InviteMessgeDao dao = new InviteMessgeDao(this);
//		List<InviteMessage> msgs = dao.getMessagesList();
//		//设置adapter
//		NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(this, allMessages);
//		listView.setAdapter(adapter);
//		XjApplication.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME).setUnreadMsgCount(0);
//
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // if(((AdapterContextMenuInfo)menuInfo).position > 0){ m,
        getMenuInflater().inflate(R.menu.delete_message, menu);
        // }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_message) {
            EMMessage tobeDeletemsg = adapter.getItem(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
            // 删除此消息
            EMChatManager.getInstance().getConversation(Constant.NEW_FRIENDS_USERNAME).removeMessage(tobeDeletemsg.getMsgId());
            adapter.remove(tobeDeletemsg);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);

            return true;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        refreshData();
    }

    private void refreshData() {

        conversation = EMChatManager.getInstance().getConversation(Constant.NEW_FRIENDS_USERNAME);
        if (conversation != null) {
            conversation.markAllMessagesAsRead();
            if (conversation.getAllMsgCount() > 0) {
                allMessages.clear();
                lastemMessages = conversation.getAllMessages();
                allMessages.addAll(lastemMessages);
                sortMessageDescByTime(allMessages);
                adapter.notifyDataSetChanged();
                pull_relay.refreshFinish(true);
            } else {
                allMessages.clear();
                adapter.notifyDataSetChanged();
                pull_relay.refreshFinish(true);
            }

        }
    }

    List<EMMessage> lastemMessages;

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        conversation = EMChatManager.getInstance().getConversation(Constant.NEW_FRIENDS_USERNAME);
        if (conversation != null) {
            conversation.markAllMessagesAsRead();
            if (conversation.getAllMsgCount() > 0) {
                String msgId = null;
                if (lastemMessages != null && lastemMessages.size() > 0) {
                    msgId = lastemMessages.get(0).getMsgId();
//                    msgId = lastemMessages.get(lastemMessages.size() - 1).getMsgId();
                }
                if (!TextUtils.isEmpty(msgId)) {
                   /*
                         loadMoreMsgFromDB(java.lang.String startMsgId, int pageSize)
                        根据传入的参数从db加载startMsgId之前(存储顺序)指定数量的message， 加载到的messages会加入到当前conversation的messages里
                       */
                    lastemMessages = conversation.loadMoreMsgFromDB(msgId, pageNum);
                    if (lastemMessages == null || lastemMessages.size() <= 0) {
                        if (allMessages.size() > 0) {
                            showNoMoreToast();
                        }
                    } else {
                        listView.setPullUpEnable(false);
                    }
                    allMessages.addAll(lastemMessages);
                    sortMessageDescByTime(allMessages);
                }
                pull_relay.loadMoreFinish(true);
                adapter.notifyDataSetChanged();
            }
        }


    }

    private void initData() {

        if (EMChat.getInstance() != null && EMChat.getInstance().isLoggedIn() && PreferencesUtil.getLogin(getmContext())) {
            // ** 免登陆情况 加载所有本地群和会话
            //不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
            //加上的话保证进了主页面会话和群组都已经load完毕
//            XJMessageHelper.loadNativeMessage();
//            EMChatManager.getInstance().loadAllConversations();

            EMChatManager.getInstance().loadAllConversations(new EMCallBack() {
                @Override
                public void onSuccess() {
                    if (handler != null) {
                        handler.sendEmptyMessage(LOAD_COMPLETE);
                    }
                }

                @Override
                public void onError(int i, String s) {
                    pull_relay.autoRefresh();
                }

                @Override
                public void onProgress(int i, String s) {
                    pull_relay.autoRefresh();
                }
            });

        }

    }

    public void onEvent(NewFriendsProcessedBackEvent newfriendsprocessedbackevent) {

//        if (newfriendsprocessedbackevent != null && newfriendsprocessedbackevent.getEmessage() != null) {
////            initData();
//            EMMessage emessage = newfriendsprocessedbackevent.getEmessage();
//
//            EMMessage emMessagelist = (EMMessage) msgMap.get(emessage.getMsgId());
//
//            emMessagelist = emessage;
//
//            EMChatManager.getInstance().updateMessageBody(emessage);
//
//            if (adapter != null) {
//                adapter.notifyDataSetChanged();
//            }
//
//        }

        initData();
    }

    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        refreshData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (complainDialog != null) {
            complainDialog.dismiss();
        }
    }

    public void back(View view) {
        finish();
    }


    @Override
    public void onClick(View v) {


    }

    public void sortMessageDescByTime(List<EMMessage> messageList) {

        Collections.sort(messageList, new Comparator<EMMessage>() {
            @Override
            public int compare(EMMessage lhs, EMMessage rhs) {
                if (lhs.getMsgTime() == rhs.getMsgTime()) {
                    return 0;
                } else if (lhs.getMsgTime() > rhs.getMsgTime()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
    }

    /**
     * 覆盖手机返回键
     */
    @Override
    public void onBackPressed() {
        if (getIntent().getBooleanExtra(Config.INTENT_BACKMAIN, false)) {
            startActivity(new Intent(this, MainActivity.class));
            return;
        }
        super.onBackPressed();
    }


}
