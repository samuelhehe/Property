package xj.property.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.TextUtils;
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
import xj.property.beans.IHasLaunchedVoteRespBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.utils.DensityUtil;
import xj.property.utils.SmileUtils;
import xj.property.utils.TimeUtils;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

/**
 * 投票我发起的投票
 */
public class ILaunchedVoteMessageAdapter extends BaseAdapter {

    private final String usertype;

    private final int screenWidth;


    private LayoutInflater layoutInflator;

    private String[] colorArray = {"#54C7C0", "#EEB355", "#FEADFF", "#60AFE6"};

    /// 默认显示4行
    private static final int DEFAULT_SHOW_LINES = 4;
    //// 实时显示的行数
    private int showLines = DEFAULT_SHOW_LINES;

    SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");

    Activity context;

    List<IHasLaunchedVoteRespBean.PageDataEntity> circleBeanList;

    private UserInfoDetailBean userInfoDetailBean = null;

    public ILaunchedVoteMessageAdapter(Activity context, List<IHasLaunchedVoteRespBean.PageDataEntity> circleBeanList) {
        this.context = context;
        this.circleBeanList = circleBeanList;
        layoutInflator = LayoutInflater.from(context);
        this.userInfoDetailBean = PreferencesUtil.getLoginInfo(context);
        usertype = PreferencesUtil.getUserType(context);
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public IHasLaunchedVoteRespBean.PageDataEntity getItem(int position) {
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

        final IHasLaunchedVoteRespBean.PageDataEntity circleBean = circleBeanList.get(position);
        if (circleBean == null) {
            return null;
        }
        ImageLoader.getInstance().displayImage(circleBean.getAvatar(), viewHolder.provider_details_iv_avtar, UserUtils.options);

        //// 2015/12/10 添加帮内头衔
        initBangzhuMedal(circleBean.getGrade(), viewHolder);

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

        viewHolder.vote_index_item_time_tv.setText(TimeUtils.fromLongToString2(circleBean.getCreateTime() + ""));

        if (TextUtils.equals(circleBean.getStatus(), "unpass")) {
            viewHolder.vote_index_item_status_tv.setText("审核失败");
            viewHolder.vote_index_item_status_tv.setTextColor(Color.parseColor("#FF9000"));
        } else if (TextUtils.equals(circleBean.getStatus(), "auditing")) {
            viewHolder.vote_index_item_status_tv.setText("审核中...");
            viewHolder.vote_index_item_status_tv.setTextColor(Color.parseColor("#B8B8B8"));
        } else if (TextUtils.equals(circleBean.getStatus(), "normal")) {
            viewHolder.vote_index_item_status_tv.setText("审核通过");
            viewHolder.vote_index_item_status_tv.setTextColor(Color.parseColor("#2FCC71"));
        }

        initBangzhuMedal(circleBean.getGrade(), viewHolder);

        viewHolder.vote_index_item_godetail_llay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goDetail(context, circleBean);
            }
        });


//        ///跳转
//        viewHolder.provider_details_iv_avtar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goDetail(context,circleBean);
//            }
//        });


//        viewHolder.provider_details_iv_avtar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                userInfoDetailBean = PreferencesUtil.getLoginInfo(context);
//                if(userInfoDetailBean!=null){
//                    Intent intent = new Intent(context, UserGroupInfoActivity.class);
//                    intent.putExtra(Config.INTENT_PARMAS2, circleBean.getEmobId());
//                    context.startActivity(intent);
//
//                }else{
//                    Intent intent = new Intent(context, RegisterLoginActivity.class);
//                    context.startActivity(intent);
//                }
//            }
//        });


//        if(TextUtils.equals(circleBean.getEmobId(),userInfoDetailBean.getEmobId())){
//            viewHolder.provider_details_findhe_btn.setVisibility(View.INVISIBLE);
//        }else{
//            viewHolder.provider_details_findhe_btn.setVisibility(View.VISIBLE);
//            viewHolder.provider_details_findhe_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (PreferencesUtil.getLogin(context)) {
//                        if (circleBean != null) {
//
//                            XJContactHelper.saveContact(circleBean.getEmobId(), circleBean.getNickname(), circleBean.getAvatar(), "-1");
//                            Intent intentPush = new Intent();
//                            intentPush.putExtra("userId", circleBean.getEmobId());//tz
//                            intentPush.putExtra(Config.EXPKey_nickname, circleBean.getNickname());
//                            intentPush.putExtra(Config.EXPKey_avatar, circleBean.getAvatar());
//                            intentPush.setClass(context, ChatActivity.class);
//                            intentPush.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
//                            context.startActivity(intentPush);
//                        } else {
//
//                            Log.d("provider_details_findhe_btn ", "circleBean  is " + circleBean);
//                        }
//                    } else {
//                        Intent intent = new Intent(context, RegisterLoginActivity.class);
//                        context.startActivityForResult(intent, 0);
//                    }
//                }
//            });
//        }


