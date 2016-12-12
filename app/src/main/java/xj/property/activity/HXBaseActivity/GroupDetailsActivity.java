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
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.NetUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;
import xj.property.R;
import xj.property.XjApplication;
import xj.property.activity.tags.GroupAddOrDeleteConfrimDialog;
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.adapter.GroupMembersForDetailAdapter;
import xj.property.beans.UserGroupBean;
import xj.property.beans.UserGroupBeanForDel;
import xj.property.beans.UserInfoDetailBean;
import xj.property.cache.GroupInfo;
import xj.property.event.FriendsChoicedBackEvent;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.netbasebean.DestoryGroupReqBean;
import xj.property.netbasebean.ExitGroupReqBean;
import xj.property.utils.other.BlackListHelper;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.ExpandGridView;
import xj.property.widget.GroupHeaderHelper;

public class GroupDetailsActivity extends HXBaseActivity implements OnClickListener, GroupAddOrDeleteConfrimDialog.OnMemberAddDeleteListener {
    private static final String TAG = "GroupDetailsActivity";
    private static final int REQUEST_CODE_ADD_USER = 0;
    private static final int REQUEST_CODE_EXIT = 1;
    private static final int REQUEST_CODE_EXIT_DELETE = 2;
    private static final int REQUEST_CODE_CLEAR_ALL_HISTORY = 3;
    private static final int REQUEST_CODE_ADD_TO_BALCKLIST = 4;
    private static final int REQUEST_CODE_EDIT_GROUPNAME = 5;

    String longClickUsername = null;

    private ExpandGridView userGridview;
    private String groupId;
    private ProgressBar loadingPB;
    private Button exitBtn;
    //private Button deleteBtn;
    private EMGroup group;

    private int referenceWidth;
    private int referenceHeight;
    private ProgressDialog progressDialog;

    private RelativeLayout rl_switch_block_groupmsg;
    private RelativeLayout rl_switch_block_groupnotify;

    private RelativeLayout rl_set;
    /**
     * 屏蔽群消息imageView
     */
    private ImageView iv_switch_block_groupmsg;
    /**
     * 关闭屏蔽群消息imageview
     */
    private ImageView iv_switch_unblock_groupmsg;
    /**
     * 不提示群消息imageView
     */
    private ImageView iv_switch_block_notify;
    /**
     * 提示群消息imageview
     */
    private ImageView iv_switch_unblock_notify;

    public static GroupDetailsActivity instance;

    // 清空所有聊天记录
    private RelativeLayout clearAllHistory;
    private RelativeLayout blacklistLayout;
    private RelativeLayout changeGroupNameLayout;
    private List<UserGroupBean> beans = new ArrayList<>();
    private GroupAddOrDeleteConfrimDialog groupAddOrDelDialog;
    private UserInfoDetailBean userInfoDetailBean;
    private Button deleteBtn;
    private ArrayList<String> membersInGroup = new ArrayList<>();
    private GroupMembersForDetailAdapter detailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);
        instance = this;
        userInfoDetailBean = PreferencesUtil.getLoginInfo(getmContext());

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        clearAllHistory = (RelativeLayout) findViewById(R.id.clear_all_history);
        userGridview = (ExpandGridView) findViewById(R.id.gridview);
        loadingPB = (ProgressBar) findViewById(R.id.progressBar);
        exitBtn = (Button) findViewById(R.id.btn_exit_grp);
        deleteBtn = (Button) findViewById(R.id.btn_exitdel_grp);
        blacklistLayout = (RelativeLayout) findViewById(R.id.rl_blacklist);
        changeGroupNameLayout = (RelativeLayout) findViewById(R.id.rl_change_group_name);
        rl_switch_block_groupmsg = (RelativeLayout) findViewById(R.id.rl_switch_block_groupmsg);
        iv_switch_block_groupmsg = (ImageView) findViewById(R.id.iv_switch_block_groupmsg);
        iv_switch_unblock_groupmsg = (ImageView) findViewById(R.id.iv_switch_unblock_groupmsg);
        rl_switch_block_groupmsg.setOnClickListener(this);
        rl_switch_block_groupnotify = (RelativeLayout) findViewById(R.id.rl_switch_block_notify);
        iv_switch_block_notify = (ImageView) findViewById(R.id.iv_switch_block_notify);
        iv_switch_unblock_notify = (ImageView) findViewById(R.id.iv_switch_unblock_notify);
        rl_switch_block_groupnotify.setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_mine_title)).setText("群组人数");
        Drawable referenceDrawable = getResources().getDrawable(R.drawable.smiley_add_btn);
        referenceWidth = referenceDrawable.getIntrinsicWidth();
        referenceHeight = referenceDrawable.getIntrinsicHeight();

        // 获取传过来的groupid
        groupId = getIntent().getStringExtra("groupId");
        group = EMGroupManager.getInstance().getGroup(groupId);
        if (PreferencesUtil.getUnNotifyGroupS(this).contains(groupId)) {

            iv_switch_block_notify.setVisibility(View.VISIBLE);
            iv_switch_unblock_notify.setVisibility(View.INVISIBLE);
        }
        blacklistLayout.setVisibility(View.GONE);
