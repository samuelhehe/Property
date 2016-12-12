package xj.property.activity.membership;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.math.BigDecimal;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.activity.surrounding.ShowAddressActivity;
import xj.property.activity.user.FixUserAddressConfrimDialog;
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.beans.MSPCardBean;
import xj.property.beans.MspCardDiscountBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.other.Arith;
import xj.property.utils.other.Config;
import xj.property.utils.other.MemberShipShareUtil;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

/**
 * 会员卡
 */
public class ActivityMShipCardDisCount extends HXBaseActivity {

    private TextView tv_title;

    private ImageView msp_card_bg_iv;

    private TextView msp_card_shop_name_tv;

    private RatingBar msp_card_discount_rb;

    private TextView msp_card_total_tv;

    private TextView msp_card_discount_tv;

    private TextView msp_card_distance_tv;

    private EditText input_consume_rmb_et;

    private Button go_buy_btn;

    private ImageView msp_shop_pic_iv;


    private TextView msp_shop_name_tv;

    private TextView msp_shop_score_tv;

    private TextView msp_shop_total_tv;

    private RatingBar msp_shop_star_rb;

    private LinearLayout panic_has_purchase_llay;


    private TextView ongoing_time;

    private TextView phone;

    private LinearLayout ll_phone;

    private TextView address;

    private TextView tv_distance;

    private com.baidu.mapapi.map.MapView bmapsView;

//    private ImageView panic_buying_seemore_iv;

    private BaiduMap mBaiduMap;
    private GeoCoder mSearch;
    private GeoCoder mSearch2;

    private LinearLayout welfare_purchase_hasgoturs_lv;
    private int screenWidth; /// 屏幕宽

    private MSPCardBean.MSPCardDetailBean mspcardBean;

    private int PayCodeOnline = 0;
    private double longitude;
    private double latitude;
    private double commlongitude;
    private double commlatitude;

    private TextView msp_card_rating_star_tv;

    private TextView msp_card_ftxt_logo_tv;
    private UserInfoDetailBean bean;
    private ScrollView scroll_root;

