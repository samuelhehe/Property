package xj.property.activity.bangzhu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.activity.runfor.MottoRunForActivity;
import xj.property.activity.runfor.RunForActivity;
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.beans.BangInternalNews;
import xj.property.beans.BangZhuInterElectionJoinResult;
import xj.property.beans.BangZhuJoinElectionReq;
import xj.property.beans.ElectionBangZhuBean;
import xj.property.beans.NeedInviteNums;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;


/**
 * 帮主竞选页面
 */
public class ActivityBangZhuElection extends HXBaseActivity {


    private TextView tv_right_text;

    private UserInfoDetailBean bean;

    public static final String log_tag = "ActivityBangZhuElection";


    /// 竞选图: 帮主头像信息
    private View bangzhu_election_bangzhu;

    /// 竞选图: 帮主头像
    private ImageView bangzhu_iv_avatar;

    /// 竞选图: 帮主名称
    private TextView bangzhu_bangzhu_election_graph_myname_tv;
    /// 竞选图: 帮主人品值
    private TextView bangzhu_bangzhu_election_graph_myrpz_tv;


    /// 竞选图: 副帮主头像信息 1
    private View f1bangzhu_election_bangzhu;

    /// 竞选图: 副帮主头像 1
    private ImageView f1bangzhu_iv_avatar;

    /// 竞选图: 副帮主名称 1
    private TextView f1bangzhu_bangzhu_election_graph_myname_tv;
    /// 竞选图: 副帮主人品值 1
    private TextView f1bangzhu_bangzhu_election_graph_myrpz_tv;


    /// 竞选图: 副帮主头像信息 2
    private View f2bangzhu_election_bangzhu;

    /// 竞选图: 副帮主头像 2
    private ImageView f2bangzhu_iv_avatar;

    /// 竞选图: 副帮主名称 2
    private TextView f2bangzhu_bangzhu_election_graph_myname_tv;
    /// 竞选图: 副帮主人品值 2
    private TextView f2bangzhu_bangzhu_election_graph_myrpz_tv;


    /// 长老信息横向布局
    private LinearLayout bangzhu_election_zhanglao_llay;


    /// 马上竞选
//    private Button bangzhu_election_rightnow_btn;


    //   private LinearLayout bangzhu_election_rightnow_llay;

    /// 点击马上竞选,参与帮主竞选
    private TextView bangzhu_election_rightnow_tv;


    // 推荐帮帮给邻居
    private Button bangzhu_election_inviate_btn;

    ///// 我的头像
    private ImageView my_iv_avatar;

    /// 我的名字
    private TextView bangzhu_election_myname_tv;
    /// 我已经邀请注册成功人数
    private TextView bangzhu_election_ihave_invite_success_tv;
    /// 我的人品值
    private TextView bangzhu_election_myrpz_tv;

    /// 帮主细则
    private LinearLayout bangzhu_election_notice_llay;

    private LinearLayout bangzhu_election_tjneedinvite_llay;

    private View bangzhu_election_graph_rpz_llay;

    private View f1bangzhu_election_graph_rpz_llay;

    private View f2bangzhu_election_graph_rpz_llay;

    private String usertype = "normal";

    /// 查看特权
    private TextView bangzhu_election_seeprivilege_tv;


    //// 总体滚动内容view
    private ScrollView srcoll_root;
    private Handler handler = new Handler();

