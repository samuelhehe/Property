package xj.property.activity.LifeCircle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.adapter.CooperationIndexMessageAdapter;
import xj.property.adapter.LifeCircleTimeLineAdapter;
import xj.property.beans.MyLifeCircleBean;
import xj.property.beans.MyLifeCirclePageBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseUtils;
import xj.property.netbase.RetrofitFactory;
import xj.property.netbasebean.SimpleUserInfoBean;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.BaseUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.FriendZoneUtil;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

public class MyLifeCircleActivity extends HXBaseActivity implements PullToRefreshLayout.OnRefreshListener {
    private static final String TAG = "MyLifeCircleActivity";
    private LifeCircleTimeLineAdapter adapter;
    private LinearLayout praise_rp;
    private ImageView iv_avatar;
    private TextView tv_name_user;
    private UserInfoDetailBean bean;
    private LinearLayout listviewTopView;
    private TextView tv_person_value;
    private TextView tv_character_percent;
    private RelativeLayout footerTimeLineView;
    private int pageNum = 1;
    private String pageSize = "10";

    private String emobid;
    private ImageView iv_user_type_header;
    private PullToRefreshLayout pull_to_refreshlayout;
    private PullListView timelineList;
    private List<MyLifeCircleBean.LifeCircleSimpleBean> timeLineBeans = new ArrayList<>();
    private boolean added = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_life_circle);
        //serUtils.saveUser(this,PreferencesUtil.getLoginInfo(this).getEmobId());
        initview();
        initData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getLifeCircle();
    }

    /**
     * 初始化用户横条奖章图片
     */
    private void initBangzhuMedal(String userType) {
        Log.d("initBangzhuMedal  ", "userType  " + userType);
        if (iv_user_type_header != null) {
//            normal , bangzhu, fubangzhu ,zhanglao,bangzhong
            if (TextUtils.equals(userType, Config.USER_TYPE_ZHANGLAO)) {
                iv_user_type_header.setImageDrawable(getResources().getDrawable(R.drawable.me_zhanglao_icon));
                iv_user_type_header.setVisibility(View.VISIBLE);
            } else if (TextUtils.equals(userType, Config.USER_TYPE_BANGZHU)) {
                iv_user_type_header.setImageDrawable(getResources().getDrawable(R.drawable.me_bangzhu_icon));
                iv_user_type_header.setVisibility(View.VISIBLE);
            } else if (TextUtils.equals(userType, Config.USER_TYPE_FUBANGZHU)) {
                iv_user_type_header.setImageDrawable(getResources().getDrawable(R.drawable.me_fubangzhu_icon));
                iv_user_type_header.setVisibility(View.VISIBLE);
            } else {
                iv_user_type_header.setVisibility(View.GONE);
            }
        }
    }

    private void initview() {
        initTitle(null, "WO的生活圈", null);
        listviewTopView = (LinearLayout) View.inflate(this, R.layout.life_circle_top_view, null);
        praise_rp = (LinearLayout) listviewTopView.findViewById(R.id.praise_rp);
        iv_avatar = (ImageView) listviewTopView.findViewById(R.id.iv_avatar);
        iv_user_type_header = (ImageView) listviewTopView.findViewById(R.id.iv_user_type_header);
        tv_name_user = (TextView) listviewTopView.findViewById(R.id.tv_name_user);
        tv_person_value = (TextView) listviewTopView.findViewById(R.id.tv_person_value);
        tv_character_percent = (TextView) listviewTopView.findViewById(R.id.tv_character_percent);

        footerTimeLineView = (RelativeLayout) View.inflate(this, R.layout.mylifecircle_timeline_footer, null);
        pull_to_refreshlayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refreshlayout);
        pull_to_refreshlayout.setOnRefreshListener(this);
        timelineList = (PullListView) findViewById(R.id.lv_time_line);
        adapter = new LifeCircleTimeLineAdapter(MyLifeCircleActivity.this, timeLineBeans, emobid);
        timelineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d("MyLifeCircleActivity ", "onItemClick position " + (position-1) + " emobid " + emobid );

                Intent intent = new Intent(getmContext(), ZoneItemActivity.class);
                intent.putExtra(Config.INTENT_PARMAS1, emobid);
                intent.putExtra(Config.INTENT_PARMAS2, timeLineBeans.get(position-1).getLifeCircleId());
                getmContext().startActivity(intent);
            }
        });
        timelineList.setAdapter(adapter);
        timelineList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (timelineList.getLastVisiblePosition() == (timelineList.getCount() - 1)) {
                            pull_to_refreshlayout.autoLoad();
                        }
                        // 判断滚动到顶部
                        if (timelineList.getFirstVisiblePosition() == 0) {

                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        timelineList.addHeaderView(listviewTopView);
    }

    private void initData() {
        bean = PreferencesUtil.getLoginInfo(this);
        emobid = getIntent().getStringExtra("emobid");
        if (bean.getEmobId().equals(emobid)) {
            praise_rp.setVisibility(View.INVISIBLE);
        } else {
            initTitle(null, "TA的生活圈", null);
        }
        praise_rp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case Config.TASKCOMPLETE:
                                showToast(R.string.praise);
                                break;
                            case Config.TASKERROR:
                                showToast("一天只能赞一次哦");
                                break;
                            case Config.NETERROR:
                                showToast(R.string.netError);
                                break;
                        }
                    }
                };
                FriendZoneUtil.zambia(emobid, 0, 1, MyLifeCircleActivity.this, handler);
            }
        });

        NetBaseUtils.extractSimpleUserInfo(getmContext(), PreferencesUtil.getCommityId(getmContext()), emobid, new NetBaseUtils.NetRespListener<CommonRespBean<SimpleUserInfoBean>>() {
            @Override
            public void successYes(CommonRespBean<SimpleUserInfoBean> commonRespBean, Response response) {

                ImageLoader.getInstance().displayImage(commonRespBean.getData().getAvatar(), iv_avatar, UserUtils.options);
                /////
                initBangzhuMedal(commonRespBean.getData().getGrade());
                initTitle(null, commonRespBean.getData().getNickname(), null);
                tv_name_user.setText(commonRespBean.getData().getNickname());
                tv_person_value.setText("" + commonRespBean.getData().getCharacterValues());
                try {
//                        tv_character_percent.setText("打败了本小区"+ StrUtils.getPrecent(bean.getInfo().getCharacterPercent())+"%的小区居民！");
                    tv_character_percent.setText("打败了" + StrUtils.getPrecent(commonRespBean.getData().getCharacterPercent()) + "%的" + PreferencesUtil.getCommityName(MyLifeCircleActivity.this) + "小区居民！");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void successNo(CommonRespBean<SimpleUserInfoBean> commonRespBean, Response response) {
                showDataErrorToast(commonRespBean.getMessage());
            }

            @Override
            public void failure(RetrofitError error) {
                showNetErrorToast();
            }
        });

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pageNum = 1;
        getLifeCircle();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        pageNum++;
        getLifeCircle();
    }

    interface LifeCircleService {
        //        @GET("/api/v1/communities/{communityId}/circles/{emobId}/single")  //// 20151014 修改v1 -> v2
//        @GET("/api/v2/communities/{communityId}/circles/{emobId}/single")
//        void getLifeCircle(@Path("communityId") long communityId, @Path("emobId") String emobId, @QueryMap Map<String, String> map, Callback<MyLifeCircleBean> cb);


//        @GET("/api/v2/communities/{communityId}/circles/{emobId}/single")

        ///api/v3/lifeCircles/users?communityId={小区ID}&emobId={用户环信ID}&page={页码}&limit={页面大小}
        @GET("/api/v3/lifeCircles/users")
        //// 2016/03/02  v2 -> v3
        void getLifeCircle(@QueryMap Map<String, String> map, Callback<CommonRespBean<MyLifeCircleBean>> cb);
    }

    private void getLifeCircle() {
        HashMap<String, String> option = new HashMap<String, String>();
        option.put("communityId", "" + PreferencesUtil.getCommityId(this));
        option.put("emobId", emobid);
        option.put("page", "" + pageNum);
        option.put("limit", pageSize);
        LifeCircleService service = RetrofitFactory.getInstance().create(getmContext(), option, LifeCircleService.class);
        Callback<CommonRespBean<MyLifeCircleBean>> callback = new Callback<CommonRespBean<MyLifeCircleBean>>() {
            @Override
            public void success(CommonRespBean<MyLifeCircleBean> bean, Response response) {
                if ("yes".equals(bean.getStatus()) && bean.getData() != null && bean.getData().getData() != null) {
                    if (pageNum == 1) {
                        timeLineBeans.clear();
                    } else {
                        if (timeLineBeans.size() > 0) {
                            if (bean.getData().getData().size() < 10) {
                                showNoMoreToast();
                            }
                        }
                    }
                    timeLineBeans.addAll(bean.getData().getData());
                    if (adapter.getCount() > 0) {
                        if (!added) {
                            timelineList.addFooterView(footerTimeLineView);
                            added = true;
                        }
                    } else {
                        timelineList.removeFooterView(footerTimeLineView);
                    }
                    adapter.notifyDataSetChanged();
                } else if (bean.getData() != null) {
                    showDataErrorToast(bean.getMessage());
                } else {
                    showDataErrorToast();
                }
                if (pageNum == 1) {
                    pull_to_refreshlayout.refreshFinish(true);
                } else {
                    pull_to_refreshlayout.loadMoreFinish(true);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                showNetErrorToast();
                if (pageNum == 1) {
                    pull_to_refreshlayout.refreshFinish(true);
                } else {
                    pull_to_refreshlayout.loadMoreFinish(true);
                }
            }
        };

        ///communityId={小区ID}&emobId={用户环信ID}&page={页码}&limit={页面大小}
        service.getLifeCircle(option, callback);
    }

    @Override
    public void onClick(View v) {
    }
}
