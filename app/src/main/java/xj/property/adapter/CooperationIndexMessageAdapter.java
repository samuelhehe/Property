package xj.property.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import xj.property.R;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.activity.cooperation.ProviderDetailsActivity;
import xj.property.beans.NeighborListV3Bean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.utils.SmileUtils;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

/**
 * 邻居帮首页适配器
 */
public class CooperationIndexMessageAdapter extends BaseAdapter {

    private final String usertype;

    private final int screenWidth;
    Activity context;

    List<NeighborListV3Bean.NeighborListData> circleBeanList;

    private UserInfoDetailBean userInfoDetailBean = null;

    public CooperationIndexMessageAdapter(Activity context, List<NeighborListV3Bean.NeighborListData> circleBeanList) {
        this.context = context;
        this.circleBeanList = circleBeanList;
        this.userInfoDetailBean = PreferencesUtil.getLoginInfo(context);
        usertype = PreferencesUtil.getUserType(context);
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public NeighborListV3Bean.NeighborListData getItem(int position) {
        return circleBeanList.get(position);
    }

    @Override
    public int getCount() {
        return circleBeanList.size();
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getCooperationId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.common_cooperation_index_item, null);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final NeighborListV3Bean.NeighborListData circleBean = circleBeanList.get(position);
        if (circleBean == null) {
            return null;
        }

        ImageLoader.getInstance().displayImage(circleBean.getAvatar(), viewHolder.provider_details_iv_avtar, UserUtils.options);

        //// 2015/11/19 添加帮内头衔
        initBangzhuMedal(circleBean.getGrade(),viewHolder);

        viewHolder.provider_details_name_tv.setText(circleBean.getNickname());
        viewHolder.provider_details_title_tv.setText(circleBean.getTitle());

        Spannable spanAll  = SmileUtils.getSmiledText(context, circleBean.getContent());

        viewHolder.provider_details_content_tv.setText(spanAll, TextView.BufferType.SPANNABLE);

//        viewHolder.provider_details_content_tv.setText(circleBean.getContent());

        ///跳转
        viewHolder.provider_details_iv_avtar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDetail(context,circleBean);
            }
        });

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
            viewHolder.provider_details_findhe_btn.setVisibility(View.INVISIBLE);
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


        viewHolder.provider_details_go_rlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              goDetail(context,circleBean);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDetail(context,circleBean);
            }
        });
        /// 加载已经看过他的用户
        loadingGoodsHasGotursHeadImgs4(viewHolder, circleBean);


        return convertView;
    }


    /**
     * 初始化用户横条奖章图片
     */
    private void initBangzhuMedal(String userType, ViewHolder holder ) {
        if ( holder.provider_iv_user_type != null) {
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
     * @param context
     * @param circleBean
     */
    private void goDetail(Activity context, NeighborListV3Bean.NeighborListData circleBean) {
        userInfoDetailBean = PreferencesUtil.getLoginInfo(context);
        if (userInfoDetailBean != null) {
            context.startActivity(new Intent(context, ProviderDetailsActivity.class).putExtra("cooperationId",""+ circleBean.getCooperationId()));
        } else {
            Intent intent = new Intent(context, RegisterLoginActivity.class);
            context.startActivity(intent);
        }
    }


    /**
     * 加载已经kan过的用户
     */
    private void loadingGoodsHasGotursHeadImgs4(final ViewHolder viewHolder, final NeighborListV3Bean.NeighborListData circleBean) {

        final List<NeighborListV3Bean.NeighborListData.NeighborListUser> users = circleBean.getUsers();

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

        if (users.size() > 6 ){
            viewHolder.welfare_purchase_hasgoturs_lv.removeAllViews();
            viewHolder.welfare_purchase_hasgoturs_lv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goDetail(context,circleBean);
                }
            });
            for (int i = 0; i < 5; i++) {

                LinearLayout usrHeadView = (LinearLayout) View.inflate(context, R.layout.common_cooperation_details_moreurs_headlay, null);
                ImageView img = (ImageView) usrHeadView.findViewById(R.id.iv_avatar);
                img.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(users.get(i).getAvatar(), img, UserUtils.options);

                TextView welfare_purchase_hasgoturs_name_tv = (TextView) usrHeadView.findViewById(R.id.welfare_purchase_hasgoturs_name_tv);
                welfare_purchase_hasgoturs_name_tv.setText(users.get(i).getNickname());
                welfare_purchase_hasgoturs_name_tv.setVisibility(View.VISIBLE);

                usrHeadView.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams rlparams = (LinearLayout.LayoutParams) img.getLayoutParams();

                rlparams.width = screenWidth * 123 / 1080;
                rlparams.height = screenWidth * 123 / 1080;

                img.setLayoutParams(rlparams);

//                final int finalI = i;
//                usrHeadView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        userInfoDetailBean = PreferencesUtil.getLoginInfo(context);
//                        if(userInfoDetailBean!=null){
//                            Intent intent = new Intent(context, UserGroupInfoActivity.class);
//                            intent.putExtra(Config.INTENT_PARMAS2, users.get(finalI).getEmobId());
//                            context.startActivity(intent);
//
//                        }else{
//                            Intent intent = new Intent(context, RegisterLoginActivity.class);
//                            context.startActivity(intent);
//                        }
//                    }
//                });

//                LinearLayout.LayoutParams llayparams = (LinearLayout.LayoutParams) viewHolder.welfare_purchase_hasgoturs_lv.getLayoutParams();
//                viewHolder.welfare_purchase_hasgoturs_lv.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                viewHolder.welfare_purchase_hasgoturs_lv.addView(usrHeadView);

            }

            /// 添加一个查看更多用户按钮

            LinearLayout usrHeadView = (LinearLayout) View.inflate(context, R.layout.common_cooperation_details_moreurs_headlay, null);
            ImageView img = (ImageView) usrHeadView.findViewById(R.id.iv_avatar);
            img.setVisibility(View.VISIBLE);

            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.help_more, img);

            TextView welfare_purchase_hasgoturs_name_tv = (TextView) usrHeadView.findViewById(R.id.welfare_purchase_hasgoturs_name_tv);
            welfare_purchase_hasgoturs_name_tv.setVisibility(View.INVISIBLE);

            usrHeadView.setVisibility(View.VISIBLE);

            LinearLayout.LayoutParams rlparams = (LinearLayout.LayoutParams) img.getLayoutParams();

            rlparams.width = screenWidth * 123 / 1080;
            rlparams.height = screenWidth * 123 / 1080;

            img.setLayoutParams(rlparams);

