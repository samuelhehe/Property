package xj.property.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.area.RoomSelectActivity;
import xj.property.beans.UserInfoDetailBean;
import xj.property.utils.other.PreferencesUtil;

/**
 * 个性签名
 * Created by n on 2015/4/7.
 */
public class UserAreaActivity extends HXBaseActivity {


    private UserInfoDetailBean bean;

    /**
     * request code
     */
    public static final int requestCode_city = 1;
    public static final int requestCode_community = 2;

    /**
     * textview of city
     */
    private TextView tv_city;
    /**
     * textview of community
     */
    private TextView tv_community;
    /**
     * button to select community
     */
    private Button btn_select_community;

    /**
     * relative layout for city and community
     */
    private RelativeLayout rl_city;
    private RelativeLayout rl_community;

    /**
     * city id
     */
    private int cityId = 0;

    private String cityName;

    private int communityId;

    private String communityName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userrg_area);
        bean= PreferencesUtil.getLoginInfo(this);
        initTitle(null, "小区信息", null);
        initView();
    }

    @Override
    public void onClick(View v) {
    }


    private void initView(){
        tv_city = (TextView)findViewById(R.id.tv_city);
        tv_community = (TextView)findViewById(R.id.tv_community);
        btn_select_community = (Button)findViewById(R.id.btn_select_community);
        rl_city = (RelativeLayout)findViewById(R.id.rl_city);
        rl_community = (RelativeLayout)findViewById(R.id.rl_community);
//        SharedPreferences sharedPreferences = getSharedPreferences("xj", MODE_PRIVATE);
        tv_city.setText(PreferencesUtil.getCommityName(this));

        btn_select_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intentPush = getIntent();
                    intentPush.setClass(UserAreaActivity.this, RoomSelectActivity.class);
                    startActivityForResult(intentPush, 1);

            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RESULT_OK==resultCode)
        {    setResult(resultCode);
            finish();}
    }



}
