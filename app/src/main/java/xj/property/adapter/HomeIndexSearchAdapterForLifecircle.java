package xj.property.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import xj.property.R;
import xj.property.activity.LifeCircle.ZoneItemActivity;
import xj.property.beans.SearchLifeCircleResultRespBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.utils.SmileUtils;
import xj.property.utils.image.utils.Config;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.FriendZoneUtil;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;
import xj.property.widget.FilterFlowLayout;

public class HomeIndexSearchAdapterForLifecircle extends BaseAdapter {
    private static final int MAX_CONTENT_LINES = 3;
    Context context;
    List<SearchLifeCircleResultRespBean> list;
    private UserInfoDetailBean userInfoDetailBean;

    public HomeIndexSearchAdapterForLifecircle(Context context, List<SearchLifeCircleResultRespBean> list) {
        if (list != null)
            this.list = list;
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public SearchLifeCircleResultRespBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SearchLifeCircleResultRespBean data = list.get(position);

        ViewHolder vh = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.common_home_search_lifecircle_item, null);
            vh = new ViewHolder(convertView);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.tv_username.setText(data.getNickname());

        ImageLoader.getInstance().displayImage(data.getAvatar(), vh.avatar, UserUtils.options);

        String usrType = data.getGrade();
        if (TextUtils.equals("bangzhu", usrType)) {
            vh.iv_user_type.setVisibility(View.VISIBLE);
            vh.iv_user_type.setImageResource(R.drawable.life_circle_bangzhu_icon);
        } else if (TextUtils.equals("fubangzhu", usrType)) {
            vh.iv_user_type.setVisibility(View.VISIBLE);
            vh.iv_user_type.setImageResource(R.drawable.life_circle_fubangzhu_icon);
        } else if (TextUtils.equals("zhanglao", usrType)) {
            vh.iv_user_type.setVisibility(View.VISIBLE);
            vh.iv_user_type.setImageResource(R.drawable.life_circle_zhanglao_icon);
        } else if (TextUtils.equals("bangzhong", usrType) || TextUtils.equals("normal", usrType)) {
            vh.iv_user_type.setVisibility(View.INVISIBLE);
        } else {
            vh.iv_user_type.setVisibility(View.INVISIBLE);
        }

        if (data.getCharacterValues() > 0) {
            vh.iv_charter.setImageResource(R.drawable.lifecircle_likeicon);
            vh.tv_value.setText("" + data.getCharacterValues());
        } else {
            vh.iv_charter.setImageResource(R.drawable.lifecircle_likeicon_nobodypressed);
            vh.tv_value.setText("");
        }

