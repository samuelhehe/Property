package xj.property.fragment;

import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.XjApplication;
import xj.property.activity.ActivityCommonWebViewPager;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.activity.LifeCircle.FriendZoneIndexActivity;
import xj.property.activity.activities.ActivitiesActivity;
import xj.property.activity.activities.HomeIndexSearchResultActivity;
import xj.property.activity.bangzhu.ActivityBangZhuElection;
import xj.property.activity.call.CourierActivity;
import xj.property.activity.call.EmergencyNumberActivity;
import xj.property.activity.call.SendWaterActivity;
import xj.property.activity.contactphone.FastShopIndexActivity;
import xj.property.activity.cooperation.CooperationIndexActivity;
import xj.property.activity.doorpaste.DoorPasteIndexActivity;
import xj.property.activity.fitmentfinish.FitmentFinishActivity;
import xj.property.activity.genius.GeniusRegisterLoginActivity;
import xj.property.activity.genius.GeniusRelationActivity;
import xj.property.activity.genius.GeniusSpecialActivity;
import xj.property.activity.invite.ActivityInviteNeighborsHome;
import xj.property.activity.membership.ActivityMSPCardList;
import xj.property.activity.property.PropertyActivity;
import xj.property.activity.repair.RepairListActivity;
import xj.property.activity.user.NotifyListActivity;
import xj.property.activity.user.UserBonusActivity;
import xj.property.activity.vote.VoteIndexActivity;
import xj.property.activity.welfare.ActivityWelfareIndex;
import xj.property.adapter.GridViewAdapter;
import xj.property.beans.IndexBean;
import xj.property.beans.IndexMenuRespBean;
import xj.property.beans.LifeCircleCountBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.cache.XJNotify;
import xj.property.event.NewNotifyEvent;
import xj.property.event.NewPushEvent;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.statistic.EventServiceUtils;
import xj.property.utils.CommonUtils;
import xj.property.utils.DensityUtil;
import xj.property.utils.ToastUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.AdminUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.FastShopCarDBUtil;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;
import xj.property.widget.MyScrollView_20151111;


public class IndexFragment extends BaseFragment implements OnItemClickListener, MyScrollView_20151111.ScrollViewListener {

    private static final String TAG = "IndexFragment";
    /**
     * 刷新首页项
     */
    private static final int REFRESH_INDEX = 2;
    /**
     * 强制刷新 模块内容
     */
    private static final int REFRESH_INDEX_FORCE = 5;
    /// 刷新模块内容
    private static final int FAST_REFRESH_MENU_IDNEX = 1;
    ////快速刷新界面所有模块的消息状态
    private static final int REFRESH_MENU_INDEX_MSG_COUNT = 3;
    /// 请求网络红点数更新
    private static final int REFRESH_MENU_INDEX_MSG_NET = 4;
    /// 刷新首页顶部背景图
    private static final int REFRESH_INDEX_TOP_IMG_BG = 6;
    /// 显示首页顶部白条
    public static final int SHOW_LINES_VISIABLE = 7;


    /// 活动
    public static final int HOME_ITEM_FLAG_ACTIVITYS = 1;
    /// 客服
    public static final int HOME_ITEM_FLAG_CUSTOMRS = 7;
    /// 快递
    public static final int HOME_ITEM_FLAG_COURIER = 8;
    /// 快店
    public static final int HOME_ITEM_FLAG_FASTSHOP = 3;
    /// 维修
    public static final int HOME_ITEM_FLAG_REPAIR = 9;
    /// 保洁
    public static final int HOME_ITEM_FLAG_REPAIRER = 11;
    /// 紧急号码
    public static final int HOME_ITEM_FLAG_EMERGENCY_NUM = 10;
    /// 生活圈
    public static final int HOME_ITEM_FLAG_FRIENDZONE = 16;
    /// 送水
    public static final int HOME_ITEM_FLAG_SENDWATER = 6;
    /// 福利
    public static final int HOME_ITEM_FLAG_WELFARE_INDEX = 19;
    /// 会员卡
    public static final int HOME_ITEM_FLAG_MSPCARDLIST = 20;
    /// 邻居帮
    public static final int HOME_ITEM_FLAG_COOPERATION = 21;
    /// 邀请邻居
    public static final int HOME_ITEM_FLAG_INVITENEGIGHBOR = 22;
    /// 投票
    public static final int HOME_ITEM_FLAG_VOTE = 23;
    /// 物业缴费
    public static final int HOME_ITEM_FLAG_PROPERTY = 24;
    /// 装修
    public static final int HOME_ITEM_FLAG_FITMENT = 25;
    /// 牛人
    public static final int HOME_ITEM_FLAG_GENIUS = 28;

    /// 门贴
    public static final int HOME_ITEM_FLAG_DOORPASTE = 30;


    /// 首页AppVersionId配置项
    //TODO appversionId ++ ;
//    private static final String HOME_CURRENT_APP_VERSION = "8";
    private static final String HOME_CURRENT_APP_VERSION = "3.0.0";

    public static final String MODEL_RELATION_STATUS_NORMAL = "normal";
    public static final String MODEL_RELATION_STATUS_UNNORMAL = "unnormal";
    public static final String HOME_ITEM_MODEL_TYPE_BANGBANG = "bangbang";
    public static final String HOME_ITEM_MODEL_TYPE_URL = "url";
    public static final int MAX_TRY_TIMES = 3;

    /**
     * 默认与上边的距离
     */
//    private static final int MARGINTOP_DEFAULT_DP = 165;
    private static final int MARGINTOP_DEFAULT_DP = 134;


    private List<IndexBean> beans = new ArrayList<>();

    private GridView gv_main;

    private GridViewAdapter adapter;

    private MyScrollView_20151111 svRoot;

    private LinearLayout ll_notify;

    private TextView tv_pay;

    public TextView tv_more;

    public static boolean newMsgIsRead;

    private TextView tvNotify, tvNotifyCount, tvContent, tvNotifyTime;//通知及详情

    private View line;
    private int differHigh;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                /// 快速软刷新
                case FAST_REFRESH_MENU_IDNEX:
                    //// 从cache中初始化首页模块
                    initMenuIndexCache();
                    break;

                case REFRESH_INDEX_FORCE:
                    /// 强制刷新
                    getIndexMenu(true);

                    break;
                case REFRESH_INDEX:

                    /// 首页菜单项
                    getIndexMenu(false);

                    getLifeCircleCount(mHandler);
                    //// 刷新所有模块红点
//                    refreshAllModelMsg();

                    ///  刷新首页其他模块
                    refreshUI();

                    break;

                case REFRESH_MENU_INDEX_MSG_COUNT:

                    Log.d(TAG, "REFRESH_MENU_INDEX_MSG_COUNT  " + REFRESH_MENU_INDEX_MSG_COUNT);
                    /// 刷新界面所有红点
                    refreshAllModelMsg();
                    ///  刷新首页其他模块
                    refreshUI();
                    break;

                case REFRESH_MENU_INDEX_MSG_NET:
                    Log.d(TAG, "REFRESH_MENU_INDEX_MSG_NET  " + REFRESH_MENU_INDEX_MSG_NET);
                    getMenuIndexMsgCount(mHandler);
                    break;

                case REFRESH_INDEX_TOP_IMG_BG:

//                    refreshMenuTopImgBg();

                    break;
                case SHOW_LINES_VISIABLE:
                    if (line != null) {
                        line.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    };


    LinearLayout.LayoutParams params;

    private XjApplication app;

    private ImageView im_top;

    private boolean isOpen;
    private boolean isOpenDown;
    private boolean isOpenTouch = false;

    private boolean haveLifeCircle = true,
            haveFastShop = true,
            haveActive = true,
            haveWelfare = true,
            haveShopVipCard = true,
            haveCooperation = true,
            haveGenius = true,
            haveDoorPaste = true,
            haveInvite = true,
            haveVote = true,
            havePayfees = true;


    /// 索引为了获取在首页中的位置, 以及更新单个模块的最新信息
    /// 活动索引
    private int activeIndex = -1;
    ///生活圈索引
    private int circleIndex = -1;
    /// 快店索引
    private int fastshopIndex = -1;
    /// 福利索引
    private int welfareIndex = -1;
    //// 首页位置
    private int shopVipcardIndex = -1;
    /// 邻居帮位置
    private int cooperationIndex = -1;
    /// 邀请邻居
    private int inviteIndex = -1;
    /// 投票
    private int voteIndex = -1;
    /// 物业缴费
    private int payFeesIndex = -1;
    /// 小区牛人
    private int geniusIndex = -1;
    //// 门贴
    private int doorpasteIndex = -1;


    int ActivitiesRequestCode = 998; /// 活动请求码

    int CircleRequestCode = 989;  /// 生活圈请求码

    private static final int VOTE_REQUEST_CODE = 100; /// 投票


    /// 所有模块显示的array
    private static String[] icons = {"home_item_activity", "home_item_payment",
            "home_item_store", "home_item_fruit", "home_item_outfood", "home_item_water",
            "home_item_servicer", "home_item_courier", "home_item_repair",
            "home_item_phonenumber", "home_item_lifecircle", "home_item_fuli",
            "home_item_shopvipcard", "home_item_neighbor_help", "home_item_vote",
            "home_item_invite", "home_item_pay_fees",
            "home_item_niuren",
            "home_item_sticker",
            "zhuangxiu_icon"};//"home_item_clearer"
    //TODO
    /// 关闭图标array
    private static String[] icons_off = {"home_item_activity_off", "home_item_payment_off",
            "home_item_store_off", "home_item_fruit_off", "home_item_outfood_off", "home_item_water_off",
            "home_item_servicer_off", "home_item_courier_off", "home_item_repair_off",
            "home_item_phonenumber_off", "home_item_lifecircle_off"};//"home_item_clearer"


