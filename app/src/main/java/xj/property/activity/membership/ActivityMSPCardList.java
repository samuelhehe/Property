package xj.property.activity.membership;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.repo.xw.library.views.PullListView;
import com.repo.xw.library.views.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.beans.MSPCardBean;
import xj.property.beans.MSPCareV3Bean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.other.Arith;
import xj.property.utils.other.PreferencesUtil;

/**
 * 会员卡列表页
 */
public class ActivityMSPCardList extends HXBaseActivity implements PullToRefreshLayout.OnRefreshListener {

    private TextView tv_title;

    private TextView tv_right_text;

    private Context mContext;
    private LinearLayout ll_errorpage;
    private LinearLayout ll_neterror;
    private LinearLayout ll_nomessage;
    private ImageView iv_nomessage_image;

    private View headView;

    private MyAdapter adapter = new MyAdapter();

    private int count;

    private int pageNum;

    private String pageSize = "10";

    private int pageCount;

    private TextView tv_nomessage;
    private List<MSPCardBean.MSPCardDetailBean> pageData = new ArrayList<>();
    private TextView msp_list_notice_tv; /// 提示条
    private PullListView msp_lv;
    private PullToRefreshLayout pull_to_refreshlayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_ship_card_list);
        mContext = this;

        initView();
        pageNum = 1;
        getMspCardListInfo();
        pull_to_refreshlayout.autoRefresh();

    }


    public void showMspNotice() {

        showPopWindow2();
        CountDownTimer countDownTimer = new CountDownTimer(4 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {

                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        };
        countDownTimer.start();
    }


    public void showMspNotice2() {

        if (msp_list_notice_tv != null) {

            msp_list_notice_tv.setVisibility(View.VISIBLE);
        }
        CountDownTimer countDownTimer = new CountDownTimer(2 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {

                if (msp_list_notice_tv != null) {

                    msp_list_notice_tv.setVisibility(View.GONE);
                }
            }
        };
        countDownTimer.start();
    }


    final PopupWindow popupWindow = new PopupWindow();

    private void showPopWindow2() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mMenuView = inflater.inflate(R.layout.msp_card_list_notice, null);
        ((TextView) mMenuView.findViewById(R.id.tv_unservicetime)).setText("使用帮帮会员卡,线上支付买单,所有消费立享折扣");

        // 设置按钮监听
        // 设置SelectPicPopupWindow的View
        popupWindow.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
//        this.setFocusable(true);

        // 设置SelectPicPopupWindow弹出窗体动画效果
//          popupWindow.setAnimationStyle(R.style.AnimTop3);
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1f,
                Animation.RELATIVE_TO_SELF, 0f
        );
        translateAnimation.setDuration(1000);
        translateAnimation.setFillAfter(true);
        ((TextView) mMenuView.findViewById(R.id.tv_unservicetime)).setAnimation(translateAnimation);
        translateAnimation.start();

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // ,Animation.RELATIVE_TO_SELF,10f,Animation.RELATIVE_TO_SELF,10f
                if (popupWindow != null && !popupWindow.isShowing()) {
                    popupWindow.showAsDropDown(findViewById(R.id.headtop_title));
                }
                //popupWindow.showAtLocation(findViewById(R.id.list),Gravity.NO_GRAVITY,0,0);
            }
        }, 200);

    }


    private void initView() {
        initTitle();

        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        ll_nomessage = (LinearLayout) findViewById(R.id.ll_nomessage);

        iv_nomessage_image = (ImageView) findViewById(R.id.iv_nomessage_image);
        iv_nomessage_image.setImageResource(R.drawable.tikuanjilu_people);

        tv_nomessage = (TextView) findViewById(R.id.tv_nomessage);
        tv_nomessage.setText("对不起, 还没有新的会员卡,敬请期待");

        msp_list_notice_tv = (TextView) findViewById(R.id.msp_list_notice_tv);

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

    private void initTitle() {

        tv_right_text = (TextView) this.findViewById(R.id.tv_right_text);
        tv_right_text.setText("往期账单");
        tv_right_text.setVisibility(View.VISIBLE);
        tv_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserInfoDetailBean bean = PreferencesUtil.getLoginInfo(getmContext());
                if (bean != null) {
                    /// 跳转至往期账单
                    startActivity(new Intent(mContext, ActivityHistoryConsumeList.class));

                } else {
                    Intent intent = new Intent(getmContext(), RegisterLoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        this.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setTextColor(getResources().getColor(R.color.sys_green_theme_text_color));
        tv_title.setText("用帮帮付款立享折扣");

    }

    interface MspCardListService {
        @GET("/api/v1/shopVipcards/page")
        void getShopVipcardsPage(@QueryMap Map<String, String> map, Callback<MSPCardBean> cb);


        /////api/v3/nearbyVipcards?communityId=2&page=1&limit=10

        @GET("/api/v3/nearbyVipcards")
        void getShopVipcardsPageV3(@QueryMap Map<String, String> map, Callback<CommonRespBean<MSPCardBean>> cb);
    }

    private void getMspCardListInfo() {

        HashMap<String, String> option = new HashMap<String, String>();
        option.put("communityId", "" + PreferencesUtil.getCommityId(this));
        option.put("page", "" + pageNum);
        option.put("limit", pageSize);
        //communityId=2&page=1&limit=10

        MspCardListService service  = RetrofitFactory.getInstance().create(getmContext(),option,MspCardListService.class);
        Callback<CommonRespBean<MSPCardBean>> callback = new Callback<CommonRespBean<MSPCardBean>>() {
            @Override
            public void success(CommonRespBean<MSPCardBean> bean, retrofit.client.Response response) {
                if (bean != null) {
                    if ("yes".equals(bean.getStatus())&&bean.getData()!=null&&bean.getData().getData()!=null) {
                        ll_errorpage.setVisibility(View.GONE);
                        ll_nomessage.setVisibility(View.GONE);
                        ll_neterror.setVisibility(View.GONE);

                        List<MSPCardBean.MSPCardDetailBean> mspCardDetailBeans = bean.getData().getData();
                        if (pageData.size() > 0) {
                            if (mspCardDetailBeans == null || mspCardDetailBeans.size() <= 0) {
                                showNoMoreToast();
                            }
                        }
                        if (pageNum == 1) {
                            pageData.clear();
                            pageData.addAll(mspCardDetailBeans);
                        } else {
                            pageData.addAll(mspCardDetailBeans);
                        }
                        count = adapter.getCount();
                        adapter.notifyDataSetChanged();
                        pageCount = bean.getData().getPage();

                    } else {
                        showToast(bean.getMessage());
                    }
                } else {
                    showNetErrorToast();
                }
                if (count == 0) {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_nomessage.setVisibility(View.VISIBLE);
                }
                if (pageNum == 1) {
                    pull_to_refreshlayout.refreshFinish(true);
                } else {
                    pull_to_refreshlayout.loadMoreFinish(true);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (pageNum == 1) {
                    pull_to_refreshlayout.refreshFinish(true);
                } else {
                    pull_to_refreshlayout.loadMoreFinish(true);
                }
                if (count == 0) {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_neterror.setVisibility(View.VISIBLE);
                }
                showNetErrorToast();
            }
        };
        service.getShopVipcardsPageV3(option, callback);
    }


    @Override
    protected void onResume() {
        super.onResume();

//  2015/12/10 移除下滑提示黑条       showMspNotice();

    }

    private class MyAdapter extends BaseAdapter {


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
                convertView = View.inflate(getmContext(), R.layout.common_membership_cardlist_item, null);
                viewHolder = new ViewHolder(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Log.i("debbug", "size=" + pageData.size());
            Log.i("debbug", "viewHolder=" + viewHolder);


//        // 外部矩形弧度
            float[] outerR = new float[]{20, 20, 20, 20, 20, 20, 20, 20};
            // 内部矩形与外部矩形的距离
//            RectF inset = new RectF(10, 10, 10, 10);
            // 内部矩形弧度
            float[] innerRadii = new float[]{20, 20, 20, 20, 20, 20, 20, 20};

            RoundRectShape rr = new RoundRectShape(outerR, null, null);

            ShapeDrawable drawable = new ShapeDrawable(rr);

            //指定填充颜色
//            drawable.getPaint().setColor(Color.YELLOW);

            // 指定填充模式
            drawable.getPaint().setStyle(Paint.Style.FILL);

//            //指定填充颜色
            drawable.getPaint().setColor(Color.argb(255,
                    Integer.valueOf(pageData.get(position).getColorR()),
                    Integer.valueOf(pageData.get(position).getColorG()),
                    Integer.valueOf(pageData.get(position).getColorB())));

            viewHolder.msp_card_bg_iv.setBackground(drawable);
            viewHolder.msp_card_bg_iv.setVisibility(View.INVISIBLE);


            ImageLoader.getInstance().displayImage(pageData.get(position).getPhoto(), viewHolder.msp_card_bg_iv, new MyImageLoadingListener(viewHolder, pageData.get(position)));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("debug ", "adapter.getCount" + adapter.getCount() + " pageData.size(" + pageData.size() + "position " + position);

//                    startActivity(new Intent(getmContext(), UserGroupInfoActivity.class).putExtra(Config.INTENT_PARMAS2, pageData.get(position).getEmobId()));

                    UserInfoDetailBean bean = PreferencesUtil.getLoginInfo(getmContext());
                    if (bean != null) {
                        startActivity(new Intent(getmContext(), ActivityMShipCardDisCount.class).putExtra("MSPCardBeanShopInfo", pageData.get(position)));
                    } else {
                        Intent intent = new Intent(getmContext(), RegisterLoginActivity.class);
                        startActivity(intent);
                    }
                }
            });

            return convertView;
        }


        class MyImageLoadingListener implements ImageLoadingListener {

            private final ViewHolder viewHolder2;
            private final MSPCardBean.MSPCardDetailBean bean;

            public MyImageLoadingListener(ViewHolder viewHolder2, MSPCardBean.MSPCardDetailBean bean) {
                this.viewHolder2 = viewHolder2;
                this.bean = bean;
            }

            @Override
            public void onLoadingStarted(String imageUri, View view) {


//                Log.d("onLoadingStarted", "onLoadingStarted" + imageUri + "view" + view);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {


//                Log.d("onLoadingFailed", "onLoadingFailed" + imageUri + "view" + view);


                viewHolder2.msp_card_ratingbar.setEnabled(false);

                viewHolder2.msp_card_ratingbar.setRating(Float.valueOf(String.valueOf(bean.getStar())));

                viewHolder2.msp_total_tv.setText(bean.getOrderCount() + "单");
                viewHolder2.msp_discount_tv.setText("" + bean.getDiscount() + "折");
                viewHolder2.msp_distance_tv.setText("" + bean.getDistance() + "m");
                viewHolder2.msp_shop_name_tv.setText("" + bean.getShopName());
                viewHolder2.msp_card_rating_star_tv.setText("" + Arith.round(bean.getStar(), 1) + "分");


                viewHolder2.msp_card_bg_iv.setVisibility(View.VISIBLE);

                viewHolder2.msp_card_ratingbar.setVisibility(View.VISIBLE);
                viewHolder2.msp_total_tv.setVisibility(View.VISIBLE);
                viewHolder2.msp_discount_tv.setVisibility(View.VISIBLE);
                viewHolder2.msp_distance_tv.setVisibility(View.VISIBLE);
                viewHolder2.msp_shop_name_tv.setVisibility(View.VISIBLE);
                viewHolder2.msp_card_rating_star_tv.setVisibility(View.VISIBLE);
                viewHolder2.msp_card_ftxt_logo_tv.setVisibility(View.VISIBLE);


            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                Log.d("onLoadingComplete", bean.toString());

                viewHolder2.msp_card_bg_iv.setBackground(null);

                viewHolder2.msp_card_ratingbar.setEnabled(false);
                viewHolder2.msp_card_ratingbar.setRating(Float.valueOf(String.valueOf(bean.getStar())));

                viewHolder2.msp_total_tv.setText(bean.getOrderCount() + "单");
                viewHolder2.msp_discount_tv.setText("" + bean.getDiscount() + "折");
                viewHolder2.msp_distance_tv.setText("" + bean.getDistance() + "m");
                viewHolder2.msp_shop_name_tv.setText("" + bean.getShopName());
                viewHolder2.msp_card_rating_star_tv.setText("" + Arith.round(bean.getStar(), 1) + "分");

                viewHolder2.msp_card_bg_iv.setVisibility(View.VISIBLE);
                viewHolder2.msp_card_ratingbar.setVisibility(View.VISIBLE);
                viewHolder2.msp_total_tv.setVisibility(View.VISIBLE);
                viewHolder2.msp_discount_tv.setVisibility(View.INVISIBLE);
                viewHolder2.msp_distance_tv.setVisibility(View.VISIBLE);
                viewHolder2.msp_shop_name_tv.setVisibility(View.INVISIBLE);
                viewHolder2.msp_card_rating_star_tv.setVisibility(View.VISIBLE);
                viewHolder2.msp_card_ftxt_logo_tv.setVisibility(View.VISIBLE);


            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        }

        class ViewHolder {

            ImageView msp_card_bg_iv;
            TextView msp_distance_tv;
            TextView msp_discount_tv;
            TextView msp_shop_name_tv;
            TextView msp_total_tv;
            TextView msp_card_ftxt_logo_tv;
            TextView msp_card_rating_star_tv;
            RatingBar msp_card_ratingbar;

            ViewHolder(View v) {

                msp_card_bg_iv = (ImageView) v.findViewById(R.id.msp_card_bg_iv);

                msp_distance_tv = (TextView) v.findViewById(R.id.msp_card_distance_tv);
                msp_discount_tv = (TextView) v.findViewById(R.id.msp_discount_tv);
                msp_total_tv = (TextView) v.findViewById(R.id.msp_card_total_tv);
                msp_shop_name_tv = (TextView) v.findViewById(R.id.msp_shop_name_tv);
                msp_card_rating_star_tv = (TextView) v.findViewById(R.id.msp_card_rating_star_tv);

                msp_card_ftxt_logo_tv = (TextView) v.findViewById(R.id.msp_card_ftxt_logo_tv);

                msp_card_ratingbar = (RatingBar) v.findViewById(R.id.msp_card_ratingbar);

                v.setTag(this);
            }
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_right_text:

                break;

            case R.id.ll_neterror:
                getMspCardListInfo();

                break;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

}
