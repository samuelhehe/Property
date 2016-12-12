package xj.property.activity.LifeCircle;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.sso.UMSsoHandler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.ChatActivity;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.activity.runfor.RunForActivity;
import xj.property.activity.user.ShowBigImageViewPager;
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.adapter.ExpressionAdapter;
import xj.property.adapter.ExpressionPagerAdapter;
import xj.property.adapter.ImageAdapter;
import xj.property.beans.LifeCircleBean;
import xj.property.beans.LifeCircleDetail;
import xj.property.beans.LifeCircleDetailRespone;
import xj.property.beans.StatusBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.cache.ItemPraiseRecoder;
import xj.property.cache.ZambiaCache;
import xj.property.event.EvaEvent;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.netbasebean.LifeCircleDelReqBean;
import xj.property.provider.ShareProvider;
import xj.property.utils.CommonUtils;
import xj.property.utils.SmileUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.FriendZoneUtil;
import xj.property.utils.other.GroupUtils;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;
import xj.property.widget.ExpandGridView;
import xj.property.widget.com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by Administrator on 2015/6/8.
 */
public class ZoneItemActivity extends HXBaseActivity {

    private static final int MAX_CONTENT_LINES = 3;
    ImageView ivavatar;
    TextView tvNickName;
    LifeCircleBean circleBean;
    ImageView avatar, ivcontent, iv_chatar, iv_operation;
    GridView gv_pic;
    TextView tv_username, tv_value, tv_content, tv_evahost, tv_zambia_host, tv_time, tv_operate, tv_person_value, tv_precent, tv_zb, tv_share_goods,tv_comment;


    TextView tv_joingroup;
    LinearLayout ll_eva_content;
    Button btnSend, btnCharter;
    EditText editText;
    PopupWindow popupWindow, popupWindowEva;
    UserInfoDetailBean userInfoDetailBean;
    View root;

