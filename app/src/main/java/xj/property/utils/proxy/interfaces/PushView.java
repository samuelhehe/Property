package xj.property.utils.proxy.interfaces;

import android.content.Context;
import android.content.Intent;

/**
 * Created by maxwell on 15/2/9.
 */
public interface PushView {
    public void pushView(Context context, boolean isAuth, Intent intentPush);
}
