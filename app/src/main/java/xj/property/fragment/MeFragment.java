package xj.property.fragment;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.activity.LifeCircle.MyLifeCircleActivity;
import xj.property.activity.LifeCircle.MyPraiseActivity;
import xj.property.activity.bangzhu.ActivityBangZhuElection;
import xj.property.activity.bangzhu.ActivityInviteNeighbors;
import xj.property.activity.move.MoveActivity;
import xj.property.activity.tags.ActivityMyTagsList;
import xj.property.activity.tags.ActivityMyTagsMgr;
import xj.property.activity.user.BangBiConsumeListActivity;
import xj.property.activity.user.FixUserSignatureActivity;
import xj.property.activity.user.LocationActivity;
import xj.property.activity.user.UserBonusActivity;
import xj.property.activity.user.UserSettingActivity;
import xj.property.beans.BangInternalNews;
import xj.property.beans.MyTagsChangeRespBean;
import xj.property.beans.MyTagsRespBean;
import xj.property.beans.RegisterNeighborNewest;
import xj.property.beans.UpDateApp;
import xj.property.beans.UserInfoDetailBean;
import xj.property.event.ExitEvent;
import xj.property.event.GeniusEvent;
import xj.property.event.MoveEvent;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseTagUtils;
import xj.property.netbase.NetBaseUtils;
import xj.property.netbase.RetrofitFactory;
import xj.property.netbasebean.BounsCoinInfoBean;
import xj.property.service.ImageUploadService;
import xj.property.ums.UpdateManager;
import xj.property.utils.BitmapHelper;
import xj.property.utils.ImageUploadUtils;
import xj.property.utils.ToastUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.AdminUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;
import xj.property.widget.FilterFlowLayout;
import xj.property.widget.LoadingDialog;
import xj.property.widget.MyPopWindow;


public class MeFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MeFragment";
    private static final int FLOWLAYOUT_MIN_LINE = 1;
    public static final int requestCode_to_Photo = 1;
    public static final int requestCode_to_album = 2;
    public static final int UPLOAD_COMPLETE = 750;
    public static final int REVITION_COMPLETE = 751;

    private final int requestLogin = 550;
    private int action = 1;
    private Uri photoUri;
    private final int CROP_AlBUM = 100;
    private String userType = "normal"; ///用户类型 //// 帮主/ ... // 默认normal , bangzhu, fubangzhu ,zhanglao,bangzhong ///普通人, 帮主
    private boolean isMore = false;//牛人更多记录

    private MyPopWindow popWindow;
    private UserInfoDetailBean bean;
    private Button btn_login_meFragment;//login button
    private TextView btn_desc_meFragment;
    private ImageView iv_versionupdate;
    private LinearLayout ll_set, ll_sianature, ll_bangbangquan, ll_mylifecircle, ll_versionupdate, ll_bangbangbi, ll_callback;
    private TextView tv_sianature, tv_bangbgangquan, tv_mylifecircle;
    private TextView tv_versionupdate, tv_bangbangbi, tv_newbangbi;
    private TextView tv_value_num;/// 人品值
    private TextView tv_rp_percent;/// 超过了多少百分比
    private ImageView iv_avatar;/// 用户头像
    private RelativeLayout frag_haveno_reg_rlay; /// 注册布局
    private LinearLayout frg_me_invite_neifhbor_lay; // 邀请邻居
    private LinearLayout frg_me_bangzhu_election_llay; /// 竞选帮主
    private TextView tv_newsbangzhuicon;/// 帮内新闻未读icon , 新模块 new字图标
    private TextView tv_newbangzhu_invite_icon; //// 邀请邻居新icon
    private TextView tv_invite_neighbors;/// 老王注册了 ...
    private ImageView frag_meinfo_grade_iv;// 用户类型 设置的帮主/ 副帮主的横条奖章..
    private LinearLayout tags_show_llay;// 标签内容
    private RelativeLayout user_info_rpz_rlay;// 人品值, 人品所占百分比
    private FilterFlowLayout tags_flay;// 标签布局
    private Button tags_manager_btn;// 管理标签
    private TextView tv_unservicetime;// 标签变更提醒
    private LinearLayout ll_genius;//牛人图标
    private TextView tv_genius_message;
    private ImageView iv_genius_more;
    private ImageView iv_genius_title;
    private LinearLayout ll_move_home;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
//        if(!EventBus.getDefault().isRegistered(this)){
        EventBus.getDefault().register(this);