    /// 金额输入总布局
    private LinearLayout msp_input_consume_llay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_msp_card_discount);
        mspcardBean = (MSPCardBean.MSPCardDetailBean) getIntent().getSerializableExtra("MSPCardBeanShopInfo");
        bean = PreferencesUtil.getLoginInfo(this);

        initView();
        initData();

    }

    /**
     * 初始化数据
     */
    private void initData() {

        mSearch = GeoCoder.newInstance();
        mSearch2 = GeoCoder.newInstance();
        mSearch2.setOnGetGeoCodeResultListener(new myOnGetGeoCoderResultListener());
        mSearch.setOnGetGeoCodeResultListener(new myOnGetGeoCoderResultListenerHome());

        getMspCardListInfo(mspcardBean.getNearbyVipcardId());
    }


    @Override
    protected void onResume() {
        super.onResume();
//        showMspNotice();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        if (fFixUserAddressConfrimDialog != null) {
            fFixUserAddressConfrimDialog.dismiss();
        }

    }

    public void showMspNotice() {

        showPopWindow2();
        CountDownTimer countDownTimer = new CountDownTimer(2 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {

                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        };
        countDownTimer.start();
    }


    final PopupWindow popupWindow = new PopupWindow();

    private void showPopWindow2() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mMenuView = inflater.inflate(R.layout.msp_card_discount_notice, null);

        // 设置按钮监听
        // 设置SelectPicPopupWindow的View
        popupWindow.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
//        this.setFocusable(true);

        // 设置SelectPicPopupWindow弹出窗体动画效果
//          popupWindow.setAnimationStyle(R.style.AnimTop3);
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1f,
                Animation.RELATIVE_TO_SELF, 0f
        );
        translateAnimation.setDuration(1000);
        translateAnimation.setFillAfter(true);
        ((TextView) mMenuView.findViewById(R.id.msp_pay_msg_tv)).setAnimation(translateAnimation);
        translateAnimation.start();

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // ,Animation.RELATIVE_TO_SELF,10f,Animation.RELATIVE_TO_SELF,10f
                if (popupWindow != null && !popupWindow.isShowing()) {
                    popupWindow.showAsDropDown(findViewById(R.id.headtop_title));
                }
                //popupWindow.showAtLocation(findViewById(R.id.list),Gravity.NO_GRAVITY,0,0);
            }
        }, 200);
    }


    private void initView() {
        initTitle();

        screenWidth = getWindowManager().getDefaultDisplay().getWidth();

        scroll_root = (ScrollView) this.findViewById(R.id.scroll_root);
        /// 会员卡背景
        msp_card_bg_iv = (ImageView) this.findViewById(R.id.msp_card_bg_iv);
        /// 会员卡店名
        msp_card_shop_name_tv = (TextView) this.findViewById(R.id.msp_card_shop_name_tv);
        //// 会员卡星级
        msp_card_discount_rb = (RatingBar) this.findViewById(R.id.msp_card_discount_rb);
        msp_card_discount_rb.setEnabled(false);
        /// 会员卡店的单数
        msp_card_total_tv = (TextView) this.findViewById(R.id.msp_card_total_tv);
        /// 会员卡折扣
        msp_card_discount_tv = (TextView) this.findViewById(R.id.msp_card_discount_tv);
        /// 会员卡店铺距离
        msp_card_distance_tv = (TextView) this.findViewById(R.id.msp_card_distance_tv);
        /// 分数
        msp_card_rating_star_tv = (TextView) this.findViewById(R.id.msp_card_rating_star_tv);
        msp_card_ftxt_logo_tv = (TextView) this.findViewById(R.id.msp_card_ftxt_logo_tv);


        /// 消费金额 输入
        input_consume_rmb_et = (EditText) this.findViewById(R.id.input_consume_rmb_et);

        msp_input_consume_llay = (LinearLayout) this.findViewById(R.id.msp_input_consume_llay);
        msp_input_consume_llay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (input_consume_rmb_et != null) {
                    input_consume_rmb_et.setFocusable(true);
                    input_consume_rmb_et.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });


        /// 买单
        go_buy_btn = (Button) this.findViewById(R.id.go_buy_btn);
        go_buy_btn.setOnClickListener(this);

        /// 店铺门面图片
        msp_shop_pic_iv = (ImageView) this.findViewById(R.id.msp_shop_pic_iv);
        //// 店铺名称
        msp_shop_name_tv = (TextView) this.findViewById(R.id.msp_shop_name_tv);
        /// 店铺星级
        msp_shop_star_rb = (RatingBar) this.findViewById(R.id.msp_shop_star_rb);
        msp_shop_star_rb.setEnabled(false);
        /// 星级评分
        msp_shop_score_tv = (TextView) this.findViewById(R.id.msp_shop_score_tv);
        /// 店铺单数
        msp_shop_total_tv = (TextView) this.findViewById(R.id.msp_shop_total_tv);
        /// 已经购买人数
        panic_has_purchase_llay = (LinearLayout) this.findViewById(R.id.panic_has_purchase_llay);
        /// 查看更多购买用户
//        panic_buying_seemore_iv  = (ImageView)this.findViewById(R.id.panic_buying_seemore_iv);
//        panic_buying_seemore_iv.setOnClickListener(this);

        /// 已购买用户横向view
        welfare_purchase_hasgoturs_lv = (LinearLayout) this.findViewById(R.id.welfare_purchase_hasgoturs_lv);

        /// 店铺营业时间
        ongoing_time = (TextView) this.findViewById(R.id.ongoing_time);
        /// 店铺联系电话
        phone = (TextView) this.findViewById(R.id.phone);
        /// 拨打电话
        ll_phone = (LinearLayout) this.findViewById(R.id.ll_phone);
        ll_phone.setOnClickListener(this);
        /// 店铺地址
        address = (TextView) this.findViewById(R.id.address);
        /// 店铺距离
        tv_distance = (TextView) this.findViewById(R.id.tv_distance);
