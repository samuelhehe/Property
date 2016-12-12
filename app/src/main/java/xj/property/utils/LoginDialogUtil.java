package xj.property.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import xj.property.R;
import xj.property.activity.user.UsernameActivity;
import xj.property.utils.other.UserUtils;

/**
 * Created by n on 2015/4/29.
 */
public class LoginDialogUtil {
    private static AlertDialog dialog;
    public  static void showLoginDialog(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_login, null);
        final EditText et_username=(EditText)view.findViewById(R.id.et_username);
        final EditText et_password=(EditText)view.findViewById(R.id.et_password);
        TextView tv_go_regist=(TextView)view.findViewById(R.id.tv_go_regist);
        TextView tv_cancle=(TextView)view.findViewById(R.id.tv_cancle);
        TextView tv_login=(TextView)view.findViewById(R.id.tv_login);
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_username.getText().toString()!=null&&et_password.getText().toString()!=null){
                    UserUtils.loginUser(context,et_username.getText().toString(),et_password.getText().toString(),new Handler());
                    dialog.dismiss();
                }
            }
        });

        tv_go_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, UsernameActivity.class);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });

        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setView(view, 0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }
}
