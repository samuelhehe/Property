package xj.property.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.easemob.EMValueCallBack;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.RetrofitError;
import retrofit.client.Response;
import xj.property.R;
import xj.property.activity.HXBaseActivity.ChatActivity;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.activity.LifeCircle.FriendZoneIndexActivity;
import xj.property.activity.LifeCircle.LifeSearchActivity;
import xj.property.activity.runfor.RunForActivity;
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.beans.LifeCircleBean;
import xj.property.beans.LifeCircleDetail;
import xj.property.beans.LifeCircleSuperZanBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.cache.ZambiaCache;
import xj.property.event.EvaEvent;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseUtils;
import xj.property.netbasebean.GeneralZanReqBean;
import xj.property.provider.ShareProvider;
import xj.property.utils.CommonUtils;
import xj.property.utils.DensityUtil;
import xj.property.utils.SmileUtils;
import xj.property.utils.ToastUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.FriendZoneUtil;
import xj.property.utils.other.GroupUtils;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;
import xj.property.widget.FilterFlowLayout;

/**
 * Created by Administrator on 2015/6/9.
 */
public class LifeMessageAdapter extends BaseAdapter {

    private static final int HAS_PRAISED_FLAG = 1;
    private static final int HAS_NO_PRAISED_FLAG = 0;
    private static final int MAX_CONTENT_LINES = 3;
    private final String usertype;
    Activity context;

    List<LifeCircleBean> circleBeanList;
    private PopupWindow popupWindow;
    private TextView tv_super_zan;
    private TextView tv_normal_zan;
    private int width;
    private int tempPosition;
    private UserInfoDetailBean userInfoDetailBean = null;

    public LifeMessageAdapter(Activity context, List<LifeCircleBean> circleBeanList) {
        this.context = context;
        this.circleBeanList = circleBeanList;
        usertype = PreferencesUtil.getUserType(context);
        userInfoDetailBean = PreferencesUtil.getLoginInfo(context);
        initPopupWindow();
    }

    @Override
    public LifeCircleBean getItem(int position) {
        return circleBeanList.get(position);
    }

    @Override
    public int getCount() {
        return circleBeanList.size();
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getLifeCircleId();
    }


    /// life_circle_praise_icon_on
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_zone, null);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.iv_bangzhu_zan.setTag(position);
        viewHolder.iv_bangzhu_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);
            }
        });
        final LifeCircleBean circleBean = circleBeanList.get(position);
        if (circleBean == null) {
            return null;
        }
        //牛人图标显示
        if ("famous".equals(circleBean.getIdentity())) {
            viewHolder.iv_genius_image.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iv_genius_image.setVisibility(View.GONE);
        }
        //公共参数
        ImageLoader.getInstance().displayImage(circleBean.getAvatar(), viewHolder.avatar, UserUtils.options);
        viewHolder.tv_username.setText(circleBean.getNickname());
        //设置默认隐藏的图标
        viewHolder.iv_sueper_zan_icon.setVisibility(View.GONE);
        viewHolder.iv_zan_icon.setVisibility(View.GONE);
        viewHolder.iv_user_type.setVisibility(View.INVISIBLE);
        switch (PreferencesUtil.getUserType(context)) {
            case "bangzhu":
                viewHolder.iv_bangzhu_zan.setVisibility(View.VISIBLE);
                viewHolder.bangzhu_privilege_tv.setVisibility(View.VISIBLE);
                tv_super_zan.setVisibility(View.VISIBLE);
                tv_normal_zan.setVisibility(View.VISIBLE);
                break;
            case "fubangzhu":
                viewHolder.iv_bangzhu_zan.setVisibility(View.VISIBLE);
                viewHolder.bangzhu_privilege_tv.setVisibility(View.VISIBLE);
                tv_super_zan.setVisibility(View.GONE);
                tv_normal_zan.setVisibility(View.VISIBLE);
                break;
            case "zhanglao":
            case "bangzhong":
            case "normal":
                viewHolder.iv_bangzhu_zan.setVisibility(View.GONE);
                viewHolder.bangzhu_privilege_tv.setVisibility(View.GONE);
                break;
        }

        String usrType = circleBean.getGrade();
        if (TextUtils.equals("bangzhu", usrType)) {
            viewHolder.iv_user_type.setVisibility(View.VISIBLE);
            viewHolder.iv_user_type.setImageResource(R.drawable.life_circle_bangzhu_icon);
        } else if (TextUtils.equals("fubangzhu", usrType)) {
            viewHolder.iv_user_type.setVisibility(View.VISIBLE);
            viewHolder.iv_user_type.setImageResource(R.drawable.life_circle_fubangzhu_icon);
        } else if (TextUtils.equals("zhanglao", usrType)) {
            viewHolder.iv_user_type.setVisibility(View.VISIBLE);
            viewHolder.iv_user_type.setImageResource(R.drawable.life_circle_zhanglao_icon);
        } else if (TextUtils.equals("bangzhong", usrType) || TextUtils.equals("normal", usrType)) {
            viewHolder.iv_user_type.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.iv_user_type.setVisibility(View.INVISIBLE);
        }

        if (circleBean.getBzPraiseSum() != null && circleBean.getBzPraiseSum() > 0) {
            viewHolder.iv_zan_icon.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iv_zan_icon.setVisibility(View.GONE);
        }

        if (position == 0 && "yes".equals(circleBean.getSuperPraise())) {
            Log.i("debbug", "第一个超赞");
            viewHolder.iv_sueper_zan_icon.setVisibility(View.VISIBLE);
            viewHolder.iv_zan_icon.setVisibility(View.GONE);
            viewHolder.iv_bangzhu_zan.setVisibility(View.GONE);
            viewHolder.bangzhu_privilege_tv.setVisibility(View.GONE);
        }
        if (position != 0 && "yes".equals(circleBean.getSuperPraise())) {
            Log.i("debbug", "后面消失的超赞");
            viewHolder.iv_sueper_zan_icon.setVisibility(View.GONE);
        }
        List<String> lifePhotos = new ArrayList<>();
        if(!TextUtils.isEmpty(circleBean.getPhotoes())){
            lifePhotos = Arrays.asList(circleBean.getPhotoes().split(","));
        }

