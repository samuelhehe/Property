package xj.property.activity.surrounding;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.PUT;
import xj.property.R;
import xj.property.activity.HXBaseActivity.ChatActivity;
import xj.property.adapter.MessageAdapter;
import xj.property.adapter.PanicBuyingChatAdapter;
import xj.property.beans.RequestEndedCodesBean;
import xj.property.beans.RequstEndedCodeBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.message.XJContactHelper;
import xj.property.utils.message.XJMessageHelper;
import xj.property.utils.other.AdminUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.LoadingDialog;

public class PanicBuyingChatActivity extends ChatActivity {
    private UserInfoDetailBean bean;
    private android.app.AlertDialog complainDialog;
    @Override
    protected boolean forChild() {
        InitDialog();
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_panic_buying_chat);
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
        rootView.findViewById(R.id.btn_complain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complainDialog.dismiss();
                String content = editText.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(PanicBuyingChatActivity.this, "投诉内容不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                AdminUtils.doComplainForShop(PanicBuyingChatActivity.this,bean.getEmobId(), toChatUsername, content, Config.SERVANT_TYPE_SHOPTOUSU);
            }
        });
    }
    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId()==R.id.tv_Complaint)//投诉
            complainDialog.show();
    }

    @Override
    protected MessageAdapter createMessageAdapter(ChatActivity chatActivity, String toChatUsername, int chatType) {
        return new PanicBuyingChatAdapter(chatActivity, toChatUsername, chatType);
    }
        /*消息拉取*/
    interface EndedCodesService {
        @PUT("/api/v1/crazysales/user/codes")
        void putBody(@Header("signature") String signature, @Body RequestEndedCodesBean bean, Callback<RequstEndedCodeBean> cb);
    }

    private void getEndedCodes() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        EndedCodesService service = restAdapter.create(EndedCodesService.class);
        Callback<RequstEndedCodeBean> callback = new Callback<RequstEndedCodeBean>() {
            @Override
            public void success(final RequstEndedCodeBean bean, retrofit.client.Response response) {
                if(bean != null) {
                    if ("yes".equals(bean.getStatus())) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for(int i=0 ; i<bean.getInfo().getCodes().size(); i++){
                                    if( XJMessageHelper.getOrderModel(bean.getInfo().getCodes().get(i), 501)==null){
                                        EMMessage message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
                                        TextMessageBody txtBody = new TextMessageBody("验码成功");
                                        message.addBody(txtBody);
                                        // 添加ext属性
                                        message.setFrom(toChatUsername);
                                        message.setTo(PreferencesUtil.getLoginInfo(PanicBuyingChatActivity.this).getEmobId());
                                        message.setAttribute("avatar", XJContactHelper.selectContact(toChatUsername).getAvatar());
                                        message.setAttribute("nickname", XJContactHelper.selectContact(toChatUsername).getNick());
                                        message.setAttribute("CMD_CODE", 501);
                                        message.setAttribute("isShowAvatar", 1);
                                        message.setAttribute("title",getTitle(bean.getInfo().getCodes().get(i)));
                                        message.setAttribute("code", bean.getInfo().getCodes().get(i));
                                        message.setAttribute(Config.EXPKey_SORT, "13");
                                        message.setMsgTime(System.currentTimeMillis());
                                        EMChatManager.getInstance().importMessage(message, false);
                                        conversation.addMessage(message);
                                        XJMessageHelper.saveMessage2DB(message.getMsgId(), message.getStringAttribute(Config.EXPKey_serial, ""), 501);
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
        RequestEndedCodesBean request = new RequestEndedCodesBean();
        request.setMethod("PUT");
        request.setCodes(getMessages());
        service.putBody(StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(request)), request,  callback);
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