//        changeGroupNameLayout.setVisibility(View.GONE);
        /*if (group.getOwner() == null || "".equals(group.getOwner())
                || !group.getOwner().equals(EMChatManager.getInstance().getCurrentUser())) {
			exitBtn.setVisibility(View.GONE);
		//	deleteBtn.setVisibility(View.GONE);
			blacklistLayout.setVisibility(View.GONE);
			changeGroupNameLayout.setVisibility(View.GONE);
		}*/
        // 如果自己是群主，显示解散按钮
        //没有解散群
    /*	if (EMChatManager.getInstance().getCurrentUser().equals(group.getOwner())) {
            exitBtn.setVisibility(View.GONE);
			deleteBtn.setVisibility(View.VISIBLE);
		}*/
//		((TextView) findViewById(R.id.group_name)).setText(group.getGroupName() + "(" + group.getAffiliationsCount() + "人)");
        ((TextView) findViewById(R.id.group_name)).setText(group.getGroupName());
        ((TextView) findViewById(R.id.group_name_content_tv)).setText(group.getGroupName());


        beans = new ArrayList<>();

//        adapter = new XJBaseAdapter(this, R.layout.gruop_user_header, beans, new String[]{"nickname"}, new String[]{"getAvatar"},
//                new DisplayImageOptions.Builder()
//                        .cacheInMemory(true).cacheOnDisk(true)
//                        .showImageOnFail(R.drawable.head_portrait_personage)
//                        .showImageForEmptyUri(R.drawable.head_portrait_personage).build());//new GridAdapter(this, R.layout.grid, group.getMembers());

        detailAdapter = new GroupMembersForDetailAdapter(this, beans);

        userGridview.setAdapter(detailAdapter);

        // 保证每次进详情看到的都是最新的group
        updateGroup();
        userGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (beans.get(position).getEmobId().equals(PreferencesUtil.getLoginInfo(GroupDetailsActivity.this).getEmobId())) {
                    Toast.makeText(GroupDetailsActivity.this, "这是你自己", Toast.LENGTH_SHORT).show();
                    return;
                } else if (beans.get(position).getEmobId().equals("group_details_plus")) {

                    Intent intent = new Intent(GroupDetailsActivity.this, NewFriendsInviteActivity.class);
                    intent.putExtra("isPublishActivity", false);
                    intent.putExtra("groupId", groupId);
                    intent.putExtra("groupowner", EMGroupManager.getInstance().getGroup(groupId).getOwner());
                    if (membersInGroup != null && membersInGroup.size() > 0) {
                        intent.putExtra("membersInGroup", membersInGroup);
                    }
                    startActivity(intent);
                } else if (beans.get(position).getEmobId().equals("group_details_minus")) {
                    Intent intent = new Intent(GroupDetailsActivity.this, NewFriendsDeleteActivity.class);
                    intent.putExtra("groupId", groupId);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(GroupDetailsActivity.this, UserGroupInfoActivity.class);
                    intent.putExtra(Config.INTENT_PARMAS1, beans.get(position));
                    startActivity(intent);
                }
            }
        });

        // 设置OnTouchListener
        /*userGridview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (adapter.isInDeleteMode) {
						adapter.isInDeleteMode = false;
						adapter.notifyDataSetChanged();
						return true;
					}
					break;
				default:
					break;
				}
				return false;
			}
		});*/

        clearAllHistory.setOnClickListener(this);
        blacklistLayout.setOnClickListener(this);
        changeGroupNameLayout.setOnClickListener(this);

        getGroupInfo(groupId);

    }

    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }


    public void onEvent(FriendsChoicedBackEvent choicededbackevent) {
        if (choicededbackevent != null) {

            ////  如果是邀请人员界面过来,则显示邀请了几个人, 刷新界面

            if (choicededbackevent.getFlag() == FriendsChoicedBackEvent.FLAG_MEMBERS_DELETE) {

                if (choicededbackevent.getMapLists() != null && choicededbackevent.getMapLists().size() > 0) {
                    groupAddOrDelDialog = new GroupAddOrDeleteConfrimDialog(getmContext(), choicededbackevent, GroupDetailsActivity.this);
                    groupAddOrDelDialog.setCancelable(true);
                    groupAddOrDelDialog.show();
                }

            } else if (choicededbackevent.getFlag() == FriendsChoicedBackEvent.FLAG_MEMBERS_ADD) {

                showToast("邀请成功");

//                if (choicededbackevent.getEmobIds() != null && choicededbackevent.getEmobIds().size() > 0) {
//                    groupAddOrDelDialog = new GroupAddOrDeleteConfrimDialog(getmContext(), choicededbackevent, GroupDetailsActivity.this);
//                    groupAddOrDelDialog.setCancelable(true);
//                    groupAddOrDelDialog.show();
//                }
                /// 刷新群组信息
                getGroupInfo(groupId);

            }
            ///  如果是从删除界面过来, 则显示删除XXX, XX等人,是否去确定, 确定之后删除.

        }
    }

    @Override
    public void onMemberAddOK(FriendsChoicedBackEvent friendsChoicedBackEvent) {

    }

    @Override
    public void onMemberAddCancel(FriendsChoicedBackEvent friendsChoicedBackEvent) {

    }

    @Override
    public void onMemberDelSuccessOK(FriendsChoicedBackEvent friendsChoicedBackEvent) {

        if (friendsChoicedBackEvent != null) {

            Map<String, UserGroupBeanForDel> mapLists = friendsChoicedBackEvent.getMapLists();
            StringBuilder deletesb = new StringBuilder();

//            ArrayList<String> choicedEmobid = new ArrayList<String>();
            for (Map.Entry<String, UserGroupBeanForDel> entry : mapLists.entrySet()) {
                System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
//                choicedEmobid.add(entry.getKey());
                deletesb.append(entry.getValue().getEmobId()).append(",");
            }

//            String[] sa = new String[choicedEmobid.size()];

            exitGrop(groupId, deletesb.substring(0, deletesb.length() - 1));

        }


    }

    @Override
    public void onMemberDelSuccessCancel(FriendsChoicedBackEvent friendsChoicedBackEvent) {
        /// 刷新群组信息
        getGroupInfo(groupId);
    }


    interface getOrderInfoService {
        ///api/v1/communities/{communityId}/groups/{groupId}/members
//        @GET("/api/v1/communities/{communityId}/groups/{groupId}/members")
        @GET("/api/v3/activities/{groupId}/members")
        void extractGroupMembers(@Path("groupId") String groupId, Callback<CommonRespBean<List<UserGroupBean>>> cb);

//        @DELETE("/api/v1/communities/{communityId}/users/{emobId}/activities/group/{emobGroupId}")
//        void destoryGroup(@Header("signature") String signature,@Path("communityId") int communityId, @Path("emobId") String emobId,  @Path("emobGroupId") String groupId, Callback<DestoryGroupRespBean> cb);
//

//        @DELETE("/api/v1/communities/{communityId}/users/{emobId}/activities/group/{emobGroupId}")
        @PUT("/api/v3/activities")  ///v3 2016/03/03  解散群组
        void destoryGroup(@Body DestoryGroupReqBean destoryGroupReqBean, Callback<CommonRespBean<String>> cb);
    }

    /**
     * 解散群组
     * @param groupId
     */
    private void destoryGroupCall(final String groupId) {
        DestoryGroupReqBean destoryGroupReqBean = new DestoryGroupReqBean();
        destoryGroupReqBean.setEmobIdOwner(PreferencesUtil.getLoginInfo(getmContext()).getEmobId());
        destoryGroupReqBean.setEmobGroupId(groupId);

        getOrderInfoService service = RetrofitFactory.getInstance().create(getmContext(),destoryGroupReqBean,getOrderInfoService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, retrofit.client.Response response) {
                progressDialog.dismiss();
                if (bean != null && "yes".equals(bean.getStatus())) {
                    showToast("解散成功");
                    finish();
                    ChatActivity.activityInstance.finish();
                } else if (bean != null && "no".equals(bean.getStatus())) {
                    showToast("解散群组失败: " + bean.getMessage());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                showNetErrorToast();
                error.printStackTrace();
            }
        };

        service.destoryGroup(destoryGroupReqBean, callback);
    }

    /**
     * 拉取群组成员列表
     * v3 2016/02/29
     *
     * @param groupId
     */
    private void getGroupInfo(final String groupId) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(GroupDetailsActivity.this);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.setMessage("正在加载....");
        if (!isFinishing()) {
            progressDialog.show();
        }
        getOrderInfoService service = RetrofitFactory.getInstance().create(getmContext(),getOrderInfoService.class);
        Callback<CommonRespBean<List<UserGroupBean>>> callback = new Callback<CommonRespBean<List<UserGroupBean>>>() {
            @Override
            public void success(CommonRespBean<List<UserGroupBean>> bean, retrofit.client.Response response) {
                progressDialog.dismiss();
                beans.clear();
                beans.addAll(bean.getData());
                //// 获取群成员列表
                fetchEmobIdFromGroupInfo(bean.getData());
                ((TextView) findViewById(R.id.tv_mine_content)).setText(beans.size() + "/500");

                EMGroup emGroup = EMGroupManager.getInstance().getGroup(groupId);
                userInfoDetailBean = PreferencesUtil.getLoginInfo(getmContext());
                if (userInfoDetailBean != null) {
                    ///  确定当前用户是群主才能显示 +-号
                    if (TextUtils.equals(userInfoDetailBean.getEmobId(), emGroup.getOwner())) {

                        exitBtn.setVisibility(View.GONE);
                        deleteBtn.setVisibility(View.VISIBLE);

                        UserGroupBean ugb1 = new UserGroupBean();
                        ugb1.setEmobId("group_details_plus");
                        ugb1.setNickname("");
                        String path1 = "drawable://" + getResources().getIdentifier("group_details_plus", "drawable", "xj.property");
                        ugb1.setAvatar(path1);
                        beans.add(ugb1);

                        UserGroupBean ugb2 = new UserGroupBean();
                        ugb2.setEmobId("group_details_minus");
                        ugb2.setNickname("");
                        String path2 = "drawable://" + getResources().getIdentifier("group_details_minus", "drawable", "xj.property");
                        ugb2.setAvatar(path2);
                        beans.add(ugb2);
                    } else {
                        exitBtn.setVisibility(View.VISIBLE);
                        deleteBtn.setVisibility(View.GONE);
                    }
                } else {
                    exitBtn.setVisibility(View.VISIBLE);
                    deleteBtn.setVisibility(View.GONE);
                }
                detailAdapter.notifyDataSetChanged();
                if ("yes".equals(bean.getStatus()) && bean.getData().size() > 1) {
                    GroupHeaderHelper.createGroupId(bean.getData(), groupId, XjApplication.getInstance());
                }

            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                showNetErrorToast();
                error.printStackTrace();
            }
        };
        service.extractGroupMembers(groupId, callback);
    }

    private void fetchEmobIdFromGroupInfo(List<UserGroupBean> info) {
        if (info != null && info.size() > 0) {
            membersInGroup = new ArrayList<String>();
            membersInGroup.clear();
            for (UserGroupBean userGroupBean : info) {
                membersInGroup.add(userGroupBean.getEmobId());
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(GroupDetailsActivity.this);
                progressDialog.setMessage("正在添加...");
                progressDialog.setCanceledOnTouchOutside(false);
            }
            switch (requestCode) {
                case REQUEST_CODE_ADD_USER:// 添加群成员
                    final String[] newmembers = data.getStringArrayExtra("newmembers");
                    progressDialog.show();
                    addMembersToGroup(newmembers);
                    break;
                case REQUEST_CODE_EXIT: // 退出群
                    progressDialog.setMessage("正在退出群聊...");
                    progressDialog.show();
                    exitGrop(groupId, PreferencesUtil.getLoginInfo(this).getEmobId());
                    break;
                case REQUEST_CODE_EXIT_DELETE: // 解散群
                    progressDialog.setMessage("正在解散群聊...");
                    progressDialog.show();
//                    deleteGrop();
                    destoryGroupCall(groupId);
                    break;
                case REQUEST_CODE_CLEAR_ALL_HISTORY:
                    // 清空此群聊的聊天记录
                    progressDialog.setMessage("正在清空群消息...");
                    progressDialog.show();
                    clearGroupHistory();
                    break;

                case REQUEST_CODE_EDIT_GROUPNAME: //修改群名称
                    String groupName = data.getStringExtra(Config.EXPKey_GROUP);
                    if (groupName != null) {
                        ((TextView) findViewById(R.id.group_name)).setText(groupName);
                        group.setGroupName(groupName);
                    }
                /*final String returnData = data.getStringExtra("data");
                if(!TextUtils.isEmpty(returnData)){
					progressDialog.setMessage("正在修改群名称...");
					progressDialog.show();
					
					new Thread(new Runnable() {
						public void run() {
							try {
								EMGroupManager.getInstance().changeGroupName(groupId, returnData);
								runOnUiThread(new Runnable() {
									public void run() {
//										((TextView) findViewById(R.id.group_name)).setText(returnData + "(" + group.getAffiliationsCount()
//												+ "人)");
                                        ((TextView) findViewById(R.id.group_name)).setText(returnData);
										progressDialog.dismiss();
										Toast.makeText(getApplicationContext(), "修改群名称成功", 0).show();
									}
								});

							} catch (EaseMobException e) {
								e.printStackTrace();
								runOnUiThread(new Runnable() {
									public void run() {
										progressDialog.dismiss();
										Toast.makeText(getApplicationContext(), "改变群名称失败，请检查网络或稍后重试", 0).show();
									}
								});
							}
						}
					}).start();
				}*/
                    break;
                case REQUEST_CODE_ADD_TO_BALCKLIST:
                    progressDialog.setMessage("正在移入至黑名单");
                    progressDialog.show();
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                EMGroupManager.getInstance().blockUser(groupId, longClickUsername);
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        detailAdapter.notifyDataSetChanged();
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "移入黑名单成功", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (EaseMobException e) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "移入黑名单失败,请检查网络或稍后重试", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();

                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 点击退出群组按钮 REQUEST_CODE_EXIT
     *
     * @param view
     */
    public void exitGroup(View view) {
        startActivityForResult(new Intent(this, ExitGroupDialog.class), REQUEST_CODE_EXIT);

    }

    /**
     * 点击解散群组按钮
     *
     * @param view
     */
    public void exitDeleteGroup(View view) {
        startActivityForResult(new Intent(this, ExitGroupDialog.class).putExtra("deleteToast", getString(R.string.dissolution_group_hint)),
                REQUEST_CODE_EXIT_DELETE);

    }

    /**
     * 清空群聊天记录
     */
    public void clearGroupHistory() {

        EMChatManager.getInstance().clearConversation(group.getGroupId());
        progressDialog.dismiss();
        // adapter.refresh(EMChatManager.getInstance().getConversation(toChatUsername));

    }

    interface ExitGroupService {
        ///api/v1/communities/{communityId}/groups/{emobGroupId}/members?emobId={emobId}
//        @DELETE("/api/v1/communities/{communityId}/groups/{emobGroupId}/members")
//        void exitGroup(@QueryMap HashMap<String, String> map, @Path("communityId") int communityId, @Path("emobGroupId") String emobGroupId, Callback<ExitGroupBean> cb);



//        @DELETE("/api/v1/communities/{communityId}/groups/{emobGroupId}/members")

        @PUT("/api/v3/activities/{emobGroupId}/members") /// v3 2016/03/03
        void exitGroup(@Path("emobGroupId") String emobGroupId, @Body ExitGroupReqBean exitGroupReqBean, Callback<CommonRespBean<String>> cb);
    }

    /**
     * 退出群,  如果本人是群主,则删除群 else 本人则退出群
     *
     * @param groupId
     * @param emboid
     */
    private void exitGrop(final String groupId, final String emboid) {

        ExitGroupReqBean exitGroupReqBean = new ExitGroupReqBean();
        exitGroupReqBean.setEmobUserId(emboid);
        ExitGroupService service = RetrofitFactory.getInstance().create(getmContext(),exitGroupReqBean,ExitGroupService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, retrofit.client.Response response) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                if (bean != null) {
                    if (group != null && !TextUtils.isEmpty(emboid)) {
                        ////
                        if (TextUtils.equals(emboid, PreferencesUtil.getLoginInfo(getmContext()).getEmobId())) {
                            if ("yes".equals(bean.getStatus())) {
                                Toast.makeText(GroupDetailsActivity.this, "您已退出该群", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(GroupDetailsActivity.this, bean.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if ("yes".equals(bean.getStatus())) {
                                Toast.makeText(GroupDetailsActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(GroupDetailsActivity.this, bean.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            if (!isFinishing()) {
                                /// 刷新群组信息
                                getGroupInfo(groupId);
                            }
                        }
                    } else {
                        if ("yes".equals(bean.getStatus())) {
                            Toast.makeText(GroupDetailsActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(GroupDetailsActivity.this, bean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(GroupDetailsActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                showNetErrorToast();
                error.printStackTrace();
            }
        };
        service.exitGroup(groupId,exitGroupReqBean, callback);
    }


    /**
     * 退出群组
     *
     * @param groupId
     */
//	private void exitGrop() {
//		new Thread(new Runnable() {
//			public void run() {
//				try {
//					EMGroupManager.getInstance().exitFromGroup(groupId);
//					runOnUiThread(new Runnable() {
//						public void run() {
//							progressDialog.dismiss();
//							setResult(RESULT_OK);
//							finish();
//							ChatActivity.activityInstance.finish();
//						}
//					});
//				} catch (final Exception e) {
//					runOnUiThread(new Runnable() {
//						public void run() {
//							progressDialog.dismiss();
//							Toast.makeText(getApplicationContext(), "退出群聊失败: " + e.getMessage(), 1).show();
//						}
//					});
//				}
//			}
//		}).start();
//	}

    /**
     * 解散群组 , 环信SDK
     */
    private void deleteGrop() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMGroupManager.getInstance().exitAndDeleteGroup(groupId);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            setResult(RESULT_OK);
                            finish();
                            ChatActivity.activityInstance.finish();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "解散群聊失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }


    /**
     * 增加群成员
     *
     * @param newmembers
     */
    private void addMembersToGroup(final String[] newmembers) {
        new Thread(new Runnable() {

            public void run() {
                try {
                    // 创建者调用add方法
                    if (EMChatManager.getInstance().getCurrentUser().equals(group.getOwner())) {
                        EMGroupManager.getInstance().addUsersToGroup(groupId, newmembers);
                    } else {
                        // 一般成员调用invite方法
                        EMGroupManager.getInstance().inviteUser(groupId, newmembers, null);
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            detailAdapter.notifyDataSetChanged();
//							((TextView) findViewById(R.id.group_name)).setText(group.getGroupName() + "(" + group.getAffiliationsCount()
//									+ "人)");
                            ((TextView) findViewById(R.id.group_name)).setText(group.getGroupName());
                            progressDialog.dismiss();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "添加群成员失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_switch_block_notify:
                mLdDialog.show();
                if (iv_switch_unblock_notify.getVisibility() == View.VISIBLE) {
                    BlackListHelper.addBlack(this, groupId, new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            mLdDialog.dismiss();
                            switch (msg.what) {
                                case Config.ADDBLACKLIST:
                                    List<String> list = PreferencesUtil.getUnNotifyGroupS(GroupDetailsActivity.this);
                                    list.add(groupId);
                                    EMChatManager.getInstance().getChatOptions().setReceiveNotNoifyGroup(list);
                                    PreferencesUtil.saveUnNotifyGroupS(GroupDetailsActivity.this, list);
                                    iv_switch_unblock_notify.setVisibility(View.INVISIBLE);
                                    iv_switch_block_notify.setVisibility(View.VISIBLE);
                                    showToast("已开启免打扰");
                                    break;
                                case Config.TASKERROR:
                                    showToast("开启免打扰失败，请重试");
                                    break;
                                case Config.NETERROR:
                                    showNetErrorToast();
                                    break;
                            }
                        }
                    });

                } else {
                    Log.i("onion", "移除消息免打扰" + groupId);
                    BlackListHelper.removeBlack(this, groupId, new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            mLdDialog.dismiss();
                            switch (msg.what) {
                                case Config.REMOVEBLACKLIST:
                                    List<String> list = PreferencesUtil.getUnNotifyGroupS(GroupDetailsActivity.this);
                                    if (list.contains(groupId)) list.remove(groupId);
                                    EMChatManager.getInstance().getChatOptions().setReceiveNotNoifyGroup(list);
                                    PreferencesUtil.saveUnNotifyGroupS(GroupDetailsActivity.this, list);
                                    iv_switch_unblock_notify.setVisibility(View.VISIBLE);
                                    iv_switch_block_notify.setVisibility(View.INVISIBLE);
                                    showToast("已关闭免打扰");
                                    break;
                                case Config.TASKERROR:
                                    showToast("关闭免打扰失败，请重试");
                                    break;
                                case Config.NETERROR:
                                    showNetErrorToast();
                                    break;
                            }
                        }
                    });
                }
                break;
            case R.id.rl_switch_block_groupmsg: // 屏蔽群组
                if (iv_switch_block_groupmsg.getVisibility() == View.VISIBLE) {
                    try {
                        EMGroupManager.getInstance().unblockGroupMessage(groupId);
                        iv_switch_block_groupmsg.setVisibility(View.INVISIBLE);
                        iv_switch_unblock_groupmsg.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        // todo: 显示错误给用户
                    }
                } else {
                    try {
                        EMGroupManager.getInstance().blockGroupMessage(groupId);
                        iv_switch_block_groupmsg.setVisibility(View.VISIBLE);
                        iv_switch_unblock_groupmsg.setVisibility(View.INVISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        // todo: 显示错误给用户
                    }
                }
                break;

            case R.id.clear_all_history: // 清空聊天记录
                Intent intent = new Intent(GroupDetailsActivity.this, AlertDialog.class);
                intent.putExtra("cancel", true);
                intent.putExtra("titleIsCancel", true);
                intent.putExtra("msg", "确定清空此群的聊天记录吗？");
                startActivityForResult(intent, REQUEST_CODE_CLEAR_ALL_HISTORY);
                break;

            case R.id.rl_blacklist: // 黑名单列表
                startActivity(new Intent(GroupDetailsActivity.this, GroupBlacklistActivity.class).putExtra("groupId", groupId));
                break;
            case R.id.rl_change_group_name:
                intent = new Intent(this, EditActivity.class);
                intent.putExtra("data", group.getGroupName());
                intent.putExtra(Config.EXPKey_GROUP, groupId);
                startActivityForResult(intent, REQUEST_CODE_EDIT_GROUPNAME);
                break;

            default:
                break;
        }

    }

    /**
     * 群组成员gridadapter
     *
     * @author admin_new
     */
    private class GridAdapter extends ArrayAdapter<String> {

        private int res;
        public boolean isInDeleteMode;
        private List<String> objects;

        public GridAdapter(Context context, int textViewResourceId, List<String> objects) {
            super(context, textViewResourceId, objects);
            this.objects = objects;
            res = textViewResourceId;
            isInDeleteMode = false;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(res, null);
            }
            final Button button = (Button) convertView.findViewById(R.id.button_avatar);
            // 最后一个item，减人按钮
            if (position == getCount() - 1) {
                button.setText("");
                // 设置成删除按钮
                button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.smiley_minus_btn, 0, 0);
                // 如果不是创建者或者没有相应权限，不提供加减人按钮
                if (!group.getOwner().equals(EMChatManager.getInstance().getCurrentUser())) {
                    // if current user is not group admin, hide add/remove btn
                    convertView.setVisibility(View.INVISIBLE);
                } else { // 显示删除按钮
                    if (isInDeleteMode) {
                        // 正处于删除模式下，隐藏删除按钮
                        convertView.setVisibility(View.INVISIBLE);
                    } else {
                        // 正常模式
                        convertView.setVisibility(View.VISIBLE);
                        convertView.findViewById(R.id.badge_delete).setVisibility(View.INVISIBLE);
                    }
                    button.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EMLog.d(TAG, "删除按钮被点击");
                            isInDeleteMode = true;
                            notifyDataSetChanged();
                        }
                    });
                }
            } else if (position == getCount() - 2) { // 添加群组成员按钮
                button.setText("");
                button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.smiley_add_btn, 0, 0);
                // 如果不是创建者或者没有相应权限
                if (!group.isAllowInvites() && !group.getOwner().equals(EMChatManager.getInstance().getCurrentUser())) {
                    // if current user is not group admin, hide add/remove btn
                    convertView.setVisibility(View.INVISIBLE);
                } else {
                    // 正处于删除模式下,隐藏添加按钮
                    if (isInDeleteMode) {
                        convertView.setVisibility(View.INVISIBLE);
                    } else {
                        convertView.setVisibility(View.VISIBLE);
                        convertView.findViewById(R.id.badge_delete).setVisibility(View.INVISIBLE);
                    }
                    button.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EMLog.d(TAG, "添加按钮被点击");
                            // 进入选人页面
                            startActivityForResult(
                                    (new Intent(GroupDetailsActivity.this, GroupPickContactsActivity.class).putExtra("groupId", groupId)),
                                    REQUEST_CODE_ADD_USER);
                        }
                    });
                }
            } else { // 普通item，显示群组成员
                final String username = getItem(position);
                button.setText(username);
                convertView.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
                Drawable avatar = getResources().getDrawable(R.drawable.default_avatar);
                avatar.setBounds(0, 0, referenceWidth, referenceHeight);
                button.setCompoundDrawables(null, avatar, null, null);
                // demo群组成员的头像都用默认头像，需由开发者自己去设置头像
                if (isInDeleteMode) {
                    // 如果是删除模式下，显示减人图标
                    convertView.findViewById(R.id.badge_delete).setVisibility(View.VISIBLE);
                } else {
                    convertView.findViewById(R.id.badge_delete).setVisibility(View.INVISIBLE);
                }
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isInDeleteMode) {
                            // 如果是删除自己，return
                            if (EMChatManager.getInstance().getCurrentUser().equals(username)) {
                                startActivity(new Intent(GroupDetailsActivity.this, AlertDialog.class).putExtra("msg", "不能删除自己"));
                                return;
                            }
                            if (!NetUtils.hasNetwork(getApplicationContext())) {
                                Toast.makeText(getApplicationContext(), getString(R.string.network_unavailable), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            EMLog.d("group", "remove user from group:" + username);
                            deleteMembersFromGroup(username);
                        } else {
                            // 正常情况下点击user，可以进入用户详情或者聊天页面等等
                            // startActivity(new
                            // Intent(GroupDetailsActivity.this,
                            // ChatActivity.class).putExtra("userId",
                            // user.getUsername()));

                        }
                    }

                    /**
                     * 删除群成员
                     *
                     * @param username
                     */
                    protected void deleteMembersFromGroup(final String username) {
                        final ProgressDialog deleteDialog = new ProgressDialog(GroupDetailsActivity.this);
                        deleteDialog.setMessage("正在移除...");
                        deleteDialog.setCanceledOnTouchOutside(false);
                        deleteDialog.show();
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    // 删除被选中的成员
                                    EMGroupManager.getInstance().removeUserFromGroup(groupId, username);
                                    isInDeleteMode = false;
                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            deleteDialog.dismiss();
                                            notifyDataSetChanged();
                                            ((TextView) findViewById(R.id.group_name)).setText(group.getGroupName() + "("
                                                    + group.getAffiliationsCount() + "人)");
                                        }
                                    });
                                } catch (final Exception e) {
                                    deleteDialog.dismiss();
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "删除失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            }
                        }).start();
                    }
                });

                button.setOnLongClickListener(new OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {
                        if (group.getOwner().equals(EMChatManager.getInstance().getCurrentUser())) {
                            Intent intent = new Intent(GroupDetailsActivity.this, AlertDialog.class);
                            intent.putExtra("msg", "确认将此成员加入至此群黑名单?");
                            intent.putExtra("cancel", true);
                            startActivityForResult(intent, REQUEST_CODE_ADD_TO_BALCKLIST);
                            longClickUsername = username;
                        }
                        return false;
                    }
                });
            }
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount() + 2;
        }
    }

    protected void updateGroup() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMGroup returnGroup = EMGroupManager.getInstance().getGroupFromServer(groupId);
