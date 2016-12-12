package xj.property.activity.cooperation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.sso.UMSsoHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.adapter.LastWeekRPValueTopListAdapter;
import xj.property.beans.RPListResult;
import xj.property.beans.RPTopListItem;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.provider.ShareProvider;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

/**
 * 上周人品排行榜 2015/11/18
 */
public class LastWeekRPValueTopListActivity extends HXBaseActivity {
    UserInfoDetailBean userInfoDetailBean;
    ListView lv_top_list;
    List<RPTopListItem> list = new ArrayList<>();
    LastWeekRPValueTopListAdapter adapter;
    TextView zan_value, top_value;
    //// 上周排行榜
    private TextView tv_title;
    private ImageView iv_back,iv_right;

    private ImageView iv_avatar;

    private Button toplist_enter_btn;
    private TextView tv_commit_name;
    private ImageView big_img;

    private int peopleNum=0;
    private int peopleTop=0;
//    private ShareProvider mShareProvider;

    //    boolean imageComplete,DataComplete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lastweek_rpvalue_top_list);
        userInfoDetailBean = PreferencesUtil.getLoginInfo(this);
        ShareProvider.getInitShareProvider(LastWeekRPValueTopListActivity.this);
        initView();
        mLdDialog.show();
//        getTopImage();
        initData();

    }

    private void initView() {
//        initTitle(null, "上周排行榜", null);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("上周排行榜");
        tv_title.setTextSize(17f);
        tv_title.setTextColor(getResources().getColor(R.color.sys_green_theme_text_color));
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        iv_back.setVisibility(View.INVISIBLE);
        iv_right=(ImageView)findViewById(R.id.iv_right);
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setBackgroundDrawable(getResources().getDrawable(R.drawable.share));
        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Config.NET_SHAREBASE+"/share/ranklist.html?communityId="+userInfoDetailBean.getCommunityId();
                ShareProvider.getShareProvider(LastWeekRPValueTopListActivity.this).showShareActivity(url, "我以“"+peopleNum+"”人品值，高居人品榜第“"+peopleTop+"”位！", "邻居帮帮", ShareProvider.CODE_CHART);
            }
        });
        iv_avatar = (ImageView) this.findViewById(R.id.iv_avatar);

        tv_commit_name = (TextView) findViewById(R.id.tv_commit_name);

        big_img = (ImageView) findViewById(R.id.big_img);
        big_img.setImageResource(R.drawable.the_charts_picture);

        tv_commit_name.setText(PreferencesUtil.getCommityName(getmContext()));
        tv_commit_name.setVisibility(View.VISIBLE);

        toplist_enter_btn = (Button) this.findViewById(R.id.toplist_enter_btn);
        toplist_enter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        zan_value = (TextView) findViewById(R.id.zan_value);
        top_value = (TextView) findViewById(R.id.top_value);
        lv_top_list = (ListView) findViewById(R.id.lv_top_list);
        lv_top_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LastWeekRPValueTopListActivity.this, UserGroupInfoActivity.class);
                intent.putExtra(Config.INTENT_PARMAS2, list.get(position).getEmobId());
                startActivity(intent);
            }
        });
    }

    /**
     * 获取ActList部分
     */
    interface RPTOPService {
//        @GET("/api/v1/communities/{communityId}/circles/{emobId}/hot")
//        void getRPList(@Path("communityId") int communityId, @Path("emobId") String emobId, Callback<CommonRespBean<RPListResult>> cb);

//        @GET("/api/v1/communities/{communityId}/circles/{emobId}/hot")
//        api/v3/lifePraises/rank?emobId=d463b16dfc014466a1e441dd685ba505&communityId=2

        @GET("/api/v3/lifePraises/rank") /// v3 2016/03/02
        void getRPList(@QueryMap HashMap<String, String> queryMap, Callback<CommonRespBean<RPListResult>> cb);

    }

    private void initData() {

        //        api/v3/lifePraises/rank?emobId=d463b16dfc014466a1e441dd685ba505&communityId=2
        HashMap<String,String> queryMap = new HashMap<>();
        queryMap.put("emobId",userInfoDetailBean == null ? "null" : userInfoDetailBean.getEmobId());
        queryMap.put("communityId",""+PreferencesUtil.getCommityId(LastWeekRPValueTopListActivity.this));

        RPTOPService service = RetrofitFactory.getInstance().create(getmContext(),queryMap,RPTOPService.class);
        Callback<CommonRespBean<RPListResult>> callback = new Callback<CommonRespBean<RPListResult>>() {
            @Override
            public void success(CommonRespBean<RPListResult> rpListResult, Response response) {
                mLdDialog.dismiss();
                if ("yes".equals(rpListResult.getStatus()) && rpListResult != null) {

//                    list.addAll(rpListResult.info.list);
//                    adapter.notifyDataSetChanged();
                    peopleNum=rpListResult.getData().getUserCharacterValue();
                    peopleTop = rpListResult.getData().getUserRank();
                    zan_value.setText("上周获赞人品" + peopleNum);
                    top_value.setText("排名" + peopleTop);
                    list.clear();
                    list.addAll(rpListResult.getData().getList());
                    if (list != null && list.size() > 0) {
                        ImageLoader.getInstance().displayImage(userInfoDetailBean.getAvatar(), iv_avatar, UserUtils.options);
                        iv_avatar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(LastWeekRPValueTopListActivity.this, UserGroupInfoActivity.class);
                                intent.putExtra(Config.INTENT_PARMAS2,userInfoDetailBean.getEmobId());
                                startActivity(intent);
                            }
                        });
                    }

                    adapter = new LastWeekRPValueTopListAdapter(LastWeekRPValueTopListActivity.this, lv_top_list, list);
                    lv_top_list.setAdapter(adapter);

                }
                tv_commit_name.setText(PreferencesUtil.getCommityName(LastWeekRPValueTopListActivity.this));
                tv_commit_name.setVisibility(View.VISIBLE);

