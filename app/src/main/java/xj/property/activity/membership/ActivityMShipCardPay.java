package xj.property.activity.membership;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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
import xj.property.beans.MSPCardBean;
import xj.property.beans.MspBangbiReqPayInfo;
import xj.property.beans.MspBangbiRespPayBean;
import xj.property.beans.MspCardGetOrderInfoRequest;
import xj.property.beans.MspWXPostOrderInfoBean;
import xj.property.beans.MspWXPostOrderInfoRespBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.beans.WelfareGoingBuyInfo;
import xj.property.cache.OrderDetailModel;
import xj.property.event.NetworkStateChangeEvent;
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
 * 会员卡支付页面
 */
public class ActivityMShipCardPay extends HXBaseActivity {

    private View msp_pay_consume_total; /// 本次消费
    private View msp_pay_discount_subrmb; /// 优惠金额
    private View msp_pay_discount_after; /// XXX折后价格
    private View msp_pay_goods_name;  /// 店名


    private TextView tv_goods_sum_price;/// 总价
    private TextView tv_bangbangbi_price; /// 帮帮币抵扣价格
    private TextView tv_goods_real_pay; /// 实际支付

    //微信支付
    PayReq req;
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    Map<String,String> resultunifiedorder;
    StringBuffer sb;
    /// 物业发货地址
    private String providerInstroctionaddress;

    private RelativeLayout rl_paybyarrivel;

    private CheckBox ck_cashondelivery;

    private RelativeLayout rl_paybyonline;

    private CheckBox ck_cashonline; /// 在线支付选择 ,没有使用
    private RelativeLayout rl_paybyali; /// 支付宝支付
    private CheckBox rb_cashbyali;
    private CheckBox rb_cashbyweixin;
    private TextView tv_sum_price;

    private LinearLayout btn_confirm_pay;
    private RelativeLayout rl_bonus;

    private TextView tv_bonus_count;

    private TextView tv_use_status;

    private CheckBox rb_cashbybangbang;

    private RelativeLayout pay_byweixin;


    //// 计算帮帮币可以抵用多少rmb
    private double bangbiedsubPrice = 0;

    /// 价格
    private float price;


    private UserInfoDetailBean bean;


    private int PayCodeOnline = 0 ;

    private OrderDetailModel orderDetailModel;

    //// 商品名称

    private MSPCardBean.MSPCardDetailBean mspcardBean;

    private double discount;


    private String inputRMBStr ;


    private double disedprice;

    private double disprice;

    /// 帮帮币的置换率
    private float  bangbiExchange = 0.01f;
    /// 帮帮币的数量
    private int bangbiCount;

    private String serial;// 支付成功订单号

    private double realpay; /// 实际支付金额

    private boolean isDataLoadComplete  = false;
    private BounsCoinInfoBean bounsCoinInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msp_card_pay);
        EventBus.getDefault().register(this);

        bean = PreferencesUtil.getLoginInfo(this);
        mspcardBean= (MSPCardBean.MSPCardDetailBean) getIntent().getSerializableExtra("MSPCardBeanShopInfo");

        inputRMBStr =   getIntent().getStringExtra("inputRMB");
        discount = mspcardBean.getDiscount();

        Log.d("ActivityMShipCardPay ", "discount  " + discount);

        Log.d("ActivityMShipCardPay ","inputRMBStr "+  inputRMBStr);
        if(Double.valueOf(inputRMBStr)<0.1){
            /// 折扣后价格
            disedprice = Double.valueOf(inputRMBStr);
            /// 折扣价格
            disprice = 0.0;
        }else{
            /// 折扣后价格
            disedprice = Double.valueOf(inputRMBStr)*discount * 0.1;
            Log.d("ActivityMShipCardPay ","disprice "+  disedprice);
            /// 折扣价格
            disprice = Double.valueOf(inputRMBStr)-disedprice;
        }

        price = Float.valueOf(String.valueOf(disedprice));

        bangbiedsubPrice = 0;

        initView();