//            usrHeadView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    userInfoDetailBean = PreferencesUtil.getLoginInfo(context);
//                    if(userInfoDetailBean!=null){
//
//                        context.startActivity(new Intent(context, CooperationProviderDetailsVisitMoreActivity.class).putExtra("cooperationId", ""+circleBean.getCooperationId()));
//
//                    }else{
//                        Intent intent = new Intent(context, RegisterLoginActivity.class);
//                        context.startActivity(intent);
//                    }
//                }
//            });

//            LinearLayout.LayoutParams llayparams = (LinearLayout.LayoutParams) viewHolder.welfare_purchase_hasgoturs_lv.getLayoutParams();
//            viewHolder.welfare_purchase_hasgoturs_lv.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            viewHolder.welfare_purchase_hasgoturs_lv.addView(usrHeadView);
        }

        if (users.size() > 0 && users.size() <= 6) {

            viewHolder.welfare_purchase_hasgoturs_lv.removeAllViews();
            viewHolder.welfare_purchase_hasgoturs_lv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goDetail(context,circleBean);
                }
            });
            for (int i = 0; i < users.size(); i++) {

                LinearLayout usrHeadView = (LinearLayout) View.inflate(context, R.layout.common_cooperation_details_moreurs_headlay, null);

                ImageView img = (ImageView) usrHeadView.findViewById(R.id.iv_avatar);
                img.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(users.get(i).getAvatar(), img, UserUtils.options);

                TextView welfare_purchase_hasgoturs_name_tv = (TextView) usrHeadView.findViewById(R.id.welfare_purchase_hasgoturs_name_tv);
                welfare_purchase_hasgoturs_name_tv.setText(users.get(i).getNickname());

                welfare_purchase_hasgoturs_name_tv.setVisibility(View.VISIBLE);

                usrHeadView.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams rlparams = (LinearLayout.LayoutParams) img.getLayoutParams();

                rlparams.width = screenWidth * 123 / 1080;
                rlparams.height = screenWidth * 123 / 1080;

                img.setLayoutParams(rlparams);

//                final int finalI = i;
//                usrHeadView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        userInfoDetailBean = PreferencesUtil.getLoginInfo(context);
//                        if(userInfoDetailBean!=null){
//
//                            Intent intent = new Intent(context, UserGroupInfoActivity.class);
//                            intent.putExtra(Config.INTENT_PARMAS2, users.get(finalI).getEmobId());
//                            context.startActivity(intent);
//
//                        }else{
//                            Intent intent = new Intent(context, RegisterLoginActivity.class);
//                            context.startActivity(intent);
//                        }
//
//                    }
//                });

//                LinearLayout.LayoutParams llayparams = (LinearLayout.LayoutParams) viewHolder.welfare_purchase_hasgoturs_lv.getLayoutParams();
//                viewHolder.welfare_purchase_hasgoturs_lv.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                viewHolder.welfare_purchase_hasgoturs_lv.addView(usrHeadView);

            }
        }
    }


    class ViewHolder {


        private TextView provider_details_name_tv;
        private TextView provider_details_title_tv;
        private TextView provider_details_content_tv;
        private Button provider_details_findhe_btn;
        private LinearLayout panic_has_purchase_llay;
        private LinearLayout welfare_purchase_hasgoturs_lv;
        private ImageView provider_details_iv_avtar;

        private ImageView provider_iv_user_type;

        private RelativeLayout provider_details_go_rlay;

        ViewHolder(View v) {

            provider_details_iv_avtar = (ImageView) v.findViewById(R.id.provider_details_iv_avtar);

            provider_iv_user_type = (ImageView) v.findViewById(R.id.provider_iv_user_type);

            provider_details_name_tv = (TextView) v.findViewById(R.id.provider_details_name_tv);

            provider_details_title_tv = (TextView) v.findViewById(R.id.provider_details_title_tv);

            provider_details_content_tv = (TextView) v.findViewById(R.id.provider_details_content_tv);

            provider_details_findhe_btn = (Button) v.findViewById(R.id.provider_details_findhe_btn);

            panic_has_purchase_llay = (LinearLayout) v.findViewById(R.id.panic_has_purchase_llay);

            welfare_purchase_hasgoturs_lv = (LinearLayout) v.findViewById(R.id.welfare_purchase_hasgoturs_lv);

            provider_details_go_rlay = (RelativeLayout) v.findViewById(R.id.provider_details_go_rlay);

            v.setTag(this);
        }
    }

}