//                pagerAdapter.notifyDataSetChanged();

            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
            }
        };

        service.getRPList(queryMap,callback);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMSsoHandler ssoHandler = ShareProvider.mController.getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

    }


//    interface RPImageService {
//        @GET("/api/v2/communities/{communityId}/publicizePhoto/16")
//        void getRPList(@Path("communityId") int communityId, Callback<TopListPhotoRespone> cb);
//    }
//
//    private void getTopImage() {
//
//        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE2).build();
//        RPImageService service = restAdapter.create(RPImageService.class);
//        Callback<TopListPhotoRespone> callback = new Callback<TopListPhotoRespone>() {
//            @Override
//            public void success(TopListPhotoRespone rpListResult, Response response) {
//                if ("yes".equals(rpListResult.status)) {
//                    if (!rpListResult.getInfo().isEmpty()) {
//
//                        ImageLoader.getInstance().displayImage(rpListResult.getInfo().get(0).getImgUrl(), big_img, new ImageLoadingListener() {
//                            @Override
//                            public void onLoadingStarted(String s, View view) {
//
//                            }
//
//                            @Override
//                            public void onLoadingFailed(String s, View view, FailReason failReason) {
//                                initData();
//                                if (view instanceof ImageView)
//                                    ((ImageView) view).setImageResource(R.drawable.the_charts_picture);
//                                tv_commit_name.setText(PreferencesUtil.getCommityName(LastWeekRPValueTopListActivity.this));
//                                tv_commit_name.setVisibility(View.VISIBLE);
//                            }
//
//                            @Override
//                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                                initData();
//                            }
//
//                            @Override
//                            public void onLoadingCancelled(String s, View view) {
//                                initData();
//                                if (view instanceof ImageView)
//                                    ((ImageView) view).setImageResource(R.drawable.the_charts_picture);
//                                tv_commit_name.setText(PreferencesUtil.getCommityName(LastWeekRPValueTopListActivity.this));
//                                tv_commit_name.setVisibility(View.VISIBLE);
//                            }
//                        });
//                    } else {
//                        big_img.setImageResource(R.drawable.the_charts_picture);
//                        tv_commit_name.setText(PreferencesUtil.getCommityName(LastWeekRPValueTopListActivity.this));
//                        tv_commit_name.setVisibility(View.VISIBLE);
//                        initData();
//                    }
//                }
////                pagerAdapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                initData();
//                error.printStackTrace();
//                showNetErrorToast();
//            }
//        };
//        service.getRPList(PreferencesUtil.getCommityId(LastWeekRPValueTopListActivity.this), callback);
//
//    }

//    @Override
//    public void onClick(View v) {
//
//    }
}