//        Log.i("getHasPraised  ", "circleBean.getHasPraised()  "+ circleBean.getHasPraised());

        //// 刷新是否为该人点过赞
        refreshPraise(viewHolder.tv_zambia_host, circleBean.getHasPraised());

        final Spannable spanAll = SmileUtils.getSmiledText(context, circleBean.getLifeContent());
        final Spannable spanExtAll = spanAll;
        //// 帮主竞选 type 23
        viewHolder.vote_bangzhu_go_llay.setVisibility(View.GONE);
        //设置分享内容
        if (circleBean.getType() == 23) {

        }
        viewHolder.tv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> photoList =null;
                if(circleBean.getPhotoes()!=null){
                  photoList=  Arrays.asList(circleBean.getPhotoes().split(","));
                }
                String photoUrl = "";
                if (photoList != null && !photoList.isEmpty()) {
                    photoUrl = photoList.get(0);
                }
                String url = "";
                if (circleBean.getType() == 23) {
                    url = Config.NET_SHAREBASE + "/share/bangzhu.html?communityId=" + circleBean.getCommunityId() + "&emobId=" + circleBean.getEmobId();
                    ShareProvider.getShareProvider(context).showShareActivity(url, "我在竞选我们小区的帮主", "邻居帮帮", ShareProvider.CODE_LIFECRILE);
                } else {
                    url = Config.NET_SHAREBASE + "/share/lifecircle.html?communityId=" + circleBean.getCommunityId() + "&emobId=" + circleBean.getEmobId() + "&lifeCircleId=" + circleBean.getLifeCircleId();
                    if (circleBean.getType() != 2 && !"".equals(spanAll.toString())) {//这张图片很赞，快来看看
                        ShareProvider.getShareProvider(context).showShareActivity(url, spanAll.toString(), "邻居帮帮", ShareProvider.CODE_LIFECRILE, photoUrl);
                    } else if (!"".equals(spanExtAll.toString())) {
                        ShareProvider.getShareProvider(context).showShareActivity(url, spanExtAll.toString(), "邻居帮帮", ShareProvider.CODE_LIFECRILE, photoUrl);
                    } else {
                        ShareProvider.getShareProvider(context).showShareActivity(url, "热心邻居分享的，很不错！", "邻居帮帮", ShareProvider.CODE_LIFECRILE, photoUrl);
                    }
                }
            }
        });

        switch (circleBean.getType()) {

            case 0:
            default:
                commonContentShowHide(viewHolder, spanAll, "", circleBean.getNickname());
                break;
            case 2:
                commonContentShowHide(viewHolder, spanExtAll, circleBean.getTypeContent(), circleBean.getNickname());
                break;
            case 19:
                commonContentShowHide(viewHolder, spanAll, "分享了福利", circleBean.getNickname());
                break;
            case 20:
                commonContentShowHide(viewHolder, spanAll, "分享了会员卡购物", circleBean.getNickname());
                break;
            case 24:
                commonContentShowHide(viewHolder, spanAll, "完成物业费缴纳",circleBean.getNickname());
                break;

            case 23:
                commonContentShowHide(viewHolder, spanAll, "", circleBean.getNickname());
                /// 竞选小区的名字  我在竞选狮子城小区的帮主,快来投票给我吧...
                String communityName = PreferencesUtil.getCommityName(context);
                if (TextUtils.isEmpty(communityName)) {
                    communityName = "本";
                }
                viewHolder.vote_bangzhu_for_cname_tv.setText("我在竞选" + communityName + "小区的帮主,快来投票给我吧...");
                viewHolder.vote_bangzhu_go_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goBangzhuElection(circleBean.getEmobId());
                    }
                });
                viewHolder.vote_bangzhu_go_llay.setVisibility(View.VISIBLE);
                break;

        }
        ///跳转
        viewHolder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfoDetailBean = PreferencesUtil.getLoginInfo(context);
                if (userInfoDetailBean != null) {
                    if (!TextUtils.equals(circleBean.getEmobId(), userInfoDetailBean.getEmobId())) {
                        Intent intent = new Intent(context, UserGroupInfoActivity.class);
                        intent.putExtra(Config.INTENT_PARMAS2, circleBean.getEmobId());
                        context.startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(context, RegisterLoginActivity.class);
                    context.startActivity(intent);
                }
            }
        });
        if (circleBean.getEmobGroupId() != null && !TextUtils.isEmpty(circleBean.getEmobGroupId())) {
            viewHolder.tv_joingroup.setVisibility(View.VISIBLE);
            if (PreferencesUtil.getLogin(context)) {
                viewHolder.tv_joingroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ProgressDialog progressDialog = new ProgressDialog(context);
                        progressDialog.setMessage("正在加载群组信息....");
                        progressDialog.show();

                        List<EMGroup> groups = EMGroupManager.getInstance().getAllGroups();/// 包含我创建的,我加入的...
                        //// 获取是否是我的群, 如果是, 直接进入群聊
                        if (isMyGroup(groups, circleBean.getEmobGroupId())) {
                            progressDialog.dismiss();
                            //// 进入群聊
                            Intent intentPush = new Intent(context, ChatActivity.class);
                            // it is group chat
                            intentPush.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                            intentPush.putExtra("groupId", circleBean.getEmobGroupId());
                            context.startActivity(intentPush);
                        } else {
                            EMGroupManager.getInstance().asyncGetGroupsFromServer(new EMValueCallBack<List<EMGroup>>() {
                                @Override
                                public void onSuccess(List<EMGroup> value) {
                                    /// 还没有群
                                    if (value == null || value.size() <= 0) {

                                        context.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                GroupUtils.joinGroup(context, circleBean.getEmobGroupId(), new Handler() {
                                                    @Override
                                                    public void handleMessage(Message msg) {
                                                        progressDialog.dismiss();
                                                        switch (msg.what) {
                                                            case Config.TASKERROR:
                                                                Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                                                                break;
                                                            case Config.TASKCOMPLETE:
                                                                Intent intentPush = new Intent(context, ChatActivity.class);
                                                                // it is group chat
                                                                intentPush.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                                                                intentPush.putExtra("groupId", circleBean.getEmobGroupId());
//                    intentPush.putExtra(Config.EXPKey_GROUP,context.bean.getActivityTitle());
                                                                context.startActivity(intentPush);
                                                                GroupUtils.getGroupInfo(circleBean.getEmobGroupId());
                                                                break;
                                                            case Config.NETERROR:
                                                                Toast.makeText(context, context.getString(R.string.netError), Toast.LENGTH_SHORT).show();
                                                                break;

                                                        }
                                                    }
                                                });
                                            }
                                        });
                                        return;
                                    }
                                    //// 是否是我的群
                                    if (isMyGroup(value, circleBean.getEmobGroupId())) {
                                        progressDialog.dismiss();
                                        //// 进入群聊
                                        Intent intentPush = new Intent(context, ChatActivity.class);
                                        // it is group chat
                                        intentPush.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                                        intentPush.putExtra("groupId", circleBean.getEmobGroupId());
//                    intentPush.putExtra(Config.EXPKey_GROUP,context.bean.getActivityTitle());
                                        context.startActivity(intentPush);
                                        GroupUtils.getGroupInfo(circleBean.getEmobGroupId());
                                    } else {
                                        Log.d("LIfeMessageAdapter ", "isMyGroup false emobGroupId " + circleBean.getEmobGroupId());
                                        context.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                GroupUtils.joinGroup(context, circleBean.getEmobGroupId(), new Handler() {
                                                    @Override
                                                    public void handleMessage(Message msg) {
                                                        progressDialog.dismiss();
                                                        switch (msg.what) {
                                                            case Config.TASKERROR:
                                                                ToastUtils.showToast(context,"数据错误："+msg.obj.toString());
                                                                break;
                                                            case Config.TASKCOMPLETE:
                                                                Intent intentPush = new Intent(context, ChatActivity.class);
                                                                // it is group chat
                                                                intentPush.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                                                                intentPush.putExtra("groupId", circleBean.getEmobGroupId());
//                    intentPush.putExtra(Config.EXPKey_GROUP,context.bean.getActivityTitle());
                                                                context.startActivity(intentPush);
                                                                GroupUtils.getGroupInfo(circleBean.getEmobGroupId());
                                                                break;
                                                            case Config.NETERROR:
                                                                ToastUtils.showNetErrorToast(context);
                                                                break;

                                                        }
                                                    }
                                                });
                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onError(int error, String errorMsg) {
                                    progressDialog.dismiss();
                                    ToastUtils.showToast(context, "获取群组信息失败,请稍后再试");
                                }
                            });
                        }
                    }
                });
            } else {
                viewHolder.tv_joingroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, RegisterLoginActivity.class);
                        context.startActivityForResult(intent, 0);
                    }
                });
            }
        } else {
            viewHolder.tv_joingroup.setVisibility(View.INVISIBLE);
        }

        if (circleBean.getPraiseSum() >= 100) {
            viewHolder.tv_zambia_host.setText("赞人品 (99+)");
        } else {
            viewHolder.tv_zambia_host.setText("赞人品 (" + circleBean.getPraiseSum() + ")");
        }

