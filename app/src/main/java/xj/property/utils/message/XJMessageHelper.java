package xj.property.utils.message;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.easemob.chat.EMChatDB;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.PUT;
import retrofit.http.Path;
import xj.property.Constant;
import xj.property.XjApplication;
import xj.property.beans.ChangeWelfareOrderStatusBean;
import xj.property.beans.CommonPostResultBean;
import xj.property.beans.FastGoodsModel;
import xj.property.cache.OrderDetailModel;
import xj.property.cache.OrderModel;
import xj.property.cache.ServiceModel;
import xj.property.cache.XJNotify;
import xj.property.domain.User;
import xj.property.event.NewNotifyEvent;
import xj.property.fragment.IndexFragment;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

/**
 * Created by Administrator on 2015/4/3.
 */
public class XJMessageHelper {

    public static void saveMessage2DB(String msgId, String serial, int cmdCode) {
        OrderModel or = new OrderModel(msgId, serial, cmdCode);
        or.save();
    }

    public static void updateCancleMessage(String serial, EMConversation conversation) {
        List<OrderModel> list = getMessage2DB(serial);
        Log.i("onion", "serial" + list.toString());
        for (int i = 0; i < list.size(); i++) {
            updateMesageStateWithId(list.get(i).msg_id, conversation);
        }
    }

    public static List<OrderModel> getMessage2DB(String serial) {
        return new Select()
                .from(OrderModel.class).where("serial = ?", serial)
                .execute();
    }

    public static void updateMesageStateWithId(final String msgId, final EMConversation conversation) {
        final EMMessage message = conversation.getMessage(msgId);
        if (message == null) {
            return;
        }
        message.setAttribute("clickable", 0);
        EMChatManager.getInstance().updateMessageBody(message);
    }

    public static void updateMessageStateWithMsg(EMMessage message) {
        message.setAttribute("clickable", 0);
        EMChatManager.getInstance().updateMessageBody(message);
    }

    //查询是否有此订单此cmdcode的消息
    public static OrderModel getOrderModel(String serial, int cmdCode) {
        return new Select()
                .from(OrderModel.class).where("serial = ? and cmd_code = ?", serial, cmdCode)
                .executeSingle();

    }


