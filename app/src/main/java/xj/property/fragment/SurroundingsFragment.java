package xj.property.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import xj.property.R;
import xj.property.XjApplication;
import xj.property.activity.activities.IndexSearchResultActivity;
import xj.property.activity.surrounding.FacilitiesActivity;
import xj.property.adapter.SurroundingAdapter;
import xj.property.beans.FacilityBean;
import xj.property.beans.FacilityListBean;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

public class SurroundingsFragment extends Fragment implements OnItemClickListener {

    /**
     * logger
     */
    /**
     * grid view
     */
    private GridView gv_main;
    /**
     * adapter
     */
    private SurroundingAdapter surroundingAdapter;
    /**
     * facility bean list, as a datasource
     */
    List<FacilityBean> facilityBeanList = new ArrayList<>();
    private Handler handler;
    private XjApplication app;
    TextView query;
    ImageView no_message ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        app = (XjApplication) getActivity().getApplication();
        //  Log.i("onion"," onCreateView:  width: "+app.getGrideWidth()+"height: "+app.getGrideHeight());
        return inflater.inflate(R.layout.fragment_circum, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gv_main = (GridView) getView().findViewById(R.id.gv_main);
        no_message = (ImageView) getView().findViewById(R.id.no_message);
        gv_main.setOnItemClickListener(this);
        query = (TextView) getView().findViewById(R.id.query);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //intent.putExtra("searchName",query.getText()+"");
                intent.setClass(getActivity(), IndexSearchResultActivity.class);
                startActivity(intent);
            }
        });
        query.setKeyListener(null);
    }

    private void initCache() {
        List<FacilityBean> list = PreferencesUtil.getFacilityBean(getActivity());
        if (facilityBeanList != null) {
            facilityBeanList.clear();
            facilityBeanList.addAll(list);
        }
        surroundingAdapter.notifyDataSetChanged();


        handler.sendEmptyMessageDelayed(2, 200);
    }

    private void initTitle() {
//        getView().findViewById(R.id.iv_back).setVisibility(View.GONE);
//      TextView tv=(TextView)  getView().findViewById(R.id.tv_title);
//    tv.setText("周边");
    }

//    @Override
//    public boolean getUserVisibleHint() {
//     boolean flag=   super.getUserVisibleHint();
//        if(flag){
//            ( (MainActivity)  getActivity()).indexFragment.eventService();
//
//        }
//        return flag;
//    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        Intent it = new Intent(getActivity(), FacilitiesActivity.class);
        it.putExtra(Config.INTENT_PARMAS1, facilityBeanList.get(position).getFacilitiesClassId());
        it.putExtra(Config.INTENT_PARMAS2, facilityBeanList.get(position).getFacilitiesClassName());
        startActivity(it);
//        switch (position) {
//        case 0:
//            Intent lostIntent=new Intent(getActivity(), FoodActivity.class);
//            startActivity(lostIntent);
//            break;
////            Intent lostIntent=new Intent(MainActivity.this, LostProtectedActivity.class);
////            startActivity(lostIntent);
//        default:
//
//            break;
//        }
    }

    /**
     * 获取周边设施service构建
     */
    interface FcilityListService {
        // @GET("/api/v1/communities/{communityId}/facilitiesClass/")
        @GET("/api/v1/communities/{communityId}/facilityClasses")
        void listFacilities(@Path("communityId") long communityId, Callback<FacilityListBean> cb);
    }

    /**
     * 获取周边设施
     */
    private void getFacilityList() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Config.NET_BASE2)
                .build();
        FcilityListService facilityListService = restAdapter.create(FcilityListService.class);

        Callback<FacilityListBean> callback = new Callback<FacilityListBean>() {
            @Override
            public void success(FacilityListBean facilityListBean, Response response) {
                boolean changeflag = false;
                List<FacilityBean> loadbeans = facilityListBean.getInfo();
                for (int i = 0; i < loadbeans.size(); i++) {
                    if (!facilityBeanList.contains(loadbeans.get(i))) {
                        changeflag = true;
                        break;
                    }
                }
                if(facilityListBean.getInfo().size()<=0){
                    no_message.setVisibility(View.VISIBLE);
                    gv_main.setVisibility(View.GONE);
                }else {
                    no_message.setVisibility(View.GONE);
                    gv_main.setVisibility(View.VISIBLE);
                }
                if (!changeflag) return;
                PreferencesUtil.saveFacilityBean(getActivity(), loadbeans);
                facilityBeanList.clear();
                facilityBeanList.addAll(facilityListBean.getInfo());
                surroundingAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
            }
        };
        facilityListService.listFacilities(PreferencesUtil.getCommityId(getActivity()), callback);
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //Log.i("onion", " onResume:  width: " + app.getGrideWidth() + "height: " + app.getGrideHeight());

                surroundingAdapter = new SurroundingAdapter(getActivity(), facilityBeanList,app.getGrideWidth(),app.getGrideHeight());
                gv_main.setAdapter(surroundingAdapter);

                handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case 2:
                                getFacilityList();
                                break;
                        }
                    }
                };
                initCache();
                //  initTitle();

            }
        }, 500);
    }
}
