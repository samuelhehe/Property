package xj.property.activity.HXBaseActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushManager;

import de.greenrobot.event.EventBus;
import xj.property.R;
import xj.property.XjApplication;
import xj.property.activity.user.ForgetUsernameActivity;
import xj.property.activity.user.UsernameActivity;
import xj.property.event.NewRefreshIndexMenuEvent;
import xj.property.utils.CommonUtils;
import xj.property.utils.ToastUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

/**
 * Created by n on 2015/4/29.
 */
public class LoginDialogActivity extends HXBaseActivity {
    EditText et_username;
    EditText et_password;
    boolean progressShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_username = (EditText) findViewById(R.id.username);
        et_password = (EditText) findViewById(R.id.password);
        TextView tv_go_regist = (TextView) findViewById(R.id.btn_register);
//        TextView tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        Button btn_login = (Button) findViewById(R.id.btn_login);
        TextView tv_forgetpwd = (TextView) findViewById(R.id.btn_get_password);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_username.getText() != null && et_username.getText().toString().trim().length() == 0) {

                    ToastUtils.showToast(getmContext(), "请输入用户名");
                    return;
                }
                if (et_password.getText() != null && et_password.getText().toString().trim().length() == 0) {
                    ToastUtils.showToast(getmContext(), "请输入密码");
                    return;
                }
                if (!CommonUtils.isNetWorkConnected(getmContext())) {
                    showNetErrorToast();
                    return;
                }

                if (et_username.getText().toString() != null && et_password.getText().toString() != null) {
                    final ProgressDialog pd = new ProgressDialog(LoginDialogActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
                    pd.setCanceledOnTouchOutside(false);
                    pd.setCancelable(false);
                    pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

                        @Override
                        public void onCancel(DialogInterface dialog) {
                            progressShow = false;
                        }
                    });
                    progressShow = true;
                    pd.setMessage("正在登录帮帮..");
                    if (pd != null && !LoginDialogActivity.this.isFinishing())
                        pd.show();
                    UserUtils.loginUser(LoginDialogActivity.this, et_username.getText().toString(), et_password.getText().toString(), new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            switch (msg.what) {
                                case Config.LoginUserComplete:
                                    /// 登陆完成强制刷新主界面模块 indexframent 处理
                                    EventBus.getDefault().post(new NewRefreshIndexMenuEvent(1));

                                    if (progressShow) pd.dismiss();
                                    setResult(RESULT_OK);
//                                    UserBonusUtils.getCanusedCount(LoginDialogActivity.this);
//                                    UserBonusUtils.getRPValue(LoginDialogActivity.this);
                                    if ("0".equals(PreferencesUtil.getCommityId(XjApplication.getInstance()))) {
                                        PreferencesUtil.saveLifeCircleCountTime(XjApplication.getInstance(), "" + System.currentTimeMillis() / 1000);
                                    }

                                    finish();
                                    break;
                                case Config.LoginUserFailure:
                                    if (progressShow && !LoginDialogActivity.this.isFinishing()) {
                                        pd.dismiss();
                                        Toast.makeText(LoginDialogActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case Config.LoginUserSuccess:
                                    pd.setMessage("正在登录帮帮..");
                                    break;
                                case Config.LogoutTourist:
                                    pd.setMessage("正在登出游客..");
                                    break;
                                case Config.LoginUserUPDATENATIVE:
                                    pd.setMessage("正在加载群，联系人信息..");

//                                    EMChatManager.getInstance().

//        PreferencesUtil.saveCid(LoginDialogActivity.this,PushManager.getInstance().getClientid(LoginDialogActivity.this));
                                    break;

                            }
                        }
                    });
                    // finish();
                    // UserUtils.loginUser(LoginDialogActivity.this, et_username.getText().toString(), et_password.getText().toString(), new Handler());
                }
            }
        });

        tv_go_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!PreferencesUtil.getTouristLogin(LoginDialogActivity.this)) {
//                    Intent intent = new Intent(LoginDialogActivity.this, LocationActivity.class);
//                    intent.putExtra(Config.UPDATE_USERINFO, 101);
//                    startActivity(intent);
//                    finish();
//                } else {
                Intent intent = new Intent(LoginDialogActivity.this, UsernameActivity.class);
                startActivity(intent);
                finish();
//                }

            }
        });

        tv_forgetpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginDialogActivity.this, ForgetUsernameActivity.class);
                startActivity(intent);
                finish();
            }
        });

//        tv_cancle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        // 如果用户名改变，清空密码
        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // et_username.setText(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (XjApplication.getInstance().getUserName() != null) {
            et_username.setText(XjApplication.getInstance().getUserName());
        }
    }


    @Override
    public void onClick(View v) {

    }
}
