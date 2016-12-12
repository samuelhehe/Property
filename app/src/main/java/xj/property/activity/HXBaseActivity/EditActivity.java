package xj.property.activity.HXBaseActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.activeandroid.query.Select;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.PUT;
import retrofit.http.Path;
import xj.property.R;
import xj.property.beans.StatusBean;
import xj.property.beans.UpDateGroupName;
import xj.property.cache.GroupInfo;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.LoadingDialog;

public class
        EditActivity extends HXBaseActivity {
	private EditText editText;
    protected LoadingDialog mLdDialog = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_edit);
        InitDialog();
		editText = (EditText) findViewById(R.id.edittext);
		String title = getIntent().getStringExtra("title");
		String data = getIntent().getStringExtra("data");
		if(title != null)
			((TextView)findViewById(R.id.tv_title)).setText(title);
		if(data != null)
			editText.setText(data);
		editText.setSelection(editText.length());
		findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=editText.getText().toString();
                if(name==null|| TextUtils.isEmpty(name)){
                    showToast("群名不能为空");
                }
                else
                    modifyName(getIntent().getStringExtra(Config.EXPKey_GROUP),name);
            }
        });
	}
    protected void InitDialog() {
        mLdDialog = new LoadingDialog(this);
        // mLdDialog.setMessage(this.getString(R.string.dialog_loading_msg));
        mLdDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                dialog.cancel();
                return false;
            }
        });
    }
	
//	public void save(View view){
//
//	}



    interface ModifyGroupNameService {
//        @PUT("/api/v1/communities/{communityId}/users/{emobId}/activities/{emobGroupId}/update")
//        void modifyName(@Header("signature") String signature, @Body UpDateGroupName upDateGroupName,
//                        @Path("communityId") int communityId,@Path("emobId") String emobId, @Path("emobGroupId") String emobGroupId,Callback<StatusBean> cb);


//        @PUT("/api/v1/communities/{communityId}/users/{emobId}/activities/{emobGroupId}/update")

        @PUT("/api/v3/communities/{communityId}/users")
        void modifyName(@Body UpDateGroupName upDateGroupName, @Path("communityId") int communityId, Callback<CommonRespBean<String>> cb);
    }


    public  void modifyName(final String emobGroupId, final String name){
        hideKeyboard();
        mLdDialog.show();
        UpDateGroupName upDateGroupName=new UpDateGroupName();
        upDateGroupName.setActivityTitle(name);
        upDateGroupName.setEmobGroupId(emobGroupId);
        upDateGroupName.setEmobIdOwner(PreferencesUtil.getLoginInfo(this).getEmobId());

        ModifyGroupNameService service = RetrofitFactory.getInstance().create(getmContext(),upDateGroupName,ModifyGroupNameService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, retrofit.client.Response response) {
                mLdDialog.dismiss();
                if("yes".equals(bean.getStatus())){
                    Intent intent=new Intent();
                    intent.putExtra(Config.EXPKey_GROUP,name);
                    GroupInfo groupInfo=new Select().from(GroupInfo.class).where("group_id = ?",emobGroupId).executeSingle();
                    if(groupInfo!=null){
                        groupInfo.setGroup_name(name);
                        groupInfo.save();
                    }
                    setResult(RESULT_OK,intent);
                    finish();
                }else {
                    showToast("修改失败："+bean.getMessage());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                showNetErrorToast();
                error.printStackTrace();
            }
        };
        service.modifyName(upDateGroupName,PreferencesUtil.getLoginInfo(this).getCommunityId(),callback);
    }


    // 隐藏键盘
    protected void hideKeyboard() {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
