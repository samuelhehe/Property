package xj.property.activity.LifeCircle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.adapter.RPValueTopListAdapter;
import xj.property.beans.RPListResult;
import xj.property.beans.RPTopListItem;
import xj.property.beans.TopListPhotoRespone;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

/**
 * 上周排行榜
 * v3 2016/03/04
 */
public class RPValueTopListActivity extends HXBaseActivity {
    UserInfoDetailBean userInfoDetailBean;
    ListView lv_top_list;
    List<RPTopListItem> list = new ArrayList<>();
    RPValueTopListAdapter adapter;
    TextView zan_value, top_value;

    //    boolean imageComplete,DataComplete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rpvalue_top_list);
        userInfoDetailBean = PreferencesUtil.getLoginInfo(this);
        initView();
        mLdDialog.show();
        getTopImage();
    }

    private void initView() {
        initTitle(null, "上周排行榜", null);
        zan_value = (TextView) findViewById(R.id.zan_value);
        top_value = (TextView) findViewById(R.id.top_value);
        lv_top_list = (ListView) findViewById(R.id.lv_top_list);
        lv_top_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RPValueTopListActivity.this, UserGroupInfoActivity.class);
                intent.putExtra(Config.INTENT_PARMAS2, list.get(position).getEmobId());
                startActivity(intent);
            }
        });
//        adapter=new RPValueTopListAdapter(this,lv_top_list,list);
//        lv_top_list.setAdapter(adapter);
    }

    interface RPTOPService {
//        @GET("/api/v1/communities/{communityId}/circles/{emobId}/hot")
//        void getRPList(@Path("communityId") long communityId, @Path("emobId") String emobId, Callback<RPListResult> cb);

        ////api/v3/lifePraises/rank?emobId={用户环信ID}&communityId={小区ID}

        @GET("/api/v3/lifePraises/rank") /// v3 2016/03/04
        void getRPList(@QueryMap HashMap<String, String> queryMap, Callback<CommonRespBean<RPListResult>> cb);
    }

    private void initData() {
        HashMap<String,String> hashMap  = new HashMap<>();
        hashMap.put("emobId", userInfoDetailBean == null ? "null" : userInfoDetailBean.getEmobId());
        hashMap.put("communityId",""+PreferencesUtil.getCommityId(getmContext()));

        RPTOPService service = RetrofitFactory.getInstance().create(getmContext(),hashMap,RPTOPService.class);
        Callback<CommonRespBean<RPListResult>> callback = new Callback<CommonRespBean<RPListResult>>() {
            @Override
            public void success(CommonRespBean<RPListResult> rpListResult, Response response) {
                mLdDialog.dismiss();
                if ("yes".equals(rpListResult.getStatus())) {
                    zan_value.setText("上周获赞人品" + rpListResult.getData().getUserCharacterValue());
                    top_value.setText("排名" + rpListResult.getData().getUserRank());
//                    list.addAll(rpListResult.info.list);
//                    adapter.notifyDataSetChanged();
                    list.clear();
                    list.addAll(rpListResult.getData().getList());
                    adapter = new RPValueTopListAdapter(RPValueTopListActivity.this, lv_top_list, list);
                    lv_top_list.setAdapter(adapter);
                }
//                pagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        service.getRPList(hashMap, callback);
    }

    interface RPImageService {
        @GET("/api/v2/communities/{communityId}/publicizePhoto/16")
        void getRPList(@Path("communityId") long communityId, Callback<TopListPhotoRespone> cb);
    }

    private void getTopImage() {

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE2).build();
        RPImageService service = restAdapter.create(RPImageService.class);
        Callback<TopListPhotoRespone> callback = new Callback<TopListPhotoRespone>() {
            @Override
            public void success(TopListPhotoRespone rpListResult, Response response) {
                if ("yes".equals(rpListResult.status)) {
                    ImageView big_img = (ImageView) findViewById(R.id.big_img);
                    if (!rpListResult.getInfo().isEmpty())
                        ImageLoader.getInstance().displayImage(rpListResult.getInfo().get(0).getImgUrl(), big_img, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {
                                initData();
                                if (view instanceof ImageView)
                                    ((ImageView) view).setImageResource(R.drawable.the_charts_picture);
                                ((TextView) findViewById(R.id.tv_commit_name)).setText(PreferencesUtil.getCommityName(RPValueTopListActivity.this) + "小区\n人品值排行榜");
                                ((TextView) findViewById(R.id.tv_commit_name)).setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                initData();
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {
                                initData();
                                if (view instanceof ImageView)
                                    ((ImageView) view).setImageResource(R.drawable.the_charts_picture);
                                ((TextView) findViewById(R.id.tv_commit_name)).setText(PreferencesUtil.getCommityName(RPValueTopListActivity.this) + "小区\n人品值排行榜");
                                ((TextView) findViewById(R.id.tv_commit_name)).setVisibility(View.VISIBLE);
                            }
                        });
                    else {
                        big_img.setImageResource(R.drawable.the_charts_picture);
                        ((TextView) findViewById(R.id.tv_commit_name)).setText(PreferencesUtil.getCommityName(RPValueTopListActivity.this) + "小区\n人品值排行榜");
                        ((TextView) findViewById(R.id.tv_commit_name)).setVisibility(View.VISIBLE);
                        initData();
                    }
                }
//                pagerAdapter.notifyDataSetChanged();

            }

            @Override
            public void failure(RetrofitError error) {
                initData();
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        service.getRPList(PreferencesUtil.getCommityId(RPValueTopListActivity.this), callback);

    }

    @Override
    public void onClick(View v) {

    }
}
