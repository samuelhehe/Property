package xj.property.activity.contactphone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

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
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.beans.ContactPhoneBean;
import xj.property.beans.ContactPhoneListBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.cache.FastShopCatModel;
import xj.property.fragment.FastShopFragment;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.other.Config;
import xj.property.utils.other.FastShopCarDBUtil;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.PagerSlidingTabStrip;


/**
 * 快店主页
 * v3 2016/03/17
 */
public class FastShopIndexActivity extends HXBaseActivity {
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyAdapter pagerAdapter = null;
    private DisplayMetrics dm;
    private ImageView ivGoodsPic;
    private ArrayList<String> titles = new ArrayList<>();
    private List<ContactPhoneBean> contactPhoneBeans = new ArrayList<>();
    private Map<Integer, FastShopFragment> contentMap = new HashMap<>();
    private ImageView iv_shop_bottom_car_empty;
    private TextView tv_sum_goods_num;
    private TextView tv_sum_price_num;
    private LinearLayout bt_confirm;
    private UserInfoDetailBean userbean;
    private String searchName;
    private TextView tv_search_contact_contact;
    private ContactPhoneListBean shopHomeInfoBean;
    //    List<FastShopCatModel> fastShopCatModels=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userbean = PreferencesUtil.getLoginInfo(this);
//        if(userbean!=null){
//            List<FastShopCatModel>  list=FastShopCarDBUtil.getAll(userbean.getEmobId());
//            if(list!=null)
//            fastShopCatModels.addAll(list);
//        }

        initView();
        searchName = (String) getIntent().getSerializableExtra(Config.SearchName);
        //// 快店搜索

