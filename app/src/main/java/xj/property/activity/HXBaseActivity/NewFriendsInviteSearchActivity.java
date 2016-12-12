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

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.repo.xw.library.views.PullListView;
import com.repo.xw.library.views.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.adapter.GroupInviteSearchAdapterForNeighbor;
import xj.property.beans.AddGroupMessageReqBean;
import xj.property.beans.AddGroupMessageRespBean;
import xj.property.beans.AddMemberToGroupReqBean;
import xj.property.beans.AddMemberToGroupRespBean;
import xj.property.beans.HomeSearchResultRespBean;
import xj.property.beans.SearchUserResultRespBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.event.FriendsChoicedBackEvent;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseUtils;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.ToastUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;


public class NewFriendsInviteSearchActivity extends HXBaseActivity implements PullToRefreshLayout.OnRefreshListener {

    private static final String stype_neighbor = "user"; /// 搜索类型为邻居
    private String groupId;
    private String searchName;
    private EditText group_search_key_et;
    private LinearLayout ll_errorpage;
    private LinearLayout ll_neterror;
    private LinearLayout ll_index_empty;
    private ImageView ll_index_empty_iv;
    private PullToRefreshLayout pull_to_refreshlayout;
    private PullListView pull_listview;
    private List<SearchUserResultRespBean> userresults = new ArrayList<>();
    private GroupInviteSearchAdapterForNeighbor searchMessageAdapter;
    private UserInfoDetailBean userbean;
    private int pageIndex = 1;

//    private Map<String, Boolean> checkedList = new HashMap<>();

    private TextView tv_right_text;
    private boolean isPublishActivity;
    private String groupowner;

    /**
     * 原群内成员包含群主
     */
    private List<String> membersInGroup = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friends_invite_search_process);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("邀请邻居");
        groupId = getIntent().getStringExtra("groupId");
        groupowner = getIntent().getStringExtra("groupowner");
        searchName = getIntent().getStringExtra("searchName");
        isPublishActivity = getIntent().getBooleanExtra("isPublishActivity", false);
        if (!isPublishActivity) {
            membersInGroup = getIntent().getStringArrayListExtra("membersInGroup");
        }
        if (isPublishActivity) {
            //// 如果是发布活动进来的, 则默认添加群主ID
            membersInGroup.clear();
            membersInGroup.add(groupowner);
        }

        userbean = PreferencesUtil.getLoginInfo(getmContext());
        tv_right_text = (TextView) findViewById(R.id.tv_right_text);
        tv_right_text.setText("邀请");
        tv_right_text.setVisibility(View.VISIBLE);
        tv_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /// 邀请邻居入群
                goInvite();
            }
        });

        group_search_key_et = (EditText) findViewById(R.id.group_search_key_et);
        group_search_key_et.setText(searchName);
        group_search_key_et.setSelection(group_search_key_et.length());
        group_search_key_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (group_search_key_et.getText().toString().length() == 0) {
//                        search_clear.setVisibility(View.INVISIBLE);
                        showToast("请输入搜索内容");
//                        resetSearchResult();
                    } else {
                        searchName = group_search_key_et.getText().toString();
//                        home_index_search_result_llay.setVisibility(View.VISIBLE);
                        pageIndex = 1;
                        search(searchName);
                    }
                }
                return false;
            }
        });

        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        ll_index_empty = (LinearLayout) findViewById(R.id.ll_index_empty);
        ll_index_empty_iv = (ImageView) findViewById(R.id.ll_index_empty_iv);
        ll_index_empty_iv.setImageResource(R.drawable.vote_index_none);

        ll_neterror.setOnClickListener(this);

        pull_to_refreshlayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refreshlayout);
        pull_to_refreshlayout.setOnRefreshListener(this);

        pull_listview = (PullListView) findViewById(R.id.pull_listview);
