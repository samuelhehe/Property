package xj.property.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import xj.property.R;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.activity.vote.VoteDetailsActivity;
import xj.property.beans.UserInfoDetailBean;
import xj.property.beans.VoteIndexRespInfoBean;
import xj.property.beans.VoteIndexRespV3Bean;
import xj.property.beans.VoteOptionsListEntity;
import xj.property.utils.DensityUtil;
import xj.property.utils.SmileUtils;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

/**
 * 投票首页适配器
 */
public class VoteIndexMessageAdapter extends BaseAdapter {

    private final String usertype;

    private final int screenWidth;

    private final LayoutInflater layoutInflator;

    //    SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
    private SimpleDateFormat format = new SimpleDateFormat("MM-dd");


    private static final String[] colorArray = {"#54C7C0", "#EEB355", "#FEADFF", "#60AFE6"};

    /// 默认显示4行
    private static final int DEFAULT_SHOW_LINES = 4;


    private  Activity context;

    private   List<VoteIndexRespInfoBean> circleBeanList;
    private UserInfoDetailBean userInfoDetailBean = null;

    public VoteIndexMessageAdapter(Activity context, List<VoteIndexRespInfoBean> circleBeanList) {
        this.context = context;
        layoutInflator = LayoutInflater.from(context);
        this.circleBeanList = circleBeanList;
        this.userInfoDetailBean = PreferencesUtil.getLoginInfo(context);
        usertype = PreferencesUtil.getUserType(context);
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public VoteIndexRespInfoBean getItem(int position) {
        return circleBeanList.get(position);
    }

    @Override
    public int getCount() {
        return circleBeanList.size();
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getVoteId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.common_vote_index_item, null);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final VoteIndexRespInfoBean circleBean = circleBeanList.get(position);
        if (circleBean == null) {
            return null;
        }
        ImageLoader.getInstance().displayImage(circleBean.getAvatar(), viewHolder.provider_details_iv_avtar, UserUtils.options);

        //// 2015/11/19 添加帮内头衔
//        initBangzhuMedal(circleBean.getGrade(), viewHolder);

        viewHolder.provider_details_name_tv.setText(circleBean.getNickname());

        Spannable spanAll = SmileUtils.getSmiledText(context, circleBean.getVoteTitle());

        viewHolder.vote_index_item_votecontent_tv.setText(spanAll, TextView.BufferType.SPANNABLE);


        if(circleBean.getVoteSum()<=0){
            viewHolder.vote_index_item_votednum_tv.setText("现在投票");
        }else{
            viewHolder.vote_index_item_votednum_tv.setText("" + circleBean.getVoteSum() + "人已投票");
        }

        Date tag_time_date = new Date(circleBean.getCreateTime() * 1000L);

//            Date tag_time_date =  new Date(pageData.get(position).getCreateTime() * 1000L);  2015/11/23

        viewHolder.vote_index_item_time_tv.setText(format.format(tag_time_date));


        initBangzhuMedal(circleBean.getGrade(), viewHolder);

        viewHolder.vote_index_item_godetail_llay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goDetail(context, circleBean);
            }
        });

        if (circleBean.getOptions()!=null&&circleBean.getOptions().size()>1) {
            if (viewHolder.panic_has_purchase_llay != null) {
                viewHolder.panic_has_purchase_llay.setVisibility(View.GONE);
            }
            ///  加载投票结果条形图
            loadingVoteResult(viewHolder, DEFAULT_SHOW_LINES, circleBean);
        } else {
            if (viewHolder.vote_index_item_result_llay != null) {
                viewHolder.vote_index_item_result_llay.setVisibility(View.GONE);
                viewHolder.vote_index_item_result_sh_btn.setVisibility(View.GONE);
            }
            /// 加载已经看过他的用户
            loadingGoodsHasGotursHeadImgs4(viewHolder, circleBean);
        }

        return convertView;
    }


    /**
     * 初始化用户横条奖章图片
     */
    private void initBangzhuMedal(String userType, ViewHolder holder) {
        if (holder.provider_iv_user_type != null) {
//            normal , bangzhu, fubangzhu ,zhanglao,bangzhong
            if (TextUtils.equals(userType, "zhanglao")) {
                holder.provider_iv_user_type.setImageDrawable(context.getResources().getDrawable(R.drawable.life_circle_zhanglao_icon));
                holder.provider_iv_user_type.setVisibility(View.VISIBLE);
            } else if (TextUtils.equals(userType, "bangzhu")) {
                holder.provider_iv_user_type.setImageDrawable(context.getResources().getDrawable(R.drawable.life_circle_bangzhu_icon));
                holder.provider_iv_user_type.setVisibility(View.VISIBLE);
            } else if (TextUtils.equals(userType, "fubangzhu")) {
                holder.provider_iv_user_type.setImageDrawable(context.getResources().getDrawable(R.drawable.life_circle_fubangzhu_icon));
                holder.provider_iv_user_type.setVisibility(View.VISIBLE);
            } else {
                holder.provider_iv_user_type.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 跳转到邻居帮详情
     *
     * @param context
     * @param circleBean
     */
    private void goDetail(Activity context, VoteIndexRespInfoBean circleBean) {
        userInfoDetailBean = PreferencesUtil.getLoginInfo(context);
        if (userInfoDetailBean != null) {
            context.startActivity(new Intent(context, VoteDetailsActivity.class).putExtra("voteId", "" + circleBean.getVoteId()));
        } else {
            Intent intent = new Intent(context, RegisterLoginActivity.class);
            context.startActivity(intent);
        }
    }

    /**
     * 加载已经kan过的用户
     */
    private void loadingGoodsHasGotursHeadImgs4(final ViewHolder viewHolder, final VoteIndexRespInfoBean circleBean) {

        final List<VoteIndexRespInfoBean.UsersListEntity> users = circleBean.getVoteDetails();

        if (users == null || users.size() <= 0) {
            if (viewHolder.panic_has_purchase_llay != null) {
                viewHolder.panic_has_purchase_llay.setVisibility(View.GONE);
            }
            return;
        }

        if (viewHolder.panic_has_purchase_llay != null) {
            viewHolder.panic_has_purchase_llay.setVisibility(View.VISIBLE);
        }

        Log.i("debbug", "info.size" + users.size());


//        viewHolder.welfare_purchase_hasgoturs_lv.measure(0, 0);
//
//        int measuredWidth = viewHolder.welfare_purchase_hasgoturs_lv.getMeasuredWidth();
//
//        int perwidth = measuredWidth / 6 - DensityUtil.dip2px(context, 5f) * 2 * 6;

        int perwidth = screenWidth * 98 / 1080;
        if (users.size() >= 6) {
            viewHolder.welfare_purchase_hasgoturs_lv.removeAllViews();
            viewHolder.welfare_purchase_hasgoturs_lv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    goDetail(context, circleBean);
                }
            });
            for (int i = 0; i < 5; i++) {

                LinearLayout usrHeadView = (LinearLayout) View.inflate(context, R.layout.common_cooperation_details_moreurs_headlay, null);
                ImageView img = (ImageView) usrHeadView.findViewById(R.id.iv_avatar);
                img.setVisibility(View.VISIBLE);

                ImageLoader.getInstance().displayImage(users.get(i).getAvatar(), img, UserUtils.options);

                TextView welfare_purchase_hasgoturs_name_tv = (TextView) usrHeadView.findViewById(R.id.welfare_purchase_hasgoturs_name_tv);
//                welfare_purchase_hasgoturs_name_tv.setText(users.get(i).getNickname());
                welfare_purchase_hasgoturs_name_tv.setVisibility(View.GONE);

                usrHeadView.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams rlparams = (LinearLayout.LayoutParams) img.getLayoutParams();

                rlparams.width = perwidth;
                rlparams.height = perwidth;

                img.setLayoutParams(rlparams);

//                LinearLayout.LayoutParams llayparams = (LinearLayout.LayoutParams) viewHolder.welfare_purchase_hasgoturs_lv.getLayoutParams();
//                viewHolder.welfare_purchase_hasgoturs_lv.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                viewHolder.welfare_purchase_hasgoturs_lv.addView(usrHeadView);
            }

            /// 添加一个查看更多用户按钮

            LinearLayout usrHeadView = (LinearLayout) View.inflate(context, R.layout.common_cooperation_details_moreurs_headlay, null);
            ImageView img = (ImageView) usrHeadView.findViewById(R.id.iv_avatar);
            img.setVisibility(View.VISIBLE);

            img.setImageResource(R.drawable.help_more_forvote);

//            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.help_more_forvote, img);

            TextView welfare_purchase_hasgoturs_name_tv = (TextView) usrHeadView.findViewById(R.id.welfare_purchase_hasgoturs_name_tv);
            welfare_purchase_hasgoturs_name_tv.setVisibility(View.GONE); /// visiblility

            usrHeadView.setVisibility(View.VISIBLE);

            LinearLayout.LayoutParams rlparams = (LinearLayout.LayoutParams) img.getLayoutParams();

            rlparams.width = perwidth;
            rlparams.height = perwidth;

            img.setLayoutParams(rlparams);

//            LinearLayout.LayoutParams llayparams = (LinearLayout.LayoutParams) viewHolder.welfare_purchase_hasgoturs_lv.getLayoutParams();
//            viewHolder.welfare_purchase_hasgoturs_lv.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

            viewHolder.welfare_purchase_hasgoturs_lv.addView(usrHeadView);
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
//                welfare_purchase_hasgoturs_name_tv.setText(users.get(i).getNickname());

                welfare_purchase_hasgoturs_name_tv.setVisibility(View.GONE);

                usrHeadView.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams rlparams = (LinearLayout.LayoutParams) img.getLayoutParams();

                rlparams.width = perwidth;
                rlparams.height = perwidth;
                img.setLayoutParams(rlparams);


//                LinearLayout.LayoutParams llayparams = (LinearLayout.LayoutParams) viewHolder.welfare_purchase_hasgoturs_lv.getLayoutParams();
//                viewHolder.welfare_purchase_hasgoturs_lv.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                viewHolder.welfare_purchase_hasgoturs_lv.addView(usrHeadView);

            }
        }
    }


    /**
     * 投票的结果展示
     *
     * @param viewHolder
     */
    private void loadingVoteResult(ViewHolder viewHolder, final int showLines, final VoteIndexRespInfoBean infoBean) {

        List<VoteOptionsListEntity> voteOptionsList = infoBean.getOptions();

        if (voteOptionsList != null && voteOptionsList.size() > 0) {
            /// 66.88
            float maxPercent = getMaxPercentVote(voteOptionsList);
            /// 总行数
            final int maxLines = voteOptionsList.size();

            if (maxLines > DEFAULT_SHOW_LINES) {
                viewHolder.vote_index_item_result_sh_btn.setVisibility(View.VISIBLE);
            } else {
                viewHolder.vote_index_item_result_sh_btn.setVisibility(View.GONE);
            }

            final int[] showLinesInner = {showLines};
            final ViewHolder viewHolder1 = viewHolder;
            final VoteIndexRespInfoBean infoBean1 = infoBean;
            viewHolder.vote_index_item_result_sh_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (showLinesInner[0] == DEFAULT_SHOW_LINES) {
                        showLinesInner[0] = maxLines;
                    } else {
                        showLinesInner[0] = DEFAULT_SHOW_LINES;
                    }
                    /// 重新加载vote_result
                    loadingVoteResult(viewHolder1, showLinesInner[0], infoBean1);
                }
            });

            viewHolder.vote_index_item_rcontent_llay.removeAllViews();
            for (int i = 0; i < showLines; i++) {
                if (i >= maxLines) {
                    break;
                }
                VoteOptionsListEntity optionsListEntity = voteOptionsList.get(i);

                float array_element = optionsListEntity.getPercent();
                float leftPercent = array_element / maxPercent;

                leftPercent *= 0.60f;

                if (maxPercent == 0) {
                    leftPercent = 0.0f;
                }

                float rightPercent = 1 - leftPercent;

                System.out.println("leftPercent " + leftPercent);

                System.out.println("rightPercent  " + rightPercent);

                LinearLayout percentItem = (LinearLayout) layoutInflator.inflate(R.layout.common_vote_percent_layout_forindex, null);

                LinearLayout leftlay = (LinearLayout) percentItem.findViewById(R.id.vote_result_percent_left_llay);

                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, DensityUtil.dip2px(context, 10f), leftPercent);
