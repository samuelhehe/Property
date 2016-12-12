package xj.property.activity.user;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.adapter.LifeCircleBalckListAdapter;
import xj.property.beans.BlackListBean;
import xj.property.beans.BlackUserInfo;
import xj.property.beans.StatusBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.domain.User;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseUtils;
import xj.property.netbase.RetrofitFactory;
import xj.property.netbasebean.BlackListReqBean;
import xj.property.utils.CommonUtils;
import xj.property.utils.message.XJContactHelper;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

/**
 * Created by Administrator on 2015/3/25.
 */
public class BlackListLifeCircleActivity extends HXBaseActivity {

    UserInfoDetailBean bean;
    LifeCircleBalckListAdapter adapter;


    private int pageIndex = 1;
    ListView lv_blacklist;

    ArrayList<BlackUserInfo> blackUserInfos;


    private LinearLayout ll_errorpage;
    private LinearLayout ll_nomessage;
    private LinearLayout ll_neterror;
    private TextView tv_getagain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklistview);
        bean = PreferencesUtil.getLoginInfo(this);
        blackUserInfos = new ArrayList<BlackUserInfo>();
        initTitle(null, "生活圈黑名单", "");
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
                    if (!CommonUtils.isNetWorkConnected(BlackListLifeCircleActivity.this)) {
                        return;
                    } else {
                        ll_errorpage.setVisibility(View.GONE);
                        getUserBlacklist();
                    }
                }
            });
        } else {
            ll_errorpage.setVisibility(View.GONE);
            getUserBlacklist();
        }
    }

    private void initView() {
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        tv_getagain = (TextView) findViewById(R.id.tv_getagain);
        ll_nomessage = (LinearLayout) findViewById(R.id.ll_nomessage);
        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);

        lv_blacklist = (ListView) findViewById(R.id.lv_blacklist);
        List<String> blackList = PreferencesUtil.getLifeCircleBlackList(BlackListLifeCircleActivity.this);
        blackUserInfos.clear();

        for (int i = 0; i < blackList.size(); i++) {
            User user = XJContactHelper.selectContact(blackList.get(i));
            BlackUserInfo userInfo = new BlackUserInfo();
            if (user != null) {
                userInfo.setAvatar(user.getAvatar());
                userInfo.setNickname(user.getNick());
            }
            blackUserInfos.add(userInfo);
        }
        adapter = new LifeCircleBalckListAdapter(BlackListLifeCircleActivity.this, blackUserInfos);
        lv_blacklist.setAdapter(adapter);
//        lv_blacklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                LifeCircleBalckListAdapter.ViewHolder viewHolder=(LifeCircleBalckListAdapter.ViewHolder)view.getTag();
//                viewHolder.i
//            }
//        });


    }

    public void refresh(int position) {
        if (blackUserInfos.get(position).getEmobIdTo() != null)
            removeBlack(blackUserInfos.get(position).getEmobIdTo(), position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right_text:
                // fixUserInfo();
                //userRegistByFix();
                break;
        }
    }

    private void getUserBlacklist() {
        mLdDialog.show();
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("emobIdFrom", bean.getEmobId());
        queryMap.put("type", BlackListReqBean.type_circle);
        NetBaseUtils.getUserBlacklist(getmContext(),queryMap, new NetBaseUtils.NetRespListener<CommonRespBean<List<BlackUserInfo>>>() {
            @Override
            public void successYes(CommonRespBean<List<BlackUserInfo>> commonRespBean, Response response) {
                mLdDialog.dismiss();
                blackUserInfos.clear();
                ArrayList<String> blackItems = new ArrayList<>();
                for (int i = 0; i < commonRespBean.getData().size(); i++) {
                    BlackUserInfo userInfo = commonRespBean.getData().get(i);
                    if (!userInfo.getNickname().isEmpty()) {
                        blackUserInfos.add(userInfo);
                        blackItems.add(userInfo.getEmobIdTo());
                    }
                }
                adapter.notifyDataSetChanged();
                PreferencesUtil.saveLifeCircleBlackList(BlackListLifeCircleActivity.this, blackItems);

                if (blackUserInfos.size() == 0) {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_neterror.setVisibility(View.GONE);
                    ll_nomessage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void successNo(CommonRespBean<List<BlackUserInfo>> commonRespBean, Response response) {
                mLdDialog.dismiss();
                if (blackUserInfos.size() == 0) {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_neterror.setVisibility(View.GONE);
                    ll_nomessage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
            }
        });
    }


    private void removeBlack(final String emobid, final int position) {
        mLdDialog.show();
        BlackListReqBean blackListReqBean = new BlackListReqBean();
        blackListReqBean.setType(BlackListReqBean.type_circle);
        blackListReqBean.setCommunityId(PreferencesUtil.getCommityId(getmContext()));
        blackListReqBean.setEmobIdTo(emobid);
        blackListReqBean.setEmobIdFrom(PreferencesUtil.getLoginInfo(getmContext()).getEmobId());

        NetBaseUtils.removeUserFromBlackList(getmContext(),blackListReqBean,new NetBaseUtils.NetRespListener<CommonRespBean<String>>() {
            @Override
            public void successYes(CommonRespBean<String> commonRespBean, Response response) {
                mLdDialog.dismiss();
                List<String> list = PreferencesUtil.getLifeCircleBlackList(BlackListLifeCircleActivity.this);
                Log.i("onion", "iv_switch_block_notify :list" + list);
                if (list.contains(emobid)) list.remove(emobid);
                PreferencesUtil.saveLifeCircleBlackList(BlackListLifeCircleActivity.this, list);
                blackUserInfos.remove(position);
                adapter.notifyDataSetChanged();
                if (blackUserInfos.size() == 0) {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_neterror.setVisibility(View.GONE);
                    ll_nomessage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void successNo(CommonRespBean<String> commonRespBean, Response response) {
                mLdDialog.dismiss();
                showToast("移除黑名单失败:"+commonRespBean.getMessage());
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                showNetErrorToast();
            }
        });
    }


}
