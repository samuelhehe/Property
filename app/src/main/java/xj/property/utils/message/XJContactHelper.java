package xj.property.utils.message;

import android.text.TextUtils;
import android.util.Log;

import com.easemob.chat.EMMessage;

import xj.property.XjApplication;
import xj.property.db.UserDao;
import xj.property.domain.User;
import xj.property.utils.other.Config;

/**
 * Created by Administrator on 2015/4/9.
 */
public class XJContactHelper {


    //// 保存当前会话
    public static void saveContact(EMMessage message) {
        String nickName = message.getStringAttribute(Config.EXPKey_nickname, "帮帮用户");
        String avatar = message.getStringAttribute(Config.EXPKey_avatar, "");
        Log.i("onion", "存联系人" + nickName + avatar + message.getIntAttribute(Config.EXPKey_CMD_CODE, 0));
        String emobId = message.getFrom();
        if (message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 401
                || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 402
                || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 403
                || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 404

                || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 500
                || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 501

                || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 701
                || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 702
                || message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) == 703) {

            Log.i("saveContact", "普通的联系人 message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) " + message.getIntAttribute(Config.EXPKey_CMD_CODE, 0));
            XJContactHelper.saveContact(emobId, nickName, avatar, message.getIntAttribute(Config.EXPKey_CMD_CODE, 0) + "");
        } else {
            //// 为了兼容IOS端的shoptype 类型。默认以获取Integer类型为准
            Log.i("debbug", "普通的联系人 EXPKey_SORT" + message.getStringAttribute(Config.EXPKey_SORT, "-1"));
            Log.i("debbug", "普通的联系人 EXPKey_SORT int " + message.getIntAttribute(Config.EXPKey_SORT, -1));

            String shoptype = message.getStringAttribute(Config.EXPKey_SORT, "-1");
            if (!TextUtils.isEmpty(shoptype)) {
                XJContactHelper.saveContact(emobId, nickName, avatar, shoptype);
            } else {
                XJContactHelper.saveContact(emobId, nickName, avatar, message.getIntAttribute(Config.EXPKey_SORT, -1) + "");
            }
        }
    }

    public static void saveContact(User user) {
        new UserDao(XjApplication.getInstance()).saveContact(user);
    }

    public static void saveContact(String emobId, String nickName, String avatar, String sort) {
        Log.i("debbug", "创建联系人");
        Log.i("debbug", "sort=" + sort);
        User userDB = selectContact(emobId);
        User user = new User();
        user.setEid(emobId);
        user.setUsername(emobId);
        user.setNick(nickName);
        user.avatar = avatar;
        user.sort = sort;
        if (userDB != null && userDB.equals(user)) {
            Log.i("debbug", "userDB=" + userDB);
            return;
        } else {
            new UserDao(XjApplication.getInstance()).saveContact(user);
        }
    }

    public static User selectContact(String emobId) {
//      return   new Select().from(XJContact.class).where("emobId = ?", emobId).executeSingle();
        return new UserDao(XjApplication.getInstance()).selectContact(emobId);
    }
}