//        //// 百度地图
        bmapsView = (com.baidu.mapapi.map.MapView) this.findViewById(R.id.bmapsView);
        bmapsView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    scroll_root.requestDisallowInterceptTouchEvent(false);
                } else {

                    scroll_root.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        mBaiduMap = bmapsView.getMap();

    }

    private void initTitle() {

//        tv_right_text = (TextView) this.findViewById(R.id.tv_right_text);
//        tv_right_text.setText("往期账单");
//        tv_right_text.setVisibility(View.VISIBLE);
//        tv_right_text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /// 跳转至往期账单
//                startActivity(new Intent(mContext,ActivityHistoryConsumeList.class));
//            }
//        });

        this.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tv_right_text = (TextView) this.findViewById(R.id.tv_right_text);
        tv_right_text.setVisibility(View.VISIBLE);
        tv_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getmContext(), ActivityDiscountSpecification.class));

            }
        });

        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText(mspcardBean.getShopName());

    }


    private FixUserAddressConfrimDialog fFixUserAddressConfrimDialog;


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.go_buy_btn:

                if (PreferencesUtil.getLogin(this)) {
                    bean = PreferencesUtil.getLoginInfo(this);
                    //// 显示地址对话框 2015/11/17
//                    if (TextUtils.isEmpty(bean.getRoom()) || TextUtils.isEmpty(bean.getUserFloor())) {
//                        fFixUserAddressConfrimDialog = new FixUserAddressConfrimDialog(this);
//                        fFixUserAddressConfrimDialog.show();
//                        return;
//                    }

                } else {
                    Intent intent = new Intent(this, RegisterLoginActivity.class);
                    startActivity(intent);
                    return;
                }

                //// 买单
                goBuy();
                break;

        }
    }

    private void goBuy() {
        if (bean == null) {
            Intent intent = new Intent(getmContext(), RegisterLoginActivity.class);
            startActivity(intent);
        }
        if (input_consume_rmb_et != null) {
            String inputRMB = input_consume_rmb_et.getText().toString().trim();
            if (TextUtils.isEmpty(inputRMB)) {
                showToast("请输入金额数");
            } else {
                if (isNum(inputRMB)) {
                    if (Double.valueOf(inputRMB) < 0.1) {
                        showToast("请输入金额不能低于0.1元");
                        return;
                    }
                    if (mspcardBean != null) {
                        Intent intent = new Intent(getmContext(), ActivityMShipCardPay.class).putExtra("MSPCardBeanShopInfo", mspcardBean);
                        intent.putExtra("inputRMB", inputRMB);
                        //// 帮帮币个数, 订单编号等等.金额数
                        startActivityForResult(intent, PayCodeOnline);
                    }
                } else {
                    showToast("请输入金额数");
                }
            }
        }

    }

    public static boolean isNum(String str) {
        try {
            if (str.contains("-")) {
                return false;
            }
            BigDecimal bigDecimal = new BigDecimal(str);
            BigDecimal bigDecimal1 = new BigDecimal(Double.valueOf(0.0));
            int compreResult = bigDecimal.compareTo(bigDecimal1);
            if (compreResult != 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PayCodeOnline) {
            if (resultCode == RESULT_OK) {

                //// 支付成功
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        handler.sendEmptyMessage(1);
//                        try {
//                            Thread.sleep(3000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        handler.sendEmptyMessage(2);
//                    }
//                }).start();


                //// 支付成功
//                WelfareUtil.showWelfareShareDialog(this, "" + info.getWelfareId());
//                WelfareUtil.create600Message(this, emobId, info.getTitle(), orderid, "" + info.getWelfareId(), info.getPoster());

                String shopVipcardId = data.getStringExtra("ShopVipcardId");
                String inputrmb = data.getStringExtra("inputrmb");

                double discountpreice = Double.valueOf(inputrmb) * mspcardBean.getDiscount();

//                showToast("RESULT_OK  ShopVipcardId  " +discountpreice + shopVipcardId  + inputrmb);

                MemberShipShareUtil.showWelfareShareDialog(this, "" + shopVipcardId, (float) Arith.round(discountpreice, 2), mspcardBean.getEmobId());

            } else if (resultCode == RESULT_CANCELED) {
                //// 支付取消或者支付结果正在等待
//                showToast("RESULT_CANCELED");
            }
        }
    }

    interface MspCardListService {

        ///api/v3/nearbyVipcards/{会员卡ID}

//        @GET("/api/v1/shopVipcards/{shopVipcardId}")
//        void getShopVipcards(@Path("shopVipcardId") int shopVipcardId, Callback<MspCardDiscountBean> cb);
//        @GET("/api/v1/shopVipcards/{shopVipcardId}")


        @GET("/api/v3/nearbyVipcards/{shopVipcardId}")
        void getShopVipcards(@Path("shopVipcardId") int shopVipcardId, Callback<CommonRespBean<MspCardDiscountBean>> cb);
    }

    private void getMspCardListInfo(int shopVipcardId) {
        if (mLdDialog != null && !mLdDialog.isShowing()) {
            mLdDialog.show();
        }
        MspCardListService service = RetrofitFactory.getInstance().create(getmContext(),MspCardListService.class);
        Callback<CommonRespBean<MspCardDiscountBean>> callback = new Callback<CommonRespBean<MspCardDiscountBean>>() {
            @Override
            public void success(CommonRespBean<MspCardDiscountBean> bean, retrofit.client.Response response) {
                if (bean != null) {
                    if ("yes".equals(bean.getStatus())) {

//                        infoEntity = bean.getInfo();
                        mspcardBean.setShoppic(bean.getData().getShopLogo());

                        /// 加载会员卡信息
                        loadMspCardInfo(bean.getData());

                        /// 加载会员店铺的信息
                        loadMspShopInfo(bean.getData());

                        /// 加载地图导航部分
                        loadBaiduMapinfo(bean.getData());

                        /// 加载已经购买过的用户
                        loadingGoodsHasGotursHeadImgs4(bean.getData());

                    } else {
                        showToast(bean.getMessage());
                    }
                } else {
                    showToast("数据异常");
                }
                if (mLdDialog != null) {
                    mLdDialog.dismiss();
                }

            }


            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                showToast("数据异常");
                if (mLdDialog != null) {
                    mLdDialog.dismiss();
                }
            }
        };
        service.getShopVipcards(shopVipcardId, callback);
    }

    private void loadBaiduMapinfo(MspCardDiscountBean bean) {

        longitude = bean.getLongitude();

        latitude = bean.getLatitude();


        commlongitude = bean.getCommunityLongitude();
        commlatitude = bean.getCommunityLatitude();


        initMap(latitude, longitude);
        LatLng ptCenter = new LatLng(latitude, longitude);
        // 反Geo搜索
        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(ptCenter));

        LatLng ptCenter2 = new LatLng(commlatitude, commlongitude);
        //   反Geo搜索
        mSearch2.reverseGeoCode(new ReverseGeoCodeOption()
                .location(ptCenter2));


