package xj.property.activity.welfare;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.Floor;
import xj.property.beans.UserInfoDetailBean;
import xj.property.beans.WXOrderRequestBean;
import xj.property.beans.WXPostOrderInfoBean;
import xj.property.beans.WelfareBangBangPayInfo;
import xj.property.beans.WelfareBangBangPayedBean;
import xj.property.beans.WelfareGetOrderInfoRequest;
import xj.property.cache.OrderDetailModel;
import xj.property.event.WXPayRequestEvent;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseUtils;
import xj.property.netbase.RetrofitFactory;
import xj.property.netbasebean.BounsCoinInfoBean;
import xj.property.utils.DensityUtil;
import xj.property.utils.other.Arith;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.weixinpay.MD5;


/**
 * 抢购福利成功支付订单页面
 */
public class
        ActivityWelfarePrePay extends HXBaseActivity {

    private OrderDetailModel orderDetailModel;

    private TextView tv_goods_sum_price;

    private CheckBox ck_cashondelivery;

    private CheckBox ck_cashonline;
    private CheckBox rb_cashbyali;
    private CheckBox rb_cashbyweixin;

    private TextView tv_fix_address;
    private TextView tv_bangbangbi_price;
    private RelativeLayout rl_paybyarrivel;

    private TextView tv_sum_price;

    private LinearLayout btn_confirm_pay;
    private RelativeLayout rl_bonus;

    //// 帮帮币总数
    private TextView tv_bonus_count;


    private TextView tv_use_status;


//    View mWheelLayout;


    private PopupWindow mPop;
    private View root_view;


    private UserInfoDetailBean bean;


    //// 计算帮帮币可以抵用多少rmb
    private double subPrice = 0;

    int PayCodeOnline = 0;

    private String emobIdUser;

    private int selectedPosition = -1;
    private RelativeLayout rl_paybyonline;
    private RelativeLayout rl_paybyali;

    private String[] item_floors;

    private String[] item_unit;
    private String[] item_data_rooms;

    List<Floor> floors = new ArrayList<>();

//    private WelfareGoingBuyInfo.InfoEntity buyinfo;

    private ImageView purchase_poster_iv;

    /// 商品名称
    private TextView tv_good_name;


    /// 小计
    private TextView tv_single_sum_price;


    /// 实际支付
    private TextView tv_goods_real_pay;


    /// 海报信息
    private String posterstr;

    /// 商品名称
    private String serverName;

    /// 价格
    private float price;

    /// 帮帮币使用选择框
    private CheckBox rb_cashbybangbang;

    private Context mContext;

    private RelativeLayout pay_byweixin;

    //微信支付
    PayReq req;
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    Map<String, String> resultunifiedorder;
    StringBuffer sb;
    /// 物业发货地址
    private String providerInstroctionaddress;
    private BounsCoinInfoBean bounsCoinInfoBean;
    private int welfareId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welfare_paypre);
        this.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.tv_title)).setText("福利支付");
        EventBus.getDefault().register(this);
        mContext = this;

        bean = PreferencesUtil.getLoginInfo(this);

        emobIdUser = bean.getEmobId();

        posterstr = getIntent().getStringExtra("posterstr");

        welfareId = getIntent().getIntExtra("welfareId",0);

        providerInstroctionaddress = getIntent().getStringExtra("provideInstruction");

        serverName = getIntent().getStringExtra("serverName");
        price = getIntent().getFloatExtra("price", 0.0f);


        initView();
        initData();
        //// 获取帮帮币的个数以及帮币的兑换率是否显示帮币
        getBangbiCountExchange();

