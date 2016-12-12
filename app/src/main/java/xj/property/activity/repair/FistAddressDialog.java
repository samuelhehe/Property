package xj.property.activity.repair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import xj.property.R;
import xj.property.activity.user.FixRoomSelectActivity;
import xj.property.beans.UserInfoDetailBean;
import xj.property.utils.other.AdminUtils;
import xj.property.utils.other.PreferencesUtil;

/**
 * Created by Administrator on 2015/5/12.
 */
public class FistAddressDialog {

    public static void showFistAddressDialog(final Activity context,final int requestFixAddress, final CancelCallBack cancelCallBack){
        View rootView = View.inflate(context, R.layout.dialog_fixaddress, null);
        final AlertDialog dialog = new AlertDialog.Builder(context).setView(rootView).create();
        final TextView textView= (TextView) rootView.findViewById(R.id.tv_address);
        UserInfoDetailBean bean= PreferencesUtil.getLoginInfo(context);
        textView.setText(bean.getUserFloor()+bean.getUserUnit()+bean.getRoom());
        rootView.findViewById(R.id.btn_thanks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                cancelCallBack.onCancel();
            }
        });
        rootView.findViewById(R.id.btn_eva).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                context.startActivityForResult(new Intent(context, FixRoomSelectActivity.class), requestFixAddress);
            }
        });
        dialog.show();
    }


  public  static interface CancelCallBack{
    void  onCancel();
  }
}
