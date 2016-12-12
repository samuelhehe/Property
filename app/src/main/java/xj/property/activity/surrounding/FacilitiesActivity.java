package xj.property.activity.surrounding;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.adapter.FacilitiesAdapter;
import xj.property.beans.FacilitiesBean;
import xj.property.beans.FacilitiesBeans;
import xj.property.utils.CommonUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.pullrefreshview.library.PullToRefreshBase;
import xj.property.widget.pullrefreshview.library.PullToRefreshListView;

/**
 * Created by Administrator on 2015/4/1.
 */
public class FacilitiesActivity extends HXBaseActivity {
    PullToRefreshListView lv_facilitieslist;
    FacilitiesAdapter adapter;
    ArrayList<FacilitiesBean> list;

    private int pageIndex = 1;

    private LinearLayout ll_errorpage;
    private LinearLayout ll_nomessage;
    private LinearLayout ll_neterror;
    private TextView tv_getagain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilities_list);
        initView();
        initTitle(null, getIntent().getStringExtra(Config.INTENT_PARMAS2), "");
        initData();
        //Log.i("onion",1/0+"");
        //getFacilitiesList();

    }

    private void initData() {
        if (!CommonUtils.isNetWorkConnected(this)) {
            ll_errorpage.setVisibility(View.VISIBLE);
            ll_neterror.setVisibility(View.VISIBLE);
            ll_nomessage.setVisibility(View.GONE);
            tv_getagain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!CommonUtils.isNetWorkConnected(FacilitiesActivity.this)) {
                        return;
                    } else {
                        ll_errorpage.setVisibility(View.GONE);
                        getFacilitiesList();
                    }
                }
            });
        } else {
            ll_errorpage.setVisibility(View.GONE);
            getFacilitiesList();
        }
    }

    private void initView() {

        lv_facilitieslist = (PullToRefreshListView) findViewById(R.id.lv_facilitieslist);

        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        tv_getagain = (TextView) findViewById(R.id.tv_getagain);
        ll_nomessage = (LinearLayout) findViewById(R.id.ll_nomessage);
        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);


        list = new ArrayList<FacilitiesBean>();
        adapter = new FacilitiesAdapter(this, list);
        lv_facilitieslist.getRefreshableView().setAdapter(adapter);
        lv_facilitieslist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                pageIndex = 1;
                new GetDataTask().execute();
            }
        });
        lv_facilitieslist.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (pageIndex==1){
                    lv_facilitieslist.getRefreshableView().setSelection(lv_facilitieslist.getRefreshableView().getCount());
                }
                pageIndex++;
                lv_facilitieslist.mFooterLoadingView.setVisibility(View.VISIBLE);
                lv_facilitieslist.mFooterLoadingView.refreshing();
                new GetDataTask().execute();

            }
        });

        lv_facilitieslist.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(FacilitiesActivity.this, FacilityContentActivity.class);
                it.putExtra(Config.INTENT_PARMAS1, list.get(position - 1));
                startActivity(it);
            }
        });

    }

    private class GetDataTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            getData(pageIndex);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 获取Repair部分
     */
    interface FacilitiesService {
        ///api/v1/communities/1/facilities?facilityClassId=24&pageNum=1&pageSize=10
        @GET("/api/v1/communities/{communityId}/facilities")
        void getFacilities(@Path("communityId") long communityId, @QueryMap Map<String, String> map, Callback<FacilitiesBeans> cb);
    }

    private void getFacilitiesList() {
        mLdDialog.show();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE2).build();
        FacilitiesService service = restAdapter.create(FacilitiesService.class);
        Callback<FacilitiesBeans> callback = new Callback<FacilitiesBeans>() {
            @Override
            public void success(FacilitiesBeans bean, retrofit.client.Response response) {
                mLdDialog.dismiss();
                if (null == bean || !"yes".equals(bean.getStatus())) return;
                if (bean.getInfo().getPageData().size() == 0) {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_neterror.setVisibility(View.GONE);
                    ll_nomessage.setVisibility(View.VISIBLE);
                } else {
                    ll_errorpage.setVisibility(View.GONE);
                    list.clear();
                    list.addAll(bean.getInfo().getPageData());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
//                error.printStackTrace();
//                showToast(error.toString());
                showNetErrorToast();
            }
        };
        Map<String, String> option = new HashMap<String, String>();
        option.put("facilityClassId", getIntent().getIntExtra(Config.INTENT_PARMAS1, 1) + "");
        option.put("pageNum", "1");
        option.put("pageSize", "10");
        service.getFacilities(PreferencesUtil.getCommityId(this), option, callback);
    }


    protected void getData(final int pageIndex) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE2).build();
        FacilitiesService service = restAdapter.create(FacilitiesService.class);
        Callback<FacilitiesBeans> callback = new Callback<FacilitiesBeans>() {
            @Override
            public void success(FacilitiesBeans bean, retrofit.client.Response response) {
                if (null == bean || !"yes".equals(bean.getStatus())) return;
                if (pageIndex == 1) {
                    list.clear();
                    list.addAll(bean.getInfo().getPageData());
                } else if (bean.getInfo().getPageCount() >= pageIndex) {
                    if (list != null) {
                        list.addAll(bean.getInfo().getPageData());
                    }
                }
                adapter.notifyDataSetChanged();
                lv_facilitieslist.onRefreshComplete();
                if(bean.getInfo().getPageData().isEmpty()) {
                    lv_facilitieslist.mFooterLoadingView.setVisibility(View.GONE);
                    showNoMoreToast();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                lv_facilitieslist.onRefreshComplete();
                showNetErrorToast();
                error.printStackTrace();
            }
        };
        Map<String, String> option = new HashMap<String, String>();
        option.put("facilityClassId", getIntent().getIntExtra(Config.INTENT_PARMAS1, 1) + "");
        option.put("pageNum", pageIndex + "");
        option.put("pageSize", "10");
        service.getFacilities(PreferencesUtil.getCommityId(this), option, callback);
    }
}
