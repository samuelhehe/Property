/**
 * Cobub Razor
 *
 * An open source analytics android sdk for mobile applications
 *
 * @package		Cobub Razor
 * @author		WBTECH Dev Team
 * @copyright	Copyright (c) 2011 - 2012, NanJing Western Bridge Co.,Ltd.
 * @license		http://www.cobub.com/products/cobub-razor/license
 * @link		http://www.cobub.com/products/cobub-razor/
 * @since		Version 0.1
 * @filesource
 */
package xj.property.ums;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.nostra13.universalimageloader.core.ImageLoader;

import xj.property.R;
import xj.property.activity.HXBaseActivity.MainActivity;
import xj.property.cache.GroupHeader;
import xj.property.ums.common.CommonUtil;
import xj.property.ums.controller.DownloadServices;
import xj.property.utils.other.Config;

public class UpdateManager {
	String appkey;
	public static Context mContext;
	public static String force;
	public  static ProgressDialog progressDialog ;
	private static String Msg = "帮帮发现新版本,现在更新?";
	private static String updateMsg = null;

	public static String apkUrl =null;

	private static Dialog noticeDialog;

	private static final String savePath = "/sdcard/";

	private static String saveFile=null;

	private static ProgressBar mProgress;

	private static final int DOWN_UPDATE = 1;

	private static final int DOWN_OVER = 2;

	private static int progress;

	private static Thread downLoadThread;

	private static boolean interceptFlag = false;
	public String newVersion;
	public String newtime;
  
	
	private static Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				progressDialog.setProgress(progress);
				break;
			case DOWN_OVER:
				installApk();
				break;
			default:
				break;
			}
		};
	};
	public static String now()
	  {
	    Time localTime = new Time("Asia/Beijing");
	    localTime.setToNow();
	    return localTime.format("%Y-%m-%d");
	  }
	public static String nametimeString= now();
      
	public UpdateManager(Context context, String version,String force,String apkurl,String description) {
		appkey = CommonUtil.getAppKey(context);
		this.newVersion = version;
		this.force=force;
		this.apkUrl=apkurl;
		this.mContext = context;
		this.updateMsg= this.Msg+"\n"+version+":"+description;
		this.saveFile=savePath+nametimeString;
	}

	public static  void showNoticeDialog(final Context context) {
        final Dialog noticeDialog = new Dialog(context, R.style.MyDialogStyle);
        noticeDialog.setContentView(R.layout.dialog_conflict);
        TextView tv_cancle = (TextView) noticeDialog.findViewById(R.id.tv_cancle);
        TextView tv_relogin = (TextView) noticeDialog.findViewById(R.id.tv_relogin);
        noticeDialog.findViewById(R.id.ll_notify).setVisibility(View.GONE);
        TextView tv_content=(TextView)noticeDialog.findViewById(R.id.tv_content);
        tv_content.setText(updateMsg);
        ((TextView) noticeDialog.findViewById(R.id.tv_title)).setText("版本更新");
        tv_cancle.setText("再等等");
        tv_relogin.setText("更新");
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noticeDialog.dismiss();
            }
        });
        tv_relogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( Environment.getExternalStorageState()==Environment.MEDIA_MOUNTED){
                    Toast.makeText(context,"没检测到sd卡",Toast.LENGTH_SHORT).show();
                    return;
                }
                //清除缓存
//                new Delete().from(GroupHeader.class).execute();
                ImageLoader.getInstance().clearDiskCache();
                ImageLoader.getInstance().clearMemoryCache();
                noticeDialog.dismiss();
                Intent intent=new Intent(context,DownloadServices.class);
                intent.putExtra(Config.INTENT_PARMAS1,UpdateManager.apkUrl);
//                intent.putExtra(Config.INTENT_PARMAS1,"http://7d9lcl.com2.z0.glb.qiniucdn.com/test_c.apk");
                context.startService(intent);

            }});




//		AlertDialog.Builder builder = new Builder(context);
//		builder.setTitle("版本更新");
//		builder.setMessage(updateMsg);
//		builder.setPositiveButton("确定", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//               if( Environment.getExternalStorageState()==Environment.MEDIA_MOUNTED){
//                   Toast.makeText(context,"没检测到sd卡",Toast.LENGTH_SHORT).show();
//                   return;
//               }
//                //清除缓存
////                new Delete().from(GroupHeader.class).execute();
//                ImageLoader.getInstance().clearDiskCache();
//                ImageLoader.getInstance().clearMemoryCache();
//                dialog.dismiss();
//                Intent intent=new Intent(context,DownloadServices.class);
//                intent.putExtra(Config.INTENT_PARMAS1,UpdateManager.apkUrl);
//                context.startService(intent);
//
//				//showDownloadDialog(context);
//			}
//		});
//		builder.setNegativeButton("取消", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				if(UpdateManager.force.equals("true")){
//					System.exit(0);
//				}else{
//					dialog.dismiss();
//				}
//			}
//		});
//		noticeDialog = builder.create();
        noticeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        noticeDialog.setCancelable(false);
		noticeDialog.show();
	}
	public static  void showSdDialog(final Context context) {
		Builder builder = new Builder(context);
		builder.setTitle("point");
		builder.setMessage("没有sd卡");
		builder.setNegativeButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
					System.exit(0);
			}
		});
		noticeDialog = builder.create();
		noticeDialog.show();
	}
	
	private static void showDownloadDialog(Context context) {
		  progressDialog   =  new ProgressDialog(context);
		  progressDialog.setTitle("正在更新..");
		 
		  progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	progressDialog.setButton("取消", new OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {

			 dialog.dismiss();
			 interceptFlag = true;
			 
		}
	});
	progressDialog.show();
	downloadApk();
	
		
		
	}

	private static Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(apkUrl);

				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(savePath);
				boolean sdCardExist =Environment.getExternalStorageState()
				.equals(Environment.MEDIA_MOUNTED);
				if (!sdCardExist)
				{
					showSdDialog(mContext);

				}
				String apkFile = saveFile;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					mHandler.sendEmptyMessage(DOWN_UPDATE);  
					if (numread <= 0) {
						progressDialog.dismiss();
						
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};
	

	/**
	 * download apk
	 * 
	 * @param url
	 */

	private static void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * install apk
	 * 
	 * @param url
	 */
	private static void installApk() {
		File apkfile = new File(saveFile);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);

	}
	
}
