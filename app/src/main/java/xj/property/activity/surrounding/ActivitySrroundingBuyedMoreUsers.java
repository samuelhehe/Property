package xj.property.activity.surrounding;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

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
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.beans.SrroudingBuyMoreUserRespBean;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.pullrefreshview.library.PullToRefreshBase;
import xj.property.widget.pullrefreshview.library.PullToRefreshListView;

/**
 * 周边查看更多已购买用户
 */
public class ActivitySrroundingBuyedMoreUsers extends HXBaseActivity {


    private static final String TAG = "ActivityMSPBuyedMoreUsers";
    private String crazySalesId;

    private ListView buyed_usrs_lv;

    private MyAdapter adapter = new MyAdapter();


    private int pageNum = 1;
    private int count;

    private int pageCount = 1;

    private String pageSize = "10";

    private List<SrroudingBuyMoreUserRespBean.InfoEntity.PageDataEntity>  pageData = new ArrayList<SrroudingBuyMoreUserRespBean.InfoEntity.PageDataEntity>();


    private PullToRefreshListView pbuyed_usrs_lv;
    private View headView;

    private LinearLayout ll_errorpage;
    private LinearLayout ll_nomessage;
    private LinearLayout ll_neterror;
    private ImageView iv_nomessage_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srrounding_buyed_more_users);
        crazySalesId = getIntent().getStringExtra("crazySalesId");

        initView();
        initData();
    }

    private void initData() {

        if(!mLdDialog.isShowing()){
            mLdDialog.show();
        }
        getWelfareInfo(crazySalesId);
    }

    private void initView() {


        TextView tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("查看已购用户");
        tv_title.setVisibility(View.VISIBLE);

        this.findViewById(R.id.iv_back).setOnClickListener(this);


        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        ll_nomessage = (LinearLayout) findViewById(R.id.ll_nomessage);
        iv_nomessage_image = (ImageView) findViewById(R.id.iv_nomessage_image);
        iv_nomessage_image.setImageResource(R.drawable.tikuanjilu_people);
        ll_neterror.setOnClickListener(this);

        pbuyed_usrs_lv = (PullToRefreshListView) this.findViewById(R.id.buyed_usrs_lv);

        buyed_usrs_lv = pbuyed_usrs_lv.getRefreshableView();

        if (headView == null) {
            headView = View.inflate(this, R.layout.circyle_headview, null);
            buyed_usrs_lv.addHeaderView(headView);
        }

        buyed_usrs_lv.setAdapter(adapter);

        pbuyed_usrs_lv.setOnRefreshListener(

                new PullToRefreshBase.OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                        String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                        pageNum = 1;
                        getWelfareInfo(crazySalesId);
                    }
                }
        );


        pbuyed_usrs_lv.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                pbuyed_usrs_lv.mFooterLoadingView.setVisibility(View.VISIBLE);
                pbuyed_usrs_lv.mFooterLoadingView.refreshing();
                pageNum++;
                getWelfareInfo(crazySalesId);
            }
        });


    }

    interface WelfareBuyedMoreUsrsService {

        @GET("/api/v1/crazysales/{crazySalesId}/communities/{communityId}/users")
        void getCommunitiesUsers(@Path("crazySalesId") String crazySalesId, @Path("communityId") String communityId, @QueryMap Map<String, String> map, Callback<SrroudingBuyMoreUserRespBean> cb);
    }

    private void getWelfareInfo(String shopEmobId) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        WelfareBuyedMoreUsrsService service = restAdapter.create(WelfareBuyedMoreUsrsService.class);
        Callback<SrroudingBuyMoreUserRespBean> callback = new Callback<SrroudingBuyMoreUserRespBean>() {
            @Override
            public void success(SrroudingBuyMoreUserRespBean bean, retrofit.client.Response response) {
                if (bean != null) {

                    if ("yes".equals(bean.getStatus())) {

                        ll_errorpage.setVisibility(View.GONE);
                        ll_nomessage.setVisibility(View.GONE);
                        ll_neterror.setVisibility(View.GONE);

                        if (pageNum == 1) {
                            pageData.clear();
                            pageData.addAll(bean.getInfo().getPageData());
                        } else {
                            pageData.addAll(bean.getInfo().getPageData());
                        }

                        count = adapter.getCount();
                        adapter.notifyDataSetChanged();
                        pageCount = bean.getInfo().getPageCount();

                    } else {
                        showToast(bean.getMessage());
                    }
                } else {
                    showToast("数据异常");
                    if (count == 0) {
                        ll_errorpage.setVisibility(View.VISIBLE);
                        ll_nomessage.setVisibility(View.VISIBLE);
                    }
                }

                if(mLdDialog!=null&&mLdDialog.isShowing()){
                    mLdDialog.dismiss();
                }

                pbuyed_usrs_lv.onRefreshComplete();

                if (bean.getInfo().getPageData().isEmpty()) {
                    pbuyed_usrs_lv.mFooterLoadingView.setVisibility(View.GONE);
                    showNoMoreToast();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                pbuyed_usrs_lv.onRefreshComplete();
                if (adapter.getCount() <= 0) {
                    pbuyed_usrs_lv.mFooterLoadingView.setVisibility(View.GONE);
                    showToast("数据异常");
                }
                if (count == 0) {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_neterror.setVisibility(View.VISIBLE);
                }
                error.printStackTrace();
                showNetErrorToast();
            }
        };


        Map<String, String> option = new HashMap<String, String>();
        option.put("pageNum", "" + pageNum);
        option.put("pageSize", pageSize);

        service.getCommunitiesUsers(crazySalesId,""+ PreferencesUtil.getCommityId(this), option, callback);
    }


    private class MyAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return pageData.size();
        }

        @Override
        public Object getItem(int position) {
            return pageData;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(ActivitySrroundingBuyedMoreUsers.this, R.layout.common_srrounding_purchase_moreusr_item, null);
                viewHolder = new ViewHolder(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Log.i("debbug", "size=" + pageData.size());
            Log.i("debbug", "viewHolder=" + viewHolder);
            Log.i("debbug", "viewholder.tvname=" + viewHolder.welfare_purchase_hasgoturs_name_tv);

            viewHolder.welfare_purchase_hasgoturs_name_tv.setText("" + pageData.get(position).getNickname());
            ImageLoader.getInstance().displayImage(pageData.get(position).getAvatar(), viewHolder.iv_avatar, options);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("debug ","adapter.getCount"+adapter.getCount()+ " pageData.size("+ pageData.size() + "position "+ position);

                    startActivity(new Intent(ActivitySrroundingBuyedMoreUsers.this, UserGroupInfoActivity.class).putExtra(Config.INTENT_PARMAS2, pageData.get(position).getEmobId()));
                }
            });


            return convertView;
        }


        class ViewHolder {
            ImageView iv_avatar;
            TextView welfare_purchase_hasgoturs_name_tv;

            ViewHolder(View v) {
                iv_avatar = (ImageView) v.findViewById(R.id.iv_avatar);
                welfare_purchase_hasgoturs_name_tv = (TextView) v.findViewById(R.id.welfare_purchase_hasgoturs_name_tv);
                v.setTag(this);
            }
        }

        private DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.head_portrait_personage)
                .showImageForEmptyUri(R.drawable.head_portrait_personage)
                .showImageOnFail(R.drawable.head_portrait_personage)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_neterror:

                getWelfareInfo(crazySalesId);

                break;
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_activity_welfare_buyed_more_users, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


}