    //    line_none_content;
    private LinearLayout ll_main;
    TextView tv_zan_rpvalue, tv_eva_ta, tv_coyp_txt;
    DisplayImageOptions option_1 = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.head_portrait_personage)
            .showImageOnFail(R.drawable.head_portrait_personage).showImageOnLoading(R.drawable.head_portrait_personage)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(false).cacheOnDisk(false)
            .build();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 115:
                    mLdDialog.dismiss();
                    finish();
                    break;
                case 0:
                    mLdDialog.dismiss();
                    mLdDialog.dismiss();
                    showToast("请求失败");
                    break;
                default:
                    mLdDialog.dismiss();
                    break;
            }
        }
    };
    /// XXX等几人赞了你的人品
    private TextView has_zambiaed_content_tv;
    /// 赞了你的人品布局
    private RelativeLayout has_zambiaed_rlay;
    private LinearLayout ll_face_container;
    private ViewPager vPager;
    private CirclePageIndicator vpager_indicator;
    private List<String> reslist;
    private CheckBox send_img_checkbox;
    //// 用户 帮主奖章
    private ImageView iv_user_type;
    //// 生活圈详情 帮主奖章
    private ImageView iv_user_type_header;
    private LinearLayout vote_bangzhu_go_llay;
    private TextView vote_bangzhu_for_cname_tv;
    private Button vote_bangzhu_go_btn;
    private String emobId;
    private int lifeCircleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoneitem);
        ShareProvider.getInitShareProvider(ZoneItemActivity.this);
        ll_main = (LinearLayout) findViewById(R.id.ll_main);
        userInfoDetailBean = PreferencesUtil.getLoginInfo(this);
        emobId = getIntent().getStringExtra(Config.INTENT_PARMAS1);
        lifeCircleId = getIntent().getIntExtra(Config.INTENT_PARMAS2, 0);

        if(userInfoDetailBean!=null&&TextUtils.equals(emobId,userInfoDetailBean.getEmobId())){
            initTitle(null, "我的生活圈详情", null);
        }else{
            initTitle(null, "TA的生活圈详情", null);
        }
        initView();
        getCircleLifeDetial(emobId,lifeCircleId);
        initPopupWindow();
        initPopupWindow2();
    }

    private void initPopupWindow() {
        View bottomview = View.inflate(this, R.layout.bottom_input, null);
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

        editText = (xj.property.widget.PasteEditText) bottomview.findViewById(R.id.et_sendmessage);
        editText.setOnClickListener(this);

        send_img_checkbox = (CheckBox) bottomview.findViewById(R.id.send_img_checkbox);
        send_img_checkbox.setOnCheckedChangeListener(new onMyCheckedChangeListener());

        btnSend = (Button) bottomview.findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);
        btnCharter = (Button) bottomview.findViewById(R.id.btn_charter);
        btnCharter.setOnClickListener(this);
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
                }
            }
        });
    }

    public void shareShow(String url,String message ){
        ShareProvider.getShareProvider(ZoneItemActivity.this).showShareActivity(url, message, "邻居帮帮", ShareProvider.CODE_LIFECRILE);
    }


    public void showKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    private class onMyCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                if (ll_face_container != null) {
                    ll_face_container.setVisibility(View.VISIBLE);
                    closeBoard(getmContext());
                }
            } else {
                if (ll_face_container != null) {
                    ll_face_container.setVisibility(View.GONE);
                    closeBoard(getmContext());
                    ;
                }
            }
        }
    }

    public static void closeBoard(Context mcontext) {
        InputMethodManager imm = (InputMethodManager) mcontext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
                return true;
            }
            finish();
        }


        return super.onKeyDown(keyCode, event);
    }


    private void initView() {
        root = findViewById(R.id.root);
        ivavatar = (ImageView) findViewById(R.id.iv_avatar);
        ivavatar.setOnClickListener(this);
        tvNickName = (TextView) findViewById(R.id.tv_name_user);
        ll_eva_content = (LinearLayout) findViewById(R.id.ll_eva_content);
        avatar = (ImageView) findViewById(R.id.avatar);

        ll_eva_content = (LinearLayout) findViewById(R.id.ll_eva_content);
        tv_operate = (TextView) findViewById(R.id.tv_operate);
        tv_person_value = (TextView) findViewById(R.id.tv_person_value);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_share_goods = (TextView) findViewById(R.id.tv_share_goods);
        tv_value = (TextView) findViewById(R.id.tv_value);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_evahost = (TextView) findViewById(R.id.tv_evahost);

//        line_none_content=findViewById(R.id.line_none_content);

        iv_operation = (ImageView) findViewById(R.id.iv_operation);
        if (userInfoDetailBean != null && TextUtils.equals( getIntent().getStringExtra(Config.INTENT_PARMAS1),userInfoDetailBean.getEmobId())){
//                getIntent().getStringExtra(Config.INTENT_PARMAS1).equals(userInfoDetailBean.getEmobId())) {
            iv_operation.setVisibility(View.VISIBLE);
            iv_operation.setOnClickListener(this);
        }
        tv_precent = (TextView) findViewById(R.id.tv_character_percent);
        tv_joingroup = (TextView) findViewById(R.id.tv_joingroup);
        iv_chatar = (ImageView) findViewById(R.id.iv_charter);
        tv_zambia_host = (TextView) findViewById(R.id.tv_zambia_host);
        tv_time = (TextView) findViewById(R.id.tv_time);
        gv_pic = (GridView) findViewById(R.id.gv_pic);
        gv_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                changeCurrent(position);
            }
        });
        tv_zb = (TextView) findViewById(R.id.tv_zb);
        tv_zb.setOnClickListener(zambiaHostOnclick);
        tv_zambia_host.setOnClickListener(zambiaHostOnclick);
        //ll_pic_content=(LinearLayout)v.findViewById(R.id.ll_pic_content);
        ivcontent = (ImageView) findViewById(R.id.iv_content);
        ivcontent.setOnClickListener(this);
        tv_comment = (TextView) findViewById(R.id.tv_comment);
        tv_comment.setOnClickListener(this);
        /// 是否为帮主
        iv_user_type = (ImageView) findViewById(R.id.iv_user_type);
        iv_user_type_header = (ImageView) findViewById(R.id.iv_user_type_header);

        has_zambiaed_content_tv = (TextView) findViewById(R.id.has_zambiaed_content_tv);
        has_zambiaed_rlay = (RelativeLayout) findViewById(R.id.has_zambiaed_rlay);

        //// 帮主竞选部分
        vote_bangzhu_go_llay = (LinearLayout) findViewById(R.id.vote_bangzhu_go_llay);
        vote_bangzhu_for_cname_tv = (TextView) findViewById(R.id.vote_bangzhu_for_cname_tv);
        vote_bangzhu_go_btn = (Button) findViewById(R.id.vote_bangzhu_go_btn);


    }

    ZambiaHostOnclick zambiaHostOnclick = new ZambiaHostOnclick();

    class ZambiaHostOnclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (userInfoDetailBean != null) {
                zambia(circleBean);
            } else {
                Intent intent = new Intent(ZoneItemActivity.this, RegisterLoginActivity.class);
                ZoneItemActivity.this.startActivity(intent);
            }
        }
    }

    //// 头部
    private void initBangzhuModel(String usrType) {
        if (TextUtils.equals("bangzhu", usrType)) {
            iv_user_type_header.setVisibility(View.VISIBLE);
            iv_user_type_header.setImageResource(R.drawable.me_bangzhu_icon);
        } else if (TextUtils.equals("fubangzhu", usrType)) {
            iv_user_type_header.setVisibility(View.VISIBLE);
            iv_user_type_header.setImageResource(R.drawable.me_fubangzhu_icon);
        } else if (TextUtils.equals("zhanglao", usrType)) {
            iv_user_type_header.setVisibility(View.VISIBLE);
            iv_user_type_header.setImageResource(R.drawable.me_zhanglao_icon);
        } else if (TextUtils.equals("bangzhong", usrType) || TextUtils.equals("normal", usrType)) {
            iv_user_type_header.setVisibility(View.INVISIBLE);
        } else {
            iv_user_type_header.setVisibility(View.INVISIBLE);
        }
    }

    private void refresh() {
        if (circleBean == null) return;
        //// 2015/11/18 生活圈详情部分
        String usrType = circleBean.getGrade();
        if (TextUtils.equals("bangzhu", usrType)) {
            iv_user_type.setVisibility(View.VISIBLE);
            iv_user_type.setImageResource(R.drawable.life_circle_bangzhu_icon);
        } else if (TextUtils.equals("fubangzhu", usrType)) {
            iv_user_type.setVisibility(View.VISIBLE);
            iv_user_type.setImageResource(R.drawable.life_circle_fubangzhu_icon);
        } else if (TextUtils.equals("zhanglao", usrType)) {
            iv_user_type.setVisibility(View.VISIBLE);
            iv_user_type.setImageResource(R.drawable.life_circle_zhanglao_icon);
        } else if (TextUtils.equals("bangzhong", usrType) || TextUtils.equals("normal", usrType)) {
            iv_user_type.setVisibility(View.INVISIBLE);
        } else {
            iv_user_type.setVisibility(View.INVISIBLE);
        }
        //// 头部帮主奖章初始化
        initBangzhuModel(usrType);

        ImageLoader.getInstance().displayImage(circleBean.getAvatar(), ivavatar, UserUtils.options);
        ImageLoader.getInstance().displayImage(circleBean.getAvatar(), avatar, UserUtils.options);

        findViewById(R.id.top_view_bar).setVisibility(View.GONE);
        tvNickName.setText(circleBean.getNickname());
        tv_username.setText(circleBean.getNickname());
        tv_person_value.setText("" + circleBean.getCharacterValues());
        tv_precent.setText("打败了" + StrUtils.getPrecent(circleBean.getCharacterPercent()) + "%的本小区居民！");

        final Spannable spanAll = SmileUtils.getSmiledText(getmContext(), circleBean.getLifeContent());
        //// 帮主竞选 type 23
        vote_bangzhu_go_llay.setVisibility(View.GONE);
        switch (circleBean.getType()) {

            case 0:
            default:
                commonContentShowHide( spanAll, "",circleBean.getNickname());
                break;

            case 2:
                commonContentShowHide(spanAll, circleBean.getTypeContent(),circleBean.getNickname());
                break;
            case 19:
                commonContentShowHide( spanAll, "分享了福利",circleBean.getNickname());
                break;
            case 24:
                commonContentShowHide( spanAll, "完成物业费缴纳",circleBean.getNickname());
                break;

            case 20:
                commonContentShowHide( spanAll, "分享了会员卡购物",circleBean.getNickname());
                break;

            case 23:
                commonContentShowHide( spanAll, "",circleBean.getNickname());
                /// 竞选小区的名字  我在竞选狮子城小区的帮主,快来投票给我吧...
                String communityName = PreferencesUtil.getCommityName(getmContext());
                if (TextUtils.isEmpty(communityName)) {
                    communityName = "本";
                }
                vote_bangzhu_for_cname_tv.setText("我在竞选" + communityName + "小区的帮主,快来投票给我吧...");
                vote_bangzhu_go_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goBangzhuElection(circleBean.getEmobId());
                    }
                });
                vote_bangzhu_go_llay.setVisibility(View.VISIBLE);
                break;
        }
        
