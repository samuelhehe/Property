package xj.property.activity.runfor;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.repo.xw.library.views.PullListView;
import com.repo.xw.library.views.PullToRefreshLayout;
import com.umeng.socialize.sso.UMSsoHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.bangzhu.ActivityBangZhuPrivilege;
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.adapter.ActivityRunForAdapter;
import xj.property.beans.RunForAllBean;
import xj.property.beans.RunForAllV3Bean;
import xj.property.beans.RunForMyBean;
import xj.property.beans.RunFordbBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.db.RunForDao;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.provider.ShareProvider;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.CircleImageView;

/**
 * aurth:asia
 * 竞选页面
 */
public class RunForActivity extends HXBaseActivity implements PullToRefreshLayout.OnRefreshListener {

    //刷新变量
    private int ListViewStateType;
    private final int LOADMORE = 1;
    private final int REFRESH = 2;
    //变量
    private int mPageNo = 1;
    private int mPageNum = 10;
    private boolean mFirstLoad = true;//是否是第一次加载
    private List<RunForAllV3Bean.RunForDataV3Bean> mRunForBeanList = new ArrayList<RunForAllV3Bean.RunForDataV3Bean>();

    private String emobId = "fcb6adf78bef4ee4940d2af8ee7905f9";
    private String uemobid;
    private int communityId = 2;
    private RunForDao mRunForDao;
    private int mRank = 0;

    public final int VOTESUCESS = 88;

    private ImageView bannerImg;
    private ActivityRunForAdapter mActivityRunForAdapter;
    private PullToRefreshLayout mRefreshLayout;
    private PullListView mPullListView;

    private CircleImageView mAvatar1;
    private TextView mTv_num;
    private TextView mTv_score1;
    private ImageView mIv_arraw;
    private ImageView mIv_arraw_down;
    private TextView mTv_go_campaign;
    private LinearLayout mLl_runfor_header;
    private LinearLayout mLl_banner;
    private LinearLayout mLl_explan;
    private RelativeLayout mRl_banner2;
    private TextView mTv_nickname;

    //title
    private RelativeLayout mHeaptop;
    private ImageView mIv_back;
    private TextView mTv_left_text;
    private TextView mTv_title;
    private TextView mTv_right_text;
    private ImageView mIv_right;

    public RunForAllV3Bean.RunForDataV3Bean mRunForBean;

    //从生活圈进入到页面
    private TextView mTv_no;
    private xj.property.widget.CircleImageView mAvatar;
    private TextView mTv_name;
    private TextView mTv_score;
    private LinearLayout mTop_left;
    private LinearLayout mTop_right;
    //    private TextView mRight_percent_tv;
    private ImageView mIv_direction;
    private ImageView mIv_direction_down;
    private TextView mTv_playnum;
    private ShareProvider mShareProvider;

    private boolean isElection = false;
    private UserInfoDetailBean bean;
    private boolean voted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runfor);
        bean = PreferencesUtil.getLoginInfo(RunForActivity.this);
        ShareProvider.getInitShareProvider(RunForActivity.this);
