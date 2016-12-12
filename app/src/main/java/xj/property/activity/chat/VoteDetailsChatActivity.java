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
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.ChatActivity;
import xj.property.adapter.MessageAdapter;
import xj.property.adapter.VoteDetailsChatAdapter;
import xj.property.beans.UserInfoDetailBean;
import xj.property.beans.WelfareOfflineMessageBean;
import xj.property.utils.message.XJMessageHelper;
import xj.property.utils.other.AdminUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.LoadingDialog;

public class VoteDetailsChatActivity extends ChatActivity {
    private UserInfoDetailBean bean;
    private android.app.AlertDialog complainDialog;
    @Override
    protected boolean forChild() {
        InitDialog();
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_vote_details_chat);
        bean = PreferencesUtil.getLoginInfo(this);

        findViewById(R.id.tv_Complaint).setVisibility(View.GONE);

//        findViewById(R.id.tv_Complaint).setOnClickListener(this);

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getEndedCodes();
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
//                    Toast.makeText(VoteDetailsChatActivity.this, "投诉内容不能为空", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                AdminUtils.doComplain(VoteDetailsChatActivity.this, PreferencesUtil.getCommityId(VoteDetailsChatActivity.this), bean.getEmobId(), toChatUsername, content, Config.SERVANT_TYPE_SHOPTOUSU);
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
        return new VoteDetailsChatAdapter(chatActivity, toChatUsername, chatType);
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