/*        if (circleBean.getLifeContent().length() > 128) {
            tv_content.setText(circleBean.getLifeContent().substring(0, 128));
            tv_operate.setVisibility(View.VISIBLE);
            tv_operate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv_content.getText().toString().length() > 128) {
                        tv_content.setText(circleBean.getLifeContent().substring(0, 128));
                        tv_operate.setText("全文");
                    } else {
                        tv_content.setText(circleBean.getLifeContent());
                        tv_operate.setText("收起");

                    }
                }
            });
        } else {
            tv_content.setText(circleBean.getLifeContent());
            tv_operate.setVisibility(View.GONE);
        }*/
        avatar.setOnClickListener(this);
        if (circleBean.getEmobGroupId() != null && !TextUtils.isEmpty(circleBean.getEmobGroupId())) {
            tv_joingroup.setVisibility(View.VISIBLE);
            if (PreferencesUtil.getLogin(ZoneItemActivity.this))
                tv_joingroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ProgressDialog progressDialog = new ProgressDialog(ZoneItemActivity.this);
                        progressDialog.setMessage("正在获取群信息..");
                        progressDialog.show();
                        GroupUtils.joinGroup(ZoneItemActivity.this, circleBean.getEmobGroupId(), new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                progressDialog.dismiss();
                                switch (msg.what) {
                                    case Config.TASKERROR:
                                        Toast.makeText(ZoneItemActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                                        break;
                                    case Config.TASKCOMPLETE:
                                        Intent intentPush = new Intent(ZoneItemActivity.this, ChatActivity.class);
                                        // it is group chat
                                        intentPush.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                                        intentPush.putExtra("groupId", circleBean.getEmobGroupId());
//                    intentPush.putExtra(Config.EXPKey_GROUP,ZoneItemActivity.this.bean.getActivityTitle());
                                        ZoneItemActivity.this.startActivity(intentPush);
                                        GroupUtils.getGroupInfo(circleBean.getEmobGroupId());
                                        break;
                                    case Config.NETERROR:
                                        Toast.makeText(ZoneItemActivity.this, ZoneItemActivity.this.getString(R.string.netError), Toast.LENGTH_SHORT).show();
                                        break;

                                }
                            }
                        });
                    }
                });
            else {
                tv_joingroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ZoneItemActivity.this, RegisterLoginActivity.class);
                        ZoneItemActivity.this.startActivity(intent);
                    }
                });
            }
        } else {
            tv_joingroup.setVisibility(View.INVISIBLE);
        }

        tv_zambia_host.setText(circleBean.getPraiseSum() + "\t 赞人品");
        if (circleBean.getCharacterValues() > 0) {
            iv_chatar.setImageResource(R.drawable.lifecircle_likeicon);
            tv_value.setText("" + circleBean.getCharacterValues());
        } else {
            iv_chatar.setImageResource(R.drawable.lifecircle_likeicon_nobodypressed);
            tv_value.setText("");
        }


        ///// XXX 赞了你的人品 内容加载
