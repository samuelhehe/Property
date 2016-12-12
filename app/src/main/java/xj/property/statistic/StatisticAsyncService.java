package xj.property.statistic;

import android.app.IntentService;
import android.content.Intent;

import com.activeandroid.util.Log;

/**
 * Created by Administrator on 2016/1/13.
 */
public class StatisticAsyncService extends IntentService {

    public StatisticAsyncService() {
        super("StatisticAsyncService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public StatisticAsyncService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("StatisticAsyncService", " onHandleIntent ");
        EventServiceUtils eventServiceUtils = new EventServiceUtils(getApplication());
        //// 上传当前时间之前的所有事件
        eventServiceUtils.postBeforeTimeEvent();

    }
}
