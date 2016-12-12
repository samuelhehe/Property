package xj.property.utils.other;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMNotifier;
import com.easemob.chat.TextMessageBody;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import xj.property.Constant;
import xj.property.HXSDKHelper;
import xj.property.R;
import xj.property.XjApplication;
import xj.property.activity.HXBaseActivity.ChatActivity;
import xj.property.activity.HXBaseActivity.GroupsActivity;
import xj.property.beans.BaseBean;
import xj.property.beans.JoinGroupRequest;
import xj.property.beans.ResultEaGroupInfo;
import xj.property.beans.ResultGroupInfo;
import xj.property.beans.ResultInfoBean;
import xj.property.beans.UserGroupBean;
import xj.property.cache.GroupHeader;
import xj.property.cache.GroupInfo;
import xj.property.domain.InviteMessage;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.netbasebean.JoinGroupRespBean;
import xj.property.utils.CommonUtils;
import xj.property.utils.ToastUtils;
import xj.property.utils.image.utils.ImageUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.widget.GroupHeaderHelper;

/**
 * Created by Administrator on 2015/5/13.
 */
public class GroupUtils {
    interface getOrderInfoService {
        ///api/v1/communities/{communityId}/groups/{groupId}/members
        @GET("/api/v1/communities/{communityId}/groups/{groupId}/members")
        void getOrderInfo(@Path("communityId") int communityId, @Path("groupId") String groupId, Callback<ResultGroupInfo> cb);
    }

    public static void getGroupInfo(final String groupId) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        getOrderInfoService service = restAdapter.create(getOrderInfoService.class);
        Callback<ResultGroupInfo> callback = new Callback<ResultGroupInfo>() {
            @Override
            public void success(final ResultGroupInfo bean, retrofit.client.Response response) {
                if ("yes".equals(bean.status) && bean.info.size() > 1)
                    GroupHeaderHelper.createGroupId(bean.info, groupId, XjApplication.getInstance());

//                    XjApplication.getInstance().pool.execute(new Runnable() {
//                        @Override
//                        public void run() {
//                            List<Bitmap> bms=new ArrayList<Bitmap>();
//                            for(int i=0;i<bean.info.size();i++){
//                                Bitmap bm=   ImageUtils.GetLocalOrNetBitmap( bean.info.get(i).getAvatar());
//                                if(bm!=null)
//                                    bms.add(bm);
//                            }
//                            GroupHeader header=	new Select().from(GroupHeader.class).where("group_id = ?",groupId).executeSingle();
//                            File cacheDir = StorageUtils.getOwnCacheDirectory(XjApplication.getInstance(), Config.BASE_GROUP_CACHE);
//                            File file = new File(cacheDir, groupId + ".png");
//                            if(header==null||header.getNum()<10) {
//                                while (bms.contains(null)){
//                                    bms.remove(null);
//                                }
//                                header = new GroupHeader(groupId, file.getAbsolutePath(), bms.size());
//                                header.save();
//                                for(int i=bms.size();i<bean.info.size()&&i<10;i++){
//                                    bms.add(BitmapFactory.decodeResource(XjApplication.getInstance().getResources(),R.drawable.head_portrait_personage));
//                                }
//                            }
//                            Bitmap bm = GroupHeaderHelper.getGroupBitmap(bms);
//                            try {
//                                FileOutputStream outputStream = new FileOutputStream(file);
//                                bm.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    });

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        };
        service.getOrderInfo(PreferencesUtil.getCommityId(XjApplication.getInstance()), groupId, callback);
    }


    interface JoinGroupService {
        ///api/v1/communities/{communityId}/groups/{emobGroupId}/members/
//        @POST("/api/v1/communities/{communityId}/groups/{emobGroupId}/members/")
//        void joinGroup(@Header("signature") String signature, @Body JoinGroupRequest jgr, @Path("communityId") int communityId, @Path("emobGroupId") String emobGroupId, Callback<ResultInfoBean> cb);


//        @POST("/api/v1/communities/{communityId}/groups/{emobGroupId}/members/")


        @POST("/api/v3/activities/{emobGroupId}/members")  /// v3 2016/03/03
        void joinGroup(@Body JoinGroupRequest jgr, @Path("emobGroupId") String emobGroupId, Callback<CommonRespBean<JoinGroupRespBean>> cb);
    }

    //// 加群方法  不需要进行群组申请的情况下调用
    public static void joinGroup(final Context context, final String emobGroupId, final Handler handler) {
        JoinGroupRequest request = new JoinGroupRequest();
        request.setEmobUserId(PreferencesUtil.getLoginInfo(context).getEmobId());
        request.setCommunityId(PreferencesUtil.getCommityId(context));

        JoinGroupService service = RetrofitFactory.getInstance().create(context,request,JoinGroupService.class);
        Callback<CommonRespBean<JoinGroupRespBean>> callback = new Callback<CommonRespBean<JoinGroupRespBean>>() {
            @Override
            public void success(CommonRespBean<JoinGroupRespBean> bean, retrofit.client.Response response) {
                if ("yes".equals(bean.getStatus()) || "exist".equals(bean.getMessage())) {
                    EMGroup group = null;
                    group = EMGroupManager.getInstance().getGroup(emobGroupId);

                    if (group == null)
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                EMGroup emGroup = null;
                                try {
                                    emGroup = EMGroupManager.getInstance().getGroupFromServer(emobGroupId);
                                    //保存获取下来的群聊信息
                                    EMGroupManager.getInstance().createOrUpdateLocalGroup(emGroup);
                                } catch (Exception e) {
                                    Log.i("onion", e.toString());
                                }
//                               if(emGroup==null){
////                        handler.sendEmptyMessage(Config.TASKERROR);
//                                   Toast.makeText(context.get, "获取群信息失败,请重新登录后重试", Toast.LENGTH_SHORT).show();
//                                   return;
//                               }
                            }
                        }.start();
                    handler.sendEmptyMessage(Config.TASKCOMPLETE);

                } else {

                    ToastUtils.showToast(context,"加群失败:"+ bean.getMessage());
                    Message message = Message.obtain();
                    message.what = Config.TASKERROR;
                    message.obj = bean.getMessage();
                    handler.sendMessage(message);
                }
               /* try {
                    EMGroupManager.getInstance().joinGroup(emobGroupId);
                }catch (Exception e){
                    Log.e("onion",e.toString());
                }*/
            }

