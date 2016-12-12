package xj.property.activity.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.PUT;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.CommonPostResultBean;
import xj.property.beans.UpdateUserInfoRequest;
import xj.property.beans.UpdateUserSignatureRequest;
import xj.property.beans.UserBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.beans.UserMessageBean;
import xj.property.beans.XJUserInfoBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.MaxLengthWatcher;
import xj.property.utils.other.PreferencesUtil;

/**
 * 个性签名
 * Created by n on 2015/4/7.
 */
public class FixUserSignatureActivity extends HXBaseActivity {
    public static final int UserKey_Signature=4;
    EditText et_Fix_Nickname;
    ImageView iv_clear;
    UserInfoDetailBean bean;

    private TextView tv_right_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_userinfo);
        initTitle(null, "个性签名", "保存");

        et_Fix_Nickname = (EditText) findViewById(R.id.et_fix_nickname);
        tv_right_text = (TextView) findViewById(R.id.tv_right_text);
        tv_right_text.setVisibility(View.VISIBLE);
        tv_right_text.setOnClickListener(this);

        //iv_clear = (ImageView) findViewById(R.id.iv_clear);
       // iv_clear.setVisibility(View.INVISIBLE);
        bean= PreferencesUtil.getLoginInfo(this);
        et_Fix_Nickname.setText(bean.getSignature());
        Editable editable = et_Fix_Nickname.getText();
        int selEndIndex = Selection.getSelectionEnd(editable);
        selEndIndex = editable.length();
        Selection.setSelection(editable, selEndIndex);
        et_Fix_Nickname.addTextChangedListener(new MaxLengthWatcher(this,30,et_Fix_Nickname,"最长30个字符！"));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right_text:
                //userRegistByFix();
                fixUserInfo();
                break;
        }
    }

    /**
     * 获取XJuser部分
     */
    interface XJUserService {
        ///api/v1/communities/{communityId}/users/{userId}
//        @PUT("/api/v1/communities/{communityId}/users/update/{userName}")
//        void updateUserInfo(@Header("signature")String signature, @Body UpdateUserSignatureRequest request, @Path("communityId") long communityId, @Path("userName") String userName, Callback<XJUserInfoBean> cb);
        @PUT("/api/v3/communities/{communityId}/users/{emobId}/signature")
        void updateUserInfo(@Body UpdateUserSignatureRequest request, @Path("communityId") long communityId, @Path("emobId") String emobId, Callback<CommonRespBean<String>> cb);
    }

    private void fixUserInfo() {
        mLdDialog.show();
        UpdateUserSignatureRequest request = new UpdateUserSignatureRequest();
        String name=et_Fix_Nickname.getText().toString();
        request.setSignature(name);
        XJUserService service = RetrofitFactory.getInstance().create(getmContext(), request, XJUserService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, Response response) {
                mLdDialog.dismiss();
                if ("no".equals(bean.getStatus())){
                    showToast("修改失败，请稍后再试");
                }else {
                    showToast("修改成功");
                    SharedPreferences spf = FixUserSignatureActivity.this.getSharedPreferences("xj_property_login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = spf.edit();
                    edit.putString("signature;",et_Fix_Nickname.getText().toString());
                    edit.commit();
                    finish();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        service.updateUserInfo(request,bean.getCommunityId(), bean.getEmobId(), callback);
    }


    /**
     *  service to judge if a user exist
     */
    interface FixUserInfoService {
        @PUT("/api/v1/communities/{communityId}/users/{emobId}")
        void userRegist(@Header("signature") String signature, @Body UserBean userBean, @Path("communityId") int communityId, @Path("emobId") String emobId, Callback<CommonPostResultBean> cb);
    }

    /**
     * check if user valid
     */
    private void userRegistByFix(){
        mLdDialog.show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Config.NET_BASE2)
                .build();
        FixUserInfoService isUserExistService = restAdapter.create(FixUserInfoService.class);
        final   UserBean userBean = new UserBean();
        userBean.setSignature(et_Fix_Nickname.getText().toString());
        userBean.setMethod("PUT");
        Callback<CommonPostResultBean> callback = new Callback<CommonPostResultBean>() {
            @Override
            public void success(CommonPostResultBean commonPostResultBean, Response response) {
                showToast("修改成功");
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
            }
        };


        isUserExistService.userRegist(StrUtils.string2md5(Config.BANGBANG_TAG+StrUtils.dataHeader(userBean)),userBean,PreferencesUtil.getCommityId(FixUserSignatureActivity.this),  PreferencesUtil.getLoginInfo(FixUserSignatureActivity.this).getEmobId(),callback);
    }



}
