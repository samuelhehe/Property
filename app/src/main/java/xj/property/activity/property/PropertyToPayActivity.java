package xj.property.activity.property;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.adapter.PropertyToPayAdapter;
import xj.property.beans.UserBonusBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseUtils;
import xj.property.utils.CommonUtils;
import xj.property.utils.ToastUtils;
import xj.property.utils.other.PreferencesUtil;

/**
 * aurth:asia
 * 缴费部分，选择帮帮券部分
 */
public class PropertyToPayActivity extends HXBaseActivity {

    private UserInfoDetailBean userInfoDetailBean;
    private ListView lv_user_bonus;
    private Button bt_confirm;

    private String emobIdShop;
    private String emobIdUser;

    private PropertyToPayAdapter adapter;
    private String strPosition;

    private LinearLayout ll_errorpage;
    private LinearLayout ll_nomessage;
    private LinearLayout ll_neterror;
    private TextView tv_getagain;
    private double totalprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus_topay);
        initTitle(null, "帮帮券", "");
        userInfoDetailBean = PreferencesUtil.getLoginInfo(PropertyToPayActivity.this);
        emobIdUser = userInfoDetailBean.getEmobId();
        emobIdShop = getIntent().getStringExtra("emobIdShop");
        //// 商品的总价格
        totalprice = getIntent().getDoubleExtra("totalprice", 0.0D);
        strPosition = "-1";
        initView();
        Log.i("onion", emobIdShop + "  " + emobIdUser);
        initData();
    }

    private void initData() {
        if (!CommonUtils.isNetWorkConnected(this)) {
            ll_errorpage.setVisibility(View.VISIBLE);
            ll_neterror.setVisibility(View.VISIBLE);
            ll_nomessage.setVisibility(View.GONE);
            tv_getagain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!CommonUtils.isNetWorkConnected(PropertyToPayActivity.this)) {
                        return;
                    } else {
                        ll_errorpage.setVisibility(View.GONE);
                        getActivities();
                    }
                }
            });
        } else {
            ll_errorpage.setVisibility(View.GONE);
            getActivities();
        }
    }

    private Integer selectedPosition;

    private void initView() {
        lv_user_bonus = (ListView) findViewById(R.id.lv_user_bonus);
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        tv_getagain = (TextView) findViewById(R.id.tv_getagain);
        ll_nomessage = (LinearLayout) findViewById(R.id.ll_nomessage);
        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);

        bt_confirm = (Button) findViewById(R.id.bt_confirm);
        lv_user_bonus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PropertyToPayAdapter.ViewHolder viewHolder = (PropertyToPayAdapter.ViewHolder) view.getTag();

                String choisedpricestr = canused.get(position).getBonusPrice();
                ///消费金额必须大于通用券
                if(Double.valueOf(choisedpricestr)>=totalprice&&canused.get(position).getBonusType()==2) {
                    ToastUtils.showToast(getmContext(), "消费金额必须大于" + choisedpricestr + "元");
                    selectedPosition = -1;
                    return ;
                }
                viewHolder.ck_bonus.toggle();
                PropertyToPayAdapter.getIsSelected().put(position, viewHolder.ck_bonus.isChecked());
                if (viewHolder.ck_bonus.isChecked()) {
                    selectedPosition = canused.get(position).getBonusId();
                    Log.i("onion", "selectedPosition:" + selectedPosition);
                    // selectedPosition = position;
                    for (int i = 0; i < canused.size(); i++) {

                        if (i != position) {
                            PropertyToPayAdapter.getIsSelected().put(i, false);
                        } else {
                            PropertyToPayAdapter.getIsSelected().put(position, true);
                        }
                    }

                } else {
                    selectedPosition = -1;
                }
                adapter.notifyDataSetChanged();
            }
        });
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent data = new Intent();
                if (selectedPosition != null && selectedPosition > -1) {
                    data.putExtra("selectedPosition", selectedPosition + "");
                    Integer a = null;
                    for (int i = 0; i < canused.size(); i++) {
                        if (canused.get(i).getBonusId() == selectedPosition) {
                            a = i;
                            break;
                        }
                    }
                    data.putExtra("bonusPrice", canused.get(a).getBonusPrice() + "");
                    data.putExtra("bonusName", canused.get(a).getBonusName());
                    data.putExtra("UserBonusId", canused.get(a).getBonusId());
                    price = canused.get(a).getBonusPrice();
                } else {
                    data.putExtra("selectedPosition", "-1");
                    data.putExtra("bonusPrice", 0);
                }
                Log.i("onion", "selectedPosition: " + selectedPosition + "  bonusPrice: " + price);
                setResult(200, data);
                finish();

            }
        });

    }

    private String price;

    @Override
    public void onClick(View v) {
    }

    private ArrayList<UserBonusBean> canused = new ArrayList<>();//可用的

    private void getActivities() {
        mLdDialog.show();
        NetBaseUtils.extractBounsQuanInfo(getmContext(), PreferencesUtil.getCommityId(this), emobIdUser, -1, userInfoDetailBean.getCityId(), new NetBaseUtils.NetRespListener<CommonRespBean<List<UserBonusBean>>>() {
            @Override
            public void successYes(CommonRespBean<List<UserBonusBean>> commonRespBean, Response response) {
                mLdDialog.dismiss();
                for (UserBonusBean info : commonRespBean.getData()) {
                    if ("yes".equals(info.getUsed().trim())) {
                    } else if ("no".equals(info.getUsed().trim())) {
                        if (info.getExpireTime() * 1000L > System.currentTimeMillis()) {
                            canused.add(info);
                            Log.i("onion", "unused " + info.getBonusId());
                        } else {
                            Log.i("onion", "used " + info.getBonusId());
                        }
                    }
                }
                adapter = new PropertyToPayAdapter(PropertyToPayActivity.this, canused);
                lv_user_bonus.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                if(canused.size()>0){
                    ll_errorpage.setVisibility(View.GONE);
                }else {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_neterror.setVisibility(View.GONE);
                    ll_nomessage.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void successNo(CommonRespBean<List<UserBonusBean>> commonRespBean, Response response) {
                mLdDialog.dismiss();
                ll_errorpage.setVisibility(View.VISIBLE);
                ll_neterror.setVisibility(View.GONE);
                ll_nomessage.setVisibility(View.VISIBLE);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                mLdDialog.dismiss();
                ll_errorpage.setVisibility(View.VISIBLE);
                ll_neterror.setVisibility(View.VISIBLE);
                ll_nomessage.setVisibility(View.GONE);
                showNetErrorToast();
            }
        });
    }


}