        vh.tv_time.setText(StrUtils.getDate4LifeCircleDay(data.getCreateTime()));
        final Spannable spanAll = SmileUtils.getSmiledText(context, data.getLifeContent());
//        Spannable spanExtAll = SmileUtils.getSmiledText(context, (data.getExtContent() + "" + data.getLifeContent()));
        //// 帮主竞选 type 23
        vh.vote_bangzhu_go_llay.setVisibility(View.GONE);
        switch (data.getType()) {
            case 0:
                commonContentShowHide(vh, spanAll, "" );
                break;
            case 2:
                ///TODO  data.getExtContent()  2015/12/21
                commonContentShowHide(vh, spanAll, data.getTypeContent());
                break;
            case 19:
                commonContentShowHide(vh, spanAll, "分享了福利");
                break;
            case 20:
                commonContentShowHide(vh, spanAll, "分享了会员卡购物");
                break;
            case 23:
                commonContentShowHide(vh, spanAll, "");
                /// 竞选小区的名字  我在竞选狮子城小区的帮主,快来投票给我吧...
                String communityName = PreferencesUtil.getCommityName(context);
                if (TextUtils.isEmpty(communityName)) {
                    communityName = "帮帮";
                }
                vh.vote_bangzhu_for_cname_tv.setText("我在竞选" + communityName + "小区的帮主,快来投票给我吧...");
                vh.vote_bangzhu_go_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goLifeCircleDetails(data);
                    }
                });
                vh.vote_bangzhu_go_llay.setVisibility(View.VISIBLE);
                break;

            default:
                commonContentShowHide(vh, spanAll, "" );
                break;
        }

        vh.tv_content.setVisibility(View.VISIBLE);


        String[] pics = data.getPhotoes().split(",");
        List<String> lifePhotos = Arrays.asList(pics);
        switch (data.getType()) {
            case 0:
                if (lifePhotos == null || lifePhotos.isEmpty()) {
                    vh.ivcontent.setVisibility(View.GONE);
                    vh.gv_pic.setVisibility(View.GONE);
                } else if (lifePhotos.size() == 1) {
                    vh.gv_pic.setVisibility(View.GONE);
                    vh.ivcontent.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(lifePhotos.get(0), vh.ivcontent, UserUtils.activity_options);

                    vh.ivcontent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goLifeCircleDetails(data);
                        }
                    });
                } else {
                    vh.gv_pic.setNumColumns(3);
                    vh.ivcontent.setVisibility(View.GONE);
                    vh.gv_pic.setVisibility(View.VISIBLE);
                    vh.gv_pic.setAdapter(new HomeSearchLifecircleImageAdapter(context, lifePhotos));
                    vh.gv_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            goLifeCircleDetails(data);
                        }
                    });
                }
                break;
            case 19: /// 福利
            case 20: /// 会员卡
            case 2: /// 快点
                if (lifePhotos == null || lifePhotos.isEmpty()) {
                    vh.ivcontent.setVisibility(View.GONE);
                    vh.gv_pic.setVisibility(View.GONE);
                } else {
                    vh.gv_pic.setNumColumns(4);
                    vh.ivcontent.setVisibility(View.GONE);
                    vh.gv_pic.setVisibility(View.VISIBLE);
                    if (lifePhotos.size() <= 4) {
                        vh.gv_pic.setAdapter(new HomeSearchLifecircleImageAdapter(context, lifePhotos));
                    } else {
                        List<String> lifePhotosCopy = new ArrayList<>();
                        for (int i = 0; i < 4; i++) {
                            lifePhotosCopy.add(lifePhotos.get(i));
                        }
                        vh.gv_pic.setAdapter(new HomeSearchLifecircleImageAdapter(context, lifePhotosCopy));
                    }
                    vh.gv_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            goLifeCircleDetails(data);
                        }
                    });
                }
                break;

            case 23:
                if (lifePhotos == null || lifePhotos.isEmpty()) {
                    vh.ivcontent.setVisibility(View.GONE);
                    vh.gv_pic.setVisibility(View.GONE);
                }
                /// 竞选小区的名字  我在竞选狮子城小区的帮主,快来投票给我吧...
                String communityName = PreferencesUtil.getCommityName(context);
                if (TextUtils.isEmpty(communityName)) {
                    communityName = "帮帮";
                }
                vh.vote_bangzhu_for_cname_tv.setText("我在竞选" + communityName + "小区的帮主,快来投票给我吧...");
                vh.vote_bangzhu_go_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goLifeCircleDetails(data);
                    }
                });
                vh.vote_bangzhu_go_llay.setVisibility(View.VISIBLE);
                break;

            default:
                Log.i("debbug", "执行了default");
                break;
        }

        /// 加载评论
        List<SearchLifeCircleResultRespBean.LifeCircleDetailsEntity> lifeCircleDetails = data.getLifeCircleDetails();

        vh.ll_eva_content.removeAllViews();

        if (lifeCircleDetails == null || lifeCircleDetails.isEmpty()) {
//            viewHolder.line_none_content.setVisibility(View.GONE);
            //TODO

        } else {
//            viewHolder.line_none_content.setVisibility(View.VISIBLE);
            /// TODO

            LinearLayout item_evaBack = null;
            for (int i = 0; i < lifeCircleDetails.size(); i++) {
                item_evaBack = (LinearLayout) View.inflate(context, R.layout.item_evaback, null);

                vh.ll_eva_content.addView(item_evaBack);

                final SearchLifeCircleResultRespBean.LifeCircleDetailsEntity lifeCircleDetail = lifeCircleDetails.get(i);
                View.OnClickListener onClickListenerEva = new View.OnClickListener() {//给评论的人回复
                    @Override
                    public void onClick(View v) {
//                        EvaEvent evaEvent = new EvaEvent(lifeCircleDetail.getEmobIdFrom(), lifeCircleDetail.getFromName(),
//                                lifeCircleDetail.getLifeCircleId(), lifeCircleDetail.getLifeCircleDetailId(), v);
//                        EventBus.getDefault().post(evaEvent);
                        goLifeCircleDetails(data); /// 2015/12/21

                    }
                };
                item_evaBack.setOnClickListener(onClickListenerEva);

                item_evaBack.findViewById(R.id.tv_zambia).setOnClickListener(onClickListenerEva);

                if (lifeCircleDetail.getEmobIdTo().equals(data.getEmobId()) || lifeCircleDetail.getEmobIdTo().equals(lifeCircleDetail.getEmobIdFrom())) {
                    FriendZoneUtil.initEva(context, lifeCircleDetail.getFromName(), lifeCircleDetail.getEmobIdFrom(), null, null, lifeCircleDetail.getDetailContent(), lifeCircleDetail.getPraiseSum(), item_evaBack);
                } else
                    FriendZoneUtil.initEva(context, lifeCircleDetail.getFromName(), lifeCircleDetail.getEmobIdFrom(), lifeCircleDetail.getToName(), lifeCircleDetail.getEmobIdTo(), lifeCircleDetail.getDetailContent(), lifeCircleDetail.getPraiseSum(), item_evaBack);
            }
        }

        vh.ll_eva_content.setClickable(true);
        vh.ll_eva_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLifeCircleDetails(data);
            }
        });

        vh.tv_evahost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLifeCircleDetails(data);
            }
        });


        //// 加载生活圈标签
