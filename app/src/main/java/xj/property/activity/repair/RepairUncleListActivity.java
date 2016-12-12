package xj.property.activity.repair;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.ChatActivity;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.adapter.RepairUncleAdapter;
import xj.property.beans.RepairMenuBeanV3;
import xj.property.beans.RepairUncleBean;
import xj.property.beans.RepairUncleV3Bean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.CommonUtils;
import xj.property.utils.message.XJContactHelper;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.pullrefreshview.library.PullToRefreshBase;
import xj.property.widget.pullrefreshview.library.PullToRefreshListView;

/**
 * Created by Administrator on 2015/3/16.
 */
public class RepairUncleListActivity extends HXBaseActivity {
    private RepairMenuBeanV3 bean;
    private RepairUncleAdapter adapter;
    // private ListView lvUncle;
    private PullToRefreshListView lvUncle;
    private int pageIndex = 1;
    private ArrayList<RepairUncleV3Bean.RepairUncleV3DataBean> items = new ArrayList<RepairUncleV3Bean.RepairUncleV3DataBean>();
    private UserInfoDetailBean userbean;
    private LinearLayout ll_errorpage;
    private LinearLayout ll_nomessage;
    private LinearLayout ll_neterror;
    private TextView tv_getagain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_master);
        bean = (RepairMenuBeanV3) getIntent().getSerializableExtra(Config.INTENT_PARMAS1);
        Log.i("onion", "catid: " + bean.getCatId());
        userbean = PreferencesUtil.getLoginInfo(RepairUncleListActivity.this);
        initTitle(null, bean.getCatDesc(), "");
        initView();
        initData();
        initListenner();
    }

    private void initListenner() {
        lvUncle.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                pageIndex = 1;
                new GetDataTask().execute();
            }
        });
        lvUncle.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                pageIndex++;
                lvUncle.mFooterLoadingView.setVisibility(View.VISIBLE);
                lvUncle.mFooterLoadingView.refreshing();
                new GetDataTask().execute();

            }
        });
        lvUncle.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // if( ! checkLogin())return;
                if (PreferencesUtil.getLogin(RepairUncleListActivity.this)) {
                    Intent intentPush = new Intent();
                    intentPush.setClass(RepairUncleListActivity.this, RepairChatActivity.class);
//                intentPush.putExtra("userId", "tz");
                    RepairUncleV3Bean.RepairUncleV3DataBean bean = (RepairUncleV3Bean.RepairUncleV3DataBean) adapter.getItem(position - 1);
//                intentPush.putExtra("userId", bean.getCommunityId());
                    intentPush.putExtra("userId", bean.getEmobId());//tz
                    intentPush.putExtra("shopId",bean.getShopId());
                    intentPush.putExtra(Config.EXPKey_nickname, bean.getShopName());
                    intentPush.putExtra(Config.EXPKey_avatar, bean.getLogo());
                    intentPush.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                    Log.i("onion", "bean" + bean);
                    XJContactHelper.saveContact(bean.getEmobId(), bean.getShopName(), bean.getLogo(), "5");
//                intentPush.putExtra("CMD_CODE", 200);
//
//                buildCmdDetail();
//                logger.info("cmd detail is :" + cmdDetail);
//
//                intentPush.putExtra("CMD_DETAIL", cmdDetail);
                    startActivity(intentPush);
                } else {
                    Intent intent = new Intent(RepairUncleListActivity.this, RegisterLoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void initData() {
        if (!CommonUtils.isNetWorkConnected(this)) {
            ll_errorpage.setVisibility(View.VISIBLE);
            ll_neterror.setVisibility(View.VISIBLE);
            ll_nomessage.setVisibility(View.GONE);
            tv_getagain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!CommonUtils.isNetWorkConnected(RepairUncleListActivity.this)) {
                        return;
                    } else {
                        ll_errorpage.setVisibility(View.GONE);
                        getRepairUncleList();
                    }
                }
            });
        } else {
            ll_errorpage.setVisibility(View.GONE);
            getRepairUncleList();
        }
    }

    private void initView() {
        lvUncle = (PullToRefreshListView) findViewById(R.id.listView);
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        tv_getagain = (TextView) findViewById(R.id.tv_getagain);
        ll_nomessage = (LinearLayout) findViewById(R.id.ll_nomessage);
        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);
        adapter = new RepairUncleAdapter(RepairUncleListActivity.this, items);
        lvUncle.getRefreshableView().setAdapter(adapter);
    }


    private class GetDataTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // Simulates a background job.
            getData(pageIndex);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }
    }

