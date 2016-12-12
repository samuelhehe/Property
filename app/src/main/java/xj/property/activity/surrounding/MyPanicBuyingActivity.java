package xj.property.activity.surrounding;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.MyPanicBuyingDetailBean;
import xj.property.utils.QRCodeUtil;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;
import xj.property.widget.LoadingDialog;

public class MyPanicBuyingActivity extends HXBaseActivity {

    private ImageView iv_back, iv_qr_code, img;
    private TextView  tv_panic_buying_code, tv_title_of_panic_buying, tv_contents_of_panic_buying, rest_time, viewed_count, ongoing_time, phone, address,tv_distance;
    private LinearLayout ll_phone, ll_address;
    private LoadingDialog loadingDialog;
    private String code;
    private int screenWidth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_panic_buying);
        initView();
        initData();
    }

    private void initView() {
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        loadingDialog = new LoadingDialog(MyPanicBuyingActivity.this);
        code = getIntent().getStringExtra("code");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_qr_code = (ImageView) findViewById(R.id.iv_qr_code);
        img = (ImageView) findViewById(R.id.img);
        tv_panic_buying_code = (TextView) findViewById(R.id.tv_panic_buying_code);
        tv_title_of_panic_buying = (TextView) findViewById(R.id.tv_title_of_panic_buying);
        tv_contents_of_panic_buying = (TextView) findViewById(R.id.tv_contents_of_panic_buying);
        rest_time = (TextView) findViewById(R.id.rest_time);
        viewed_count = (TextView) findViewById(R.id.viewed_count);
        ongoing_time = (TextView) findViewById(R.id.ongoing_time);
        phone = (TextView) findViewById(R.id.phone);
        address = (TextView) findViewById(R.id.address);
        ll_phone = (LinearLayout) findViewById(R.id.ll_phone);
        ll_address = (LinearLayout) findViewById(R.id.ll_address);
        tv_distance = (TextView) findViewById(R.id.tv_distance);

        TextPaint tp = tv_title_of_panic_buying.getPaint();
        tp.setFakeBoldText(true);

        RelativeLayout.LayoutParams rlparams = (RelativeLayout.LayoutParams) img.getLayoutParams();
        rlparams.width = screenWidth;
        rlparams.height = screenWidth*3/4;
        img.setLayoutParams(rlparams);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        getDetailInfo();
    }

    interface GetDetailInfoService {
        //        Path : http://bangbang.ixiaojian.com/api/v1/crazysales/communities/{communityId}/detail/{code}
//        HTTP Method : GET
        @GET("/api/v1/crazysales/communities/{communityId}/detail/{code}")
        void getInfoList(@Path("communityId") int communityId, @Path("code") String code, Callback<MyPanicBuyingDetailBean> callback);
    }

    private void getDetailInfo() {
        loadingDialog.show();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        GetDetailInfoService service = restAdapter.create(GetDetailInfoService.class);
        Callback<MyPanicBuyingDetailBean> callback = new Callback<MyPanicBuyingDetailBean>() {
            @Override
            public void success(final MyPanicBuyingDetailBean myPanicBuyingDetailBean, Response response) {
                loadingDialog.dismiss();
                if (myPanicBuyingDetailBean != null && "yes".equals(myPanicBuyingDetailBean.getStatus())) {
                    long restTime=myPanicBuyingDetailBean.getInfo().getEndTime()-(System.currentTimeMillis()/1000);
                    tv_panic_buying_code.setText("抢购码：" + code);
                    iv_qr_code.setImageBitmap(QRCodeUtil.generateCode(Long.parseLong(code)));
                    tv_title_of_panic_buying.setText(myPanicBuyingDetailBean.getInfo().getTitle() + "");
                    tv_contents_of_panic_buying.setText(myPanicBuyingDetailBean.getInfo().getDescr() + "");
                    rest_time.setText(formatDuring(restTime * 1000));//将毫秒值转换为天
                    for (int i=0 ; i<myPanicBuyingDetailBean.getInfo().getCrazySalesImg().size(); i++){
                        ImageLoader.getInstance().displayImage(myPanicBuyingDetailBean.getInfo().getCrazySalesImg().get(i).getImgUrl(), img, UserUtils.options);
                    }
                    viewed_count.setText("被浏览次数：" + myPanicBuyingDetailBean.getInfo().getViewCount());
                    ongoing_time.setText(myPanicBuyingDetailBean.getInfo().getShop().getShopName() + "营业时间：" + myPanicBuyingDetailBean.getInfo().getShop().getBusinessStartTime() + "-" + myPanicBuyingDetailBean.getInfo().getShop().getBusinessEndTime());
                    phone.setText(myPanicBuyingDetailBean.getInfo().getShop().getPhone() + "");
                    address.setText(myPanicBuyingDetailBean.getInfo().getShop().getAddress() + "");
                    tv_distance.setText("距离小区："+myPanicBuyingDetailBean.getInfo().getDistance()+"m");
                    ll_phone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent phoneIntent = new Intent(
                                    "android.intent.action.CALL", Uri.parse("tel:"
                                    + myPanicBuyingDetailBean.getInfo().getShop().getPhone()));
                            startActivity(phoneIntent);
                        }
                    });
                    ll_address.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent addRessIntent = new Intent(MyPanicBuyingActivity.this, ShowAddressActivity.class);
                            addRessIntent.putExtra("longitude", myPanicBuyingDetailBean.getInfo().getShop().getLongitude());
                            addRessIntent.putExtra("latitude", myPanicBuyingDetailBean.getInfo().getShop().getLatitude());
                            addRessIntent.putExtra("address", myPanicBuyingDetailBean.getInfo().getShop().getAddress());
                            addRessIntent.putExtra("businessStartTime", myPanicBuyingDetailBean.getInfo().getShop().getBusinessStartTime());
                            addRessIntent.putExtra("businessEndTime", myPanicBuyingDetailBean.getInfo().getShop().getBusinessEndTime());
                            addRessIntent.putExtra("shopName", myPanicBuyingDetailBean.getInfo().getShop().getShopName());
                            startActivity(addRessIntent);
                        }
                    });
                }
            }

            @Override
            public void failure(RetrofitError error) {
                loadingDialog.dismiss();
                showNetErrorToast();
            }
        };
        service.getInfoList(PreferencesUtil.getCommityId(MyPanicBuyingActivity.this), code, callback);
    }

    @Override
    public void onClick(View v) {
    }
    //倒计时
    public static String formatDuring(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        return days + " 天 " + hours + " 小时 ";
    }
}
