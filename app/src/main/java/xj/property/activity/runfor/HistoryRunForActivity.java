package xj.property.activity.runfor;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.repo.xw.library.views.PullListView;
import com.repo.xw.library.views.PullToRefreshLayout;

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
import xj.property.adapter.MyRunForHistoryAdapter;
import xj.property.beans.RunForScoreHistoryAllBean;
import xj.property.beans.RunForScoreHistoryV3Bean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.CircleImageView;

/**
 * aurth:asia
 * 竞选历史页面
 * v3 2016/03/16
 */
public class HistoryRunForActivity extends HXBaseActivity {

    //刷新变量
    private String mEmobid="d463b16dfc014466a1e441dd685ba505";
    private String mSearchTime;

    private MyRunForHistoryAdapter mMyRunForHistoryAdapter;
    private PullToRefreshLayout mRefreshLayout;
    private PullListView mPullListView;

    //titile
    private RelativeLayout mHeaptop;
    private ImageView mIv_back;
    private TextView mTv_left_text;
    private TextView mTv_title;
    private TextView mTv_right_text;
    private ImageView mIv_right;

    //mymessage
    private CircleImageView mAvatar;
    private TextView mTv_name;
    private TextView mTv_score;
    private TextView mTv_num;
    private long communityId = 2;

    //header
    private LinearLayout mLl_banner1;
    private LinearLayout mLl_banner2;
    private CircleImageView mAvatar_fubangzhu1;
    private CircleImageView mAvatar_bangzhu;
    private CircleImageView mAvatar_fubangzhu2;
    private CircleImageView mAvatar_tangzhu1;
    private CircleImageView mAvatar_tangzhu2;
    private CircleImageView mAvatar_tangzhu3;
    private CircleImageView mAvatar_tangzhu4;
    private CircleImageView mAvatar_tangzhu5;
    private CircleImageView mAvatar_tangzhu6;

    private LinearLayout mLl_tangzhu1;
    private LinearLayout mLl_tangzhu2;
    private LinearLayout mLl_tangzhu3;
    private LinearLayout mLl_tangzhu4;
    private LinearLayout mLl_tangzhu5;
    private LinearLayout mLl_tangzhu6;

    private LinearLayout mLl_bangzhu1;
    private LinearLayout mLl_bangzhu2;
    private LinearLayout mLl_bangzhu3;

