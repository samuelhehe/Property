package xj.property.activity.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.easemob.EMValueCallBack;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.PUT;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.ChatActivity;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.beans.ActivityBean;
import xj.property.beans.LifeCircleSuperZanBean;
import xj.property.beans.ResultInfoBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.cache.ActivityReaded;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseUtils;
import xj.property.netbasebean.GeneralZanReqBean;
import xj.property.utils.DensityUtil;
import xj.property.utils.SmileUtils;
import xj.property.utils.ToastUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.GroupUtils;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

public class ActivityDetailActivity extends HXBaseActivity implements JoinInGroupApplyConfrimDialog.OnJoinInStatusListener {

    private static final int SUPER_ZAN = 2;
    private static final int NORMAL_ZAN = 1;

    private ImageView iv_img;
    private TextView tv_detail_activityDetail;
    private TextView tv_title_activityDetail;
    private TextView tv_groupnum;
    private TextView tv_time;
    private TextView tv_address;
    private TextView tv_week;
    private RelativeLayout rl_chat_activityDetail;
    private TextView btn_chat_activityDetail;
    private ActivityBean bean;
    private xj.property.widget.MyGridView gv_pic;
    private PicAdapter adapter;
    private ViewPager vp_Pic;
    private ArrayList<ImageView> images = new ArrayList<ImageView>();

    //帮助点赞的时候用的控件
    private PopupWindow popupWindow;
    private TextView tv_super_zan;
    private TextView tv_normal_zan;
    private int width;
    private TextView tv_zhanglao_zan;
    private int zanType = -9;
    private String usertype;

    private JoinInGroupApplyConfrimDialog joinInGroupApplyConfrimDialog;
    private LinearLayout group_pics_llay;

    private xj.property.widget.MyGridView activity_group_memeber_mgv;
    private LinearLayout group_memeber_llay;
    private ProgressDialog progressDialog;
    private LinearLayout act_detail_time_llay;
    private LinearLayout act_detail_address_llay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_detail);
        bean = (ActivityBean) getIntent().getSerializableExtra(Config.INTENT_PARMAS1);
        usertype = PreferencesUtil.getUserType(this);
        initView();
        initTitle(null, "活动详情", "");

        initPopupWindow();

//        loadAllGroupInfo();
    }

    ///// 从环信服务器拉取所有群组信息
    private void loadAllGroupInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMGroupManager.getInstance().getAllPublicGroupsFromServer();
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initView() {
//        findViewById(R.id.iv_right).setVisibility(View.INVISIBLE);

        tv_detail_activityDetail = (TextView) findViewById(R.id.tv_detail_activityDetail);
        tv_groupnum = (TextView) findViewById(R.id.tv_groupnum);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_title_activityDetail = (TextView) findViewById(R.id.tv_title_activityDetail);
        tv_week = (TextView) findViewById(R.id.tv_week);
        tv_zhanglao_zan = (TextView) findViewById(R.id.tv_zhanglao_zan);
        /// 正常角色, 帮众,长老不能点赞
        group_pics_llay = (LinearLayout) findViewById(R.id.group_pics_llay);
        group_memeber_llay = (LinearLayout) findViewById(R.id.group_memeber_llay);

        act_detail_time_llay = (LinearLayout) findViewById(R.id.act_detail_time_llay);
        act_detail_address_llay = (LinearLayout) findViewById(R.id.act_detail_address_llay);


        if (null != bean.getUsers() && bean.getUsers().size() > 0) {
            activity_group_memeber_mgv = (xj.property.widget.MyGridView) findViewById(R.id.activity_group_memeber_mgv);
            activity_group_memeber_mgv.setAdapter(new PicAdapter2(getmContext(), bean.getUsers()));
            group_memeber_llay.setVisibility(View.VISIBLE);
        } else {
            group_memeber_llay.setVisibility(View.GONE);
        }
        usertype = PreferencesUtil.getUserType(this);
        if (TextUtils.equals(usertype, "normal") || TextUtils.equals(usertype, "bangzhong") || TextUtils.equals(usertype, Config.USER_TYPE_ZHANGLAO)) {
            tv_zhanglao_zan.setVisibility(View.GONE);
            tv_zhanglao_zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showPopupWindow(v);
                }
            });
        } else {
            tv_zhanglao_zan.setVisibility(View.VISIBLE);
            tv_zhanglao_zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showPopupWindow(v);
                }
            });
        }

        tv_title_activityDetail.setText(bean.getActivityTitle());

        Spannable spanAll = SmileUtils.getSmiledText(getmContext(), bean.getActivityDetail());
        tv_detail_activityDetail.setText(spanAll, TextView.BufferType.SPANNABLE);

