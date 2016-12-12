package xj.property.activity.contactphone;

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

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.alipay.PayResult;
import xj.property.beans.GetOrderInfoRequest;
import xj.property.beans.OrderInfoBean;
import xj.property.beans.ShopOrderInfoReqBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Arith;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;


/**
 * 支付宝支付快店页
 * v3 2016/02/29
 */
public class ShopAliPayActivity extends HXBaseActivity {

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

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(ShopAliPayActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                        String address = getIntent().getStringExtra("address");
                        int useBonusId = getIntent().getIntExtra("useBonusId", 0);
                        Intent intent = new Intent();
                        intent.setClass(ShopAliPayActivity.this, PayPreActivity.class);
                        intent.putExtra("address", address);
                        intent.putExtra("useBonusId", useBonusId);
                        setResult(103, intent);
                        finish();

                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(ShopAliPayActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(ShopAliPayActivity.this, "支付失败",
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

    ShopOrderInfoReqBean shopOrderInfoReqBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_main);
        bean = PreferencesUtil.getLoginInfo(this);
        TextView product_desc=(TextView)findViewById(R.id.product_desc);
        TextView product_price=(TextView)findViewById(R.id.product_price);
        product_desc.setText(getIntent().getStringExtra("goodsname"));
        shopOrderInfoReqBean = (ShopOrderInfoReqBean) getIntent().getSerializableExtra("shopOrderInfoReqBean");

        product_price.setText(getIntent().getDoubleExtra(Config.EXPKey_totalPrice,0.0)+"");
        getOrderInfo();
        check(null);
    }

    String orderInfo;

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(View v) {
        // 订单
        //orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01");
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
                PayTask alipay = new PayTask(ShopAliPayActivity.this);
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
                PayTask payTask = new PayTask(ShopAliPayActivity.this);
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


    private void getOrderInfo() {
        mLdDialog.show();
        ShopOrderInfoService service = RetrofitFactory.getInstance().create(getmContext(),ShopOrderInfoService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, retrofit.client.Response response) {
                mLdDialog.dismiss();
                orderInfo = bean.getData();
                showToast("获取订单成功");
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
            }
        };
        // {"subject":"","body":"","price":"","orderId":0,"userBonusId":0}

        if(shopOrderInfoReqBean!=null){
            service.shopAliPayOrder(shopOrderInfoReqBean,callback);
        }
    }

    interface ShopOrderInfoService {
        ///api/v1/communities/1/users/123/alipay/orderInfo 订单信息
//        @POST("/api/v1/communities/{communityId}/users/{emobId}/alipay/orderInfo")
//        void getOrderInfo(@Header("signature") String signature, @Body GetOrderInfoRequest acr, @Path("communityId") long communityId, @Path("emobId") String emobId, Callback<OrderInfoBean> cb);


//        @POST("/api/v1/communities/{communityId}/users/{emobId}/alipay/orderInfo")


//        @POST("/api/v1/communities/{communityId}/users/{emobId}/alipay/orderInfo")
//        void getOrderInfo(@Header("signature") String signature, @Body GetOrderInfoRequest acr, @Path("communityId") long communityId, @Path("emobId") String emobId, Callback<OrderInfoBean> cb);

        //        @POST("/api/v1/communities/{communityId}/users/{emobId}/alipay/orderInfo")

        @POST("/api/v3/alipay")
        void shopAliPayOrder(@Body ShopOrderInfoReqBean acr, Callback<CommonRespBean<String>> cb);
    }


    @Override
    public void onClick(View v) {

    }
}
