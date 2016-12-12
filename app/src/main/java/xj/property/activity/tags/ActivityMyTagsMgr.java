package xj.property.activity.tags;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.beans.MyTagsRespBean;
import xj.property.beans.SysDefaultTagsV3Bean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseTagUtils;
import xj.property.utils.CommonUtils;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.FilterFlowLayout;

/**
 * 标签管理
 */
public class ActivityMyTagsMgr extends HXBaseActivity
        implements MyOwnerTagsManagerDialog.onTagsA2BAddStatusListener, MyTagsManagerConfrimDialog.onTagsA2BDelStatusListener {

    private LinearLayout ll_errorpage;
    private LinearLayout ll_neterror;
    private LinearLayout ll_nomessage;
    private ImageView iv_nomessage_image;
    private TextView tv_nomessage;

    private UserInfoDetailBean bean;
    private CheckBox checkbox_right_text;

    private TextView tv_title;

    private FilterFlowLayout mytags_fflay;
    /// 系统默认标签
    private List<String> systemDefaulttags;

    /// 添加标签按钮
    private Button mytags_addtags_btn;
    //// 自己对自己添加的标签
    private List<String> tagsA2Btags;
    private LayoutInflater layoutInflater;
    /// 是否处于编辑状态
    private boolean isEditingStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytags_mgr);
        bean = PreferencesUtil.getLoginInfo(this);
        layoutInflater = LayoutInflater.from(this);

        if (bean != null) {
            initView();
            initData();
        } else {
            Intent intent = new Intent(this, RegisterLoginActivity.class);
            startActivity(intent);
        }

    }

    private void initData() {

        if (CommonUtils.isNetWorkConnected(this)) {

            getSystemDefaultTags();
            getUserTagsInfo();

        } else {
            ll_errorpage.setVisibility(View.VISIBLE);
            ll_neterror.setVisibility(View.VISIBLE);
        }

    }

    private void initView() {
        initTitle();

        checkbox_right_text = (CheckBox) this.findViewById(R.id.checkbox_right_text);
        checkbox_right_text.setOnCheckedChangeListener(new MyTagsMgrEditCheckedChangeListener());
        checkbox_right_text.setText("");
        checkbox_right_text.setBackground(getResources().getDrawable(R.drawable.my_label_remove));
        checkbox_right_text.setVisibility(View.VISIBLE);


        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("添加管理标签");

        mytags_addtags_btn = (Button) this.findViewById(R.id.mytags_addtags_btn);
        mytags_addtags_btn.setOnClickListener(this);

        if(mytags_addtags_btn!=null){
            mytags_addtags_btn.setVisibility(View.VISIBLE);
        }

        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        ll_nomessage = (LinearLayout) findViewById(R.id.ll_nomessage);

        iv_nomessage_image = (ImageView) findViewById(R.id.iv_nomessage_image);
        iv_nomessage_image.setImageResource(R.drawable.tikuanjilu_people);

        tv_nomessage = (TextView) findViewById(R.id.tv_nomessage);
        tv_nomessage.setText("还没有标签哦!");

        ll_neterror.setOnClickListener(this);
        mytags_fflay = (FilterFlowLayout) findViewById(R.id.mytags_fflay);

    }

    private void initTitle() {
        this.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void onTagsA2BAddSuccess(String message) {
        ///刷新添加后的数据
        initData();
    }

    @Override
    public void onTagsA2BAddFail(String message) {
        /// 添加失败
        showToast(message);
    }

    @Override
    public void onTagsA2BDelSuccess(String message) {
        ///刷新删除后的数据
        initData();
    }

    @Override
    public void onTagsA2BDelFail(String message) {
        /// 添加失败
        showToast(message);
    }


    interface MspCardListService {
//
//        @GET("/api/v1/communities/{communityId}/labels")
//        void getSysDefaultTags(@Path("communityId") String communityId, @QueryMap Map<String, String> map, Callback<SysDefaultTagsBean> cb);
////
//
//        @GET("/api/v3/labels/defaults")
//        void getSysDefaultTagsV3(@QueryMap Map<String, String> map, Callback<CommonRespBean<SysDefaultTagsV3Bean>> cb);


//        @GET("/api/v1/communities/{communityId}/labels/user")
//        void getTagsA2BTags(@Path("communityId") String communityId, @QueryMap Map<String, String> map, Callback<TagsA2BRespBean> cb);

    }

//
//    interface getUserTags {
//        @GET("/api/v1/communities/{communityId}/labels/user/{emobId}")
//        void getUserTags(@Path("communityId") int communityId, @Path("emobId") String emobid, Callback<MyTagsRespBean> cb);
//
//        /**
//         * 获取用户的标签信息
//         * @param cb
//         */
//        @GET("/api/v3/labels/details")
//        void getUserTagsV3(@QueryMap Map<String, String> map,Callback<CommonRespBean<List<MyTagsRespBean.InfoEntity>>> cb);
//
//    }


    private List<MyTagsRespBean.InfoEntity> info = new ArrayList<>();


    public void getUserTagsInfo() {
        if (!mLdDialog.isShowing()) {
            mLdDialog.show();
        }
        NetBaseTagUtils.getUserTagsInfo(getmContext(),bean.getEmobId(), new NetBaseTagUtils.NetRespListener<CommonRespBean<List<MyTagsRespBean.InfoEntity>>>() {
            @Override
            public void success(CommonRespBean<List<MyTagsRespBean.InfoEntity>> bean, Response response) {
                if (TextUtils.equals("yes", bean.getStatus())) {
                    info = bean.getData();
                    refreshNormalTags(info);
                }
                mLdDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                if (mLdDialog != null) {
                    mLdDialog.dismiss();
                }
                error.printStackTrace();
            }
        });

    }


    private void refreshNormalTags(List<MyTagsRespBean.InfoEntity> info) {
        if (isEditingStatus) {
            if (tv_title != null) {
                tv_title.setText("管理编辑标签");
            }
            if (checkbox_right_text != null) {
                checkbox_right_text.setText("");
                checkbox_right_text.setBackground(getResources().getDrawable(R.drawable.label_finish));
            }
            if(mytags_addtags_btn!=null){
                mytags_addtags_btn.setVisibility(View.GONE);
            }

            initTagdel(info);
        } else {
            if (tv_title != null) {
                tv_title.setText("添加管理标签");
            }

            if (checkbox_right_text != null) {
                checkbox_right_text.setText("");
                checkbox_right_text.setBackground(getResources().getDrawable(R.drawable.my_label_remove));
            }

            if(mytags_addtags_btn!=null){
                mytags_addtags_btn.setVisibility(View.VISIBLE);
            }
            initMyTags(getmContext(), info);
        }
    }

    /**
     * 初始化临时列表
     */
    private void initTagdel(final List<MyTagsRespBean.InfoEntity> info) {


        if (info == null || info.isEmpty()) {
            Log.d("initMyTags  ", "initMyTags is null 该用户无标签 ");
            if (mytags_fflay != null) {
                mytags_fflay.removeAllViews();
            }
            return;
        }
        if (mytags_fflay != null) {
            mytags_fflay.removeAllViews();
            for (final MyTagsRespBean.InfoEntity infoEntity : info) {
                View common_tags_item = layoutInflater.inflate(R.layout.common_tags_item_fora2b, null);
                TextView common_tags_name_tv = (TextView) common_tags_item.findViewById(R.id.common_tags_name_tv);
                TextView common_tags_nums_tv = (TextView) common_tags_item.findViewById(R.id.common_tags_nums_tv);
                common_tags_name_tv.setText(infoEntity.getLabelContent());
                common_tags_nums_tv.setVisibility(View.GONE);
                common_tags_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //// 弹对话框后
                        myTagsManagerConfrimDialog = new MyTagsManagerConfrimDialog(getmContext(), infoEntity.getLabelContent(), bean.getEmobId(), ActivityMyTagsMgr.this);
                        myTagsManagerConfrimDialog.show();
                        WindowManager windowManager = getWindowManager();
                        Display display = windowManager.getDefaultDisplay();
                        WindowManager.LayoutParams lp = myTagsManagerConfrimDialog.getWindow().getAttributes();
                        lp.width = (int) (display.getWidth()); //设置宽度
                        myTagsManagerConfrimDialog.getWindow().setAttributes(lp);

                    }
                });

                mytags_fflay.addView(common_tags_item);
            }
        } else {
            Log.d("tags_flay  ", "tags_flay is null  ");
        }

    }


    private void initMyTags(final Context context, List<MyTagsRespBean.InfoEntity> info) {

        if (info == null || info.isEmpty()) {
            Log.d("initMyTags  ", "initMyTags is null 该用户无标签 ");

            if (mytags_fflay != null) {
                mytags_fflay.removeAllViews();
            }
            return;
        }
        if (mytags_fflay != null) {
            mytags_fflay.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(context);
            for (final MyTagsRespBean.InfoEntity bean : info) {
                View common_tags_item = inflater.inflate(R.layout.common_tags_item, null);
                TextView common_tags_name_tv = (TextView) common_tags_item.findViewById(R.id.common_tags_name_tv);
                TextView common_tags_nums_tv = (TextView) common_tags_item.findViewById(R.id.common_tags_nums_tv);
                common_tags_name_tv.setText(bean.getLabelContent());
                common_tags_nums_tv.setVisibility(View.GONE);

//                common_tags_item.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(context, ActivityMyTagsList.class);
//                        intent.putExtra("tagContent", bean.getLabelContent());
//                        startActivity(intent);
//
//                    }
//                });

                mytags_fflay.addView(common_tags_item);
            }

        } else {
            Log.d("tags_flay  ", "tags_flay is null  ");
        }
    }

    private void getSystemDefaultTags() {
        HashMap<String, String> option = new HashMap<String, String>();
        option.put("communityId", bean.getCommunityId() + "");
        option.put("time", "" + PreferencesUtil.getLastReqTagsTime(getmContext()));
        NetBaseTagUtils.getSysDefaultTags(bean.getEmobId(), getmContext(), option, new NetBaseTagUtils.NetRespListener<CommonRespBean<SysDefaultTagsV3Bean>>() {
            @Override
            public void success(CommonRespBean<SysDefaultTagsV3Bean> tagsbean, Response response) {
                if (tagsbean != null) {
                    if (TextUtils.equals("yes", tagsbean.getStatus())) {
                        systemDefaulttags = tagsbean.getData().getList();
                        if (systemDefaulttags == null || systemDefaulttags.isEmpty() || systemDefaulttags.size() < 1) {
                            systemDefaulttags = PreferencesUtil.getNewSysTags(getmContext(), bean.getEmobId());
                        }
                    } else {
                        systemDefaulttags = PreferencesUtil.getNewSysTags(getmContext(), bean.getEmobId());
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private MyOwnerTagsManagerDialog myTagsManagerDialog;

    private MyTagsManagerConfrimDialog myTagsManagerConfrimDialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_neterror:
                initData();
                break;

            case R.id.mytags_addtags_btn:

//                if (!CommonUtils.isNetWorkConnected(this)) {
//                    showToast("网络异常,请稍后重试");
//                }else{

                if (systemDefaulttags == null || systemDefaulttags.isEmpty()) {
                    getSystemDefaultTags();
                } else {
                    myTagsManagerDialog = new MyOwnerTagsManagerDialog(getmContext(),
                            systemDefaulttags,
                            "" + bean.getCommunityId(),
                            bean.getEmobId(),
                            bean.getEmobId(),
                            ActivityMyTagsMgr.this);
                    myTagsManagerDialog.show();
                    WindowManager windowManager = getWindowManager();
                    Display display = windowManager.getDefaultDisplay();
                    WindowManager.LayoutParams lp = myTagsManagerDialog.getWindow().getAttributes();
                    lp.width = (int) (display.getWidth()); //设置宽度
                    myTagsManagerDialog.getWindow().setAttributes(lp);
                }
//                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myTagsManagerDialog != null) {
            myTagsManagerDialog.dismiss();
        }
        if (myTagsManagerConfrimDialog != null) {
            myTagsManagerConfrimDialog.dismiss();
        }
        if (mLdDialog != null) {
            mLdDialog.dismiss();
        }
    }

    private class MyTagsMgrEditCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            switch (buttonView.getId()) {

                case R.id.checkbox_right_text:
                    //// 处于编辑状态
                    if (isChecked) {
                        isEditingStatus = true;
                        refreshNormalTags(info);
                        //// 处于非编辑状态
                    } else {
                        isEditingStatus = false;
                        refreshNormalTags(info);

                    }
                    break;
            }
        }
    }
}
