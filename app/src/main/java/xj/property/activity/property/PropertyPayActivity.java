package xj.property.activity.property;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.contactphone.PayPreActivity;
import xj.property.alipay.PayResult;
import xj.property.beans.PropertyPayAlipayV3Bean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.event.ApilyEvent;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
/**
 * aurth:asia
 * 支付宝缴费页面
 */
public class PropertyPayActivity extends HXBaseActivity {

    private final String mTag = "PropertyPayActivity";

    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    private final String PAYSTATE = "7";

    private String orderNo;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(PropertyPayActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                        String address = getIntent().getStringExtra("address");
                        int useBonusId = getIntent().getIntExtra("useBonusId", 0);
                        Intent intent = new Intent();
                        intent.setClass(PropertyPayActivity.this, PayPreActivity.class);
                        intent.putExtra("address", address);
                        intent.putExtra("useBonusId", useBonusId);
                        setResult(103, intent);
                        EventBus.getDefault().post(new ApilyEvent(orderNo,address,useBonusId));
                        finish();

                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PropertyPayActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(PropertyPayActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
//                    Toast.makeText(PayDemoActivity.this, "检查结果为：" + msg.obj,
//                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };
    UserInfoDetailBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_main);
        bean = PreferencesUtil.getLoginInfo(this);
        TextView product_desc=(TextView)findViewById(R.id.product_desc);
        TextView product_price=(TextView)findViewById(R.id.product_price);
        product_desc.setText(getIntent().getStringExtra("goodsname"));
        product_price.setText(getIntent().getStringExtra("Price")+"");
        getOrderInfo();
        check(null);
    }

    String orderInfo;

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(View v) {
        // 订单
        if (orderInfo == null) return;
        Log.i("onion", "orderInfo" + orderInfo);
        // 对订单做RSA 签名
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PropertyPayActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(orderInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     */
    public void check(View v) {
        Runnable checkRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(PropertyPayActivity.this);
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();

    }


    interface getOrderInfoService {
//        @POST("/api/v1/communities/{communityId}/users/{emobId}/alipay/orderInfo")
//        void getOrderInfo(@Header("signature") String signature, @Body PropertyPayAlipayBean acr, @Path("communityId") long communityId, @Path("emobId") String emobId, Callback<OrderInfoBean> cb);

        @POST("/api/v3/alipay")
        void getOrderInfoV3(@Header("signature") String signature, @Body PropertyPayAlipayV3Bean acr, Callback<CommonRespBean<String>> cb);
    }

    private void getOrderInfo() {
        mLdDialog.show();
        PropertyPayAlipayV3Bean request = new PropertyPayAlipayV3Bean();
        request.setBeanId("alipayPayment");
        request.setSubject("物业费");
        request.setCityId(bean.getCityId());
        request.setCommunityId(bean.getCommunityId());
        request.setEmobId(bean.getEmobId());
        request.setBonusId(getIntent().getIntExtra("UserBonusId", 0));
        request.setBonuscoinCount(getIntent().getIntExtra("BonusCoinCount", 0));
        request.setName(getIntent().getStringExtra("name"));
        request.setAddress(getIntent().getStringExtra("adress"));
        request.setUnitCount(getIntent().getIntExtra("UnitCount", 0));
        request.setCommunityOwnerId(getIntent().getIntExtra("CommunityOwnerId", 0));
        request.setCommunityPaymentId(getIntent().getIntExtra("communityPaymentId",0));
//        PropertyPayAlipayBean request = new PropertyPayAlipayBean();
//        request.setType("物业缴费");
//        request.setAdress(getIntent().getStringExtra("adress"));
//        request.setBeanId("paymentProcessor");
//        request.setBonusCoinCount(getIntent().getIntExtra("BonusCoinCount",0));
//        request.setBonusPrice(getIntent().getStringExtra("BonusPrice"));
//        request.setCommunityOwnerId(getIntent().getIntExtra("CommunityOwnerId",0));
//        request.setName(getIntent().getStringExtra("name"));
//        request.setPaymentPrice(getIntent().getStringExtra("PaymentPrice"));
//        request.setUnitCount(getIntent().getIntExtra("UnitCount",0));
//        request.setPrice(getIntent().getStringExtra("Price"));
//        request.setSubject("天华物业");
//        request.setUserBonusId(getIntent().getIntExtra("UserBonusId",0));
//        request.setCommunityPaymentId(getIntent().getIntExtra("communityPaymentId",0));
        getOrderInfoService service = RetrofitFactory.getInstance().create(getmContext(),request,getOrderInfoService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, retrofit.client.Response response) {
                if("yes".equals(bean.getStatus())){
                    if (bean != null) {
                        mLdDialog.dismiss();
                        orderInfo = bean.getData();
                        orderNo = (String) bean.getField("orderNo");
                        showToast("获取订单成功");
                    } else {
                        showToast("数据异常");
                    }
                }else{
                    mLdDialog.dismiss();
                    Toast.makeText(getApplicationContext(),bean.getMessage()+"",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(),error.toString()+"",Toast.LENGTH_SHORT).show();
                Log.d(mTag,error.toString());
                mLdDialog.dismiss();
            }
        };
        service.getOrderInfoV3(StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(request)), request, callback);
    }

}