//        loadingLifeCircleTags(vh, data);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLifeCircleDetails(data);
            }
        });

        return convertView;
    }


    /**
     * 生活圈内容的显示与隐藏
     *
     * @param viewHolder
     * @param spanAll
     * @param shareGoodsName
     */
    private void commonContentShowHide(final ViewHolder viewHolder, final Spannable spanAll, String shareGoodsName) {
        if (TextUtils.isEmpty(shareGoodsName)) {
            viewHolder.tv_share_goods.setText("");
            viewHolder.tv_share_goods.setVisibility(View.GONE);
        } else {
            viewHolder.tv_share_goods.setText(shareGoodsName);
            viewHolder.tv_share_goods.setVisibility(View.VISIBLE);
        }
        viewHolder.tv_content.setText(spanAll, TextView.BufferType.SPANNABLE);
        viewHolder.tv_operate.setVisibility(View.GONE);
//        viewHolder.tv_content.requestLayout();
        viewHolder.tv_content.setVisibility(View.VISIBLE);
    }





    private void goLifeCircleDetails(SearchLifeCircleResultRespBean data) {

        Intent intent = new Intent(context, ZoneItemActivity.class);
        intent.putExtra(Config.INTENT_PARMAS1, data.getEmobId());
        intent.putExtra(Config.INTENT_PARMAS2, data.getLifeCircleId());
        context.startActivity(intent);
    }

    public static class ViewHolder {
        ImageView ivcontent;

        GridView gv_pic;

        ImageView avatar;
        ImageView iv_charter;
        ImageView iv_user_type;

        ImageView lifecircle_more_iv;

        TextView tv_username;
        TextView tv_value;
        TextView tv_time;

        TextView tv_content;

        TextView vote_bangzhu_for_cname_tv;
        Button vote_bangzhu_go_btn;

        LinearLayout vote_bangzhu_go_llay;

        LinearLayout ll_eva_content;

        TextView tv_evahost;

        TextView tv_share_goods;

        TextView tv_operate;

        FilterFlowLayout lifecircle_filterflowlay;


        ViewHolder(View v) {
            avatar = (ImageView) v.findViewById(R.id.avatar);

            iv_charter = (ImageView) v.findViewById(R.id.iv_charter);

            iv_user_type = (ImageView) v.findViewById(R.id.iv_user_type);

            gv_pic = (GridView) v.findViewById(R.id.gv_pic);

            ivcontent = (ImageView) v.findViewById(R.id.iv_content);

            lifecircle_more_iv = (ImageView) v.findViewById(R.id.lifecircle_more_iv);

            tv_username = (TextView) v.findViewById(R.id.tv_username);

            tv_value = (TextView) v.findViewById(R.id.tv_value);

            tv_time = (TextView) v.findViewById(R.id.tv_time);

            tv_content = (TextView) v.findViewById(R.id.tv_content);

            tv_evahost = (TextView) v.findViewById(R.id.tv_evahost);

            tv_share_goods = (TextView) v.findViewById(R.id.tv_share_goods);

            tv_operate = (TextView) v.findViewById(R.id.tv_operate);

            vote_bangzhu_for_cname_tv = (TextView) v.findViewById(R.id.vote_bangzhu_for_cname_tv);

            vote_bangzhu_go_btn = (Button) v.findViewById(R.id.vote_bangzhu_go_btn);

            vote_bangzhu_go_llay = (LinearLayout) v.findViewById(R.id.vote_bangzhu_go_llay);

            ll_eva_content = (LinearLayout) v.findViewById(R.id.ll_eva_content);

            lifecircle_filterflowlay  = (FilterFlowLayout) v.findViewById(R.id.lifecircle_filterflowlay);

            v.setTag(this);
        }

    }


}
