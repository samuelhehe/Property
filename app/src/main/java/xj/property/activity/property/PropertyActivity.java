package xj.property.activity.property;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.PropertyBBbiRequestV3;
import xj.property.beans.PropertyPayHistoryV3Bean;
import xj.property.beans.PropertyPayInfoBean;
import xj.property.beans.PropertyPaySubmitInfoV3Bean;
import xj.property.beans.PropertyPayWeiXinV3Bean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.beans.WXOrderRequestBean;
import xj.property.event.ApilyEvent;
import xj.property.event.WXPayRequestEvent;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseUtils;
import xj.property.netbase.RetrofitFactory;
import xj.property.netbasebean.BounsCoinInfoBean;
import xj.property.utils.ToastUtils;
import xj.property.utils.other.Arith;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.weixinpay.MD5;

/**
 * aurth:asia
 * 缴费页面
 */
public class PropertyActivity extends HXBaseActivity implements IWXAPIEventHandler {

    private String mTag = "PropertyActivity";

    private final int PROPERTYPAY = 88;
    private final int CLOSEPROPERTY = 66;
    private final int BANGBANGJUAN = 200;
    private final int APILYPAY = 68;
    private final int WEIXINPAY = 200;
    private final int CHANGEUIBANGBANGBI = 69;

    private final int APILY = 1;
    private final int WEIXIN = 2;
    private final int BANGBANGBI = 3;
    private int payType = APILY;

    private boolean hasBangBangBi = false;
    private boolean hasBangBangJuan = false;
    private String TAG = "PropertyActivity";

    //微信支付
    PayReq req;
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    StringBuffer sb;
    private float bangbiExchange = 0.01f;/// 帮帮币的置换率
    private int bangbiCount;/// 帮帮币的数量
    private boolean isDataLoadComplete = false;

    //titile
    private RelativeLayout mHeaptop;
    private ImageView mIv_back;
    private TextView mTv_left_text;
    private TextView mTv_title;
    private TextView mTv_right_text;
    private ImageView mIv_right;

    //ui
    private TextView mTv_property_name;
    private TextView mTv_property_message;
    private TextView mTv_people_name;
    private TextView mTv_address;
    private TextView mTv_edit;
    private TextView mTv_house_area;
    private TextView mTv_house_unit_price;
    private TextView mTv_property_pay_time_long;
    private TextView mTv_property_pay_umit_price;
    private TextView mTv_property_all_money;
    private TextView mTv_rubbish_pay_time_long;
    private TextView mTv_rubbish_pay_umit_price;
    private TextView mTv_rubbish_all_money;
    private TextView mTv_bangbangline;
    private TextView mTv_all_money;
    private TextView mTv_all_price;
    private TextView mTv_bangbang_price;
    private TextView mTv_money;
    private LinearLayout mLl_zhifubao;
    private ImageView mIv_zhifubao;
    private LinearLayout mLl_weixin;
    private ImageView mIv_weixin;
    private LinearLayout mLl_bangbang;
    private ImageView mIv_bangbang;
    private TextView mTv_juan;
    private TextView mTv_juan_message;
    private TextView mIv_bangbang_use;
    private TextView mTv_bangbang_bi;
    private LinearLayout mLl_bangbangjuan;
    private LinearLayout mLl_pay;
    private TextView mTv_pay_num;
    private TextView mTv_submit;

    private UserInfoDetailBean userbean;
    private PropertyPaySubmitInfoV3Bean mPropertyPaySubmitAllBean;

    private String emobId;
    private String beanId = "wxpayPayment";
    private int communityId = 1;
    private double bonusPrice;
    private String bonusName;
    private int userBonusId;

    private String orderNo;

    private double allMoney;
    private double propertyPrice;
    private double calculateMoney;//实际价格
    private double favorableMoney;//优惠价格
    private double subPrice;
    private float mHouseBig;//房屋大小

    private String others;

