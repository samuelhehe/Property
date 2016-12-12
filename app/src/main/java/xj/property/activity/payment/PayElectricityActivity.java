package xj.property.activity.payment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.alipay.PayDemoActivity;
import xj.property.beans.PayValueBeans;
import xj.property.utils.other.Arith;
import xj.property.utils.other.Config;
import xj.property.widget.wheelview.ArrayWheelAdapter;
import xj.property.widget.wheelview.WheelView;

/**
 * Created by Administrator on 2015/4/13.
 */
public class PayElectricityActivity extends HXBaseActivity {
    List<PayValueBeans.PayValueBean> list;
    List<ImageView> ivs;
    List<View> views;
    TextView tv_MineInfo;
    LinearLayout ll_payvalue;
    TextView tvPrice;
    TextView tvValue;
    TextView tv_singleprice;
    private PopupWindow mPop;
    private View root_view;
    View mWheelLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payele);
        initTitle(null, "电费", "");
        root_view = findViewById(R.id.root_view);
        tv_MineInfo = (TextView) findViewById(R.id.tv_mine_content);
        tv_MineInfo.setOnClickListener(this);
        ll_payvalue = (LinearLayout) findViewById(R.id.ll_payvalue);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvValue = (TextView) findViewById(R.id.tv_value);
        tv_singleprice = (TextView) findViewById(R.id.tv_singleprice);
        getPayValue(1);
        ivs = new ArrayList<>();
        views = new ArrayList<>();
        mWheelLayout = View.inflate(this, R.layout.wheel_choose_city_three,
                null);
        initWheelCity(mWheelLayout, null, null, null);
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PayElectricityActivity.this, PayDemoActivity.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        showPw(mWheelLayout);
    }

    private void upDateColor(int choicePostion) {
        if (choicePostion < ivs.size() && choicePostion < views.size()) {
            for (int i = 0; i < ivs.size(); i++) {
                if (i == choicePostion) {
                    ivs.get(i).setImageResource(R.drawable.payment_el_select);
//                views.get(i).setBackgroundColor(Color.GRAY);
                    tvValue.setText(list.get(i).getEntrySum() + "度");
                    tvPrice.setText(list.get(i).getPrice() + "元");
                    try {
                        double price = Arith.div(Double.parseDouble(list.get(i).getPrice()), Double.parseDouble(list.get(i).getEntrySum()));
                                tv_singleprice.setText(price + "元/度");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    ivs.get(i).setImageResource(R.drawable.payment_el_normal);
//                views.get(i).setBackgroundColor(Color.WHITE);
                }
            }

        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Integer postion = (Integer) v.getTag();
            upDateColor(postion);
        }
    };

    private LinearLayout addItem(PayValueBeans.PayValueBean bean, int postion) {
        LinearLayout ll = (LinearLayout) View.inflate(this,
                R.layout.item_payment, null);
//        if (ivId != null) {
//            ImageView iv = (ImageView) ll.findViewById(R.id.iv_mine);
//            iv.setImageResource(ivId);
//        } else {
//            ll.findViewById(R.id.iv_mine).setVisibility(View.GONE);
//        }
        TextView tvPrice = (TextView) ll.findViewById(R.id.tv_price);
        tvPrice.setText(bean.getPrice());
        TextView tv_value = (TextView) ll.findViewById(R.id.tv_value);
        tv_value.setText(bean.getEntrySum());
        views.add(ll);
        ivs.add((ImageView) ll.findViewById(R.id.iv_choice));
        ll.setTag(postion);
        ll.setOnClickListener(listener);
        return ll;
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

    /* private HashMap<String, String[]> map2 = null;
    private HashMap<String, String[]> map1 = null;
    private HashMap<String, String[]> map3 = null;
*/
    private String[] proIndexStr = new String[]{"1号楼", "1号楼", "1号楼", "1号楼"};
    private String[] proNameStr = new String[]{"1号楼", "2号楼", "3号楼", "4号楼"};
    private String[] cityIndexStr = new String[]{"1号楼", "1号楼", "1号楼", "1号楼"};
    private String[] cityNameStr = new String[]{"一单元", "二单元", "三单元", "四单元"};
    private String[] twonIndexStr = new String[]{"1号楼", "1号楼", "1号楼", "1号楼"};
    private String[] twonNameStr = new String[]{"501", "502", "503", "504"};

    private void initWheelCity(View cityView, String proindex,
                               String cityindex, String townindex) {

        final WheelView mWheelViewPro = (WheelView) cityView
                .findViewById(R.id.provice);
        final WheelView mWheelViewCity = (WheelView) cityView
                .findViewById(R.id.city);
        final WheelView mWheelViewTwon = (WheelView) cityView
                .findViewById(R.id.area);
        Button okBtn = (Button) cityView.findViewById(R.id.ok_btn);
        Button cancelBtn = (Button) cityView.findViewById(R.id.ca_btn);

        // province
        // 初始化的位置--------------------------------------------------------------
        int proPosition = 0;
        if (proindex != null) {
            proPosition = getArrayPosition(proIndexStr, proindex);
        }
        // city
        // 初始化的位置--------------------------------------------------------------
        int cityPosition = 0;
        if (cityindex != null) {
            cityPosition = getArrayPosition(cityIndexStr, cityindex);
        }


        // town
        // 初始化的位置--------------------------------------------------------------
        int townPosition = 0;
        if (townindex != null) {
            townPosition = getArrayPosition(twonIndexStr, townindex);
        }

        ArrayWheelAdapter<String> proAdapter = new ArrayWheelAdapter<String>(
               proNameStr);
//		proAdapter.setItemTextResource(Typeface.BOLD);
        mWheelViewPro.setSoundEffectsEnabled(true);
        mWheelViewPro.setCurrentItem(proPosition); // 初始化province的位置----------------------------

        mWheelViewPro.setAdapter(proAdapter);
        mWheelViewPro.setCyclic(false);
//        mWheelViewPro.addScrollingListener(new OnWheelScrollListener() {
//
//            public void onScrollingStarted(WheelView wheel) {
//
//            }
//
//            public void onScrollingFinished(WheelView v) {
//
//                int proCurrentPosition = v.getCurrentItem();
//                map2 = dbManager
//                        .getCityStringArray(proIndexStr[proCurrentPosition]);
//                cityIndexStr = map2.get("cityindex");
//                cityNameStr = map2.get("cityname");
//                mWheelViewCity.setViewAdapter(new ArrayWheelAdapter<String>(
//                        InsertAddressActivity.this, cityNameStr));
//                mWheelViewCity.setCurrentItem(0);
//
//                int cityCurrentPosition = mWheelViewCity.getCurrentItem();
//                map3 = dbManager.getTwonStringArray(
//                        proIndexStr[proCurrentPosition],
//                        cityIndexStr[cityCurrentPosition]);
//                twonIndexStr = map3.get("townindex");
//                twonNameStr = map3.get("townname");
//                mWheelViewTwon.setViewAdapter(new ArrayWheelAdapter<String>(
//                        InsertAddressActivity.this, twonNameStr));
//                mWheelViewTwon.setCurrentItem(0);
//
//            }
//        });

        ArrayWheelAdapter<String> cityAdapter = new ArrayWheelAdapter<String>(
                cityNameStr);
        mWheelViewCity.setAdapter(cityAdapter);

        mWheelViewCity.setCurrentItem(cityPosition); // 初始化city的位置-------------------------------------------

//        mWheelViewCity.addScrollingListener(new OnWheelScrollListener() {
//
//            public void onScrollingStarted(WheelView wheel) {
//
//            }
//
//            public void onScrollingFinished(WheelView v) {
//
//                int proCurrentPosition = mWheelViewPro.getCurrentItem();
//                int cityCurrentPosition = v.getCurrentItem();
//                map3 = dbManager.getTwonStringArray(
//                        proIndexStr[proCurrentPosition],
//                        cityIndexStr[cityCurrentPosition]);
//                twonIndexStr = map3.get("townindex");
//                twonNameStr = map3.get("townname");
//                mWheelViewTwon.setViewAdapter(new ArrayWheelAdapter<String>(
//                        InsertAddressActivity.this, twonNameStr));
//                mWheelViewTwon.setCurrentItem(0);
//
//            }
//        });

        ArrayWheelAdapter<String> twonAdapter = new ArrayWheelAdapter<String>(
                 twonNameStr);
        mWheelViewTwon.setAdapter(twonAdapter);

        mWheelViewTwon.setCurrentItem(townPosition); // 初始化town的位置------------------------------------------

        okBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

//                String proIndexStr1 = proIndexStr[mWheelViewPro
//                        .getCurrentItem()];
                String proName1 = proNameStr[mWheelViewPro.getCurrentItem()];
//                String cityIndexStr1 = cityIndexStr[mWheelViewCity
//                        .getCurrentItem()];
                String cityName1 = cityNameStr[mWheelViewCity.getCurrentItem()];
//                String twonIndexStr1 = twonIndexStr[mWheelViewTwon
//                        .getCurrentItem()];
                String twonName1 = twonNameStr[mWheelViewTwon.getCurrentItem()];
                tv_MineInfo.setText(proName1 + cityName1 + twonName1);
                dismissPw();
//                select_address_et.setBackgroundColor(getResources().getColor(
//                        R.color.white));
//                proId = proIndexStr1;
//                cityId = cityIndexStr1;
//                twonId = twonIndexStr1;
//
//                proName = proName1;
//                cityName = cityName1;
//                twonName = twonName1;

//                select_address_et.setText(proName1 + cityName1 + twonName1);
                // Toast.makeText(mContext,
                // proIndexStr1 + cityIndexStr1 + twonIndexStr1,
                // Toast.LENGTH_SHORT).show();

            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {

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

    interface getPayValueService {
        ///api/v1/communities/1/payStandards?q=1
        @GET("/api/v1/communities/{communityId}/payStandards?q=1")
        void getPayValue(@Path("communityId") int communityId, @QueryMap HashMap<String, Integer> option, Callback<PayValueBeans> cb);
    }

    public void getPayValue(int num) {
        mLdDialog.show();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE2).build();
        getPayValueService service = restAdapter.create(getPayValueService.class);
        Callback<PayValueBeans> callback = new Callback<PayValueBeans>() {
            @Override
            public void success(PayValueBeans bean, retrofit.client.Response response) {
                mLdDialog.dismiss();
                list = bean.getInfo().getList();
                for (int i = 0; i < list.size(); i++)
                    ll_payvalue.addView(addItem(list.get(i), i));
                upDateColor(0);

            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                showNetErrorToast();
                error.printStackTrace();
            }
        };
        HashMap<String, Integer> option = new HashMap<>();
        option.put("p", num);
        service.getPayValue(1, option, callback);
    }
}