    private LinearLayout bangzhu_election_privilege_llay;
    //// 去投票
    private Button bangzhu_election_rightnow_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bang_zhu_election);
        bean = PreferencesUtil.getLoginInfo(this);
        usertype = PreferencesUtil.getUserType(this);

        initView();
        initData();

    }

    private void initData() {
        /// 获取帮主图信息
        getBangzhuInfo();
        /// 获取帮内最消息
        getBangInternalNewss();
        /// 获取需要邀请的人数.
//        getNeedInviteNums();
        /// 初始化我得头像图标
//        initMyInfo();
    }

    private void initMyInfo() {
        if (bean != null) {
            if (my_iv_avatar != null) {
                ImageLoader.getInstance().displayImage(bean.getAvatar(), my_iv_avatar, UserUtils.bangzhu_election_me_options);
            }

            if (bangzhu_election_myname_tv != null) {
                bangzhu_election_myname_tv.setText(bean.getNickname());
            }

            if (bangzhu_election_myrpz_tv != null) {
                /// 我的人品值
                bangzhu_election_myrpz_tv.setText("人品值" + PreferencesUtil.getRPValue(this));
//                bangzhu_election_myrpz_tv.setText("我的人品值"+PreferencesUtil.getRPValue(this));
            }
        }

    }


    private void initView() {
        initTitle();

        srcoll_root = (ScrollView) this.findViewById(R.id.srcoll_root);

        /// 帮主
        bangzhu_election_bangzhu = this.findViewById(R.id.bangzhu_election_bangzhu);
        bangzhu_iv_avatar = (ImageView) bangzhu_election_bangzhu.findViewById(R.id.iv_avatar);
        bangzhu_bangzhu_election_graph_myname_tv = (TextView) bangzhu_election_bangzhu.findViewById(R.id.bangzhu_election_graph_myname_tv);
        bangzhu_bangzhu_election_graph_myrpz_tv = (TextView) bangzhu_election_bangzhu.findViewById(R.id.bangzhu_election_graph_myrpz_tv);

        //// 帮主人品值设置
        bangzhu_election_graph_rpz_llay = bangzhu_election_bangzhu.findViewById(R.id.bangzhu_election_graph_rpz_llay);

        /// 副帮主1
        f1bangzhu_election_bangzhu = this.findViewById(R.id.bangzhu_election_fubangzhu1);
        f1bangzhu_iv_avatar = (ImageView) f1bangzhu_election_bangzhu.findViewById(R.id.iv_avatar);
        f1bangzhu_bangzhu_election_graph_myrpz_tv = (TextView) f1bangzhu_election_bangzhu.findViewById(R.id.bangzhu_election_graph_myrpz_tv);

        f1bangzhu_bangzhu_election_graph_myname_tv = (TextView) f1bangzhu_election_bangzhu.findViewById(R.id.bangzhu_election_graph_myname_tv);
        /// 副帮主1人品值设置
        f1bangzhu_election_graph_rpz_llay = f1bangzhu_election_bangzhu.findViewById(R.id.bangzhu_election_graph_rpz_llay);


        /// 副帮主2
        f2bangzhu_election_bangzhu = this.findViewById(R.id.bangzhu_election_fubangzhu2);
        f2bangzhu_iv_avatar = (ImageView) f2bangzhu_election_bangzhu.findViewById(R.id.iv_avatar);
        f2bangzhu_bangzhu_election_graph_myname_tv = (TextView) f2bangzhu_election_bangzhu.findViewById(R.id.bangzhu_election_graph_myname_tv);
        f2bangzhu_bangzhu_election_graph_myrpz_tv = (TextView) f2bangzhu_election_bangzhu.findViewById(R.id.bangzhu_election_graph_myrpz_tv);

        /// 副帮主2人品值设置
        f2bangzhu_election_graph_rpz_llay = f2bangzhu_election_bangzhu.findViewById(R.id.bangzhu_election_graph_rpz_llay);

        /// 初始化长老信息横向布局
        bangzhu_election_zhanglao_llay = (LinearLayout) this.findViewById(R.id.bangzhu_election_zhanglao_llay);
        /// 马上竞选tv
        bangzhu_election_rightnow_tv = (TextView) this.findViewById(R.id.bangzhu_election_rightnow_tv);

        /// 竞选细则一模块布局
        bangzhu_election_tjneedinvite_llay = (LinearLayout) this.findViewById(R.id.bangzhu_election_tjneedinvite_llay);


        //// 推荐帮帮给邻居 // 推荐帮帮给邻居  /// 麻黄素那个
        bangzhu_election_inviate_btn = (Button) this.findViewById(R.id.bangzhu_election_inviate_btn);
        bangzhu_election_inviate_btn.setOnClickListener(this);

        //// 去投票
        bangzhu_election_rightnow_btn = (Button) this.findViewById(R.id.bangzhu_election_rightnow_btn);
        bangzhu_election_rightnow_btn.setOnClickListener(this);


        /// 初始化我的信息
        my_iv_avatar = (ImageView) this.findViewById(R.id.my_iv_avatar);
        /// 我的名字
        bangzhu_election_myname_tv = (TextView) this.findViewById(R.id.bangzhu_election_myname_tv);
        /// 我成功邀请注册了X名
        bangzhu_election_ihave_invite_success_tv = (TextView) this.findViewById(R.id.bangzhu_election_ihave_invite_success_tv);
        /// 我的人品值
        bangzhu_election_myrpz_tv = (TextView) this.findViewById(R.id.bangzhu_election_myrpz_tv);

        /// 帮主细则
        bangzhu_election_notice_llay = (LinearLayout) this.findViewById(R.id.bangzhu_election_notice_llay);

        /// 竞选日提醒
        bangzhu_election_privilege_llay = (LinearLayout) this.findViewById(R.id.bangzhu_election_privilege_llay);

        /// 查看帮主特权
        bangzhu_election_seeprivilege_tv = (TextView) this.findViewById(R.id.bangzhu_election_seeprivilege_tv);
        //创建一个 SpannableString对象
        SpannableString sp = new SpannableString("查看帮主特权");
        //设置超链接
        sp.setSpan(new URLSpan("#"), 0, sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(Color.parseColor("#2fcc71")), 0, sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        bangzhu_election_seeprivilege_tv.setText(sp);
        bangzhu_election_seeprivilege_tv.setOnClickListener(this);


        if (bangzhu_election_rightnow_tv != null) {

            String url="以上职务每月选举一次，详情请查看 特权/规则";
            SpannableStringBuilder style = new SpannableStringBuilder(url);

            TextViewURLSpan myURLSpan = new TextViewURLSpan();
            style.setSpan(myURLSpan,url.length()-5,url.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            style.setSpan(new ForegroundColorSpan(Color.parseColor("#2fcc71")),url.length()-5,url.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            bangzhu_election_rightnow_tv.setText(style);
            bangzhu_election_rightnow_tv.setMovementMethod(LinkMovementMethod.getInstance());
            bangzhu_election_rightnow_tv.setVisibility(View.VISIBLE);
        }


//        initUserTypeStatus();

    }

    class TextViewURLSpan extends ClickableSpan {
        @Override
        public void updateDrawState(TextPaint ds) {
//            ds.setColor(getResources().getColor(R.color.c_new_blue));
//            ds.setUnderlineText(false); //去掉下划线
        }

        @Override
        public void onClick(View widget) {//点击事件
            /// 跳转至帮主特权
            startActivity(new Intent(getmContext(), ActivityBangZhuPrivilege.class));
        }
    }

    private void initUserTypeStatus() {

        usertype = PreferencesUtil.getUserType(this);

        if (TextUtils.equals(usertype, "normal")) {

            if (bangzhu_election_rightnow_tv != null) {

                String url="以上职务每月选举一次，详情请查看 特权/规则";
                SpannableStringBuilder style = new SpannableStringBuilder(url);

                TextViewURLSpan myURLSpan = new TextViewURLSpan();
                style.setSpan(myURLSpan,url.length()-5,url.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                style.setSpan(new ForegroundColorSpan(Color.parseColor("#2fcc71")),url.length()-5,url.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                bangzhu_election_rightnow_tv.setText(style);
                bangzhu_election_rightnow_tv.setMovementMethod(LinkMovementMethod.getInstance());
                bangzhu_election_rightnow_tv.setVisibility(View.VISIBLE);
            }

            if (bangzhu_election_inviate_btn != null) {
                bangzhu_election_inviate_btn.setText("马上竞选");
            }

            if (bangzhu_election_tjneedinvite_llay != null) {

                bangzhu_election_tjneedinvite_llay.setVisibility(View.GONE);
            }

        } else {

            if (bangzhu_election_rightnow_tv != null) {
                bangzhu_election_rightnow_tv.setVisibility(View.GONE);
            }

            if (bangzhu_election_inviate_btn != null) {
                bangzhu_election_inviate_btn.setText("推荐帮帮给邻居");
            }

            if (bangzhu_election_tjneedinvite_llay != null) {
                bangzhu_election_tjneedinvite_llay.setVisibility(View.VISIBLE);
            }

        }

    }


    private void initUserTypeData() {

        /// 获取需要邀请的人数.
//        getNeedInviteNums(); v3 2016/03/22
        /// 初始化我得头像图标
        initMyInfo();

    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        tv_right_text = (TextView) this.findViewById(R.id.tv_right_text);
        tv_right_text.setOnClickListener(this);
        tv_right_text.setText("特权/规则");
        tv_right_text.setVisibility(View.VISIBLE);
        this.findViewById(R.id.iv_back).setOnClickListener(this);
        /// 2015/12/16
        ((TextView)this.findViewById(R.id.tv_title)).setText("竞选帮主");
//        this.findViewById(R.id.tv_title).setBackgroundDrawable(getResources().getDrawable(R.drawable.campaign_for_bangzhu));
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_right_text:
            case R.id.bangzhu_election_seeprivilege_tv:

                /// 跳转至帮主特权
                startActivity(new Intent(this, ActivityBangZhuPrivilege.class));
                break;

            case R.id.iv_back:
                finish();
                break;

            case R.id.bangzhu_election_inviate_btn:
//                usertype = PreferencesUtil.getUserType(this);
//                if (TextUtils.equals(usertype, "normal")) {
//                    getBangZhuJoinElectionResult();
//
//
//
//                } else {
//                    // 推荐帮帮给邻居
//                    startActivity(new Intent(this, ActivityInviteNeighbors.class));
//                }

                if (PreferencesUtil.getLogin(getmContext())) {
                    /// todo 去拉票
                    startActivity(new Intent(this, MottoRunForActivity.class));
                } else {
                    startActivity(new Intent(this, RegisterLoginActivity.class));
                }
                break;

            case R.id.bangzhu_election_rightnow_btn:
                goBangzhuElection();
                break;


        }

    }

    /**
     * 去帮主竞选页面
     */
    private void goBangzhuElection() {

        if (PreferencesUtil.getLogin(getmContext())) {
            startActivity(new Intent(getmContext(), RunForActivity.class));

        } else {
            startActivity(new Intent(this, RegisterLoginActivity.class));
        }

    }

    interface BangZhuInfoService {

        /**
         * 获取帮主图信息
         *
         */
//        @GET("/api/v1/communities/{communityId}/bangzhu")
//        void getBangzhuGraphInfo(@Path("communityId") int communityId, Callback<ElectionBangZhuBean> cb);
//        @GET("/api/v1/communities/{communityId}/bangzhu")

        @GET("/api/v3/elections/bangzhu")
        void getBangzhuGraphInfo(@QueryMap HashMap<String, String> queryMap, Callback<CommonRespBean<ElectionBangZhuBean>> cb);

        /**
         * 参加帮主竞选
         *
         * @param communityId
         * @param cb
         */
        @PUT("/api/v1/communities/{communityId}/bangzhu/in")
        void getBangzhuJoinElection(@Header("signature") String signature, @Body BangZhuJoinElectionReq request, @Path("communityId") int communityId, Callback<BangZhuInterElectionJoinResult> cb);


        /**
         * 获取帮内最新消息
         *
         * @param cb
         */
//        @GET("/api/v1/communities/{communityId}/bangzhu/self")
//        void getBangInternalNews(@Path("communityId") int communityId, @QueryMap Map<String, String> map, Callback<BangInternalNews> cb);
//        @GET("/api/v1/communities/{communityId}/bangzhu/self")
//

//        /api/v3/elections/bangzhu/tips/user?communityId={小区ID}&emobId={环信ID}&time={上次获取到的变化时间}
        @GET("/api/v3/elections/bangzhu/tips/user")
        void getBangInternalNews(@QueryMap HashMap<String, String> map, Callback<CommonRespBean<BangInternalNews>> cb);



        /**
         * 获取成为帮主副帮主需要邀请的人数
         *
         * @param emobId
         * @param cb
         */
        @GET("/api/v1/users/{emobId}/neighbors/unInvite")
        void getNeedInviteNums(@Path("emobId") String emobId, Callback<NeedInviteNums> cb);

    }


    /**
     * 成为帮主,副帮主还需要邀请的人数
     */
    private void getNeedInviteNums() {
        mLdDialog.show();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        BangZhuInfoService service = restAdapter.create(BangZhuInfoService.class);
        Callback<NeedInviteNums> callback = new Callback<NeedInviteNums>() {
            @Override
            public void success(NeedInviteNums bean, retrofit.client.Response response) {
                if (bean != null && TextUtils.equals(bean.getStatus(), "yes")) {
                    initNeedInvite(bean);
                    if (bangzhu_election_ihave_invite_success_tv != null) {
                        /// 成功注册了X名用户。
                        bangzhu_election_ihave_invite_success_tv.setText("已推荐成功注册邻居" + bean.getInfo().getRegistCount() + "名");
                    }
                } else {
                    /// 默认数据..
                    initNeedInvite(null);
                    if (bangzhu_election_ihave_invite_success_tv != null) {
                        bangzhu_election_ihave_invite_success_tv.setVisibility(View.INVISIBLE); // 设置为不可见
                    }
                }
                mLdDialog.dismiss();
            }

            private void initNeedInvite(NeedInviteNums bean) {

                if (bean != null) {

                    if (bangzhu_election_notice_llay != null) {
                        bangzhu_election_notice_llay.removeAllViews();
                        /// 添加帮主, 副帮主,长老细则到细则
                        for (int i = 0; i < 3; i++) {
                            RelativeLayout bangzhu_rlay = (RelativeLayout) getLayoutInflater().inflate(R.layout.common_bangzhu_election_needinvite_bzlay, null);
                            String needNums = "";
                            String ruleContent = "";
                            int imageResource = 0;

                            if (i == 0) {
                                if (bean.getInfo().getUnInvited4Bangzhu() > 0) {
                                    needNums = "您还需要邀请 " + bean.getInfo().getUnInvited4Bangzhu() + "位邻居就可以当帮主了";
                                } else {
                                    needNums = "您已具备竞选帮主资格,每月2号竞选之前增加更多人品值获得帮主职位";
                                }

                                if (bean.getInfo().getRequire4Bangzhu() <= 0) {
                                    ruleContent = "帮主竞选根据人品值排行产生,您已具备竞选资格";
                                } else {
                                    ruleContent = "帮主竞选根据人品值排行产生,竞选资格至少" + bean.getInfo().getRequire4Bangzhu() + "名以上的邻居成功注册成为帮帮用户才有资格竞选帮主";
                                }
                                imageResource = R.drawable.bangzhu_condition;

                            } else if (i == 1) {

                                if (bean.getInfo().getUnInvited4FuBangzhu() > 0) {
                                    needNums = "您还需要邀请 " + bean.getInfo().getUnInvited4FuBangzhu() + "位邻居就可以当副帮主了";
                                } else {
                                    needNums = "您已具备竞选副帮主资格,每月2号竞选之前增加更多人品值获得副帮主职位";
                                }

                                if (bean.getInfo().getRequire4FuBangzhu() <= 0) {
                                    ruleContent = "副帮主竞选根据人品值排行产生,您已具备竞选资格";
                                } else {
                                    ruleContent = "副帮主竞选根据人品值排行产生,竞选资格至少" + bean.getInfo().getRequire4FuBangzhu() + "名以上的邻居成功注册成为帮帮用户才有资格竞选副帮主";
                                }

                                imageResource = R.drawable.fubangzhu_condition;

                            } else if (i == 2) {

                                if (bean.getInfo().getRequire4Zhanglao() <= 0) {

                                    ruleContent = "长老竞选根据人品值排行产生,您已具备竞选资格";

                                } else {
                                    ruleContent = "长老竞选根据人品值排行产生,竞选资格至少" + bean.getInfo().getRequire4FuBangzhu() + "名以上的邻居成功注册成为帮帮用户才有资格竞选长老";

                                }
                                if (bean.getInfo().getUnInvited4Zhanglao() > 0) {

                                    needNums = "您还需要邀请 " + bean.getInfo().getUnInvited4Zhanglao() + "位邻居就可以当长老了";

                                } else {
                                    needNums = "您已具备竞选长老资格,每月2号竞选之前增加更多人品值获得长老职位";
                                }

                                imageResource = R.drawable.zhanglao_condition;
                            }

                            ((TextView) bangzhu_rlay.findViewById(R.id.bangzhu_election_needinvited_nums_tv)).setText(needNums);
                            ((TextView) bangzhu_rlay.findViewById(R.id.bangzhu_election_rule_tv)).setText(ruleContent);
                            ((ImageView) bangzhu_rlay.findViewById(R.id.bangzhu_logo_iv)).setImageResource(imageResource);
                            //创建一个 SpannableString对象
                            SpannableString sp = new SpannableString("查看竞选规则");
                            //设置超链接
                            sp.setSpan(new URLSpan("#"), 0, sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            sp.setSpan(new ForegroundColorSpan(Color.parseColor("#2fcc71")), 0, sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            ((TextView) bangzhu_rlay.findViewById(R.id.bangzhu_election_see_rule_tv)).setText(sp);
                            bangzhu_rlay.findViewById(R.id.bangzhu_election_see_rule_tv).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    /// 跳转至帮主特权/规则
                                    startActivity(new Intent(getmContext(), ActivityBangZhuPrivilege.class));
                                }
                            });

                            /// 马上去邀请

                            bangzhu_rlay.findViewById(R.id.bangzhu_election_goinvite_rightnow_btn).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(getmContext(), ActivityInviteNeighbors.class));
                                }
                            });

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(0, 5, 0, 10);
                            bangzhu_election_notice_llay.addView(bangzhu_rlay, layoutParams);
                        }
                    }
                } else {
                    if (bangzhu_election_notice_llay != null) {
                        bangzhu_election_notice_llay.removeAllViews();
                        /// 添加帮主, 副帮主,长老细则到细则
                        for (int i = 0; i < 3; i++) {
                            RelativeLayout bangzhu_rlay = (RelativeLayout) getLayoutInflater().inflate(R.layout.common_bangzhu_election_needinvite_bzlay, null);

                            String needNums = "";
                            String ruleContent = "";
                            int imageResource = 0;
                            if (i == 0) {
                                needNums = "您还需要邀请3位邻居就可以当帮主了";
                                ruleContent = "帮主竞选根据人品值排行产生,竞选资格至少3名以上的邻居成功注册成为帮帮用户才有资格竞选帮主";
                                imageResource = R.drawable.bangzhu_condition;
                            } else if (i == 1) {
                                needNums = "您还需要邀请1位邻居就可以当副帮主了";
                                ruleContent = "副帮主竞选根据人品值排行产生,竞选资格至少1名以上的邻居成功注册成为帮帮用户才有资格竞选副帮主";
                                imageResource = R.drawable.fubangzhu_condition;
                            } else if (i == 2) {
                                needNums = "您现在已经可以当长老了,现在就去赚人品值吧";
                                ruleContent = "您已具备竞选长老资格,每月2号竞选之前增加更多人品值获得长老职位";
                                imageResource = R.drawable.zhanglao_condition;
                            }
                            ((TextView) bangzhu_rlay.findViewById(R.id.bangzhu_election_needinvited_nums_tv)).setText(needNums);
                            ((TextView) bangzhu_rlay.findViewById(R.id.bangzhu_election_rule_tv)).setText(ruleContent);
                            ((ImageView) bangzhu_rlay.findViewById(R.id.bangzhu_logo_iv)).setImageResource(imageResource);

                            //创建一个 SpannableString对象
                            SpannableString sp = new SpannableString("查看竞选规则");
                            //设置超链接
                            sp.setSpan(new URLSpan("#"), 0, sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            sp.setSpan(new ForegroundColorSpan(Color.parseColor("#2fcc71")), 0, sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            ((TextView) bangzhu_rlay.findViewById(R.id.bangzhu_election_see_rule_tv)).setText(sp);
                            bangzhu_rlay.findViewById(R.id.bangzhu_election_see_rule_tv).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    /// 跳转至帮主特权/规则
                                    startActivity(new Intent(getmContext(), ActivityBangZhuPrivilege.class));
                                }
                            });

                            /// 马上去邀请
                            bangzhu_rlay.findViewById(R.id.bangzhu_election_goinvite_rightnow_btn).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(getmContext(), ActivityInviteNeighbors.class));
                                }
                            });
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(0, 5, 0, 10);
                            bangzhu_election_notice_llay.addView(bangzhu_rlay, layoutParams);
                        }
                    }
                }

            }

            @Override
            public void failure(RetrofitError error) {
                //// 添加默认布局
                initNeedInvite(null);
                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        service.getNeedInviteNums(bean == null ? "null" : bean.getEmobId(), callback);
    }


    /**
     * 参加帮主竞选
     */
    private void getBangZhuJoinElectionResult() {
        mLdDialog.show();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE2).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        BangZhuInfoService service = restAdapter.create(BangZhuInfoService.class);

        Callback<BangZhuInterElectionJoinResult> callback = new Callback<BangZhuInterElectionJoinResult>() {
            @Override
            public void success(final BangZhuInterElectionJoinResult resultbean, retrofit.client.Response response) {

                if (resultbean != null && TextUtils.equals(resultbean.getStatus(), "yes")) {

                    //// 参与竞选成功
                    showMiddleToast(resultbean.getInfo());
                    // 设置用户类型为帮众
                    PreferencesUtil.saveUserType(getmContext(), "bangzhong");
                    //// 初始化用户类型页面内容
                    initUserTypeStatus();

                    // 滚动到帮主细则内容处
                    if (srcoll_root != null && bangzhu_election_privilege_llay != null) {

                        Log.d("ActivityBangzhuElection", "srcoll_root.getMeasuredHeight() : " + srcoll_root.getMeasuredHeight());
                        Log.d("ActivityBangzhuElection", "srcoll_root.h() : " + srcoll_root.getHeight());
                        Log.d("ActivityBangzhuElection", "bangzhu_election_notice_llay.getMeasuredHeight() : " + bangzhu_election_privilege_llay.getMeasuredHeight());
                        Log.d("ActivityBangzhuElection", "bangzhu_election_notice_llay.h() : " + bangzhu_election_privilege_llay.getHeight());
                        Log.d("ActivityBangzhuElection", "subheigh : " + (bangzhu_election_privilege_llay.getMeasuredHeight() - srcoll_root.getMeasuredHeight()));

//                        srcoll_root.scrollTo(0, 0);

//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                srcoll_root.fullScroll(ScrollView.FOCUS_DOWN);
//
////                                srcoll_root.scrollTo(0,bangzhu_election_notice_llay.getMeasuredHeight()-srcoll_root.getMeasuredHeight());
//                            }
//                        });
//

                        //// 滚动到每月竞选2号的地方
                        srcoll_root.post(new Runnable() {
                            @Override
                            public void run() {
                                //To change body of implemented methods use File | Settings | File Templates.
//                    mRootScrollView.fullScroll(ScrollView.FOCUS_DOWN);


                                int[] location = new int[2];
                                bangzhu_election_privilege_llay.getLocationOnScreen(location);
                                int offset = location[1] - srcoll_root.getMeasuredHeight();
                                if (offset < 0) {
                                    offset = 0;
                                }
                                srcoll_root.smoothScrollTo(0, offset);

                            }
                        });

                    }
                } else if (resultbean != null && TextUtils.equals(resultbean.getStatus(), "no")) {
                    showMiddleToast(resultbean.getMessage());

                } else {
                    showToast("数据异常,请重试");
                }
                mLdDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                mLdDialog.dismiss();
                showNetErrorToast();

            }
        };

        BangZhuJoinElectionReq request = new BangZhuJoinElectionReq();
        request.setEmobId(bean == null ? "null" : bean.getEmobId());
        request.setMethod("PUT");
        service.getBangzhuJoinElection(StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(request)), request, PreferencesUtil.getCommityId(getmContext()), callback);
    }

    /**
     * 获取帮内最新消息
     */
    private void getBangInternalNewss() {
        mLdDialog.show();
        HashMap<String, String> option = new HashMap<>();
        option.put("emobId", bean == null ? "null" : bean.getEmobId());
        option.put("communityId", ""+PreferencesUtil.getCommityId(getmContext()));
        option.put("time", "" + PreferencesUtil.getBangZhuElectionLastTimeReqTime(getmContext()));

        BangZhuInfoService service = RetrofitFactory.getInstance().create(getmContext(),option,BangZhuInfoService.class);
        Callback<CommonRespBean<BangInternalNews>> callback = new Callback<CommonRespBean<BangInternalNews>>() {
            @Override
            public void success(CommonRespBean<BangInternalNews> bean, retrofit.client.Response response) {
                if (bean != null&&bean.getData()!=null) {
                    if (TextUtils.equals(bean.getStatus(), "yes")) {
                        showTopToast(bean.getData().getMessage());
                        PreferencesUtil.setBangZhuElectionLastTimeReqTime(getmContext(), bean.getData().getTime());
                    }
                }
                mLdDialog.dismiss();
            }
            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
            }
        };
