package xj.property.activity.cooperation;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.sso.UMSsoHandler;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import xj.property.R;
import xj.property.XjApplication;
import xj.property.activity.HXBaseActivity.ChatActivity;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.activity.tags.MyOwnerTagsManagerDialog;
import xj.property.activity.tags.MyTagsManagerDialog;
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.adapter.ExpressionAdapter;
import xj.property.adapter.ExpressionPagerAdapter;
import xj.property.beans.LookedDetailsReqBean;
import xj.property.beans.LookedDetailsRespBean;
import xj.property.beans.MyTagsRespBean;
import xj.property.beans.NeighborDetailsV3Bean;
import xj.property.beans.ProviderDetailsRespBean;
import xj.property.beans.SysDefaultTagsV3Bean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.event.NetworkStateChangeEvent;
import xj.property.event.ProviderEvaEvent;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseTagUtils;
import xj.property.netbase.RetrofitFactory;
import xj.property.provider.ShareProvider;
import xj.property.utils.SmileUtils;
import xj.property.utils.message.XJContactHelper;
import xj.property.utils.other.Config;
import xj.property.utils.other.CooperationUtil;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;
import xj.property.widget.ExpandGridView;
import xj.property.widget.FilterFlowLayout;
import xj.property.widget.com.viewpagerindicator.CirclePageIndicator;

/**
 * 互助详情
 */
public class ProviderDetailsActivity extends HXBaseActivity implements MyTagsManagerDialog.onTagsA2BAddStatusListener, MyOwnerTagsManagerDialog.onTagsA2BAddStatusListener {

    private LinearLayout ll_errorpage;
    private LinearLayout ll_neterror;


    private UserInfoDetailBean userInfoDetailBean;

    private TextView tv_title;

    //// 时间
    private TextView provider_details_time_tv;

    /// 用户头像
    private ImageView provider_details_iv_avtar;
    /// 用户昵称
    private TextView provider_details_name_tv;
    /// 互助标题
    private TextView provider_details_title_tv;
    /// 互助内容
    private TextView provider_details_content_tv;
    /// 找TA
    private Button provider_details_findhe_btn;
    /// 赞人品
    private Button provider_details_zanrenpin_btn;
    ////标签
    private FilterFlowLayout provider_details_fflayout;
    /// 找过他的邻居
    private LinearLayout panic_has_purchase_llay;
    /// 更多用户
    private LinearLayout welfare_purchase_hasgoturs_lv;
    /// 加载评论列表
    private xj.property.widget.MyListView provider_details_discuss_lv;

    /// 标签添加对话框
    private MyTagsManagerDialog myTagsManagerDialog;

    private List<String> systemDefaulttags = new ArrayList<>();
    /// 添加标签
    private View provider_details_tags_add;
    /// 互助ID
    private String cooperationId;
    /// 评论条
    private PopupWindow popupWindow;

    private EditText editText;

    private Button btnSend;

    private Button btnCharter;

    ///
    private ScrollView root_view;


    //// 邻居帮详情Bean
    private NeighborDetailsV3Bean providerDetailsBean;
    /// 屏幕宽度
    private int screenWidth;
    /// 评论列表
    private LinearLayout provider_details_discuss_llay;
    /// 默认输入框
    private View defbtn_send;

    private View default_input_view;

    private View bar_bottom;

    private View et_sendmessage;

    private boolean isDataLoadComplete = false;
    private LinearLayout ll_face_container;
    private ViewPager vPager;
    private CirclePageIndicator vpager_indicator;
    private List<String> reslist;
    private CheckBox send_img_checkbox;
    private InputMethodManager imm;
    /// 2015/11/19 添加帮主头衔
    private ImageView provider_iv_user_type;

