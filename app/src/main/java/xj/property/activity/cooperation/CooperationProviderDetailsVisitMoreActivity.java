package xj.property.activity.cooperation;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.repo.xw.library.views.PullListView;
import com.repo.xw.library.views.PullToRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import xj.property.beans.NeighborMoreBean;
import xj.property.beans.ProviderDetailsVisitMoreRespBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.CommonUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

/**
 * 谁找过TA , 列表
 */
public class CooperationProviderDetailsVisitMoreActivity extends HXBaseActivity implements PullToRefreshLayout.OnRefreshListener {

    private LinearLayout ll_errorpage;
    private LinearLayout ll_neterror;
    private LinearLayout ll_nomessage;
    private ImageView iv_nomessage_image;
    private TextView tv_nomessage;

    private View headView;

    private UserInfoDetailBean bean;

    private MyAdapter adapter = new MyAdapter();

    private List<NeighborMoreBean.NeighborMoreUser> pageData = new ArrayList<NeighborMoreBean.NeighborMoreUser>();

    private int pageNum = 1;
    private int count;
//    private int pageCount;

    private String pageSize = "10";

    private String cooperationId;
    private PullToRefreshLayout pull_to_refreshlayout;
    private PullListView msp_lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_visitme_list);
        bean = PreferencesUtil.getLoginInfo(this);
        if (getIntent() != null) {
            cooperationId = getIntent().getStringExtra("cooperationId");
        } else {
            showToast("数据异常");
            finish();
            return;
        }

        if (!TextUtils.isEmpty(cooperationId)) {
            initView();
            initData();
        } else {
//            Intent intent = new Intent(this, RegisterLoginActivity.class);
//            startActivity(intent);
            showToast("数据异常");
            finish();
            return;
        }

    }

    private void initData() {

        if (CommonUtils.isNetWorkConnected(this)) {
            pull_to_refreshlayout.autoRefresh();
        } else {
            ll_errorpage.setVisibility(View.VISIBLE);
            ll_neterror.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        initTitle();

        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        ll_nomessage = (LinearLayout) findViewById(R.id.ll_nomessage);

        iv_nomessage_image = (ImageView) findViewById(R.id.iv_nomessage_image);
        iv_nomessage_image.setImageResource(R.drawable.tikuanjilu_people);

        tv_nomessage = (TextView) findViewById(R.id.tv_nomessage);
        tv_nomessage.setText("");

        ll_neterror.setOnClickListener(this);

        pull_to_refreshlayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refreshlayout);
        pull_to_refreshlayout.setOnRefreshListener(this);
        msp_lv = (PullListView) findViewById(R.id.msp_lv);
        if (headView == null) {
            headView = View.inflate(this, R.layout.circyle_headview, null);
            msp_lv.addHeaderView(headView);
        }
        msp_lv.setAdapter(adapter);
    }

    private void initTitle() {
        this.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("找过他的邻居");

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pageNum = 1;
        getMyTagedListInfo();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        pageNum++;
        getMyTagedListInfo();
    }

    interface MspCardListService {
//        @GET("/api/v1/communities/{communityId}/cooperations/{cooperationId}/more")
//        void getCooperationsMore(@Path("communityId") String communityId, @Path("cooperationId") String cooperationId, @QueryMap Map<String, String> map, Callback<ProviderDetailsVisitMoreRespBean> cb);
    // /api/v3/cooperations/1/users

        @GET("/api/v3/cooperations/{cooperationId}/users")
        void getCooperationsMoreV3(@Path("cooperationId") String cooperationId, @QueryMap Map<String, String> map, Callback<CommonRespBean<NeighborMoreBean>> cb);
    }

    private void getMyTagedListInfo() {

        HashMap<String, String> option = new HashMap<String, String>();
        option.put("page", "" + pageNum);
        option.put("limit", pageSize);
        option.put("communityId", ""+PreferencesUtil.getCommityId(getmContext()));

        MspCardListService service = RetrofitFactory.getInstance().create(getmContext(),option,MspCardListService.class);
        Callback<CommonRespBean<NeighborMoreBean>> callback = new Callback<CommonRespBean<NeighborMoreBean>>() {
            @Override
            public void success(CommonRespBean<NeighborMoreBean> bean, retrofit.client.Response response) {

                if (bean != null && "yes".equals(bean.getStatus())) {
                    ll_errorpage.setVisibility(View.GONE);
                    ll_nomessage.setVisibility(View.GONE);
                    ll_neterror.setVisibility(View.GONE);

                    List<NeighborMoreBean.NeighborMoreUser> list = bean.getData().getData();
                    if (pageData.size() > 0) {
                        if (list == null || list.size() <= 0) {
                            showNoMoreToast();
                        }
                    }
                    if (pageNum == 1) {
                        pageData.clear();
                        pageData.addAll(list);
                    } else {
                        pageData.addAll(list);
                    }
                    count = adapter.getCount();
                    adapter.notifyDataSetChanged();
//                    pageCount = bean.getInfo().getPageCount();
                } else {
                    showToast(bean.getMessage());
                }

                if (count == 0) {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_nomessage.setVisibility(View.VISIBLE);
                }
                if (pageNum == 1) {
                    pull_to_refreshlayout.refreshFinish(true);
                } else {
                    pull_to_refreshlayout.loadMoreFinish(true);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (count == 0) {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_neterror.setVisibility(View.VISIBLE);
                }
                if (pageNum == 1) {
                    pull_to_refreshlayout.refreshFinish(true);
                } else {
                    pull_to_refreshlayout.loadMoreFinish(true);
                }
                error.printStackTrace();
                showNetErrorToast();

            }
        };

//        communityId=2&page=1&limit=10
        service.getCooperationsMoreV3(cooperationId, option, callback);

    }


    private class MyAdapter extends BaseAdapter {

        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");

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
                convertView = View.inflate(getmContext(), R.layout.common_cooperation_details_visit_item, null);
                viewHolder = new ViewHolder(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Log.i("debbug", "size=" + pageData.size());
            Log.i("debbug", "viewHolder=" + viewHolder);

            viewHolder.whotags_me_name_tv.setText(pageData.get(position).getNickname());

            Date tag_time_date = new Date(pageData.get(position).getCreateTime() * 1000L);

//            Date tag_time_date =  new Date(pageData.get(position).getCreateTime() * 1000L);  2015/11/23

            viewHolder.whotags_me_tag_time_tv.setText(format.format(tag_time_date));

            ImageLoader.getInstance().displayImage(pageData.get(position).getAvatar(), viewHolder.iv_avatar, UserUtils.msp_card_iv_options);

            viewHolder.iv_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("debug ", "adapter.getCount" + adapter.getCount() + " pageData.size(" + pageData.size() + "position " + position);

                    startActivity(new Intent(getmContext(), UserGroupInfoActivity.class).putExtra(Config.INTENT_PARMAS2, pageData.get(position).getEmobId()));
                }
            });

            return convertView;
        }

        class ViewHolder {
            ImageView iv_avatar;
            TextView whotags_me_name_tv;
            TextView whotags_me_tag_time_tv;


            ViewHolder(View v) {

                iv_avatar = (ImageView) v.findViewById(R.id.iv_avatar);
                whotags_me_name_tv = (TextView) v.findViewById(R.id.whotags_me_name_tv);
                whotags_me_tag_time_tv = (TextView) v.findViewById(R.id.whotags_me_tag_time_tv);

                v.setTag(this);
            }
        }

    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.ll_neterror:
                initData();
                break;
        }
    }


}
