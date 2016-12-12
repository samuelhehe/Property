package xj.property.utils.other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.activeandroid.util.Log;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import xj.property.beans.ActivityBean;
import xj.property.beans.ContactPhoneBean;
import xj.property.beans.ContactPhoneListBean;
import xj.property.beans.CooperationPraiseDiscussNotify;
import xj.property.beans.FacilityBean;
import xj.property.beans.IndexBean;
import xj.property.beans.LifeCircleBean;
import xj.property.beans.NeighborListV3Bean;
import xj.property.beans.NewPraiseNotify;
import xj.property.beans.UserInfoDetailBean;
import xj.property.beans.VoteIndexDiscussInfoNotify;
import xj.property.beans.VoteIndexDiscussInfoRespV3Bean;
import xj.property.beans.VoteIndexRespInfoBean;
import xj.property.beans.VoteIndexRespV3Bean;

@SuppressLint("NewApi")
public final class PreferencesUtil {
    // 偏好文件名
    public static final String SHAREDPREFERENCES_NAME = "xj_property";
    public static final String SHAREDPREFERENCES_COMMID = "xj_commid";
    public static final String SHAREDPREFERENCES_TOURIST = "xj_tourist";
    public static final String SHAREDPREFERENCES_USERLOGIN = "xj_property_login";
    public static final String SHAREDPREFERENCES_ACTIVECOUNT = "xj_active_count";
    public static final String SHAREDPREFERENCES_ACTIVEMODEL = "xj_active_model";
    public static final String SHAREDPREFERENCES_MOVE = "xj_move";
    public static final String SHAREDPREFERENCES_CIRCLELIFE = "xj_circle_life";
    public static final String SHAREDPREFERENCES_UPDATEPHONE = "xj_update_phone";
    public static final String SHAREDPREFERENCES_INDEXCACHE = "xj_index_cache";
    public static final String SHAREDPREFERENCES_COOPERATION = "xj_cooperation";
    public static final String SHAREDPREFERENCES_DOORPASTE = "xj_doorpaste";
    public static final String SHAREDPREFERENCES_REFRESHSAME = "refresh_same";

    public static final String SHAREDPREFERENCES_VOTE = "xj_vote";
    /**
     * 保存fragment 中我的状态信息
     */
    public static final String SHAREDPREFERENCES_ME = "xj_me";


    //用户活跃
    public static final String SHAREDPREFERENCES_ACTIVE = "xj_active";

    public static final String UPDATE_PHONE_KEY = "update_phone";

    public static final String SERIAL_KEY = "serial_key";
    public static final String FIRST_OPEN = "first_open";
    public static final String FIRST_ADRESS = "first_address";
    public static final String FIRST_LOGIN = "first_login";
    public static final String NOTIFY_DELETE = "notify_delete";
    // 引导界面KEY
    private static final String KEY_GUIDE_ACTIVITY = "guide_activity";
    private static final String PREF_KEY_CID = "CId";
    private static final String PREF_KEY_USER_ID = "userId";
    private static final String PREF_KEY_LOGIN = "Login";
    private static final String PREF_KEY_NICKNAME = "nickname";
    private static final String PREF_KEY_AVATAR = "avatar";
    private static final String PREF_KEY_USERNAME = "username";
    private static final String PREF_KEY_EMOID = "emobId";
    private static final String PREF_KEY_PHONE = "phone";
    private static final String PREF_KEY_GENDER = "gender";
    private static final String PREF_KEY_AGE = "age";
    private static final String PREF_KEY_IDCARD = "idcard";
    private static final String PREF_KEY_IDNUMBER = "idnumber";
    private static final String PREF_KEY_longitude = "longitude";
    private static final String PREF_KEY_latitude = "latitude";
    private static final String PREF_KEY_COMMUN = "communityId";
    private static final String PREF_KEY_ROLE = "role";
    private static final String PREF_KEY_ROOM = "room";
    private static final String PREF_KEY_CREATE = "createTime";
    private static final String PREF_KEY_STATUS = "status;";
    private static final String PREF_KEY_ISLOGIN = "islogin;";
    private static final String PREF_KEY_IDENTITY = "identity";
    private static final String PREF_KEY_INTRO = "intro";


    //// 2016/02/24 v3 新字段
    private static final String PREF_KEY_CITYID = "cityid";
    private static final String PREF_KEY_cityName = "cityName";
    private static final String PREF_KEY_communityName = "communityName";
    private static final String PREF_KEY_equipment = "equipment";
    private static final String PREF_KEY_equipmentVersion = "equipmentVersion";
    private static final String PREF_KEY_characterValues = "characterValues";
    private static final String PREF_KEY_bonuscoinCount = "bonuscoinCount";
    private static final String PREF_KEY_houtaiAdmin = "houtaiAdmin";
    private static final String PREF_KEY_deviceToken = "deviceToken";


    /// 2/26
    private static final String PREF_KEY_characterPercent = "characterPercent";
    private static final String PREF_KEY_lifeCircleSum = "lifeCircleSum";
    private static final String PREF_KEY_bonuscoinEnable = "bonuscoinEnable";
    private static final String PREF_KEY_famousIntroduce = "famousIntroduce";
    /// 2/26
    //// 2016/02/24 v3 新字段 end

    private static final String PREF_KEY_PASSWORD = "password;";
    private static final String PREF_KEY_SIGNATURE = "signature;";
    private static final String PREF_KEY_USERFLOOR = "userFloor;";
    private static final String PREF_KEY_USERUNIT = "userUnit;";
    private static final String PREF_KEY_UNREADACT = "un_read_count";
    private static final String PREF_KEY_INDEX = "indexbean";
    private static final String PREF_KEY_INDEXCACHE = "indexcache";
    private static final String PREF_KEY_INDEXTIME = "indextime";
    private static final String PREF_KEY_SHOPFASTINDEX = "shopfastindex";
    private static final String PREF_KEY_SHOPFASTINDEXTIME = "shopfasttime";
    private static final String PREF_KEY_ACTIVE = "activebean";
    private static final String PREF_KEY_HASMOVE = "hasMove";
    private static final String PREF_KEY_SAME = "is_refresh";
    public static final String PREF_KEY_PRAISE = "";
    private static final String PREF_KEY_DEFAULT_TAGS = "system_default_tags";

    private static final String PREF_KEY_COOPERATION_CONTENT = "cooperation_content_bean";


    private static final String PREF_KEY_VOTE_CONTENT = "vote_index_content_bean";


    private static final String PREF_KEY_FACILINDEX = "facilitybean";//FacilityBean
    private static final String PREF_KEY_COMMUNName = "communityName";
    private static final String PREF_KEY_UnReadGroup = "unreadgroup";
    private static final String PREF_KEY_BlackList = "BlcakList";
    private static final String PREF_KEY_LIFECIRCLE_BlackList = "LifeCircleBlcakList";

    private static final String PREF_KEY_LASTCHECKTIME = "LastCheckTime";

    private static final String PREF_KEY_LIFECIRCLE = "LifeCircleBean";


    private static final String PREF_LOGOUT_KEY_USER_ID = "logoutuserId";

    private static final String PREF_LOGOUT_KEY_EMOBID = "logoutemobId";
    private static final String PREF_RP_VALUE = "characterValues";//人品值

    private static final String PREF_RP_PERCENT = "characterPercent";//人品值百分比
    private static final String PREF_LIFECIRCLE_COUNT = "lifeCircleSum";//生活圈条数
    private static final String PREF_BANGBANGQUAN_COUNT = "bangbangquan_count";//人品值


