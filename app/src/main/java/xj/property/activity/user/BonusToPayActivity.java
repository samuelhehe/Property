package xj.property.activity.user;

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
import xj.property.adapter.BonusToPayAdapter;
import xj.property.beans.UserBonusBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseUtils;
import xj.property.utils.CommonUtils;
import xj.property.utils.other.PreferencesUtil;

/**
 * Created by n on 2015/4/7.
 */
public class BonusToPayActivity extends HXBaseActivity {

    UserInfoDetailBean userInfoDetailBean;
    private ListView lv_user_bonus;
    private Button bt_confirm;

    private String emobIdShop;
    private String emobIdUser;

    private BonusToPayAdapter adapter;
    private String strPosition;

    private LinearLayout ll_errorpage;
    private LinearLayout ll_nomessage;
    private LinearLayout ll_neterror;
    private TextView tv_getagain;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus_topay);
        initTitle(null, "帮帮券", "");
        userInfoDetailBean = PreferencesUtil.getLoginInfo(BonusToPayActivity.this);
        emobIdUser = userInfoDetailBean.getEmobId();
        emobIdShop = getIntent().getStringExtra("emobIdShop");
        //  strPosition=getIntent().getStringExtra("selectedPosition");
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
                    if (!CommonUtils.isNetWorkConnected(BonusToPayActivity.this)) {
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
                BonusToPayAdapter.ViewHolder viewHolder = (BonusToPayAdapter.ViewHolder) view.getTag();
                viewHolder.ck_bonus.toggle();
                BonusToPayAdapter.getIsSelected().put(position, viewHolder.ck_bonus.isChecked());

                if (viewHolder.ck_bonus.isChecked() == true) {
                    selectedPosition = canused.get(position).getBonusId();
                    Log.i("onion", "selectedPosition:" + selectedPosition);
                    // selectedPosition = position;
                    for (int i = 0; i < canused.size(); i++) {
                        if (i != position) {
                            BonusToPayAdapter.getIsSelected().put(i, false);
                        } else {
                            BonusToPayAdapter.getIsSelected().put(position, true);
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
                    data.putExtra("bonusPrice", canused.get(a).getBonusPrice());
                    price = canused.get(a).getBonusPrice();
                } else {
                    data.putExtra("selectedPosition", -1);
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


    //根据用户emobId 以及店家emobId获取 其可用帮帮卷
//    interface BonusListService {
//        ///http://114.215.114.56:8080/api/v1/communities/1/bonuses?emobIdShop=e33cc229df1395eef74c783ec3c2e021&emobIdUser=68e6d778f2e02719f602ffaab93342f1
//        @GET("/api/v1/communities/{communityId}/bonuses")
//        void getActList(@Path("communityId") long communityId, @QueryMap Map<String, String> map, Callback<UserBonusBean> cb);
//    }

    private ArrayList<UserBonusBean> canused = new ArrayList<UserBonusBean>();//可用的

    private void getActivities() {
        mLdDialog.show();
        NetBaseUtils.extractBounsQuanInfo(getmContext(), PreferencesUtil.getCommityId(getmContext()), emobIdUser, 2,
                userInfoDetailBean.getCityId(), new NetBaseUtils.NetRespListener<CommonRespBean<List<UserBonusBean>>>() {
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
                                }
                            }
                        }
                        adapter = new BonusToPayAdapter(BonusToPayActivity.this, canused);
                        lv_user_bonus.setAdapter(adapter);

                        if (commonRespBean.getData().size() != 0) {
                            ll_errorpage.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        } else {
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
                        showNetErrorToast();
                    }
                });


//        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE2).build();
//        BonusListService service = restAdapter.create(BonusListService.class);
//        Callback<UserBonusBean> callback = new Callback<UserBonusBean>() {
//            @Override
//            public void success(UserBonusBean bean, retrofit.client.Response response) {
//                mLdDialog.dismiss();
//                userBonusBean = bean;
//                for (UserBonusBean info : bean.getInfo()) {
//                    if ("yes".equals(info.getUsed().trim())) {
//                    } else if ("no".equals(info.getUsed().trim())) {
//                        if (info.getExpireTime() * 1000 > System.currentTimeMillis()) {
//                            canused.add(info);
//                            Log.i("onion", "unused " + info.getBonusId());
//                        } else {
//                        }
//                    }
//                }
//
//                adapter = new BonusToPayAdapter(BonusToPayActivity.this, canused);
//                lv_user_bonus.setAdapter(adapter);
//               /* if (Integer.parseInt(strPosition)>-1)
//                {
//                    selectedPosition=Integer.parseInt(strPosition);
//                    for (int i=0;i<bean.getInfo().size();i++){
//                        if (selectedPosition==Integer.parseInt(bean.getInfo().get(i).getUserBonusId())){
//                            BonusToPayAdapter.getIsSelected().put(i,true);
//                            break;
//                        }
//                    }
//                    adapter.notifyDataSetChanged();
//                }*/
//
//                if (bean.getInfo().size() != 0) {
//                    ll_errorpage.setVisibility(View.GONE);
//                    adapter.notifyDataSetChanged();
//                } else {
//                    ll_errorpage.setVisibility(View.VISIBLE);
//                    ll_neterror.setVisibility(View.GONE);
//                    ll_nomessage.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                error.printStackTrace();
//                mLdDialog.dismiss();
//                showNetErrorToast();
//            }
//        };
//        Map<String, String> option = new HashMap<String, String>();
//        option.put("emobIdShop", emobIdShop);
//        option.put("emobIdUser", emobIdUser);
////        option.put("emobIdShop", "e33cc229df1395eef74c783ec3c2e021");
////        option.put("emobIdUser", "68e6d778f2e02719f602ffaab93342f1");
//        service.getActList(PreferencesUtil.getCommityId(this), option, callback);
    }


}