    private MyOwnerTagsManagerDialog myTagsManagerDialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_details);
        ShareProvider.getInitShareProvider(ProviderDetailsActivity.this);
        userInfoDetailBean = PreferencesUtil.getLoginInfo(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (userInfoDetailBean != null) {
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            screenWidth = getmContext().getResources().getDisplayMetrics().widthPixels;
            cooperationId = getIntent().getStringExtra("cooperationId");

            if (TextUtils.isEmpty(cooperationId)) {
                showToast("数据异常");
                finish();
            }
            initView();
            initData();

        } else {
            Intent intent = new Intent(this, RegisterLoginActivity.class);
            startActivity(intent);
        }
    }


    private Handler mHandler = new Handler();


    private void initData() {

        getProviderDetails();

        getSystemDefaultTags();

    }

    private void initView() {

        initTitle();

        provider_details_time_tv = (TextView) this.findViewById(R.id.provider_details_time_tv);
        provider_details_name_tv = (TextView) this.findViewById(R.id.provider_details_name_tv);

        provider_details_title_tv = (TextView) this.findViewById(R.id.provider_details_title_tv);
        provider_details_content_tv = (TextView) this.findViewById(R.id.provider_details_content_tv);

        provider_details_iv_avtar = (ImageView) this.findViewById(R.id.provider_details_iv_avtar);

        provider_iv_user_type = (ImageView) this.findViewById(R.id.provider_iv_user_type);

        provider_details_findhe_btn = (Button) this.findViewById(R.id.provider_details_findhe_btn);
        provider_details_findhe_btn.setOnClickListener(this);


        provider_details_zanrenpin_btn = (Button) this.findViewById(R.id.provider_details_zanrenpin_btn);
        provider_details_zanrenpin_btn.setOnClickListener(this);

        provider_details_tags_add = this.findViewById(R.id.provider_details_tags_add);
        provider_details_tags_add.setOnClickListener(new MyAddTagsOnClickListener());

        provider_details_fflayout = (FilterFlowLayout) this.findViewById(R.id.provider_details_fflayout);

        panic_has_purchase_llay = (LinearLayout) this.findViewById(R.id.panic_has_purchase_llay);

        welfare_purchase_hasgoturs_lv = (LinearLayout) this.findViewById(R.id.welfare_purchase_hasgoturs_lv);

        root_view = (ScrollView) this.findViewById(R.id.root_view);

        provider_details_discuss_llay = (LinearLayout) this.findViewById(R.id.provider_details_discuss_llay);

        provider_details_discuss_lv = (xj.property.widget.MyListView) this.findViewById(R.id.provider_details_discuss_lv);


        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);

        ll_neterror.setOnClickListener(this);


        initPopupWindow();

        default_input_view = this.findViewById(R.id.default_input_view);

        bar_bottom = this.findViewById(R.id.bar_bottom);

        et_sendmessage = this.findViewById(R.id.et_sendmessage);

        defbtn_send = this.findViewById(R.id.defbtn_send);

        bar_bottom.setOnClickListener(new MyDefaultInputClickListener());

        default_input_view.setOnClickListener(new MyDefaultInputClickListener());

        defbtn_send.setOnClickListener(new MyDefaultInputClickListener());
        et_sendmessage.setFocusable(false);
        et_sendmessage.setOnClickListener(new MyDefaultInputClickListener());


    }

    class MyDefaultInputClickListener implements View.OnClickListener {


        @Override
        public void onClick(View v) {
            if (providerDetailsBean != null) {
                ProviderEvaEvent currentEvent = new ProviderEvaEvent(
                        userInfoDetailBean.getEmobId(),
                        providerDetailsBean.getEmobId(),
                        providerDetailsBean.getNickname(),
                        providerDetailsBean.getCooperationId(), "");

                EventBus.getDefault().post(currentEvent);

            }
        }
    }


    /**
     * 初始化评论条
     */
    private void initPopupWindow() {
        View bottomview = View.inflate(this, R.layout.common_cooperation_discuss_bottom_input, null);

        popupWindow = new PopupWindow(bottomview, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        ll_face_container = (LinearLayout) bottomview.findViewById(R.id.ll_face_container);

        vPager = (ViewPager) bottomview.findViewById(R.id.vPager);
        vpager_indicator = (CirclePageIndicator) bottomview.findViewById(R.id.vpager_indicator);

        // 表情list 目前表情数量为99个
        reslist = getExpressionRes(99);

        // 初始化表情viewpager
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        View gv3 = getGridChildView(3);
        View gv4 = getGridChildView(4);
        View gv5 = getGridChildView(5);

        views.add(gv1);
        views.add(gv2);
        views.add(gv3);
        views.add(gv4);
        views.add(gv5);

        vPager.setAdapter(new ExpressionPagerAdapter(views));
        vpager_indicator.setViewPager(vPager);

        send_img_checkbox = (CheckBox) bottomview.findViewById(R.id.send_img_checkbox);
        send_img_checkbox.setOnCheckedChangeListener(new onMyCheckedChangeListener());

        editText = (xj.property.widget.PasteEditText) bottomview.findViewById(R.id.et_sendmessage);

//        editText.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
////                edittext_layout.setBackgroundResource(R.drawable.login_line);
////                send_img_checkbox.setChecked(false);
//        send_img_checkbox.setButtonDrawable(getResources().getDrawable(R.drawable.chatting_biaoqing_btn_normal));
//        ll_face_container.setVisibility(View.GONE);
////                showKeyBoard();
//            }
//        });

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //强制显示软键盘
//                boolean bool = imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
                boolean bool = imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                if (bool) {
                    send_img_checkbox.setButtonDrawable(getResources().getDrawable(R.drawable.chatting_biaoqing_btn_normal));
                    ll_face_container.setVisibility(View.GONE);
                }
                return false;
            }
        });

        /// 编辑文字
