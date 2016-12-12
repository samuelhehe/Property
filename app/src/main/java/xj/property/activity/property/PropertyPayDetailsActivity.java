package xj.property.activity.property;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.PropertyPayHistoryV3Bean;
import xj.property.beans.PropertyShareMessage;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.TimeUtils;
import xj.property.utils.ToastUtils;
import xj.property.utils.other.Arith;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.PropertyAfterShareUtil;

/**
 * aurth:asia
 * 缴费详情页面
 */
public class PropertyPayDetailsActivity extends HXBaseActivity {

    //titile
    private RelativeLayout mHeaptop;
    private ImageView mIv_back;
    private TextView mTv_left_text;
    private TextView mTv_title;
    private TextView mTv_right_text;
    private ImageView mIv_right;

    private TextView mTv_community_name;
    private TextView mTv_init_price;
    private TextView mTv_house_num;
    private TextView mTv_house_area;
    private TextView mTv_property_pay_time_long;
    private TextView mTv_property_pay_umit_price;
    private TextView mTv_property_all_money;
    private TextView mTv_rubbish_pay_time_long;
    private TextView mTv_rubbish_pay_umit_price;
    private TextView mTv_rubbish_all_money;
    private TextView mTv_all_money;
    private TextView mTv_pay_time;
    private TextView mTv_success;

    private PropertyPayHistoryV3Bean mPropertyPayHistoryBean;
    private UserInfoDetailBean userbean;

//    private String mArea;

    private int paymentId;

    /**
     * 用来获取分享信息
     */
    private String orderNo;

    private String type;
    private PropertyShareMessage propertyShareMessage;
    private TextView tv_community_property_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_pay_cost_details);
        mPropertyPayHistoryBean = (PropertyPayHistoryV3Bean) getIntent().getSerializableExtra("PropertyPayHistoryBean");
