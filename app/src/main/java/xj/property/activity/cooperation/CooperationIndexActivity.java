package xj.property.activity.cooperation;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.repo.xw.library.views.PullListView;
import com.repo.xw.library.views.PullToRefreshLayout;
import com.umeng.socialize.sso.UMSsoHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import xj.property.adapter.CooperationIndexMessageAdapter;
import xj.property.adapter.XJBaseAdapter;
import xj.property.beans.CooperationIndexRespBean;
import xj.property.beans.CooperationPraiseDiscussNotify;
import xj.property.beans.NeighborListV3Bean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.provider.ShareProvider;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

/**
 * Created by Administrator on 2015/6/8.
 */
public class CooperationIndexActivity extends HXBaseActivity implements PullToRefreshLayout.OnRefreshListener {

    final static int REQUEST_NEW_Provider = 778;
    public ListView lv_eva;

//    ImageView avatar;


    UserInfoDetailBean userInfoDetailBean;

    HashMap<Integer, NeighborListV3Bean.NeighborListData> map = new HashMap<>();

    List<NeighborListV3Bean.NeighborListData> circleBeanList = new ArrayList<NeighborListV3Bean.NeighborListData>();

    List<CooperationPraiseDiscussNotify> praiseNotifies = new ArrayList<>();

    TextView tv_right_text;

    CooperationIndexMessageAdapter lifeMessageAdapter;

    XJBaseAdapter adapter;

    View headView;

    int pageIndex = 0;
    int pageSize = 10;

    private DisplayImageOptions options;
    //// 如果发布过邻居帮,则返回cooperationID 邻居帮ID
    private int iProvideredCooperationId = -1; ///
    private LinearLayout ll_errorpage;
    private LinearLayout ll_neterror;
    private LinearLayout ll_index_empty;
    private ImageView ll_index_empty_iv;
    private int count;
    private Button cooperation_provider_btn;
    private PullListView lv_zone;
    private PullToRefreshLayout pull_to_refreshlayout;
    private ShareProvider mShareProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooperationindex);
        mShareProvider = ShareProvider.getInitShareProvider(CooperationIndexActivity.this);
        userInfoDetailBean = PreferencesUtil.getLoginInfo(this);
        ///IMP 设置未读消息0
        PreferencesUtil.saveCooperationIndexCount(this, 0);

        if (userInfoDetailBean != null) {
            praiseNotifies.addAll(PreferencesUtil.getCooperationNotify(this, userInfoDetailBean.getEmobId()));
            getCircleTipsList(userInfoDetailBean.getEmobId());
        }
        initView();

        getData();
//        refreshData();

        pull_to_refreshlayout.autoRefresh();


        boolean isreadCooperation = PreferencesUtil.isUnReadCooperationIndex(getmContext());
        if (isreadCooperation) {
            startActivity(new Intent(getmContext(), CooperationNoticesPagerActivity.class));
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        userInfoDetailBean = PreferencesUtil.getLoginInfo(this);
        if (userInfoDetailBean != null) {
            getCircleTipsList(userInfoDetailBean.getEmobId());
        }
        if (pull_to_refreshlayout != null) {
            pull_to_refreshlayout.autoRefresh();
        }
    }


    private void initTitle() {

        findViewById(R.id.iv_back).setOnClickListener(this);
//        findViewById(R.id.tv_right_text).setVisibility(View.VISIBLE);
//        findViewById(R.id.tv_right_text).setOnClickListener(this);
//        ((TextView)findViewById(R.id.tv_right_text)).setText("分享");
//        avatar = (ImageView) findViewById(R.id.iv_avatar);
//        avatar.setOnClickListener(this);

//        if (userInfoDetailBean != null) {
//            ImageLoader.getInstance().displayImage(userInfoDetailBean.getAvatar(), avatar, UserUtils.options);
//        }

//        tv_right_text = (TextView) findViewById(R.id.tv_right_text);
//        tv_right_text.setVisibility(View.INVISIBLE);
//        tv_right_text.setOnClickListener(this);

    }

    private void initView() {

        initTitle();

        cooperation_provider_btn = (Button) findViewById(R.id.cooperation_provider_btn);
        cooperation_provider_btn.setVisibility(View.GONE);
        cooperation_provider_btn.setOnClickListener(this);

        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        ll_index_empty = (LinearLayout) findViewById(R.id.ll_index_empty);
        ll_index_empty_iv = (ImageView) findViewById(R.id.ll_index_empty_iv);
        ll_index_empty_iv.setImageResource(R.drawable.cooperation_index_none);

        ll_neterror.setOnClickListener(this);

        pull_to_refreshlayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refreshlayout);
        pull_to_refreshlayout.setOnRefreshListener(this);

        lv_zone = (PullListView) findViewById(R.id.lv_zone);
        lv_zone.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (lv_zone.getLastVisiblePosition() == (lv_zone.getCount() - 1)) {
                            pull_to_refreshlayout.autoLoad();
                        }
                        // 判断滚动到顶部
                        if (lv_zone.getFirstVisiblePosition() == 0) {

                        }

                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });


        lifeMessageAdapter = new CooperationIndexMessageAdapter(this, circleBeanList);

        if (headView == null) {
            headView = View.inflate(this, R.layout.common_cooperation_headview, null);
            /// 谁评论了你
            lv_eva = (ListView) headView.findViewById(R.id.lv_eva);
            lv_zone.addHeaderView(headView);
        }

        lv_zone.setAdapter(lifeMessageAdapter);


        headView.findViewById(R.id.search).setOnClickListener(this);

        adapter = new XJBaseAdapter(this, R.layout.common_cooperation_neweva_item, praiseNotifies, new String[]{"contentShow"}, new String[]{"getAvatar"}, new int[]{R.id.neweva_head}, UserUtils.options);

