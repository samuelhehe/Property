package xj.property.activity.vote;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.sso.UMSsoHandler;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.ChatActivity;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.adapter.ExpressionAdapter;
import xj.property.adapter.ExpressionPagerAdapter;
import xj.property.beans.UserInfoDetailBean;
import xj.property.beans.VoteDetailsRespBean;
import xj.property.beans.VoteGoReqBean;
import xj.property.beans.VoteOptionsListEntity;
import xj.property.event.NetworkStateChangeEvent;
import xj.property.event.NewVotedBackEvent;
import xj.property.event.ProviderEvaEvent;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.provider.ShareProvider;
import xj.property.utils.DensityUtil;
import xj.property.utils.SmileUtils;
import xj.property.utils.TimeUtils;
import xj.property.utils.message.XJContactHelper;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;
import xj.property.utils.other.VoteDetailsUtil;
import xj.property.widget.ExpandGridView;
import xj.property.widget.com.viewpagerindicator.CirclePageIndicator;

/**
 * 投票详情
 */
public class VoteDetailsActivity extends HXBaseActivity implements MyVoteConfirmDialog.onDoVoteStatusListener {

    private LinearLayout ll_errorpage;
    private LinearLayout ll_neterror;

    private LayoutInflater layoutInflator;

    private String[] colorArray = {"#54C7C0", "#EEB355", "#FEADFF", "#60AFE6"};

    /// 默认显示4行
    private static final int DEFAULT_SHOW_LINES = 4;
    //// 实时显示的行数
    private int showLines = DEFAULT_SHOW_LINES;

    private int maxLines = 0;

    private UserInfoDetailBean userInfoDetailBean;

    private TextView tv_title;

    //// 时间
    private TextView vote_index_item_time_tv;

    /// 用户头像
    private ImageView provider_details_iv_avtar;

    /// 用户昵称
    private TextView provider_details_name_tv;
    /// XXX 人已投票
    private TextView vote_detail_xxhasvote_tv;

    /// 找过他的邻居
    private LinearLayout panic_has_purchase_llay;

    /// 更多用户
    private LinearLayout welfare_purchase_hasgoturs_lv;

    /// 加载评论列表
    private xj.property.widget.MyListView provider_details_discuss_lv;

    /// 标签添加对话框
    private MyVoteConfirmDialog myTagsManagerDialog;


    private String voteId;
    /// 评论条
    private PopupWindow popupWindow;

    private EditText editText;

    private Button btnSend;

    private Button btnCharter;

    private ScrollView root_view;

    private VoteDetailsRespBean providerDetailsBean;

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

    /// 投票宣言
    private TextView vote_detail_vc_tv;
    /// 投票结果
    private LinearLayout vote_detail_result_llay;
    /// 投票选项
    private LinearLayout vote_detail_list_llay;
    /// 投票按钮
    private Button vote_detail_govote_btn;

    private LinearLayout vote_index_item_rcontent_llay;

    private LinearLayout vote_detail_item_result_sh_btn;
    //// 投票选项组
    private RadioGroup vote_detail_list_rg;

