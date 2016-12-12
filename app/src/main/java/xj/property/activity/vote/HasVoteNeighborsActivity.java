package xj.property.activity.vote;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
import xj.property.beans.HasVoteMoreNeighborRespBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.CommonUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

/**
 * 已经投过票的邻居 , 列表
 */
public class HasVoteNeighborsActivity extends HXBaseActivity implements PullToRefreshLayout.OnRefreshListener {

    private LinearLayout ll_errorpage;
    private LinearLayout ll_neterror;
    private LinearLayout ll_nomessage;
    private ImageView iv_nomessage_image;
    private TextView tv_nomessage;

    private UserInfoDetailBean bean;

    private MyAdapter adapter = new MyAdapter();

    private List<HasVoteMoreNeighborRespBean.PageDataEntity> pageData = new ArrayList<>();

    private int pageNum = 1;

    private String pageSize = "10";

    private String voteId;

    //// pull to refresh layoutf
    private PullToRefreshLayout pull_to_refreshlayout;
    ////
    private PullListView pull_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_neighbors_list);

        bean = PreferencesUtil.getLoginInfo(this);
        if (getIntent() != null) {
            voteId = getIntent().getStringExtra("voteId");
        } else {
            showToast("数据异常");
            finish();
            return;
        }

        if (!TextUtils.isEmpty(voteId)) {
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
            getMyTagedListInfo();

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
        tv_nomessage.setText("还没有邻居给你投票");

        ll_neterror.setOnClickListener(this);

        pull_to_refreshlayout = (PullToRefreshLayout) this.findViewById(R.id.pull_to_refreshlayout);
        pull_to_refreshlayout.setOnRefreshListener(this);

        pull_listview = (PullListView) this.findViewById(R.id.pull_listview);
        pull_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (pull_listview.getLastVisiblePosition() == (pull_listview.getCount() - 1)) {
                            pull_to_refreshlayout.autoLoad();
                        }
                        // 判断滚动到顶部
                        if (pull_listview.getFirstVisiblePosition() == 0) {

                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


            }
        });

        pull_listview.setAdapter(adapter);

    }

    private void initTitle() {

        this.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("投过票的邻居");

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

        pull_listview.post(new Runnable() {
            @Override
            public void run() {
                pageNum = 1;
                getMyTagedListInfo();
            }
        });

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

        pull_listview.post(new Runnable() {
            @Override
            public void run() {
                pageNum++;
                getMyTagedListInfo();
            }
        });
    }


    interface MspCardListService {
//        @GET("/api/v1/communities/{communityId}/vote/neighbor")
//        void getWelfareInfo(@Path("communityId") String communityId, @QueryMap Map<String, String> map, Callback<HasVoteMoreNeighborRespBean> cb);
//        @GET("/api/v1/communities/{communityId}/vote/neighbor")

//        /api/v3/votes/{投票ID}/neighbor?page={页码}&limit={页面大小}
        @GET("/api/v3/votes/{voteId}/neighbor")
        void getWelfareInfo(@Path("voteId") String voteId, @QueryMap Map<String, String> map, Callback<CommonRespBean<HasVoteMoreNeighborRespBean>> cb);
    }

    private void getMyTagedListInfo() {
        HashMap<String, String> option = new HashMap<String, String>();
        option.put("page", "" + pageNum);
        option.put("limit", pageSize);
        MspCardListService service = RetrofitFactory.getInstance().create(getmContext(),option,MspCardListService.class);
        Callback<CommonRespBean<HasVoteMoreNeighborRespBean>> callback = new Callback<CommonRespBean<HasVoteMoreNeighborRespBean>>() {
            @Override
            public void success(CommonRespBean<HasVoteMoreNeighborRespBean> bean, retrofit.client.Response response) {
                if (bean != null && "yes".equals(bean.getStatus())&& bean.getData()!=null) {
                    ll_errorpage.setVisibility(View.GONE);
                    ll_nomessage.setVisibility(View.GONE);
                    ll_neterror.setVisibility(View.GONE);
                    List<HasVoteMoreNeighborRespBean.PageDataEntity> pageDatanew = bean.getData().getData();
                    if (pageData.size() > 0) {
                        if (pageDatanew == null || pageDatanew.size() < 1) {
                            showNoMoreToast();
                        }
                    }
                    if (pageNum == 1) {
                        pageData.clear();
                    }
                    if (pageData == null) {
                        pageData = new ArrayList<>();
                    }
                    if (pageDatanew != null && pageDatanew.size() > 0) {
                        pageData.addAll(pageDatanew);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    showNetErrorToast();
                }
                if (adapter.getCount() == 0) {
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
                if (pageNum == 1) {
                    pull_to_refreshlayout.refreshFinish(true);
                } else {
                    pull_to_refreshlayout.loadMoreFinish(true);
                }

                if (adapter.getCount() == 0) {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_neterror.setVisibility(View.VISIBLE);
                }

                error.printStackTrace();
                showNetErrorToast();
            }
        };
        service.getWelfareInfo(voteId, option, callback);
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
                convertView = View.inflate(getmContext(), R.layout.common_vote_details_morevote_item, null);
                viewHolder = new ViewHolder(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Log.i("debbug", "size=" + pageData.size());
            Log.i("debbug", "viewHolder=" + viewHolder);

            viewHolder.whotags_me_name_tv.setText(pageData.get(position).getNickname());////TODO  2016/03/15  NICKNAME

            Date tag_time_date = new Date(pageData.get(position).getCreateTime() * 1000L);

            viewHolder.whotags_me_tag_time_tv.setText(format.format(tag_time_date));
            ///2015/12/10 add grade field
            initBangzhuMedal(pageData.get(position).getGrade(), viewHolder); /// TODO  2016/03/15   grade

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

        /**
         * 初始化用户横条奖章图片
         */
        private void initBangzhuMedal(String userType, ViewHolder holder) {
            if (holder.provider_iv_user_type != null) {
//            normal , bangzhu, fubangzhu ,zhanglao,bangzhong
                if (TextUtils.equals(userType, "zhanglao")) {
                    holder.provider_iv_user_type.setImageDrawable(getResources().getDrawable(R.drawable.life_circle_zhanglao_icon));
                    holder.provider_iv_user_type.setVisibility(View.VISIBLE);
                } else if (TextUtils.equals(userType, "bangzhu")) {
                    holder.provider_iv_user_type.setImageDrawable(getResources().getDrawable(R.drawable.life_circle_bangzhu_icon));
                    holder.provider_iv_user_type.setVisibility(View.VISIBLE);
                } else if (TextUtils.equals(userType, "fubangzhu")) {
                    holder.provider_iv_user_type.setImageDrawable(getResources().getDrawable(R.drawable.life_circle_fubangzhu_icon));
                    holder.provider_iv_user_type.setVisibility(View.VISIBLE);
                } else {
                    holder.provider_iv_user_type.setVisibility(View.GONE);
                }
            }
        }


        class ViewHolder {
            ImageView iv_avatar;
            TextView whotags_me_name_tv;
            TextView whotags_me_tag_time_tv;
            ImageView provider_iv_user_type;


            ViewHolder(View v) {

                iv_avatar = (ImageView) v.findViewById(R.id.iv_avatar);
                provider_iv_user_type = (ImageView) v.findViewById(R.id.provider_iv_user_type);
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