//        ll_address.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent addRessIntent = new Intent(getmContext(), ShowAddressActivity.class);
//                addRessIntent.putExtra("longitude", singlePanicBuyingBean.getInfo().getShop().getLongitude());
//                addRessIntent.putExtra("latitude", singlePanicBuyingBean.getInfo().getShop().getLatitude());
//                addRessIntent.putExtra("address", singlePanicBuyingBean.getInfo().getShop().getAddress());
//                addRessIntent.putExtra("businessStartTime", singlePanicBuyingBean.getInfo().getShop().getBusinessStartTime());
//                addRessIntent.putExtra("businessEndTime", singlePanicBuyingBean.getInfo().getShop().getBusinessEndTime());
//                addRessIntent.putExtra("shopName", singlePanicBuyingBean.getInfo().getShop().getShopName());
//                addRessIntent.putExtra("commlongitude", singlePanicBuyingBean.getInfo().getCommunityInfo().getLongitude());
//                addRessIntent.putExtra("commlatitude", singlePanicBuyingBean.getInfo().getCommunityInfo().getLatitude());
////                                startActivity(addRessIntent);
//            }
//        });

    }

    private void loadMspShopInfo(final MspCardDiscountBean bean) {

        ImageLoader.getInstance().displayImage(bean.getShopLogo(), msp_shop_pic_iv);

        msp_shop_name_tv.setText(bean.getShopName());

        msp_shop_star_rb.setRating(Float.valueOf(String.valueOf(bean.getStar())));

        msp_shop_score_tv.setText("" + Arith.round(bean.getStar(), 1) + "分");

        msp_shop_total_tv.setText(bean.getOrderCount() + "单");

        ll_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneIntent = new Intent(
                        "android.intent.action.CALL", Uri.parse("tel:"
                        + bean.getPhone()));
                startActivity(phoneIntent);
            }
        });

        /// 店铺地址
        address = (TextView) this.findViewById(R.id.address);
        /// 店铺距离
        tv_distance = (TextView) this.findViewById(R.id.tv_distance);

        ongoing_time.setText("" + bean.getShopName() + "营业时间" + bean.getBusinessStartTime() + "-" + bean.getBusinessEndTime());
        phone.setText("" + bean.getPhone());

        address.setText("" + bean.getAddress());

        tv_distance.setText(bean.getDistance() + "m");

    }

    /**
     * 加载会员卡信息
     *
     * @param bean
     */
    private void loadMspCardInfo(MspCardDiscountBean bean) {


//        // 外部矩形弧度
        float[] outerR = new float[]{20, 20, 20, 20, 20, 20, 20, 20};
        // 内部矩形与外部矩形的距离
//            RectF inset = new RectF(10, 10, 10, 10);
        // 内部矩形弧度
        float[] innerRadii = new float[]{20, 20, 20, 20, 20, 20, 20, 20};

        RoundRectShape rr = new RoundRectShape(outerR, null, null);

        ShapeDrawable drawable = new ShapeDrawable(rr);

        //指定填充颜色
//            drawable.getPaint().setColor(Color.YELLOW);

        // 指定填充模式
        drawable.getPaint().setStyle(Paint.Style.FILL);

//            //指定填充颜色
        drawable.getPaint().setColor(Color.argb(255,
                Integer.valueOf(mspcardBean.getColorR()),
                Integer.valueOf(mspcardBean.getColorG()),
                Integer.valueOf(mspcardBean.getColorB())));

        msp_card_bg_iv.setBackground(drawable);
        msp_card_bg_iv.setVisibility(View.INVISIBLE);

        DisplayImageOptions msp_card_iv_options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(drawable)
                .showImageOnFail(drawable)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

        ImageLoader.getInstance().displayImage(mspcardBean.getPhoto(), msp_card_bg_iv, msp_card_iv_options, new MyImageLoadingListener(mspcardBean));

    }

    class MyImageLoadingListener implements ImageLoadingListener {

        private final MSPCardBean.MSPCardDetailBean bean;

        public MyImageLoadingListener(MSPCardBean.MSPCardDetailBean bean) {
            this.bean = bean;
        }

        @Override
        public void onLoadingStarted(String imageUri, View view) {
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            msp_card_discount_rb.setEnabled(false);
            msp_card_discount_rb.setRating(Float.valueOf(String.valueOf(bean.getStar())));

            msp_card_total_tv.setText(bean.getOrderCount() + "单");
            msp_card_discount_tv.setText("" + bean.getDiscount() + "折");
            msp_card_distance_tv.setText("" + bean.getDistance() + "m");
            msp_card_rating_star_tv.setText("" + Arith.round(bean.getStar(), 1) + "分");
            msp_card_shop_name_tv.setText("" + bean.getShopName());


            msp_card_bg_iv.setVisibility(View.VISIBLE);
            msp_card_discount_rb.setVisibility(View.VISIBLE);
            msp_card_total_tv.setVisibility(View.VISIBLE);
            msp_card_discount_tv.setVisibility(View.VISIBLE);
            msp_card_distance_tv.setVisibility(View.VISIBLE);
            msp_card_rating_star_tv.setVisibility(View.VISIBLE);
            msp_card_shop_name_tv.setVisibility(View.VISIBLE);
            msp_card_ftxt_logo_tv.setVisibility(View.VISIBLE);

        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

            msp_card_bg_iv.setBackground(null);

            msp_card_discount_rb.setEnabled(false);
            msp_card_discount_rb.setRating(Float.valueOf(String.valueOf(bean.getStar())));

            msp_card_total_tv.setText(bean.getOrderCount() + "单");
            msp_card_discount_tv.setText("" + bean.getDiscount() + "折");
            msp_card_distance_tv.setText("" + bean.getDistance() + "m");
            msp_card_rating_star_tv.setText("" + Arith.round(bean.getStar(), 1) + "分");
            msp_card_shop_name_tv.setText("" + bean.getShopName());

            msp_card_bg_iv.setVisibility(View.VISIBLE);
            msp_card_discount_rb.setVisibility(View.VISIBLE);
            msp_card_total_tv.setVisibility(View.VISIBLE);
            msp_card_discount_tv.setVisibility(View.INVISIBLE);
            msp_card_distance_tv.setVisibility(View.VISIBLE);
            msp_card_rating_star_tv.setVisibility(View.VISIBLE);
            msp_card_shop_name_tv.setVisibility(View.INVISIBLE);
            msp_card_ftxt_logo_tv.setVisibility(View.VISIBLE);


        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

        }
    }


    /**
     * 加载已经购买过的用户
     *
     * @param info
     */
    private void loadingGoodsHasGotursHeadImgs4(final MspCardDiscountBean info) {

        final List<MspCardDiscountBean.UsersEntity> users = info.getUsers();
        Log.i("debbug", "info.size" + info.getUsers().size());
        if (users.size() <= 0) {
            if (panic_has_purchase_llay != null) {
                panic_has_purchase_llay.setVisibility(View.GONE);

            }
        }

        if (users.size() > 6) {
            for (int i = 0; i < 5; i++) {
                LinearLayout usrHeadView = (LinearLayout) View.inflate(this, R.layout.common_msp_purchase_hasgoturs_headlay, null);
                ImageView img = (ImageView) usrHeadView.findViewById(R.id.iv_avatar);
                img.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(users.get(i).getAvatar(), img, UserUtils.options);
                usrHeadView.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams rlparams = (LinearLayout.LayoutParams) img.getLayoutParams();
                rlparams.width = screenWidth * 123 / 1080;
                rlparams.height = screenWidth * 123 / 1080;

                img.setLayoutParams(rlparams);

                final int finalI = i;
                usrHeadView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getmContext(), UserGroupInfoActivity.class).putExtra(Config.INTENT_PARMAS2, users.get(finalI).getEmobId()));
                    }
                });

