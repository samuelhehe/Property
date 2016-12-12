package xj.property.utils.other;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import xj.property.R;
import xj.property.beans.IndexBean;

/**
 * Created by Administrator on 2015/3/24.
 */
public class BaseUtils {
    public static LinearLayout add3Menu2LinearLyout(Context context, List<IndexBean> beans, int width, int high) {
        LinearLayout layout = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, high);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(params);
        for (int i = 0; i < beans.size(); i++) {
            layout.addView(addSingleMenu(context, beans.get(i), width));
        }
        return layout;
    }

    private static View addSingleMenu(Context context, IndexBean bean, int width) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        View view = View.inflate(context, R.layout.item_main, null);
        ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon_main);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name_main);
        iv_icon.setImageResource(context.getResources().getIdentifier(bean.getImgName(), "drawable",
                "xj.property"));
        tv_name.setText(bean.getServiceName());
        view.setLayoutParams(params);
//        view.setOnClickListener(bean.listener);  2015/12/2
        return view;
    }

    /**
     * 设置loading旋转
     *
     * @param view
     */
    public static void setLoadingImageAnimation(ImageView view) {
        RotateAnimation am = new RotateAnimation(0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        am.setDuration(1000);
        am.setInterpolator(new LinearInterpolator()); // 匀速
        am.setFillAfter(true);
        am.setRepeatCount(-1);
        view.setAnimation(am);
        am.startNow();
    }

    /**
     * 设置loading旋转
     *
     * @param view
     */
    public static void setJumpAnimation(View view, float targetX, float targetY) {
        TranslateAnimation am = new TranslateAnimation(Animation.ABSOLUTE, view.getX(), Animation.ABSOLUTE, targetX, Animation.ABSOLUTE, view.getY(), Animation.ABSOLUTE, targetY);
        am.setDuration(30000);
        am.setFillAfter(true);
        view.setAnimation(am);
        am.startNow();
    }

    /**
     * 获取sdk版本号
     *
     * @return
     */
    public static int getAndroidOSVersion() {
        int osVersion;
        try {
            osVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            osVersion = 0;
        }

        return osVersion;
    }
}
