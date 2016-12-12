package xj.property.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import xj.property.R;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.activity.vote.VoteDetailsActivity;
import xj.property.beans.ActivityBean;
import xj.property.beans.VoteIndexRespInfoBean;
import xj.property.utils.DensityUtil;
import xj.property.utils.SmileUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

/**
 * Created by maxwell on 15/1/8.
 */
public class ActivitiesAdapter extends BaseAdapter {

    private Context mContext;
    private List<ActivityBean> dataList;
    private RequestQueue requestQueue;
    ListView listView;

    public ActivitiesAdapter(Context context, ListView listView, List<ActivityBean> dataList) {
        this.mContext = context;
        this.dataList = dataList;
        this.listView = listView;
    }

    @Override
    public int getCount() {
        // loading界面
        return dataList.size();
    }

    @Override
    public ActivityBean getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        ActivityBean bean = dataList.get(position);

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_activity, null);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.iv_bangzhu_zan.setVisibility(View.INVISIBLE);
        if (bean.getBzPraiseSum() > 0) {
            viewHolder.iv_bangzhu_zan.setVisibility(View.VISIBLE);
            viewHolder.iv_bangzhu_zan.setImageResource(R.drawable.life_circle_zambia_red);
        }

        if (bean.getSuperPraise() != null && "yes".equals(bean.getSuperPraise()) && position == 0) {
            viewHolder.iv_bangzhu_zan.setVisibility(View.VISIBLE);
            viewHolder.iv_bangzhu_zan.setImageResource(R.drawable.lifecircle_super_zambia_red);
        }

        // 设置资源
        viewHolder.tv_title_activity.setText(bean.getActivityTitle());
        long time = bean.getActivityTime();
        if (time == 0) {
            viewHolder.tv_time_activity.setVisibility(View.GONE);
        } else {
            viewHolder.tv_time_activity.setVisibility(View.VISIBLE);
            viewHolder.tv_time_activity.setText(StrUtils.getDate4Millions(time * 1000L));
        }
        String place = bean.getPlace();
        if (place == null || "".equals(place)) {
            viewHolder.tv_address_activity.setVisibility(View.GONE);
        } else {
            viewHolder.tv_address_activity.setVisibility(View.VISIBLE);
            viewHolder.tv_address_activity.setText(place);
        }
        if (time == 0 && "".equals(place)) {
            viewHolder.tv_content.setVisibility(View.VISIBLE);

            Spannable spanAll = SmileUtils.getSmiledText(mContext, bean.getActivityDetail());
            viewHolder.tv_content.setText(spanAll, TextView.BufferType.SPANNABLE);
//            viewHolder.tv_content.setText(bean.getActivityDetail());

        } else {
            viewHolder.tv_content.setVisibility(View.GONE);
        }



        viewHolder.tv_joincount.setText(formatUserSume(bean.getActivityUserSum()));

//        if (bean.getStatus().equals("review")) {
//            viewHolder.tv_status_activity.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.tv_status_activity.setVisibility(View.GONE);
//        }
        viewHolder.ivIsread.setVisibility(bean.isRead() ? View.GONE : View.VISIBLE);

        int positionColor= position%3;
        if(positionColor==0){
            viewHolder.iv_activities_small_circle.setBackgroundResource(R.drawable.activities_small_circle_color1);
        }else if(positionColor==1){
            viewHolder.iv_activities_small_circle.setBackgroundResource(R.drawable.activities_small_circle_color2);
        }else if(positionColor ==2){
            viewHolder.iv_activities_small_circle.setBackgroundResource(R.drawable.activities_small_circle_color3);        }