//    private boolean checkLogin() {
//        if (PreferencesUtil.getLogin(this)) {
//            View rootView = View.inflate(this, R.layout.dialog_userlogin, null);
//            final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this).setView(rootView).create();
//            rootView.findViewById(R.id.dialog_cancel_btn).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startActivity(new Intent(RepairUncleListActivity.this, UsernameActivity.class));
//                }
//            });
//            rootView.findViewById(R.id.dialog_submit_btn).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//        }
//        return PreferencesUtil.getLogin(this);
//    }

//    @Override
//    public void onClick(View v) {
//
//    }

    /**
     * 获取Repair部分
     */
    interface RepairtService {
        //api/v1/communities/1/shops?q=5&pageNum=1&pageSize=10
//        @GET("/api/v1/communities/{communityId}/shopsCategory/{catid}")
//        void getRepairList(@Path("communityId") long communityId, @Path("catid") int catid, @QueryMap Map<String, String> map, Callback<RepairUncleBean> cb);

        @GET("/api/v3/repairs/shops")
        void getRepairListV3(@QueryMap Map<String, String> map, Callback<CommonRespBean<RepairUncleV3Bean>> cb);

    }

    private void getRepairUncleList() {
        mLdDialog.show();

        HashMap<String,String> map = new HashMap<String,String>();
        map.put("communityId",PreferencesUtil.getCommityId(getApplicationContext())+"");
        map.put("catId",bean.getCatId()+"");
        map.put("page","1");
        map.put("limit","10");
        RepairtService service = RetrofitFactory.getInstance().create(getApplicationContext(),map,RepairtService.class);
//        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE2).build();
//        RepairtService service = restAdapter.create(RepairtService.class);
        Callback<CommonRespBean<RepairUncleV3Bean>> callback = new Callback<CommonRespBean<RepairUncleV3Bean>>() {
            @Override
            public void success(CommonRespBean<RepairUncleV3Bean> bean, retrofit.client.Response response) {
                mLdDialog.dismiss();
                items.clear();
                items.addAll((ArrayList) bean.getData().getData());
                if (items.size() != 0) {
                    ll_errorpage.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                } else {
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
        };
//        service.getRepairList(PreferencesUtil.getCommityId(this), bean.getCatId(), option, callback);
        service.getRepairListV3(map,callback);
    }


    protected void getData(final int pageIndex) {
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("communityId",PreferencesUtil.getCommityId(getApplicationContext())+"");
        map.put("catId",bean.getCatId()+"");
        map.put("page", pageIndex + "");
        map.put("limit", "10");
        RepairtService service = RetrofitFactory.getInstance().create(getApplicationContext(),map,RepairtService.class);
        Callback<CommonRespBean<RepairUncleV3Bean>> callback = new Callback<CommonRespBean<RepairUncleV3Bean>>() {
            @Override
            public void success(CommonRespBean<RepairUncleV3Bean> bean, retrofit.client.Response response) {
                if (pageIndex == 1) {
                    items.clear();
                    items.addAll(bean.getData().getData());
                } else if (bean.getData().getPage() >= pageIndex) {
                    if (items != null) {
                        items.addAll(bean.getData().getData());
                    }
                }
                adapter.notifyDataSetChanged();
                lvUncle.onRefreshComplete();
                if(bean.getData().getData().isEmpty()) {
                    lvUncle.mFooterLoadingView.setVisibility(View.GONE);
                    showNoMoreToast();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                lvUncle.onRefreshComplete();
                showNetErrorToast();
                error.printStackTrace();
            }
        };
        service.getRepairListV3(map,callback);
    }

}