//                LinearLayout.LayoutParams llayparams = (LinearLayout.LayoutParams) welfare_purchase_hasgoturs_lv.getLayoutParams();
//                welfare_purchase_hasgoturs_lv.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                welfare_purchase_hasgoturs_lv.addView(usrHeadView);

            }

            /// 添加一个查看更多用户按钮

            LinearLayout usrHeadView = (LinearLayout) View.inflate(this, R.layout.common_msp_purchase_hasgoturs_headlay, null);
            ImageView img = (ImageView) usrHeadView.findViewById(R.id.iv_avatar);
            img.setVisibility(View.VISIBLE);

            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.msp_card_buyed_see_more, img);
            usrHeadView.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams rlparams = (LinearLayout.LayoutParams) img.getLayoutParams();

            rlparams.width = screenWidth * 123 / 1080;
            rlparams.height = screenWidth * 123 / 1080;

            img.setLayoutParams(rlparams);

            usrHeadView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ///
                    startActivity(new Intent(getmContext(), ActivityMSPBuyedMoreUsers.class).putExtra("shopvipcardid", mspcardBean.getEmobId()));

                }
            });
//
//            LinearLayout.LayoutParams llayparams = (LinearLayout.LayoutParams) welfare_purchase_hasgoturs_lv.getLayoutParams();
//            welfare_purchase_hasgoturs_lv.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            welfare_purchase_hasgoturs_lv.addView(usrHeadView);
        }

        if (users.size() > 0 && users.size() <= 6) {

            for (int i = 0; i < users.size(); i++) {

                LinearLayout usrHeadView = (LinearLayout) View.inflate(this, R.layout.common_msp_purchase_hasgoturs_headlay, null);
                ImageView img = (ImageView) usrHeadView.findViewById(R.id.iv_avatar);
                img.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(users.get(i).getAvatar(), img, UserUtils.options);
                usrHeadView.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams rlparams = (LinearLayout.LayoutParams) img.getLayoutParams();

                rlparams.width = screenWidth * 123 / 1080;
                rlparams.height = screenWidth * 123 / 1080;

                img.setLayoutParams(rlparams);


                final int finalI = i;
                usrHeadView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getmContext(), UserGroupInfoActivity.class).putExtra(Config.INTENT_PARMAS2, users.get(finalI).getEmobId()));
                    }
                });

