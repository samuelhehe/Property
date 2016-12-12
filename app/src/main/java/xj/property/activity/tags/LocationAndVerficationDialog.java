package xj.property.activity.tags;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import xj.property.R;
import xj.property.beans.UserGroupBean;
import xj.property.event.FriendsChoicedBackEvent;

public class LocationAndVerficationDialog extends Dialog  {

    public static final int FLAG_LOCATIONING = 1;
    public static final int FLAG_VERFICATIONING = 2;
    private static String TAG = "LocationAndVerficationDialog";
    private final int flag;

    private Context mContext;

    private TextView location_verfication_status_tv;
    private ImageView iv_cricle;


    public LocationAndVerficationDialog(Context context, int flag) {
        super(context, R.style.Dialog_Fullscreen);
        this.mContext = context;
        this.flag = flag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_location_verfication_mgr);
        initView();
    }

    private RotateAnimation am;

    private void initView() {
        iv_cricle = (ImageView) findViewById(R.id.iv_cricle);
        location_verfication_status_tv = (TextView) findViewById(R.id.location_verfication_status_tv);
        location_verfication_status_tv.setText("");
        if(flag==FLAG_LOCATIONING){
            location_verfication_status_tv.setText("正在定位...");
        }else if(flag==FLAG_VERFICATIONING) {
            location_verfication_status_tv.setText("正在验证...");
        }
        am = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        am.setDuration(1000);
        am.setInterpolator(new LinearInterpolator()); // 匀速
        am.setFillAfter(true);
        am.setRepeatCount(-1);
        iv_cricle.startAnimation(am);

    }



    @Override
    public void dismiss() {
        super.dismiss();
        if (iv_cricle != null) {
            iv_cricle.clearAnimation();
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        if (iv_cricle != null) {
            iv_cricle.clearAnimation();
        }
    }

}
