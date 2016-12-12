package xj.property.activity.genius;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import xj.property.R;
import xj.property.activity.HXBaseActivity.LoginDialogActivity;
import xj.property.activity.user.UsernameActivity;

/**
 * Created by n on 2015/4/29.
 */
public class GeniusRegisterLoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genius_register);

        Button btn_register_now = (Button) findViewById(R.id.btn_register_now);
        TextView tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        TextView tv_have_an_account = (TextView) findViewById(R.id.tv_have_an_account);

        tv_have_an_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GeniusRegisterLoginActivity.this, LoginDialogActivity.class);
                startActivity(intent);
                finish();
            }
        });


        btn_register_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GeniusRegisterLoginActivity.this, UsernameActivity.class);
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