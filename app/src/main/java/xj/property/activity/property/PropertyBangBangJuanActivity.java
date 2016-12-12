package xj.property.activity.property;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.PropertyPayAllBean;
import xj.property.utils.other.Config;

/**
 * aurth:asia
 * 帮帮卷页面
 */
public class PropertyBangBangJuanActivity extends HXBaseActivity {

    //titile
    private RelativeLayout mHeaptop;
    private ImageView mIv_back;
    private TextView mTv_left_text;
    private TextView mTv_title;
    private TextView mTv_right_text;
    private ImageView mIv_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_bangbangjuan);
        initView();
        initData();
        initListenner();
    }

    private void initData() {
        mTv_title.setText("使用帮帮卷");
        mTv_right_text.setVisibility(View.VISIBLE);
        mTv_right_text.setText("使用说明");
    }

    private void initListenner() {

    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right_text:
                //TODO 跳入到历史账单页面
                Intent intentHistory = new Intent(PropertyBangBangJuanActivity.this,PropertyHistoryActivity.class);
                startActivity(intentHistory);
                break;
            case R.id.iv_back:
                finish();
                break;

        }
    }

    public void initView() {
        mHeaptop = (RelativeLayout) findViewById(R.id.heaptop);
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mTv_left_text = (TextView) findViewById(R.id.tv_left_text);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mTv_right_text = (TextView) findViewById(R.id.tv_right_text);
        mIv_right = (ImageView) findViewById(R.id.iv_right);

    }


    /**
     * 获取用户是否缴过费
     */
    interface ActIsPayed{
        @GET("/api/v1/communities/{communityId}/payment/user/{emobId}/noPaidPayment")
        void getIsPayed(@Path("communityId") int communityId, @Path("emobId") String emobId, Callback<PropertyPayAllBean> cb);
    }


    private void getIsPayed() {
        mLdDialog.show();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        ActIsPayed service = restAdapter.create(ActIsPayed.class);
        Callback<PropertyPayAllBean> callback = new Callback<PropertyPayAllBean>() {
            @Override
            public void success(PropertyPayAllBean bean, retrofit.client.Response response) {
                mLdDialog.dismiss();
                Log.d("PropertyActivity",response.toString());
                if (bean != null) {
                    if("yes".equals(bean.getStatus())&&bean.getInfo()!=null){
                        Intent intent = new Intent(PropertyBangBangJuanActivity.this,PropertyPayedActivity.class);
                        intent.putExtra("PropertyPayInfoBean",bean.getInfo());
                        startActivity(intent);
                        finish();
                    }else if("no".equals(bean.getStatus())){
                        Intent intent = new Intent(PropertyBangBangJuanActivity.this,PropertyDialogActivity.class);
                        startActivityForResult(intent,88);
                    }else {

                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("error PropertyActivity",error.toString());
                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
            }
        };

        //emobid 缴过费     dd9e5477aac52293cd23718945d31059
        //emobid 未缴过费   4b5a37edb806731640e3e86fdf22a85a

        service.getIsPayed(1, "4b5a37edb806731640e3e86fdf22a85a", callback);
    }

}