    private TextView home_index_community_name_tv;/// 小区名字

    /**
     * 当前EventBus 处理事件 消息码
     */
    private int current_notify_cmd_code;

    /// 最新通知消息
    private XJNotify notify;

    //// 首页模块从Server端拉取为空的次数, 超过三次 ,取消
    private int emptyTimes = 0;
    private String indexfragmentUUID;
    private LinearLayout translation_llay;
    private int translationllayWidth;
    private int tvNotifyMeasuredWidth;
    private float scrollX2YRatio; // 横向滑动与竖向滑动的比率
    private int scrollvertialY;  /// 纵向可滑动的距离
    private int cantranslationwidth;
    private LinearLayout translation_inner_llay;
    private RelativeLayout home_index_nofity_bar_rlay;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * 刷新通知公告
     */
    // This method will be called when a MessageEvent is posted
    public void onEvent(NewNotifyEvent event) {
        if (event.isNew) {//收到新消息，标识未读
            newMsgIsRead = false;
        }
        initNotify();
    }

//                    1、帮主投票里，有人给我投票了     XXX对你投了一票-->election-->CMD_CODE 141
//
//                    2、我发起的投票有人投了         XXX参与了你的投票-->vote-->CMD_CODE 142
//
//                    3、有人给我发起的投票评论了     XXX对你的投票发表了评论-->comment-->CMD_CODE 143
//
//                    4、有人回复了我对某投票的评论。   XXX回复了你的投票评论 -->reply-->CMD_CODE 144

    /**
     * 以事件驱动刷新模块,单模块刷新
     *
     * @param event
     */
    public void onEvent(NewPushEvent event) {
        switch (event.cmdCode) {
            case 110:
                refreshActivityMsg();
                break;
            case 121:
            case 122:
                refreshLifeCircleMsg();
                break;
            case 131:
            case 132:
                /// 刷新邻居帮消息
                refreshCooperationMsg();
                break;
            case 141:
            case 142:
            case 143:
            case 144:
                /// 刷新投票,帮主信息
                refreshVoteMsg();
                break;
            default:
                refreshAllModelMsg();
                break;
        }

        if (mHandler != null) {
            mHandler.sendEmptyMessage(REFRESH_MENU_INDEX_MSG_NET);
        } else {
            Log.i(TAG, "onEvent(NewPushEvent event) mHandler is null ");
        }
    }

    /**
     * 最新通知消息处理
     */
    private void initNotify() {
        /// 获取最新通知消息
        notify = selectNewNotify();
        /// 消息标题
        tvNotify.setText(notify.title + "");
        /// 消息码
        current_notify_cmd_code = notify.CMD_CODE;
        switch (notify.CMD_CODE) {

            case 100:
                tv_pay.setVisibility(View.GONE);
                break;

            case 106:
                tv_pay.setVisibility(View.VISIBLE);
                tv_pay.setText("查看帮帮券");

            case 107:
                tv_pay.setVisibility(View.VISIBLE);
                tv_pay.setText("查看详情");

            default:
                break;
        }

//     int width=   getResources().getDimensionPixelOffset(R.dimen.notify_content_width);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String strs[] = notify.content.split("\n");
                int width = tvContent.getWidth();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < strs.length && i < 3; i++) {
                    if (strs[i].length() > 0) {
                        String s = strs[i];
                        if (i == 0) {
                            s = "\t" + s;
                        }
                        strs[i] = StrUtils.str2SingleNotify(s, tvContent.getPaint(), width);
                        stringBuilder.append(strs[i]);
                    }
                }
                tvContent.setText(StrUtils.convert2DBC(stringBuilder.toString()));

//2015/12/18                line.setVisibility(View.VISIBLE);

                tv_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        switch (current_notify_cmd_code) {
                            case 106:
                                if (PreferencesUtil.getLogin(getActivity()))
                                    startActivity(new Intent(getActivity(), UserBonusActivity.class));
                                break;
                            case 107:
                                if (PreferencesUtil.getLogin(getActivity())) {
                                    startActivity(new Intent(getActivity(), ActivityBangZhuElection.class));
                                } else {
                                    startActivity(new Intent(getActivity(), RegisterLoginActivity.class));
                                }
                                break;
                        }
                    }
                });
            }
        }, 50);
//        tvContent.setText(notify.content);
        tvNotifyTime.setText(StrUtils.getDate4Second(notify.timestamp));
        int count = selectNewNotifyCount();
        if (count != 0) {
            tvNotifyCount.setVisibility(View.VISIBLE);
            tvNotifyCount.setText("" + selectNewNotifyCount());
            tv_more.setText("未读" + count + "条");
        } else {
            newMsgIsRead = "yes".equals(notify.read_status);
            tvNotifyCount.setVisibility(View.GONE);
            tv_more.setText("更多通知");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        isaddMargin = false;
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            if (mHandler != null) {
                mHandler.sendEmptyMessageDelayed(REFRESH_MENU_INDEX_MSG_NET, 200);
//        if (mHandler != null)
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    svRoot.fullScroll(ScrollView.FOCUS_DOWN);
//                }
//            }, 500);

//                initNotify();
            }

            Log.d(TAG, "isVisibleToUser " + isVisibleToUser);


        }

    }

    private int selectNewNotifyCount() {
        return new Select().from(XJNotify.class).where("emobid = ? and read_status = ?", PreferencesUtil.getLogin(getActivity()) ? PreferencesUtil.getLoginInfo(getActivity()).getEmobId() : "-1", "no").execute().size();
    }

    //获取最新通知
    private XJNotify selectNewNotify() {

        XJNotify notify = new Select().from(XJNotify.class).where("emobid = ?", PreferencesUtil.getLogin(getActivity()) ? PreferencesUtil.getLoginInfo(getActivity()).getEmobId() : "-1").orderBy("timestamp DESC").executeSingle();
        /// 从db中查询消息
        if (notify == null) {
            /// 默认消息

//            return new XJNotify("", 100, "社区通知栏", " 注册成为帮帮用户，即可实时查看物业最新通知。停水停电、快递查收、小区保修…时刻掌握小区动态。有帮帮，没麻烦！", (int) (new Date().getTime() / 1000), true, "yes");

            String notice_txt = "我们生活在这样的一个圈子，分享同一个社区的不同精彩; 朋友离得太远，邻居近在身边，组局不再三缺一，遇到烦恼时仗义相助； 用情、用心、用日子，与邻居一起享受生活的快乐。";

            return new XJNotify("", 100, "和邻居们在一起", StrUtils.convert2DBC(notice_txt), (int) (System.currentTimeMillis() / 1000), true, "yes");

//            和邻居们在一起
//            我们生活在这样的一个圈子，分享同一个社区的不同精彩；朋友离得太远，邻居近在身边，组局不再三缺一，遇到烦恼时仗义相助；用情、用心、用日子，与邻居一起享受生活的快乐。


        }
        return notify;
    }

    int margindefault;
    boolean isaddMargin = false;
    int lastTime;
    float downY = 0;
    private UserInfoDetailBean bean;

    EventServiceUtils eventServiceUtils;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        eventServiceUtils = new EventServiceUtils(activity);
        indexfragmentUUID = eventServiceUtils.generateUUID();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
