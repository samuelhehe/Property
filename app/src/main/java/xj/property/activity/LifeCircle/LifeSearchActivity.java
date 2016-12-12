package xj.property.activity.LifeCircle;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.repo.xw.library.views.PullListView;
import com.repo.xw.library.views.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.XjApplication;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.MainActivity;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.activity.user.ShowBigImageViewPager;
import xj.property.adapter.LifeMessageAdapter;
import xj.property.beans.CircleListRespone;
import xj.property.beans.LifeCircleBean;
import xj.property.beans.LifeCircleDetail;
import xj.property.beans.NewPraiseResponse;
import xj.property.beans.UserInfoDetailBean;
import xj.property.cache.ZambiaCache;
import xj.property.event.EvaEvent;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.CommonUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.FriendZoneUtil;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

import static com.repo.xw.library.views.PullToRefreshLayout.OnRefreshListener;

/**
 * Created by Administrator on 2015/6/8.
 */
public class LifeSearchActivity extends HXBaseActivity implements OnRefreshListener {
    final static int REQUEST_NEW_RECORE = 778;
    ImageView avatar;
    UserInfoDetailBean userInfoDetailBean;
    HashMap<Integer, LifeCircleBean> map = new HashMap<>();
    List<LifeCircleBean> circleBeanList = new ArrayList<>();
    TextView tv_title, tv_person_value, tv_content;
    LifeMessageAdapter lifeMessageAdapter;
    LinearLayout ll_character_llay;
    FrameLayout ll_none_eva;
    Button btnSend, btnCharter;
    EditText editText;
    PopupWindow popupWindow;
    View headView;
    int pageIndex;
    String searchName;
    View root;
    ArrayList<String> currentlifePhotos = new ArrayList<>();
    private PullToRefreshLayout pull_to_refreshlayout;
    private PullListView lv_zone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendzondindex);
        initView();

        userInfoDetailBean = PreferencesUtil.getLoginInfo(this);
        PreferencesUtil.saveUnReadCircleCount(this, 0);
        if (userInfoDetailBean != null) {
            getCircleTipsList(userInfoDetailBean.getEmobId());
        }
        getData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        userInfoDetailBean = PreferencesUtil.getLoginInfo(this);
        if (userInfoDetailBean != null) {
            getCircleTipsList(userInfoDetailBean.getEmobId());
        }
    }

    private void initView() {
        root = findViewById(R.id.root);

        pull_to_refreshlayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refreshlayout);
        pull_to_refreshlayout.setOnRefreshListener(this);

        lv_zone = (PullListView) findViewById(R.id.lv_zone);
//        pull_listview.setPullUpEnable(false);

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


//        lv_zone=(ListView)findViewById(R.id.lv_zone);
        // lv_eva=(ListView)findViewById(R.id.lv_eva);
        findViewById(R.id.iv_back).setOnClickListener(this);
        avatar = (ImageView) findViewById(R.id.iv_avatar);
        findViewById(R.id.iv_search).setVisibility(View.INVISIBLE);
        findViewById(R.id.iv_right_text).setVisibility(View.INVISIBLE);

        avatar.setOnClickListener(this);

        tv_title = (TextView) findViewById(R.id.tv_title);
        ll_none_eva = (FrameLayout) findViewById(R.id.ll_none_eva);
        ll_character_llay = (LinearLayout) findViewById(R.id.ll_character_llay);
        ll_character_llay.setOnClickListener(this);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_person_value = (TextView) findViewById(R.id.tv_person_value);
        tv_title.setText("搜索结果");
        if (userInfoDetailBean != null) {
            ImageLoader.getInstance().displayImage(userInfoDetailBean.getAvatar(), avatar, UserUtils.options);
            tv_title.setText(userInfoDetailBean.getNickname());
        }


        lifeMessageAdapter = new LifeMessageAdapter(this, circleBeanList);
        headView = View.inflate(this, R.layout.circyle_headview, null);
        headView.findViewById(R.id.search).setVisibility(View.GONE);
        lv_zone.addHeaderView(headView);
        lv_zone.setAdapter(lifeMessageAdapter);

