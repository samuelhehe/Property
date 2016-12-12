package xj.property.activity.invite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.Constant;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.bangzhu.ActivityInviteNumList;
import xj.property.beans.InvatePhoneResult;
import xj.property.beans.InvitatePhone;
import xj.property.beans.InviteNeighborBean;
import xj.property.beans.NeedInviteNums;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseUtils;
import xj.property.netbase.RetrofitFactory;
import xj.property.netbasebean.SendSMSReqBean;
import xj.property.service.BackGroundService;
import xj.property.utils.CommonUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

/**
 * 首页邀请邻居
 */
public class ActivityInviteNeighborsHome extends HXBaseActivity {


    private final String TAG = "ActivityInviteNeighborsHome";
    ///该页面的头部方条消息显示
    private TextView invite_neighbors_msg_tv;
    /// 推荐到微信
    private Button invite_by_weixin_btn;

    /// 推荐到QQ
    private Button invite_by_qq_btn;

    /// 推荐到的手机号
    private EditText invite_by_phone_num_et;

    /// 跳转到通讯录页面
    private Button invite_by_phonelist_btn;

    /// 确认邀请
    private Button confirm_invite_btn;

    ///已成功邀请了X位邻居
//    private TextView has_invited_success_tv;

    /// 已邀请过人员列表
//    private ListView has_invited_list_lv;


    /// 初始化友盟社会化分享组件.
    private final UMSocialService mController = UMServiceFactory.getUMSocialService(Constant.DESCRIPTOR);

    private Context mContext;

    private UserInfoDetailBean bean;

//    private LinearLayout footerView;

//    private ImageView footerimage;


//    private MyAdapter adapter = new MyAdapter();


//    private int pageNum = 1;
//    private int lastItem;
//    private int count;
//    private int pageCount = 1;
//    private String pageSize = "10";

//    private List<InviteNeighborBean.InfoEntity.NeighborsEntity.PageDataEntity> pageData = new ArrayList<InviteNeighborBean.InfoEntity.NeighborsEntity.PageDataEntity>();

