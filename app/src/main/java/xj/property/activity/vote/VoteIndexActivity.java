package xj.property.activity.vote;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.repo.xw.library.views.PullListView;
import com.repo.xw.library.views.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.MainActivity;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.activity.LifeCircle.MyLifeCircleActivity;
import xj.property.activity.LifeCircle.RPValueTopListActivity;
import xj.property.activity.LifeCircle.SearchLifeCircle;
import xj.property.activity.runfor.RunForActivity;
import xj.property.adapter.VoteIndexMessageAdapter;
import xj.property.adapter.XJBaseAdapter;
import xj.property.beans.CooperationStatusRespBean;
import xj.property.beans.RunForAllV3Bean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.beans.VoteDetailsRespBean;
import xj.property.beans.VoteHeaderRankListRespBean;
import xj.property.beans.VoteIndexDiscussInfoRespBean;
import xj.property.beans.VoteIndexDiscussInfoRespV3Bean;
import xj.property.beans.VoteIndexRespBean;
import xj.property.beans.VoteIndexRespInfoBean;
import xj.property.beans.VoteIndexRespV3Bean;
import xj.property.beans.VoteOptionsListEntity;
import xj.property.event.NewVotedBackEvent;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.CommonUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

/**
 * 投票首页列表
 */
public class VoteIndexActivity extends HXBaseActivity implements PullToRefreshLayout.OnRefreshListener {

    final static int REQUEST_NEW_Provider = 778;
    ////未发起过投票
    private static final int IHAS_LAUNCHED_VOTE = 0;
    /// 网络请求异常 请稍后重试
    private static final int IHAS_LAUNCHED_VOTE_PULL_ERROR = 2;

    public ListView lv_eva;

//    ImageView avatar;

    private UserInfoDetailBean userInfoDetailBean;

    private HashMap<Integer, VoteIndexRespInfoBean> map = new HashMap<>();

    private List<VoteIndexDiscussInfoRespV3Bean> praiseNotifies = new ArrayList<>();

    private List<VoteIndexRespInfoBean> circleBeanList = new ArrayList<>();

//    TextView tv_right_text;

    private VoteIndexMessageAdapter lifeMessageAdapter;

    private XJBaseAdapter adapter;

    private View headView;

    private int pageIndex = 0;

    private LinearLayout ll_errorpage;
    private LinearLayout ll_neterror;
    private LinearLayout ll_index_empty_outer;

    private ImageView ll_index_empty_outer_iv;

    private TextView vote_index_bangzhu_cname_tv;
    //// 已经投过票的人排名
    private LinearLayout panic_has_purchase_llay;
    //// 已经投过票的人排名横向填充
    private LinearLayout welfare_purchase_hasgoturs_lv;
    /// 屏幕宽度
    private int screenWidth;

    private PullToRefreshLayout pull_to_refreshlayout;
    ///
    private PullListView pull_listview;
    /// 发起投票
    private Button ilaunch_vote_go_btn;
    /// 去竞选帮主
    private LinearLayout vote_bangzhu_go_llay;

