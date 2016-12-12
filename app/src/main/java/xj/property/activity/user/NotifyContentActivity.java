package xj.property.activity.user;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.cache.XJNotify;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;

/**
 * Created by Administrator on 2015/4/1.
 */
public class NotifyContentActivity extends HXBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifycontent);
        initTitle(null, "通知详情", "");
        XJNotify notify = (XJNotify) getIntent().getSerializableExtra(Config.INTENT_PARMAS1);
        Log.i("onion", "查看通知" + notify.toString());
        ((TextView) findViewById(R.id.tv_notify_title)).setText(notify.title + "");
        ((TextView) findViewById(R.id.tv_time)).setText(StrUtils.getTime4Millions(notify.timestamp * 1000l));
        ((TextView) findViewById(R.id.tv_content)).setText(notify.content + "");
    }

    @Override
    public void onClick(View v) {

    }
}
