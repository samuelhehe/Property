package xj.property.activity.fitmentfinish;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.adapter.FitmentDetailsBusinessAdapter;
import xj.property.adapter.FitmentDetailsCommentAdapter;
import xj.property.beans.FitMentFinishCommentRequest;
import xj.property.beans.FitMentFinishDetailsRequest;
import xj.property.beans.FitmentDetailsComment;
import xj.property.beans.FitmentDetailsDecorationUsers;
import xj.property.beans.FitmentDetailsFeatures;
import xj.property.beans.FitmentDetailsUser;
import xj.property.beans.FitmentDetailsViewedUser;
import xj.property.beans.FitmentFinishDetailsInfo;
import xj.property.beans.FitmentFinishDetailsResponse;
import xj.property.beans.UserInfoDetailBean;
import xj.property.utils.TimeUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.CircleImageView;


/**
 * 作者：asia on 2015/12/11 11:56
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class FitMentFinishDetailsActivity extends HXBaseActivity {

    private String mTag = "FitMentFinishDetailsActivity";

    private final int MESSAGE = 88;
    private final int CLOSEDIALOG = 66;

    private String emobId;
    private long communityId;
    private String mPhone="";

    private UserInfoDetailBean bean;
    private int decorationId;

    private ScrollView mSl_scroll;
    private ImageView mIv_img;
    private TextView mTv_name;
    private ImageView mIv_tese1;
    private ImageView mIv_tese2;
    private ImageView mIv_tese3;
    private ImageView mIv_tese4;
    private ImageView mIv_tese5;
    private CircleImageView mIv_devise1;
    private CircleImageView mIv_devise2;
    private CircleImageView mIv_devise3;
    private CircleImageView mIv_devise4;
    private CircleImageView mIv_devise5;
    private CircleImageView mIv_devise6;
    private CircleImageView mIv_devise7;

    private LinearLayout mLl_looked;
    private CircleImageView mIv_Looked1;
    private CircleImageView mIv_Looked2;
    private CircleImageView mIv_Looked3;
    private CircleImageView mIv_Looked4;
    private CircleImageView mIv_Looked5;
    private CircleImageView mIv_Looked6;
    private CircleImageView mIv_Looked7;
    private LinearLayout mLl_used;
    private CircleImageView mIv_used1;
    private CircleImageView mIv_used2;
    private CircleImageView mIv_used3;
    private CircleImageView mIv_used4;
    private CircleImageView mIv_used5;
    private CircleImageView mIv_used6;
    private CircleImageView mIv_used7;

    private TextView mTv_message;
    private TextView mTv_phone;
    private TextView mTv_address;
    private TextView mTv_sign_data;
    private TextView mTv_sign;
    private TextView mTv_register;
    private TextView mTv_helpme;

    private FitmentDetailsBusinessAdapter mFitmentDetailsBusinessAdapter;
    private ListView mLv_demo;

    private FitmentDetailsCommentAdapter mFitmentDetailsCommentAdapter;
    private ListView mLv_comment;

    private FitmentFinishDetailsInfo mDetailsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitment_details);
        bean = PreferencesUtil.getLoginInfo(FitMentFinishDetailsActivity.this);
        emobId = bean.getEmobId();
        communityId =  bean.getCommunityId();
        decorationId = getIntent().getIntExtra("decorationId", 0);
        initTitle();
        initView();
        initData();
        initListenner();
    }

    private void initListenner() {
        mTv_helpme.setOnClickListener(this);
        mTv_phone.setOnClickListener(this);
    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        this.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.tv_title)).setText("公司详情");
        ((TextView) this.findViewById(R.id.tv_right_text)).setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.tv_right_text)).setText("发表评论");
        ((TextView) this.findViewById(R.id.tv_right_text)).setOnClickListener(this);
    }


    private void initData() {
        getFitmentFinishDetails();
    }

    private void initView() {
        mSl_scroll = (ScrollView) findViewById(R.id.sl_scroll);
        mIv_img = (ImageView) findViewById(R.id.iv_img);
        mTv_name = (TextView) findViewById(R.id.tv_name);
        mIv_tese1 = (ImageView) findViewById(R.id.iv_tese1);
        mIv_tese2 = (ImageView) findViewById(R.id.iv_tese2);
        mIv_tese3 = (ImageView) findViewById(R.id.iv_tese3);
        mIv_tese4 = (ImageView) findViewById(R.id.iv_tese4);
        mIv_tese5 = (ImageView) findViewById(R.id.iv_tese5);
        mIv_devise1 = (CircleImageView) findViewById(R.id.iv_devise1);
        mIv_devise2 = (CircleImageView) findViewById(R.id.iv_devise2);
        mIv_devise3 = (CircleImageView) findViewById(R.id.iv_devise3);
        mIv_devise4 = (CircleImageView) findViewById(R.id.iv_devise4);
        mIv_devise5 = (CircleImageView) findViewById(R.id.iv_devise5);
        mIv_devise6 = (CircleImageView) findViewById(R.id.iv_devise6);
        mIv_devise7 = (CircleImageView) findViewById(R.id.iv_devise7);
        mLl_looked = (LinearLayout) findViewById(R.id.ll_looked);
        mIv_Looked1 = (CircleImageView) findViewById(R.id.iv_Looked1);
        mIv_Looked2 = (CircleImageView) findViewById(R.id.iv_Looked2);
        mIv_Looked3 = (CircleImageView) findViewById(R.id.iv_Looked3);
        mIv_Looked4 = (CircleImageView) findViewById(R.id.iv_Looked4);
        mIv_Looked5 = (CircleImageView) findViewById(R.id.iv_Looked5);
        mIv_Looked6 = (CircleImageView) findViewById(R.id.iv_Looked6);
        mIv_Looked7 = (CircleImageView) findViewById(R.id.iv_Looked7);
        mLl_used = (LinearLayout) findViewById(R.id.ll_used);
        mIv_used1 = (CircleImageView) findViewById(R.id.iv_used1);
        mIv_used2 = (CircleImageView) findViewById(R.id.iv_used2);
        mIv_used3 = (CircleImageView) findViewById(R.id.iv_used3);
        mIv_used4 = (CircleImageView) findViewById(R.id.iv_used4);
        mIv_used5 = (CircleImageView) findViewById(R.id.iv_used5);
        mIv_used6 = (CircleImageView) findViewById(R.id.iv_used6);
        mIv_used7 = (CircleImageView) findViewById(R.id.iv_used7);

        mTv_message = (TextView) findViewById(R.id.tv_message);
        mTv_phone = (TextView) findViewById(R.id.tv_phone);
        mTv_address = (TextView) findViewById(R.id.tv_address);
        mTv_sign_data = (TextView) findViewById(R.id.tv_sign_data);
        mTv_sign = (TextView) findViewById(R.id.tv_sign);
        mTv_register = (TextView) findViewById(R.id.tv_register);
        mTv_helpme = (TextView) findViewById(R.id.tv_helpme);
        mLv_demo = (ListView) findViewById(R.id.lv_demo);
        mLv_comment = (ListView) findViewById(R.id.lv_comment);
    }

    private void initBusinessListView(String[] fitmentFinish) {
        mFitmentDetailsBusinessAdapter = new FitmentDetailsBusinessAdapter(this, fitmentFinish);
        mLv_demo.setAdapter(mFitmentDetailsBusinessAdapter);
        setListViewHeightBasedOnChildren(mLv_demo);
    }

    /**
     * 获取ActList部分
     */
    interface ActFitmentFinishDetailsZone {
        @PUT("/api/v1/decoration/{decorationId}")
        void getFitmentFinishDetails(@Header("signature") String signature, @Path("decorationId") int decorationId, @Body FitMentFinishDetailsRequest circleNewRecord, Callback<FitmentFinishDetailsResponse> cb);

        @POST("/api/v1/decoration/decorationComment")
        void postComment(@Header("signature") String signature, @Body FitMentFinishCommentRequest circleNewRecord, Callback<FitmentFinishDetailsResponse> cb);
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = listAdapter.getCount()-1; i >=0 ; i--) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void getFitmentFinishDetails() {
        mLdDialog.show();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        ActFitmentFinishDetailsZone service = restAdapter.create(ActFitmentFinishDetailsZone.class);
        Callback<FitmentFinishDetailsResponse> callback = new Callback<FitmentFinishDetailsResponse>() {
            @Override
            public void success(FitmentFinishDetailsResponse bean, retrofit.client.Response response) {
                mLdDialog.dismiss();
                if (bean != null) {
                    if ("yes".equals(bean.getStatus())&&bean.getInfo()!=null) {
                        mDetailsInfo =  bean.getInfo();
                        FitmentFinishDetailsInfo info = bean.getInfo();
                        initLooked(info.getViewedUser());
                        initUsed(info.getUser());
                        initTeam(info.getDecorationUsers());
                        initComment(info.getDecorationComment());
                        String[] strs = info.getCaseImage().split(",");
                        initBusinessListView(strs);
                        initFeatures(info.getDecorationFeatures());
                        initOther(info);
                        mPhone=info.getPhone();
                        ImageLoader.getInstance().displayImage(info.getLogo(), mIv_img, options);
                        mSl_scroll.smoothScrollTo(0,0);
                        mTv_name.setText(info.getCompanyName());
                        mTv_name.getBackground().setAlpha(120);
                    } else if ("no".equals(bean.getStatus())) {
                        Log.d(mTag, "getFitmentFinishDetails  ==========   null");
                    } else {
                        Log.d(mTag,"getFitmentFinishDetails  ==========   null");
                    }
                }

            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        FitMentFinishDetailsRequest circleNewRecord = new FitMentFinishDetailsRequest();
        circleNewRecord.setEmobId(emobId);
        circleNewRecord.setCommunityId(communityId);
        circleNewRecord.setAvatar(bean.getAvatar());
        circleNewRecord.setNickname(bean.getNickname());
        service.getFitmentFinishDetails(StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(circleNewRecord)), decorationId, circleNewRecord, callback);
    }

    private void initOther(FitmentFinishDetailsInfo info) {
        mTv_phone.setText("咨询电话:" + info.getPhone());
        mTv_address.setText("公司地址:" + info.getAddress());
        mTv_sign_data.setText("成立日期:" + TimeUtils.fromLongToString2(info.getFoundTime() + ""));
        mTv_sign.setText("注册资金:" + info.getRegisterMoney());
        mTv_register.setText("登记机关:" + info.getRegisterOrganization());
    }

    private void initFeatures(List<FitmentDetailsFeatures> decorationFeatures) {
        if (decorationFeatures.size() > 0) {
            ImageLoader.getInstance().displayImage(decorationFeatures.get(0).getFeatureImage(), mIv_tese1, options);
            mIv_tese1.setVisibility(View.VISIBLE);
        }
        if (decorationFeatures.size() > 1) {
            ImageLoader.getInstance().displayImage(decorationFeatures.get(1).getFeatureImage(), mIv_tese2, options);
            mIv_tese2.setVisibility(View.VISIBLE);
        }
        if (decorationFeatures.size() > 2) {
            ImageLoader.getInstance().displayImage(decorationFeatures.get(2).getFeatureImage(), mIv_tese3, options);
            mIv_tese3.setVisibility(View.VISIBLE);
        }
        if (decorationFeatures.size() > 3) {
            ImageLoader.getInstance().displayImage(decorationFeatures.get(3).getFeatureImage(), mIv_tese4, options);
            mIv_tese4.setVisibility(View.VISIBLE);
        }
        if (decorationFeatures.size() > 4) {
            ImageLoader.getInstance().displayImage(decorationFeatures.get(4).getFeatureImage(), mIv_tese5, options);
            mIv_tese5.setVisibility(View.VISIBLE);
        }
    }

    private void initComment(List<FitmentDetailsComment> decorationComment) {
        mFitmentDetailsCommentAdapter = new FitmentDetailsCommentAdapter(this, decorationComment);
        mLv_comment.setAdapter(mFitmentDetailsCommentAdapter);
        setListViewHeightBasedOnChildren(mLv_comment);
    }

    private void initTeam(List<FitmentDetailsDecorationUsers> decorationFeatures) {
        if (decorationFeatures.size() > 0) {
            ImageLoader.getInstance().displayImage(decorationFeatures.get(0).getAvatar(), mIv_devise1, options);
            mIv_devise1.setVisibility(View.VISIBLE);
        }
        if (decorationFeatures.size() > 1) {
            ImageLoader.getInstance().displayImage(decorationFeatures.get(1).getAvatar(), mIv_devise2, options);
            mIv_devise2.setVisibility(View.VISIBLE);
        }
        if (decorationFeatures.size() > 2) {
            ImageLoader.getInstance().displayImage(decorationFeatures.get(2).getAvatar(), mIv_devise3, options);
            mIv_devise3.setVisibility(View.VISIBLE);
        }
        if (decorationFeatures.size() > 3) {
            ImageLoader.getInstance().displayImage(decorationFeatures.get(3).getAvatar(), mIv_devise4, options);
            mIv_devise4.setVisibility(View.VISIBLE);
        }
        if (decorationFeatures.size() > 4) {
            ImageLoader.getInstance().displayImage(decorationFeatures.get(4).getAvatar(), mIv_devise5, options);
            mIv_devise5.setVisibility(View.VISIBLE);
        }
        if (decorationFeatures.size() > 5) {
            ImageLoader.getInstance().displayImage(decorationFeatures.get(5).getAvatar(), mIv_devise6, options);
            mIv_devise6.setVisibility(View.VISIBLE);
        }
        if (decorationFeatures.size() > 6) {
            ImageLoader.getInstance().displayImage(decorationFeatures.get(6).getAvatar(), mIv_devise7, options);
            mIv_devise7.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化用过的用户
     *
     * @param viewedUser
     */
    private void initUsed(List<FitmentDetailsUser> viewedUser) {
        if (viewedUser.size() > 0) {
            ImageLoader.getInstance().displayImage(viewedUser.get(0).getAvatar(), mIv_used1, options);
            mIv_used1.setVisibility(View.VISIBLE);
        }
        if (viewedUser.size() > 1) {
            ImageLoader.getInstance().displayImage(viewedUser.get(1).getAvatar(), mIv_used2, options);
            mIv_used2.setVisibility(View.VISIBLE);
        }
        if (viewedUser.size() > 2) {
            ImageLoader.getInstance().displayImage(viewedUser.get(2).getAvatar(), mIv_used3, options);
            mIv_used3.setVisibility(View.VISIBLE);
        }
        if (viewedUser.size() > 3) {
            ImageLoader.getInstance().displayImage(viewedUser.get(3).getAvatar(), mIv_used4, options);
            mIv_used4.setVisibility(View.VISIBLE);
        }
        if (viewedUser.size() > 4) {
            ImageLoader.getInstance().displayImage(viewedUser.get(4).getAvatar(), mIv_used5, options);
            mIv_used5.setVisibility(View.VISIBLE);
        }
        if (viewedUser.size() > 5) {
            ImageLoader.getInstance().displayImage(viewedUser.get(5).getAvatar(), mIv_used6, options);
            mIv_used6.setVisibility(View.VISIBLE);
        }
        if (viewedUser.size() > 6) {
            ImageLoader.getInstance().displayImage(viewedUser.get(6).getAvatar(), mIv_used7, options);
            mIv_used7.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化看过的用户
     *
     * @param user
     */
    private void initLooked(List<FitmentDetailsViewedUser> user) {
        if (user.size() > 0) {
            ImageLoader.getInstance().displayImage(user.get(0).getAvatar(), mIv_Looked1, options);
            mIv_Looked1.setVisibility(View.VISIBLE);
        }
        if (user.size() > 1) {
            ImageLoader.getInstance().displayImage(user.get(1).getAvatar(), mIv_Looked2, options);
            mIv_Looked2.setVisibility(View.VISIBLE);
        }
        if (user.size() > 2) {
            ImageLoader.getInstance().displayImage(user.get(2).getAvatar(), mIv_Looked3, options);
            mIv_Looked3.setVisibility(View.VISIBLE);
        }
        if (user.size() > 3) {
            ImageLoader.getInstance().displayImage(user.get(3).getAvatar(), mIv_Looked4, options);
            mIv_Looked4.setVisibility(View.VISIBLE);
        }
        if (user.size() > 4) {
            ImageLoader.getInstance().displayImage(user.get(4).getAvatar(), mIv_Looked5, options);
            mIv_Looked5.setVisibility(View.VISIBLE);
        }
        if (user.size() > 5) {
            ImageLoader.getInstance().displayImage(user.get(5).getAvatar(), mIv_Looked6, options);
            mIv_Looked6.setVisibility(View.VISIBLE);
        }
        if (user.size() > 6) {
            ImageLoader.getInstance().displayImage(user.get(6).getAvatar(), mIv_Looked7, options);
            mIv_Looked7.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_getagain:
                initData();
                /// 重试
                break;

            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_helpme:
                Intent intent=new Intent(FitMentFinishDetailsActivity.this, FitMentFinishHelpActivity.class);
                intent.putExtra("phone",mDetailsInfo.getPhone()+"");
                intent.putExtra("mobilePhone",mDetailsInfo.getMobilePhone()+"");
                intent.putExtra("decorationId",mDetailsInfo.getDecorationId());
                startActivity(intent);
                break;

            case R.id.tv_right_text:
                startActivityForResult(new Intent(FitMentFinishDetailsActivity.this, FitmentDialogActivity.class), MESSAGE);
                break;
            case R.id.tv_phone:
                if(mPhone != null&&!"".equals(mPhone)){
                    Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mPhone));
                    startActivity(intent1);
                }
                break;
        }


    }

    private void commentMessage(String success, String message) {
        mLdDialog.show();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        ActFitmentFinishDetailsZone service = restAdapter.create(ActFitmentFinishDetailsZone.class);
        Callback<FitmentFinishDetailsResponse> callback = new Callback<FitmentFinishDetailsResponse>() {
            @Override
            public void success(FitmentFinishDetailsResponse bean, retrofit.client.Response response) {
                mLdDialog.dismiss();
                if (bean != null) {
                    if ("yes".equals(bean.getStatus())) {
                        getFitmentFinishDetails();
                    } else if ("no".equals(bean.getStatus())) {
                        Toast.makeText(getApplicationContext(),bean.getMessage()+"",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        FitMentFinishCommentRequest circleNewRecord = new FitMentFinishCommentRequest();
        circleNewRecord.setEmobId(emobId);
        circleNewRecord.setDecorationId(decorationId);
        circleNewRecord.setCode(success);
        circleNewRecord.setComment(message);
        service.postComment(StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(circleNewRecord)), circleNewRecord, callback);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (MESSAGE == resultCode) {
            String success = data.getStringExtra("success");
            String message = data.getStringExtra("message");
            commentMessage(success, message);
        } else if (CLOSEDIALOG == resultCode) {

        }
    }

    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.default_picture)
            .showImageForEmptyUri(R.drawable.default_picture)
            .showImageOnFail(R.drawable.default_picture)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();

}
