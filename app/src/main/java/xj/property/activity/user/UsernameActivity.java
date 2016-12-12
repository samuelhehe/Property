package xj.property.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;


/**
 * 验证用户名是否会重复
 */
public class UsernameActivity extends HXBaseActivity {
    /**
     * edit text for username
     */
    private EditText et_username;

    private Button btn_username;

    private LinearLayout ll_useragreement;
    private TextView tv_useragreement;


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
    private void initView() {
        //username
        et_username = (EditText) findViewById(R.id.et_username);
        //button next
        btn_username = (Button) findViewById(R.id.btn_username);
        ll_useragreement = (LinearLayout) findViewById(R.id.ll_useragreement);
        tv_useragreement = (TextView) findViewById(R.id.tv_useragreement);
        ll_useragreement.setVisibility(View.VISIBLE);
        tv_useragreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPush = new Intent(UsernameActivity.this, UserArgementActivity.class);
                startActivity(intentPush);
            }
        });
        btn_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMobileNO(et_username.getText().toString())) {
                    Toast.makeText(UsernameActivity.this, "请输入正确手机号", Toast.LENGTH_SHORT).show();
                } else {
                    CheckUser();
                }
            }
        });
        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_username.getText().toString().trim().length() != 0) {
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
     *
     * @param mobiles
     * @return
     */
    public boolean isMobileNO(String mobiles) {

        Pattern p = Pattern .compile("^(0|86|17951)?(13[0-9]|15[012356789]|17[6780]|18[0-9]|14[57])[0-9]{8}$");

        Matcher m = p.matcher(mobiles);

        return m.matches();

    }


    /**
     * service to judge if a user exist
     */
    interface IsUserExistService {

        @GET("/api/v3/communities/{communityId}/users/{phoneno}/exsit")
        void isUserExist(@Path("communityId") int communityId, @Path("phoneno") String phoneno, Callback<CommonRespBean<Boolean>> cb);
    }

    /**
     * check if user valid
     */
    private void CheckUser() {
        mLdDialog.show();
        IsUserExistService isUserExistService = RetrofitFactory.getInstance().create(getmContext(),IsUserExistService.class);
        Callback<CommonRespBean<Boolean>> callback = new Callback<CommonRespBean<Boolean>>() {
            @Override
            public void success(CommonRespBean<Boolean> commonPostResultBean, Response response) {
                mLdDialog.dismiss();
                if (commonPostResultBean.getStatus().equals("yes")) {
                    if (commonPostResultBean.getData()) {
                        Toast.makeText(UsernameActivity.this, "该用户已存在", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intentPush = new Intent();
                        String username = et_username.getText() + "";
                        intentPush.putExtra(Config.LoginUserName, username);
                        storeLocation(username);
                        intentPush.setClass(UsernameActivity.this, VerificationActivity.class);
                        startActivityForResult(intentPush, 1);
                    }
                } else {
                    showNetErrorToast();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                showNetErrorToast();
            }
        };

        // UserBean userBean = new UserBean();
        // userBean.setUsername(et_username.getText().toString());
        isUserExistService.isUserExist(PreferencesUtil.getCommityId(this), et_username.getText().toString(), callback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    private void storeLocation(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences("xj", this.MODE_PRIVATE);
        //get editor
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.commit();//提交修改
    }

}