//        List<LifeCircleBean.LifePraise>  lifePraises =    circleBean.getLifePraises();
//
//        if(lifePraises!=null&&!lifePraises.isEmpty()){
//            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
//
//            for (LifeCircleBean.LifePraise lp: lifePraises){
//                spannableStringBuilder.append(lp.getNickname()).append("、");
//
//            }
//            spannableStringBuilder  = new SpannableStringBuilder( spannableStringBuilder.subSequence(0,spannableStringBuilder.length()-1));
//
//            int end = spannableStringBuilder.length();
//
//            if(lifePraises.size()>=3){
//
//                spannableStringBuilder.append("等").append("" + circleBean.getPraiseUserSum()).append("人");
//            }
//            spannableStringBuilder.append(" 赞了你的人品");
//
////            Drawable drawable = context.getResources().getDrawable(R.drawable.lifecircle_bigger_likeicon);
////            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
////            spannableStringBuilder.setSpan(new ImageSpan(drawable), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#2fcc71")), 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  // 设置点赞人为绿色
//
//            has_zambiaed_content_tv.setText(spannableStringBuilder);
//
//            has_zambiaed_rlay.setVisibility(View.GONE);
//            //// XXX等人给你点了赞
//        }else{
//            has_zambiaed_rlay.setVisibility(View.GONE);
//        }


        tv_time.setText(StrUtils.getDate4LifeCircleDay(circleBean.getCreateTime()));

        List<String> lifePhotos = Arrays.asList(circleBean.getPhotoes().split(","));
        ////////////////////////

        switch (circleBean.getType()) {
            case 0:
                if (lifePhotos == null || lifePhotos.isEmpty()) {

                    ivcontent.setVisibility(View.GONE);
                    gv_pic.setVisibility(View.GONE);
                } else if (lifePhotos.size() == 1) {
                    gv_pic.setVisibility(View.GONE);
                    ivcontent.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(lifePhotos.get(0), ivcontent);
                } else {
                    ivcontent.setVisibility(View.GONE);
                    gv_pic.setVisibility(View.VISIBLE);
                    gv_pic.setAdapter(new ImageAdapter(ZoneItemActivity.this, lifePhotos));
                }
                break;
            case 19:  /// welfare
            case 24: /// 分享了物业缴费
            case 2:  //// fastshop
            case 20: /// 会员卡
                if (lifePhotos == null || lifePhotos.isEmpty()) {

                    ivcontent.setVisibility(View.GONE);
                    gv_pic.setVisibility(View.GONE);
                } else {
                    gv_pic.setNumColumns(4);
                    ivcontent.setVisibility(View.GONE);
                    gv_pic.setVisibility(View.VISIBLE);
                    if (lifePhotos.size() <= 4) {
                        gv_pic.setAdapter(new ImageAdapter(ZoneItemActivity.this, lifePhotos, circleBean.getType()));
                    } else {
                        List<String> lifePhotosCopy = new ArrayList<String>();
                        for (int i = 0; i < 4; i++) {
                            lifePhotosCopy.add(lifePhotos.get(i));
                        }
                        gv_pic.setAdapter(new ImageAdapter(ZoneItemActivity.this, lifePhotosCopy, circleBean.getType()));
                    }

//                    gv_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            if(context instanceof FriendZoneIndexActivity)
//                                ((FriendZoneIndexActivity)context).changeCurrent(circleBean.getLifeCircleId(),position);
//                            else
//                                ((LifeSearchActivity)context).changeCurrent(circleBean.getLifeCircleId(),position);
//                        }
//                    });


                }
                break;
            case 23:
                if (lifePhotos.isEmpty()) {

                    ivcontent.setVisibility(View.GONE);
                    gv_pic.setVisibility(View.GONE);
                }
                ///TODO  帮主竞选
                vote_bangzhu_go_llay.setVisibility(View.VISIBLE);

                /// 竞选小区的名字  我在竞选狮子城小区的帮主,快来投票给我吧...
                String communityName = PreferencesUtil.getCommityName(getmContext());
                if (TextUtils.isEmpty(communityName)) {
                    communityName = "本";
                }
                vote_bangzhu_for_cname_tv.setText("我在竞选" + communityName + "小区的帮主,快来投票给我吧...");
                vote_bangzhu_go_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goBangzhuElection(circleBean.getEmobId());
                    }

                });

                break;
            default:
                Log.i("debbug", "执行了default");
                break;
        }



/*        if (lifePhotos == null || lifePhotos.isEmpty()) {

            ivcontent.setVisibility(View.GONE);
            gv_pic.setVisibility(View.GONE);
        } else if (lifePhotos.size() == 1) {
            gv_pic.setVisibility(View.GONE);
            ivcontent.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(lifePhotos.get(0).getPhotoUrl(), ivcontent);
        } else {
            ivcontent.setVisibility(View.GONE);
            gv_pic.setVisibility(View.VISIBLE);
            gv_pic.setAdapter(new ImageAdapter(ZoneItemActivity.this, lifePhotos));
        }*/

        List<LifeCircleDetail> lifeCircleDetails = circleBean.getLifeCircleDetails();