//        }
        initView(view);
        initDate();
        initListenner();
        return view;
    }

    /**
     * init view
     *
     * @param view root view
     */
    private void initView(View view) {
        tv_unservicetime = (TextView) view.findViewById(R.id.tv_unservicetime);
        ll_set = (LinearLayout) view.findViewById(R.id.ll_set);
        ll_sianature = (LinearLayout) view.findViewById(R.id.ll_sianature);
        ll_bangbangquan = (LinearLayout) view.findViewById(R.id.ll_bangbangquan);
        frg_me_invite_neifhbor_lay = (LinearLayout) view.findViewById(R.id.frg_me_invite_neifhbor_lay);
        frg_me_bangzhu_election_llay = (LinearLayout) view.findViewById(R.id.frg_me_bangzhu_election_llay);
        ll_mylifecircle = (LinearLayout) view.findViewById(R.id.ll_mylifecircle);
        frag_haveno_reg_rlay = (RelativeLayout) view.findViewById(R.id.frag_haveno_reg_rlay);
        user_info_rpz_rlay = (RelativeLayout) view.findViewById(R.id.user_info_rpz_rlay);
        tv_newsbangzhuicon = (TextView) view.findViewById(R.id.tv_newsbangzhuicon);
        tv_newbangzhu_invite_icon = (TextView) view.findViewById(R.id.tv_newbangzhu_invite_icon);
        ll_genius = (LinearLayout) view.findViewById(R.id.ll_genius);//// 邀请邻居的new icon
        tv_genius_message = (TextView) view.findViewById(R.id.tv_genius_message);
        iv_genius_more = (ImageView) view.findViewById(R.id.iv_genius_more);
        iv_genius_title = (ImageView) view.findViewById(R.id.iv_genius_title);
        tags_show_llay = (LinearLayout) view.findViewById(R.id.tags_show_llay);
        tags_flay = (FilterFlowLayout) view.findViewById(R.id.tags_flay);
        tags_manager_btn = (Button) view.findViewById(R.id.tags_manager_btn);
        ll_versionupdate = (LinearLayout) view.findViewById(R.id.ll_versionupdate);
        ll_bangbangbi = (LinearLayout) view.findViewById(R.id.ll_bangbangbi);
        ll_callback = (LinearLayout) view.findViewById(R.id.ll_callback);
        tv_sianature = (TextView) view.findViewById(R.id.tv_sianature);
        tv_bangbgangquan = (TextView) view.findViewById(R.id.tv_bangbgangquan);
        tv_mylifecircle = (TextView) view.findViewById(R.id.tv_mylifecircle);
        tv_value_num = (TextView) view.findViewById(R.id.tv_value_num);
        tv_rp_percent = (TextView) view.findViewById(R.id.tv_rp_percent);
        tv_versionupdate = (TextView) view.findViewById(R.id.tv_versionupdate);
        tv_bangbangbi = (TextView) view.findViewById(R.id.tv_bangbangbi);
        tv_newbangbi = (TextView) view.findViewById(R.id.tv_newbangbi);
        tv_invite_neighbors = (TextView) view.findViewById(R.id.tv_invite_neighbors);
        iv_versionupdate = (ImageView) view.findViewById(R.id.iv_versionupdate);
        frag_meinfo_grade_iv = (ImageView) view.findViewById(R.id.frag_meinfo_grade_iv);
        btn_login_meFragment = (Button) view.findViewById(R.id.btn_login_meFragment);
        btn_desc_meFragment = (TextView) view.findViewById(R.id.btn_desc_meFragment); //// 用户昵称
        iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
        ll_move_home = (LinearLayout) view.findViewById(R.id.ll_move_home);
    }


    private void initDate() {
        //// 帮主的new icon
        if (PreferencesUtil.getBangzhuUnReadStatus(getActivity())) {
            tv_newsbangzhuicon.setVisibility(View.VISIBLE);
        } else {
            tv_newsbangzhuicon.setVisibility(View.GONE);
        }
        if (PreferencesUtil.getInvitedNeighborsUnReadStatus(getActivity())) {
            tv_newbangzhu_invite_icon.setVisibility(View.VISIBLE);
        } else {
            tv_newbangzhu_invite_icon.setVisibility(View.GONE);
        }
        boolean isLogin = PreferencesUtil.getLogin(getActivity());
        if (!isLogin) {
            btn_login_meFragment.setVisibility(View.VISIBLE);
        } else {
            btn_login_meFragment.setVisibility(View.GONE);
        }
    }

    //TODO ddd
    private void initListenner() {
        tags_manager_btn.setOnClickListener(this);
        iv_avatar.setOnClickListener(this);
        btn_login_meFragment.setOnClickListener(this);
        ll_callback.setOnClickListener(this);
        ll_genius.setOnClickListener(this);
        ll_set.setOnClickListener(this);
        ll_bangbangbi.setOnClickListener(this);
        ll_sianature.setOnClickListener(this);
        ll_bangbangquan.setOnClickListener(this);
        ll_mylifecircle.setOnClickListener(this);
        frg_me_invite_neifhbor_lay.setOnClickListener(this);
        frg_me_bangzhu_election_llay.setOnClickListener(this);
        user_info_rpz_rlay.setOnClickListener(this);
        ll_versionupdate.setOnClickListener(this);
        ll_move_home.setOnClickListener(this);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bean = PreferencesUtil.getLoginInfo(getActivity());
        updateUI(bean);
    }

    private void updateUI(final UserInfoDetailBean bean) {
        refreshUi(bean);
        //// 检查更新
        initUpdate();
    }

    private void refreshUi(final UserInfoDetailBean bean) {
        if (bean == null) {
            initUnRegUserInfo();
            tv_sianature.setText("");
            tv_bangbgangquan.setText("");
            tv_mylifecircle.setText("");
            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.default_avatar, iv_avatar);
            btn_login_meFragment.setVisibility(View.VISIBLE);
            btn_desc_meFragment.setVisibility(View.GONE);
        } else {
            /// 获取用户信息
            getUser(getActivity(), bean.getEmobId());
            /// 刷新帮帮币信息
            getBangBiInfo(getActivity(),bean.getEmobId());
            /// 获取帮内的消息调用..
            getBangZhuNewss();
            //// 刷新用户的标签信息
            getUserTagsInfo(bean.getEmobId());
            ///刷新用户最新变动的tag
            getUserTagsChangeInfo(bean.getEmobId());
            ////初始化用户信息
            initRegedUserInfo();
            tv_sianature.setText(bean.getSignature());
            tv_bangbgangquan.setText(Integer.parseInt(PreferencesUtil.getPrefBangbangquanCount(getActivity())) > 0 ? PreferencesUtil.getPrefBangbangquanCount(getActivity()) + "张" : "");
            tv_mylifecircle.setText(Integer.parseInt(PreferencesUtil.getPrefLifeCircleCount(getActivity())) > 0 ? PreferencesUtil.getPrefLifeCircleCount(getActivity()) + "条" : "");
            tv_bangbangbi.setText(PreferencesUtil.getPrefBangbangbiCount(getActivity()) + "个");

            if (PreferencesUtil.getLogin(getActivity())) {
                initBangzhuMedal();
            } else {
                /// 帮主横条奖章
                if (frag_meinfo_grade_iv != null) {
                    frag_meinfo_grade_iv.setVisibility(View.GONE);
                }
            }
            if (bean.getAvatar() != null && bean.getAvatar().length() != 0) {
                ImageLoader.getInstance().displayImage(bean.getAvatar(), iv_avatar, UserUtils.bangzhu_election_me_options);
            } else {
                ImageLoader.getInstance().displayImage("drawable://" + R.drawable.default_avatar, iv_avatar);
                btn_desc_meFragment.setVisibility(View.VISIBLE);
            }
            btn_desc_meFragment.setText(bean.getNickname());
            btn_login_meFragment.setVisibility(View.GONE);
            btn_desc_meFragment.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取小区帮帮币信息
     * 包含是否可用，兑换比率， 帮帮币个数
     *
     * @param activity
     * @param emobId
     */
    private void getBangBiInfo(final Activity activity, String emobId) {

        NetBaseUtils.extractBounsCoinInfo(activity,PreferencesUtil.getCommityId(activity),emobId,new NetBaseUtils.NetRespListener<CommonRespBean<BounsCoinInfoBean>>() {
            @Override
            public void successYes(CommonRespBean<BounsCoinInfoBean> commonRespBean, Response response) {
                BounsCoinInfoBean commonRespBeanData = commonRespBean.getData();

                PreferencesUtil.saveBangbangbiCount(activity,""+commonRespBeanData.getCount());
                if(commonRespBeanData.getEnable()==1){
                    PreferencesUtil.saveShowBonuscoin(activity,"yes");
                }else{
                    PreferencesUtil.saveShowBonuscoin(activity,"no");
                }
            }

            @Override
            public void successNo(CommonRespBean<BounsCoinInfoBean> commonRespBean, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    @Override
    public void onStop() {
//        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEventMainThread(ExitEvent event) {
        ll_genius.setVisibility(View.GONE);
        iv_genius_title.setVisibility(View.GONE);
    }

    public void onEventMainThread(MoveEvent event) {
        getActivity().finish();
        Intent intent = new Intent(getActivity(), LocationActivity.class);
        intent.putExtra("isMove", true);
        startActivity(intent);
    }

    public void onEventMainThread(GeniusEvent event) {
        getUser(getActivity(), bean.getEmobId());
    }

    /**
     * 初始化用户横条奖章图片
     */
    private void initBangzhuMedal() {

        if (frag_meinfo_grade_iv != null) {
//            normal , bangzhu, fubangzhu ,zhanglao,bangzhong
            userType = PreferencesUtil.getUserType(getActivity());
            if (TextUtils.equals(userType, "zhanglao")) {
                frag_meinfo_grade_iv.setImageDrawable(getResources().getDrawable(R.drawable.me_zhanglao_icon));
                frag_meinfo_grade_iv.setVisibility(View.VISIBLE);
            } else if (TextUtils.equals(userType, "bangzhu")) {
                frag_meinfo_grade_iv.setImageDrawable(getResources().getDrawable(R.drawable.me_bangzhu_icon));
                frag_meinfo_grade_iv.setVisibility(View.VISIBLE);
            } else if (TextUtils.equals(userType, "fubangzhu")) {
                frag_meinfo_grade_iv.setImageDrawable(getResources().getDrawable(R.drawable.me_fubangzhu_icon));
                frag_meinfo_grade_iv.setVisibility(View.VISIBLE);
            } else {
                frag_meinfo_grade_iv.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 初始化注册后用户信息界面
     */
    private void initRegedUserInfo() {
        if (frag_haveno_reg_rlay != null) {
            frag_haveno_reg_rlay.setVisibility(View.GONE);
        }
        /// 标签不可见
        if (tags_show_llay != null) {
            tags_show_llay.setVisibility(View.VISIBLE);
        }
        //// 人品所占百分比
        if (user_info_rpz_rlay != null) {
            user_info_rpz_rlay.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化没有注册时用户信息界面
     */
    private void initUnRegUserInfo() {
        /// 标签不可见
        if (tags_show_llay != null) {
            tags_show_llay.setVisibility(View.GONE);
        }
        //// 人品所占百分比
        if (user_info_rpz_rlay != null) {
            user_info_rpz_rlay.setVisibility(View.GONE);
        }
        if (frag_haveno_reg_rlay != null) {
            frag_haveno_reg_rlay.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tags_manager_btn:
                startActivity(new Intent(getActivity(), ActivityMyTagsMgr.class));
                break;
            case R.id.iv_avatar:
                if (bean == null || !PreferencesUtil.getLogin(getActivity())) {
                    Intent intent = new Intent(getActivity(), RegisterLoginActivity.class);
                    startActivity(intent);
                } else {
                    addpic();
                    popWindow.showAtLocation(getView(), Gravity.BOTTOM
                            | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
                break;
            case R.id.btn_login_meFragment:
                startActivityForResult(new Intent(getActivity(), RegisterLoginActivity.class), requestLogin);
                break;
            case R.id.ll_callback:
                if (PreferencesUtil.getLogin(getActivity())) {
                    ///// 小区客服
                    AdminUtils.askCallback(getActivity(), Config.SERVANT_TYPE_BANGBANG);
                } else {
                    Intent intent = new Intent(getActivity(), RegisterLoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_genius:
                if (!isMore) {
                    if (tv_genius_message.getLayout().getLineCount() > 2) {
                        tv_genius_message.setLines(tv_genius_message.getLayout().getLineCount());
                        iv_genius_more.setBackgroundDrawable(getResources().getDrawable(R.drawable.tags_me_less));
                        isMore = true;
                    }
                } else {
                    iv_genius_more.setBackgroundDrawable(getResources().getDrawable(R.drawable.tags_me_more));
                    tv_genius_message.setLines(2);
                    isMore = false;
                }
                break;
            case R.id.ll_set:
                if (bean != null) {
                    Intent intentset = new Intent(getActivity(), UserSettingActivity.class);
                    startActivity(intentset);
                } else {
                    Intent intent = new Intent(getActivity(), RegisterLoginActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.ll_bangbangbi:
                if (bean != null) {
                    Intent intentbi = new Intent(getActivity(), BangBiConsumeListActivity.class);
                    startActivity(intentbi);
                    tv_newbangbi.setVisibility(View.INVISIBLE);
                    PreferencesUtil.saveNewBangBangBi(getActivity(), false);
                } else {
                    Intent intent1 = new Intent(getActivity(), RegisterLoginActivity.class);
                    startActivity(intent1);
                }

                break;
            case R.id.ll_sianature:
                if (bean != null) {
                    Intent intentsina = new Intent(getActivity(), FixUserSignatureActivity.class);
                    startActivity(intentsina);

                } else {
                    Intent intent2 = new Intent(getActivity(), RegisterLoginActivity.class);
                    startActivity(intent2);
                }
                break;
            case R.id.ll_bangbangquan:
                if (bean != null) {
                    Intent intentjuan = new Intent(getActivity(), UserBonusActivity.class);
                    startActivity(intentjuan);
                } else {
                    Intent intent3 = new Intent(getActivity(), RegisterLoginActivity.class);
                    startActivity(intent3);
                }
                break;
            case R.id.ll_mylifecircle:
                if (bean != null) {
                    Intent intentCircle = new Intent(getActivity(), MyLifeCircleActivity.class);
                    intentCircle.putExtra("emobid", bean.getEmobId());
                    startActivity(intentCircle);
                } else {
                    Intent intent4 = new Intent(getActivity(), RegisterLoginActivity.class);
                    startActivity(intent4);
                }
                break;
            case R.id.frg_me_invite_neifhbor_lay:
                if (bean != null) {
                    if (tv_newbangzhu_invite_icon != null) {
                        tv_newbangzhu_invite_icon.setVisibility(View.GONE);
                    }
                    /// 设置状态已读
                    PreferencesUtil.setInvitedNeighborsUnReadStatus(getActivity(), false);
                    /// 跳转至邀请邻居页面.
                    startActivity(new Intent(getActivity(), ActivityInviteNeighbors.class));
                } else {
                    Intent intent5 = new Intent(getActivity(), RegisterLoginActivity.class);
                    startActivity(intent5);
                }
                break;
            case R.id.frg_me_bangzhu_election_llay:
                if (bean != null) {
                    /// 帮主竞选...
                    if (tv_newsbangzhuicon != null) {
                        tv_newsbangzhuicon.setVisibility(View.INVISIBLE);
                    }
                    /// 保存消息已读状态.
                    PreferencesUtil.setCurrentBangzhuNewsReadStatus(getActivity(), true);
                    /// 保存帮主未读状态
                    PreferencesUtil.setBangzhuUnReadStatus(getActivity(), false);
                    startActivity(new Intent(getActivity(), ActivityBangZhuElection.class));
                } else {
                    Intent intent6 = new Intent(getActivity(), RegisterLoginActivity.class);
                    startActivity(intent6);
                }
                break;
            case R.id.user_info_rpz_rlay:
                Intent intent7 = new Intent(getActivity(), MyPraiseActivity.class);
                intent7.putExtra("emobid", bean.getEmobId());
                startActivity(intent7);
                break;
            case R.id.ll_versionupdate:
                UpdateManager.showNoticeDialog(getActivity());
                break;
            case R.id.ll_move_home:
//                if (bean != null) {
                startActivity(new Intent(getActivity(), MoveActivity.class));
//                } else {
//                    Intent intent8 = new Intent(getActivity(), RegisterLoginActivity.class);
//                    startActivity(intent8);
//                }
                break;
        }
    }

    public void initUpdate() {
        String emobId = "";
        int communityId;
        if (PreferencesUtil.getLogin(getActivity())) {
            emobId = PreferencesUtil.getLoginInfo(getActivity()).getEmobId();
            communityId = PreferencesUtil.getLoginInfo(getActivity()).getCommunityId();
        } else {
            if (PreferencesUtil.getTouristLogin(getActivity())) {
                emobId = PreferencesUtil.getTourist(getActivity());
            } else {
                emobId = PreferencesUtil.getlogoutEmobId(getActivity());
            }
            communityId = PreferencesUtil.getCommityId(getActivity());
        }
        NetBaseUtils.getAppUpdateInfoV3(getActivity(),communityId, emobId, new NetBaseUtils.NetRespListener<CommonRespBean<UpDateApp.Info>>() {
            @Override
            public void successYes(CommonRespBean<UpDateApp.Info> commonRespBean, Response response) {
                PreferencesUtil.saveCheckTime(getActivity(), System.currentTimeMillis());
                String version = "";
                try {
                    version = UserUtils.getVersion(getActivity());
                } catch (Exception e) {
                }
                if (equalsVersion(commonRespBean.getData().getVersion(), version)) {
                    iv_versionupdate.setVisibility(View.VISIBLE);
                    tv_versionupdate.setText("有新版本可用");
                    ll_versionupdate.setClickable(true);
                } else {
                    tv_versionupdate.setText("已是最新版本");
                    ll_versionupdate.setClickable(false);
                }
            }

            @Override
            public void successNo(CommonRespBean<UpDateApp.Info> commonRespBean, Response response) {
                PreferencesUtil.saveCheckTime(getActivity(), new Date().getTime());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("onion", "失败" + error.toString());
            }
        });

    }

    private boolean equalsVersion(String serviceVersion, String localVersion) {
        boolean b = false;
        int serviceint, localint;
        String[] serviceVersions = serviceVersion.split("\\.");
        String[] loaclVersions = localVersion.split("\\.");
        for (int i = 0; i < serviceVersions.length; i++) {
            serviceint = Integer.parseInt(serviceVersions[i]);
            localint = Integer.parseInt(loaclVersions[i]);
            if (serviceint > localint) {
                b = true;
                break;
            } else if (serviceint < localint) {
                b = false;
                break;
            }
        }
        return b;
    }

    /**
     * 获取UserMessageBean部分
     */
    interface UserMessageService {

        /**
         * 获取小区帮主产生变化，或者，我的帮内头衔变化时显示红点
         *
         * @param cb
         */
//        @GET("/api/v1/communities/{communityId}/bangzhu/tip")
//        void getBangZhuNews(@Path("communityId") int communityId, @QueryMap Map<String, String> map, Callback<BangInternalNews> cb);
//        @GET("/api/v1/communities/{communityId}/bangzhu/tip")
//        /api/v3/elections/bangzhu/tips?communityId={小区ID}&emobId={用户环信ID}&time={上次获取到的变化时间}

        @GET("/api/v3/elections/bangzhu/tips")
        void getBangZhuNews(@QueryMap Map<String, String> map, Callback<CommonRespBean<String>> cb);
    }


    /**
     * 获取小区帮主产生变化，或者，我的帮内头衔变化时显示红点
     * <p/>
     * 需求:
     * 1. 点击帮主进入后红点消失
     * 2. 进入我的界面后如果有消息出现红点.
     * 3. frag tab 出现红色角标.
     */
    private void getBangZhuNewss() {
        String timeStamp = PreferencesUtil.getCurrentBangZhuNewsTimeStamp(getActivity());
        HashMap<String, String> option = new HashMap<String, String>();
        option.put("emobId", bean == null ? "null" : bean.getEmobId());
        option.put("time", timeStamp);
        option.put("communityId",""+ PreferencesUtil.getCommityId(getActivity()));

        UserMessageService service = RetrofitFactory.getInstance().create(getActivity(),option,UserMessageService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, Response response) {
                if (bean != null) {
                    if (TextUtils.equals(bean.getStatus(), "yes")) {

                        if (TextUtils.isEmpty(bean.getData())) {
                            Log.e(TAG, "bean.getinfo--->>>" + bean.getData() + " bean.getMessage--->>>" + bean.getMessage());
                            return;
                        }
                        /// 在更新之前的时间戳
                        String currentTimeStamp = PreferencesUtil.getCurrentBangZhuNewsTimeStamp(getActivity());

                        if (TextUtils.equals("-1", currentTimeStamp)) {

                            /// news
                            if (tv_newsbangzhuicon != null) {
                                tv_newsbangzhuicon.setVisibility(View.VISIBLE);
                            }
                            /// 设置当前访问返回时间戳.
                            PreferencesUtil.setCurrentBangZhuNewsTimeStamp(getActivity(), bean.getData());
                            PreferencesUtil.setCurrentBangzhuNewsReadStatus(getActivity(), false);

                            /// 相同消息时间戳,多次加载,
                        } else if (TextUtils.equals(currentTimeStamp, bean.getData())) {

                            boolean isreadflag = PreferencesUtil.getCurrentBangzhuNewsReadStatus(getActivity(), currentTimeStamp);

                            if (isreadflag) {
                                /// 如果点击过则不显示
                                /// 帮内新闻未读icon , 新模块 new字图标

                                if (tv_newsbangzhuicon != null) {
                                    tv_newsbangzhuicon.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                /// 显示星
                                if (tv_newsbangzhuicon != null) {
                                    tv_newsbangzhuicon.setVisibility(View.INVISIBLE);
                                }

                            }
                            //// 如果不相同,则存储
                        } else {
                            PreferencesUtil.removeCurrentBangZhuNewsTimeStamp(getActivity(), currentTimeStamp);
                            /// 设置当前访问返回时间戳.
                            PreferencesUtil.setCurrentBangZhuNewsTimeStamp(getActivity(), bean.getData());
                            PreferencesUtil.setCurrentBangzhuNewsReadStatus(getActivity(), false);
                            /// 显示new
                            if (tv_newsbangzhuicon != null) {
                                tv_newsbangzhuicon.setVisibility(View.VISIBLE);
                            }
                        }

                    } else {
                        /// no 则表示已读该消息
                        if (tv_newsbangzhuicon != null) {
                            tv_newsbangzhuicon.setVisibility(View.INVISIBLE);
                        }
                        PreferencesUtil.setCurrentBangzhuNewsReadStatus(getActivity(), true);
                    }
                }
                //// 帮主的new icon
                if (PreferencesUtil.getBangzhuUnReadStatus(getActivity())) {
                    tv_newsbangzhuicon.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        };

//        communityId={小区ID}&emobId={用户环信ID}&time={上次获取到的变化时间}
        service.getBangZhuNews( option, callback);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (PreferencesUtil.getLogin(getActivity())) {
            bean = PreferencesUtil.getLoginInfo(getActivity());
        } else {
            bean = null;
        }
        updateUI(bean);
    }

    protected void addpic() {
        popWindow = new MyPopWindow(getActivity(), R.layout.paizhao_pop,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            // 拍照
                            case R.id.pop_xi:
                                popWindow.dismiss();
                                takePhoto();
                                break;
                            // 从相册中选
                            case R.id.pop_mu:

                                popWindow.dismiss();

                                Intent intents = new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intents.setType("image/*");
                                action = requestCode_to_album;
                                startActivityForResult(intents, requestCode_to_album);

                                break;
                            // 取消
                            case R.id.pop_no:
                                popWindow.dismiss();
                                break;
                        }
                    }
                });
    }

    Uri uri = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) return;
        if (requestCode == requestCode_to_album) {
            if (data != null) {
                uri = data.getData();
                beginCrop2(uri);

            }
        }

        if (requestCode == requestCode_to_Photo) {
            if (data != null && data.getData() != null) {
                uri = data.getData();
            }
            // 一些机型无法从getData中获取uri，则需手动指定拍照后存储照片的Uri
            if (uri == null) {
                if (photoUri != null) {
                    uri = photoUri;
                }
            }
            beginCrop(photoUri);
        }

        if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }

        if (requestCode == CROP_AlBUM) {
            handleCrop2(resultCode, data);
        }


    }

    private void beginCrop(Uri source) {

        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));

        Crop.of(source, destination).asSquare().start(getActivity(), this, Crop.REQUEST_CROP);

    }

    private void beginCrop2(Uri source) {

        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));

        Crop.of(source, destination).asSquare().start(getActivity(), this, CROP_AlBUM);

    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
            Log.i("debbug getOutput  ", "" + Crop.getOutput(result));

            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Crop.getOutput(result));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoName != null) {
                bmp = BitmapHelper.rotateBitmapByDegree(bmp, BitmapHelper.getBitmapDegree(photoName));
            }
//            Log.i("debbug",""+getBitmapSize(bmp));

            iv_avatar.setImageBitmap(bmp);

            final Bitmap finalBmp = bmp;

            if (TextUtils.isEmpty(photoName)) photoName = getFileName();

            new Thread(new Runnable() {
                public void run() {

                    try {

                        BitmapHelper.saveMyBitmap(photoName, finalBmp);
                        handler.sendEmptyMessage(REVITION_COMPLETE);

                    } catch (IOException e) {

                        e.printStackTrace();

                        savePhotoError.sendEmptyMessage(1);
                    }
                }
            }).start();
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getActivity(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 处理pickfrom album 裁剪结果
     *
     * @param resultCode
     * @param result
     */
    private void handleCrop2(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {

            Log.i("debbug getOutput album  ", "" + Crop.getOutput(result));

            uri = Crop.getOutput(result);

            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoName != null) {
                bmp = BitmapHelper.rotateBitmapByDegree(bmp, BitmapHelper.getBitmapDegree(photoName));
            }

            final Bitmap finalBmp = bmp;

            handler.post(new Runnable() {
                @Override
                public void run() {
                    iv_avatar.setImageBitmap(finalBmp);
                }
            });

            if (TextUtils.isEmpty(photoName)) photoName = getFileName();

            new Thread(new Runnable() {
                public void run() {

                    try {
//                        FileUtils.saveBitmapForTakePhoto(finalBmp, photoName);
                        BitmapHelper.saveMyBitmap(photoName, finalBmp);

                        handler.sendEmptyMessage(REVITION_COMPLETE);

                    } catch (IOException e) {

                        e.printStackTrace();
                        savePhotoError.sendEmptyMessage(1);
                    }
                }
            }).start();

//            resultView.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getActivity(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    Handler savePhotoError = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(getActivity(), "保存图片出现异常，请重试！", Toast.LENGTH_SHORT).show();
            photoName = null;
        }
    };

    private String photoName;

    ////拍照
    private void takePhoto() {

        //先验证手机是否有sdcard
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {

            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            openCameraIntent.putExtra("return-data", true);

            photoName = getFileName();

            ContentValues values = new ContentValues();

            values.put(MediaStore.Images.Media.TITLE, photoName);

            photoUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);


            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);


            action = requestCode_to_Photo;

            startActivityForResult(openCameraIntent, requestCode_to_Photo);
        }
    }

    private String getFileName() {
        String fileName = String.valueOf(System.currentTimeMillis());
        String path;
        File sdcardPath = Environment.getExternalStorageDirectory();
        path = sdcardPath.getAbsolutePath() + File.separator + Config.BASE_GROUP_CACHE + File.separator + "image";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path + File.separator + fileName + ".jpg";
    }

    /**
     * 上传用户头像 图片信息 更新本地数据
     *
     */
    private void uploadHeadIcon() {
        NetBaseUtils.extractNewToken(getActivity(),new NetBaseUtils.NetRespListener<CommonRespBean<String>>() {
            @Override
            public void successYes(CommonRespBean<String> commonRespBean, Response response) {
                String token = commonRespBean.getData();
                String key = ImageUploadUtils.generateResKey();
                final String avatar = Config.QINIU_BASE_URL + key;
                Log.d("doregistProcess ", " token " + token + " key " + key + " avatar " + avatar);
                /// 不关心结果的异步Service 上传
                Intent intent = new Intent(getActivity(), ImageUploadService.class);
                intent.putExtra("path", photoName);
                intent.putExtra("reskey", key);
                intent.putExtra("token", token);
                getActivity().startService(intent);

                mLdDialog.dismiss();
                bean.setAvatar(avatar);
                Toast.makeText(getActivity(), "修改头像成功", Toast.LENGTH_SHORT).show();
                PreferencesUtil.saveLogin(getActivity(), bean);/// 更新本地用户信息
                photoName = "";

//                ImageLoader.getInstance().displayImage("file://" + photoName, iv_avatar);
//                UploadManager um = new UploadManager();
//                um.put(photoName, key, token, new UpCompletionHandler() {
//                    @Override
//                    public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
//                        mLdDialog.dismiss();
//                        bean.setAvatar(avatar);
//                        Toast.makeText(getActivity(), "修改头像成功", Toast.LENGTH_SHORT).show();
//                        PreferencesUtil.saveLogin(getActivity(), bean);/// 更新本地用户信息
//                        photoName = "";
//                    }
//                }, null);
            }

            @Override
            public void successNo(CommonRespBean<String> commonRespBean, Response response) {
                ToastUtils.showToast(getActivity(), "请求失败：" + commonRespBean.getMessage());

            }

            @Override
            public void failure(RetrofitError error) {
                ToastUtils.showNetErrorToast(getActivity());
            }
        });

    }

    LoadingDialog mLdDialog;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case REVITION_COMPLETE:

                    mLdDialog = new LoadingDialog(getActivity());
                    mLdDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        public boolean onKey(DialogInterface dialog, int keyCode,
                                             KeyEvent event) {
                            dialog.cancel();
                            return false;
                        }
                    });
                    mLdDialog.show();
                    uploadHeadIcon();
            }

        }
    };

    /**
     * 判断用户是否是牛人
     * @param identity
     * @param intro
     */
    private void checkGenius(String identity, String intro) {
        if ("famous".equals(identity)) {
            ll_genius.setVisibility(View.VISIBLE);
            tv_genius_message.setText(intro);
            iv_genius_title.setVisibility(View.VISIBLE);
        } else {
            ll_genius.setVisibility(View.GONE);
            iv_genius_title.setVisibility(View.GONE);
        }
    }

    public void getUser(final Context context, String emobid) {
        NetBaseUtils.extractUserInfo(context,PreferencesUtil.getCommityId(getActivity()), emobid, new NetBaseUtils.NetRespListener<CommonRespBean<UserInfoDetailBean>>() {
            @Override
            public void successYes(CommonRespBean<UserInfoDetailBean> commonRespBean, Response response) {

                UserInfoDetailBean data = commonRespBean.getData();
//                    PreferencesUtil.saveRp(context,bean.getInfo().getCharacterValues()+"",(int)bean.getInfo().getCharacterPercent()+"",bean.getInfo().getLifeCircleSum()+"");
                tv_mylifecircle.setText(data.getLifeCircleSum() > 0 ? data.getLifeCircleSum() + "条" : "");
                tv_value_num.setText("" + data.getCharacterValues());
                tv_bangbangbi.setText(data.getBonuscoinCount() > 0 ? data.getBonuscoinCount() + "个" : "0个");
                tv_rp_percent.setText("打败了" + StrUtils.getPrecent(data.getCharacterPercent()) + "%的" + data.getCommunityName() + "小区居民！");

                if (data.getBonuscoinCount() > 0) {
                    ll_bangbangbi.setVisibility(View.VISIBLE);
                    if (PreferencesUtil.getNewBangBangBi(getActivity())) {
                        tv_newbangbi.setVisibility(View.VISIBLE);
                    } else {
                        tv_newbangbi.setVisibility(View.INVISIBLE);
                    }
                } else {
                    ll_bangbangbi.setVisibility(View.GONE);
                    PreferencesUtil.saveNewBangBangBi(getActivity(), false);
                }

                if (data.getBonuscoinEnable() == 0) {
                    ll_bangbangbi.setVisibility(View.GONE);
                    PreferencesUtil.saveNewBangBangBi(getActivity(), false);
                }

                if (data.getIdentity() != null) {
                    checkGenius(data.getIdentity(), data.getFamousIntroduce());
                }

                PreferencesUtil.saveRPValue(getActivity(), "" + data.getCharacterValues());
                /// 更新小区信息
                PreferencesUtil.saveCommity(getActivity(), data.getCommunityId(), data.getCommunityName());
                /// 帮帮币总数
                PreferencesUtil.saveBangbangbiCount(getActivity(), data.getBonuscoinCount() + "");

                PreferencesUtil.saveExchange(getActivity(), data + "");

//                PreferencesUtil.saveShowBonuscoin(getActivity(), commonRespBean.getData().getShowBonuscoin());
                //// 设置用户类型, 帮主副帮主///

                PreferencesUtil.saveUserType(getActivity(), data.getGrade());

                //// 存储当前用户是否是水军
                PreferencesUtil.setUserInfoTest(getActivity(), data.getTest());

                if (TextUtils.isEmpty(data.getGrade())) {
                    userType = PreferencesUtil.getUserType(getActivity());
                } else {
                    userType = data.getGrade();
//                        userType = "normal";
                    PreferencesUtil.saveUserType(getActivity(), userType);
                }
                //// 刷新帮主奖章信息
                initBangzhuMedal();
            }

            @Override
            public void successNo(CommonRespBean<UserInfoDetailBean> commonRespBean, Response response) {

                ToastUtils.showToast(getActivity(), "请求失败:" + commonRespBean.getMessage());

            }

            @Override
            public void failure(RetrofitError error) {
                ToastUtils.showNetErrorToast(getActivity());
            }
        });
    }


    interface getUserTags {

//        @GET("/api/v1/communities/{communityId}/labels/user/{emobId}")
//        void getUserTags(@Path("communityId") int communityId, @Path("emobId") String emobid, Callback<MyTagsRespBean> cb);

//        @GET("/api/v1/communities/{communityId}/labels/latest")
//        void getUserTagsChangeInfo(@Path("communityId") int communityId, @QueryMap Map<String, String> map, Callback<MyTagsChangeRespBean> cb);

        @GET("/api/v3/labels/last")
        void getUserTagsChangeInfoV3(@QueryMap Map<String, String> map, Callback<CommonRespBean<MyTagsChangeRespBean.InfoEntity>> cb);

    }

    public void showMspNotice(CommonRespBean<MyTagsChangeRespBean.InfoEntity> msg, final String emobid) {

        if (tv_unservicetime != null) {
            final MyTagsChangeRespBean.InfoEntity info = msg.getData();

            String notice = null;
            if (info != null) {
                notice = info.getNickname() + "给您添加了新的标签," + info.getLabelContent() + ", 点击查看详情";
            } else {
                return;
            }
            tv_unservicetime.setText(notice);
            tv_unservicetime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getActivity(), ActivityMyTagsList.class);
                    intent.putExtra("tagContent", info.getLabelContent());
                    intent.putExtra("tagEmobId", emobid);
                    startActivity(intent);
                }
            });
            tv_unservicetime.setVisibility(View.VISIBLE);
        }

        CountDownTimer countDownTimer = new CountDownTimer(2 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {

                if (tv_unservicetime != null) {
                    tv_unservicetime.setVisibility(View.GONE);
                }
            }
        };
        countDownTimer.start();
    }

    public void getUserTagsChangeInfo(final String emobid) {
        HashMap<String, String> query = new HashMap<>();
        query.put("emobIdTo", emobid+"");
        query.put("time", "" + PreferencesUtil.getLastReqTagsChangeTime(getActivity()));

        getUserTags service = RetrofitFactory.getInstance().create(getActivity(),query,getUserTags.class);
        Callback<CommonRespBean<MyTagsChangeRespBean.InfoEntity>> callback = new Callback<CommonRespBean<MyTagsChangeRespBean.InfoEntity>>() {
            @Override
            public void success(CommonRespBean<MyTagsChangeRespBean.InfoEntity> bean, Response response) {

                if ("yes".equals(bean.getStatus())) {
                    /// 显示黑条,告诉用户标签改变
                    showMspNotice(bean, emobid);
                    /// 显示
                    PreferencesUtil.saveIsUnReadTagsChange(getActivity(), true);
                } else {
                    Log.d("getUserTagsChangeInfo  ", "getUserTagsChangeInfo is no  ");
                    /// 不用显示
                    PreferencesUtil.saveIsUnReadTagsChange(getActivity(), false);
                }

                int time = (int) (new Date().getTime() / 1000);
                PreferencesUtil.setLastReqTagsChangeTime(getActivity(), time);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Log.e("getUserTagsChangeInfo  ", "getUserTagsChangeInfo is failure  ");

                int time = (int) (new Date().getTime() / 1000);
                PreferencesUtil.setLastReqTagsChangeTime(getActivity(), time);
            }
        };
        service.getUserTagsChangeInfoV3( query, callback);
    }

    public void getUserTagsInfo(final String emobid) {
        NetBaseTagUtils.getUserTagsInfo(getActivity(),emobid, new NetBaseTagUtils.NetRespListener<CommonRespBean<List<MyTagsRespBean.InfoEntity>>>() {
            @Override
            public void success(CommonRespBean<List<MyTagsRespBean.InfoEntity>> bean, Response response) {
                if (TextUtils.equals("yes", bean.getStatus())) {
                    List<MyTagsRespBean.InfoEntity> info = bean.getData();
                    initMyTags(getActivity(), info, emobid);
                }
            }
            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void initMyTags(final Context context, final List<MyTagsRespBean.InfoEntity> info, final String emobid) {

        if (info == null || info.isEmpty()) {
            Log.d("initMyTags  ", "initMyTags is null 该用户无标签 ");
            if (tags_flay != null) {
                tags_flay.removeAllViews();
            }
            return;
        }
        if (tags_flay != null) {
            tags_flay.removeAllViews();
            tags_flay.setMaxLines(FLOWLAYOUT_MIN_LINE);
            LayoutInflater inflater = LayoutInflater.from(context);
            for (final MyTagsRespBean.InfoEntity bean : info) {
                View common_tags_item = inflater.inflate(R.layout.common_tags_item, null);
                TextView common_tags_name_tv = (TextView) common_tags_item.findViewById(R.id.common_tags_name_tv);
                TextView common_tags_nums_tv = (TextView) common_tags_item.findViewById(R.id.common_tags_nums_tv);
                common_tags_name_tv.setText(bean.getLabelContent());
                common_tags_nums_tv.setText(bean.getCount());
                common_tags_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ActivityMyTagsList.class);
                        intent.putExtra("tagContent", bean.getLabelContent());
                        intent.putExtra("tagEmobId", emobid);
                        startActivity(intent);
                    }
                });
                tags_flay.addView(common_tags_item);
            }
        } else {
            Log.d("tags_flay  ", "tags_flay is null  ");
        }
    }

}
