package xj.property.fragment;//package xj.property.fragment;
//
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.text.format.DateUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.activeandroid.query.Select;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import retrofit.Callback;
//import retrofit.RestAdapter;
//import retrofit.RetrofitError;
//import retrofit.client.Response;
//import retrofit.http.GET;
//import retrofit.http.Path;
//import retrofit.http.QueryMap;
//import xj.property.R;
//import xj.property.XjApplication;
//import xj.property.activity.surrounding.PanicBuyingActivity;
//import xj.property.adapter.PeripheryAdapter;
//import xj.property.beans.PanicBuyingDetailBean;
//import xj.property.beans.PublishedBuyingBean;
//import xj.property.cache.PanicBuyingItemInfo;
//import xj.property.utils.CommonUtils;
//import xj.property.utils.other.Config;
//import xj.property.utils.other.PreferencesUtil;
//import xj.property.widget.pullrefreshview.library.PullToRefreshBase;
//import xj.property.widget.pullrefreshview.library.PullToRefreshListView;
//
//public class PeripheryFragment extends Fragment {
//
//    private XjApplication app;
//    private PullToRefreshListView ptrf_listview;
//    //    private ListView lv_list_view;
//    private PeripheryAdapter peripheryAdapter;
//    private int pageIndex = 1;
//    private List<PanicBuyingDetailBean> panicBuyingDetailBeanList;
//    private ListView lv_ptrf;
//    private LinearLayout ll_errorpage;
////    private LinearLayout ll_empty;
//    private LinearLayout ll_nomessage;
//    private LinearLayout ll_neterror;
//    private TextView tv_getagain;
//    private String emobid;
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        app = (XjApplication) getActivity().getApplication();
//        View view = inflater.inflate(R.layout.fragment_periphery, container, false);
//        initView(view);
//        initData();
//        return view;
//    }
//
//    private void initData() {
//        if (!CommonUtils.isNetWorkConnected(getActivity())) {
//            ll_errorpage.setVisibility(View.VISIBLE);
//            ll_neterror.setVisibility(View.VISIBLE);
//            ll_nomessage.setVisibility(View.GONE);
//            tv_getagain.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (!CommonUtils.isNetWorkConnected(getActivity())) {
//                        return;
//                    } else {
//                        ll_errorpage.setVisibility(View.GONE);
//                        getPublishedBuyingInfo();
//                    }
//                }
//            });
//        } else {
//            ll_errorpage.setVisibility(View.GONE);
//            getPublishedBuyingInfo();
//        }
//    }
//
//    private void initView(View view) {
//        panicBuyingDetailBeanList = new ArrayList<>();
//
//        ll_neterror = (LinearLayout) view.findViewById(R.id.ll_neterror);
//        tv_getagain = (TextView) view.findViewById(R.id.tv_getagain);
//        ll_nomessage = (LinearLayout) view.findViewById(R.id.ll_no_messages);
//        ll_errorpage = (LinearLayout) view.findViewById(R.id.ll_errorpage);
////        ll_empty = (LinearLayout) view.findViewById(R.id.ll_empty);
//
//        ptrf_listview = (PullToRefreshListView) view.findViewById(R.id.ptrf_listview);
//        ptrf_listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
//        lv_ptrf = ptrf_listview.getRefreshableView();
//        peripheryAdapter = new PeripheryAdapter(getActivity(), lv_ptrf, panicBuyingDetailBeanList);
////        lv_list_view = (ListView) view.findViewById(R.id.lv_list_view);
//        ptrf_listview.getRefreshableView().setAdapter(peripheryAdapter);
////        lv_list_view.setAdapter(peripheryAdapter);
//
//
//        ptrf_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
//            @Override
//            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
//                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//                pageIndex = 1;
//                new GetDataTask().execute();
//            }
//        });
//        ptrf_listview.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
//            @Override
//            public void onLastItemVisible() {
//                if (pageIndex == 1) {
//                    ptrf_listview.getRefreshableView().setSelection(ptrf_listview.getRefreshableView().getCount());
//                }
//                pageIndex++;
//                ptrf_listview.mFooterLoadingView.setVisibility(View.VISIBLE);
//                ptrf_listview.mFooterLoadingView.refreshing();
//                new GetDataTask().execute();
//
//            }
//        });
//        //在此处判断用户是否登录
//
//        ptrf_listview.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                if (PreferencesUtil.getLoginInfo(getActivity()) == null) {
////                    Intent intent = new Intent(getActivity(), RegisterLoginActivity.class);
////                    startActivity(intent);
////                } else {
//
//                if(PreferencesUtil.getLogin(getActivity())){
//                    emobid = PreferencesUtil.getLoginInfo(getActivity()).getEmobId();
//                }else {
//                    emobid = PreferencesUtil.getlogoutEmobId(getActivity());
//                }
//
//                    long time = ((panicBuyingDetailBeanList.get(position - 1).getEndTime()) * 1000l) - (System.currentTimeMillis());
//                    Log.i("time_test_test1", System.currentTimeMillis() + "");
//                    Log.i("time_test_test2", (panicBuyingDetailBeanList.get(position - 1).getEndTime()) + "");
////                Log.i("panicBuyingDetailBeanList.get(position).getEndTime()_fragment",time+"");
//                    if (time < 0) {
//                        Toast.makeText(getActivity(), "抢购活动已过期！", Toast.LENGTH_SHORT).show();
//                    } else {
//
//                        PanicBuyingItemInfo  panicBuyingItemInfo= new Select().from(PanicBuyingItemInfo.class).where("crazySalesId = ? and emobId = ?", panicBuyingDetailBeanList.get(position-1).getCrazySalesId(),
//                                emobid).executeSingle();
//                        if(panicBuyingItemInfo == null){
//                            new PanicBuyingItemInfo(panicBuyingDetailBeanList.get(position-1).getCrazySalesId(),emobid).save();
//                        }
//
//                        Intent intent = new Intent(getActivity(), PanicBuyingActivity.class);
//                        intent.putExtra("crazySalesId", panicBuyingDetailBeanList.get(position - 1).getCrazySalesId());
//                        Log.i("crazySalesId----", "" + panicBuyingDetailBeanList.get(position - 1).getCrazySalesId());
//                        startActivity(intent);
////                        for (int i = 0; i < panicBuyingDetailBeanList.size(); i++) {
////                           PanicBuyingItemInfo  panicBuyingItemInfo= new Select().from(PanicBuyingItemInfo.class).where("crazySalesId = ?", panicBuyingDetailBeanList.get(i).getCrazySalesId()).executeSingle();
////                            if (panicBuyingItemInfo == null) {
////                                panicBuyingItemInfo = new PanicBuyingItemInfo(panicBuyingDetailBeanList.get(i).getCrazySalesId(), PreferencesUtil.getLoginInfo(getActivity()).getEmobId());
////                                panicBuyingItemInfo.save();
////                            }
////                        }
//                    }
////                    Intent intent = new Intent(getActivity(), PanicBuyingActivity.class);
////                    intent.putExtra("crazySalesId", panicBuyingDetailBeanList.get(position).getCrazySalesId());
////                    startActivity(intent);
//                }
////            }
//        });
////        lv_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                @Override
////                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                    Intent intent = new Intent(getActivity(), PanicBuyingActivity.class);
////                    intent.putExtra("crazySalesId", panicBuyingDetailBeanList.get(position).getCrazySalesId());
////                    Log.i("crazySalesId", panicBuyingDetailBeanList.get(position).getCrazySalesId() + "");
////                    Log.i("info", PreferencesUtil.getLoginInfo(getActivity()).getEmobId());
////                    Log.i("info", PreferencesUtil.getLoginInfo(getActivity()).getCommunityId() + "");
////                    startActivity(intent);
////                }
////            });
//
//    }
//
//    private class GetDataTask extends AsyncTask<Void, Void, Boolean> {
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            getMoreData(pageIndex);
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result) {
//            super.onPostExecute(result);
//        }
//    }
//
//    interface GetInfoService {
//        //        http://bangbang.ixiaojian.com/api/v1/crazysales/communities/{communityId}?pageNum=xx&pageSize=xxx
////        HTTP Method : GET
//        @GET("/api/v1/crazysales/communities/{communityId}")
//        void getInfoList(@Path("communityId") int communityId, @QueryMap Map<String, String> map, Callback<PublishedBuyingBean> cb);
//    }
//
//    public void getPublishedBuyingInfo() {
//        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
//        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
//        GetInfoService service = restAdapter.create(GetInfoService.class);
//        Callback<PublishedBuyingBean> callback = new Callback<PublishedBuyingBean>() {
//            @Override
//            public void success(PublishedBuyingBean publishedBuyingBean, Response response) {
////                lv_list_view.setVisibility(View.VISIBLE);
//                Log.i("success", "获取数据成功");
//                if (publishedBuyingBean != null && "yes".equals(publishedBuyingBean.getStatus())) {
//                    panicBuyingDetailBeanList.clear();
//                    panicBuyingDetailBeanList.addAll(publishedBuyingBean.getInfo().getPageData());
//
//                    Log.i("success", "" + panicBuyingDetailBeanList.size());
//                    peripheryAdapter.updateData(panicBuyingDetailBeanList);
//                    Log.i("debbug", "getCount()" + peripheryAdapter.getCount());
//                    peripheryAdapter.notifyDataSetChanged();
//
//                    if (publishedBuyingBean.getInfo().getPageData().size() == 0) {
//                        Log.i("success", "没有数据");
//                        ll_nomessage.setVisibility(View.VISIBLE);
//                        lv_ptrf.setVisibility(View.GONE);
//                    }else {
//                        ll_nomessage.setVisibility(View.GONE);
//                        lv_ptrf.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Toast.makeText(getActivity(), "请求数据失败，请稍后重试！", Toast.LENGTH_SHORT).show();
//            }
//        };
//        Map<String, String> map = new HashMap<>();
//        map.put("pageNum", "1");
//        map.put("pageSize", "10");
//        service.getInfoList(PreferencesUtil.getCommityId(getActivity()), map, callback);
//    }
//
//    public void getMoreData(final int pageIndex) {
//        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
//        GetInfoService service = restAdapter.create(GetInfoService.class);
//        Callback<PublishedBuyingBean> callback = new Callback<PublishedBuyingBean>() {
//            @Override
//            public void success(PublishedBuyingBean publishedBuyingBean, Response response) {
//                if (null == publishedBuyingBean || !"yes".equals(publishedBuyingBean.getStatus()))
//                    return;
//                if (pageIndex == 1) {
//                    panicBuyingDetailBeanList.clear();
//                    panicBuyingDetailBeanList.addAll(publishedBuyingBean.getInfo().getPageData());
//                } else if (publishedBuyingBean.getInfo().getPageCount() >= pageIndex) {
//                    if (panicBuyingDetailBeanList != null) {
//                        panicBuyingDetailBeanList.addAll(publishedBuyingBean.getInfo().getPageData());
//                    }
//                }
//                peripheryAdapter.updateData(panicBuyingDetailBeanList);
//                peripheryAdapter.notifyDataSetChanged();
//                ptrf_listview.onRefreshComplete();
//                if (publishedBuyingBean.getInfo().getPageData().isEmpty()) {
//                    ptrf_listview.mFooterLoadingView.setVisibility(View.GONE);
//                    Toast.makeText(getActivity(), "没有更多数据！", Toast.LENGTH_SHORT).show();
//                }
//                if(publishedBuyingBean.getInfo().getPageData().size() > 0){
//                    ll_nomessage.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                ptrf_listview.onRefreshComplete();
//                Toast.makeText(getActivity(), "请求数据失败，请稍后重试！", Toast.LENGTH_SHORT).show();
//            }
//        };
//        Map<String, String> map = new HashMap<>();
//        map.put("pageNum", pageIndex + "");
//        map.put("pageSize", "10");
//        service.getInfoList(PreferencesUtil.getCommityId(getActivity()), map, callback);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        peripheryAdapter.notifyDataSetChanged();
//    }
//
//    public void updateUI() {
//        Log.i("debbug", "peripheryUpdate");
//        pageIndex = 1;
//        panicBuyingDetailBeanList.clear();
//        getPublishedBuyingInfo();
//    }
//}