//        editText = (EditText) bottomview.findViewById(R.id.et_sendmessage);
//        editText.setOnClickListener(this);
        /// 发送
        btnSend = (Button) bottomview.findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);
        //// 赞人品
        btnCharter = (Button) bottomview.findViewById(R.id.btn_charter);
        btnCharter.setOnClickListener(this);
        popupWindow.setAnimationStyle(R.style.Anim_style);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (editText != null) {
                    editText.setText("");
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                }
                if (bar_bottom != null) {
                    bar_bottom.setVisibility(View.VISIBLE);
                }
            }
        });

    }


    private class onMyCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                if (ll_face_container != null) {
                    ll_face_container.setVisibility(View.VISIBLE);
//                    closeBoard();
                    //// 强制隐藏输入法
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                }
            } else {
                if (ll_face_container != null) {
                    ll_face_container.setVisibility(View.GONE);
                    showKeyBoard();
                }
            }
        }
    }

    public void closeBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
        if (imm.isActive())  //一直是true
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 获取表情的gridview的子view
     *
     * @param i
     * @return
     */
    private View getGridChildView(int i) {
        View view = View.inflate(this, R.layout.expression_gridview, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();
        if (i == 1) {
            List<String> list1 = reslist.subList(0, 20);/// 20/27//34
            list.addAll(list1);
        } else if (i == 2) {
            list.addAll(reslist.subList(20, 40));
        } else if (i == 3) {
            list.addAll(reslist.subList(40, 60));
        } else if (i == 4) {
            list.addAll(reslist.subList(60, 80));
        } else if (i == 5) {
            list.addAll(reslist.subList(80, reslist.size()));
        }
        list.add("delete_expression");
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this, 1, list);
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = expressionAdapter.getItem(position);
                try {
                    if (filename != "delete_expression") { // 不是删除键，显示表情
                        // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                        Class clz = Class.forName("xj.property.utils.SmileUtils");

                        Field field = clz.getField(filename);
                        int selectionStart = editText.getSelectionStart();// 获取光标的位置
                        Spannable smiledText = SmileUtils.getSmiledText(getmContext(), (String) field.get(null));
                        Editable editableText = editText.getEditableText();
                        editableText.insert(selectionStart, smiledText);

//                        editText.append(SmileUtils.getSmiledText(getmContext(), (String) field.get(null)));

                    } else {
                        // 删除文字或者表情
                        if (!TextUtils.isEmpty(editText.getText())) {

                            int selectionStart = editText.getSelectionStart();// 获取光标的位置

                            if (selectionStart > 0) {

                                String body = editText.getText().toString();

                                String tempStr = body.substring(0, selectionStart);

                                int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                if (i != -1) {
                                    /// 截取最后一个表情
                                    CharSequence cs = tempStr.substring(i, selectionStart);

                                    if (SmileUtils.containsKey(cs.toString()))
                                        /// 删除最后一个表情字符串的占位符
                                        editText.getEditableText().delete(i, selectionStart);
                                    else
                                        editText.getEditableText().delete(selectionStart - 1, selectionStart);
                                } else {
                                    editText.getEditableText().delete(selectionStart - 1, selectionStart);
                                }
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        return view;
    }

    public List<String> getExpressionRes(int getSum) {
        List<String> reslist = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "ee_" + x;

            reslist.add(filename);

        }
        return reslist;

    }


    private ProviderEvaEvent currentEvent;

    public void onEvent(ProviderEvaEvent event) {

        if (event != null) {

            currentEvent = event;

            if (userInfoDetailBean != null) {

                bar_bottom.setVisibility(View.INVISIBLE);

                if (event.getTo() == null && event.getFromNike() == null) {
                    Log.i("debbug", "debbugnull");
                    return;
                }

                editText.setHint("回复:" + event.getFromNike());

                if (event.getCooperationId() == 0) {
                    btnCharter.setVisibility(View.GONE);
                } else {
                    btnCharter.setVisibility(View.GONE);
                }

                popupWindow.showAtLocation(root_view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                if (ll_face_container != null) {
                    ll_face_container.setVisibility(View.GONE);
//                    send_img_checkbox.setChecked(false);
                }
                showKeyBoard();

            } else {
                Intent intent = new Intent(this, RegisterLoginActivity.class);
                startActivityForResult(intent, Config.TASKCOMPLETE);
            }

        }

    }


    public void onEventMainThread(NetworkStateChangeEvent networkStateChangeEvent) {

        if (networkStateChangeEvent != null && networkStateChangeEvent.isConnected()) {

            Log.d("onEventNetworkStatusChange ", "" + networkStateChangeEvent.isConnected());

            if (!isDataLoadComplete) {

                Log.d("isDataLoadComplete ", "" + isDataLoadComplete + "network is ok , reconnect bangbi info ");
                initData();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMSsoHandler ssoHandler = ShareProvider.mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    public void showKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void initTitle() {
        this.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("邻居帮详情");
        ((ImageView) findViewById(R.id.iv_right)).setVisibility(View.VISIBLE);
        ((ImageView) findViewById(R.id.iv_right)).setBackgroundDrawable(getResources().getDrawable(R.drawable.share));
        ((ImageView) findViewById(R.id.iv_right)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = Config.NET_SHAREBASE + "/share/neighbor.html?communityId=" + userInfoDetailBean.getCommunityId() + "&cooperationId=" + cooperationId;
                ShareProvider.getShareProvider(ProviderDetailsActivity.this).showShareActivity(url, "这位是我的热心邻居“" + providerDetailsBean.getNickname() + "”，有需要帮忙的可以找Ta！", "邻居帮帮", ShareProvider.CODE_NEIGHBOR, providerDetailsBean.getAvatar());
            }
        });
    }

    @Override
    public void onTagsA2BAddSuccess(String message) {
        initData();
    }

    @Override
    public void onTagsA2BAddFail(String message) {
        showToast(message);
    }

    interface NeighborDetailsService {

        @GET("/api/v3/cooperations/{cooperationId}/details")
        void getNeighborMessage(@Path("cooperationId") String cooperationId, Callback<CommonRespBean<NeighborDetailsV3Bean>> cb);

        ///api/v1/communities/{communityId}/circles/{emobId}

        @POST("/api/v3/communities/{communityId}/users")
        void lookedDetail(@Path("communityId") int communityId, @Body LookedDetailsReqBean lifeEvaBean, Callback<CommonRespBean> cb);
    }

    /**
     * 添加看过他部分
     *
     * @param lifeEvaBean
     */
    public  void addlookedDetails(LookedDetailsReqBean lifeEvaBean) {
        Log.i("onion", "lifeEvaBean" + lifeEvaBean);
        NeighborDetailsService service = RetrofitFactory.getInstance().create(getmContext(),lifeEvaBean,NeighborDetailsService.class);
        Callback<CommonRespBean> callback = new Callback<CommonRespBean>() {
            @Override
            public void success(CommonRespBean bean, Response response) {
                if ("yes".equals(bean.getStatus())) {
                    Log.d("addlookedDetails ", "success yes ");
                } else {
                    Log.d("addlookedDetails ", "success no ");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Log.d("addlookedDetails ", "failure " + error.getMessage());
            }
        };
        service.lookedDetail( PreferencesUtil.getCommityId(XjApplication.getInstance()), lifeEvaBean, callback);
    }

    //// 邻居帮详情获取
    private void getProviderDetails() {


//        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
//        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
//        MspCardListService service = restAdapter.create(MspCardListService.class);
        NeighborDetailsService service = RetrofitFactory.getInstance().create(getmContext(),NeighborDetailsService.class);
        Callback<CommonRespBean<NeighborDetailsV3Bean>> callback = new Callback<CommonRespBean<NeighborDetailsV3Bean>>() {
            @Override
            public void success(CommonRespBean<NeighborDetailsV3Bean> respBean, Response response) {

                if (respBean != null && TextUtils.equals("yes", respBean.getStatus())) {

                    providerDetailsBean = respBean.getData();

                    addLooked(providerDetailsBean);

                    initProviderDetail(respBean.getData());
                    getUserTagsInfo(respBean.getData().getEmobId());


                } else if (respBean != null && TextUtils.equals("no", respBean.getStatus())) {
                    Toast.makeText(getmContext(), respBean.getMessage(), Toast.LENGTH_SHORT).show();
                    setNetworkError();
                } else {
                    Toast.makeText(getmContext(), "数据异常", Toast.LENGTH_SHORT).show();
                    setNetworkError();
                }
                if (mLdDialog != null) {
                    mLdDialog.dismiss();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();

                setNetworkError();
                showNetErrorToast();

                if (mLdDialog != null) {
                    mLdDialog.dismiss();
                }
            }
        };

        if (mLdDialog != null) {
            mLdDialog.show();
        }

        service.getNeighborMessage(cooperationId, callback);

    }


    public void setNetworkError() {
        isDataLoadComplete = false;
        if (ll_errorpage != null) {

            ll_errorpage.setVisibility(View.VISIBLE);
            ll_neterror.setVisibility(View.VISIBLE);
        }
        if (bar_bottom != null) {
            bar_bottom.setVisibility(View.GONE);
        }
    }

    public void setNetworkOk() {
        isDataLoadComplete = true;
        if (ll_errorpage != null) {

            ll_errorpage.setVisibility(View.GONE);
            ll_neterror.setVisibility(View.GONE);
        }
        if (bar_bottom != null) {
            bar_bottom.setVisibility(View.VISIBLE);
        }

    }


    private void addLooked(final NeighborDetailsV3Bean providerDetailsBean) {
        if (TextUtils.equals(providerDetailsBean.getEmobId(), userInfoDetailBean.getEmobId())) {
            Log.d("addLooked ", "addLooked  TextUtils.equals(providerDetailsBean.getEmobId(),userInfoDetailBean.getEmobId()) ");
            return;
        }

        long randomDelay = new Random(5).nextInt(3) * 1000;
        Log.d("randomDelay ", "randomDelay  " + randomDelay);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                LookedDetailsReqBean lookedDetailsReqBean = new LookedDetailsReqBean();
                lookedDetailsReqBean.setAvatar(userInfoDetailBean.getAvatar());
                lookedDetailsReqBean.setEmobId(userInfoDetailBean.getEmobId());
                lookedDetailsReqBean.setNickname(userInfoDetailBean.getNickname());
                lookedDetailsReqBean.setCommuntityId(providerDetailsBean.getCooperationId());
                addlookedDetails( lookedDetailsReqBean);

            }
        }, randomDelay);
    }

    /**
     * 初始化邻居帮信息
     *
     * @param respBean
     */
    private void initProviderDetail(final NeighborDetailsV3Bean respBean) {

        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        if (provider_details_findhe_btn != null) {
            if (TextUtils.equals(userInfoDetailBean.getEmobId(), respBean.getEmobId())) {

                provider_details_findhe_btn.setVisibility(View.GONE);
            } else {
                provider_details_findhe_btn.setVisibility(View.VISIBLE);
            }
        }
        if (provider_details_time_tv != null) {
            Date tag_time_date = new Date(respBean.getCreateTime() * 1000L);
            provider_details_time_tv.setText(format.format(tag_time_date));
        }

        if (provider_details_name_tv != null) {
            provider_details_name_tv.setText(respBean.getNickname());
        }

        if (provider_details_title_tv != null) {
            provider_details_title_tv.setText(respBean.getTitle());
        }

        if (provider_details_content_tv != null) {

            Spannable spanAll = SmileUtils.getSmiledText(getmContext(), respBean.getContent());
            provider_details_content_tv.setText(spanAll, TextView.BufferType.SPANNABLE);
//            provider_details_content_tv.setText(respBean.getInfo().getContent());
        }

        if (provider_details_iv_avtar != null) {
            ImageLoader.getInstance().displayImage(respBean.getAvatar(), provider_details_iv_avtar, UserUtils.options);
            //// 2015/11/19 添加帮主头衔
            initBangzhuMedal(respBean.getGrade());
            provider_details_iv_avtar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    userInfoDetailBean = PreferencesUtil.getLoginInfo(getmContext());
                    if (userInfoDetailBean != null) {
                        //// 非自己才可以进入查看该人信息
                        if (!TextUtils.equals(respBean.getEmobId(), userInfoDetailBean.getEmobId())) {
                            getmContext().startActivity(new Intent(getmContext(), UserGroupInfoActivity.class).putExtra(Config.INTENT_PARMAS2, respBean.getEmobId()));
                        }
                    }

                }
            });
        }


        /// 查看过他的人
        loadingGoodsHasGotursHeadImgs4(respBean);


        /// 加载评论列表
        initDiscussList(respBean.getCooperationDetails());
    }


    /**
     * 初始化用户横条奖章图片
     */
    private void initBangzhuMedal(String userType) {
        if (provider_iv_user_type != null) {
//            normal , bangzhu, fubangzhu ,zhanglao,bangzhong
            if (TextUtils.equals(userType, Config.USER_TYPE_ZHANGLAO)) {
                provider_iv_user_type.setImageDrawable(getResources().getDrawable(R.drawable.life_circle_zhanglao_icon));
                provider_iv_user_type.setVisibility(View.VISIBLE);
            } else if (TextUtils.equals(userType, Config.USER_TYPE_BANGZHU)) {
                provider_iv_user_type.setImageDrawable(getResources().getDrawable(R.drawable.life_circle_bangzhu_icon));
                provider_iv_user_type.setVisibility(View.VISIBLE);
            } else if (TextUtils.equals(userType, Config.USER_TYPE_FUBANGZHU)) {
                provider_iv_user_type.setImageDrawable(getResources().getDrawable(R.drawable.life_circle_fubangzhu_icon));
                provider_iv_user_type.setVisibility(View.VISIBLE);
            } else {
                provider_iv_user_type.setVisibility(View.GONE);
            }
        }
    }


    private List<NeighborDetailsV3Bean.NeighborDetailsCooperationV3Bean> cooperationDetailsEntityList;

    private  MyAdapter myAdapter = new MyAdapter();

    /// 加载评论列表
    private void initDiscussList(List<NeighborDetailsV3Bean.NeighborDetailsCooperationV3Bean> cooperationDetails) {
        cooperationDetailsEntityList = cooperationDetails;
        if (cooperationDetailsEntityList != null && cooperationDetailsEntityList.size() > 0) {

            if (provider_details_discuss_lv != null) {

                provider_details_discuss_lv.setAdapter(myAdapter);

//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        ViewUtils.setListViewHeightBasedOnChildren(provider_details_discuss_lv);
//                    }
//                });


//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (root_view != null) {
//                            root_view.smoothScrollTo(0, 0);
//                        }
//                    }
//                });
            }

            provider_details_discuss_llay.setVisibility(View.VISIBLE);
        } else {
            provider_details_discuss_llay.setVisibility(View.GONE);
        }
    }


    /**
     * 加载已经kan过的用户
     */
    private void loadingGoodsHasGotursHeadImgs4(final NeighborDetailsV3Bean circleBean) {

        final List<NeighborDetailsV3Bean.NeighborDetailsUserV3Bean> users = circleBean.getUsers();

        if (users == null || users.size() <= 0) {

            if (panic_has_purchase_llay != null) {
                panic_has_purchase_llay.setVisibility(View.GONE);
            }
            return;
        }

        if (panic_has_purchase_llay != null) {
            panic_has_purchase_llay.setVisibility(View.VISIBLE);
        }

        Log.i("debbug", "info.size" + users.size());

        if (users.size() > 6) {
            welfare_purchase_hasgoturs_lv.removeAllViews();

            for (int i = 0; i < 5; i++) {

                LinearLayout usrHeadView = (LinearLayout) View.inflate(getmContext(), R.layout.common_cooperation_details_moreurs_headlay, null);
                ImageView img = (ImageView) usrHeadView.findViewById(R.id.iv_avatar);
                img.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(users.get(i).getAvatar(), img, UserUtils.options);

                TextView welfare_purchase_hasgoturs_name_tv = (TextView) usrHeadView.findViewById(R.id.welfare_purchase_hasgoturs_name_tv);
                welfare_purchase_hasgoturs_name_tv.setText(users.get(i).getNickname());
                welfare_purchase_hasgoturs_name_tv.setVisibility(View.VISIBLE);

                usrHeadView.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams rlparams = (LinearLayout.LayoutParams) img.getLayoutParams();

                rlparams.width = screenWidth * 123 / 1080;
                rlparams.height = screenWidth * 123 / 1080;

                img.setLayoutParams(rlparams);

                final int finalI = i;
                usrHeadView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getmContext().startActivity(new Intent(getmContext(), UserGroupInfoActivity.class).putExtra(Config.INTENT_PARMAS2, users.get(finalI).getEmobId()));
                    }
                });

//                LinearLayout.LayoutParams llayparams = (LinearLayout.LayoutParams) viewHolder.welfare_purchase_hasgoturs_lv.getLayoutParams();
//                viewHolder.welfare_purchase_hasgoturs_lv.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                welfare_purchase_hasgoturs_lv.addView(usrHeadView);

            }

            /// 添加一个查看更多用户按钮

            LinearLayout usrHeadView = (LinearLayout) View.inflate(getmContext(), R.layout.common_cooperation_details_moreurs_headlay, null);
            ImageView img = (ImageView) usrHeadView.findViewById(R.id.iv_avatar);
            img.setVisibility(View.VISIBLE);

            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.help_more, img);

            TextView welfare_purchase_hasgoturs_name_tv = (TextView) usrHeadView.findViewById(R.id.welfare_purchase_hasgoturs_name_tv);
            welfare_purchase_hasgoturs_name_tv.setVisibility(View.INVISIBLE);

            usrHeadView.setVisibility(View.VISIBLE);

            LinearLayout.LayoutParams rlparams = (LinearLayout.LayoutParams) img.getLayoutParams();

            rlparams.width = screenWidth * 123 / 1080;
            rlparams.height = screenWidth * 123 / 1080;

            img.setLayoutParams(rlparams);
            usrHeadView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getmContext().startActivity(new Intent(getmContext(), CooperationProviderDetailsVisitMoreActivity.class).putExtra("cooperationId", "" + circleBean.getCooperationId()));

                }
            });