//        tv_detail_activityDetail.setText(bean.getActivityDetail());
        if (bean.getActivityTime() <= 0) {
            tv_time.setVisibility(View.GONE);
            tv_week.setVisibility(View.GONE);
            act_detail_time_llay.setVisibility(View.GONE);
        } else {
            tv_time.setText(StrUtils.getDate24Millions(bean.getActivityTime() * 1000L));
            tv_week.setText("(" + StrUtils.getWeek4Millions(bean.getActivityTime() * 1000L) + ")");
            act_detail_time_llay.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(bean.getPlace())) {
            tv_address.setVisibility(View.GONE);
            act_detail_address_llay.setVisibility(View.GONE);
        } else {
            tv_address.setText(bean.getPlace());
            act_detail_address_llay.setVisibility(View.VISIBLE);
        }
        tv_groupnum.setText(bean.getActivityUserSum());
        if (!TextUtils.isEmpty(bean.getPhotoes())) {
            gv_pic = (xj.property.widget.MyGridView) findViewById(R.id.gv_activity_pic);
            vp_Pic = (ViewPager) findViewById(R.id.vp_pic);
            adapter = new PicAdapter(this, Arrays.asList(bean.getPhotoes().split(",")));
            gv_pic.setAdapter(adapter);
            gv_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    vp_Pic.setVisibility(View.VISIBLE);
                    vp_Pic.setCurrentItem(position);
                }
            });
            final  List<String> photos = Arrays.asList(bean.getPhotoes().split(","));
            for (int i = 0; i < photos.size(); i++)
                images.add(new ImageView(ActivityDetailActivity.this));
                  vp_Pic.setAdapter(new PagerAdapter() {

                @Override
                public int getCount() {
                    return photos.size();
                }

                @Override
                public boolean isViewFromObject(View view, Object object) {
                    return view.equals(object);
                }

                // PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
                @Override
                public void destroyItem(ViewGroup view, int position, Object object) {
                    view.removeView(images.get(position));
                }
                // 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
                @Override
                public Object instantiateItem(ViewGroup view, int position) {
                    images.get(position).setOnClickListener(ActivityDetailActivity.this);
                    view.addView(images.get(position));
                    ImageLoader.getInstance().displayImage(photos.get(position), images.get(position));
                    return images.get(position);
                }

            });
            /// 群图片可见
            group_pics_llay.setVisibility(View.VISIBLE);
        } else {
            /// 群图片不可见
            group_pics_llay.setVisibility(View.GONE);

        }
        final String emobGroupId = bean.getEmobGroupId();
        final String emobGroupOwner = bean.getEmobIdOwner();

        rl_chat_activityDetail = (RelativeLayout) findViewById(R.id.rl_chat_activityDetail);
        btn_chat_activityDetail = (TextView) findViewById(R.id.btn_chat_activityDetail);
        iv_img = (ImageView) findViewById(R.id.iv_img);
        rl_chat_activityDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final UserInfoDetailBean loginInfo = PreferencesUtil.getLoginInfo(ActivityDetailActivity.this);
                /// 登陆状态
                if (loginInfo != null) {
                    List<EMGroup> groups = EMGroupManager.getInstance().getAllGroups();/// 包含我创建的,我加入的...

                    //// 获取是否是我的群, 如果是, 直接进入群聊
                    if (isMyGroup(groups, emobGroupId) || TextUtils.equals(loginInfo.getEmobId(), emobGroupOwner)) {
                        //// 进入群聊
                        Intent intentPush = new Intent(ActivityDetailActivity.this, ChatActivity.class);
                        // it is group chat
                        intentPush.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                        intentPush.putExtra("groupId", emobGroupId);
//                    intentPush.putExtra(Config.EXPKey_GROUP,context.bean.getActivityTitle());
                        startActivity(intentPush);
//                        GroupUtils.getGroupInfo(emobGroupId); /// 这个接口作用? /// 2015/12/29
                        /// 申请加群
                    } else {
                        if (TextUtils.isEmpty(emobGroupId)) {
                            showToast("群组数据异常");
                        } else {
                            //// 如果群需要申请,则进入申请逻辑
                            if (bean != null && "yes".equals(bean.getApproval())) {

                                if (progressDialog == null) {
                                    progressDialog = new ProgressDialog(ActivityDetailActivity.this);
                                    progressDialog.setCanceledOnTouchOutside(false);
                                }
                                progressDialog.setMessage("正在加载群组信息....");
                                progressDialog.show();
                                EMGroupManager.getInstance().asyncGetGroupsFromServer(new EMValueCallBack<List<EMGroup>>() {

                                    @Override
                                    public void onSuccess(List<EMGroup> value) {
                                        progressDialog.dismiss();
                                        /// 还没有群
                                        if (value == null || value.size() <= 0) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    joinInGroupApplyConfrimDialog = new JoinInGroupApplyConfrimDialog(ActivityDetailActivity.this, emobGroupOwner, emobGroupId, ActivityDetailActivity.this);
                                                    joinInGroupApplyConfrimDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                        @Override
                                                        public void onDismiss(DialogInterface dialog) {

                                                        }
                                                    });
                                                    joinInGroupApplyConfrimDialog.setCancelable(true);
                                                    joinInGroupApplyConfrimDialog.show();
                                                }
                                            });
                                            return;
                                        }
                                        //// 是否是我的群
                                        if (isMyGroup(value, emobGroupId)) {
                                            //// 进入群聊
                                            Intent intentPush = new Intent(ActivityDetailActivity.this, ChatActivity.class);
                                            // it is group chat
                                            intentPush.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                                            intentPush.putExtra("groupId", emobGroupId);
                                            startActivity(intentPush);
                                        } else {
                                            Log.d("ActivityDetailActivity ", "isMyGroup false emobGroupId " + emobGroupId);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    joinInGroupApplyConfrimDialog = new JoinInGroupApplyConfrimDialog(ActivityDetailActivity.this, emobGroupOwner, emobGroupId, ActivityDetailActivity.this);
                                                    joinInGroupApplyConfrimDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                        @Override
                                                        public void onDismiss(DialogInterface dialog) {

                                                        }
                                                    });
                                                    joinInGroupApplyConfrimDialog.setCancelable(true);
                                                    joinInGroupApplyConfrimDialog.show();
                                                    Log.d("ActivityDetailActivity ", "isMyGroup false emobGroupId " + emobGroupId + "joinInGroupApplyConfrimDialog.show() end ");
                                                }
                                            });

                                        }
                                    }

                                    @Override
                                    public void onError(int error, String errorMsg) {
                                        progressDialog.dismiss();
                                        showToast("获取群组信息失败,请稍后再试");
                                    }
                                });
                            } else {
                                if (progressDialog == null) {
                                    progressDialog = new ProgressDialog(ActivityDetailActivity.this);
                                    progressDialog.setCanceledOnTouchOutside(false);
                                }
                                progressDialog.setMessage("正在进入群组....");
                                progressDialog.show();

                                //// 加群方法  不需要进行群组申请的情况下调用
                                GroupUtils.joinGroup(ActivityDetailActivity.this, emobGroupId, new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        switch (msg.what) {
                                            case Config.TASKERROR:
                                                progressDialog.dismiss();
                                                showToast(msg.obj.toString());
                                                break;
                                            case Config.TASKCOMPLETE:
                                                Intent intentPush = new Intent(ActivityDetailActivity.this, ChatActivity.class);
                                                // it is group chat
                                                intentPush.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                                                intentPush.putExtra("groupId", emobGroupId);
//                    intentPush.putExtra(Config.EXPKey_GROUP,context.bean.getActivityTitle());
                                                startActivity(intentPush);
                                                progressDialog.dismiss();
                                                GroupUtils.getGroupInfo(emobGroupId);
                                                break;
                                            case Config.NETERROR:
                                                progressDialog.dismiss();
                                                showNetErrorToast();
                                                break;

                                        }
                                    }
                                });
                            }

                        }
                    }
                    //// 去登陆
                } else {
                    Intent intent = new Intent(ActivityDetailActivity.this, RegisterLoginActivity.class);
                    startActivity(intent);

                }
                //进入群聊