    private int hasCreatedVoteToDay = -1; /// 今天是否投过票
    private ImageView iv_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_index);

        screenWidth = getmContext().getResources().getDisplayMetrics().widthPixels;
        userInfoDetailBean = PreferencesUtil.getLoginInfo(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ///IMP 设置未读消息0
        PreferencesUtil.saveVoteIndexCount(this, 0);
        if (userInfoDetailBean != null) {
            praiseNotifies.addAll(PreferencesUtil.getVoteDiscussInfoNotify(this, userInfoDetailBean.getEmobId()));
//            praiseNotifies.addAll(PreferencesUtil.getCooperationNotify(this, userInfoDetailBean.getEmobId()));
            getCircleTipsList(userInfoDetailBean.getEmobId());
        }

        initView();

//        refreshData();
        getData();

        //// 首次加载刷新数据
        pull_to_refreshlayout.autoRefresh();

        //// 如果首次进入则跳转至操作提示页
        boolean isunreadvote = PreferencesUtil.isUnReadVoteIndex(getmContext());
        if (isunreadvote) {
            startActivity(new Intent(getmContext(), VoteNoticesPagerActivity.class));
        }
    }

    private void refreshData() {

        if (CommonUtils.isNetWorkConnected(this)) {
            getData();
        } else {
            ll_errorpage.setVisibility(View.VISIBLE);
            ll_neterror.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        userInfoDetailBean = PreferencesUtil.getLoginInfo(this);
        if (userInfoDetailBean != null) {
            getCircleTipsList(userInfoDetailBean.getEmobId());
        }
    }


    public void onEvent(NewVotedBackEvent newVotedBackEvent) {
        if (newVotedBackEvent != null) {
            VoteDetailsRespBean providerDetailsBean = newVotedBackEvent.getProviderDetailsBean();
            if (providerDetailsBean != null) {
                VoteIndexRespInfoBean voteIndexRespInfoBean = map.get(providerDetailsBean.getVoteId());
                voteIndexRespInfoBean.setVoteSum(providerDetailsBean.getVoteSum());
                voteIndexRespInfoBean.setVoteId(providerDetailsBean.getVoteId());
                List<VoteOptionsListEntity> voteOptionsList = voteIndexRespInfoBean.getOptions();
                if (voteOptionsList != null) {
                    voteOptionsList.clear();
                    voteOptionsList.addAll(providerDetailsBean.getOptions());
                } else {
                    voteOptionsList = new ArrayList<>();
                    voteOptionsList.addAll(providerDetailsBean.getOptions());
                    voteIndexRespInfoBean.setOptions(voteOptionsList);
                }
                if (lifeMessageAdapter != null) {
                    lifeMessageAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void initTitle() {

        findViewById(R.id.iv_back).setOnClickListener(this);

//        avatar = (ImageView) findViewById(R.id.iv_avatar);
//        avatar.setOnClickListener(this);

//        if (userInfoDetailBean != null) {
//            ImageLoader.getInstance().displayImage(userInfoDetailBean.getAvatar(), avatar, UserUtils.options);
//        }

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("投票");

        iv_right = (ImageView) findViewById(R.id.iv_right);
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setImageResource(R.drawable.vote_history_icon);
        iv_right.setOnClickListener(this);

//        tv_right_text = (TextView) findViewById(R.id.tv_right_text);
//        tv_right_text.setVisibility(View.VISIBLE);
//        tv_right_text.setTextSize(14);
//        tv_right_text.setPadding(13,3,13,8);
//        tv_right_text.setBackground(getResources().getDrawable(R.drawable.vote_right_top_rounded_rectangle));
//        tv_right_text.setText("我发过的投票");
//        tv_right_text.setTextColor(Color.parseColor("#979797"));
//        tv_right_text.setOnClickListener(this);


    }

    private void initView() {

        initTitle();

        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        ll_index_empty_outer = (LinearLayout) findViewById(R.id.ll_index_empty_outer);
        ll_index_empty_outer_iv = (ImageView) findViewById(R.id.ll_index_empty_outer_iv);
        ll_index_empty_outer_iv.setImageResource(R.drawable.vote_index_none);

        ll_neterror.setOnClickListener(this);

        pull_to_refreshlayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refreshlayout);
        pull_to_refreshlayout.setOnRefreshListener(this);

        pull_listview = (PullListView) findViewById(R.id.pull_listview);

        pull_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (pull_listview.getLastVisiblePosition() == (pull_listview.getCount() - 1)) {
                            pull_to_refreshlayout.autoLoad();
                        }
                        // 判断滚动到顶部
                        if (pull_listview.getFirstVisiblePosition() == 0) {

                        }

                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


            }
        });
        ilaunch_vote_go_btn = (Button) findViewById(R.id.ilaunch_vote_go_btn);
        ilaunch_vote_go_btn.setOnClickListener(this);

        lifeMessageAdapter = new VoteIndexMessageAdapter(this, circleBeanList);

        if (headView == null) {
            headView = View.inflate(this, R.layout.common_vote_index_bangzhu_item, null);

            //// 哪个小区的投票信息
            vote_index_bangzhu_cname_tv = (TextView) headView.findViewById(R.id.vote_index_bangzhu_cname_tv);

            ///
            String cname = PreferencesUtil.getCommityName(getmContext());
            if (TextUtils.isEmpty(cname)) {
                vote_index_bangzhu_cname_tv.setText("帮帮小区");
            } else {
                vote_index_bangzhu_cname_tv.setText(cname + "小区");
            }
            panic_has_purchase_llay = (LinearLayout) headView.findViewById(R.id.panic_has_purchase_llay);

            welfare_purchase_hasgoturs_lv = (LinearLayout) headView.findViewById(R.id.welfare_purchase_hasgoturs_lv);
            welfare_purchase_hasgoturs_lv.setClickable(true);
            welfare_purchase_hasgoturs_lv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goBangzhuElection();
                }
            });

            vote_bangzhu_go_llay = (LinearLayout) headView.findViewById(R.id.vote_bangzhu_go_llay);
            vote_bangzhu_go_llay.setClickable(true);
            vote_bangzhu_go_llay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goBangzhuElection();
                }
            });


            /// 谁评论了你
            lv_eva = (ListView) headView.findViewById(R.id.lv_eva);

            pull_listview.addHeaderView(headView);
        }

        pull_listview.setAdapter(lifeMessageAdapter);