//        mWheelLayout = View.inflate(this, R.layout.wheel_choose_city_three, null);
//        showWelfareToast("本次抢福利要求最低人品值不低于人品值5");
    }

    private void initData() {
//        if (TextUtils.isEmpty(serial)) {
//            setResult(RESULT_CANCELED);
//            finish();
//            return;
//        }
        if (!TextUtils.isEmpty(posterstr) && purchase_poster_iv != null) {
            ImageLoader.getInstance().displayImage(posterstr, purchase_poster_iv);
            purchase_poster_iv.setVisibility(View.VISIBLE);
        } else if (purchase_poster_iv != null) {
            purchase_poster_iv.setVisibility(View.GONE);
        }

        initBounsView();
//        intiAliPayView();

        orderDetailModel = new OrderDetailModel();
        orderDetailModel.setOnline(true);
        orderDetailModel.setOder_detail_servicename(serverName);
        orderDetailModel.setOder_detail_count("1");
        orderDetailModel.setOder_detail_price("" + price);
//        orderDetailModel.setSerial(serial); v3 把 serial去掉  2016/03/14
        orderDetailModel.setTotal_count(1);
        orderDetailModel.setTotal_price("" + 1 * price);

        tv_good_name.setText(serverName);

        tv_single_sum_price.setText("￥" + 1 * price);

        tv_goods_sum_price.setText("￥" + 1 * price);
        /// 设置收货地址
        tv_fix_address.setText(providerInstroctionaddress);

        tv_goods_real_pay.setText("￥" + (price - subPrice));

        tv_sum_price.setText("￥" + (price - subPrice));

        reloadbangbangView(subPrice);

    }

    private void intiAliPayView() {

        if (isNeedUseAlipay()) {
            //// 帮帮币不够,支付宝选项必须选择
            rb_cashbyali.setChecked(true);
            rl_paybyali.setEnabled(false);
        } else {
            rl_paybyali.setEnabled(true);
        }

    }

    private void initBounsView() {

        if (bounsCoinInfoBean != null && tv_bonus_count != null) {
            tv_bonus_count.setText("" + bounsCoinInfoBean.getCount() + "枚帮帮币可用");
            tv_bonus_count.setVisibility(View.VISIBLE);

            if (bounsCoinInfoBean.getCount() < 0) {
                rb_cashbybangbang.setChecked(false);
                rb_cashbybangbang.setClickable(false);
            } else {
                rb_cashbybangbang.setClickable(true);
            }
        }

    }


    private void initView() {

        tv_fix_address = (TextView) findViewById(R.id.tv_fix_address);

        root_view = findViewById(R.id.root_view);


        tv_good_name = (TextView) findViewById(R.id.tv_good_name);


        tv_single_sum_price = (TextView) findViewById(R.id.tv_single_sum_price);

        tv_goods_real_pay = (TextView) findViewById(R.id.tv_goods_real_pay);


        tv_goods_sum_price = (TextView) findViewById(R.id.tv_goods_sum_price);

        rl_paybyarrivel = (RelativeLayout) findViewById(R.id.rl_paybyarrivel);

        ck_cashondelivery = (CheckBox) findViewById(R.id.ck_cashondelivery);


        rl_paybyonline = (RelativeLayout) findViewById(R.id.rl_paybyonline);

        ck_cashonline = (CheckBox) findViewById(R.id.ck_cashonline);


        rl_paybyali = (RelativeLayout) findViewById(R.id.rl_paybyali);

        rb_cashbyali = (CheckBox) findViewById(R.id.rb_cashbyali);

        rb_cashbyweixin = (CheckBox) findViewById(R.id.rb_cashbyweixin);


        tv_bangbangbi_price = (TextView) findViewById(R.id.tv_bangbangquan_price);

        /// 实际支付/左下角
        tv_sum_price = (TextView) findViewById(R.id.tv_sum_price);

        btn_confirm_pay = (LinearLayout) findViewById(R.id.btn_confirm_pay);

        rl_bonus = (RelativeLayout) findViewById(R.id.rl_bonus);

        if (TextUtils.equals(PreferencesUtil.getShowBonuscoin(this), "no")) {
            Log.d("initView ", "getShowBonuscoin no");
            rl_bonus.setVisibility(View.GONE);
        }


        tv_bonus_count = (TextView) findViewById(R.id.tv_bonus_count);

        tv_use_status = (TextView) findViewById(R.id.tv_use_status);

        purchase_poster_iv = (ImageView) findViewById(R.id.purchase_poster_iv);

        rb_cashbybangbang = (CheckBox) findViewById(R.id.rb_cashbybangbang);

        pay_byweixin = (RelativeLayout) findViewById(R.id.pay_byweixin);
        pay_byweixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_cashbyweixin.setChecked(!rb_cashbyweixin.isChecked());
                rb_cashbyali.setChecked(false);
            }
        });


        rb_cashbybangbang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (bounsCoinInfoBean != null) {
                        if (bounsCoinInfoBean.getCount() * bounsCoinInfoBean.getRatio() > price) {
                            subPrice = price;
                        } else {
                            subPrice = bounsCoinInfoBean.getCount() * bounsCoinInfoBean.getRatio();
                        }
                        reloadbangbangView(subPrice);
                    }
                } else {
                    subPrice = 0.0;
                    reloadbangbangView(subPrice);
                }
            }
        });

        /// 支付宝支付按钮
        rl_paybyali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("debbug", "rlpaybyali=");
                rb_cashbyali.setChecked(!rb_cashbyali.isChecked());

                if (rb_cashbyali.isChecked()) {
                    rl_bonus.setClickable(true);
                    rb_cashbyweixin.setChecked(false);
                    rl_paybyarrivel.setBackgroundColor(getResources().getColor(R.color.white));
                    ck_cashonline.setChecked(true);
                    ck_cashondelivery.setChecked(false);
                }

            }
        });

        //// 确认支付按钮
        btn_confirm_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!rb_cashbyali.isChecked() && !rb_cashbybangbang.isChecked() && !rb_cashbyweixin.isChecked()) {
                    showWelfareToast("请选择支付方式!");
//                    //// 线下支付
//                } else if (ck_cashondelivery.isChecked()) {
//                    Intent data = new Intent();
//                    data.putExtra(Config.EXPKey_ADDRESS, tv_fix_address.getText() + "");
//                    setResult(PayCodeOffline, data);
//                    finish();

                    /// 仅使用支付宝支付
                } else if (rb_cashbyali.isChecked() && !rb_cashbybangbang.isChecked()) {
                    usealiPay("0", "0.0");

                } else if (rb_cashbyali.isChecked() && rb_cashbybangbang.isChecked()) {
                    if (isNeedUseAlipay()) {

                        //// 调用帮帮币支付,然后支付宝支付.

                        usealiPay("" + getUsedBangBangBiCount(), "" + subPrice);


                    } else {
                        //// 不需要支付宝支付,直接使用帮帮币支付.
                        bangbangbiPay();
                    }

                    //// 如果支付宝没有选中帮帮币支付选中,但是如果帮帮币不够,则
                } else if (!rb_cashbyali.isChecked() && rb_cashbybangbang.isChecked() && !rb_cashbyweixin.isChecked()) {
                    if (isNeedUseAlipay()) {
                        String showTxt = "您的帮帮币不够支付本次购买,请点击其他支付剩余款项";
                        showDialogToast(showTxt);

                    } else {
                        //// 不需要支付宝支付,直接使用帮帮币支付.
                        bangbangbiPay();
                    }

                } else if (rb_cashbyweixin.isChecked() && !rb_cashbybangbang.isChecked()) {

                    weixinpay();

                } else if (rb_cashbyweixin.isChecked() && rb_cashbybangbang.isChecked()) {

                    if (isNeedUseAlipay()) {
                        weixinpay();
                    } else {
                        bangbangbiPay();
                    }
                }
            }
        });

        //// 帮帮币支付选择项
        rl_bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rb_cashbybangbang.setChecked(!rb_cashbybangbang.isChecked());
                if (rb_cashbybangbang.isChecked()) {

                    if (bounsCoinInfoBean != null) {
                        /// 如果够使用 支付宝按钮可以点掉
                        if (bounsCoinInfoBean.getCount() * bounsCoinInfoBean.getRatio() >= price) {
                            subPrice = price;
                            ///不够使用 支付宝按钮不可以点掉
                        } else if (bounsCoinInfoBean.getCount() * bounsCoinInfoBean.getRatio() < price) {
                            if (!rb_cashbyali.isChecked()) {
                                String showTxt = "您的帮帮币不够支付本次购买,请点击其他支付剩余款项";
                                showDialogToast(showTxt);
                            }
                            subPrice = bounsCoinInfoBean.getCount() * bounsCoinInfoBean.getRatio();
                        }
                        reloadbangbangView(subPrice);
                    }
                } else {
                    subPrice = 0.0;
                    reloadbangbangView(subPrice);
                }
            }


        });

    }

    /**
     * * 获取帮帮币数量
     */
    private void getBangbiCountExchange() {
        NetBaseUtils.extractBounsCoinInfo(getmContext(),PreferencesUtil.getCommityId(getmContext()), bean.getEmobId(), new NetBaseUtils.NetRespListener<CommonRespBean<BounsCoinInfoBean>>() {
            @Override
            public void successYes(CommonRespBean<BounsCoinInfoBean> commonRespBean, Response response) {
                //// 没开通
                bounsCoinInfoBean = commonRespBean.getData();
                if (0 == bounsCoinInfoBean.getEnable()) {
                    if (rl_bonus != null) {
                        rl_bonus.setVisibility(View.GONE);
                    }
                    android.util.Log.d("getShowBonuscoin ", "  getShowBonuscoin no ");
                } else {
                    if (rl_bonus != null) {
                        rl_bonus.setVisibility(View.VISIBLE);
                    }
                    android.util.Log.d("getShowBonuscoin ", "  getShowBonuscoin yes ");
                }
                float bangbiExchange = (float) bounsCoinInfoBean.getRatio();
                android.util.Log.d("getBangbiCountExchange ", "  bangbiExchange " + bangbiExchange);
                int bangbiCount = bounsCoinInfoBean.getCount();

                android.util.Log.d("getBangbiCountExchange ", "  bangbiCount " + bangbiCount);
                initData();
            }

            @Override
            public void successNo(CommonRespBean<BounsCoinInfoBean> commonRespBean, Response response) {
                showToast("获取帮帮币信息失败：" + commonRespBean.getMessage());
            }

            @Override
            public void failure(RetrofitError error) {
                showNetErrorToast();
            }
        });
    }


    private boolean isNeedUseAlipay() {

        if (bounsCoinInfoBean.getCount() * bounsCoinInfoBean.getRatio() >= price) {
            return false;
        } else {
            return true;
        }

    }


    //// 使用支付宝支付
    private void usealiPay(String bonusCoin, String bonusCoinPrice) {

        Intent intent = getIntent();
        intent.setClass(ActivityWelfarePrePay.this, ActivityWelfarePay.class);
        WelfareGetOrderInfoRequest welfareGetOrderInfoRequest = new WelfareGetOrderInfoRequest();

//        {
//                "totalFee": "233",
//                "subject": "帮帮福利",
//                "cityId": 373,
//                "communityId": 1,
//                "emobId": "d463b16dfc014466a1e441dd685ba505",
//                "bonuscoinCount": 100,
//                "dataId":93
//        }
//
//        {
//            "beanId": "alipayWelfare",
//                "totalFee": "{支付金额，未减去帮帮券和帮帮币折扣之前的金额，单位元}",
//                "subject": "{商品名称}",
//                "cityId": {城市ID},
//            "communityId": {小区ID},
//            "emobId": "{用户环信ID}",
//                "dataId": {福利ID},
//            "bonuscoinCount": {帮帮币数量}
//        }


        welfareGetOrderInfoRequest.setCommunityId(bean.getCommunityId());
        welfareGetOrderInfoRequest.setCityId(bean.getCityId());
        welfareGetOrderInfoRequest.setSubject("福利商品");/// servername
        welfareGetOrderInfoRequest.setEmobId(bean.getEmobId());
        welfareGetOrderInfoRequest.setTotalFee(orderDetailModel.getTotal_price());

        /// 帮帮币使用数
        welfareGetOrderInfoRequest.setBonusCoinCount(bonusCoin);

        welfareGetOrderInfoRequest.setDataId(welfareId);
        /// 实际支付  setRealPrice
        welfareGetOrderInfoRequest.setPrice("" + Arith.round(Arith.sub(Double.parseDouble(orderDetailModel.getTotal_price()), subPrice), 2));

        intent.putExtra("welfareOrderInfo", welfareGetOrderInfoRequest);
        startActivityForResult(intent, PayCodeOnline);

    }


    /// 帮帮币够用使用帮帮币支付
    private void bangbangbiPay() {

        final Dialog dialog = new Dialog(mContext, R.style.werlfare_DialogStyle);
        dialog.setContentView(R.layout.common_welfare_toast_or_prepay);
        FrameLayout.LayoutParams layparams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(mContext, 125));
        layparams.setMargins(0, 0, 0, 0);
        dialog.findViewById(R.id.dialog_root_ll).setLayoutParams(layparams);
        dialog.findViewById(R.id.dialog_opt_okcancel_ll).setVisibility(View.VISIBLE);
        dialog.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                subPrice = price;
                reloadbangbangView(subPrice);
                dialog.dismiss();
                rb_cashbybangbang.setChecked(true);

                WelfareBangBangPayInfo welfareBangBangPayInfo = new WelfareBangBangPayInfo();
                welfareBangBangPayInfo.setBonuscoin(getUsedBangBangBiCount());
                welfareBangBangPayInfo.setEmobId(bean.getEmobId());
                welfareBangBangPayInfo.setWelfareId(welfareId);
                welfareBangBangPayInfo.setCommunityId(bean.getCommunityId());
