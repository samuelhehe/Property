package xj.property.activity.user;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.PUT;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.UpdateUserPasswordRequest;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.CommonUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.MaxLengthWatcher;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

/**
 * 修改密码
 */
public class FixUserPasswordActivity extends HXBaseActivity {
    private EditText et_fix_oldpwd;
    private EditText et_fix_newpwd;

    private String oldPass;
    private String newPass;
    UserInfoDetailBean userInfoDetailBean;

    private boolean progressShow;
    private ProgressDialog pd;

    private TextView tv_right_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_user_password);
        pd = new ProgressDialog(FixUserPasswordActivity.this);
        userInfoDetailBean = PreferencesUtil.getLoginInfo(FixUserPasswordActivity.this);
        initTitle(null, "修改密码", "保存");
        initView();
    }


    private void initView() {
        et_fix_oldpwd = (EditText) findViewById(R.id.et_fix_oldpwd);
        et_fix_newpwd = (EditText) findViewById(R.id.et_fix_newpwd);
        tv_right_text = (TextView) findViewById(R.id.tv_right_text);
        tv_right_text.setVisibility(View.VISIBLE);
        tv_right_text.setOnClickListener(this);

        et_fix_newpwd.addTextChangedListener(new MaxLengthWatcher(FixUserPasswordActivity.this, 16, et_fix_newpwd, "密码最少为6个字符最多16个字符！"));
        et_fix_oldpwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                } else {
                    // 此处为失去焦点时的处理内容
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right_text:
                //  fixUserPassInfo();
                newPass = et_fix_newpwd.getText().toString();
                oldPass = et_fix_oldpwd.getText().toString().trim();

                if (newPass.length() >= 6&&newPass.length()<=16) {
                    if (CommonUtils.isNetWorkConnected(getmContext())) {
                        pd.setCanceledOnTouchOutside(false);
                        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                progressShow = false;
                            }
                        });
                        pd.setMessage("正在修改，请稍后...");
                        if (pd != null && !FixUserPasswordActivity.this.isFinishing()) {
                            progressShow = true;
                            pd.show();
                        }
                        updateUserPassInfo(userInfoDetailBean.getCommunityId(), newPass, oldPass);
                    } else {
                        showNetErrorToast();
                    }

                } else {
                    Toast toast = Toast.makeText(FixUserPasswordActivity.this, "密码最少为6个字符最多16个字符！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                break;

        }
    }


    interface XJUserPassService {
        ///api/v1/communities/{communityId}/users/{userId}

        //// /api/v3/communities/{小区ID}/users/{手机号}/updatePassword

        //        @PUT("/api/v1/communities/{communityId}/users/update/{userName}")
        ///api/v3/communities/{小区ID}/users/{手机号}/resetPassword
        @PUT("/api/v3/communities/{communityId}/users/{userName}/resetPassword")
        void updateUserPassInfo(@Body UpdateUserPasswordRequest request, @Path("communityId") int communityId, @Path("userName") String userName, Callback<CommonRespBean<String>> cb);
    }


    /**
     * 重置密码
     * v3 2016/02/29
     *
     * @param communityId
     * @param newpassword
     * @param password
     */
    private void updateUserPassInfo(int communityId, final String newpassword, final String password) {
        final UpdateUserPasswordRequest request = new UpdateUserPasswordRequest();
        request.setUsername(userInfoDetailBean.getUsername());
        request.setNewPassword(StrUtils.string2md5(newpassword));
        request.setPassword(StrUtils.string2md5(password));

        XJUserPassService service2 = RetrofitFactory.getInstance().create(getmContext(), request, XJUserPassService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, retrofit.client.Response response) {
                if ("no".equals(bean.getStatus())) {
                    showToast("修改失败：" + bean.getMessage());
                    if (progressShow) pd.dismiss();
                } else {
                    pd.setMessage("密码修改成功，正在重新登录..");
                    UserUtils.loginUser(FixUserPasswordActivity.this, userInfoDetailBean.getUsername(), newpassword, new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            //finish();
                            switch (msg.what) {
                                case Config.LoginUserComplete:
                                    finish();
                                    if (progressShow) pd.dismiss();
                                    break;
                                case Config.LoginUserFailure:
                                    if (progressShow && !FixUserPasswordActivity.this.isFinishing())
                                        pd.dismiss();
                                    break;
                                case Config.LoginUserSuccess:
                                    pd.setMessage("正在登录帮帮..");
                                    break;
                                case Config.LogoutTourist:
                                    pd.setMessage("正在登录帮帮..");
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
                if (progressShow) pd.dismiss();
                showNetErrorToast();
            }
        };
        service2.updateUserPassInfo(request, communityId, userInfoDetailBean.getUsername(), callback);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pd != null) {
            pd.dismiss();
        }
    }
}
