package xj.property.activity.user;

import android.content.Intent;
import android.os.Bundle;
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

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.utils.other.Config;

public class PasswordActivity extends HXBaseActivity {

    /**
     * logger
     */
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        initTitle(null, "设置密码", "");
        initView();
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
                    Toast toast = Toast.makeText(PasswordActivity.this, "密码最少为6个字符最多16个字符！", Toast.LENGTH_SHORT);
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
                Intent intentPush = getIntent();
                if (et_password.getText().toString().trim().length() < 6) {
                    Toast toast = Toast.makeText(PasswordActivity.this, "密码最少为6个字符多16个字符！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    intentPush.putExtra(Config.LoginUserPwd, et_password.getText().toString());
                    intentPush.putExtra("authcode",getIntent().getStringExtra("authcode"));
                    //intentPush.setClass(PasswordActivity.this, CommunitySelectActivity.class);RoomSelectActivity
                    intentPush.setClass(PasswordActivity.this, UserNickNameActivity.class);
                    startActivityForResult(intentPush, 1);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RESULT_OK==resultCode)
        {    setResult(resultCode);
        finish();}
    }

    @Override
    public void onClick(View v) {

    }

}