//        viewHolder.provider_details_go_rlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//              goDetail(context,circleBean);
//            }
//        });
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goDetail(context,circleBean);
//            }
//        });

        if (TextUtils.equals(circleBean.getStatus(), "normal")) {
            ///  加载投票结果条形图
            loadingVoteResult(viewHolder, DEFAULT_SHOW_LINES, circleBean);
        } else {
            /// 设置条形图不可见
            viewHolder.vote_index_item_result_llay.setVisibility(View.GONE);
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
     * 跳转到投票详情
     *
     * @param context
     * @param circleBean
     */
    private void goDetail(Activity context, IHasLaunchedVoteRespBean.PageDataEntity circleBean) {
        userInfoDetailBean = PreferencesUtil.getLoginInfo(context);
        if (userInfoDetailBean != null) {
            context.startActivity(new Intent(context, VoteDetailsActivity.class).putExtra("voteId", "" + circleBean.getVoteId()));
        } else {
            Intent intent = new Intent(context, RegisterLoginActivity.class);
            context.startActivity(intent);
        }
    }

    /**
     * 投票的结果展示
     *
     * @param viewHolder
     */
    private void loadingVoteResult(ViewHolder viewHolder, final int showLines, final IHasLaunchedVoteRespBean.PageDataEntity infoBean) {

        List<IHasLaunchedVoteRespBean.PageDataEntity.VoteOptionsListEntity> voteOptionsList = infoBean.getOptions();

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
            final IHasLaunchedVoteRespBean.PageDataEntity infoBean1 = infoBean;
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
                IHasLaunchedVoteRespBean.PageDataEntity.VoteOptionsListEntity optionsListEntity = voteOptionsList.get(i);
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

//
//                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, DensityUtil.dip2px(context, 10f), leftPercent);
////                p.setMargins(0,DensityUtil.dip2px(context,3),0,DensityUtil.dip2px(context,3));
//
//                p.gravity = Gravity.CENTER_VERTICAL;

                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, DensityUtil.dip2px(context, 10f), leftPercent);
//                p.setMargins(0,DensityUtil.dip2px(context,3),0,DensityUtil.dip2px(context,3));
                p.gravity = Gravity.CENTER_VERTICAL;


                leftlay.setLayoutParams(p);

                int index = i % colorArray.length;

                Drawable bgDrawable = null;
                //// 默认四种颜色
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

//                leftlay.setBackgroundColor(Color.parseColor(colorArray[i % 4]));

                LinearLayout rightlay = (LinearLayout) percentItem.findViewById(R.id.vote_result_percent_right_llay);


                LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, rightPercent);
//                rp.gravity = Gravity.CENTER_VERTICAL;
//                rp.setMargins(0, 0, 0, DensityUtil.dip2px(context, 3f));
                rightlay.setLayoutParams(rp);

                TextView vote_right_percent_tv = (TextView) rightlay.findViewById(R.id.vote_right_percent_tv);


                String count = ""+optionsListEntity.getCount();
                if(optionsListEntity.getCount()>=1000){
                    count = "999+";
                }
                String percentStr = optionsListEntity.getPercentText()+"% ("+ count+"票)";

                vote_right_percent_tv.setText(percentStr);

                TextView vote_result_pername_tv = (TextView) percentItem.findViewById(R.id.vote_result_pername_tv);

                vote_result_pername_tv.setText(optionsListEntity.getContent());

                ImageView vote_result_percent_chosed_iv = (ImageView) percentItem.findViewById(R.id.vote_result_percent_chosed_iv);

                if (optionsListEntity.getVoted() == 1) {
                    vote_result_percent_chosed_iv.setVisibility(View.VISIBLE);
                } else {
                    vote_result_percent_chosed_iv.setVisibility(View.INVISIBLE);
                }

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 0);
                viewHolder.vote_index_item_rcontent_llay.addView(percentItem, params);
            }
            viewHolder.vote_index_item_result_llay.setVisibility(View.VISIBLE);
            viewHolder.vote_index_item_result_llay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goDetail(context, infoBean);
                }
            });

        } else {
            /// 设置为不可见
            viewHolder.vote_index_item_result_llay.setVisibility(View.GONE);
        }
    }

    private float getMaxPercentVote(List<IHasLaunchedVoteRespBean.PageDataEntity.VoteOptionsListEntity> voteOptionsList) {

        float percent = voteOptionsList.get(0).getPercent();

        for (IHasLaunchedVoteRespBean.PageDataEntity.VoteOptionsListEntity optionsListEntity : voteOptionsList) {

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
        /// 投票审核状态
        private TextView vote_index_item_status_tv;

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

            vote_index_item_status_tv = (TextView) v.findViewById(R.id.vote_index_item_status_tv);

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
