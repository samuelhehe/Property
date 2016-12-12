package xj.property.activity.user;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.PUT;
import retrofit.http.Path;
import xj.property.R;
import xj.property.beans.UpdateUserRoomRequest;
import xj.property.beans.UserInfoDetailBean;
import xj.property.beans.XJUserInfoBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseUtils;
import xj.property.netbasebean.ModifyUserAddressReqBean;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

public class FixUserAddressConfrimDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = "FixUserAddressConfrimDialog";
    private final UserInfoDetailBean userbean;
    private Context mContext;
    //// 获取A2B的标签
    private List<String> tagsA2Btags;

    /// 取消
    private Button btn_cancel;
    /// 确定
    private Button btn_ok;


    private onChangeAddressStatusListener onTagsA2BDelStatusListener;

    private EditText select_floor_et;
    private EditText select_unit_et;
    private EditText select_room_et;

    public FixUserAddressConfrimDialog(Context context,  onChangeAddressStatusListener onTagsA2BDelStatusListener) {
        super(context, R.style.Theme_CustomDialog);
        this.mContext = context;
        userbean = PreferencesUtil.getLoginInfo(mContext);
        this.onTagsA2BDelStatusListener =onTagsA2BDelStatusListener;
    }
    public FixUserAddressConfrimDialog(Context context) {
        super(context, R.style.Theme_CustomDialog);
        this.mContext = context;
        userbean = PreferencesUtil.getLoginInfo(mContext);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_fix_useraddress);
        initView();
        initData();
    }


    private void initData() {

        if (userbean != null) {

            if (select_floor_et != null) {
                select_floor_et.setText(userbean.getUserFloor());
                select_floor_et.setSelection(select_floor_et.getText().length());
            }

            if (select_unit_et != null) {
                select_unit_et.setText(userbean.getUserUnit());
                select_unit_et.setSelection(select_unit_et.getText().length());
            }

            if (select_room_et != null) {
                select_room_et.setText(userbean.getRoom());
                select_room_et.setSelection(select_room_et.getText().length());
            }
        }else{
            Log.d("initData", "UserInfoDetailBean bean is null");
        }

    }

    public interface onChangeAddressStatusListener{

        void onChangeAddressSuccess(String message);

        void onChangeAddressFail(String message);

        void onChangeAddressCancel();
    }

    private void initView() {

        select_floor_et = (EditText) this.findViewById(R.id.fix_user_floor_et);
        select_unit_et = (EditText) this.findViewById(R.id.fix_user_unit_et);
        select_room_et = (EditText) this.findViewById(R.id.fix_user_room_et);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_cancel:
                cancel();
                break;

            case R.id.btn_ok:

                String item_floor = select_floor_et.getText().toString().trim();
                String item_unit = select_unit_et.getText().toString().trim();
                String item_room = select_room_et.getText().toString().trim();

                if(TextUtils.isEmpty(item_floor)||TextUtils.isEmpty(item_room)){

                    Toast.makeText(getContext(),"请输入正确的地址信息",Toast.LENGTH_SHORT).show();
                }else{
                    fixUserInfo( item_room,item_floor, item_unit);
                }

                break;
        }
    }


    @Override
    public void dismiss() {
        super.dismiss();
//        if(onTagsA2BDelStatusListener!=null){
//            onTagsA2BDelStatusListener.onChangeAddressCancel();
//        }
    }

    private void fixUserInfo(final String item_room, final String item_floors , final String item_unit  ) {
        ModifyUserAddressReqBean request = new ModifyUserAddressReqBean();
        request.setRoom(item_room);
        request.setUserFloor(item_floors);
        request.setUserUnit(item_unit);
        System.out.println("地址： " + item_room + "   " + item_floors + "  " + item_unit);
        NetBaseUtils.modifyUserAddress(getContext(),userbean.getCommunityId(),userbean.getEmobId(),request,new NetBaseUtils.NetRespListener<CommonRespBean<String>>() {
            @Override
            public void successYes(CommonRespBean<String> commonRespBean, Response response) {
                Toast.makeText(getContext(),"修改成功",Toast.LENGTH_SHORT).show();
                PreferencesUtil.saveUserAddress(getContext(),item_floors,item_unit,item_room);
                userbean.setUserFloor(item_floors);
                userbean.setUserUnit(item_unit);
                userbean.setRoom(item_room);
                PreferencesUtil.saveLogin(getContext(),userbean);

                if(onTagsA2BDelStatusListener!=null){
                    onTagsA2BDelStatusListener.onChangeAddressSuccess("");
                }
                dismiss();
            }
            @Override
            public void successNo(CommonRespBean<String> commonRespBean, Response response) {
                Toast.makeText(getContext(),"修改失败，请稍后再试",Toast.LENGTH_SHORT).show();
                if(onTagsA2BDelStatusListener!=null){
                    onTagsA2BDelStatusListener.onChangeAddressFail("'");
                }
            }
            @Override
            public void failure(RetrofitError error) {
                if(onTagsA2BDelStatusListener!=null){
                    onTagsA2BDelStatusListener.onChangeAddressFail("'");
                }
                Toast.makeText(getContext(),"修改失败，请稍后再试",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }


    @Override
    public void cancel() {
        super.cancel();
    }

}
