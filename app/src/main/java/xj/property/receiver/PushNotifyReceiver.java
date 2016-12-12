package xj.property.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.activeandroid.query.Select;
import com.igexin.sdk.PushConsts;

import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import xj.property.HXSDKHelper;
import xj.property.R;
import xj.property.XjApplication;
import xj.property.activity.HXBaseActivity.MainActivity;
import xj.property.activity.LifeCircle.FriendZoneIndexActivity;
import xj.property.activity.cooperation.CooperationIndexActivity;
import xj.property.activity.vote.VoteIndexActivity;
import xj.property.cache.XJNotify;
import xj.property.event.NewNotifyEvent;
import xj.property.event.NewPushEvent;
import xj.property.hxlib.model.HXSDKModel;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.PushUtil;

/**
 * Created by Administrator on 2015/5/18.
 */
public class PushNotifyReceiver extends BroadcastReceiver {


    NotifyNotification myNotification;
    Ringtone ringtone = null;
    protected AudioManager audioManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Intent updateIntent = null;
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传（payload）数据
                byte[] payload = bundle.getByteArray("payload");
                if (payload != null) {
                    String data = new String(payload);
                    Log.i("onion", "Got Payload:" + data);
                    // TODO:接收处理透传（payload）数据
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d("pushNotifyreceiver", "[MyReceiver] jsonObject=" + jsonObject);
                    if (jsonObject == null) return;
                    if (!PushUtil.onPushComing(context,jsonObject, "ige")) return;

                    int cmd_code = jsonObject.optInt(Config.EXPKey_CMD_CODE);

                    switch (cmd_code) {
                        case 100:
                        case 101:
                        case 102:
                        case 103:
                        case 104:
                        case 105:
                        case 106: // 交电费
                        case 107: // 帮主
                            String emobid = "-1";
                            if (PreferencesUtil.getLogin(XjApplication.getInstance()))
                                emobid = PreferencesUtil.getLoginInfo(XjApplication.getInstance()).getEmobId();
//                        else
//                            emobid = "-1";
                            String messageId =     jsonObject.optString("messageId");
                            XJNotify notifydb = new Select().from(XJNotify.class).where("messageId = ?", messageId).executeSingle();
                            /// 从db中查询消息
                            if (notifydb != null) {
                                Log.d("XJNotify ","processingUnreadInfo receiver getui  repeat return messageId  "+ messageId);
                                return;
                            }
                            XJNotify notify = new XJNotify(emobid == null ? "-1" : emobid, cmd_code, jsonObject.optString("title"), jsonObject.optString("content"), jsonObject.optInt("timestamp"), false, "no");
                            notify.save();
                            EventBus.getDefault().post(new NewNotifyEvent(notify, true));
                            break;
//                            String emobid2 = "-1";
//
//                            Log.d("getui :  ", "emobid2  " + emobid2);
//                            if (PreferencesUtil.getLogin(XjApplication.getInstance()))
//                                emobid2 = PreferencesUtil.getLoginInfo(XjApplication.getInstance()).getEmobId();
//                            //                        else
//                            //                            emobid = "-1";
//                            XJNotify notify2 = new XJNotify(emobid2 == null ? "-1" : emobid2, cmd_code, jsonObject.optString("title"), jsonObject.optString("content"), jsonObject.optInt("timestamp"), false, "no");
//                            notify2.save();
//                            EventBus.getDefault().post(new NewNotifyEvent(notify2, true));

//                            break;
                        case 110:

//                            int count = PreferencesUtil.getUnReadCount(XjApplication.getInstance()) + 1;
//                            PreferencesUtil.saveUnReadCount(XjApplication.getInstance(), count);

                            EventBus.getDefault().post(new NewPushEvent(cmd_code));
                            break;
                        case -1:
                            break;
                        case 121://评论
                        case 122://赞
//                            int circleCount = PreferencesUtil.getUnReadCircleCount(XjApplication.getInstance()) + 1;
//                            PreferencesUtil.saveUnReadCircleCount(XjApplication.getInstance(), circleCount);

                            EventBus.getDefault().post(new NewPushEvent(cmd_code));
                            updateIntent = new Intent(context, FriendZoneIndexActivity.class);
                            updateIntent.putExtra(Config.INTENT_BACKMAIN, true);
                            break;

                        case 131://邻居帮评论
                        case 132://邻居帮赞 // 没有使用

//                            int unreadCooperationCount = PreferencesUtil.getCooperationIndexCount(XjApplication.getInstance()) + 1;
//                            PreferencesUtil.saveCooperationIndexCount(XjApplication.getInstance(), unreadCooperationCount); 2015/12/2

                            EventBus.getDefault().post(new NewPushEvent(cmd_code));
                            updateIntent = new Intent(context, CooperationIndexActivity.class);
                            updateIntent.putExtra(Config.INTENT_BACKMAIN, true);
                            break;

                        case 141:
                        case 142:
                        case 143:
                        case 144:

//                    1、帮主投票里，有人给我投票了     XXX对你投了一票-->election-->CMD_CODE 141
//
//                    2、我发起的投票有人投了         XXX参与了你的投票-->vote-->CMD_CODE 142
//
//                    3、有人给我发起的投票评论了     XXX对你的投票发表了评论-->comment-->CMD_CODE 143
//
//                    4、有人回复了我对某投票的评论。   XXX回复了你的投票评论 -->reply-->CMD_CODE 144

//                            int unreadVoteCount = PreferencesUtil.getVoteIndexCount(XjApplication.getInstance()) + 1;
//                            PreferencesUtil.saveVoteIndexCount(XjApplication.getInstance(), unreadVoteCount);

                            EventBus.getDefault().post(new NewPushEvent(cmd_code));
                            updateIntent = new Intent(context, VoteIndexActivity.class);
                            updateIntent.putExtra(Config.INTENT_BACKMAIN, true);
                            break;

                    }

                    if (updateIntent == null)
                        updateIntent = new Intent(context, MainActivity.class);
                    updateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent updatePendingIntent = PendingIntent.getActivity(context, 0, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    // myNotification=new NotifyNotification(context, updatePendingIntent, 1);
                    //	myNotification.showDefaultNotification(R.drawable.ic_launcher, "测试", "开始下载");
                    //  myNotification.showCustomizeNotification(R.drawable.ic_launcher,  jsonObject.optString("title"), R.layout.pushnotification, jsonObject.optString("content"));

                    Bitmap btm = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.ic_launcher);
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                            context).setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(jsonObject.optString("title"))
                            .setContentText(jsonObject.optString("content"));
                    mBuilder.setLargeIcon(btm);
                    mBuilder.setAutoCancel(true);//自己维护通知的消失
                    mBuilder.setContentIntent(updatePendingIntent);
                    NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                    Notification notification=;
//                    notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
//                    notification.defaults |=;
                    viberateAndPlayTone(context);
                    mNotificationManager.notify(0, mBuilder.build());
                }
                break;
            //添加其他case
            //.........
            default:
                break;
        }
    }

    long lastNotifiyTime = 0;

    public void viberateAndPlayTone(Context context) {

        HXSDKModel model = HXSDKHelper.getInstance().getModel();
        if (!model.getSettingMsgNotification()) {
            return;
        }

        if (System.currentTimeMillis() - lastNotifiyTime < 1000) {
            // received new messages within 2 seconds, skip play ringtone
            return;
        }
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        try {
            lastNotifiyTime = System.currentTimeMillis();

            // 判断是否处于静音模式
            if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                return;
            }

            if (model.getSettingMsgVibrate()) {
                long[] pattern = new long[]{0, 180, 80, 120};
                vibrator.vibrate(pattern, -1);
            }
            //    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            if (model.getSettingMsgSound()) {
                if (ringtone == null) {
                    Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    ringtone = RingtoneManager.getRingtone(context, notificationUri);
                    if (ringtone == null) {
//                        EMLog.d(TAG, "cant find ringtone at:" + notificationUri.getPath());
                        return;
                    }
                }

                if (!ringtone.isPlaying()) {
                    String vendor = Build.MANUFACTURER;

                    ringtone.play();
                    // for samsung S3, we meet a bug that the phone will
                    // continue ringtone without stop
                    // so add below special handler to stop it after 3s if
                    // needed
                    if (vendor != null && vendor.toLowerCase().contains("samsung")) {
                        Thread ctlThread = new Thread() {
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                    if (ringtone.isPlaying()) {
                                        ringtone.stop();
                                    }
                                } catch (Exception e) {
                                }
                            }
                        };
                        ctlThread.run();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}