package xj.property.activity.LifeCircle;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.adapter.RPValueAdapter;
import xj.property.beans.RPValueAllBean;
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


/**
 * 谁给我赞了人品值,列表,
 * v3 2016/03/04
 */
public class MyPraiseActivity extends HXBaseActivity {

    private static final String TAG = "MyPraiseActivity";
    private RPValueAdapter adapter;
    private ListView listView;
    private LinearLayout praise_rp;
    private ImageView iv_avatar;
    private TextView tv_name_user;
    private UserInfoDetailBean bean;
    private LinearLayout listviewTopView;
    private TextView tv_person_value;
    private TextView tv_character_percent;
    private LinearLayout footerView;
    private ImageView footerimage;
    private int pageNum = 1;
    private int lastItem;
    private int count;
    private int pageCount = 1;
    private boolean b = true;//控制listviewfooter执行次数
    private String emobid;

    private ImageView iv_user_type; /// 用户帮内级别

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_praise);
        initTitle(null, "RP值", null);
        init();
        initData();
    }

    private void init() {
        listView = (ListView) findViewById(R.id.lv_my_praise);
        listviewTopView = (LinearLayout) View.inflate(this, R.layout.life_circle_top_view_rpvalue, null);
        praise_rp = (LinearLayout) listviewTopView.findViewById(R.id.praise_rp);
        iv_avatar = (ImageView) listviewTopView.findViewById(R.id.iv_avatar);
        iv_user_type = (ImageView) listviewTopView.findViewById(R.id.iv_user_type);

        tv_name_user = (TextView) listviewTopView.findViewById(R.id.tv_name_user);
        tv_person_value = (TextView) listviewTopView.findViewById(R.id.tv_person_value);
        tv_character_percent = (TextView) listviewTopView.findViewById(R.id.tv_character_percent);

        footerView = (LinearLayout) View.inflate(this, R.layout.item_grid_footer, null);
        footerimage = (ImageView) footerView.findViewById(R.id.footview);
        footerView.findViewById(R.id.tv_temp).setVisibility(View.INVISIBLE);
        BaseUtils.setLoadingImageAnimation(footerimage);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                Log.i(TAG, "scrollState="+scrollState+"  lastItem="+lastItem+"  count="+count);
                //下拉到空闲是，且最后一个item的数等于数据的总数时，进行更新
                if (lastItem == count && scrollState == this.SCROLL_STATE_IDLE) {
//                    Log.i("debugg", "拉到最底部");
                    if (pageNum > pageCount) {
//                        listView.removeFooterView(footerView);
                        if (b) {
                            footerView.setVisibility(View.GONE);
                            footerimage.clearAnimation();
                            b = false;
                        }
                        showNoMoreToast();
                    } else {
                        pageNum++;
                        getPageRPValue();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.i(TAG, "firstVisibleItem=" + firstVisibleItem + "\nvisibleItemCount=" +
                        visibleItemCount + "\ntotalItemCount" + totalItemCount);
                lastItem = firstVisibleItem + visibleItemCount - 2;
            }
        });