    private List<RunForScoreHistoryV3Bean.RunForScoreHistoryDataV3Bean> mRunForScoreBeanList = new ArrayList<RunForScoreHistoryV3Bean.RunForScoreHistoryDataV3Bean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrunfor);
        UserInfoDetailBean bean = PreferencesUtil.getLoginInfo(HistoryRunForActivity.this);
        mSearchTime = getIntent().getStringExtra("searchTime");
        communityId = PreferencesUtil.getCommityId(this);
        if (bean != null) {
            mEmobid = bean.getEmobId();
        }
        initView();
        initDate();
        initListenner();
    }

    private void initListenner() {
        mIv_back.setOnClickListener(this);
    }

    private void initDate() {
        mLl_banner1.setVisibility(View.VISIBLE);
        mLl_banner2.setVisibility(View.VISIBLE);
//        getHistoryScoreDetails();
        getHistoryActListV3();
    }

    private void initList(List<RunForScoreHistoryV3Bean.RunForScoreHistoryDataV3Bean> myBean) {
        mMyRunForHistoryAdapter = new MyRunForHistoryAdapter(this, myBean);
        mPullListView.setAdapter(mMyRunForHistoryAdapter);
        mPullListView.setPullUpEnable(false);
        mPullListView.setPullDownEnable(false);
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
            case R.id.iv_right:
                break;
        }
    }

    public void initView() {
        mRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pullToRefreshLayout);
        mPullListView = (PullListView) findViewById(R.id.pullListView);

        mHeaptop = (RelativeLayout) findViewById(R.id.heaptop);
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mTv_left_text = (TextView) findViewById(R.id.tv_left_text);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mTv_right_text = (TextView) findViewById(R.id.tv_right_text);
        mIv_right = (ImageView) findViewById(R.id.iv_right);

        mAvatar = (CircleImageView) findViewById(R.id.avatar);
        mTv_name = (TextView) findViewById(R.id.tv_name);
        mTv_score = (TextView) findViewById(R.id.tv_score);
        mTv_num = (TextView) findViewById(R.id.tv_num);

        mLl_banner1 = (LinearLayout) findViewById(R.id.ll_banner1);
        mLl_banner2 = (LinearLayout) findViewById(R.id.ll_banner2);
        mAvatar_fubangzhu1 = (CircleImageView) findViewById(R.id.avatar_fubangzhu1);
        mAvatar_fubangzhu2 = (CircleImageView) findViewById(R.id.avatar_fubangzhu2);
        mAvatar_bangzhu = (CircleImageView) findViewById(R.id.avatar_bangzhu);
        mAvatar_tangzhu1 = (CircleImageView) findViewById(R.id.avatar_tangzhu1);
        mAvatar_tangzhu2 = (CircleImageView) findViewById(R.id.avatar_tangzhu2);
        mAvatar_tangzhu3 = (CircleImageView) findViewById(R.id.avatar_tangzhu3);
        mAvatar_tangzhu4 = (CircleImageView) findViewById(R.id.avatar_tangzhu4);
        mAvatar_tangzhu5 = (CircleImageView) findViewById(R.id.avatar_tangzhu5);
        mAvatar_tangzhu6 = (CircleImageView) findViewById(R.id.avatar_tangzhu6);

        mLl_tangzhu1 = (LinearLayout) findViewById(R.id.ll_tangzhu1);
        mLl_tangzhu2 = (LinearLayout) findViewById(R.id.ll_tangzhu2);
        mLl_tangzhu3 = (LinearLayout) findViewById(R.id.ll_tangzhu3);
        mLl_tangzhu4 = (LinearLayout) findViewById(R.id.ll_tangzhu4);
        mLl_tangzhu5 = (LinearLayout) findViewById(R.id.ll_tangzhu5);
        mLl_tangzhu6 = (LinearLayout) findViewById(R.id.ll_tangzhu6);

        mLl_bangzhu1 = (LinearLayout) findViewById(R.id.ll_bangzhu1);
        mLl_bangzhu2 = (LinearLayout) findViewById(R.id.ll_bangzhu2);
        mLl_bangzhu3 = (LinearLayout) findViewById(R.id.ll_bangzhu3);
    }


    /**
     * 获取ActList部分
     */
    interface scoreDetailsService {
        @GET("/api/v1/communities/{communityId}/election/detail")
        void getHistoryActList(@Path("communityId") long communityId, @QueryMap Map<String, String> map, Callback<RunForScoreHistoryAllBean> cb);

//        /api/v3/elections/rank/month
        @GET("/api/v3/elections/rank/month")
        void getHistoryActListV3(@QueryMap HashMap<String, String> map, Callback<CommonRespBean<RunForScoreHistoryV3Bean>> cb);
    }

    private void getHistoryActListV3() {
        mLdDialog.show();

        HashMap<String, String> option = new HashMap<String, String>();
        option.put("emobId", mEmobid);
        option.put("communityId", mEmobid);
        option.put("time", mSearchTime + ""); /// 指定月份的UNIX时间戳
//        /api/v3/elections/rank/month?communityId={小区ID}&emobId={用户环信ID}&time={指定月份的UNIX时间戳}

        scoreDetailsService service = RetrofitFactory.getInstance().create(getmContext(), option, scoreDetailsService.class);
        Callback<CommonRespBean<RunForScoreHistoryV3Bean>> callback = new Callback<CommonRespBean<RunForScoreHistoryV3Bean>>() {
            @Override
            public void success(CommonRespBean<RunForScoreHistoryV3Bean> bean, retrofit.client.Response response) {
                mLdDialog.dismiss();
                Log.d("MyRunForActivity", response.toString());
                if (bean != null) {
                    if ("yes".equals(bean.getStatus())) {
                        //listiew
                        RunForScoreHistoryV3Bean info = bean.getData();
                        List<RunForScoreHistoryV3Bean.RunForScoreHistoryDataV3Bean> myBean = info.getRank();
                        mRunForScoreBeanList.clear();
                        mRunForScoreBeanList.addAll(myBean);
                        initList(myBean);
                        initBanner(myBean);

                        //title
                        RunForScoreHistoryV3Bean.RunForScoreHistoryDataV3Bean runForHistoryBean = info.getMine();
                        if (runForHistoryBean != null) {
                            ImageLoader.getInstance().displayImage(runForHistoryBean.getAvatar(), mAvatar, options);
                            mTv_name.setText(runForHistoryBean.getNickname());
                            mTv_score.setText("本月积分:" + runForHistoryBean.getScore() + "分");
                            mTv_num.setText(runForHistoryBean.getRank() + "");
                            mTv_title.setText("新任帮主（班底）上任数据");
                        }
                    } else {

                    }

                }
            }


            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        service.getHistoryActListV3(option, callback);
    }

//    private void getHistoryScoreDetails() {
//        mLdDialog.show();
//        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
//        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
//        scoreDetailsService service = restAdapter.create(scoreDetailsService.class);
//        Callback<RunForScoreHistoryAllBean> callback = new Callback<RunForScoreHistoryAllBean>() {
//            @Override
//            public void success(RunForScoreHistoryAllBean bean, retrofit.client.Response response) {
//                mLdDialog.dismiss();
//                Log.d("MyRunForActivity", response.toString());
//                if (bean != null) {
//                    if ("yes".equals(bean.getStatus())) {
//                        //listiew
//                        RunForScoreHistoryInfoBean info = bean.getInfo();
//                        List<RunForHistoryBean> myBean = info.getRankList();
//                        mRunForScoreBeanList.clear();
//                        mRunForScoreBeanList.addAll(myBean);
//                        initList(myBean);
//                        initBanner(myBean);
//                        //title
//                        RunForHistoryBean runForHistoryBean = info.getMyselfRank();
//                        if (runForHistoryBean != null) {
//                            ImageLoader.getInstance().displayImage(runForHistoryBean.getAvatar(), mAvatar, options);
//                            mTv_name.setText(runForHistoryBean.getNickname());
//                            mTv_score.setText("本月积分:" + runForHistoryBean.getScore() + "分");
//                            mTv_num.setText(runForHistoryBean.getRank() + "");
//                            mTv_title.setText("新任帮主（班底）上任数据");
//                        }
//                    } else {
//
//                    }
//
//                }
//            }
//
//
//            @Override
//            public void failure(RetrofitError error) {
//                mLdDialog.dismiss();
//                error.printStackTrace();
//                showNetErrorToast();
//            }
//        };
//        Map<String, String> option = new HashMap<String, String>();
//        option.put("emobId", mEmobid);
//        option.put("time", mSearchTime + "");
//        service.getHistoryActList(communityId, option, callback);
//    }

    private void initBanner(List<RunForScoreHistoryV3Bean.RunForScoreHistoryDataV3Bean> myBean) {
        if (null != myBean) {
            if (myBean.size() > 0) {
                ImageLoader.getInstance().displayImage(myBean.get(0).getAvatar(), mAvatar_bangzhu, options);
            } else {
                mLl_bangzhu2.setVisibility(View.GONE);
            }
            if (myBean.size() > 1) {
                ImageLoader.getInstance().displayImage(myBean.get(1).getAvatar(), mAvatar_fubangzhu1, options);
            } else {
                mLl_bangzhu1.setVisibility(View.GONE);
            }
            if (myBean.size() > 2) {
                ImageLoader.getInstance().displayImage(myBean.get(2).getAvatar(), mAvatar_fubangzhu2, options);
            } else {
                mLl_bangzhu3.setVisibility(View.GONE);
            }
            if (myBean.size() > 3) {
                ImageLoader.getInstance().displayImage(myBean.get(3).getAvatar(), mAvatar_tangzhu1, options);
            } else {
                mLl_tangzhu1.setVisibility(View.GONE);
            }
            if (myBean.size() > 4) {
                ImageLoader.getInstance().displayImage(myBean.get(4).getAvatar(), mAvatar_tangzhu2, options);
            } else {
                mLl_tangzhu2.setVisibility(View.GONE);
            }
            if (myBean.size() > 5) {
                ImageLoader.getInstance().displayImage(myBean.get(5).getAvatar(), mAvatar_tangzhu3, options);
            } else {
                mLl_tangzhu3.setVisibility(View.GONE);
            }
            if (myBean.size() > 6) {
                ImageLoader.getInstance().displayImage(myBean.get(6).getAvatar(), mAvatar_tangzhu4, options);
            } else {
                mLl_tangzhu4.setVisibility(View.GONE);
            }
            if (myBean.size() > 7) {
                ImageLoader.getInstance().displayImage(myBean.get(7).getAvatar(), mAvatar_tangzhu5, options);
            } else {
                mLl_tangzhu5.setVisibility(View.GONE);
            }
            if (myBean.size() > 8) {
                ImageLoader.getInstance().displayImage(myBean.get(8).getAvatar(), mAvatar_tangzhu6, options);
            } else {
                mLl_tangzhu6.setVisibility(View.GONE);
            }
        }

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
