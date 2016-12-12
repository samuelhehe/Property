package xj.property.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.area.CityActivity;
import xj.property.activity.area.CommunityActivity;
import xj.property.beans.UserInfoDetailBean;
import xj.property.beans.UserMessageBean;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

/**
 * 个性签名
 * Created by n on 2015/4/7.
 */
public class FixUserAreaActivity extends HXBaseActivity {


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
        setContentView(R.layout.activity_area);
        bean= PreferencesUtil.getLoginInfo(this);
        initTitle(null, "修改小区", null);
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

        rl_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPush = getIntent();
                intentPush.setClass(FixUserAreaActivity.this,CityActivity.class);
                startActivityForResult(intentPush, requestCode_city);
            }
        });

        rl_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cityId == 0){
                    Toast.makeText(FixUserAreaActivity.this,"请选择城市",Toast.LENGTH_SHORT).show();
                }else{

                    Intent intentPush = new Intent();
                    intentPush.setClass(FixUserAreaActivity.this,CommunityActivity.class);
                    intentPush.putExtra("cityId",cityId);
                    startActivityForResult(intentPush, requestCode_community);
                }
            }
        });

        btn_select_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(communityId == 0){
                    Toast.makeText(FixUserAreaActivity.this,"请选择城小区",Toast.LENGTH_SHORT).show();
                }else{


                    //Intent intentPush = getIntent();
                    Intent intentPush = new Intent(FixUserAreaActivity.this, FixRoomSelectActivity.class);
                  intentPush.setClass(FixUserAreaActivity.this, FixRoomSelectActivity.class);
//                    System.out.println("城市信息："+cityId+"  "+communityId);
//                    intentPush.putExtra("cityId",cityId);
//                    intentPush.putExtra("communityId",communityId);
                    //startActivityForResult(intentPush, 190);
                    startActivity(intentPush);
                    finish();

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
                UserInfoDetailBean bean= PreferencesUtil.getLoginInfo(FixUserAreaActivity.this);
                bean.setCommunityId(communityId);
                PreferencesUtil.saveLogin(FixUserAreaActivity.this,bean);
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
        //get editor
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("cityId",cityId);
        editor.putString("cityName", cityName);

        editor.putInt("communityId",communityId);
        editor.putString("communityName", communityName);

        //set bool flag
        editor.putBoolean("isCommunitySet",true);

        editor.commit();//提交修改
    }

    private void getLocation(){
        SharedPreferences sharedPreferences = getSharedPreferences("xj", this.MODE_PRIVATE);
        int cityId = sharedPreferences.getInt("cityId",0);
        String cityName = sharedPreferences.getString("cityName","");
    }









    /**
     * 获取XJuser部分
     */
//    interface XJUserService {
//        ///api/v1/communities/{communityId}/users/{userId}
//        @PUT("/api/v1/communities/{communityId}/users/{userName}")
//        void updateUserInfo(@Body UpdateUserSignatureRequest request, @Path("communityId") long communityId, @Path("userName") String userName, Callback<XJUserInfoBean> cb);
//    }
//
//    private void fixUserInfo() {
//        mLdDialog.show();
//        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
//        XJUserService service = restAdapter.create(XJUserService.class);
//        Callback<XJUserInfoBean> callback = new Callback<XJUserInfoBean>() {
//            @Override
//            public void success(XJUserInfoBean bean, retrofit.client.Response response) {
//                mLdDialog.dismiss();
//                showToast("修改成功");
//                //getUserMessage();
//                finish();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                mLdDialog.dismiss();
//                error.printStackTrace();
//                Toast.makeText(FixUserAreaActivity.this, error.toString(), Toast.LENGTH_LONG).show();
//            }
//        };
////        UpdateUserSignatureRequest request = new UpdateUserSignatureRequest();
////        request.setSignature(name);
////
////        bean.setSignature(name);
////        PreferencesUtil.saveLogin(this,bean);
//        service.updateUserInfo(null,bean.getCommunityId() ,bean.getUsername(), callback);
//    }


    /**
     * 获取UserMessageBean部分
     */
    interface UserMessageService {
        ///api/v1/communities/{communityId}/users/{userId}
        /// /{baseUrl}/api/v1/communities/{communityId}/users/{emobId}
        ///http://114.215.105.202:8080/api/v1/communities/1/users/byEmobId/123
        @GET("/api/v1/communities/{communityId}/users/{emobId}")
        void getUserMessageInfo(@Path("communityId") int communityId, @Path("emobId") String emobId, Callback<UserMessageBean> cb);
    }

    private void getUserMessage() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        UserMessageService service = restAdapter.create(UserMessageService.class);
        Callback<UserMessageBean> callback = new Callback<UserMessageBean>() {
            @Override
            public void success(UserMessageBean bean, retrofit.client.Response response) {

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        };
        service.getUserMessageInfo(PreferencesUtil.getCommityId(this), bean.getEmobId(), callback);
    }



}
