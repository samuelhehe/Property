package xj.property.utils.other;

import android.content.Context;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;

import xj.property.R;
import xj.property.utils.message.XJContactHelper;
import xj.property.utils.message.XJMessageHelper;

/**
 */
public class VoteUtil {


    /**
     * 创建一条本地消息 CMD 703 投票消息
     *
     * @param context
     * @param from
     * @param title
     * @param code
     * @param image
     */
    public static void create703Message(final Context context, final String from, final String title, final String code, final String voteId, final String image){
                        new Thread(new Runnable() {
                    @Override
                    public void run() {
                            final EMMessage message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
                            TextMessageBody txtBody = new TextMessageBody("发起投票");
                            message.addBody(txtBody);
                            message.setFrom(from);
                            message.setTo(PreferencesUtil.getLoginInfo(context).getEmobId());
                            message.setAttribute("avatar", "drawable://"+R.drawable.home_item_vote);
                            message.setAttribute("nickname", "投票");
                            message.setAttribute("CMD_CODE", 703);
                            message.setAttribute("clickable", 1);
                            message.setAttribute("isShowAvatar", 1);
                            message.setAttribute("title", title);
                            message.setAttribute("code", code);
                            message.setAttribute(Config.EXPKey_SORT, "23");
                            message.setAttribute("voteId",voteId);
                            message.setAttribute("image",image);
//                          EMChatManager.getInstance().saveMessage(message,true);
                            EMChatManager.getInstance().importMessage(message, true);
                            XJContactHelper.saveContact(message);
                            XJMessageHelper.saveMessage2DB(message.getMsgId(), message.getStringAttribute("voteId", ""), 703);
                    }
                }).start();
    }

}
