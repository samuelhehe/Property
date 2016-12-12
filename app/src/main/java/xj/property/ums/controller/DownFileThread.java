package xj.property.ums.controller;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

/**
 * Created by Administrator on 2015/5/7.
 */
@SuppressLint("NewApi")
public class DownFileThread implements Runnable {
    public final static int DOWNLOAD_COMPLETE = -2;
    public final static int DOWNLOAD_FAIL = -1;
    public final static String TAG = "onion";
    Handler mHandler; //传入的Handler,用于像Activity或service通知下载进度
    String urlStr;  //下载URL
    File apkFile;   //文件保存路径
    boolean isFinished; //下载是否完成
    boolean interupted=false;  //是否强制停止下载线程
    public DownFileThread(Handler handler,String urlStr,String filePath)
    {
        Log.i(TAG, urlStr);
        this.mHandler=handler;
        this.urlStr=urlStr;
        apkFile=new File(filePath);
        isFinished=false;
    }
    public File getApkFile()
    {
        if(isFinished)
            return apkFile;
        else
            return null;
    }
    public boolean isFinished() {
        return isFinished;
    }

    /**
     * 强行终止文件下载
     */
    public void  interuptThread()
    {
        interupted=true;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            java.net.URL url = null;
            HttpURLConnection conn = null;
            InputStream iStream = null;
//			if (DEVELOPER_MODE)
            {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }else
                { StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                        .detectDiskReads()
                        .detectDiskWrites()
                        .detectNetwork()   // or .detectAll() for all detectable problems
                        .penaltyLog()
                        .build());
                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                        .detectLeakedSqlLiteObjects()
                        .detectLeakedClosableObjects()
                        .penaltyLog()
                        .penaltyDeath()
                        .build());
                }

            }
            try {
                url = new java.net.URL(urlStr);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(20000);
                iStream = conn.getInputStream();
            } catch (MalformedURLException e) {
                Log.i(TAG, "MalformedURLException");
                e.printStackTrace();
            } catch (Exception e) {
                Log.i(TAG, "获得输入流失败");
                e.printStackTrace();
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(apkFile);
            } catch (FileNotFoundException e) {
                Log.i(TAG, "获得输出流失败：new FileOutputStream(apkFile);");
                e.printStackTrace();
            }
            BufferedInputStream bis = new BufferedInputStream(iStream);
            byte[] buffer = new byte[1024];
            int len;
            // 获取文件总长度
            int length = conn.getContentLength();
            double rate=(double)100/length;  //最大进度转化为100
            int total = 0;
            int times=0;//设置更新频率，频繁操作ＵＩ线程会导致系统奔溃
            try {
                Log.i("threadStatus", "开始下载");
                while (false==interupted  && ((len = bis.read(buffer)) != -1)) {
                    fos.write(buffer, 0, len);
                    // 获取已经读取长度

                    total += len;
                    int p=(int)(total*rate);
//                    Log.i("num", rate+","+total+","+p);
                    if(times>=512 || p==100)
                    {/*
					这是防止频繁地更新通知，而导致系统变慢甚至崩溃。
                                                             非常重要。。。。。*/
                        Log.i("time", "time");
                        times=0;
                        Message msg = Message.obtain();
                        msg.what =p ;
                        mHandler.sendMessage(msg);
                    }
                    times++;
                }
                fos.close();
                bis.close();
                iStream.close();
                if(total==length)
                {
                    isFinished=true;
                    mHandler.sendEmptyMessage(DOWNLOAD_COMPLETE);
                    Log.i(TAG, "下载完成结束");
                }
                Log.i(TAG, "强制中途结束");
                //mhandler.sendEmptyMessage(4);
            } catch (IOException e) {
                Log.i(TAG, "异常中途结束");
                mHandler.sendEmptyMessage(DOWNLOAD_FAIL);
                e.printStackTrace();
            }
        }
        else
        {
            Log.i(TAG, "外部存储卡不存在，下载失败！");
            mHandler.sendEmptyMessage(DOWNLOAD_FAIL);
        }
    }
}