//        initData();
        getBangbiCountExchange();
    }

    private void initTitle() {

        this.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tv_title= (TextView)this.findViewById(R.id.tv_title);
        tv_title.setText(mspcardBean.getShopName());

    }

    private void  initData() {

        orderDetailModel = new OrderDetailModel();
        orderDetailModel.setOnline(true);
        orderDetailModel.setOder_detail_servicename(mspcardBean.getShopName());
        orderDetailModel.setOder_detail_count("1");
        orderDetailModel.setOder_detail_price("" + price);
        orderDetailModel.setTotal_count(1);
        orderDetailModel.setTotal_price(""+price);

        if(msp_pay_consume_total!=null){

            ((TextView)msp_pay_consume_total.findViewById(R.id.tv_good_name)).setText("本次消费");
            ((TextView)msp_pay_consume_total.findViewById(R.id.tv_single_sum_price)).setText("¥"+Arith.round(Double.valueOf(inputRMBStr),2));

            ((TextView)msp_pay_discount_subrmb.findViewById(R.id.tv_good_name)).setText("优惠金额");
            ((TextView)msp_pay_discount_subrmb.findViewById(R.id.tv_single_sum_price)).setText("-"+ String.valueOf(Arith.round(disprice, 2)) );

            ((TextView)msp_pay_discount_after.findViewById(R.id.tv_good_name)).setText(mspcardBean.getDiscount()+"折后价格");
            ((TextView)msp_pay_discount_after.findViewById(R.id.tv_single_sum_price)).setText("¥"+String.valueOf(Arith.round(disedprice, 2)));

            ((TextView)msp_pay_goods_name.findViewById(R.id.tv_good_name)).setText(mspcardBean.getShopName());
            ((TextView)msp_pay_goods_name.findViewById(R.id.tv_single_sum_price)).setText("¥"+String.valueOf(Arith.round(disedprice, 2)));


        }else{
            showToast("数据异常,请重试");
            finish();
        }

        initBounsView();
//        intiAliPayView();

        tv_goods_sum_price.setText("￥" + 1 * price);

        tv_goods_real_pay.setText("￥" + (price - bangbiedsubPrice));

        tv_sum_price.setText("￥" + (price - bangbiedsubPrice));

        reloadbangbangView(bangbiedsubPrice);
    }

    private void intiAliPayView() {

        if(isNeedUseAlipay()){
            //// 帮帮币不够,支付宝选项必须选择
            rb_cashbyali.setChecked(true);
            rl_paybyali.setEnabled(false);
        }else{
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
        initTitle();
        msp_pay_consume_total =    this.findViewById(R.id.msp_pay_consume_total);

        msp_pay_discount_subrmb =    this.findViewById(R.id.msp_pay_discount_subrmb);

        msp_pay_discount_after =    this.findViewById(R.id.msp_pay_discount_after);

        msp_pay_goods_name =    this.findViewById(R.id.msp_pay_goods_name);
        /// 总价
        tv_goods_sum_price = (TextView) this.findViewById(R.id.tv_goods_sum_price);

        /// 帮帮币抵扣价格
        tv_bangbangbi_price = (TextView) this.findViewById(R.id.tv_bangbangbi_price);

        tv_goods_real_pay = (TextView) this.findViewById(R.id.tv_goods_real_pay);


        rl_paybyarrivel = (RelativeLayout) findViewById(R.id.rl_paybyarrivel);

        ck_cashondelivery = (CheckBox) findViewById(R.id.ck_cashondelivery);


        rl_paybyonline = (RelativeLayout) findViewById(R.id.rl_paybyonline);

        ck_cashonline = (CheckBox) findViewById(R.id.ck_cashonline);


        rl_paybyali = (RelativeLayout) findViewById(R.id.rl_paybyali);


        rb_cashbyali = (CheckBox) findViewById(R.id.rb_cashbyali);

        rb_cashbyweixin = (CheckBox) findViewById(R.id.rb_cashbyweixin);

        /// 实际支付/左下角
        tv_sum_price = (TextView) findViewById(R.id.tv_sum_price);

        /// 支付按钮
        btn_confirm_pay = (LinearLayout) findViewById(R.id.btn_confirm_pay);

        /// 帮帮币支付
        rl_bonus = (RelativeLayout) findViewById(R.id.rl_bonus);

        //// 有几枚帮帮币可用
        tv_bonus_count = (TextView) findViewById(R.id.tv_bonus_count);
        tv_use_status = (TextView) findViewById(R.id.tv_use_status);

        /// 帮帮币支付
        rb_cashbybangbang = (CheckBox) findViewById(R.id.rb_cashbybangbang);

        /// 微信支付
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

                            bangbiedsubPrice = price;

                        } else {
                            bangbiedsubPrice =bounsCoinInfoBean.getCount() * bounsCoinInfoBean.getRatio();
                        }

                        reloadbangbangView(bangbiedsubPrice);

                    }
                } else {
                    bangbiedsubPrice = 0.0;
                    reloadbangbangView(bangbiedsubPrice);
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


                if (!rb_cashbyali.isChecked() && !rb_cashbybangbang.isChecked() && !rb_cashbyweixin.isChecked() ) {
                    showWelfareToast("请选择支付方式!");
//                    //// 线下支付
//                } else if (ck_cashondelivery.isChecked()) {
//                    Intent data = new Intent();
//                    data.putExtra(Config.EXPKey_ADDRESS, tv_fix_address.getText() + "");
//                    setResult(PayCodeOffline, data);
//                    finish();

                    /// 仅使用支付宝支付
                } else if (rb_cashbyali.isChecked() && !rb_cashbybangbang.isChecked()) {
                    usealiPay(0, "0.0");

                } else if (rb_cashbyali.isChecked() && rb_cashbybangbang.isChecked()) {
                    if (isNeedUseAlipay()) {
                        //// 调用帮帮币支付,然后支付宝支付.

                        usealiPay(getUsedBangBangBiCount(), "" + bangbiedsubPrice);

                    } else {
                        //// 不需要支付宝支付,直接使用帮帮币支付.
                        bangbangbiPay();
                    }

                    //// 如果支付宝没有选中帮帮币支付选中,但是如果帮帮币不够,则
                } else if(!rb_cashbyali.isChecked() && rb_cashbybangbang.isChecked()&&!rb_cashbyweixin.isChecked()){
                    if(isNeedUseAlipay()){
                        String showTxt = "您的帮帮币不够支付本次购买,请点击其他支付剩余款项";
                        showDialogToast(showTxt);

                    }else{
                        //// 不需要支付宝支付,直接使用帮帮币支付.
                        bangbangbiPay();
                    }

                }else if (rb_cashbyweixin.isChecked()&&!rb_cashbybangbang.isChecked()) {

                    weixinpay();

                }else if(rb_cashbyweixin.isChecked()&&rb_cashbybangbang.isChecked()){

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

                            bangbiedsubPrice = price;

                            ///不够使用 支付宝按钮不可以点掉
                        } else  {

                            if(!rb_cashbyali.isChecked()){
                                String showTxt = "您的帮帮币不够支付本次购买,请点击其他支付剩余款项";
                                showDialogToast(showTxt);
                            }
                            bangbiedsubPrice =bounsCoinInfoBean.getCount() * bounsCoinInfoBean.getRatio();

                        }

                        reloadbangbangView(bangbiedsubPrice);
                    }
                } else {
                    bangbiedsubPrice = 0.0;
                    reloadbangbangView(bangbiedsubPrice);
                }
            }


        });

        ////////////////////

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PayCodeOnline) {
            if (resultCode == 103) {
                //  showWelfareToast("支付成功");

                Intent intent = new Intent();

                intent.setClass(getmContext(), ActivityMShipCardPaySuccess.class);
//                setResult(RESULT_OK, intent);
                intent.putExtra("MSPCardBeanShopInfo", mspcardBean);
                intent.putExtra("paytime",System.currentTimeMillis());
                intent.putExtra("serial",data.getStringExtra("serial"));
                intent.putExtra("payedprice",""+Arith.round(realpay,2));

                startActivityForResult(intent, PayCodeOnline);

            } else  if(resultCode ==RESULT_CANCELED) {

                //   showWelfareToast("支付充值失败");
                Intent intent = new Intent();
                intent.setClass(getmContext(), ActivityMShipCardDisCount.class);
                setResult(RESULT_CANCELED, intent);

            }else if(resultCode ==RESULT_OK){

                Intent intent =new Intent (getmContext(), ActivityMShipCardDisCount.class);
                intent.putExtra("ShopVipcardId",""+mspcardBean.getNearbyVipcardId());
                intent.putExtra("inputrmb", inputRMBStr);

                setResult(RESULT_OK,intent);

//                showToast("MispCard pay  RESULT_OK " + "ShopVipcardId"+mspcardBean.getShopVipcardId());

                finish();
            }
        }
    }



    private boolean isNeedUseAlipay() {
            if (bounsCoinInfoBean.getCount() * bounsCoinInfoBean.getRatio() >= price) {
                return false;
            } else {
                return true;
            }
    }


    //// 使用支付宝支付
    private void usealiPay(int bonusCoin, String bonusCoinPrice) {

        Intent intent = getIntent();
        intent.setClass(getmContext(), ActivityMspAliPay.class);

        MspCardGetOrderInfoRequest mspCardGetOrderInfoRequest = new MspCardGetOrderInfoRequest();

        mspCardGetOrderInfoRequest.setSubject(mspcardBean.getShopName());
        /// 帮帮币使用数
        mspCardGetOrderInfoRequest.setBonuscoinCount(bonusCoin);
        /// 实际支付  setRealPrice
        realpay = Arith.sub(Double.parseDouble(orderDetailModel.getTotal_price()), bangbiedsubPrice);

        mspCardGetOrderInfoRequest.setTotalFee("" + Arith.round(realpay, 2));
        mspCardGetOrderInfoRequest.setShopEmobId(mspcardBean.getEmobId());
        mspCardGetOrderInfoRequest.setDiscount("" + discount); // 折扣价格
        mspCardGetOrderInfoRequest.setCityId(bean.getCityId());
        mspCardGetOrderInfoRequest.setEmobId(bean.getEmobId());
        mspCardGetOrderInfoRequest.setCommunityId(bean.getCommunityId());
        mspCardGetOrderInfoRequest.setOrderPrice(inputRMBStr);

//        mspCardGetOrderInfoRequest.setDiscountPrice(String.valueOf(Arith.round(disedprice, 2)));      /// 10/22 添加折扣后价格字段

        intent.putExtra("mspCardGetOrderInfoRequest", mspCardGetOrderInfoRequest);

        startActivityForResult(intent, PayCodeOnline);

    }




    /// 帮帮币够用使用帮帮币支付
    private void bangbangbiPay() {

        final Dialog dialog = new Dialog(getmContext(), R.style.werlfare_DialogStyle);
        dialog.setContentView(R.layout.common_welfare_toast_or_prepay);
        FrameLayout.LayoutParams layparams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getmContext(), 125));
        layparams.setMargins(0, 0, 0, 0);
        dialog.findViewById(R.id.dialog_root_ll).setLayoutParams(layparams);
        dialog.findViewById(R.id.dialog_opt_okcancel_ll).setVisibility(View.VISIBLE);
        dialog.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bangbiedsubPrice = price;
                reloadbangbangView(bangbiedsubPrice);
                dialog.dismiss();
                rb_cashbybangbang.setChecked(true);


                MspBangbiReqPayInfo mspBangbiReqPayInfo = new MspBangbiReqPayInfo();
                mspBangbiReqPayInfo.setOrderPrice(inputRMBStr);

                //// 10/22 帮帮币支付添加 折后价格字段
                mspBangbiReqPayInfo.setDiscountPrice(String.valueOf(Arith.round(disedprice, 2)));
                mspBangbiReqPayInfo.setDiscount(String.valueOf(discount));
                mspBangbiReqPayInfo.setBonuscoin(getUsedBangBangBiCount());
                mspBangbiReqPayInfo.setCommunityId(bean.getCommunityId());
                mspBangbiReqPayInfo.setEmobIdShop(mspcardBean.getEmobId());
                mspBangbiReqPayInfo.setEmobIdUser(bean.getEmobId());
                realpay = price;
                mspBangBangBiPay(mspBangbiReqPayInfo);

            }

        });
        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bangbiedsubPrice = 0.0;
                //// 使用全部帮帮币进行购买 抵用RMB 更新至帮帮币界面
                reloadbangbangView(bangbiedsubPrice);
                dialog.dismiss();
                rb_cashbybangbang.setChecked(false);
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                bangbiedsubPrice = 0.0;
                //// 使用全部帮帮币进行购买 抵用RMB 更新至帮帮币界面
                reloadbangbangView(bangbiedsubPrice);
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
        final Dialog dialog = new Dialog(getmContext(), R.style.werlfare_DialogStyle);
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