//        if(lifeCircleDetails.isEmpty())line_none_content.setVisibility(View.GONE);

        ll_eva_content.removeAllViews();
        for (int i = 0; i < lifeCircleDetails.size(); i++) {
            initItemEva(lifeCircleDetails.get(i));
        }


        ////赞人列表
        List<LifeCircleBean.LifePraise> lifePraises = circleBean.getLifePraises();
        for (int i = 0; i < lifePraises.size(); i++) {
            initItemPraise(lifePraises.get(i));
        }
        tv_evahost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//给楼主回复
                evaEvent = new EvaEvent(circleBean.getEmobId(), circleBean.getNickname(), circleBean.getLifeCircleId(), 0);
                btnCharter.setVisibility(View.GONE);
                onEvent();
            }
        });
        ll_main.setVisibility(View.VISIBLE);

    }

    /**
     * 生活圈内容的显示与隐藏
     *
     * @param spanAll
     * @param shareGoodsName
     */
    private void commonContentShowHide( final Spannable spanAll, String shareGoodsName, String nickname) {
        if (TextUtils.isEmpty(shareGoodsName)) {
            tv_share_goods.setText("");
            tv_share_goods.setVisibility(View.GONE);
        } else {
            tv_share_goods.setText(shareGoodsName);
            tv_share_goods.setVisibility(View.VISIBLE);
        }
//        Log.d("commonContentShowHide ","nickname "+ nickname  +" spanAll "+ spanAll.toString()+ " span length "+ spanAll.toString().length());

        tv_content.setText("");
//        tv_content.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
        tv_content.setText(spanAll, TextView.BufferType.SPANNABLE);
        final int lineCount = tv_content.getLineCount();
        if (lineCount > 3) {
            final int[] tempCount = {MAX_CONTENT_LINES};
            tv_content.setLines(MAX_CONTENT_LINES);
            tv_content.setText(spanAll, TextView.BufferType.SPANNABLE);
            tv_operate.setVisibility(View.VISIBLE);
            tv_operate.setText("全文");

            tv_operate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //// 当前设置行数
                    if (tempCount[0] > MAX_CONTENT_LINES) {
                        tempCount[0] = MAX_CONTENT_LINES;
                        tv_content.setLines(MAX_CONTENT_LINES);
                        tv_content.setText(spanAll, TextView.BufferType.SPANNABLE);
                        tv_operate.setText("全文");

                        Log.d("MAX_CONTENT_LINES lines : ", "MAX_CONTENT_LINES " + MAX_CONTENT_LINES);
                    } else {
                        tempCount[0] = lineCount;
                        /// 实际多少行
                        Log.d("real lines : ", "lineCount " + lineCount);
                        tv_content.setLines(lineCount);
                        tv_content.setText(spanAll, TextView.BufferType.SPANNABLE);
                        tv_operate.setText("收起");
                    }
                    tv_content.requestLayout();
                    tv_content.setVisibility(View.VISIBLE);
                }
            });
        } else {
            tv_content.setLines(lineCount);
            tv_operate.setVisibility(View.GONE);
            tv_operate.setText("全文");
        }

        Log.d("real lines outer  : ", "lineCount " + lineCount + " nickname " + nickname);