//
//            LinearLayout.LayoutParams llayparams = (LinearLayout.LayoutParams) viewHolder.welfare_purchase_hasgoturs_lv.getLayoutParams();
//            viewHolder.welfare_purchase_hasgoturs_lv.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            welfare_purchase_hasgoturs_lv.addView(usrHeadView);
        }

        if (users.size() > 0 && users.size() <= 6) {
            welfare_purchase_hasgoturs_lv.removeAllViews();
            for (int i = 0; i < users.size(); i++) {

                LinearLayout usrHeadView = (LinearLayout) View.inflate(getmContext(), R.layout.common_cooperation_details_moreurs_headlay, null);
                ImageView img = (ImageView) usrHeadView.findViewById(R.id.iv_avatar);
                img.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(users.get(i).getAvatar(), img, UserUtils.options);

                TextView welfare_purchase_hasgoturs_name_tv = (TextView) usrHeadView.findViewById(R.id.welfare_purchase_hasgoturs_name_tv);
                welfare_purchase_hasgoturs_name_tv.setText(users.get(i).getNickname());
                welfare_purchase_hasgoturs_name_tv.setVisibility(View.VISIBLE);

                usrHeadView.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams rlparams = (LinearLayout.LayoutParams) img.getLayoutParams();

                rlparams.width = screenWidth * 123 / 1080;
                rlparams.height = screenWidth * 123 / 1080;

                img.setLayoutParams(rlparams);

                final int finalI = i;
                usrHeadView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getmContext().startActivity(new Intent(getmContext(), UserGroupInfoActivity.class).putExtra(Config.INTENT_PARMAS2, users.get(finalI).getEmobId()));
                    }
                });