    private int paymentId =0;
    private Handler mHandler;
    private BounsCoinInfoBean bounsCoinInfoBean;
    private TextView tv_community_property_phone_pay;
    private TextView tv_bangbangtype_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_pay_cost);
        UserInfoDetailBean bean = PreferencesUtil.getLoginInfo(PropertyActivity.this);
        emobId = bean.getEmobId();
        communityId = PreferencesUtil.getCommityId(this);
        EventBus.getDefault().register(this);
        initView();
        initDate();
        initListenner();
        initHandler();
    }

    private void initHandler() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case CHANGEUIBANGBANGBI:
                        changeUi(0.0, "");
                        break;
                }
                return false;
            }
        });
    }

    private void initDate() {
        mTv_title.setText("物业缴费");
        mTv_right_text.setVisibility(View.VISIBLE);
        mTv_right_text.setText("历史账单");
        userbean = PreferencesUtil.getLoginInfo(getApplicationContext());
        getIsPayed();
    }

    private void initListenner() {
        mIv_back.setOnClickListener(this);
        mTv_right_text.setOnClickListener(this);
        mTv_submit.setOnClickListener(this);
        mTv_edit.setOnClickListener(this);
        mLl_zhifubao.setOnClickListener(this);
        mLl_weixin.setOnClickListener(this);
        mLl_bangbang.setOnClickListener(this);
        mLl_bangbangjuan.setOnClickListener(this);
    }

    public void initView() {
        mHeaptop = (RelativeLayout) findViewById(R.id.heaptop);
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mTv_left_text = (TextView) findViewById(R.id.tv_left_text);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mTv_right_text = (TextView) findViewById(R.id.tv_right_text);
        mIv_right = (ImageView) findViewById(R.id.iv_right);
        mTv_property_name = (TextView) findViewById(R.id.tv_property_name);//PropertyPayAlipayBean
        mTv_property_message = (TextView) findViewById(R.id.tv_property_message);
        mTv_people_name = (TextView) findViewById(R.id.tv_people_name);
        mTv_address = (TextView) findViewById(R.id.tv_address);
        mTv_edit = (TextView) findViewById(R.id.tv_edit);
        mTv_house_area = (TextView) findViewById(R.id.tv_house_area);
        mTv_house_unit_price = (TextView) findViewById(R.id.tv_house_unit_price);
        mTv_property_pay_time_long = (TextView) findViewById(R.id.tv_property_pay_time_long);
        mTv_property_pay_umit_price = (TextView) findViewById(R.id.tv_property_pay_umit_price);
        mTv_property_all_money = (TextView) findViewById(R.id.tv_property_all_money);
        mTv_rubbish_pay_time_long = (TextView) findViewById(R.id.tv_rubbish_pay_time_long);
        mTv_rubbish_pay_umit_price = (TextView) findViewById(R.id.tv_rubbish_pay_umit_price);
        mTv_rubbish_all_money = (TextView) findViewById(R.id.tv_rubbish_all_money);
        mTv_all_money = (TextView) findViewById(R.id.tv_all_money);
        mTv_all_price = (TextView) findViewById(R.id.tv_all_price);
        mTv_bangbang_price = (TextView) findViewById(R.id.tv_bangbang_price);

        tv_bangbangtype_name = (TextView) findViewById(R.id.tv_bangbangtype_name);
        tv_community_property_phone_pay = (TextView) findViewById(R.id.tv_community_property_phone_pay);
        mTv_money = (TextView) findViewById(R.id.tv_money);
        mLl_zhifubao = (LinearLayout) findViewById(R.id.ll_zhifubao);
        mIv_zhifubao = (ImageView) findViewById(R.id.iv_zhifubao);
        mLl_weixin = (LinearLayout) findViewById(R.id.ll_weixin);
        mIv_weixin = (ImageView) findViewById(R.id.iv_weixin);
        mLl_bangbang = (LinearLayout) findViewById(R.id.ll_bangbang);
        mTv_bangbangline = (TextView) findViewById(R.id.tv_bangbangline);
        mIv_bangbang = (ImageView) findViewById(R.id.iv_bangbang);
        mTv_bangbang_bi = (TextView) findViewById(R.id.tv_bangbang_bi);
        mTv_juan = (TextView) findViewById(R.id.tv_juan);
        mTv_juan_message = (TextView) findViewById(R.id.tv_juan_message);
        mIv_bangbang_use = (TextView) findViewById(R.id.iv_bangbang_use);
        mLl_pay = (LinearLayout) findViewById(R.id.ll_pay);
        mLl_bangbangjuan = (LinearLayout) findViewById(R.id.ll_bangbangjuan);
        mTv_pay_num = (TextView) findViewById(R.id.tv_pay_num);
        mTv_submit = (TextView) findViewById(R.id.tv_submit);

        tv_community_property_phone_pay = (TextView) findViewById(R.id.tv_community_property_phone_pay);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right_text:
                Intent intentHistory = new Intent(PropertyActivity.this, PropertyHistoryActivity.class);
                startActivity(intentHistory);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_edit:
                userbean = new UserInfoDetailBean();
                Intent intent = new Intent(PropertyActivity.this, PropertyDialogActivity.class);
                startActivityForResult(intent, 88);
                break;
            case R.id.ll_zhifubao:
                if (payType != BANGBANGBI) {
                    payType = APILY;
                    mIv_zhifubao.setBackgroundDrawable(getResources().getDrawable(R.drawable.ellipse_1));
                    mIv_weixin.setBackgroundDrawable(getResources().getDrawable(R.drawable.ellipse_2));
                } else {
                    Toast.makeText(getApplicationContext(), "亲，可以用帮帮币直接支付！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_weixin:
                if (payType != BANGBANGBI) {
                    payType = WEIXIN;
                    mIv_zhifubao.setBackgroundDrawable(getResources().getDrawable(R.drawable.ellipse_2));
                    mIv_weixin.setBackgroundDrawable(getResources().getDrawable(R.drawable.ellipse_1));
                } else {
                    Toast.makeText(getApplicationContext(), "亲，可以用帮帮币直接支付！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_bangbang:
                if (bangbiCount > 0) {
                    if (hasBangBangBi) {
                        hasBangBangBi = false;
                        Message meg = mHandler.obtainMessage();
                        meg.what = CHANGEUIBANGBANGBI;
                        mHandler.sendMessage(meg);
                    } else {
                        hasBangBangBi = true;
                        hasBangBangJuan = false;
                        Message meg = mHandler.obtainMessage();
                        meg.what = CHANGEUIBANGBANGBI;
                        mHandler.sendMessage(meg);
                    }
                } else {
                    ToastUtils.showToast(getmContext(),"没有可用的帮帮币");
                }
                break;
            case R.id.ll_bangbangjuan:
                //跳转的PayPreActivity  跳转到 UserBonusActivity
                Intent intentBonusToPay = new Intent(PropertyActivity.this, PropertyToPayActivity.class);
                intentBonusToPay.putExtra("emobIdShop", 7 + "");
                intentBonusToPay.putExtra("totalprice",allMoney);
                startActivityForResult(intentBonusToPay, BANGBANGJUAN);
                break;
            case R.id.tv_submit:
                submitPayMoney();
                break;
        }
    }



    /**
     * 支付宝支付结束    PropertyPayDetailsActivity
     *
     * @param event
     */
    public void onEventMainThread(ApilyEvent event) {
        orderNo = event.getOrderNo();
        payForSuccess();
    }

    /**
     * 缴费成功跳转
     */
    private void payForSuccess() {
//        PropertyPayHistoryBean propertyPayHistoryBean = new PropertyPayHistoryBean();
//
//        PropertyPaySubmitPayInfoBean propertyPaySubmitPayInfoBean = new PropertyPaySubmitPayInfoBean();
//        propertyPaySubmitPayInfoBean.setPaymentOptions(mPropertyPaySubmitAllBean.getCommunityPayment().getPaymentOptions());
//        propertyPaySubmitPayInfoBean.setUnitCount(mPropertyPaySubmitAllBean.getCommunityPayment().getUnitCount());
//
//        PropertyPaySubmitInfoV3Bean.PropertyPaySubmitOwnerV3Bean owner = mPropertyPaySubmitAllBean.getOwner();
//        PropertyPaySubmitInfoV3Bean.PropertyPaySubmitPaymentV3Bean payment = mPropertyPaySubmitAllBean.getPayment();
//
//        propertyPayHistoryBean.setCommunityPayment(propertyPaySubmitPayInfoBean);
//        PropertyPaySubmitInfoBean info = mPropertyPaySubmitAllBean;
//        propertyPayHistoryBean.setAdress(info.getFloor() + "-" + info.getUnit() + "-" + info.getRoom() + "");
//        propertyPayHistoryBean.setCreateTime(new Date().getTime());

        PropertyPayHistoryV3Bean propertyPayHistoryV3Bean = new PropertyPayHistoryV3Bean();
        propertyPayHistoryV3Bean.setCommunityPaymentId(mPropertyPaySubmitAllBean.getPayment().getCommunityPaymentId());
        propertyPayHistoryV3Bean.setPaymentId(0);
        propertyPayHistoryV3Bean.setCommunityId(communityId);
        propertyPayHistoryV3Bean.setEmobId("");
        propertyPayHistoryV3Bean.setAddress(mPropertyPaySubmitAllBean.getOwner().getFloor() + "-" + mPropertyPaySubmitAllBean.getOwner().getUnit() + "-" + mPropertyPaySubmitAllBean.getOwner().getRoom());
        propertyPayHistoryV3Bean.setArea(mPropertyPaySubmitAllBean.getOwner().getArea());
        propertyPayHistoryV3Bean.setArrearageCount(mPropertyPaySubmitAllBean.getOwner().getArrearageCount());
        propertyPayHistoryV3Bean.setArrearage(mPropertyPaySubmitAllBean.getOwner().getArrearage());
        propertyPayHistoryV3Bean.setCreateTime(new Date().getTime() / 1000);
        propertyPayHistoryV3Bean.setName(mPropertyPaySubmitAllBean.getOwner().getName());
        propertyPayHistoryV3Bean.setMoney(allMoney);
        propertyPayHistoryV3Bean.setUnitPrice(mPropertyPaySubmitAllBean.getOwner().getUnitPrice());
        propertyPayHistoryV3Bean.setPaymentPrice(allMoney);
        propertyPayHistoryV3Bean.setUnitCount(mPropertyPaySubmitAllBean.getPayment().getUnitCount());
        Intent intent = new Intent(PropertyActivity.this, PropertyPayDetailsActivity.class);
        intent.putExtra("PropertyPayHistoryBean", propertyPayHistoryV3Bean);
        intent.putExtra("orderNo",orderNo);
        startActivity(intent);
        finish();
    }

    /**
     * 微信支付结果
     *
     * @param event
     */
    public void onEventMainThread(WXPayRequestEvent event) {
        if (!event.isSuccess()) {
            Toast.makeText(getApplicationContext(), "微信支付失败error", Toast.LENGTH_SHORT);
        } else {
            payForSuccess();
            Toast.makeText(getApplicationContext(), "微信支付成功success", Toast.LENGTH_SHORT);
        }
    }

    /**
     * 　支付模块
     */
    private void submitPayMoney() {
        if (payType == APILY) {
            apilySubmit();
        } else if (payType == WEIXIN) {
            weixinSubmit();
        } else if (payType == BANGBANGBI) {
            getBBbi();
        }
    }

    /**
     * 微信支付提交
     */
    private void weixinSubmit() {
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
        Log.i("debbug", "点击了微信支付");
        subPrice = getSubPriceUseBangbi();
        //// 如果帮帮币支付选中
        if(hasBangBangBi){
            if(isNeedUseAliPay()){
                getWeiXinPayMessage("" + bangbiCount, subPrice);
            }else{
                getBBbi();
            }
        }else{
            getWeiXinPayMessage("" + bangbiCount, subPrice);
        }

    }

    /**
     * 支付宝支付提交
     */
    private void apilySubmit() {

        //// 如果帮帮币支付选中
        if(hasBangBangBi){
            if(!isNeedUseAliPay()){ //// 如果不需要支付工具支付
                getBBbi();
                return ;
            }
        }
        Intent intent = getIntent();
        intent.setClass(PropertyActivity.this, PropertyPayActivity.class);

        PropertyPaySubmitInfoV3Bean.PropertyPaySubmitOwnerV3Bean owner = mPropertyPaySubmitAllBean.getOwner();
        PropertyPaySubmitInfoV3Bean.PropertyPaySubmitPaymentV3Bean payment = mPropertyPaySubmitAllBean.getPayment();

        intent.putExtra("adress", owner.getFloor() + "-" + owner.getUnit() + "-" + owner.getRoom() + "");//地址
        intent.putExtra("BonusCoinCount", bangbiCount);//帮帮币数量
        intent.putExtra("CommunityOwnerId", owner.getCommunityOwnerId());
        intent.putExtra("name", owner.getName());
        intent.putExtra("Subject", "物业缴费");
        intent.putExtra("UnitCount", payment.getUnitCount());
        intent.putExtra("communityPaymentId", payment.getCommunityPaymentId());
        if (hasBangBangJuan) {
            intent.putExtra("BonusPrice", favorableMoney + "");
            intent.putExtra("UserBonusId", userBonusId);
            intent.putExtra("BonusCoinCount", 0);
            intent.putExtra("Price", calculateMoney + "");
            intent.putExtra("PaymentPrice", Arith.round(allMoney, 2) + "");
        } else if (hasBangBangBi) {
            intent.putExtra("BonusPrice", 0 + "");
            intent.putExtra("UserBonusId", 0);
            intent.putExtra("BonusCoinCount", bangbiCount);//帮帮币数量
            intent.putExtra("Price", calculateMoney + "");
            intent.putExtra("PaymentPrice", Arith.round(allMoney, 2) + "");
        } else {
            intent.putExtra("BonusPrice", 0 + "");
            intent.putExtra("UserBonusId", 0);
            intent.putExtra("BonusCoinCount", 0);//帮帮币数量
            intent.putExtra("Price", allMoney + "");
            intent.putExtra("PaymentPrice", Arith.round(allMoney, 2) + "");
        }
        startActivity(intent);
    }

    /**
     * 获取使用帮币可以省去的差价.
     *
     * @return
     */
    private double getSubPriceUseBangbi() {
        return bangbiCount * bangbiExchange;
    }

    /**
     * 如果两者微信帮帮币或者支付宝帮帮币同时选中看是否需要 微信、支付宝支付
     * @return
     */
    private boolean isNeedUseAliPay(){
        if(getSubPriceUseBangbi()>=allMoney){
            return false;
        }else{
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (PROPERTYPAY == resultCode) {
            String floor = data.getStringExtra("floor");
            String unit = data.getStringExtra("unit");
            String room = data.getStringExtra("room");
            getPayMessage(floor, unit, room);
        } else if (CLOSEPROPERTY == resultCode) {
            finish();
        } else if (BANGBANGJUAN == resultCode) {
            //1.获得参数，
            String Position = data.getStringExtra("selectedPosition");
            if (!"-1".equals(Position)) {
                bonusPrice = Arith.round(Double.parseDouble(data.getStringExtra("bonusPrice")), 2);
                bonusName = data.getStringExtra("bonusName");
                userBonusId = data.getIntExtra("UserBonusId", 0);
                hasBangBangJuan = true;
                hasBangBangBi = false;
                mIv_bangbang.setBackgroundDrawable(getResources().getDrawable(R.drawable.ellipse_2));

                payType = APILY;
                mIv_zhifubao.setBackgroundDrawable(getResources().getDrawable(R.drawable.ellipse_1));
                mIv_weixin.setBackgroundDrawable(getResources().getDrawable(R.drawable.ellipse_2));

                //2.修改ui
                changeUi(bonusPrice, bonusName);
            } else {
                Toast.makeText(getApplicationContext(), "亲，未选择帮帮卷", Toast.LENGTH_SHORT).show();
            }
        } else if (APILYPAY == resultCode) {
            if (resultCode == 103) {
                Intent intent = new Intent(PropertyActivity.this, PropertyPayDetailsActivity.class);
                if (data.getIntExtra("useBonusId", 0) != 0) {
                    data.putExtra(Config.EXPKey_BONUS, 1);
                }
                String address = data.getStringExtra("address");
                intent.putExtra("address", address);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    /**
     * 帮帮卷修改ui
     *
     * @param bonusPrice 抵扣多少钱
     * @param bonusName  帮帮卷名字
     */
    private void changeUi(Double bonusPrice, String bonusName) {
        mIv_bangbang.setBackgroundDrawable(getResources().getDrawable(R.drawable.ellipse_2));
        if (bonusName.contains("通用券")) {
            //1.修改帮帮币
            favorableMoney = bonusPrice;
            mTv_bangbang_price.setText("-￥" + bonusPrice + "元");
            tv_bangbangtype_name.setText("通用券");
            //2.帮帮券里边加上卷名和使用时间
            mTv_juan.setText(bonusName);
            mTv_juan_message.setText("-￥" + bonusPrice + "元");
            //3.修改两个实收金额
            calculateMoney = Arith.round((allMoney - bonusPrice), 2);
            mTv_pay_num.setText("￥" + calculateMoney);
            mTv_money.setText(calculateMoney + "");
            mIv_bangbang_use.setText("已使用");
        } else if (bonusName.contains("物业券")) {
            //1.修改帮帮币
            favorableMoney = Arith.round((propertyPrice ) * bonusPrice, 2);
            calculateMoney = Arith.round(allMoney - (propertyPrice ) * bonusPrice, 2);
            mTv_bangbang_price.setText("-￥" + Arith.round((propertyPrice) * bonusPrice, 2) + "元");
            tv_bangbangtype_name.setText("物业券");
            //2.帮帮卷里边加上卷名和使用时间
            mTv_juan.setText(bonusName);
            mTv_juan_message.setText("免费" + bonusPrice + "个月");
            //3.修改两个实收金额
            mTv_pay_num.setText("￥" + calculateMoney);
            mTv_money.setText(calculateMoney + "");
            mIv_bangbang_use.setText("已使用");
        } else {
        //使用帮帮币的时候
            //1.修改帮帮币
            //TODO 帮帮币换算
            if (hasBangBangBi) {
                mIv_bangbang.setBackgroundDrawable(getResources().getDrawable(R.drawable.ellipse_1));
                favorableMoney = Double.valueOf(bangbiCount * bangbiExchange);
                if (favorableMoney > allMoney) {
                    payType = BANGBANGBI;
                    mIv_zhifubao.setBackgroundDrawable(getResources().getDrawable(R.drawable.ellipse_2));
                    mIv_weixin.setBackgroundDrawable(getResources().getDrawable(R.drawable.ellipse_2));
                    tv_bangbangtype_name.setText("帮帮币");
                    mTv_bangbang_price.setText("-￥" + Arith.round(allMoney, 2) + "元");
                    //2.帮帮卷里边加上卷名和使用时间
                    mTv_juan.setText("");
                    mTv_juan_message.setText("");
                    //3.修改两个实收金额
                    mTv_pay_num.setText("￥" + 0);
                    mTv_money.setText(0 + "");
                    mIv_bangbang_use.setText("未使用 >");
                } else {
                    calculateMoney = Arith.round(allMoney - Double.valueOf(bangbiCount * bangbiExchange), 2);
                    tv_bangbangtype_name.setText("帮帮币");
                    mTv_bangbang_price.setText("-￥" + Arith.round(Double.valueOf(bangbiCount * bangbiExchange), 2) + "元");
                    //2.帮帮卷里边加上卷名和使用时间
                    mTv_juan.setText("");
                    mTv_juan_message.setText("");
                    //3.修改两个实收金额
                    mTv_pay_num.setText("￥" + calculateMoney);
                    mTv_money.setText(calculateMoney + "");
                    mIv_bangbang_use.setText("未使用 >");
                }
            } else {
                if (payType == BANGBANGBI) {
                    payType = APILY;
                    mIv_zhifubao.setBackgroundDrawable(getResources().getDrawable(R.drawable.ellipse_1));
                }
                mIv_bangbang_use.setText("未使用 >");
                mIv_bangbang.setBackgroundDrawable(getResources().getDrawable(R.drawable.ellipse_2));
                tv_bangbangtype_name.setText("帮帮币");
                mTv_bangbang_price.setText("");
                //2.帮帮券里边加上卷名和使用时间
                mTv_juan.setText("");
                mTv_juan_message.setText("");
                //3.修改两个实收金额
                mTv_pay_num.setText("￥" + Arith.round(allMoney, 2));
                mTv_money.setText(Arith.round(allMoney, 2) + "");
                mIv_bangbang_use.setText("未使用 >");
            }
        }
    }

    /**
     * 初始化页面信息
     *
     * @param propertyPaySubmitInfoV3Bean 页面信息类
     */
    public void setActivityView(PropertyPaySubmitInfoV3Bean propertyPaySubmitInfoV3Bean) {

        PropertyPaySubmitInfoV3Bean.PropertyPaySubmitOwnerV3Bean owner = propertyPaySubmitInfoV3Bean.getOwner();
        PropertyPaySubmitInfoV3Bean.PropertyPaySubmitPaymentV3Bean payment = propertyPaySubmitInfoV3Bean.getPayment();
//        PropertyPaySubmitInfoBean info = propertyPaySubmitAllBean.getInfo();
        //物业名称
        String cname = PreferencesUtil.getCommityName(getApplicationContext());
        mTv_property_name.setText(cname + "物业");
        //1缴费信息
        mTv_property_message.setText(payment.getPaymentExplain() + "");
        //2.人名
        mTv_people_name.setText("户主名称：" + getDisplayStr(owner.getName()) + "");
        //3.单元号
        mTv_address.setText(owner.getFloor() + "-" + owner.getUnit() + "-" + owner.getRoom());
        //4.房屋面积
        mTv_house_area.setText("您的房屋面积：" + owner.getArea() + "平方米");
        mTv_house_unit_price = (TextView) findViewById(R.id.tv_house_unit_price);
        //5.物业单价
//        List<PropertyPayMessageBean> list = info.getCommunityPayment().getPaymentOptions();
//        PropertyPayMessageBean bean = null;
//        PropertyPayMessageBean bean1 = null;
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).getOptionName().equals("物业费")) {
//                bean = list.get(i);
//            } else {
//                bean1 = list.get(i);
//            }
//        }
        propertyPrice = Float.parseFloat(owner.getUnitPrice())*mHouseBig;
        mTv_house_unit_price.setText("物业费单价(每月)：" + String.valueOf(owner.getUnitPrice()) + "/平方米");
        //6.物业费表格   支付
        mTv_property_pay_time_long.setText(payment.getUnitCount() + "个月");
        mTv_property_pay_umit_price.setText(Arith.round(Float.parseFloat(owner.getUnitPrice()) * mHouseBig, 2) + "");
        mTv_property_all_money.setText(Arith.round((Float.parseFloat(owner.getUnitPrice()) * mHouseBig * payment.getUnitCount()), 2) + "");
        //7.欠费表格
        mTv_rubbish_pay_time_long.setText(owner.getArrearageCount() + "个月");
        mTv_rubbish_pay_umit_price.setText(Arith.round(Float.parseFloat(owner.getUnitPrice())*mHouseBig,2) + "");
        mTv_rubbish_all_money.setText(Arith.round((Float.parseFloat(owner.getUnitPrice())*mHouseBig * owner.getArrearageCount()),2) + "");
        //8.计算总共金额
        allMoney = Arith.round(Float.parseFloat(owner.getUnitPrice()) * mHouseBig * payment.getUnitCount() + Float.parseFloat(owner.getUnitPrice()) * mHouseBig * owner.getArrearageCount(), 2);
        mTv_all_money.setText(allMoney + "");
        mTv_all_price.setText("￥" + allMoney + "元");
        mTv_bangbang_price.setText("");//TODO 帮帮币默认没有值
        //9.默认的实付金额
        mTv_money.setText("￥" + allMoney + "元");//TODO 实际付款金额
        mTv_pay_num.setText("￥" + allMoney + "元");
        //其它
        mIv_bangbang_use.setText("未使用 >");
        mTv_juan.setText("");
        mTv_juan_message.setText("");

        final String phone = payment.getPhone();
        if (!TextUtils.isEmpty(phone)) {
            tv_community_property_phone_pay.setText(cname + "物业客服电话 " + phone);
            tv_community_property_phone_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phone));
                    startActivity(phoneIntent);
                }
            });
        }else{
            tv_community_property_phone_pay.setVisibility(View.INVISIBLE);
        }
        getBangbiCountExchange();
    }

    /**
     * 隐藏部分字符
     *
     * @param realStr
     * @return
     */
    private String getDisplayStr(String realStr) {
        String result = new String(realStr);
        char[] cs = result.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            if (i >= 1 && i <= cs.length - 1) {//把3和10区间的字符隐藏
                cs[i] = '*';
            }
        }
        return new String(cs);
    }

    /**
     * 微信支付回调
     *
     * @param baseReq
     */
    @Override
    public void onReq(BaseReq baseReq) {
        Log.d(TAG, "basereq_req");
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
            payForSuccess();
        } else {
            //微信支付失败
        }
    }


    /**
     * 获取用户是否缴过费
     */
    interface ActIsPayed {

        @GET("/api/v3/payments/paid")
        void getIsPayed(@QueryMap Map<String, String> map, Callback<CommonRespBean<PropertyPayInfoBean>> cb);
    }

    interface ActPayMessage {
        /**
         * 验证业主地址信息
         * 03/17
         */
        @GET("/api/v3/payments/owner")
        void getPayMessage(@QueryMap Map<String, String> map, Callback<CommonRespBean<PropertyPaySubmitInfoV3Bean>> cb);
    }

    /**
     * 获取微信支付预支付订单
     */
    interface WXOrderService {

        @POST("/api/v3/wxpay")
        void postInfoV3(@Body PropertyPayWeiXinV3Bean bean, Callback<CommonRespBean<WXOrderRequestBean>> cb);
    }

    /**
     * 获取帮帮币支付信息
     */
    interface BBBiService {
//        @POST("/api/v1/communities/{communityId}/payment/pay")
//        void postBBbi(@Header("signature") String signature, @Body PropertyBBbiRequest bean, @Path("communityId") long communityId, Callback<PropertyBBbiAllResponse> cb);
        @POST("/api/v3/payments/bonuscoinPay")
        void postBBbiV3(@Body PropertyBBbiRequestV3 bean, Callback<CommonRespBean<String>> cb);
    }

    /**
     * 获取帮帮币支付信息
     */
    private void getBBbi() {
        mLdDialog.show();
        PropertyPaySubmitInfoV3Bean.PropertyPaySubmitOwnerV3Bean owner = mPropertyPaySubmitAllBean.getOwner();
        PropertyPaySubmitInfoV3Bean.PropertyPaySubmitPaymentV3Bean payment = mPropertyPaySubmitAllBean.getPayment();
//        PropertyBBbiRequest request1 = new PropertyBBbiRequest();
        PropertyBBbiRequestV3 request = new PropertyBBbiRequestV3();
        request.setEmobId(emobId);
//        request1.setPaymentPrice(Arith.round(allMoney, 2) + "");
//        request.setBonusCoinCount(bonusCoinCount);
//        request.setBonusCoin(bangbiCount);
//        request.setType("物业缴费");
        int num = (int) (allMoney / bangbiExchange);
        request.setUnitCount(payment.getUnitCount());
//        request.setStatus("paid");
        request.setName(owner.getName());
        request.setBonusCoin(num);//bangbiExchange
        request.setAddress(owner.getFloor() + "-" + owner.getUnit() + "-" + owner.getRoom() + "");
        request.setCommunityOwnerId(owner.getCommunityOwnerId());
        request.setCommunityId(communityId);
        request.setCommunityPaymentId(payment.getCommunityPaymentId());

        BBBiService service = RetrofitFactory.getInstance().create(getmContext(),request,BBBiService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, Response response) {
                mLdDialog.dismiss();
                if ("yes".equals(bean.getStatus())) {
                    orderNo = bean.getData();
                    EventBus.getDefault().post(new ApilyEvent(orderNo,"", 1));
                } else if("no".equals(bean.getStatus())){
                    Toast.makeText(getApplicationContext(), bean.getMessage() + "", Toast.LENGTH_SHORT).show();
                }else {
                    EventBus.getDefault().post(new ApilyEvent(orderNo,"", 1));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        service.postBBbiV3(request, callback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mLdDialog!=null){
            mLdDialog.dismiss();
        }
    }

    /**
     * 获取微信支付信息
     */
    private void getWeiXinPayMessage(String bonusCoinCount, double subPrice) {
        PropertyPaySubmitInfoV3Bean.PropertyPaySubmitOwnerV3Bean owner = mPropertyPaySubmitAllBean.getOwner();
        PropertyPaySubmitInfoV3Bean.PropertyPaySubmitPaymentV3Bean payment = mPropertyPaySubmitAllBean.getPayment();
        PropertyPayWeiXinV3Bean request = new PropertyPayWeiXinV3Bean();
//        PropertyPayWeiXinBean request = new PropertyPayWeiXinBean();

        if (hasBangBangJuan) {
            request.setBonusId(userBonusId);
//            request.setPrice(calculateMoney * 100 + "");
//            request.setBonusPrice(favorableMoney + "");
//            request.setPaymentPrice(Arith.round(allMoney, 2) + "");
//            request.setTotalFee((int) (calculateMoney * 100));
        } else if (hasBangBangBi) {
//            request.setPrice(calculateMoney * 100 + "");
//            request.setTotalFee((int) (calculateMoney * 100));
            request.setBonuscoinCount(Integer.parseInt(bonusCoinCount));
//            request.setPaymentPrice(Arith.round(allMoney, 2) + "");
        } else {
//            request.setTotalFee((int) (allMoney * 100));
//            request.setPrice(Arith.round(allMoney * 100, 0) + "");
//            request.setPaymentPrice(Arith.round(allMoney, 2) + "");
        }
        request.setCommunityId(communityId);
        request.setBeanId(beanId);
        request.setSubject("物业费");
//        request.setType("物业缴费");//type 值
        request.setUnitCount(payment.getUnitCount()+owner.getArrearageCount());
        request.setName(owner.getName());
        request.setAddress(owner.getFloor() + "-" + owner.getUnit() + "-" + owner.getRoom() + "");
        request.setEmobId(emobId);
        request.setCommunityOwnerId(owner.getCommunityOwnerId());
        request.setCommunityPaymentId(payment.getCommunityPaymentId());
//        request.setBody("物业缴费");//福利名称
//        request.setDataId();//福利id
//        request.setTradeType("APP");

        WXOrderService service = RetrofitFactory.getInstance().create(getmContext(),request,WXOrderService.class);
        Callback<CommonRespBean<WXOrderRequestBean>> callback = new Callback<CommonRespBean<WXOrderRequestBean>>() {
            @Override
            public void success(CommonRespBean<WXOrderRequestBean> bean, Response response) {
                if (bean!=null&&"yes".equals(bean.getStatus()) && bean.getData() != null) {
                        orderNo=bean.getData().getOrderNo();
                        genPayReq(bean.getData().getPrepay_id());
                        sendPayReq();
                } else if(bean!=null) {
                    showDataErrorToast(bean.getMessage());
                }else{
                    showDataErrorToast();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        service.postInfoV3( request, callback);
    }


    /**
     * 微信支付参数
     *
     * @param prepay_id
     */
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

        com.activeandroid.util.Log.e("orion", signParams.toString());

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
        com.activeandroid.util.Log.e("orion", appSign);
        return appSign;
    }

    private void sendPayReq() {
        msgApi.registerApp(Config.APP_ID);
        msgApi.sendReq(req);
    }

    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取帮帮币数量
     * 02/26
     */
    private void getBangbiCountExchange() {

        NetBaseUtils.extractBounsCoinInfo(getmContext(),communityId, emobId, new NetBaseUtils.NetRespListener<CommonRespBean<BounsCoinInfoBean>>() {
            @Override
            public void successYes(CommonRespBean<BounsCoinInfoBean> commonRespBean, Response response) {

                bounsCoinInfoBean = commonRespBean.getData();

                bangbiExchange = (float) bounsCoinInfoBean.getRatio();

                Log.d("getBangbiCountExchange ", "  bangbiExchange " + bangbiExchange);
                bangbiCount = bounsCoinInfoBean.getCount();
                bangbiExchange = bounsCoinInfoBean.getRatio();
                Log.d("getBangbiCountExchange ", "  bangbiCount " + bangbiCount);


                mTv_bangbang_bi.setText(bangbiCount + "枚帮帮币可用");
                isDataLoadComplete = true;
            }

            @Override
            public void successNo(CommonRespBean<BounsCoinInfoBean> commonRespBean, Response response) {
                showToast(commonRespBean.getMessage());
                isDataLoadComplete = false;
            }

            @Override
            public void failure(RetrofitError error) {
                showNetErrorToast();
                isDataLoadComplete = false;
            }
        });
    }

    private void showBangbangBi(){
        mLl_bangbang.setVisibility(View.VISIBLE);
        mTv_bangbangline.setVisibility(View.VISIBLE);
    }

    private void hideBangbangBi(){
        mLl_bangbang.setVisibility(View.GONE);
        mTv_bangbangline.setVisibility(View.GONE);
    }

    /**
     * 用户缴费单查询
     *02/26
     * @param floor
     * @param unit
     * @param room
     */
    private void getPayMessage(String floor, String unit, String room) {
        mLdDialog.show();

        HashMap<String, String> option = new HashMap<String, String>();
        option.put("floor", floor);
        option.put("unit", unit);
        option.put("room", room);
        option.put("communityId", "" + userbean.getCommunityId());
        option.put("emobId", userbean.getEmobId());
        option.put("room", room);

        ///communityId=2&emobId=d463b16dfc014466a1e441dd685ba505&floor=2&unit=2&room=202
        ActPayMessage service = RetrofitFactory.getInstance().create(getmContext(),option,ActPayMessage.class);
        Callback<CommonRespBean<PropertyPaySubmitInfoV3Bean>> callback = new Callback<CommonRespBean<PropertyPaySubmitInfoV3Bean>>() {
            @Override
            public void success(CommonRespBean<PropertyPaySubmitInfoV3Bean> bean, Response response) {
                mLdDialog.dismiss();
                Log.d("PropertyActivity", response.toString());
                if (bean != null) {
                    if ("yes".equals(bean.getStatus()) && bean.getData() != null) {
                        mPropertyPaySubmitAllBean = bean.getData();
                        mHouseBig = bean.getData().getOwner().getArea();
                        setActivityView(bean.getData());
                    } else if ("no".equals(bean.getStatus())) {
                        Intent intent = new Intent(PropertyActivity.this, PropertyDialogActivity.class);
                        startActivityForResult(intent, 88);
                        Toast.makeText(getApplicationContext(), bean.getMessage() + "", Toast.LENGTH_SHORT).show();
                    } else {

                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("error PropertyActivity", error.toString());
                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        service.getPayMessage(option, callback);
    }


    /**
     * 是否有缴费记录
     * 02/26
     */
    private void getIsPayed() {
        mLdDialog.show();

        HashMap<String, String> option = new HashMap<>();
        option.put("communityId", "" + communityId);
        option.put("emobId", emobId);
//        communityId=2&emobId=d463b16dfc014466a1e441dd685ba505
        ActIsPayed service = RetrofitFactory.getInstance().create(getmContext(),option,ActIsPayed.class);
        Callback<CommonRespBean<PropertyPayInfoBean>> callback = new Callback<CommonRespBean<PropertyPayInfoBean>>() {
            @Override
            public void success(CommonRespBean<PropertyPayInfoBean> bean, Response response) {
                mLdDialog.dismiss();
                Log.d("PropertyActivity", response.toString());
                if (bean != null) {
                    if ("yes".equals(bean.getStatus()) && bean.getData() != null) {
                        Intent intent = new Intent(PropertyActivity.this, PropertyPayedActivity.class);
                        intent.putExtra("PropertyPayInfoBean", bean.getData());
                        startActivity(intent);
                        finish();
                    } else if ("no".equals(bean.getStatus())) {
                        Intent intent = new Intent(PropertyActivity.this, PropertyDialogActivity.class);
                        startActivityForResult(intent, 88);
                    }
                }
            }
            @Override
            public void failure(RetrofitError error) {
                Log.d("error PropertyActivity", error.toString());
                mLdDialog.dismiss();
                showNetErrorToast();
                error.printStackTrace();
            }
        };

        service.getIsPayed(option, callback);
    }

}
