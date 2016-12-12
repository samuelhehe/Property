package xj.property.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import xj.property.activity.LifeCircle.MyLifeCircleActivity;
import xj.property.activity.LifeCircle.MyPraiseActivity;
import xj.property.activity.repair.RepairChatActivity;
import xj.property.activity.tags.MyOwnerTagsManagerDialog;
import xj.property.activity.tags.MyTagsManagerDialog;
import xj.property.activity.takeout.SuperMarketChatActivity;
import xj.property.beans.MyTagsRespBean;
import xj.property.beans.RPValueAllBean;
import xj.property.beans.SysDefaultTagsV3Bean;
import xj.property.beans.UserGroupBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseTagUtils;
import xj.property.netbase.NetBaseUtils;
import xj.property.utils.ToastUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.message.XJContactHelper;
import xj.property.utils.other.BlackListHelper;
import xj.property.utils.other.Config;
import xj.property.utils.other.FriendZoneUtil;
import xj.property.utils.other.LifeCircleBlackListHelper;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;
import xj.property.widget.FilterFlowLayout;

/**
 * Created by Administrator on 2015/4/9.
 */
public class UserGroupInfoActivity extends HXBaseActivity
        implements MyTagsManagerDialog.onTagsA2BAddStatusListener, MyOwnerTagsManagerDialog.onTagsA2BAddStatusListener {
    private ImageView ivAvatar;
    private TextView tvName, tv_person_value, tv_percent, tv_lifecircle_count, tv_his, tv_her, lifecircle_his, lifecircle_her;
    private LinearLayout contentL, ll_zan, ll_anim, ll_add, ll_rp_value;
    private UserGroupBean bean;
    /**
     * 加入黑名单imageView
     */
    private ImageView iv_switch_block_notify, iv_switch_block_lifecircle, iv_switch_unblock_lifecircle;
    /**
     * 取消黑名单imageview
     */
    private ImageView iv_switch_unblock_notify;
    private RelativeLayout rl_switch_block_groupnotify, rl_other_lifecircle, rl_switch_block_lifecircle;
    private String emobid = "", sort = "", avatar = "", singleture = "", address = "", nikeName = "", phone, gender;

    Animation in = new AlphaAnimation(0.0f, 1.0f);   //设置透明度变化动画
    Animation stay = new AlphaAnimation(1.0f, 1.0f);   //设置透明度变化动画
    Animation out = new AlphaAnimation(1.0f, 0.0f);   //设置透明度变化动画

    private ScrollView sl_scroll;

    private int rpvalue;
    private String rppercent;
    ///人品值布局
    private RelativeLayout groupuserinfo_rpz_rlay;

    //// 标签布局
    private FilterFlowLayout tags_flay;

    private MyTagsManagerDialog myTagsManagerDialog;
    private MyOwnerTagsManagerDialog myTagsManagerDialog2;

    private List<String> systemDefaulttags = new ArrayList<>();
    private View user_tags_add_rlay; /// 添加标签
    private ImageView iv_user_type; /// 用户类别
    private UserInfoDetailBean userbean;
    //牛人图表
    private LinearLayout ll_genius;
    private TextView tv_genius_message;
    private ImageView iv_genius_more;
    private LinearLayout ll_genius_more;
    private ImageView iv_genius_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags_groupuserinfo);
        userbean = PreferencesUtil.getLoginInfo(UserGroupInfoActivity.this);

        tv_his = (TextView) findViewById(R.id.tv_his);
        tv_her = (TextView) findViewById(R.id.tv_her);
        lifecircle_his = (TextView) findViewById(R.id.lifecircle_his);
        lifecircle_her = (TextView) findViewById(R.id.lifecircle_her);
        ll_genius = (LinearLayout) findViewById(R.id.ll_genius);
        tv_genius_message = (TextView) findViewById(R.id.tv_genius_message);
        iv_genius_more = (ImageView) findViewById(R.id.iv_genius_more);
        ll_genius_more = (LinearLayout) findViewById(R.id.ll_genius_more);
        iv_genius_title = (ImageView) findViewById(R.id.iv_genius_title);
        contentL = (LinearLayout) findViewById(R.id.ll_mine);
        groupuserinfo_rpz_rlay = (RelativeLayout) this.findViewById(R.id.groupuserinfo_rpz_rlay);

        tvName = (TextView) findViewById(R.id.tv_name_user);
        ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
        iv_user_type = (ImageView) findViewById(R.id.iv_user_type);
        tags_flay = (FilterFlowLayout) findViewById(R.id.tags_flay);

        user_tags_add_rlay = findViewById(R.id.user_tags_add_rlay);
        user_tags_add_rlay.setOnClickListener(new MyAddTagsOnClickListener());

        bean = (UserGroupBean) getIntent().getSerializableExtra(Config.INTENT_PARMAS1);

        if (bean != null) {
            emobid = bean.getEmobId();
            mLdDialog.show();
            // FriendZoneUtil.getRPValue(emobid, this, handler);
            // initBean();
            //-1 bushi-1 buxianshi

            Log.i("UserGroupInfoActivity ", "userGroupInfo bean != null emobid " + emobid);
        } else {

            emobid = getIntent().getStringExtra(Config.INTENT_PARMAS2);
            Log.i("UserGroupInfoActivity ", "userGroupInfo emobid " + emobid);
            mLdDialog.show();
            // sort=XJContactHelper.selectContact(emobid).sort;
        }
        NetBaseUtils.extractUserInfo2(UserGroupInfoActivity.this, PreferencesUtil.getCommityId(getmContext()), emobid, new NetBaseUtils.NetRespListener<CommonRespBean<UserGroupBean>>() {
            @Override
            public void successYes(CommonRespBean<UserGroupBean> commonRespBean, Response response) {
                bean = commonRespBean.getData();
                initBean();
            }

            @Override
            public void successNo(CommonRespBean<UserGroupBean> commonRespBean, Response response) {
                showDataErrorToast(commonRespBean.getMessage());
                finish();
            }
            @Override
            public void failure(RetrofitError error) {
                showDataErrorToast();
                finish();
            }
        });
        ivAvatar.setOnClickListener(this);

        //getRPValue(this);
    }


    @Override
    public void onTagsA2BAddSuccess(String message) {

        /// 刷新用户标签
        getUserTagsInfo(emobid);

//        showToast("添加标签成功");
    }

    @Override
    public void onTagsA2BAddFail(String message) {

        if (TextUtils.isEmpty(message)) {
            showToast("添加标签失败");
        }
    }

    /**
     *
     * 获取用户信息
     *
     * @param emobid
     */
    public void getUserTagsInfo(String emobid) {
        NetBaseTagUtils.getUserTagsInfo(getmContext(), emobid, new NetBaseTagUtils.NetRespListener<CommonRespBean<List<MyTagsRespBean.InfoEntity>>>() {
            @Override
            public void success(CommonRespBean<List<MyTagsRespBean.InfoEntity>> bean, Response response) {
                if (TextUtils.equals("yes", bean.getStatus())) {
                    List<MyTagsRespBean.InfoEntity> info = bean.getData();
                    initMyTags(getmContext(), info);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void initMyTags(final Context context, final List<MyTagsRespBean.InfoEntity> info) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View common_tags_add = inflater.inflate(R.layout.common_tags_add_item, null);
        common_tags_add.setOnClickListener(new MyAddTagsOnClickListener());

        if (info == null || info.isEmpty()) {
            Log.d("initMyTags  ", "initMyTags is null 该用户无标签 ");
            if (tags_flay != null) {
                tags_flay.removeAllViews();
                tags_flay.addView(common_tags_add);
            }
            return;
        }

        if (tags_flay != null) {
            tags_flay.removeAllViews();
            tags_flay.addView(common_tags_add);

            for (final MyTagsRespBean.InfoEntity bean : info) {
                View common_tags_item = inflater.inflate(R.layout.common_tags_item, null);
                TextView common_tags_name_tv = (TextView) common_tags_item.findViewById(R.id.common_tags_name_tv);
                TextView common_tags_nums_tv = (TextView) common_tags_item.findViewById(R.id.common_tags_nums_tv);
                common_tags_name_tv.setText(bean.getLabelContent());
                common_tags_nums_tv.setText(bean.getCount());
                common_tags_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(context, ActivityMyTagsList.class);
//                        intent.putExtra("tagContent", bean.getLabelContent());
//                        intent.putExtra("tagEmobId", emobid );
//
//                        startActivity(intent);
//                        2015/11/17
                    }
                });
                tags_flay.addView(common_tags_item);
            }
        } else {
            Log.d("tags_flay  ", "tags_flay is null  ");
        }
    }

    /**
     * 获取系统默认Tags
     */
    private void getSystemDefaultTags() {
        HashMap<String, String> option = new HashMap<String, String>();
        option.put("communityId", bean.getCommunityId() + "");
        option.put("time", "" + PreferencesUtil.getLastReqTagsTime(getmContext()));
        NetBaseTagUtils.getSysDefaultTags(bean.getEmobId(), getmContext(), option, new NetBaseTagUtils.NetRespListener<CommonRespBean<SysDefaultTagsV3Bean>>() {
            @Override
            public void success(CommonRespBean<SysDefaultTagsV3Bean> tagsbean, Response response) {
                if (tagsbean != null) {
                    if (TextUtils.equals("yes", tagsbean.getStatus())) {
                        systemDefaulttags = tagsbean.getData().getList();
                        if (systemDefaulttags == null || systemDefaulttags.isEmpty() || systemDefaulttags.size() < 1) {
                            systemDefaulttags = PreferencesUtil.getNewSysTags(getmContext(), bean.getEmobId());
                        }
                    } else {
                        systemDefaulttags = PreferencesUtil.getNewSysTags(getmContext(), bean.getEmobId());
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                showNetErrorToast();
            }
        });
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mLdDialog.dismiss();

                    /// TODO  看是否需要加载个人信息在小区内的百分比 没有提供接口
//                    RPValueAllBean bean = (RPValueAllBean) msg.obj;
//                    tv_person_value.setText("" + bean.getInfo().getCharacterValues());
//                    try {
//                        tv_percent.setText("打败了" + StrUtils.getPrecent(bean.getInfo().getCharacterPercent()) + "%的本小区居民！");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    break;
                case 0:
                    mLdDialog.dismiss();
                    showToast("请求失败");
                    break;
                case Config.TASKCOMPLETE:
                    Toast.makeText(UserGroupInfoActivity.this, R.string.praise, Toast.LENGTH_SHORT).show();

                  /*  ll_add.clearAnimation();
                    ll_add.setVisibility(View.VISIBLE);
                    in.setDuration(1000);
                    stay.setDuration(1200);
                    out.setDuration(800);
                    AnimationSet set = new AnimationSet(true);    //创建动画集对象
                    set.addAnimation(in);
                    set.addAnimation(stay);
                    set.addAnimation(out);
                    set.setFillAfter(true);                 //停留在最后的位置
                    set.setFillEnabled(true);
                    ll_add.setAnimation(set);
                    set.startNow();*/


                    break;
                case Config.TASKERROR:
                    /*ll_add.clearAnimation();
                    ll_add.setVisibility(View.VISIBLE);
                    in.setDuration(1000);
                    stay.setDuration(1200);
                    out.setDuration(800);
                    AnimationSet set2 = new AnimationSet(true);    //创建动画集对象
                    set2.addAnimation(in);
                    set2.addAnimation(stay);
                    set2.addAnimation(out);
                    set2.setFillAfter(true);                 //停留在最后的位置
                    set2.setFillEnabled(true);
                    ll_add.setAnimation(set2);
                    set2.startNow();*/
                    showToast("一天只能赞一次哦！");
                    break;
            }
        }
    };

    private void initBean() {
        if (TextUtils.isEmpty(bean.getSort())) {

            sort = "-1";
            nikeName = bean.getNickname();
            avatar = bean.getAvatar();
            address = bean.getUserFloor();
            singleture = bean.getSignature();
            gender = bean.getGender();
            Log.d("initBean  ", "bean " + bean.toString() + "  ");

        } else {
            sort = bean.getSort();
            nikeName = bean.getShopName();
            avatar = bean.getLogo();
            address = bean.getAddress();
            singleture = bean.getShopsDesc();
            phone = bean.getPhone();
        }

        if ("famous".equals(userbean.getIdentity()) && "famous".equals(bean.getIdentity())) {
            ll_genius.setVisibility(View.VISIBLE);
            tv_genius_message.setText(bean.getFamousIntroduce());
            ll_genius.setOnClickListener(new View.OnClickListener() {
                boolean isMore = false;

                @Override
                public void onClick(View v) {//tags_me_less
                    if (!isMore) {
                        if (tv_genius_message.getLayout().getLineCount() > 2) {
                            tv_genius_message.setLines(tv_genius_message.getLayout().getLineCount());
                            iv_genius_more.setBackgroundDrawable(getResources().getDrawable(R.drawable.tags_me_less));
                            isMore = true;
                        }
                    } else {
                        iv_genius_more.setBackgroundDrawable(getResources().getDrawable(R.drawable.tags_me_more));
                        tv_genius_message.setLines(2);
                        isMore = false;
                    }

                }
            });
            iv_genius_title.setVisibility(View.VISIBLE);
        } else {
            ll_genius.setVisibility(View.GONE);
            iv_genius_title.setVisibility(View.GONE);
        }
        XJContactHelper.saveContact(emobid, nikeName, avatar, sort);
        tvName.setText(nikeName);
        tv_person_value = (TextView) findViewById(R.id.tv_person_value);
        tv_percent = (TextView) findViewById(R.id.tv_percent);
        tv_lifecircle_count = (TextView) findViewById(R.id.tv_lifecircle_count);


        sl_scroll = (ScrollView) findViewById(R.id.sl_scroll);
        ll_anim = (LinearLayout) findViewById(R.id.ll_anim);
        ll_anim.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return sl_scroll.dispatchTouchEvent(event);
            }
        });

        ll_add = (LinearLayout) findViewById(R.id.ll_add);
        ll_zan = (LinearLayout) findViewById(R.id.ll_zan);
        ll_zan.setOnClickListener(this);

        rl_other_lifecircle = (RelativeLayout) findViewById(R.id.rl_other_lifecircle);
        rl_other_lifecircle.setOnClickListener(this);

        ll_rp_value = (LinearLayout) findViewById(R.id.ll_rp_value);
        ll_rp_value.setOnClickListener(this);


        rl_switch_block_lifecircle = (RelativeLayout) findViewById(R.id.rl_switch_block_lifecircle);
        iv_switch_block_lifecircle = (ImageView) findViewById(R.id.iv_switch_block_lifecircle);
        iv_switch_unblock_lifecircle = (ImageView) findViewById(R.id.iv_switch_unblock_lifecircle);
        rl_switch_block_lifecircle.setOnClickListener(this);


        rl_switch_block_groupnotify = (RelativeLayout) findViewById(R.id.rl_switch_block_notify);
        iv_switch_block_notify = (ImageView) findViewById(R.id.iv_switch_block_notify);
        iv_switch_unblock_notify = (ImageView) findViewById(R.id.iv_switch_unblock_notify);
        rl_switch_block_groupnotify.setOnClickListener(this);
        if (PreferencesUtil.getBlackList(this).contains(emobid)) {
            iv_switch_block_notify.setVisibility(View.VISIBLE);
            iv_switch_unblock_notify.setVisibility(View.INVISIBLE);
        }

        if (PreferencesUtil.getLifeCircleBlackList(this).contains(emobid)) {
            iv_switch_block_lifecircle.setVisibility(View.VISIBLE);
            iv_switch_unblock_lifecircle.setVisibility(View.INVISIBLE);
        }
        // Log.i("onion","sort:"+sort);
        if (!"-1".equals(sort)) {
            rl_other_lifecircle.setVisibility(View.GONE);
            rl_switch_block_lifecircle.setVisibility(View.GONE);
            ll_rp_value.setVisibility(View.GONE);
            ll_zan.setVisibility(View.GONE);

        } else {
            rl_other_lifecircle.setVisibility(View.VISIBLE);
            rl_switch_block_lifecircle.setVisibility(View.VISIBLE);
            ll_rp_value.setVisibility(View.VISIBLE);
        }
        initTitle(null, "成员资料", "");

        ImageLoader.getInstance().displayImage(avatar, ivAvatar, new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .showImageOnFail(R.drawable.head_portrait_personage).showImageForEmptyUri(R.drawable.head_portrait_personage).build());
        //// 初始化帮主身份横条奖章
        initBangzhuMedal(bean);


        //性别 ，地址，个性签名
