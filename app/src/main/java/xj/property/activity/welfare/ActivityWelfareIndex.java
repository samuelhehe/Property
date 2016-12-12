package xj.property.activity.welfare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
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
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.activity.user.FixUserAddressConfrimDialog;
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.beans.UserInfoDetailBean;
import xj.property.beans.WelfareGoingBuyInfo;
import xj.property.beans.WelfareInfo;
import xj.property.beans.WelfareInfoEntity;
import xj.property.beans.WelfareQueryGoingBuy;
import xj.property.beans.WelfareUsersEntity;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.DensityUtil;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;
import xj.property.utils.other.WelfareUtil;

/**
 * 福利首页
 * <p/>
 * 600: 查看福利详情
 * 500: 往期福利详情
 */
public class ActivityWelfareIndex extends HXBaseActivity {

    //// 商品海报
    private ImageView welfare_purchase_goods_niv;


    //// 剩余名额,剩余多少人
    private TextView left_nums_content_tv, left_nums_title_tv;

    /// 截止时间
    private RelativeLayout welfare_stop_time_rlay;

    /// 剩余名额,总布局 设置为不可见
    private RelativeLayout purchase_right_icon_rlay;

    //// 抢购结束图章
    private ImageView welfare_have_been_robbed_iv;

    /// 截止时间
    private TextView stop_timer_tv;

    /// 福利价,福利价格, 抢购状态
    private TextView benefits_purchase_title_tv, purshing_price_tv, purchasing_status_tv;


    private LinearLayout goodImgs;


    private TextView welfare_goods_name_tv;

    /// 活动细则
    private TextView purchasing_actdetails_tv;

    /// 已经购买的用户头像信息
    private LinearLayout welfare_purchase_hasgoturs_lv;


    private Button welfare_submit_btn;

    /// 抢购状态条
    private RelativeLayout welfare_purchase_status_lay;

    //// 抢福利需要的人品值
    private TextView welfare_goods_grab_nedd_rpz_tv;

    //// 查看更多用户

    private LinearLayout welfare_moreuser;
    //用户分享的订单号

//    private String orderid;


    //创建消息用的:emobid,nickname,avatar
    private String emobId;

    private String nickname;
    private String avatar;


    LinearLayout ll_welfare_fail;

    TextView tv_fail_reason;

    LinearLayout ll_welfare_success;

    TextView tv_success_code;

    /// 海报的父容器根据比例 7:5进行设置
    private RelativeLayout welfare_poster_rl;

    private LinearLayout welfare_purchase_hasgoturs_lay;

    //// 商品发放地址
    private TextView welfare_provide_instruction_tv;


    /// 福利至少需要多少人才可以进行抢购
    private TextView welfare_grab_least_numman_tv;


    private LinearLayout welfare_loading_ll;

    private Context mContext;
    private UserInfoDetailBean bean;
    private String emobIdUser;
    private int PayCodeOnline = 0;
    int flag = -1;
    private int screenWidth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welfare_index);
        mContext = this;
        flag = getIntent().getFlags();
        bean = PreferencesUtil.getLoginInfo(getmContext());
        initView();
        initData();

