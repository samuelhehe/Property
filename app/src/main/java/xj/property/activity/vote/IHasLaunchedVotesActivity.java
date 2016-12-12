package xj.property.activity.vote;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.repo.xw.library.views.PullListView;
import com.repo.xw.library.views.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.adapter.ILaunchedVoteMessageAdapter;
import xj.property.beans.IHasLaunchedVoteRespBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.CommonUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

/**
 * 我发过的投票 list
 */
public class IHasLaunchedVotesActivity extends HXBaseActivity implements PullToRefreshLayout.OnRefreshListener {

    UserInfoDetailBean userInfoDetailBean;

    List<IHasLaunchedVoteRespBean.PageDataEntity> circleBeanList = new ArrayList<>();

    TextView tv_right_text;

    ILaunchedVoteMessageAdapter lifeMessageAdapter;

    private DisplayImageOptions options;

    private LinearLayout ll_errorpage;

    private LinearLayout ll_neterror;
    private LinearLayout ll_nomessage;
    private ImageView iv_nomessage_image;
    private TextView tv_nomessage;
    private int count;
    int pageIndex = 1;


    private PullToRefreshLayout pull_to_refreshlayout;
    private PullListView pull_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_ilanuched_index);

        userInfoDetailBean = PreferencesUtil.getLoginInfo(this);

        initView();

//        refreshData();
        //// 自动下拉加载数据
        pull_to_refreshlayout.autoRefresh();

    }

    private void refreshData() {

        if (CommonUtils.isNetWorkConnected(this)) {
            getCircleLifeList();

        } else {
            ll_errorpage.setVisibility(View.VISIBLE);
            ll_neterror.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        userInfoDetailBean = PreferencesUtil.getLoginInfo(this);
    }


    private void initTitle() {

        findViewById(R.id.iv_back).setOnClickListener(this);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("我发过的投票");
        tv_right_text = (TextView) findViewById(R.id.tv_right_text);
        tv_right_text.setVisibility(View.INVISIBLE);
        tv_right_text.setOnClickListener(this);

    }

    private void initView() {

        initTitle();

        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        ll_nomessage = (LinearLayout) findViewById(R.id.ll_nomessage);
        iv_nomessage_image = (ImageView) findViewById(R.id.iv_nomessage_image);
        iv_nomessage_image.setImageResource(R.drawable.tikuanjilu_people);

        tv_nomessage = (TextView) findViewById(R.id.tv_nomessage);
        tv_nomessage.setText("暂时什么都没有哦，快来发布你的投票，看看你\n" +
                "可以为大家做什么，有了你的帮助一定可以有一个\n" +
                "更美好的小区。");

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


        lifeMessageAdapter = new ILaunchedVoteMessageAdapter(this, circleBeanList);

        pull_listview.setAdapter(lifeMessageAdapter);

    }


    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

        pull_listview.post(new Runnable() {
            @Override
            public void run() {
                pageIndex = 1;
                getCircleLifeList();
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
                getCircleLifeList();
            }
        });
    }

    @Override
    public void onClick(View v) {
        userInfoDetailBean = PreferencesUtil.getLoginInfo(this);
        switch (v.getId()) {

            case R.id.tv_right_text:
//                if (userInfoDetailBean == null) {
//                    startActivityForResult(new Intent(this, RegisterLoginActivity.class), 0);
//                } else {
//                    Intent intent = new Intent();
//                    intent.setClass(this, IwantProviderActivity.class);
//                    /// 如果用户发布过投票直接将投票ID传递,进行修改操作
//                    if (iProvideredCooperationId != -1) {
//                        intent.putExtra("iProvideredCooperationId", iProvideredCooperationId);
//                    }
//                    startActivityForResult(intent, REQUEST_NEW_Provider);
//                }

                break;
            case R.id.iv_back:
//                if (getIntent().getBooleanExtra(Config.INTENT_BACKMAIN, false)) {
//                    startActivity(new Intent(this, MainActivity.class));
//                }
                finish();
                break;

            case R.id.ll_neterror:
                refreshData();
                break;

            default:
        }
    }


    interface CircleLifeListService {
        //// 获取投票的列表

        //        /api1/communities/{communityId}/vote/myVote
//        @GET("/api/v1/communities/{communityId}/vote/myVote")
//        void getList(@Path("communityId") int communityId, @QueryMap HashMap<String, Object> option, Callback<IHasLaunchedVoteRespBean> cb);
//        @GET("/api/v1/communities/{communityId}/vote/myVote")

//        /api/v3/votes/mine?emobId={用户环信ID}&page={页码}&limit={页面大小}


        @GET("/api/v3/votes/mine")
        void getList(@QueryMap HashMap<String, String> option, Callback<CommonRespBean<IHasLaunchedVoteRespBean>> cb);
    }

    public void getCircleLifeList() {
        userInfoDetailBean = PreferencesUtil.getLoginInfo(getmContext());
        HashMap<String, String> map = new HashMap<>();
        map.put("emobId", userInfoDetailBean.getEmobId());
        map.put("page",""+ pageIndex);
        map.put("limit", ""+10);
        CircleLifeListService service = RetrofitFactory.getInstance().create(getmContext(), map, CircleLifeListService.class);
        Callback<CommonRespBean<IHasLaunchedVoteRespBean>> callback = new Callback<CommonRespBean<IHasLaunchedVoteRespBean>>() {
            @Override
            public void success(CommonRespBean<IHasLaunchedVoteRespBean> respone, Response response) {
                if (respone != null && "yes".equals(respone.getStatus())) {
                    ll_errorpage.setVisibility(View.GONE);
                    ll_nomessage.setVisibility(View.GONE);
                    ll_neterror.setVisibility(View.GONE);

                    List<IHasLaunchedVoteRespBean.PageDataEntity> list = respone.getData().getPageData();

                    if (circleBeanList.size() > 0 ){
                        if(list == null || list.size() <= 0){
                            showNoMoreToast();
                        }
                    }

                    if (pageIndex == 1) {
                        circleBeanList.clear();
                        pull_to_refreshlayout.refreshFinish(true);
                    }
                    circleBeanList.addAll(list);
                    pull_to_refreshlayout.loadMoreFinish(true);
                    lifeMessageAdapter.notifyDataSetChanged();

                    count = lifeMessageAdapter.getCount();


                } else {
                    showNetErrorToast();
                }
                if (count == 0) {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_nomessage.setVisibility(View.VISIBLE);
                }
                pull_to_refreshlayout.refreshFinish(true);
            }

            @Override
            public void failure(RetrofitError error) {
                pull_to_refreshlayout.refreshFinish(true);
                showNetErrorToast();
                if (count == 0) {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_neterror.setVisibility(View.VISIBLE);
                }
                error.printStackTrace();
            }
        };
//        emobId={用户环信ID}&page={页码}&limit={页面大小}
        service.getList(map, callback);
    }

}