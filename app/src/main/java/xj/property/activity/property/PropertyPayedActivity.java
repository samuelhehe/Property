package xj.property.activity.property;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.PropertyPayHistoryBean;
import xj.property.beans.PropertyPayInfoBean;
import xj.property.utils.TimeUtils;
import xj.property.utils.other.PreferencesUtil;

/**
 * aurth:asia
 * 已缴费页面
 */
public class PropertyPayedActivity extends HXBaseActivity {

    //titile
    private RelativeLayout mHeaptop;
    private ImageView mIv_back;
    private TextView mTv_left_text;
    private TextView mTv_title;
    private TextView mTv_right_text;
    private ImageView mIv_right;

    private TextView mTv_property_name;
    private TextView mTv_property_address;
    private TextView mTv_property_message;
    private TextView mTv_property_time;
    private TextView mTv_property;

    private PropertyPayInfoBean mPropertyPayInfoBean;
    private PropertyPayHistoryBean mPropertyPayHistoryBean;
    private TextView tv_community_property_phone_payed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_payed);
        initView();
        initDate();
        initListenner();
    }

    private void initDate() {
        mTv_title.setText("物业缴费");
        mTv_right_text.setVisibility(View.VISIBLE);
        mTv_right_text.setText("历史账单");
        mPropertyPayInfoBean = (PropertyPayInfoBean) getIntent().getSerializableExtra("PropertyPayInfoBean");
        mPropertyPayHistoryBean = (PropertyPayHistoryBean) getIntent().getSerializableExtra("PropertyPayHistoryBean");
        if (mPropertyPayInfoBean != null) {
            setPayed(mPropertyPayInfoBean);
        } else if (mPropertyPayHistoryBean != null) {
            setPayed(mPropertyPayHistoryBean);
        }
    }

    private void initListenner() {
        mIv_back.setOnClickListener(this);
        mTv_right_text.setOnClickListener(this);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right_text:
                Intent intentHistory = new Intent(PropertyPayedActivity.this, PropertyHistoryActivity.class);
                startActivity(intentHistory);
                break;
            case R.id.iv_back:
                finish();
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

        mTv_property_name = (TextView) findViewById(R.id.tv_property_name);
        mTv_property_address = (TextView) findViewById(R.id.tv_property_address);
        mTv_property_message = (TextView) findViewById(R.id.tv_property_message);
        mTv_property_time = (TextView) findViewById(R.id.tv_property_time);
        mTv_property = (TextView) findViewById(R.id.tv_property);
        tv_community_property_phone_payed = (TextView) findViewById(R.id.tv_community_property_phone_payed);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public void setPayed(PropertyPayInfoBean payed) {
        String cname = PreferencesUtil.getCommityName(getApplicationContext());
        mTv_property_name.setText(cname + "物业");
        mTv_property_address.setText(payed.getAddress());
        mTv_property_message.setText(payed.getPaymentExplain());
        mTv_property_time = (TextView) findViewById(R.id.tv_property_time);
        mTv_property_time.setText(TimeUtils.fromLongToString2(payed.getCreateTime() + ""));
        mTv_property.setText(payed.getAddress() + "物业处");

        final String phone = payed.getPhone();
        if (!TextUtils.isEmpty(phone)) {
            tv_community_property_phone_payed.setText(cname + "物业客服电话 " + phone);
            tv_community_property_phone_payed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phone));
                    startActivity(phoneIntent);
                }
            });
        }else{
            tv_community_property_phone_payed.setVisibility(View.INVISIBLE);
        }
    }

    public void setPayed(PropertyPayHistoryBean payed) {
        String cname = PreferencesUtil.getCommityName(getApplicationContext());
        mTv_property_name.setText(cname + "物业");
        mTv_property_address.setText(payed.getAdress());
        mTv_property_message.setText(payed.getCommunityPayment().getPaymentExplain());
        mTv_property_time = (TextView) findViewById(R.id.tv_property_time);
        mTv_property_time.setText(TimeUtils.fromLongToString2(payed.getCreateTime() + ""));
        mTv_property.setText(payed.getAdress() + "物业处");

        final String phone = payed.getCommunityPayment().getPhone();
        if (!TextUtils.isEmpty(phone)) {
            tv_community_property_phone_payed.setText(cname + "物业客服电话 " + phone);
            tv_community_property_phone_payed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phone));
                    startActivity(phoneIntent);
                }
            });
        }else{
            tv_community_property_phone_payed.setVisibility(View.INVISIBLE);
        }
    }
}
