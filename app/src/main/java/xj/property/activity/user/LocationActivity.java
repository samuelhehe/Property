package xj.property.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import xj.property.R;
import xj.property.activity.HXBaseActivity.AddressActivity;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.utils.XJPushManager;

/**
 * Created by asia on 2016/1/13.
 */
public class LocationActivity extends HXBaseActivity {

    private String TAG = "LocationActivity";

    private Button btn_location_code;
    private EditText group_search_key_et;
    private Button go_verfication_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        XJPushManager xjPush = new XJPushManager(getApplicationContext());
        xjPush.unregisterLoginedPushService();
        xjPush.unInitPushService();
        btn_location_code = (Button) findViewById(R.id.btn_location_code);
        btn_location_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LocationActivity.this, LocationCodeActivity.class));
            }
        });
        initView();
    }

    private void initView() {
        group_search_key_et = (EditText) findViewById(R.id.group_search_key_et);
        group_search_key_et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(LocationActivity.this, AddressActivity.class);
                startActivity(intent);
                finish();
                return false;
            }
        });

//        group_search_key_et.setOnClickListener(this);
        go_verfication_btn = (Button) findViewById(R.id.go_verfication_btn);
        go_verfication_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go_verfication_btn:
                Intent intent1 = new Intent(LocationActivity.this, AddressActivity.class);
                startActivity(intent1);
                finish();
                break;

            case R.id.group_search_key_et:
                Intent intent = new Intent(LocationActivity.this, AddressActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        XJPushManager xjPush = new XJPushManager(getApplicationContext());
        xjPush.initPushService();
        xjPush.registerLoginedPushService();
    }
}
