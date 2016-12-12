package xj.property.activity.takeout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.ChatActivity;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.LoginDialogActivity;
import xj.property.adapter.EvaAdapter;
import xj.property.beans.EvaBean;
import xj.property.beans.EvaResult;
import xj.property.beans.EvaResults;
import xj.property.utils.CommonUtils;
import xj.property.utils.message.XJContactHelper;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.pullrefreshview.library.PullToRefreshBase;
import xj.property.widget.pullrefreshview.library.PullToRefreshListView;

/**
 * Created by Administrator on 2015/5/5.
 */
public class EvaActivity extends HXBaseActivity {
    ArrayList<EvaBean> evaBeans;
    TextView tvShopName;
    ImageView ivShopScoer;
    ImageView ivShopLogo;
    PullToRefreshListView lv_eva;
    EvaAdapter adapter;
    private int pageIndex = 1;
    private LinearLayout ll_errorpage;
    private LinearLayout ll_nomessage;
    private LinearLayout ll_neterror;
    private TextView tv_getagain;

    private LinearLayout ll_top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eva);
        initTitle(null, "评价", null);
        evaBeans = new ArrayList<>();
        Log.i("onion", "shopEmoid: " + getIntent().getStringExtra(Config.INTENT_PARMAS1));
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
                    if (!CommonUtils.isNetWorkConnected(EvaActivity.this)) {
                        return;
                    } else {
                        ll_errorpage.setVisibility(View.GONE);
                        getEvaList();
                    }
                }
            });
        } else {
            ll_errorpage.setVisibility(View.GONE);
            getEvaList();
        }
    }


    private void initView() {
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        tv_getagain = (TextView) findViewById(R.id.tv_getagain);
        ll_nomessage = (LinearLayout) findViewById(R.id.ll_nomessage);
        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);
        tvShopName = (TextView) findViewById(R.id.tv_shop_name);
        ivShopScoer = (ImageView) findViewById(R.id.iv_evastar);
        ivShopLogo = (ImageView) findViewById(R.id.iv_shoplogo);
        lv_eva = (PullToRefreshListView) findViewById(R.id.lv_eva);
        ll_top=(LinearLayout)findViewById(R.id.ll_top);



        adapter = new EvaAdapter(evaBeans, this);
        lv_eva.getRefreshableView().setAdapter(adapter);
        lv_eva.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                pageIndex = 1;
                mLdDialog.show();
                new GetDataTask().execute();
            }
        });
        lv_eva.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                pageIndex++;
                mLdDialog.show();
                new GetDataTask().execute();

            }
        });

        lv_eva.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (PreferencesUtil.getLogin(EvaActivity.this)) {

                    Intent intentPush = new Intent();
                    EvaBean evaBean = evaBeans.get(position - 1);
                    intentPush.setClass(EvaActivity.this, ChatActivity.class);
                    intentPush.putExtra("userId", evaBean.getEmobId());//tz
                    intentPush.putExtra(Config.EXPKey_nickname, evaBean.getNickname());
                    intentPush.putExtra(Config.EXPKey_avatar, evaBean.getAvatar());
                    intentPush.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                    XJContactHelper.saveContact(evaBean.getEmobId(), evaBean.getNickname(), evaBean.getAvatar(), "-1");
                    if (!evaBean.getEmobId().equals(PreferencesUtil.getLoginInfo(EvaActivity.this).getEmobId())){
                        startActivity(intentPush);
                    }else {
                        Toast.makeText(EvaActivity.this,"这是你自己！",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    //showToast("请先登录");
                    //LoginDialogUtil.showLoginDialog(this);
                    Intent intent = new Intent(EvaActivity.this, LoginDialogActivity.class);
                    startActivity(intent);
                }

            }
        });
    }


    private class GetDataTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // Simulates a background job.
            if (pageIndex==1){
                getEvaList();
            }else {
                getData(pageIndex);
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            lv_eva.onRefreshComplete();
            mLdDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {

    }

    interface getEvaListService {
        ///api/v1/communities/{communityId}/shops/{emobId}/comments/detail
        @GET("/api/v1/communities/{communityId}/shops/{emobId}/comments/detail")
        void getEvaList(@Path("communityId") int communityId, @Path("emobId") String emobId, @QueryMap HashMap<String, String> option, Callback<EvaResult> cb);
    }

    public void getEvaList() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        getEvaListService service = restAdapter.create(getEvaListService.class);
        Callback<EvaResult> callback = new Callback<EvaResult>() {
            @Override
            public void success(EvaResult result, retrofit.client.Response response) {
                if ("yes".equals(result.getStatus())) {
                    tvShopName.setText(result.getInfo().getShopName());
                    ImageLoader.getInstance().displayImage(result.getInfo().getLogo(), ivShopLogo);
                    try {
                        Log.i("onion","score :"+Integer.parseInt(result.getInfo().getScore()));

                        switch (Integer.parseInt(result.getInfo().getScore())) {
                            case 0:
                                ivShopScoer.setImageResource(R.drawable.evaluate_star_zero);
                                break;
                            case 1:
                                ivShopScoer.setImageResource(R.drawable.evaluate_star_one);
                                break;
                            case 2:
                                ivShopScoer.setImageResource(R.drawable.evaluate_star_two);
                                break;
                            case 3:
                                ivShopScoer.setImageResource(R.drawable.evaluate_star_three);
                                break;
                            case 4:
                                ivShopScoer.setImageResource(R.drawable.evaluate_star_four);
                                break;
                            case 5:
                                ivShopScoer.setImageResource(R.drawable.evaluate_star_five);
                                break;
                        }
                    } catch (Exception e) {
                        ivShopScoer.setImageResource(R.drawable.evaluate_star_one);
                    }
                    evaBeans.clear();
                    evaBeans.addAll(result.getInfo().getList().getPageData());
                    adapter.notifyDataSetChanged();
                    if (evaBeans.size()!=0){
                        ll_top.setVisibility(View.VISIBLE);
                        ll_errorpage.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }else {
                        ll_errorpage.setVisibility(View.VISIBLE);
                        ll_neterror.setVisibility(View.GONE);
                        ll_nomessage.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                showNetErrorToast();
                error.printStackTrace();
            }
        };
        HashMap<String, String> option = new HashMap<>();
        option.put("pageNum", "1");
        option.put("pageSize", "10");
        Log.i("onion","shopId"+getIntent().getStringExtra(Config.INTENT_PARMAS1));
        service.getEvaList(PreferencesUtil.getCommityId(this), getIntent().getStringExtra(Config.INTENT_PARMAS1), option, callback);
    }



    interface getEvaListService2 {
        ///api/v1/communities/{communityId}/shops/{emobId}/comments/detail
        @GET("/api/v1/communities/{communityId}/shops/{emobId}/comments/detail")
        void getEvaList(@Path("communityId") int communityId, @Path("emobId") String emobId, @QueryMap HashMap<String, String> option, Callback<EvaResults> cb);
    }
    protected void getData(final int pageIndex) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        getEvaListService2 service2 = restAdapter.create(getEvaListService2.class);
        Callback<EvaResults> callback = new Callback<EvaResults>() {
            @Override
            public void success(EvaResults result, retrofit.client.Response response) {
                if ("yes".equals(result.getStatus())) {
                   if (result.getInfo().getPageCount()>= pageIndex) {
                        if (evaBeans != null) {
                            evaBeans.addAll(result.getInfo().getPageData());
                            adapter.notifyDataSetChanged();
                            if (evaBeans.size()!=0){
                                ll_top.setVisibility(View.VISIBLE);
                                ll_errorpage.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();
                            }else {
                                ll_errorpage.setVisibility(View.VISIBLE);
                                ll_neterror.setVisibility(View.GONE);
                                ll_nomessage.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                showNetErrorToast();
                //Toast.makeText(EvaActivity.this, "系统出错", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        };
        HashMap<String, String> option = new HashMap<>();
        option.put("pageNum", pageIndex + "");
        option.put("pageSize", "10");
        service2.getEvaList(PreferencesUtil.getCommityId(this), getIntent().getStringExtra(Config.INTENT_PARMAS1), option, callback);
    }

}
