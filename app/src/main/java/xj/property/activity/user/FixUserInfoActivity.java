package xj.property.activity.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.util.EMLog;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.PUT;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.UpdateUserNickNameRequest;
import xj.property.beans.UserInfoDetailBean;
import xj.property.beans.XJUserInfoBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

/**
 * Created by Administrator on 2015/3/25.
 */
public class FixUserInfoActivity extends HXBaseActivity {
    EditText et_Fix_Nickname;
    ImageView iv_clear;
    public static final int UserKey_Head = 0;
    public static final int UserKey_NikeName = 1;
    public static final int UserKey_Address = 2;
    public static final int UserKey_Gender = 3;

    private UserInfoDetailBean bean;

    private TextView tv_right_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_userinfo);
        initTitle(null, "修改昵称", "保存");

        et_Fix_Nickname = (EditText) findViewById(R.id.et_fix_nickname);
        tv_right_text = (TextView) findViewById(R.id.tv_right_text);
        tv_right_text.setVisibility(View.VISIBLE);
        // iv_clear = (ImageView) findViewById(R.id.iv_clear);
        bean = PreferencesUtil.getLoginInfo(this);
        et_Fix_Nickname.setText(bean.getNickname());

        Editable editable = et_Fix_Nickname.getText();
        int selEndIndex = Selection.getSelectionEnd(editable);
        selEndIndex = editable.length();
        Selection.setSelection(editable, selEndIndex);
//        et_Fix_Nickname.addTextChangedListener(new MaxLengthWatcher(this,4,et_Fix_Nickname,"最长4个字符！"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right_text:
                fixUserInfo();
                //userRegistByFix();
                break;

        }
    }

    /**
     * 获取XJuser部分
     */
    interface XJUserService {
        ///api/v1/communities/{communityId}/users/{userId}
//        @PUT("/api/v1/communities/{communityId}/users/update/{userName}")
//        void updateUserInfo(@Header("signature") String signature, @Body UpdateUserNickNameRequest request, @Path("communityId") long communityId, @Path("userName") String userName, Callback<XJUserInfoBean> cb);
//        @PUT("/api/v1/communities/{communityId}/users/update/{userName}")

//        PUT {baseUrl}/api/v3/communities/{小区ID}/users/{emobId}/nickname
        @PUT("/api/v3/communities/{communityId}/users/{emobId}/nickname")
        void updateUserInfo(@Body UpdateUserNickNameRequest request, @Path("communityId") int communityId, @Path("emobId") String emobId, Callback<CommonRespBean<String>> cb);
    }

    private void fixUserInfo() {
        final String nikename = et_Fix_Nickname.getText().toString();
        if (et_Fix_Nickname.getPaint().measureText(nikename) > 240) {
            Toast.makeText(this, "昵称过长，建议使用真实姓名!", Toast.LENGTH_SHORT).show();
            return;
        } else if (nikename.trim().length() == 0) {
            Toast.makeText(this, "昵称不能为空，建议使用真实姓名!", Toast.LENGTH_SHORT).show();
            return;
        }
        mLdDialog.show();
        UpdateUserNickNameRequest request = new UpdateUserNickNameRequest();
        request.setNickname(nikename);

        XJUserService service = RetrofitFactory.getInstance().create(getmContext(),request,XJUserService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> respBean, retrofit.client.Response response) {
                mLdDialog.dismiss();
                if ("no".equals(respBean.getStatus())) {
                    showToast("修改失败，请稍后再试");
                } else {
                    showToast("修改成功");
                    SharedPreferences spf = FixUserInfoActivity.this.getSharedPreferences("xj_property_login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = spf.edit();
                    edit.putString("nickname", et_Fix_Nickname.getText().toString());
                    edit.commit();
                    bean.setNickname(nikename);
                    PreferencesUtil.saveLogin(getmContext(),bean);

                    boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(nikename);
                    if (!updatenick) {
                        EMLog.e("LoginActivity", "update current user nick fail");
                    }
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

        service.updateUserInfo( request, bean.getCommunityId(), bean.getEmobId(), callback);
    }

}
