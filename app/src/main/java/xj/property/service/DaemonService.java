package xj.property.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.coolerfall.daemon.Daemon;

import xj.property.utils.XJPushManager;

public class DaemonService extends Service {

//	Timer timer;
//	TimerTask timerTask;
//	Logger gLogger;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("Daemon", "onCreate in Daemon service");
//		startForeground(-1213, new Notification());
//		Daemon.run(this, DaemonService.class, Daemon.INTERVAL_ONE_MINUTE * 2);
		Daemon.run(this, DaemonService.class, Daemon.INTERVAL_ONE_MINUTE);
//		configLog();
//		startTimer();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		/* do something here */
//		return super.onStartCommand(intent, flags, startId);

        Log.d("daemon", "onStartCommand of service time : "+ System.currentTimeMillis()/1000L);

        XJPushManager xjPushManager = new XJPushManager(getApplicationContext());
        xjPushManager.checkPushService();

//        xjPushManager.registerLoginedPushService();
        Log.d("daemon", "onStartCommand of service init pushservice  end ");

		return Service.START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("daemon", "onDestroy of service");
	}


//	public void startTimer() {
//		Log.i("timer","start timer");
//		timer = new Timer();
//		//initialize the TimerTask's job
//		initializeTimerTask();
//		//schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
//		timer.schedule(timerTask, 500, 30000); // 每30s 记录一次log
//	}
//
//	public void stoptimertask(View v) {
//		//stop the timer, if it's not already null
//		if (timer != null) {
//			timer.cancel();
//			timer = null;
//		}
//	}
//
//	public void initializeTimerTask() {
//		timerTask = new TimerTask() {
//			public void run() {
//				//use a handler to run a toast that shows the current timestamp
////				Log.i("timer","working");
//				gLogger.debug(new Date().toString() + " working");
//			}
//		};
//	}
//
//	//log4jconfig
//	public void configLog()
//	{
//		Log.i("timer","config log");
//		final LogConfigurator logConfigurator = new LogConfigurator();
//
//		logConfigurator.setFileName(Environment.getExternalStorageDirectory() + File.separator + "working.log");
//		// Set the root log level
//		logConfigurator.setRootLevel(Level.DEBUG);
//		// Set log level of a specific logger
//		logConfigurator.setLevel("org.apache", Level.ERROR);
//		logConfigurator.configure();
//
//		//gLogger = Logger.getLogger(this.getClass());
//		gLogger = Logger.getLogger("timer");
//	}

}