//        mArea = getIntent().getStringExtra("area");
        userbean = PreferencesUtil.getLoginInfo(getApplicationContext());
        type = getIntent().getStringExtra("type");
        paymentId = getIntent().getIntExtra("paymentId", 0);
        orderNo = getIntent().getStringExtra("orderNo");//TODO　这个字段是用来获取缴费信息的id
        initView();
        if (paymentId != 0) {
            getPropertyDetails(paymentId); /// 从网络拉取缴费详情信息
        } else {
            initDate(); ///  从历史缴费列表信息中转过来
        }
        initListenner();
    }

    private void initDate() {

        if(!TextUtils.equals("yes",mPropertyPayHistoryBean.getShared())){
            getPropertyShare(getmContext(),orderNo);
        }else{
            /// 设置为已分享
            setTvSuccessShared();
        }
        mTv_title.setText("物业缴费");
        mTv_right_text.setVisibility(View.VISIBLE);
        mTv_right_text.setText("历史账单");
//        if (!TextUtils.isEmpty(type)) {
//            mTv_success.setVisibility(View.GONE);
//        }

        if (mPropertyPayHistoryBean != null) {
            String cname = PreferencesUtil.getCommityName(getApplicationContext());
            mTv_community_name.setText(cname + "物业");
            mTv_init_price.setText("物业费单价（每月）：" + mPropertyPayHistoryBean.getUnitPrice() + "/平方米");
            mTv_house_num.setText(mPropertyPayHistoryBean.getAddress() + "");
            mTv_house_area = (TextView) findViewById(R.id.tv_house_area);  //房屋面积

            final String phone = mPropertyPayHistoryBean.getPhone();
            if (mPropertyPayHistoryBean != null && !TextUtils.isEmpty(phone)) {
                tv_community_property_phone.setText(cname + "物业客服电话 " + phone);
                tv_community_property_phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phone));
                        startActivity(phoneIntent);
                    }
                });
            }else{
                tv_community_property_phone.setVisibility(View.INVISIBLE);
            }

            float big = mPropertyPayHistoryBean.getArea();
            if (big > 0) {
                mTv_house_area.setText("房屋面积：" + Arith.round(big, 0) + "平方米");
                mTv_property_pay_time_long.setText(mPropertyPayHistoryBean.getUnitCount() + "");
                mTv_property_pay_umit_price.setText(Arith.round(Float.parseFloat(mPropertyPayHistoryBean.getUnitPrice()) * Arith.round(big, 0), 2) + "");
                mTv_property_all_money.setText(Arith.round(mPropertyPayHistoryBean.getUnitCount() * Float.parseFloat(mPropertyPayHistoryBean.getUnitPrice()) * Arith.round(big, 0), 2) + "");
                mTv_rubbish_pay_time_long.setText(mPropertyPayHistoryBean.getArrearageCount() + "");
                mTv_rubbish_pay_umit_price.setText(Arith.round(Float.parseFloat(mPropertyPayHistoryBean.getUnitPrice()) * Arith.round(big, 0), 2) + "");

                mTv_rubbish_all_money.setText(Arith.round(mPropertyPayHistoryBean.getArrearageCount() * Float.parseFloat(mPropertyPayHistoryBean.getUnitPrice()) * Arith.round(big, 0), 2) + "" + "");
                mTv_all_money.setText("共计" + mPropertyPayHistoryBean.getPaymentPrice() + "元");
                String time = TimeUtils.fromLongToString2(mPropertyPayHistoryBean.getCreateTime() + "");
                if (time.split("-")[0].length() > 4) {
                    time = TimeUtils.fromLongToString2(mPropertyPayHistoryBean.getCreateTime() / 1000 + "");
                }
                mTv_pay_time.setText("交费时间:" + time);
            }
        }
    }

    private void setTvSuccessShared() {
        mTv_success.setBackgroundColor(Color.parseColor("#DFDFDF"));
        mTv_success.setTextColor(Color.WHITE);
        mTv_success.setText("已分享");
        mTv_success.setVisibility(View.VISIBLE);
    }

    /**
     * 获取需要的缴费
     */
    interface DetailService {
        /**
         * 获取缴费的订单Id
         *
         * @param paymentId
         * @param cb
         */
        @GET("/api/v3/payments/{paymentId}")
        void getPropertyDetails(@Path("paymentId") int paymentId, Callback<CommonRespBean<PropertyPayHistoryV3Bean>> cb);
        /**
         * 获取需要分享的缴费信息
         *
         * @param orderNo
         * @param cb
         */
        @GET("/api/v3/payments/{orderNo}/share")
        void getPropertyShareMessage(@Path("orderNo") String orderNo, @QueryMap HashMap<String, String> querymap, Callback<CommonRespBean<PropertyShareMessage>> cb);

    }

    /**
     * 获取分享信息接口
     */
    private void getPropertyShare( final Context context,  final String orderNo ) {
        HashMap<String,String> querymap = new HashMap<>();
        querymap.put("communityId", "" + PreferencesUtil.getCommityId(context));
        DetailService service = RetrofitFactory.getInstance().create(getmContext(), querymap,DetailService.class);
        Callback<CommonRespBean<PropertyShareMessage>> callback = new Callback<CommonRespBean<PropertyShareMessage>>() {
            @Override
            public void success(CommonRespBean<PropertyShareMessage> commonRespBean, retrofit.client.Response response) {
                if (commonRespBean != null) {
                    if ("yes".equals(commonRespBean.getStatus())) {
                        propertyShareMessage = commonRespBean.getData();
                        if(propertyShareMessage!=null){
                            int coinCount = propertyShareMessage.getBonuscoin();
                            if (coinCount <= 0) {
                                mTv_success.setText("分享");
                            } else {
                                mTv_success.setText("分享得" + coinCount + "帮帮币");
                            }
                            mTv_success.setVisibility(View.VISIBLE);
                            PropertyAfterShareUtil.showPropertyPayAfterShareDialog(context, orderNo, propertyShareMessage.getContent(),propertyShareMessage.getPhoto(),coinCount, new PropertyAfterShareUtil.ShareStatusListener() {
                                @Override
                                public void sharedSuccess() {
                                    mTv_success.setClickable(false);
                                    finish();
                                }
                                @Override
                                public void sharedFailure() {
                                    mTv_success.setClickable(true);
                                }
                            });
                        }
//                        PropertyAfterShareUtil.showPropertyPayAfterShareDialog();
                    } else {
                        Toast.makeText(context, "获取分享内容失败：" + commonRespBean.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        };
        service.getPropertyShareMessage(orderNo, querymap, callback);
    }


    /**
     * 获取订单详情信息
     */
    private void getPropertyDetails(  int paymentId ) {
        DetailService service = RetrofitFactory.getInstance().create(getmContext(), DetailService.class);
        Callback<CommonRespBean<PropertyPayHistoryV3Bean>> callback = new Callback<CommonRespBean<PropertyPayHistoryV3Bean>>() {
            @Override
            public void success(CommonRespBean<PropertyPayHistoryV3Bean> commonRespBean, retrofit.client.Response response) {
                if (commonRespBean != null) {
                    if ("yes".equals(commonRespBean.getStatus())) {
                        mPropertyPayHistoryBean      = commonRespBean.getData();
                        if(mPropertyPayHistoryBean!=null){
                            initDate();
                        }
                    } else {
                        showDataErrorToast(commonRespBean.getMessage());
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        service.getPropertyDetails(paymentId, callback);
    }

    private void initListenner() {
        mIv_back.setOnClickListener(this);
        mTv_right_text.setOnClickListener(this);
        mTv_success.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right_text:
                //TODO 跳入到历史账单页面
                Intent intentHistory = new Intent(PropertyPayDetailsActivity.this, PropertyHistoryActivity.class);
                startActivity(intentHistory);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_success:
                if(TextUtils.equals("yes",mPropertyPayHistoryBean.getShared())){
                    showToast("该订单已分享");
                    return;
                }
                if(!TextUtils.isEmpty(orderNo)){
                    getPropertyShare(getmContext(),orderNo);
                }else{
                    showDataErrorToast();
                }
                break;
        }
    }
    public void initView() {
        mHeaptop = (RelativeLayout) findViewById(R.id.heaptop);
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mTv_left_text = (TextView) findViewById(R.id.tv_left_text);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mTv_right_text = (TextView) findViewById(R.id.tv_right_text);
        mIv_right = (ImageView) findViewById(R.id.iv_right);

        mTv_community_name = (TextView) findViewById(R.id.tv_community_name);
        mTv_init_price = (TextView) findViewById(R.id.tv_init_price);
        mTv_house_num = (TextView) findViewById(R.id.tv_house_num);
        mTv_house_area = (TextView) findViewById(R.id.tv_house_area);
        mTv_property_pay_time_long = (TextView) findViewById(R.id.tv_property_pay_time_long);
        tv_community_property_phone = (TextView) findViewById(R.id.tv_community_property_phone);
        mTv_property_pay_umit_price = (TextView) findViewById(R.id.tv_property_pay_umit_price);
        mTv_property_all_money = (TextView) findViewById(R.id.tv_property_all_money);
        mTv_rubbish_pay_time_long = (TextView) findViewById(R.id.tv_rubbish_pay_time_long);
        mTv_rubbish_pay_umit_price = (TextView) findViewById(R.id.tv_rubbish_pay_umit_price);
        mTv_rubbish_all_money = (TextView) findViewById(R.id.tv_rubbish_all_money);
        mTv_all_money = (TextView) findViewById(R.id.tv_all_money);
        mTv_pay_time = (TextView) findViewById(R.id.tv_pay_time);
        mTv_success = (TextView) findViewById(R.id.tv_success);
        tv_community_property_phone = (TextView) findViewById(R.id.tv_community_property_phone);
    }

}
