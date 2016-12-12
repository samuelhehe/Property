package xj.property.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.utils.other.Config;


/**
 * 找回密码 输入手机号
 *
 * @date 2016/02/23
 *
 */
public class ForgetUsernameActivity extends HXBaseActivity {
    /**
     * edit text for username
     */
    private EditText et_username;

    private  Button btn_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);
        initTitle(null, "用户名", "");
        initView();
    }

    /**
     * init view
     */
    private void initView(){
        //username
        et_username = (EditText)findViewById(R.id.et_username);
        //button next
         btn_username = (Button)findViewById(R.id.btn_username);
        btn_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isMobileNO(et_username.getText().toString())){
                    Toast.makeText(ForgetUsernameActivity.this,"请输入正确手机号",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intentPush = new Intent();
                    String username=et_username.getText()+"";
                    intentPush.putExtra(Config.LoginUserName,username);
                    intentPush.setClass(ForgetUsernameActivity.this,ForgetVerificationActivity.class);
                    startActivityForResult(intentPush, 1);
                }
            }
        });
        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (et_username.getText().toString().trim().length()!=0){
                btn_username.setBackgroundResource(R.drawable.darkgreen_btn_disable);
            }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * if mobile NO valid
     * @param mobiles
     * @return
     */
    public boolean isMobileNO(String mobiles) {

        Pattern p = Pattern

                .compile("^(0|86|17951)?(13[0-9]|15[012356789]|17[6780]|18[0-9]|14[57])[0-9]{8}$");

        Matcher m = p.matcher(mobiles);

        return m.matches();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    finish();
    }

    @Override
    public void onClick(View v) {

    }

}
