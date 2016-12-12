package xj.property.activity.membership;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.repo.xw.library.views.PullListView;
import com.repo.xw.library.views.PullToRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.beans.MspHistoryConsumeBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.CommonUtils;
import xj.property.utils.other.Arith;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

/**
 * 往期账单
 *
 */
public class ActivityHistoryConsumeList extends HXBaseActivity implements PullToRefreshLayout.OnRefreshListener {

    private LinearLayout ll_errorpage;
    private LinearLayout ll_neterror;
    private LinearLayout ll_nomessage;
    private ImageView iv_nomessage_image;
    private TextView tv_nomessage;
    private View headView;

    private UserInfoDetailBean bean;


    private MyAdapter adapter = new MyAdapter();

    private List<MspHistoryConsumeBean.MspHConsumeDetailsBean> pageData = new ArrayList<>();


    private int pageNum=1;
    private int count;
    private String pageSize= "10";
    private PullToRefreshLayout pull_to_refreshlayout;
    private PullListView msp_lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_ship_historyconsume_list);

        bean =PreferencesUtil.getLoginInfo(this);

        if(bean!=null){
            initView();
            initData();

        }else{

            Intent intent = new Intent(this, RegisterLoginActivity.class);
            startActivity(intent);

        }

    }

    private void initData() {
        if(CommonUtils.isNetWorkConnected(this)){
            pull_to_refreshlayout.autoRefresh();
        }else{
            ll_errorpage.setVisibility(View.VISIBLE);
            ll_neterror.setVisibility(View.VISIBLE);
        }

    }

    private void initView() {
        initTitle();

        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        ll_nomessage = (LinearLayout) findViewById(R.id.ll_nomessage);

        iv_nomessage_image = (ImageView) findViewById(R.id.iv_nomessage_image);
        iv_nomessage_image.setImageResource(R.drawable.tikuanjilu_people);

        tv_nomessage = (TextView) findViewById(R.id.tv_nomessage);
        tv_nomessage.setText("还没有新的账单, 赶快去消费哦!");

        ll_neterror.setOnClickListener(this);

        pull_to_refreshlayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refreshlayout);
        pull_to_refreshlayout.setOnRefreshListener(this);

        msp_lv = (PullListView) findViewById(R.id.msp_pull_lv);
        msp_lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (msp_lv.getLastVisiblePosition() == (msp_lv.getCount() - 1)) {
                            pull_to_refreshlayout.autoLoad();
                        }
                        // 判断滚动到顶部
                        if (msp_lv.getFirstVisiblePosition() == 0) {

                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


            }
        });

        if (headView == null) {
            headView = View.inflate(this, R.layout.circyle_headview, null);
            msp_lv.addHeaderView(headView);
        }

        msp_lv.setAdapter(adapter);
    }

    private void initTitle() {

        this.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tv_title= (TextView)this.findViewById(R.id.tv_title);
        tv_title.setText("往期账单");

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pageNum = 1;
        getMspCardListInfo();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        pageNum++;
        getMspCardListInfo();
    }


    interface MspCardListService {
//        @GET("/api/v1/shopVipcards/orders/{emobId}")
//        void getShopVipcardsOrders(@Path("emobId") String emobId,  @QueryMap Map<String, String> map, Callback<MspHistoryConsumeBean> cb);
//        @GET("/api/v1/shopVipcards/orders/{emobId}")

       ///api/v3/nearbyVipcards/orders/{用户环信ID}?page={页码}&limit={页面大小}
        @GET("/api/v3/nearbyVipcards/orders/{emobId}")
        void getShopVipcardsOrders(@Path("emobId") String emobId, @QueryMap Map<String, String> map, Callback<CommonRespBean<MspHistoryConsumeBean>> cb);
    }

    private void getMspCardListInfo() {



        HashMap<String, String> option = new HashMap<String, String>();
        option.put("page", "" + pageNum);
        option.put("limit", pageSize);
        //page={页码}&limit={页面大小}

        MspCardListService service = RetrofitFactory.getInstance().create(getmContext(),option,MspCardListService.class);
        Callback<CommonRespBean<MspHistoryConsumeBean>> callback = new Callback<CommonRespBean<MspHistoryConsumeBean>>() {
            @Override
            public void success(CommonRespBean<MspHistoryConsumeBean> bean, retrofit.client.Response response) {
                if (bean != null) {
                    if ("yes".equals(bean.getStatus())&&bean.getData()!=null) {
                        ll_errorpage.setVisibility(View.GONE);
                        ll_nomessage.setVisibility(View.GONE);
                        ll_neterror.setVisibility(View.GONE);
                        List<MspHistoryConsumeBean.MspHConsumeDetailsBean> mspbeans = bean.getData().getData();
                        if(pageData.size()>0){
                            if(mspbeans==null||mspbeans.size()<=0){
                                showNoMoreToast();
                            }
                        }
                        if (pageNum == 1) {
                            pageData.clear();
                            pageData.addAll(mspbeans);
                        } else {
                            pageData.addAll(mspbeans);
                        }
                        count = adapter.getCount();
                        adapter.notifyDataSetChanged();
                    } else {
                        showToast(bean.getMessage());
                    }
                }

                if (count == 0) {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_nomessage.setVisibility(View.VISIBLE);
                }
                if(pageNum==1){
                    pull_to_refreshlayout.refreshFinish(true);
                }else{
                    pull_to_refreshlayout.loadMoreFinish(true);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (count == 0) {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_neterror.setVisibility(View.VISIBLE);
                }
                if(pageNum==1){
                    pull_to_refreshlayout.refreshFinish(true);
                }else{
                    pull_to_refreshlayout.loadMoreFinish(true);
                }
                error.printStackTrace();
                showNetErrorToast();

            }
        };
        service.getShopVipcardsOrders(bean.getEmobId(),option, callback);

    }


    private class MyAdapter extends BaseAdapter {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        @Override
        public int getCount() {
            return pageData.size();
        }

        @Override
        public Object getItem(int position) {
            return pageData;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(getmContext(), R.layout.common_membership_historyconsume_item, null);
                viewHolder = new ViewHolder(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Log.i("debbug", "size=" + pageData.size());
            Log.i("debbug", "viewHolder=" + viewHolder);

            viewHolder.msp_shop_name_tv.setText(pageData.get(position).getShopName());

            String star = pageData.get(position).getStar();
            if(TextUtils.isEmpty(star)||TextUtils.equals(star,"null")){
                viewHolder.msp_shop_star_rb.setRating(5.0f);
                viewHolder.msp_shop_score_tv.setText("5分");
            }else{
                viewHolder.msp_shop_star_rb.setRating((float) Arith.round(Double.valueOf(star),1));
                viewHolder.msp_shop_score_tv.setText((float) Arith.round(Double.valueOf(star),1)+"分");
            }
            viewHolder.msp_consume_rmb_tv.setText(Arith.round(pageData.get(position).getDiscountPrice(),2) + "元");
            viewHolder.msp_consume_time_tv.setText("" + format.format(new Date(pageData.get(position).getCreateTime() * 1000L)));
            viewHolder.msp_history_item_consume_discount_tv.setText(pageData.get(position).getDiscount()+"折");
            ImageLoader.getInstance().displayImage(pageData.get(position).getShopLogo(), viewHolder.msp_shop_pic_iv, UserUtils.msp_card_iv_options);

            return convertView;
        }

        class ViewHolder {

            ImageView msp_shop_pic_iv;
            TextView msp_shop_name_tv;
            TextView msp_consume_rmb_tv;
            TextView msp_shop_total_tv;

            TextView msp_history_item_consume_discount_tv;

            TextView msp_shop_score_tv;

            TextView msp_consume_time_tv;
            RatingBar msp_shop_star_rb;


            ViewHolder(View v) {
                msp_shop_pic_iv = (ImageView) v.findViewById(R.id.msp_shop_pic_iv);
                msp_shop_name_tv = (TextView) v.findViewById(R.id.msp_shop_name_tv);
                msp_history_item_consume_discount_tv = (TextView) v.findViewById(R.id.msp_history_item_consume_discount_tv);
                msp_shop_total_tv = (TextView) v.findViewById(R.id.msp_shop_total_tv);

                msp_shop_score_tv = (TextView) v.findViewById(R.id.msp_shop_score_tv);

                msp_consume_rmb_tv = (TextView) v.findViewById(R.id.msp_consume_rmb_tv);
                msp_consume_time_tv = (TextView) v.findViewById(R.id.msp_consume_time_tv);

                msp_shop_star_rb = (RatingBar) v.findViewById(R.id.msp_shop_star_rb);

                v.setTag(this);
            }
        }

    }



    @Override
    public void onClick(View v) {


        switch (v.getId()){

            case R.id.ll_neterror:
                initData();
                break;
        }


    }



}