        if (searchName != null) {
            tabs.setVisibility(View.GONE);
            titles.add(searchName);
            pagerAdapter = new MyAdapter(getSupportFragmentManager(), titles);
            pager.setAdapter(pagerAdapter);
            tabs.setViewPager(pager);

        } else {
            getContactPhoneDataList();
        }
        refresh();
    }

    private void initView() {
        setContentView(R.layout.activity_fastshop_index);
        dm = getResources().getDisplayMetrics();
        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        ivGoodsPic = (ImageView) findViewById(R.id.iv_goodspic);
        ivGoodsPic.setOnClickListener(this);
        iv_shop_bottom_car_empty = (ImageView) findViewById(R.id.iv_shop_bottom_car_empty);
        tv_sum_goods_num = (TextView) findViewById(R.id.tv_sum_goods_num);
        tv_sum_price_num = (TextView) findViewById(R.id.tv_sum_price_num);
        bt_confirm = (LinearLayout) findViewById(R.id.bt_confirm);
        tv_search_contact_contact = (TextView) findViewById(R.id.tv_search_contact_contact);
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //// 看购物车是否空的，查询本地数据库
                if (FastShopCarDBUtil.getAll(userbean.getEmobId()).isEmpty()) {
                    return;
                } else {
                    /// 去购物车
                    Intent it = new Intent(FastShopIndexActivity.this, FastShopCarActivity.class);
                    startActivity(it);
                }
            }
        });
        findViewById(R.id.tv_title).setOnClickListener(this);
        findViewById(R.id.tv_right_text).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
    }


    public class MyAdapter extends FragmentPagerAdapter {
        ArrayList<String> _titles;

        public MyAdapter(FragmentManager fm, ArrayList<String> titles) {
            super(fm);
            _titles = titles;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return _titles.get(position);
        }

        @Override
        public int getCount() {
            return _titles.size();
        }

        @Override
        public Fragment getItem(int position) {
            if (contentMap.containsKey(position)) {
                return contentMap.get(position);
            } else {
                FastShopFragment ffrag = new FastShopFragment();
                if (searchName != null) {
                    ffrag.changeSearchName(searchName);
                } else {
                    ffrag.changeCatId(contactPhoneBeans.get(position).getCatId() + "");
                }
                contentMap.put(position, ffrag);
                return ffrag;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        userbean = PreferencesUtil.getLoginInfo(this);


//        if(userbean!=null){
//            List<FastShopCatModel>  list=FastShopCarDBUtil.getAll(userbean.getEmobId());
//            if(list!=null){
//                fastShopCatModels.clear();
//                fastShopCatModels.addAll(list);
//            }
//        }

        refresh();
    }

    //// 滑动底部与顶部的显示与不显示
    public void scroll4Visable(boolean isVisable) {

        if (!isVisable) {
            findViewById(R.id.fastshop_top).setVisibility(View.GONE);
            findViewById(R.id.bottom).setVisibility(View.GONE);
        } else {
            findViewById(R.id.fastshop_top).setVisibility(View.VISIBLE);
            userbean = PreferencesUtil.getLoginInfo(getmContext()); //// 2015/12/25 修改Bug
            if (userbean == null) return;
            List<FastShopCatModel> lists = FastShopCarDBUtil.getAllAndState(userbean.getEmobId());
            if (lists != null && lists.size() > 0)
                findViewById(R.id.bottom).setVisibility(View.VISIBLE);
            else
                findViewById(R.id.bottom).setVisibility(View.GONE);
        }
    }

    public void refresh() {
        List<FastShopCatModel> lists;
        if (PreferencesUtil.getLogin(this)) {
            userbean = PreferencesUtil.getLoginInfo(this);
            lists = FastShopCarDBUtil.getAllAndState(userbean.getEmobId());
            if (lists != null) {
                int size = lists.size();
                if (size > 0) {
                    findViewById(R.id.bottom).setVisibility(View.VISIBLE);
                    bt_confirm.setVisibility(View.VISIBLE);
                } else {
                    bt_confirm.setVisibility(View.GONE);
                }
                int count = FastShopCarDBUtil.getGoodsCount(lists);
                tv_sum_goods_num.setText(count > 99 ? 99 + "" : count + "");
                if (size == 0) {
                    tv_sum_price_num.setText("共计￥0");
                } else {
                    tv_sum_price_num.setText("共计￥" + FastShopCarDBUtil.getAllGoodsPrice(lists) + "");
                }
            } else {
                bt_confirm.setVisibility(View.GONE);
                tv_sum_price_num.setText("共计￥0");
            }
        } else {
            bt_confirm.setVisibility(View.GONE);
            tv_sum_price_num.setText("共计￥0");
        }


    }

    public FastShopFragment getCurrentItemFragment() {
//        Log.i("onion", "current");
        return (FastShopFragment) pagerAdapter.getItem(pager.getCurrentItem());
    }

    /**
     * widgets activities page
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title:
                startActivity(new Intent(this, FastShopSearchListActivity.class));
                break;
            case R.id.tv_right_text:
                if (userbean == null) {
                    startActivityForResult(new Intent(this, RegisterLoginActivity.class), 0);
                } else {
                    startActivity(new Intent(this, FastShopOrderActivity.class));
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                ivGoodsPic.setVisibility(View.GONE);
                break;
        }
    }

    public void changePic(String imgUrl) {
        ivGoodsPic.setVisibility(View.VISIBLE);
        ImageLoader.getInstance().displayImage(imgUrl, ivGoodsPic, options);

//        ImageLoader.getInstance().cancelDisplayTask(ivGoodsPic);
    }

    DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.default_picture)
            .showImageOnFail(R.drawable.default_picture).showImageOnLoading(R.drawable.default_picture)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(true).cacheOnDisk(true)
            .build();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 退出系统
            if (ivGoodsPic == null || ivGoodsPic.getVisibility() == View.GONE) {
                finish();
                return true;
            }
            ivGoodsPic.setVisibility(View.GONE);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    interface ContactPhoneService {
//        @GET("/api/v2/communities/{communityId}/users/{emobId}/itemCategories?q=2")
//        void getContactPhoneList(@Path("communityId") long communityId, @Path("emobId") String emobId, Callback<ContactPhoneListBean> cb);
//
//        @GET("/api/v2/communities/{communityId}/users/{emobId}/itemCategories?q=2")


        //GET {baseUrl}/api/v3/shops?communityId={小区ID}
        @GET("/api/v3/shops")
        void getContactPhoneList(@QueryMap HashMap<String, String> queryMap, Callback<CommonRespBean<ContactPhoneListBean>> cb);


    }

    private void getContactPhoneDataList() {
        //// 保存快店分类属性
        contactPhoneBeans = PreferencesUtil.getFastShopIndex(this);
        if (contactPhoneBeans != null && !contactPhoneBeans.isEmpty()) {
            for (int i = 0; i < contactPhoneBeans.size(); i++) {
                titles.add(contactPhoneBeans.get(i).getCatName());
            }
            pagerAdapter = new MyAdapter(getSupportFragmentManager(), titles);
            pager.setAdapter(pagerAdapter);
            tabs.setViewPager(pager);
            tv_search_contact_contact.setText("" + PreferencesUtil.getDeliverLimit(this) + "元起送，" + PreferencesUtil.getDeliverTime(this) + "送达！");
        }

        HashMap<String, String> querymap = new HashMap<>();
        querymap.put("communityId", "" + PreferencesUtil.getCommityId(getmContext()));
        ContactPhoneService service = RetrofitFactory.getInstance().create(getmContext(), querymap, ContactPhoneService.class);
        Callback<CommonRespBean<ContactPhoneListBean>> callback = new Callback<CommonRespBean<ContactPhoneListBean>>() {
            @Override
            public void success(CommonRespBean<ContactPhoneListBean> respBean, Response response) {
                if (respBean != null && TextUtils.equals("yes", respBean.getStatus()) && respBean.getData() != null) {
                    shopHomeInfoBean = respBean.getData();
                    if (contactPhoneBeans != null && !contactPhoneBeans.isEmpty() && shopHomeInfoBean.getCreateTime() == PreferencesUtil.getFastShopTime(FastShopIndexActivity.this)) {
                        return;
                    }
                    contactPhoneBeans.clear();
                    titles.clear();
                    PreferencesUtil.saveFastShopTime(FastShopIndexActivity.this, shopHomeInfoBean.getCreateTime());
                    PreferencesUtil.saveDeliverLimit(FastShopIndexActivity.this, shopHomeInfoBean.getDeliverLimit());
                    PreferencesUtil.saveDeliverTime(FastShopIndexActivity.this, shopHomeInfoBean.getDeliverTime());
                    tv_search_contact_contact.setText("" + shopHomeInfoBean.getDeliverLimit() + "元起送，" + shopHomeInfoBean.getDeliverTime() + "送达！");
                    contactPhoneBeans.addAll(shopHomeInfoBean.getCategories());
                    PreferencesUtil.saveFastShopIndexInfo(getmContext(),shopHomeInfoBean);
                    PreferencesUtil.saveFastShopIndex(FastShopIndexActivity.this, contactPhoneBeans);
                    for (int i = 0; i < contactPhoneBeans.size(); i++) {
                        titles.add(contactPhoneBeans.get(i).getCatName());
                    }
                    pagerAdapter = new MyAdapter(getSupportFragmentManager(), titles);
                    pager.setAdapter(pagerAdapter);
                    tabs.setViewPager(pager);
//                pagerAdapter.notifyDataSetChanged();
                } else {
                    if (respBean != null) {
                        showDataErrorToast(respBean.getMessage());
                    } else {
                        showDataErrorToast();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        service.getContactPhoneList(querymap, callback);
    }
}
