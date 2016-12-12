package xj.property.activity.call;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
import xj.property.adapter.XJBaseAdapter;
import xj.property.beans.CallShopBean;
import xj.property.beans.CallShopListBean;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

/**
 * Created by Administrator on 2015/3/25.
 */
public class CallActivity extends HXBaseActivity {
    ListView lv_Call;
    XJBaseAdapter CallAdapter;
    ArrayList<CallShopBean> shopList=new ArrayList<CallShopBean>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        lv_Call=(ListView)findViewById(R.id.lv_call);
        initTitle(null,getIntent().getStringExtra(Config.INTENT_PARMAS1),"");
        getCallList(getIntent().getIntExtra(Config.XJKEY_SHOP, 4));
        if(getIntent().getIntExtra(Config.XJKEY_SHOP, 4)!=12) {
            CallAdapter = new XJBaseAdapter(this, R.layout.item_callshop, shopList, new String[]{"shopName", "phone", "businessStartTime", "businessEndTime"});//
        }else{
            findViewById(R.id.ll_express).setVisibility(View.VISIBLE);
            CallAdapter = new XJBaseAdapter(this, R.layout.item_callshop, shopList, new String[]{"shopName", "phone", "businessStartTime", "businessEndTime"}, new String[]{"getLogo"}, new int[]{R.id.iv_circleshop});//
        }
        lv_Call.setAdapter(CallAdapter);
        lv_Call.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent phoneIntent = new Intent(
                        "android.intent.action.CALL", Uri.parse("tel:"
                        + shopList.get(position).getPhone()));
                startActivity(phoneIntent);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 获取ActList部分
     */
    interface CallListService {
        ///api/v1/communities/1/shops/4?pageNum=1&pageSize=10
        @GET("/api/v1/communities/{communityId}/shops/{sort}?pageNum=1&pageSize=10")
        void getCallList(@Path("communityId") long communityId, @Path("sort") int sort, @QueryMap Map<String, String> map, Callback<CallShopListBean> cb);
    }

    private void getCallList(int sort) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        CallListService service = restAdapter.create(CallListService.class);
        mLdDialog.show();
        Callback<CallShopListBean> callback = new Callback<CallShopListBean>() {
            @Override
            public void success(CallShopListBean bean, retrofit.client.Response response) {
                mLdDialog.dismiss();
                shopList.clear();
                shopList.addAll(bean.getPageData());
                CallAdapter  .notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                error.printStackTrace();
                showToast(error.toString());
            }
        };
        Map<String,String> option=new HashMap<String,String>();
        option.put("pageNum","1");
        option.put("pageSize","10");
        service.getCallList(PreferencesUtil.getCommityId(this), sort, option, callback);
    }
}
