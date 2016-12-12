package xj.property.activity.membership;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.MSPCardBean;
import xj.property.beans.RatingReqBean;
import xj.property.beans.RatingRespBean;
import xj.property.beans.SendNotifyReqBean;
import xj.property.beans.SendNotifyRespBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Arith;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

/**
 * 会员卡支付页面
 */
public class ActivityMShipCardPaySuccess extends HXBaseActivity {

    private TextView tv_title;

    private TextView msp_pay_time_tv;

    private TextView msp_pay_money_tv;

    private ImageView msp_shop_pic_iv;

    private TextView msp_shop_name_tv;

    private RatingBar msp_shop_star_rb;

    private TextView msp_shop_score_tv;

    private TextView msp_shop_total_tv;

    private RatingBar msp_paysuccess_ratingscore_rb;

    private Button msp_paysuccess_btn;

    private float star ;

    private long payTime;

    private String payedprice;

    private MSPCardBean.MSPCardDetailBean mspcardBean;
    private String serial;

    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    private UserInfoDetailBean userInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msp_card_paysuccess);

        initView();
        initData();

    }

    private void initData() {
        mspcardBean = (MSPCardBean.MSPCardDetailBean) getIntent().getSerializableExtra("MSPCardBeanShopInfo");
        userInfoBean = PreferencesUtil.getLoginInfo(getmContext());
        payTime = getIntent().getLongExtra("paytime", System.currentTimeMillis());

        payedprice = getIntent().getStringExtra("payedprice");

        serial = getIntent().getStringExtra("serial");

        if(mspcardBean!=null){
            msp_pay_time_tv.setText("支付时间: "+ format.format(new Date(payTime)));
            msp_pay_money_tv.setText("¥"+payedprice);
            ImageLoader.getInstance().displayImage(mspcardBean.getShoppic(),msp_shop_pic_iv);

            msp_shop_name_tv.setText(mspcardBean.getShopName());

            msp_shop_star_rb.setRating(Float.valueOf(String.valueOf(mspcardBean.getStar())));

            msp_shop_score_tv.setText(""+Arith.round(mspcardBean.getStar(),1)+"分");

            msp_shop_total_tv.setText(mspcardBean.getOrderCount()+"单");

            //// 调用通知店家端 , 用户已经消费成功 取消调用, 现在server端 调用 推送至商家端
//            nofitySurroundingShop();


        }else{
           showToast("数据异常");
           finish();
        }
    }

    private void initView() {
        initTitle();
        /// 支付时间
        msp_pay_time_tv = (TextView) this.findViewById(R.id.msp_pay_time_tv);
        /// 支付了多少rmb
        msp_pay_money_tv = (TextView) this.findViewById(R.id.msp_pay_money_tv);
        /// 店铺门面图
        msp_shop_pic_iv  = (ImageView) this.findViewById(R.id.msp_shop_pic_iv);
        //// 店铺名
        msp_shop_name_tv = (TextView) this.findViewById(R.id.msp_shop_name_tv);
        /// 店铺星级
        msp_shop_star_rb = (RatingBar) this.findViewById(R.id.msp_shop_star_rb);
        msp_shop_star_rb.setEnabled(false);
        /// 店铺评分
        msp_shop_score_tv = (TextView) this.findViewById(R.id.msp_shop_score_tv);

        /// 店铺距离
        msp_shop_total_tv = (TextView) this.findViewById(R.id.msp_shop_total_tv);

        /// 滑动评分
        msp_paysuccess_ratingscore_rb = (RatingBar) this.findViewById(R.id.msp_paysuccess_ratingscore_rb);
        msp_paysuccess_ratingscore_rb.setRating(5.0f);
        star = 5.0f;
        msp_paysuccess_ratingscore_rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                star =rating;
                Log.d("onRatingChanged: ", ""+ rating);
            }
        });
        msp_paysuccess_btn = (Button)this.findViewById(R.id.msp_paysuccess_btn);
        msp_paysuccess_btn.setOnClickListener(this);

    }

    private void initTitle() {

        this.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title= (TextView)this.findViewById(R.id.tv_title);
        tv_title.setText("消费清单");
    }



    interface InvitateByPhoneNumService {


//        @POST("/api/v1/shopVipcards/grade")
//        void inviateByPhoneNum(@Header("signature") String signature, @Body RatingReqBean acr, Callback<RatingRespBean> cb);

//        @POST("/api/v1/shopVipcards/grade")

        @POST("/api/v3/nearbyVipcards/grade")
        void goshopGrade(@Body RatingReqBean acr, Callback<CommonRespBean<String>> cb);
    }

    private void invateByPhone() {
        mLdDialog.show();

        RatingReqBean request = new RatingReqBean();
        request.setOrderNo(serial);
        /// 订单编号 , 评分数
        request.setStar(""+ star);
        request.setEmobIdUser(userInfoBean.getEmobId());
        InvitateByPhoneNumService service = RetrofitFactory.getInstance().create(getmContext(),request,InvitateByPhoneNumService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, retrofit.client.Response response) {
                if(bean!=null&& TextUtils.equals(bean.getStatus(), "yes")){
                    showMiddleToast("评分成功");

                    Intent intent =new Intent (getmContext(), ActivityMShipCardPay.class);
                    intent.putExtra("ShopVipcardId",mspcardBean.getNearbyVipcardId());

                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    showMiddleToast(bean.getMessage());

                }
                mLdDialog.dismiss();
            }
            @Override
            public void failure(RetrofitError error) {
                showMiddleToast("评分失败,请重试");
                mLdDialog.dismiss();
            }
        };
        service.goshopGrade(request, callback);

    }


        @Override
    public void onClick(View v) {

            switch (v.getId()){

                case R.id.msp_paysuccess_btn:

                    invateByPhone();

                    break;
            }


    }


}