//        viewHolder.tv_zambia_host.setText(circleBean.getPraiseSum() + "  赞人品");


        if (circleBean.getCharacterValues() > 0) {
            viewHolder.iv_chatar.setImageResource(R.drawable.lifecircle_likeicon);
            viewHolder.tv_value.setText("" + circleBean.getCharacterValues());
        } else {
            viewHolder.iv_chatar.setImageResource(R.drawable.lifecircle_likeicon_nobodypressed);
            viewHolder.tv_value.setText("");
        }
        viewHolder.tv_time.setText(StrUtils.getDate4LifeCircleDay(circleBean.getCreateTime()));

        final TextView textView = viewHolder.tv_zambia_host;
        viewHolder.tv_zambia_host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferencesUtil.getLogin(context)) {
//为发布者点赞
                    zambia(circleBean, textView);
                } else {
                    Intent intent = new Intent(context, RegisterLoginActivity.class);
                    context.startActivityForResult(intent, 0);
                }
            }
        });

        switch (circleBean.getType()) {
            case 0:
            default:
                if (lifePhotos == null || lifePhotos.isEmpty()) {
                    viewHolder.ivcontent.setVisibility(View.GONE);
                    viewHolder.gv_pic.setVisibility(View.GONE);
                } else if (lifePhotos.size() == 1) {
                    viewHolder.gv_pic.setVisibility(View.GONE);
                    viewHolder.ivcontent.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(lifePhotos.get(0), viewHolder.ivcontent, options);
                    viewHolder.ivcontent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (context instanceof FriendZoneIndexActivity)
                                ((FriendZoneIndexActivity) context).changeCurrent(circleBean.getLifeCircleId(), 0);
                            else
                                ((LifeSearchActivity) context).changeCurrent(circleBean.getLifeCircleId(), 0);
                        }
                    });
                } else {
                    viewHolder.gv_pic.setNumColumns(3);
                    viewHolder.ivcontent.setVisibility(View.GONE);
                    viewHolder.gv_pic.setVisibility(View.VISIBLE);
                    viewHolder.gv_pic.setAdapter(new ImageAdapter(context, lifePhotos));
                    viewHolder.gv_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (context instanceof FriendZoneIndexActivity)
                                ((FriendZoneIndexActivity) context).changeCurrent(circleBean.getLifeCircleId(), position);
                            else
                                ((LifeSearchActivity) context).changeCurrent(circleBean.getLifeCircleId(), position);
                        }
                    });
                }
                break;
            case 19: /// 福利
            case 20: /// 会员卡
            case 24: /// 物业缴费
            case 2: /// 快点
                if (lifePhotos == null || lifePhotos.isEmpty()) {
                    viewHolder.ivcontent.setVisibility(View.GONE);
                    viewHolder.gv_pic.setVisibility(View.GONE);
                } else {
                    viewHolder.gv_pic.setNumColumns(4);
                    viewHolder.ivcontent.setVisibility(View.GONE);
                    viewHolder.gv_pic.setVisibility(View.VISIBLE);
                    if (lifePhotos.size() <= 4) {
                        viewHolder.gv_pic.setAdapter(new ImageAdapter(context, lifePhotos, circleBean.getType()));
                    } else {
                        List<String> lifePhotosCopy = new ArrayList<>();
                        for (int i = 0; i < 4; i++) {
                            lifePhotosCopy.add(lifePhotos.get(i));
                        }
                        viewHolder.gv_pic.setAdapter(new ImageAdapter(context, lifePhotosCopy, circleBean.getType()));
                    }
                    viewHolder.gv_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (context instanceof FriendZoneIndexActivity)
                                ((FriendZoneIndexActivity) context).changeCurrent(circleBean.getLifeCircleId(), position);
                            else
                                ((LifeSearchActivity) context).changeCurrent(circleBean.getLifeCircleId(), position);
                        }
                    });
                }
                break;

            case 23:
                if (lifePhotos == null || lifePhotos.isEmpty()) {
                    viewHolder.ivcontent.setVisibility(View.GONE);
                    viewHolder.gv_pic.setVisibility(View.GONE);
                }
                /// 竞选小区的名字  我在竞选狮子城小区的帮主,快来投票给我吧...
                String communityName = PreferencesUtil.getCommityName(context);
                if (TextUtils.isEmpty(communityName)) {
                    communityName = "本";
                }
                viewHolder.vote_bangzhu_for_cname_tv.setText("我在竞选" + communityName + "小区的帮主,快来投票给我吧...");
                viewHolder.vote_bangzhu_go_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goBangzhuElection(circleBean.getEmobId());
                    }

                });
                viewHolder.vote_bangzhu_go_llay.setVisibility(View.VISIBLE);
                break;
        }

        ///////////////"赞了你的人品"

        List<LifeCircleBean.LifePraise> lifePraises = circleBean.getLifePraises();

        if (lifePraises != null && !lifePraises.isEmpty()) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

            for (LifeCircleBean.LifePraise lp : lifePraises) {
                spannableStringBuilder.append(lp.getNickname()).append("、");

            }
            spannableStringBuilder = new SpannableStringBuilder(spannableStringBuilder.subSequence(0, spannableStringBuilder.length() - 1));

            int end = spannableStringBuilder.length();

            if (lifePraises.size() >= 3) {

                spannableStringBuilder.append("等").append("" + circleBean.getPraiseUserSum()).append("人");
            }
            spannableStringBuilder.append(" 赞了你的人品");

