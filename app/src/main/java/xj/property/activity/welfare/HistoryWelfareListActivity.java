package xj.property.activity.welfare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
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
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.WelfareHistoryBean;
import xj.property.beans.WelfareHistoryV3Bean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.DensityUtil;
import xj.property.utils.other.BaseUtils;
import xj.property.utils.other.PreferencesUtil;

public class HistoryWelfareListActivity extends HXBaseActivity implements PullToRefreshLayout.OnRefreshListener {

    private int pageNum = 1;

    private int lastItem;

    private int count;

    private int pageCount = 1;

    private String pageSize = "10";


    LinearLayout footerView;
    ImageView footerimage;
    private LinearLayout ll_errorpage, ll_neterror, ll_nomessage;
    ImageView iv_nomessage_image;

    private List<WelfareHistoryV3Bean.WelfareHistoryDataV3Bean> pageData = new ArrayList<WelfareHistoryV3Bean.WelfareHistoryDataV3Bean>();
    MyAdapter adapter = new MyAdapter();
    private int screenWidth;

    private View headView;
    private PullToRefreshLayout pull_to_refreshlayout;
    private PullListView lv_history_welfare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_welfare_list);
        initView();
        initData();
    }

    private void initView() {

        initTitle(null, "往期福利", "消息测试");

        screenWidth = getWindowManager().getDefaultDisplay().getWidth() - DensityUtil.dip2px(this, 30f);

        pull_to_refreshlayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refreshlayout);
        pull_to_refreshlayout.setOnRefreshListener(this);
        lv_history_welfare = (PullListView) findViewById(R.id.lv_history_welfare);
        lv_history_welfare.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (lv_history_welfare.getLastVisiblePosition() == (lv_history_welfare.getCount() - 1)) {
                            pull_to_refreshlayout.autoLoad();
                        }
                        // 判断滚动到顶部
                        if (lv_history_welfare.getFirstVisiblePosition() == 0) {

                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


            }
        });

        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        ll_nomessage = (LinearLayout) findViewById(R.id.ll_nomessage);
        iv_nomessage_image = (ImageView) findViewById(R.id.iv_nomessage_image);
        iv_nomessage_image.setImageResource(R.drawable.tikuanjilu_people);
        ll_neterror.setOnClickListener(this);

        footerView = (LinearLayout) View.inflate(this, R.layout.item_grid_footer, null);
        footerimage = (ImageView) footerView.findViewById(R.id.footview);
        footerView.findViewById(R.id.tv_temp).setVisibility(View.INVISIBLE);
        BaseUtils.setLoadingImageAnimation(footerimage);

        if (headView == null) {
            headView = View.inflate(this, R.layout.circyle_headview, null);
            lv_history_welfare.addHeaderView(headView);
        }
        lv_history_welfare.setAdapter(adapter);

    }

    private void initData() {
        pull_to_refreshlayout.autoRefresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_neterror:
                getWelfareHistoryPageData();
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pageNum = 1;
        getWelfareHistoryPageData();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        pageNum++;
        getWelfareHistoryPageData();
    }

    interface WelfareHistoryService {
        @GET("/api/v1/communities/{communityId}/welfares")
        void getDate(@Path("communityId") int communityId, @QueryMap Map<String, String> map, Callback<WelfareHistoryBean> cb);

        @GET("/api/v3/welfares/history")
        void getWelfaresDate(@QueryMap Map<String, String> map, Callback<CommonRespBean<WelfareHistoryV3Bean>> cb);
    }

    private void getWelfareHistoryPageData() {

        HashMap<String, String> option = new HashMap<String, String>();
        option.put("page", "" + pageNum);
        option.put("limit", pageSize);
        option.put("communityId", PreferencesUtil.getCommityId(this)+"");
        WelfareHistoryService service = RetrofitFactory.getInstance().create(getmContext(),option,WelfareHistoryService.class);
        Callback<CommonRespBean<WelfareHistoryV3Bean>> callback = new Callback<CommonRespBean<WelfareHistoryV3Bean>>() {
            @Override
            public void success(CommonRespBean<WelfareHistoryV3Bean> bean, retrofit.client.Response response) {
                if (bean != null) {
                    if ("yes".equals(bean.getStatus())) {
                        ll_errorpage.setVisibility(View.GONE);
                        ll_nomessage.setVisibility(View.GONE);
                        ll_neterror.setVisibility(View.GONE);

                        List<WelfareHistoryV3Bean.WelfareHistoryDataV3Bean> pageDatanew = bean.getData().getData();
                        if (pageData.size() > 0) {
                            if (pageDatanew == null || pageDatanew.size() <= 0) {
                                showNoMoreToast();
                            }
                        }
                        if (pageNum == 1) {
                            pageData.clear();
                        }
                        pageData.addAll(pageDatanew);
                        adapter.notifyDataSetChanged();
                        pageCount = bean.getData().getPage();
                        count = adapter.getCount();
                    } else {
                        showToast(bean.getMessage());
                    }
                } else {
                    showNetErrorToast();
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
                error.printStackTrace();
                showNetErrorToast();

                if (pageNum == 1) {
                    pull_to_refreshlayout.refreshFinish(true);
                } else {
                    pull_to_refreshlayout.loadMoreFinish(true);
                }
                if (count == 0) {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_neterror.setVisibility(View.VISIBLE);
                }

            }
        };
        service.getWelfaresDate(option,callback);
    }

    private class MyAdapter extends BaseAdapter {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");

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
                convertView = View.inflate(HistoryWelfareListActivity.this, R.layout.welfare_history_list_itme, null);
                viewHolder = new ViewHolder(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
//            Log.i("debbug", "size=" + pageData.size());
//            Log.i("debbug", "viewHolder=" + viewHolder);
//            Log.i("debbug", "viewholder.tvname=" + viewHolder.tv_welfare_goods_name);
            viewHolder.tv_welfare_goods_name.setText("" + pageData.get(position).getTitle());
            viewHolder.tv_welfare_goods_time.setText("" + format.format(new Date(pageData.get(position).getEndTime() * 1000L)));
            ImageLoader.getInstance().displayImage(pageData.get(position).getPoster(), viewHolder.iv_history_image, options);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HistoryWelfareListActivity.this, ActivityWelfareIndex.class);
                    intent.setFlags(500);
                    intent.putExtra("welfareId", "" + pageData.get(position).getWelfareId());
                    startActivity(intent);

                }
            });

            return convertView;
        }


        class ViewHolder {
            ImageView iv_history_image;
            ImageView iv_history_status_image;
            TextView tv_welfare_goods_name, tv_welfare_goods_time;

            ViewHolder(View v) {
                iv_history_image = (ImageView) v.findViewById(R.id.iv_history_image);
                iv_history_status_image = (ImageView) v.findViewById(R.id.iv_history_status_image);
                tv_welfare_goods_name = (TextView) v.findViewById(R.id.tv_welfare_goods_name);
                tv_welfare_goods_time = (TextView) v.findViewById(R.id.tv_welfare_goods_time);

                RelativeLayout.LayoutParams rlparams = (RelativeLayout.LayoutParams) iv_history_image.getLayoutParams();
                rlparams.width = screenWidth;
                rlparams.height = screenWidth * 5 / 7;
                iv_history_image.setLayoutParams(rlparams);
                v.setTag(this);
            }
        }

        private DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_picture)
                .showImageForEmptyUri(R.drawable.default_picture)
                .showImageOnFail(R.drawable.default_picture)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
//                .displayer(new RoundedBitmapDisplayer(20))
                .build();
    }

}
