package xj.property.activity.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.beans.AddGroupMessageReqBean;
import xj.property.beans.AddGroupMessageRespBean;
import xj.property.beans.AddMemberToGroupReqBean;
import xj.property.beans.AddMemberToGroupRespBean;
import xj.property.beans.TagsA2BAddReqBean;
import xj.property.beans.TagsA2BAddRespBean;
import xj.property.beans.TagsA2BDelReqBean;
import xj.property.beans.TagsA2BDelRespBean;
import xj.property.beans.TagsA2BRespBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.utils.ToastUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.GroupUtils;
import xj.property.utils.other.PreferencesUtil;

public class JoinInGroupApplyConfrimDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = "JoinInGroupApplyConfrimDialog";
    private final String emobIdGroup;
    private final UserInfoDetailBean userBean;
    private final String emobGroupOwner;
    private Context mContext;

    /// 取消
    private Button btn_cancel;
    /// 确定
    private Button btn_ok;

    private EditText group_join_reason_et;

    public OnJoinInStatusListener getOnJoinInStatusListener() {
        return onJoinInStatusListener;
    }

    public void setOnJoinInStatusListener(OnJoinInStatusListener onJoinInStatusListener) {
        this.onJoinInStatusListener = onJoinInStatusListener;
    }


    public interface OnJoinInStatusListener {

        void onjoinSuccess(String s);

        void onjoinFail(String reason);

    }

    private OnJoinInStatusListener onJoinInStatusListener;


    public JoinInGroupApplyConfrimDialog(Context context, String emobGroupOwner, String emobIdGroup, OnJoinInStatusListener onJoinInStatusListener) {
        super(context, R.style.Theme_CustomDialog);
        this.mContext = context;
        this.emobGroupOwner = emobGroupOwner;
        /// 群组ID
        this.emobIdGroup = emobIdGroup;
        this.onJoinInStatusListener = onJoinInStatusListener;
        this.userBean = PreferencesUtil.getLoginInfo(this.mContext);

        Log.d(TAG,"emobGroupOwner "+ emobGroupOwner + " emobIdGroup "+ emobIdGroup );

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_join_groupapply_mgr);
        initView();
    }


    interface GroupChangeCallBackServer {

        /**
         * @param signature
         * @param qt
         * @param communityId
         * @param cb
         */
        @POST("/api/v1/communities/{communityId}/groups/{groupId}/members")
        void addMemberToGroup(@Header("signature") String signature, @Body AddMemberToGroupReqBean qt, @Path("communityId") long communityId, @Path("groupId") String groupId, Callback<AddMemberToGroupRespBean> cb);

        /**
         * 添加一条群组消息
         *
         * @param signature
         * @param qt
         * @param emobId
         * @param cb
         */
        /// /api/v1/communities/{communityId}/users/{emobId}/activities/message
        @POST("/api/v1/communities/{communityId}/users/{emobId}/activities/message")
        void addAGroupMessage(@Header("signature") String signature, @Body AddGroupMessageReqBean qt, @Path("communityId") long communityId, @Path("emobId") String emobId, Callback<AddGroupMessageRespBean> cb);
    }

    /**
     * messageContent : 大家一起玩
     * messageId : 1451369301003
     * emobIdFrom : ce04f45b22793b5a2425962b38c74d08
     * emobIdTo : 1e45c249f64eead873aa8a580b30733c
     * groupId : 1429702488658259
     * type : accept  //// reject , apply, invite
     */
    private void addAGroupMessageCall(AddGroupMessageReqBean quaryToken, String emobId) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        if(Config.SHOW_lOG){
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        }
        GroupChangeCallBackServer service = restAdapter.create(GroupChangeCallBackServer.class);
        Callback<AddGroupMessageRespBean> callback = new Callback<AddGroupMessageRespBean>() {
            @Override
            public void success(AddGroupMessageRespBean respBean, Response response) {
                if (respBean != null && "yes".equals(respBean.getStatus())) {
                    if (onJoinInStatusListener != null) {
                        onJoinInStatusListener.onjoinSuccess("申请成功,等待审核");
                    }
                } else if (respBean != null && "no".equals(respBean.getStatus())) {
                    android.util.Log.d("NewFriendsMsgProcessActivity", " addAGroupMessageCall no msg: " + respBean.getMessage());
                    if (onJoinInStatusListener != null) {
                        onJoinInStatusListener.onjoinFail("申请失败,请稍后重试");
                    }
                } else {
                    android.util.Log.d("NewFriendsMsgProcessActivity", " addAGroupMessageCall add fail ");
                    if (onJoinInStatusListener != null) {
                        onJoinInStatusListener.onjoinFail("网络错误,请稍后重试");
                    }
                }
                dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                dismiss();
            }
        };
        service.addAGroupMessage(StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(quaryToken)), quaryToken, PreferencesUtil.getCommityId(getContext()), emobId, callback);
    }

    private void getTagsA2BDel(final String joinReason)  {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    JSONObject jsonObject = new JSONObject();

                    ///// 消息ID组成部分: groupID+"_"+goupownerID+"_"+emobID /// 群ID+ 群主ID+用户申请者ID

                    String n_messageId = emobIdGroup+"_"+emobGroupOwner+"_"+userBean.getEmobId();

//                    String n_messageId = UUID.randomUUID().toString().replaceAll("-", "");

                    jsonObject = jsonObject.put("u_userName", userBean.getNickname());
                    jsonObject = jsonObject.put("u_userAvatar", userBean.getAvatar());
                    jsonObject = jsonObject.put("n_reason", joinReason);
                    jsonObject = jsonObject.put("g_groupOwerEmobId", emobGroupOwner);
                    jsonObject = jsonObject.put("n_messageId", n_messageId);


//                        @{@"u_userName" : [XJAccountTool account].nickname,
//                        @"u_userAvatar" : [XJAccountTool account].avatar,
//                        @"n_reason" : alertView.textView.text,
//                        @"n_messageId" : message.messageId,
//                        @"g_groupOwerEmobId" : weakSelf.activity.emobIdOwner};


                    String joooinReason = jsonObject.toString();
                    android.util.Log.d(TAG, "apply joinreason  " + joooinReason);

                    EMGroupManager.getInstance().applyJoinToGroup(emobIdGroup, joooinReason);

                    AddGroupMessageReqBean quaryToken = new AddGroupMessageReqBean();
                    quaryToken.setGroupId(emobIdGroup);
                    quaryToken.setMessageId(n_messageId);
                    android.util.Log.d(TAG, "apply setMessageId  " + n_messageId);

                    quaryToken.setType("apply");
                    quaryToken.setEmobIdFrom(userBean.getEmobId());
                    quaryToken.setEmobIdTo(emobGroupOwner);
                    quaryToken.setMessageContent(joinReason);

                    addAGroupMessageCall(quaryToken, userBean.getEmobId());

                } catch (EaseMobException | JSONException e) {
                    onJoinInStatusListener.onjoinFail(e.getMessage());
                    e.printStackTrace();
                    dismiss();

                }
            }
        }).start();
    }