//                p.setMargins(0,DensityUtil.dip2px(context,3),0,DensityUtil.dip2px(context,3));
                p.gravity = Gravity.CENTER_VERTICAL;

                leftlay.setLayoutParams(p);
//                leftlay.setBackgroundColor(Color.parseColor(colorArray[i % colorArray.length]));

                int index = i % colorArray.length;
                Drawable bgDrawable = null;
                switch (index) {
                    case 0:
                        bgDrawable = context.getResources().getDrawable(R.drawable.vote_index_result_shape_01);
                        break;
                    case 1:
                        bgDrawable = context.getResources().getDrawable(R.drawable.vote_index_result_shape_02);
                        break;
                    case 2:
                        bgDrawable = context.getResources().getDrawable(R.drawable.vote_index_result_shape_03);
                        break;
                    case 3:
                        bgDrawable = context.getResources().getDrawable(R.drawable.vote_index_result_shape_04);
                        break;
                }
                leftlay.setBackgroundDrawable(bgDrawable);

                LinearLayout rightlay = (LinearLayout) percentItem.findViewById(R.id.vote_result_percent_right_llay);

                LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, rightPercent);
//                LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(0, DensityUtil.dip2px(context, 16f), rightPercent);
//                rp.gravity = Gravity.CENTER_VERTICAL;
//                rp.setMargins(0, 0, 0, DensityUtil.dip2px(context, 3f));

                rightlay.setLayoutParams(rp);

                TextView vote_right_percent_tv = (TextView) rightlay.findViewById(R.id.vote_right_percent_tv);

                String count = ""+optionsListEntity.getCount();
                if(optionsListEntity.getCount()>=1000){
                    count = "999+";
                }
                String percentStr = optionsListEntity.getPercent()+"% ("+ count+"票)";

                vote_right_percent_tv.setText(percentStr);

                TextView vote_result_pername_tv = (TextView) percentItem.findViewById(R.id.vote_result_pername_tv);

                vote_result_pername_tv.setText(optionsListEntity.getContent());

                ImageView vote_result_percent_chosed_iv = (ImageView) percentItem.findViewById(R.id.vote_result_percent_chosed_iv);

                if (optionsListEntity.getVoted() == 1) {
                    vote_result_percent_chosed_iv.setVisibility(View.VISIBLE);
                } else {
                    vote_result_percent_chosed_iv.setVisibility(View.INVISIBLE);
                }

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 0);
                viewHolder.vote_index_item_rcontent_llay.addView(percentItem, params);
                viewHolder.vote_index_item_rcontent_llay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goDetail(context, infoBean);
                    }
                });

            }

            viewHolder.vote_index_item_result_llay.setVisibility(View.VISIBLE);
