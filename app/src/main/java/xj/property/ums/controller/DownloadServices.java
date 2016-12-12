package xj.property.ums.controller;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.io.File;

import xj.property.R;
import xj.property.activity.HXBaseActivity.LoginDialogActivity;
import xj.property.activity.HXBaseActivity.MainActivity;
import xj.property.activity.user.UpdateComplete;
import xj.property.utils.other.Config;

/**
 * Created by Administrator on 2015/5/7.
 */
public class DownloadServices extends Service {
    private final static int DOWNLOAD_COMPLETE = -2;
    private final static int DOWNLOAD_FAIL = -1;

    //自定义通知栏类
    MyNotification myNotification;

    String filePathString; //下载文件绝对路径(包括文件名)

    //通知栏跳转Intent
    private Intent updateIntent = null;
    private PendingIntent updatePendingIntent = null;

    DownFileThread downFileThread;  //自定义文件下载线程

    private Handler updateHandler = new  Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case DOWNLOAD_COMPLETE:
                    //点击安装PendingIntent
                    Uri uri = Uri.fromFile(downFileThread.getApkFile());
                    Intent installIntent = new Intent(Intent.ACTION_VIEW);
                    installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
                    updatePendingIntent = PendingIntent.getActivity(DownloadServices.this, 0, installIntent, 0);
                    myNotification.changeContentIntent(updatePendingIntent);
                    myNotification.notification.defaults= Notification.DEFAULT_SOUND;//铃声提醒

                    myNotification.changeNotificationText("下载完成，请点击安装！");
//           Intent intent=         new Intent(DownloadServices.this, UpdateComplete.class);
//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
                    //停止服务
                    //  myNotification.removeNotification();
                    installApk();

                    stopSelf();
                    break;
                case DOWNLOAD_FAIL:
                    //下载失败
                    //                	myNotification.changeProgressStatus(DOWNLOAD_FAIL);
                    myNotification.changeNotificationText("文件下载失败！");
                    stopSelf();
                    break;
                default:  //下载中
                    Log.i("service", "default" + msg.what);
                    //        	myNotification.changeNotificationText(msg.what+"%");
                    myNotification.changeProgressStatus(msg.what);
            }
        }
    };

    public DownloadServices() {
        // TODO Auto-generated constructor stub
        //	mcontext=context;
        Log.i("service","DownloadServices1");

    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        Log.i("service","onCreate");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Log.i("service","onDestroy");
        if(downFileThread!=null)
            downFileThread.interuptThread();
        stopSelf();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.i("service","onStartCommand");

        updateIntent = new Intent(this, MainActivity.class);
        PendingIntent	updatePendingIntent = PendingIntent.getActivity(this,0,updateIntent,0);
        myNotification=new MyNotification(this, updatePendingIntent, 1);

        //	myNotification.showDefaultNotification(R.drawable.ic_launcher, "测试", "开始下载");
        myNotification.showCustomizeNotification(R.drawable.ic_launcher, "帮帮", R.layout.notification);

        filePathString= Environment.getExternalStorageDirectory().getAbsolutePath() + "/bangbangreplase.apk";
        if(intent!=null){
String apkurl=intent.getStringExtra(Config.INTENT_PARMAS1);
        //开启一个新的线程下载，如果使用Service同步下载，会导致ANR问题，Service本身也会阻塞
//        downFileThread=new  DownFileThread(updateHandler,"http://10.103.241.247:8013/update/download",filePathString);
        downFileThread=new  DownFileThread(updateHandler,apkurl,filePathString);
        new Thread(downFileThread).start();
        }
        return super.onStartCommand(intent, Service.START_REDELIVER_INTENT, startId);
//        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    @Deprecated
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        Log.i("service","onStart");
        super.onStart(intent, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        Log.i("service","onBind");
        return null;
    }

    /**
     * install apk
     *
     */
    private void installApk() {
//        Log.i("debbug","installApk");
        File apkfile = new File(filePathString);
//        Log.i("debbug","installApk"+"   file://" + apkfile.toString());
        if (!apkfile.exists()) {
            return;
        }
//        Log.i("debbug","installApk");
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }

}