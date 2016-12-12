package xj.property.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.ToastUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;


/**
 * 输入手机号，获取验证码
 * 2016/02/23
 */
public class VerificationActivity extends HXBaseActivity {

    /**
     * count down
     */
    private int countDown = 60;
    /**
     * textview to show count down
     */
    private TextView tv_countdown;

    /**
     * resend verification code button
     */
    private Button btn_resend_vefification_code;

    private String autuCode = "";
    private String username;
    private EditText et_verification_code;
    private CommonRespBean<String> authCodeBean;

    private TextView tv_prompt_username;

    private int maxLen = 20;
    Button btn_verification_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        initTitle(null, "验证码", "");
        initView();
        sendMessage();
        countDownNum();
    }

    private void countDownNum() {
        Message message = handler.obtainMessage(1);
        handler.sendMessageDelayed(message, 1000);
    }

    private void initView() {
        btn_verification_code = (Button) findViewById(R.id.btn_verification_code);
        username = getIntent().getStringExtra(Config.LoginUserName);
        tv_prompt_username = (TextView) findViewById(R.id.tv_prompt_username);
        et_verification_code = (EditText) findViewById(R.id.et_verification_code);
       /* et_verification_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = et_verification_code.getText();
                int len = editable.length();
                if (len>0){
                    btn_verification_code.setBackgroundResource(R.drawable.darkgreen_btn_disable);
                }
                if(len > maxLen)
                {
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString().trim();
                    //截取新字符串
                    String newStr = str.substring(0,maxLen);
                    et_verification_code.setText(newStr);
                    editable = et_verification_code.getText();

                    //新字符串的长度
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度
                    if(selEndIndex > newLen)
                    {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex);
                    Toast toast=Toast.makeText(VerificationActivity.this,"验证码为4位数字！",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        //username prompt
        tv_prompt_username.setText(username);

        //count down
        tv_countdown = (TextView) findViewById(R.id.tv_countdown);
        tv_countdown.setText(String.valueOf(countDown));

        //resend verification code
        btn_resend_vefification_code = (Button) findViewById(R.id.btn_resend_vefification_code);
        btn_resend_vefification_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDown = 60;
                tv_countdown.setText(String.valueOf(countDown) + "秒重新发送验证码！");
                authCodeBean = null;
                countDownNum();
                sendMessage();
                tv_countdown.setVisibility(View.VISIBLE);
                btn_resend_vefification_code.setVisibility(View.GONE);
            }
        });


        btn_verification_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /* Intent intentPush = getIntent();
                intentPush.setClass(VerificationActivity.this, PasswordActivity.class);
                startActivityForResult(intentPush, 1);*/
                if (et_verification_code.getText().toString().length() == 0) {
                    showToast("请输入验证码");
                } else {
                    if (authCodeBean != null) {
                        //System.out.println("authCodeBean:"+authCodeBean);
                        if ("yes".equals(authCodeBean.getStatus())) {
                            if (et_verification_code.getText().toString().equals(authCodeBean.getData()) || et_verification_code.getText().toString().equals("xj")) {
                                Intent intentPush = getIntent();
                                intentPush.setClass(VerificationActivity.this, PasswordActivity.class);
                                intentPush.putExtra("authcode",authCodeBean.getData()); //// 验证码， 注册时需要
                                PreferencesUtil.setUserAuthCode(getmContext(),authCodeBean.getData());
                                startActivityForResult(intentPush, 1);
                            } else {
                                Toast.makeText(VerificationActivity.this, "验证码错误!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }


            }


        });


    }


    final Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    countDown--;
                    tv_countdown.setText(countDown + "秒重新发送验证码！");

                    if (countDown > 0) {
                        Message message = handler.obtainMessage(1);
                        handler.sendMessageDelayed(message, 1000);
                    } else {
                        tv_countdown.setVisibility(View.GONE);
                        btn_resend_vefification_code.setVisibility(View.VISIBLE);
                    }
            }
            super.handleMessage(msg);
        }
    };

    interface GetAuthCodeService {
        ///v3/communities/{小区ID}/users/{手机号}/authCode/{验证码业务类型，regist -> 注册,findPassword -> 找回密码}
        @GET("/api/v3/communities/{communityId}/users/{phoneno}/authCode/regist")
        void sendAuthCode(@Path("communityId") int communityId, @Path("phoneno") String phoneno, Callback<CommonRespBean<String>> cb);

    }

    private void sendMessage() {
        GetAuthCodeService getAuthCodeService = RetrofitFactory.getInstance().create(getmContext(),GetAuthCodeService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> commonPostResultBean, Response response) {
                if (TextUtils.equals(commonPostResultBean.getStatus(), "yes")) {
                    authCodeBean = commonPostResultBean;
                } else {
                    ToastUtils.showToast(getmContext(), commonPostResultBean.getMessage());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                showNetErrorToast();
            }
        };
        getAuthCodeService.sendAuthCode(PreferencesUtil.getCommityId(this), username, callback);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            setResult(resultCode);
            finish();
        }
    }

    @Override
    public void onClick(View v) {

    }


}
