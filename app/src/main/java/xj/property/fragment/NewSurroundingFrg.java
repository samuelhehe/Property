package xj.property.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.XjApplication;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.activity.surrounding.ActivitySrroundingMapList;
import xj.property.activity.surrounding.PanicBuyingActivity;
import xj.property.beans.PublishedBuyingBean;
import xj.property.beans.SrroundingInfoBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.cache.PanicBuyingItemInfo;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseUtils;
import xj.property.netbase.RetrofitFactory;
import xj.property.netbasebean.CommunityInfoRespBean;
import xj.property.utils.CommonUtils;
import xj.property.utils.ToastUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.MyGridView;
import xj.property.widget.pullrefreshview.library.PullToRefreshBase;

public class NewSurroundingFrg extends BaseFragment {

    private XjApplication app;


    private List<PublishedBuyingBean.InfoEntity.PageDataEntity> panicBuyingDetailBeanList = new ArrayList<PublishedBuyingBean.InfoEntity.PageDataEntity>();

    private LinearLayout ll_errorpage;
    private LinearLayout ll_empty;
    private LinearLayout ll_neterror;
    private TextView tv_getagain;


    private MyGridView frg_jssearch_gv;
    private UserInfoDetailBean userInfoDetailBean;

    private List<SrroundingInfoBean.InfoEntity> infoEntities = new ArrayList<SrroundingInfoBean.InfoEntity>();


    private MyAdapter myAdapter = new MyAdapter();

    private RecyclerView ptrf_scrollview;

    private xj.property.widget.pullrefreshview.library.PullToRefreshScrollView frg_srrounding_ptrf_srcollview;

    private MyRecyclerAdapter myRecyclerAdapter;

    private int pageCount;

    private int pageIndex = 1;

    private String pageSize = "10";
    private int rowcount;
    private CommunityInfoRespBean commonRespBeanData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        app = (XjApplication) getActivity().getApplication();

        userInfoDetailBean = PreferencesUtil.getLoginInfo(getActivity());

        View view = inflater.inflate(R.layout.fragment_newsrounding, container, false);

        initView2(view);
        initData();

        return view;
    }


    private void initView2(View view) {
        initTitle(view);

        ll_neterror = (LinearLayout) view.findViewById(R.id.ll_neterror);
        tv_getagain = (TextView) view.findViewById(R.id.tv_getagain);
        ll_errorpage = (LinearLayout) view.findViewById(R.id.ll_errorpage);
        ll_empty = (LinearLayout) view.findViewById(R.id.ll_empty);

        frg_srrounding_ptrf_srcollview = (xj.property.widget.pullrefreshview.library.PullToRefreshScrollView) view.findViewById(R.id.frg_srrounding_ptrf_srcollview);

        frg_srrounding_ptrf_srcollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {

                pageIndex = 1;

                if (!CommonUtils.isNetWorkConnected(getActivity())) {
                    showToast("网络异常,请连接网络后重试!");
                } else {
//                    getPublishedBuyingInfo();
                    getSrroundingInfoList();
                }
            }
        });


        ptrf_scrollview = (RecyclerView) view.findViewById(R.id.ptrf_scrollview);

        ptrf_scrollview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        myRecyclerAdapter = new MyRecyclerAdapter(getActivity());

        myRecyclerAdapter.setOnItemClickListener(new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                PublishedBuyingBean.InfoEntity.PageDataEntity beanPageDataEntity = panicBuyingDetailBeanList.get(position);
                String emobid;
                if (PreferencesUtil.getLogin(getActivity())) {
                    emobid = PreferencesUtil.getLoginInfo(getActivity()).getEmobId();
                } else {
                    emobid = PreferencesUtil.getlogoutEmobId(getActivity());
                }

                long time = ((beanPageDataEntity.getEndTime()) * 1000l) - (System.currentTimeMillis());
                Log.i("time_test_test1", System.currentTimeMillis() + "");
                Log.i("time_test_test2", (beanPageDataEntity.getEndTime()) + "");
                if (time <= 0) {
                    Toast.makeText(getActivity(), "抢购活动已过期！", Toast.LENGTH_SHORT).show();
                } else {
                    PanicBuyingItemInfo panicBuyingItemInfo = new Select().from(PanicBuyingItemInfo.class).where("crazySalesId = ? and emobId = ?", beanPageDataEntity.getCrazySalesId(),
                            emobid).executeSingle();
                    if (panicBuyingItemInfo == null) {
                        new PanicBuyingItemInfo(beanPageDataEntity.getCrazySalesId(), emobid).save();
                    }
                    Intent intent = new Intent(getActivity(), PanicBuyingActivity.class);
                    intent.putExtra("crazySalesId", beanPageDataEntity.getCrazySalesId());
                    Log.i("crazySalesId----", "" + beanPageDataEntity.getCrazySalesId());
                    getActivity().startActivity(intent);
                }
            }

        });

        ptrf_scrollview.setAdapter(myRecyclerAdapter);
        /// V3 2016/03/21 周边抢购模块移除， 不显示
        ptrf_scrollview.setVisibility(View.GONE);

        ptrf_scrollview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            //用来标记是否正在向最后一个滑动，既是否向右滑动或向下滑动
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();

                    // 判断是否滚动到底部，并且是向右滚动
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        //加载更多功能的代码
                        Log.e("howes right", "" + manager.findLastCompletelyVisibleItemPosition());
