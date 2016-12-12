package xj.property.activity.fitmentfinish;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.util.Log;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.CommonPostResultBean;
import xj.property.beans.FitmentFinishHelpRequest;
import xj.property.beans.UserInfoDetailBean;
import xj.property.utils.ToastUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;


/**
 * 作者：asia on 2015/12/11 11:56
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class FitMentFinishHelpActivity extends HXBaseActivity {

    private String mTag = "FitMentFinishHelpActivity";

    private int decorationId;

    private TextView mTv_phone;
    private TextView mTv_play_phone;
    private TextView mTv_message;
    private EditText mEt_phone;
    private TextView mTv_edit_phone;

    private String mPhone;
    private String mMobilePhone;

    private UserInfoDetailBean userBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitment_design);
        userBean = PreferencesUtil.getLoginInfo(FitMentFinishHelpActivity.this);
        decorationId = getIntent().getIntExtra("decorationId", 0);
        mPhone = getIntent().getStringExtra("phone");
        mMobilePhone = getIntent().getStringExtra("mobilePhone");
        initView();
        initData();
        initListenner();
    }

    /**
     * 初始化标题
     */
    private void initView() {
        mTv_phone = (TextView) findViewById(R.id.tv_phone);
        mTv_play_phone = (TextView) findViewById(R.id.tv_play_phone);
        mTv_message = (TextView) findViewById(R.id.tv_message);
        mEt_phone = (EditText) findViewById(R.id.et_phone);
        mTv_edit_phone = (TextView) findViewById(R.id.tv_edit_phone);
    }

    private void initData() {
        ((TextView) this.findViewById(R.id.tv_title)).setText("装修");
        this.findViewById(R.id.iv_back).setOnClickListener(this);
        editPhone(getPhoneNum(userBean));
        mTv_phone.setText("咨询电话："+mPhone);
        getFriendZone(mMobilePhone,getPhoneNum(userBean));//TODO 需要把我的手机号发送给他   初始化
    }

    private void initListenner() {
        mTv_play_phone.setOnClickListener(this);
        mTv_edit_phone.setOnClickListener(this);
    }

    /**
     * 获取ActList部分
     */
    interface ActHelp{
        @POST("/api/v1/decoration/sendSMS/{phone}")
        void sendMessage(@Header("signature") String signature, @Body FitmentFinishHelpRequest circleNewRecord, @Path("phone") String phone, Callback<CommonPostResultBean> cb);
    }

    private void getFriendZone(String phone,final String userPhone) {
        mLdDialog.show();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        ActHelp service = restAdapter.create(ActHelp.class);
        Callback<CommonPostResultBean> callback = new Callback<CommonPostResultBean>() {
            @Override
            public void success(CommonPostResultBean bean, retrofit.client.Response response) {
                mLdDialog.dismiss();
                if (bean != null) {
                    if("yes".equals(bean.getStatus())&&bean.getInfo()!=null){

                        ToastUtils.showToast(getmContext(),"亲，已经把您的电话号发送给商家！");

//                        Toast.makeText(getApplicationContext(), "亲，已经把您的电话号发送给商家！", Toast.LENGTH_SHORT ).show();
                        editPhone(userPhone);
                    }else if("no".equals(bean.getStatus())){

                        ToastUtils.showToast(getmContext(),"亲，电话发送失败，请重新发送！");
                    }else {
                        Log.d(mTag, "getFriendZone  ==========   null");
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        FitmentFinishHelpRequest circleNewRecord = new FitmentFinishHelpRequest();
        circleNewRecord.setEmobId(userBean.getEmobId());
        circleNewRecord.setDecorationId(decorationId);
        circleNewRecord.setPhone(userPhone);
        service.sendMessage(StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(circleNewRecord)), circleNewRecord, phone, callback);
    }

    private void editPhone(String phone){
        int fstart = 36;
        int fend = 47;
        String str = "帮帮已将您的需求意向通知给东易日盛装饰设计有限公司，负责人将在1小时内与"+phone+"，请您保持电话畅通.";
        SpannableStringBuilder style=new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(0xff2FCC71),fstart,fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        mTv_message.setText(style);
    }

    private String getPhoneNum(UserInfoDetailBean userBean){
        return "".equals(userBean.getPhone())?userBean.getUsername():userBean.getPhone();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_getagain:
                initData();
                /// 重试
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_play_phone:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mPhone));
                startActivity(intent);
                break;
            case R.id.tv_edit_phone:
                if(isMobileNO(mEt_phone.getText().toString())){
                    getFriendZone(mMobilePhone,mEt_phone.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(),"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

}
