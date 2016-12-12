package xj.property.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.activity.contactphone.FastShopIndexActivity;
import xj.property.adapter.FastShopDetailAdapter;
import xj.property.beans.ContactPhoneListBean;
import xj.property.beans.FastShopDetailListBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.CommonUtils;
import xj.property.utils.other.AutoLoadListener;
import xj.property.utils.other.BaseUtils;
import xj.property.utils.other.FastShopCarDBUtil;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.CarJumpView;
import xj.property.widget.LoadingDialog;
import xj.property.widget.wheelview.MyPopItems;

/**
 * Created by Administrator on 2015/6/23.
 * <p/>
 * 快店左右滑动的每一个fragment
 * v3  2016/03/17
 */
public class FastShopFragment extends BaseFragment implements View.OnClickListener {
    private UserInfoDetailBean userbean;

    /**
     * widgets activities page
     */
//    private PullToRefreshGridView gv_fastshop_detail;
    private xj.property.widget.GridViewWithHeaderAndFooter mGridView;

    //    private TextView tv_search_good;
    //private RelativeLayout rl_car_animation;
    private CarJumpView carJumpView;
    private FastShopDetailAdapter adapter;
    //    private boolean isrefresh = true;
//    private ContactPhoneBean bean;
    private String catId = "67";
    private String searchName;
    private int pageIndex = 1;

    private MyPopItems popWindow;
    private LinearLayout ll_errorpage;
    private LinearLayout ll_nomessage;
    private LinearLayout ll_neterror;
    private TextView tv_getagain;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
//        bean = (ContactPhoneBean) getIntent().getSerializableExtra(Config.INTENT_PARMAS1);
        initData();
    }

    public void showChoicePop(List<FastShopDetailListBean.PagerItemBean> list) {
        if (userbean == null) {
            startActivity(new Intent(getActivity(), RegisterLoginActivity.class));

        } else {
            addpic(list);
            popWindow.showAtLocation(getView(), Gravity.BOTTOM
                    | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    private void addpic(List<FastShopDetailListBean.PagerItemBean> list) {
        popWindow = new MyPopItems(getActivity(), list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fastshopitem, container, false);
    }

    public void changeCatId(String catId) {
        this.catId = catId;
    }

    public void changeSearchName(String searchName) {
        this.searchName = searchName;
    }

    private void initData() {
        if (!CommonUtils.isNetWorkConnected(getActivity())) {
            ll_errorpage.setVisibility(View.VISIBLE);
            ll_neterror.setVisibility(View.VISIBLE);
            ll_nomessage.setVisibility(View.GONE);
            tv_getagain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!CommonUtils.isNetWorkConnected(getActivity())) {
                        return;
                    } else {
                        ll_errorpage.setVisibility(View.GONE);
//                        String searchIntentName = getIntent().getStringExtra("searchName");
                        if (searchName != null && searchName.length() != 0) {
                            getSearchDataList();
                        } else {
                            getFastShopDetailDataList();
                        }
                    }
                }
            });
        } else {
            if (ll_errorpage != null)
                ll_errorpage.setVisibility(View.GONE);
//            String searchIntentName = getIntent().getStringExtra("searchName");
            if (searchName != null && searchName.length() != 0) {
                getSearchDataList();
            } else {
                getFastShopDetailDataList();
            }
        }
    }

    View footerView;
    ImageView footerimage;
    LinearLayout foot_layout;
    TextView tv_temp;

    float downY = 0.0f;

    private void initView() {
//        gv_fastshop_detail = (PullToRefreshGridView) findViewById(R.id.gv_fastshop_detail);
//        mGridView = gv_fastshop_detail.getRefreshableView();
        mGridView = (xj.property.widget.GridViewWithHeaderAndFooter) getView().findViewById(R.id.gv_fastshop_detail);
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        footerView = layoutInflater.inflate(R.layout.item_grid_footer, null);
        footerimage = (ImageView) footerView.findViewById(R.id.footview);
        foot_layout = (LinearLayout) footerView.findViewById(R.id.foot_layout);
        tv_temp = (TextView) footerView.findViewById(R.id.tv_temp);
        View view = new View(getActivity());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.height_fastshop_top));
        view.setLayoutParams(params);
        mGridView.addHeaderView(view);
        mGridView.addFooterView(footerView);
        //tv_temp.setVisibility(View.VISIBLE);
        tv_temp.setBackgroundColor(Color.WHITE);
        BaseUtils.setLoadingImageAnimation(footerimage);