//       List<RPValueBean> lists = new ArrayList<RPValueBean>();
//       lists.add(new RPValueBean("drawable://" + R.drawable.chat_file_pressed,"测试名字","10"));
//        lists.add(new RPValueBean("drawable://" + R.drawable.chat_file_pressed,"测试名字1","10"));
//        lists.add(new RPValueBean("drawable://" + R.drawable.chat_file_pressed,"测试名字2","10"));
//        lists.add(new RPValueBean("drawable://" + R.drawable.chat_file_pressed,"测试名字3","10"));
//        lists.add(new RPValueBean("drawable://" + R.drawable.chat_file_pressed,"测试名字4","10"));
//        lists.add(new RPValueBean("drawable://" + R.drawable.chat_file_pressed,"测试名字5","10"));
//
//        adapter = new RPValueAdapter(this,lists);
//
//        listView.setAdapter(adapter);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                RPValueBean rpValueBean = (RPValueBean) adapter.getItem(position);
//                Toast.makeText(MyPraiseActivity.this,""+rpValueBean.getName(),Toast.LENGTH_SHORT).show();
//            }
//        });


    }

    private void initData() {
        bean = PreferencesUtil.getLoginInfo(this);
        emobid = getIntent().getStringExtra("emobid");
        if (bean.getEmobId().equals(emobid)) {
            praise_rp.setVisibility(View.INVISIBLE);
        } else {
//            initTitle(null,"TA的生活圈",null);
        }
//        ImageLoader.getInstance().displayImage(bean.getAvatar(), iv_avatar);
//        tv_name_user.setText(bean.getNickname());

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
                                showNetErrorToast();
                                break;
                        }
                        praise_rp.setClickable(true);
                    }
                };
                praise_rp.setClickable(false);
                FriendZoneUtil.zambia(emobid, 0, 1, MyPraiseActivity.this, handler);
            }
        });

        //// 加载用户的个人信息
        NetBaseUtils.extractSimpleUserInfo(getmContext(), PreferencesUtil.getCommityId(getmContext()), emobid, new NetBaseUtils.NetRespListener<CommonRespBean<SimpleUserInfoBean>>() {
            @Override
            public void successYes(CommonRespBean<SimpleUserInfoBean> commonRespBean, Response response) {
                ImageLoader.getInstance().displayImage(commonRespBean.getData().getAvatar(), iv_avatar, UserUtils.options);
                initBangzhuMedal(commonRespBean.getData().getGrade());
                tv_name_user.setText(commonRespBean.getData().getNickname());
                tv_person_value.setText("" + commonRespBean.getData().getCharacterValues());
                try {
                    tv_character_percent.setText("打败了" + StrUtils.getPrecent(commonRespBean.getData().getCharacterPercent()) + "%的" + PreferencesUtil.getCommityName(MyPraiseActivity.this) + "小区居民！");
                } catch (Exception e) {
                }
                if (PreferencesUtil.getLogin(MyPraiseActivity.this)) {
                    if (emobid.equals(PreferencesUtil.getLoginInfo(MyPraiseActivity.this).getEmobId())) {
                        PreferencesUtil.saveRp(MyPraiseActivity.this, commonRespBean.getData().getCharacterValues() + "", commonRespBean.getData().getCharacterPercent() + "");
                    }
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
        getRPValue(emobid,getmContext());
    }


    interface RPValueService {
//        @GET("/api/v1/communities/{communityId}/circles/{emobId}/praises")
//        void getRPValue(@Path("communityId") int communityId, @Path("emobId") String emobId, @QueryMap Map<String, String> map, Callback<RPValueAllBean> cb);

//        @GET("/api/v1/communities/{communityId}/circles/{emobId}/praises")

        ////api/v3/lifePraises?emobId={用户环信ID}&page={页码}&limit={页面大小}

        @GET("/api/v3/lifePraises")
            ///v3 2016/03/04
        void getRPValue(@QueryMap Map<String, String> map, Callback<CommonRespBean<RPValueAllBean>> cb);
    }

    public  void getRPValue(String emobid, Context context) {
        HashMap<String, String> option = new HashMap<>();
        option.put("page", "1");
        option.put("limit", "10");
        option.put("emobId", emobid);
//        emobId={用户环信ID}&page={页码}&limit={页面大小}

        RPValueService service = RetrofitFactory.getInstance().create(context, option, RPValueService.class);
        Callback<CommonRespBean<RPValueAllBean>> callback = new Callback<CommonRespBean<RPValueAllBean>>() {
            @Override
            public void success(CommonRespBean<RPValueAllBean> bean, Response response) {
                if ("yes".equals(bean.getStatus())&&bean.getData()!=null) {
                    adapter = new RPValueAdapter(MyPraiseActivity.this, bean.getData().getData());
                    listView.addFooterView(footerView);
                    footerView.setVisibility(View.GONE);
                    listView.addHeaderView(listviewTopView);
                    listView.setAdapter(adapter);
                    count = adapter.getCount();
                } else {
                    showDataErrorToast(bean.getMessage());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                showToast("请求失败");
            }
        };
        service.getRPValue(option, callback);
    }


    /**
     * 初始化用户横条奖章图片
     */
    private void initBangzhuMedal(String userType) {
        if (iv_user_type != null) {
//            normal , bangzhu, fubangzhu ,zhanglao,bangzhong
            if (TextUtils.equals(userType, Config.USER_TYPE_ZHANGLAO)) {
                iv_user_type.setImageDrawable(getResources().getDrawable(R.drawable.me_zhanglao_icon));
                iv_user_type.setVisibility(View.VISIBLE);
            } else if (TextUtils.equals(userType, Config.USER_TYPE_BANGZHU)) {
                iv_user_type.setImageDrawable(getResources().getDrawable(R.drawable.me_bangzhu_icon));
                iv_user_type.setVisibility(View.VISIBLE);
            } else if (TextUtils.equals(userType, Config.USER_TYPE_FUBANGZHU)) {
                iv_user_type.setImageDrawable(getResources().getDrawable(R.drawable.me_fubangzhu_icon));
                iv_user_type.setVisibility(View.VISIBLE);
            } else {
                iv_user_type.setVisibility(View.GONE);
            }
        }
    }


    interface RPValuePageService {
//        @GET("/api/v1/communities/{communityId}/circles/{emobId}/praises")
//        void getRPValue(@Path("communityId") long communityId, @Path("emobId") String emobId, @QueryMap Map<String, String> map, Callback<RPValuePageBean> cb);
//

        @GET("/api/v3/lifePraises")
            ///v3 2016/03/04
        void getRPValue(@QueryMap Map<String, String> map, Callback<CommonRespBean<RPValueAllBean>> cb);

    }

    private void getPageRPValue() {

        HashMap<String, String> option = new HashMap<>();
        option.put("page", "" + pageNum);
        option.put("limit", "10");
        option.put("emobId", emobid);
        RPValuePageService service = RetrofitFactory.getInstance().create(getmContext(), option, RPValuePageService.class);
        Callback<CommonRespBean<RPValueAllBean>> callback = new Callback<CommonRespBean<RPValueAllBean>>() {
            @Override
            public void success(CommonRespBean<RPValueAllBean> bean, Response response) {
                if ("yes".equals(bean.getStatus()) && bean.getData() != null) {
                    adapter.addData(bean.getData().getData());
                    count = adapter.getCount();
                }
                footerView.setVisibility(View.GONE);
            }

            @Override
            public void failure(RetrofitError error) {
                footerView.setVisibility(View.GONE);
                footerimage.clearAnimation();
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        footerView.setVisibility(View.VISIBLE);
        service.getRPValue(option, callback);
    }


    @Override
    public void onClick(View v) {

    }
}
