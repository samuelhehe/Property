package xj.property.activity.contactphone;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.XjApplication;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.adapter.FastGoodHistoryAdapter;
import xj.property.beans.BaseBean;
import xj.property.beans.FastShopDetailListBean;
import xj.property.beans.HistoryFastShopBean;
import xj.property.beans.OrderFastShopHistoryBean;
import xj.property.beans.StatusBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.cache.FastShopCatModel;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.netbasebean.DeleteShopOrder;
import xj.property.utils.CommonUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.FastShopCarDBUtil;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.CarJumpView;
import xj.property.widget.pullrefreshview.library.PullToRefreshBase;
import xj.property.widget.pullrefreshview.library.PullToRefreshListView;

public class FastShopOrderActivity extends HXBaseActivity {
    /**
     * logger
     */
    /**
     * widgets activities page
     */
    private PullToRefreshListView lv_order_history;
    private FastGoodHistoryAdapter historyAdapter;
    private ArrayList<HistoryFastShopBean> historyFastShopBeans = new ArrayList<>();
    private UserInfoDetailBean userbean;
    private CarJumpView carJumpView;
    private LinearLayout ll_errorpage;
    private LinearLayout ll_nomessage;
    private LinearLayout ll_neterror;
    private TextView tv_getagain;
    private ImageView iv_shop_bottom_car_empty;
    private TextView tv_sum_goods_num;
    private TextView tv_sum_price_num;
    private LinearLayout bt_confirm;
    int pageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fastshop_order);
        initTitle(null, "购买订单", null);
        userbean = PreferencesUtil.getLoginInfo(FastShopOrderActivity.this);
        //setUpViews();
        initView();
        historyAdapter = new FastGoodHistoryAdapter(FastShopOrderActivity.this, historyFastShopBeans, new FastGoodHistoryAdapter.CallBack() {
            @Override
            public void call(HistoryFastShopBean bean) {

            }
        });
        lv_order_history.setAdapter(historyAdapter);
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
                    if (!CommonUtils.isNetWorkConnected(FastShopOrderActivity.this)) {
                        return;
                    } else {
                        ll_errorpage.setVisibility(View.GONE);
                        getOrderindexList();
                    }
                }
            });
        } else {
//            ll_errorpage.setVisibility(View.GONE);
            getOrderindexList();
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        userbean = PreferencesUtil.getLoginInfo(FastShopOrderActivity.this);
//        refresh();
//    }


    private void initPullToRefresh() {
        lv_order_history.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                pageIndex = 1;
                getOrderindexList();
            }
        });
        lv_order_history.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                pageIndex++;
                lv_order_history.mFooterLoadingView.setVisibility(View.VISIBLE);
                lv_order_history.mFooterLoadingView.refreshing();
                getOrderindexList();
            }
        });
    }
    private void initView() {
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        tv_getagain = (TextView) findViewById(R.id.tv_getagain);
        ll_nomessage = (LinearLayout) findViewById(R.id.ll_nomessage);
        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);
        lv_order_history = (PullToRefreshListView) findViewById(R.id.lv_order_history);
        lv_order_history.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        initPullToRefresh();
        carJumpView = (CarJumpView) findViewById(R.id.ivcarjump);
        iv_shop_bottom_car_empty = (ImageView) findViewById(R.id.iv_shop_bottom_car_empty);
        tv_sum_goods_num = (TextView) findViewById(R.id.tv_sum_goods_num);
        tv_sum_price_num = (TextView) findViewById(R.id.tv_sum_price_num);
        bt_confirm = (LinearLayout) findViewById(R.id.bt_confirm);
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FastShopCarDBUtil.getAll(userbean.getEmobId()).size() == 0) {
                    return;
                } else {
                    Intent it = new Intent(FastShopOrderActivity.this, FastShopCarActivity.class);
                    startActivity(it);
                }
            }
        });
        carJumpView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //   gv_fastshop_detail.dispatchTouchEvent(event);
                return lv_order_history.dispatchTouchEvent(event);
            }
        });
    }

    public void changePrice(HistoryFastShopBean.ShopBean shopBean, View v) {
        if (shopBean == null) return;
        if (PreferencesUtil.getLogin(this)) {
            userbean = PreferencesUtil.getLoginInfo(this);
            FastShopDetailListBean.PagerItemBean itemBean=new FastShopDetailListBean.PagerItemBean();
            itemBean.setServiceId(shopBean.getServiceId());
            itemBean.setShopName(shopBean.getShopName());
            itemBean.setShopEmobId(shopBean.getShopEmobId());
            itemBean.setCurrentPrice(shopBean.getCurrentPrice());
            itemBean.setOriginPrice(shopBean.getOriginPrice());
            itemBean.setServiceImg(shopBean.getServiceImg());
            itemBean.setServiceName(shopBean.getServiceName());
            saveItemToDB(itemBean);
//            ImageView iv=new ImageView(this);
//            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(v.getWidth(),v.getHeight());
//            iv.setLayoutParams(params);
//            iv.setImageResource(R.drawable.snacks_add);
//            rl_car_animation.addView(iv);

//            Log.i("onion",Arrays.toString(location));
//            BaseUtils.setJumpAnimation(iv,location[0],location[1]);
            int[] location = new int[2];
            int[] carJumpViewY = new int[2];
            v.getLocationOnScreen(location);
//            gv_fastshop_detail.getLocationOnScreen(carJumpViewY);
            lv_order_history.getLocationOnScreen(carJumpViewY);
//            carJumpView.startJump(location[0], location[1] - carJumpViewY[1], iv_shop_bottom_car_empty.getWidth() / 2, 300);
            carJumpView.startJump(location[0], location[1] - carJumpViewY[1], 150, 300);
            refresh();
        } else {
            //showToast("请先登录");
            //LoginDialogUtil.showLoginDialog(this);
            Intent intent = new Intent(this, RegisterLoginActivity.class);
            startActivity(intent);
        }
    }




    @Override
    public void onClick(View v) {

    }

    interface DeleteOrderService {
//        @PUT("/api/v2/communities/{communityId}/fastShop/{orderId}")
//        void deleteCallBack(@Header("signature") String signature, @Body BaseBean baseBean,
//                          @Path("communityId") int communityId,@Path("orderId") int orderId,  Callback<StatusBean> cb);

        ///api/v3/shopOrders
        @PUT("/api/v3/shopOrders")
        void deleteCallBack(@Body DeleteShopOrder baseBean, Callback<CommonRespBean<String>> cb);
    }


    /**
     *
     * 删除订单
     * v3 2016/03/23
     *
     * @param orderId
     */
    private void deleteOrder(final int orderId){
        DeleteShopOrder  baseBean =new DeleteShopOrder();
        baseBean.setShopOrderId(orderId);
        baseBean.setEmobIdUser(PreferencesUtil.getLoginInfo(getmContext()).getEmobId());

        DeleteOrderService service = RetrofitFactory.getInstance().create(getmContext(),baseBean,DeleteOrderService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, Response response) {
                if("yes".equals(bean.getStatus())){
                    showToast("已删除");
                   for(int i=0;i< historyFastShopBeans.size();i++){

                       if(Integer.parseInt( historyFastShopBeans.get(i).getShopOrderId())==orderId){
                           historyFastShopBeans.remove(i);
                           historyAdapter.notifyDataSetChanged();
                           break;
                       }
                   }
                }else {
                    showToast("删除失败:"+bean.getMessage());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                showNetErrorToast();
                error.printStackTrace();
            }
        };
        service.deleteCallBack(baseBean,callback);

    }
    public void showDeleteDialog(final String orderId) {
        final Dialog noticeDialog = new Dialog(this, R.style.MyDialogStyle);
        noticeDialog.setContentView(R.layout.dialog_deletecircle);
        TextView tv_cancle = (TextView) noticeDialog.findViewById(R.id.tv_cancle);
        TextView tv_relogin = (TextView) noticeDialog.findViewById(R.id.tv_relogin);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noticeDialog.dismiss();
            }
        });
        tv_relogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOrder(Integer.parseInt(orderId));
                noticeDialog.dismiss();
            }
        });
        noticeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        noticeDialog.setCancelable(false);
        noticeDialog.show();
    }