//        switch (bean.getActivityId() % 3) {
//            case 0:
//                viewHolder.iv_activities_small_circle.setImageResource(R.color.activities_cricle_small_color1);
//                break;
//            case 1:
//                viewHolder.iv_activities_small_circle.setImageResource(R.color.activities_cricle_small_color2);
//                break;
//            case 2:
//                viewHolder.iv_activities_small_circle.setImageResource(R.color.activities_cricle_small_color3);
//                break;
//        }

        /// 加载已经看过他的用户
        loadingGoodsHasGotursHeadImgs4(viewHolder, bean, mContext);


        return convertView;
    }

    private String formatUserSume(String activityUserSum) {
        if(TextUtils.isDigitsOnly(activityUserSum)){
            int userSum = Integer.parseInt(activityUserSum);
            if(userSum>=0&&userSum<=9){
                return "0"+userSum;
            }else{
                return activityUserSum;
            }
        }else{
            return  activityUserSum;
        }
    }

    /**
     * 跳转到邻居帮详情
     *
     * @param context
     * @param circleBean
     */
    private void goDetail(Activity context, VoteIndexRespInfoBean circleBean) {
//        userInfoDetailBean = PreferencesUtil.getLoginInfo(context);
//        if (userInfoDetailBean != null) {
//            context.startActivity(new Intent(context, VoteDetailsActivity.class).putExtra("voteId", "" + circleBean.getVoteId()));
//        } else {
//            Intent intent = new Intent(context, RegisterLoginActivity.class);
//            context.startActivity(intent);
//        }
    }

    /**
     * 加载已经kan过的用户
     */
    private void loadingGoodsHasGotursHeadImgs4(final ViewHolder viewHolder, ActivityBean bean, Context context) {

        final List<ActivityBean.ActivityMate> users = bean.getUsers();

        if (users == null || users.size() <= 0) {
            if (viewHolder.panic_has_purchase_llay != null) {
                viewHolder.panic_has_purchase_llay.setVisibility(View.GONE);
            }
            return;
        }

        if (viewHolder.panic_has_purchase_llay != null) {
            viewHolder.panic_has_purchase_llay.setVisibility(View.VISIBLE);
        }
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;

        Log.i("debbug", "info.size" + users.size());


//        viewHolder.welfare_purchase_hasgoturs_lv.measure(0, 0);
//
//        int measuredWidth = viewHolder.welfare_purchase_hasgoturs_lv.getMeasuredWidth();
//
//        int perwidth = measuredWidth / 6 - DensityUtil.dip2px(context, 5f) * 2 * 6;

        int perwidth = screenWidth * 85 / 1080;
        int left = DensityUtil.dip2px(context, 5f);
        int left2 = DensityUtil.dip2px(context, 3f);
        if (users.size() >= 6) {
            viewHolder.welfare_purchase_hasgoturs_lv.removeAllViews();
            viewHolder.welfare_purchase_hasgoturs_lv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    goDetail(context, circleBean);
                }
            });
            for (int i = 0; i < 6; i++) {
                LinearLayout usrHeadView = (LinearLayout) View.inflate(context, R.layout.common_cooperation_details_moreurs_headlay, null);
                ImageView img = (ImageView) usrHeadView.findViewById(R.id.iv_avatar);
                img.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(users.get(i).getAvatar(), img, UserUtils.options);
                TextView welfare_purchase_hasgoturs_name_tv = (TextView) usrHeadView.findViewById(R.id.welfare_purchase_hasgoturs_name_tv);
                welfare_purchase_hasgoturs_name_tv.setVisibility(View.GONE);
                usrHeadView.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams rlparams = (LinearLayout.LayoutParams) img.getLayoutParams();
                rlparams.width = perwidth;
                rlparams.height = perwidth;
                if (i == 0) {
                    rlparams.setMargins(left2, 0, left, 0);
                } else {
                    rlparams.setMargins(left, 0, left, 0);
                }
                img.setLayoutParams(rlparams);
                viewHolder.welfare_purchase_hasgoturs_lv.addView(usrHeadView);
            }
        }

        if (users.size() > 0 && users.size() < 6) {
            viewHolder.welfare_purchase_hasgoturs_lv.removeAllViews();
            viewHolder.welfare_purchase_hasgoturs_lv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    goDetail(context, circleBean);
                }
            });
            for (int i = 0; i < users.size(); i++) {
                LinearLayout usrHeadView = (LinearLayout) View.inflate(context, R.layout.common_cooperation_details_moreurs_headlay, null);
                ImageView img = (ImageView) usrHeadView.findViewById(R.id.iv_avatar);
                img.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(users.get(i).getAvatar(), img, UserUtils.options);
                TextView welfare_purchase_hasgoturs_name_tv = (TextView) usrHeadView.findViewById(R.id.welfare_purchase_hasgoturs_name_tv);
                welfare_purchase_hasgoturs_name_tv.setVisibility(View.GONE);
                usrHeadView.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams rlparams = (LinearLayout.LayoutParams) img.getLayoutParams();
                rlparams.width = perwidth;
                rlparams.height = perwidth;
                if (i == 0) {
                    rlparams.setMargins(left2, 0, left, 0);
                } else {
                    rlparams.setMargins(left, 0, left, 0);
                }
                img.setLayoutParams(rlparams);
                viewHolder.welfare_purchase_hasgoturs_lv.addView(usrHeadView);

            }
        }
    }


    class ViewHolder {
        public ImageView iv_activities_small_circle, iv_bangzhu_zan;
        public TextView tv_title_activity;
        public TextView tv_time_activity;
        public TextView tv_address_activity;
        public ImageView tv_status_activity;
        TextView tv_joincount;
        TextView tv_content;
        ImageView ivIsread;

        LinearLayout panic_has_purchase_llay;
        LinearLayout welfare_purchase_hasgoturs_lv;

        ViewHolder(View v) {
            tv_title_activity = (TextView) v.findViewById(R.id.tv_title_activity);
            tv_status_activity = (ImageView) v.findViewById(R.id.tv_status_activity);
            tv_time_activity = (TextView) v.findViewById(R.id.tv_time_activity);
            tv_address_activity = (TextView) v.findViewById(R.id.tv_address_activity);
            tv_joincount = (TextView) v.findViewById(R.id.tv_join_num);
            tv_content = (TextView) v.findViewById(R.id.tv_content_activity);
            ivIsread = (ImageView) v.findViewById(R.id.iv_isread);
            panic_has_purchase_llay = (LinearLayout) v.findViewById(R.id.panic_has_purchase_llay);
            welfare_purchase_hasgoturs_lv = (LinearLayout) v.findViewById(R.id.welfare_purchase_hasgoturs_lv);


            iv_activities_small_circle = (ImageView) v.findViewById(R.id.iv_activities_small_circle);
            iv_bangzhu_zan = (ImageView) v.findViewById(R.id.iv_bangzhu_zan);
            v.setTag(this);
        }
    }

    public void changeDataSource(int postion) {
        ActivityBean tempActivityBean = new ActivityBean();
        tempActivityBean = dataList.get(postion);
        tempActivityBean.setSuperPraise("yes");
        dataList.remove(postion);
        dataList.add(0, tempActivityBean);
        notifyDataSetChanged();
    }

    public void changeDataPZZan(int postion) {
        dataList.get(postion).setBzPraiseSum(1);
        notifyDataSetChanged();
    }


}