    /// 投票选中的ID
    private int votecheckedId = -1;
    /// 投票选中的RadioButton
    private RadioButton voteCheckedRadioButton = null;
    //分享类
    private ShareProvider mShareProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_details);
        userInfoDetailBean = PreferencesUtil.getLoginInfo(this);
        layoutInflator = LayoutInflater.from(this);
        ShareProvider.getInitShareProvider(VoteDetailsActivity.this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (userInfoDetailBean != null) {
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            screenWidth = getmContext().getResources().getDisplayMetrics().widthPixels;

            voteId = getIntent().getStringExtra("voteId");

            if (TextUtils.isEmpty(voteId)) {
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
    }

    private void initView() {

        initTitle();

        vote_index_item_time_tv = (TextView) this.findViewById(R.id.vote_index_item_time_tv);

        provider_details_name_tv = (TextView) this.findViewById(R.id.provider_details_name_tv);

        vote_detail_xxhasvote_tv = (TextView) this.findViewById(R.id.vote_detail_xxhasvote_tv);

        vote_detail_vc_tv = (TextView) this.findViewById(R.id.vote_detail_vc_tv);

        provider_details_iv_avtar = (ImageView) this.findViewById(R.id.provider_details_iv_avtar);

        provider_iv_user_type = (ImageView) this.findViewById(R.id.provider_iv_user_type);

        panic_has_purchase_llay = (LinearLayout) this.findViewById(R.id.panic_has_purchase_llay);

        welfare_purchase_hasgoturs_lv = (LinearLayout) this.findViewById(R.id.welfare_purchase_hasgoturs_lv);

        root_view = (ScrollView) this.findViewById(R.id.root_view);

        provider_details_discuss_llay = (LinearLayout) this.findViewById(R.id.provider_details_discuss_llay);

        vote_detail_result_llay = (LinearLayout) this.findViewById(R.id.vote_detail_result_llay);

        vote_index_item_rcontent_llay = (LinearLayout) this.findViewById(R.id.vote_index_item_rcontent_llay);

        vote_detail_item_result_sh_btn = (LinearLayout) this.findViewById(R.id.vote_detail_item_result_sh_btn);
//        vote_detail_item_result_sh_btn.setOnClickListener(new MyShowHiddenClickListener());

        vote_detail_list_llay = (LinearLayout) this.findViewById(R.id.vote_detail_list_llay);

        vote_detail_list_rg = (RadioGroup) this.findViewById(R.id.vote_detail_list_rg);
        vote_detail_list_rg.setOnCheckedChangeListener(new VoteCheckedChangeListener());

        vote_detail_govote_btn = (Button) this.findViewById(R.id.vote_detail_govote_btn);
        vote_detail_govote_btn.setOnClickListener(this);

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

    @Override
    public void onDoVoteSuccess(String message) {
        getProviderDetails();
    }

    @Override
    public void onDoVoteFail(String message) {

    }

    class MyDefaultInputClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (providerDetailsBean != null) {
                ProviderEvaEvent currentEvent = new ProviderEvaEvent(
                        userInfoDetailBean.getEmobId(),
                        providerDetailsBean.getEmobId(),
                        providerDetailsBean.getNickname(),
                        providerDetailsBean.getVoteId(), "");

                EventBus.getDefault().post(currentEvent);

            }
        }
    }


    /**
     * 初始化评论条
     */
    private void initPopupWindow() {
        View bottomview = View.inflate(this, R.layout.common_vote_discuss_bottom_input, null);

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
                        editableText.insert(selectionStart,smiledText);

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


    public void showKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void initTitle() {
        this.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        findViewById(R.id.tv_right_text).setVisibility(View.VISIBLE);

        ((ImageView) findViewById(R.id.iv_right)).setVisibility(View.GONE);
        ((ImageView) findViewById(R.id.iv_right)).setBackgroundDrawable(getResources().getDrawable(R.drawable.share));
        findViewById(R.id.iv_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Config.NET_SHAREBASE + "/share/vote-result.html?emobId=" + providerDetailsBean.getEmobId()+"&voteId="+voteId+ "&communityId=" + userInfoDetailBean.getCommunityId();
                ShareProvider.getShareProvider(VoteDetailsActivity.this).showShareActivity(url, providerDetailsBean.getVoteTitle(), "邻居帮帮", ShareProvider.CODE_VOTE);
            }
        });

        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("投票详情");
    }