//
//        if (tv_single_sum_price != null) {
//            tv_single_sum_price.setText("￥" + String.valueOf(Arith.round(price, 2)));
//        }


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
        /**
         * 帮帮币支付
         * @param acr
         * @param cb
         */
//        @POST("/api/v1/shopVipcards/pay")
//        /api/v3/nearbyVipcards/bonuscoinPay
        @POST("/api/v3/nearbyVipcards/bonuscoinPay")
        void mspBangBangbiPay(@Body MspBangbiReqPayInfo acr, Callback<CommonRespBean<MspBangbiRespPayBean>> cb);
    }


    /**
     * 获取帮帮币数量 以及帮帮币的对话比率
     */
    private void getBangbiCountExchange() {

        NetBaseUtils.extractBounsCoinInfo(getmContext(),bean.getCommunityId(),bean.getEmobId(),new NetBaseUtils.NetRespListener<CommonRespBean<BounsCoinInfoBean>>() {
            @Override
            public void successYes(CommonRespBean<BounsCoinInfoBean> commonRespBean, Response response) {
                 bounsCoinInfoBean = commonRespBean.getData();
                if(0 == bounsCoinInfoBean.getEnable()){
                    if(rl_bonus!=null){
                        rl_bonus.setVisibility(View.GONE);
                    }
                    Log.d("getShowBonuscoin ","  getShowBonuscoin no ");
                }
//                        bangbiExchange =(float) bean.getInfo().getExchange()/(float)bean.getInfo().getBonuscoin();
                bangbiExchange = bounsCoinInfoBean.getRatio();
                Log.d("getBangbiCountExchange ", "  bangbiExchange " + bangbiExchange);
                bangbiCount = bounsCoinInfoBean.getCount();

                Log.d("getBangbiCountExchange ","  bangbiCount "+bangbiCount);

                initData();
                isDataLoadComplete = true;
            }

            @Override
            public void successNo(CommonRespBean<BounsCoinInfoBean> commonRespBean, Response response) {
                showToast("获取帮帮币数失败："+commonRespBean.getMessage());
                isDataLoadComplete = true;
            }

            @Override
            public void failure(RetrofitError error) {
                showNetErrorToast();
                isDataLoadComplete = false;
            }
        });
    }

    /// 帮帮币支付
    private void mspBangBangBiPay(MspBangbiReqPayInfo request) {
        mLdDialog.show();
        GetOrderInfoService service = RetrofitFactory.getInstance().create(getmContext(),request,GetOrderInfoService.class);
        Callback<CommonRespBean<MspBangbiRespPayBean>> callback = new Callback<CommonRespBean<MspBangbiRespPayBean>>() {
            @Override
            public void success(CommonRespBean<MspBangbiRespPayBean> bean, Response response) {
                if (bean != null && TextUtils.equals(bean.getStatus(), "yes")) {
                    /// 帮帮币支付成功
                    showMiddleToast("支付成功");
                    Intent intent = new Intent();
                    intent.setClass(getmContext(), ActivityMShipCardPaySuccess.class);
                    intent.putExtra("MSPCardBeanShopInfo", mspcardBean);
                    intent.putExtra("paytime",System.currentTimeMillis());
                    intent.putExtra("payedprice",""+Arith.round(realpay,2));
                    intent.putExtra("serial",bean.getData().getOrderNo());
                    startActivityForResult(intent, PayCodeOnline);

                } else if (bean != null && TextUtils.equals(bean.getStatus(), "no")) {
                    String msg = bean.getMessage();
                    if(TextUtils.isEmpty(msg)||TextUtils.equals(msg,"null")){
                        showWelfareToast("支付超时");
                    }else{
                        showWelfareToast(msg);
                    }
                }else{
                    showNetErrorToast();
                }
                mLdDialog.dismiss();

            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                showWelfareToast(""+error.getMessage());
            }
        };

        //{"subject":"商家重置","price":"0.01","beanId":"crazySalesProcessor"}
        // {"subject":"","body":"","price":"","orderId":0,"userBonusId":0}

        ////   支付信息确认
        service.mspBangBangbiPay(request, callback);
    }

    private void showWelfareToast(String showT){

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.common_welfare_toast_lay,null);
        TextView title = (TextView) layout.findViewById(R.id.toast_title_tv);
        title.setText(showT);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0 , 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }



    @Override
    public void onClick(View v) {

    }

    private void weixinpay(){
        req = new PayReq();
        sb=new StringBuffer();
        msgApi.registerApp(Config.APP_ID);
        if (!msgApi.isWXAppInstalled()) {
            showToast("您还没有安装微信哦!");
            return;
        }
        if(!msgApi.isWXAppSupportAPI()){
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

        this.sb.append("sign str\n"+sb.toString()+"\n\n");

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

        sb.append("sign\n"+req.sign+"\n\n");

//        showToast(sb.toString());

        Log.e("orion", signParams.toString());

    }

    private void sendPayReq() {

        msgApi.registerApp(Config.APP_ID);
        msgApi.sendReq(req);

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 微信支付EventBus回调 勿删
     * @param event
     */
    public void onEventMainThread(WXPayRequestEvent event) {
        if (!event.isSuccess()) {
            showToast("微信支付失败");
            Intent intent = new Intent();

            intent.setClass(getmContext(), ActivityMShipCardPay.class);

            setResult(RESULT_CANCELED, intent);
            finish();

        }else {
            showToast("微信支付成功");
            Intent intent = new Intent();
            intent.setClass(getmContext(), ActivityMShipCardPaySuccess.class);
            intent.putExtra("MSPCardBeanShopInfo", mspcardBean);
            intent.putExtra("paytime",System.currentTimeMillis());
            intent.putExtra("payedprice",""+Arith.round(realpay, 2));
            intent.putExtra("serial",serial);
            startActivityForResult(intent,PayCodeOnline);

        }
    }


    public void  onEventMainThread(NetworkStateChangeEvent networkStateChangeEvent){

        if(networkStateChangeEvent!=null&& networkStateChangeEvent.isConnected()){

            Log.d("onEventNetworkStatusChange ",""+ networkStateChangeEvent.isConnected());

            if(!isDataLoadComplete){
                Log.d("isDataLoadComplete ",""+ isDataLoadComplete + "network is ok , reconnect bangbi info ");
                getBangbiCountExchange();
            }
        }
    }

    /**
     * 获取微信支付预支付订单
     */
    interface WXOrderService {

//        @POST("/api/v1/wxpay/wxpayShopVipcardProcessor")
//        void postInfo(@Header("signature")String signature,@Body MspWXPostOrderInfoBean bean, Callback<MspWXPostOrderInfoRespBean> cb);

//        @POST("/api/v1/wxpay/wxpayShopVipcardProcessor")
        @POST("/api/v3/wxpay")
        void postInfo(@Body MspWXPostOrderInfoBean bean, Callback<CommonRespBean<MspWXPostOrderInfoRespBean>> cb);
    }

    private void getWXOrder() {
        mLdDialog.show();

        MspWXPostOrderInfoBean request = new MspWXPostOrderInfoBean();

        /**
         *
         * {
         "beanId": "wxpayShopVipcard",
         "subject": "{商品名称}",
         "cityId": {城市ID},
         "communityId": {小区ID},
         "emobId": "{用户环信ID}",
         "shopEmobId": {周边店家环信ID},
         "totalFee": "{通过会员卡折扣之后的金额，单位分}",
         "orderPrice": {订单总价},
         "discount": {订单折扣},
         "bonuscoinCount": {帮帮币数量}
         }
         *
         */

        request.setSubject(mspcardBean.getShopName());
        request.setCommunityId(bean.getCommunityId());
        request.setEmobId(bean.getEmobId()); /// 用户emobid
        request.setCityId(bean.getCityId());
        request.setShopEmobId(mspcardBean.getEmobId()); /// 店铺emobid

        realpay = Arith.round(Arith.sub(disedprice, bangbiedsubPrice),2);

//
//        request.setTotalFee("" +  (int)(realpay* 100)); /// 单位分

        double orderprice = Arith.round(Double.valueOf(inputRMBStr),2);

        request.setOrderPrice(""+orderprice); /// 原价格 单位元

        request.setDiscount(""+discount); // 折扣


//        request.setTotalFee(String.valueOf(Arith.round(disedprice*100, 0))); //通过会员卡折扣之后的金额，单位分
        request.setTotalFee(String.valueOf((int)(disedprice*100))); //通过会员卡折扣之后的金额，单位分

        if(rb_cashbybangbang.isChecked()){
            request.setBonuscoinCount(getUsedBangBangBiCount());
        }else {
            request.setBonuscoinCount(0);
        }

        WXOrderService service = RetrofitFactory.getInstance().create(getmContext(),request,WXOrderService.class);
        Callback<CommonRespBean<MspWXPostOrderInfoRespBean>> callback = new Callback<CommonRespBean<MspWXPostOrderInfoRespBean>>() {
            @Override
            public void success(CommonRespBean<MspWXPostOrderInfoRespBean> bean, Response response) {
                mLdDialog.dismiss();

                if("yes".equals(bean.getStatus())){

                    List<NameValuePair> signParams = new LinkedList<NameValuePair>();
//                    if(bean.getData().getCode_url() != null){
//                        signParams.add(new BasicNameValuePair("code_url", bean.getData().getCode_url()));
//                    }
                    signParams.add(new BasicNameValuePair("orderNo", bean.getData().getOrderNo()));
                    signParams.add(new BasicNameValuePair("nonce_str", bean.getData().getNonce_str()));
                    signParams.add(new BasicNameValuePair("prepay_id", bean.getData().getPrepay_id()));
//                    signParams.add(new BasicNameValuePair("trade_type", "APP"));
                    String sign = genAppSign(signParams);

//                    if(sign.equals(bean.getData().getSign())){

                        serial = bean.getData().getOrderNo();

                        genPayReq(bean.getData().getPrepay_id());
                        sendPayReq();

//                    }else {
//                        showToast("签名异常");
//                    }

                }else {
                    showDataErrorToast(bean.getMessage());
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
