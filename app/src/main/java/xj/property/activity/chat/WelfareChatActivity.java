package xj.property.activity.chat;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.ChatActivity;
import xj.property.adapter.MessageAdapter;
import xj.property.adapter.WelfareChatAdapter;
import xj.property.beans.UserInfoDetailBean;
import xj.property.beans.WelfareOfflineMessageBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.message.XJMessageHelper;
import xj.property.utils.other.AdminUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.LoadingDialog;

public class WelfareChatActivity extends ChatActivity {
    private UserInfoDetailBean bean;
    private android.app.AlertDialog complainDialog;
    @Override
    protected boolean forChild() {
        InitDialog();
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welfare_chat);
        bean = PreferencesUtil.getLoginInfo(this);
        findViewById(R.id.tv_Complaint).setOnClickListener(this);

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getEndedCodes();
    }

    protected void InitDialog() {
        mLdDialog = new LoadingDialog(this);
        mLdDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                dialog.cancel();
                return false;
            }
        });
        View rootView = View.inflate(this, R.layout.dialog_complain, null);
        complainDialog = new android.app.AlertDialog.Builder(this).setView(rootView).create();
        final EditText editText = (EditText) rootView.findViewById(R.id.et_complain);
        rootView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complainDialog.dismiss();
            }
        });