//        WelfareUtil.create600Message(getmContext(), "emobid", "create600Message", "create600Message"+System.currentTimeMillis(), "" + System.currentTimeMillis()/1000, "");
    }

    private void initWelfareDetails(String welfareId) {

        getWelfareDetailInfo(welfareId);

        TextView tv = (TextView) this.findViewById(R.id.tv_right_text);
        tv.setOnClickListener(this);
        tv.setVisibility(View.GONE);
        this.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.tv_title)).setText("查看福利详情");
        /// 设置抢购按钮为不可见
        if (welfare_submit_btn != null) {
            welfare_submit_btn.setVisibility(View.GONE);
        }

    }

    /**
     * 获取屏幕的宽度  按照W:H= 7:5的比例进行计算图片的高度.
     *
     * @return
     */
    private int getDisplayHeightWH75() {
        return getResources().getDisplayMetrics().widthPixels * 5 / 7;
    }

    /**
     * 获取商品图片按照W:H= 7:5的比例进行计算图片的高度.
     *
     * @return
     */
    private int getDisplayHeightWH75GoodsImgs() {
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        int marginLeftRightpx = DensityUtil.dip2px(mContext, 15 * 2f);
        return (widthPixels - marginLeftRightpx) * 5 / 7;
    }

    private void initView() {


        welfare_loading_ll = (LinearLayout) this.findViewById(R.id.welfare_loading_ll);
        welfare_loading_ll.setVisibility(View.INVISIBLE);

        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        TextView tv = (TextView) this.findViewById(R.id.tv_right_text);
        tv.setOnClickListener(this);
        tv.setVisibility(View.VISIBLE);
        this.findViewById(R.id.iv_back).setOnClickListener(this);

        /// 海报的父容器根据比例 7:5进行设置
        welfare_poster_rl = (RelativeLayout) this.findViewById(R.id.welfare_poster_rl);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, getDisplayHeightWH75());

        Log.d("ActivityWelfareIndex ,welfare_poster_rl: height:  ", "" + getResources().getDisplayMetrics().widthPixels * 5 / 7);

        welfare_poster_rl.setLayoutParams(params);

        welfare_purchase_goods_niv = (ImageView) this.findViewById(R.id.welfare_purchase_goods_niv);

        /// left title
        left_nums_title_tv = (TextView) this.findViewById(R.id.left_nums_title_tv);
        /// left nums
        left_nums_content_tv = (TextView) this.findViewById(R.id.left_nums_content_tv);

        welfare_stop_time_rlay = (RelativeLayout) this.findViewById(R.id.welfare_stop_time_rlay);

        /// 截止时间
        stop_timer_tv = (TextView) this.findViewById(R.id.stop_timer_tv);

        /// 购买截止时间不可见
        purchase_right_icon_rlay = (RelativeLayout) this.findViewById(R.id.purchase_right_icon_rlay);

        /// 抢购结束图章 默认不可见
        welfare_have_been_robbed_iv = (ImageView) this.findViewById(R.id.welfare_have_been_robbed_iv);


        //// 抢购价格
        purshing_price_tv = (TextView) this.findViewById(R.id.purshing_price_tv);

        //// 抢购标题
        benefits_purchase_title_tv = (TextView) this.findViewById(R.id.benefits_purchase_tv);

        //// 抢购状态
        purchasing_status_tv = (TextView) this.findViewById(R.id.purchasing_status_tv);

        //// 商品的图片列表
        goodImgs = (LinearLayout) this.findViewById(R.id.welfare_purchase_imgs_ll);


        //// 提交支付信息
        welfare_submit_btn = (Button) this.findViewById(R.id.welfare_submit_btn);
        welfare_submit_btn.setOnClickListener(this);

        /// 商品名称描述
        welfare_goods_name_tv = (TextView) this.findViewById(R.id.welfare_goods_name_tv);

        /// 活动细则
        purchasing_actdetails_tv = (TextView) this.findViewById(R.id.purchasing_actdetails_tv);

        //// 已经购买的用户头像信息
        welfare_purchase_hasgoturs_lv = (LinearLayout) this.findViewById(R.id.welfare_purchase_hasgoturs_lv);

        /// 抢购状态条
        welfare_purchase_status_lay = (RelativeLayout) this.findViewById(R.id.welfare_purchase_status_lay);

        //// 截止时间条
        welfare_stop_time_rlay = (RelativeLayout) welfare_purchase_status_lay.findViewById(R.id.welfare_stop_time_rlay);
        /// 抢福利需要的人品值
        welfare_goods_grab_nedd_rpz_tv = (TextView) this.findViewById(R.id.welfare_goods_grab_nedd_rpz_tv);

        welfare_purchase_hasgoturs_lay = (LinearLayout) this.findViewById(R.id.welfare_purchase_hasgoturs_lay);

        welfare_moreuser = (LinearLayout) this.findViewById(R.id.welfare_moreuser);

        welfare_moreuser.setOnClickListener(this);

        welfare_provide_instruction_tv = (TextView) this.findViewById(R.id.welfare_provide_instruction_tv);

        welfare_grab_least_numman_tv = (TextView) this.findViewById(R.id.welfare_grab_least_numman_tv);


        ll_welfare_fail = (LinearLayout) findViewById(R.id.ll_welfare_fail);
        tv_fail_reason = (TextView) findViewById(R.id.tv_fail_reason);
        ll_welfare_success = (LinearLayout) findViewById(R.id.ll_welfare_success);
        tv_success_code = (TextView) findViewById(R.id.tv_success_code);

        if (flag == 500) {
            ((TextView) this.findViewById(R.id.tv_title)).setText("往期福利详情");
            ((TextView) this.findViewById(R.id.tv_right_text)).setVisibility(View.GONE);
            /// 设置抢购按钮不可见
            if (welfare_submit_btn != null) {
                welfare_submit_btn.setVisibility(View.GONE);
            }
        } else if (flag == 600) {
            ((TextView) this.findViewById(R.id.tv_title)).setText("查看福利详情");
            ((TextView) this.findViewById(R.id.tv_right_text)).setVisibility(View.GONE);
        } else {
            this.findViewById(R.id.tv_title).setBackgroundDrawable(getResources().getDrawable(R.drawable.grab_benefits));
        }
    }

    private void initData() {

        ////查看福利详情
        if (flag == 600) {
            String welfareId = getIntent().getStringExtra("welfareId");
            initWelfareDetails(welfareId);
            return;
        }
        //往期福利详情
        if (flag == 500) {
            String welfareId = getIntent().getStringExtra("welfareId");
            ////获取往期福利详情
            initHistoryWelfareDetailInfo(welfareId);
            return;
        }

        /**
         * 在抢福利页面按钮都是可见的.
         */
        getWelfareInfo();
    }

    private void initHistoryWelfareDetailInfo(String welfareId) {

        getHistoryWelfareDetailInfo(welfareId);
        /// 设置抢购按钮为不可见
        if (welfare_submit_btn != null) {
            welfare_submit_btn.setVisibility(View.GONE);
        }
    }


    interface WelfareDetails {
        @GET("/api/v1/communities/{communityId}/welfares/{welfareId}/user/{emobId}/welfareCode")
        void getWelfareDetailInfo(@Path("communityId") int communityId, @Path("welfareId") String welfareId, @Path("emobId") String emobId, Callback<WelfareInfo> cb);

//        * 从福利列表中点击进入福利详情获取的福利码

        ///api/v3/welfares/{福利ID}/code?emobId={用户环信ID}&communityId={小区ID}

        @GET("/api/v3/welfares/{welfareId}/code")
        void getWelfareDetailInfoV3(@Path("welfareId") String welfareId, @QueryMap HashMap<String, String> queryMap, Callback<CommonRespBean<WelfareInfoEntity>> cb);
    }


    /**
     * 从福利列表中点击进入福利详情获取的福利码
     * v3 2016/03/14
     *
     * @param welfareId
     */
    private void getWelfareDetailInfo(String welfareId) {
        mLdDialog.show();

        HashMap<String, String> map = new HashMap<>();
        map.put("communityId", "" + PreferencesUtil.getCommityId(this));
        map.put("emobId", PreferencesUtil.getLoginInfo(getmContext()).getEmobId());

        WelfareDetails service = RetrofitFactory.getInstance().create(getmContext(),map,WelfareDetails.class);
        Callback<CommonRespBean<WelfareInfoEntity>> callback = new Callback<CommonRespBean<WelfareInfoEntity>>() {
            @Override
            public void success(CommonRespBean<WelfareInfoEntity> bean, retrofit.client.Response response) {
                if (bean != null && TextUtils.equals(bean.getStatus(), "yes")) {
                    WelfareInfoEntity infoEntity = bean.getData();
                    if (infoEntity != null) {
                        /// 加载海报状态
                        loadingPoster(infoEntity);

                        /// 加载福利价格信息
                        initWelfarePriceView(infoEntity);

                        //// 加载福利商品信息名称
                        initWelfareGoodsNameInfo(infoEntity);

                        ///
                        initWelfareNoticeInfo(infoEntity);

                        ///加载商品信息
                        loadingGoodsImgs(infoEntity);

                        /// 加载已经购买的用户头像
                        loadingGoodsHasGotursHeadImgs4(infoEntity);

                        //// 加载活动细则
                        initWelfareRules(infoEntity);

                        /// 设置抢购状态
                        initWelfareStatus(infoEntity);

                    }

                    /// 活动正在进行
                    if (infoEntity != null && TextUtils.equals(infoEntity.getStatus(), "ongoing")) {

                        ll_welfare_fail.setVisibility(View.GONE);
                        tv_success_code.setText(bean.getData().getCode());
                        ll_welfare_success.setVisibility(View.VISIBLE);

                        /// 活动团成功
                    } else if (infoEntity != null && TextUtils.equals(infoEntity.getStatus(), "success")) {

                        ll_welfare_fail.setVisibility(View.GONE);
                        tv_success_code.setText(infoEntity.getCode());
                        ll_welfare_success.setVisibility(View.VISIBLE);

                        /// 活动失败
                    } else if (infoEntity != null && TextUtils.equals(infoEntity.getStatus(), "failure")) {
                        ll_welfare_fail.setVisibility(View.VISIBLE);
                        String failresaon = "本次福利由于人数不足,所以导致无法发放,您的款项将在7个工作日退回您的支付宝账户请您查收";
                        tv_fail_reason.setText(TextUtils.isEmpty(infoEntity.getReason()) ? failresaon : infoEntity.getReason());
                        ll_welfare_success.setVisibility(View.GONE);

                    }
                    setloadingViewVisiable();

                } else {

                    showToast(bean.getMessage());
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
        service.getWelfareDetailInfoV3(welfareId, map, callback);
    }


    interface WelfareInfoService {
        @GET("/api/v1/communities/{communityId}/welfares/current")
        void getWelfareInfo(@Path("communityId") int communityId, Callback<WelfareInfo> cb);

        /**
         * 获取小区当前福利
         *
         * @param map
         * @param cb
         */
        @GET("/api/v3/welfares")
        void getWelfareInfoV3(@QueryMap HashMap<String, String> map, Callback<CommonRespBean<WelfareInfoEntity>> cb);
    }

    private WelfareInfoEntity info;


    /**
     * 获取福利信息
     */
    private void getWelfareInfo() {
        mLdDialog.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("communityId", ""+PreferencesUtil.getCommityId(this));
        WelfareInfoService service = RetrofitFactory.getInstance().create(getmContext(),map,WelfareInfoService.class);
        Callback<CommonRespBean<WelfareInfoEntity>> callback = new Callback<CommonRespBean<WelfareInfoEntity>>() {
            @Override
            public void success(CommonRespBean<WelfareInfoEntity> bean, retrofit.client.Response response) {
                if (bean != null && TextUtils.equals(bean.getStatus(), "yes")) {
                    info = bean.getData();
                    if (info != null) {
                        /// 加载海报状态
                        loadingPoster(info);

                        /// 加载福利价格信息
                        initWelfarePriceView(info);

                        //// 加载福利商品信息名称
                        initWelfareGoodsNameInfo(info);

                        initWelfareNoticeInfo(info);

                        ///加载商品信息
                        loadingGoodsImgs(info);

                        /// 加载已经购买的用户头像
                        loadingGoodsHasGotursHeadImgs4(info);

                        //// 加载活动细则
                        initWelfareRules(info);

                        /// 设置活动抢购状态
                        initWelfareStatus(info);

                        //// 设置加载内容可见
                        setloadingViewVisiable();
                    }

                    //// 无内容....
                    mLdDialog.dismiss();

                } else if (bean != null && TextUtils.equals(bean.getStatus(), "no")) {
                    setContentView(R.layout.activity_no_welfare);
                    TextView tv = (TextView) findViewById(R.id.tv_right_text);
                    tv.setOnClickListener(ActivityWelfareIndex.this);
                    tv.setVisibility(View.VISIBLE);
                    findViewById(R.id.iv_back).setOnClickListener(ActivityWelfareIndex.this);
                    findViewById(R.id.tv_title).setBackgroundDrawable(getResources().getDrawable(R.drawable.grab_benefits));
                    mLdDialog.dismiss();
                } else {
                    setContentView(R.layout.activity_no_welfare);
                    TextView tv = (TextView) findViewById(R.id.tv_right_text);
                    tv.setOnClickListener(ActivityWelfareIndex.this);
                    tv.setVisibility(View.VISIBLE);
                    findViewById(R.id.iv_back).setOnClickListener(ActivityWelfareIndex.this);
                    findViewById(R.id.tv_title).setBackgroundDrawable(getResources().getDrawable(R.drawable.grab_benefits));
                    mLdDialog.dismiss();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        service.getWelfareInfoV3(map, callback);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PayCodeOnline) {
            if (resultCode == RESULT_OK) {

                findViewById(R.id.tv_success_top).setVisibility(View.VISIBLE);
                CountDownTimer countDownTimer = new CountDownTimer(3 * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        findViewById(R.id.tv_success_top).setVisibility(View.GONE);
                    }
                };
                countDownTimer.start();
                //// 支付成功
                WelfareUtil.showWelfareShareDialog(getmContext(), "" + info.getWelfareId());
                ///TODO  第四个字段 code 是原orderid ,现未确定
                WelfareUtil.create600Message(getmContext(), emobId, info.getTitle(), "", "" + info.getWelfareId(), info.getPoster());
            } else if (resultCode == RESULT_CANCELED) {
                //// 支付取消或者支付结果正在等待
            }
        }
    }


//    private FixUserAddressConfrimDialog fFixUserAddressConfrimDialog;

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(fFixUserAddressConfrimDialog!=null){
//            fFixUserAddressConfrimDialog.dismiss();
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.welfare_submit_btn:
                if (PreferencesUtil.getLogin(mContext)) {

                    bean = PreferencesUtil.getLoginInfo(this);

                    emobIdUser = bean.getEmobId();

                    //// 显示地址对话框 2015/11/17
//                    if(TextUtils.isEmpty(bean.getRoom())||TextUtils.isEmpty(bean.getUserFloor())){
//                        fFixUserAddressConfrimDialog = new FixUserAddressConfrimDialog(this);
//                        fFixUserAddressConfrimDialog.show();
//                        return;
//                    }

//                    String rpvalue = PreferencesUtil.getRPValue(mContext);

                    /// 人品值检查.
//                    if (info.getCharactervalues() > Integer.valueOf(rpvalue)) {
//                        showToast("人品值不足" + info.getCharactervalues() + "无法参与此福利抢购");
//                        return;
//                    }   2015/12/10 去掉人品值验证  576--582

                    getGoingBuy();

                } else {
                    Intent intent = new Intent(mContext, RegisterLoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right_text:
                //// 跳转至历史福利记录
                startActivity(new Intent(this, HistoryWelfareListActivity.class));
                break;
            case R.id.welfare_moreuser:
                //点击跳转至查看更多用户页面///
                startActivity(new Intent(mContext, ActivityWelfareBuyedMoreUsers.class).putExtra("welfareId", info.getWelfareId()));
                break;
        }
    }


    interface WelfareGoingBuyService {

//        @POST("/api/v1/communities/{communityId}/welfares/{welfareId}")
//        void getGoingBuyInfo(@Header("signature") String signature, @Body WelfareQueryGoingBuy qt, @Path("communityId") int communityId, @Path("welfareId") int welfareId, Callback<WelfareGoingBuyInfo> cb);

        @POST("/api/v3/welfares")
        void getGoingBuyInfov3(@Body WelfareQueryGoingBuy qt, Callback<CommonRespBean<WelfareGoingBuyInfo>> cb);
    }

    /**
     * 抢购
     */
    private void getGoingBuy() {
        mLdDialog.show();
        WelfareQueryGoingBuy quaryToken = new WelfareQueryGoingBuy();
        quaryToken.setCommunityId(PreferencesUtil.getCommityId(getmContext()));
        quaryToken.setEmobId(bean.getEmobId());
        quaryToken.setWelfareId(info.getWelfareId());

        WelfareGoingBuyService service = RetrofitFactory.getInstance().create(getmContext(),quaryToken,WelfareGoingBuyService.class);
        Callback<CommonRespBean<WelfareGoingBuyInfo>> callback = new Callback<CommonRespBean<WelfareGoingBuyInfo>>() {
            @Override
            public void success(CommonRespBean<WelfareGoingBuyInfo> bean, retrofit.client.Response response) {
                if (bean != null && TextUtils.equals(bean.getStatus(), "yes")) {
                    /// 抢购成功
                    Log.d("getGoingBuy", "getGoingBuy " + bean.toString());
                    /// 跳转至支付前页面

                    /*
                        * emobId : {福利消息管理员环信ID}
                        * nickname : {福利消息管理员昵称}
                        * avatar : {福利消息管理员昵称}
                     */
                    Intent intent = new Intent();
                    intent.setClass(mContext, ActivityWelfarePrePay.class);
                    intent.putExtra("posterstr", info.getPoster());
                    intent.putExtra("serverName", info.getTitle());
                    intent.putExtra("price", info.getPrice());
                    intent.putExtra("welfareId", info.getWelfareId());
                    intent.putExtra("provideInstruction", info.getProvideInstruction());
                    startActivityForResult(intent, PayCodeOnline);
//                    orderid = bean.getInfo().getWelfareOrder().getSerial();  //// orderid 抢购成功之后生成的订单ID

                    emobId = "" + bean.getData().getEmobId();
                    nickname = "" + bean.getData().getNickname();
                    avatar = "" + bean.getData().getAvatar();

                    mLdDialog.dismiss();
                } else {
                    if (bean != null) {
                        //// 没有抢购到,或者说抢购结束,或者人品值不足5
                        showToast(bean.getMessage());
                    } else {
                        showToast("数据异常");
                    }
                    mLdDialog.dismiss();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        service.getGoingBuyInfov3(quaryToken, callback);
    }


    interface HistoryWelfareDetails {
        @GET("/api/v1/communities/{communityId}/welfares/{welfareId}")
        void getHistoryWelfareDetailInfo(@Path("communityId") int communityId, @Path("welfareId") String welfareId, Callback<WelfareInfo> cb);

        //// 获取往期福利详情
        @GET("/api/v3/welfares/{welfareId}")
        void getHistoryWelfareDetailInfoV3(@Path("welfareId") String welfareId, Callback<CommonRespBean<WelfareInfoEntity>> cb);
    }

    /**
     * 获取福利历史详情
     *
     * @param welfareId
     */
    private void getHistoryWelfareDetailInfo(String welfareId) {
        mLdDialog.show();
        HistoryWelfareDetails service = RetrofitFactory.getInstance().create(getmContext(),HistoryWelfareDetails.class);
        Callback<CommonRespBean<WelfareInfoEntity>> callback = new Callback<CommonRespBean<WelfareInfoEntity>>() {
            @Override
            public void success(CommonRespBean<WelfareInfoEntity> bean, retrofit.client.Response response) {
                if (bean != null && TextUtils.equals(bean.getStatus(), "yes")) {
                    info = bean.getData();
                    if (info != null) {

                        /// 加载海报状态
                        loadingPoster(info);

                        /// 加载福利价格信息
                        initWelfarePriceView(info);

                        //// 加载福利商品信息名称
                        initWelfareGoodsNameInfo(info);

                        ///  福利领取注意事项
                        initWelfareNoticeInfo(info);

                        ///加载商品信息
                        loadingGoodsImgs(info);

                        /// 加载已经购买的用户头像
                        loadingGoodsHasGotursHeadImgs4(info);

                        //// 加载活动细则
                        initWelfareRules(info);

                        /// 设置抢购结束状态样式
                        initWelfareStatus(info);

                    }
                    setloadingViewVisiable();
                    mLdDialog.dismiss();
                } else if (bean != null && TextUtils.equals(bean.getStatus(), "no")) {

                    setContentView(R.layout.activity_no_welfare);
                    TextView tv = (TextView) findViewById(R.id.tv_right_text);
                    tv.setOnClickListener(ActivityWelfareIndex.this);
                    tv.setVisibility(View.VISIBLE);
                    findViewById(R.id.iv_back).setOnClickListener(ActivityWelfareIndex.this);
                    findViewById(R.id.tv_title).setBackgroundDrawable(getResources().getDrawable(R.drawable.grab_benefits));
                    mLdDialog.dismiss();

                } else {
                    setContentView(R.layout.activity_no_welfare);
                    TextView tv = (TextView) findViewById(R.id.tv_right_text);
                    tv.setOnClickListener(ActivityWelfareIndex.this);
                    tv.setVisibility(View.VISIBLE);
                    findViewById(R.id.iv_back).setOnClickListener(ActivityWelfareIndex.this);
                    findViewById(R.id.tv_title).setBackgroundDrawable(getResources().getDrawable(R.drawable.grab_benefits));
                    mLdDialog.dismiss();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
            }
        };

        service.getHistoryWelfareDetailInfoV3(welfareId, callback);
    }


    /**
     * 设置主界面可见
     */
    private void setloadingViewVisiable() {

        if (welfare_loading_ll != null) {
            //// 设置加载内容可见
            welfare_loading_ll.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 福利领取注意事项
     *
     * @param info
     */
    private void initWelfareNoticeInfo(WelfareInfoEntity info) {
        if (welfare_goods_grab_nedd_rpz_tv != null) {
            /// 需要的人品值设置
            welfare_goods_grab_nedd_rpz_tv.setText("" + info.getCharactervalues());
        }
        //// 商品领取地址
        if (welfare_provide_instruction_tv != null) {

            welfare_provide_instruction_tv.setText(info.getProvideInstruction());

            welfare_provide_instruction_tv.setVisibility(View.VISIBLE);
        }

        if (welfare_grab_least_numman_tv != null) {
            welfare_grab_least_numman_tv.setText("此福利需要至少" + info.getTotal() + "抢购人才可生效");
        }
    }


    /**
     * 抢购完样式,不可抢状态
     *
     * @param info
     */
    private void initWelfareGrabOver(WelfareInfoEntity info) {

        if ((info.getRemain() <= 0) || TextUtils.equals(info.getExpire(), "true")) {

            //// 图章可见 /// 两种状态: 一种是抢完,一种是结束
            if (welfare_have_been_robbed_iv != null) {

                if (info.getRemain() <= 0) {
                    //// 已抢完
                    welfare_have_been_robbed_iv.setImageDrawable(getResources().getDrawable(R.drawable.have_been_robbed));
                    /// 抢购状态
                    if (purchasing_status_tv != null) {
                        purchasing_status_tv.setText("已抢完");
                        purchasing_status_tv.setTextColor(getResources().getColor(R.color.white));
                        purchasing_status_tv.setVisibility(View.VISIBLE);
                    }
                }
                /// 过期图
                if (TextUtils.equals(info.getExpire(), "true")) {
                    //// 已结束
                    welfare_have_been_robbed_iv.setImageDrawable(getResources().getDrawable(R.drawable.welfare_grab_over));

                    /// 抢购状态
                    if (purchasing_status_tv != null) {
                        purchasing_status_tv.setText("抢购结束");
                        purchasing_status_tv.setTextColor(getResources().getColor(R.color.white));
                        purchasing_status_tv.setVisibility(View.VISIBLE);
                    }
                }
                welfare_have_been_robbed_iv.setVisibility(View.VISIBLE);
            }

            /// 剩余人数不可见
            if (purchase_right_icon_rlay != null) {
                purchase_right_icon_rlay.setVisibility(View.INVISIBLE);/// 设置为不可见
            }

            /// 设置抢购状态条的背景色为灰色
            if (welfare_purchase_status_lay != null) {
                welfare_purchase_status_lay.setBackgroundDrawable(getResources().getDrawable(R.color.welfare_grab_status_bg_color));
            }

            /// 抢购价格
            if (purshing_price_tv != null) {
                purshing_price_tv.setText("" + info.getPrice() + "元");
                purshing_price_tv.setTextColor(getResources().getColor(R.color.white));
                purshing_price_tv.setVisibility(View.VISIBLE);
            }

            /// 福利价
            if (benefits_purchase_title_tv != null) {
                benefits_purchase_title_tv.setVisibility(View.VISIBLE);
                benefits_purchase_title_tv.setTextColor(getResources().getColor(R.color.white));
            }

            //// 设置截止时间条为灰色
            if (welfare_stop_time_rlay != null) {
                welfare_stop_time_rlay.setBackgroundDrawable(getResources().getDrawable(R.drawable.grab_welfare_over_lable));
            }

            /// 截止时间
            if (stop_timer_tv != null) {
                stop_timer_tv.setText("已结束");
                stop_timer_tv.setTextColor(getResources().getColor(android.R.color.darker_gray));
                stop_timer_tv.setVisibility(View.VISIBLE);
            }

            /// 设置抢购按钮为灰色字体白色
            if (welfare_submit_btn != null) {
                welfare_submit_btn.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                welfare_submit_btn.setTextColor(getResources().getColor(android.R.color.background_light));
                welfare_submit_btn.setEnabled(false);
            }
        }
    }


    /**
     * 团购成功样式
     *
     * @param info
     */
    private void initWelfareStatusSuccess(WelfareInfoEntity info) {

        if (info != null && TextUtils.equals(info.getStatus(), "success")) {
            if (welfare_have_been_robbed_iv != null) {
                //// 已抢完
                welfare_have_been_robbed_iv.setImageDrawable(getResources().getDrawable(R.drawable.have_been_robbed));
                welfare_have_been_robbed_iv.setVisibility(View.VISIBLE);
            }
            /// 抢购状态
            if (purchasing_status_tv != null) {
                purchasing_status_tv.setText("已抢完");
                purchasing_status_tv.setTextColor(getResources().getColor(R.color.white));
                purchasing_status_tv.setVisibility(View.VISIBLE);
            }

            /// 剩余人数不可见
            if (purchase_right_icon_rlay != null) {
                purchase_right_icon_rlay.setVisibility(View.INVISIBLE);/// 设置为不可见
            }

            /// 设置抢购状态条的背景色为灰色
            if (welfare_purchase_status_lay != null) {
                welfare_purchase_status_lay.setBackgroundDrawable(getResources().getDrawable(R.color.welfare_grab_status_bg_color));
            }

            /// 抢购价格
            if (purshing_price_tv != null) {
                purshing_price_tv.setText("" + info.getPrice() + "元");
                purshing_price_tv.setTextColor(getResources().getColor(R.color.white));
                purshing_price_tv.setVisibility(View.VISIBLE);
            }

            /// 福利价
            if (benefits_purchase_title_tv != null) {
                benefits_purchase_title_tv.setVisibility(View.VISIBLE);
                benefits_purchase_title_tv.setTextColor(getResources().getColor(R.color.white));
            }

            //// 设置截止时间条为灰色
            if (welfare_stop_time_rlay != null) {
                welfare_stop_time_rlay.setBackgroundDrawable(getResources().getDrawable(R.drawable.grab_welfare_over_lable));
            }

            /// 截止时间
            if (stop_timer_tv != null) {
                stop_timer_tv.setText("已结束");
                stop_timer_tv.setTextColor(getResources().getColor(android.R.color.darker_gray));
                stop_timer_tv.setVisibility(View.VISIBLE);
            }

            /// 设置抢购按钮为灰色字体白色
            if (welfare_submit_btn != null) {
                welfare_submit_btn.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                welfare_submit_btn.setTextColor(getResources().getColor(android.R.color.background_light));
                welfare_submit_btn.setEnabled(false);
            }
        }


    }

    /**
     * 设置福利失败样式
     *
     * @param info
     */
    private void initWelfareFailure(WelfareInfoEntity info) {

        if (info != null && TextUtils.equals(info.getStatus(), "failure")) {

            //// 图章可见 /// 两种状态: 一种是抢完,一种是结束
            if (welfare_have_been_robbed_iv != null) {

                //// 已结束
                welfare_have_been_robbed_iv.setImageDrawable(getResources().getDrawable(R.drawable.welfare_grab_over));

                /// 抢购状态
                if (purchasing_status_tv != null) {
                    purchasing_status_tv.setText("抢购结束");
                    purchasing_status_tv.setTextColor(getResources().getColor(R.color.white));
                    purchasing_status_tv.setVisibility(View.VISIBLE);
                }
                welfare_have_been_robbed_iv.setVisibility(View.VISIBLE);
            }

            /// 剩余人数不可见
            if (purchase_right_icon_rlay != null) {
                purchase_right_icon_rlay.setVisibility(View.INVISIBLE);/// 设置为不可见
            }

            /// 设置抢购状态条的背景色为灰色
            if (welfare_purchase_status_lay != null) {
                welfare_purchase_status_lay.setBackgroundDrawable(getResources().getDrawable(R.color.welfare_grab_status_bg_color));
            }

            /// 抢购价格
            if (purshing_price_tv != null) {
                purshing_price_tv.setText("" + info.getPrice() + "元");
                purshing_price_tv.setTextColor(getResources().getColor(R.color.white));
                purshing_price_tv.setVisibility(View.VISIBLE);
            }

            /// 福利价
            if (benefits_purchase_title_tv != null) {
                benefits_purchase_title_tv.setVisibility(View.VISIBLE);
                benefits_purchase_title_tv.setTextColor(getResources().getColor(R.color.white));
            }

            //// 设置截止时间条为灰色
            if (welfare_stop_time_rlay != null) {
                welfare_stop_time_rlay.setBackgroundDrawable(getResources().getDrawable(R.drawable.grab_welfare_over_lable));
            }

            /// 截止时间
            if (stop_timer_tv != null) {
                stop_timer_tv.setText("已结束");
                stop_timer_tv.setTextColor(getResources().getColor(android.R.color.darker_gray));
                stop_timer_tv.setVisibility(View.VISIBLE);
            }
        }

    }

    /**
     * 福利抢购状态,
     *
     * @param info
     */
    private void initWelfareStatus(WelfareInfoEntity info) {
        //  /// 根据info 信息进行状态判断,设置抢购状态样式

        if (info != null && TextUtils.equals(info.getStatus(), "ongoing")) {
            initWelfareGrabOver(info);


        }
        /// 设置团购失败
        else if (info != null && TextUtils.equals(info.getStatus(), "failure")) {

            initWelfareFailure(info);

        }
        /// 设置状态团购成功
        else if (info != null && TextUtils.equals(info.getStatus(), "success")) {
            ///
            initWelfareStatusSuccess(info);

        }
    }

    private void initWelfareRules(WelfareInfoEntity info) {

        if (purchasing_actdetails_tv != null) {
            purchasing_actdetails_tv.setText(info.getRule());
            purchasing_actdetails_tv.setVisibility(View.VISIBLE);
        }
    }

    private void initWelfareGoodsNameInfo(WelfareInfoEntity info) {
        if (welfare_goods_name_tv != null) {
            welfare_goods_name_tv.setText(info.getTitle());
            welfare_goods_name_tv.setVisibility(View.VISIBLE);
        }
    }

    private void loadingGoodsImgs(WelfareInfoEntity info) {
        if (goodImgs != null) {
            String content = info.getContent();
            String imgurls[] = content.split(",");
            if (imgurls != null && imgurls.length > 0) {

                int imgswidth = getDisplayHeightWH75GoodsImgs();
                for (String imgurl : imgurls) {
                    ImageView img = new ImageView(ActivityWelfareIndex.this);
                    LinearLayout.LayoutParams imgvwMargin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, imgswidth);
                    imgvwMargin.setMargins(0, 0, 0, 0);
                    img.setScaleType(ImageView.ScaleType.FIT_XY);
                    img.setLayoutParams(imgvwMargin);
                    com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(imgurl, img);
                    goodImgs.addView(img);
                }
            }
        }
    }

    private void loadingPoster(WelfareInfoEntity info) {
        if (welfare_purchase_goods_niv != null) {
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.welfare_loading_pic)
                    .showImageForEmptyUri(R.drawable.welfare_loading_pic)
                    .showImageOnFail(R.drawable.welfare_loading_pic)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .build();
            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(info.getPoster(), welfare_purchase_goods_niv, options);
        }

        if (left_nums_content_tv != null) {
            left_nums_content_tv.setText("" + (info.getRemain()) + "位");
            if (left_nums_title_tv != null) {
                left_nums_title_tv.setVisibility(View.VISIBLE);
                left_nums_content_tv.setVisibility(View.VISIBLE);
                if (purchase_right_icon_rlay != null) {
                    purchase_right_icon_rlay.setVisibility(View.VISIBLE);/// 设置为可见
                }
            }
        }
    }

    private void initWelfarePriceView(WelfareInfoEntity info) {
//        dd HH:mm:ss
        if (purshing_price_tv != null) {
            purshing_price_tv.setText("" + info.getPrice() + "元");
            purshing_price_tv.setVisibility(View.VISIBLE);
        }
        if (purchasing_status_tv != null) {
            if (TextUtils.equals(info.getStatus(), "ongoing")) {
                purchasing_status_tv.setText("抢购中...");
            } else if (TextUtils.equals(info.getStatus(), "failure")) {
                purchasing_status_tv.setText("抢购失败");

            } else if (TextUtils.equals(info.getStatus(), "success")) {
                purchasing_status_tv.setText("抢购成功");
            }
            purchasing_status_tv.setVisibility(View.VISIBLE);
        }

        if (benefits_purchase_title_tv != null) {
            benefits_purchase_title_tv.setVisibility(View.VISIBLE);
        }

        if (stop_timer_tv != null) {
            stop_timer_tv.setText(formatDuring(info.getEndTime() - (int) (System.currentTimeMillis() / 1000)));
            stop_timer_tv.setVisibility(View.VISIBLE);
        }

    }


    /**
     * 要转换的秒数
     *
     * @param
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     */
    public static String formatDuring(int mss) {
        int days = mss / (60 * 60 * 24);
        int hours = (mss % (60 * 60 * 24)) / (60 * 60);
        int minutes = (mss % (60 * 60)) / (60);
//        long seconds = (mss % (1000 * 60)) / 1000;
//        if(days==0){
//            return  hours + "时" + minutes + "分";
//        }
//        if(hours==0){
//            return  hours + "时" + minutes + "分";
//        }
        return days + "日" + hours + "时" + minutes + "分";
//                + seconds + " seconds ";
    }

    private void loadingGoodsHasGotursHeadImgs4(final WelfareInfoEntity info) {

        final List<WelfareUsersEntity> users = info.getUsers();
        Log.i("debbug", "info.size" + info.getUsers().size());
        if (users.isEmpty()) {
            if (welfare_purchase_hasgoturs_lay != null) {
                welfare_purchase_hasgoturs_lay.setVisibility(View.GONE);
            }
        }
        if (users.size() > 5) {
            for (int i = 0; i < 4; i++) {
                LinearLayout usrHeadView = (LinearLayout) View.inflate(this, R.layout.common_welfare_purchase_hasgoturs_headlay, null);
                ImageView img = (ImageView) usrHeadView.findViewById(R.id.iv_avatar);
                img.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(users.get(i).getAvatar(), img, UserUtils.options);
                TextView headtv = (TextView) usrHeadView.findViewById(R.id.welfare_purchase_hasgoturs_name_tv);
                headtv.setText(users.get(i).getNickname());
                headtv.setVisibility(View.VISIBLE);
                usrHeadView.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams rlparams = (LinearLayout.LayoutParams) img.getLayoutParams();
                rlparams.width = screenWidth * 123 / 1080;
                rlparams.height = screenWidth * 123 / 1080;
                img.setLayoutParams(rlparams);
                LinearLayout.LayoutParams rlparams2 = (LinearLayout.LayoutParams) headtv.getLayoutParams();
                rlparams2.width = screenWidth * 123 / 1080;
                headtv.setLayoutParams(rlparams2);
                final int finalI = i;
                usrHeadView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ActivityWelfareIndex.this, UserGroupInfoActivity.class).putExtra(Config.INTENT_PARMAS2, users.get(finalI).getEmobId()));
                    }
                });
                welfare_purchase_hasgoturs_lv.addView(usrHeadView);
            }
            LinearLayout usrHeadView = (LinearLayout) View.inflate(this, R.layout.common_welfare_purchase_hasgoturs_headlay, null);
            ImageView img = (ImageView) usrHeadView.findViewById(R.id.iv_avatar);
            img.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.gray_see_more, img);
            TextView headtv = (TextView) usrHeadView.findViewById(R.id.welfare_purchase_hasgoturs_name_tv);
            headtv.setVisibility(View.GONE);
            usrHeadView.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams rlparams = (LinearLayout.LayoutParams) img.getLayoutParams();
            rlparams.width = screenWidth * 123 / 1080;
            rlparams.height = screenWidth * 123 / 1080;
            img.setLayoutParams(rlparams);
            LinearLayout.LayoutParams rlparams2 = (LinearLayout.LayoutParams) headtv.getLayoutParams();
            rlparams2.width = screenWidth * 123 / 1080;
            headtv.setLayoutParams(rlparams2);
            usrHeadView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext, ActivityWelfareBuyedMoreUsers.class).putExtra("welfareId", info.getWelfareId()));
                }
            });
            welfare_purchase_hasgoturs_lv.addView(usrHeadView);
        }

        if (users.size() > 0 && users.size() <= 5) {
            for (int i = 0; i < users.size(); i++) {
                LinearLayout usrHeadView = (LinearLayout) View.inflate(this, R.layout.common_welfare_purchase_hasgoturs_headlay, null);
                ImageView img = (ImageView) usrHeadView.findViewById(R.id.iv_avatar);
                img.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(users.get(i).getAvatar(), img, UserUtils.options);
                TextView headtv = (TextView) usrHeadView.findViewById(R.id.welfare_purchase_hasgoturs_name_tv);
                headtv.setText(users.get(i).getNickname());
                headtv.setVisibility(View.VISIBLE);
                usrHeadView.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams rlparams = (LinearLayout.LayoutParams) img.getLayoutParams();
                rlparams.width = screenWidth * 123 / 1080;
                rlparams.height = screenWidth * 123 / 1080;
                img.setLayoutParams(rlparams);
                LinearLayout.LayoutParams rlparams2 = (LinearLayout.LayoutParams) headtv.getLayoutParams();
                rlparams2.width = screenWidth * 123 / 1080;
                headtv.setLayoutParams(rlparams2);

                final int finalI = i;
                usrHeadView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ActivityWelfareIndex.this, UserGroupInfoActivity.class).putExtra(Config.INTENT_PARMAS2, users.get(finalI).getEmobId()));
                    }
                });
                welfare_purchase_hasgoturs_lv.addView(usrHeadView);

            }
        }
    }


}