//        headView.findViewById(R.id.search).setOnClickListener(this);


//       NewPraiseNotify praiseNotify=new NewPraiseNotify();
//        praiseNotify.setAvatar4Show("http://baidu.logo");
//        praiseNotify.setContent4Show("测试");
//        praiseNotifies.add(praiseNotify);

        adapter = new XJBaseAdapter(this, R.layout.common_vote_neweva_item, praiseNotifies, new String[]{"contentShow"}, new String[]{"getAvatar"}, new int[]{R.id.neweva_head}, UserUtils.options);

//        adapter = new XJBaseAdapter(this, R.layout.item_neweva_notify, praiseNotifies, new String[]{"content4Show"}, new String[]{"getAvatar4Show"}, new int[]{R.id.neweva_head}, UserUtils.options);
        lv_eva.setAdapter(adapter);

        lv_eva.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                VoteIndexDiscussInfoRespV3Bean newPraiseNotify = praiseNotifies.remove(position);
                for (int i = 0; i < praiseNotifies.size(); ) {

                    if (TextUtils.equals(newPraiseNotify.getSourceId(), praiseNotifies.get(i).getSourceId())) {

//                    if (newPraiseNotify.getSourceId() == praiseNotifies.get(i).getLifeCircleId()) {

                        praiseNotifies.remove(i);
                    } else {
                        i++;
                    }
                }

                PreferencesUtil.saveVoteDiscussInfoNotify(VoteIndexActivity.this, userInfoDetailBean.getEmobId(), praiseNotifies);
//                PreferencesUtil.saveCooperationNotify(VoteIndexActivity.this, userInfoDetailBean.getEmobId(), praiseNotifies);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        lifeMessageAdapter.notifyDataSetChanged();
                    }
                });

                /// 点赞 点赞功能取消
                if (TextUtils.equals(newPraiseNotify.getType(), "election")) {
                    goBangzhuElection();

                } else if (TextUtils.equals(newPraiseNotify.getType(), "vote")
                        || TextUtils.equals(newPraiseNotify.getType(), "comment")
                        || TextUtils.equals(newPraiseNotify.getType(), "reply")) {

                    Intent intent = new Intent(VoteIndexActivity.this, VoteDetailsActivity.class);

                    intent.putExtra("emobId", userInfoDetailBean.getEmobId());
                    intent.putExtra("voteId", newPraiseNotify.getSourceId());
                    startActivity(intent);
                }