//        tv_content.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv_content.requestLayout();
        tv_content.setVisibility(View.VISIBLE);
    }

    private void goBangzhuElection(String emobId) {

        ////  跳转帮主竞选页面
        Intent intenet = new Intent(getmContext(), RunForActivity.class);
        intenet.putExtra("uemobid", emobId);
        startActivity(intenet);

    }

    public void onEvent() {
        if (userInfoDetailBean != null) {

            if (evaEvent.getFrom() == null && evaEvent.getFromNike() == null) {
                Log.i("debbug", "debbugnull");
                return;
            }

            if (evaEvent.getCircleLifeDetialId() == 0) {
                btnCharter.setVisibility(View.GONE);
            } else {
                btnCharter.setVisibility(View.GONE);
            }
            editText.setHint("回复" + evaEvent.getFromNike() + ":");
//            currentEvent = event;
            if (evaEvent.getView() != null) {
                showPopupWindowEva(evaEvent.getView(), evaEvent.getFromNike());
                return;
            }

            popupWindow.showAtLocation(root, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            if (ll_face_container != null) {
                send_img_checkbox.setChecked(false);
                ll_face_container.setVisibility(View.GONE);
                showKeyBoard();
            }

        } else {
            Intent intent = new Intent(this, RegisterLoginActivity.class);
            startActivityForResult(intent, Config.TASKCOMPLETE);

        }
    }


    private void initItemPraise(final LifeCircleBean.LifePraise lifePraise) {
        final ItemPraiseRecoder itemPraiseRecoder = new Select().from(ItemPraiseRecoder.class).where("host_emobid = ? and lifecircle_id = ? and custor_emobid = ?", userInfoDetailBean == null ? "" : userInfoDetailBean.getEmobId(), circleBean.getLifeCircleId(), lifePraise.getEmobId()).executeSingle();
        final LinearLayout item_praise = (LinearLayout) View.inflate(ZoneItemActivity.this, R.layout.praise_rank_item, null);
        item_praise.setBackgroundColor(itemPraiseRecoder != null ? 0xfffafafa : 0xffffffff);
        ll_eva_content.addView(item_praise);
        item_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent();

                if (itemPraiseRecoder == null) {
                    new ItemPraiseRecoder(userInfoDetailBean == null ? "" : userInfoDetailBean.getEmobId(), lifePraise.getEmobId(), circleBean.getLifeCircleId()).save();
                    item_praise.setBackgroundColor(0xfffafafa);
                }
                Intent intent = new Intent(ZoneItemActivity.this, UserGroupInfoActivity.class);
                intent.putExtra(Config.INTENT_PARMAS2, lifePraise.getEmobId());
                startActivity(intent);
            }
        });
        ImageLoader.getInstance().displayImage(lifePraise.getAvatar(), (ImageView) item_praise.findViewById(R.id.iv_avatar), UserUtils.options);
        ((TextView) item_praise.findViewById(R.id.tv_content)).setText(lifePraise.getNickname());
        ((TextView) item_praise.findViewById(R.id.tv_zambia_count)).setText(lifePraise.getPraiseSum() + "");
    }

    private void initItemEva(final LifeCircleDetail lifeCircleDetail) {
        LinearLayout item_evaBack = (LinearLayout) View.inflate(ZoneItemActivity.this, R.layout.item_evaback, null);
        ll_eva_content.addView(item_evaBack);
        View.OnClickListener onClickListenerEva = new View.OnClickListener() {//给评论的人回复
            @Override
            public void onClick(View v) {
                //弹出评论框;
//                btnCharter.setVisibility(View.VISIBLE);
                evaEvent = new EvaEvent(lifeCircleDetail.getEmobIdFrom(), lifeCircleDetail.getFromName(), lifeCircleDetail.getLifeCircleId(), lifeCircleDetail.getLifeCircleDetailId(), v);
                onEvent();
            }
        };
        item_evaBack.setOnClickListener(onClickListenerEva);
        item_evaBack.findViewById(R.id.tv_zambia).setOnClickListener(onClickListenerEva);

        if (lifeCircleDetail.getEmobIdTo().equals(circleBean.getEmobId()) || lifeCircleDetail.getEmobIdTo().equals(lifeCircleDetail.getEmobIdFrom())) {
            FriendZoneUtil.initEva(ZoneItemActivity.this, lifeCircleDetail.getFromName(), lifeCircleDetail.getEmobIdFrom(), null, null, lifeCircleDetail.getDetailContent(), lifeCircleDetail.getPraiseSum(), item_evaBack);
        } else
            FriendZoneUtil.initEva(ZoneItemActivity.this, lifeCircleDetail.getFromName(), lifeCircleDetail.getEmobIdFrom(), lifeCircleDetail.getToName(), lifeCircleDetail.getEmobIdTo(), lifeCircleDetail.getDetailContent(), lifeCircleDetail.getPraiseSum(), item_evaBack);
    }

    public void changeCurrent(int postion) {
        String[] split = circleBean.getPhotoes().split(",");
        ArrayList<String> imageuris =new ArrayList<>();
        for(String s : split){
            imageuris.add(s);
        }
        if (imageuris.size() <= postion) return;
        Intent intent = new Intent(ZoneItemActivity.this, ShowBigImageViewPager.class);
        intent.putExtra("images", imageuris);
        intent.putExtra("position", postion);
        startActivity(intent);
    }

    EvaEvent evaEvent;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                popupWindow.dismiss();
                popupWindow = null;
                finish();
                break;
            case R.id.iv_avatar:
            case R.id.avatar:
                Intent intent = new Intent(ZoneItemActivity.this, UserGroupInfoActivity.class);
                intent.putExtra(Config.INTENT_PARMAS2, circleBean.getEmobId());
                startActivity(intent);
                break;
            case R.id.et_sendmessage:
                break;
            case R.id.iv_operation://删除操作
                showDeleteDialog(ZoneItemActivity.this, circleBean.getLifeCircleId());

                break;

            case R.id.iv_content:
                changeCurrent(0);
                break;
            case R.id.btn_send:
                final String content = editText.getText().toString();
                if (content == null || TextUtils.isEmpty(content)) {
                    showToast("请输入评论内容");
                    return;
                }
                if (evaEvent != null) {
                    mLdDialog.show();
                    FriendZoneUtil.eva(getmContext(),userInfoDetailBean.getCommunityId(),evaEvent.getFrom(),userInfoDetailBean.getEmobId(),  content, evaEvent.getCircleLifeId(), new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            mLdDialog.dismiss();
                            switch (msg.what) {
                                case Config.NETERROR:
                                    showNetErrorToast();
                                    break;
                                case Config.TASKCOMPLETE:
                                    showToast("评论成功");
                                    int time = (int) (new Date().getTime() / 1000);
                                    LifeCircleDetail lifeCircleDetail = new LifeCircleDetail(msg.arg1, userInfoDetailBean.getEmobId(), evaEvent.getFrom(), userInfoDetailBean.getNickname(), evaEvent.getFromNike(), 0, time, time, evaEvent.getCircleLifeId(), content);
                                    circleBean.getLifeCircleDetails().add(lifeCircleDetail);
                                    refresh();
                                    break;
                                case Config.TASKERROR:
                                    showToast("评论失败");
                                    break;
                            }
                        }
                    });
                }
                editText.getText().clear();
                popupWindow.dismiss();
                break;
            case R.id.btn_charter:
                List<LifeCircleDetail> list = circleBean.getLifeCircleDetails();
                if (evaEvent.getCircleLifeDetialId() == 0) {
                    zambia(circleBean);
                    return;
                }
                for (int i = 0; i < list.size(); i++) {
                    if (evaEvent.getCircleLifeDetialId() == list.get(i).getLifeCircleDetailId()) {
                        zambia(list.get(i));
                    }
                }

                break;
            case R.id.tv_comment:


                List<String> photoList = Arrays.asList(circleBean.getPhotoes().split(","));
                String photoUrl = "";
                if(!photoList.isEmpty()){
                    photoUrl=photoList.get(0);
                }
                String url="";
                final Spannable spanAll = SmileUtils.getSmiledText(getApplicationContext(), circleBean.getLifeContent());
                if(circleBean.getType()==23){
                    url=Config.NET_SHAREBASE+"/share/bangzhu.html?communityId="+circleBean.getCommunityId()+"&emobId="+circleBean.getEmobId();
                    ShareProvider.getShareProvider(ZoneItemActivity.this).showShareActivity(url, "我在竞选我们小区的帮主", "邻居帮帮", ShareProvider.CODE_LIFECRILE);
                }else{
                    url = Config.NET_SHAREBASE+"/share/lifecircle.html?communityId="+circleBean.getCommunityId()+"&emobId="+circleBean.getEmobId()+"&lifeCircleId="+circleBean.getLifeCircleId();
                    if(circleBean.getType()!=2&&!"".equals(spanAll.toString())){//这张图片很赞，快来看看
                        ShareProvider.getShareProvider(ZoneItemActivity.this).showShareActivity(url, spanAll.toString(), "邻居帮帮", ShareProvider.CODE_LIFECRILE,photoUrl);
                    }else if(!"".equals(spanAll.toString())){
                        ShareProvider.getShareProvider(ZoneItemActivity.this).showShareActivity(url, spanAll.toString(), "邻居帮帮", ShareProvider.CODE_LIFECRILE,photoUrl);
                    }else{
                        ShareProvider.getShareProvider(ZoneItemActivity.this).showShareActivity(url, "热心邻居分享的，很不错！", "邻居帮帮", ShareProvider.CODE_LIFECRILE,photoUrl);
                    }
                }