//        getLifeCircleCount(mHandler);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /// 上边margin 165
        margindefault = DensityUtil.dip2px(getActivity(), MARGINTOP_DEFAULT_DP);

        emptyTimes = 0;
        bean = PreferencesUtil.getLoginInfo(getActivity());

        //向上拖动，通知栏隐藏，scrollview的详情显示，

        View inflatedView = inflater.inflate(R.layout.fragment_index, container, false);
        initView(inflatedView);
        inflatedView.post(domeasureLayout);
        return inflatedView;
    }


    private Runnable domeasureLayout = new Runnable() {
        @Override
        public void run() {
            translationllayWidth = translation_llay.getMeasuredWidth();
            tvNotifyMeasuredWidth = translation_inner_llay.getMeasuredWidth();
            computeTranslationRatio(translationllayWidth, tvNotifyMeasuredWidth);
        }
    };

    private void initData() {
        /// 初始化模块缓存
        initMenuIndexCache();
    }

    /**
     * 初始化缓存的同时,
     * 请求最新Model是否有更新
     * 如果有刷新缓存,更新界面model
     * 如果没有更新,return;
     */
    private void initMenuIndexCache() {

        List<IndexBean> list = PreferencesUtil.getIndexBean(getActivity());
        boolean isRefresh = PreferencesUtil.isRefreshIndexCache(getActivity());
        if (list != null && list.size() > 0 && !isRefresh) {
            ////重置模块缓存
            reSetModelCache(list);
            //// 延迟3秒内随机秒惰刷新
            mHandler.sendEmptyMessageDelayed(REFRESH_INDEX, new Random(5).nextInt(2) * 1000);
            Log.d("initMenuIndexCache ", "sendEmptyMessageDelayed REFRESH_INDEX");
        } else {
            /// 强制刷新
            Log.d("initMenuIndexCache ", "initMenuIndexCache REFRESH_INDEX_FORCE");
            mHandler.sendEmptyMessage(REFRESH_INDEX_FORCE);
        }
    }

    /**
     * 初始化view
     *
     * @param inflatedView
     */
    private void initView(View inflatedView) {
        app = (XjApplication) getActivity().getApplication();
        svRoot = (MyScrollView_20151111) inflatedView.findViewById(R.id.sv_root);
        svRoot.setScrollViewListener(this);

        gv_main = (GridView) inflatedView.findViewById(R.id.gv_main);
        gv_main.setVisibility(View.VISIBLE);

        translation_llay = (LinearLayout) inflatedView.findViewById(R.id.translation_llay);

        translation_inner_llay = (LinearLayout) inflatedView.findViewById(R.id.translation_inner_llay);

        tvNotify = (TextView) inflatedView.findViewById(R.id.tv_notify);

        ll_notify = (LinearLayout) inflatedView.findViewById(R.id.ll_notify);
        tvContent = (TextView) inflatedView.findViewById(R.id.tv_notify_content);
        tv_more = (TextView) inflatedView.findViewById(R.id.tv_notify_more);
        tvNotifyTime = (TextView) inflatedView.findViewById(R.id.tv_notify_time);

        tv_pay = (TextView) inflatedView.findViewById(R.id.tv_notify_pay);

        tvNotifyCount = (TextView) inflatedView.findViewById(R.id.tv_newnotify_count);

        inflatedView.findViewById(R.id.invite_neighbor_home_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PreferencesUtil.getLogin(getActivity())) {
                    Intent intentPush = new Intent();
                    intentPush.setClass(getActivity(), ActivityInviteNeighborsHome.class);
                    intentPush.putExtra("inviteType", "home");
                    startActivity(intentPush);

                } else {
                    Intent intent = new Intent(getActivity(), RegisterLoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        line = inflatedView.findViewById(R.id.line);
        line.setVisibility(View.GONE);

        home_index_nofity_bar_rlay = (RelativeLayout) inflatedView.findViewById(R.id.home_index_nofity_bar_rlay);

        im_top = (ImageView) inflatedView.findViewById(R.id.im_top);

        home_index_community_name_tv = (TextView) inflatedView.findViewById(R.id.home_index_community_name_tv);

//        final TextView query = (TextView) inflatedView.findViewById(R.id.query);

        final ImageButton search_clear = (ImageButton) inflatedView.findViewById(R.id.search_clear);

        final ImageButton search_bar = (ImageButton) inflatedView.findViewById(R.id.search_bar);
        search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferencesUtil.getLogin(getActivity())) {
                    Intent intent = new Intent();
                    //intent.putExtra("searchName",query.getText()+"");
//                    intent.setClass(getActivity(), IndexSearchResultActivity.class);
                    intent.setClass(getActivity(), HomeIndexSearchResultActivity.class);
                    startActivity(intent);

                } else {

                    Intent intent = new Intent(getActivity(), RegisterLoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NotifyListActivity.class));
            }
        });

        svRoot.setOnTouchListener(new View.OnTouchListener() {

            private int lastY = 0;
            private int touchEventId = -9983761;

            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    View scroller = (View) msg.obj;
                    svRoot.topHeifght = margindefault;
                    if (msg.what == touchEventId) {
                        if (lastY == scroller.getScrollY()) {
                            //停止了

                            if (!isOpen) { //colseing

                                isaddMargin = true;

//                                if (lastY > margindefault / 3 * 2) {
//                                    svRoot.fullScroll(ScrollView.FOCUS_UP);//关闭  滚动到顶部
//                                    isOpen = false;
//                                } else {
//                                    svRoot.fullScroll(ScrollView.FOCUS_UP);//打开
//                                    isOpen = true;
//                                }
                                if (lastY > margindefault / 3 * 2) {
//                                    svRoot.fullScroll(ScrollView.FOCUS_DOWN);//关闭
                                    if (isOpen) {
                                        svRoot.isHeight = true;
                                        svRoot.topHeifght = margindefault;
                                        svRoot.smoothScrollTo(0, margindefault);
                                        isOpenDown = false;
                                    } else if (!svRoot.isHeight && lastY < (margindefault + margindefault / 2 + 20)) {
                                        svRoot.smoothScrollTo(0, margindefault);
                                        isOpenDown = false;
                                    } else {
                                        isOpenDown = true;
                                    }
                                    if (lastY > (margindefault + margindefault / 2 + 20)) {
                                        svRoot.fullScroll(ScrollView.FOCUS_DOWN);
                                    }
                                    isOpen = false;
                                } else {
                                    svRoot.smoothScrollTo(0, -margindefault);
//                                    svRoot.fullScroll(ScrollView.FOCUS_UP);//打开
                                    isOpen = true;
                                }

                            } else {//opening
                                isaddMargin = true;
                                if (lastY < margindefault / 3) {
                                    svRoot.smoothScrollTo(0, -margindefault);
//                                    svRoot.fullScroll(ScrollView.FOCUS_UP);//打开
                                    isOpen = true;
                                } else {
                                    if (isOpen) {
                                        svRoot.isHeight = true;
                                        svRoot.topHeifght = margindefault;
                                        svRoot.smoothScrollTo(0, margindefault);
                                        isOpenDown = false;
                                    } else if (!svRoot.isHeight && lastY < (margindefault + margindefault / 2 + 20)) {
                                        svRoot.smoothScrollTo(0, margindefault);
                                        isOpenDown = false;
                                    } else {
                                        isOpenDown = true;
                                    }
                                    if (lastY > (margindefault + margindefault / 2 + 20)) {
                                        svRoot.fullScroll(ScrollView.FOCUS_DOWN);
                                    }
                                    isOpen = false;
                                }
                            }
                        } else {
                            handler.sendMessage(handler.obtainMessage(touchEventId, scroller));
                            lastY = scroller.getScrollY();
                        }
                    }
                }
            };


            @Override
            public boolean onTouch(View v, MotionEvent event) {

//                if (event.getAction() == MotionEvent.ACTION_MOVE && !newMsgIsRead) {
//                    newMsgIsRead = true;
//                    XJNotify notify = selectNewNotify();
//                    notify.isReaded = true;
//                    notify.save();
//                    EventBus.getDefault().post(new NewNotifyEvent(notify, true));
//                }


                if (isOpen) {
                    svRoot.isHeight = true;
                    svRoot.topHeifght = margindefault;
                } else {
                    svRoot.isHeight = false;
                    svRoot.topHeifght = margindefault;
                }

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.sendMessage(handler.obtainMessage(touchEventId, v));
                    svRoot.isHeight = false;
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    downY = event.getY();
                    int scrollY = v.getScrollY();
                    if (v.getScrollY() == 2 * margindefault / 3) {
//                        eventService(0 + "", "通知公告");
//                        eventServiceUtils.postClickEvent(indexfragmentUUID,"0");
                        eventServiceUtils.postScheduleClickEvent(indexfragmentUUID, "0");
                    }

                    if (isOpenDown && scrollY < (margindefault + margindefault / 2 + 20) && scrollY > margindefault) {
                        svRoot.smoothScrollTo(0, margindefault);
                        isOpenTouch = true;
                    }
                    if (!isOpen && scrollY >= margindefault / 3) {
//                        isOpen = true;
////                        if (!isaddMargin) {
//                            params = (LinearLayout.LayoutParams) line.getLayoutParams();
//                            params.setMargins(0, margindefault, 0, 0);
//                            line.setLayoutParams(params);
//                            isaddMargin = true;
//
////                        }

                    } else {
//
//            isOpen = false;
                        // eventService(0 + "","通知公告");`
                    }

//                    if(!svRoot.isHeight && lastY < (margindefault + margindefault / 2 + 20) && lastY>margindefault) {
//                        handler.sendMessage(handler.obtainMessage(touchEventId, v));
//                    }


                    if (!newMsgIsRead || notify.read_status.equals("no")) {
                        newMsgIsRead = true;
                        XJNotify notify = selectNewNotify();
                        notify.read_status = "yes";
                        notify.save();
                        EventBus.getDefault().post(new NewNotifyEvent(notify, true));
                    }


                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    handler.sendMessage(handler.obtainMessage(touchEventId, v));
//                    handler.sendMessageDelayed(handler.obtainMessage(touchEventId, v));
                    isOpenTouch = false;
                    int time = (int) (System.currentTimeMillis() / 1000);
//                    boolean isDown=event.getY()>=downY;
                    if (time - lastTime > 7 && v.getScrollY() < margindefault / 3 * 2) {
                        lastTime = time;
//                        eventService(0 + "", "通知公告");
                        ////合上的时候.
//                        eventServiceUtils.postClickEvent(indexfragmentUUID,"0");

                    }


//                    Log.i("onion","ScrollY"+v.getScrollY());
//                    Log.i("onion","downY"+downY);
//                    Log.i("onion","upY"+event.getY());
//                    if(isDown){
//                        svRoot.fullScroll(ScrollView.FOCUS_DOWN);
//                    }else {
//                        svRoot.scrollTo(0,0);
//                    }

                }


                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (isOpen) {
//                        eventService(0 + "", "通知公告");
                        eventServiceUtils.postScheduleClickEvent(indexfragmentUUID, "0");

                    }
                }
                if (!isaddMargin) {
                    params = (LinearLayout.LayoutParams) line.getLayoutParams();
                    params.setMargins(0, margindefault, 0, 0);
                    line.setLayoutParams(params);
                    isaddMargin = true;

                }
                if (isOpenTouch) {
                    return isOpenTouch;
                } else {
                    if (v.getId() == R.id.gv_main)
                        return gv_main.dispatchTouchEvent(event);
                    else {
                        return ll_notify.dispatchTouchEvent(event);
                    }
                }
            }
        });

        home_index_nofity_bar_rlay.setOnClickListener(onClickListener);
        im_top.setOnClickListener(onClickListener);

    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            params = (LinearLayout.LayoutParams) line.getLayoutParams();
            if (!isOpen) {//打开
                if (!isaddMargin) {
                    params.setMargins(0, margindefault, 0, 0);
                    line.setLayoutParams(params);
                    isaddMargin = true;
                }

                XJNotify notify = selectNewNotify();
                notify.read_status = "yes";
                notify.save();

                EventBus.getDefault().post(new NewNotifyEvent(notify, true));
                svRoot.isHeight = true;
                svRoot.topHeifght = margindefault;
                svRoot.smoothScrollTo(0, margindefault);
                isOpenDown = false;

//                doTranslation2Right(translation_inner_llay);
            } else {//关闭
                svRoot.scrollTo(0, 0);

//                doTranslation2Left(translation_inner_llay);

                //采集
//                    eventService(0 + "", "通知公告");
                //// 关闭采集事件  1/13
//                    eventServiceUtils.postClickEvent(indexfragmentUUID,"0");

            }
            isOpen = !isOpen;
        }
    };


    /**
     * 刷新UI 上需要刷新的,从SP中获取数据更新
     */
    private void refreshUI() {
        if (getActivity() != null) {
            String cname = PreferencesUtil.getCommityName(getActivity());
            Log.i("debbug", "refreshUI" + cname);
            if (home_index_community_name_tv != null) {
                if (!TextUtils.isEmpty(cname)) {
                    home_index_community_name_tv.setText(cname + "小区");
                } else {
                    home_index_community_name_tv.setText("帮帮小区");
                }
            }
        }
    }


    /**
     * 刷新首页模块的排序
     * TODO
     *
     * @param beans
     */
    private void reSetModelIndex(List<IndexBean> beans) {
        if (beans == null || beans.size() <= 0) {
            return;
        }
        haveLifeCircle = false;
        haveFastShop = false;
        haveActive = false;
        haveWelfare = false;

        haveShopVipCard = false;

        haveCooperation = false;

        haveInvite = false;
        haveVote = false;

        havePayfees = false;

        haveGenius = false;

        haveDoorPaste = false;

        for (int i = 0; i < beans.size(); i++) {

            if (beans.get(i) != null) {
                switch (beans.get(i).getServiceId()) {

                    case HOME_ITEM_FLAG_ACTIVITYS:
                        activeIndex = i;
                        haveActive = true;
                        break;
                    case HOME_ITEM_FLAG_FRIENDZONE:
                        circleIndex = i;
                        haveLifeCircle = true;
                        break;
                    case HOME_ITEM_FLAG_FASTSHOP:
                        fastshopIndex = i;
                        haveFastShop = true;
                        break;

                    case HOME_ITEM_FLAG_WELFARE_INDEX:
                        welfareIndex = i;
                        haveWelfare = true;
                        break;

                    case HOME_ITEM_FLAG_MSPCARDLIST:
                        shopVipcardIndex = i;
                        haveShopVipCard = true;
                        break;

                    case HOME_ITEM_FLAG_COOPERATION:
                        cooperationIndex = i;
                        haveCooperation = true;
                        break;

                    case HOME_ITEM_FLAG_INVITENEGIGHBOR:
                        inviteIndex = i;
                        haveInvite = true;
                        break;

                    case HOME_ITEM_FLAG_VOTE:
                        voteIndex = i;
                        haveVote = true;
                        break;

                    case HOME_ITEM_FLAG_PROPERTY:
                        payFeesIndex = i;
                        havePayfees = true;
                        break;

                    case HOME_ITEM_FLAG_GENIUS:
                        geniusIndex = i;
                        haveGenius = true;
                        break;

                    case HOME_ITEM_FLAG_DOORPASTE:
                        doorpasteIndex = i;
                        haveDoorPaste = true;
                        break;

//                    /// 缴费
//                    public static final int HOME_ITEM_FLAG_PAYLIST = 2;
//                    /// 客服
//                    public static final int HOME_ITEM_FLAG_CUSTOMRS = 7;
//                    /// 快递
//                    public static final int HOME_ITEM_FLAG_COURIER = 8;
//                    /// 维修
//                    public static final int HOME_ITEM_FLAG_REPAIR = 9;
//                    /// 保洁
//                    public static final int HOME_ITEM_FLAG_REPAIRER = 11;
//                    /// 紧急号码
//                    public static final int HOME_ITEM_FLAG_EMERGENCY_NUM = 10;
//                    /// 送水
//                    public static final int HOME_ITEM_FLAG_SENDWATER = 6;
//                    /// 装修
//                    public static final int HOME_ITEM_FLAG_FITMENT = 25;


                    /// TODO
                }
            }
        }
        Log.i("debbug", "haveActive=" + haveActive
                + "  haveLifeCircle=" + haveLifeCircle
                + "   haveFastShop" + haveFastShop
                + " haveCooperation " + haveCooperation);
    }