//        iv_shop_bottom_car_empty = (ImageView) getView().findViewById(R.id.iv_shop_bottom_car_empty);
//        tv_sum_goods_num = (TextView) getView().findViewById(R.id.tv_sum_goods_num);
//        tv_sum_price_num = (TextView)getView(). findViewById(R.id.tv_sum_price_num);
//        bt_confirm = (LinearLayout) getView().findViewById(R.id.bt_confirm);
        carJumpView = (CarJumpView) getView().findViewById(R.id.ivcarjump);

        ll_neterror = (LinearLayout) getView().findViewById(R.id.ll_neterror);
        tv_getagain = (TextView) getView().findViewById(R.id.tv_getagain);
        if (searchName == null)
            ll_nomessage = (LinearLayout) getView().findViewById(R.id.ll_noservice_time);
        else {
            ll_nomessage = (LinearLayout) getView().findViewById(R.id.ll_nomessage);

            ((TextView) getView().findViewById(R.id.tv_nomessage)).setText("没有找到您想要的商品，换一个搜搜看");
        }
        ll_errorpage = (LinearLayout) getView().findViewById(R.id.ll_errorpage);

        carJumpView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //   gv_fastshop_detail.dispatchTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //// 朝上滑动
                        if (event.getY() - downY < -10) {
                            ((FastShopIndexActivity) getActivity()).scroll4Visable(false);
                        } else {
                            ((FastShopIndexActivity) getActivity()).scroll4Visable(true);
                        }
                        break;
                }
                return mGridView.dispatchTouchEvent(event);
            }
        });

        mGridView.setOnScrollListener(new AutoLoadListener(new AutoLoadListener.AutoLoadCallBack() {
            @Override
            public void execute() {
                //tv_temp.setVisibility(View.GONE);
                pageIndex++;
                new GetDataTask().execute();
            }
        }));
    }


    private class GetDataTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            if (searchName == null)
                getData(pageIndex);
            else {
                getSearchDataList();
            }
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
     * 获取ActList部分
     */
    interface FastShopSearchService {
        //http://114.215.105.202/api/v2/communities/1/shops/goods?q={name}&pageNum=1&pageSize=10
//        @GET("/api/v2/communities/{communityId}/shops/goods")
//        void getSearchList(@Path("communityId") long communityId, @QueryMap Map<String, String> map, Callback<FastShopDetailListBean> cb);
//        @GET("/api/v2/communities/{communityId}/shops/goods")
//        /api/v3/shops/{店铺ID}/shopItems/search?q={商品名称}&page={页码}&limit={页面大小}
        @GET("/api/v3/shops/{shopId}/shopItems/search")
        void getSearchList(@Path("shopId") int shopId, @QueryMap HashMap<String, String> map, Callback<CommonRespBean<FastShopDetailListBean>> cb);
    }

    private void getSearchDataList() {
        HashMap<String, String> option = new HashMap<String, String>();
        option.put("q", searchName);
        option.put("page", pageIndex + "");
        option.put("limit", "10");
        FastShopSearchService service = RetrofitFactory.getInstance().create(getActivity(), option, FastShopSearchService.class);
        Callback<CommonRespBean<FastShopDetailListBean>> callback = new Callback<CommonRespBean<FastShopDetailListBean>>() {
            @Override
            public void success(CommonRespBean<FastShopDetailListBean> bean, retrofit.client.Response response) {
                //获取高度
//               iv_shop_bottom_car_empty.getLocationInWindow(rly);
//                System.out.println(bean.getInfo().getPageData());
//                if(pageIndex==1)
//                listBean.clear();

                if (pageIndex == 1) {
                    listBean = bean.getData().getPageData();
                    adapter = new FastShopDetailAdapter(getActivity(), listBean);
                    mGridView.setAdapter(adapter);
                } else {
                    if (bean.getData().getPageData().isEmpty()) {
                        foot_layout.setVisibility(View.INVISIBLE);
                        footerimage.clearAnimation();
                        showToast(R.string.no_more);
                    } else {
                        listBean.addAll(bean.getData().getPageData());
                    }
                }
                if (listBean.size() > 0) {
                    ll_errorpage.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                } else {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_neterror.setVisibility(View.GONE);
                    ll_nomessage.setVisibility(View.VISIBLE);
                }
                if (listBean.size() <= 4) {
                    tv_temp.setVisibility(View.VISIBLE);
                } else {
                    tv_temp.setVisibility(View.GONE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
//                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
            }
        };
//        q={商品名称}&page={页码}&limit={页面大小}
        service.getSearchList(PreferencesUtil.getFastShopIndexInfo(getActivity()).getShopId(), option, callback);
    }

    /**
     * 获取ActList部分
     */
    interface FastShopDetailService {
        ///http://114.215.105.202:8080/api/v1/communities/1/shops/findShopItem/46?pageNum=1&pageSize=10?pageNum={}&pageSize={}
        //http://114.215.105.202:8080/api/v1/communities/{communityId}/shops/shopItems?q={catId}&pageNum=1&pageSize=10

///api/v3/shops/{店铺ID}/shopItems?catId={分类ID}&page={页码}&limit={页面大小}
//        @GET("/api/v2/communities/{communityId}/shops/shopItems")
//        void getRepairList(@Path("communityId") long communityId, @QueryMap Map<String, String> map, Callback<FastShopDetailListBean> cb);

        @GET("/api/v3/shops/{shopId}/shopItems")
        void getShopGoodsList(@Path("shopId") int shopId, @QueryMap HashMap<String, String> map, Callback<CommonRespBean<FastShopDetailListBean>> cb);
    }

    List<FastShopDetailListBean.PagerItemBean> listBean = new ArrayList<FastShopDetailListBean.PagerItemBean>();

    private void getFastShopDetailDataList() {

        HashMap<String, String> option = new HashMap<String, String>();
        option.put("catId", catId);
//        option.put("q", bean.getCatId() + "");
        option.put("page", "1");
        option.put("limit", "10");

//        catId={分类ID}&page={页码}&limit={页面大小}
        FastShopDetailService service = RetrofitFactory.getInstance().create(getActivity(), option, FastShopDetailService.class);
        Callback<CommonRespBean<FastShopDetailListBean>> callback = new Callback<CommonRespBean<FastShopDetailListBean>>() {
            @Override
            public void success(CommonRespBean<FastShopDetailListBean> bean, retrofit.client.Response response) {
//                mLdDialog.dismiss();
                //获取高度
//               iv_shop_bottom_car_empty.getLocationInWindow(rly);
//                System.out.println(bean.getInfo().getPageData());
                if (bean != null && "yes".equals(bean.getStatus()) && bean.getData() != null) {
                    pageIndex = 1;
                    listBean.clear();
                    listBean = resetShopDetailBean(bean.getData().getPageData());

                    adapter = new FastShopDetailAdapter(getActivity(), listBean);
                    mGridView.setAdapter(adapter);
                    if (listBean.size() > 0) {
                        ll_errorpage.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    } else {
                        ll_errorpage.setVisibility(View.VISIBLE);
                        ll_neterror.setVisibility(View.GONE);
                        ll_nomessage.setVisibility(View.VISIBLE);
                    }

                    if (listBean.size() <= 4) {
                        tv_temp.setVisibility(View.VISIBLE);
                    } else {
                        tv_temp.setVisibility(View.GONE);
                    }
                } else {
                    if (bean != null) {
                        showToast("数据异常：" + bean.getMessage());
                    } else {
                        showNetErrorToast();
                    }
                }

            }

            @Override
            public void failure(RetrofitError error) {
//                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        service.getShopGoodsList(PreferencesUtil.getFastShopIndexInfo(getActivity()).getShopId(), option, callback);
    }

    /**
     * 为了兼容老版本中 shopname，shopemobid两个字段
     * v3 2016/03/17
     *
     * @param listBean
     * @return
     */
    private List<FastShopDetailListBean.PagerItemBean> resetShopDetailBean(List<FastShopDetailListBean.PagerItemBean> listBean) {
        if (listBean == null || listBean.size() < 1) {
            return new ArrayList<>();
        }
        ContactPhoneListBean fastShopIndexInfo = PreferencesUtil.getFastShopIndexInfo(getActivity());
        for (FastShopDetailListBean.PagerItemBean pagerItemBean : listBean) {
            pagerItemBean.setShopEmobId(fastShopIndexInfo.getEmobId());
            pagerItemBean.setShopName(fastShopIndexInfo.getShopName());
        }
        return listBean;
    }

    int[] rly = new int[2];

//    public void toEva(String Emobid) {
//        Intent intent = new Intent(this, EvaActivity.class);
//        intent.putExtra(Config.INTENT_PARMAS1, Emobid);
//        startActivity(intent);
//    }

    public void changePrice(FastShopDetailListBean.PagerItemBean itemBean, View v) {
        Log.i("onion", "itemBean" + itemBean + "v" + v);
        if (itemBean == null) return;
        if (PreferencesUtil.getLogin(getActivity())) {
            userbean = PreferencesUtil.getLoginInfo(getActivity());
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
            mGridView.getLocationOnScreen(carJumpViewY);
//            carJumpView.startJump(location[0], location[1] - carJumpViewY[1], iv_shop_bottom_car_empty.getWidth() / 2, 300);
            carJumpView.startJump(location[0], location[1] - carJumpViewY[1], 150, 300);
        } else {
            //showToast("请先登录");
            //LoginDialogUtil.showLoginDialog(this);
            Intent intent = new Intent(getActivity(), RegisterLoginActivity.class);
            startActivity(intent);
        }
    }


    private void saveItemToDB(FastShopDetailListBean.PagerItemBean itemBean) {

        //第一条数据直接插入
        if (FastShopCarDBUtil.isDBEmpty(userbean.getEmobId())) {
            FastShopCarDBUtil.insert(itemBean, userbean.getEmobId());
        } else {
            if (FastShopCarDBUtil.isExistByserviceId(itemBean.getServiceId(), userbean.getEmobId()).size() > 0) {
                FastShopCarDBUtil.updateGoodCountByServiceId(itemBean.getServiceId(), userbean.getEmobId());
            } else {
                FastShopCarDBUtil.insert(itemBean, userbean.getEmobId());
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        userbean = PreferencesUtil.getLoginInfo(getActivity());
        Log.i("FastShopFragment  ", " onResume  ");
    }


    protected void getData(final int pageIndex) {
//        if(bean==null)return;
        HashMap<String, String> option = new HashMap<String, String>();
        option.put("catId", catId);
        option.put("page", pageIndex + "");
        option.put("limit", "10");
        Log.i("FastShopFragment  ", " getData  catId " + catId + " pageNum " + pageIndex + " PreferencesUtil.getCommityId(getActivity()) " + PreferencesUtil.getCommityId(getActivity()));
//        catId={分类ID}&page={页码}&limit={页面大小}

        FastShopDetailService service = RetrofitFactory.getInstance().create(getActivity(), option, FastShopDetailService.class);
        Callback<CommonRespBean<FastShopDetailListBean>> callback = new Callback<CommonRespBean<FastShopDetailListBean>>() {
            @Override
            public void success(CommonRespBean<FastShopDetailListBean> bean, retrofit.client.Response response) {
                if (null == bean || !"yes".equals(bean.getStatus()) || bean.getData() == null) {
                    if (bean != null) {
                        showToast("数据异常：" + bean.getMessage());
                    } else {
                        showNetErrorToast();
                    }
                    return;
                }

                if (pageIndex == 1) {
                    listBean.clear();
                    listBean.addAll(resetShopDetailBean(bean.getData().getPageData()));
                    adapter.notifyDataSetChanged();
                } else if (bean.getData().getPage() >= pageIndex) {
                    if (listBean != null) {
                        listBean.addAll(resetShopDetailBean(bean.getData().getPageData()));
                        adapter.notifyDataSetChanged();
                    }
                }
                if (bean.getData().getPageData().isEmpty()) {
                    foot_layout.setVisibility(View.INVISIBLE);
                    footerimage.clearAnimation();
                    showToast(R.string.no_more);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                showNetErrorToast();
                foot_layout.setVisibility(View.INVISIBLE);
                footerimage.clearAnimation();
            }
        };
        service.getShopGoodsList(PreferencesUtil.getFastShopIndexInfo(getActivity()).getShopId(), option, callback);
    }

}
