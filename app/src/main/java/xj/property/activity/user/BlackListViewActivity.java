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
import xj.property.adapter.BalckListAdapter;
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
public class BlackListViewActivity extends HXBaseActivity {

    UserInfoDetailBean bean;
    BalckListAdapter adapter;


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
        initTitle(null, "群组黑名单", "");
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
                    if (!CommonUtils.isNetWorkConnected(BlackListViewActivity.this)) {
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
        List<String> blackList = PreferencesUtil.getBlackList(BlackListViewActivity.this);
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
        adapter = new BalckListAdapter(BlackListViewActivity.this, blackUserInfos);
        lv_blacklist.setAdapter(adapter);
//        lv_blacklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                BalckListAdapter.ViewHolder viewHolder=(BalckListAdapter.ViewHolder)view.getTag();
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
        //emobIdFrom={拉黑方环信ID}&type={黑名单类型：activity->聊天黑名单，circle->生活圈黑名单}
        HashMap<String,String> queryMap = new HashMap<>();
        queryMap.put("emobIdFrom",bean.getEmobId());
        queryMap.put("type", BlackListReqBean.type_activity);
        NetBaseUtils.getUserBlacklist(getmContext(),queryMap,new NetBaseUtils.NetRespListener<CommonRespBean<List<BlackUserInfo>>>() {
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
                PreferencesUtil.saveBlackList(BlackListViewActivity.this, blackItems);
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

//    interface delBlackListService {
//        ///api/v1/communities/{communityId}/blacklist/{emobId}?emobIdTo={emobIdTo}
//        @DELETE("/api/v1/communities/{communityId}/blacklist/{emobId}")
//        void removeBlack(@Path("communityId") int communityId, @Path("emobId") String emobId, @QueryMap Map<String, String> option, Callback<StatusBean> cb);
//    }

    private void removeBlack(final String emobid, final int position) {
        mLdDialog.show();
        BlackListReqBean blackListReqBean = new BlackListReqBean();
        blackListReqBean.setType(BlackListReqBean.type_activity);
        blackListReqBean.setCommunityId(PreferencesUtil.getCommityId(getmContext()));
        blackListReqBean.setEmobIdTo(emobid);
        blackListReqBean.setEmobIdFrom(PreferencesUtil.getLoginInfo(getmContext()).getEmobId());
        NetBaseUtils.removeUserFromBlackList(getmContext(),blackListReqBean,new NetBaseUtils.NetRespListener<CommonRespBean<String>>(){
            @Override
            public void successYes(CommonRespBean<String> commonRespBean, Response response) {
                mLdDialog.dismiss();
                List<String> list = PreferencesUtil.getBlackList(BlackListViewActivity.this);
                Log.i("onion", "iv_switch_block_notify :list" + list);
                if (list.contains(emobid)) list.remove(emobid);
                PreferencesUtil.saveBlackList(BlackListViewActivity.this, list);
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
                showToast("移除黑名单失败:"+ commonRespBean.getMessage());
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                showNetErrorToast();
            }
        });
//
//
//
//
//
//        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE2).build();
//        delBlackListService service = restAdapter.create(delBlackListService.class);
//        Callback<StatusBean> callback = new Callback<StatusBean>() {
//            @Override
//            public void success(StatusBean bean, retrofit.client.Response response) {
//                mLdDialog.dismiss();
//                if ("yes".equals(bean.getStatus())) {
//                    //showToast("已从黑名单里移除");
//                    List<String> list = PreferencesUtil.getBlackList(BlackListViewActivity.this);
//                    Log.i("onion", "iv_switch_block_notify :list" + list);
//                    if (list.contains(emobid)) list.remove(emobid);
//                    PreferencesUtil.saveBlackList(BlackListViewActivity.this, list);
//                    blackUserInfos.remove(position);
//                    adapter.notifyDataSetChanged();
//                    if (blackUserInfos.size() == 0) {
//                        ll_errorpage.setVisibility(View.VISIBLE);
//                        ll_neterror.setVisibility(View.GONE);
//                        ll_nomessage.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    showToast("移除黑名单失败");
//                }
//               /* try {
//                    EMGroupManager.getInstance().joinGroup(emobGroupId);
//                }catch (Exception e){
//                    Log.e("onion",e.toString());
//                }*/
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                mLdDialog.dismiss();
//                showNetErrorToast();
//            }
//        };
//        HashMap<String, String> option = new HashMap<>();
//        option.put("emobIdTo", emobid);
//        service.removeBlack(PreferencesUtil.getCommityId(this), PreferencesUtil.getLoginInfo(this).getEmobId(), option, callback);
    }


}
