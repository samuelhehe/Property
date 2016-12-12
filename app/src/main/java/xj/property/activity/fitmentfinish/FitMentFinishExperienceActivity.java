package xj.property.activity.fitmentfinish;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.activeandroid.util.Log;
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
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.adapter.FitmentFinishExperienceAdapter;
import xj.property.beans.FitmentFinishCompany;
import xj.property.beans.FitmentFinishCompanyData;
import xj.property.beans.FitmentFinishCompanyInfo;
import xj.property.utils.other.Config;


/**
 * 作者：asia on 2015/12/11 11:56
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class FitMentFinishExperienceActivity extends HXBaseActivity implements PullToRefreshLayout.OnRefreshListener{

    private String mTag="FitMentFinishExperienceActivity";

    //刷新变量
    private int ListViewStateType;
    private final int LOADMORE = 1;
    private final int REFRESH = 2;
    //变量
    private boolean mFirstLoad = true;//是否是第一次加载
    private List<FitmentFinishCompanyData> mFitmentFinishList= new ArrayList<FitmentFinishCompanyData>();
    private int mPageNum=1;
    private int mPageSize=10;
    private int communityId=1;

    private PullToRefreshLayout mRefreshLayout;
    private PullListView mPullListView;
    private FitmentFinishExperienceAdapter mFitmentFinishExperienceAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitment_scheme);
        initTitle();
        initView();
        initData();
    }

    private void initData() {
        getFitmentFinish();
    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        this.findViewById(R.id.iv_back).setOnClickListener(this);
       ((TextView) this.findViewById(R.id.tv_title)).setText("超级装修免费预划案");
    }

    private void initView() {
        mRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pullToRefreshLayout);
        mPullListView = (PullListView) findViewById(R.id.pullListView);
    }

    private void initListView(List<FitmentFinishCompanyData> fitmentFinishList){
        mFitmentFinishExperienceAdapter = new FitmentFinishExperienceAdapter(this,fitmentFinishList);
        mPullListView.setAdapter(mFitmentFinishExperienceAdapter);
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_getagain:
                initData();
                break;

            case R.id.iv_back:
                finish();
                break;
        }

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        mPullListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPageNum = 1;
                ListViewStateType = REFRESH;
                getFitmentFinish();
            }
        }, 40);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        mPullListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPageNum++;
                ListViewStateType = LOADMORE;
                getFitmentFinish();
            }
        }, 40);
    }

    interface FitmentFinishService{
        @GET("/api/v1/decoration/communities/{communityId}")
        void getFitmentFinish(@Path("communityId") int communityId, @QueryMap Map<String, String> map, Callback<FitmentFinishCompany> cb);
    }

    private void getFitmentFinish(){

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        FitmentFinishService service = restAdapter.create(FitmentFinishService.class);
        Callback<FitmentFinishCompany> callback = new Callback<FitmentFinishCompany>() {
            @Override
            public void success(FitmentFinishCompany bean, retrofit.client.Response response) {
                if (bean != null) {
                    if("yes".equals(bean.getStatus())&&bean.getInfo()!=null){
                        FitmentFinishCompanyInfo info= bean.getInfo();
                        List<FitmentFinishCompanyData> list = info.getPageData();
                        mFitmentFinishList.clear();
                        mFitmentFinishList.addAll(list);
                        if(mFirstLoad){
                            list.add(0,new FitmentFinishCompanyData());
                            initListView(list);
                            mFirstLoad =false;
                        }else if(ListViewStateType==LOADMORE){
                            mRefreshLayout.loadMoreFinish(true);
                            mFitmentFinishExperienceAdapter.LoadMoreRefresh(mFitmentFinishList);
                        }else{
                            list.add(0,new FitmentFinishCompanyData());
                            mRefreshLayout.refreshFinish(true);
                            mFitmentFinishExperienceAdapter.ChangeRefresh(list);
                        }
                    }else if("no".equals(bean.getStatus())){
                        Log.d(mTag, "getFitmentFinish  ==========   null");
                    }else {
                        Log.d(mTag, "getFitmentFinish  ==========   null");
                    }

                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        Map<String, String> option = new HashMap<String, String>();
        option.put("pageNum", mPageNum+"");
        option.put("pageSize", mPageSize+"");
        service.getFitmentFinish(communityId, option, callback);
    }
}