    private String inviteType = "home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_neighbors_home);
        this.mContext = this;
        bean = PreferencesUtil.getLoginInfo(this);
        if (bean != null) {
            inviteType = getIntent().getStringExtra("inviteType");
            initView();
            initData();
        } else {
            showToast("初始化数据有误");
            finish();
        }
    }

    private void initData() {
        /// 配置分享组建
        configPlatforms();

        /// 设置分享内容
        setShareContent();

        /// 获取最新邀请注册成功的邻居
//        getRegisterNeighborsNewest();

        /// 获取已经邀请的邻居列表以及最新邀请成功的消息
//        getInvitedNeighborList();

        /// 获取已经邀请的邻居人数
//        getNeedInviteNums();
        getUpdatePhone();
    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        this.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.tv_title)).setText("邀请邻居");

    }


    private void initView() {
        initTitle();

        invite_neighbors_msg_tv = (TextView) this.findViewById(R.id.invite_neighbors_msg_tv);
//        has_invited_success_tv = (TextView) this.findViewById(R.id.has_invited_success_tv);

        invite_by_weixin_btn = (Button) this.findViewById(R.id.invite_by_weixin_btn);
        invite_by_weixin_btn.setOnClickListener(this);

        invite_by_qq_btn = (Button) this.findViewById(R.id.invite_by_qq_btn);
        invite_by_qq_btn.setOnClickListener(this);

        invite_by_phone_num_et = (EditText) this.findViewById(R.id.invite_by_phone_num_et);

        invite_by_phonelist_btn = (Button) this.findViewById(R.id.invite_by_phonelist_btn);
        invite_by_phonelist_btn.setOnClickListener(this);

        confirm_invite_btn = (Button) this.findViewById(R.id.confirm_invite_btn);
        confirm_invite_btn.setOnClickListener(this);

//        has_invited_list_lv = (ListView) this.findViewById(R.id.has_invited_list_lv);


//        footerView = (LinearLayout) View.inflate(this, R.layout.item_grid_footer, null);
//        footerimage = (ImageView) footerView.findViewById(R.id.footview);
//        footerView.findViewById(R.id.tv_temp).setVisibility(View.INVISIBLE);
//        BaseUtils.setLoadingImageAnimation(footerimage);
//        has_invited_list_lv.addFooterView(footerView);
//        has_invited_list_lv.setAdapter(adapter);

//        has_invited_list_lv.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                Log.i(TAG, "scrollState="+scrollState+"  lastItem="+lastItem+"  count="+count);
//                Log.i(TAG, "scrollState="+scrollState+"  pageNum="+pageNum+"  pageCount="+pageCount);
        //下拉到空闲是，且最后一个item的数等于数据的总数时，进行更新
//                if (lastItem == count && scrollState == this.SCROLL_STATE_IDLE) {
//
//                }
//
//                switch (scrollState) {
//                    // 当不滚动时
//                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
//                        // 判断滚动到底部
//                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
//                            if (pageNum > pageCount) {
//                                Log.i("debugg", "拉到最底部");
//
//                                has_invited_list_lv.removeFooterView(footerView);
//                                footerimage.clearAnimation();
//
//                                showToast("没有更多数据");
//                            } else {
//                                getInvitedNeighborList();
//                            }
//                        }
//                        break;
//                }
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                lastItem = firstVisibleItem + visibleItemCount - 2;
//            }
//        });
//        has_invited_list_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                startActivity(new Intent(ActivityWelfareBuyedMoreUsers.this, UserGroupInfoActivity.class).putExtra(Config.INTENT_PARMAS2, pageData.get(position).getEmobId()));
//            }
//        });


    }


    interface InvitateByPhoneNumService {
        /**
         * 通过手机号进行邀请
         *
         * @param signature 签名信息
         * @param acr  手机号封装体
         * @param emobId
         * @param cb 访问结果回调
         */
//        @POST("/api/v1/users/{emobId}/neighbors/invite")
//        void inviateByPhoneNum(@Header("signature") String signature, @Body InvitatePhone acr, @Path("emobId") String emobId, Callback<InvatePhoneResult> cb);
//        @POST("/api/v1/users/{emobId}/neighbors/invite")

        /**
         * 获取已经邀请的邻居列表
         *
         * @param emobId
         * @param cb
         */
        @GET("/api/v1/users/{emobId}/neighbors")
        void getInvitedNums(@Path("emobId") String emobId, @QueryMap Map<String, String> map, Callback<InviteNeighborBean> cb);


        /**
         * 获取成为帮主副帮主需要邀请的人数
         *
         * @param emobId
         * @param cb
         */
        @GET("/api/v1/users/{emobId}/neighbors/unInvite")
        void getNeedInviteNums(@Path("emobId") String emobId, Callback<NeedInviteNums> cb);

        /**
         * 是否上传过电话号
         *
         * @param cb
         */
//        @GET("/api/v1/users/{emobId}/neighbors/phoneList/status")
//        void getIsUpdatePhone(@Path("emobId") String emobId, Callback<InvatePhoneResult> cb);
//        @GET("/api/v1/users/{emobId}/neighbors/phoneList/status")

//        /api/v3/communities/{小区ID}/users/{用户环信ID}/addressBook
        @GET("/api/v3/communities/{communityId}/users/{emobId}/addressBook")
        void getIsUpdatePhone(@Path("communityId") int communityId, @Path("emobId") String emobId, Callback<CommonRespBean<Boolean>> cb);
    }

    private void getUpdatePhone() {
        InvitateByPhoneNumService service = RetrofitFactory.getInstance().create(getmContext(),InvitateByPhoneNumService.class);
        Callback<CommonRespBean<Boolean>> callback = new Callback<CommonRespBean<Boolean>>() {
            @Override
            public void success(CommonRespBean<Boolean> bean, Response response) {
//                {是否需要上传用户通讯录：true->需要，false->不需要}
                if (bean != null && bean.getData()) {
                    boolean isUpdatePhone = PreferencesUtil.getUpdatePhone(getApplicationContext(), false);
                    if (!isUpdatePhone) {
                        BackGroundService.updatePhone(ActivityInviteNeighborsHome.this);
                    }
                } else {
                    Log.i(TAG, "   no");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "getUpdatePhone   error=====" + error.toString());
                error.printStackTrace();
            }
        };
        service.getIsUpdatePhone(PreferencesUtil.getCommityId(getmContext()),bean == null ? "null" : bean.getEmobId(), callback);
    }

    /**
     * 成为帮主,副帮主还需要邀请的人数
     */
    private void getNeedInviteNums() {
        mLdDialog.show();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        InvitateByPhoneNumService service = restAdapter.create(InvitateByPhoneNumService.class);
        Callback<NeedInviteNums> callback = new Callback<NeedInviteNums>() {
            @Override
            public void success(NeedInviteNums bean, Response response) {
                if (bean != null && TextUtils.equals(bean.getStatus(), "yes")) {

//                    if(has_invited_success_tv!=null){
//                        int inviteSuccessNum =  bean.getInfo().getRegistCount();
//                        has_invited_success_tv.setText("您已成功推荐"+inviteSuccessNum+"位邻居");
//                    }
                }
                mLdDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                //// 添加默认布局
                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        service.getNeedInviteNums(bean == null ? "null" : bean.getEmobId(), callback);
    }

    /**
     * 获取已经邀请的邻居列表
     */
    private void getInvitedNeighborList() {
        mLdDialog.show();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        InvitateByPhoneNumService service = restAdapter.create(InvitateByPhoneNumService.class);
        Callback<InviteNeighborBean> callback = new Callback<InviteNeighborBean>() {
            @Override
            public void success(InviteNeighborBean bean, Response response) {
                if (bean != null) {
                    if ("yes".equals(bean.getStatus())) {
                        if (bean.getInfo().getNeighbors() != null) {
                            if (bean.getInfo().getNeighbors().getPageData() != null
                                    && !bean.getInfo().getNeighbors().getPageData().isEmpty()
                                    && bean.getInfo().getNeighbors().getPageData().get(0) != null) {
//                                pageData.addAll(bean.getInfo().getNeighbors().getPageData());
//                                count = adapter.getCount();
//                                pageCount= bean.getInfo().getNeighbors().getPageCount();
//                                pageNum++;

                                ////您邀请的... 已注册, 您的人品值增加了...
                                if (!TextUtils.isEmpty(bean.getInfo().getMessage())) {
//                                    showTopToast(bean.getInfo().getMessage());
                                    if (invite_neighbors_msg_tv != null) {
                                        invite_neighbors_msg_tv.setText(bean.getInfo().getMessage());
                                        invite_neighbors_msg_tv.setVisibility(View.VISIBLE);
                                    }
                                    CountDownTimer countDownTimer = new CountDownTimer(2 * 1000, 1000) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {

                                        }

                                        @Override
                                        public void onFinish() {
                                            if (invite_neighbors_msg_tv != null) {
                                                invite_neighbors_msg_tv.setVisibility(View.GONE);
                                            }
                                        }
                                    };
                                    countDownTimer.start();

                                } else {
                                    if (invite_neighbors_msg_tv != null) {
                                        invite_neighbors_msg_tv.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }
//                        if(has_invited_list_lv!=null){
//                            ViewUtils.setListViewHeightBasedOnChildren(has_invited_list_lv);
//                        }
//                        adapter.notifyDataSetChanged();
                        mLdDialog.dismiss();
                    } else {

                        mLdDialog.dismiss();
                        showToast(bean.getMessage());
                    }
                } else {
                    mLdDialog.dismiss();
                    showToast("数据异常");
                }
//                footerView.setVisibility(View.GONE);
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
//                footerView.setVisibility(View.GONE);
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        Map<String, String> option = new HashMap<String, String>();
        option.put("pageNum", "" + 1);
        option.put("pageSize", "10");
//        footerView.setVisibility(View.VISIBLE);
        service.getInvitedNums(bean == null ? "null" : bean.getEmobId(), option, callback);
    }


    /**
     * 通过手机号发送短信邀请邻居
     *
     * @param phoneNum
     */
    private void invateByPhone(String phoneNum) {
        mLdDialog.show();

        SendSMSReqBean sendSMSReqBean = new SendSMSReqBean();
        sendSMSReqBean.setEmobId(bean.getEmobId());
        sendSMSReqBean.setCommunityName(PreferencesUtil.getCommityName(getmContext()));
        sendSMSReqBean.setNickname(bean.getNickname());

        NetBaseUtils.sendSMSbyPhone(getmContext(), PreferencesUtil.getCommityId(getmContext()), phoneNum, sendSMSReqBean, new NetBaseUtils.NetRespListener<CommonRespBean<String>>() {
            @Override
            public void successYes(CommonRespBean<String> commonRespBean, Response response) {
                showMiddleToast("帮帮已将免费信息发送给邻居");
                mLdDialog.dismiss();
            }

            @Override
            public void successNo(CommonRespBean<String> commonRespBean, Response response) {
                showMiddleToast("邀请短信发送失败:" + commonRespBean.getMessage());
                mLdDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                showMiddleToast("邀请短信发送失败,请重试");
                mLdDialog.dismiss();
            }
        });
    }

//    private class MyAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return pageData.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return pageData;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            ViewHolder viewHolder = null;
//            if (convertView == null) {
//                convertView = View.inflate(ActivityInviteNeighborsHome.this, R.layout.common_bangzhu_invited_neighboritem, null);
//                viewHolder = new ViewHolder(convertView);
//            } else {
//                viewHolder = (ViewHolder) convertView.getTag();
//            }
////            Log.i("debbug", "size=" + pageData.size());
////            Log.i("debbug", "viewHolder=" + viewHolder);
////            Log.i("debbug", "viewholder.tvname=" + viewHolder.invited_username_tv);
//
//            if(position!=0){
//                viewHolder.invited_top_line.setVisibility(View.GONE);
//            }
//            if(pageData.get(position)!=null){
//                if(TextUtils.isEmpty(pageData.get(position).getRegistNickname())){
//                    viewHolder.invited_username_tv.setText("我的邻居");
//                    viewHolder.invited_phoneno_tv.setText(pageData.get(position).getPhone());
//                    viewHolder.invited_phoneno_tv.setTextColor(getResources().getColor(android.R.color.darker_gray));
//
//                    viewHolder.invited_item_again_tv.setText("再次推荐");
//                    viewHolder.invited_item_again_tv.setTextColor(Color.parseColor("#66d99f"));
//                    viewHolder.invited_item_again_tv.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            /// 再次邀请
//                            invateByPhone(pageData.get(position).getPhone());
//                        }
//                    });
//
//                }else{
//                    viewHolder.invited_username_tv.setText(pageData.get(position).getRegistNickname());
//
//                    viewHolder.invited_phoneno_tv.setVisibility(View.GONE);
//                    viewHolder.invited_item_again_tv.setText("已注册");
//                    viewHolder.invited_item_again_tv.setBackground(null);
//                    viewHolder.invited_item_again_tv.setTextColor(Color.GRAY);
//                    viewHolder.invited_item_again_tv.setEnabled(false);
//                }
//                ImageLoader.getInstance().displayImage(pageData.get(position).getRegistAvatar(), viewHolder.iv_avatar, options);
//                return convertView;
//            }else{
//                return null;
//            }
//        }
//
//
//        class ViewHolder {
//            ImageView iv_avatar;
//            TextView invited_username_tv;
//            TextView invited_item_again_tv;
//            TextView invited_phoneno_tv;
//            View invited_top_line;
//
//
//            ViewHolder(View v) {
//                iv_avatar = (ImageView) v.findViewById(R.id.iv_avatar);
//                invited_username_tv = (TextView) v.findViewById(R.id.invited_username_tv);
//                invited_item_again_tv = (TextView) v.findViewById(R.id.invited_item_again_tv);
//                invited_phoneno_tv = (TextView) v.findViewById(R.id.invited_phoneno_tv);
//                invited_top_line = (View) v.findViewById(R.id.invited_top_line);
//                v.setTag(this);
//            }
//        }
//
//        private DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.head_portrait_personage)
//                .showImageForEmptyUri(R.drawable.head_portrait_personage)
//                .showImageOnFail(R.drawable.head_portrait_personage)
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .considerExifParams(true)
//                .build();
//    }
//

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 根据requestCode获取对应的SsoHandler
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
                requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
            Log.d("", "#### ssoHandler.authorizeCallBack");
        }
    }

    private void performShare(SHARE_MEDIA platform) {
        mController.postShare(mContext, platform, new SocializeListeners.SnsPostListener() {

            @Override
            public void onStart() {
//                showButtomToast("正在跳转");
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
//                String showText = platform.toString();
//                if(TextUtils.equals(showText,"weixin")){
//                    showText = "微信";
//                }else   if(TextUtils.equals(showText,"qq")){
//                    showText = "QQ";
//                }
//                if (eCode == StatusCode.ST_CODE_SUCCESSED) {
//                    showText += "平台分享成功";
//                } else {
//                    showText += "平台分享失败";
//                }
//                showMiddleToast(showText);
            }
        });

//        mController.getConfig().cleanListeners();

    }

    /**
     * 配置分享平台参数</br>
     */
    private void configPlatforms() {
        // // 添加新浪SSO授权
        // mController.getConfig().setSsoHandler(new SinaSsoHandler());
        // // 添加腾讯微博SSO授权
        // mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
        // 添加人人网SSO授权
        // RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(mContext,
        // "201874", "28401c0964f04a72a14c812d6132fcef",
        // "3bf66e42db1e4fa9829b955cc300b737");
        // mController.getConfig().setSsoHandler(renrenSsoHandler);

        // 添加QQ、QZone平台
        addQQQZonePlatform();

        // 添加微信、微信朋友圈平台
        addWXPlatform();

    }


    /**
     * 根据不同的平台设置不同的分享内容</br>
     */
    private void setShareContent() {

        String communityName = PreferencesUtil.getCommityName(getmContext());
//        StringBuilder msg = new StringBuilder("【帮帮】").append(communityName).append("业主自己的APP，好多邻居在这，你也来吧！点击分享地址：").append("http://bangbang.ixiaojian.com/jsp/invitedUserAccept.jsp?way=sms&emobId=").append(bean.getEmobId()).append("&q=").append(URLEncoder.encode(communityName, "utf-8"));

        StringBuilder wxshareURL = new StringBuilder(Config.NET_BASE);
        wxshareURL.append("/jsp/invitedUserAccept.jsp?way=wx&emobId=");
        try {
            wxshareURL.append(bean.getEmobId())
                    .append("&q=").append(URLEncoder.encode(URLEncoder.encode(communityName, "utf-8"), "utf-8"))
                    .append("&type=" + inviteType)
                    .append("&u=" + bean.getNickname());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringBuilder qqshareURL = new StringBuilder(Config.NET_BASE);
        qqshareURL.append("/jsp/invitedUserAccept.jsp?way=qq&emobId=");
        try {
            qqshareURL.append(bean.getEmobId())
                    .append("&q=").append(URLEncoder.encode(URLEncoder.encode(communityName, "utf-8"), "utf-8"))
                    .append("&type=" + inviteType)
                    .append("&u=" + bean.getNickname());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 配置SSO
//        mController.getConfig().setSsoHandler(new SinaSsoHandler());
//        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity) mContext,
                "100424468", "c7394704798a158208a74ab60104f0ba");
        qZoneSsoHandler.addToSocialSDK();


        mController.setShareContent("咱们小区专属的app,好多邻居都在，你也快来吧!");

//        mController.setShareContent("帮帮 业主自己的APP，好多邻居在这，你也来吧！点击分享地址：" + qqshareURL.toString());

        UMImage localImage = new UMImage(mContext, R.drawable.bangbang_logonew);

//        UMImage urlImage = new UMImage(mContext,
//                "http://www.umeng.com/images/pic/social/integrated_3.png");
        // UMImage resImage = new UMImage(mContext, R.drawable.icon);

        // 视频分享
//        UMVideo video = new UMVideo(
//                "http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
//        // vedio.setThumb("http://www.umeng.com/images/pic/home/social/img-1.png");
//        video.setTitle("友盟社会化组件视频");
//        video.setThumb(urlImage);
//
//        UMusic uMusic = new UMusic(
//                "http://music.huoxing.com/upload/20130330/1364651263157_1085.mp3");
//        uMusic.setAuthor("umeng");
//        uMusic.setTitle("天籁之音");
//        // uMusic.setThumb(urlImage);
//        uMusic.setThumb("http://www.umeng.com/images/pic/social/chart_1.png");

        // UMEmoji emoji = new UMEmoji(mContext,
        // "http://www.pc6.com/uploadimages/2010214917283624.gif");
        // UMEmoji emoji = new UMEmoji(mContext,
        // "/storage/sdcard0/emoji.gif");

        WeiXinShareContent weixinContent = new WeiXinShareContent();

        weixinContent.setShareContent("咱们小区专属的app,好多邻居都在，你也快来吧!");
        weixinContent.setTitle("小区邻居发来的邀请");

        weixinContent.setTargetUrl(wxshareURL.toString());
//        weixinContent.setShareMedia(urlImage);
        weixinContent.setShareMedia(localImage);
        mController.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
//        CircleShareContent circleMedia = new CircleShareContent();
//        circleMedia.setShareContent("帮帮 业主自己的APP，好多邻居在这，你也来吧！点击分享地址：" + wxshareURL.toString());
//        circleMedia.setTitle("帮帮业主自己的APP你也来吧！");
//        circleMedia.setTargetUrl( wxshareURL.toString());
////        circleMedia.setShareMedia(urlImage);
//        circleMedia.setShareMedia(localImage);
//        // circleMedia.setShareMedia(uMusic);
//        // circleMedia.setShareMedia(video);
//        mController.setShareMedia(circleMedia);


//        UMImage qzoneImage = new UMImage(mContext,
//                "http://www.umeng.com/images/pic/social/integrated_3.png");
//        qzoneImage .setTargetUrl("http://www.umeng.com/images/pic/social/integrated_3.png");

        // 设置QQ空间分享内容
//        QZoneShareContent qzone = new QZoneShareContent();
//        qzone.setShareContent("share test");
//        qzone.setTargetUrl(wxshareURL.toString());
//        qzone.setTitle("QZone title");
//        qzone.setShareMedia(localImage);
//        // qzone.setShareMedia(uMusic);
//        mController.setShareMedia(qzone);

//        video.setThumb(new UMImage(mContext, BitmapFactory.decodeResource(
//                getResources(), R.drawable.bangbang_logonew)));

//        UMImage image = new UMImage(mContext,  BitmapFactory.decodeResource(getResources(), R.drawable.bangbang_logonew));
//        image.setTitle("thumb title");
//        image.setThumb("http://www.umeng.com/images/pic/social/integrated_3.png");

        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent("咱们小区专属的app,好多邻居都在，你也快来吧!");
//        qqShareContent.setShareContent("帮帮 业主自己的APP，好多邻居在这，你也来吧！点击分享地址：" + qqshareURL.toString());
        qqShareContent.setTitle("小区邻居发来的邀请");

        qqShareContent.setShareMedia(localImage);
        qqShareContent.setTargetUrl(qqshareURL.toString());
        mController.setShareMedia(qqShareContent);

//        // 视频分享
//        UMVideo umVideo = new UMVideo(
//                "http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
//        umVideo.setThumb("http://www.umeng.com/images/pic/home/social/img-1.png");
//        umVideo.setTitle("友盟社会化组件视频");
//
//        TencentWbShareContent tencent = new TencentWbShareContent();
//        tencent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-腾讯微博。http://www.umeng.com/social");
//        // 设置tencent分享内容
//        mController.setShareMedia(tencent);

    }


    /**
     * @return
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     * image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     * 要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     * : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     */
    private void addQQQZonePlatform() {

        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) mContext, Config.QQAPPID,
                Config.QQKEY);
        qqSsoHandler.setTargetUrl("http://www.linjubangbang.com");
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity) mContext, Config.QQAPPID,
                Config.QQKEY);
        qZoneSsoHandler.addToSocialSDK();
    }

    /**
     * @return
     * @功能描述 : 添加微信平台分享
     */
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppSID
//		String appId = "wxe5013db2df614611";
//		String appSecret = "426686f8eaacac48009903535c8b8969";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(mContext, Config.APP_ID, Config.WEIXINAPPSCRET);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(mContext,Config.APP_ID, Config.WEIXINAPPSCRET);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.invite_by_weixin_btn:

                // 推荐到微信
                if (CommonUtils.isNetWorkConnected(this)) {
                    performShare(SHARE_MEDIA.WEIXIN);
                } else {
//                    showToast("无网络");
                    showNetErrorToast();
                }

                break;
            case R.id.invite_by_qq_btn:

                // 推荐到QQ
                if (CommonUtils.isNetWorkConnected(this)) {
                    performShare(SHARE_MEDIA.QQ);
                } else {
//                    showToast("无网络");
                    showNetErrorToast();
                }

                break;

            case R.id.invite_by_phonelist_btn:
                startActivity(new Intent(this, ActivityInviteNumList.class).putExtra("inviteType", inviteType));


                break;
            case R.id.confirm_invite_btn:

                if (invite_by_phone_num_et != null) {

                    String phoneNum = invite_by_phone_num_et.getText().toString().trim();

                    if (TextUtils.isEmpty(phoneNum)) {

                        showToast("手机号不能为空");
                    } else {
                        invateByPhone(phoneNum);
                    }
                }
                break;
            case R.id.iv_back:
                finish();
                break;

        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mController.getConfig().cleanListeners();
    }

}
