package xj.property.activity.genius;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import xj.property.adapter.GeniusSpecialAdapter;
import xj.property.beans.GeniusBean;
import xj.property.beans.GeniusInfoBean;
import xj.property.beans.RunForBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.other.PreferencesUtil;

/**
 * aurth:asia
 * 我的牛人列表
 */
public class GeniusSpecialActivity extends HXBaseActivity implements PullToRefreshLayout.OnRefreshListener {

    private String mTag = "GeniusSpecialActivity";

    //刷新变量
    private int ListViewStateType;
    private final int LOADMORE = 1;
    private final int REFRESH = 2;
    //变量
    private int mPageNo = 1;
    private int mPageNum = 10;
    private boolean mFirstLoad = true;//是否是第一次加载

    private ImageView bannerImg;
    private GeniusSpecialAdapter mGeniusSpecialAdapter;
    private PullToRefreshLayout mRefreshLayout;
    private PullListView mPullListView;
    private RunForBean mRunForBean;

    //titile
    private RelativeLayout mHeaptop;
    private ImageView mIv_back;
    private TextView mTv_left_text;
    private TextView mTv_title;
    private TextView mTv_right_text;
    private TextView mTv_count;
    private ImageView mIv_right;

    private int communityId;

    private List<GeniusBean> mRunForScoreBeanList = new ArrayList<GeniusBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genius_special);
        mRunForBean = (RunForBean) getIntent().getSerializableExtra("runForBean");
        communityId = PreferencesUtil.getCommityId(this);
        initView();
        initDate();
//        getGeniusList();
        initListenner();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getGeniusList();

    }

    private void initListenner() {
        mIv_back.setOnClickListener(this);
        mTv_right_text.setOnClickListener(this);
    }

    private void initDate() {
        mTv_title.setText("小区牛人");
        mTv_title.setTextColor(0xff2FCC71);
        ((ImageView) findViewById(R.id.iv_right)).setBackgroundDrawable(getResources().getDrawable(R.drawable.amend_icon));
        findViewById(R.id.iv_right).setOnClickListener(this);
    }

    private void initList(List<GeniusBean> myBean) {
        mGeniusSpecialAdapter = new GeniusSpecialAdapter(this, myBean);
        mPullListView.setAdapter(mGeniusSpecialAdapter);
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
                Intent intent = new Intent(GeniusSpecialActivity.this, GeniusApplyActivity.class);
                startActivity(intent);
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
        mTv_count = (TextView) findViewById(R.id.tv_count);
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
                getGeniusList();
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
                getGeniusList();
            }
        }, 40);

    }

    /**
     * 获取ActList部分
     */
    interface scoreDetailsService {
        //        @GET("/api/v1/communities/{communityId}/famousPerson")

        //
//        @GET("/api/v1/communities/{communityId}/famousPerson")
//        void getActList(@Path("communityId") int communityId, @QueryMap Map<String, String> map, Callback<CommonRespBean<GeniusInfoBean>> cb);

        @GET("/api/v3/communities/{communityId}/users/niuren")
        void getActListV3(@Path("communityId") int communityId, @QueryMap Map<String, String> map, Callback<CommonRespBean<GeniusInfoBean>> cb);
    }

    private void getGeniusList() {

        HashMap<String, String> option = new HashMap<String, String>();
        option.put("page", mPageNo + "");
        option.put("limit", mPageNum + "");

        //page=1&limit=10
        scoreDetailsService service = RetrofitFactory.getInstance().create(getmContext(),option,scoreDetailsService.class);
        Callback<CommonRespBean<GeniusInfoBean>> callback = new Callback<CommonRespBean<GeniusInfoBean>>() {
            @Override
            public void success(CommonRespBean<GeniusInfoBean> bean, retrofit.client.Response response) {
                Log.d("MyRunForActivity", response.toString());
                if (bean != null) {
                    if ("yes".equals(bean.getStatus()) && bean.getData() != null) {
                        GeniusInfoBean info = bean.getData();
                        List<GeniusBean> myBean = info.getData();
                        mRunForScoreBeanList.clear();
                        mRunForScoreBeanList.addAll(myBean);
                        if (mFirstLoad) {
                            initList(myBean);
//  2016/02/26 接口问题,                          mTv_count.setText("恭喜你进入小区牛人，咱们小区共有" + info.getRowCount() + "位邻居展现了他的牛人信息。快快和牛人做朋友吧。");
                            mFirstLoad = false;
                        } else if (ListViewStateType == LOADMORE) {
                            mRefreshLayout.loadMoreFinish(true);
                            mGeniusSpecialAdapter.LoadMoreRefresh(mRunForScoreBeanList);
                        } else {
                            mRefreshLayout.refreshFinish(true);
                            mGeniusSpecialAdapter.ChangeRefresh(mRunForScoreBeanList);
                        }

                    } else {
                        Log.d(mTag, "getGeniusList==========null");
                    }

                } else {
                    Log.d(mTag, "getGeniusList==========null");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
            }
        };

        service.getActListV3(communityId, option, callback);
    }

}