//            Drawable drawable = context.getResources().getDrawable(R.drawable.lifecircle_bigger_likeicon);
//            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//            spannableStringBuilder.setSpan(new ImageSpan(drawable), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#2fcc71")), 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  // 设置点赞人为绿色

            viewHolder.has_zambiaed_content_tv.setText(spannableStringBuilder);

            viewHolder.has_zambiaed_rlay.setVisibility(View.GONE);
            //// TODO xxx 等人点赞

        } else {
            viewHolder.has_zambiaed_rlay.setVisibility(View.GONE);
        }
        /// 加载评论
        List<LifeCircleDetail> lifeCircleDetails = circleBean.getLifeCircleDetails();

        viewHolder.ll_eva_content.removeAllViews();

        if (lifeCircleDetails == null || lifeCircleDetails.isEmpty()) {
//            viewHolder.line_none_content.setVisibility(View.GONE);
            //TODO

        } else {
//            viewHolder.line_none_content.setVisibility(View.VISIBLE);
            /// TODO

            LinearLayout item_evaBack = null;
            for (int i = 0; i < lifeCircleDetails.size(); i++) {
                item_evaBack = (LinearLayout) View.inflate(context, R.layout.item_evaback, null);


                viewHolder.ll_eva_content.addView(item_evaBack);

                final LifeCircleDetail lifeCircleDetail = lifeCircleDetails.get(i);
                View.OnClickListener onClickListenerEva = new View.OnClickListener() {//给评论的人回复
                    @Override
                    public void onClick(View v) {
                        EvaEvent evaEvent = new EvaEvent(lifeCircleDetail.getEmobIdFrom(), lifeCircleDetail.getFromName(),
                                lifeCircleDetail.getLifeCircleId(), lifeCircleDetail.getLifeCircleDetailId(), v);
                        EventBus.getDefault().post(evaEvent);
                    }
                };
                item_evaBack.setOnClickListener(onClickListenerEva);

                item_evaBack.findViewById(R.id.tv_zambia).setOnClickListener(onClickListenerEva);



                if (lifeCircleDetail.getEmobIdTo().equals(circleBean.getEmobId())

                        || lifeCircleDetail.getEmobIdTo().equals(lifeCircleDetail.getEmobIdFrom())) {

                    FriendZoneUtil.initEva(context, lifeCircleDetail.getFromName(), lifeCircleDetail.getEmobIdFrom(), null, null, lifeCircleDetail.getDetailContent(), lifeCircleDetail.getPraiseSum(), item_evaBack);
                } else{

                    FriendZoneUtil.initEva(context, lifeCircleDetail.getFromName(), lifeCircleDetail.getEmobIdFrom(), lifeCircleDetail.getToName(), lifeCircleDetail.getEmobIdTo(), lifeCircleDetail.getDetailContent(), lifeCircleDetail.getPraiseSum(), item_evaBack);
                }
            }
        }
        viewHolder.tv_evahost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//给楼主回复
                EvaEvent evaEvent = new EvaEvent(circleBean.getEmobId(), circleBean.getNickname(), circleBean.getLifeCircleId(), 0);
                EventBus.getDefault().post(evaEvent);
            }
        });

        //// 加载生活圈标签
        loadingLifeCircleTags(viewHolder, circleBean);

        return convertView;
    }

    private boolean isMyGroup(List<EMGroup> groups, String emobGroupId) {
        if (groups != null && groups.size() > 0) {
            for (EMGroup group : groups) {
                if (TextUtils.equals(group.getGroupId(), emobGroupId)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * 生活圈内容的显示与隐藏
     *
     * @param viewHolder
     * @param spanAll
     * @param shareGoodsName
     */
    private void commonContentShowHide(final ViewHolder viewHolder, final Spannable spanAll, String shareGoodsName, String nickname) {
        if (TextUtils.isEmpty(shareGoodsName)) {
            viewHolder.tv_share_goods.setText("");
            viewHolder.tv_share_goods.setVisibility(View.GONE);
        } else {
            viewHolder.tv_share_goods.setText(shareGoodsName);
            viewHolder.tv_share_goods.setVisibility(View.VISIBLE);
        }

//        Log.d("commonContentShowHide ","nickname "+ nickname  +" spanAll "+ spanAll.toString()+ " span length "+ spanAll.toString().length());

        viewHolder.tv_content.setText("");
//        viewHolder.tv_content.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
        viewHolder.tv_content.setText(spanAll, TextView.BufferType.SPANNABLE);


        final int lineCount = viewHolder.tv_content.getLineCount();

        viewHolder.tv_content.post(new Runnable() {
            @Override
            public void run() {
                final int lineCount = viewHolder.tv_content.getLineCount();
                if (viewHolder.tv_content.getLineCount() > 3) {
                    final int[] tempCount = {MAX_CONTENT_LINES};
                    viewHolder.tv_content.setLines(MAX_CONTENT_LINES);
                    viewHolder.tv_content.setText(spanAll, TextView.BufferType.SPANNABLE);
                    viewHolder.tv_operate.setVisibility(View.VISIBLE);
                    viewHolder.tv_operate.setText("全文");

                    viewHolder.tv_operate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //// 当前设置行数
                            if (tempCount[0] > MAX_CONTENT_LINES) {
                                tempCount[0] = MAX_CONTENT_LINES;
                                viewHolder.tv_content.setLines(MAX_CONTENT_LINES);
                                viewHolder.tv_content.setText(spanAll, TextView.BufferType.SPANNABLE);
                                viewHolder.tv_operate.setText("全文");

//                        Log.d("MAX_CONTENT_LINES lines : ", "MAX_CONTENT_LINES " + MAX_CONTENT_LINES);
                            } else {
                                tempCount[0] = lineCount;
                                /// 实际多少行
                                Log.d("real lines : ", "lineCount " + lineCount);
                                viewHolder.tv_content.setLines(lineCount);
                                viewHolder.tv_content.setText(spanAll, TextView.BufferType.SPANNABLE);
                                viewHolder.tv_operate.setText("收起");
                            }
                            viewHolder.tv_content.requestLayout();
                            viewHolder.tv_content.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    viewHolder.tv_content.setLines(lineCount);
                    viewHolder.tv_operate.setVisibility(View.GONE);
                    viewHolder.tv_operate.setText("全文");
                }
            }
        });


//        viewHolder.tv_content.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                final int lineCount = viewHolder.tv_content.getLineCount();
//                if (viewHolder.tv_content.getLineCount() > 3) {
//                    final int[] tempCount = {MAX_CONTENT_LINES};
//                    viewHolder.tv_content.setLines(MAX_CONTENT_LINES);
//                    viewHolder.tv_content.setText(spanAll, TextView.BufferType.SPANNABLE);
//                    viewHolder.tv_operate.setVisibility(View.VISIBLE);
//                    viewHolder.tv_operate.setText("全文");
//
//                    viewHolder.tv_operate.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            //// 当前设置行数
//                            if (tempCount[0] > MAX_CONTENT_LINES) {
//                                tempCount[0] = MAX_CONTENT_LINES;
//                                viewHolder.tv_content.setLines(MAX_CONTENT_LINES);
//                                viewHolder.tv_content.setText(spanAll, TextView.BufferType.SPANNABLE);
//                                viewHolder.tv_operate.setText("全文");
//
////                        Log.d("MAX_CONTENT_LINES lines : ", "MAX_CONTENT_LINES " + MAX_CONTENT_LINES);
//                            } else {
//                                tempCount[0] = lineCount;
//                                /// 实际多少行
//                                Log.d("real lines : ", "lineCount " + lineCount);
//                                viewHolder.tv_content.setLines(lineCount);
//                                viewHolder.tv_content.setText(spanAll, TextView.BufferType.SPANNABLE);
//                                viewHolder.tv_operate.setText("收起");
//                            }
//                            viewHolder.tv_content.requestLayout();
//                            viewHolder.tv_content.setVisibility(View.VISIBLE);
//                        }
//                    });
//                } else {
//                    viewHolder.tv_content.setLines(lineCount);
//                    viewHolder.tv_operate.setVisibility(View.GONE);
//                    viewHolder.tv_operate.setText("全文");
//                }
//            }
//        });


//        if (lineCount > 3) {
//            final int[] tempCount = {MAX_CONTENT_LINES};
//            viewHolder.tv_content.setLines(MAX_CONTENT_LINES);
//            viewHolder.tv_content.setText(spanAll, TextView.BufferType.SPANNABLE);
//            viewHolder.tv_operate.setVisibility(View.VISIBLE);
//            viewHolder.tv_operate.setText("全文");
//
//            viewHolder.tv_operate.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //// 当前设置行数
//                    if (tempCount[0] > MAX_CONTENT_LINES) {
//                        tempCount[0] = MAX_CONTENT_LINES;
//                        viewHolder.tv_content.setLines(MAX_CONTENT_LINES);
//                        viewHolder.tv_content.setText(spanAll, TextView.BufferType.SPANNABLE);
//                        viewHolder.tv_operate.setText("全文");
//
////                        Log.d("MAX_CONTENT_LINES lines : ", "MAX_CONTENT_LINES " + MAX_CONTENT_LINES);
//                    } else {
//                        tempCount[0] = lineCount;
//                        /// 实际多少行
//                        Log.d("real lines : ", "lineCount " + lineCount);
//                        viewHolder.tv_content.setLines(lineCount);
//                        viewHolder.tv_content.setText(spanAll, TextView.BufferType.SPANNABLE);
//                        viewHolder.tv_operate.setText("收起");
//                    }
//                    viewHolder.tv_content.requestLayout();
//                    viewHolder.tv_content.setVisibility(View.VISIBLE);
//                }
//            });
//        } else {
//            viewHolder.tv_content.setLines(lineCount);
//            viewHolder.tv_operate.setVisibility(View.GONE);
//            viewHolder.tv_operate.setText("全文");
//        }
        //Log.d("real lines outer  : ", "lineCount " + lineCount + " nickname "+ nickname );
        viewHolder.tv_content.setVisibility(View.VISIBLE);
    }

    /**
     * 跳转帮主竞选页面
     *
     * @param emobid
     */
    private void goBangzhuElection(String emobid) {

//        Toast.makeText(context,"去竞选帮主",Toast.LENGTH_LONG).show();

        UserInfoDetailBean loginInfo = PreferencesUtil.getLoginInfo(context);
        if (loginInfo != null) {
            ////  跳转帮主竞选页面
            Intent intenet = new Intent(context, RunForActivity.class);
            intenet.putExtra("uemobid", emobid);
            context.startActivity(intenet);

        } else {
            context.startActivity(new Intent(context, RegisterLoginActivity.class));
        }


    }

    /**
     * 加载生活圈标签
     *
     * @param viewHolder
     * @param circleBean
     */
    private void loadingLifeCircleTags(ViewHolder viewHolder, LifeCircleBean circleBean) {

        List<LifeCircleBean.LifeLabelBean> labels = circleBean.getLabels();

        if (labels != null && !labels.isEmpty()) {

            if (viewHolder.lifecircle_filterflowlay != null) {
                viewHolder.lifecircle_filterflowlay.removeAllViews();

                for (LifeCircleBean.LifeLabelBean lifeLabelBean : labels) {
                    View layout = View.inflate(context, R.layout.common_tags_item_forlifecircle, null);
                    TextView common_tags_name_tv = (TextView) layout.findViewById(R.id.common_tags_name_tv);
                    TextView common_tags_nums_tv = (TextView) layout.findViewById(R.id.common_tags_nums_tv);
                    common_tags_name_tv.setText(lifeLabelBean.getLabelContent());

                    int tagsCount = Integer.parseInt(lifeLabelBean.getCount());
                    if (tagsCount >= 100) {
                        common_tags_nums_tv.setText("99+");
                    } else {
                        common_tags_nums_tv.setText(lifeLabelBean.getCount());
                    }
                    viewHolder.lifecircle_filterflowlay.addView(layout);
                }
                final LifeCircleBean circleBeanFinal = circleBean;
                viewHolder.lifecircle_filterflowlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        userInfoDetailBean = PreferencesUtil.getLoginInfo(context);
                        if (userInfoDetailBean != null) {
                            Intent intent = new Intent(context, UserGroupInfoActivity.class);
                            intent.putExtra(Config.INTENT_PARMAS2, circleBeanFinal.getEmobId());
                            context.startActivity(intent);

                        } else {
                            Intent intent = new Intent(context, RegisterLoginActivity.class);
                            context.startActivity(intent);
                        }

                    }
                });

                if (labels.size() > 2) {
                    viewHolder.lifecircle_more_iv.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.lifecircle_more_iv.setVisibility(View.GONE);
                }
            }
        } else {
            if (viewHolder.lifecircle_filterflowlay != null) {
                viewHolder.lifecircle_filterflowlay.removeAllViews();
            }
            if (viewHolder.lifecircle_more_iv != null) {
                viewHolder.lifecircle_more_iv.setVisibility(View.GONE);
            }
        }
    }


    private View getThreePhoto(List<String> lifePhotos) {
        Log.i("onion", "size" + lifePhotos.size());
        LinearLayout layout = (LinearLayout) View.inflate(context, R.layout.threephotos, null);
        switch (lifePhotos.size()) {
            case 1:
                ImageLoader.getInstance().displayImage(lifePhotos.get(0), (ImageView) layout.findViewById(R.id.iv_one));
                layout.findViewById(R.id.iv_one).setTag(lifePhotos.get(0));
                break;
            case 2:
                ImageLoader.getInstance().displayImage(lifePhotos.get(0), (ImageView) layout.findViewById(R.id.iv_one));
                layout.findViewById(R.id.iv_one).setTag(lifePhotos.get(0));
                ImageLoader.getInstance().displayImage(lifePhotos.get(1), (ImageView) layout.findViewById(R.id.iv_two));
                layout.findViewById(R.id.iv_two).setTag(lifePhotos.get(1));
                break;
            case 3:
                ImageLoader.getInstance().displayImage(lifePhotos.get(0), (ImageView) layout.findViewById(R.id.iv_one));
                layout.findViewById(R.id.iv_one).setTag(lifePhotos.get(0));
                ImageLoader.getInstance().displayImage(lifePhotos.get(1), (ImageView) layout.findViewById(R.id.iv_two));
                layout.findViewById(R.id.iv_two).setTag(lifePhotos.get(1));
                ImageLoader.getInstance().displayImage(lifePhotos.get(2), (ImageView) layout.findViewById(R.id.iv_three));
                layout.findViewById(R.id.iv_three).setTag(lifePhotos.get(2));
                break;
        }
        return layout;
    }

    /**
     * //为发布者点赞
     */
    private void zambia(final LifeCircleBean circleBean, final TextView textView) {

        if (userInfoDetailBean == null) userInfoDetailBean = PreferencesUtil.getLoginInfo(context);
        if (!CommonUtils.isNetWorkConnected(context)) {
            Toast.makeText(context, context.getString(R.string.network_unavailable), Toast.LENGTH_SHORT).show();
            return;
        }

        if (textView != null) {
            textView.setClickable(false);
        }

        ZambiaCache zambiaCache = new Select().from(ZambiaCache.class).where("emobid = ? and emobidhost = ?", circleBean.getEmobId(), userInfoDetailBean.getEmobId()).executeSingle();

//       int zambiacount =  new Select().from(ZambiaCache.class).where("emobid = ? and emobidhost = ?", circleBean.getEmobId(), userInfoDetailBean.getEmobId()).count();

        String usrType = PreferencesUtil.getUserType(context);
        if (zambiaCache != null) {
            if (TextUtils.equals(usrType, "bangzhong") || TextUtils.equals(usrType, "normal")) {
                //本地检验未通过
                if (StrUtils.isInDay(zambiaCache.getZambiatime())) {
                    Toast.makeText(context, "同一天，同一人只能赞一次", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            FriendZoneUtil.zambia(circleBean.getEmobId(), circleBean.getLifeCircleId(), 1, context, new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {

                        case Config.TASKCOMPLETE:

                            /**
                             * 赞成功 人品值+1
                             *
                             */
                            circleBean.setPraiseSum(circleBean.getPraiseSum() + 1);
//                            circleBean.setPraiseUserSum(circleBean.getPraiseUserSum() + 1);

                            /// 设置成红心
                            textView.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.life_circle_praise_icon_on), null, null, null);

//                            textView.setText("赞人品 (" + circleBean.getPraiseSum() + ")");


                            if (circleBean.getPraiseSum() >= 100) {
                                textView.setText("赞人品 (99+)");
                            } else {
                                textView.setText("赞人品 (" + circleBean.getPraiseSum() + ")");
                            }

                            ZambiaCache zambiaCache = new ZambiaCache();
                            zambiaCache.setEmobid(circleBean.getEmobId());
                            zambiaCache.setEmobidhost(userInfoDetailBean.getEmobId());
                            zambiaCache.setZambiatime((int) (new Date().getTime() / 1000));
                            zambiaCache.save();

                            Toast.makeText(context, context.getString(R.string.praise), Toast.LENGTH_SHORT).show();

                            textView.setClickable(true);

                            break;

                        case Config.NETERROR:

                            Toast.makeText(context, context.getString(R.string.netError), Toast.LENGTH_SHORT).show();
                            new Delete().from(ZambiaCache.class).where("emobid = ? and emobidhost = ?", circleBean.getEmobId(), userInfoDetailBean.getEmobId()).execute();

                            refreshPraise(textView, circleBean.getHasPraised());

                            textView.setClickable(true);

                            break;
                        case Config.TASKERROR:
                            Toast.makeText(context, (String) msg.obj, Toast.LENGTH_SHORT).show();
//                            circleBean.setPraiseSum(circleBean.getPraiseUserSum());

                            refreshPraise(textView, circleBean.getHasPraised());

                            circleBean.setPraiseSum(circleBean.getPraiseSum());
//                            textView.setText("赞人品 (" + circleBean.getPraiseSum() + ")");

                            if (circleBean.getPraiseSum() >= 100) {
                                textView.setText("赞人品 (99+)");
                            } else {
                                textView.setText("赞人品 (" + circleBean.getPraiseSum() + ")");
                            }

                            textView.setClickable(true);
                            break;
                    }
                }
            });
            return;
        }

        FriendZoneUtil.zambia(circleBean.getEmobId(), circleBean.getLifeCircleId(), 1, context, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {

                    case Config.TASKCOMPLETE:

                        /**
                         * 赞成功 人品值+1
                         *
                         */
                        circleBean.setPraiseSum(circleBean.getPraiseSum() + 1);


                        if (circleBean.getPraiseSum() >= 100) {
                            textView.setText("赞人品 (99+)");
                        } else {
                            textView.setText("赞人品 (" + circleBean.getPraiseSum() + ")");
                        }
//                        textView.setText("赞人品 (" + circleBean.getPraiseSum() + ")");

                        textView.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.life_circle_praise_icon_on), null, null, null);

                        circleBean.setHasPraised(HAS_PRAISED_FLAG);

                        ZambiaCache zambiaCache = new ZambiaCache();
                        zambiaCache.setEmobid(circleBean.getEmobId());
                        zambiaCache.setEmobidhost(userInfoDetailBean.getEmobId());
                        zambiaCache.setZambiatime((int) (new Date().getTime() / 1000));
                        zambiaCache.save();

                        Toast.makeText(context, context.getString(R.string.praise), Toast.LENGTH_SHORT).show();

                        textView.setClickable(true);

                        break;
                    case Config.NETERROR:

                        Toast.makeText(context, context.getString(R.string.netError), Toast.LENGTH_SHORT).show();
                        new Delete().from(ZambiaCache.class).where("emobid = ? and emobidhost = ?", circleBean.getEmobId(), userInfoDetailBean.getEmobId()).execute();
                        textView.setClickable(true);
                        refreshPraise(textView, circleBean.getHasPraised());

                        break;


                    case Config.TASKERROR:

                        Toast.makeText(context, (String) msg.obj, Toast.LENGTH_SHORT).show();
                        circleBean.setPraiseSum(circleBean.getPraiseSum());

                        if (circleBean.getPraiseSum() >= 100) {
                            textView.setText("赞人品 (99+)");
                        } else {
                            textView.setText("赞人品 (" + circleBean.getPraiseSum() + ")");
                        }

//                        textView.setText("赞人品 (" + circleBean.getPraiseSum() + ")");
                        textView.setClickable(true);
                        refreshPraise(textView, circleBean.getHasPraised());

                        break;
                }

            }
        });

    }

    /**
     * 刷新用户当天是否为该人点过赞
     *
     * @param textView
     */
    private void refreshPraise(TextView textView, int hasPraised) {

        if (hasPraised != 1) {
            /// 设置成绿心
//            textView.setCompoundDrawables(context.getResources().getDrawable(R.drawable.green_like_icon),null,null,null);

            textView.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.green_like_icon), null, null, null);

        } else {
            /// 设置成红心
//            textView.setCompoundDrawables(context.getResources().getDrawable(R.drawable.life_circle_praise_icon_on),null,null,null);

            textView.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.life_circle_praise_icon_on), null, null, null);
        }
    }

    class ViewHolder {
        ImageView avatar, iv_genius_image, ivcontent, iv_chatar, iv_bangzhu_zan, iv_zan_icon, iv_sueper_zan_icon, iv_user_type;
        GridView gv_pic;
        //        View line_none_content;
        TextView tv_username, tv_value, tv_content, tv_evahost, tv_zambia_host, tv_time, tv_operate, tv_share_goods, bangzhu_privilege_tv, tv_comment, tv_joingroup;
        LinearLayout ll_eva_content;


        /// XXX 等 X人 赞了你的人品
        TextView has_zambiaed_content_tv;


        RelativeLayout has_zambiaed_rlay;

        FilterFlowLayout lifecircle_filterflowlay;

        ImageView lifecircle_more_iv;

        /// 去投票
        LinearLayout vote_bangzhu_go_llay;
        /// 竞选小区的名字
        TextView vote_bangzhu_for_cname_tv;
        /// 去投票Btn
        Button vote_bangzhu_go_btn;


//    LinearLayout  ll_pic_content;

        ViewHolder(View v) {
            avatar = (ImageView) v.findViewById(R.id.avatar);
            iv_genius_image = (ImageView) v.findViewById(R.id.iv_genius_image);
            ll_eva_content = (LinearLayout) v.findViewById(R.id.ll_eva_content);
            tv_operate = (TextView) v.findViewById(R.id.tv_operate);
            tv_username = (TextView) v.findViewById(R.id.tv_username);
            tv_value = (TextView) v.findViewById(R.id.tv_value);
            tv_content = (TextView) v.findViewById(R.id.tv_content);
            tv_evahost = (TextView) v.findViewById(R.id.tv_evahost);
            tv_comment = (TextView) v.findViewById(R.id.tv_comment);

            tv_joingroup = (TextView) v.findViewById(R.id.tv_joingroup);
            iv_chatar = (ImageView) v.findViewById(R.id.iv_charter);
            tv_zambia_host = (TextView) v.findViewById(R.id.tv_zambia_host);
            tv_time = (TextView) v.findViewById(R.id.tv_time);
//            line_none_content = v.findViewById(R.id.line_none_content);
            gv_pic = (GridView) v.findViewById(R.id.gv_pic);

            tv_share_goods = (TextView) v.findViewById(R.id.tv_share_goods);

            has_zambiaed_rlay = (RelativeLayout) v.findViewById(R.id.has_zambiaed_rlay);

            has_zambiaed_content_tv = (TextView) v.findViewById(R.id.has_zambiaed_content_tv);

            bangzhu_privilege_tv = (TextView) v.findViewById(R.id.bangzhu_privilege_tv);

            //ll_pic_content=(LinearLayout)v.findViewById(R.id.ll_pic_content);


            ivcontent = (ImageView) v.findViewById(R.id.iv_content);
            iv_zan_icon = (ImageView) v.findViewById(R.id.iv_zan_icon);
            iv_sueper_zan_icon = (ImageView) v.findViewById(R.id.iv_sueper_zan_icon);
            iv_bangzhu_zan = (ImageView) v.findViewById(R.id.iv_bangzhu_zan);
            iv_user_type = (ImageView) v.findViewById(R.id.iv_user_type);

            int w = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            iv_bangzhu_zan.measure(w, h);
            width = iv_bangzhu_zan.getMeasuredWidth();

            lifecircle_filterflowlay = (FilterFlowLayout) v.findViewById(R.id.lifecircle_filterflowlay);

            lifecircle_more_iv = (ImageView) v.findViewById(R.id.lifecircle_more_iv);

            vote_bangzhu_go_llay = (LinearLayout) v.findViewById(R.id.vote_bangzhu_go_llay);
            vote_bangzhu_for_cname_tv = (TextView) v.findViewById(R.id.vote_bangzhu_for_cname_tv);
            vote_bangzhu_go_btn = (Button) v.findViewById(R.id.vote_bangzhu_go_btn);


            v.setTag(this);
        }
    }

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnFail(ru.truba.touchgallery.R.drawable.default_img)
            .showImageForEmptyUri(ru.truba.touchgallery.R.drawable.default_img)
            .showImageOnLoading(ru.truba.touchgallery.R.drawable.default_img)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();

    private void initPopupWindow() {
        final View superZan = View.inflate(context, R.layout.pop_super_zan_view, null);
        popupWindow = new PopupWindow(superZan, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        tv_super_zan = (TextView) superZan.findViewById(R.id.tv_super_zan);
        tv_normal_zan = (TextView) superZan.findViewById(R.id.tv_normal_zan);

        if (TextUtils.equals(usertype, "bangzhu")) {
            tv_super_zan.setVisibility(View.VISIBLE);
            tv_normal_zan.setVisibility(View.VISIBLE);
        } else {
            tv_super_zan.setVisibility(View.GONE);
            tv_normal_zan.setVisibility(View.VISIBLE);
        }
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        tv_super_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferencesUtil.getLoginInfo(context) != null) {
                    superZan("superPraise");
                } else {

                    Intent intent = new Intent(context, RegisterLoginActivity.class);
                    context.startActivity(intent);
                }
                popupWindow.dismiss();
            }
        });

        tv_normal_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferencesUtil.getLoginInfo(context) != null) {
                    superZan("praise");
                } else {
                    Intent intent = new Intent(context, RegisterLoginActivity.class);
                    context.startActivity(intent);
                }
                popupWindow.dismiss();
            }
        });
    }

    private void showPopupWindow(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.showAsDropDown(view, -DensityUtil.dip2px(context, 81 - 15), 0);
        tempPosition = (int) view.getTag();
//        if(circleBeanList.get(tempPosition).getBzPraiseSum()!= null && circleBeanList.get(tempPosition).getBzPraiseSum()>0){
//            tv_normal_zan.setVisibility(View.GONE);
//        }
    }

    /**
     * 修改数据源置顶
     */
    private void changeDataSource() {
        LifeCircleBean tempData = circleBeanList.get(tempPosition);
        tempData.setSuperPraise("yes");
        circleBeanList.remove(tempPosition);
        circleBeanList.add(0, tempData);
    }

    /**
     * 修改数据源展示标识赞
     */
    private void pzPraiseChangeData() {
        // 1.3.0 版本,  不显示多个赞 只进行修改
        circleBeanList.get(tempPosition).setBzPraiseSum(1);
    }

    private void superZan(final String praiseMothod) {
        if(TextUtils.equals("superPraise",praiseMothod)){
            LifeCircleSuperZanBean request = new LifeCircleSuperZanBean();
            request.setLifeCircleId(circleBeanList.get(tempPosition).getLifeCircleId());
            request.setCommunityId( PreferencesUtil.getCommityId(context));
            request.setEmobIdTo(circleBeanList.get(tempPosition).getEmobId());
            request.setEmobIdFrom(PreferencesUtil.getLoginInfo(context).getEmobId());
            request.setType(LifeCircleSuperZanBean.type_superzan_lifeCircle);
            NetBaseUtils.superZan(context,request,new NetBaseUtils.NetRespListener<CommonRespBean<String>>() {
                @Override
                public void successYes(CommonRespBean<String> commonRespBean, Response response) {
                    Toast.makeText(context, "超赞！！", Toast.LENGTH_SHORT).show();
                    changeDataSource();
                    notifyDataSetChanged();
                }

                @Override
                public void successNo(CommonRespBean<String> commonRespBean, Response response) {
                    Toast.makeText(context, "" + commonRespBean.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    Toast.makeText(context, "点赞失败", Toast.LENGTH_SHORT).show();
                }
            });
        }else if(TextUtils.equals("praise",praiseMothod)){
            GeneralZanReqBean addtoblreqbean =new GeneralZanReqBean();
            addtoblreqbean.setLifeCircleId(circleBeanList.get(tempPosition).getLifeCircleId());
            addtoblreqbean.setEmobIdTo(circleBeanList.get(tempPosition).getEmobId());
            addtoblreqbean.setEmobIdFrom(PreferencesUtil.getLoginInfo(context).getEmobId());
            addtoblreqbean.setType(GeneralZanReqBean.type_superzan_lifeCircle);
            NetBaseUtils.generalZan(context,addtoblreqbean,new NetBaseUtils.NetRespListener<CommonRespBean<String>>() {
                @Override
                public void successYes(CommonRespBean<String> commonRespBean, Response response) {
                    ToastUtils.showToast(context, "赞！！");
                    pzPraiseChangeData();
                    notifyDataSetChanged();
                }
                @Override
                public void successNo(CommonRespBean<String> commonRespBean, Response response) {
                    ToastUtils.showToast(context, "" + commonRespBean.getMessage());
                }

                @Override
                public void failure(RetrofitError error) {
                    ToastUtils.showToast(context, "点赞失败");
                }
            });
        }
    }

}