//                        Toast.makeText(getActivity(), "加载更多", Toast.LENGTH_LONG).show();

                        pageIndex++;
                        if (pageIndex <= pageCount) {
//                            getPublishedBuyingInfo();

                        }

                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                if (dx > 0) {
                    //大于0表示，正在向右滚动
                    isSlidingToLast = true;
                } else {
                    //小于等于0 表示停止或向左滚动
                    isSlidingToLast = false;
                }

            }
        });


        frg_jssearch_gv = (MyGridView) view.findViewById(R.id.frg_jssearch_gv);
        frg_jssearch_gv.setAdapter(myAdapter);


    }

    private void initTitle(View view) {
        view.findViewById(R.id.iv_back).setVisibility(View.INVISIBLE);
//        view.findViewById(R.id.tv_title).setBackground(getResources().getDrawable(R.drawable.nearby_title));
       TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText("周边");
        tv_title.setTextColor(getResources().getColor(R.color.sys_green_theme_text_color));
//        .setBackground(getResources().getDrawable(R.drawable.nearby_title));
    }


    private void initData() {
        if (!CommonUtils.isNetWorkConnected(getActivity())) {

            ll_errorpage.setVisibility(View.VISIBLE);
            ll_neterror.setVisibility(View.VISIBLE);

            tv_getagain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!CommonUtils.isNetWorkConnected(getActivity())) {
                        ll_errorpage.setVisibility(View.VISIBLE);

                        return;
                    } else {
                        ll_errorpage.setVisibility(View.GONE);

//                        getPublishedBuyingInfo();
                        getSrroundingInfoList();

                    }
                }
            });
        } else {
            ll_errorpage.setVisibility(View.GONE);
            NetBaseUtils.extractCommunityInfo(getActivity(),PreferencesUtil.getCommityId(getActivity()),new NetBaseUtils.NetRespListener<CommonRespBean<CommunityInfoRespBean>>() {


                @Override
                public void successYes(CommonRespBean<CommunityInfoRespBean> commonRespBean, Response response) {
                     commonRespBeanData = commonRespBean.getData();
                }

                @Override
                public void successNo(CommonRespBean<CommunityInfoRespBean> commonRespBean, Response response) {
                    showToast("数据异常："+ commonRespBean.getMessage());
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                }
            });
//            getPublishedBuyingInfo();
            getSrroundingInfoList();
        }

    }


    interface GetInfoService {
        //        http://bangbang.ixiaojian.com/api/v1/crazysales/communities/{communityId}?pageNum=xx&pageSize=xxx
//        HTTP Method : GET

        @GET("/api/v1/crazysales/communities/{communityId}")
        void getInfoList(@Path("communityId") long communityId, @QueryMap Map<String, String> map, Callback<PublishedBuyingBean> cb);


//        @GET("/api/v2/communities/{communityId}/facilityClasses")
//        void getSrroundingInfoList(@Path("communityId") long communityId, Callback<SrroundingInfoBean> cb);
//        @GET("/api/v2/communities/{communityId}/facilityClasses")

//        /api/v3/nearby/category
        @GET("/api/v3/nearby/category")
        void getSrroundingInfoList(Callback<CommonRespBean<List<SrroundingInfoBean.InfoEntity>>> cb);

    }

    private Handler mHandler = new Handler();

    /**
     * 获取周边百度服务信息列表
     */
    public void getSrroundingInfoList() {
        GetInfoService service = RetrofitFactory.getInstance().create(getActivity(),GetInfoService.class);
        Callback<CommonRespBean<List<SrroundingInfoBean.InfoEntity>>> callback = new Callback<CommonRespBean<List<SrroundingInfoBean.InfoEntity>>>() {
            @Override
            public void success(CommonRespBean<List<SrroundingInfoBean.InfoEntity>> publishedBuyingBean, Response response) {
//                lv_list_view.setVisibility(View.VISIBLE);
//                Log.i("success", "获取数据成功");
                if (publishedBuyingBean != null && "yes".equals(publishedBuyingBean.getStatus())) {

                    infoEntities.clear();
                    infoEntities.addAll(publishedBuyingBean.getData());

                    Log.i("success", "" + infoEntities.size());

//                    myAdapter.setLatitude((float) publishedBuyingBean.getLatitude());
//                    myAdapter.setLongitude((float) publishedBuyingBean.getLongitude());

                    myAdapter.notifyDataSetChanged();

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (frg_srrounding_ptrf_srcollview != null) {
                                frg_srrounding_ptrf_srcollview.getRefreshableView().fullScroll(ScrollView.FOCUS_UP);
                            }
                        }
                    });

                    if (infoEntities.size() <= 0) {
                        Log.e("getSrroundingInfoList", "没有数据");
                        frg_jssearch_gv.setVisibility(View.GONE);
                    } else {

                        ll_errorpage.setVisibility(View.GONE);
                        frg_jssearch_gv.setVisibility(View.VISIBLE);
                    }

                }
                if (frg_srrounding_ptrf_srcollview != null) {
                    frg_srrounding_ptrf_srcollview.onRefreshComplete();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (frg_srrounding_ptrf_srcollview != null) {
                    frg_srrounding_ptrf_srcollview.onRefreshComplete();
                }
                if (rowcount >= 0) {
                    if (!CommonUtils.isNetWorkConnected(getActivity())) {
                        showToast("网络异常,请连接网络后重试!");
                    } else {
                        showToast("数据异常,请稍后重试!");
                    }
                } else {
                    ptrf_scrollview.setVisibility(View.GONE);
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (frg_srrounding_ptrf_srcollview != null) {
                            frg_srrounding_ptrf_srcollview.getRefreshableView().fullScroll(ScrollView.FOCUS_UP);
                        }
                    }
                });

            }
        };
        service.getSrroundingInfoList(callback);
    }


    /**
     * 加载周边活动列表
     */
    public void getPublishedBuyingInfo() {

        final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        GetInfoService service = restAdapter.create(GetInfoService.class);
        Callback<PublishedBuyingBean> callback = new Callback<PublishedBuyingBean>() {
            @Override
            public void success(PublishedBuyingBean publishedBuyingBean, Response response) {
//                Log.i("success", "获取数据成功");

                if (publishedBuyingBean != null && "yes".equals(publishedBuyingBean.getStatus())) {

                    pageCount = publishedBuyingBean.getInfo().getPageCount();

                    List<PublishedBuyingBean.InfoEntity.PageDataEntity> subitems = publishedBuyingBean.getInfo().getPageData();

                    if (subitems == null || subitems.isEmpty()) {

                        if(panicBuyingDetailBeanList!=null&&panicBuyingDetailBeanList.size()>0){
                            showToast("没有更多数据了");
                        }
                    } else {
                        if (pageIndex == 1) {
                            panicBuyingDetailBeanList.clear();
                            panicBuyingDetailBeanList.addAll(subitems);

                        } else {
                            panicBuyingDetailBeanList.addAll(subitems);
                        }

                        if (myRecyclerAdapter != null) {

                            myRecyclerAdapter.notifyDataSetChanged();
                        } else {

                            myRecyclerAdapter = new MyRecyclerAdapter(getActivity());

                            myRecyclerAdapter.notifyDataSetChanged();
                        }

                    }


                    Log.i("success", "" + panicBuyingDetailBeanList.size());

                    rowcount = panicBuyingDetailBeanList.size();

                    if (rowcount > 0) {

                        ptrf_scrollview.setVisibility(View.VISIBLE);
                        ll_errorpage.setVisibility(View.GONE);


                    } else {
                        ptrf_scrollview.setVisibility(View.GONE);
                    }


                } else {
                    showToast("数据异常");
                }

                if (frg_srrounding_ptrf_srcollview != null) {
                    frg_srrounding_ptrf_srcollview.onRefreshComplete();
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (frg_srrounding_ptrf_srcollview != null) {
                            frg_srrounding_ptrf_srcollview.getRefreshableView().fullScroll(ScrollView.FOCUS_UP);
                        }
                    }
                });

            }

            @Override
            public void failure(RetrofitError error) {
                if (frg_srrounding_ptrf_srcollview != null) {
                    frg_srrounding_ptrf_srcollview.onRefreshComplete();
                }
                if (rowcount >= 0) {

                    if (!CommonUtils.isNetWorkConnected(getActivity())) {
                        showToast("网络异常,请连接网络后重试!");
                    } else {
                        showToast("数据异常,请稍后重试!");
                    }

                } else {
                    ptrf_scrollview.setVisibility(View.GONE);
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (frg_srrounding_ptrf_srcollview != null) {
                            frg_srrounding_ptrf_srcollview.getRefreshableView().fullScroll(ScrollView.FOCUS_UP);
                        }
                    }
                });
            }
        };
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", "" + pageIndex);
        map.put("pageSize", pageSize);
        service.getInfoList(PreferencesUtil.getCommityId(getActivity()), map, callback);
    }


    private class MyAdapter extends BaseAdapter {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");


        @Override
        public int getCount() {
            return infoEntities.size();
        }

        @Override
        public Object getItem(int position) {
            return infoEntities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.common_srrounding_map_item, null);
                viewHolder = new ViewHolder(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

//            Log.i("debbug", "size=" + infoEntities.size());
//            Log.i("debbug", "viewHolder=" + viewHolder);

            viewHolder.srrounding_item_name_tv.setText(infoEntities.get(position).getName());

            ImageLoader.getInstance().displayImage(infoEntities.get(position).getPhoto(), viewHolder.srrounding_icon_item_civ);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    userInfoDetailBean = PreferencesUtil.getLoginInfo(getActivity());
                    if (userInfoDetailBean != null) {
                        Intent intent = new Intent(getActivity(), ActivitySrroundingMapList.class);
                        intent.putExtra("searchmKeys", infoEntities.get(position).getName());
                        if(commonRespBeanData!=null){
                            intent.putExtra("latitude", commonRespBeanData.getLatitude());
                            intent.putExtra("longitude",commonRespBeanData.getLongitude());
                            startActivity(intent);
                        }else{
                            ToastUtils.showToast(getActivity(),"小区坐标信息有误");
                        }

//                        Intent intent = new Intent(getActivity(), ActivitySurroundingMap.class);
//                        intent.putExtra("searchmKeys", infoEntities.get(position).getFacilitiesClassName());
//                        intent.putExtra("latitude", latitude);
//                        intent.putExtra("longitude", longitude);
//                        startActivity(intent);

                    } else {
                        Intent intent = new Intent(getActivity(), RegisterLoginActivity.class);
                        startActivity(intent);

                    }

                }
            });
            return convertView;
        }

        class ViewHolder {

            ImageView srrounding_icon_item_civ;
            TextView srrounding_item_name_tv;

            ViewHolder(View v) {
                srrounding_icon_item_civ = (ImageView) v.findViewById(R.id.srrounding_icon_item_civ);
                srrounding_item_name_tv = (TextView) v.findViewById(R.id.srrounding_item_name_tv);

                v.setTag(this);
            }
        }

    }


    public interface MyItemClickListener {
        public void onItemClick(View view, int postion);

    }


    public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {


        private final LayoutInflater mLayoutInflater;

        private MyItemClickListener mItemClickListener;


        public MyRecyclerAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int position) {
            //获取列表项控件
            //这里获取了item_view的布局
            View v = mLayoutInflater.inflate(R.layout.common_srrounding_list_item, viewGroup, false);

            return new ViewHolder(v, mItemClickListener);
        }

        //为控件设置数据
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            final PublishedBuyingBean.InfoEntity.PageDataEntity beanPageDataEntity = panicBuyingDetailBeanList.get(position);

            viewHolder.srrouding_item_desc_tv.setText(beanPageDataEntity.getDescr());
            viewHolder.srrounding_item_shopname_tv.setText(beanPageDataEntity.getTitle());
            viewHolder.srrounding_item_shopdistance_tv.setText(beanPageDataEntity.getDistance() + "m");

            ImageLoader.getInstance().displayImage(beanPageDataEntity.getCrazySalesImg().get(0).getImgUrl(), viewHolder.srrounding_item_icon_iv, msp_srrounding_item_iv_options);

        }

        public DisplayImageOptions msp_srrounding_item_iv_options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_picture)
                .showImageForEmptyUri(R.drawable.default_picture)
                .showImageOnFail(R.drawable.default_picture)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

        /**
         * 设置Item点击监听
         *
         * @param listener
         */
        public void setOnItemClickListener(MyItemClickListener listener) {
            this.mItemClickListener = listener;
        }


        //获取Items长度
        @Override
        public int getItemCount() {

            return panicBuyingDetailBeanList == null ? 0 : panicBuyingDetailBeanList.size();
        }


        //ViewHolder 继承RecyclerView.ViewHolder
        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            ImageView srrounding_item_icon_iv;
            TextView srrouding_item_desc_tv;
            TextView srrounding_item_shopname_tv;
            TextView srrounding_item_shopdistance_tv;

            public int position;

            private MyItemClickListener mListener;


            public ViewHolder(View v, MyItemClickListener listener) {
                super(v);
                srrounding_item_icon_iv = (ImageView) v.findViewById(R.id.srrounding_item_icon_iv);
                srrouding_item_desc_tv = (TextView) v.findViewById(R.id.srrouding_item_desc_tv);
                srrounding_item_shopname_tv = (TextView) v.findViewById(R.id.srrounding_item_shopname_tv);
                srrounding_item_shopdistance_tv = (TextView) v.findViewById(R.id.srrounding_item_shopdistance_tv);
                this.mListener = listener;

                v.setOnClickListener(this);
            }

            /**
             * 点击监听
             */
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v, getPosition());
                }
            }

            /**
             * 长按监听
             */
            @Override
            public boolean onLongClick(View arg0) {
                return true;
            }


        }

    }


    @Override
    public void onResume() {
        super.onResume();

    }

    public void updateUI() {
        Log.i("debbug", "peripheryUpdate");

        pageIndex = 1;
        panicBuyingDetailBeanList.clear();
//        getPublishedBuyingInfo();
        getSrroundingInfoList();

    }
}
