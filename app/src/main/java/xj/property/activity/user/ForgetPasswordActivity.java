package xj.property.activity.user;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.PUT;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.UpdateUserPasswordRequest;
import xj.property.beans.XJUserInfoBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;


/**
 * 忘记密码，进行设置。
 *
 */
public class ForgetPasswordActivity extends HXBaseActivity {

    /**
     * button to see password
     */
    private ImageButton ib_view_password;
    /**
     * password input
     */
    private EditText et_password;

    /**
     * if password is visible
     */
    private boolean isPasswordVisible = false;
    private Button btn_password;
    boolean progressShow;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        initTitle(null, "设置密码", "");
        initView();
        pd = new ProgressDialog(this);
    }


    private void initView() {
        //edit text for password
        et_password = (EditText) findViewById(R.id.et_password);
        //et_password.addTextChangedListener(new MaxLengthWatcher(PasswordActivity.this,16,et_password,"密码最少为6个字符多16个字符！"));
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = et_password.getText();
                int len = editable.length();
                if (len > 0) {
                    btn_password.setBackgroundResource(R.drawable.darkgreen_btn_disable);
                }
                if (len > 16) {
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString().trim();
                    //截取新字符串
                    String newStr = str.substring(0, 16);
                    et_password.setText(newStr);
                    editable = et_password.getText();

                    //新字符串的长度
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度
                    if (selEndIndex > newLen) {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex);
                    Toast toast = Toast.makeText(ForgetPasswordActivity.this, "密码最少为6个字符多16个字符！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //image button to view password
        ib_view_password = (ImageButton) findViewById(R.id.ib_view_password);
        ib_view_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible == false) {
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isPasswordVisible = true;
                } else {
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isPasswordVisible = false;
                }
            }
        });

        //register complete button
        btn_password = (Button) findViewById(R.id.btn_password);
        btn_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = et_password.getText().toString();
                if (password == null || TextUtils.isEmpty(password)) {
                    showToast("密码不能为空");
                    return;
                }
                if (password.length() < 6 || password.length() > 16) {
                    showToast("密码为6至16位字符");
                    return;
                }

                if (et_password.getText().toString().trim().length() < 6) {
                    Toast toast = Toast.makeText(ForgetPasswordActivity.this, "密码最少为6个字符多16个字符！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    if ("-1".equals(PreferencesUtil.getCommityId(ForgetPasswordActivity.this))) {
                        Intent intentPush = getIntent();
                        intentPush.putExtra(Config.LoginUserPwd, et_password.getText().toString());
                        intentPush.putExtra(Config.UPDATE_USERINFO, 101);
                        intentPush.setClass(ForgetPasswordActivity.this, LocationActivity.class);
                        startActivityForResult(intentPush, 1);
                    } else {
                        pd.setCanceledOnTouchOutside(false);
                        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                progressShow = false;
                            }
                        });
                        progressShow = true;
                        pd.setMessage("正在登陆...");
                        if (pd != null && !ForgetPasswordActivity.this.isFinishing())
                            pd.show();
                        updateUserPassInfo(et_password.getText().toString());

                    }

                }
            }
        });
    }


    @Override
    public void onClick(View v) {

    }

    interface XJUserPassService {
        //http://114.215.114.56:8080/api/v1/communities/1/users/update/18310129836
        ///api/v1/communities/{communityId}/users/{userId}
//        getIntent().getStringExtra(Config.LoginUserName)
////api/v3/communities/{小区ID}/users/{手机号}/updatePassword

        @PUT("/api/v3/communities/{communityId}/users/{userName}/findPassword")
        void updateUserPassInfo(@Body UpdateUserPasswordRequest request, @Path("communityId") long communityId, @Path("userName") String userName, Callback<CommonRespBean> cb);
    }


    private void updateUserPassInfo(String password) {

        UpdateUserPasswordRequest request = new UpdateUserPasswordRequest();
        request.setPassword(StrUtils.string2md5(password));
        request.setUsername(getIntent().getStringExtra(Config.LoginUserName));
        request.setAuthCode(getIntent().getStringExtra("authcode"));
        XJUserPassService service2 = RetrofitFactory.getInstance().create(getmContext(),request,XJUserPassService.class);
        Callback<CommonRespBean> callback = new Callback<CommonRespBean>() {
            @Override
            public void success(CommonRespBean bean, retrofit.client.Response response) {
                if ("no".equals(bean.getStatus())) {
                    showToast("修改失败，请稍后再试");
                } else {
                    showToast("修改成功");
                    setResult(1);
                    UserUtils.loginUser(ForgetPasswordActivity.this, getIntent().getStringExtra(Config.LoginUserName), et_password.getText().toString().trim(), new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            //finish();
                            switch (msg.what) {
                                case Config.LoginUserComplete:
                                    if (progressShow) pd.dismiss();
                                    finish();
                                    break;
                                case Config.LoginUserFailure:
                                    if (progressShow && !ForgetPasswordActivity.this.isFinishing())
                                        pd.dismiss();
                                    break;
                                case Config.LoginUserSuccess:
                                    pd.setMessage("正在登录聊天系统..");
                                    break;
                                case Config.LogoutTourist:
                                    pd.setMessage("正在登出游客..");
                                    break;
                                case Config.LoginUserUPDATENATIVE:
                                    pd.setMessage("正在加载群，联系人信息..");
                                    break;
                            }
                        }
                    });
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        service2.updateUserPassInfo(request, PreferencesUtil.getCommityId(this),getIntent().getStringExtra(Config.LoginUserName), callback);
    }


}