//                final String url = Config.NET_BASE+"/jsp/app/share/lifecircle.jsp?communityId="+circleBean.getCommunityId()+"&emobId="+circleBean.getEmobId
//                ()+"&lifeCircleId="+circleBean.getLifeCircleId();
//                final Spannable spanAll = SmileUtils.getSmiledText(getApplicationContext(), circleBean.getLifeContent());
//                final Spannable spanExtAll = SmileUtils.getSmiledText(getApplicationContext(), (circleBean.getExtContent() + "" + circleBean.getLifeContent()));
//                if(circleBean.getType()!=2&&!"".equals(spanAll.toString())){//这张图片很赞，快来看看
//                    shareShow(url, spanAll.toString());
//                }else if(!"".equals(spanExtAll.toString())){
//                    shareShow(url, spanExtAll.toString());
//                }else{
//                    shareShow(url, spanAll.toString());
//                }
                break;
        }
    }


    interface CircleLifeDetialService {
        ///api/v1/communities/{communityId}/circles/{emobId}/single?q={lifeCircleId}
//        @GET("/api/v1/communities/{communityId}/circles/{emobId}/single")
//        void getCircleLifeDetial(@Path("communityId") long communityId, @Path("emobId") String emobId, @QueryMap HashMap<String, Object> option, Callback<LifeCircleDetailRespone> cb);
//        @GET("/api/v1/communities/{communityId}/circles/{emobId}/single")

        @GET("/api/v3/lifeCircles/{lifeCircleId}") //// v3 2013/03/04  获取生活圈详情
        void getCircleLifeDetial(@Path("lifeCircleId") int lifeCircleId, Callback<CommonRespBean<LifeCircleBean>> cb);
    }

    public void getCircleLifeDetial(String emboId, int lifeCircleId) {
        if (TextUtils.isEmpty(emboId)) {
            finish();
            return;
        }
        mLdDialog.show();
        CircleLifeDetialService service = RetrofitFactory.getInstance().create(getmContext(),CircleLifeDetialService.class);
        Callback<CommonRespBean<LifeCircleBean>> callback = new Callback<CommonRespBean<LifeCircleBean>>() {
            @Override
            public void success(CommonRespBean<LifeCircleBean> respone, retrofit.client.Response response) {
                mLdDialog.dismiss();
                if ("yes".equals(respone.getStatus())) {
                    circleBean = respone.getData();
                    refresh();
                } else {
                    showToast("未找到该动态");
                    finish();
                }
            }
            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                showNetErrorToast();
            }
        };
        service.getCircleLifeDetial(lifeCircleId, callback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMSsoHandler ssoHandler = ShareProvider.mController.getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private void zambia(final LifeCircleDetail circleDetail) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(popupWindow.getContentView().getWindowToken(), 0);
        if (!CommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, this.getString(R.string.network_unavailable), Toast.LENGTH_SHORT).show();
            return;
        }
        ZambiaCache zambiaCache = new Select().from(ZambiaCache.class).where("emobid = ? and emobidhost = ?", circleDetail.getEmobIdFrom(), userInfoDetailBean.getEmobId()).executeSingle();
        if (zambiaCache != null) {
            if (!StrUtils.isInDay(zambiaCache.getZambiatime())) {

                zambiaCache.setZambiatime((int) (new Date().getTime() / 1000));
                circleDetail.setPraiseSum(circleDetail.getPraiseSum() + 1);
                refresh();
                zambiaCache.save();
            } else {//本地检验未通过
                showToast("同一天，同一人只能赞一次");
                return;
            }
        } else {
            circleDetail.setPraiseSum(circleDetail.getPraiseSum() + 1);
            refresh();
            zambiaCache = new ZambiaCache();
            zambiaCache.setEmobid(circleDetail.getEmobIdFrom());
            zambiaCache.setZambiatime((int) (new Date().getTime() / 1000));
            zambiaCache.setEmobidhost(userInfoDetailBean.getEmobId());
            zambiaCache.save();
        }

        FriendZoneUtil.zambia(circleDetail.getEmobIdFrom(), circleDetail.getLifeCircleId(), circleDetail.getLifeCircleDetailId(), 2, ZoneItemActivity.this, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Config.TASKCOMPLETE:
                        Toast.makeText(ZoneItemActivity.this, getString(R.string.praise), Toast.LENGTH_LONG).show();
                        break;
                    case Config.NETERROR:
                        showNetErrorToast();
                        new Delete().from(ZambiaCache.class).where("emobid = ? and emobidhost = ?", circleDetail.getEmobIdFrom(), userInfoDetailBean.getEmobId()).execute();
                        break;
                    case Config.TASKERROR:
                        showToast("同一天，同一人只能赞一次");
                        break;
                }

            }
        });
    }


    private void zambia(final LifeCircleBean circleBean) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(popupWindow.getContentView().getWindowToken(), 0);
        if (!CommonUtils.isNetWorkConnected(this)) {
            showNetErrorToast();
            return;
        }
        ZambiaCache zambiaCache = new Select().from(ZambiaCache.class).where("emobid = ? and emobidhost = ?", circleBean.getEmobId(), userInfoDetailBean.getEmobId()).executeSingle();
        if (zambiaCache != null) {
            if (!StrUtils.isInDay(zambiaCache.getZambiatime())) {
                zambiaCache.setZambiatime((int) (new Date().getTime() / 1000));
                circleBean.setPraiseSum(circleBean.getPraiseSum() + 1);
                tv_zambia_host.setText(circleBean.getPraiseSum() + "\t 赞人品");
                zambiaCache.save();
            } else {//本地检验未通过
                Toast.makeText(this, "同一天，同一人只能赞一次", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            circleBean.setPraiseSum(circleBean.getPraiseSum() + 1);
            tv_zambia_host.setText(circleBean.getPraiseSum() + "\t 赞人品");
            zambiaCache = new ZambiaCache();
            zambiaCache.setEmobid(circleBean.getEmobId());
            zambiaCache.setEmobidhost(userInfoDetailBean.getEmobId());
            zambiaCache.setZambiatime((int) (new Date().getTime() / 1000));
            zambiaCache.save();
        }
        FriendZoneUtil.zambia(circleBean.getEmobId(), 0, 1, this, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Config.TASKCOMPLETE:
                        Toast.makeText(ZoneItemActivity.this, getString(R.string.praise), Toast.LENGTH_LONG).show();
                        break;
                    case Config.NETERROR:
                        showNetErrorToast();
                        new Delete().from(ZambiaCache.class).where("emobid = ? and emobidhost = ?", circleBean.getEmobId(), userInfoDetailBean.getEmobId()).execute();
                        break;
                    case Config.TASKERROR:
                        showToast("同一天，同一人只能赞一次");
                        break;
                }
            }
        });
    }

    ///http://114.215.105.202/api/v1/communities/{communityId}/circles/{lifeCircleId}
    interface RPValueService {
//        @DELETE("/api/v1/communities/{communityId}/circles/{lifeCircleId}")
//        void getRPValue(@Path("communityId") long communityId, @Path("lifeCircleId") int lifeCircleId, Callback<StatusBean> cb);
//        @DELETE("/api/v1/communities/{communityId}/circles/{lifeCircleId}")
        @PUT("/api/v3/lifeCircles")
        void getCircleById(@Body LifeCircleDelReqBean lifeCircleDelReqBean, Callback<CommonRespBean<String>> cb);
    }

    /**
     * 删除生活圈
     * @param lifeCircleId
     * @param handler
     */
    public void deleteMyCircle(int lifeCircleId, final Handler handler) {
        mLdDialog.show();
        LifeCircleDelReqBean lifeCircleDelReqBean = new LifeCircleDelReqBean();
        lifeCircleDelReqBean.setEmobId(emobId);
        lifeCircleDelReqBean.setLifeCircleId(lifeCircleId);
        RPValueService service = RetrofitFactory.getInstance().create(getmContext(),lifeCircleDelReqBean,RPValueService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, retrofit.client.Response response) {
                mLdDialog.dismiss();
                if ("yes".equals(bean.getStatus())) {
                    handler.sendEmptyMessage(115);
                } else {
                    handler.sendEmptyMessage(0);
                }
            }
            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                handler.sendEmptyMessage(0);
            }
        };
        service.getCircleById(lifeCircleDelReqBean, callback);
    }

    public void showDeleteDialog(final Context context, final int lifeCircleId) {
        final Dialog noticeDialog = new Dialog(context, R.style.MyDialogStyle);
        noticeDialog.setContentView(R.layout.dialog_deletecircle);
        TextView tv_cancle = (TextView) noticeDialog.findViewById(R.id.tv_cancle);
        TextView tv_relogin = (TextView) noticeDialog.findViewById(R.id.tv_relogin);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noticeDialog.dismiss();
            }
        });
        tv_relogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMyCircle(lifeCircleId,  handler);
                noticeDialog.dismiss();
            }
        });
        noticeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        noticeDialog.setCancelable(false);
        noticeDialog.show();
    }

    private void initPopupWindow2() {
        View evaview = View.inflate(this, R.layout.life_circle_eva_popwindow, null);
        popupWindowEva = new PopupWindow(evaview, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        tv_zan_rpvalue = (TextView) evaview.findViewById(R.id.tv_zan_rpvalue);
        tv_eva_ta = (TextView) evaview.findViewById(R.id.tv_eva_ta);
        popupWindowEva.setFocusable(false);
        popupWindowEva.setOutsideTouchable(true);
        popupWindowEva.setBackgroundDrawable(new BitmapDrawable());

        tv_zan_rpvalue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<LifeCircleDetail> list = circleBean.getLifeCircleDetails();
                if (evaEvent.getCircleLifeDetialId() == 0) {
                    zambia(circleBean);
                    return;
                }
                for (int i = 0; i < list.size(); i++) {
                    if (evaEvent.getCircleLifeDetialId() == list.get(i).getLifeCircleDetailId()) {
                        zambia(list.get(i));
                    }
                }
                popupWindowEva.dismiss();
            }
        });
        tv_eva_ta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAtLocation(root, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                if (ll_face_container != null) {
                    send_img_checkbox.setChecked(false);
                    ll_face_container.setVisibility(View.GONE);
                    showKeyBoard();
                }
                popupWindowEva.dismiss();
            }
        });
    }

    private void showPopupWindowEva(View view, String nickname) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
//        Log.i("debbug", "nickname=" + nickname);
//        tv_coyp_txt.setVisibility(View.GONE);
//        tv_coyp_txt.setText(nickname);
//        tv_coyp_txt.setVisibility(View.INVISIBLE);
//        Log.i("debbug", "nicknamesize=" + tv_coyp_txt.getMeasuredWidth());

        Paint paint = new Paint();
        paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.lifecircle_eva_txt_size));
        float strWidth = paint.measureText(nickname);
        Log.i("debbug", "nicknamesize=" + strWidth);
        popupWindowEva.showAtLocation(view, Gravity.NO_GRAVITY, (int) (location[0] + strWidth), location[1] - view.getHeight() / 2);
    }


}
