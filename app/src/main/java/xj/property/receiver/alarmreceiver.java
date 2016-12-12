package xj.property.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Administrator on 2015/6/17.
 */
public  class alarmreceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
// TODO Auto-generated method stub
        Log.i("onion","接到广播");
        if(intent.getAction().equals("short")){
            Toast.makeText(context, "short alarm", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context, "repeating alarm",Toast.LENGTH_LONG).show();
        }

        Intent pushintent =new Intent(context, alarmreceiver.class);
        pushintent.setAction("short");
        PendingIntent sender=
                PendingIntent.getBroadcast(context, 0, pushintent, 0);

//设定一个五秒后的时间
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 5);

        AlarmManager alarm=(AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }
}