//                LinearLayout.LayoutParams llayparams = (LinearLayout.LayoutParams) welfare_purchase_hasgoturs_lv.getLayoutParams();
//                welfare_purchase_hasgoturs_lv.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                welfare_purchase_hasgoturs_lv.addView(usrHeadView);

            }
        }
    }


    private class myOnGetGeoCoderResultListener implements OnGetGeoCoderResultListener {

        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(getmContext(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
                        .show();
                return;
            }
//        mBaiduMap.clear();
//        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
//                .icon(BitmapDescriptorFactory
//                        .fromResource(R.drawable.icon_marka)));
            Log.i("debbug", "result=" + result.getLocation().toString());
            Log.i("debbug", "address=" + result.getAddress());
            View frameLayout = View.inflate(getmContext(), R.layout.map_marker_view, null);
            TextView t1 = (TextView) frameLayout.findViewById(R.id.tv_txt);
            t1.setText(PreferencesUtil.getCommityName(getmContext()));

            OverlayOptions ooA = new MarkerOptions().position(result.getLocation()).icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_marka_green))
                    .zIndex(6).draggable(true);
            OverlayOptions ooA2 = new MarkerOptions().position(result.getLocation()).icon(BitmapDescriptorFactory
                    .fromView(t1))
                    .zIndex(5).draggable(true)
                    .anchor(0f, 0f);
            mBaiduMap.addOverlay(ooA);
            mBaiduMap.addOverlay(ooA2);
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                    .getLocation()));
//        Toast.makeText(this, result.getAddress(),
//                Toast.LENGTH_LONG).show();
        }
    }

    private class myOnGetGeoCoderResultListenerHome implements OnGetGeoCoderResultListener {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(getmContext(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
                        .show();
                return;
            }
            mBaiduMap.clear();
            mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_marka)));
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                    .getLocation()));
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(getmContext(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
                        .show();
                return;
            }
//        mBaiduMap.clear();
//        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
//                .icon(BitmapDescriptorFactory
//                        .fromResource(R.drawable.icon_marka)));
//            initMap(result.getLocation().latitude,result.getLocation().longitude);
            Log.i("debbug", "result=" + result.getLocation().toString());
            Log.i("debbug", "address=" + result.getAddress());
            View frameLayout = View.inflate(getmContext(), R.layout.map_marker_view, null);
            TextView t1 = (TextView) frameLayout.findViewById(R.id.tv_txt);
            t1.setText(mspcardBean.getShopName());

            OverlayOptions ooA = new MarkerOptions().position(result.getLocation()).icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_marka_red))
                    .zIndex(4).draggable(true);
            OverlayOptions ooA2 = new MarkerOptions().position(result.getLocation()).icon(BitmapDescriptorFactory
                    .fromView(t1))
                    .zIndex(3).draggable(true)
                    .anchor(0f, 0f);
            mBaiduMap.addOverlay(ooA);
            mBaiduMap.addOverlay(ooA2);
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                    .getLocation()));
//        Toast.makeText(this, result.getAddress(),
//                Toast.LENGTH_LONG).show();
        }
    }

    //初始化地图
    private void initMap(double latitude, double longitude) {

        // 普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        LatLng cenpt = new LatLng(latitude, longitude);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);

//        PoiInfo info = new PoiInfo();
//        info.location = new LatLng(latitude,longitude);
//        LatLng llA =info.location;
//        CoordinateConverter converter= new CoordinateConverter();
//        converter.coord(llA);
//        converter.from(CoordinateConverter.CoordType.COMMON);
//        LatLng convertLatLng = converter.convert();
//
//        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 17.0f);
//        mBaiduMap.animateMapStatus(u);
    }

}
