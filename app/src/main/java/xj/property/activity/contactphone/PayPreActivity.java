package xj.property.activity.contactphone;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.takeout.SuperMarketChatActivity;
import xj.property.activity.user.BonusToPayActivity;
import xj.property.adapter.PayGoodItemAdapter;
import xj.property.beans.BangBiPayReqBean;
import xj.property.beans.BangBiPayRespBean;
import xj.property.beans.Floor;
import xj.property.beans.OrderDetailBean;
import xj.property.beans.ShopOrderInfoReqBean;
import xj.property.beans.UserBonusBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.beans.WXOrderRequestBean;
import xj.property.beans.WXPostOrderInfoForBounsBean;
import xj.property.cache.OrderDetailModel;
import xj.property.event.WXPayRequestEvent;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseUtils;
import xj.property.netbase.RetrofitFactory;
import xj.property.netbasebean.BounsCoinInfoBean;
import xj.property.fragment.IndexFragment;
import xj.property.utils.DensityUtil;
import xj.property.utils.other.Arith;
import xj.property.utils.other.Config;
import xj.property.utils.other.HomeOwnerUtil;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.weixinpay.MD5;
import xj.property.widget.MyListView;
import xj.property.widget.wheelview.ArrayWheelAdapter;
import xj.property.widget.wheelview.OnWheelChangedListener;
import xj.property.widget.wheelview.WheelView;

/**
 * Created by n on 2015/4/23.
 */
public class PayPreActivity extends HXBaseActivity {
    private OrderDetailModel orderDetailModel;
    private String serial;
    private MyListView lv_goods;
    private TextView tv_goods_sum_price;
    private CheckBox ck_cashondelivery;
    private CheckBox ck_cashonline;
    private CheckBox rb_cashbyali;
    private CheckBox rb_cashbyweixin;
    private TextView tv_fix_address;
    private TextView tv_bangbangquan_price;
    private RelativeLayout rl_paybyarrivel, pay_byweixin;

    private TextView tv_sum_price;
    private LinearLayout btn_confirm_pay;
    private RelativeLayout rl_bonus;
    private TextView tv_bonus_count;
    private TextView tv_use_status;


    //    View mWheelLayout;
    private PopupWindow mPop;
    private View root_view;

    private UserBonusBean userBonusBean;
    private ArrayList<OrderDetailBean> list;
    private PayGoodItemAdapter adapter;

    private UserInfoDetailBean bean;

    private double subPrice = 0;

    int PayCodeOffline = 1;
    int PayCodeOnline = 0;

    private String emobIdShop;
    private String emobIdUser;

    private int selectedPosition = -1;
    private RelativeLayout rl_paybyonline;
    private RelativeLayout rl_paybyali;
    private String[] item_floors;
    private String[] item_unit;
    private String[] item_data_rooms;
    List<Floor> floors = new ArrayList<>();

    //微信支付
    PayReq req;
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    StringBuffer sb;
    /// 帮帮币兑换比率 默认 0.01
    private float bangbiExchange = 0.01f;
    //// 帮帮币数量 初始化0
    private int bangbiCount = 0;
    //// 帮币布局
    private RelativeLayout rl_bangbi;
    ///  帮币数量
    private TextView tv_bangbi_count;
    /// 使用帮币支付
    private CheckBox rb_cashbybangbi;
    /// 减价格的方式
    private TextView tv_subway_name_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypre);
        initTitle(null, "支付", "");
        EventBus.getDefault().register(this);
        serial = getIntent().getStringExtra(Config.EXPKey_serial);
        emobIdShop = getIntent().getStringExtra("emobIdShop");
        bean = PreferencesUtil.getLoginInfo(PayPreActivity.this);
        emobIdUser = bean.getEmobId();

        initView();
        initData();
        getActivities();

        /// 获取帮帮币数量以及显示状态
        getBangbiCountExchange();

        /// 2015/11/23