/// 申请加入XX群等待群主同意        GroupUtils.createEMConversationMessage();


//        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
//        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
//        MspCardListService service = restAdapter.create(MspCardListService.class);
//        Callback<TagsA2BDelRespBean> callback = new Callback<TagsA2BDelRespBean>() {
//            @Override
//            public void success(TagsA2BDelRespBean respBean, Response response) {
//
//
//                if(respBean!=null&& TextUtils.equals(respBean.getStatus(),"yes")){
//
//                    if(onTagsA2BDelStatusListener!=null){
//                        onTagsA2BDelStatusListener.onTagsA2BDelSuccess(respBean.getMessage());
//                    }
//                    Toast.makeText(getContext(),"标签删除成功", Toast.LENGTH_SHORT).show();
//                    dismiss();
//                }else if(respBean!=null&&TextUtils.equals(respBean.getStatus(),"no")){
//                    if(onTagsA2BDelStatusListener!=null){
//                        onTagsA2BDelStatusListener.onTagsA2BDelFail(respBean.getMessage());
//                    }
//                    Toast.makeText(getContext(),"no", Toast.LENGTH_SHORT).show();
//                    dismiss();
//                }else{
//                    Toast.makeText(getContext(), "删除标签错误", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                error.printStackTrace();
//
//                Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
//            }
//        };
//        TagsA2BDelReqBean quaryToken = new TagsA2BDelReqBean();
//        quaryToken.setMethod("PUT");
//        service.getTagsA2BDel(StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(quaryToken)), quaryToken, PreferencesUtil.getCommityId(getContext()), callback);
//    }


    private void initView() {
        /// 加入群组的理由
        group_join_reason_et = (EditText) findViewById(R.id.group_join_reason_et);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_cancel:
                cancel();
                break;

            case R.id.btn_ok:
                if (group_join_reason_et != null) {
                    String join_reason = group_join_reason_et.getText().toString().trim();
                    if(!TextUtils.isEmpty(join_reason)){
                        getTagsA2BDel(join_reason);
                    }else{
                        ToastUtils.showToast(mContext, "请输入申请理由");
                    }
                }
                break;

        }
    }

    @Override
    public void cancel() {
        super.cancel();
    }

}