//        if (phone == null)
//            contentL.addView(addItem("性别", gender.equals("m") ? "男" : "女", false, true, 0, true));
//        else contentL.addView(addItem("电话", phone, false, true, 0, true));


        if (TextUtils.equals(gender, "m")) {
            contentL.addView(addItem("性别", "男", false, true, 0, true));
        } else if (TextUtils.equals(gender, "f")) {
            contentL.addView(addItem("性别", "女", false, true, 0, true));
        } else {
            contentL.addView(addItem("性别", "保密", false, true, 0, true));
        }

        if (PreferencesUtil.getLoginInfo(getmContext()) != null && PreferencesUtil.getLoginInfo(getmContext()).getEmobId() != null &&
                TextUtils.equals(emobid, PreferencesUtil.getLoginInfo(getmContext()).getEmobId())) {
            contentL.addView(addItem("地址", address + "", false, true, 0, true)); /// 自己信息自己可见
            findViewById(R.id.tv_sendmessage).setVisibility(View.GONE); /// 不可以给自己发送消息
        } else {
            findViewById(R.id.tv_sendmessage).setVisibility(View.VISIBLE);
        }
        contentL.addView(addItem(phone == null ? "个性签名" : "介绍", singleture, false, true, 0, true));

        findViewById(R.id.tv_sendmessage).setOnClickListener(this);

        tv_lifecircle_count.setText(bean.getLifeCircleSum() + "条");
        tv_person_value.setText("" + bean.getCharacterValues());
        try {

            tv_percent.setText("打败了" + StrUtils.getPrecent(bean.getCharacterPercent()) + "%的" + PreferencesUtil.getCommityName(this) + "小区居民！");
        } catch (Exception e) {

        }

        changeGender(gender);

        getUserTagsInfo(emobid);

        getSystemDefaultTags();

        mLdDialog.dismiss();

    }


    /**
     * 初始化用户横条奖章图片
     */
    private void initBangzhuMedal(UserGroupBean userGroupBean) {
        String userType = userGroupBean.getGrade();
        if (iv_user_type != null) {
//            normal , bangzhu, fubangzhu ,zhanglao,bangzhong
            if (TextUtils.equals(userType, Config.USER_TYPE_ZHANGLAO)) {
                iv_user_type.setImageDrawable(getResources().getDrawable(R.drawable.me_zhanglao_icon));
                iv_user_type.setVisibility(View.VISIBLE);
            } else if (TextUtils.equals(userType, Config.USER_TYPE_BANGZHU)) {
                iv_user_type.setImageDrawable(getResources().getDrawable(R.drawable.me_bangzhu_icon));
                iv_user_type.setVisibility(View.VISIBLE);
            } else if (TextUtils.equals(userType, Config.USER_TYPE_FUBANGZHU)) {
                iv_user_type.setImageDrawable(getResources().getDrawable(R.drawable.me_fubangzhu_icon));
                iv_user_type.setVisibility(View.VISIBLE);
            } else {
                iv_user_type.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (!PreferencesUtil.getLogin(this)) {
            startActivity(new Intent(this, RegisterLoginActivity.class));
            return;
        }
        userbean = PreferencesUtil.getLoginInfo(getmContext());
        switch (v.getId()) {
            case R.id.rl_switch_block_lifecircle:
                if (TextUtils.equals(userbean.getEmobId(), emobid)) {
                    showToast("不能将自己加入黑名单");
                    return;
                }
                if (iv_switch_unblock_lifecircle.getVisibility() == View.VISIBLE) {//加入黑名单 //
                    mLdDialog.show();
                    LifeCircleBlackListHelper.addBlack(this, emobid, new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            mLdDialog.dismiss();
                            switch (msg.what) {
                                case Config.ADDBLACKLIST:
                                    List<String> list = PreferencesUtil.getLifeCircleBlackList(UserGroupInfoActivity.this);
                                    list.add(emobid);
                                    PreferencesUtil.saveLifeCircleBlackList(UserGroupInfoActivity.this, list);
                                    iv_switch_unblock_lifecircle.setVisibility(View.INVISIBLE);
                                    iv_switch_block_lifecircle.setVisibility(View.VISIBLE);
                                    showToast("已加入生活圈黑名单");
                                    break;
                                case Config.TASKERROR:
                                    showToast("加入生活圈黑名单失败");
                                    break;
                                case Config.NETERROR:
                                    showNetErrorToast();
                                    break;
                            }
                        }
                    });
                } else {//取消黑名单
                    mLdDialog.show();
                    LifeCircleBlackListHelper.removeBlack(this, emobid, new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            mLdDialog.dismiss();
                            switch (msg.what) {
                                case Config.REMOVEBLACKLIST:
                                    List<String> list = PreferencesUtil.getLifeCircleBlackList(UserGroupInfoActivity.this);
                                    if (list.contains(emobid)) list.remove(emobid);
                                    PreferencesUtil.saveLifeCircleBlackList(UserGroupInfoActivity.this, list);
                                    iv_switch_unblock_lifecircle.setVisibility(View.VISIBLE);
                                    iv_switch_block_lifecircle.setVisibility(View.INVISIBLE);
                                    showToast("移除黑名单");
                                    break;
                                case Config.TASKERROR:
                                    showToast("移除黑名单失败");
                                    break;
                                case Config.NETERROR:
                                    showNetErrorToast();
                                    break;
                            }
                        }
                    });
                }
                break;
            case R.id.ll_zan:

                FriendZoneUtil.zambia(emobid, 0, 1, UserGroupInfoActivity.this, handler);
                break;

            case R.id.rl_other_lifecircle:
                Intent intentLifeCircle = new Intent(this, MyLifeCircleActivity.class);
                intentLifeCircle.putExtra("emobid", emobid);
                startActivity(intentLifeCircle);
                break;
            case R.id.ll_rp_value:
                Intent intentRPValue = new Intent(this, MyPraiseActivity.class);
                intentRPValue.putExtra("emobid", emobid);
                startActivity(intentRPValue);
                break;


            case R.id.iv_avatar:
                //   Log.i("onion","iv_avatar");

                Intent intent = new Intent();
                intent.putExtra("avatar", avatar);//tz
                intent.setClass(this, ShowBigImageActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_sendmessage:

                Intent intentPush = new Intent();
                intentPush.putExtra("userId", bean.getEmobId());//tz
                intentPush.putExtra(Config.EXPKey_nickname, nikeName);
                intentPush.putExtra(Config.EXPKey_avatar, avatar);
                /// 4,  送水  5, 维修
                if ("4".equals(sort) || "5".equals(sort))
                    intentPush.setClass(this, RepairChatActivity.class);
                else if ("-1".equals(sort)) ///
                    intentPush.setClass(this, ChatActivity.class);
                else if ("2".equals(sort)) /// 快店
                    intentPush.setClass(this, SuperMarketChatActivity.class);

                intentPush.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                startActivity(intentPush);

                break;

            case R.id.rl_switch_block_notify:
                if (TextUtils.equals(userbean.getEmobId(), emobid)) {
                    showToast("不能将自己加入黑名单");
                    return;
                }
                if (iv_switch_unblock_notify.getVisibility() == View.VISIBLE) {//加入黑名单
                    mLdDialog.show();
                    BlackListHelper.addBlack(this, emobid, new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            if (mLdDialog.isShowing()) {
                                mLdDialog.dismiss();
                            }
                            switch (msg.what) {
                                case Config.ADDBLACKLIST:
                                    List<String> list = PreferencesUtil.getBlackList(UserGroupInfoActivity.this);
                                    list.add(emobid);
                                    PreferencesUtil.saveBlackList(UserGroupInfoActivity.this, list);
                                    iv_switch_unblock_notify.setVisibility(View.INVISIBLE);
                                    iv_switch_block_notify.setVisibility(View.VISIBLE);
                                    showToast("已加入黑名单");
                                    break;
                                case Config.TASKERROR:
                                    showToast("加入黑名单失败");
                                    break;
                                case Config.NETERROR:
                                    showNetErrorToast();
                                    break;
                            }
                        }
                    });
                } else {//取消黑名单
                    mLdDialog.show();
                    BlackListHelper.removeBlack(this, emobid, new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            mLdDialog.dismiss();
                            switch (msg.what) {
                                case Config.REMOVEBLACKLIST:
                                    List<String> list = PreferencesUtil.getBlackList(UserGroupInfoActivity.this);
                                    if (list.contains(emobid)) list.remove(emobid);
                                    PreferencesUtil.saveBlackList(UserGroupInfoActivity.this, list);
                                    iv_switch_unblock_notify.setVisibility(View.VISIBLE);
                                    iv_switch_block_notify.setVisibility(View.INVISIBLE);
                                    showToast("移除黑名单");
                                    break;
                                case Config.TASKERROR:
                                    showToast("移除黑名单失败");
                                    break;
                                case Config.NETERROR:
                                    showNetErrorToast();
                                    break;
                            }
                        }
                    });
                }
                break;
        }
    }

    private LinearLayout addItem(String title, String content,
                                 boolean bMore, boolean bline, int topMargin, boolean flag) {
        LinearLayout ll = (LinearLayout) View.inflate(this,
                R.layout.item_group_uesrinfo, null);
        TextView tvTitle = (TextView) ll.findViewById(R.id.tv_mine_title);
        tvTitle.setText(title);
        TextView tvContent = (TextView) ll.findViewById(R.id.tv_mine_content);
        tvContent.setText(content);
        View mine_line = (View) ll.findViewById(R.id.mine_line);
        if (!bMore) {
            ll.findViewById(R.id.iv_more).setVisibility(View.GONE);
        }
        if (!bline) {
            ll.findViewById(R.id.mine_line).setVisibility(View.GONE);
        }
        if (topMargin > 0) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = topMargin;
            ll.setLayoutParams(params);
        }
        if (flag == false) {
            mine_line.setVisibility(View.GONE);
        }
        return ll;
    }


    public void changeGender(String m) {

        if ("m".equals(m)) {//nan
            lifecircle_his.setVisibility(View.VISIBLE);
            tv_his.setVisibility(View.VISIBLE);
            lifecircle_her.setVisibility(View.GONE);
            tv_her.setVisibility(View.GONE);

        } else {
            lifecircle_his.setVisibility(View.GONE);
            tv_his.setVisibility(View.GONE);
            lifecircle_her.setVisibility(View.VISIBLE);
            tv_her.setVisibility(View.VISIBLE);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLdDialog != null) {
            mLdDialog.dismiss();
        }
        if (myTagsManagerDialog != null) {
            myTagsManagerDialog.dismiss();
        }
        if (myTagsManagerDialog2 != null) {
            myTagsManagerDialog2.dismiss();
        }
    }


    private class MyAddTagsOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            String localemobId = PreferencesUtil.getLoginInfo(getmContext()).getEmobId();

            if (TextUtils.equals(localemobId, bean.getEmobId())) {
                myTagsManagerDialog2 = new MyOwnerTagsManagerDialog(getmContext(),
                        systemDefaulttags,
                        "" + bean.getCommunityId(),
                        bean.getEmobId(),
                        bean.getEmobId(),
                        UserGroupInfoActivity.this);
                WindowManager windowManager = getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = myTagsManagerDialog2.getWindow().getAttributes();
                lp.width = (int) (display.getWidth()); //设置宽度
                myTagsManagerDialog2.getWindow().setAttributes(lp);

            } else {
                myTagsManagerDialog = new MyTagsManagerDialog(getmContext(), systemDefaulttags, "" + bean.getCommunityId(), PreferencesUtil.getLoginInfo(getmContext()).getEmobId(), emobid, UserGroupInfoActivity.this);
                myTagsManagerDialog.show();
                WindowManager windowManager = getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = myTagsManagerDialog.getWindow().getAttributes();
                lp.width = (int) (display.getWidth()); //设置宽度
                myTagsManagerDialog.getWindow().setAttributes(lp);
            }
        }
    }
}
