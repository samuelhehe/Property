package xj.property.fragment;//package xj.property.fragment;
//
//
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.GridView;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.activeandroid.query.Select;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import de.greenrobot.event.EventBus;
//import retrofit.Callback;
//import retrofit.RestAdapter;
//import retrofit.RetrofitError;
//import retrofit.client.Response;
//import retrofit.http.Body;
//import retrofit.http.GET;
//import retrofit.http.Header;
//import retrofit.http.POST;
//import retrofit.http.Path;
//import retrofit.http.QueryMap;
//import xj.property.R;
//import xj.property.XjApplication;
//import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
//import xj.property.activity.LifeCircle.FriendZoneIndexActivity;
//import xj.property.activity.activities.ActivitiesActivity;
//import xj.property.activity.activities.IndexSearchResultActivity;
//import xj.property.activity.bangzhu.ActivityBangZhuElection;
//import xj.property.activity.call.CourierActivity;
//import xj.property.activity.call.EmergencyNumberActivity;
//import xj.property.activity.call.SendWaterActivity;
//import xj.property.activity.contactphone.FastShopIndexActivity;
//import xj.property.activity.membership.ActivityMSPCardList;
//import xj.property.activity.payment.PayListActivity;
//import xj.property.activity.repair.RepairListActivity;
//import xj.property.activity.user.NotifyListActivity;
//import xj.property.activity.user.UserBonusActivity;
//import xj.property.activity.welfare.ActivityWelfareIndex;
//import xj.property.adapter.GridViewAdapter;
//import xj.property.beans.IndexBean;
//import xj.property.beans.IndexMenuResult;
//import xj.property.beans.LifeCircleCountBean;
//import xj.property.beans.StatusBean;
//import xj.property.beans.VistorEvent;
//import xj.property.cache.XJNotify;
//import xj.property.event.NewNotifyEvent;
//import xj.property.event.NewPushEvent;
//import xj.property.utils.DensityUtil;
//import xj.property.utils.image.utils.StrUtils;
//import xj.property.utils.other.AdminUtils;
//import xj.property.utils.other.Config;
//import xj.property.utils.other.FastShopCarDBUtil;
//import xj.property.utils.other.PreferencesUtil;
//import xj.property.utils.other.UserUtils;
//import xj.property.widget.MyScrollView;
//
//public class IndexFragment2 extends BaseFragment implements OnItemClickListener {
//    private static final String TAG = "MainActivity";
//    //search bar
////    private TextView query;
//
//
//    private ImageButton search_clear;
//    private ImageButton search_bar;
//    private List<IndexBean> beans = new ArrayList<>();
//    private GridView gv_main;
//    private GridViewAdapter adapter;
//    private MyScrollView svRoot;
//    private LinearLayout ll_notify;
//    private TextView tv_pay;
//    public TextView tv_more;
//    public static boolean newMsgIsRead;
//    //    private LinearLayout ll_index;
//    private TextView tvNotify, tvNotifyCount, tvContent, tvNotifyTime;//通知及详情
//    private View line;
//    private int differHigh;
//    private Handler handler;
//    LinearLayout.LayoutParams params;
//    private XjApplication app;
//    private ImageView im_top;
//    private boolean isOpen;
//
//
//    private boolean haveLifeCircle = true,haveFastShop = true,haveActive = true,haveWelfare = true,  haveShopVipCard = true;
//
//    // http://114.215.105.202:8080/api/v1/communities/1/webIm/communityRelation/getRelationAndName
////    private static String[] names = {"业主活动","缴费","快店","蔬菜水果","外卖","送水","客服","快递","维修","紧急号码","保洁","更多"};
////    private static int[] icons = { R.drawable.home_item_activity, R.drawable.home_item_payment,
////            R.drawable.home_item_store, R.drawable.home_item_fruit,R.drawable.home_item_outfood, R.drawable.home_item_water,
////            R.drawable.home_item_servicer, R.drawable.home_item_courier, R.drawable.home_item_repair,
////            R.drawable.home_item_phonenumber,R.drawable.home_item_clearer};
//    private static String[] icons = {"home_item_activity", "home_item_payment",
//            "home_item_store", "home_item_fruit", "home_item_outfood", "home_item_water",
//            "home_item_servicer", "home_item_courier", "home_item_repair",
//            "home_item_phonenumber", "home_item_lifecircle","home_item_fuli","home_item_shopvipcard" };//"home_item_clearer"
//
//    private TextView home_index_community_name_tv;/// 小区名字
//
//    /**
//     * 当前EventBus 处理事件 消息码
//     */
//    private int current_notify_cmd_code;
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onStop() {
//        EventBus.getDefault().unregister(this);
//        super.onStop();
//    }
//
//
//    /**
//     * EventBus 事件来袭
//     */
//    // This method will be called when a MessageEvent is posted
//    public void onEvent(NewNotifyEvent event) {
//        if (event.isNew) {//收到新消息，标识未读
//            newMsgIsRead = false;
//        }
//
//        initNotify();
//
//    }
//
//
//
//
//
//    public void onEvent(NewPushEvent event) {
//        switch (event.cmdCode) {
//            case 110:
//                selectNewActivity();
//            case 121:
//            case 122:
//                selectNewCircle();
//        }
//    }
//
//    XJNotify notify;
//
//    /**
//     * EventBus 事件来袭
//     */
//    private void initNotify() {
//
//        notify = selectNewNotify();
//
//        /// 消息标题
//        tvNotify.setText(notify.title + "");
//        /// 消息码
//        current_notify_cmd_code = notify.CMD_CODE;
//        switch (notify.CMD_CODE) {
//
//            case 100:
//                tv_pay.setVisibility(View.GONE);
//                break;
//
//            case 106:
//                tv_pay.setVisibility(View.VISIBLE);
//                tv_pay.setText("查看帮帮券");
//
//            case 107:
//                tv_pay.setVisibility(View.VISIBLE);
//                tv_pay.setText("查看详情");
//
//
//            default:
//                break;
//        }
//
//
////     int width=   getResources().getDimensionPixelOffset(R.dimen.notify_content_width);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                String strs[] = notify.content.split("\n");
//                int width = tvContent.getWidth();
//                StringBuilder stringBuilder = new StringBuilder();
//                for (int i = 0; i < strs.length && i < 3; i++) {
//                    if (strs[i].length() > 0)
//                        strs[i] = StrUtils.str2SingleNotify(strs[i], tvContent.getPaint(), width - 100);
//                    stringBuilder.append(strs[i]);
//                }
//
//                tvContent.setText(stringBuilder.toString());
//                line.setVisibility(View.VISIBLE);
//
//
//                tv_pay.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        switch (current_notify_cmd_code){
//
//                            case 106:
//                                if (PreferencesUtil.getLogin(getActivity()))
//                                    startActivity(new Intent(getActivity(), UserBonusActivity.class));
//                                break;
//                            case 107:
//
//                                if (PreferencesUtil.getLogin(getActivity())){
//                                    startActivity(new Intent(getActivity(), ActivityBangZhuElection.class));
//                                }else{
//                                    startActivity(new Intent(getActivity(), RegisterLoginActivity.class));
//                                }
//                                break;
//                        }
//
//                    }
//                });
//
//
//
//            }
//        }, 50);
//
//
//
////        tvContent.setText(notify.content);
//        tvNotifyTime.setText(StrUtils.getTime4Millions(notify.timestamp * 1000l));
//        int count = selectNewNotifyCount();
//        if (count != 0)//
//        {
//            tvNotifyCount.setVisibility(View.VISIBLE);
//            tvNotifyCount.setText("" + selectNewNotifyCount());
//            tv_more.setText("未读" + count + "条");
//        } else {
//            newMsgIsRead = "yes".equals(notify.read_status);
//            tvNotifyCount.setVisibility(View.GONE);
//            tv_more.setText("查看更多");
//        }
//
//
//        /*handler.post(new Runnable() {
//            @Override
//            public void run() {
//                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                params.setMargins(0,ll_notify.getHeight(),0,0);
//                gv_main.setLayoutParams(params);
////                        differHigh=  svRoot.getHeight()-gv_main.getHeight();
//                //differHigh = tvContent.getHeight() - tvNotify.getHeight();
//            }
//        });*/
//    }
//
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        isaddMargin = false;
//        super.setUserVisibleHint(isVisibleToUser);
//    }
//
//    private int selectNewNotifyCount() {
//        return new Select().from(XJNotify.class).where("emobid = ? and read_status = ?", PreferencesUtil.getLogin(getActivity()) ? PreferencesUtil.getLoginInfo(getActivity()).getEmobId() : "-1", "no").execute().size();
//    }
//
//    //获取最新通知
//    private XJNotify selectNewNotify() {
//
//        XJNotify notify = new Select().from(XJNotify.class).where("emobid = ?", PreferencesUtil.getLogin(getActivity()) ? PreferencesUtil.getLoginInfo(getActivity()).getEmobId() : "-1").orderBy("timestamp DESC").executeSingle();
//        /// 从db中查询消息
//        if (notify == null) {
//            /// 默认消息
//            return new XJNotify("", 100, "社区通知栏", " 注册成为帮帮用户，即可实时查看物业最新通知。停水停电、快递查收、小区保修…时刻掌握小区动态。有帮帮，没麻烦！", (int) (new Date().getTime() / 1000), true, "yes");
//        }
//        return notify;
//
//    }
//
//    int margindefault;
//    boolean isaddMargin = false;
//    int lastTime;
//    float downY = 0;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        margindefault = DensityUtil.dip2px(getActivity(), 165);
////向上拖动，通知栏隐藏，scrollview的详情显示，
//        return inflater.inflate(R.layout.fragment_index, container, false);
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        app = (XjApplication) getActivity().getApplication();
//        gv_main = (GridView) getView().findViewById(R.id.gv_main);
////        adapter = new GridViewAdapter(getActivity(),names,icons);
////        gv_main.setAdapter(adapter);
//
//        gv_main.setOnItemClickListener(this);
//        tvNotify = (TextView) getView().findViewById(R.id.tv_notify);
//        ll_notify = (LinearLayout) getView().findViewById(R.id.ll_notify);
//        tvContent = (TextView) getView().findViewById(R.id.tv_notify_content);
//        tv_more = (TextView) getView().findViewById(R.id.tv_notify_more);
//        tvNotifyTime = (TextView) getView().findViewById(R.id.tv_notify_time);
//
//        tv_pay = (TextView) getView().findViewById(R.id.tv_notify_pay);
//
//        tvNotifyCount = (TextView) getView().findViewById(R.id.tv_newnotify_count);
//        svRoot = (MyScrollView) getView().findViewById(R.id.sv_root);
//        line = getView().findViewById(R.id.line);
//        im_top = (ImageView) getView().findViewById(R.id.im_top);
//
//        home_index_community_name_tv = (TextView) getView().findViewById(R.id.home_index_community_name_tv);
//
////        final TextView query = (TextView) getView().findViewById(R.id.query);
//
//
//        final ImageButton search_clear = (ImageButton) getView().findViewById(R.id.search_clear);
//        final ImageButton search_bar = (ImageButton) getView().findViewById(R.id.search_bar);
//        search_bar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent();
//                //intent.putExtra("searchName",query.getText()+"");
//                intent.setClass(getActivity(), IndexSearchResultActivity.class);
//                startActivity(intent);
//
//            }
//        });
//
//
//        handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                switch (msg.what) {
//
//                    case 1:
//                        initCache();
//
//                        break;
//
//                    case 2:
//
//                        selectNewActivity();
//
//                        selectNewCircle();
//
//                        getIndexMenu();
//
//                        refreshUI();
//
//                        break;
//                    case 3:
//                        selectNewActivity();
//                        selectNewCircle();
//                        break;
//                }
//            }
//        };
//
////
////        query.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent intent = new Intent();
////                //intent.putExtra("searchName",query.getText()+"");
////                intent.setClass(getActivity(), IndexSearchResultActivity.class);
////                startActivity(intent);
////            }
////        });
////        query.setKeyListener(null);
//
//
//        /***
//
//         query.addTextChangedListener(new TextWatcher() {
//        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//        }
//
//        @Override public void afterTextChanged(Editable s) {
//        if (s.length() == 0) {
//        search_clear.setVisibility(View.INVISIBLE);
//        } else {
//        search_clear.setVisibility(View.VISIBLE);
//        }
//        }
//        });
//
//         search_clear.setOnClickListener(new View.OnClickListener() {
//        @Override public void onClick(View v) {
//        query.setText("");
//        }
//        });
//         query.setOnKeyListener(new View.OnKeyListener() {
//        @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
//        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (inputMethodManager.isActive()) {
//        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
//        }
//        if (query.getText() + "" != null) {
//        Intent intent = new Intent();
//        intent.putExtra("searchName", query.getText() + "");
//        intent.setClass(getActivity(), IndexSearchResultActivity.class);
//        startActivity(intent);
//        return true;
//        }
//
//        }
//        return false;
//        }
//        });
//         *
//         */
//
////        ll_index=(LinearLayout)getView().findViewById(R.id.ll_index);
////        svRoot.scrollTo(0,tvNotify.getHeight()-tvContent.getHeight());
///*       svRoot.setScrollViewListener(new MyScrollView.ScrollViewListener() {
//            @Override
//            public void onScrollChanged(View v, int x, int y, int oldx, int oldy) {
//
//                if (y == 0 && !newMsgIsRead) {
//                    newMsgIsRead = true;
//                    XJNotify notify = selectNewNotify();
//                    notify.isReaded = true;
//                    notify.save();
//                    EventBus.getDefault().post(new NewNotifyEvent(notify, true));
//                }
//                if (!isaddMargin || y > oldy && params.topMargin < margindefault) {
//                    params = (LinearLayout.LayoutParams) line.getLayoutParams();
//                    params.setMargins(0, y, 0, 0);
//                    line.setLayoutParams(params);
//                    isaddMargin = true;
//                }
//              if(y>=ll_notify.getHeight()){
//                XJNotify notify=  selectNewNotify();
//                  notify.isReaded=true;
//                  notify.save();
//              EventBus.getDefault().post(new NewNotifyEvent(notify, true));
//              }
//            }
//        });*/
//
//
//
//        tv_more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), NotifyListActivity.class));
//            }
//        });
////        svRoot.setScrollViewListener(new MyScrollView.ScrollViewListener() {
////            @Override
////            public void onScrollChanged(View v, int x, int y, int oldx, int oldy) {
////
////            }
////        });
//
//        svRoot.setOnTouchListener(new View.OnTouchListener() {
//            private int lastY = 0;
//            private int touchEventId = -9983761;
//
//
//            Handler handler = new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    super.handleMessage(msg);
//                    View scroller = (View) msg.obj;
//
//                    if (msg.what == touchEventId) {
//                        if (lastY == scroller.getScrollY()) {
//                            //停止了
//
//                            if (!isOpen) { //colseing
//                                isaddMargin = true;
//                                if (lastY > margindefault / 3 * 2) {
//                                    svRoot.fullScroll(ScrollView.FOCUS_DOWN);//关闭
//                                    isOpen = false;
//                                } else {
//                                    svRoot.fullScroll(ScrollView.FOCUS_UP);//打开
//                                    isOpen = true;
//                                }
//                            } else {//opening
//                                isaddMargin = true;
//                                if (lastY < margindefault / 3) {
//                                    svRoot.fullScroll(ScrollView.FOCUS_UP);//打开
//                                    isOpen = true;
//                                } else {
//                                    isOpen = false;
//                                    svRoot.fullScroll(ScrollView.FOCUS_DOWN);//关闭
//                                }
//                            }
//                        } else {
//                            handler.sendMessageDelayed(handler.obtainMessage(touchEventId, scroller), 1);
//                            lastY = scroller.getScrollY();
//                        }
//                    }
//                }
//            };
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
////                if (event.getAction() == MotionEvent.ACTION_MOVE && !newMsgIsRead) {
////                    newMsgIsRead = true;
////                    XJNotify notify = selectNewNotify();
////                    notify.isReaded = true;
////                    notify.save();
////                    EventBus.getDefault().post(new NewNotifyEvent(notify, true));
////                }
//                if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                    downY = event.getY();
//                    int scrollY = v.getScrollY();
//                    if (v.getScrollY() == 2 * margindefault / 3) {
//                        eventService(0 + "", "通知公告");
//                    }
//
//
//                    if (!isOpen && scrollY >= margindefault / 3) {
////                        isOpen = true;
//////                        if (!isaddMargin) {
////                            params = (LinearLayout.LayoutParams) line.getLayoutParams();
////                            params.setMargins(0, margindefault, 0, 0);
////                            line.setLayoutParams(params);
////                            isaddMargin = true;
////
//////                        }
//
//                    } else {
////                        isOpen = false;
//                        // eventService(0 + "","通知公告");
//                    }
//                    if (!newMsgIsRead || notify.read_status.equals("no")) {
//                        newMsgIsRead = true;
//                        XJNotify notify = selectNewNotify();
//                        notify.read_status = "yes";
//                        notify.save();
//                        EventBus.getDefault().post(new NewNotifyEvent(notify, true));
//                    }
//
//                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    handler.sendMessageDelayed(handler.obtainMessage(touchEventId, v), 5);
//                    int time = (int) (new Date().getTime() / 1000);
////                    boolean isDown=event.getY()>=downY;
//                    if (time - lastTime > 7 && v.getScrollY() < margindefault / 3 * 2) {
//                        lastTime = time;
//                        eventService(0 + "", "通知公告");
//                    }
//
//
////                    Log.i("onion","ScrollY"+v.getScrollY());
////                    Log.i("onion","downY"+downY);
////                    Log.i("onion","upY"+event.getY());
////                    if(isDown){
////                        svRoot.fullScroll(ScrollView.FOCUS_DOWN);
////                    }else {
////                        svRoot.scrollTo(0,0);
////                    }
//
//                }
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (isOpen) {
//                        eventService(0 + "", "通知公告");
//                    }
//                }
//
//                if (!isaddMargin) {
//                    params = (LinearLayout.LayoutParams) line.getLayoutParams();
//                    params.setMargins(0, margindefault, 0, 0);
//                    line.setLayoutParams(params);
//                    isaddMargin = true;
//
//                }
//                if (v.getId() == R.id.gv_main)
//                    return gv_main.dispatchTouchEvent(event);
//                else {
//                    return ll_notify.dispatchTouchEvent(event);
//                }
//
//            }
//        });
//        im_top.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                params = (LinearLayout.LayoutParams) line.getLayoutParams();
//                if (!isOpen) {//打开
//                    if (!isaddMargin) {
//                        params.setMargins(0, margindefault, 0, 0);
//                        line.setLayoutParams(params);
//                        isaddMargin = true;
//
//                    }
//                    XJNotify notify = selectNewNotify();
//                    notify.read_status = "yes";
//                    notify.save();
//
//                    EventBus.getDefault().post(new NewNotifyEvent(notify, true));
//                    svRoot.fullScroll(ScrollView.FOCUS_DOWN);
//
//
//                } else {//关闭
//                    svRoot.scrollTo(0, 0);
//                    //采集
//                    eventService(0 + "", "通知公告");
//                }
//                isOpen = !isOpen;
//            }
//        });
//
//
//    }
//
//    /**
//     * 刷新UI 上需要刷新的,从SP中获取数据更新
//     */
//    private void refreshUI() {
//
//        Log.i("debbug","refreshUI"+PreferencesUtil.getCommityName(getActivity()));
//        if(home_index_community_name_tv!=null){
//            home_index_community_name_tv.setText(PreferencesUtil.getCommityName(getActivity()));
//        }
//    }
//
//    private void initCache() {
//        List<IndexBean> list = PreferencesUtil.getIndexBean(getActivity());
//        if (list != null) {
//            beans.clear();
//            beans.addAll(list);
//        }
//        adapter = new GridViewAdapter(getActivity(), beans, svRoot.getWidth() / 3 - DensityUtil.px2dip(getActivity(), 1), svRoot.getHeight() / 4 - DensityUtil.px2dip(getActivity(), 1));
//        app.setGrideWidth(svRoot.getWidth() / 3 - DensityUtil.px2dip(getActivity(), 1));
//        app.setGrideHeight(svRoot.getHeight() / 4 - DensityUtil.px2dip(getActivity(), 4));
//        gv_main.setAdapter(adapter);
//
//        for (int i = 0; i < beans.size(); i++) {
//            switch (beans.get(i).getServiceId()) {
//
//                case 1:
//                    activeIndex = i;
//                    break;
//                case 16:
//                    circleIndex = i;
//                    break;
//                case 3:
//                    fastshopIndex = i;
//                    break;
//                case 19:
//                    welfareIndex = i;
//                    break;
//
//                /// 会员卡
//                case 20:
//                    shopVipcardIndex = i;
//                    break;
//
//
//
//
//                /// TODO
//            }
//        }
//        handler.sendEmptyMessageDelayed(2, 200);
//    }
//
//
////    private void addListener() {
////    }
//
//    interface IndexService {
//        @GET("/api/v1/communities/{communityId}/services")
////        void getIndex(@QueryMap Map<String,String> options, Callback<IndexBean> cb);
//        void getIndex(@Path("communityId") int communityId, @QueryMap HashMap<String, String> map, Callback<IndexMenuResult> cb);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        handler.sendEmptyMessageDelayed(1, 200);
////        if (handler != null)
////            handler.postDelayed(new Runnable() {
////                @Override
////                public void run() {
////                    svRoot.fullScroll(ScrollView.FOCUS_DOWN);
////                }
////            }, 500);
//
//
//        initNotify();
//
//
//
//    }
//
//    int activeIndex = 0;
//
//    int circleIndex = 7;
//
//    int fastshopIndex = 3;
//
//    /// 福利索引
//    int welfareIndex = -1;
//
//    //// 首页位置
//    int shopVipcardIndex = -1;
//
//
//
////    int fastshopNewCount = 0;
//
//    private void getIndexMenu() {
//
//
//        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
//        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
//        IndexService service = restAdapter.create(IndexService.class);
//        Callback<IndexMenuResult> callback = new Callback<IndexMenuResult>() {
//            @Override
//            public void success(IndexMenuResult indexMenuResult, Response response) {
//                if ("yes".equals(indexMenuResult.getStatus())) {
//
//                    List<IndexBean> loadbeans = indexMenuResult.getInfo();
//
//
//                    if (loadbeans.size() < 11) {
//                        for (int i = 0; i < icons.length; i++) {
//                            int j = 0;
//                            for (; j < loadbeans.size(); j++) {
//                                if (icons[i].equals(loadbeans.get(j).getImgName())) break;
//                            }
//                            if (j == loadbeans.size()&&loadbeans.size()<11) {
//                                loadbeans.add(new IndexBean(i * (-1), null, null, null, icons[i] + "_off", false));
//                            }
//                        }
//                    }
//
//
//                    loadbeans.add(new IndexBean(11, null, null, "更多", "home_item_more_off", false));
////                    loadbeans.add(new IndexBean(-2, null, null, "水果", "home_item_fruit",false));
////                    loadbeans.add(new IndexBean(-3, null, null, "保洁", "home_item_clearer",false));
////                    loadbeans.add(new IndexBean(-4, null, null, "缴费", "home_item_payment",false));
////                    loadbeans.add(new IndexBean(-5, null, null, "更多", "home_item_more", false));
////                    boolean changeflag = false;
//
//
//                    line.setVisibility(View.VISIBLE);
////                    if (!changeflag) return;
//
//                    if (PreferencesUtil.getIndexTime(getActivity()) == indexMenuResult.getCreateTime())
//                        return;
//                    PreferencesUtil.saveIndexTime(getActivity(), indexMenuResult.getCreateTime());
//                    PreferencesUtil.saveIndexBean(getActivity(), loadbeans);
//
//                    haveLifeCircle = false;
//                    haveFastShop = false;
//                    haveActive =  false;
//                    haveWelfare = false;
//
//                    haveShopVipCard = false;
//
//
//                    for (int i = 0; i < loadbeans.size(); i++) {
//
//                        switch (loadbeans.get(i).getServiceId()) {
//
//                            case 1:
//                                activeIndex = i;
//                                haveActive = true;
//                                break;
//                            case 16:
//                                circleIndex = i;
//                                haveLifeCircle = true;
//                                break;
//                            case 3:
//                                fastshopIndex = i;
//                                haveFastShop = true;
//                                break;
//
//                            case 19:
//                                welfareIndex = i;
//                                haveWelfare = true;
//                                break;
//
//                            ///TODO
//
//                            case 20:
//                                shopVipcardIndex = i;
//                                haveShopVipCard = true;
//                                break;
//
//
//
//                        }
////                        if (!beans.contains(loadbeans.get(i)) || beans.size() != loadbeans.size()) {
////                            changeflag = true;
////                            break;
////                        }
//                        Log.i("debbug", "haveActive=" + haveActive + "  haveLifeCircle=" + haveLifeCircle + "   haveFastShop" + haveFastShop);
//                    }
//
//                    beans.clear();
//                    beans.addAll(loadbeans);
//                    adapter.notifyDataSetChanged();
//                    handler.sendEmptyMessageDelayed(3, 100);
//                } else {
//                    Toast.makeText(getActivity(), "服务器异常", Toast.LENGTH_LONG).show();
//
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                line.setVisibility(View.VISIBLE);
//                error.printStackTrace();
//            }
//        };
//
//        HashMap<String, String> option = new HashMap<>();
//        option.put("appVersionId", "4");
////        option.put("appVersionId", "3");
//        service.getIndex(PreferencesUtil.getCommityId(getActivity()), option, callback);
//
//    }
//
//    int ActivitiesRequestCode = 998; /// 活动请求码
//
//    int CircleRequestCode = 989;  /// 生活圈请求码
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == ActivitiesRequestCode) {
//            selectNewActivity();
//        } else if (CircleRequestCode == requestCode) {
//            selectNewCircle();
//        }
//    }
//
//    //TODO
//    @Override
//    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//
//        eventService(beans.get(position).getServiceId() + "", beans.get(position).getServiceName());
//
//        Intent intentPush = new Intent();
//
//        switch (beans.get(position).getServiceId()) {
//
//
//            case 1:
//                intentPush.setClass(getActivity(), ActivitiesActivity.class);
//                startActivityForResult(intentPush, ActivitiesRequestCode);
//                break;
//
//            case 2:
//                intentPush.setClass(getActivity(), PayListActivity.class);
//                startActivity(intentPush);
//                break;
//
//            case 7:
//                if (PreferencesUtil.getLogin(getActivity())) {
//                    AdminUtils.askAdmin(getActivity(), "service", null, null);
//                } else {
//                    arg1.setClickable(false);
//                    Intent intent = new Intent(getActivity(), RegisterLoginActivity.class);
//                    startActivity(intent);
//                    arg1.setClickable(true);
//                }
//                break;
//
//            case 8:
//                intentPush.setClass(getActivity(), CourierActivity.class);
//                intentPush.putExtra(Config.XJKEY_SHOP, 12);
//                intentPush.putExtra(Config.INTENT_PARMAS1, beans.get(position).getServiceName());
//                startActivity(intentPush);
//                break;
//
//            case 3:
////                intentPush.setClass(getActivity(), FastShopListActivity.class);
//                intentPush.setClass(getActivity(), FastShopIndexActivity.class);
//                startActivity(intentPush);
//                FastShopCarDBUtil.initData(getActivity());
////                fastshopNewCount = 0;
//                PreferencesUtil.saveFastshopNewCount(XjApplication.getInstance(),0);
//                break;
//
//            case 9:
//                startActivity(new Intent(getActivity(), RepairListActivity.class));
//                break;
//
//            case 10:
//                intentPush.setClass(getActivity(), EmergencyNumberActivity.class);
//                intentPush.putExtra(Config.XJKEY_SHOP, 11);
//                intentPush.putExtra(Config.INTENT_PARMAS1, beans.get(position).getServiceName());
//                startActivity(intentPush);
//                break;
//
//
//
//            case 11://保洁
//                startActivity(new Intent(getActivity(), RepairListActivity.class));
//                break;
//
//
//            case 6:
//                intentPush.setClass(getActivity(), SendWaterActivity.class);
//                intentPush.putExtra(Config.XJKEY_SHOP, 4);
//                intentPush.putExtra(Config.INTENT_PARMAS1, beans.get(position).getServiceName());
//                startActivity(intentPush);
//                break;
//
//
//            case 16:
//                intentPush.setClass(getActivity(), FriendZoneIndexActivity.class);
//                startActivityForResult(intentPush, CircleRequestCode);
//                break;
//
//
//            case 19:
//                intentPush.setClass(getActivity(), ActivityWelfareIndex.class);
//                startActivity(intentPush);
//                PreferencesUtil.saveWelfareCount(XjApplication.getInstance(), 0);
//                break;
//
//
//            /// TODO
//
//            case 20:
//
//                intentPush.setClass(getActivity(), ActivityMSPCardList.class);
//                startActivity(intentPush);
//                PreferencesUtil.saveShopVipCardCount(XjApplication.getInstance(), 0);
//                PreferencesUtil.saveIsUnReadVipCard(XjApplication.getInstance(), false);
//
//                break;
//
//
//
////            case 9:
////                intentPush.setClass(getActivity(), TakeOutActivity.class);
////                startActivity(intentPush);
////                break;
////            case 11:
////                intentPush.setClass(getActivity(), MoreActivity.class);
////                startActivity(intentPush);
////                break;
//
//
//            default:
//                break;
//        }
//    }
//
//    private void selectNewActivity() {
//        if (gv_main.getChildCount() < 1) return;
//
//        TextView tv = (TextView) gv_main.getChildAt(activeIndex).findViewById(R.id.tv_num);
//
//        int count = PreferencesUtil.getUnReadCount(getActivity());
//
//        if (count > 0) {
//            tv.setVisibility(View.VISIBLE);
//            tv.setText(count + "");
//        } else {
//            tv.setVisibility(View.GONE);
//        }
//
//        if(!haveActive){
//            tv.setVisibility(View.GONE);
//        }
//    }
//
//    /**
//     * 刷新新消息
//     */
//    private void selectNewCircle() {
//
//        if (gv_main == null || gv_main.getChildCount() < 1) return;
//
//        TextView tvLife = (TextView) gv_main.getChildAt(circleIndex).findViewById(R.id.tv_num);
//
//        Log.i("debbug", "havaactive==" + circleIndex);
//
//        int count = PreferencesUtil.getUnReadCircleCount(getActivity());
//        if (count > 0) {
//            tvLife.setVisibility(View.VISIBLE);
//            tvLife.setText(count + "");
//        } else {
//            tvLife.setVisibility(View.GONE);
//        }
//        if(!haveLifeCircle){
//            tvLife.setVisibility(View.GONE);
//        }
//
//        /// 快店
//        TextView tvFastshop = (TextView) gv_main.getChildAt(fastshopIndex).findViewById(R.id.tv_num);
//        if (PreferencesUtil.getFastshopNewCount(XjApplication.getInstance()) > 0) {
//            tvFastshop.setVisibility(View.VISIBLE);
//            tvFastshop.setText(""+PreferencesUtil.getFastshopNewCount(XjApplication.getInstance()));
//        } else {
//            tvFastshop.setVisibility(View.GONE);
//        }
//
//        if(!haveFastShop) {
//            tvFastshop.setVisibility(View.GONE);
//            PreferencesUtil.saveFastshopNewCount(XjApplication.getInstance(), 0);
//        }
//
//
//        /// 福利
//        if(PreferencesUtil.getWelfareCount(XjApplication.getInstance())>0&&welfareIndex != -1){
//            TextView tvWelfare = (TextView) gv_main.getChildAt(welfareIndex).findViewById(R.id.tv_num);
//            if (PreferencesUtil.getWelfareCount(XjApplication.getInstance()) > 0) {
//                tvWelfare.setVisibility(View.VISIBLE);
//                tvWelfare.setText("新");
//            } else {
//                tvWelfare.setVisibility(View.GONE);
//            }
//
//            if(!haveWelfare) {
//                tvWelfare.setVisibility(View.GONE);
//                PreferencesUtil.saveWelfareCount(XjApplication.getInstance(), 0);
//            }
//        }
//
//
//        /// TODO 添加会员卡
//
//        if(PreferencesUtil.getShopVipCardCount(XjApplication.getInstance())>0&&shopVipcardIndex != -1){
//            TextView tvshopVipcard = (TextView) gv_main.getChildAt(shopVipcardIndex).findViewById(R.id.tv_num);
//            if (PreferencesUtil.getShopVipCardCount(XjApplication.getInstance()) > 0) {
//
//                tvshopVipcard.setText(""+PreferencesUtil.getShopVipCardCount(XjApplication.getInstance()));
//                tvshopVipcard.setVisibility(View.VISIBLE);
//
//            } else {
//
//                if(PreferencesUtil.isUnReadVipCard(getActivity())){
//                    tvshopVipcard.setText("新");
//                    tvshopVipcard.setVisibility(View.VISIBLE);
//
//                }else{
//                    tvshopVipcard.setVisibility(View.GONE);
//                }
//            }
//
//            if(!haveShopVipCard) {
//                tvshopVipcard.setVisibility(View.GONE);
//                PreferencesUtil.saveShopVipCardCount(XjApplication.getInstance(), 0);
//            }
//        }else {
//            TextView tvshopVipcard = (TextView) gv_main.getChildAt(shopVipcardIndex).findViewById(R.id.tv_num);
//
//            if(PreferencesUtil.isUnReadVipCard(getActivity())){
//
//                tvshopVipcard.setText("新");
//                tvshopVipcard.setVisibility(View.VISIBLE);
//
//            }else{
//                tvshopVipcard.setVisibility(View.GONE);
//            }
//        }
//
//
//    }
//
//
//
//    interface EventServiceService {
//        ///api/v1/events/
//        @POST("/api/v1/communities/{communityId}/events/")
//        void onEventService(@Header("signature") String signature, @Body VistorEvent vistorEvent, @Path("communityId") int communityId, Callback<StatusBean> callback);
//    }
//
//    //自定义事件采集
//    public void eventService(String serviceId, String serviceName) {
//        eventService(serviceId, serviceName, 2);
//
//    }
//
//    public void eventService(final String serviceId, final String serviceName, final int count) {
//        Log.i("debbug","eventService"+serviceId+serviceName+count);
//        if (count < 1) return;
//        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_AGENT).build();
////        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://192.168.1.51:9095").build();
//        EventServiceService service = restAdapter.create(EventServiceService.class);
//        Callback<StatusBean> callback = new Callback<StatusBean>() {
//            @Override
//            public void success(StatusBean object, Response response) {
////               Log.i("onion", "自定义事件采集成功"+object);
//                if (object != null && !"true".equals(object.getStatus()))
//                    eventService(serviceId, serviceName, count - 1);
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
////                Log.i("onion", "自定义事件采集失败" + error.toString());
//                eventService(serviceId, serviceName, count - 1);
//            }
//        };
//
////        Log.i("debbug","context="+getActivity());
//        if(getActivity() == null){
//            return;
//        }
//        VistorEvent vistorEvent = new VistorEvent();
//        vistorEvent.hour = (int) (new Date().getTime() / 1000);
//        vistorEvent.serviceId = serviceId;
//        vistorEvent.serviceName = serviceName;
//        try {
//            vistorEvent.label = getActivity().getLocalClassName();
//            vistorEvent.appVersion = getVersionName();
//        } catch (Exception e) {
//            vistorEvent.appVersion = "";
//        }
//
//        Log.i("debbug","getTourist="+PreferencesUtil.getTourist(getActivity()));
//
//        vistorEvent.setEmobId(PreferencesUtil.getLogin(getActivity()) ? PreferencesUtil.getLoginInfo(getActivity()).getEmobId() : PreferencesUtil.getTourist(getActivity()));
//        vistorEvent.setUserId(PreferencesUtil.getLogin(getActivity()) ? PreferencesUtil.getUserId(getActivity()) + "" : PreferencesUtil.getTouristid(getActivity()) + "");
//        // Log.i("onion","info1: "+info.toString());
//        if (vistorEvent.getEmobId().trim().length() == 0) {
//            vistorEvent.setUserId(PreferencesUtil.getlogoutUserId(getActivity()) + "");
//            vistorEvent.setEmobId(PreferencesUtil.getlogoutEmobId(getActivity()));
//        }
//
//
//        //  Log.i("onion", "自定义事件采集: " + "serviceId: " + serviceId + "  " + vistorEvent.toString());
//
//        service.onEventService(StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(vistorEvent)), vistorEvent, PreferencesUtil.getCommityId(getActivity()), callback);
//    }
//
//
//    private String getVersionName() throws Exception {
//// 获取packagemanager的实例
//        PackageManager packageManager = getActivity().getPackageManager();
//
//
//// getPackageName()是你当前类的包名，0代表是获取版本信息
//        PackageInfo packInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
//        String version = packInfo.versionName;
//        return version;
//    }
//
//    public void getLifeCircleCount(Handler handler) {
//        getShopInfo(handler);
//    }
//
//    interface LifeCircleCountService {
//        @GET("/api/v1/communities/{communityId}/home/{emobId}/tips")
//        void getIndexSearchDataList(@Path("communityId") int communityId, @Path("emobId") String emobId, @QueryMap Map<String, String> map, Callback<LifeCircleCountBean> cb);
//    }
//
//    private void getShopInfo(final Handler handler) {
//        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
//        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
//        LifeCircleCountService service = restAdapter.create(LifeCircleCountService.class);
//        Callback<LifeCircleCountBean> callback = new Callback<LifeCircleCountBean>() {
//            @Override
//            public void success(LifeCircleCountBean bean, Response response) {
//                if ("yes".equals(bean.getStatus())) {
//                    PreferencesUtil.saveLifeCircleCountTime(XjApplication.getInstance(), "" + System.currentTimeMillis() / 1000);
//                    if (PreferencesUtil.getUnReadCircleCount(XjApplication.getInstance()) < bean.getInfo().lifeCircleCount) {
//                        PreferencesUtil.saveUnReadCircleCount(XjApplication.getInstance(), bean.getInfo().lifeCircleCount);
//                    }
//                    if(PreferencesUtil.getCrazySalesCount(XjApplication.getInstance())<bean.info.CrazySalesCount){
//                        PreferencesUtil.saveCrazySalesCount(XjApplication.getInstance(), bean.info.CrazySalesCount);
//                    }
////                    PreferencesUtil.saveCrazySalesCount(XjApplication.getInstance(), bean.info.crazySalesCount);
////                    fastshopNewCount += bean.getInfo().getFastCount();
//
//
//                    if(bean.getInfo().welfare>0) {
//                        PreferencesUtil.saveWelfareCount(XjApplication.getInstance(), bean.getInfo().welfare);
//                    }
//
//
//                    ////TODO 保存未读消息个数
//
//                    if(bean.getInfo().getShopVipcard()>0) {
//                        PreferencesUtil.saveShopVipCardCount(XjApplication.getInstance(), bean.getInfo().getShopVipcard());
//                    }
//
//                    PreferencesUtil.saveFastshopNewCount(XjApplication.getInstance(),PreferencesUtil.getFastshopNewCount(XjApplication.getInstance())+bean.getInfo().getFastCount());
//
//                    Log.i("debbug", "getWelfareCount=" + PreferencesUtil.getWelfareCount(XjApplication.getInstance()));
//
//                    Log.i("debbug", "getShopVipcard=" + PreferencesUtil.getShopVipCardCount(XjApplication.getInstance()));
//
//
//
//                    selectNewCircle();
//
//                    handler.sendEmptyMessage(1);
//
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Log.e("onion", error.toString());
//            }
//        };
//        if ("0".equals(PreferencesUtil.getPrefLifeCircleCountTime(XjApplication.getInstance()))) {
//            PreferencesUtil.saveLifeCircleCountTime(XjApplication.getInstance(), "" + PreferencesUtil.isFirstUpdate(XjApplication.getInstance(), UserUtils.getVersion(XjApplication.getInstance())));
//            PreferencesUtil.saveUnReadCircleCount(XjApplication.getInstance(), 3);
//            selectNewCircle();
//            return;
//        }
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("time", PreferencesUtil.getPrefLifeCircleCountTime(XjApplication.getInstance()));
//        String version = "1.2.3";
//        try {
//            version = getVersionName();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        map.put("appVersionId", version);
//        String emobid;
//        if (PreferencesUtil.getLoginInfo(XjApplication.getInstance()) == null) {
//            emobid = "emobid";
//        } else {
//            emobid = PreferencesUtil.getLoginInfo(XjApplication.getInstance()).getEmobId();
//        }
//        service.getIndexSearchDataList(PreferencesUtil.getCommityId(XjApplication.getInstance()), emobid, map, callback);
//    }
//
//}