//       NewPraiseNotify praiseNotify=new NewPraiseNotify();
//        praiseNotify.setAvatar4Show("http://baidu.logo");
//        praiseNotify.setContent4Show("测试");
//        praiseNotifies.add(praiseNotify);
        if (userInfoDetailBean != null) {
            ll_none_eva.setVisibility(View.GONE);
            ll_character_llay.setVisibility(View.VISIBLE);
        } else {
            ll_character_llay.setVisibility(View.GONE);
            ll_none_eva.setVisibility(View.VISIBLE);
        }
        initPopupWindow();

    }

    private void initPopupWindow() {
        View bottomview = View.inflate(this, R.layout.bottom_input, null);
        popupWindow = new PopupWindow(bottomview, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        editText = (EditText) bottomview.findViewById(R.id.et_sendmessage);
        editText.setOnClickListener(this);
        btnSend = (Button) bottomview.findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);
        btnCharter = (Button) bottomview.findViewById(R.id.btn_charter);
        btnCharter.setOnClickListener(this);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }


    private ArrayList<String> imageuris;

    public void changeCurrent(int lifeCircleId, int postion) {
        imageuris = new ArrayList<String>();
        currentlifePhotos.clear();
        currentlifePhotos.addAll(Arrays.asList(map.get(lifeCircleId).getPhotoes().split(",")));
        for (int i = 0; i < currentlifePhotos.size(); i++) {
            imageuris.add(currentlifePhotos.get(i));
        }
        if (currentlifePhotos.size() <= postion) return;
        Intent intent = new Intent(LifeSearchActivity.this, ShowBigImageViewPager.class);
        intent.putExtra("images", imageuris);
        intent.putExtra("position", postion);
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        PreferencesUtil.saveLifeCircleCountTime(this,(new Date().getTime()/1000)+"");

//        PreferencesUtil.saveLifeCircleCountTime(XjApplication.getInstance(), "" + System.currentTimeMillis() / 1000);
    }


    public void showKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
                return true;
            } else {
                if (getIntent().getBooleanExtra(Config.INTENT_BACKMAIN, false)) {
                    startActivity(new Intent(this, MainActivity.class));
                }
                finish();
                return true;
            }
        }


        return super.onKeyDown(keyCode, event);
    }

    private void getData() {
        if (pageIndex == 0) {
            List<LifeCircleBean> lifeCircleBeans = new ArrayList<>();
            //PreferencesUtil.getCircleList(this);
//            if (lifeCircleBeans.isEmpty())
//            mLdDialog.show();
//            else {//显示缓存的
//                map.clear();
//                circleBeanList.clear();
//                circleBeanList.addAll(lifeCircleBeans);
//                for (int i = 0; i < circleBeanList.size(); i++) {
//                    map.put(circleBeanList.get(i).getLifeCircleId(), circleBeanList.get(i));
//                }
//                lifeMessageAdapter.notifyDataSetChanged();
//                lv_zone.setSelection(0);
//            }
            pageIndex = 1;
//            mLdDialog.show();
        }
        if (searchName == null) {
            searchName = getIntent().getStringExtra(Config.SearchName);
//            ( (TextView)   headView.findViewById(R.id.search)).setText(searchName);
        }
        getCircleLifeList(searchName);
    }

    @Override
    public void onClick(View v) {
        userInfoDetailBean = PreferencesUtil.getLoginInfo(this);
        switch (v.getId()) {
            case R.id.ll_character_llay:
                startActivity(new Intent(this, RPValueTopListActivity.class));
                break;
            case R.id.search:
//                startActivity(new Intent(this,LifeSearchActivity.class));
                startActivity(new Intent(this, SearchLifeCircle.class));
                break;
            case R.id.tv_right_text:
                if (userInfoDetailBean == null) {
                    startActivityForResult(new Intent(this, RegisterLoginActivity.class), 0);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(this, NewRecoreActivity.class);
                    startActivityForResult(intent, REQUEST_NEW_RECORE);
                }
                break;
            case R.id.iv_back:
                popupWindow.dismiss();
                popupWindow = null;
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
            case R.id.btn_send:
                final String content = editText.getText().toString();
                if (content == null || TextUtils.isEmpty(content)) {
                    showToast("请输入评论内容");
                    return;
                }
                if (currentEvent != null) {
                    mLdDialog.show();
                    FriendZoneUtil.eva(getmContext(),userInfoDetailBean.getCommunityId(), currentEvent.getFrom(), userInfoDetailBean.getEmobId(), content, currentEvent.getCircleLifeId(), new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            mLdDialog.dismiss();
                            switch (msg.what) {
                                case Config.NETERROR:
                                    showNetErrorToast();
                                    break;
                                case Config.TASKCOMPLETE:
                                    showToast("评论成功");
                                    List<LifeCircleDetail> list = map.get(currentEvent.getCircleLifeId()).getLifeCircleDetails();
                                    int time = (int) (new Date().getTime() / 1000);
                                    list.add(new LifeCircleDetail(msg.arg1, userInfoDetailBean.getEmobId(), currentEvent.getFrom(), userInfoDetailBean.getNickname(), currentEvent.getFromNike(), 0, time, time, currentEvent.getCircleLifeId(), content));
                                    lifeMessageAdapter.notifyDataSetChanged();
                                    break;
                                case Config.TASKERROR:
                                    showToast("评论失败");
                                    break;
                            }
                        }
                    });
                }
                editText.getText().clear();
                popupWindow.dismiss();
                break;
            case R.id.btn_charter:
                List<LifeCircleDetail> list = map.get(currentEvent.getCircleLifeId()).getLifeCircleDetails();
                if (currentEvent.getCircleLifeDetialId() == 0) {
                    zambia(map.get(currentEvent.getCircleLifeId()));
                    return;
                }
                for (int i = 0; i < list.size(); i++) {
                    if (currentEvent.getCircleLifeDetialId() == list.get(i).getLifeCircleDetailId()) {
                        zambia(list.get(i));
                    }
                    ;
                }

                break;
            default:
        }
    }

    public void onEvent(EvaEvent event) {
        if (userInfoDetailBean != null) {
            popupWindow.showAtLocation(root, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            if (event.getCircleLifeDetialId() == 0) {
                btnCharter.setVisibility(View.GONE);
            } else {
                btnCharter.setVisibility(View.VISIBLE);
            }
            showKeyBoard();
            editText.setHint("回复" + event.getFromNike() + ":");
            currentEvent = event;
        } else {
            Intent intent = new Intent(this, RegisterLoginActivity.class);
            startActivityForResult(intent, Config.TASKCOMPLETE);

        }
    }


    EvaEvent currentEvent;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_NEW_RECORE) {
                pageIndex = 0;
                getData();
                return;
            }
            PreferencesUtil.saveUnReadCircleCount(this, 0);
            userInfoDetailBean = PreferencesUtil.getLoginInfo(this);