//        mWheelLayout = View.inflate(this, R.layout.wheel_choose_city_three, null);

        Log.i("onion", "emobIdShop:  " + emobIdShop + "   emobIdUser:  " + emobIdUser);

    }

    private void initData() {
        if (serial == null) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        orderDetailModel = new Select().from(OrderDetailModel.class).where("serial = ?", serial).executeSingle();
        Gson gson = new Gson();
        list = gson.fromJson(orderDetailModel.getOrderDetailBeanList(), new TypeToken<List<OrderDetailBean>>() {
        }.getType());
        adapter = new PayGoodItemAdapter(this, list);
        lv_goods.setAdapter(adapter);
        tv_goods_sum_price.setText("￥ " + orderDetailModel.getTotal_price());
        tv_fix_address.setText(bean.getUserFloor() + bean.getUserUnit() + bean.getRoom());

        changeFinallyPrice(subPrice);

        if (tv_bangbi_count != null) {
            tv_bangbi_count.setText(bangbiCount + "枚帮帮币可用");
        }

    }

    private void initView() {
        lv_goods = (MyListView) findViewById(R.id.lv_goods);

        tv_fix_address = (TextView) findViewById(R.id.tv_fix_address);

        tv_subway_name_tv = (TextView) findViewById(R.id.tv_subway_name_tv);

        root_view = findViewById(R.id.root_view);

        tv_goods_sum_price = (TextView) findViewById(R.id.tv_goods_sum_price);

        rl_paybyarrivel = (RelativeLayout) findViewById(R.id.rl_paybyarrivel);
        ck_cashondelivery = (CheckBox) findViewById(R.id.ck_cashondelivery);

        rl_paybyonline = (RelativeLayout) findViewById(R.id.rl_paybyonline);
        ck_cashonline = (CheckBox) findViewById(R.id.ck_cashonline);

        rl_paybyali = (RelativeLayout) findViewById(R.id.rl_paybyali);
        rb_cashbyali = (CheckBox) findViewById(R.id.rb_cashbyali);

        pay_byweixin = (RelativeLayout) findViewById(R.id.pay_byweixin);
        rb_cashbyweixin = (CheckBox) findViewById(R.id.rb_cashbyweixin);

        rl_bangbi = (RelativeLayout) findViewById(R.id.rl_bangbi);

        tv_bangbi_count = (TextView) findViewById(R.id.tv_bangbi_count);
        rb_cashbybangbi = (CheckBox) findViewById(R.id.rb_cashbybangbi);


        tv_bangbangquan_price = (TextView) findViewById(R.id.tv_bangbangquan_price);
        tv_sum_price = (TextView) findViewById(R.id.tv_sum_price);
        btn_confirm_pay = (LinearLayout) findViewById(R.id.btn_confirm_pay);
        rl_bonus = (RelativeLayout) findViewById(R.id.rl_bonus);
        tv_bonus_count = (TextView) findViewById(R.id.tv_bonus_count);
        tv_use_status = (TextView) findViewById(R.id.tv_use_status);

        pay_byweixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_cashbyweixin.setChecked(!rb_cashbyweixin.isChecked());
                if (rb_cashbyweixin.isChecked()) {
                    rb_cashbyali.setChecked(false);
                    rl_paybyali.setBackgroundColor(getResources().getColor(R.color.white));
                    rl_bonus.setClickable(true);
                    pay_byweixin.setBackgroundColor(getResources().getColor(R.color.item_pay_background));
                    rl_paybyarrivel.setBackgroundColor(getResources().getColor(R.color.white));
                    ck_cashonline.setChecked(true);
                    ck_cashondelivery.setChecked(false);


                } else {
                    pay_byweixin.setBackgroundColor(getResources().getColor(R.color.white));
                }
            }
        });


        rl_paybyarrivel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ck_cashondelivery.setChecked(!ck_cashondelivery.isChecked());
                if (ck_cashondelivery.isChecked()) {
                    showToast("货到付款不支持使用帮帮券!");
                    selectedPosition = -1;
                    subPrice = 0;
                    tv_sum_price.setText("￥ " + Arith.sub(Double.parseDouble(orderDetailModel.getTotal_price()), subPrice));

                    tv_bangbangquan_price.setText("-￥ " + subPrice);

                    tv_use_status.setText("未使用");

                    rl_bonus.setClickable(false);

                    rl_paybyarrivel.setBackgroundColor(getResources().getColor(R.color.item_pay_background));

                    rl_paybyali.setBackgroundColor(getResources().getColor(R.color.white));
                    pay_byweixin.setBackgroundColor(getResources().getColor(R.color.white));

                    rl_bangbi.setBackgroundColor(getResources().getColor(R.color.white));
                    rb_cashbybangbi.setChecked(false);


                    // rl_paybyali.setBackgroundColor(getResources().getColor(R.color.white));


                    ck_cashonline.setChecked(false);
                    rb_cashbyali.setChecked(false);
                    rb_cashbyweixin.setChecked(false);


                } else {
                    rl_paybyarrivel.setBackgroundColor(getResources().getColor(R.color.white));
                }
            }
        });


        rl_paybyali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rb_cashbyali.setChecked(!rb_cashbyali.isChecked());
                if (rb_cashbyali.isChecked()) {
                    rb_cashbyweixin.setChecked(false);
                    pay_byweixin.setBackgroundColor(getResources().getColor(R.color.white));
                    rl_bonus.setClickable(true);
                    rl_paybyali.setBackgroundColor(getResources().getColor(R.color.item_pay_background));
                    rl_paybyarrivel.setBackgroundColor(getResources().getColor(R.color.white));
                    ck_cashonline.setChecked(true);
                    ck_cashondelivery.setChecked(false);


                } else {
                    rl_paybyali.setBackgroundColor(getResources().getColor(R.color.white));
                }

            }
        });

        rl_bangbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rb_cashbybangbi.setChecked(!rb_cashbybangbi.isChecked());

                if (rb_cashbybangbi.isChecked()) {

                    if (tv_subway_name_tv != null) {
                        tv_subway_name_tv.setText("帮帮币");
                    }

                    tv_use_status.setText("未使用");

                    rl_bangbi.setBackgroundColor(getResources().getColor(R.color.item_pay_background));
                    rl_paybyarrivel.setBackgroundColor(getResources().getColor(R.color.white));

                    ck_cashonline.setChecked(true);
                    ck_cashondelivery.setChecked(false);
                    if (bangbiCount > 0) {

                        subPrice = getSubPriceUseBangbi();

                        reloadPayPrice();

                    } else {
                        showToast("帮币数量不足,请选择其他支付方式");
                    }
                } else {
                    rl_bangbi.setBackgroundColor(getResources().getColor(R.color.white));
                    if (tv_subway_name_tv != null) {
                        tv_subway_name_tv.setText("帮帮券");
                    }
                    subPrice = 0.0;
                    /// 重新刷新价格
                    reloadPayPrice();
                }

            }


        });
        btn_confirm_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ck_cashondelivery.isChecked() == false && ck_cashonline.isChecked() == false) {
                    showToast("请选择支付方式!");
                } else if (ck_cashondelivery.isChecked()) {
//                    if (selectedPosition>-1){
//                        fixBonusStatus(selectedPosition+"");
//                    }
                    Intent data = new Intent();
                    data.putExtra(Config.EXPKey_ADDRESS, tv_fix_address.getText() + "");
                    setResult(PayCodeOffline, data);
                    finish();

                }
                if (rb_cashbybangbi.isChecked() && !isNeedUseAlipay()) {

                    ///TODO  单单的帮帮币支付 ,弹框
//                    bangBiPay(""+getSubPriceUseBangbiCount());
                    bangbangbiPay();

                } else if (rb_cashbyali.isChecked()) {
                    //// 判断帮币是否选择的.  判断帮币的个数. 是否足够支付,

                    if (rb_cashbybangbi.isChecked()) {

                        if (isNeedUseAlipay()) {
                            ///TODO  是否需要支付宝支付, 混合支付接口

                           goshopAliPay(getSubPriceUseBangbiCount());

                        } else {
                            ///TODO  单单的帮帮币支付 ,弹框

//                            bangBiPay(""+getSubPriceUseBangbiCount());
                            bangbangbiPay();
                        }
                    } else {
                        ////TODO  原来的支付宝支付逻辑
                        goshopAliPay(0);
                    }


                } else if (rb_cashbyweixin.isChecked()) {

                    if (rb_cashbybangbi.isChecked()) {

                        if (isNeedUseAlipay()) {
                            ///TODO  是否需要微信支付, 混合支付接口
                            subPrice = getSubPriceUseBangbi();
                            weixinpay( getSubPriceUseBangbiCount(), subPrice);

                        } else {
                            ///TODO  单单的帮帮币支付 ,弹框

//                            bangBiPay(""+getSubPriceUseBangbiCount());
                            bangbangbiPay();
                        }

                    } else {
                        weixinpay(0, 0.0);
                    }

                } else {
                    showToast("请选择其他支付方式");
                }


            }
        });

        rl_bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rb_cashbybangbi.setChecked(false);
                rl_bangbi.setBackgroundColor(getResources().getColor(R.color.white));
                if (tv_subway_name_tv != null) {
                    tv_subway_name_tv.setText("帮帮券");
                }
                subPrice = 0.0;
                /// 重新刷新价格
                reloadPayPrice();

                if (canused.size() != 0) {
                    Intent intent = new Intent(PayPreActivity.this, BonusToPayActivity.class);
                    intent.putExtra("emobIdShop", emobIdShop);
                    intent.putExtra("selectedPosition", selectedPosition + "");
                    startActivityForResult(intent, 200);
                } else {
                    Toast.makeText(PayPreActivity.this, "没有可使用的帮帮券！", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    /**
     *
     * 支付宝支付
     *
     * @param bonusCoinCount
     */
    private void goshopAliPay( int bonusCoinCount) {


        Intent intent = getIntent();
        intent.setClass(PayPreActivity.this, ShopAliPayActivity.class);


        ShopOrderInfoReqBean  shopOrderInfoReqBean = new ShopOrderInfoReqBean();
        shopOrderInfoReqBean.setCityId(bean.getCityId());
        shopOrderInfoReqBean.setCommunityId(bean.getCommunityId());
        shopOrderInfoReqBean.setEmobId(bean.getEmobId());
        shopOrderInfoReqBean.setOrderNo(serial);

        if(bonusCoinCount>0){
            shopOrderInfoReqBean.setBonuscoinCount(bonusCoinCount);
        }
//        shopOrderInfoReqBean.setBonuscoinCount(getSubPriceUseBangbiCount());

        if(selectedPosition !=-1){
            shopOrderInfoReqBean.setBonusId(selectedPosition);
        }

        shopOrderInfoReqBean.setSubject(list.get(0).getServiceName()+"...");
        shopOrderInfoReqBean.setTotalFee(""+Arith.round(Arith.sub(Double.parseDouble(orderDetailModel.getTotal_price()), 0), 2));///支付金额，未减去帮帮券和帮帮币折扣之前的金额，单位元
        intent.putExtra("goodsname", list.get(0).getServiceName() + "...");
        //intent.putExtra(Config.EXPKey_serial, serial);
        intent.putExtra(Config.EXPKey_orginPrice, Arith.round(Arith.sub(Double.parseDouble(orderDetailModel.getTotal_price()), 0), 2));
        intent.putExtra(Config.EXPKey_totalPrice, Arith.round(Arith.sub(Double.parseDouble(orderDetailModel.getTotal_price()), getSubPriceUseBangbi()), 2));
        intent.putExtra("address", tv_fix_address.getText() + "");
        intent.putExtra("shopOrderInfoReqBean",shopOrderInfoReqBean);
        startActivityForResult(intent, PayCodeOnline);
    }

    /**
     * 获取使用帮币可以省去的差价.
     *
     * @return
     */
    private double getSubPriceUseBangbi() {

        double bangbiPrice = bangbiCount * bangbiExchange;
        if (bangbiPrice <= 0.0) {
            subPrice = 0.0;
            return subPrice;
        }
        if (orderDetailModel != null) {
            if (Double.parseDouble(orderDetailModel.getTotal_price()) > bangbiPrice) {
                subPrice = bangbiPrice;
            } else {
                subPrice = Double.parseDouble(orderDetailModel.getTotal_price());
            }
        } else {
            Log.e("orderDetailModel ", " is null ");
            subPrice = 0.0;
        }
        return subPrice;
    }


    /**
     * 获取如果使用帮帮币, 则使用的帮币个数
     *
     * @return
     */
    private int getSubPriceUseBangbiCount() {

        double bangbiPrice = bangbiCount * bangbiExchange;

        if (bangbiPrice <= 0.0) {
            subPrice = 0.0;
            return 0;
        }
        if (orderDetailModel != null) {
            if (Double.parseDouble(orderDetailModel.getTotal_price()) > bangbiPrice) {
                return bangbiCount;

            } else {
                subPrice = Double.parseDouble(orderDetailModel.getTotal_price());
                return (int) ((float) subPrice / (float) bangbiExchange);
            }
        } else {
            Log.e("orderDetailModel ", " is null ");
            subPrice = 0.0;
            return 0;
        }
    }


    /**
     * 判断是否需要除帮帮币支付外的其他支付方式
     *
     * @return
     */
    private boolean isNeedUseAlipay() {

        if (bangbiCount <= 0) {
            return true;
        }
        double bangbiPrice = bangbiCount * bangbiExchange;

        if (orderDetailModel != null) {
            if (Double.parseDouble(orderDetailModel.getTotal_price()) > bangbiPrice) {
                return true;
            } else {

                return false;
            }
        } else {
            Log.e("orderDetailModel ", " is null ");
            return true;
        }
    }

    /**
     * 重新刷新支付价格, 及差价
     */
    private void reloadPayPrice() {

        if (rb_cashbybangbi.isChecked()) {
            if (tv_subway_name_tv != null) {
                tv_subway_name_tv.setText("帮帮币");
            }
        } else {
            if (tv_subway_name_tv != null) {
                tv_subway_name_tv.setText("帮帮券");
            }
        }

        if (tv_sum_price != null) {
            tv_sum_price.setText("￥ " + Arith.round(Arith.sub(Double.parseDouble(orderDetailModel.getTotal_price()), subPrice), 2));
        }

        if (tv_bangbangquan_price != null) {
            tv_bangbangquan_price.setText("-￥ " + Arith.round(subPrice, 2));
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PayCodeOnline) {
            if (resultCode == 103) {
                Intent intent = new Intent();
                intent.setClass(PayPreActivity.this, SuperMarketChatActivity.class);
                if (data.getIntExtra("useBonusId", 0) != 0) {
                    data.putExtra(Config.EXPKey_BONUS, 1);
                }
                String address = data.getStringExtra("address");
                intent.putExtra("address", address);
                setResult(RESULT_OK, intent);
                finish();
            }
        } else {
            if (requestCode == 200) {
                if (resultCode == 200) {
                    String bonusPrice = data.getStringExtra("bonusPrice");
                    String temp = data.getStringExtra("selectedPosition");
                    if (temp == null || temp.equals("null")) {
                        selectedPosition = -1;
                    } else {
                        selectedPosition = Integer.parseInt(temp);
                    }
                    if (bonusPrice != null && !("null".equals(bonusPrice)) && selectedPosition > -1) {
                        subPrice = Double.parseDouble(bonusPrice);
                        if (Arith.sub(Double.parseDouble(orderDetailModel.getTotal_price()), subPrice) <= 0.0) {
                            selectedPosition = -1;
                            subPrice = 0;
                            showToast("对不起，商品总价格必须大于帮帮券价格！");
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_sum_price.setText("￥ " + Arith.sub(Double.parseDouble(orderDetailModel.getTotal_price()), subPrice));
                                    tv_bangbangquan_price.setText("-￥ " + subPrice);
                                    tv_use_status.setText("已使用");
                                }
                            });
                        }

                    } else {
                        subPrice = 0;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_sum_price.setText("￥ " + Arith.sub(Double.parseDouble(orderDetailModel.getTotal_price()), subPrice));
                                tv_bangbangquan_price.setText("-￥ " + subPrice);
                                tv_use_status.setText("未使用");
                            }
                        });
                    }
                }
            }
        }

    }


    @Override
    public void onClick(View v) {

    }

    WheelView mWheelViewPro;
    WheelView mWheelViewCity;
    WheelView mWheelViewTwon;

    private void initWheelCity(View cityView, String proindex,
                               String cityindex, String townindex) {
        mWheelViewPro = (WheelView) cityView.findViewById(R.id.provice);
        mWheelViewCity = (WheelView) cityView.findViewById(R.id.city);
        mWheelViewTwon = (WheelView) cityView.findViewById(R.id.area);
        mWheelViewPro.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
//                int proCurrentPosition = v.getCurrentItem();

                item_unit = HomeOwnerUtil.getuserUnit(floors.get(newValue));
                mWheelViewCity.setAdapter(new ArrayWheelAdapter<String>(
                        item_unit));
                mWheelViewCity.setCurrentItem(item_unit.length / 2);
                item_data_rooms = HomeOwnerUtil.getuserRoom(floors.get(newValue).getList().get(0));
                mWheelViewTwon.setAdapter(new ArrayWheelAdapter<String>(
                        item_data_rooms));
                mWheelViewTwon.setCurrentItem(item_data_rooms.length / 2);
            }
        });
        mWheelViewCity.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int proCurrentPosition = mWheelViewPro.getCurrentItem();
                item_data_rooms = HomeOwnerUtil.getuserRoom(floors.get(proCurrentPosition).getList().get(newValue));
                mWheelViewTwon.setAdapter(new ArrayWheelAdapter<String>(
                        item_data_rooms));
                mWheelViewTwon.setCurrentItem(item_data_rooms.length / 2);
            }
        });

        // province
        // 初始化的位置--------------------------------------------------------------
        int proPosition = 0;
        if (proindex != null) {
            proPosition = getArrayPosition(item_floors, proindex);
        }
        // city
        // 初始化的位置--------------------------------------------------------------
        int cityPosition = 0;
        if (cityindex != null) {
            cityPosition = getArrayPosition(item_unit, cityindex);
        }


        // town
        // 初始化的位置--------------------------------------------------------------
        int townPosition = 0;
        if (townindex != null) {
            townPosition = getArrayPosition(item_data_rooms, townindex);
        }

        ArrayWheelAdapter<String> proAdapter = new ArrayWheelAdapter<String>(
                item_floors);
