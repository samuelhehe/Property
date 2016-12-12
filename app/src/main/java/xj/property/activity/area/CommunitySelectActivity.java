package xj.property.activity.area;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;

public class CommunitySelectActivity extends HXBaseActivity {
    /**
     * logger
     */
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
        setContentView(R.layout.activity_area);
        initView();
    }

    /**
     * init view
     */
    private void initView(){
        tv_city = (TextView)findViewById(R.id.tv_city);
        tv_community = (TextView)findViewById(R.id.tv_community);
        btn_select_community = (Button)findViewById(R.id.btn_select_community);
        rl_city = (RelativeLayout)findViewById(R.id.rl_city);
        rl_community = (RelativeLayout)findViewById(R.id.rl_community);

        rl_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPush = getIntent();
                intentPush.setClass(CommunitySelectActivity.this,CityActivity.class);
                startActivityForResult(intentPush, requestCode_city);
            }
        });

        rl_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cityId == 0){
                    Toast.makeText(CommunitySelectActivity.this,"请选择城市",Toast.LENGTH_SHORT).show();
                }else{

                    Intent intentPush = new Intent();
                    intentPush.setClass(CommunitySelectActivity.this,CommunityActivity.class);
                    intentPush.putExtra("cityId",cityId);
                    startActivityForResult(intentPush, requestCode_community);
                }
            }
        });

        btn_select_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(communityId == 0){
                    Toast.makeText(CommunitySelectActivity.this,"请选择城小区",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intentPush = getIntent();
                    intentPush.setClass(CommunitySelectActivity.this, RoomSelectActivity.class);
                    startActivityForResult(intentPush, 190);
                }
            }
        });

        //get stored value
        SharedPreferences sharedPreferences = getSharedPreferences("xj", this.MODE_PRIVATE);
        if(!sharedPreferences.getString("cityName","").equals("")){
            tv_city.setText(sharedPreferences.getString("cityName",""));
            cityId = sharedPreferences.getInt("cityId",0);
        }
        if(!sharedPreferences.getString("communityName","").equals("")){
            tv_community.setText(sharedPreferences.getString("communityName",""));
            communityId = sharedPreferences.getInt("communityId",0);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("onion", "CommunitySelectActivity onDestroy");
    }

    @Override
    protected void onActivityResult(int requestCode ,int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("onion","CommunitySelectActivity onActivityResult");
if(requestCode==190)finish();
        if(resultCode == CityActivity.resultCode_city){
            if(data != null){
                Bundle bundle = data.getExtras();
                cityId = bundle.getInt("cityId");
                cityName = bundle.getString("cityName");
                tv_city.setText(cityName);
                storeLocation();
            }
        }else{
            if(data != null){
                Bundle bundle = data.getExtras();
                communityName = bundle.getString("communityName");
                communityId = bundle.getInt("communityId");
                tv_community.setText(communityName);

                storeLocation();
                getLocation();
            }
        }
    }

    /**
     * store city and community
     */
    private void storeLocation(){
        SharedPreferences sharedPreferences = getSharedPreferences("xj", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("cityId",cityId);
        editor.putString("cityName", cityName);
        editor.putInt("communityId",communityId);
        editor.putString("communityName", communityName);
        editor.putBoolean("isCommunitySet",true);
        editor.commit();//提交修改
    }

    private void getLocation(){
        SharedPreferences sharedPreferences = getSharedPreferences("xj", this.MODE_PRIVATE);
        int cityId = sharedPreferences.getInt("cityId",0);
        String cityName = sharedPreferences.getString("cityName","");
    }

    @Override
    public void onClick(View v) {

    }
}
