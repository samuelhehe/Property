package xj.property.activity.welfare;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.POST;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.alipay.PayResult;
import xj.property.beans.UserInfoDetailBean;
import xj.property.beans.WelfareGetOrderInfoRequest;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.other.PreferencesUtil;


/**
 * 福利支付页面,订单.
 */
public class ActivityWelfarePay extends HXBaseActivity {


    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case SDK_PAY_FLAG: {

                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();
                    Intent intent = new Intent();
                    intent.setClass(ActivityWelfarePay.this, ActivityWelfarePrePay.class);

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {

                        showWelfareToast("支付成功");

                        intent.putExtra("resultInfo", resultInfo);
                        setResult(103, intent);
                        finish();

                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）

                        if (TextUtils.equals(resultStatus, "8000")) {
//                            Toast.makeText(ActivityWelfarePay.this, "支付结果确认中",  Toast.LENGTH_SHORT).show();
                            showWelfareToast("支付结果确认中");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            showWelfareToast("支付充值失败");
//                            showDialogToast("支付充值失败");
                        }
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {

//                    Toast.makeText(ActivityWelfarePay.this, "检查结果为：" + msg.obj,
//                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

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


    private void showDialogToast(String showTxt) {

        final Dialog dialog = new Dialog(ActivityWelfarePay.this, R.style.werlfare_DialogStyle);
        dialog.setContentView(R.layout.common_welfare_toast_or_prepay);
        ((TextView) dialog.findViewById(R.id.toast_title_tv)).setText(showTxt);
        dialog.show();
        CountDownTimer countDownTimer = new CountDownTimer(1500, 1000) {
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


    private UserInfoDetailBean bean;


    private WelfareGetOrderInfoRequest welfareOrderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_main);
        bean = PreferencesUtil.getLoginInfo(this);

        welfareOrderInfo = (WelfareGetOrderInfoRequest) getIntent().getSerializableExtra("welfareOrderInfo");
        if (welfareOrderInfo == null) {
            showToast("订单信息获取失败请稍后重试");
            finish();
        }
        TextView product_desc = (TextView) findViewById(R.id.product_desc);
        TextView product_price = (TextView) findViewById(R.id.product_price);

        product_desc.setText(TextUtils.isEmpty(welfareOrderInfo.getSubject()) ? "帮帮商品" : welfareOrderInfo.getSubject());
        product_price.setText(welfareOrderInfo.getPrice());

        getOrderInfo();
        check(null);
    }


    //// 服务器端返回的商品订单串
    private String orderInfo;

    /**

     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(View v) {
        // 订单
//        orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01");
        if (orderInfo == null) return;
        Log.i("onion", "orderInfo" + orderInfo);
        // 对订单做RSA 签名
//		String sign = sign(orderInfo);
//		try {
//			// 仅需对sign 做URL编码
//			sign = URLEncoder.encode(sign, "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//
//		// 完整的符合支付宝参数规范的订单信息
//		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
//				+ getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(ActivityWelfarePay.this);
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
                PayTask payTask = new PayTask(ActivityWelfarePay.this);
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

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }


    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    public String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }


    interface GetOrderInfoService {
        ///api/v1/communities/1/users/123/alipay/orderInfo 订单信息
//        @POST("/api/v1/communities/{communityId}/users/{emobId}/alipay/orderInfo")
//        void welfareAlipay(@Header("signature") String signature, @Body WelfareGetOrderInfoRequest acr, @Path("communityId") long communityId, @Path("emobId") String emobId, Callback<OrderInfoBean> cb);
//

        @POST("/api/v3/alipay")
        void welfareAlipay(@Body WelfareGetOrderInfoRequest acr, Callback<CommonRespBean<String>> cb);
    }

    private void getOrderInfo() {
        mLdDialog.show();

        ////   支付信息确认

        WelfareGetOrderInfoRequest request = welfareOrderInfo;

        GetOrderInfoService service = RetrofitFactory.getInstance().create(getmContext(),request,GetOrderInfoService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, retrofit.client.Response response) {
                mLdDialog.dismiss();
                if (bean != null && TextUtils.equals(bean.getStatus(), "yes")) {
                    orderInfo = bean.getData();
                    showToast("获取订单成功");

                } else if (bean != null && TextUtils.equals(bean.getStatus(), "no")) {
                    ////
                    showToast("获取订单失败："+bean.getMessage());
                    Log.d("Pay: ", bean.getData());
                }else{
                    showNetErrorToast();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
            }

        };

        //{"subject":"商家重置","price":"0.01","beanId":"crazySalesProcessor"}
        // {"subject":"","body":"","price":"","orderId":0,"userBonusId":0}


        service.welfareAlipay(request, callback);
    }


    @Override
    public void onClick(View v) {

    }
}