//        adapter = new XJBaseAdapter(this, R.layout.item_neweva_notify, praiseNotifies, new String[]{"content4Show"}, new String[]{"getAvatar4Show"}, new int[]{R.id.neweva_head}, UserUtils.options);
        lv_eva.setAdapter(adapter);

        lv_eva.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CooperationPraiseDiscussNotify newPraiseNotify = praiseNotifies.remove(position);
                for (int i = 0; i < praiseNotifies.size(); ) {

                    if (TextUtils.equals(newPraiseNotify.getSourceId(), praiseNotifies.get(i).getSourceId())) {

//                    if (newPraiseNotify.getSourceId() == praiseNotifies.get(i).getLifeCircleId()) {

                        praiseNotifies.remove(i);
                    } else {
                        i++;
                    }
                }

                PreferencesUtil.saveCooperationNotify(CooperationIndexActivity.this, userInfoDetailBean.getEmobId(), praiseNotifies);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        lifeMessageAdapter.notifyDataSetChanged();
                    }
                });

                /// 点赞 点赞功能取消
                if (TextUtils.equals(newPraiseNotify.getType(), "praise")) {

                    Intent intent = new Intent(CooperationIndexActivity.this, xj.property.activity.LifeCircle.MyPraiseActivity.class);
                    intent.putExtra(Config.Emobid, userInfoDetailBean.getEmobId());
                    startActivity(intent);

                } else if (TextUtils.equals(newPraiseNotify.getType(), "content")) {

                    Intent intent = new Intent(CooperationIndexActivity.this, ProviderDetailsActivity.class);

                    intent.putExtra("emobId", userInfoDetailBean.getEmobId());
                    intent.putExtra("cooperationId", newPraiseNotify.getSourceId());
                    startActivity(intent);
                }

            }
        });

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.head_portrait_personage)
                .showImageOnFail(R.drawable.head_portrait_personage)
                .showImageOnLoading(R.drawable.head_portrait_personage)
                .build();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pageIndex = 0;
        if (userInfoDetailBean != null)
            getCircleTipsList(userInfoDetailBean.getEmobId());
        getData();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

        if (pageIndex == 1) {
            lv_zone.setSelection(lv_zone.getCount());
        }
        pageIndex++;
        getData();

    }


    @Override
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
            List<NeighborListV3Bean.NeighborListData> lifeCircleBeans = PreferencesUtil.getCooperationBeanList(this);
            if (lifeCircleBeans == null || lifeCircleBeans.isEmpty()) {
            } // mLdDialog.show();
            else {//显示缓存的
                map.clear();

                circleBeanList.clear();
                circleBeanList.addAll(lifeCircleBeans);

                for (int i = 0; i < circleBeanList.size(); i++) {

                    map.put(circleBeanList.get(i).getCooperationId(), circleBeanList.get(i));
                }
                lifeMessageAdapter.notifyDataSetChanged();

                adapter.notifyDataSetChanged();
                lv_zone.setSelection(0);
            }
            pageIndex = 1;
        }