//
//                {
//                    "welfareId": 93,
//                        "communityId": 2,
//                        "emobId": "d463b16dfc014466a1e441dd685ba505",
//                        "bonuscoin": 698
//                }

                welfareBangBangBiPay(welfareBangBangPayInfo);

            }

        });
        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subPrice = 0.0;
                //// 使用全部帮帮币进行购买 抵用RMB 更新至帮帮币界面
                reloadbangbangView(subPrice);
                dialog.dismiss();
                rb_cashbybangbang.setChecked(false);
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                subPrice = 0.0;
                //// 使用全部帮帮币进行购买 抵用RMB 更新至帮帮币界面
                reloadbangbangView(subPrice);
                dialog.dismiss();
                rb_cashbybangbang.setChecked(false);
            }

        });

        ((TextView) dialog.findViewById(R.id.toast_title_tv)).setText("本次购买是否全部使用帮帮币支付");
        dialog.show();


    }


    //// 如果帮帮币够获取使用的帮帮币
    private int getUsedBangBangBiCount() {

        double canuseRMB = bounsCoinInfoBean.getCount() * bounsCoinInfoBean.getRatio();
        if (canuseRMB > price) {
            return (int) Arith.div(price, bounsCoinInfoBean.getRatio(), 0);
        } else {
            return bounsCoinInfoBean.getCount();
        }
    }


    private void showDialogToast(String showTxt) {

        ///// 不足以支付 弹窗 没按钮
        final Dialog dialog = new Dialog(mContext, R.style.werlfare_DialogStyle);
        dialog.setContentView(R.layout.common_welfare_toast_or_prepay);
        ((TextView) dialog.findViewById(R.id.toast_title_tv)).setText(showTxt);
        dialog.show();
        CountDownTimer countDownTimer = new CountDownTimer(2 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                dialog.dismiss();
            }
        };
        countDownTimer.start();
    }


    private void reloadbangbangView(double subPrice) {

        if (tv_bangbangbi_price != null) {
            tv_bangbangbi_price.setText("-￥ " + String.valueOf(Arith.round(subPrice, 2)));
        }

        if (tv_single_sum_price != null) {
            tv_single_sum_price.setText("￥" + String.valueOf(Arith.round(price, 2)));
        }

        if (tv_goods_sum_price != null) {
            tv_goods_sum_price.setText("￥" + String.valueOf(Arith.round(price, 2)));
        }


        if (tv_goods_real_pay != null) {
            tv_goods_real_pay.setText("￥" + String.valueOf(Arith.round(Arith.sub(Double.parseDouble(orderDetailModel.getTotal_price()), subPrice), 2)));
        }

        if (tv_sum_price != null) {
            tv_sum_price.setText("￥" + String.valueOf(Arith.round(Arith.sub(Double.parseDouble(orderDetailModel.getTotal_price()), subPrice), 2)));
        }

    }


    interface GetOrderInfoService {

//        @PUT("/api/v1/communities/{communityId}/welfares/bonusCoin/pay")
//        void getOrderInfo(@Header("signature") String signature, @Body WelfareBangBangPayInfo acr, @Path("communityId") long communityId, Callback<WelfareBangBangPayedBean> cb);
        @POST("/api/v3/welfares/bonuscoinPay")
        void welfareBangBangBiPay(@Body WelfareBangBangPayInfo acr, Callback<CommonRespBean<WelfareBangBangPayedBean>> cb);
    }


    /**
     * 帮帮币支付
     *
     * @param request
     */
    private void welfareBangBangBiPay(WelfareBangBangPayInfo request) {
        mLdDialog.show();
        GetOrderInfoService service = RetrofitFactory.getInstance().create(getmContext(),request,GetOrderInfoService.class);
        Callback<CommonRespBean<WelfareBangBangPayedBean>> callback = new Callback<CommonRespBean<WelfareBangBangPayedBean>>() {
            @Override
            public void success(CommonRespBean<WelfareBangBangPayedBean> bean, Response response) {
                mLdDialog.dismiss();
                if (bean != null && TextUtils.equals(bean.getStatus(), "yes")) {

                    /// 帮帮币支付成功
                    showWelfareToast("支付成功");
                    Intent intent = new Intent();
                    intent.setClass(mContext, ActivityWelfareIndex.class);
                    setResult(RESULT_OK, intent);
                    finish();

                } else if (bean != null && TextUtils.equals(bean.getStatus(), "no")) {
                    String msg = bean.getMessage();
                    if (TextUtils.isEmpty(msg) || TextUtils.equals(msg, "null")) {
                        showWelfareToast("支付超时");
                    } else {
                        showWelfareToast(msg);
                    }
                } else {
                    String msg = bean.getMessage();
                    if (TextUtils.isEmpty(msg) || TextUtils.equals(msg, "null")) {
                        showWelfareToast("支付失败");
                    } else {
                        showWelfareToast(msg);
                    }
                }

            }
            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                showWelfareToast("" + error.getMessage());
            }
        };

        //{"subject":"商家重置","price":"0.01","beanId":"crazySalesProcessor"}
        // {"subject":"","body":"","price":"","orderId":0,"userBonusId":0}

        ////   支付信息确认
        service.welfareBangBangBiPay(request, callback);

    }

    private void showWelfareToast(String showT) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.common_welfare_toast_lay, null);
        TextView title = (TextView) layout.findViewById(R.id.toast_title_tv);
        title.setText(showT);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PayCodeOnline) {
            if (resultCode == 103) {

                //  showWelfareToast("支付成功");

                Intent intent = new Intent();
                intent.setClass(ActivityWelfarePrePay.this, ActivityWelfareIndex.class);
                setResult(RESULT_OK, intent);
                finish();
            } else {

                //   showWelfareToast("支付充值失败");
                Intent intent = new Intent();
                intent.setClass(ActivityWelfarePrePay.this, ActivityWelfareIndex.class);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        } else {

            if (requestCode == 200) {
                if (resultCode == 200) {
                    String bonusPrice = data.getStringExtra("bonusPrice");
                    String temp = data.getStringExtra("selectedPosition");
                    if (temp == null || temp.equals("null")) {
                        selectedPosition = -1;
                    } else {
                        selectedPosition = Integer.parseInt(temp);
                    }
                    if (bonusPrice != null && !("null".equals(bonusPrice)) && selectedPosition > -1) {
                        subPrice = Double.parseDouble(bonusPrice);

                        if (Arith.sub(Double.parseDouble(orderDetailModel.getTotal_price()), subPrice) <= 0) {
                            selectedPosition = -1;
                            subPrice = 0;
                            showToast("对不起，商品总价格不能低于帮帮券价格！");
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_sum_price.setText("￥ " + Arith.sub(Double.parseDouble(orderDetailModel.getTotal_price()), subPrice));
                                    tv_bangbangbi_price.setText("-￥ " + subPrice);
                                    tv_use_status.setText("已使用");
                                }
                            });
                        }

                    } else {
                        subPrice = 0;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_sum_price.setText("￥ " + Arith.sub(Double.parseDouble(orderDetailModel.getTotal_price()), subPrice));
                                tv_bangbangbi_price.setText("-￥ " + subPrice);
                                tv_use_status.setText("未使用");
                            }
                        });
                    }
                }
            }
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }


    }

    private void showPw(View v) {
        if (mPop == null) {
            mPop = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mPop.setOutsideTouchable(false);
            mPop.setFocusable(true);
            // mPop.setBackgroundDrawable(new BitmapDrawable());
            mPop.showAtLocation(root_view, Gravity.BOTTOM, 0, 0);
            mPop.update();

        } else {
            mPop.showAtLocation(root_view, Gravity.BOTTOM, 0, 0);

        }
    }

    private void dismissPw() {
        if (mPop != null) {
            mPop.dismiss();
            mPop = null;
        }
    }

    private void weixinpay() {
        req = new PayReq();
        sb = new StringBuffer();
        msgApi.registerApp(Config.APP_ID);
        if (!msgApi.isWXAppInstalled()) {
            showToast("您还没有安装微信哦!");
            return;
        }
        if (!msgApi.isWXAppSupportAPI()) {
            showToast("您当前的微信版本不支持微信支付！");
            return;
        }
//        GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
//        getPrepayId.execute();
        getWXOrder();
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Config.API_KEY);

        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion", appSign);
        return appSign;
    }


    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }


    private void genPayReq(String prepay_id) {

        req.appId = Config.APP_ID;
        req.partnerId = Config.MCH_ID;
        req.prepayId = prepay_id;
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());


        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);

        sb.append("sign\n" + req.sign + "\n\n");