//        Log.i("RunForActivity", bean.toString() + "");
        if (bean != null) {
            emobId = bean.getEmobId();
            communityId = PreferencesUtil.getCommityId(this);
            uemobid = getIntent().getStringExtra("uemobid");
            mRunForDao = new RunForDao(getApplicationContext());
            initView();
            initDate();
            initListenner();
        } else {
            showDataErrorToast();
            finish();
        }
    }

    private void initListenner() {
        mTv_go_campaign.setOnClickListener(this);
        mLl_runfor_header.setOnClickListener(this);
        mTv_right_text.setOnClickListener(this);
        mIv_back.setOnClickListener(this);
        mLl_explan.setOnClickListener(this);
        mIv_right.setOnClickListener(this);
    }

    private void initDate() {
        mTv_title.setText("帮主竞选");
        mIv_right.setVisibility(View.VISIBLE);
        mIv_right.setBackgroundDrawable(getResources().getDrawable(R.drawable.share));
//        getMySelf();
        getAllMessage();
        isVoteElection();
        List<RunForAllV3Bean.RunForDataV3Bean> runForList = new ArrayList<RunForAllV3Bean.RunForDataV3Bean>();
        mActivityRunForAdapter = new ActivityRunForAdapter(this, runForList);
        mPullListView.setAdapter(mActivityRunForAdapter);
        mRefreshLayout.setOnRefreshListener(this);
    }

    private void initListView(List<RunForAllV3Bean.RunForDataV3Bean> runForList, String electedEmobId) {
        mActivityRunForAdapter.setMyElectedEmobId(electedEmobId);
        mActivityRunForAdapter.ChangeRefresh(runForList);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_go_campaign:
                if (!isElection) {
                    Intent mottoRunForintent = new Intent(this, MottoRunForActivity.class);
                    mottoRunForintent.putExtra("runForBean", mRunForBean);
                    startActivity(mottoRunForintent);
                } else {
                    Toast.makeText(getApplicationContext(), "您本月已经拉过选票了", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_runfor_header:
                Intent myRunForintent = new Intent(this, MyRunForActivity.class);
                myRunForintent.putExtra("runForBean", mRunForBean);
                startActivity(myRunForintent);
                break;
            case R.id.ll_explan:
                startActivity(new Intent(this, ActivityBangZhuPrivilege.class));
                break;
            case R.id.iv_right:
                String url = Config.NET_SHAREBASE + "/share/bangzhu.html?communityId=" + bean.getCommunityId() + "&emobId=" + bean.getEmobId();
                ShareProvider.getShareProvider(RunForActivity.this).showShareActivity(url, "我正在参与帮主竞选，目前名列第“" + mRank + "”位！", "邻居帮帮", mShareProvider.CODE_RUNFOR, bean.getAvatar());
                break;
        }
    }

    public void initView() {
        mRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pullToRefreshLayout);
        mPullListView = (PullListView) findViewById(R.id.pullListView);
        mAvatar1 = (CircleImageView) findViewById(R.id.avatar1);
        mTv_num = (TextView) findViewById(R.id.tv_num);
        mTv_score1 = (TextView) findViewById(R.id.tv_score1);
        mIv_arraw = (ImageView) findViewById(R.id.iv_arraw);
        mIv_arraw_down = (ImageView) findViewById(R.id.iv_arraw_down);
        mTv_go_campaign = (TextView) findViewById(R.id.tv_go_campaign);
        mLl_runfor_header = (LinearLayout) findViewById(R.id.ll_runfor_header);
        mTv_nickname = (TextView) findViewById(R.id.tv_nickname);
        mIv_right = (ImageView) findViewById(R.id.iv_right);
        mHeaptop = (RelativeLayout) findViewById(R.id.heaptop);
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mTv_left_text = (TextView) findViewById(R.id.tv_left_text);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mTv_right_text = (TextView) findViewById(R.id.tv_right_text);
        mLl_explan = (LinearLayout) findViewById(R.id.ll_explan);
        mIv_right = (ImageView) findViewById(R.id.iv_right);
        mLl_banner = (LinearLayout) findViewById(R.id.ll_banner);
        mRl_banner2 = (RelativeLayout) findViewById(R.id.rl_banner2);
        mTv_no = (TextView) findViewById(R.id.tv_no);
        mAvatar = (xj.property.widget.CircleImageView) findViewById(R.id.avatar);
        mTv_name = (TextView) findViewById(R.id.tv_name);
        mTv_score = (TextView) findViewById(R.id.tv_score);
        mTop_left = (LinearLayout) findViewById(R.id.top_left);
        mTop_right = (LinearLayout) findViewById(R.id.top_right);
        mIv_direction = (ImageView) findViewById(R.id.iv_direction);
        mIv_direction_down = (ImageView) findViewById(R.id.iv_direction_down);
        mTv_playnum = (TextView) findViewById(R.id.tv_playnum);
    }

    private List<RunForAllV3Bean.RunForDataV3Bean> upDateArrowList(List<RunForAllV3Bean.RunForDataV3Bean> runForBeanList) {

        List<RunForAllV3Bean.RunForDataV3Bean> changeList = new ArrayList<RunForAllV3Bean.RunForDataV3Bean>();
        for (int i = 0; i < runForBeanList.size(); i++) {
            RunForAllV3Bean.RunForDataV3Bean bean = runForBeanList.get(i);
            RunForAllV3Bean.RunForDataV3Bean changeBean = upDateArrow(bean);
            changeList.add(changeBean);
        }
        return changeList;
    }

    /**
     * TODO 本地缓存箭头对比
     *
     * @param bean
     * @return
     */
    private RunForAllV3Bean.RunForDataV3Bean upDateArrow(RunForAllV3Bean.RunForDataV3Bean bean) {
        //1.先查询本地是否有当前id的数据
        RunFordbBean bean2 = mRunForDao.getRunFordbBean(bean.getEmobId());
        //2.有数据对比修改箭头方向，没有数据箭头方向默认是向上
        if (bean2 != null && bean2.getRank() != 0) {
            if (bean2.getRank() < bean.getRank()) {
                bean.setArrowUpOrDown(false);
            } else {
                bean.setArrowUpOrDown(true);
            }
            bean2.setRank(bean.getRank());
            mRunForDao.updateRunFordbBean(bean2);
        } else {
            bean.setArrowUpOrDown(true);
            mRunForDao.saveRunFordbBean(bean);
        }
        return bean;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == VOTESUCESS) {
            mPageNo = 1;
            ListViewStateType = REFRESH;
            mFirstLoad = true;
            getAllMessage();
//            getMySelf();
        }
        UMSsoHandler ssoHandler = ShareProvider.mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        mPullListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPageNo = 1;
                ListViewStateType = REFRESH;
                getAllMessage();
            }
        }, 40);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        mPullListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPageNo++;
                ListViewStateType = LOADMORE;
                getAllMessage();
            }
        }, 40);
    }


    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