//
//    @Override
//    public void onTagsA2BAddSuccess(String message) {
//        initData();
//    }
//
//    @Override
//    public void onTagsA2BAddFail(String message) {
//        showToast(message);
//    }

    interface MspCardListService {
//        @GET("/api/v1/communities/{communityId}/vote/detail")
//        void getVoteDetail(@Path("communityId") int communityId, @QueryMap HashMap<String, Object> option, Callback<VoteDetailsRespBean> cb);
//        @GET("/api/v1/communities/{communityId}/vote/detail")
//        /api/v3/votes/{投票ID}?emobId={当前用户的环信ID}
        @GET("/api/v3/votes/{voteid}")
        void getVoteDetail(@Path("voteid") String voteid, @QueryMap HashMap<String, String> option, Callback<CommonRespBean<VoteDetailsRespBean>> cb);
    }

    private void getProviderDetails() {
        HashMap<String, String> map = new HashMap<>();
        map.put("emobId", userInfoDetailBean.getEmobId());
        MspCardListService service = RetrofitFactory.getInstance().create(getmContext(),map,MspCardListService.class);
        Callback<CommonRespBean<VoteDetailsRespBean>> callback = new Callback<CommonRespBean<VoteDetailsRespBean>>() {
            @Override
            public void success(CommonRespBean<VoteDetailsRespBean> respBean, Response response) {

                if (respBean != null && TextUtils.equals("yes", respBean.getStatus())) {
                    providerDetailsBean = respBean.getData();
                    if(providerDetailsBean.getVoted()==1){////TODO  判断自己是否已经投过票了
                        findViewById(R.id.iv_right).setVisibility(View.VISIBLE);
                    }
                    initProviderDetail(providerDetailsBean);
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
        service.getVoteDetail(voteId, map, callback);
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


    /**
     * 初始化投票详情信息
     *
     * @param respBean
     */
    private void initProviderDetail(final VoteDetailsRespBean respBean) {

        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");

        if (vote_index_item_time_tv != null) {

//            Date tag_time_date = new Date(respBean.getCreateTime() * 1000L);

            vote_index_item_time_tv.setText(TimeUtils.fromLongToString2(respBean.getCreateTime() + ""));
        }

        if (provider_details_name_tv != null) {
            provider_details_name_tv.setText(respBean.getNickname());
        }

        if (vote_detail_xxhasvote_tv != null) {
            if(respBean.getVoteSum()<=0){
                vote_detail_xxhasvote_tv.setText("现在投票");
            }else{
                vote_detail_xxhasvote_tv.setText(respBean.getVoteSum() + "人已投票");
            }
        }

        if (vote_detail_vc_tv != null) {

            if (!TextUtils.isEmpty(respBean.getVoteTitle())) {
                Spannable spanAll = SmileUtils.getSmiledText(VoteDetailsActivity.this, respBean.getVoteTitle());
                vote_detail_vc_tv.setText(spanAll, TextView.BufferType.SPANNABLE);
            } else {
                vote_detail_vc_tv.setText("");
            }
//            provider_details_content_tv.setText(respBean.getInfo().getContent());
        }

        if (provider_details_iv_avtar != null) {
            ImageLoader.getInstance().displayImage(respBean.getAvatar(), provider_details_iv_avtar, UserUtils.options);

            //// 2015/12/10 添加帮主头衔
            initBangzhuMedal(respBean.getGrade());

            provider_details_iv_avtar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getmContext().startActivity(new Intent(getmContext(), UserGroupInfoActivity.class).putExtra(Config.INTENT_PARMAS2, respBean.getEmobId()));
                }
            });
        }


        /// 加载投票项 or 加载投票结果

        if (respBean.getVoted()==1) {
            if (vote_detail_list_llay != null) {
                vote_detail_list_llay.setVisibility(View.GONE);
            }

            //// 投票结果加载
            loadingVoteResult();

        } else {
            if (vote_index_item_rcontent_llay != null) {
                vote_index_item_rcontent_llay.setVisibility(View.VISIBLE);
            }
            ///// 投票选项加载
            loadingVotingItems();
        }
        /// 查看过他的人
        loadingGoodsHasGotursHeadImgs4(respBean);

        /// 加载评论列表
        initDiscussList(respBean.getComments());

    }


    private void loadingVotingItems() {

        if (vote_detail_list_rg != null) {
            List<VoteOptionsListEntity> voteOptionsList = providerDetailsBean.getOptions();

            if (voteOptionsList != null && voteOptionsList.size() > 0) {
                vote_detail_list_rg.removeAllViews();
                for (int i = 0; i < voteOptionsList.size(); i++) {

                    VoteOptionsListEntity voteOptionsListEntity = voteOptionsList.get(i);

                    if (voteOptionsListEntity != null) {

                        RadioButton radioButton = (RadioButton) layoutInflator.inflate(R.layout.common_vote_details_radio_item, null);

//                        RadioButton radioButton = (RadioButton) linearLayout.findViewById(R.id.vote_detail_vote_rbtn_id);

//                        radioButton.setId(voteOptionsListEntity.getSort());
//                        radioButton.setId(voteOptionsListEntity.getVoteOptionsId());
                        radioButton.setId(i);
                        radioButton.setText(voteOptionsListEntity.getContent());

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        ////设置ID, 添加排序 int left, int top, int right, int bottom
//                        params.setMargins(30, 0, 30, 0);

                        vote_detail_list_rg.addView(radioButton, params);

                    } else {
                        continue;
                    }
                }

            } else {

                Log.e("loadingVotingItems  ", "voteOptionsList is null ");
            }

        } else {

            Log.e("loadingVotingItems  ", "vote_detail_list_rg is null ");
        }

        vote_detail_list_llay.setVisibility(View.VISIBLE);
    }


    /**
     * 初始化用户横条奖章图片
     */
    private void initBangzhuMedal(String userType) {
        if (provider_iv_user_type != null) {
//            normal , bangzhu, fubangzhu ,zhanglao,bangzhong
            if (TextUtils.equals(userType, "zhanglao")) {
                provider_iv_user_type.setImageDrawable(getResources().getDrawable(R.drawable.life_circle_zhanglao_icon));
                provider_iv_user_type.setVisibility(View.VISIBLE);
            } else if (TextUtils.equals(userType, "bangzhu")) {
                provider_iv_user_type.setImageDrawable(getResources().getDrawable(R.drawable.life_circle_bangzhu_icon));
                provider_iv_user_type.setVisibility(View.VISIBLE);
            } else if (TextUtils.equals(userType, "fubangzhu")) {
                provider_iv_user_type.setImageDrawable(getResources().getDrawable(R.drawable.life_circle_fubangzhu_icon));
                provider_iv_user_type.setVisibility(View.VISIBLE);
            } else {
                provider_iv_user_type.setVisibility(View.GONE);
            }
        }
    }


    private List<VoteDetailsRespBean.VoteChatListEntity> cooperationDetailsEntityList;

    MyAdapter myAdapter = new MyAdapter();


    /**
     * 投票的结果展示
     */
    private void loadingVoteResult() {

//        /// 投票票数百分比
//        private LinearLayout vote_index_item_result_llay;
//        /// 放置百分比内容
//        private LinearLayout vote_index_item_rcontent_llay;
//
//        /// show_hide
//        private Button vote_index_item_result_sh_btn;

        List<VoteOptionsListEntity> voteOptionsList = providerDetailsBean.getOptions();

        if (voteOptionsList != null && voteOptionsList.size() > 0) {
            /// 66.88
            float maxPercent = getMaxPercentVote(voteOptionsList);
            /// 总行数
            maxLines = voteOptionsList.size();

            vote_index_item_rcontent_llay.removeAllViews();

            for (int i = 0; i < maxLines; i++) {

                VoteOptionsListEntity optionsListEntity = voteOptionsList.get(i);

                float array_element = Float.valueOf(optionsListEntity.getPercent());

                float leftPercent = array_element / maxPercent;

                leftPercent *= 0.60f;

                if (maxPercent == 0) {
                    leftPercent = 0.0f;
                }

                float rightPercent = 1 - leftPercent;

                System.out.println("leftPercent " + leftPercent);

                System.out.println("rightPercent  " + rightPercent);

                LinearLayout percentItem = (LinearLayout) layoutInflator.inflate(R.layout.common_vote_percent_layout, null);

                LinearLayout leftlay = (LinearLayout) percentItem.findViewById(R.id.vote_result_percent_left_llay);

                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, DensityUtil.dip2px(getmContext(), 10f), leftPercent);
                p.gravity = Gravity.CENTER_VERTICAL;

                leftlay.setLayoutParams(p);

//                leftlay.setBackgroundColor(Color.parseColor(colorArray[i % colorArray.length]));

                int index = i % colorArray.length;

                Drawable bgDrawable = null;

                switch (index) {
                    case 0:
                        bgDrawable = getResources().getDrawable(R.drawable.vote_index_result_shape_01);
                        break;
                    case 1:
                        bgDrawable = getResources().getDrawable(R.drawable.vote_index_result_shape_02);
                        break;
                    case 2:
                        bgDrawable = getResources().getDrawable(R.drawable.vote_index_result_shape_03);
                        break;
                    case 3:
                        bgDrawable = getResources().getDrawable(R.drawable.vote_index_result_shape_04);
                        break;
                }

                leftlay.setBackgroundDrawable(bgDrawable);


                LinearLayout rightlay = (LinearLayout) percentItem.findViewById(R.id.vote_result_percent_right_llay);

                LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, rightPercent);
//                rp.gravity = Gravity.CENTER_VERTICAL;
//                rp.setMargins(0, 0, 0, DensityUtil.dip2px(getmContext(), 3f));

                rightlay.setLayoutParams(rp);
                TextView vote_right_percent_tv = (TextView) rightlay.findViewById(R.id.vote_right_percent_tv);
                String count = ""+optionsListEntity.getCount();
                if(optionsListEntity.getCount()>=1000){
                    count = "999+";
                }
                String percentStr = optionsListEntity.getPercentText()+"% ("+ count+"票)";

                vote_right_percent_tv.setText(percentStr);

                TextView vote_result_pername_tv = (TextView) percentItem.findViewById(R.id.vote_result_pername_tv);

                vote_result_pername_tv.setText(optionsListEntity.getContent());

                ImageView vote_result_percent_chosed_iv = (ImageView) percentItem.findViewById(R.id.vote_result_percent_chosed_iv);
                if (optionsListEntity.getVoted() == 1) {
                    vote_result_percent_chosed_iv.setVisibility(View.VISIBLE);
                } else {
                    vote_result_percent_chosed_iv.setVisibility(View.INVISIBLE);
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                params.setMargins(0, 5, 5, 0);

//                View v = new View(getmContext());
//                v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, DensityUtil.dip2px(getmContext(),1f)));
//                v.setBackgroundColor(getResources().getColor(R.color.bg_color));

                vote_index_item_rcontent_llay.addView(percentItem, params);
//                vote_index_item_rcontent_llay.addView(v);

            }
            vote_detail_result_llay.setVisibility(View.VISIBLE);
        } else {
            /// 设置为不可见
            vote_detail_result_llay.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMSsoHandler ssoHandler = ShareProvider.mController.getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private float getMaxPercentVote(List<VoteOptionsListEntity> voteOptionsList) {

        float percent = Float.valueOf(voteOptionsList.get(0).getPercent());

        for (VoteOptionsListEntity optionsListEntity : voteOptionsList) {

            float temp = Float.valueOf(optionsListEntity.getPercent());

            if (temp > percent) {
                percent = temp;
            }
        }
        return percent;
    }


    /// 加载评论列表
    private void initDiscussList(List<VoteDetailsRespBean.VoteChatListEntity> cooperationDetails) {
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
            }
            provider_details_discuss_llay.setVisibility(View.VISIBLE);
        } else {
            provider_details_discuss_llay.setVisibility(View.GONE);
        }
    }


    /**
     * 加载已经kan过的用户
     */
    private void loadingGoodsHasGotursHeadImgs4(final VoteDetailsRespBean circleBean) {

        final List<VoteDetailsRespBean.UsersListEntity> users = circleBean.getVoteDetails();

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

            for (int i = 0; i < 6; i++) {

                LinearLayout usrHeadView = (LinearLayout) View.inflate(getmContext(), R.layout.common_vote_details_moreurs_headlay, null);
                ImageView img = (ImageView) usrHeadView.findViewById(R.id.iv_avatar);
                img.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(users.get(i).getAvatar(), img, UserUtils.options);

                TextView welfare_purchase_hasgoturs_name_tv = (TextView) usrHeadView.findViewById(R.id.welfare_purchase_hasgoturs_name_tv);
//                welfare_purchase_hasgoturs_name_tv.setText(users.get(i).getNickname());
                welfare_purchase_hasgoturs_name_tv.setVisibility(View.GONE);

                usrHeadView.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams rlparams = (LinearLayout.LayoutParams) img.getLayoutParams();

                rlparams.width = screenWidth * 100 / 1080;
                rlparams.height = screenWidth * 100 / 1080;

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

            LinearLayout usrHeadView = (LinearLayout) View.inflate(getmContext(), R.layout.common_vote_details_moreurs_headlay, null);
            ImageView img = (ImageView) usrHeadView.findViewById(R.id.iv_avatar);
            img.setVisibility(View.VISIBLE);
            img.setImageResource(R.drawable.help_more_forvote);
//            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.help_more_forvote, img);

            TextView welfare_purchase_hasgoturs_name_tv = (TextView) usrHeadView.findViewById(R.id.welfare_purchase_hasgoturs_name_tv);
            welfare_purchase_hasgoturs_name_tv.setVisibility(View.GONE);

            usrHeadView.setVisibility(View.VISIBLE);

            LinearLayout.LayoutParams rlparams = (LinearLayout.LayoutParams) img.getLayoutParams();

            rlparams.width = screenWidth * 100 / 1080;
            rlparams.height = screenWidth * 100 / 1080;

            img.setLayoutParams(rlparams);

            usrHeadView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getmContext().startActivity(new Intent(getmContext(), HasVoteNeighborsActivity.class).putExtra("voteId", "" + circleBean.getVoteId()));

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

                LinearLayout usrHeadView = (LinearLayout) View.inflate(getmContext(), R.layout.common_vote_details_moreurs_headlay, null);
                ImageView img = (ImageView) usrHeadView.findViewById(R.id.iv_avatar);
                img.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(users.get(i).getAvatar(), img, UserUtils.options);

                TextView welfare_purchase_hasgoturs_name_tv = (TextView) usrHeadView.findViewById(R.id.welfare_purchase_hasgoturs_name_tv);
//                welfare_purchase_hasgoturs_name_tv.setText(users.get(i).getNickname());
                welfare_purchase_hasgoturs_name_tv.setVisibility(View.GONE);

                usrHeadView.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams rlparams = (LinearLayout.LayoutParams) img.getLayoutParams();

                rlparams.width = screenWidth * 100 / 1080;
                rlparams.height = screenWidth * 100 / 1080;

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
                convertView = View.inflate(getmContext(), R.layout.common_vote_details_discuss_item, null);

                viewHolder = new ViewHolder(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Log.i("debbug", "size=" + cooperationDetailsEntityList.size());

//            Log.i("debbug", "viewHolder=" + viewHolder);

            final VoteDetailsRespBean.VoteChatListEntity cooperationDetailsEntity = cooperationDetailsEntityList.get(position);

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


            //// 如果评论的人是投票发起者，则后边的回复了谁不显示
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

            Spannable spanAll = SmileUtils.getSmiledText(getmContext(), cooperationDetailsEntity.getChatContent());

            viewHolder.discuss_content_tv.setText(spanAll, TextView.BufferType.SPANNABLE);

//            viewHolder.discuss_content_tv.setText(cooperationDetailsEntity.getDetailContent());

            ImageLoader.getInstance().displayImage(cooperationDetailsEntity.getAvatarFrom(), viewHolder.iv_avatar, UserUtils.options);

            //// 2015/12/10 添加帮内头衔
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
                if (TextUtils.equals(userType, Config.USER_TYPE_ZHANGLAO)) {
                    viewHolder.iv_user_type.setImageDrawable(getResources().getDrawable(R.drawable.life_circle_zhanglao_icon));
                    viewHolder.iv_user_type.setVisibility(View.VISIBLE);
                } else if (TextUtils.equals(userType,Config.USER_TYPE_BANGZHU)) {
                    viewHolder.iv_user_type.setImageDrawable(getResources().getDrawable(R.drawable.life_circle_bangzhu_icon));
                    viewHolder.iv_user_type.setVisibility(View.VISIBLE);
                } else if (TextUtils.equals(userType, Config.USER_TYPE_FUBANGZHU)) {
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

            case R.id.vote_detail_govote_btn:

                if (voteCheckedRadioButton != null && votecheckedId != -1) {

                    if (providerDetailsBean != null) {
                        goVote(voteCheckedRadioButton, votecheckedId);
                    } else {
                        showToast("投票数据异常");
                    }

                } else {
                    showToast("请选择投票内容");
                }

                break;

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
                //// 输入评论
                VoteDetailsUtil.eva(getmContext(),userInfoDetailBean.getEmobId(), currentEvent.getTo(), content, providerDetailsBean.getVoteId(),1, new Handler() {
                    @Override
                    public void handleMessage(Message msg) {

                        switch (msg.what) {
                            case Config.NETERROR:
                                showNetErrorToast();
                                break;
                            case Config.TASKCOMPLETE:
                                showToast("评论成功");

                                VoteDetailsRespBean.VoteChatListEntity  voteChatListEntity = new VoteDetailsRespBean.VoteChatListEntity();
                                voteChatListEntity.setEmobIdTo(currentEvent.getTo());
                                voteChatListEntity.setEmobIdFrom(userInfoDetailBean.getEmobId());
                                voteChatListEntity.setGradeFrom(userInfoDetailBean.getGrade());
                                voteChatListEntity.setAvatarFrom(userInfoDetailBean.getAvatar());
                                voteChatListEntity.setChatContent(content);
                                voteChatListEntity.setNicknameFrom(userInfoDetailBean.getNickname());
                                voteChatListEntity.setNicknameTo(currentEvent.getFromNike());
                                voteChatListEntity.setCreateTime((int)System.currentTimeMillis()/1000);
                                if(cooperationDetailsEntityList!=null&&cooperationDetailsEntityList.size()>0){
                                    cooperationDetailsEntityList.add(voteChatListEntity);
                                    ///TODO  刷新数据
                                    myAdapter.notifyDataSetChanged();
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (root_view != null) {
                                                root_view.fullScroll(ScrollView.FOCUS_DOWN);
                                            }
                                        }
                                    });

                                }else{

//                                    cooperationDetailsEntityList = new ArrayList<VoteDetailsRespBean.VoteChatListEntity>();
//                                    cooperationDetailsEntityList.add(voteChatListEntity);
//                                    if(myAdapter==null){
//                                        myAdapter = new MyAdapter();
//                                    }
////                                    initDiscussList(cooperationDetailsEntityList);
//                                    if (provider_details_discuss_lv != null) {
//                                        provider_details_discuss_lv.setAdapter(myAdapter);
//                                        myAdapter.notifyDataSetChanged();
//                                    }
//                                    provider_details_discuss_llay.setVisibility(View.VISIBLE);

                                    getProviderDetails();
                                }

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
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);

//        hideKeyboard();

        if (mLdDialog != null) {
            mLdDialog.dismiss();
        }
        if (myTagsManagerDialog != null) {
            myTagsManagerDialog.dismiss();
        }
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        if(imm!=null){
//            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
//                    InputMethodManager.HIDE_NOT_ALWAYS);
//        }

        if (providerDetailsBean != null) {
//            boolean hasVoted = providerDetailsBean.getHasVoted();
            if (providerDetailsBean.getVoted()==1) {
                NewVotedBackEvent newVotedBackEvent = new NewVotedBackEvent(providerDetailsBean);
                EventBus.getDefault().post(newVotedBackEvent);
            }
        }
    }

    private class MyShowHiddenClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            if (showLines == DEFAULT_SHOW_LINES) {
                showLines = maxLines;
            } else {
                showLines = DEFAULT_SHOW_LINES;
            }
            /// 重新加载vote_result
            loadingVoteResult();
        }
    }


    private class VoteCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (group != null) {
                voteCheckedRadioButton = (RadioButton) group.findViewById(checkedId);
                votecheckedId = checkedId;
                Log.d("VoteCheckedChangeListener ", "onCheckedChanged checkedId " + checkedId);
//                goVote(voteCheckedRadioButton, checkedId);
            }

        }
    }

    public void goVote(RadioButton radioButton, int checkedId) {
        if (providerDetailsBean != null) {
            if (radioButton != null) {
                Log.d("VoteCheckedChangeListener ", "onCheckedChanged checkedId " + checkedId);

                userInfoDetailBean = PreferencesUtil.getLoginInfo(getmContext());
                VoteGoReqBean quaryToken = new VoteGoReqBean();
                quaryToken.setEmobId(userInfoDetailBean.getEmobId()); /// 投票者的emobid
                quaryToken.setEmobIdTo(providerDetailsBean.getEmobId());/// 投票发起人的环信ID
                quaryToken.setVoteId(providerDetailsBean.getVoteId()); ////
                quaryToken.setVoteOptionsId(radioButton.getId());
                String optName = radioButton.getText().toString().trim();
                myTagsManagerDialog = new MyVoteConfirmDialog(getmContext(), optName, quaryToken, VoteDetailsActivity.this);
                myTagsManagerDialog.show();
                //            WindowManager windowManager = getWindowManager();
//            Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = myTagsManagerDialog.getWindow().getAttributes();
//            lp.width = (int) (display.getWidth()); //设置宽度
                lp.width = screenWidth;
                myTagsManagerDialog.getWindow().setAttributes(lp);
            } else {
                Log.d("VoteCheckedChangeListener ", "onCheckedChanged radioButton==null " + checkedId);
            }
        } else {
            Log.d("VoteCheckedChangeListener ", "goVote providerDetailsBean ==null ");
        }
    }


}
