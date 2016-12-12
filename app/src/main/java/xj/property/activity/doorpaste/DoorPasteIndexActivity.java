package xj.property.activity.doorpaste;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.adapter.DoorPasteIndexAdapter;
import xj.property.beans.DoorPasteIndexBean;
import xj.property.beans.DoorPasteIndexRespBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseUtils;
import xj.property.netbase.RetrofitFactory;
import xj.property.netbasebean.SimpleUserInfoBean;
import xj.property.utils.CommonUtils;
import xj.property.utils.other.AutoLoadListener;
import xj.property.utils.other.BaseUtils;
import xj.property.utils.other.DensityUtil;
import xj.property.utils.other.PreferencesUtil;

/**
 * Created by Administrator on 2015/6/23.
 */
public class DoorPasteIndexActivity extends HXBaseActivity implements View.OnClickListener {
    private UserInfoDetailBean userbean;
    private xj.property.widget.GridViewWithHeaderAndFooter common_doorpaste_gv;

    private DoorPasteIndexAdapter adapter;
    private int pageIndex = 1;
    private LinearLayout ll_errorpage;
    private LinearLayout ll_nomessage;
    private LinearLayout ll_neterror;
    private TextView tv_getagain;
    private TextView community_doorpaste_status_tv;
    private TextView tv_title;
    private ImageView iv_right;
    private DoorPastedoShareDialog doorPastedoShareDialog;
    private int screentWidthdp; /// 屏幕宽
    private float itemWidthdp; /// 单个Itemwidth
    private float ratioW2H;
    private float itemHightdp; /// 单个itemhigh

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doorpaste_index);
        PreferencesUtil.saveDoorPasteIndexCount(getmContext(), 0);
        userbean = PreferencesUtil.getLoginInfo(getmContext());
        computeItemWH();
        initView();
        initData();
        boolean isunreaddoorpastenindex = PreferencesUtil.isUnReadDoorPastenIndex(getmContext());
        if (isunreaddoorpastenindex) {
            startActivity(new Intent(getmContext(), DoorPasteNoticesPagerActivity.class));
        }
    }

    private void computeItemWH() {
        screentWidthdp = DensityUtil.px2dip(getmContext(), getResources().getDisplayMetrics().widthPixels);
        itemWidthdp = (screentWidthdp - 12 * 2 - 5) / 2f;
        ratioW2H = 330f / 448f;
        itemHightdp = itemWidthdp / ratioW2H;
        if (itemWidthdp == 0 || itemHightdp == 0) {
            itemWidthdp = 156;
            itemHightdp = 224;
        }
        Log.d("DoorPasteIndexActivity ", "computeItemWH " + " itemWidthdp-->" + itemWidthdp + " itemHightdp-->" + itemHightdp);

    }

    private void initData() {
        if (!CommonUtils.isNetWorkConnected(getmContext())) {
            ll_errorpage.setVisibility(View.VISIBLE);
            ll_neterror.setVisibility(View.VISIBLE);
            ll_nomessage.setVisibility(View.GONE);
            tv_getagain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!CommonUtils.isNetWorkConnected(getmContext())) {
                        return;
                    } else {
                        ll_errorpage.setVisibility(View.GONE);
                        getDoorPasteIndexDataList();
                    }
                }
            });
        } else {
            if (ll_errorpage != null)
                ll_errorpage.setVisibility(View.GONE);
            getDoorPasteIndexDataList();
        }
    }

    private View footerView;
    private ImageView footerimage;
    private LinearLayout foot_layout;
    private TextView tv_temp;

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("门贴");
        iv_right = (ImageView) findViewById(R.id.iv_right);
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doorPastedoShareDialog = new DoorPastedoShareDialog(DoorPasteIndexActivity.this, new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
                        if (doorPastedoShareDialog != null) {
                            doorPastedoShareDialog.dismiss();
                        }
                    }
                });
                doorPastedoShareDialog.setCancelable(true);
                doorPastedoShareDialog.show();
            }
        });
        community_doorpaste_status_tv = (TextView) findViewById(R.id.community_doorpaste_status_tv);
        common_doorpaste_gv = (xj.property.widget.GridViewWithHeaderAndFooter) findViewById(R.id.common_doorpaste_gv);
        LayoutInflater layoutInflater = LayoutInflater.from(getmContext());
        footerView = layoutInflater.inflate(R.layout.item_grid_footer, null);
        footerimage = (ImageView) footerView.findViewById(R.id.footview);
        foot_layout = (LinearLayout) footerView.findViewById(R.id.foot_layout);
        tv_temp = (TextView) footerView.findViewById(R.id.tv_temp);

        common_doorpaste_gv.addFooterView(footerView);
        tv_temp.setBackgroundColor(Color.WHITE);
        BaseUtils.setLoadingImageAnimation(footerimage);
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        tv_getagain = (TextView) findViewById(R.id.tv_getagain);
        ll_nomessage = (LinearLayout) findViewById(R.id.ll_nomessage);
        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);
        common_doorpaste_gv.setOnScrollListener(new AutoLoadListener(new AutoLoadListener.AutoLoadCallBack() {
            @Override
            public void execute() {
                pageIndex++;
                new GetDataTask().execute();
            }
        }));
    }


    private class GetDataTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            getData(pageIndex);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(requestCode==500){
//            DoorPasteIndexBean itembean    = (DoorPasteIndexBean) data.getSerializableExtra("iwantaddpaste");
//            if(itembean!=null){
//                listBean.remove(itembean);
//                adapter.notifyDataSetChanged();
//            }
//        }
        /// 刷新用户信息
        getUserInfo();
        /// 刷新门贴列表
        getDoorPasteIndexDataList();
    }

    interface FastShopDetailService {
        //        @GET("/api/v1/doors")
//        void getRepairList(@QueryMap Map<String, String> map, Callback<DoorPasteIndexRespBean> cb);
///api/v3/doors?communityId={小区ID}&page={页码}&limit={页面大小}
        @GET("/api/v3/doors")
        void getRepairList(@QueryMap Map<String, String> map, Callback<CommonRespBean<DoorPasteIndexRespBean>> cb);
    }

    public void getUserInfo() {
        userbean = PreferencesUtil.getLoginInfo(getmContext());
        if (userbean != null) {
            NetBaseUtils.extractSimpleUserInfo(getmContext(), PreferencesUtil.getCommityId(getmContext()), userbean.getEmobId(), new NetBaseUtils.NetRespListener<CommonRespBean<SimpleUserInfoBean>>() {

                @Override
                public void successYes(CommonRespBean<SimpleUserInfoBean> bean, Response response) {
                    PreferencesUtil.saveRPValue(getmContext(), "" + bean.getData().getCharacterValues());
                }

                @Override
                public void successNo(CommonRespBean<SimpleUserInfoBean> commonRespBean, Response response) {

                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                }
            });
        }
    }

    private List<DoorPasteIndexBean> listBean = new ArrayList<>();

    private void getDoorPasteIndexDataList() {

//        communityId={小区ID}&page={页码}&limit={页面大小}
        HashMap<String, String> option = new HashMap<>();
        option.put("communityId", "" + PreferencesUtil.getCommityId(getmContext()));
        option.put("page", "1");
        option.put("limit", "10");
        FastShopDetailService service = RetrofitFactory.getInstance().create(getmContext(), option, FastShopDetailService.class);
        Callback<CommonRespBean<DoorPasteIndexRespBean>> callback = new Callback<CommonRespBean<DoorPasteIndexRespBean>>() {
            @Override
            public void success(CommonRespBean<DoorPasteIndexRespBean> bean, Response response) {
                if (bean != null && TextUtils.equals("yes", bean.getStatus()) && bean.getData() != null && bean.getData().getData() != null) {
                    pageIndex = 1;
                    listBean.clear();

                    List<DoorPasteIndexBean> dpIndexBeans = bean.getData().getData();
                    /// 说明需要示例
                    listBean = checkOrAddListBean(dpIndexBeans);
                    adapter = new DoorPasteIndexAdapter(DoorPasteIndexActivity.this, listBean, (int) itemWidthdp, (int) itemHightdp);
                    common_doorpaste_gv.setAdapter(adapter);

                    int doorCount = bean.getField("doorCount", Integer.class);
                    int stickCount = bean.getField("stickCount", Integer.class);

                    PreferencesUtil.saveDoorPasteShareContent(getmContext(), "小区有" + doorCount + "个房被贴，共计被贴" + stickCount + "次");
//                    小区有5个房被贴，共计被贴32次
                    community_doorpaste_status_tv.setText("小区有" + doorCount + "个房被贴，共计" + stickCount + "个门贴");

                    if (listBean.size() != 0) {
                        ll_errorpage.setVisibility(View.GONE);

                        adapter.notifyDataSetChanged();
                    } else {
                        ll_errorpage.setVisibility(View.VISIBLE);
                        ll_neterror.setVisibility(View.GONE);
                        ll_nomessage.setVisibility(View.VISIBLE);
                    }
                    foot_layout.setVisibility(View.GONE);
                    footerimage.clearAnimation();

//                    tv_temp.setVisibility(View.GONE);
//                    if (listBean.size() <= 4) {
//                        tv_temp.setVisibility(View.VISIBLE);
//                    } else {
//                        tv_temp.setVisibility(View.GONE);
//                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
//                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
                foot_layout.setVisibility(View.INVISIBLE);
                footerimage.clearAnimation();
            }
        };
        service.getRepairList(option, callback);
    }


    /**
     * 将列表中多余的Add模块 移除， 并添加一个Add模块到列表最后
     *
     * @param dpIndexBeans
     * @return
     */
    private List<DoorPasteIndexBean> checkOrAddListBean(List<DoorPasteIndexBean> dpIndexBeans) {
        if (dpIndexBeans == null || dpIndexBeans.size() <= 0) {
            return getExampleListBean();
        }
        List<DoorPasteIndexBean> addList = new ArrayList<>();
        for (DoorPasteIndexBean pageDataEntity : dpIndexBeans) {
            if (pageDataEntity.isAddBlock()) {
                addList.add(pageDataEntity);
            }
        }
        /// 把刚检测出来的移除。
        for (DoorPasteIndexBean pageDataEntity : addList) {
            dpIndexBeans.remove(pageDataEntity);
        }
        if (dpIndexBeans.size() <= 0) {
            return getExampleListBean();
        }
        /// 添加一条增加模块
        DoorPasteIndexBean pageDataEntityAdd = new DoorPasteIndexBean();
        pageDataEntityAdd.setIsexample(false);
        pageDataEntityAdd.setAddBlock(true);/// 属于添加模块
        dpIndexBeans.add(pageDataEntityAdd);

        return dpIndexBeans;
    }


    private List<DoorPasteIndexBean> getExampleListBean() {
        List<DoorPasteIndexBean> dpIndexBeans = new ArrayList<>();
        DoorPasteIndexBean pageDataEntity = new DoorPasteIndexBean();
        pageDataEntity.setTimes(new Random(10).nextInt(10));
        pageDataEntity.setIsexample(true);
        pageDataEntity.setDoorId(10000);
        pageDataEntity.setAddress("1楼四单元505");
        dpIndexBeans.add(pageDataEntity);

        DoorPasteIndexBean pageDataEntityAdd = new DoorPasteIndexBean();
        pageDataEntityAdd.setIsexample(false);
        pageDataEntityAdd.setAddBlock(true);/// 属于添加模块
        dpIndexBeans.add(pageDataEntityAdd);
        return dpIndexBeans;
    }

    protected void getData(final int pageIndex) {
        HashMap<String, String> option = new HashMap<>();
        option.put("communityId", "" + PreferencesUtil.getCommityId(getmContext()));
        option.put("page", pageIndex + "");
        option.put("limit", "10");
        FastShopDetailService service = RetrofitFactory.getInstance().create(getmContext(), option, FastShopDetailService.class);
        Callback<CommonRespBean<DoorPasteIndexRespBean>> callback = new Callback<CommonRespBean<DoorPasteIndexRespBean>>() {
            @Override
            public void success(CommonRespBean<DoorPasteIndexRespBean> bean, Response response) {
                if (null == bean || !"yes".equals(bean.getStatus()) || bean.getData() == null || bean.getData().getData() == null) {
                    return;
                }
                List<DoorPasteIndexBean> dpIndexBeansData = bean.getData().getData();
                if (pageIndex == 1) {
                    listBean.clear();
                    listBean.addAll(dpIndexBeansData);
                    listBean = checkOrAddListBean(listBean);
                    adapter.notifyDataSetChanged();
                } else if (bean.getData().getPage() >= pageIndex) {
                    if (listBean != null) {
                        listBean.addAll(dpIndexBeansData);
                        listBean = checkOrAddListBean(listBean);
                        adapter.notifyDataSetChanged();
                    }
                }
                foot_layout.setVisibility(View.INVISIBLE);
                footerimage.clearAnimation();
            }

            @Override
            public void failure(RetrofitError error) {
                showNetErrorToast();
                foot_layout.setVisibility(View.INVISIBLE);
                footerimage.clearAnimation();
            }
        };

        service.getRepairList(option, callback);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (doorPastedoShareDialog != null) {
            doorPastedoShareDialog.dismiss();
        }
    }
}