//    /**
//     * 我的个人信息
//     */
//    interface MySelfService {
//        @GET("/api/v1/communities/{communityId}/election/myself")
//        void getActList(@Path("communityId") int communityId, @QueryMap Map<String, String> map, Callback<RunForMyBean> cb);
//    }

    interface RunForService {

        @GET("/api/v1/communities/{communityId}/election/myself")
        void getMySelf(@Path("communityId") int communityId, @QueryMap Map<String, String> map, Callback<RunForMyBean> cb);

        @GET("/api/v1/communities/{communityId}/election")
        void getActList(@Path("communityId") int communityId, @QueryMap Map<String, String> map, Callback<RunForAllBean> cb);

        /**
         * 本月竞选人列表
         *
         * @param map
         * @param cb
         */
        @GET("/api/v3/elections/rank")
        void getRunForList(@QueryMap Map<String, String> map, Callback<CommonRespBean<RunForAllV3Bean>> cb);

        /**
         * 判断用户本月能否拉选举票
         *
         * @param map
         * @param cb
         */
        @GET("/api/v3/elections/canCanvass")
        void isVoteElection(@QueryMap Map<String, String> map, Callback<CommonRespBean<String>> cb);

        /**
         * 获取用户本月帮主竞选排名情况
         *
         * @param map
         * @param cb
         */
        @GET("/api/v3/elections/rank/user")
        void getUserVoteMessage(@QueryMap Map<String, String> map, Callback<CommonRespBean<RunForAllV3Bean.RunForDataV3Bean>> cb);

    }

    private void isVoteElection() {

        HashMap<String, String> option = new HashMap<String, String>();
        option.put("emobId", emobId);
        option.put("communityId", communityId + "");

        RunForService Service = RetrofitFactory.getInstance().create(getmContext(), option, RunForService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, retrofit.client.Response response) {
                if (bean != null) {
                    if ("yes".equals(bean.getStatus())) {
                        isElection = false;
                    } else {
                        isElection = true;
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        Service.isVoteElection(option, callback);
    }

//    private void getMySelf() {
//        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
//        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
//        RunForService service = restAdapter.create(RunForService.class);
//        Callback<RunForMyBean> callback = new Callback<RunForMyBean>() {
//            @Override
//            public void success(RunForMyBean bean, retrofit.client.Response response) {
//                if (bean != null) {
//                    if ("yes".equals(bean.getStatus()) && null != bean.getInfo()) {
//                        RunForBean myBean = bean.getInfo();
//                        ImageLoader.getInstance().displayImage(myBean.getAvatar(), mAvatar1, options);
//                        mTv_num.setText("" + myBean.getRank());
//                        mRank = myBean.getRank();
//                        mTv_score1.setText(myBean.getScore() + "分");
//                        mTv_nickname.setText(myBean.getNickname());
//                        //mIv_arraw
//                        myBean.setEmobId(emobId);
//                        mRunForBean = upDateArrow(myBean);
//                        if (mRunForBean.isArrowUpOrDown()) {
//                            mIv_arraw.setVisibility(View.VISIBLE);
//                            mIv_arraw_down.setVisibility(View.GONE);
//                        } else {
//                            mIv_arraw.setVisibility(View.GONE);
//                            mIv_arraw_down.setVisibility(View.VISIBLE);
//                        }
//
//                    } else {
//
//                    }
//
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                error.printStackTrace();
//                showNetErrorToast();
//            }
//        };
//        Map<String, String> option = new HashMap<String, String>();
//        option.put("emobId", emobId);
//        service.getMySelf(communityId, option, callback);
//    }


    private void getMessage(final String emobId, final String uemobid) {

        ObjectAnimator  animator = new ObjectAnimator();


        HashMap<String, String> option = new HashMap<String, String>();
        option.put("emobId", uemobid);
        option.put("myEmobId", emobId);
        option.put("communityId", communityId + "");

        RunForService service = RetrofitFactory.getInstance().create(getmContext(), option, RunForService.class);
        Callback<CommonRespBean<RunForAllV3Bean.RunForDataV3Bean>> callback = new Callback<CommonRespBean<RunForAllV3Bean.RunForDataV3Bean>>() {
            @Override
            public void success(final CommonRespBean<RunForAllV3Bean.RunForDataV3Bean> bean, retrofit.client.Response response) {
                if (bean != null && "yes".equals(bean.getStatus()) && bean.getData() != null) {
                    final RunForAllV3Bean.RunForDataV3Bean myBean = bean.getData();
                    mTv_no.setText(myBean.getRank() + "");
                    ImageLoader.getInstance().displayImage(myBean.getAvatar(), mAvatar, options);
                    mTv_name.setText(myBean.getNickname());
                    mTv_score.setText(myBean.getScore() + "分");
                    int maxNum = 0;
                    if (mRunForBeanList.size() > 0) {
                        mRunForBeanList.get(0).getScore();
                    }
//                        RunForAllV3Bean.RunForDataV3Bean runfor = mRunForBeanList.get(0);
                    RunForAllV3Bean.RunForDataV3Bean messageRunfor = upDateArrow(myBean);
                    if (messageRunfor.isArrowUpOrDown()) {
                        mIv_direction.setVisibility(View.VISIBLE);
                        mIv_direction_down.setVisibility(View.GONE);
                    } else {
                        mIv_direction.setVisibility(View.GONE);
                        mIv_direction_down.setVisibility(View.VISIBLE);
                    }
                    float leftPercent = Float.valueOf(myBean.getScore()) / maxNum;
                    leftPercent *= 0.8f;
                    if (maxNum == 0) {
                        leftPercent = 0.0f;
                    }
                    if (!myBean.isVoted()) {
                        mTv_playnum.setVisibility(View.VISIBLE);
                        mTv_playnum.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent runForDialogActivity = new Intent(RunForActivity.this, RunForDialogActivity.class);
                                runForDialogActivity.putExtra("runForBean", myBean);
                                runForDialogActivity.putExtra("mRunForBean", mRunForBean);
                                startActivityForResult(runForDialogActivity, VOTESUCESS);
                            }
                        });
                    } else {
                        mTv_playnum.setVisibility(View.GONE);
                    }

                    mAvatar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent userGroupInfoActivity = new Intent(RunForActivity.this, UserGroupInfoActivity.class);
                            userGroupInfoActivity.putExtra(Config.INTENT_PARMAS2, bean.getData().getEmobId());
                            startActivity(userGroupInfoActivity);
                        }
                    });
                    mTv_score.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent myRunForIntent = new Intent(RunForActivity.this, MyRunForActivity.class);
                            myRunForIntent.putExtra("runForBean", bean.getData());
                            startActivity(myRunForIntent);
                        }
                    });
                }else if(bean!=null){
                    showDataErrorToast(bean.getMessage());
                }else{
                    showDataErrorToast();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        service.getUserVoteMessage(option, callback);
    }

    private void initMyBean(final RunForAllV3Bean.RunForDataV3Bean bean, String passID) {
        RunForAllV3Bean.RunForDataV3Bean myBean = bean;
        ImageLoader.getInstance().displayImage(myBean.getAvatar(), mAvatar1, options);
        mTv_num.setText("" + myBean.getRank());
        mRank = myBean.getRank();
        mTv_score1.setText(myBean.getScore() + "分");
        mTv_nickname.setText(myBean.getNickname());
        //mIv_arraw
        myBean.setEmobId(emobId);
        mRunForBean = upDateArrow(myBean);
        if (mRunForBean.isArrowUpOrDown()) {
            mIv_arraw.setVisibility(View.VISIBLE);
            mIv_arraw_down.setVisibility(View.GONE);
        } else {
            mIv_arraw.setVisibility(View.GONE);
            mIv_arraw_down.setVisibility(View.VISIBLE);
        }
    }

    private void getAllMessage() {

        HashMap<String, String> option = new HashMap<String, String>();
        option.put("emobId", emobId);
        option.put("page", mPageNo + "");
        option.put("limit", mPageNum + "");
        option.put("communityId", communityId + "");

        RunForService service = RetrofitFactory.getInstance().create(getmContext(), option, RunForService.class);
        Callback<CommonRespBean<RunForAllV3Bean>> callback = new Callback<CommonRespBean<RunForAllV3Bean>>() {
            @Override
            public void success(CommonRespBean<RunForAllV3Bean> bean, retrofit.client.Response response) {
                if (bean != null) {
                    if ("yes".equals(bean.getStatus()) && bean.getData() != null&&bean.getData().getData()!=null) {
                        RunForAllV3Bean myBean = bean.getData();
                        if(mPageNo==1){
                            voted = (Boolean) bean.getField("voted");
                        }

                        List<RunForAllV3Bean.RunForDataV3Bean> list = myBean.getData();
                        list = upDateArrowList(list);
                        if (list.size() > 0) {
                            mRunForBean = list.get(0);
                            mRunForBeanList.clear();
                            list.remove(0);
                            mRunForBeanList.addAll(list);
                            initMyBean(mRunForBean, "");
                            if (mFirstLoad) {
                                initListView(list, "");
                                mFirstLoad = false;
                                if (mActivityRunForAdapter != null) {
                                    mActivityRunForAdapter.setVoted((Boolean) bean.getField("voted"));
                                    if (uemobid != null && !"".equals(uemobid) && !uemobid.equals(emobId)) {
                                        getMessage(emobId, uemobid);
                                        mLl_banner.setVisibility(View.VISIBLE);
                                        mRl_banner2.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else if (ListViewStateType == LOADMORE) {
                                mActivityRunForAdapter.LoadMoreRefresh(mRunForBeanList);
                            } else {
                                mActivityRunForAdapter.ChangeRefresh(list);
                            }
                        }
                    }
                    mRefreshLayout.loadMoreFinish(true);
                    mRefreshLayout.refreshFinish(true);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                showNetErrorToast();
                if (ListViewStateType == LOADMORE) {
                    mRefreshLayout.loadMoreFinish(false);
                    mPullListView.canPullDown();
                } else {
                    mRefreshLayout.refreshFinish(false);
                    mPullListView.canPullUp();
                }
            }
        };
        service.getRunForList(option, callback);
    }

    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.default_picture)
            .showImageForEmptyUri(R.drawable.default_picture)
            .showImageOnFail(R.drawable.default_picture)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();


}
