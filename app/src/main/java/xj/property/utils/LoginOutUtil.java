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
import xj.property.fragment.MeFragment;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

/**
 * Created by n on 2015/4/29.
 */
public class LoginOutUtil {
    private static AlertDialog logout_dialog;
    public  static void showLoginDialog(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_userlogout, null);
        final TextView cancle=(TextView)view.findViewById(R.id.cancle);
        final TextView submit=(TextView)view.findViewById(R.id.submit);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout_dialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesUtil.Logout(context);

                logout_dialog.dismiss();
            }
        });




        logout_dialog = builder.create();
        logout_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        logout_dialog.setView(view, 0, 0, 0, 0);
        logout_dialog.setCanceledOnTouchOutside(true);
        logout_dialog.show();

    }
}
