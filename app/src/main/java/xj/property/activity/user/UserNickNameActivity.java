package xj.property.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.utils.other.Config;

public class UserNickNameActivity extends HXBaseActivity {
    /**
     * logger
     */
    /**
     * edit text for username
     */
    private EditText et_username;
    private Button btn_username;
    /// 敏感词提示
    private TextView verification_result_tv;
    /// 敏感词列表  2015/12/08  null
    private static final String [] UNPASS_NICKNAME_WORD = {"帮帮", "物业", "客服", "管家","null"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usernickname);
        initTitle(null, "输入昵称", "");
        initView();
    }

    /**
     * init view
     */
    private void initView() {

        verification_result_tv = (TextView) findViewById(R.id.verification_result_tv);
        //username
        et_username = (EditText) findViewById(R.id.et_username);
        //  et_username.addTextChangedListener(new MaxLengthWatcher(this,4,et_username,"最长4个字符！"));
//        et_username.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                Editable editable = et_username.getText();
//                int len = editable.length();
//                if (len>0){
//                    btn_username.setBackgroundResource(R.drawable.darkgreen_btn_disable);
//                }
//                if(len > 4)
//                {
//                    int selEndIndex = Selection.getSelectionEnd(editable);
//                    String str = editable.toString().trim();
//                    //截取新字符串
//                    String newStr = str.substring(0,4);
//                    et_username.setText(newStr);
//                    editable = et_username.getText();
//
//                    //新字符串的长度
//                    int newLen = editable.length();
//                    //旧光标位置超过字符串长度
//                    if(selEndIndex > newLen)
//                    {
//                        selEndIndex = editable.length();
//                    }
//                    //设置新光标所在的位置
//                    Selection.setSelection(editable, selEndIndex);
//                    Toast toast=Toast.makeText(UserNickNameActivity.this,"最长4个字符！",Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER,0,0);
//                    toast.show();
//                }
//
//            }
//        });

        btn_username = (Button) findViewById(R.id.btn_username);
        btn_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nickName = et_username.getText().toString().trim();
                /// 检测昵称的合法性
                if (checkNickNameIsPass(nickName)) {
                    Intent intentPush = getIntent();
                    String nickname = et_username.getText() + "";
                    intentPush.putExtra(Config.LoginUserNickName, nickname);
                    storeLocation(nickname);
                    intentPush.setClass(UserNickNameActivity.this, UserAreaActivity.class);
                    startActivityForResult(intentPush, 1);
                }
            }
        });
    }

    private boolean checkNickNameIsPass(String nickName) {

        if (TextUtils.isEmpty(nickName)) {
            Toast.makeText(UserNickNameActivity.this, "昵称不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (et_username.getPaint().measureText(nickName) > 210) {
            Toast.makeText(UserNickNameActivity.this, "昵称过长，建议使用真实姓名!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(checkUnPassWord(nickName)){
            if(verification_result_tv!=null){
                verification_result_tv.setVisibility(View.INVISIBLE);
            }
            return true;
        }else{
            if(verification_result_tv!=null){
                verification_result_tv.setVisibility(View.VISIBLE);
            }
            return false;
        }

    }

    /**
     *
     * true 不包含 , false 包含
     * @param nickName
     * @return
     */
    private boolean checkUnPassWord(String nickName) {
        List<String> unpassWords = Arrays.asList(UNPASS_NICKNAME_WORD);
        for(String s:  unpassWords){
            if(nickName.contains(s)){
                return false;
            }
        }
        return true;
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

    /**
     * store city and community
     */

    private void storeLocation(String nickname) {
        SharedPreferences sharedPreferences = getSharedPreferences("xj", this.MODE_PRIVATE);
        //get editor
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nickname", nickname);
        editor.commit();//提交修改
    }

}
