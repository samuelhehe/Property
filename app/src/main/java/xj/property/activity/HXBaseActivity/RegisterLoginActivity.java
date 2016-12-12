package xj.property.activity.HXBaseActivity;

import android.app.Activity;
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

import xj.property.R;
import xj.property.XjApplication;
import xj.property.activity.user.ForgetUsernameActivity;
import xj.property.activity.user.UsernameActivity;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

/**
 * Created by n on 2015/4/29.
 */
public class RegisterLoginActivity extends Activity {
    Button btn_register_now;
    TextView tv_cancle;
    TextView tv_have_an_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_dialog_login);

        Button btn_register_now = (Button) findViewById(R.id.btn_register_now);
        TextView tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        TextView tv_have_an_account = (TextView) findViewById(R.id.tv_have_an_account);

        tv_have_an_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterLoginActivity.this, LoginDialogActivity.class);
                startActivity(intent);
                finish();
            }
        });


        btn_register_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterLoginActivity.this, UsernameActivity.class);
                startActivity(intent);
                finish();

            }
        });


        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}