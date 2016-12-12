package xj.property.activity.repair;

import android.os.Bundle;
import android.view.View;
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
import xj.property.adapter.XJBaseAdapter;
import xj.property.beans.ValueSheetBean;
import xj.property.beans.ValueSheetV3Bean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.other.PreferencesUtil;

/**
 * Created by Administrator on 2015/3/20.
 */
public class ValueSheetActivity extends HXBaseActivity {
    ListView lv_tools;
    LinearLayout ll_item;
    XJBaseAdapter adapter;
    List<ValueSheetV3Bean> bean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valuesheet);
        ll_item=  (LinearLayout)   findViewById(R.id.ll_item);
        lv_tools=(ListView)findViewById(R.id.lv_tools);
        getValueSheetList();
        initTitle(null,"报价表","");
    }

    interface ValueSheetService {
        ///api/v1/communities/{communityId}/shops/quotation
        @GET("/api/v1/communities/{communityId}/shops/quotation")
        void getValueSheet(@Path("communityId") long communityId, Callback<ValueSheetBean> cb);

        @GET("/api/v3/repairs/items")
        void getValueSheetV3(@QueryMap Map<String, String> map, Callback<CommonRespBean<List<ValueSheetV3Bean>>> cb);
    }


    private void initItem(){
        for(int i=0;i<bean.size();i++) {
            View view=(LinearLayout)View.inflate(this,R.layout.item_sheet,null);
            TextView tv_left=(TextView)view.findViewById(R.id.tv_left);
            ll_item.addView(view);
            final int position=i;
            tv_left.setText(bean.get(i).getCatName());
            tv_left.setTextColor(getResources().getColor(R.color.sheet_text_normal));
            tv_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.changeData(bean.get(position).getItems());
                    for(int i=0;i<ll_item.getChildCount();i++){
//                        ll_item.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.sheet_normal));
//                        ((TextView)ll_item.getChildAt(i)).setTextColor(getResources().getColor(R.color.sheet_text_normal));
                        ((LinearLayout)ll_item.getChildAt(i)).getChildAt(0).setBackgroundColor(getResources().getColor(R.color.sheet_normal));
                        ((TextView)v).setTextColor(getResources().getColor(R.color.sheet_text_normal));
                    }
                    v.setBackgroundColor(getResources().getColor(R.color.white));
                    ((TextView)v).setTextColor(getResources().getColor(R.color.sheet_text_pressed));

                }
            });
            if(i==0)tv_left.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    private void getValueSheetList() {
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("communityId",PreferencesUtil.getCommityId(this)+"");
        ValueSheetService service = RetrofitFactory.getInstance().create(getmContext(), map, ValueSheetService.class);
        Callback<CommonRespBean<List<ValueSheetV3Bean>>> callback = new Callback<CommonRespBean<List<ValueSheetV3Bean>>>() {
            @Override
            public void success(CommonRespBean<List<ValueSheetV3Bean>> commonRespBean, retrofit.client.Response response) {
                if(commonRespBean!=null&&commonRespBean.getData()!=null) {
                    bean = commonRespBean.getData();
                    initItem();
                    adapter = new XJBaseAdapter(ValueSheetActivity.this, R.layout.item_tools, bean.get(0).getItems(),
                            new String[]{"serviceName", "price"});
                    lv_tools.setAdapter(adapter);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        service.getValueSheetV3(map, callback);
    }
}
