package xj.property.utils.other;

/**
 * Created by Administrator on 2015/3/13.
 */
public class Config {


    public static final String BANGBANG_TAG = "&bang#bang@";//post 、 put请求 签名的key
    //// 是否显示Debug log
    public static boolean SHOW_lOG = Boolean.TRUE;

    public static final String BASE_IMAGE_CACHE = "XJONION";
    public static final String BASE_GROUP_CACHE = "XJGROUP";
    public static final String IMG_IRL = "";
    public static final String INTENT_PARMAS1 = "OnionParmas1";
    public static final String INTENT_PARMAS2 = "OnionParmas2";
    public static final String INTENT_BACKMAIN = "OnionBackMain";
    public static final String INTENT_IMAGECOUNT = "imagecount";


    //   public static final String NET_BASE="http://114.215.105.202:8080";
    public static final String NET_AGENT = "http://stats.ixiaojian.com";
//

//   public static final String NET_BASE="http://192.168.1.60:8080/MyWebApp";
//   public static final String NET_BASE2="http://192.168.1.60:8080/MyWebApp";

//    public static final String NET_BASE = "http://bangbang.ixiaojian.com";
//    public static final String NET_BASE2 = "http://bangbang.ixiaojian.com";

    /// 测试服务器

    public static final String NET_BASE = "http://114.215.153.192:8089";
    public static final String NET_BASE2 = "http://114.215.153.192:8089";


    public static final String NET_SHAREBASE = "http://www.linjubangbang.com";

    //// 帮帮券说明
    public static final String NET_BASE3 = "http://114.215.114.56:8080";



    //    public static final String NET_BASE2 = "http://120.24.232.121";

//    public static final String NET_BASE = "http://114.215.153.192:8089";
//    public static final String NET_BASE2 = "http://114.215.153.192:8089";

    //// 用户信息统计使用BaseURL
    public static final String NET_BASE_STATISTICS = "http://statistics.ixiaojian.com";

//    public static final String NET_BASE = "http://192.168.1.62:8080";
//    public static final String NET_BASE2 = "http://192.168.1.62:8080";

//    public static final String NET_BASE = "http://120.24.232.121";

    //正式服务器baseurl
//    public static final String NET_BASE="http://bangbang.ixiaojian.com";
//    public static final String NET_BASE2="http://bangbang.ixiaojian.com";



    //// 七牛上传资源定位baseurl
    public static final String QINIU_BASE_URL= "http://7d9lcl.com2.z0.glb.qiniucdn.com/";

    //环信拓展消息 extkey
    public static final String EXPKey_avatar = "avatar";//头像

    public static final String EXPKey_username = "username";//手机号

    public static final String EXPKey_communityId = "communityId";//小区ID

    public static final String EXPKey_gender = "gender";//性别

    public static final String EXPKey_testFrom  = "testFrom";//发送者的test

    public static final String EXPKey_testTo  = "testTo";//接受者的test

    public static final String EXPKey_groupName = "groupName";//// 群组的名字


    public static final String EXPKey_communityName = "communityName";//小区名字
    public static final String EXPKey_room = "room";//房间号
    public static final String EXPKey_userunit = "userUnit";//单元号
    public static final String EXPKey_userFloor = "userFloor";//楼层
    public static final String EXPKey_module = "module";//module

    public static final String EXPKey_nickname = "nickname";//昵称

    public static final String EXPKey_msgstatus = "msgstatus"; //  消息状态 ,InviteMesageStatus

    public static final String EXPKey_declinereason = "declinereason"; //   ,拒绝理由

    public static final String EXPKey_ishandle = "ishandle"; // 消息是否处理

    public static final String EXPKey_clickable = "clickable";//按钮状态

    public static final String EXPKey_isShowAvatar = "isShowAvatar";//头像状态

    public static final String EXPKey_serial = "serial";//订单号

    public static final String EXPKey_msgId = "msgId";

    public static final String EXPKey_CMD_CODE = "CMD_CODE";//环信消息拓展的code。用于区分订单消息的状态

    public static final String EXPKey_CMD_DETAIL = "CMD_DETAIL";

    public static final String EXPKey_CMD_DATA = "data";

    public static final String EXPKey_shareBonuscoinCount = "shareBonuscoinCount";