    private static final String PREF_BANGBANGBI_COUNT = "bangbangbi_count";//帮帮币
    private static final String PREF_BANGBANGBI_JISHU = "bonuscoin";//帮帮币基数
    private static final String PREF_BANGBANGBI_EXCHANGE = "exchange";//帮帮币兑换值
    private static final String LIFECIRCLE_COUNT_TIME = "lifecirclecounttime";//获取index 更新数量的time
    private static final String PREF_BANGBANGBI_NEW = "newbangbangbi";//新获取的帮帮币
    private static final String SHOWBONUSCOIN = "showBonuscoin";//是否支持帮帮币
    public static final String SHAREDPREFERENCES_CLICKSTATE = "click_state";

    private static final String CLICK_STATE = "clicked";

    /**
     * 记录当前帮内消息访问时间戳
     */
    private static final String PREF_KEY_FRAG_BANGZHU_NEWSS_TIMESTAMP = "BANGZHU_NEWSS_TIMESTAMP";
    private static final String PREF_KEY_SHOPFASTINDEX_INFO = "PREF_KEY_SHOPFASTINDEX_INFO";


    private PreferencesUtil() {
    }


    /**
     * 保存本次访问帮内消息的时间戳
     *
     * @param context
     * @param timestamp
     */
    public static void setCurrentBangZhuNewsTimeStamp(Context context, String timestamp) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(PREF_KEY_FRAG_BANGZHU_NEWSS_TIMESTAMP, timestamp);
        edit.commit();
    }


    /**
     * 清除已当前时间戳为key的sp
     *
     * @param context
     * @param timestamp
     */
    public static void removeCurrentBangZhuNewsTimeStamp(Context context, String timestamp) {

        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.remove(timestamp);

        edit.commit();
    }


    /**
     * 获取当前帮内消息的时间戳
     *
     * @param context
     */
    public static String getCurrentBangZhuNewsTimeStamp(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ME, Context.MODE_PRIVATE);
        if (spf.contains(PREF_KEY_FRAG_BANGZHU_NEWSS_TIMESTAMP)) {
            return spf.getString(PREF_KEY_FRAG_BANGZHU_NEWSS_TIMESTAMP, "-1");
        }
        return "-1";
    }

    /**
     * 设置当前时间戳是否已经阅读过.
     *
     * @param context
     * @param isread
     */
    public static void setCurrentBangzhuNewsReadStatus(Context context, boolean isread) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        String currentTimeStamp = getCurrentBangZhuNewsTimeStamp(context);
        if (TextUtils.equals(currentTimeStamp, "-1")) {
            return;
        } else {
            editor.putBoolean(currentTimeStamp, isread);
        }
        editor.commit();
    }


    /**
     * 设置当前时间戳是否已经阅读过.
     * false : 未读
     *
     * @param context
     */
    public static boolean getCurrentBangzhuNewsReadStatus(Context context, String currentTimeStamp) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ME, Context.MODE_PRIVATE);
        if (spf.contains(currentTimeStamp)) {
            /// 如果存在则直接返回内容.
            return spf.getBoolean(currentTimeStamp, false);
        } else {
            return false;
        }
    }


    public static boolean getInvitedNeighborsUnReadStatus(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ME, Context.MODE_PRIVATE);
        return spf.getBoolean("InvitedNeighborsReadStatus", true);
    }


    public static void setInvitedNeighborsUnReadStatus(Context context, boolean readStatus) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putBoolean("InvitedNeighborsReadStatus", readStatus);
        editor.commit();
    }


    public static boolean getBangzhuUnReadStatus(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ME, Context.MODE_PRIVATE);
        return spf.getBoolean("getBangzhuUnReadStatus", true);
    }


    public static void setBangzhuUnReadStatus(Context context, boolean readStatus) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putBoolean("getBangzhuUnReadStatus", readStatus);
        editor.commit();
    }


    /**
     * 获取帮内最新消息时间戳
     *
     * @param context
     * @return
     */
    public static int getBangZhuElectionLastTimeReqTime(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ME, Context.MODE_PRIVATE);
        return spf.getInt("BangZhuElectionLastTimeReqTime", 0);
    }

    /**
     * 设置帮内最新消息时间戳
     *
     * @param context
     * @param lastReqTime
     */
    public static void setBangZhuElectionLastTimeReqTime(Context context, int lastReqTime) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putInt("BangZhuElectionLastTimeReqTime", lastReqTime);
        editor.commit();
    }

    public static void saveHasMove(Context context, boolean isMove) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_MOVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putBoolean(PREF_KEY_HASMOVE, isMove);
        edit.commit();
    }

    public static boolean getHasMove(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_MOVE, Context.MODE_PRIVATE);
        return spf.getBoolean(PREF_KEY_HASMOVE, false);
    }

    /**
     * 登出系统
     *
     * @param context
     */
    public static void Logout(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_USERLOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putInt(PREF_KEY_USER_ID, 0);
        edit.putLong(PREF_KEY_COMMUN, -1);
        edit.putInt(PREF_KEY_AGE, 0);
        edit.putString(PREF_KEY_AVATAR, "");
        edit.putLong(PREF_KEY_CREATE, 0);
        edit.putString(PREF_KEY_EMOID, "");
        edit.putString(PREF_KEY_IDCARD, "");
        edit.putString(PREF_KEY_IDNUMBER, "");
        edit.putString(PREF_KEY_GENDER, "");
        edit.putString(PREF_KEY_NICKNAME, "");
        edit.putString(PREF_KEY_PHONE, "");
        edit.putString(PREF_KEY_ROLE, "");
        edit.putString(PREF_KEY_ROOM, "");
        edit.putString(PREF_KEY_STATUS, "");
        edit.putString(PREF_KEY_USERNAME, "");
        edit.putString(PREF_KEY_PASSWORD, "");
        edit.putString(PREF_KEY_SIGNATURE, "");
        edit.putString(PREF_KEY_USERFLOOR, "");
        edit.putString(PREF_KEY_USERUNIT, "");
        edit.putBoolean(PREF_KEY_ISLOGIN, false).commit();
        spf.edit().clear().commit();
        saveLogin(context, false);
        clearRp(context);
    }

    //存游客的empbId
    public static void saveTourist(Context context, String emobId, int touristId) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_TOURIST, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(PREF_KEY_EMOID, emobId);
        edit.putInt(PREF_KEY_USER_ID, touristId);
        edit.commit();
    }

    /**
     * 获取游客emoid
     *
     * @param context
     * @return
     */
    public static String getTourist(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_TOURIST, Context.MODE_PRIVATE);
        return spf.getString(PREF_KEY_EMOID, "");
    }

    /**
     * 获取游客userId
     *
     * @param context
     * @return
     */
    public static int getTouristid(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_TOURIST, Context.MODE_PRIVATE);
        return spf.getInt(PREF_KEY_USER_ID, 0);
    }

    /**
     * 获取游客emoid
     *
     * @param context
     * @return
     */
    public static String getTouristEmob(Context context) {
        return getTourist(context);
    }

    //存游客的cid
    public static void saveCid(Context context, String cid) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_TOURIST, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(PREF_KEY_CID, cid);
        edit.commit();
    }

    //获取用户保存的版本信息
    public static String getSavedVersionName(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        return spf.getString("SavedVersionName", "");
    }

    public static void saveCurrentVersionName(Context context, String versionName) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString("SavedVersionName", versionName);
        edit.commit();
    }

    //更新时间检测
    public static long getCheckTime(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        return spf.getLong(PREF_KEY_LASTCHECKTIME, 0);
    }

    public static void saveCheckTime(Context context, long time) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putLong(PREF_KEY_LASTCHECKTIME, time);
        edit.commit();
    }

    //更新首页时间
    public static int getIndexTime(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        return spf.getInt(PREF_KEY_INDEXTIME, 0);
    }

    public static void saveIndexTime(Context context, int time) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putInt(PREF_KEY_INDEXTIME, time);
        edit.commit();
    }

    //更新快店标签时间
    public static int getFastShopTime(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        return spf.getInt(PREF_KEY_SHOPFASTINDEXTIME, 0);
    }

    public static void saveFastShopTime(Context context, int time) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putInt(PREF_KEY_SHOPFASTINDEXTIME, time);
        edit.commit();
    }

    //获取新动态时间
    public static int getNewCircleTime(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_CIRCLELIFE, Context.MODE_PRIVATE);
        return spf.getInt(SHAREDPREFERENCES_CIRCLELIFE, (int) (new Date().getTime() / 1000));
    }

    public static void saveNewCircleTime(Context context, int time) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_CIRCLELIFE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putInt(SHAREDPREFERENCES_CIRCLELIFE, time);
        edit.commit();
    }

    public static boolean getUpdatePhone(Context context, Boolean isUpdate) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_UPDATEPHONE, Context.MODE_PRIVATE);
        return spf.getBoolean(UPDATE_PHONE_KEY, isUpdate);
    }

    public static void saveUpdatePhone(Context context, Boolean isUpdate) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_UPDATEPHONE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putBoolean(UPDATE_PHONE_KEY, isUpdate);
        edit.commit();
    }


    public static String getCid(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_TOURIST, Context.MODE_PRIVATE);
        return spf.getString(PREF_KEY_CID, "");
    }

    //存游客的登录状态
    public static void saveTourist(Context context, boolean isSuccess) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_TOURIST, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putBoolean(PREF_KEY_LOGIN, isSuccess);
        edit.commit();
    }

    //// 看是否是游客状态登陆
    public static boolean getTouristLogin(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_TOURIST, Context.MODE_PRIVATE);
        return spf.getBoolean(PREF_KEY_LOGIN, false);
    }

    //存新获取帮帮币
    public static void saveNewBangBangBi(Context context, boolean isSuccess) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_TOURIST, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putBoolean(PREF_BANGBANGBI_NEW, isSuccess);
        edit.commit();
    }

    public static boolean getNewBangBangBi(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_TOURIST, Context.MODE_PRIVATE);
        return spf.getBoolean(PREF_BANGBANGBI_NEW, false);
    }

    /**
     * 保存小区信息
     *
     * @param context
     * @param id      小区ID
     * @param name    小区名字
     */
    public static void saveCommity(Context context, int id, String name) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_COMMID, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putInt(PREF_KEY_COMMUN, id);
        edit.putString(PREF_KEY_COMMUNName, name);
        edit.commit();
    }

    public static int getCommityId(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_COMMID, Context.MODE_PRIVATE);
        return spf.getInt(PREF_KEY_COMMUN, -1);
    }

    public static String getCommityName(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_COMMID, Context.MODE_PRIVATE);
        return spf.getString(PREF_KEY_COMMUNName, "");
    }

    public static void saveLogin(Context context, UserInfoDetailBean bean) {
        saveUserIdAndEmobTd(context, bean.getUserId(), bean.getEmobId());
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_USERLOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putInt(PREF_KEY_USER_ID, bean.getUserId());
        edit.putInt(PREF_KEY_COMMUN, bean.getCommunityId());
        edit.putInt(PREF_KEY_AGE, bean.getAge());
        edit.putString(PREF_KEY_AVATAR, bean.getAvatar());
        edit.putLong(PREF_KEY_CREATE, bean.getCreateTime());
        edit.putString(PREF_KEY_EMOID, bean.getEmobId());
        edit.putString(PREF_KEY_IDCARD, bean.getIdcard());
        edit.putString(PREF_KEY_IDNUMBER, bean.getIdnumber());
        edit.putString(PREF_KEY_GENDER, bean.getGender());
        edit.putString(PREF_KEY_NICKNAME, bean.getNickname());
        edit.putString(PREF_KEY_PHONE, bean.getPhone());
        edit.putString(PREF_KEY_ROLE, bean.getRole());
        edit.putString(PREF_KEY_ROOM, bean.getRoom());
        edit.putString(PREF_KEY_STATUS, bean.getStatus());
        edit.putString(PREF_KEY_USERNAME, bean.getUsername());
        edit.putString(PREF_KEY_PASSWORD, bean.getPassword());
        edit.putString(PREF_KEY_SIGNATURE, bean.getSignature());
        edit.putString(PREF_KEY_USERFLOOR, bean.getUserFloor());
        edit.putString(PREF_KEY_USERUNIT, bean.getUserUnit());
        edit.putString(PREF_KEY_IDENTITY, bean.getIdentity());
        edit.putString(PREF_KEY_INTRO, bean.getIntro());


        //// 2016/02/24  v3 start

        edit.putInt(PREF_KEY_CITYID, bean.getCityId());
        edit.putString(PREF_KEY_cityName, bean.getCityName());
        edit.putString(PREF_KEY_communityName, bean.getCommunityName());
        edit.putString(PREF_KEY_equipment, bean.getEquipment());
        edit.putString(PREF_KEY_equipmentVersion, bean.getEquipmentVersion());
        edit.putInt(PREF_KEY_characterValues, bean.getCharacterValues());
        edit.putInt(PREF_KEY_bonuscoinCount, bean.getBonuscoinCount());
        edit.putString(PREF_KEY_houtaiAdmin, bean.getHoutaiAdmin());
        edit.putString(PREF_KEY_deviceToken, bean.getDeviceToken());

        /**
         * characterPercent : 0.0
         * lifeCircleSum : 0
         * bonuscoinEnable : 1
         * famousIntroduce  牛人介绍
         */

        edit.putString(PREF_KEY_famousIntroduce, bean.getFamousIntroduce());

        edit.putInt(PREF_KEY_bonuscoinEnable, bean.getBonuscoinEnable());

        edit.putInt(PREF_KEY_lifeCircleSum, bean.getLifeCircleSum());

        edit.putFloat(PREF_KEY_characterPercent, bean.getCharacterPercent());

        //// 2016/02/24  v3 end
        edit.putBoolean(PREF_KEY_ISLOGIN, true).commit();

        saveLogin(context, true);
    }

    public static UserInfoDetailBean getLoginInfo(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_USERLOGIN, Context.MODE_PRIVATE);
        UserInfoDetailBean bean = null;
        if (spf.getBoolean(PREF_KEY_ISLOGIN, false)) {
            bean = new UserInfoDetailBean();
            bean.setUserId(spf.getInt(PREF_KEY_USER_ID, 0));
            bean.setCommunityId(spf.getInt(PREF_KEY_COMMUN, 0));
            bean.setAge(spf.getInt(PREF_KEY_AGE, 0));
            bean.setAvatar(spf.getString(PREF_KEY_AVATAR, ""));
            bean.setUserId(spf.getInt(PREF_KEY_USER_ID, 0));
            bean.setCreateTime(spf.getLong(PREF_KEY_CREATE, 0));
            bean.setEmobId(spf.getString(PREF_KEY_EMOID, ""));
            bean.setIdcard(spf.getString(PREF_KEY_IDCARD, ""));
            bean.setIdnumber(spf.getString(PREF_KEY_IDNUMBER, ""));
            bean.setNickname(spf.getString(PREF_KEY_NICKNAME, ""));
            bean.setPhone(spf.getString(PREF_KEY_PHONE, ""));
            bean.setRole(spf.getString(PREF_KEY_ROLE, ""));
            bean.setRoom(spf.getString(PREF_KEY_ROOM, ""));
            bean.setStatus(spf.getString(PREF_KEY_STATUS, ""));
            bean.setUsername(spf.getString(PREF_KEY_USERNAME, ""));
            bean.setPassword(spf.getString(PREF_KEY_PASSWORD, ""));
            bean.setGender(spf.getString(PREF_KEY_GENDER, ""));
            bean.setSignature(spf.getString(PREF_KEY_SIGNATURE, ""));
            bean.setUserUnit(spf.getString(PREF_KEY_USERUNIT, ""));
            bean.setUserFloor(spf.getString(PREF_KEY_USERFLOOR, ""));
            bean.setIdentity(spf.getString(PREF_KEY_IDENTITY, ""));
            bean.setIntro(spf.getString(PREF_KEY_INTRO, ""));

            //2016/02/24 v3
            bean.setCityId(spf.getInt(PREF_KEY_CITYID, 0));
            bean.setCityName(spf.getString(PREF_KEY_cityName, ""));
            bean.setCommunityName(spf.getString(PREF_KEY_communityName, ""));
            bean.setEquipment(spf.getString(PREF_KEY_equipment, ""));
            bean.setEquipmentVersion(spf.getString(PREF_KEY_equipmentVersion, ""));
            bean.setCharacterValues(spf.getInt(PREF_KEY_characterValues, 0));
            bean.setBonuscoinCount(spf.getInt(PREF_KEY_bonuscoinCount, 0));
            bean.setHoutaiAdmin(spf.getString(PREF_KEY_houtaiAdmin, ""));
            bean.setDeviceToken(spf.getString(PREF_KEY_deviceToken, ""));

            /**
             * characterPercent : 0.0
             * lifeCircleSum : 0
             * bonuscoinEnable : 1
             * famousIntroduce  牛人介绍
             */
            bean.setBonuscoinEnable(spf.getInt(PREF_KEY_bonuscoinEnable, 0)); /// 1 开通，  0 默认未开通
            bean.setLifeCircleSum(spf.getInt(PREF_KEY_lifeCircleSum, 0));
            bean.setFamousIntroduce(spf.getString(PREF_KEY_famousIntroduce, ""));
            bean.setCharacterPercent(spf.getFloat(PREF_KEY_characterPercent, 0f));

            //2016/02/24 v3 end
        }
        return bean;
    }

    // 保存用户id  SHAREDPREFERENCES_NAME
    public static void saveUserId(Context context, int id) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_USERLOGIN, Context.MODE_PRIVATE);
        spf.edit().putInt(PREF_KEY_USER_ID, id).commit();
    }

    // 获得用户id
    public static int getUserId(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_USERLOGIN, Context.MODE_PRIVATE);
        return spf.getInt(PREF_KEY_USER_ID, 0);
    }

    // 保存登陆状态
    public static void saveLogin(Context context, final boolean isTrue) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        spf.edit().putBoolean(PREF_KEY_LOGIN, isTrue).commit();
    }


    // 获得登陆状态
    public static boolean getLogin(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        return spf.getBoolean(PREF_KEY_LOGIN, false);
    }


    //// 是否第一次打开应用
    public static boolean isFirstOpen(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        return spf.getBoolean(FIRST_OPEN, true);
    }

    ////设置第一次打开// 标记打开小区人口种类对话框
    public static void setFirstOpen(Context context, boolean isfirstOpen) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        spf.edit().putBoolean(PreferencesUtil.FIRST_OPEN, isfirstOpen).commit();
    }


    //升级后第一次运行
    public static int isFirstUpdate(Context context, String version) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        return spf.getInt(version, 0);
    }

    //升级后第一次运行时间
    public static void saveFirstUpdate(Context context, String version, int time) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        spf.edit().putInt(version, time).commit();
    }

    public static int isNotifyDelete(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        return spf.getInt(NOTIFY_DELETE, 0);
    }

    public static void saveNotifyDelete(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        spf.edit().putInt(NOTIFY_DELETE, 1).commit();
    }


    public static boolean isFirstLogin(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        return spf.getBoolean(FIRST_LOGIN, false);
    }

    public static boolean isFirstAddress(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        return spf.getBoolean(FIRST_ADRESS, true);
    }

    // 保存第一次登陆
    public static void saveFirstLogin(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVECOUNT, Context.MODE_PRIVATE);
        spf.edit().putBoolean(FIRST_LOGIN, true).commit();
    }

    // 保存第一次使用活动
    public static void saveFirstUseActivities(Context context, boolean isFirst) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVECOUNT, Context.MODE_PRIVATE);
        spf.edit().putBoolean("FirstUseActivities", isFirst).commit();
    }

    // 是否是第一次使用活动
    public static boolean isFirstUseActivities(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVECOUNT, Context.MODE_PRIVATE);
        return spf.getBoolean("FirstUseActivities", true);
    }

    // 保存未读活动数量
    public static void saveUnReadCount(Context context, int count) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVECOUNT, Context.MODE_PRIVATE);
        spf.edit().putInt(PREF_KEY_UNREADACT, count).commit();
    }

    // 获得未读活动数量
    public static int getUnReadCount(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVECOUNT, Context.MODE_PRIVATE);
        return spf.getInt(PREF_KEY_UNREADACT, 0);
    }

    // 保存未读生活圈数量
    public static void saveUnReadCircleCount(Context context, int count) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_CIRCLELIFE, Context.MODE_PRIVATE);
        spf.edit().putInt(PREF_KEY_UNREADACT, count).commit();
    }

    // 获得未读活动数量
    public static int getUnReadCircleCount(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_CIRCLELIFE, Context.MODE_PRIVATE);
        return spf.getInt(PREF_KEY_UNREADACT, 0);
    }

    // 保存未读福利数量
    public static void saveWelfareCount(Context context, int count) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_CIRCLELIFE, Context.MODE_PRIVATE);
        spf.edit().putInt("WelfareCount", count).commit();
    }


    // 获得未读福利数量
    public static int getWelfareCount(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_CIRCLELIFE, Context.MODE_PRIVATE);
        return spf.getInt("WelfareCount", 0);
    }


    // 保存未读会员卡数量
    public static void saveShopVipCardCount(Context context, int count) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_CIRCLELIFE, Context.MODE_PRIVATE);
        spf.edit().putInt("ShopVipCard", count).commit();
    }

    // 获得未读会员卡数量
    public static int getShopVipCardCount(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_CIRCLELIFE, Context.MODE_PRIVATE);
        return spf.getInt("ShopVipCard", 0);
    }


    //// 是否读过会员卡
    public static void saveIsUnReadVipCard(Context context, boolean unreaded) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_CIRCLELIFE, Context.MODE_PRIVATE);
        spf.edit().putBoolean("saveIsUnReadVipCard", unreaded).commit();
    }


    public static boolean isUnReadVipCard(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_CIRCLELIFE, Context.MODE_PRIVATE);
        return spf.getBoolean("saveIsUnReadVipCard", true);
    }


    // 保存未读邻居帮数量
    public static void saveCooperationIndexCount(Context context, int count) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_COOPERATION, Context.MODE_PRIVATE);
        spf.edit().putInt("CooperationIndex", count).commit();
    }

    // 获得未读邻居帮数量
    public static int getCooperationIndexCount(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_COOPERATION, Context.MODE_PRIVATE);
        return spf.getInt("CooperationIndex", 0);
    }


    // 保存未读门贴数量
    public static void saveDoorPasteIndexCount(Context context, int count) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_DOORPASTE, Context.MODE_PRIVATE);
        spf.edit().putInt("DoorPasteIndex", count).commit();
    }

    // 获得未读门贴数量
    public static int getDoorPasteIndexCount(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_DOORPASTE, Context.MODE_PRIVATE);
        return spf.getInt("DoorPasteIndex", 0);
    }


    // 保存未读投票数量
    public static void saveVoteIndexCount(Context context, int count) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_VOTE, Context.MODE_PRIVATE);
        spf.edit().putInt("voteIndex_count", count).commit();
    }

    // 获得未读投票数量
    public static int getVoteIndexCount(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_VOTE, Context.MODE_PRIVATE);
        return spf.getInt("voteIndex_count", 0);
    }


    // 保存当前阅读人品排行榜时间
    public static void saveReadRPTopListTime(Context context, long readTime) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_COOPERATION, Context.MODE_PRIVATE);
        spf.edit().putLong("ReadRPTopListTime", readTime).commit();
    }

    // 获取阅读人品排行榜时间
    public static long getReadRPTopListTime(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_COOPERATION, Context.MODE_PRIVATE);
        return spf.getLong("ReadRPTopListTime", 0L);
    }


    //// 是否读过邻居帮
    public static void saveIsUnReadCooperationIndex(Context context, boolean unreaded) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_COOPERATION, Context.MODE_PRIVATE);
        spf.edit().putBoolean("UnReadCooperationIndex", unreaded).commit();
    }


    public static boolean isUnReadCooperationIndex(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_COOPERATION, Context.MODE_PRIVATE);
        return spf.getBoolean("UnReadCooperationIndex", true);
    }


    //// 是否读过门贴
    public static void saveIsUnReadDoorPasteIndex(Context context, boolean unreaded) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_DOORPASTE, Context.MODE_PRIVATE);
        spf.edit().putBoolean("saveIsUnReadDoorPasteIndex", unreaded).commit();
    }


    public static boolean isUnReadDoorPastenIndex(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_DOORPASTE, Context.MODE_PRIVATE);
        return spf.getBoolean("saveIsUnReadDoorPasteIndex", true);
    }

    ///是否读过投票部分
    public static void saveIsUnReadVoteIndex(Context context, boolean unreaded) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_VOTE, Context.MODE_PRIVATE);
        spf.edit().putBoolean("UnReadVoteIndex", unreaded).commit();
    }


    public static boolean isUnReadVoteIndex(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_VOTE, Context.MODE_PRIVATE);
        return spf.getBoolean("UnReadVoteIndex", true);
    }


    public static List<String> getUnNotifyGroupS(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        Set<String> set = spf.getStringSet(PREF_KEY_UnReadGroup, null);
        if (set == null) return new ArrayList<>();
        return new ArrayList<>(set);
    }

    public static void saveUnNotifyGroupS(Context context, List<String> list) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        spf.edit().putStringSet(PREF_KEY_UnReadGroup, new HashSet<String>(list)).commit();
    }

    public static List<String> getBlackList(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        Set<String> set = spf.getStringSet(PREF_KEY_BlackList, null);
        if (set == null) return new ArrayList<>();
        return new ArrayList<>(set);
    }

    public static void saveBlackList(Context context, List<String> list) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        spf.edit().putStringSet(PREF_KEY_BlackList, new HashSet<String>(list)).commit();
    }

    public static List<String> getLifeCircleBlackList(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        Set<String> set = spf.getStringSet(PREF_KEY_LIFECIRCLE_BlackList, null);
        if (set == null) return new ArrayList<>();
        return new ArrayList<>(set);
    }

    public static void saveLifeCircleBlackList(Context context, List<String> list) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        spf.edit().putStringSet(PREF_KEY_LIFECIRCLE_BlackList, new HashSet<String>(list)).commit();
    }

    public static void clearBlackList(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        List<String> list = new ArrayList<String>();
        spf.edit().putStringSet(PREF_KEY_BlackList, new HashSet<String>(list)).commit();
    }

    // 保存生活圈首页动态
    public static void saveCircleList(Context context, List<LifeCircleBean> beans) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_CIRCLELIFE, Context.MODE_PRIVATE);
        String content = new Gson().toJsonTree(beans).toString();
        spf.edit().putString(PREF_KEY_LIFECIRCLE, content).commit();
    }

    // 获得生活圈首页动态
    public static List<LifeCircleBean> getCircleList(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_CIRCLELIFE, Context.MODE_PRIVATE);
        String content = spf.getString(PREF_KEY_LIFECIRCLE, "");
        List<LifeCircleBean> beans = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(content);
            if (array != null) {
                Gson gson = new Gson();
                for (int i = 0; i < array.length(); i++) {
                    beans.add(gson.fromJson(array.getJSONObject(i).toString(), LifeCircleBean.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beans;
    }


    // 保存邻居帮首页第一页缓存
    public static void saveCooperationBeanList(Context context, List<NeighborListV3Bean.NeighborListData> beans) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_COOPERATION, Context.MODE_PRIVATE);
        String content = new Gson().toJsonTree(beans).toString();
        spf.edit().putString(PREF_KEY_COOPERATION_CONTENT, content).commit();
    }

    // 获得邻居帮首页第一页缓存
    public static List<NeighborListV3Bean.NeighborListData> getCooperationBeanList(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_COOPERATION, Context.MODE_PRIVATE);
        String content = spf.getString(PREF_KEY_COOPERATION_CONTENT, "");
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        List<NeighborListV3Bean.NeighborListData> beans = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(content);
            if (array != null) {
                Gson gson = new Gson();
                for (int i = 0; i < array.length(); i++) {
                    beans.add(gson.fromJson(array.getJSONObject(i).toString(), NeighborListV3Bean.NeighborListData.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beans;
    }

    // 保存投票首页第一页缓存
    public static void saveVoteBeanList(Context context, List<VoteIndexRespInfoBean> beans) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_VOTE, Context.MODE_PRIVATE);
        String content = new Gson().toJsonTree(beans).toString();
        spf.edit().putString(PREF_KEY_VOTE_CONTENT, content).commit();
    }

    // 获得投票首页第一页缓存
    public static List<VoteIndexRespInfoBean> getVoteBeanList(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_VOTE, Context.MODE_PRIVATE);
        String content = spf.getString(PREF_KEY_VOTE_CONTENT, "");
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        List<VoteIndexRespInfoBean> beans = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(content);
            if (array != null) {
                Gson gson = new Gson();
                for (int i = 0; i < array.length(); i++) {
                    beans.add(gson.fromJson(array.getJSONObject(i).toString(), VoteIndexRespInfoBean.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beans;
    }


    // 保存快店首页
    public static void saveFastShopIndexInfo(Context context, ContactPhoneListBean beans) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        String content = new Gson().toJsonTree(beans).toString();
        spf.edit().putString(PREF_KEY_SHOPFASTINDEX_INFO, content).commit();
    }

    // 获得快店首页信息
    public static ContactPhoneListBean getFastShopIndexInfo(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        String content = spf.getString(PREF_KEY_SHOPFASTINDEX_INFO, "");
        if (TextUtils.isEmpty(content)) {
            return new ContactPhoneListBean();
        }
        Gson gson = new Gson();
        return gson.fromJson(content, ContactPhoneListBean.class);
    }


    // 保存首页按钮
    public static void saveFastShopIndex(Context context, List<ContactPhoneBean> beans) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        String content = new Gson().toJsonTree(beans).toString();
        spf.edit().putString(PREF_KEY_SHOPFASTINDEX, content).commit();
    }

    // 获得快店首页按钮
    public static List<ContactPhoneBean> getFastShopIndex(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        String content = spf.getString(PREF_KEY_SHOPFASTINDEX, "");
        if (TextUtils.isEmpty(content)) {
            return new ArrayList<ContactPhoneBean>();
        }
        List<ContactPhoneBean> beans = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(content);
            if (array != null) {
                Gson gson = new Gson();
                for (int i = 0; i < array.length(); i++) {
                    beans.add(gson.fromJson(array.getJSONObject(i).toString(), ContactPhoneBean.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beans;
    }

    // 保存首页按钮
    public static void saveIndexBean(Context context, List<IndexBean> beans) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        String content = new Gson().toJsonTree(beans).toString();
        spf.edit().remove(PREF_KEY_INDEX).commit();
        Log.d("saveIndexBean", "saveIndexBean " + content);
        spf.edit().putString(PREF_KEY_INDEX, content).commit();
    }

    // 获得首页按钮
    public static List<IndexBean> getIndexBean(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        String content = spf.getString(PREF_KEY_INDEX, "");
//        Log.d("getIndexBean","getIndexBean "+ content);
        List<IndexBean> beans = new ArrayList<IndexBean>();

        try {
            if (!TextUtils.isEmpty(content)) {
                JSONArray array = new JSONArray(content);
                if (array != null) {
                    Gson gson = new Gson();
                    for (int i = 0; i < array.length(); i++) {
                        beans.add(gson.fromJson(array.getJSONObject(i).toString(), IndexBean.class));
                    }
                }
            } else {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beans;
    }

    public static final boolean isRefreshIndexCache(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(SHAREDPREFERENCES_INDEXCACHE, Context.MODE_PRIVATE);
        return spf.getBoolean(PREF_KEY_INDEXCACHE, false);
    }

    public static final void setRefreshIndexCache(Context context, boolean isCache) {
        final SharedPreferences spf = context.getSharedPreferences(SHAREDPREFERENCES_INDEXCACHE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putBoolean(PREF_KEY_INDEXCACHE, isCache).commit();
    }

    /**
     * 记录标签是否需要刷新
     *
     * @param context
     */
    public static final boolean isRefreshSame(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(SHAREDPREFERENCES_REFRESHSAME, Context.MODE_PRIVATE);
        return spf.getBoolean(PREF_KEY_SAME, false);
    }

    /**
     * 记录标签是否需要刷新
     *
     * @param context
     * @param isCache
     */
    public static final void setRefreshSame(Context context, boolean isCache) {
        final SharedPreferences spf = context.getSharedPreferences(SHAREDPREFERENCES_REFRESHSAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putBoolean(PREF_KEY_SAME, isCache).commit();
    }

    public static final String removeBOM(String data) {
        if (TextUtils.isEmpty(data)) {
            return data;
        }

        if (data.startsWith("\ufeff")) {
            return data.substring(1);
        } else {
            return data;
        }
    }

    // 保存评论
    public static void saveNewPraise(Context context, String emobid, List<NewPraiseNotify> beans) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_CIRCLELIFE, Context.MODE_PRIVATE);
        String content = new Gson().toJsonTree(beans).toString();
        android.util.Log.d("saveNewPraise  ---", content);
        spf.edit().putString(PREF_KEY_PRAISE + emobid, content).commit();
    }


    // 获得评论
    public static List<NewPraiseNotify> getNewPraise(Context context, String emobid) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_CIRCLELIFE, Context.MODE_PRIVATE);
        String content = spf.getString(PREF_KEY_PRAISE + emobid, "");

        android.util.Log.d("getNewPraise  ---", content);
        if (TextUtils.isEmpty(content)) {
            return new ArrayList<NewPraiseNotify>();
        }
        content = removeBOM(content);

        android.util.Log.d("getNewPraise  ---", content);
        List<NewPraiseNotify> beans = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(content);
            if (array != null) {
                Gson gson = new Gson();
                for (int i = 0; i < array.length(); i++) {
                    beans.add(gson.fromJson(array.getJSONObject(i).toString(), NewPraiseNotify.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (NewPraiseNotify nofity : beans) {
            android.util.Log.d("getNewPraise  ---", "" + nofity.toString());
        }
        return beans;
    }


    // 保存投票首页评论
    public static void saveVoteDiscussInfoNotify(Context context, String emobid, List<VoteIndexDiscussInfoRespV3Bean> beans) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_VOTE, Context.MODE_PRIVATE);
        String content = new Gson().toJsonTree(beans).toString();
        android.util.Log.d("saveVoteDiscussInfoNotify  ---", content);
        spf.edit().putString("VoteDiscussInfoNotify" + emobid, content).commit();
    }

    // 获得投票首页评论
    public static List<VoteIndexDiscussInfoRespV3Bean> getVoteDiscussInfoNotify(Context context, String emobid) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_VOTE, Context.MODE_PRIVATE);
        String content = spf.getString("VoteDiscussInfoNotify" + emobid, "");
        android.util.Log.d("getVoteDiscussInfoNotify  ---", content);
        if (TextUtils.isEmpty(content)) {
            return new ArrayList<VoteIndexDiscussInfoRespV3Bean>();
        }
        content = removeBOM(content);

        android.util.Log.d("getVoteDiscussInfoNotify removeBOM  ---", content);

        List<VoteIndexDiscussInfoRespV3Bean> beans = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(content);
            if (array != null) {
                Gson gson = new Gson();
                for (int i = 0; i < array.length(); i++) {
                    beans.add(gson.fromJson(array.getJSONObject(i).toString(), VoteIndexDiscussInfoRespV3Bean.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (VoteIndexDiscussInfoRespV3Bean nofity : beans) {
            android.util.Log.d("getVoteDiscussInfoNotify  ---", "" + nofity.toString());
        }
        return beans;
    }


    // 保存邻居帮评论
    public static void saveCooperationNotify(Context context, String emobid, List<CooperationPraiseDiscussNotify> beans) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_COOPERATION, Context.MODE_PRIVATE);
        String content = new Gson().toJsonTree(beans).toString();
        android.util.Log.d("saveCooperationNotify  ---", content);
        spf.edit().putString("CooperationNotify" + emobid, content).commit();
    }


    // 获得邻居帮评论
    public static List<CooperationPraiseDiscussNotify> getCooperationNotify(Context context, String emobid) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_COOPERATION, Context.MODE_PRIVATE);
        String content = spf.getString("CooperationNotify" + emobid, "");
        android.util.Log.d("getCooperationNotify  ---", content);
        if (TextUtils.isEmpty(content)) {
            return new ArrayList<CooperationPraiseDiscussNotify>();
        }

        content = removeBOM(content);

        android.util.Log.d("getCooperationNotify removeBOM  ---", content);

        List<CooperationPraiseDiscussNotify> beans = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(content);
            if (array != null) {
                Gson gson = new Gson();
                for (int i = 0; i < array.length(); i++) {
                    beans.add(gson.fromJson(array.getJSONObject(i).toString(), CooperationPraiseDiscussNotify.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (CooperationPraiseDiscussNotify nofity : beans) {
            android.util.Log.d("getCooperationNotify  ---", "" + nofity.toString());
        }
        return beans;
    }


    // 保存系统默认标签
    public static void saveNewSysTags(Context context, String emobid, List<String> beans) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        String content = new Gson().toJsonTree(beans).toString();
        Log.d("saveNewSysTags ", "" + content);
        spf.edit().putString(PREF_KEY_DEFAULT_TAGS + emobid, content).commit();
    }


    // 获得系统默认标签
    public static List<String> getNewSysTags(Context context, String emobid) {
        final SharedPreferences spf = context.getSharedPreferences(SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        String content = spf.getString(PREF_KEY_DEFAULT_TAGS + emobid, "");

        content = removeBOM(content);

        Log.d("getNewSysTags ", "content " + content);
        List<String> beans = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(content);
            if (array != null) {
                for (int i = 0; i < array.length(); i++) {
                    beans.add(String.valueOf(array.getJSONObject(i)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("getNewSysTags ", "" + beans);
        return beans;
    }


    public static int getLastReqTagsTime(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        return spf.getInt("LastReqTagsTime", 0);
    }

    public static void setLastReqTagsTime(Context context, int lastTime) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        spf.edit().putInt("LastReqTagsTime", lastTime).commit();
    }


    public static int getLastReqTagsChangeTime(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_COOPERATION, Context.MODE_PRIVATE);
        return spf.getInt("LastReqTagsChangeTime", 0);
    }


    public static void setLastReqTagsChangeTime(Context context, int lastTime) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_COOPERATION, Context.MODE_PRIVATE);
        spf.edit().putInt("LastReqTagsChangeTime", lastTime).commit();
    }


    //// 标签的变化用户是否阅读, 有消息如果阅读过则不显示,有消息没有阅读过则显示, 如果没有消息不显示
    public static void saveIsUnReadTagsChange(Context context, boolean unreaded) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_COOPERATION, Context.MODE_PRIVATE);
        spf.edit().putBoolean("IsUnReadTagsChange", unreaded).commit();
    }


    public static boolean getisUnReadTagsChange(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_COOPERATION, Context.MODE_PRIVATE);
        return spf.getBoolean("IsUnReadTagsChange", false);
    }


    //// 活动顶部的图片URL缓存 ... ///2016/1/12
    public static void saveActivityBannerImg(Context context, String bannerImgurl) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVEMODEL, Context.MODE_PRIVATE);
        spf.edit().putString("ActivityBannerImg", bannerImgurl).commit();
    }


    public static String getActivityBannerImg(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVEMODEL, Context.MODE_PRIVATE);
        return spf.getString("ActivityBannerImg", "");
    }


    // 保存活动
    public static void saveActivityBean(Context context, List<ActivityBean> beans) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVEMODEL, Context.MODE_PRIVATE);
        String content = new Gson().toJsonTree(beans).toString();
        spf.edit().putString(PREF_KEY_ACTIVE, content).commit();
    }

    // 获得首页活动
    public static List<ActivityBean> getActivityBean(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVEMODEL, Context.MODE_PRIVATE);
        String content = spf.getString(PREF_KEY_ACTIVE, "");
        List<ActivityBean> beans = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(content);
            if (array != null) {
                Gson gson = new Gson();
                for (int i = 0; i < array.length(); i++) {
                    beans.add(gson.fromJson(array.getJSONObject(i).toString(), ActivityBean.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beans;
    }

    // 保存周边
    public static void saveFacilityBean(Context context, List<FacilityBean> beans) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        String content = new Gson().toJsonTree(beans).toString();
        spf.edit().putString(PREF_KEY_FACILINDEX, content).commit();
    }

    // 获得登陆状态
    public static List<FacilityBean> getFacilityBean(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        String content = spf.getString(PREF_KEY_FACILINDEX, "");
        List<FacilityBean> beans = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(content);
            if (array != null) {
                Gson gson = new Gson();
                for (int i = 0; i < array.length(); i++) {
                    beans.add(gson.fromJson(array.getJSONObject(i).toString(), FacilityBean.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beans;
    }

    /**
     * 设置该activity被引导过了。 将类名已 |a|b|c这种形式保存为value，因为偏好中只能保存键值对
     *
     * @param className
     */
    public static void setIsGuided(Context context, final String className) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        if (className == null || "".equalsIgnoreCase(className))
            return;
        String classNames = spf.getString(KEY_GUIDE_ACTIVITY, "");
        StringBuilder sb = new StringBuilder(classNames).append("|").append(
                className);// 添加值
        spf.edit().putString(KEY_GUIDE_ACTIVITY, sb.toString()).commit();// 保存修改后的值
    }

    /**
     * 用户活跃数统计使用
     */
    // 保存退出时用户信息
    public static void saveUserIdAndEmobTd(Context context, int userId, String emobId) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(PREF_LOGOUT_KEY_EMOBID, emobId);
        edit.putInt(PREF_LOGOUT_KEY_USER_ID, userId);
        edit.commit();
    }

    public static int getlogoutUserId(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        return spf.getInt(PREF_LOGOUT_KEY_USER_ID, -1);
    }

    public static String getlogoutEmobId(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        return spf.getString(PREF_LOGOUT_KEY_EMOBID, "");
    }

    /**
     * 人品值百分比,人品值，生活圈数记录``
     */
    public static void saveRp(Context context, String rp, String persent, String count) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(PREF_RP_PERCENT, persent);
        edit.putString(PREF_RP_VALUE, rp);
        edit.putString(PREF_LIFECIRCLE_COUNT, count);
        edit.commit();
    }


    public static void saveRp(Context context, String rp, String persent) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(PREF_RP_PERCENT, persent);
        edit.putString(PREF_RP_VALUE, rp);
        edit.commit();
    }

    public static void clearRp(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(PREF_RP_PERCENT, "0");
        edit.putString(PREF_RP_VALUE, "0");
        edit.putString(PREF_LIFECIRCLE_COUNT, "0");
        edit.putString(PREF_BANGBANGQUAN_COUNT, "0");
        edit.putString(PREF_BANGBANGBI_COUNT, "0");
        edit.commit();
    }

    public static String getRpValue(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        return spf.getString(PREF_RP_VALUE, "0");
    }

    public static String getRpPercent(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        return spf.getString(PREF_RP_PERCENT, "0");
    }

    public static void saveBangBangQuanCount(Context context, String count) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(PREF_BANGBANGQUAN_COUNT, count);
        edit.commit();
    }

    public static String getPrefBangbangquanCount(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        return spf.getString(PREF_BANGBANGQUAN_COUNT, "0");
    }

    public static void saveBangbangbiCount(Context context, String count) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(PREF_BANGBANGBI_COUNT, count);
        edit.commit();
    }

    public static String getPrefBangbangbiCount(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        return spf.getString(PREF_BANGBANGBI_COUNT, "0");
    }

    public static void saveExchange(Context context, String count) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(PREF_BANGBANGBI_EXCHANGE, count);
        edit.commit();
    }

    public static String getPrefBangbangbiExchange(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        return spf.getString(PREF_BANGBANGBI_EXCHANGE, "0");
    }

    public static void saveLifeCircleCount(Context context, String count) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(PREF_LIFECIRCLE_COUNT, count);
        edit.commit();
    }

    public static String getPrefLifeCircleCount(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        return spf.getString(PREF_LIFECIRCLE_COUNT, "0");
    }

    public static void saveLifeCircleCountTime(Context context, String time) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(LIFECIRCLE_COUNT_TIME, time);
        edit.commit();
    }

    public static String getPrefLifeCircleCountTime(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        return spf.getString(LIFECIRCLE_COUNT_TIME, "0");
    }

    public static void saveShowBonuscoin(Context context, String showBonuscoin) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(SHOWBONUSCOIN, showBonuscoin);
        edit.commit();
    }

    public static String getShowBonuscoin(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        return spf.getString(SHOWBONUSCOIN, "no");
    }

    //items是否被点击
    public static void saveClickState(Context context, boolean isClick) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_CLICKSTATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putBoolean(CLICK_STATE, isClick);
        edit.commit();
    }

    public static boolean getClickState(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_CLICKSTATE, Context.MODE_PRIVATE);
        return spf.getBoolean(CLICK_STATE, false);
    }


    public static void saveCrazySalesCount(Context context, int count) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putInt("CrazySalesCount", count);
        edit.commit();
    }

    public static int getCrazySalesCount(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        return spf.getInt("CrazySalesCount", 0);
    }

    public static void saveFastshopNewCount(Context context, int count) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putInt("FastshopNewCount", count);
        edit.commit();
    }

    public static int getFastshopNewCount(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        return spf.getInt("FastshopNewCount", 0);
    }

    public static void saveGeniusCount(Context context, int count) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putInt("GeniusCount", count);
        edit.commit();
    }

    public static int getGeniusCount(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        return spf.getInt("GeniusCount", 0);
    }

    public static void saveDeliverLimit(Context context, int deliverLimit) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putInt("deliverLimit", deliverLimit);
        edit.commit();
    }

    public static int getDeliverLimit(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        return spf.getInt("deliverLimit", 0);
    }

    public static void saveDeliverTime(Context context, String deliverTime) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString("deliverTime", deliverTime);
        edit.commit();
    }

    public static String getDeliverTime(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        return spf.getString("deliverTime", "");
    }

    public static void saveLongitude(Context context, String deliverTime) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(PREF_KEY_longitude, deliverTime);
        edit.commit();
    }

    public static String getLongitude(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        return spf.getString(PREF_KEY_longitude, "");
    }

    public static void saveLatitude(Context context, String deliverTime) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(PREF_KEY_latitude, deliverTime);
        edit.commit();
    }

    public static String getLatitude(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        return spf.getString(PREF_KEY_latitude, "");
    }

    public static void saveRPValue(Context context, String rpValue) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString("rpvalue", rpValue);
        edit.commit();
    }

    public static String getRPValue(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        return spf.getString("rpvalue", "0");
    }

    public static void saveUserType(Context context, String userType) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString("userType", userType);
        edit.commit();
    }

    public static String getUserType(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        return spf.getString("userType", "normal");
    }

    public static long getLastReqRefreshHomeTopImgTime(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        return spf.getLong("LastReqRefreshHomeTopImgTime", 0);
    }


    public static void setLastReqRefreshHomeTopImgTime(Context context, long lastReqtime) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putLong("LastReqRefreshHomeTopImgTime", lastReqtime);
        edit.commit();
    }


    public static int getHomeLastPullReqTime(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        return spf.getInt("getHomeLastPullReqTime", 0);
    }

    public static void setHomeLastPullReqTime(Context context, int lastReqtime) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putInt("getHomeLastPullReqTime", lastReqtime);
        edit.commit();
    }


    /**
     * 保存福利抢购成功消息数
     *
     * @param context
     * @param unreadMsgsCount
     */
    public static void saveWelfareSuccessMsgCount(Context context, int unreadMsgsCount) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putInt("WelfareSuccessMsgCount", unreadMsgsCount);
        edit.commit();
    }

    /**
     * 获取福利消息数
     *
     * @param context
     * @return
     */
    public static int getWelfareSuccessMsgCount(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        return spf.getInt("WelfareSuccessMsgCount", 0);
    }

    /**
     * 获取是否需要发送首次进入群组消息
     * key = groupId+emobid
     *
     * @param context
     */
    public static boolean getIsNeedSendFirstEnterGroupMsg(Context context, String key) {

        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        return spf.getBoolean(key, true);
    }

    /**
     * 设置是否需要发送首次进入群组消息
     *
     * @param context
     * @param key     key = groupId+emobid
     * @param isNeed
     */
    public static void setIsNeedSendFirstEnterGroupMsg(Context context, String key, Boolean isNeed) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putBoolean(key, isNeed);
        edit.commit();

    }

    /**
     * 获取上次请求群组申请消息时间 如果=0 则时间设置当前时间
     *
     * @param context
     * @return
     */
    public static int getLastReqGroupInfosTime(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        return spf.getInt("getLastReqGroupInfosTime", 0);
    }

    /**
     * 设置上次请求群组申请处理消息时间,
     *
     * @param context
     * @param lastReqtime
     */
    public static void setLastReqGroupInfosTime(Context context, int lastReqtime) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putInt("getLastReqGroupInfosTime", lastReqtime);
        edit.commit();
    }


    /**
     * 获取当前用户是否是水军
     * test >0 是水军
     *
     * @param context
     * @return
     */
    public static int getUserInfoTest(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        return spf.getInt("getUserInfoTest", 0);//// 默认用户状态是正常用户
    }

    /**
     * 存储用户的当前是否是水军状态
     *
     * @param context
     * @param usertest
     */
    public static void setUserInfoTest(Context context, int usertest) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putInt("getUserInfoTest", usertest);
        edit.commit();
    }


    /**
     * 查看用户信息时获取到的用户水军信息
     *
     * @param context
     * @param userEmobid
     * @return
     */
    public static int getGroupUserInfoTest(Context context, String userEmobid) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        if (spf.contains(userEmobid)) {
            return spf.getInt("groupuserinfo_" + userEmobid, 0);//// 默认用户状态是正常用户
        } else {
            return 0;
        }
    }

    /**
     * 存储群组用户信息时进行存储当前emobid 的水军信息。
     *
     * @param context
     * @param userEmobid
     * @param emobTest
     */
    public static void setGroupUserInfoTest(Context context, String userEmobid, int emobTest) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putInt("groupuserinfo_" + userEmobid, emobTest);
        edit.commit();
    }

        /**保存分享内容
         *
         * @param context
         * @param s
         */

    public static void saveDoorPasteShareContent(Context context, String s) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_DOORPASTE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString("DoorPasteShareContent", s);
        edit.commit();
    }

    /**
     * 注册验证码
     *
     * @param context
     * @param data
     */
    public static void setUserAuthCode(Context context, String data) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString("regist_authcode", data);
        edit.commit();
    }


    /**
     * 获取注册验证码，注册提交信息使用
     *
     * @param context
     * @return
     */
    public static String getUserAuthCode(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_ACTIVE, Context.MODE_PRIVATE);
        return spf.getString("regist_authcode", "");
    }


    /**
     * 从缓存中获取应用版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        String appVersionName = spf.getString("app_versionName", "");
        if (!TextUtils.isEmpty(appVersionName)) {
            return appVersionName;
        } else {
            return setVersionName(context);
        }
    }


    /**
     * 获取最新版本编号保存到sp文件中
     *
     * @param context
     * @return
     */
    public static String setVersionName(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        String appVersionName = UserUtils.getVersion(context);
        edit.putString("app_versionName", appVersionName);
        edit.commit();
        return appVersionName;
    }

    /**
     * 保存用户的地址信息
     *
     * @param context
     * @param item_floors
     * @param item_unit
     * @param item_room
     */
    public static void saveUserAddress(Context context, String item_floors, String item_unit, String item_room) {
        SharedPreferences spf = context.getSharedPreferences(SHAREDPREFERENCES_USERLOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString("userFloor;", item_floors);
        edit.putString("userUnit;", item_unit);
        edit.putString("room", item_room);
        edit.commit();
    }

    /**
     * 获取用户的地址信息-楼层
     *
     * @param context
     */
    public static String getUserAddress_floor(Context context) {
        SharedPreferences spf = context.getSharedPreferences(SHAREDPREFERENCES_USERLOGIN, Context.MODE_PRIVATE);
        return spf.getString("userFloor", "");
    }

    /**
     * 获取用户的地址信息-单元
     *
     * @param context
     */
    public static String getUserAddress_unit(Context context) {
        SharedPreferences spf = context.getSharedPreferences(SHAREDPREFERENCES_USERLOGIN, Context.MODE_PRIVATE);
        return spf.getString("userUnit", "");
    }

    /**
     * 获取用户的地址信息-房间号
     *
     * @param context
     */
    public static String getUserAddress_room(Context context) {
        SharedPreferences spf = context.getSharedPreferences(SHAREDPREFERENCES_USERLOGIN, Context.MODE_PRIVATE);
        return spf.getString("room", "");
    }


    public static String getDoorPasteShareContent(Context context) {
        final SharedPreferences spf = context.getSharedPreferences(
                SHAREDPREFERENCES_DOORPASTE, Context.MODE_PRIVATE);
        return spf.getString("DoorPasteShareContent", "小区有5个房被贴，共计被贴32次");
    }
}
