package xj.property.activity.doorpaste;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.repo.xw.library.views.PullListView;
import com.repo.xw.library.views.PullToRefreshLayout;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.adapter.DoorPasteDetailAdapter;
import xj.property.beans.CanPasteRespBean;
import xj.property.beans.DoorPasteAddReqBean;
import xj.property.beans.DoorPasteDetailsBean;
import xj.property.beans.DoorPasteDetailsRespBean;
import xj.property.beans.DoorPasteIndexBean;
import xj.property.beans.DoorPasteRemoveReqBean;
import xj.property.beans.IWantProviderRespBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.CommonUtils;
import xj.property.utils.ToastUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

/**
 * 门贴详情
 * 2016/03/24
 */
public class DoorPasteDetailActivity extends HXBaseActivity implements View.OnClickListener, PullToRefreshLayout.OnRefreshListener {
    private static final int IWANTADDPASTE_FLAG = 1000;
    private UserInfoDetailBean userbean;
    private int pageIndex = 1;
    private LinearLayout ll_errorpage;
    private LinearLayout ll_nomessage;
    private LinearLayout ll_neterror;
    private TextView tv_getagain;
    private ImageView iv_back;
    private TextView tv_title;
    private DoorPasteIndexBean itemBean;
    private ImageView iv_right;
    private DoorPastedoShareDialog doorPastedoShareDialog;
    private PullToRefreshLayout pull_to_refreshlayout;
    private PullListView pull_to_lv;

