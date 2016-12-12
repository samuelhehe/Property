package xj.property.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import xj.property.beans.BangBiCusumeBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.other.BaseUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;


/**
 * 帮帮币总数,消费列表页面
 */
public class BangBiConsumeListActivity extends HXBaseActivity {


    /// 帮帮币总数
    private TextView bangbangbi_left_nums_tv;

    private ListView bangbangbi_consumelist_lv;

    private MyAdapter adapter = new MyAdapter();

    private int pageNum = 1;
    //    private int lastItem;
    private int pageCount = 1;
    private int pageSize = 10;

    private List<BangBiCusumeBean.DataEntity> pageData = new ArrayList<BangBiCusumeBean.DataEntity>();

    private LinearLayout footerView;
    private ImageView footerimage;
    private UserInfoDetailBean bean;
    private int currentReqCount = pageSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bangbangbi_consumelist);
        initview();
        initData();
    }

    private void initData() {
        bean = PreferencesUtil.getLoginInfo(this);
        bangbangbi_left_nums_tv.setText("" + (Integer.parseInt(PreferencesUtil.getPrefBangbangbiCount(this)) > 0 ? PreferencesUtil.getPrefBangbangbiCount(this) + "" : "0"));
        getBangbiConsumeList();
    }

    private void initview() {
        initTitle();
        bangbangbi_left_nums_tv = (TextView) this.findViewById(R.id.bangbangbi_left_nums_tv);
        bangbangbi_consumelist_lv = (ListView) this.findViewById(R.id.bangbangbi_consumelist_lv);

        footerView = (LinearLayout) View.inflate(this, R.layout.item_grid_footer, null);
        footerimage = (ImageView) footerView.findViewById(R.id.footview);
        footerView.findViewById(R.id.tv_temp).setVisibility(View.INVISIBLE);
        BaseUtils.setLoadingImageAnimation(footerimage);

        bangbangbi_consumelist_lv.addFooterView(footerView);
        bangbangbi_consumelist_lv.setAdapter(adapter);

        bangbangbi_consumelist_lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                Log.i(TAG, "scrollState="+scrollState+"  lastItem="+lastItem+"  count="+count);
//                Log.i(TAG, "scrollState="+scrollState+"  pageNum="+pageNum+"  pageCount="+pageCount);
                //下拉到空闲是，且最后一个item的数等于数据的总数时，进行更新
//                if (lastItem == count && scrollState == this.SCROLL_STATE_IDLE) {
//
//                }
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
//                            isLastisNext++;

                            if (currentReqCount < pageSize) {
                                Log.i("debugg", "拉到最底部");
                                bangbangbi_consumelist_lv.removeFooterView(footerView);
                                footerimage.clearAnimation();
                                showNoMoreToast();
                            } else {
                                getBangbiConsumeList();
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                lastItem = firstVisibleItem + visibleItemCount - 2;
            }
        });
    }


    private void initTitle() {

        TextView tv = (TextView) this.findViewById(R.id.tv_right_text);
        tv.setOnClickListener(this);
        tv.setText("使用说明");
        tv.setVisibility(View.VISIBLE);

        TextView tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("帮帮币");
        tv_title.setVisibility(View.VISIBLE);
        this.findViewById(R.id.iv_back).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right_text:
                /// 跳转至帮帮币使用说明页面
                startActivity(new Intent(this, BangBiExplanationActivity.class));
                break;
        }
    }

    interface IConsumeList {
        /**
         * 获取已经邀请的邻居列表
         *
         * @param emobId
         * @param cb
         */
        /////api/v3/communities/1/users/9064d94ce3da4ad68979b1151b2f6588/bonuscoinHistory?page=1&limit=10
        @GET("/api/v3/communities/{communityId}/users/{emobId}/bonuscoinHistory")
        void getConsumeList(@Path("communityId") String communityId, @Path("emobId") String emobId, @QueryMap Map<String, String> map, Callback<CommonRespBean<BangBiCusumeBean>> cb);
    }

    /**
     * 获取帮币消费列表
     */
    private void getBangbiConsumeList() {
        mLdDialog.show();
        HashMap<String, String> option = new HashMap<String, String>();
        option.put("page", "" + pageNum);
        option.put("limit", "" + pageSize);
        IConsumeList service = RetrofitFactory.getInstance().create(getmContext(),option,IConsumeList.class);
        Callback<CommonRespBean<BangBiCusumeBean>> callback = new Callback<CommonRespBean<BangBiCusumeBean>>() {
            @Override
            public void success(CommonRespBean<BangBiCusumeBean> bean, retrofit.client.Response response) {
                if (bean != null) {
                    if ("yes".equals(bean.getStatus())) {
                        if (bean.getData() != null) {
                            if (bean.getData().getData() != null && !bean.getData().getData().isEmpty()) {
                                currentReqCount = bean.getData().getData().size();
                                pageData.addAll(bean.getData().getData());
                                pageNum++;
                            }
                        }
//                        if(bangbangbi_consumelist_lv!=null){
//                            ViewUtils.setListViewHeightBasedOnChildren(bangbangbi_consumelist_lv);
//                        }
                        adapter.notifyDataSetChanged();
                        mLdDialog.dismiss();
                    } else {
                        mLdDialog.dismiss();
                        showToast(bean.getMessage());
                    }
                } else {
                    mLdDialog.dismiss();
                    showToast("数据异常");
                }
                footerView.setVisibility(View.GONE);
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                footerView.setVisibility(View.GONE);
                showNetErrorToast();
                error.printStackTrace();
            }
        };
        footerView.setVisibility(View.VISIBLE);
        if (bean != null) {
            service.getConsumeList("" + bean.getCommunityId(), bean.getEmobId(), option, callback);
        } else {
            showDataErrorToast();
        }
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

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(BangBiConsumeListActivity.this, R.layout.common_bangbi_consume_item, null);
                viewHolder = new ViewHolder(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
//            Log.i("debbug", "size=" + pageData.size());
//            Log.i("debbug", "viewHolder=" + viewHolder);
//            Log.i("debbug", "viewholder.tvname=" + viewHolder.invited_username_tv);
            if (pageData.get(position) != null) {
                viewHolder.name_text_view.setText(pageData.get(position).getBonuscoinSource());
                viewHolder.time_number_text_view.setText("" + format.format(new Date(pageData.get(position).getCreateTime() * 1000L)));
                viewHolder.bangbi_custom_num_tv.setText("" + pageData.get(position).getBonuscoinCount());
                return convertView;
            } else {
                return null;
            }
        }


        class ViewHolder {
            TextView name_text_view;
            TextView time_number_text_view;
            TextView bangbi_custom_num_tv;

            ViewHolder(View v) {
                name_text_view = (TextView) v.findViewById(R.id.name_text_view);
                time_number_text_view = (TextView) v.findViewById(R.id.time_number_text_view);
                bangbi_custom_num_tv = (TextView) v.findViewById(R.id.bangbi_custom_num_tv);
                v.setTag(this);
            }
        }

    }
}