//                LinearLayout.LayoutParams llayparams = (LinearLayout.LayoutParams) viewHolder.welfare_purchase_hasgoturs_lv.getLayoutParams();
//                viewHolder.welfare_purchase_hasgoturs_lv.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                welfare_purchase_hasgoturs_lv.addView(usrHeadView);

            }
        }
    }

    /// 用户标签获取
    public void getUserTagsInfo(final String emobid) {
        NetBaseTagUtils.getUserTagsInfo(getmContext(),emobid, new NetBaseTagUtils.NetRespListener<CommonRespBean<List<MyTagsRespBean.InfoEntity>>>() {
            @Override
            public void success(CommonRespBean<List<MyTagsRespBean.InfoEntity>> bean, Response response) {
                if (TextUtils.equals("yes", bean.getStatus())) {
                    List<MyTagsRespBean.InfoEntity> info = bean.getData();
                    initMyTags(getmContext(), info, emobid);
                    setNetworkOk();
                } else {
                    setNetworkError();
                    systemDefaulttags = PreferencesUtil.getNewSysTags(getmContext(), userInfoDetailBean.getEmobId());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                setNetworkError();
            }
        });
    }

    private void initMyTags(final Context context, final List<MyTagsRespBean.InfoEntity> info, final String emobid) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if (provider_details_fflayout != null) {
            provider_details_fflayout.removeAllViews();
            View common_tags_item = inflater.inflate(R.layout.common_tags_add_item, null);
            common_tags_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    userInfoDetailBean = PreferencesUtil.getLoginInfo(getmContext());
                    String localemobId = userInfoDetailBean.getEmobId();
                    /// 自己给自己添加标签
                    if (TextUtils.equals(localemobId, providerDetailsBean.getEmobId())) {
                        myTagsManagerDialog2 = new MyOwnerTagsManagerDialog(getmContext(),
                                systemDefaulttags,
                                "" + userInfoDetailBean.getCommunityId(),
                                userInfoDetailBean.getEmobId(),
                                userInfoDetailBean.getEmobId(),
                                ProviderDetailsActivity.this);
                        WindowManager windowManager = getWindowManager();
                        Display display = windowManager.getDefaultDisplay();
                        WindowManager.LayoutParams lp = myTagsManagerDialog2.getWindow().getAttributes();
                        lp.width = (int) (display.getWidth()); //设置宽度
                        myTagsManagerDialog2.getWindow().setAttributes(lp);
                        myTagsManagerDialog2.show();
                        /// 自己给别人添加标签
                    } else {

                        myTagsManagerDialog = new MyTagsManagerDialog(getmContext(), systemDefaulttags, "" + userInfoDetailBean.getCommunityId(), userInfoDetailBean.getEmobId(), providerDetailsBean.getEmobId(), ProviderDetailsActivity.this);
                        myTagsManagerDialog.show();
//            WindowManager windowManager = getWindowManager();
//            Display display = windowManager.getDefaultDisplay();
                        WindowManager.LayoutParams lp = myTagsManagerDialog.getWindow().getAttributes();
//            lp.width = (int) (display.getWidth()); //设置宽度
                        lp.width = screenWidth;
                        myTagsManagerDialog.getWindow().setAttributes(lp);

                    }
                }
            });
            provider_details_fflayout.addView(common_tags_item);

        }

        for (final MyTagsRespBean.InfoEntity bean : info) {

            View common_tags_item = inflater.inflate(R.layout.common_tags_item, null);
            TextView common_tags_name_tv = (TextView) common_tags_item.findViewById(R.id.common_tags_name_tv);
            TextView common_tags_nums_tv = (TextView) common_tags_item.findViewById(R.id.common_tags_nums_tv);
            common_tags_name_tv.setText(bean.getLabelContent());
            common_tags_nums_tv.setText(bean.getCount());
            common_tags_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context, ActivityMyTagsList.class);