//        getCircleLifeList();
        getNeighborList();
        userInfoDetailBean = PreferencesUtil.getLoginInfo(this);
        if (userInfoDetailBean != null) {
            isPutNeighbor(userInfoDetailBean.getEmobId());
        }

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

            case R.id.cooperation_provider_btn:

                if (userInfoDetailBean == null) {
                    startActivityForResult(new Intent(this, RegisterLoginActivity.class), 0);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(this, IwantProviderActivity.class);
                    /// 如果用户发布过邻居帮直接将邻居帮ID传递,进行修改操作
                    if (iProvideredCooperationId != -1) {
                        intent.putExtra("iProvideredCooperationId", iProvideredCooperationId);
                    }
                    startActivityForResult(intent, REQUEST_NEW_Provider);
                }

                break;
            case R.id.iv_back:
                if (getIntent().getBooleanExtra(Config.INTENT_BACKMAIN, false)) {
                    startActivity(new Intent(this, MainActivity.class));
                }
                finish();
                break;
            case R.id.tv_right_text:

                mShareProvider.showShareActivity("http://www.baidu.com","分享内容","分享标题",mShareProvider.CODE_NEIGHBOR);
                break;
            case R.id.iv_avatar:
                break;
            case R.id.et_sendmessage:
                break;

            case R.id.ll_neterror:
                if (pull_to_refreshlayout != null) {
                    pull_to_refreshlayout.autoRefresh();
                }
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
            PreferencesUtil.saveCooperationIndexCount(this, 0);

            userInfoDetailBean = PreferencesUtil.getLoginInfo(this);

            if (userInfoDetailBean != null) {
                praiseNotifies.addAll(PreferencesUtil.getCooperationNotify(this, userInfoDetailBean.getEmobId()));
                getCircleTipsList(userInfoDetailBean.getEmobId());
            }
            initView();
        }
        UMSsoHandler ssoHandler = ShareProvider.mController.getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

    }


    interface CooperationService {
        //// 获取邻居帮的列表
        @GET("/api/v1/communities/{communityId}/cooperations")
        void getList(@Path("communityId") long communityId, @QueryMap HashMap<String, Object> option, Callback<CooperationIndexRespBean> cb);


        /**
         * 获取邻居帮列表
         * V3 接口
         */
        @GET("/api/v3/cooperations")
        void getNeighborList(@QueryMap HashMap<String, String> option, Callback<CommonRespBean<NeighborListV3Bean>> cb);

        //   /api/v3/cooperations/exsit

        /**
         * 判断用户有没有发布过邻居帮，如果有，返回邻居帮的ID
         * V3 接口
         */
        @GET("/api/v3/cooperations/exsit")
        void isPutNeighbor(@QueryMap HashMap<String, String> option, Callback<CommonRespBean<Integer>> cb);

        /**
         * 获取邻居帮新提示
         * V3 接口
         */
        @GET("/api/v3/cooperations/tips")
        void getNeighborNewMessage(@QueryMap HashMap<String, String> option, Callback<CommonRespBean<List<CooperationPraiseDiscussNotify>>> cb);

    }

    public void getNeighborList(){

        HashMap<String, String> querymap = new HashMap<>();
        querymap.put("page", ""+pageIndex);
        querymap.put("limit", ""+pageSize);
        querymap.put("communityId",""+PreferencesUtil.getCommityId(getApplicationContext()));

        CooperationService service = RetrofitFactory.getInstance().create(getmContext(),querymap,CooperationService.class);
        Callback<CommonRespBean<NeighborListV3Bean>> callback = new Callback<CommonRespBean<NeighborListV3Bean>>() {
            @Override
            public void success(CommonRespBean<NeighborListV3Bean> respone, Response response) {
                if (respone != null && "yes".equals(respone.getStatus())) {
                    ll_errorpage.setVisibility(View.GONE);
                    ll_index_empty.setVisibility(View.GONE);
                    ll_neterror.setVisibility(View.GONE);

                    List<NeighborListV3Bean.NeighborListData> list = respone.getData().getData();

                    if (circleBeanList.size() > 0) {
                        if (list == null || list.size() <= 0) {
                            showNoMoreToast();
                        }
                    }
                    if (pageIndex == 1) {
                        circleBeanList.clear();
                        map.clear();
                        PreferencesUtil.saveCooperationBeanList(CooperationIndexActivity.this, list);

//                        String time = "" + System.currentTimeMillis() / 1000;
////                        Log.i("debbug","" + time);
//
//                        PreferencesUtil.saveLifeCircleCountTime(XjApplication.getInstance(), "" + time);//保存indexfragment页面刷新时间
                    }
                    circleBeanList.addAll(list);

                    for (int i = 0; i < list.size(); i++) {

                        map.put(list.get(i).getCooperationId(), list.get(i));
                    }
                    lifeMessageAdapter.notifyDataSetChanged();

                    count = lifeMessageAdapter.getCount();

                    adapter.notifyDataSetChanged();

                } else {
                    showNetErrorToast();
                }

                if (pageIndex == 1) {
                    pull_to_refreshlayout.refreshFinish(true);
                } else {
                    pull_to_refreshlayout.loadMoreFinish(true);
                }

                if (count == 0) {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_index_empty.setVisibility(View.VISIBLE);
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
                if (count == 0) {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_neterror.setVisibility(View.VISIBLE);
                }
                error.printStackTrace();
            }
        };
        service.getNeighborList(querymap, callback);
    }

    public void isPutNeighbor(final String emobid) {

        HashMap<String, String> map = new HashMap<>();
        map.put("emobId",emobid);
        map.put("communityId",""+PreferencesUtil.getCommityId(getApplicationContext()));
        CooperationService service = RetrofitFactory.getInstance().create(getmContext(),map,CooperationService.class);
        Callback<CommonRespBean<Integer>> callback = new Callback<CommonRespBean<Integer>>() {
            @Override
            public void success(CommonRespBean<Integer> respone, Response response) {
                if (respone != null && "yes".equals(respone.getStatus())) {

                    if (cooperation_provider_btn != null) {
                        cooperation_provider_btn.setText("修改已发布的帮助信息");
                        cooperation_provider_btn.setVisibility(View.VISIBLE);
                    }
                    iProvideredCooperationId = respone.getData();
                } else if (respone != null && "no".equals(respone.getStatus())) {
                    if (cooperation_provider_btn != null) {
                        cooperation_provider_btn.setText("+我也要提供");
                        cooperation_provider_btn.setVisibility(View.VISIBLE);
                    }
                    iProvideredCooperationId = -1;
                } else {
                    if (cooperation_provider_btn != null) {
                        cooperation_provider_btn.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();

                if (cooperation_provider_btn != null) {
                    cooperation_provider_btn.setText("+我也要提供");
                    cooperation_provider_btn.setVisibility(View.GONE);
                }
                iProvideredCooperationId = -1;
            }
        };

        service.isPutNeighbor(map, callback);
    }

    public void getCircleTipsList(final String emobid) {
        HashMap<String,String> querymap = new HashMap<String,String>();
        querymap.put("communityId",""+PreferencesUtil.getCommityId(getmContext()));
        querymap.put("emobId",emobid);
        CooperationService service = RetrofitFactory.getInstance().create(getmContext(),querymap,CooperationService.class);
        Callback<CommonRespBean<List<CooperationPraiseDiscussNotify>>> callback = new Callback<CommonRespBean<List<CooperationPraiseDiscussNotify>>>() {
            @Override
            public void success(CommonRespBean<List<CooperationPraiseDiscussNotify>> respone, Response response) {

                if (respone != null && "yes".equals(respone.getStatus())) {
                    List<CooperationPraiseDiscussNotify> tips = respone.getData();

//                    respone.getField("")
                    /**
                     *
                     "characterPercent": {用户人品值打败同小区内用户数量的百分比},
                     "characterValues": {人品值},
                     *
                     */
                    if (tips != null && tips.size() > 0) {
                        for (int i = 0; i < tips.size(); i++) {
                            CooperationPraiseDiscussNotify newPraiseNotify = tips.get(i);
                            newPraiseNotify.setContentShow(newPraiseNotify.getNickname()); /// 目的是为了适配XX评论了您..
                        }
                        praiseNotifies.addAll(tips);
                        PreferencesUtil.saveCooperationNotify(getmContext(), emobid, praiseNotifies);
                        lifeMessageAdapter.notifyDataSetChanged();
                        adapter.notifyDataSetChanged();

//                    if (respone.getInfo().getCharacterValues() > 0) {

//                        ll_character_llay.setVisibility(View.VISIBLE);
//                        ll_none_eva.setVisibility(View.GONE);
//
//                        tv_content.setText("打败了本小区" + StrUtils.getPrecent(respone.getInfo().getCharacterPercent()) + "%的居民！");
////                        tv_content.setText("打败了" + StrUtils.getPrecent(respone.getInfo().getCharacterPercent()) + "%的" + PreferencesUtil.getCommityName(FriendZoneIndexActivity.this) + "居民！");
//                        tv_person_value.setText("" + respone.getInfo().getCharacterValues());

//                        tv_content.setText("打败了100.00%的本小区居民！");
//                        tv_person_value.setText("" +1000);
//                    } else {
//                        ll_character_llay.setVisibility(View.GONE);
//                        ll_none_eva.setVisibility(View.VISIBLE);
//                    }


                    }

                } else {
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        };

        service.getNeighborNewMessage(querymap, callback);
    }


}
