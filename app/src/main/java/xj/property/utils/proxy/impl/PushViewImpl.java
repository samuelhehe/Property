package xj.property.utils.proxy.impl;

import android.content.Context;
import android.content.Intent;

import xj.property.utils.proxy.interfaces.PushView;

/**
 * Created by maxwell on 15/2/9.
 */
public class PushViewImpl implements PushView{

    @Override
    public void pushView(Context context, boolean isAuth, Intent intentPush) {
        context.startActivity(intentPush);
    }
}