//		proAdapter.setItemTextResource(Typeface.BOLD);
        mWheelViewPro.setSoundEffectsEnabled(true);
        mWheelViewPro.setCurrentItem(proPosition); // 初始化province的位置----------------------------
        mWheelViewPro.setAdapter(proAdapter);
        mWheelViewPro.setCyclic(false);
        ArrayWheelAdapter<String> cityAdapter = new ArrayWheelAdapter<String>(
                item_unit);
        mWheelViewCity.setAdapter(cityAdapter);

        mWheelViewCity.setCurrentItem(cityPosition); // 初始化city的位置-------------------------------------------

        ArrayWheelAdapter<String> twonAdapter = new ArrayWheelAdapter<String>(
                item_data_rooms);
        mWheelViewTwon.setAdapter(twonAdapter);

        mWheelViewTwon.setCurrentItem(townPosition); // 初始化town的位置------------------------------------------

        cityView.findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String proName1 = item_floors[mWheelViewPro.getCurrentItem()];
                String cityName1 = item_unit[mWheelViewCity.getCurrentItem()];
//                String twonIndexStr1 = twonIndexStr[mWheelViewTwon
//                        .getCurrentItem()];
                String twonName1 = item_data_rooms[mWheelViewTwon.getCurrentItem()];
                tv_fix_address.setText(proName1 + cityName1 + twonName1);
                dismissPw();

            }
        });
        cityView.findViewById(R.id.ca_btn).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dismissPw();
            }
        });
    }

    private int getArrayPosition(String[] data, String dataIndex) {

        for (int i = 0; i < data.length; i++) {
            if (data[i].equals(dataIndex)) {
                return i;
            }
        }

        return 0;
    }

    private void showPw(View v) {
        if (mPop == null) {
            mPop = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mPop.setOutsideTouchable(false);
            mPop.setFocusable(true);
            // mPop.setBackgroundDrawable(new BitmapDrawable());
            mPop.showAtLocation(root_view, Gravity.BOTTOM, 0, 0);
            mPop.update();

        } else {
            mPop.showAtLocation(root_view, Gravity.BOTTOM, 0, 0);

        }
    }

    private void dismissPw() {
        if (mPop != null) {
            mPop.dismiss();
            mPop = null;
        }
    }

    public void changeFinallyPrice(final double subPrice) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_sum_price.setText("￥ " + Arith.sub(Double.parseDouble(orderDetailModel.getTotal_price()), subPrice));
            }
        });
    }


    interface GetOrderInfoService {

//        @POST("/api/v1/communities/{communityId}/users/{emobId}/orders/bonusCoin/pay")
//        void postBangbiPayInfo(@Header("signature") String signature, @Body BangBiPayReqBean bean, @Path("communityId") long communityId, @Path("emobId") String emobId, Callback<BangBiPayRespBean> cb);

//        @POST("/api/v1/communities/{communityId}/users/{emobId}/orders/bonusCoin/pay")

        @POST("/api/v3/shopOrders/bonuscoinPay")
        void postBangbiPayInfo(@Body BangBiPayReqBean bean, Callback<CommonRespBean<BangBiPayRespBean>> cb);

    }

    /**
     * 帮帮币支付
     * v3 16/02/29
     * @param bangbiCount
     */
    private void bangBiPay(int bangbiCount) {
        if (TextUtils.isEmpty(serial)) {
            showToast("数据异常帮帮币支付失败");
            return;
        }
        mLdDialog.show();
        Log.i("debbug", "帮帮币支付接口");
        BangBiPayReqBean request = new BangBiPayReqBean();
        request.setSerial(serial);
        Log.i("bangbiCount ", "" + bangbiCount);
        request.setBonuscoin(bangbiCount);
        request.setEmobIdUser(bean.getEmobId());

        GetOrderInfoService service = RetrofitFactory.getInstance().create(getmContext(),request,GetOrderInfoService.class);
        Callback<CommonRespBean<BangBiPayRespBean>> callback = new Callback<CommonRespBean<BangBiPayRespBean>>() {
            @Override
            public void success(CommonRespBean<BangBiPayRespBean> bean, Response response) {
                mLdDialog.dismiss();
                if ("yes".equals(bean.getStatus())) {

                    showMiddleToast("支付成功!");
                    Intent intent = new Intent();
                    intent.setClass(PayPreActivity.this, SuperMarketChatActivity.class);
        //            if (data.getIntExtra("useBonusId", 0) != 0) {
        //                data.putExtra(Config.EXPKey_BONUS, 1);
        //            }
                    intent.putExtra("address", tv_fix_address.getText() + "");
                    setResult(RESULT_OK, intent);
                    finish();

                } else {
                    showMiddleToast("支付失败:" + bean.getMessage());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                showNetErrorToast();
            }
        };
        service.postBangbiPayInfo(request,callback);

    }


    /// 帮帮币足够使用帮帮币支付
    private void bangbangbiPay() {

        final Dialog dialog = new Dialog(getmContext(), R.style.werlfare_DialogStyle);
        dialog.setContentView(R.layout.common_welfare_toast_or_prepay);
        FrameLayout.LayoutParams layparams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getmContext(), 125));
        layparams.setMargins(0, 0, 0, 0);
        dialog.findViewById(R.id.dialog_root_ll).setLayoutParams(layparams);
        dialog.findViewById(R.id.dialog_opt_okcancel_ll).setVisibility(View.VISIBLE);
        dialog.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bangBiPay( getSubPriceUseBangbiCount());

                dialog.dismiss();

            }

        });
        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadPayPrice();
                dialog.dismiss();
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                reloadPayPrice();
                dialog.dismiss();
            }

        });

        ((TextView) dialog.findViewById(R.id.toast_title_tv)).setText("本次购买是否全部使用帮帮币支付");
        dialog.show();
    }

    /**
     * * 获取帮帮币数量
     */
    private void getBangbiCountExchange() {
        NetBaseUtils.extractBounsCoinInfo(getmContext(),bean.getCommunityId(),bean.getEmobId(),new NetBaseUtils.NetRespListener<CommonRespBean<BounsCoinInfoBean>>() {
            @Override
            public void successYes(CommonRespBean<BounsCoinInfoBean> commonRespBean, Response response) {

                if (0 == commonRespBean.getData().getEnable()) {
                    if (rl_bangbi != null) {
                        rl_bangbi.setVisibility(View.GONE);
                    }
                    Log.d("getShowBonuscoin ", "  getShowBonuscoin no ");
                }
                bangbiExchange = commonRespBean.getData().getRatio();
                Log.d("getBangbiCountExchange ", "  bangbiExchange " + bangbiExchange);
                bangbiCount = commonRespBean.getData().getCount();
                Log.d("getBangbiCountExchange ", "  bangbiCount " + bangbiCount);
                initData();
            }

            @Override
            public void successNo(CommonRespBean<BounsCoinInfoBean> commonRespBean, Response response) {
                showToast("获取帮帮币出错："+commonRespBean.getMessage());
            }

            @Override
            public void failure(RetrofitError error) {
                showNetErrorToast();
            }
        });
    }


    //根据用户emobId 以及店家emobId获取 其可用帮帮卷

    private ArrayList<UserBonusBean> canused = new ArrayList<UserBonusBean>();//可用的

    private void getActivities() {
        NetBaseUtils.extractBounsQuanInfo(getmContext(),PreferencesUtil.getCommityId(getmContext()),emobIdUser,2,bean.getCityId(),new NetBaseUtils.NetRespListener<CommonRespBean<List<UserBonusBean>>>() {
            @Override
            public void successYes(CommonRespBean<List<UserBonusBean>> commonRespBean, Response response) {
                Log.i("onion", "userBonusBean: " + commonRespBean);
                for (UserBonusBean info : commonRespBean.getData()) {
                    if ("no".equals(info.getUsed().trim())) {
                        if (info.getExpireTime() * 1000L > System.currentTimeMillis()) {
                            canused.add(info);
//                            Log.i("onion", "info: " + info.getUserBonusId() + "  " + info.getBonusStatus());
                        } else {
                        }
                    }
                }
                tv_bonus_count.setText(canused.size() + "张可用");
            }

            @Override
            public void successNo(CommonRespBean<List<UserBonusBean>> commonRespBean, Response response) {
                showDataErrorToast(commonRespBean.getMessage());
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                showNetErrorToast();
            }
        });
    }

    private void weixinpay(int bonusCoinCount, double subPrice) {
        req = new PayReq();
        sb = new StringBuffer();
        msgApi.registerApp(Config.APP_ID);
        if (!msgApi.isWXAppInstalled()) {
            showToast("您还没有安装微信哦!");
            return;
        }
        if (!msgApi.isWXAppSupportAPI()) {
            showToast("您当前的微信版本不支持微信支付！");
            return;
        }
//        GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
//        getPrepayId.execute();
        Log.i("debbug", "点击了微信支付");
        getWXOrder(bonusCoinCount, subPrice);
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Config.API_KEY);

        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        com.activeandroid.util.Log.e("orion", appSign);
        return appSign;
    }


    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }


    private void genPayReq(String prepay_id) {

        req.appId = Config.APP_ID;
        req.partnerId = Config.MCH_ID;
        req.prepayId = prepay_id;
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());


        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);

        sb.append("sign\n" + req.sign + "\n\n");

