package xj.property.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.adapter.UserBonusAdapter;
import xj.property.beans.UserBonusAdapterBean;
import xj.property.beans.UserBonusBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseUtils;
import xj.property.utils.CommonUtils;
import xj.property.utils.other.PreferencesUtil;

/**
 * Created by n on 2015/4/7.
 *
 * v3 2016/03/18
 *
 */
public class UserBonusActivity extends HXBaseActivity {

    private ExpandableListView lv_ueer_bonus;


    UserInfoDetailBean userInfoDetailBean;

    // used 使用的
    //unused 未使用的-- 失效的（faile） 可用的（）
    private ArrayList<UserBonusBean> canused = new ArrayList<UserBonusBean>();//可用的
    private ArrayList<UserBonusBean> hasused = new ArrayList<UserBonusBean>();//使用的
    private ArrayList<UserBonusBean> failure = new ArrayList<UserBonusBean>();//失效的

    private LinearLayout ll_errorpage;
    private LinearLayout ll_nomessage;
    private LinearLayout ll_neterror;
    private TextView tv_getagain;
    private TextView tv_nomessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus);
        userInfoDetailBean = PreferencesUtil.getLoginInfo(UserBonusActivity.this);
        initTitle(null, "帮帮券", "使用说明");
        initView();
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
                    if (!CommonUtils.isNetWorkConnected(UserBonusActivity.this)) {
                        return;
                    } else {
                        ll_errorpage.setVisibility(View.GONE);
                        getUserBonusList(userInfoDetailBean);
                    }
                }
            });
        } else {
            ll_errorpage.setVisibility(View.GONE);
            getUserBonusList(userInfoDetailBean);
        }
    }


    private void initView() {
        lv_ueer_bonus = (ExpandableListView) findViewById(R.id.lv_ueer_bonus);
        //lv_ueer_bonus.setChildDivider(null);
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        tv_getagain = (TextView) findViewById(R.id.tv_getagain);
        ll_nomessage = (LinearLayout) findViewById(R.id.ll_nomessage);
        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);
        tv_nomessage = (TextView) findViewById(R.id.tv_nomessage);
        tv_nomessage.setText("您还没有帮帮券哦！");

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right_text:
                Intent intent = new Intent(this, UserBonusInstructionsActivity.class);
                startActivity(intent);
                break;
        }
    }

    private ArrayList<UserBonusAdapterBean> group;
    private UserBonusAdapter adapter;

    private void getUserBonusList(UserInfoDetailBean bean) {
        mLdDialog.show();

        NetBaseUtils.extractBounsQuanInfo(getmContext(), PreferencesUtil.getCommityId(getmContext()), bean.getEmobId(), -1, bean.getCityId(), new NetBaseUtils.NetRespListener<CommonRespBean<List<UserBonusBean>>>() {
            @Override
            public void successYes(CommonRespBean<List<UserBonusBean>> commonRespBean, Response response) {
                mLdDialog.dismiss();
                fillData(commonRespBean.getData());
                System.out.println(" bean.getInfo(): " + commonRespBean.getData());
                adapter = new UserBonusAdapter(UserBonusActivity.this, group);
                lv_ueer_bonus.setAdapter(adapter);

                //取消expandListview默认图标
                lv_ueer_bonus.setGroupIndicator(null);
                //设置expandListview默认展开
                for (int k = 0; k < adapter.getGroupCount(); k++) {
                    lv_ueer_bonus.expandGroup(k);
                }
                //设置expandListview点击不关闭
                lv_ueer_bonus.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        return true;//不关闭
                    }
                });
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
                mLdDialog.dismiss();
                showNetErrorToast();
            }
        });
    }

    private void fillData(List<UserBonusBean> list) {
        group = new ArrayList<>();
        group.clear();
        for (UserBonusBean info : list) {
            Log.i("onion", "R:" + info.getBonusR());
            Log.i("onion", "G:" + info.getBonusG());
            Log.i("onion", "B:" + info.getBonusB());
            if ("yes".equals(info.getUsed().trim())) {
                hasused.add(info);
            } else if ("no".equals(info.getUsed().trim())) {
                if (info.getExpireTime() * 1000 > System.currentTimeMillis()) {
                    canused.add(info);
                } else {
                    failure.add(info);
                }
            }
        }
        if (canused != null && canused.size() != 0) {
            UserBonusAdapterBean userBonusAdapterBean = new UserBonusAdapterBean();
            userBonusAdapterBean.setBonusStatusStr("可用优惠券");
            userBonusAdapterBean.setChildList(canused);
            group.add(userBonusAdapterBean);
        }
        if (failure != null && failure.size() != 0) {
            UserBonusAdapterBean userBonusAdapterBean = new UserBonusAdapterBean();
            userBonusAdapterBean.setBonusStatusStr("已失效的券");
            userBonusAdapterBean.setChildList(failure);
            group.add(userBonusAdapterBean);
        }
        if (hasused != null && hasused.size() != 0) {
            UserBonusAdapterBean userBonusAdapterBean = new UserBonusAdapterBean();
            userBonusAdapterBean.setBonusStatusStr("已使用的券");
            userBonusAdapterBean.setChildList(hasused);
            group.add(userBonusAdapterBean);
        }

        PreferencesUtil.saveBangBangQuanCount(UserBonusActivity.this, canused.size() + "");

    }


}
