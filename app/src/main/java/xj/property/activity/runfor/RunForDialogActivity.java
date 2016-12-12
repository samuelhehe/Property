package xj.property.activity.runfor;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.CommonPostResultBean;
import xj.property.beans.RunForAllV3Bean;
import xj.property.beans.VoteBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.other.PreferencesUtil;

/**
 * aurth:asia
 * 竞选页面
 */
public class RunForDialogActivity extends HXBaseActivity {

    private RunForAllV3Bean.RunForDataV3Bean mRunForBean;
    private RunForAllV3Bean.RunForDataV3Bean mMyRunForBean;

    private TextView mTv_out;
    private TextView mTv_submit;
    public final int VOTESUCESS = 88;
    public final int VOTEFAILURE = 66;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runfor_dialog);
        mRunForBean = (RunForAllV3Bean.RunForDataV3Bean) getIntent().getSerializableExtra("runForBean");
        mMyRunForBean = (RunForAllV3Bean.RunForDataV3Bean) getIntent().getSerializableExtra("mRunForBean");
        initView();
        initListenner();
    }

    private void initView() {
        mTv_out = (TextView) findViewById(R.id.tv_out);
        mTv_submit = (TextView) findViewById(R.id.tv_submit);
    }

    private void initListenner() {
        mTv_out.setOnClickListener(this);
        mTv_submit.setOnClickListener(this);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_out:
                finish();
                break;
            case R.id.tv_submit:
//                getVote();
                putVote();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    interface ActVoteZone {
        @POST("/api/v1/communities/{communityId}/election")
        void putFriendZone(@Header("signature") String signature, @Body VoteBean circleNewRecord, @Path("communityId") int communityId, Callback<CommonPostResultBean> cb);

        @POST("/api/v3/elections")
        void putVote(@Body VoteBean circleNewRecord, Callback<CommonRespBean<String>> cb);
    }

//    private void getVote() {
//        mLdDialog.show();
//        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
//        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
//        ActVoteZone service = restAdapter.create(ActVoteZone.class);
//        Callback<CommonPostResultBean> callback = new Callback<CommonPostResultBean>() {
//            @Override
//            public void success(CommonPostResultBean bean, retrofit.client.Response response) {
//                mLdDialog.dismiss();
//                if (bean != null) {
//                    if ("yes".equals(bean.getStatus())) {
//                        VoteSuccess();
//                    } else if ("elected".equals(bean.getStatus())) {
//                        VoteFailure();
//                    }
//                } else {
//                    VoteFailure();
//                }
//
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                mLdDialog.dismiss();
//                error.printStackTrace();
//                showNetErrorToast();
//                Intent intent = new Intent();
//                intent.putExtra("second", "I am second!");
//                setResult(VOTEFAILURE, intent);
//                finish();
//            }
//        };
//        VoteBean voteBean = new VoteBean();
//        voteBean.setCommunityId(PreferencesUtil.getCommityId(this));
//        voteBean.setEmobId(mRunForBean.getEmobId());
//        voteBean.setEmobIdFrom(mMyRunForBean.getEmobId());
//        service.putFriendZone(StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(voteBean)), voteBean, PreferencesUtil.getCommityId(getApplicationContext()), callback);
//    }

    private void putVote() {
        mLdDialog.show();
        VoteBean voteBean = new VoteBean();
        voteBean.setCommunityId(PreferencesUtil.getCommityId(this));
        voteBean.setEmobId(mRunForBean.getEmobId());
        voteBean.setEmobIdFrom(mMyRunForBean.getEmobId());

        ActVoteZone tokenReqService = RetrofitFactory.getInstance().create(getmContext(),voteBean,ActVoteZone.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, retrofit.client.Response response) {
                mLdDialog.dismiss();
                if (bean != null) {
                    if ("yes".equals(bean.getStatus())) {
                        VoteSuccess();
                    } else if ("no".equals(bean.getStatus())) {
                        showToast("投票失败："+bean.getMessage());
                        VoteFailure();
                    }
                } else {
                    showDataErrorToast();
                    VoteFailure();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
                Intent intent = new Intent();
                intent.putExtra("second", "I am second!");
                setResult(VOTEFAILURE, intent);
                finish();
            }
        };
        tokenReqService.putVote( voteBean, callback);
    }

    private void VoteSuccess() {
        Intent intent = new Intent();
        intent.putExtra("second", "I am second!");
        setResult(VOTESUCESS, intent);
        finish();
        Toast.makeText(getApplicationContext(), "投票成功", Toast.LENGTH_SHORT).show();
    }

    private void VoteFailure() {
        Intent intent = new Intent();
        intent.putExtra("second", "I am second!");
        setResult(VOTEFAILURE, intent);
        finish();

    }

}
