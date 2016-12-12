package xj.property.activity.repair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.adapter.RepairAdapter;
import xj.property.adapter.XJBaseAdapter;
import xj.property.beans.RepairMenuBeanV3;
import xj.property.beans.RepairMenuListBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.CommonUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

/**
 * Created by Administrator on 2015/3/16.
 */
public class RepairListActivity extends HXBaseActivity {
    ListView lvRepair;
    XJBaseAdapter adapter;
    RepairAdapter repairAdapter;
    private LinearLayout ll_errorpage;
    private LinearLayout ll_nomessage;
    private LinearLayout ll_neterror;
    private TextView tv_getagain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix);
        initTitle(null, "维修", "历史");
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
                    if (!CommonUtils.isNetWorkConnected(RepairListActivity.this)){
                        return;
                    }else {
                        ll_errorpage.setVisibility(View.GONE);
                        getRepairMenuList();
                    }
                }
            });
        }else {
            ll_errorpage.setVisibility(View.GONE);
            getRepairMenuList();
        }
    }

    private void initView() {
        lvRepair = (ListView) findViewById(R.id.listView);
        ll_neterror=(LinearLayout)findViewById(R.id.ll_neterror);
        tv_getagain=(TextView)findViewById(R.id.tv_getagain);
        ll_nomessage=(LinearLayout)findViewById(R.id.ll_nomessage);
        ll_errorpage=(LinearLayout)findViewById(R.id.ll_errorpage);

        lvRepair.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(RepairListActivity.this, RepairUncleListActivity.class);
                it.putExtra(Config.INTENT_PARMAS1, repairAdapter.getItem(position));
                startActivity(it);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_right_text) {
            if(PreferencesUtil.getLogin(RepairListActivity.this)){
                startActivity(new Intent(this, OrderHistoryActivity.class));
            }else {
                Intent intent=new Intent(RepairListActivity.this, RegisterLoginActivity.class);
                startActivity(intent);
            }

        }
    }

    /**
     * 获取ActList部分
     */
    interface RepairtService {
        ///api/v1/communities/{communityId}/users/{emobId}/itemCategories?q={shopType}
        @GET("/api/v1/communities/{communityId}/users/{emobId}/itemCategories")
        void getRepairList(@Path("communityId") long communityId, @Path("emobId") String emobId, @QueryMap Map<String, Integer> map, Callback<RepairMenuListBean> cb);

        @GET("/api/v3/repairs/categories")
        void getRepairListV3(@QueryMap Map<String, String> map, Callback<CommonRespBean<List<RepairMenuBeanV3>>> cb);

    }

    private void getRepairMenuList() {
        mLdDialog.show();
//        final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
//        RepairtService service = restAdapter.create(RepairtService.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("communityId", PreferencesUtil.getCommityId(getApplicationContext())+"");
        RepairtService service = RetrofitFactory.getInstance().create(getApplicationContext(), map, RepairtService.class);
        Callback<CommonRespBean<List<RepairMenuBeanV3>>> callback = new Callback<CommonRespBean<List<RepairMenuBeanV3>>>() {
            @Override
            public void success(CommonRespBean<List<RepairMenuBeanV3>> bean, retrofit.client.Response response) {
                mLdDialog.dismiss();
                //adapter = new XJBaseAdapter(RepairListActivity.this, R.layout.item_repair, bean.getInfo(), new String[]{"catName", "catDesc"}, new String[]{"getImgName"},true);
                if (bean!=null){
                    ll_errorpage.setVisibility(View.GONE);
                    repairAdapter = new RepairAdapter(RepairListActivity.this, bean.getData());
                    lvRepair.setAdapter(repairAdapter);
                }else {
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
        //PreferencesUtil.getLoginInfo(this).getEmobId()

        service.getRepairListV3(map, callback);
    }
}