//                testCache();
//                ActivityGroupModel activityGroupModel = getRandom();
//                logger.info("emod_id is :"+ activityGroupModel.emobGroupId);
            }
        });

        if (bean.getStatus().equals("review")) {
            iv_img.setVisibility(View.GONE);
            rl_chat_activityDetail.setClickable(false);
            rl_chat_activityDetail.setBackgroundColor(this.getResources().getColor(R.color.voip_interface_text_color_bg));
            btn_chat_activityDetail.setTextColor(this.getResources().getColor(R.color.voip_interface_text_color));
            btn_chat_activityDetail.setText("审核中");
            btn_chat_activityDetail.setGravity(Gravity.CENTER);
        }
        ActivityReaded readed = new Select().from(ActivityReaded.class).where("activity_id = ? ", bean.getActivityId()).executeSingle();
        if (!readed.isreaded) {
            int count = PreferencesUtil.getUnReadCount(this) - 1;
            PreferencesUtil.saveUnReadCount(this, count);
        }
        readed.isreaded = true;
        readed.save();

    }


    @Override
    public void onjoinSuccess(String s) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast("申请成功,等待审核");

            }
        });
    }

    @Override
    public void onjoinFail(String reason) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast("申请数据异常,请稍后重试");
            }
        });

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 退出系统
            if (vp_Pic == null || vp_Pic.getVisibility() == View.GONE) {
                finish();
                return true;
            }
            vp_Pic.setVisibility(View.GONE);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (joinInGroupApplyConfrimDialog != null) {
            joinInGroupApplyConfrimDialog.dismiss();
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        vp_Pic.setVisibility(View.GONE);
    }

    private void initPopupWindow() {
        View superZan = View.inflate(this, R.layout.pop_super_zan_view, null);
        popupWindow = new PopupWindow(superZan, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        tv_super_zan = (TextView) superZan.findViewById(R.id.tv_super_zan);
        tv_normal_zan = (TextView) superZan.findViewById(R.id.tv_normal_zan);

        if (TextUtils.equals(usertype, Config.USER_TYPE_BANGZHU)) {
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

//                Toast.makeText(ActivityDetailActivity.this, "超赞！！", Toast.LENGTH_SHORT).show();
                zanType = SUPER_ZAN;
                superZan("superPraise");
                popupWindow.dismiss();
            }
        });

        tv_normal_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ActivityDetailActivity.this,"赞！！",Toast.LENGTH_SHORT).show();
                zanType = NORMAL_ZAN;
                superZan("praise");
                popupWindow.dismiss();
            }
        });

        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        tv_zhanglao_zan.measure(w, h);
        width = tv_zhanglao_zan.getMeasuredWidth();
    }

    private void showPopupWindow(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.showAsDropDown(view, -DensityUtil.dip2px(this, 81 - 50), 0);
//        popupWindow.showAsDropDown(view);
    }


    private void superZan(final String praiseMothod) {
        if(TextUtils.equals("superPraise",praiseMothod)){
            LifeCircleSuperZanBean request = new LifeCircleSuperZanBean();
            request.setActivityId(bean.getActivityId());
            request.setCommunityId( PreferencesUtil.getCommityId(getmContext()));
            request.setEmobIdTo(bean.getEmobGroupId());
            request.setEmobIdFrom(PreferencesUtil.getLoginInfo(getmContext()).getEmobId());
            request.setType(LifeCircleSuperZanBean.type_superzan_activity);
            NetBaseUtils.superZan(getmContext(), request, new NetBaseUtils.NetRespListener<CommonRespBean<String>>() {
                @Override
                public void successYes(CommonRespBean<String> commonRespBean, Response response) {
                    showToast("超赞成功!");
                    Intent intent = new Intent();
                    intent.putExtra("zanType", zanType);
                    setResult(2, intent);

                }

                @Override
                public void successNo(CommonRespBean<String> commonRespBean, Response response) {
                    showToast("点赞失败:"+commonRespBean.getMessage());
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    showToast("点赞失败");
                }
            });
        }else if(TextUtils.equals("praise",praiseMothod)){
            GeneralZanReqBean addtoblreqbean =new GeneralZanReqBean();
            addtoblreqbean.setActivityId(bean.getActivityId());
            addtoblreqbean.setEmobIdTo(bean.getEmobGroupId());
            addtoblreqbean.setEmobIdFrom(PreferencesUtil.getLoginInfo(getmContext()).getEmobId());
            addtoblreqbean.setType(GeneralZanReqBean.type_superzan_activity);
            NetBaseUtils.generalZan(getmContext(),addtoblreqbean,new NetBaseUtils.NetRespListener<CommonRespBean<String>>() {
                @Override
                public void successYes(CommonRespBean<String> commonRespBean, Response response) {
                    showToast("点赞成功!");
                    Intent intent = new Intent();
                    intent.putExtra("zanType", zanType);
                    setResult(2, intent);

                }
                @Override
                public void successNo(CommonRespBean<String> commonRespBean, Response response) {
                    showToast("点赞失败:"+commonRespBean.getMessage());
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    showToast("点赞失败");
                }
            });
        }
    }


    /**
     * Created by Administrator on 2015/3/13.
     */
    class PicAdapter2 extends BaseAdapter {
        private Context context;
        private List<ActivityBean.ActivityMate> photos;
        private AbsListView.LayoutParams params;

        public PicAdapter2(Context context, List<ActivityBean.ActivityMate> photos) {
            this.context = context;
            this.photos = photos;
            int width = DensityUtil.dip2px(context, 58f);
            params = new AbsListView.LayoutParams(width, width);
        }

        @Override
        public int getCount() {
            return photos.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.img_active, null);
                convertView.setLayoutParams(params);
//            ((ImageView)convertView).setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            ImageLoader.getInstance().displayImage(photos.get(position).getAvatar(), (ImageView) convertView, UserUtils.options);
            return convertView;
        }

        @Override
        public Object getItem(int position) {
            return photos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

    }


}