    public static final String EXPKey_bonuscoinCount = "bonuscoinCount";

    public static final String EXPKey_totalPrice = "totalPrice";

    public static final String EXPKey_orginPrice = "orginPrice";

    public static final String EXPKey_SORT = "shopType";//联系人类别，在联系人列表页进行区分显示

    public static final String EXPKey_BUYER = "buyer";//快店订单购买人的key
    public static final String EXPKey_ADDRESS = "address";
    public static final String EXPKey_BONUS = "bonus";
    public static final String EXPKey_GROUP = "group";
    public static final String EXPKey_GROUP_DESTORY = "group_destory";
    public static final String EXPKey_PayMethod = "payMethod";
    public static final String EXPKey_Star = "star";
    public static final String EXPKey_REASON = "reason";
    public static final String EXPKey_VERSION = "version";

    public static final String EXPKey_EVA = "eva";


    //normal , bangzhu, fubangzhu ,zhanglao,bangzhong

    public static final String USER_TYPE_BANGZHU = "bangzhu";
    public static final String USER_TYPE_NORMAL = "normal";
    public static final String USER_TYPE_FUBANGZHU = "fubangzhu";
    public static final String USER_TYPE_ZHANGLAO = "zhanglao";
    public static final String USER_TYPE_BANGZHONG = "bangzhong";

    public static final String USER_TYPE_TANGZHU = USER_TYPE_ZHANGLAO;


    /// 物业
    public static final String SERVANT_TYPE_WUYE = "wuye";
    /// 店家端
    public static final String FeedBackOrComplain_SHOP = "shop";
    /// 帮帮反馈
    public static final String SERVANT_TYPE_BANGBANG = "bangbang";
    //// 服务类型.
    public static final String SERVANT_TYPE = "servanttype";
    /// 维修投诉
    public static final String SERVANT_TYPE_WEIXIUTOUSU = "repair";
    /// 快店投诉
    public static final String SERVANT_TYPE_SHOPTOUSU = "shop"; /// v3 2016/03/17

    public static final String UPDATE_USERINFO = "update_userinfo";
    public static final String LoginUserName = "loginName";
    public static final String LoginUserNickName = "nickName";
    public static final String LoginUserPwd = "password";
    public static final String XJKEY_SHOP = "xjshop";
    public static final String PAYBODY = "xjpaybody";
    public static final String ComplainContent = "complain";
    public static final String InServiceTime = "inServiceTime";
    public static final String IsLoginOtherApp = "inServiceTime";
    public static final String Emobid = "emobid";
    public static final String SearchName = "searchName";

    public static final int HeaderClcikEvent = -99;
    public static final int SelectAblum = 617;
    public static final int TouristLogin = 798;
    public static final int TouristGETEMOBID = 598;
    public static final int TouristLoginError = 648;
    public static final int LoginUserSuccess = 998;
    public static final int LoginUserComplete = 967;
    public static final int LoginUserFailure = 997;
    public static final int LoginUserUPDATENATIVE = 996;
    public static final int LogoutTourist = 944;
    public static final int ADDBLACKLIST = 943;
    public static final int REMOVEBLACKLIST = 942;
    public static final int TASKCOMPLETE = 79;
    public static final int TASKERROR = 49;
    public static final int SINGLETASKCOMPLETE = 59;

    public static final int NETERROR = 100;
    public static final int NOTHING = 101;

    public static final String PHONETYPE = "Xiaomi";

    //appid
    //请同时修改  androidmanifest.xml里面，.PayActivityd里的属性<data android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
    public static final String APP_ID = "wx6333375cc8afdae7";

    public static final String WEIXINAPPSCRET = "32af9fbceb1416f7ed0ec5a64979ea08";



    public static final String QQAPPID = "1104822795";

    public static final String QQKEY = "04TNHIrySWHR2Jx9";

//    public static final String QQAPPID = "1104879006";
//
//    public static final String QQKEY = "xdBs1p4srrKGMr4u";

    //商户号
    public static final String MCH_ID = "1234787001";

    //  API密钥，在商户平台设置
    public static final String API_KEY = "x4i2a8o1j0i1a8n0techhcetnaijoaix";

    public static final String IMAGE_CACHE_PATH = "XJGROUP/imageloader/Cache";


}