    public static void loadOffLineMessages(Context context,List<EMMessage> messages) {
        Map<String, User> contacts = new HashMap<String, User>();
//        EMGroupManager.getInstance().loadAllGroups();
//        EMChatManager.getInstance().loadAllConversations();
        for (int i = 0; i < messages.size(); i++) {

            EMMessage message = messages.get(i);
            if (XJMessageHelper.getOrderModel(message.getStringAttribute(Config.EXPKey_serial, ""), 201) != null) {
                Log.i("debbug", "已经有message了");
                EMConversation conversation = EMChatManager.getInstance().getConversation(message.getFrom());
                conversation.removeMessage(message.getMsgId());
                EMChatDB.getInstance().deleteMessage(message.getMsgId());
                continue;
            }
            if (!contacts.containsKey(message.getFrom())) {
                User user = new User();
                if (message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 701
                                || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 702
                                || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 703) {

                    user.sort = "" + message.getIntAttribute(Config.EXPKey_CMD_CODE, 0);
                } else {
                    user.sort = message.getStringAttribute(Config.EXPKey_SORT, "-1");
                }
                user.avatar = message.getStringAttribute(Config.EXPKey_avatar, "");
                user.setNick(message.getStringAttribute(Config.EXPKey_nickname, "帮帮用户"));
                user.setUsername(message.getFrom());
                user.setEid(message.getFrom());
                contacts.put(message.getFrom(), user);
            }
            if (message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) != 0) {
                XJMessageHelper.operatNewMessage(context,message);
            }
        }
        List<User> users = new ArrayList<>(contacts.values());
        for (int i = 0; i < users.size(); i++) {
            XJContactHelper.saveContact(users.get(i));
        }

    }


    public static boolean loadOffLineMessage(Context context,EMConversation conversation) {
        EMMessage message = conversation.getLastMessage();
//        if(message==null)return true;
        String nike = message.getStringAttribute(Config.EXPKey_nickname, null);
        boolean flag = false;
        if (message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) != 0 || !"-1".equals(message.getStringAttribute(Config.EXPKey_SORT, "-1"))) {
//                if ( message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) != 0) {
            List<EMMessage> messages = conversation.getAllMessages();
            int count = messages.size();
            for (int i = 0; i < count; i++) {
                flag = XJMessageHelper.operatNewMessage(context,messages.get(0));
            }
//                    continue;
        }
        if (flag) {
            Log.i("onion", "这是个通知，不存");

            return true;
        }
        User user = new User();
        user.setEid(message.getFrom());
        user.setUsername(message.getFrom());
        if ( message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 701
                        || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 702
                        || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 703) {

            user.sort = message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) + "";
        } else user.sort = message.getStringAttribute(Config.EXPKey_SORT, "-1");
        user.avatar = message.getStringAttribute(Config.EXPKey_avatar, "");
        user.setNick(nike);
        XJContactHelper.saveContact(user);
        return false;
    }

    public static void loadNativeMessage() {

        Map<String, User> userlist = new HashMap<String, User>();
        EMGroupManager.getInstance().loadAllGroups();
        EMChatManager.getInstance().loadAllConversations();
        Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
        // 过滤掉messages seize为0的conversation
        for (EMConversation conversation : conversations.values()) {
            EMMessage message = conversation.getLastMessage();
            if (message.direct == EMMessage.Direct.RECEIVE && message.getChatType() != EMMessage.ChatType.GroupChat) {
//                boolean flag=false;
//                if ( message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) != 0||!"-1".equals(message.getStringAttribute(Config.EXPKey_SORT,"-1"))) {
////                if ( message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) != 0) {
//                    List<EMMessage> messages = conversation.getAllMessages();
//                    for (int i = 0; i < messages.size(); i++) {
//                        flag=    XJMessageHelper.operatNewMessage(messages.get(i));
//                    }
//                }
//                if(flag){
//                    Log.i("onion","这是个通知，不存");
//
//                    continue;
//                }

//                -----------------------------------------------------------------------
//
//                if (message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 401
//                        || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 402
//                        || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 403
//                        || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 404
//
//                        || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 500
//                        || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 501
//
//                        || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 701
//                        || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 702
//                        || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 703) {
//
//                    Log.i("saveContact", "普通的联系人 message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) "+ message.getIntAttribute(Config.EXPKey_CMD_CODE, 0)  );
//                    XJContactHelper.saveContact(emobId, nickName, avatar, message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) + "");
//                } else {
//                    Log.i("debbug", "普通的联系人 EXPKey_SORT"+message.getStringAttribute(Config.EXPKey_SORT, "-1"));
//                    XJContactHelper.saveContact(emobId, nickName, avatar, message.getStringAttribute(Config.EXPKey_SORT, "-1"));
//                }

//                -----------------------------------------------------------------------
                User user = new User();
                user.setEid(message.getFrom());
                user.setUsername(message.getFrom());
                if ( message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 401
                                || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 402
                                || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 403
                                || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 404

                                || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 500
                                || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 501

                                || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 601
                                || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 602
                                || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 603

                                || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 701
                                || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 702
                                || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 703){

                    user.sort = message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) + "";
                    Log.i("debbug", "load native conversation EXPKey_CMD_CODE"+message.getIntAttribute(Config.EXPKey_CMD_CODE, 0));
                } else{
                    Log.i("debbug", "load native conversation EXPKey_SORT"+message.getStringAttribute(Config.EXPKey_SORT, "-1"));
                    user.sort = message.getStringAttribute(Config.EXPKey_SORT, "-1");
                }
                user.avatar = message.getStringAttribute(Config.EXPKey_avatar, "");
                user.setNick(message.getStringAttribute(Config.EXPKey_nickname, "帮帮用户"));
                XJContactHelper.saveContact(user);
                userlist.put(message.getFrom(), user);
            } else {
                User user = new XJContactHelper().selectContact(message.getTo());
//                user.setEid(message.getFrom());
//                user.setUsername(message.getFrom());
//                user.sort = message.getStringAttribute(Config.EXPKey_SORT, "-1");
//                user.avatar = message.getStringAttribute(Config.EXPKey_avatar, "");
//                user.setNick(nike);
//                UserUtils. setUserHearder(message.getFrom(), user);
                userlist.put(message.getTo(), user);
            }
        }

        // 添加user"申请与通知"
        User newFriends = new User();
        newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
        newFriends.setNick("申请与通知");
        newFriends.setHeader("");
        userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
        // 添加"群聊"
        User groupUser = new User();
        groupUser.setUsername(Constant.GROUP_USERNAME);
        groupUser.setNick("群聊");
        groupUser.setHeader("");
        userlist.put(Constant.GROUP_USERNAME, groupUser);

        // 存入内存
        XjApplication.getInstance().setContactList(userlist);
        // 存入db
