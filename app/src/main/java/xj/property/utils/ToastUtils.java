package xj.property.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/12/11.
 */
public class ToastUtils {


    private static String oldMsg;
    protected static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;

    public static void showToast(Context context, String s) {
        if (toast == null) {
            toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
        }
        oneTime = twoTime;
    }


    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }


    public static void showNetErrorToast(Context context) {
//        Toast.makeText(this, this.getResources().getString(R.string.netError), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "网络异常，请稍后重试！", Toast.LENGTH_SHORT).show();
//        Toast toast = getToast(this, "网络异常，请稍后重试！");
//        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.show();

        showToast(context, "网络异常，请稍后重试！");


    }

    public static void showNoMoreToast(Context context) {

        ToastUtils.showToast(context, "没有更多数据了");
    }


}
