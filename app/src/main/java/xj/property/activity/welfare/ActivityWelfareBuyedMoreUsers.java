package xj.property.activity.welfare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.repo.xw.library.views.PullListView;
import com.repo.xw.library.views.PullToRefreshLayout;

import java.util.ArrayList;
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
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.beans.PageDataEntityV3Bean;
import xj.property.beans.WelfareBuyedMoreUserInfo;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.other.Config;

/**
 * 查看更多已购买用户
 */
public class ActivityWelfareBuyedMoreUsers extends HXBaseActivity implements PullToRefreshLayout.OnRefreshListener {


    private static final String TAG = "ActivityWelfareBuyedMoreUsers";
    private int welfareId;

    private MyAdapter adapter = new MyAdapter();


    private int pageNum = 1;
    //    private int lastItem;
    private int count;
    private int pageCount = 1;

    private String pageSize = "10";

    private List<PageDataEntityV3Bean.PageDataEntityUserV3Bean> pageData = new ArrayList<PageDataEntityV3Bean.PageDataEntityUserV3Bean>();


    private View headView;

    private LinearLayout ll_errorpage;
    private LinearLayout ll_nomessage;
    private LinearLayout ll_neterror;
    private ImageView iv_nomessage_image;
    private PullToRefreshLayout pull_to_refreshlayout;
    private PullListView buyed_usrs_lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welfare_buyed_more_users);
        welfareId = getIntent().getIntExtra("welfareId", 0);

        initView();
        initData();

    }

    private void initData() {
//        getWelfareInfo(welfareId);
        pull_to_refreshlayout.autoRefresh();
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

        pull_to_refreshlayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refreshlayout);
        pull_to_refreshlayout.setOnRefreshListener(this);
        buyed_usrs_lv = (PullListView) findViewById(R.id.buyed_usrs_lv);
        buyed_usrs_lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (buyed_usrs_lv.getLastVisiblePosition() == (buyed_usrs_lv.getCount() - 1)) {
                            pull_to_refreshlayout.autoLoad();
                        }
                        // 判断滚动到顶部
                        if (buyed_usrs_lv.getFirstVisiblePosition() == 0) {

                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        if (headView == null) {
            headView = View.inflate(this, R.layout.circyle_headview, null);
            buyed_usrs_lv.addHeaderView(headView);
        }

        buyed_usrs_lv.setAdapter(adapter);

//        footerView = (LinearLayout) View.inflate(this, R.layout.item_grid_footer, null);
//        footerimage = (ImageView) footerView.findViewById(R.id.footview);
//        footerView.findViewById(R.id.tv_temp).setVisibility(View.INVISIBLE);
//        BaseUtils.setLoadingImageAnimation(footerimage);
//        buyed_usrs_lv.addFooterView(footerView);
//        buyed_usrs_lv.setAdapter(adapter);
//        buyed_usrs_lv.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
////                Log.i(TAG, "scrollState="+scrollState+"  lastItem="+lastItem+"  count="+count);
////                Log.i(TAG, "scrollState="+scrollState+"  pageNum="+pageNum+"  pageCount="+pageCount);
//                //下拉到空闲是，且最后一个item的数等于数据的总数时，进行更新
////                if (lastItem == count && scrollState == this.SCROLL_STATE_IDLE) {
////
////
////                }
//                switch (scrollState) {
//                    // 当不滚动时
//                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
//                        // 判断滚动到底部
//                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
//                            Log.i(TAG, "view.getLastVisiblePosition()="+view.getLastVisiblePosition()+"  view.getCount()="+view.getCount());
//                            if (pageNum > pageCount) {
//                                Log.i("debugg", "拉到最底部");
//
//                                buyed_usrs_lv.removeFooterView(footerView);
//                                footerimage.clearAnimation();
//                                showToast("没有更多数据");
//
//                            } else {
//
//                                getWelfareInfo(welfareId);
//                            }
//                        }
//                        break;
//                }
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
////                lastItem = firstVisibleItem + visibleItemCount - 1;
//            }
//        });


//        buyed_usrs_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Log.d("debug ","adapter.getCount"+adapter.getCount()+ " pageData.size("+ pageData.size() + "position "+ position);
//
//                startActivity(new Intent(ActivityWelfareBuyedMoreUsers.this, UserGroupInfoActivity.class).putExtra(Config.INTENT_PARMAS2, pageData.get(position-2).getEmobId()));
//
//            }
//        });

//        pbuyed_usrs_lv.setOnItemClickListener( new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Log.d("debug ","adapter.getCount"+adapter.getCount()+ " pageData.size("+ pageData.size() + "position "+ position);
//
//                startActivity(new Intent(ActivityWelfareBuyedMoreUsers.this, UserGroupInfoActivity.class).putExtra(Config.INTENT_PARMAS2, pageData.get(position).getEmobId()));
//
//            }
//        });


    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pageNum = 1;
        getWelfareInfo(welfareId);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        pageNum++;
        getWelfareInfo(welfareId);
    }

    interface WelfareBuyedMoreUsrsService {
        @GET("/api/v1/communities/{communityId}/welfares/{welfareId}/users")
        void getWelfareInfo(@Path("communityId") int communityId, @Path("welfareId") int welfareId, @QueryMap Map<String, String> map, Callback<WelfareBuyedMoreUserInfo> cb);

        @GET("/api/v3/welfares/{welfareId}/users")
        void getWelfareInfoV3(@Path("welfareId") int welfareId, @QueryMap Map<String, String> map, Callback<CommonRespBean<PageDataEntityV3Bean>> cb);
    }

    private void getWelfareInfo(int welfareId) {
        HashMap<String, String> option = new HashMap<String, String>();
        option.put("page", "" + pageNum);
        option.put("limit", pageSize);
        WelfareBuyedMoreUsrsService service = RetrofitFactory.getInstance().create(getmContext(),option,WelfareBuyedMoreUsrsService.class);
        Callback<CommonRespBean<PageDataEntityV3Bean>> callback = new Callback<CommonRespBean<PageDataEntityV3Bean>>() {
            @Override
            public void success(CommonRespBean<PageDataEntityV3Bean> bean, retrofit.client.Response response) {
                if (bean != null) {
                    if ("yes".equals(bean.getStatus())) {
                        ll_errorpage.setVisibility(View.GONE);
                        ll_nomessage.setVisibility(View.GONE);
                        ll_neterror.setVisibility(View.GONE);

                        List<PageDataEntityV3Bean.PageDataEntityUserV3Bean> pageDatanew = bean.getData().getData();
                        if (pageData.size() > 0) {
                            if (pageDatanew == null || pageDatanew.size() <= 0) {
                                showNoMoreToast();
                            }
                        }
                        if (pageNum == 1) {
                            pageData.clear();
                            pageData.addAll(pageDatanew);
                        } else {
                            pageData.addAll(pageDatanew);
                        }
                        count = adapter.getCount();
                        adapter.notifyDataSetChanged();
                        pageCount = bean.getData().getPage();

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
        service.getWelfareInfoV3(welfareId, option, callback);
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
                convertView = View.inflate(ActivityWelfareBuyedMoreUsers.this, R.layout.common_welfare_purchase_hasgotmoreurs, null);
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
                    Log.d("debug ", "adapter.getCount" + adapter.getCount() + " pageData.size(" + pageData.size() + "position " + position);

                    startActivity(new Intent(ActivityWelfareBuyedMoreUsers.this, UserGroupInfoActivity.class).putExtra(Config.INTENT_PARMAS2, pageData.get(position).getEmobId()));
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

                getWelfareInfo(welfareId);

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