//        pull_listview.setPullUpEnable(false);
        pull_listview.setPullDownEnable(false); /// 禁用刷新

        searchMessageAdapter = new GroupInviteSearchAdapterForNeighbor(this, userresults, membersInGroup, searchName);
        pull_listview.setAdapter(searchMessageAdapter);
        pull_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (userresults != null && userresults.size() > 0) {

                    SearchUserResultRespBean searchUserResultRespBean = userresults.get(position);

                    GroupInviteSearchAdapterForNeighbor.ViewHolder vh = (GroupInviteSearchAdapterForNeighbor.ViewHolder) view.getTag();

//                    ToastUtils.showToast(getmContext(), "size: "+ pull_listview.getCount() +" position "+ position );
                    if (membersInGroup != null && membersInGroup.contains(searchUserResultRespBean.getEmobId())) {
                        ToastUtils.showToast(getmContext(), "已经是群成员了");
                        if (searchMessageAdapter.getMapLists().contains(searchUserResultRespBean.getEmobId())) {
                            searchMessageAdapter.getMapLists().remove(searchUserResultRespBean.getEmobId());
                        }
                        vh.search_neighbor_cb.setChecked(false);
                        return;
                    }

                    vh.search_neighbor_cb.setChecked(!vh.search_neighbor_cb.isChecked());
                    if (searchMessageAdapter.getMapLists() != null) {
                        if (vh.search_neighbor_cb.isChecked()) {
                            searchMessageAdapter.getMapLists().add(searchUserResultRespBean.getEmobId());
                        } else {
                            searchMessageAdapter.getMapLists().remove(searchUserResultRespBean.getEmobId());
                        }
                    }

//                    searchMessageAdapter.notifyDataSetChanged();

//                    userresults.get(position).isChecked = !userresults.get(position).isChecked;

//                    searchMessageAdapter.notifyDataSetChanged();
                }

            }
        });

        pageIndex = 1;
        search(searchName);

    }

    private void goInvite() {
        if (searchMessageAdapter.getMapLists() == null || searchMessageAdapter.getMapLists().size() <= 0) {
            showToast("请选择要邀请的邻居");
            return;
        }
        if (mLdDialog != null) {
            mLdDialog.show();
        }
        ArrayList<String> choicedEmobid = new ArrayList<String>();
//        for (Map.Entry<String, Boolean> entry : checkedList.entrySet()) {
//            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
//            choicedEmobid.add(entry.getKey());
//        }

        choicedEmobid.addAll(searchMessageAdapter.getMapLists());

        String[] sa = new String[choicedEmobid.size()];
//            EMGroupManager.getInstance().addUsersToGroup(groupId, choicedEmobid.toArray(sa));
        /// 启动子线程添加群成员
        addMembersToGroup(choicedEmobid.toArray(sa), groupId, groupowner);

//            EMGroupManager.getInstance().inviteUser();

        // The group administrator calls this method to add people to the group.
//            EMGroupManager.getInstance().addUsersToGroup(groupId, newmembers);//need to handle asynchronously

// If allowing group members to invite people into a private group, the group members can call the following method to invite people.
//            EMGroupManager.getInstance().inviteUser(groupId, newmembers, null);//need to handle asynchronously

        //delete username from the group chat.
//            EMGroupManager.getInstance().removeUserFromGroup(groupId, username);//need to handle asynchronously
    }


    /**
     * 增加群成员
     *
     * @param newmembers
     */
    private void addMembersToGroup(final String[] newmembers, final String groupId, final String groupowner) {
        new Thread(new Runnable() {

            public void run() {
                try {
                    // 创建者调用add方法
                    if (EMChatManager.getInstance().getCurrentUser().equals(groupowner)) {
                        EMGroupManager.getInstance().addUsersToGroup(groupId, newmembers);
                    } else {
                        // 一般成员调用invite方法
                        EMGroupManager.getInstance().inviteUser(groupId, newmembers, null);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ///添加群成员至群组
                            String n_messageId = UUID.randomUUID().toString().replaceAll("-", "");
                            AddGroupMessageReqBean quaryToken = new AddGroupMessageReqBean();
                            StringBuilder emobIdTo = new StringBuilder();
                            for (String emobid : newmembers) {
                                emobIdTo.append(emobid).append(",");
                            }
                            if (!TextUtils.isEmpty(emobIdTo)) {
                                quaryToken.setEmobIdTo(emobIdTo.substring(0, emobIdTo.length() - 1));
                            }
//                            showToast("emobIdTo: " + emobIdTo);
                            quaryToken.setEmobIdFrom(groupId);
                            quaryToken.setType("invited");
                            quaryToken.setGroupId(groupId);
                            quaryToken.setMessageContent("邀请入群");
                            quaryToken.setMessageId(n_messageId);
                            quaryToken.setCommunityId(PreferencesUtil.getCommityId(getmContext()));
                            addAGroupMessageCall(quaryToken );
                        }
                    });
                } catch (final Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ToastUtils.showToast(getApplicationContext(), "添加群成员失败");
                            Log.d("NewFriendInviteSearch", "添加群成员失败: " + e.getMessage());
                        }
                    });
                }


            }
        }).start();
    }


    private void search(String searchName) {
        getIndexSearchDataList(searchName);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

        pageIndex++;
        search(searchName);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ll_neterror:
                pageIndex = 1;
                search(searchName);
                break;
        }
    }

    interface IndexSearchService {
//        @GET("/api/v2/communities/{communityId}/home/user/{emobId}/serachResult")
//        void getIndexSearchDataList(@Path("communityId") long communityId, @Path("emobId") String emobId, @QueryMap Map<String, String> map, Callback<HomeSearchResultRespBean> cb);
//        @GET("/api/v2/communities/{communityId}/home/user/{emobId}/serachResult")

        @GET("/api/v3/home/search")
        void getIndexSearchDataList(@QueryMap HashMap<String, String> map, Callback<CommonRespBean<HomeSearchResultRespBean>> cb);
    }

    private void getIndexSearchDataList(String s) {
        userbean = PreferencesUtil.getLoginInfo(getmContext());
        HashMap<String, String> option = new HashMap<>();
        option.put("q", s);
        option.put("emobId", userbean==null? PreferencesUtil.getTourist(getmContext()):userbean.getEmobId());
        option.put("communityId", "" + PreferencesUtil.getCommityId(getmContext()));
        option.put("type", stype_neighbor);
        option.put("page", "" + pageIndex);
        option.put("limit", "10");

        IndexSearchService service = RetrofitFactory.getInstance().create(getmContext(), option, IndexSearchService.class);
        Callback<CommonRespBean<HomeSearchResultRespBean>> callback = new Callback<CommonRespBean<HomeSearchResultRespBean>>() {
            @Override
            public void success(CommonRespBean<HomeSearchResultRespBean> resultBean, Response response) {

                if (resultBean != null && resultBean.getData() != null && "yes".equals(resultBean.getStatus())) {
                    ll_errorpage.setVisibility(View.GONE);
                    ll_neterror.setVisibility(View.GONE);
                    final HomeSearchResultRespBean.UsersEntity users = resultBean.getData().getUsers();
                    /// 没有搜索结果
                    if (users == null) {
                        if (userresults != null && userresults.size() > 0) {
                            showNoMoreToast();
                        } else {
                            ll_errorpage.setVisibility(View.VISIBLE);
                            ll_neterror.setVisibility(View.GONE);
                            /// TODO 显示没有搜索到结果
                        }
                    } else {
                        List<SearchUserResultRespBean> usersbeans = users.getPageData();
                        if (usersbeans != null && usersbeans.size() > 0) {
                            if (pageIndex == 1) {
                                userresults.clear();
                            }
                            userresults.addAll(usersbeans);
                            searchMessageAdapter.setSearchKey(searchName);
                            searchMessageAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
//                    showNetErrorToast();
                    ll_errorpage.setVisibility(View.GONE);
                    ll_neterror.setVisibility(View.GONE);
//                    ll_nomessage.setVisibility(View.GONE);
                    /// TODO 显示没有搜索到结果
                }
                pull_to_refreshlayout.loadMoreFinish(true);
            }
            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                pull_to_refreshlayout.loadMoreFinish(true);
                showNetErrorToast();
            }
        };
        service.getIndexSearchDataList( option, callback);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new FriendsChoicedBackEvent());
    }

    private void addAGroupMessageCall(final AddGroupMessageReqBean quaryToken) {
        NetBaseUtils.addAGroupMessage(getmContext(), quaryToken, new NetBaseUtils.NetRespListener<CommonRespBean<String>>() {
            @Override
            public void successYes(CommonRespBean<String> commonRespBean, Response response) {
                if (mLdDialog != null) {
                    mLdDialog.dismiss();
                }
                if (isPublishActivity) {
                    /// 跳转至活动列表
                    showToast("添加成功");
                    setResult(RESULT_OK);
                    onBackPressed();
                } else {
                    /// TODO 跳转至群组详情
                    if (quaryToken != null && !TextUtils.isEmpty(quaryToken.getEmobIdTo())) {
                        List<String> newemboids = Arrays.asList(quaryToken.getEmobIdTo().split(","));
                        EventBus.getDefault().post(new FriendsChoicedBackEvent(FriendsChoicedBackEvent.FLAG_MEMBERS_ADD, newemboids));
                        setResult(RESULT_OK);
                        finish();
                    }
                }
            }

            @Override
            public void successNo(CommonRespBean<String> commonRespBean, Response response) {
                if (mLdDialog != null) {
                    mLdDialog.dismiss();
                }
                showDataErrorToast(commonRespBean.getMessage());
                Log.d("NewFriendsMsgProcessActivity", " addAGroupMessageCall no msg: " + commonRespBean.getMessage());
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                if (mLdDialog != null) {
                    mLdDialog.dismiss();
                }
            }
        });
    }

}