//                if(TextUtils.equals("reply", this.getType())){
//                    this.contentShow = nikeName+ "回复了你的投票评论";
//                }
//                else if(TextUtils.equals("vote",this.getType())){
//                    this.contentShow = nikeName+ "参与了你的投票";
//                }
//                else if(TextUtils.equals("election",this.getType())){
//                    this.contentShow = nikeName+ "对你投了一票";
//                }
//                else if(TextUtils.equals("comment",this.getType())){
//                    this.contentShow = nikeName+ "对你的投票发表了评论";
//                }


            }
        });

    }

    /**
     * 去帮主竞选页面
     */
    private void goBangzhuElection() {
        userInfoDetailBean = PreferencesUtil.getLoginInfo(getmContext());
        if (userInfoDetailBean == null) {
            startActivityForResult(new Intent(this, RegisterLoginActivity.class), 0);
        } else {
            startActivity(new Intent(getmContext(), RunForActivity.class));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

        pull_listview.post(new Runnable() {
            @Override
            public void run() {
                pageIndex = 0;
                getData();
            }
        });

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

        pull_listview.post(new Runnable() {
            @Override
            public void run() {

                if (pageIndex == 1) {
                    pull_listview.setSelection(pull_listview.getCount());
                }
                pageIndex++;
                getData();
            }
        });
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getIntent().getBooleanExtra(Config.INTENT_BACKMAIN, false)) {
                startActivity(new Intent(this, MainActivity.class));
            }
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    private void getData() {

        if (pageIndex == 0) {
            List<VoteIndexRespInfoBean> lifeCircleBeans = PreferencesUtil.getVoteBeanList(getmContext());
            if (lifeCircleBeans == null || lifeCircleBeans.isEmpty()) {
            } else {//显示缓存的
                map.clear();
                circleBeanList.clear();
                circleBeanList.addAll(lifeCircleBeans);

                for (int i = 0; i < circleBeanList.size(); i++) {

                    map.put(circleBeanList.get(i).getVoteId(), circleBeanList.get(i));
                }
                lifeMessageAdapter.notifyDataSetChanged();

                adapter.notifyDataSetChanged();
                pull_listview.setSelection(0);
            }
            pageIndex = 1;
        }

        userInfoDetailBean = PreferencesUtil.getLoginInfo(this);
        if (userInfoDetailBean != null) {
            getCircleTipsList(userInfoDetailBean.getEmobId());
        }

        getCircleLifeList();
        isVotesed();
//        getVotesList();

//        if (userInfoDetailBean != null) {
//            getCooperationStatus(userInfoDetailBean.getEmobId());
//        }


        //// 获取小区帮主竞选排名列表
        getHeaderRankList();

    }

    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onClick(View v) {
        userInfoDetailBean = PreferencesUtil.getLoginInfo(this);
        switch (v.getId()) {

            case R.id.ll_character_llay:
                startActivity(new Intent(this, RPValueTopListActivity.class));
                break;
            case R.id.iv_search:

//                startActivity(new Intent(this,LifeSearchActivity.class));
                startActivity(new Intent(this, SearchLifeCircle.class));
                break;

            case R.id.tv_right_text:
            case R.id.iv_right:

                userInfoDetailBean = PreferencesUtil.getLoginInfo(getmContext());
                if (userInfoDetailBean == null) {
                    startActivityForResult(new Intent(this, RegisterLoginActivity.class), 0);
                } else {
                    startActivity(new Intent(this, IHasLaunchedVotesActivity.class));
                }
                break;

            case R.id.ilaunch_vote_go_btn:
                userInfoDetailBean = PreferencesUtil.getLoginInfo(getmContext());
                if (userInfoDetailBean == null) {
                    startActivityForResult(new Intent(this, RegisterLoginActivity.class), 0);
                } else {
                    if (hasCreatedVoteToDay == IHAS_LAUNCHED_VOTE) {
                        Intent intent = new Intent();
                        intent.setClass(this, IwantVoteActivity.class);
                        startActivityForResult(intent, REQUEST_NEW_Provider);
                    } else if (hasCreatedVoteToDay == IHAS_LAUNCHED_VOTE_PULL_ERROR) {
                        showNetErrorToast();
                    } else {
                        showToast("每天只能发起一个投票");
                    }
                }

                break;
            case R.id.iv_back:
                if (getIntent().getBooleanExtra(Config.INTENT_BACKMAIN, false)) {
                    startActivity(new Intent(this, MainActivity.class));
                }
                finish();
                break;
            case R.id.iv_avatar:
                v.setClickable(false);
                if (userInfoDetailBean == null) {
                    startActivityForResult(new Intent(this, RegisterLoginActivity.class), 0);
                } else {
                    Intent intent = new Intent(this, MyLifeCircleActivity.class);
                    intent.putExtra(Config.Emobid, userInfoDetailBean.getEmobId());
                    startActivity(intent);
                }
                v.setClickable(true);
                break;
            case R.id.et_sendmessage:
                break;

            case R.id.ll_neterror:
                refreshData();
                break;

            default:
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_NEW_Provider) {
                pageIndex = 0;
                getData();
                return;
            }
//            PreferencesUtil.saveUnReadCircleCount(this, 0);

            PreferencesUtil.saveVoteIndexCount(this, 0);

            userInfoDetailBean = PreferencesUtil.getLoginInfo(this);

            if (userInfoDetailBean != null) {
                praiseNotifies.addAll(PreferencesUtil.getVoteDiscussInfoNotify(this, userInfoDetailBean.getEmobId()));
                getCircleTipsList(userInfoDetailBean.getEmobId());
            }
            initView();
        }

    }

    interface VoteIndexService {
        //// 获取投票的列表
//        @GET("/api/v1/communities/{communityId}/vote")
//        void getList(@Path("communityId") int communityId, @QueryMap HashMap<String, Object> option, Callback<VoteIndexRespBean> cb);

        /**
         * 分页获取小区内投票
         *
         * @param option
         * @param cb
         */
        @GET("/api/v3/votes")
        void getVotesList(@QueryMap HashMap<String, String> option, Callback<CommonRespBean<VoteIndexRespBean>> cb);

        /**
         * 判断用户今天是否已经发起过投票
         *
         * @param option
         * @param cb
         */
        @GET("/api/v3/votes/limited")
        void isVotesed(@QueryMap HashMap<String, String> option, Callback<CommonRespBean<Integer>> cb);

    }


    public void isVotesed() {
        userInfoDetailBean = PreferencesUtil.getLoginInfo(getmContext());
        HashMap<String, String> map = new HashMap<>();
        map.put("emobId", userInfoDetailBean != null ? userInfoDetailBean.getEmobId() : "");
        VoteIndexService service = RetrofitFactory.getInstance().create(getmContext(), map, VoteIndexService.class);
        Callback<CommonRespBean<Integer>> callback = new Callback<CommonRespBean<Integer>>() {
            @Override
            public void success(CommonRespBean<Integer> respone, Response response) {
                if ("yes".equals(respone.getStatus())) {
                    hasCreatedVoteToDay = respone.getData();
                } else {
                    hasCreatedVoteToDay = IHAS_LAUNCHED_VOTE_PULL_ERROR;
                }
            }

            @Override
            public void failure(RetrofitError error) {
                hasCreatedVoteToDay = IHAS_LAUNCHED_VOTE_PULL_ERROR;
                showNetErrorToast();
            }
        };
        service.isVotesed(map, callback);
    }

    public void getCircleLifeList() {
        userInfoDetailBean = PreferencesUtil.getLoginInfo(getmContext());
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("communityId", PreferencesUtil.getCommityId(this) + "");
        queryMap.put("page", pageIndex + "");
        queryMap.put("limit", 10 + "");
        queryMap.put("emobId", userInfoDetailBean != null ? userInfoDetailBean.getEmobId() : "");

        VoteIndexService service = RetrofitFactory.getInstance().create(getmContext(), queryMap, VoteIndexService.class);
        Callback<CommonRespBean<VoteIndexRespBean>> callback = new Callback<CommonRespBean<VoteIndexRespBean>>() {
            @Override
            public void success(CommonRespBean<VoteIndexRespBean> respone, Response response) {
                if (respone != null && "yes".equals(respone.getStatus()) && respone.getData() != null) {
                    ll_errorpage.setVisibility(View.GONE);
                    ll_index_empty_outer.setVisibility(View.GONE);
                    ll_neterror.setVisibility(View.GONE);
                    List<VoteIndexRespInfoBean> list = respone.getData().getData();
                    if (circleBeanList.size() > 0) {
                        if (list == null || list.size() <= 0) {
                            showNoMoreToast();
                        }
                    }
                    if (pageIndex == 1) {
                        circleBeanList.clear();
                        map.clear();
                        PreferencesUtil.saveVoteBeanList(getmContext(), list);

//                        String time = "" + System.currentTimeMillis() / 1000;
////                        Log.i("debbug","" + time);
//
//                        PreferencesUtil.saveLifeCircleCountTime(XjApplication.getInstance(), "" + time);//保存indexfragment页面刷新时间
                    }
                    circleBeanList.addAll(list);
                    for (int i = 0; i < list.size(); i++) {
                        map.put(list.get(i).getVoteId(), list.get(i));
                    }
                    lifeMessageAdapter.notifyDataSetChanged();
                    adapter.notifyDataSetChanged();
                } else {
                    showNetErrorToast();
                }
                lifeMessageAdapter.notifyDataSetChanged();
                if (circleBeanList != null && circleBeanList.size() == 0) {
                    ll_errorpage.setVisibility(View.GONE);
                    ll_index_empty_outer.setVisibility(View.VISIBLE);
                }
                if (pageIndex == 1) {
                    pull_to_refreshlayout.refreshFinish(true);
                } else {
                    pull_to_refreshlayout.loadMoreFinish(true);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (pageIndex == 1) {
                    pull_to_refreshlayout.refreshFinish(true);
                } else {
                    pull_to_refreshlayout.loadMoreFinish(true);
                }
                showNetErrorToast();
                if (circleBeanList == null || circleBeanList.isEmpty()) {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_neterror.setVisibility(View.VISIBLE);
                }
                error.printStackTrace();
            }
        };
        service.getVotesList(queryMap, callback);
    }


    interface CircleTipsListService {
        //        /api/v1/communities/1/vote/user/9f8445aa682e68962713f458b0285ebd/tips
        @GET("/api/v1/communities/{communityId}/vote/user/{emobId}/tips")
        void getListTips(@Path("communityId") int communityId, @Path("emobId") String emobId, Callback<VoteIndexDiscussInfoRespBean> cb);

        /**
         * 获取投票信息变化相关提示
         *
         * @param cb
         */
        @GET("/api/v3/votes/tips")
        void getListTipsV3(@QueryMap HashMap<String, String> option, Callback<CommonRespBean<List<VoteIndexDiscussInfoRespV3Bean>>> cb);


        /**
         * 获取投票首页帮主竞选列表
         *
         * @param option
         * @param cb
         */
        @GET("/api/v1/communities/{communityId}/election")
        void getHeaderRanklist(@QueryMap HashMap<String, Object> option, Callback<VoteHeaderRankListRespBean> cb);


        /**
         * 获取投票首页帮主竞选列表v3
         *
         * @param option
         * @param cb
         */
        @GET("/api/v3/elections/rank")
        void getHeaderRanklistV3(@QueryMap HashMap<String, String> option, Callback<CommonRespBean<RunForAllV3Bean>> cb);


        @GET("/api/v1/communities/{communityId}/cooperations/user/{emobId}/status")
        void getCooperationStatus(@Path("communityId") int communityId, @Path("emobId") String emobId, Callback<CooperationStatusRespBean> cb);

    }


    public void getHeaderRankList() {
        userInfoDetailBean = PreferencesUtil.getLoginInfo(getmContext());
        HashMap<String, String> option = new HashMap<>();
        option.put("emobId", userInfoDetailBean != null ? userInfoDetailBean.getEmobId() : "");
        option.put("page", 1 + "");
        option.put("limit", 10 + "");
        option.put("communityId", PreferencesUtil.getCommityId(this) + "");
        CircleTipsListService service = RetrofitFactory.getInstance().create(getmContext(), option, CircleTipsListService.class);
        Callback<CommonRespBean<RunForAllV3Bean>> callback = new Callback<CommonRespBean<RunForAllV3Bean>>() {
            @Override
            public void success(CommonRespBean<RunForAllV3Bean> respone, Response response) {
                if (respone != null && "yes".equals(respone.getStatus()) && respone.getData() != null && respone.getData().getData() != null) {
                    List<RunForAllV3Bean.RunForDataV3Bean> pageData = respone.getData().getData();
                    refreshHeaderRankList(pageData);
                } else {
                    // showToast("数据异常");
                    if (panic_has_purchase_llay != null) {
                        panic_has_purchase_llay.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                if (panic_has_purchase_llay != null) {
                    panic_has_purchase_llay.setVisibility(View.GONE);
                }
            }
        };

        service.getHeaderRanklistV3(option, callback);
    }


    private void refreshHeaderRankList(List<RunForAllV3Bean.RunForDataV3Bean> pageData) {

        if (pageData != null && pageData.size() > 0) {
            loadingGoodsHasGotursHeadImgs4(pageData);
        } else {
            if (panic_has_purchase_llay != null) {
                panic_has_purchase_llay.setVisibility(View.GONE);
            }
        }
    }

    public void getCircleTipsList(final String emobid) {
        HashMap<String, String> map = new HashMap();
        map.put("communityId", "" + PreferencesUtil.getCommityId(this));
        map.put("emobId", "" + emobid);
        CircleTipsListService service = RetrofitFactory.getInstance().create(getmContext(), map, CircleTipsListService.class);
        Callback<CommonRespBean<List<VoteIndexDiscussInfoRespV3Bean>>> callback = new Callback<CommonRespBean<List<VoteIndexDiscussInfoRespV3Bean>>>() {
            @Override
            public void success(CommonRespBean<List<VoteIndexDiscussInfoRespV3Bean>> respone, Response response) {
                if (respone != null && "yes".equals(respone.getStatus())) {
                    List<VoteIndexDiscussInfoRespV3Bean> tips = respone.getData();
                    if (tips != null && tips.size() > 0) {
                        for (int i = 0; i < tips.size(); i++) {
                            VoteIndexDiscussInfoRespV3Bean newPraiseNotify = tips.get(i);
                            newPraiseNotify.setContentShow(newPraiseNotify.getNickname()); /// 目的是为了适配XX评论了您..
                        }
                        praiseNotifies.addAll(tips);
                        PreferencesUtil.saveVoteDiscussInfoNotify(getmContext(), emobid, praiseNotifies);
//                        PreferencesUtil.saveCooperationNotify(getmContext(), emobid, praiseNotifies);
                        lifeMessageAdapter.notifyDataSetChanged();
                        adapter.notifyDataSetChanged();
                    }

                } else {

                    Log.d("getCircleTipsList ", " no ");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        };

        service.getListTipsV3(map, callback);
    }

    /**
     * 加载已经kan过的用户
     */
    private void loadingGoodsHasGotursHeadImgs4(final List<RunForAllV3Bean.RunForDataV3Bean> users) {

        if (users == null || users.size() <= 0) {
            if (panic_has_purchase_llay != null) {
                panic_has_purchase_llay.setVisibility(View.GONE);
            }
            return;
        }
        if (panic_has_purchase_llay != null) {
            panic_has_purchase_llay.setVisibility(View.VISIBLE);
        }
        Log.i("debbug", "info.size" + users.size());
        int userSize = users.size();
        int perwidth = screenWidth * 100 / 1080;
        final int shownum = 6;

        if (userSize > shownum) {
            welfare_purchase_hasgoturs_lv.removeAllViews();
            welfare_purchase_hasgoturs_lv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goBangzhuElection();
                }
            });
            for (int i = 0; i < shownum; i++) {
                LinearLayout usrHeadView = (LinearLayout) View.inflate(getmContext(), R.layout.common_vote_details_moreurs_headlay, null);
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
                welfare_purchase_hasgoturs_lv.addView(usrHeadView);
            }

            /// 添加一个查看更多用户按钮
            LinearLayout usrHeadView = (LinearLayout) View.inflate(getmContext(), R.layout.common_vote_details_moreurs_headlay, null);
            ImageView img = (ImageView) usrHeadView.findViewById(R.id.iv_avatar);
            img.setVisibility(View.VISIBLE);
            img.setImageResource(R.drawable.help_more_forvote);

//            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.help_more_forvote, img);

            TextView welfare_purchase_hasgoturs_name_tv = (TextView) usrHeadView.findViewById(R.id.welfare_purchase_hasgoturs_name_tv);
            //                welfare_purchase_hasgoturs_name_tv.setText(users.get(i).getNickname());
            welfare_purchase_hasgoturs_name_tv.setVisibility(View.GONE);

            usrHeadView.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams rlparams = (LinearLayout.LayoutParams) img.getLayoutParams();

            rlparams.width = perwidth;
            rlparams.height = perwidth;
            img.setLayoutParams(rlparams);
            welfare_purchase_hasgoturs_lv.addView(usrHeadView);
        }
        if (userSize > 0 && userSize <= shownum) {
            welfare_purchase_hasgoturs_lv.removeAllViews();
            welfare_purchase_hasgoturs_lv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goBangzhuElection();
                }
            });
            for (int i = 0; i < userSize; i++) {
                LinearLayout usrHeadView = (LinearLayout) View.inflate(getmContext(), R.layout.common_vote_details_moreurs_headlay, null);
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
                welfare_purchase_hasgoturs_lv.addView(usrHeadView);

            }
        }
    }

}