    private DoorPasteDetailAdapter doorPasteDetailAdapter;
    private List<DoorPasteDetailsBean> doorPasteDetailsBeans = new ArrayList<>();
    private LinearLayout ll_index_empty;
    private ImageView ll_index_empty_iv;
    private ImageView iwant_addpaste_btn;
    private Button iwant_removepaste_btn;
    private int totalPastedTimes;
    private DoorPastedoDeleteDialog doorPastedoDeleteDialog;
    private boolean isExample;
    private DoorPastedoDeleteDialogForExample doorPastedoDeleteDialogForExample;
    private UserInfoDetailBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doorpaste_details);
        itemBean = (DoorPasteIndexBean) getIntent().getSerializableExtra("iwantaddpaste");
        isExample = getIntent().getBooleanExtra("isExample", false);
        userbean = PreferencesUtil.getLoginInfo(getmContext());
        if (itemBean != null) {
            initView();
            initData();
        } else {
            showToast("数据异常");
            finish();
            return;
        }
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
                        getFastShopDetailDataList();
                    }
                }
            });
        } else {
            if (ll_errorpage != null)
                ll_errorpage.setVisibility(View.GONE);
            getFastShopDetailDataList();
        }

    }


    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(itemBean.getAddress());
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_right = (ImageView) findViewById(R.id.iv_right);
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doorPastedoShareDialog = new DoorPastedoShareDialog(DoorPasteDetailActivity.this, new SocializeListeners.SnsPostListener() {
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
        iwant_addpaste_btn = (ImageView) findViewById(R.id.iwant_addpaste_btn);
        iwant_addpaste_btn.setOnClickListener(this);

        iwant_removepaste_btn = (Button) findViewById(R.id.iwant_removepaste_btn);
        iwant_removepaste_btn.setOnClickListener(this);

        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        ll_index_empty = (LinearLayout) findViewById(R.id.ll_index_empty);
        ll_index_empty_iv = (ImageView) findViewById(R.id.ll_index_empty_iv);
        ll_neterror.setOnClickListener(this);

        pull_to_refreshlayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refreshlayout);
        pull_to_refreshlayout.setOnRefreshListener(this);
        pull_to_lv = (PullListView) findViewById(R.id.pull_to_lv);
        pull_to_lv.setPullUpEnable(false);
        pull_to_lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (pull_to_lv.getLastVisiblePosition() == (pull_to_lv.getCount() - 1)) {
                            pull_to_refreshlayout.autoLoad();
                        }
                        // 判断滚动到顶部
                        if (pull_to_lv.getFirstVisiblePosition() == 0) {

                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        doorPasteDetailAdapter = new DoorPasteDetailAdapter(getmContext(), doorPasteDetailsBeans, this);
        pull_to_lv.setAdapter(doorPasteDetailAdapter);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iwant_addpaste_btn:
                bean = PreferencesUtil.getLoginInfo(getmContext());
                if (bean != null) {
                    gocanAddOrStick(getmContext(), isExample, itemBean);
                } else {
                    Intent intent = new Intent(getmContext(), RegisterLoginActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.iwant_removepaste_btn:

                bean = PreferencesUtil.getLoginInfo(getmContext());
                if (bean != null) {
                    //// 谈对话框告诉用户需要消耗人品值。
                    doorPastedoDeleteDialog = new DoorPastedoDeleteDialog(getmContext(), totalPastedTimes, new DoorPastedoDeleteDialog.DelListener() {
                        @Override
                        public void doOK(String type) {
                            if (TextUtils.equals("yes", type)||TextUtils.equals("example",type)) {
                                removeDoorPaste(itemBean.getDoorId());
                            } else {
                                if (doorPastedoDeleteDialog != null) {
                                    doorPastedoDeleteDialog.dismiss();
                                }
                            }
                        }
                        @Override
                        public void doCancel() {
                        }
                    });
                    doorPastedoDeleteDialog.show();
                } else {
                    Intent intent = new Intent(getmContext(), RegisterLoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }


    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
//        pageIndex = 1;
        getFastShopDetailDataList();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
//        if (pageIndex == 1) {
//            pull_to_lv.setSelection(pull_to_lv.getCount());
//        }
//        pageIndex++;
        getFastShopDetailDataList();

    }
    /**
     * 加贴，赞同
     *
     * @param doorId
     * @param doorStickerId
     */
    public void doAddPaste(int doorId, int doorStickerId) {
        if (mLdDialog != null) {
            mLdDialog.show();
        }
        if (isExample) {
            CountDownTimer countDownTimer = new CountDownTimer(2 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }
                @Override
                public void onFinish() {
                    if (mLdDialog != null) {
                        mLdDialog.dismiss();
                    }
                    pull_to_refreshlayout.autoRefresh();
                    ToastUtils.showToast(getmContext(), "成功贴了一个门贴");
                }
            };
            countDownTimer.start();
            return;
        }

        DoorPasteAddReqBean quaryToken = new DoorPasteAddReqBean();
        quaryToken.setEmobId(PreferencesUtil.getLoginInfo(getmContext()).getEmobId());

        FastShopDetailService service = RetrofitFactory.getInstance().create(getmContext(),quaryToken,FastShopDetailService.class);
        Callback<CommonRespBean> callback = new Callback<CommonRespBean>() {
            @Override
            public void success(CommonRespBean respBean, Response response) {
                if (mLdDialog != null) {
                    mLdDialog.dismiss();
                }
                if (respBean != null && TextUtils.equals("yes", respBean.getStatus())) {
                    pull_to_refreshlayout.autoRefresh();
                    Toast.makeText(getmContext(), "成功贴了一个门贴", Toast.LENGTH_SHORT).show();
                    doorPastedoShareDialog = new DoorPastedoShareDialog(DoorPasteDetailActivity.this, new SocializeListeners.SnsPostListener() {
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
                } else if (respBean != null && TextUtils.equals("no", respBean.getStatus())) {
                    Toast.makeText(getmContext(), respBean.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getmContext(), "加贴失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                showNetErrorToast();
                if (mLdDialog != null) {
                    mLdDialog.dismiss();
                }
            }
        };
        service.getTagsA2BAdd( doorId, doorStickerId, quaryToken, callback);
    }

    /**
     * 摘门贴
     *
     * @param doorId
     */
    public void removeDoorPaste(int doorId) {
        if (isExample) {
            doorPastedoDeleteDialogForExample = new DoorPastedoDeleteDialogForExample(getmContext(), totalPastedTimes, isExample, new DoorPastedoDeleteDialogForExample.DelListener() {
                @Override
                public void doOK(String type) {
                    if (mLdDialog != null) {
                        mLdDialog.show();
                    }

                    CountDownTimer countDownTimer = new CountDownTimer(2 * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                        @Override
                        public void onFinish() {
                            if (mLdDialog != null) {
                                mLdDialog.dismiss();
                            }
                            ToastUtils.showToast(getmContext(), "门贴已摘掉");
                            finish();
                        }
                    };
                    countDownTimer.start();
                }

                @Override
                public void doCancel() {
                }
            });
            doorPastedoDeleteDialogForExample.show();

            return;
        }

        if (mLdDialog != null) {
            mLdDialog.show();
        }

        DoorPasteRemoveReqBean quaryToken = new DoorPasteRemoveReqBean();
        quaryToken.setEmobId(PreferencesUtil.getLoginInfo(getmContext()).getEmobId());
        if (totalPastedTimes <= 0) {
            showToast("门贴数异常，请刷新后重试");
            return;
        }
        quaryToken.setTimes(totalPastedTimes);
        FastShopDetailService service = RetrofitFactory.getInstance().create(getmContext(), quaryToken, FastShopDetailService.class);
        Callback<CommonRespBean> callback = new Callback<CommonRespBean>() {
            @Override
            public void success(CommonRespBean respBean, Response response) {
                if (mLdDialog != null) {
                    mLdDialog.dismiss();
                }
                if (respBean != null && TextUtils.equals("yes", respBean.getStatus())) {
                    Toast.makeText(getmContext(), "门贴已摘掉", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (respBean != null && TextUtils.equals("no", respBean.getStatus())) {
                    Toast.makeText(getmContext(), "摘门贴失败：" + respBean.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getmContext(), "摘门贴失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                showNetErrorToast();
                if (mLdDialog != null) {
                    mLdDialog.dismiss();
                }
            }
        };
        service.removeDoorPaste(doorId, quaryToken, callback);
    }

    interface FastShopDetailService {
//        @GET("/api/v1/doors/{doorId}")
//        void getRepairList(@Path("doorId") int doorId, Callback<DoorPasteDetailsRespBean> cb);
///api/v3/doors/{门的ID}
        @GET("/api/v3/doors/{doorId}")
        void getRepairList(@Path("doorId") int doorId, Callback<CommonRespBean<List<DoorPasteDetailsBean>>> cb);

//        @POST("/api/v1/doors/{doorId}/stickers/{doorStickerId}")
//        void getTagsA2BAdd(@Header("signature") String signature, @Path("doorId") int doorId, @Path("doorStickerId") int doorStickerId, @Body DoorPasteAddReqBean qt, Callback<IWantProviderRespBean> cb);
//        /api/v3/doors/{房门ID}/stickers/{门贴ID}
        @POST("/api/v3/doors/{doorId}/stickers/{doorStickerId}")
        void getTagsA2BAdd(@Path("doorId") int doorId, @Path("doorStickerId") int doorStickerId, @Body DoorPasteAddReqBean qt, Callback<CommonRespBean> cb);

//        @PUT("/api/v1/doors/{doorId}")
//        void removeDoorPaste(@Header("signature") String signature, @Path("doorId") int doorId, @Body DoorPasteRemoveReqBean qt, Callback<IWantProviderRespBean> cb);
///api/v3/doors/{房门ID}
        @PUT("/api/v3/doors/{doorId}")
        void removeDoorPaste(@Path("doorId") int doorId, @Body DoorPasteRemoveReqBean qt, Callback<CommonRespBean> cb);
    }

    /**
     * 门贴列表
     */
    private void getFastShopDetailDataList() {
        if (isExample) {
            getDoorPasteExampleData();
            return;
        }
        FastShopDetailService service = RetrofitFactory.getInstance().create(getmContext(),FastShopDetailService.class);
        Callback<CommonRespBean<List<DoorPasteDetailsBean>>> callback = new Callback<CommonRespBean<List<DoorPasteDetailsBean>>>() {
            @Override
            public void success(CommonRespBean<List<DoorPasteDetailsBean>> respBean, Response response) {
                if (respBean != null && TextUtils.equals("yes", respBean.getStatus()) && respBean.getData() != null) {
                    doorPasteDetailsBeans.clear();
                    initTotalPastedTimes(respBean.getData());
                    doorPasteDetailsBeans.addAll(respBean.getData());
                    doorPasteDetailAdapter.notifyDataSetChanged();
                }
                if (doorPasteDetailAdapter.getCount() <= 0) {
                    ll_errorpage.setVisibility(View.VISIBLE);
                    ll_neterror.setVisibility(View.VISIBLE);
                    ll_index_empty.setVisibility(View.GONE);
                } else {
                    ll_errorpage.setVisibility(View.GONE);
                    ll_neterror.setVisibility(View.VISIBLE);
                    ll_index_empty.setVisibility(View.GONE);
                }
                pull_to_refreshlayout.loadMoreFinish(true);
                pull_to_refreshlayout.refreshFinish(true);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                pull_to_refreshlayout.loadMoreFinish(true);
                pull_to_refreshlayout.refreshFinish(true);
                showNetErrorToast();
            }
        };
        service.getRepairList(itemBean.getDoorId(), callback);
    }

    /**
     * 获取示例数据
     */
    private void getDoorPasteExampleData() {
        doorPasteDetailsBeans.clear();
        List<DoorPasteDetailsBean> info = getExampleData();
        initTotalPastedTimes(info);
        doorPasteDetailsBeans.addAll(info);
        doorPasteDetailAdapter.notifyDataSetChanged();

        ll_errorpage.setVisibility(View.GONE);
        pull_to_refreshlayout.loadMoreFinish(true);
        pull_to_refreshlayout.refreshFinish(true);
    }

    /**
     * 生成示例数据
     *
     * @return
     */
    private List<DoorPasteDetailsBean> getExampleData() {

        List<DoorPasteDetailsBean> info = new ArrayList<>();
        DoorPasteDetailsBean data1 = new DoorPasteDetailsBean();
        data1.setDoorId(10000);
        data1.setTimes(new Random(10).nextInt(10));
        data1.setContent("晚上回来太晚，并且声音很大，影响休息，请注意，并且门口垃圾过多，影响心情。");
        data1.setCreateTime((int) (System.currentTimeMillis() / 1000));
        data1.setDoorStickerId(10000);
        info.add(data1);

        DoorPasteDetailsBean data2 = new DoorPasteDetailsBean();
        data2.setDoorId(10001);
        data2.setTimes(new Random(10).nextInt(10));
        data2.setContent("晚上回来太晚，并且声音很大，影响休息，请注意!");
        data2.setCreateTime((int) (System.currentTimeMillis() / 1000));
        data2.setDoorStickerId(10001);
        info.add(data2);
        return info;
    }


    /**
     * 获取当前用户一共被贴了多少个门贴
     *
     * @param info
     */
    private void initTotalPastedTimes(List<DoorPasteDetailsBean> info) {
        if (info == null || info.size() < 1) {
            totalPastedTimes = 0;
            return;
        }
        totalPastedTimes = 0;
        for (DoorPasteDetailsBean detailsBean : info) {
            totalPastedTimes += detailsBean.getTimes();
        }
    }

    interface DoorPasteIndexService {
//        @GET("/api/v1/doors/{doorId}/canAddOrStick")
//        void canAddOrStick(@Path("doorId") int doorId, @QueryMap Map<String, String> map, Callback<CanPasteRespBean> cb);
//        @GET("/api/v1/doors/{doorId}/canAddOrStick")
//        /api/v3/doors/{门的ID}/canAddOrStick?emobId={用户环信ID}
        @GET("/api/v3/doors/{doorId}/canAddOrStick")
        void canAddOrStick(@Path("doorId") int doorId, @QueryMap Map<String, String> map, Callback<CommonRespBean<Boolean>> cb);
    }

    /**
     * 大加贴
     *
     * @param context
     * @param itemBean
     */
    private void gocanAddOrStick(final Context context, boolean isExample, final DoorPasteIndexBean itemBean) {
        if (isExample) {
            Intent intent = new Intent(context, IwantAddPasteActivity.class);
            intent.putExtra("iwantaddpaste", itemBean);
            intent.putExtra("isExample", isExample);
            context.startActivity(intent);
            return;
        }
        HashMap<String, String> option = new HashMap<>();
        option.put("emobId", "" + PreferencesUtil.getLoginInfo(context).getEmobId());
        DoorPasteIndexService service = RetrofitFactory.getInstance().create(getmContext(),option,DoorPasteIndexService.class);
        Callback<CommonRespBean<Boolean>> callback = new Callback<CommonRespBean<Boolean>>() {
            @Override
            public void success(CommonRespBean<Boolean> bean, Response response) {
                mLdDialog.dismiss();
                if (bean != null && bean.getData()) {
                    Intent intent = new Intent(getmContext(), IwantAddPasteActivity.class);
                    intent.putExtra("iwantaddpaste", itemBean);
                    startActivityForResult(intent, IWANTADDPASTE_FLAG);
                } else if (bean != null) {
                    ToastUtils.showToast(context, "同一天，同一门只能贴一次");
                } else {
                    ToastUtils.showToast(context, "数据异常");
                }
            }
            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                error.printStackTrace();
                ToastUtils.showToast(context, "网络异常，请稍后重试！");
            }
        };
        mLdDialog.show();
        service.canAddOrStick(itemBean.getDoorId(), option, callback);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (pull_to_refreshlayout != null) {
            pull_to_refreshlayout.autoRefresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        userbean = PreferencesUtil.getLoginInfo(getmContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLdDialog != null) {
            mLdDialog.dismiss();
        }
        if (doorPastedoShareDialog != null) {
            doorPastedoShareDialog.dismiss();
        }
        if (doorPastedoDeleteDialog != null) {
            doorPastedoDeleteDialog.dismiss();
        }
        if (doorPastedoDeleteDialogForExample != null) {
            doorPastedoDeleteDialogForExample.dismiss();
        }
    }
}