//        showToast(sb.toString());

        com.activeandroid.util.Log.e("orion", signParams.toString());

    }

    private void sendPayReq() {

        msgApi.registerApp(Config.APP_ID);
        msgApi.sendReq(req);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(WXPayRequestEvent event) {
        if (!event.isSuccess()) {
            showToast("微信支付失败");
//            Intent intent = new Intent();
//            intent.setClass(PayPreActivity.this, ActivityWelfareIndex.class);
//            setResult(RESULT_CANCELED, intent);
            finish();
        } else {
            showToast("微信支付成功");
            Intent intent = new Intent();
            intent.setClass(PayPreActivity.this, SuperMarketChatActivity.class);
//            if (data.getIntExtra("useBonusId", 0) != 0) {
//                data.putExtra(Config.EXPKey_BONUS, 1);
//            }
            intent.putExtra("address", tv_fix_address.getText() + "");
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    /**
     * 获取微信支付预支付订单
     */
    interface WXOrderService {
//        @POST("/api/v1/wxpay/wxpayBonusProcessor")
//        void postInfo(@Header("signature") String signature, @Body WXPostOrderInfoForBounsBean bean, Callback<WXOrderRequestBean> cb);

//        @POST("/api/v1/wxpay/wxpayBonusProcessor")

        @POST("/api/v3/wxpay")
        void postInfo(@Body WXPostOrderInfoForBounsBean bean, Callback<CommonRespBean<WXOrderRequestBean>> cb);

    }

    /**
     * 微信支付订单获取
     *
     * @param bangbiCount
     * @param subPrice
     */
    private void getWXOrder(int bangbiCount, double subPrice) {
        mLdDialog.show();


        Log.i("debbug", "微信支付接口");
        WXPostOrderInfoForBounsBean request = new WXPostOrderInfoForBounsBean();
        request.setCityId(bean.getCityId());
        request.setEmobId(bean.getEmobId());
        request.setCommunityId(bean.getCommunityId());
        request.setSubject(list.get(0).getServiceName() + "...");
        request.setTotalFee(""+((int) (Arith.round(Arith.sub(Double.parseDouble(orderDetailModel.getTotal_price()), subPrice), 2) * 100)));
        request.setBonusId(selectedPosition);
        request.setOrderNo(serial);
        request.setBonuscoinCount(bangbiCount);

        WXOrderService service = RetrofitFactory.getInstance().create(getmContext(),request,WXOrderService.class);
        Callback<CommonRespBean<WXOrderRequestBean>> callback = new Callback<CommonRespBean<WXOrderRequestBean>>() {
            @Override
            public void success(CommonRespBean<WXOrderRequestBean> bean, Response response) {
                mLdDialog.dismiss();
                if ("yes".equals(bean.getStatus())) {
                    genPayReq(bean.getData().getPrepay_id());
                    sendPayReq();
                } else {
                    Log.i("debbug", "微信订单获取失败");
                    showToast("获取订单失败："+ bean.getMessage());
                }
            }
            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                showNetErrorToast();
            }
        };


        /**
         *     * {
         * "beanId": "wxpayShopOrder",
         * "totalFee": "{支付金额，未减去帮帮券和帮帮币折扣之前的金额，单位分}",
         * "orderNo": "{订单号}",
         * "subject": "{商品名称}",
         * "cityId": {城市ID},
         * "communityId": {小区ID},
         * "emobId": "{用户环信ID}",
         * "bonusId": {帮帮券ID},
         * "bonuscoinCount": {帮帮币数量}
         * }
         */


        service.postInfo(request, callback);
    }

}