//TODO
    public void toCarFromOldOrder(HistoryFastShopBean bean) {
        String emobid = bean.getShopItems().get(0).getShopEmobId();
        for (int i = 0; i < bean.getShopItems().size(); i++) {
          HistoryFastShopBean.ShopBean shopBean= bean.getShopItems().get(i);
            FastShopDetailListBean.PagerItemBean itemBean = new FastShopDetailListBean.PagerItemBean();
            itemBean.setShopName(shopBean.getShopName());
            itemBean.setServiceName(shopBean.getServiceName());
            itemBean.setServiceImg(shopBean.getServiceImg());
            itemBean.setCurrentPrice(shopBean.getCurrentPrice());
            itemBean.setOriginPrice(shopBean.getOriginPrice());
            itemBean.setServiceId(shopBean.getServiceId());
            itemBean.setShopId(shopBean.getShopId());
            itemBean.setShopEmobId(emobid);
            saveItemToDBWithCount(itemBean,shopBean.getCount());
//            for (int j = 0; j < bean.getList().get(i).getCount(); j++) {
//                if( ! FastShopCarDBUtil.addCarAble(bean.getList().get(i).getServiceId())){
//                    Toast.makeText(this, R.string.fast_shop_add_error, Toast.LENGTH_SHORT).show();
//                    break;
//                };
//                FastShopDetailListBean.PagerItemBean itemBean = new FastShopDetailListBean.PagerItemBean();
//                itemBean.setShopName(bean.getList().get(i).getShopName());
//                itemBean.setServiceName(bean.getList().get(i).getServiceName());
//                itemBean.setServiceImg(bean.getList().get(i).getServiceImg());
//                itemBean.setCurrentPrice(bean.getList().get(i).getCurrentPrice());
//                itemBean.setOriginPrice(bean.getList().get(i).getOriginPrice());
//                itemBean.setServiceId(bean.getList().get(i).getServiceId());
//                itemBean.setShopId(bean.getList().get(i).getShopId());
//                itemBean.setShopEmobId(emobid);
//                saveItemToDB(itemBean);
//            }

        }
        Intent it = new Intent(FastShopOrderActivity.this, FastShopCarActivity.class);
        startActivity(it);


    }

    private void saveItemToDB(FastShopDetailListBean.PagerItemBean itemBean) {
        //第一条数据直接插入
        if (FastShopCarDBUtil.isDBEmpty(userbean.getEmobId())) {
            FastShopCarDBUtil.insert(itemBean, userbean.getEmobId());
        } else {
            if (FastShopCarDBUtil.isExistByserviceId(itemBean.getServiceId(),userbean.getEmobId()).size() > 0) {
                FastShopCarDBUtil.updateGoodCountByServiceId(itemBean.getServiceId(),userbean.getEmobId());
            } else {
                FastShopCarDBUtil.insert(itemBean, userbean.getEmobId());
            }
        }

    }
    private void saveItemToDBWithCount(FastShopDetailListBean.PagerItemBean itemBean,int count) {
        //第一条数据直接插入
        if (FastShopCarDBUtil.isDBEmpty(userbean.getEmobId())) {
            FastShopCarDBUtil.insert(itemBean, userbean.getEmobId());
        } else {
            if (FastShopCarDBUtil.isExistByserviceId(itemBean.getServiceId(),userbean.getEmobId()).size() > 0) {
                FastShopCarDBUtil.updateGoodCountByServiceId(itemBean.getServiceId(), userbean.getEmobId(), count);
            } else {
                FastShopCarDBUtil.insert(itemBean, userbean.getEmobId());
            }
        }

    }

    /**
     * 获取ActList部分
     */
    interface OrderHistoryService {
        ///api/v2/communities/1/fastShop?q={emobId}&pageNum=1&pageSize=20
//        @GET("/api/v2/communities/{communityId}/fastShop")
//        void getOrderHistoryList(@QueryMap HashMap<String, Object> map, @Path("communityId") long communityId, Callback<OrderFastShopHistoryBean> cb);
//        @GET("/api/v2/communities/{communityId}/fastShop")

//       /api/v3/shopOrders?emobId={用户环信ID}&page={页码}&limit={页面大小}
        @GET("/api/v3/shopOrders")
        void getOrderHistoryList(@QueryMap HashMap<String, String> map, Callback<CommonRespBean<OrderFastShopHistoryBean>> cb);
    }



    private void getOrderindexList() {

        HashMap<String, String> option = new HashMap<>();
        option.put("emobId", userbean == null ? "1" : userbean.getEmobId());
        option.put("page", ""+pageIndex);
        option.put("limit", "4");
        OrderHistoryService service = RetrofitFactory.getInstance().create(getmContext(),option,OrderHistoryService.class);
        Callback<CommonRespBean<OrderFastShopHistoryBean>> callback = new Callback<CommonRespBean<OrderFastShopHistoryBean>>() {
            @Override
            public void success(CommonRespBean<OrderFastShopHistoryBean> bean, Response response) {
                // historyFastShopBeans.clear();
                if(bean.getData()!=null&&bean.getData().getData()!=null){
                    if (bean.getData().getPage() == 1) {
                        historyFastShopBeans.clear();
                        historyFastShopBeans.addAll(bean.getData().getData());
                    } else
                    if (bean.getData().getPage() >= pageIndex) {
                        historyFastShopBeans.addAll(bean.getData().getData());
                        lv_order_history.onRefreshComplete();
                    }else {
                        showNoMoreToast();
                        lv_order_history.mFooterLoadingView.setVisibility(View.GONE);
                    }
                    historyAdapter.notifyDataSetChanged();
                    refresh();
                }else{
                    showDataErrorToast();
                }
            }
            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        service.getOrderHistoryList(option,  callback);

    }
    public void refresh() {
        List<FastShopCatModel> lists;
        if (PreferencesUtil.getLogin(this) ){
            lists = FastShopCarDBUtil.getAllAndState(PreferencesUtil.getLoginInfo(this).getEmobId());
            if (lists != null) {
                int size = lists.size();
                if (size > 0) {
                    findViewById(R.id.bottom).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.bottom).setVisibility(View.GONE);
                }
                int count = FastShopCarDBUtil.getGoodsCount(lists);
                tv_sum_goods_num.setText(count > 99 ? 99 + "" : count + "");
                if (size == 0) {
                    tv_sum_price_num.setText("总价:￥0");
                } else {
                    tv_sum_price_num.setText("总价:￥" + FastShopCarDBUtil.getAllGoodsPrice(lists) + "");
                }
            } else {
                bt_confirm.setVisibility(View.GONE);
                tv_sum_price_num.setText("总价:￥0");
            }
        } else {
            bt_confirm.setVisibility(View.GONE);
            tv_sum_price_num.setText("总价:￥0");
        }

        if(historyAdapter.getCount()<=0){

            ll_nomessage.setVisibility(View.VISIBLE);
            ll_errorpage.setVisibility(View.VISIBLE);
            TextView tv_nomessage = (TextView) findViewById(R.id.tv_nomessage);
            tv_nomessage.setText("您还没有订单，马上去买点什么吧！");

        }else {
            ll_nomessage.setVisibility(View.GONE);
            ll_errorpage.setVisibility(View.GONE);
        }

    }
}
