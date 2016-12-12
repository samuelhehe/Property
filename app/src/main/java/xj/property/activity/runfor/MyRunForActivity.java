package xj.property.activity.runfor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.adapter.MyRunFormAdapter;
import xj.property.beans.RunForAllV3Bean;
import xj.property.beans.RunForScoreV3Bean;
import xj.property.netbase.CommonRespBean;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.CircleImageView;

/**
 * aurth:asia
 * 我的竞选页面
 */
public class MyRunForActivity extends HXBaseActivity implements PullToRefreshLayout.OnRefreshListener{

    //刷新变量
    private int ListViewStateType;
    private final int LOADMORE = 1;
    private final int REFRESH = 2;
    //变量
    private int mPageNo = 1;
    private int mPageNum = 10;
    private boolean mFirstLoad = true;//是否是第一次加载

    private ImageView bannerImg;
    private MyRunFormAdapter mActivityRunForAdapter;
    private PullToRefreshLayout mRefreshLayout;
    private PullListView mPullListView;
    private RunForAllV3Bean.RunForDataV3Bean mRunForBean;

    //titile
    private RelativeLayout mHeaptop;
    private ImageView mIv_back;
    private TextView mTv_left_text;
    private TextView mTv_title;
    private TextView mTv_right_text;
    private ImageView mIv_right;

    //mymessage
    private xj.property.widget.CircleImageView mAvatar;
    private TextView mTv_name;
    private TextView mTv_score;
    private TextView mTv_num;
    private int communityId = 1;

    private List<RunForScoreV3Bean.RunForScoreV3DataBean> mRunForScoreBeanList = new ArrayList<RunForScoreV3Bean.RunForScoreV3DataBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrunfor);
        mRunForBean=(RunForAllV3Bean.RunForDataV3Bean)getIntent().getSerializableExtra("runForBean");
        communityId = PreferencesUtil.getCommityId(this);
        initView();
        initData();
//        getScoreDetails();
        initListenner();
    }

    private void initListenner() {
        mIv_back.setOnClickListener(this);
    }

    private void initData() {
        if(mRunForBean!=null){
            ImageLoader.getInstance().displayImage(mRunForBean.getAvatar(), mAvatar, options);
            mTv_name.setText(mRunForBean.getNickname());
            mTv_score.setText("本月积分:"+mRunForBean.getScore()+"分");
            mTv_num.setText(mRunForBean.getRank()+"");
            mTv_title.setText(mRunForBean.getNickname()+"本月得分详情");
        }
        getMyScoreMessage();
    }

    private void initList(List<RunForScoreV3Bean.RunForScoreV3DataBean> myBean) {
        mActivityRunForAdapter = new MyRunFormAdapter(this,myBean);
        mPullListView.setAdapter(mActivityRunForAdapter);
        mRefreshLayout.setOnRefreshListener(this);
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
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        mPullListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPageNo = 1;
                ListViewStateType = REFRESH;
                getMyScoreMessage();
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
                getMyScoreMessage();
            }
        }, 40);

    }

    /**
     * 获取ActList部分
     */
    interface scoreDetailsService {
        @GET("/api/v3/elections")
        void getMyScoreMessage(@QueryMap Map<String, String> map, Callback<CommonRespBean<RunForScoreV3Bean>> cb);
    }

    private void getMyScoreMessage() {
        mLdDialog.show();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        scoreDetailsService service = restAdapter.create(scoreDetailsService.class);
        Callback<CommonRespBean<RunForScoreV3Bean>> callback = new Callback<CommonRespBean<RunForScoreV3Bean>>() {
            @Override
            public void success(CommonRespBean<RunForScoreV3Bean> bean, retrofit.client.Response response) {
                mLdDialog.dismiss();
                Log.d("MyRunForActivity",response.toString());
                if (bean != null) {
                    if("yes".equals(bean.getStatus())){
                        RunForScoreV3Bean info = bean.getData();
                        List<RunForScoreV3Bean.RunForScoreV3DataBean> myBean = info.getData();
                        mRunForScoreBeanList.clear();
                        mRunForScoreBeanList.addAll(myBean);
                        if(mFirstLoad){
                            initList(myBean);
                            mFirstLoad =false;
                        }else if(ListViewStateType==LOADMORE){
                            mRefreshLayout.loadMoreFinish(true);
                            mActivityRunForAdapter.LoadMoreRefresh(mRunForScoreBeanList);
                        }else{
                            mRefreshLayout.refreshFinish(true);
                            mActivityRunForAdapter.ChangeRefresh(mRunForScoreBeanList);
                        }

                    }else {

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
        Map<String, String> option = new HashMap<String, String>();
        option.put("emobId", mRunForBean.getEmobId());
        option.put("page", mPageNo+"");
        option.put("limit", mPageNum+"");
        option.put("communityId", communityId+"");
        service.getMyScoreMessage( option, callback);
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