//    public void refresh() {
//        boolean isRfresh = PreferencesUtil.isRefreshIndexCache(getActivity());
//        if(isRfresh){
//            getIndexMenu(true);
//        }
//    }


    interface IndexService {

        //        /api/v1/communities/{communityId}/home/services?appVersionId={appVersionId}&time={time}
//        @GET("/api/v1/communities/{communityId}/services") 2012/12/2
//        @GET("/api/v2/communities/{communityId}/home/services")
//        void getIndex(@Path("communityId") int communityId, @QueryMap HashMap<String, String> map, Callback<IndexMenuRespBean> cb);

        @GET("/api/v3/communities/{communityId}/services")
        void getIndexV3(@Path("communityId") int communityId, @QueryMap HashMap<String, String> map, Callback<CommonRespBean<List<IndexBean>>> cb);


//        @GET("/api/v2/communities/{communityId}/home/homeBackgroundImg")
//        void getHomeMenuTopImg(@Path("communityId") int communityId, Callback<HomeMenuTopRespBean> cb);
//
//        @GET("/api/v2/communities/{communityId}/home/homeBackgroundImg")

        @GET("/api/v3/home/background")
        void getHomeMenuTopImg(@QueryMap HashMap<String, String> hashMap, Callback<CommonRespBean<String>> cb);

    }

    @Override
    public void onScrollChanged(View v, int x, int y, int oldx, int oldy) {
//        Log.i("onScrollChanged", "  x=" + x
//                        + "  y=" + y
//                        + "  oldx=" + oldx
//                        + "  oldy=" + oldy);
////        scrollX2YRatio = 0.5f;
//        Log.i("onScrollChanged", "x  ratio " + scrollX2YRatio + " translationX " + scrollX2YRatio * y);
        float needTranslationX = scrollX2YRatio * y;
        /// scroll to left
        if (y < scrollvertialY) {
            doTranslation(translation_inner_llay, cantranslationwidth - needTranslationX);
            /// left point
        } else if (y > scrollvertialY) {
            doTranslation(translation_inner_llay, 0);
        }

        initNotify();
//        Log.i("onScrollChanged", "x  ratio "+scrollX2YRatio+" translationX " + scrollX2YRatio * y);
//        float needTranslationX =  scrollX2YRatio * y;
//        /// scroll to left
//        if (y < scrollvertialY) {
//            doTranslation(translation_inner_llay, 0 - needTranslationX);
//            /// left point
//        } else if (y >= scrollvertialY) {
//            doTranslation(translation_inner_llay, -cantranslationwidth);
//        }
    }


    /**
     * 计算横向可滑动距离与竖向可滑动距离的比率
     *
     * @param translationllayWidth
     * @param tvNotifyMeasuredWidth
     */
    private void computeTranslationRatio(int translationllayWidth, int tvNotifyMeasuredWidth) {
        ///可以滑动的横向距离
        cantranslationwidth = (translationllayWidth - tvNotifyMeasuredWidth) / 2;
        scrollvertialY = xj.property.utils.other.DensityUtil.dip2px(getActivity(), MARGINTOP_DEFAULT_DP);
        scrollX2YRatio = (float) cantranslationwidth / (float) scrollvertialY;
//        Log.i("onScrollChanged", "computeTranslationRatio cantranslationwidth " + cantranslationwidth +
//                " translationllayWidth " + translationllayWidth + " tvNotifyMeasuredWidth " + tvNotifyMeasuredWidth +
//                " scrollvertialY " + scrollvertialY + " scrollX2YRatio " + scrollX2YRatio);
    }

    private void doTranslation2Left(LinearLayout translation_inner_llay) {
        scrollvertialY = 0;
        ObjectAnimator animator = ObjectAnimator.ofFloat(translation_inner_llay, "translationX", 0);
        animator.setDuration(500);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    private void doTranslation2Right(LinearLayout translation_inner_llay) {
        scrollvertialY = xj.property.utils.other.DensityUtil.dip2px(getActivity(), MARGINTOP_DEFAULT_DP);
        ObjectAnimator animator = ObjectAnimator.ofFloat(translation_inner_llay, "translationX", cantranslationwidth);
        animator.setDuration(500);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }


    /**
     * 从当前偏移量到移动到指定的偏移量
     *
     * @param translation_inner_llay
     * @param goTranslationX
     */
    private void doTranslation(LinearLayout translation_inner_llay, float goTranslationX) {
//        ObjectAnimator animator = ObjectAnimator.ofFloat(translation_inner_llay, "translationX", goTranslationX);
//        animator.setDuration(1);
//        animator.start();

        doTransValueanimator(translation_inner_llay, goTranslationX);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void doTransValueanimator(final LinearLayout translation_inner_llay, final float goTranslationX) {

        final FloatEvaluator floatEvaluator = new FloatEvaluator();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(translation_inner_llay.getTranslationX(), goTranslationX);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
//                Log.d("onScrollChanged ", " doTransValueanimator onAnimationUpdate " + animatedValue);
                float animatedFraction = animation.getAnimatedFraction();
                Float evaluate = floatEvaluator.evaluate(animatedFraction, translation_inner_llay.getTranslationX(), animatedValue);
                translation_inner_llay.setTranslationX(evaluate);
                translation_inner_llay.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(100);
        valueAnimator.start();
    }


//    public void refresh() {
//        boolean isRfresh = PreferencesUtil.isRefreshIndexCache(getActivity());
//        if(isRfresh){
//            getIndexMenu(true);
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
        initNotify();
    }

    public void refreshIndexMenu() {
        //// 强制刷新首页模块
        Log.d(TAG, "force refresh indexmenu  from main ... ");
        boolean isRfresh = PreferencesUtil.isRefreshIndexCache(getActivity());
        if (isRfresh) {
            getIndexMenu(true);
        }

    }

    /**
     * 获取首页菜单项
     *
     * @param refreshIndexMenuCacheForce
     */
    private void getIndexMenu(boolean refreshIndexMenuCacheForce) {
        /// MainActivity onresume中调用防止为空
        if (getActivity() != null) {
            List<IndexBean> list = PreferencesUtil.getIndexBean(getActivity());
            if (list != null && list.size() > 0) {
                reSetModelCache(list);
                Log.d(TAG, "isNetWorkConnected  status is   using cache ...   ");
            } else {
                Log.d(TAG, "isNetWorkConnected  status is   cache is null  ");
            }
        }

        if (!CommonUtils.isNetWorkConnected(getActivity())) {
            Log.d(TAG, " getIndexMenu isNetWorkConnected  status is no  ");
            return;
        }
        HashMap<String, String> option = new HashMap<>();
        String versionName = UserUtils.getVersion(getActivity());
        if (TextUtils.isEmpty(versionName)) {
            option.put("appVersion", HOME_CURRENT_APP_VERSION);
        } else {
            option.put("appVersion", UserUtils.getVersion(getActivity()));
        }
        if (refreshIndexMenuCacheForce) {
            option.put("time", "0");/// 强制刷新所有menuIndex
            Log.d("cityId", "getCommityId =============" + PreferencesUtil.getCommityId(getActivity()));
        } else {
            option.put("time", "" + PreferencesUtil.getIndexTime(getActivity()));
        }

        IndexService service = RetrofitFactory.getInstance().create(getActivity(), option, IndexService.class);
        Callback<CommonRespBean<List<IndexBean>>> callback = new Callback<CommonRespBean<List<IndexBean>>>() {
            @Override
            public void success(CommonRespBean<List<IndexBean>> indexMenuResult, Response response) {
                if (indexMenuResult != null && "yes".equals(indexMenuResult.getStatus())) {
                    PreferencesUtil.setRefreshIndexCache(getActivity(), false);
                    List<IndexBean> loadbeans = indexMenuResult.getData();
                    Integer maxTime = indexMenuResult.getField("maxTime", Integer.class);
                    //Log.d("index", "=========================================size=" + loadbeans.size());
                    if (loadbeans == null || loadbeans.isEmpty()) {
                        Log.d("index", "=====================================data no change ");
                        return;
                    }
                    icons_off = new String[]{"home_item_activity_off", "home_item_payment_off",
                            "home_item_store_off", "home_item_fruit_off", "home_item_outfood_off", "home_item_water_off",
                            "home_item_servicer_off", "home_item_courier_off", "home_item_repair_off",
                            "home_item_phonenumber_off", "home_item_lifecircle_off"};
                    /**
                     *  再次加载GridViewAdapter
                     */
                    if (loadbeans.size() > 12) {
                        int num = 3 - loadbeans.size() % 3;
                        if (num != 3) {
                            for (int i = 0; i < num; i++) {
                                loadbeans.add(new IndexBean(i * (-1), MODEL_RELATION_STATUS_NORMAL, "", "empty", HOME_ITEM_MODEL_TYPE_BANGBANG, ""));
                            }
                        }
                    } else if (loadbeans.size() < 12) {
                        //TODO   去掉重复
                        for (int i = 0; i < icons_off.length; i++) {
                            if (i < loadbeans.size()) {
                                for (int j = 0; j < loadbeans.size(); j++) {
                                    if (icons[i].equals(loadbeans.get(j).getImgName())) {
                                        icons_off[i] = null;
                                    }
                                }
                            } else {
                                break;
                            }
                        }
                        int beansSize = loadbeans.size();
                        for (int i = 0; i < 12 - beansSize; i++) {
                            int j = 0;
                            do {
                                if (icons_off[j] != null) {
                                    loadbeans.add(new IndexBean(i * (-1), MODEL_RELATION_STATUS_NORMAL, "", icons_off[j], HOME_ITEM_MODEL_TYPE_BANGBANG, ""));
                                    icons_off[j] = null;
                                    break;
                                }
                            } while (icons_off[j++] == null);
                            icons_off[j] = null;
                        }
                    }
                    reSetModelCache(loadbeans);

                    PreferencesUtil.saveIndexTime(getActivity(), maxTime);
                    /// 刷新缓存数据
                    PreferencesUtil.saveIndexBean(getActivity(), loadbeans);
                    //// 请求模块数为空的请求次数
                    emptyTimes = 0;
                    if (mHandler != null) {
                        mHandler.sendEmptyMessage(REFRESH_MENU_INDEX_MSG_COUNT);
                    }
                } else if (indexMenuResult != null && "no".equals(indexMenuResult.getStatus())) {
//                    initMenuIndexCache();

                    List<IndexBean> list = PreferencesUtil.getIndexBean(getActivity());
                    if (list != null && list.size() > 0) {
                        reSetModelCache(list);
                    } else {
                        if (emptyTimes < MAX_TRY_TIMES) {
                            ///  强制刷新所有menuIndex
                            getIndexMenu(true);
                            emptyTimes++;
                            Log.d(TAG, "getIndexMenu try  has " + emptyTimes + " times ");
                        } else {
                            Log.d(TAG, "getIndexMenu try  " + MAX_TRY_TIMES + " times abandon");
                        }
                    }
                    Log.d(TAG, "getIndexMenu status is no  ");
                } else {
                    ToastUtils.showToast(getActivity(), "服务器异常");
                }
//                long lastReqTime = PreferencesUtil.getLastReqRefreshHomeTopImgTime(getActivity());
//                if(TimeUtils.isSameDayOfMillis(lastReqTime,System.currentTimeMillis())){
//                    Log.d(TAG, "REFRESH_INDEX_TOP_IMG_BG  isSameDayOfMillis  ");
//                    return ;
//                }else{

                if (mHandler != null) {
                    mHandler.sendEmptyMessage(REFRESH_INDEX_TOP_IMG_BG);
                }
//                }
            }

            @Override
            public void failure(RetrofitError error) {
                line.setVisibility(View.GONE);
                error.printStackTrace();
            }
        };
        service.getIndexV3(PreferencesUtil.getCommityId(getActivity()), option, callback);
    }

    /**
     * 重置模块缓存
     *
     * @param loadbeans
     */
    private void reSetModelCache(List<IndexBean> loadbeans) {
        if (loadbeans != null && loadbeans.size() > 0) {
            if (beans != null) {
                beans.clear();
                beans.addAll(loadbeans);
                //// 刷新首页模块索引
                reSetModelIndex(loadbeans);
                adapter = new GridViewAdapter(getActivity(), beans, svRoot.getWidth() / 3 - DensityUtil.px2dip(getActivity(), 1),
                        svRoot.getHeight() / 4 - DensityUtil.px2dip(getActivity(), 1),
                        mHandler
                );
                /// todo 这两个参数应该没有用. ..
                app.setGrideWidth(svRoot.getWidth() / 3 - DensityUtil.px2dip(getActivity(), 1));
                app.setGrideHeight(svRoot.getHeight() / 4 - DensityUtil.px2dip(getActivity(), 4));
                /// todo 这两个参数应该没有用. ..

                gv_main.setAdapter(adapter);
                gv_main.setOnItemClickListener(this);
                adapter.notifyDataSetChanged();
            } else {
                Log.e("reSetModelCache ", "beans is null !!! ");
            }
        } else {
            Log.d("reSetModelCache ", "loadBeans is null ");
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getMenuIndexMsgCount(mHandler);
        refreshAllModelMsg();

//        if (requestCode == HOME_ITEM_FLAG_ACTIVITYS) {
//            /// 刷新活动信息
//            refreshAllModelMsg();
//        } else if (HOME_ITEM_FLAG_FRIENDZONE == requestCode) {
//            refreshAllModelMsg();
////            refreshLifeCircleMsg();
//        }else if(HOME_ITEM_FLAG_VOTE == requestCode){
//            refreshAllModelMsg();
//        }
    }

    //TODO
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

        IndexBean clickedIndexBean = beans.get(position);

        /// 自定义事件采集
//        eventService(clickedIndexBean.getServiceId() + "", clickedIndexBean.getServiceName());

        Intent intentPush = new Intent();
        if (TextUtils.equals(HOME_ITEM_MODEL_TYPE_BANGBANG, clickedIndexBean.getType())) {

            switch (clickedIndexBean.getServiceId()) {

                case HOME_ITEM_FLAG_ACTIVITYS:
                    intentPush.setClass(getActivity(), ActivitiesActivity.class);
                    startActivityForResult(intentPush, HOME_ITEM_FLAG_ACTIVITYS);
                    break;

                case HOME_ITEM_FLAG_CUSTOMRS:
                    if (PreferencesUtil.getLogin(getActivity())) {
//                        AdminUtils.askAdmin(getActivity(), "service", null, null);
                        AdminUtils.askAdminCallBack(getActivity(), Config.SERVANT_TYPE_WUYE, null);
                    } else {
                        Intent intent = new Intent(getActivity(), RegisterLoginActivity.class);
                        startActivity(intent);
                    }
                    break;
                case HOME_ITEM_FLAG_COURIER:
                    intentPush.setClass(getActivity(), CourierActivity.class);
                    intentPush.putExtra(Config.XJKEY_SHOP, 12);
                    intentPush.putExtra(Config.INTENT_PARMAS1, clickedIndexBean.getServiceName());
                    startActivity(intentPush);
                    break;

                case HOME_ITEM_FLAG_FASTSHOP:
//                intentPush.setClass(getActivity(), FastShopListActivity.class);
                    intentPush.setClass(getActivity(), FastShopIndexActivity.class);
                    startActivity(intentPush);
                    FastShopCarDBUtil.initData(getActivity());
//                fastshopNewCount = 0;
                    PreferencesUtil.saveFastshopNewCount(XjApplication.getInstance(), 0);
                    break;

                case HOME_ITEM_FLAG_REPAIR:
                    startActivity(new Intent(getActivity(), RepairListActivity.class));
                    break;

                case HOME_ITEM_FLAG_EMERGENCY_NUM:
                    intentPush.setClass(getActivity(), EmergencyNumberActivity.class);
                    intentPush.putExtra(Config.XJKEY_SHOP, 11);
                    intentPush.putExtra(Config.INTENT_PARMAS1, clickedIndexBean.getServiceName());
                    startActivity(intentPush);

                    break;

                case HOME_ITEM_FLAG_REPAIRER://保洁
                    startActivity(new Intent(getActivity(), RepairListActivity.class));
                    break;


                case HOME_ITEM_FLAG_SENDWATER:
                    intentPush.setClass(getActivity(), SendWaterActivity.class);
                    intentPush.putExtra(Config.XJKEY_SHOP, 4);
                    intentPush.putExtra(Config.INTENT_PARMAS1, clickedIndexBean.getServiceName());
                    startActivity(intentPush);
                    break;

                case HOME_ITEM_FLAG_FRIENDZONE:
                    intentPush.setClass(getActivity(), FriendZoneIndexActivity.class);
                    startActivityForResult(intentPush, HOME_ITEM_FLAG_FRIENDZONE);
                    break;

                case HOME_ITEM_FLAG_WELFARE_INDEX:
                    intentPush.setClass(getActivity(), ActivityWelfareIndex.class);
                    startActivity(intentPush);
                    PreferencesUtil.saveWelfareCount(XjApplication.getInstance(), 0);

                    break;
                /// TODO

                case HOME_ITEM_FLAG_MSPCARDLIST:

                    intentPush.setClass(getActivity(), ActivityMSPCardList.class);
                    startActivity(intentPush);
                    PreferencesUtil.saveShopVipCardCount(XjApplication.getInstance(), 0);
                    PreferencesUtil.saveIsUnReadVipCard(XjApplication.getInstance(), false);

                    break;

                case HOME_ITEM_FLAG_COOPERATION:

                    intentPush.setClass(getActivity(), CooperationIndexActivity.class);
                    startActivityForResult(intentPush, HOME_ITEM_FLAG_COOPERATION);
                    PreferencesUtil.saveCooperationIndexCount(XjApplication.getInstance(), 0);
                    break;

                case HOME_ITEM_FLAG_INVITENEGIGHBOR:

                    if (PreferencesUtil.getLogin(getActivity())) {
                        intentPush.setClass(getActivity(), ActivityInviteNeighborsHome.class);
                        intentPush.putExtra("inviteType", "home");
                        startActivity(intentPush);
                    } else {
                        Intent intent = new Intent(getActivity(), RegisterLoginActivity.class);
                        startActivity(intent);
                    }


//                PreferencesUtil.saveCooperationIndexCount(XjApplication.getInstance(), 0);
//                PreferencesUtil.saveIsUnReadCooperationIndex(XjApplication.getInstance(), false);

                    break;
                case HOME_ITEM_FLAG_VOTE:

                    /// home_item_vote
//                if (PreferencesUtil.getLogin(getActivity())) {

                    intentPush.setClass(getActivity(), VoteIndexActivity.class);
                    startActivityForResult(intentPush, HOME_ITEM_FLAG_VOTE);

                    PreferencesUtil.saveVoteIndexCount(XjApplication.getInstance(), 0);

//                } else {
//                    arg1.setClickable(false);
//                    Intent intent = new Intent(getActivity(), RegisterLoginActivity.class);
//                    startActivity(intent);
//                    arg1.setClickable(true);
//                }

                    break;
                case HOME_ITEM_FLAG_PROPERTY:

                    if (PreferencesUtil.getLogin(getActivity())) {

                        intentPush.setClass(getActivity(), PropertyActivity.class);
                        startActivity(intentPush);

//                    PreferencesUtil.saveVoteIndexCount(XjApplication.getInstance(), 0);
//                    PreferencesUtil.saveIsUnReadVoteIndex(XjApplication.getInstance(), false);

                    } else {
                        Intent intent = new Intent(getActivity(), RegisterLoginActivity.class);
                        startActivity(intent);
                    }

                    break;
                case HOME_ITEM_FLAG_FITMENT:
                    if (PreferencesUtil.getLogin(getActivity())) {
                        intentPush.setClass(getActivity(), FitmentFinishActivity.class);
                        startActivity(intentPush);
                    } else {
                        Intent intent = new Intent(getActivity(), RegisterLoginActivity.class);
                        startActivity(intent);
                    }
                    break;
                case HOME_ITEM_FLAG_GENIUS:
                    if (PreferencesUtil.getLogin(getActivity())) {
                        bean = PreferencesUtil.getLoginInfo(getActivity());
                        PreferencesUtil.saveGeniusCount(getActivity(), -1);
                        if ("famous".equals(bean.getIdentity())) {
                            intentPush.setClass(getActivity(), GeniusSpecialActivity.class);
                            startActivityForResult(intentPush, HOME_ITEM_FLAG_GENIUS);
                        } else {
                            intentPush.setClass(getActivity(), GeniusRelationActivity.class);
                            startActivityForResult(intentPush, HOME_ITEM_FLAG_GENIUS);
                        }
                    } else {
                        Intent intent = new Intent(getActivity(), GeniusRegisterLoginActivity.class);
                        startActivity(intent);
                    }
                    break;
                case HOME_ITEM_FLAG_DOORPASTE:
                    intentPush.setClass(getActivity(), DoorPasteIndexActivity.class);
                    startActivityForResult(intentPush, HOME_ITEM_FLAG_DOORPASTE);
                    PreferencesUtil.saveDoorPasteIndexCount(XjApplication.getInstance(), 0);
                    break;

//            case 9:
//                intentPush.setClass(getActivity(), TakeOutActivity.class);
//                startActivity(intentPush);
//                break;
//            case 11:
//                intentPush.setClass(getActivity(), MoreActivity.class);
//                startActivity(intentPush);
//                break;


            }

        } else if (TextUtils.equals(HOME_ITEM_MODEL_TYPE_URL, clickedIndexBean.getType())) {
            /// tODO 跳转至指定的WebView...
            intentPush.setClass(getActivity(), ActivityCommonWebViewPager.class);
            intentPush.putExtra("clickedIndexBean", clickedIndexBean);
            startActivity(intentPush);
        }

    }

    /**
     * 刷新活动红点消息
     */
    private void refreshActivityMsg() {

        if (gv_main == null || gv_main.getChildCount() < 1 || gv_main.getChildAt(activeIndex) == null)
            return;

        ////刷新活动红点
        TextView tv = (TextView) gv_main.getChildAt(activeIndex).findViewById(R.id.tv_num);
        int actcount = PreferencesUtil.getUnReadCount(getActivity());
        if (actcount > 0) {
            tv.setVisibility(View.VISIBLE);
            int count = PreferencesUtil.getUnReadCount(XjApplication.getInstance());
            if (count >= 100) {
                tv.setText("99");
            } else {
                tv.setText("" + actcount);
            }
        } else {
            tv.setVisibility(View.GONE);
        }

        if (!haveActive) {
            tv.setVisibility(View.GONE);
        }
    }

    /**
     * 刷新生活圈红点
     */
    private void refreshLifeCircleMsg() {
        if (gv_main == null || gv_main.getChildCount() < 1 || gv_main.getChildAt(circleIndex) == null)
            return;
        /// 刷新生活圈红点
        TextView tvLife = (TextView) gv_main.getChildAt(circleIndex).findViewById(R.id.tv_num);

        Log.i("debbug", "havaactive==" + circleIndex);

        int count = PreferencesUtil.getUnReadCircleCount(getActivity());
        if (count > 0) {
            tvLife.setVisibility(View.VISIBLE);
            if (count >= 100) {
                tvLife.setText("99");
            } else {
                tvLife.setText(count + "");
            }
        } else {
            tvLife.setVisibility(View.GONE);
        }
        if (!haveLifeCircle) {
            tvLife.setVisibility(View.GONE);
        }
    }

    /**
     * 刷新快店红点
     */
    private void refreshFastShopMsg() {

        if (gv_main == null || gv_main.getChildCount() < 1 || gv_main.getChildAt(fastshopIndex) == null)
            return;
        /// 刷新快店红点
        TextView tvFastshop = (TextView) gv_main.getChildAt(fastshopIndex).findViewById(R.id.tv_num);
        if (PreferencesUtil.getFastshopNewCount(XjApplication.getInstance()) > 0) {
            tvFastshop.setVisibility(View.VISIBLE);
            int count = PreferencesUtil.getFastshopNewCount(XjApplication.getInstance());
            if (count >= 100) {
                tvFastshop.setText("99");
            } else {
                tvFastshop.setText("" + count);
            }

        } else {
            tvFastshop.setVisibility(View.GONE);
        }
        if (!haveFastShop) {
            tvFastshop.setVisibility(View.GONE);
            PreferencesUtil.saveFastshopNewCount(XjApplication.getInstance(), 0);
        }
    }

    /**
     * 刷新福利红点
     */
    private void refreshWelfareMsg() {

        if (gv_main == null || gv_main.getChildCount() < 1 || gv_main.getChildAt(welfareIndex) == null)
            return;
        /// 福利红点
        if (welfareIndex != -1 && gv_main.getChildAt(welfareIndex) != null) {
            TextView tvWelfare = (TextView) gv_main.getChildAt(welfareIndex).findViewById(R.id.tv_num);
            if (PreferencesUtil.getWelfareCount(XjApplication.getInstance()) > 0) {
                tvWelfare.setVisibility(View.VISIBLE);
                tvWelfare.setText("新");
            } else {
                tvWelfare.setVisibility(View.GONE);
            }
            if (!haveWelfare) {
                tvWelfare.setVisibility(View.GONE);
                PreferencesUtil.saveWelfareCount(XjApplication.getInstance(), 0);
            }
        }
    }

    /**
     * 刷新会员卡红点
     */
    private void refreshVIPCardMsg() {

        if (gv_main == null || gv_main.getChildCount() < 1 || gv_main.getChildAt(shopVipcardIndex) == null)
            return;
        /// 刷新会员卡红点
        if (PreferencesUtil.getShopVipCardCount(XjApplication.getInstance()) > 0 && shopVipcardIndex != -1) {
            if (gv_main != null && gv_main.getChildAt(shopVipcardIndex) != null && shopVipcardIndex != -1) {

                TextView tvshopVipcard = (TextView) gv_main.getChildAt(shopVipcardIndex).findViewById(R.id.tv_num);
                if (PreferencesUtil.getShopVipCardCount(XjApplication.getInstance()) > 0) {
                    int count = PreferencesUtil.getShopVipCardCount(XjApplication.getInstance());
                    if (count >= 100) {
                        tvshopVipcard.setText("99");
                    } else {
                        tvshopVipcard.setText("" + count);
                    }
                    tvshopVipcard.setVisibility(View.VISIBLE);

                } else {
                    if (PreferencesUtil.isUnReadVipCard(getActivity())) {
                        tvshopVipcard.setText("新");
                        tvshopVipcard.setVisibility(View.VISIBLE);

                    } else {
                        tvshopVipcard.setVisibility(View.GONE);
                    }
                }

                if (!haveShopVipCard) {
                    tvshopVipcard.setVisibility(View.GONE);
                    PreferencesUtil.saveShopVipCardCount(XjApplication.getInstance(), 0);
                }
            }
        }


    }

    /**
     * 刷新邻居帮红点
     */
    private void refreshCooperationMsg() {

        Log.d(TAG, "refreshCooperationMsg  is executed ");

        if (gv_main == null || gv_main.getChildCount() < 1 || gv_main.getChildAt(cooperationIndex) == null)
            return;

        Log.d(TAG, "refreshCooperationMsg  is executed  " + PreferencesUtil.getCooperationIndexCount(XjApplication.getInstance()) + " cooperationIndex " + cooperationIndex);

        /// 刷新邻居帮红点
        if (PreferencesUtil.getCooperationIndexCount(XjApplication.getInstance()) >= 0 && cooperationIndex != -1) {
            if (gv_main != null && gv_main.getChildAt(cooperationIndex) != null) {

                TextView tvshopVipcard = (TextView) gv_main.getChildAt(cooperationIndex).findViewById(R.id.tv_num);
                if (PreferencesUtil.getCooperationIndexCount(XjApplication.getInstance()) > 0) {
                    int count = PreferencesUtil.getCooperationIndexCount(XjApplication.getInstance());
                    if (count >= 100) {
                        tvshopVipcard.setText("99");
                    } else {
                        tvshopVipcard.setText("" + count);
                    }
                    tvshopVipcard.setVisibility(View.VISIBLE);
                } else {
                    if (PreferencesUtil.isUnReadCooperationIndex(getActivity())) {
                        tvshopVipcard.setText("新");
                        tvshopVipcard.setVisibility(View.VISIBLE);
                    } else {
                        tvshopVipcard.setVisibility(View.GONE);
                    }
                }
                if (!haveCooperation) {
                    tvshopVipcard.setVisibility(View.GONE);
                    PreferencesUtil.saveCooperationIndexCount(XjApplication.getInstance(), 0);
                }
            }
        }
    }

    /**
     * 刷新小区牛人红点
     */
    private void refreshGeniusMsg() {

        Log.d(TAG, "refreshGeniusMsg  is executed ");

        if (gv_main == null || gv_main.getChildCount() < 1 || gv_main.getChildAt(geniusIndex) == null)
            return;

        Log.d(TAG, "refreshGeniusMsg  is executed  " + PreferencesUtil.getGeniusCount(XjApplication.getInstance()) + " geniusIndex " + geniusIndex);

        /// 刷新小区牛人红点
        if (gv_main != null && geniusIndex != -1 && gv_main.getChildAt(geniusIndex) != null) {

            TextView tvshopVipcard = (TextView) gv_main.getChildAt(geniusIndex).findViewById(R.id.tv_num);
            if (PreferencesUtil.getGeniusCount(XjApplication.getInstance()) > 0) {
                int count = PreferencesUtil.getGeniusCount(XjApplication.getInstance());
                if (count >= 100) {
                    tvshopVipcard.setText("99");
                } else {
                    tvshopVipcard.setText("" + count);
                }
                tvshopVipcard.setVisibility(View.VISIBLE);
            } else {
                if (PreferencesUtil.getGeniusCount(getActivity()) == 0) {
                    tvshopVipcard.setText("新");
                    tvshopVipcard.setVisibility(View.VISIBLE);
                } else {
                    tvshopVipcard.setVisibility(View.GONE);
                }
            }

            if (!haveGenius) {
                tvshopVipcard.setVisibility(View.GONE);
                PreferencesUtil.saveGeniusCount(XjApplication.getInstance(), -1);
            }
        }
    }

    /**
     * 刷新小区门贴
     */
    private void refreshDoorPaste() {

        Log.d(TAG, "refreshGeniusMsg  is executed ");

        if (gv_main == null || gv_main.getChildCount() < 1 || gv_main.getChildAt(doorpasteIndex) == null)
            return;

        Log.d(TAG, "refreshGeniusMsg  is executed  " + PreferencesUtil.getDoorPasteIndexCount(XjApplication.getInstance()) + " getDoorPasteIndexCount " + doorpasteIndex);

        /// 刷新小区门贴红点
        if (gv_main != null && doorpasteIndex != -1 && gv_main.getChildAt(doorpasteIndex) != null) {
            TextView tvshopVipcard = (TextView) gv_main.getChildAt(doorpasteIndex).findViewById(R.id.tv_num);
            if (PreferencesUtil.getDoorPasteIndexCount(XjApplication.getInstance()) > 0) {

                int count = PreferencesUtil.getDoorPasteIndexCount(XjApplication.getInstance());
                if (count >= 100) {
                    tvshopVipcard.setText("99");
                } else {
                    tvshopVipcard.setText("" + count);
                }
                tvshopVipcard.setVisibility(View.VISIBLE);
            } else {
                if (PreferencesUtil.isUnReadDoorPastenIndex(getActivity())) {
                    tvshopVipcard.setText("新");
                    tvshopVipcard.setVisibility(View.VISIBLE);
                } else {
                    tvshopVipcard.setVisibility(View.GONE);
                }
            }
            if (!haveDoorPaste) {
                tvshopVipcard.setVisibility(View.GONE);
                PreferencesUtil.saveDoorPasteIndexCount(XjApplication.getInstance(), -1);
            }
        }
    }

    /**
     * 刷新投票红点
     */
    private void refreshVoteMsg() {

        if (gv_main == null || gv_main.getChildCount() < 1 || gv_main.getChildAt(voteIndex) == null)
            return;
        //////刷新投票红点
        if (voteIndex != -1) {
            if (gv_main != null && gv_main.getChildAt(voteIndex) != null) {

                TextView tvshopVipcard = (TextView) gv_main.getChildAt(voteIndex).findViewById(R.id.tv_num);
                if (PreferencesUtil.getVoteIndexCount(XjApplication.getInstance()) > 0) {
                    int count = PreferencesUtil.getVoteIndexCount(XjApplication.getInstance());
                    if (count >= 100) {
                        tvshopVipcard.setText("99");
                    } else {
                        tvshopVipcard.setText("" + count);
                    }
                    tvshopVipcard.setVisibility(View.VISIBLE);

                } else {
                    if (PreferencesUtil.isUnReadVoteIndex(getActivity())) {
                        tvshopVipcard.setText("新");
                        tvshopVipcard.setVisibility(View.VISIBLE);

                    } else {
                        tvshopVipcard.setVisibility(View.GONE);
                    }
                }

                if (!haveVote) {
                    tvshopVipcard.setVisibility(View.GONE);
                    PreferencesUtil.saveVoteIndexCount(XjApplication.getInstance(), 0);
                }
            }

        }
    }


    /**
     * 刷新所有模块的红点部分
     * 可以进行调单独的方法进行局部刷新.
     * <p/>
     * TODO 添加模块红点刷新部分
     */
    private void refreshAllModelMsg() {
        boolean isRefreshIndexCache = PreferencesUtil.isRefreshIndexCache(getActivity());
        if (gv_main == null || gv_main.getChildCount() < 1 && !isRefreshIndexCache) return;

        ////刷新活动红点
        refreshActivityMsg();

        /// 刷新生活圈红点
        refreshLifeCircleMsg();

        /// 刷新快店红点
        refreshFastShopMsg();

        /// 福利红点
        refreshWelfareMsg();

        /// 刷新会员卡红点
        refreshVIPCardMsg();

        //// 刷新邻居帮消息
        refreshCooperationMsg();

        //// 刷新牛人消息
        refreshGeniusMsg();
        /// 刷新门贴
        refreshDoorPaste();
        //////刷新投票红点
        refreshVoteMsg();

        Log.d(TAG, "refreshAllModelMsg is complete ");
        /// todo 物业缴费角标处理

    }

//    //自定义事件采集
//    public void eventService(String serviceId, String serviceName) {
//        EventServiceStatistics.eventService(getActivity(), serviceId, serviceName, 2);
//    }

    /// MainActivity的消息刷新
    public void getLifeCircleCount(Handler handler) {
        getMenuIndexMsgCount(handler);
    }

    interface LifeCircleCountService {
        // /api/v1/communities/{communityId}/home/{emobId}/homeTips?appVersionId={appVersionId}&time={time}
//        @GET("/api/v1/communities/{communityId}/home/{emobId}/tips")  2015/12/2

//        @GET("/api/v2/communities/{communityId}/home/{emobId}/homeTips")
//        void getIndexSearchDataList(@Path("communityId") int communityId, @Path("emobId") String emobId, @QueryMap Map<String, String> map, Callback<LifeCircleCountBean> cb);
//        @GET("/api/v2/communities/{communityId}/home/{emobId}/homeTips")
//        /api/v3/home?communityId={小区ID}&emobId={用户环信ID}&time={UNIX时间戳}&appVersionId={APP版本}

        @GET("/api/v3/home")
        void getIndexSearchDataList(@QueryMap HashMap<String, String> map, Callback<CommonRespBean<LifeCircleCountBean>> cb);
    }

    private void getMenuIndexMsgCount(final Handler handler1) {

        String reqcountTime = PreferencesUtil.getPrefLifeCircleCountTime(XjApplication.getInstance());
        Log.i(TAG, "reqcountTime =" + reqcountTime);

        if ("0".equals(reqcountTime)) {
            int isfirstupdateTime = PreferencesUtil.isFirstUpdate(XjApplication.getInstance(), UserUtils.getVersion(XjApplication.getInstance()));

            Log.i(TAG, "isFirstUpdate time =" + isfirstupdateTime);
            if (isfirstupdateTime <= 0) {
                int currentTime = (int) (System.currentTimeMillis() / 1000);
                PreferencesUtil.saveLifeCircleCountTime(XjApplication.getInstance(), "" + currentTime);
            } else {
                PreferencesUtil.saveLifeCircleCountTime(XjApplication.getInstance(), "" + PreferencesUtil.isFirstUpdate(XjApplication.getInstance(), UserUtils.getVersion(XjApplication.getInstance())));
            }
            PreferencesUtil.saveUnReadCircleCount(XjApplication.getInstance(), 3);
            /**邻居帮1, 投票1, 活动/话题1, */
            PreferencesUtil.saveVoteIndexCount(XjApplication.getInstance(), 1);
            PreferencesUtil.saveCooperationIndexCount(XjApplication.getInstance(), 1);
            PreferencesUtil.saveUnReadCount(XjApplication.getInstance(), 1);

            if (mHandler != null) {
                mHandler.sendEmptyMessage(REFRESH_MENU_INDEX_MSG_COUNT);
            }
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("time", reqcountTime);
        map.put("appVersion", HOME_CURRENT_APP_VERSION);
        map.put("communityId", "" + PreferencesUtil.getCommityId(getActivity()));
        String emobid;
        if (PreferencesUtil.getLoginInfo(XjApplication.getInstance()) == null) {
            emobid = PreferencesUtil.getTourist(getActivity());
        } else {
            emobid = PreferencesUtil.getLoginInfo(XjApplication.getInstance()).getEmobId();
        }
        map.put("emobid", "" + emobid);
        PreferencesUtil.saveLifeCircleCountTime(XjApplication.getInstance(), "" + System.currentTimeMillis() / 1000);
//        communityId={小区ID}&emobId={用户环信ID}&time={UNIX时间戳}&appVersion={APP版本}

        LifeCircleCountService service = RetrofitFactory.getInstance().create(getActivity(), map, LifeCircleCountService.class);
        Callback<CommonRespBean<LifeCircleCountBean>> callback = new Callback<CommonRespBean<LifeCircleCountBean>>() {
            @Override
            public void success(CommonRespBean<LifeCircleCountBean> bean, Response response) {
                if (bean != null && "yes".equals(bean.getStatus())) {

//                    Log.d("getMenuIndexMsgCount  success ", "LifeCircleCountBean bean." + bean);

                    LifeCircleCountBean indexMsgCountInfo = bean.getData();

                    if (indexMsgCountInfo.getLifeCircle() > 0) {
                        int lifeCircleTotalcount = PreferencesUtil.getUnReadCircleCount(XjApplication.getInstance()) + indexMsgCountInfo.getLifeCircle();
//                        Log.d("getMenuIndexMsgCount  LifeCircleCountBean ", " lifeCircleCount." + indexMsgCountInfo.getLifeCircle() + "lifeCircleTotalcount" + lifeCircleTotalcount);
                        PreferencesUtil.saveUnReadCircleCount(XjApplication.getInstance(), lifeCircleTotalcount);
                    }

                    //// v3 2016/03/15 具体含义 该字段有待讨论
//                    if (indexMsgCountInfo.getCrazySalesCount() > 0) {
//
//                        int crazysalescount = PreferencesUtil.getCrazySalesCount(XjApplication.getInstance()) + indexMsgCountInfo.CrazySalesCount;
//
//                        Log.d("getMenuIndexMsgCount ", " getUnReadCircleCount." + indexMsgCountInfo.CrazySalesCount + "crazysalescount" + crazysalescount);
//                        PreferencesUtil.saveCrazySalesCount(XjApplication.getInstance(), crazysalescount);
//                    }

                    if (indexMsgCountInfo.welfare > 0) {
                        PreferencesUtil.saveWelfareCount(XjApplication.getInstance(), indexMsgCountInfo.welfare);
                    }

                    ////TODO 保存未读消息个数

                    if (indexMsgCountInfo.getNearbyVipcard() > 0) {
                        int vipcount = PreferencesUtil.getShopVipCardCount(XjApplication.getInstance()) + indexMsgCountInfo.getNearbyVipcard();
                        PreferencesUtil.saveShopVipCardCount(XjApplication.getInstance(), vipcount);
                    }
                    if (indexMsgCountInfo.getCooperation() > 0) {
                        int cooperationCount = PreferencesUtil.getCooperationIndexCount(XjApplication.getInstance()) + indexMsgCountInfo.getCooperation();
                        PreferencesUtil.saveCooperationIndexCount(XjApplication.getInstance(), cooperationCount);
                    }
                    /// 投票数量
                    if (indexMsgCountInfo.getVote() > 0) {
                        int voteIndexCount = PreferencesUtil.getVoteIndexCount(XjApplication.getInstance()) + indexMsgCountInfo.getVote();
                        PreferencesUtil.saveVoteIndexCount(XjApplication.getInstance(), voteIndexCount);
                    }
                    if (indexMsgCountInfo.getShop() > 0) {

                        int fastCount = PreferencesUtil.getFastshopNewCount(XjApplication.getInstance()) + indexMsgCountInfo.getShop();

                        PreferencesUtil.saveFastshopNewCount(XjApplication.getInstance(), fastCount);
                    }
                    //// 活动数量
                    if (indexMsgCountInfo.getActivity() > 0) {
                        int actCount = PreferencesUtil.getUnReadCount(XjApplication.getInstance()) + indexMsgCountInfo.getActivity();
                        PreferencesUtil.saveUnReadCount(XjApplication.getInstance(), actCount);
                    }
                    //// 门贴
                    if (indexMsgCountInfo.getDoor() > 0) {
                        int actCount = PreferencesUtil.getDoorPasteIndexCount(XjApplication.getInstance()) + indexMsgCountInfo.getDoor();
                        PreferencesUtil.saveDoorPasteIndexCount(XjApplication.getInstance(), actCount);
                    }
                    Log.i(TAG, "getMenuIndexMsgCount  refresh complete " + new Date());
                    Log.i("debbug", "getWelfareCount=" + PreferencesUtil.getWelfareCount(XjApplication.getInstance()));

                    Log.i("debbug", "getShopVipcard=" + PreferencesUtil.getShopVipCardCount(XjApplication.getInstance()));

                    Log.i("debbug", "getCooperation=" + PreferencesUtil.getCooperationIndexCount(XjApplication.getInstance()));

                    Log.i("debbug", "getDoorPasteIndexCount=" + PreferencesUtil.getDoorPasteIndexCount(XjApplication.getInstance()));

                    Log.i("debbug", "getVoteCount=" + PreferencesUtil.getVoteIndexCount(XjApplication.getInstance()));

                    Log.i("debbug", "getUnReadCount=" + PreferencesUtil.getUnReadCount(XjApplication.getInstance()));
//                    handler1.sendEmptyMessage(REFRESH_MENU_INDEX_MSG_COUNT);
                }
                if (mHandler != null) {
                    mHandler.sendEmptyMessage(REFRESH_MENU_INDEX_MSG_COUNT);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("onion", error.toString());
            }
        };
        service.getIndexSearchDataList(map, callback);
    }


}