//            ImageLoader.getInstance().displayImage(userInfoDetailBean.getAvatar(),avatar);
            if (userInfoDetailBean != null) {
                getCircleTipsList(userInfoDetailBean.getEmobId());
            }
            initView();
//            getData();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
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
        pageIndex++;
        getData();
    }


    interface CircleLifeListService {
        ///api/v1/communities/{communityId}/circles/{emobId}/search?q={text}&pageNum=1&pageSize=20
//        @GET("/api/v2/communities/{communityId}/circles/{emobId}/search")
//        void getList(@Path("communityId") int communityId, @Path("emobId") String emobId, @QueryMap HashMap<String, Object> option, Callback<CircleListRespone> cb);

//        @GET("/api/v2/communities/{communityId}/circles/{emobId}/search")

  ///api/v3/lifeCircles/search?communityId={小区ID}&emobId={用户环信ID}&page={页码}&limit={页面大小}&q={搜索内容}

        @GET("/api/v3/lifeCircles/search") //v3 2016/03/04
        void getList(@QueryMap HashMap<String, String> option, Callback<CommonRespBean<CircleListRespone>> cb);
    }

    public void getCircleLifeList(String searchName) {


        ///communityId={小区ID}&emobId={用户环信ID}&page={页码}&limit={页面大小}&q={搜索内容}
        HashMap<String, String> querymap = new HashMap<>();
        querymap.put("q", searchName);
        querymap.put("page", ""+pageIndex);
        querymap.put("limit", ""+10);
        querymap.put("communityId", ""+PreferencesUtil.getCommityId(this));
        querymap.put("emobId", userInfoDetailBean == null ? "-1" : userInfoDetailBean.getEmobId());
        CircleLifeListService service = RetrofitFactory.getInstance().create(getmContext(),querymap,CircleLifeListService.class);
        Callback<CommonRespBean<CircleListRespone>> callback = new Callback<CommonRespBean<CircleListRespone>>() {
            @Override
            public void success(CommonRespBean<CircleListRespone> respone, retrofit.client.Response response) {
                if ("yes".equals(respone.getStatus())&&respone.getData()!=null&&respone.getData().getData()!=null) {
                    if (pageIndex == 1) {
                        circleBeanList.clear();
                        map.clear();
                        PreferencesUtil.saveCircleList(LifeSearchActivity.this, respone.getData().getData());
                        String time = "" + System.currentTimeMillis() / 1000;
//                        Log.i("debbug","" + time);
                        PreferencesUtil.saveLifeCircleCountTime(XjApplication.getInstance(), "" + time);//保存indexfragment页面刷新时间
                    }
                    circleBeanList.addAll(respone.getData().getData());
                    List<LifeCircleBean> list = respone.getData().getData();
                    for (int i = 0; i < list.size(); i++) {
                        map.put(list.get(i).getLifeCircleId(), list.get(i));
                    }
                    lifeMessageAdapter.notifyDataSetChanged();
                    if (respone.getData().getData().isEmpty()) {
                        if (pageIndex > 1) {
                            showNoMoreToast();
                        } else {
                            findViewById(R.id.ll_noservice_time).setVisibility(View.VISIBLE);
                        }
                    }
                } else {

                    showNetErrorToast();
                }

                if(pageIndex == 1 ){

                    pull_to_refreshlayout.refreshFinish(true);
                }else{
                    pull_to_refreshlayout.loadMoreFinish(true);
                }
            }

            @Override
            public void failure(RetrofitError error) {
//                mLdDialog.dismiss();
                showNetErrorToast();
                error.printStackTrace();

                if(pageIndex == 1 ){

                    pull_to_refreshlayout.refreshFinish(true);
                }else{
                    pull_to_refreshlayout.loadMoreFinish(true);
                }
            }
        };

        service.getList(querymap, callback);
    }


    interface CircleTipsListService {
        ///api/v1/communities/{communityId}/circles/{emobId}/tips?time=0
//        @GET("/api/v1/communities/{communityId}/circles/{emobId}/tips")
//        void getList(@Path("communityId") int communityId, @Path("emobId") String emobId, @QueryMap HashMap<String, Integer> option, Callback<CommonRespBean<NewPraiseResponse>> cb);

//        @GET("/api/v1/communities/{communityId}/circles/{emobId}/tips")

        @GET("/api/v3/lifeCircles/tips")
        void getList(@QueryMap HashMap<String, String> option, Callback<CommonRespBean<NewPraiseResponse>> cb);
    }

    public void getCircleTipsList(final String emobid) {
        final int time = PreferencesUtil.getNewCircleTime(this);
        HashMap<String, String> option = new HashMap();
//        option.put("time",1434335978);
        option.put("time", ""+time);
        option.put("communityId", ""+PreferencesUtil.getCommityId(this));
        option.put("emobId", emobid);

        CircleTipsListService service = RetrofitFactory.getInstance().create(getmContext(),option,CircleTipsListService.class);
        Callback<CommonRespBean<NewPraiseResponse>> callback = new Callback<CommonRespBean<NewPraiseResponse>>() {
            @Override
            public void success(CommonRespBean<NewPraiseResponse> respone, retrofit.client.Response response) {
                if ("yes".equals(respone.getStatus())) {
                    lifeMessageAdapter.notifyDataSetChanged();
                    if (respone.getData().getCharacterValues() > 0) {
                        ll_character_llay.setVisibility(View.VISIBLE);
                        ll_none_eva.setVisibility(View.GONE);

                        tv_content.setText("打败了本小区" + StrUtils.getPrecent(respone.getData().getCharacterPercent()) + "%的居民！");
//                        tv_content.setText("打败了" + StrUtils.getPrecent(respone.getInfo().getCharacterPercent()) + "%的" + PreferencesUtil.getCommityName(LifeSearchActivity.this) + "居民！");
                        tv_person_value.setText("" + respone.getData().getCharacterValues());

//                        tv_content.setText("打败了100.00%的本小区居民！");
//                        tv_person_value.setText("" +1000);
                    } else {
                        ll_character_llay.setVisibility(View.GONE);
                        ll_none_eva.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        };

        service.getList( option, callback);
    }

    private void zambia(final LifeCircleDetail circleDetail) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(popupWindow.getContentView().getWindowToken(), 0);
        if (!CommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, this.getString(R.string.network_unavailable), Toast.LENGTH_SHORT).show();
            return;
        }
        ZambiaCache zambiaCache = new Select().from(ZambiaCache.class).where("emobid = ? and emobidhost = ?", circleDetail.getEmobIdFrom(), userInfoDetailBean.getEmobId()).executeSingle();
        if (zambiaCache != null) {
            if (!StrUtils.isInDay(zambiaCache.getZambiatime())) {
                zambiaCache.setZambiatime((int) (new Date().getTime() / 1000));
                circleDetail.setPraiseSum(circleDetail.getPraiseSum() + 1);
                lifeMessageAdapter.notifyDataSetChanged();
                zambiaCache.save();
            } else {//本地检验未通过
                Toast.makeText(this, "同一天，同一人只能赞一次", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            circleDetail.setPraiseSum(circleDetail.getPraiseSum() + 1);
            lifeMessageAdapter.notifyDataSetChanged();
            zambiaCache = new ZambiaCache();
            zambiaCache.setEmobid(circleDetail.getEmobIdFrom());
            zambiaCache.setEmobidhost(userInfoDetailBean.getEmobId());
            zambiaCache.setZambiatime((int) (new Date().getTime() / 1000));
            zambiaCache.save();
        }
//        mLdDialog.show();
        FriendZoneUtil.zambia(circleDetail.getEmobIdFrom(), circleDetail.getLifeCircleId(), circleDetail.getLifeCircleDetailId(), 2, LifeSearchActivity.this, new Handler() {
            @Override
            public void handleMessage(Message msg) {
//                mLdDialog.dismiss();
                switch (msg.what) {
                    case Config.TASKCOMPLETE:
                        Toast.makeText(LifeSearchActivity.this, getString(R.string.praise), Toast.LENGTH_LONG).show();
                        break;
                    case Config.NETERROR:
                        showNetErrorToast();
                        new Delete().from(ZambiaCache.class).where("emobid = ? and emobidhost = ?", circleDetail.getEmobIdFrom(), userInfoDetailBean.getEmobId()).execute();
                        break;
                    case Config.TASKERROR:
                        showToast("同一天，同一人只能赞一次");
                        break;
                }

            }
        });
    }

    private void zambia(final LifeCircleBean circleBean) {
        mLdDialog.show();
        FriendZoneUtil.zambia(circleBean.getEmobId(), 0, 1, this, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mLdDialog.dismiss();
                switch (msg.what) {
                    case Config.TASKCOMPLETE:
                        Toast.makeText(LifeSearchActivity.this, getString(R.string.praise), Toast.LENGTH_LONG).show();
                        circleBean.setPraiseSum(circleBean.getPraiseSum() + 1);
                        lifeMessageAdapter.notifyDataSetChanged();
                        break;
                    case Config.NETERROR:
                        showNetErrorToast();
                        break;
                    case Config.TASKERROR:
                        showToast("同一天，同一人只能赞一次");
                        break;
                }
            }
        });
    }
}