//        UserDao dao = new UserDao(context);
//        List<User> users = new ArrayList<User>(userlist.values());
//        dao.saveContactList(users);

    }

    public static void operatNotify(EMConversation conversation) {
        while (conversation.getAllMsgCount() > 0) {
            List<EMMessage> list = conversation.getAllMessages();
            for (int i = 0; i < list.size(); i++) {
                String emobid = "";
                if (PreferencesUtil.getLogin(XjApplication.getInstance()))
                    emobid = PreferencesUtil.getLoginInfo(XjApplication.getInstance()).getEmobId();
                else
                    emobid = PreferencesUtil.getTourist(XjApplication.getInstance());
                EMMessage message = list.get(i);
                XJNotify notify = new XJNotify(emobid == null ? "" : emobid, message.getIntAttribute(Config.EXPKey_CMD_CODE, 0), message.getStringAttribute("title", ""), message.getStringAttribute("content", ""), message.getIntAttribute("timestamp", 0), false, "no");
                notify.save();
                EventBus.getDefault().post(new NewNotifyEvent(notify, true));
                IndexFragment.newMsgIsRead = false;
                conversation.removeMessage(message.getMsgId());
                int countt = conversation.getAllMsgCount();
                if (countt == 0)
                    EMChatManager.getInstance().deleteConversation(message.getFrom());
            }
        }


    }

    //retrun true 不继续走其他逻辑,不保存用户信息
    public static boolean operatNewMessage(Context context,EMMessage message) {
//        Log.i("onion", "收到消息" +message+message.getBody()+message.getStringAttribute(Config.EXPKey_nickname,"nikename")+message.getStringAttribute(Config.EXPKey_avatar,"")+message.getIntAttribute(Config.EXPKey_CMD_CODE,0));
        if (message.getChatType() == EMMessage.ChatType.GroupChat || message.direct == EMMessage.Direct.SEND)
            return false;

        int cmdCode = message.getIntAttribute(Config.EXPKey_CMD_CODE, 0);
        OrderModel orderModel;
        switch (cmdCode) {//通知
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
            case 121:
            case 122:
                Log.i("onion", "开始删除会话了" + message.getFrom());
//                String emobid = "";
//                if (PreferencesUtil.getLogin(XjApplication.getInstance()))
//                    emobid = PreferencesUtil.getLoginInfo(XjApplication.getInstance()).getEmobId();
//                else
//                    emobid = PreferencesUtil.getTourist(XjApplication.getInstance());
//                XJNotify notify = new XJNotify(emobid == null ? "" : emobid, message.getIntAttribute(Config.EXPKey_CMD_CODE, 0), message.getStringAttribute("title", ""), message.getStringAttribute("content", ""), message.getIntAttribute("timestamp", 0), false);
//                notify.save();
//                EventBus.getDefault().post(new NewNotifyEvent(notify, true));
//                IndexFragment.newMsgIsRead = false;
//                EMConversation  conversation=EMChatManager.getInstance().getConversation(message.getFrom());
//                conversation.removeMessage(message.getMsgId());
//                int countt = conversation.getAllMsgCount();
//                Log.i("onion", "countt" + countt);
//                if (countt == 0)
                EMChatManager.getInstance().deleteConversation(message.getFrom());
//                operatNotify(EMChatManager.getInstance().getConversation(message.getFrom()));
                return true;
            case 110://有新活动
//                int count = PreferencesUtil.getUnReadCount(XjApplication.getInstance()) + 1;
//                PreferencesUtil.saveUnReadCount(XjApplication.getInstance(), count);
//                EventBus.getDefault().post(new NewPushEvent(110));
//                EMConversation conversation1 = EMChatManager.getInstance().getConversation(message.getFrom());
//                conversation1.removeMessage(message.getMsgId());
                EMChatManager.getInstance().deleteConversation(message.getFrom());
                return true;


            case 201:
                XJMessageHelper.saveMessage2DB(message.getMsgId(), message.getStringAttribute(Config.EXPKey_serial, ""), 201); //存订单
                if (null != getOrderModel(message.getStringAttribute(Config.EXPKey_serial, ""), 210)) {
                    EMChatManager.getInstance().getConversation(message.getFrom()).removeMessage(message.getMsgId());
                    break;
                }
                OrderDetailModel orderDetailModel = new Select().from(OrderDetailModel.class).where("serial = ?", message.getStringAttribute(Config.EXPKey_serial, "")).executeSingle();
                if (orderDetailModel == null) {
                    orderDetailModel = new OrderDetailModel();
                    FastGoodsModel model = new Gson().fromJson(message.getStringAttribute(Config.EXPKey_CMD_DETAIL, "{}"), FastGoodsModel.class);
                    orderDetailModel.serial = message.getStringAttribute(Config.EXPKey_serial, "");
                    orderDetailModel.orderDetailBeanList = model.getOrderDetailBeanList().toString();
                    orderDetailModel.total_count = model.totalCount;
                    orderDetailModel.total_price = model.totalPrice + "";
                    orderDetailModel.save();
                }
                orderModel = XJMessageHelper.getOrderModel(message.getStringAttribute(Config.EXPKey_serial, ""), 200);
                if (orderModel != null)
                    updateMesageStateWithId(orderModel.getMsg_id(), EMChatManager.getInstance().getConversation(message.getFrom()));
                break;
            case 202:
                if (null != getOrderModel(message.getStringAttribute(Config.EXPKey_serial, ""), 210)) {
                    EMChatManager.getInstance().getConversation(message.getFrom()).removeMessage(message.getMsgId());
                    break;
                }
                orderModel = XJMessageHelper.getOrderModel(message.getStringAttribute(Config.EXPKey_serial, ""), 200);
                if (orderModel != null)
                    updateMesageStateWithId(orderModel.getMsg_id(), EMChatManager.getInstance().getConversation(message.getFrom()));
            case 302:
                //师傅拒绝
                if (null != getOrderModel(message.getStringAttribute(Config.EXPKey_serial, ""), 310)) {
                    EMChatManager.getInstance().getConversation(message.getFrom()).removeMessage(message.getMsgId());
                    break;
                }
                orderModel = XJMessageHelper.getOrderModel(message.getStringAttribute(Config.EXPKey_serial, ""), 300);
                if (orderModel != null)
                    updateMesageStateWithId(orderModel.getMsg_id(), EMChatManager.getInstance().getConversation(message.getFrom()));
                //  updateMessageStateWithMsg(message);
                orderFail(message.getFrom());
                // updateCancleMessage(message.getStringAttribute(Config.EXPKey_serial, ""),EMChatManager.getInstance().getConversation(message.getFrom()));
                break;
            case 301:
                if (null != XJMessageHelper.getOrderModel(message.getStringAttribute(Config.EXPKey_serial, ""), 310)) {
                    EMChatManager.getInstance().getConversation(message.getFrom()).removeMessage(message.getMsgId());
                    break;
                }
                orderModel = XJMessageHelper.getOrderModel(message.getStringAttribute(Config.EXPKey_serial, ""), 300);
                if (orderModel != null)
                    updateMesageStateWithId(orderModel.getMsg_id(), EMChatManager.getInstance().getConversation(message.getFrom()));
                break;
            case 303:
                //订单已被取消，不处理此信息
                if (null != getOrderModel(message.getStringAttribute(Config.EXPKey_serial, ""), 310)) {
                    EMChatManager.getInstance().getConversation(message.getFrom()).removeMessage(message.getMsgId());
                    break;
                }
                saveMessage2DB(message.getMsgId(), message.getStringAttribute(Config.EXPKey_serial, ""), 303);
                break;
            case 304:
                //双方费用确认
                orderModel = XJMessageHelper.getOrderModel(message.getStringAttribute(Config.EXPKey_serial, ""), 303);
                if (orderModel != null)
                    updateMesageStateWithId(orderModel.getMsg_id(), EMChatManager.getInstance().getConversation(message.getFrom()));
                break;
//            case 307:
//                saveMessage2DB(message.getMsgId(), message.getStringAttribute(Config.EXPKey_serial, ""), 307);
            case 313:
                updateCancleMessage(message.getStringAttribute(Config.EXPKey_serial, ""), EMChatManager.getInstance().getConversation(message.getFrom()));
                orderFail(message.getFrom());
                break;
            case 319:
                if (null != getOrderModel(message.getStringAttribute(Config.EXPKey_serial, ""), 310)) {
                    EMChatManager.getInstance().getConversation(message.getFrom()).removeMessage(message.getMsgId());
                    break;
                }
                orderModel = XJMessageHelper.getOrderModel(message.getStringAttribute(Config.EXPKey_serial, ""), 300);
                if (orderModel != null)
                    updateMesageStateWithId(orderModel.getMsg_id(), EMChatManager.getInstance().getConversation(message.getFrom()));
                // XJMessageHelper.  updateMesageStateWithId(message.getStringAttribute(Config.EXPKey_msgId,""),EMChatManager.getInstance().getConversation(message.getFrom()));
                orderFail(message.getFrom());
                break;

            case 501:
                XJMessageHelper.saveMessage2DB(message.getMsgId(), message.getStringAttribute(Config.EXPKey_serial, ""), 501);
                orderModel = XJMessageHelper.getOrderModel(message.getStringAttribute("code", ""), 500);
                if (orderModel != null)
                    updateMesageStateWithId(orderModel.getMsg_id(), EMChatManager.getInstance().getConversation(message.getFrom()));
                break;
            case 601:
                XJMessageHelper.saveMessage2DB(message.getMsgId(), message.getStringAttribute("welfareId", ""), 601);
                toChnageWelfareOrderStatus(context,message.getStringAttribute("messageId", ""));
                break;

            case 602:
                XJMessageHelper.saveMessage2DB(message.getMsgId(), message.getStringAttribute("welfareId", ""), 602);
                toChnageWelfareOrderStatus(context,message.getStringAttribute("messageId", ""));
                break;
            case 603:
                XJMessageHelper.saveMessage2DB(message.getMsgId(), message.getStringAttribute("messageId", ""), 603);
                toChnageWelfareOrderStatus(context,message.getStringAttribute("messageId", ""));
                break;

//            case 403:
//            case 401:
//                String path = "drawable://" + XjApplication.getInstance().getResources().getIdentifier("ic_launcher", "drawable",
//                        "xj.property");
//                XJContactHelper.saveContact(message.getFrom(), "帮帮客服", path, cmdCode + "");
//                return false;
        }
//            if (message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) != 0 && message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) / 10 < 2) {
//                int notifycode = message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) % 10;
//                Log.i("onion", "notifycode" + notifycode);
//                XJNotify notify = new XJNotify(0, message.getStringAttribute("title", ""), message.getStringAttribute("content", ""), Long.parseLong(message.getStringAttribute("timestamp", "")), false);
//                EventBus.getDefault().post(new NewNotifyEvent(notify, false));
//            }

//        if (message.getChatType() == ChatType.GroupChat)//群聊检测是否有此联系人
//        {
        //        {

//        }
        return false;
    }

    //删除与师傅的订单关系
    public static void orderFail(String emobid) {
        try {
            new Delete().from(ServiceModel.class).where("service_emobid = ? and useremobid=?", emobid, PreferencesUtil.getLoginInfo(XjApplication.getInstance()).getEmobId()).execute();
        } catch (Exception e) {
        }

    }

    //创建与师傅的订单关系
    public static void orderBegin(String emobid) {
        new ServiceModel(emobid, PreferencesUtil.getLoginInfo(XjApplication.getInstance()).getEmobId()).save();
    }

    //查询与师傅的订单关系
    public static ServiceModel orderSelect(String emobid) {
        return new Select().from(ServiceModel.class).where("service_emobid = ? and useremobid=?", emobid, PreferencesUtil.getLoginInfo(XjApplication.getInstance()).getEmobId()).executeSingle();
    }
    interface ChangeWelfareOrderStatusService {
        //        @PUT("/api/v1/communities/{communityId}/welfares/welfareMessages")
//        void post(@Header("signature") String signature, @Body ChangeWelfareOrderStatusBean request, @Path("communityId") int communityId, Callback<CommonPostResultBean> cb);

//        @PUT("/api/v1/communities/{communityId}/welfares/welfareMessages")
        ///api/v3/welfares/messages
        @PUT("/api/v3/welfares/messages")
        void post(@Body ChangeWelfareOrderStatusBean request, Callback<CommonRespBean<String>> cb);
    }

    private static void toChnageWelfareOrderStatus(Context context,String orderid) {
        ChangeWelfareOrderStatusBean request = new ChangeWelfareOrderStatusBean(orderid);
        ChangeWelfareOrderStatusService service = RetrofitFactory.getInstance().create(context,request,ChangeWelfareOrderStatusService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, Response response) {
                if(bean!=null&& TextUtils.equals("yes",bean.getStatus())){
                    Log.d("toChnageWelfareOrderStatus","toChnageWelfareOrderStatus is success");

                }else{
                    Log.d("toChnageWelfareOrderStatus","toChnageWelfareOrderStatus is no");
                }
            }
            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Log.d("toChnageWelfareOrderStatus","toChnageWelfareOrderStatus is failure");
            }
        };
        service.post(request,callback);
    }
}