//        rootView.findViewById(R.id.btn_complain).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                complainDialog.dismiss();
//                String content = editText.getText().toString();
//                if (TextUtils.isEmpty(content)) {
//                    Toast.makeText(WelfareChatActivity.this, "投诉内容不能为空", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                AdminUtils.doComplain(WelfareChatActivity.this,  bean.getEmobId(), toChatUsername, content, Config.SERVANT_TYPE_SHOPTOUSU);
//            }
//        });
    }
    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId()==R.id.tv_Complaint)//投诉
            complainDialog.show();
    }

    @Override
    protected MessageAdapter createMessageAdapter(ChatActivity chatActivity, String toChatUsername, int chatType) {
        return new WelfareChatAdapter(chatActivity, toChatUsername, chatType);
    }
    /*消息拉取*/
    interface EndedCodesService {
//        @GET("/api/v1/communities/{commityId}/welfares/user/{emobId}/welfareMessages")
//        void putBody(@Path("commityId") String commityId,@Path("emobId")String emobId, Callback<WelfareOfflineMessageBean> cb);
//        @GET("/api/v1/communities/{commityId}/welfares/user/{emobId}/welfareMessages")

//        /api/v3/welfares/messages/emobId={用户环信ID}
        @GET("/api/v3/welfares/messages")
        void putBody(@QueryMap HashMap<String, String> querymap, Callback<CommonRespBean<List<WelfareOfflineMessageBean>>> cb);
    }

    private void getEndedCodes() {
        String emobId ;
        if(PreferencesUtil.getLogin(this)){
            emobId = PreferencesUtil.getLoginInfo(this).getEmobId();
        }else {
            if(PreferencesUtil.getTouristLogin(this)){
                emobId = PreferencesUtil.getTourist(this);
            }else {
                emobId = PreferencesUtil.getlogoutEmobId(this);
            }
        }
        HashMap<String,String> queryMap =new HashMap<>();
        queryMap.put("emobId",emobId);
        EndedCodesService service = RetrofitFactory.getInstance().create(getmContext(),queryMap,EndedCodesService.class);
        Callback<CommonRespBean<List<WelfareOfflineMessageBean>>> callback = new Callback<CommonRespBean<List<WelfareOfflineMessageBean>>>() {
            @Override
            public void success(final CommonRespBean<List<WelfareOfflineMessageBean>> bean, retrofit.client.Response response) {
                if(bean != null) {
                    if ("yes".equals(bean.getStatus())) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                List<WelfareOfflineMessageBean> msglist = bean.getData();
                                int msgsize= msglist.size();
                                Log.i("debbug", "bean.size=" + msgsize);
                                for(int i=0 ; i<msgsize; i++) {
                                    Log.i("debbug", "i=" + i);
                                    if (msglist.get(i).getCMD_CODE() == 601 && XJMessageHelper.getOrderModel(msglist.get(i).getWelfareId(), 601) == null) {
                                        EMMessage message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
                                        TextMessageBody txtBody = new TextMessageBody(msglist.get(i).getContent());
                                        message.addBody(txtBody);
                                        // 添加ext属性
                                        message.setFrom(toChatUsername);
                                        message.setTo(msglist.get(i).getEmobId());
                                        message.setAttribute("avatar", msglist.get(i).getAvatar());
                                        message.setAttribute("nickname", msglist.get(i).getNickname());
                                        message.setAttribute("CMD_CODE", 601);
                                        message.setAttribute("clickable", 1);
                                        message.setAttribute("isShowAvatar", 1);
                                        message.setAttribute("title", msglist.get(i).getTitle());
                                        message.setAttribute("welfareId", msglist.get(i).getWelfareId());
                                        message.setAttribute("code", msglist.get(i).getCode());
                                        message.setAttribute(Config.EXPKey_SORT, "19");
                                        message.setMsgTime(System.currentTimeMillis());
                                        EMChatManager.getInstance().importMessage(message, false);
                                        conversation.addMessage(message);
                                        XJMessageHelper.saveMessage2DB(message.getMsgId(), message.getStringAttribute("welfareId", ""), 601);
                                        XJMessageHelper.operatNewMessage(getmContext(),message);
                                    }
                                    if (msglist.get(i).getCMD_CODE() == 602 && XJMessageHelper.getOrderModel(msglist.get(i).getWelfareId(), 602) == null) {
                                        EMMessage message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
                                        TextMessageBody txtBody = new TextMessageBody(msglist.get(i).getContent());
                                        message.addBody(txtBody);
                                        // 添加ext属性
                                        message.setFrom(toChatUsername);
                                        message.setTo(msglist.get(i).getEmobId());
                                        message.setAttribute("avatar", msglist.get(i).getAvatar());
                                        message.setAttribute("nickname", msglist.get(i).getNickname());
                                        message.setAttribute("CMD_CODE", 602);
                                        message.setAttribute("clickable", 1);
                                        message.setAttribute("isShowAvatar", 1);
                                        message.setAttribute("title", msglist.get(i).getTitle());
                                        message.setAttribute("welfareId", msglist.get(i).getWelfareId());
                                        message.setAttribute("code", msglist.get(i).getCode());
                                        message.setAttribute(Config.EXPKey_SORT, "19");
                                        message.setMsgTime(System.currentTimeMillis());
                                        EMChatManager.getInstance().importMessage(message, false);
                                        conversation.addMessage(message);
                                        XJMessageHelper.saveMessage2DB(message.getMsgId(), message.getStringAttribute("welfareId", ""), 602);
                                        XJMessageHelper.operatNewMessage(getmContext(),message);
                                    }
                                    if (msglist.get(i).getCMD_CODE() == 603 && XJMessageHelper.getOrderModel(msglist.get(i).getMessageId(), 603) == null) {
                                        EMMessage message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
                                        TextMessageBody txtBody = new TextMessageBody(msglist.get(i).getContent());
                                        message.addBody(txtBody);
                                        // 添加ext属性
                                        message.setFrom(toChatUsername);
                                        message.setTo(msglist.get(i).getEmobId());
                                        message.setAttribute("avatar", msglist.get(i).getAvatar());
                                        message.setAttribute("nickname", msglist.get(i).getNickname());
                                        message.setAttribute("CMD_CODE", 603);
                                        message.setAttribute("clickable", 1);
                                        message.setAttribute("isShowAvatar", 1);
                                        message.setAttribute("title", msglist.get(i).getTitle());
                                        message.setAttribute("welfareId", msglist.get(i).getWelfareId());
                                        message.setAttribute("code", msglist.get(i).getCode());
                                        message.setAttribute(Config.EXPKey_SORT, "19");
                                        message.setMsgTime(System.currentTimeMillis());
                                        EMChatManager.getInstance().importMessage(message, false);
                                        conversation.addMessage(message);
                                        XJMessageHelper.saveMessage2DB(message.getMsgId(), message.getStringAttribute("messageId", ""), 603);
                                        XJMessageHelper.operatNewMessage(getmContext(),message);
                                    }

                                }
                                refreshUIWithNewMessage();
                            }
                        }).start();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
//                showNetErrorToast();
            }
        };
        service.putBody(queryMap,callback);
    }

    private List<String> getMessages() {
        List<String> lists = new ArrayList<String>();
        List<EMMessage> messages = conversation.getAllMessages();
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getIntAttribute("CMD_CODE", 0) == 500 && messages.get(i).getIntAttribute("clickable", -1) == 1) {
                lists.add(messages.get(i).getStringAttribute("code",""));
            }
        }

        return lists;
    }

    private String getTitle(String code){
        return EMChatManager.getInstance().getMessage(XJMessageHelper.getOrderModel(code, 500).getMsg_id()).getStringAttribute("title","");
    }

}