//        communityId={小区ID}&emobId={环信ID}&time={上次获取到的变化时间}
        service.getBangInternalNews(option, callback);

    }

    /**
     * 获取帮主图信息
     */
    private void getBangzhuInfo() {
        mLdDialog.show();
        HashMap<String,String> queryMap =new HashMap<>();
        queryMap.put("communityId",""+PreferencesUtil.getCommityId(this));

        BangZhuInfoService service = RetrofitFactory.getInstance().create(getmContext(),queryMap,BangZhuInfoService.class);
        Callback<CommonRespBean<ElectionBangZhuBean>> callback = new Callback<CommonRespBean<ElectionBangZhuBean>>() {
            @Override
            public void success(CommonRespBean<ElectionBangZhuBean> bean, retrofit.client.Response response) {
                if (bean != null && TextUtils.equals(bean.getStatus(), "yes")) {

                    ElectionBangZhuBean infoEntity = bean.getData();
                    if (infoEntity != null) {
                        /// 初始化帮主信息
                        initBangzhuInfo(infoEntity.getBangzhu());
                        /// 填充副帮主1, 副帮主2信息
                        initFuBangzhu1Info(infoEntity.getFubangzhu());
                        //  初始化长老横向布局信息
                        initZhangLaoInfo(infoEntity.getDaren());
                    }
                    mLdDialog.dismiss();

                } else if (bean != null && TextUtils.equals(bean.getStatus(), "no")) {
                    mLdDialog.dismiss();
                } else {
                    mLdDialog.dismiss();
                }
            }

            /**
             * 初始化长老横向布局信息
             * @param zhanglao
             */
            private void initZhangLaoInfo(List<ElectionBangZhuBean.DarenEntity> zhanglao) {
                if (zhanglao != null && !zhanglao.isEmpty()) {
                    if (bangzhu_election_zhanglao_llay != null) {
                        bangzhu_election_zhanglao_llay.removeAllViews();

                        int listSize = zhanglao.size();
                        for (int i = 0; i < 6; i++) {

                            RelativeLayout zhanglaoInfoRlay = (RelativeLayout) getLayoutInflater().inflate(R.layout.common_bangzhu_election_zhanglao_headiconlay, null);
                            /// 长老不为空的情况下添加布局至横向布局.
                            if ((listSize - 1) >= i) {
                                final ElectionBangZhuBean.DarenEntity entity = zhanglao.get(i);
                                if (entity != null) {
                                    ImageView imageView = (ImageView) zhanglaoInfoRlay.findViewById(R.id.iv_avatar);
                                    ImageLoader.getInstance().displayImage(entity.getAvatar(), imageView, UserUtils.bangzhu_election_options);
                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            startActivity(new Intent(getmContext(), UserGroupInfoActivity.class).putExtra(Config.INTENT_PARMAS2, entity.getEmobId()));
                                        }
                                    });
                                    ((TextView) zhanglaoInfoRlay.findViewById(R.id.bangzhu_election_graph_myname_tv)).setText(entity.getNickname());
                                    ((TextView) zhanglaoInfoRlay.findViewById(R.id.bangzhu_election_graph_myrpz_tv)).setText("" + entity.getCharacterValues());
                                    zhanglaoInfoRlay.findViewById(R.id.bangzhu_election_graph_myname_tv).setVisibility(View.VISIBLE);
                                    zhanglaoInfoRlay.findViewById(R.id.bangzhu_election_graph_rpz_llay).setVisibility(View.VISIBLE);
                                } else {
                                    zhanglaoInfoRlay.findViewById(R.id.bangzhu_election_graph_myname_tv).setVisibility(View.GONE);
                                    zhanglaoInfoRlay.findViewById(R.id.bangzhu_election_graph_rpz_llay).setVisibility(View.GONE);
                                }
                            }
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
//                            layoutParams.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            bangzhu_election_zhanglao_llay.addView(zhanglaoInfoRlay, layoutParams);
                        }
                    }
                }
            }

            /**
             * 填充副帮主1, 副帮主2 信息
             * @param fubangzhuEntities
             */
            private void initFuBangzhu1Info(List<ElectionBangZhuBean.FubangzhuEntity> fubangzhuEntities) {
                if (fubangzhuEntities != null && !fubangzhuEntities.isEmpty()) {
                    if (fubangzhuEntities.size() >= 1) {
                        final ElectionBangZhuBean.FubangzhuEntity fubangzhuEntity = fubangzhuEntities.get(0);
                        if (fubangzhuEntity != null) {
                            if (f1bangzhu_iv_avatar != null) {
                                ImageLoader.getInstance().displayImage(fubangzhuEntity.getAvatar(), f1bangzhu_iv_avatar, UserUtils.bangzhu_election_options);
                                f1bangzhu_iv_avatar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(getmContext(), UserGroupInfoActivity.class).putExtra(Config.INTENT_PARMAS2, fubangzhuEntity.getEmobId()));
                                    }
                                });
                            }
                            if (f1bangzhu_bangzhu_election_graph_myname_tv != null) {
                                f1bangzhu_bangzhu_election_graph_myname_tv.setText(fubangzhuEntity.getNickname());
                                f1bangzhu_bangzhu_election_graph_myname_tv.setVisibility(View.VISIBLE);
                            }
                            if (f1bangzhu_bangzhu_election_graph_myrpz_tv != null) {
                                f1bangzhu_bangzhu_election_graph_myrpz_tv.setText("" + fubangzhuEntity.getCharacterValues());
                                f1bangzhu_bangzhu_election_graph_myrpz_tv.setVisibility(View.VISIBLE);
                            }

                            //////////
                            if (f1bangzhu_election_graph_rpz_llay != null) {
                                f1bangzhu_election_graph_rpz_llay.setVisibility(View.VISIBLE);
                            }
                            ///////
                        }
                    } else {
                        //// 没有数据, 默认头像,不可点...
                        if (f1bangzhu_election_graph_rpz_llay != null) {
                            f1bangzhu_election_graph_rpz_llay.setVisibility(View.GONE);
                        }
                        if (f1bangzhu_bangzhu_election_graph_myname_tv != null) {
                            f1bangzhu_bangzhu_election_graph_myname_tv.setVisibility(View.GONE);
                        }
                        //////////////
                    }

                    if (fubangzhuEntities.size() >= 2) {
                        final ElectionBangZhuBean.FubangzhuEntity fu2bangzhuEntity = fubangzhuEntities.get(1);
                        if (fu2bangzhuEntity != null) {
                            if (f2bangzhu_iv_avatar != null) {
                                ImageLoader.getInstance().displayImage(fu2bangzhuEntity.getAvatar(), f2bangzhu_iv_avatar, UserUtils.bangzhu_election_options);
                                f2bangzhu_iv_avatar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(getmContext(), UserGroupInfoActivity.class).putExtra(Config.INTENT_PARMAS2, fu2bangzhuEntity.getEmobId()));
                                    }
                                });
                            }
                            if (f2bangzhu_bangzhu_election_graph_myname_tv != null) {
                                f2bangzhu_bangzhu_election_graph_myname_tv.setText(fu2bangzhuEntity.getNickname());
                                f2bangzhu_bangzhu_election_graph_myname_tv.setVisibility(View.VISIBLE);
                            }
                            if (f2bangzhu_bangzhu_election_graph_myrpz_tv != null) {
                                f2bangzhu_bangzhu_election_graph_myrpz_tv.setText("" + fu2bangzhuEntity.getCharacterValues());
                                f2bangzhu_bangzhu_election_graph_myrpz_tv.setVisibility(View.VISIBLE);
                            }
                            /////
                            if (f2bangzhu_election_graph_rpz_llay != null) {
                                f2bangzhu_election_graph_rpz_llay.setVisibility(View.VISIBLE);
                            }
                            ///
                        }
                    } else {
                        //// 没有数据, 默认头像,不可点

                        if (f2bangzhu_election_graph_rpz_llay != null) {
                            f2bangzhu_election_graph_rpz_llay.setVisibility(View.GONE);
                        }
                        if (f2bangzhu_bangzhu_election_graph_myname_tv != null) {
                            f2bangzhu_bangzhu_election_graph_myname_tv.setVisibility(View.GONE);
                        }
                        /////
                    }
                }
            }

            /**
             * 初始化帮主信息
             *
             * @param bangzhuEntityList 帮主信息
             */
            private void initBangzhuInfo(List<ElectionBangZhuBean.BangzhuEntity> bangzhuEntityList) {
                if (bangzhuEntityList != null && !bangzhuEntityList.isEmpty()) {
//                    注意: 这里的帮主虽然是一个数组格式,但实际帮主只有一个,所以只需要获取第一个节点即可
                    final ElectionBangZhuBean.BangzhuEntity bangzhuEntity = bangzhuEntityList.get(0);
                    if (bangzhuEntity != null) {
                        if (bangzhu_iv_avatar != null) {
                            bangzhu_iv_avatar = (ImageView) bangzhu_election_bangzhu.findViewById(R.id.iv_avatar);
                            ImageLoader.getInstance().displayImage(bangzhuEntity.getAvatar(), bangzhu_iv_avatar, UserUtils.bangzhu_election_options);
                            bangzhu_iv_avatar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(getmContext(), UserGroupInfoActivity.class).putExtra(Config.INTENT_PARMAS2, bangzhuEntity.getEmobId()));
                                }
                            });
                        }
                        if (bangzhu_bangzhu_election_graph_myname_tv != null) {
                            bangzhu_bangzhu_election_graph_myname_tv.setText(bangzhuEntity.getNickname());
                            bangzhu_bangzhu_election_graph_myname_tv.setVisibility(View.VISIBLE);
                        }
                        if (bangzhu_bangzhu_election_graph_myrpz_tv != null) {
                            bangzhu_bangzhu_election_graph_myrpz_tv.setText("" + bangzhuEntity.getCharacterValues());
                            bangzhu_bangzhu_election_graph_myrpz_tv.setVisibility(View.VISIBLE);
                        }

                        /////////////
                        if (bangzhu_election_graph_rpz_llay != null) {
                            bangzhu_election_graph_rpz_llay.setVisibility(View.VISIBLE);
                        }

                    } else {

                        if (bangzhu_election_graph_rpz_llay != null) {
                            bangzhu_election_graph_rpz_llay.setVisibility(View.GONE);
                        }
                        if (bangzhu_bangzhu_election_graph_myname_tv != null) {
                            bangzhu_bangzhu_election_graph_myname_tv.setVisibility(View.GONE);
                        }

                        ////
                    }
                }
            }


            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        service.getBangzhuGraphInfo(queryMap, callback);
    }


}