//        showToast(sb.toString());

        Log.e("orion", signParams.toString());

    }

    private void sendPayReq() {

        msgApi.registerApp(Config.APP_ID);
        msgApi.sendReq(req);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(WXPayRequestEvent event) {
        if (!event.isSuccess()) {
            showToast("支付失败");
            ///2015/12/25 有问题???
            Intent intent = new Intent();
            intent.setClass(ActivityWelfarePrePay.this, ActivityWelfareIndex.class);
            setResult(RESULT_CANCELED, intent);
            finish();
        } else {
            showToast("支付成功");
            Intent intent = new Intent();
            intent.setClass(ActivityWelfarePrePay.this, ActivityWelfareIndex.class);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    /**
     * 获取微信支付预支付订单
     */
    interface WXOrderService {
//        @POST("/api/v1/wxpay/wxpayWelfareProcessor")
//        void postInfo(@Header("signature") String signature, @Body WXPostOrderInfoBean bean, Callback<WXOrderRequestBean> cb);

        @POST("/api/v3/wxpay")
        void postInfo(@Body WXPostOrderInfoBean bean, Callback<CommonRespBean<WXOrderRequestBean>> cb);

    }

    private void getWXOrder() {
        mLdDialog.show();

        WXPostOrderInfoBean request = new WXPostOrderInfoBean();
        request.setSubject("帮帮福利");
        request.setCityId(bean.getCityId());
        request.setCommunityId(PreferencesUtil.getCommityId(this));
        request.setEmobId(bean.getEmobId());
        request.setDataId(welfareId);
        request.setTotalFee("" + (int) (Arith.sub(Double.parseDouble(orderDetailModel.getTotal_price()), subPrice) * 100));

        if (rb_cashbybangbang.isChecked()) {
            request.setBonuscoinCount(getUsedBangBangBiCount());
        } else {
            request.setBonuscoinCount(0);
        }
        WXOrderService service = RetrofitFactory.getInstance().create(getmContext(),request,WXOrderService.class);
        Callback<CommonRespBean<WXOrderRequestBean>> callback = new Callback<CommonRespBean<WXOrderRequestBean>>() {
            @Override
            public void success(CommonRespBean<WXOrderRequestBean> bean, Response response) {
                mLdDialog.dismiss();
                if ("yes".equals(bean.getStatus())) {

                    /**
                     * {
                     "sign": "637520C28B0F2A006D8CC61FDF9DF543",
                     "orderNo": "1020020160229122445493427",
                     "prepay_id": "wx20160229122446d1f8c09e3e0031144653",
                     "nonce_str": "vdjkffyqid2nnljdyrppgc7lx2o18nja"
                     }
                     */

                    List<NameValuePair> signParams = new LinkedList<NameValuePair>();
//                    if (bean.getInfo().getCode_url() != null) {
//                        signParams.add(new BasicNameValuePair("code_url", bean.getInfo().getCode_url()));
//                    }

                    signParams.add(new BasicNameValuePair("orderNo", bean.getData().getOrderNo()));
                    signParams.add(new BasicNameValuePair("nonce_str", bean.getData().getNonce_str()));
                    signParams.add(new BasicNameValuePair("prepay_id", bean.getData().getPrepay_id()));

//                    signParams.add(new BasicNameValuePair("trade_type", bean.getInfo().getTrade_type()));

//TODO  V3 2016/03/14        String sign = genAppSign(signParams);
//                    if (sign.equals(bean.getData().getSign())) {
                        genPayReq(bean.getData().getPrepay_id());
                        sendPayReq();

//                    } else {
//                        showToast("签名异常");
//                    }

                } else {
                    showToast("订单获取失败");
                }

            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                showNetErrorToast();
            }
        };
        service.postInfo(request, callback);
    }
}