//                    returnGroup.getOwner();/// 群主

                    // 更新本地数据
                    EMGroupManager.getInstance().createOrUpdateLocalGroup(returnGroup);

                    runOnUiThread(new Runnable() {
                        public void run() {
                            GroupInfo groupInfo = new Select().from(GroupInfo.class).where("group_id = ?", group.getGroupId()).executeSingle();
                            if (groupInfo != null) {
                                ((TextView) findViewById(R.id.group_name)).setText(groupInfo.getGroup_name());
                                group.setGroupName(groupInfo.getGroup_name());
                            } else {
                                ((TextView) findViewById(R.id.group_name)).setText(group.getGroupName() + "(" + group.getAffiliationsCount() + "人)");

                            }
                            loadingPB.setVisibility(View.INVISIBLE);
                            detailAdapter.notifyDataSetChanged();
                        /*	if (EMChatManager.getInstance().getCurrentUser().equals(group.getOwner())) {
                                // 显示解散按钮
								exitBtn.setVisibility(View.GONE);
								deleteBtn.setVisibility(View.VISIBLE);
							} else {
								// 显示退出按钮
								exitBtn.setVisibility(View.VISIBLE);
							deleteBtn.setVisibility(View.GONE);

							}*/

                            // update block
                            if (group.getMsgBlocked()) {
                                iv_switch_block_groupmsg.setVisibility(View.VISIBLE);
                                iv_switch_unblock_groupmsg.setVisibility(View.INVISIBLE);
                            } else {
                                iv_switch_block_groupmsg.setVisibility(View.INVISIBLE);
                                iv_switch_unblock_groupmsg.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            loadingPB.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        }).start();
    }

    public void back(View view) {
        Intent data = new Intent();
        data.putExtra(Config.EXPKey_GROUP, group.getGroupName());
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (groupAddOrDelDialog != null) {
            groupAddOrDelDialog.dismiss();
        }
        instance = null;
    }

}