            @Override
            public void failure(RetrofitError error) {
                handler.sendEmptyMessage(Config.NETERROR);
            }
        };
        service.joinGroup(request, emobGroupId, callback);
    }


    interface getEaGroupInfoService {
        ///http://114.215.105.202/api/v1/communities/{communityId}/eagroup/{emobGroupId}
        @GET("/api/v1/communities/{communityId}/eagroup/{emobGroupId}")
        void getOrderInfo(@Path("communityId") int communityId, @Path("emobGroupId") String emobGroupId, Callback<ResultEaGroupInfo> cb);
    }

    public static void getEaGroupInfo(final String emobGroupId, final Handler handler) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        getEaGroupInfoService service = restAdapter.create(getEaGroupInfoService.class);
        Callback<ResultEaGroupInfo> callback = new Callback<ResultEaGroupInfo>() {
            @Override
            public void success(final ResultEaGroupInfo bean, retrofit.client.Response response) {
                if ("yes".equals(bean.status)) {
                    Log.i("onion", "群聊图像：  " + emobGroupId + "    " + bean.getInfo().getGroupName() + "   " + bean.getInfo().getMaxValue());
                    GroupInfo groupInfo = new Select().from(GroupInfo.class).where("group_id = ?", emobGroupId).executeSingle();
                    if (groupInfo == null) {
                        new GroupInfo(bean.getInfo().getGroupName(), emobGroupId, bean.getInfo().getMaxValue()).save();
                    } else {
                        groupInfo.setGroup_name(bean.getInfo().getGroupName());
                        groupInfo.save();
                    }
                    //EMGroupManager.getInstance().changeGroupName();
                    Message message = Message.obtain();
                    message.what = Config.TASKCOMPLETE;
                    message.obj = bean.getInfo().getGroupName();
                    handler.sendMessage(message);
                }

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        };
        service.getOrderInfo(PreferencesUtil.getCommityId(XjApplication.getInstance()), emobGroupId, callback);
    }

    /**
     * 创建本地会话消息
     *
     * 这个消息接受者,创建者的emobid 都是定义好的.
     *
     * @param messageTxt
     */
    public static void createEMConversationMessage(Context context, String messageTxt,String n_messageId, InviteMessage.InviteMesageStatus status) {
        //// 创建本地申请加群会话,记录群聊状态

        String toEmobId= Constant.NEW_FRIENDS_USERNAME_DEFEMOBID;

// 获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此  申请与通知申请会话处理
        EMConversation conversation = EMChatManager.getInstance().getConversation(Constant.NEW_FRIENDS_USERNAME);
// 创建一条文本消息  receivemessage
        EMMessage message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
// 设置消息body
        TextMessageBody txtBody = new TextMessageBody(messageTxt);
        message.addBody(txtBody);

//        message.setAttribute(Config.EXPKey_nickname,"群组申请与通知");

        /// 设置MSGID
        message.setMsgId(n_messageId);

        message.setAttribute(Config.EXPKey_msgstatus, status.ordinal());
        /// 消息是否处理
        message.setAttribute(Config.EXPKey_ishandle, "n"); //// y: n

        message.setFrom(Constant.NEW_FRIENDS_USERNAME);
        message.setTo(toEmobId);

//        message.setUnread(false);/// 设置状态为已读

        message.setUnread(false);

        message.setMsgTime(System.currentTimeMillis());

//        message.setReceipt(toEmobId);

        EMMessage msgDB = conversation.getMessage(message.getMsgId());

        if(msgDB!=null){
            Log.d("createEMConversationMessage ", "info is not null  "+ msgDB);
            conversation.removeMessage(msgDB.getMsgId());
            Log.d("createEMConversationMessage ", "removeMessage is success ");
        }
        // 保存邀请消息
        EMChatManager.getInstance().saveMessage(message);

        Log.d("createEMConversationMessage ", "saveMessage is success "+ message);
//        EMChatManager.getInstance().importMessage(message,true);

        // 把消息加入到此会话对象中
        conversation.addMessage(message);

        // 提醒新消息
//        EMNotifier.getInstance(context).notifyOnNewMsg();

        HXSDKHelper.getInstance().getNotifier().onNewMsg(message);


// 发送消息
//        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
//            @Override
//            public void onSuccess() {
//                Log.d("GroupUtils ", "createEMConversationMessage success ");
//            }
//
//            @Override
//            public void onError(int i, String s) {
//
//                Log.d("GroupUtils ", "createEMConversationMessage error " + s);
//            }
//
//            @Override
//            public void onProgress(int i, String s) {
//
//            }
//        });

//        EMChatManager.getInstance().saveMessage(message);

    }


}
