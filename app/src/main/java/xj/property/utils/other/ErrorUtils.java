package xj.property.utils.other;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import java.util.List;

import xj.property.R;
import xj.property.utils.CommonUtils;

/**
 * Created by n on 2015/5/7.
 */
public class ErrorUtils {

    private static Handler handler;

    public static  void errorPage(Context context,List list, Handler handler){
        ErrorUtils.handler=handler;
        if (!CommonUtils.isNetWorkConnected(context)) {
            handler.sendEmptyMessage(Config.NETERROR);
            return;
        }else if (list==null&&list.size()==0){
            handler.sendEmptyMessage(Config.NOTHING);
            return;
        }
    }



}