//            if (maxLines > DEFAULT_SHOW_LINES) {
//                viewHolder.vote_index_item_result_sh_btn.setVisibility(View.VISIBLE);
//            } else {
//                viewHolder.vote_index_item_result_sh_btn.setVisibility(View.GONE);
//            }
        } else {
            /// 设置为不可见
            viewHolder.vote_index_item_result_llay.setVisibility(View.GONE);
            viewHolder.vote_index_item_result_sh_btn.setVisibility(View.GONE);
        }


    }

    private float getMaxPercentVote(List<VoteOptionsListEntity> voteOptionsList) {

        float percent = voteOptionsList.get(0).getPercent();

        for (VoteOptionsListEntity optionsListEntity : voteOptionsList) {

            float temp = optionsListEntity.getPercent();

            if (temp > percent) {
                percent = temp;
            }
        }
        return percent;
    }


    class ViewHolder {

        /// name
        private TextView provider_details_name_tv;
        /// time
        private TextView vote_index_item_time_tv;

        /// 投票宣言
        private TextView vote_index_item_votecontent_tv;

        /// 投过票的邻居
        private LinearLayout panic_has_purchase_llay;

        private LinearLayout welfare_purchase_hasgoturs_lv;

        //// 头像
        private ImageView provider_details_iv_avtar;
        /// 头衔
        private ImageView provider_iv_user_type;
        /// 投票数
        private TextView vote_index_item_votednum_tv;
        /// 投票票数百分比
        private LinearLayout vote_index_item_result_llay;
        /// 放置百分比内容
        private LinearLayout vote_index_item_rcontent_llay;

        /// show_hide
        private LinearLayout vote_index_item_result_sh_btn;

        private LinearLayout vote_index_item_godetail_llay;


        ViewHolder(View v) {

            provider_details_iv_avtar = (ImageView) v.findViewById(R.id.provider_details_iv_avtar);

            provider_iv_user_type = (ImageView) v.findViewById(R.id.provider_iv_user_type);

            provider_details_name_tv = (TextView) v.findViewById(R.id.provider_details_name_tv);

            vote_index_item_time_tv = (TextView) v.findViewById(R.id.vote_index_item_time_tv);

            vote_index_item_votednum_tv = (TextView) v.findViewById(R.id.vote_index_item_votednum_tv);

            vote_index_item_votecontent_tv = (TextView) v.findViewById(R.id.vote_index_item_votecontent_tv);

            panic_has_purchase_llay = (LinearLayout) v.findViewById(R.id.panic_has_purchase_llay);

            welfare_purchase_hasgoturs_lv = (LinearLayout) v.findViewById(R.id.welfare_purchase_hasgoturs_lv);

            vote_index_item_result_llay = (LinearLayout) v.findViewById(R.id.vote_index_item_result_llay);

            vote_index_item_rcontent_llay = (LinearLayout) v.findViewById(R.id.vote_index_item_rcontent_llay);

            vote_index_item_result_sh_btn = (LinearLayout) v.findViewById(R.id.vote_index_item_result_sh_btn);

            vote_index_item_godetail_llay = (LinearLayout) v.findViewById(R.id.vote_index_item_godetail_llay);

            v.setTag(this);
        }
    }

}