//                    intent.putExtra("tagContent", bean.getLabelContent());
//                    intent.putExtra("tagEmobId", emobid);
//                    startActivity(intent);
//                    不能点击标签查看谁给对方添加了标签    2015/11/17
                }
            });

            provider_details_fflayout.addView(common_tags_item);
        }
    }


    ///// 获取系统默认Tags

    private void getSystemDefaultTags() {
        HashMap<String, String> option = new HashMap<>();
        option.put("communityId", userInfoDetailBean.getCommunityId() + "");
        option.put("time", "" + PreferencesUtil.getLastReqTagsTime(getmContext()));
        NetBaseTagUtils.getSysDefaultTags(userInfoDetailBean.getEmobId(),getApplicationContext(),option, new NetBaseTagUtils.NetRespListener<CommonRespBean<SysDefaultTagsV3Bean>>() {
            @Override
            public void success(CommonRespBean<SysDefaultTagsV3Bean> tagsbean, Response response) {
                if (tagsbean != null) {
                    if (TextUtils.equals("yes", tagsbean.getStatus())) {
                        systemDefaulttags = tagsbean.getData().getList();
                        if (systemDefaulttags == null || systemDefaulttags.isEmpty() || systemDefaulttags.size() < 1) {
                            systemDefaulttags = PreferencesUtil.getNewSysTags(getmContext(), userInfoDetailBean.getEmobId());
                        }
                    } else {
                        systemDefaulttags = PreferencesUtil.getNewSysTags(getmContext(), userInfoDetailBean.getEmobId());
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }


    private class MyAdapter extends BaseAdapter {

        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");

        @Override
        public int getCount() {
            return cooperationDetailsEntityList.size();
        }

        @Override
        public Object getItem(int position) {
            return cooperationDetailsEntityList;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;

            if (convertView == null) {
                convertView = View.inflate(getmContext(), R.layout.common_cooperation_details_discuss_item, null);

                viewHolder = new ViewHolder(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Log.i("debbug", "size=" + cooperationDetailsEntityList.size());

//            Log.i("debbug", "viewHolder=" + viewHolder);

            final NeighborDetailsV3Bean.NeighborDetailsCooperationV3Bean cooperationDetailsEntity = cooperationDetailsEntityList.get(position);

            viewHolder.whotags_me_name_from_tv.setText(cooperationDetailsEntity.getNicknameFrom());
            viewHolder.whotags_me_name_from_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//
//                    public ProviderEvaEvent(String from, String to, String fromNike, int cooperationId, String content) {
//                        this.from = from;
//                        this.to = to;
//                        this.fromNike = fromNike;
//                        this.cooperationId = cooperationId;
//                        this.content = content;
//                    }

                    //// 邻居帮详情 Event发送  cooperationId , content 没有value
                    ProviderEvaEvent providerEvaEvent = new ProviderEvaEvent(userInfoDetailBean.getEmobId(), cooperationDetailsEntity.getEmobIdFrom(), cooperationDetailsEntity.getNicknameFrom(), -1, "");
                    EventBus.getDefault().post(providerEvaEvent);

                }
            });


            if (TextUtils.equals(cooperationDetailsEntity.getEmobIdTo(), providerDetailsBean.getEmobId())) {

                viewHolder.whodiscuss_me_to_tv.setVisibility(View.GONE);
                viewHolder.whodiscuss_me_reply_tv.setVisibility(View.GONE);
            } else {
                viewHolder.whodiscuss_me_to_tv.setVisibility(View.VISIBLE);
                viewHolder.whodiscuss_me_reply_tv.setVisibility(View.VISIBLE);

                viewHolder.whodiscuss_me_to_tv.setText(cooperationDetailsEntity.getNicknameTo());
                viewHolder.whodiscuss_me_to_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ProviderEvaEvent providerEvaEvent = new ProviderEvaEvent(userInfoDetailBean.getEmobId(), cooperationDetailsEntity.getEmobIdTo(), cooperationDetailsEntity.getNicknameTo(), -1, "");
                        EventBus.getDefault().post(providerEvaEvent);
                    }
                });
            }

            Spannable spanAll = SmileUtils.getSmiledText(getmContext(), cooperationDetailsEntity.getDetailContent());

            viewHolder.discuss_content_tv.setText(spanAll, TextView.BufferType.SPANNABLE);

//            viewHolder.discuss_content_tv.setText(cooperationDetailsEntity.getDetailContent());

            ImageLoader.getInstance().displayImage(cooperationDetailsEntity.getAvatarFrom(), viewHolder.iv_avatar, UserUtils.options);

            //// 2015/11/19 添加帮内头衔
            initBangzhuMedal(cooperationDetailsEntity.getGradeFrom(), viewHolder);

            viewHolder.iv_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    Log.d("debug ", "adapter.getCount" + myAdapter.getCount() + " pageData.size(" + cooperationDetailsEntityList.size() + "position " + position);

                    userInfoDetailBean = PreferencesUtil.getLoginInfo(getmContext());
                    if (userInfoDetailBean != null) {
                        //// 非自己才可以进入查看该人信息
                        if (!TextUtils.equals(cooperationDetailsEntity.getEmobIdFrom(), userInfoDetailBean.getEmobId())) {

                            startActivity(new Intent(getmContext(), UserGroupInfoActivity.class).putExtra(Config.INTENT_PARMAS2, cooperationDetailsEntity.getEmobIdFrom()));
                        }
                    }

                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //// 邻居帮详情 Event发送  cooperationId , content 没有value
                    ProviderEvaEvent providerEvaEvent = new ProviderEvaEvent(userInfoDetailBean.getEmobId(), cooperationDetailsEntity.getEmobIdFrom(), cooperationDetailsEntity.getNicknameFrom(), -1, "");
                    EventBus.getDefault().post(providerEvaEvent);

                }
            });

            return convertView;
        }

        /**
         * 初始化用户横条奖章图片
         */
        private void initBangzhuMedal(String userType, ViewHolder viewHolder) {
            if (viewHolder.iv_user_type != null) {
//            normal , bangzhu, fubangzhu ,zhanglao,bangzhong
                if (TextUtils.equals(userType, "zhanglao")) {
                    viewHolder.iv_user_type.setImageDrawable(getResources().getDrawable(R.drawable.life_circle_zhanglao_icon));
                    viewHolder.iv_user_type.setVisibility(View.VISIBLE);
                } else if (TextUtils.equals(userType, "bangzhu")) {
                    viewHolder.iv_user_type.setImageDrawable(getResources().getDrawable(R.drawable.life_circle_bangzhu_icon));
                    viewHolder.iv_user_type.setVisibility(View.VISIBLE);
                } else if (TextUtils.equals(userType, "fubangzhu")) {
                    viewHolder.iv_user_type.setImageDrawable(getResources().getDrawable(R.drawable.life_circle_fubangzhu_icon));
                    viewHolder.iv_user_type.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.iv_user_type.setVisibility(View.GONE);
                }
            }
        }


        class ViewHolder {

            ImageView iv_avatar;

            ImageView iv_user_type;

            TextView whotags_me_name_from_tv;

            TextView whodiscuss_me_reply_tv;

            TextView whodiscuss_me_to_tv;

            TextView discuss_content_tv;

            ViewHolder(View v) {

                iv_avatar = (ImageView) v.findViewById(R.id.iv_avatar);

                iv_user_type = (ImageView) v.findViewById(R.id.iv_user_type);

                whotags_me_name_from_tv = (TextView) v.findViewById(R.id.whotags_me_name_from_tv);

                whodiscuss_me_reply_tv = (TextView) v.findViewById(R.id.whodiscuss_me_reply_tv);

                whodiscuss_me_to_tv = (TextView) v.findViewById(R.id.whodiscuss_me_to_tv);

                discuss_content_tv = (TextView) v.findViewById(R.id.discuss_content_tv);

                v.setTag(this);
            }
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.provider_details_findhe_btn:

                launchChatActivity();

                break;

            case R.id.provider_details_zanrenpin_btn:

                break;

            case R.id.ll_errorpage:

                initData();

                break;

            case R.id.btn_send:

                final String content = editText.getText().toString();
                if (content == null || TextUtils.isEmpty(content)) {
                    showToast("请输入评论内容");
                    return;
                }

                mLdDialog.show();
                CooperationUtil.eva(getmContext(),userInfoDetailBean.getEmobId(), currentEvent.getTo(), content, providerDetailsBean.getCooperationId(),PreferencesUtil.getCommityId(getmContext()), new Handler() {
                    @Override
                    public void handleMessage(Message msg) {

                        switch (msg.what) {
                            case Config.NETERROR:
                                showNetErrorToast();
                                break;
                            case Config.TASKCOMPLETE:
                                showToast("评论成功");

//                                List<LifeCircleDetail> list = map.get(currentEvent.getCircleLifeId()).getLifeCircleDetails();
//                                int time = (int) (new Date().getTime() / 1000);
//                                list.add(new LifeCircleDetail(msg.arg1, userInfoDetailBean.getEmobId(), currentEvent.getFrom(), userInfoDetailBean.getNickname(), currentEvent.getFromNike(), 0, time, time, currentEvent.getCircleLifeId(), content));
                                NeighborDetailsV3Bean neighborDetailsV3Bean = new NeighborDetailsV3Bean();
                                NeighborDetailsV3Bean.NeighborDetailsCooperationV3Bean cooperationDetailsEntity = neighborDetailsV3Bean.new NeighborDetailsCooperationV3Bean();
                                cooperationDetailsEntity.setEmobIdFrom(userInfoDetailBean.getEmobId());
                                cooperationDetailsEntity.setEmobIdTo(currentEvent.getTo());
                                cooperationDetailsEntity.setAvatarFrom(userInfoDetailBean.getAvatar());
                                cooperationDetailsEntity.setDetailContent(content);
                                cooperationDetailsEntity.setNicknameFrom(userInfoDetailBean.getNickname());
                                cooperationDetailsEntity.setNicknameTo(currentEvent.getFromNike());
                                cooperationDetailsEntity.setGradeFrom(userInfoDetailBean.getGrade());

                                if (cooperationDetailsEntityList != null && cooperationDetailsEntityList.size() > 0) {
                                    cooperationDetailsEntityList.add(cooperationDetailsEntity);
                                    ///TODO  刷新数据
                                    myAdapter.notifyDataSetChanged();
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (root_view != null) {
//                                            root_view.smoothScrollTo(0, 0);
                                                root_view.fullScroll(ScrollView.FOCUS_DOWN);
                                            }
                                        }
                                    });

                                } else {
                                    getProviderDetails();
                                }

//                                getProviderDetails();


                                break;
                            case Config.TASKERROR:
                                showToast("评论失败");
                                break;
                        }

                        bar_bottom.setVisibility(View.VISIBLE);
                        mLdDialog.dismiss();
                    }
                });
                editText.getText().clear();
                popupWindow.dismiss();
                break;

        }
    }

    private void launchChatActivity() {

        if (providerDetailsBean != null) {
            XJContactHelper.saveContact(providerDetailsBean.getEmobId(), providerDetailsBean.getNickname(), providerDetailsBean.getAvatar(), "-1");
            Intent intentPush = new Intent();
            intentPush.putExtra("userId", providerDetailsBean.getEmobId());//tz
            intentPush.putExtra(Config.EXPKey_nickname, providerDetailsBean.getNickname());
            intentPush.putExtra(Config.EXPKey_avatar, providerDetailsBean.getAvatar());
            intentPush.setClass(getmContext(), ChatActivity.class);
            intentPush.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
            getmContext().startActivity(intentPush);
        } else {
            Log.d("provider_details_findhe_btn ", "circleBean  is " + providerDetailsBean);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);

        if (mLdDialog != null) {
            mLdDialog.dismiss();
        }
        if (myTagsManagerDialog != null) {
            myTagsManagerDialog.dismiss();
        }

        if (myTagsManagerDialog2 != null) {
            myTagsManagerDialog2.dismiss();
        }
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }


    private class MyAddTagsOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            if (providerDetailsBean != null) {
                myTagsManagerDialog = new MyTagsManagerDialog(getmContext(), systemDefaulttags, "" + userInfoDetailBean.getCommunityId(), userInfoDetailBean.getEmobId(), providerDetailsBean.getEmobId(), ProviderDetailsActivity.this);
                myTagsManagerDialog.show();
//            WindowManager windowManager = getWindowManager();
//            Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = myTagsManagerDialog.getWindow().getAttributes();
//            lp.width = (int) (display.getWidth()); //设置宽度
                lp.width = screenWidth;
                myTagsManagerDialog.getWindow().setAttributes(lp);

            } else {
                showNetErrorToast();
            }


        }
    }


}
