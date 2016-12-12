package xj.property.activity.property;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
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
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.adapter.PropertyHistoryAdapter;
import xj.property.beans.PropertyPayHistoryBean;
import xj.property.beans.PropertyPayHistoryInfoBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.other.PreferencesUtil;

/**
 * aurth:asia
 * 缴费历史页面
 */
public class PropertyHistoryActivity extends HXBaseActivity implements PullToRefreshLayout.OnRefreshListener{

    //刷新变量
    private int ListViewStateType;
    private final int LOADMORE = 1;
    private final int REFRESH = 2;

    private int mPageNo = 1;
    private int mPageNum = 10;
    private boolean mFirstLoad = true;//是否是第一次加

    private List<PropertyPayHistoryBean> mPropertyHistoryList = new ArrayList<PropertyPayHistoryBean>();
    private PullToRefreshLayout mRefreshLayout;
    private PullListView mPullListView;

    //titile
    private RelativeLayout mHeaptop;
    private ImageView mIv_back;
    private TextView mTv_left_text;
    private TextView mTv_title;
    private TextView mTv_right_text;
    private ImageView mIv_right;

    private String emobId= "9a86c7273e9e3f7ae3fb1fc24c0a2a2a";
    private long communityId = 1;

    private PropertyHistoryAdapter mPropertyHistoryAdapter;
    private int currentPageCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_bill);
        UserInfoDetailBean bean = PreferencesUtil.getLoginInfo(PropertyHistoryActivity.this);
        emobId = bean.getEmobId();
        communityId = PreferencesUtil.getCommityId(this);
        initView();
        initDate();
        initListenner();
    }

    private void initDate() {
        mTv_title.setText("历史账单");
        getPropertyHistory();
    }

    private void initListenner() {
        mIv_back.setOnClickListener(this);
        mTv_right_text.setOnClickListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right_text:
                //TODO 跳入到历史说明界面
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    public void initView() {
        mHeaptop = (RelativeLayout) findViewById(R.id.heaptop);
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mTv_left_text = (TextView) findViewById(R.id.tv_left_text);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mTv_right_text = (TextView) findViewById(R.id.tv_right_text);
        mIv_right = (ImageView) findViewById(R.id.iv_right);

        mRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pullToRefreshLayout);
        mPullListView = (PullListView) findViewById(R.id.pullListView);
        mPullListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (mPullListView.getLastVisiblePosition() == (mPullListView.getCount() - 1)) {
                            mRefreshLayout.autoLoad();
                        }
                        // 判断滚动到顶部
                        if (mPullListView.getFirstVisiblePosition() == 0) {

                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });


    }

    private void initListView(List<PropertyPayHistoryBean> propertyPayHistoryBean){
        mPropertyHistoryAdapter = new PropertyHistoryAdapter(this,propertyPayHistoryBean);
        mPullListView.setAdapter(mPropertyHistoryAdapter);
        mRefreshLayout.setOnRefreshListener(this);
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
                getPropertyHistory();
            }
        }, 500);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        mPullListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPageNo++;
                ListViewStateType = LOADMORE;
                getPropertyHistory();
            }
        }, 500);
    }

    /**
     * 获取需要的缴费
     */
    interface ActPropertyHistory{
//        @GET("/api/v1/communities/{communityId}/payment/user/{emobId}/paymentsInfos")


//        /api/v3/payments/page?communityId=2&emobId=d463b16dfc014466a1e441dd685ba505&page=1&limit=10


        @GET("/api/v3/payments/page")
        void getPropertyHistory(@QueryMap Map<String, String> map, Callback<CommonRespBean<PropertyPayHistoryInfoBean>> cb);
    }

    private void getPropertyHistory() {
        mLdDialog.show();
        HashMap<String, String> option = new HashMap<String, String>();
        option.put("page", mPageNo+"");
        option.put("limit", mPageNum+"");
        option.put("communityId", ""+communityId);
        option.put("emobId", ""+emobId);
        ///communityId=2&emobId=d463b16dfc014466a1e441dd685ba505&page=1&limit=10
        //第一个参数是小区id  第二个是个人id
        ActPropertyHistory service = RetrofitFactory.getInstance().create(getmContext(),option,ActPropertyHistory.class);
        Callback<CommonRespBean<PropertyPayHistoryInfoBean>> callback = new Callback<CommonRespBean<PropertyPayHistoryInfoBean>>() {
            @Override
            public void success(CommonRespBean<PropertyPayHistoryInfoBean> bean, retrofit.client.Response response) {
                mLdDialog.dismiss();
                if (bean != null) {
                    if("yes".equals(bean.getStatus())){
                        PropertyPayHistoryInfoBean myBean = bean.getData();
                        List<PropertyPayHistoryBean> list = myBean.getData();

                        if(list!=null&&list.size()>0){
                            currentPageCount = list.size();
                        }
                        if(mPageNo>1 &&currentPageCount<mPageNum){
                            showNoMoreToast();
                        }
                        mPropertyHistoryList.clear();
                        mPropertyHistoryList.addAll(list);
                        if(mFirstLoad){
                            initListView(list);
                            mFirstLoad =false;
                        }else if(ListViewStateType==LOADMORE){
                            mRefreshLayout.loadMoreFinish(true);
                            mPropertyHistoryAdapter.LoadMoreRefresh(mPropertyHistoryList);
                        }else{
                            mRefreshLayout.refreshFinish(true);
                            mPropertyHistoryAdapter.ChangeRefresh(mPropertyHistoryList);
                        }
                    }else{
                        mRefreshLayout.loadMoreFinish(true);
                        mRefreshLayout.refreshFinish(true);
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
        service.getPropertyHistory(option, callback);
    }

}
