package xj.property.activity.tags;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.beans.TagsWhoTagMeRespV3Bean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseTagUtils;
import xj.property.utils.CommonUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;
import xj.property.widget.pullrefreshview.library.PullToRefreshBase;
import xj.property.widget.pullrefreshview.library.PullToRefreshListView;

/**
 * 点击某一个tag 进入, 为我添加此tag的所有人列表 ,谁给我添加了tag, 目前仅限本小区的tag
 */
public class ActivityMyTagsList extends HXBaseActivity {

    private LinearLayout ll_errorpage;
    private LinearLayout ll_neterror;
    private LinearLayout ll_nomessage;
    private ImageView iv_nomessage_image;
    private TextView tv_nomessage;

    private PullToRefreshListView mytags_pull_lv;
    private ListView msp_lv;
    private View headView;

    private UserInfoDetailBean bean;

    private MyAdapter adapter = new MyAdapter();

    private List<TagsWhoTagMeRespV3Bean.DataEntity> pageData = new ArrayList<>();


    private int pageNum = 1;
    private int count;
    private int pageCount;

    private String pageSize = "10";
    private String tagContent;

    private String tagEmobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytags_list);
        bean = PreferencesUtil.getLoginInfo(this);
        if (getIntent() != null) {
            tagContent = getIntent().getStringExtra("tagContent");
            tagEmobId = getIntent().getStringExtra("tagEmobId");

        } else {
            showDataErrorToast();
            finish();
            return;
        }

        if (!TextUtils.isEmpty(tagEmobId)) {
            initView();
            initData();
        } else {
//            Intent intent = new Intent(this, RegisterLoginActivity.class);
//            startActivity(intent);
            showDataErrorToast();
            finish();
            return;
        }

    }

    private void initData() {

        if (CommonUtils.isNetWorkConnected(this)) {
            if (!mLdDialog.isShowing()) {
                mLdDialog.show();
            }
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
        tv_nomessage.setText("还没有标签, 赶快去添加吧!");

        ll_neterror.setOnClickListener(this);
        mytags_pull_lv = (PullToRefreshListView) this.findViewById(R.id.mytags_pull_lv);
        msp_lv = mytags_pull_lv.getRefreshableView();
        if (headView == null) {
            headView = View.inflate(this, R.layout.circyle_headview, null);
            msp_lv.addHeaderView(headView);
        }
        msp_lv.setAdapter(adapter);
        mytags_pull_lv.setOnRefreshListener(
                new PullToRefreshBase.OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                        String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                        pageNum = 1;
                        getMyTagedListInfo();
                    }
                }
        );


        mytags_pull_lv.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                mytags_pull_lv.mFooterLoadingView.setVisibility(View.VISIBLE);
                mytags_pull_lv.mFooterLoadingView.refreshing();
                pageNum++;
                getMyTagedListInfo();
            }
        });
    }

    private void initTitle() {
        this.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("标签"+ tagContent);
    }

    private void getMyTagedListInfo() {
        HashMap<String, String> option = new HashMap<String, String>();
        option.put("page", "" + pageNum);
        option.put("limit", pageSize);
        option.put("emobIdTo", tagEmobId);
        NetBaseTagUtils.getLabelsHistory(getmContext(),tagContent, option, new NetBaseTagUtils.NetRespListener<CommonRespBean<TagsWhoTagMeRespV3Bean>>() {
            @Override
            public void success(CommonRespBean<TagsWhoTagMeRespV3Bean> bean, Response response) {
                if (bean != null) {

                    if ("yes".equals(bean.getStatus())&&bean.getData()!=null) {

                        ll_errorpage.setVisibility(View.GONE);
                        ll_nomessage.setVisibility(View.GONE);
                        ll_neterror.setVisibility(View.GONE);
                        if (pageNum == 1) {
                            pageData.clear();
                            pageData.addAll(bean.getData().getData());
                        } else {
                            pageData.addAll(bean.getData().getData());
                        }

                        count = adapter.getCount();
                        adapter.notifyDataSetChanged();
                        pageCount = bean.getData().getLimit();

                        if (count == 0) {
                            ll_errorpage.setVisibility(View.VISIBLE);
                            ll_nomessage.setVisibility(View.VISIBLE);
                        }

                    } else {
//                        showToast(bean.getMessage());
                        if (count == 0) {
                            ll_errorpage.setVisibility(View.VISIBLE);
                            ll_nomessage.setVisibility(View.VISIBLE);
                        }
                    }
                }

                if (count == 0) {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_nomessage.setVisibility(View.VISIBLE);
                }

                mytags_pull_lv.mFooterLoadingView.setVisibility(View.GONE);
                mytags_pull_lv.onRefreshComplete();

                if (mLdDialog.isShowing()) {
                    mLdDialog.dismiss();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mytags_pull_lv.onRefreshComplete();
                if (adapter.getCount() <= 0) {
                    mytags_pull_lv.mFooterLoadingView.setVisibility(View.GONE);
                    showDataErrorToast();
                }
                if (count == 0) {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_neterror.setVisibility(View.VISIBLE);
                }
                showNetErrorToast();
                if (mLdDialog.isShowing()) {
                    mLdDialog.dismiss();
                }

            }
        });

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
                convertView = View.inflate(getmContext(), R.layout.common_tags_who_me_item, null);
                viewHolder = new ViewHolder(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Log.i("debbug", "size=" + pageData.size());
            Log.i("debbug", "viewHolder=" + viewHolder);

            viewHolder.whotags_me_name_tv.setText(pageData.get(position).getNickname());

            Date tag_time_date =  new Date(pageData.get(position).getCreateTime() * 1000L);

            viewHolder.whotags_me_tag_time_tv.setText(format.format(tag_time_date));

            viewHolder.common_tags_name_tv.setText(tagContent);

            viewHolder.common_tags_nums_tv.setVisibility(View.GONE);

            ImageLoader.getInstance().displayImage(pageData.get(position).getAvatar(), viewHolder.iv_avatar, UserUtils.msp_card_iv_options);

            viewHolder.iv_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("debug ", "adapter.getCount" + adapter.getCount() + " pageData.size(" + pageData.size() + "position " + position);

                    startActivity(new Intent(getmContext(), UserGroupInfoActivity.class).putExtra(Config.INTENT_PARMAS2, pageData.get(position).getEmobIdFrom()));

                }
            });

            return convertView;
        }

        class ViewHolder {
            ImageView iv_avatar;
            TextView whotags_me_name_tv;
            TextView whotags_me_tag_time_tv;
            TextView common_tags_name_tv;
            TextView common_tags_nums_tv;


            ViewHolder(View v) {

                iv_avatar = (ImageView) v.findViewById(R.id.iv_avatar);

                whotags_me_name_tv = (TextView) v.findViewById(R.id.whotags_me_name_tv);
                whotags_me_tag_time_tv = (TextView) v.findViewById(R.id.whotags_me_tag_time_tv);

                common_tags_name_tv = (TextView) v.findViewById(R.id.common_tags_name_tv);

                common_tags_nums_tv = (TextView) v.findViewById(R.id.common_tags_nums_tv);

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
