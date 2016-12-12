package xj.property.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.igexin.sdk.PushConsts;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.json.JSONException;
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

public class MessageReceiver extends XGPushBaseReceiver {
    //	private Intent intent = new Intent("com.qq.xgdemo.activity.UPDATE_LISTVIEW");
    public static final String LogTag = "TPushReceiver";
    private AudioManager audioManager;
    private Ringtone ringtone;

    private void show(Context context, String text) {
//        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    // 通知展示
    @Override
    public void onNotifactionShowedResult(Context context,
                                          XGPushShowedResult notifiShowedRlt) {
        if (context == null || notifiShowedRlt == null) {
            return;
        }
//		XGNotification notific = new XGNotification();
//		notific.setMsg_id(notifiShowedRlt.getMsgId());
//		notific.setTitle(notifiShowedRlt.getTitle());
//		notific.setContent(notifiShowedRlt.getContent());
//		// notificationActionType==1为Activity，2为url，3为intent
//		notific.setNotificationActionType(notifiShowedRlt
//				.getNotificationActionType());
//		// Activity,url,intent都可以通过getActivity()获得
//		notific.setActivity(notifiShowedRlt.getActivity());
//		notific.setUpdate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//				.format(Calendar.getInstance().getTime()));
//		NotificationService.getInstance(context).save(notific);
//		context.sendBroadcast(intent);
        show(context, "您有1条新消息, " + "通知被展示 ， " + notifiShowedRlt.toString());

    }

    @Override
    public void onUnregisterResult(Context context, int errorCode) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "反注册成功";
        } else {
            text = "反注册失败" + errorCode;
        }
        Log.d(LogTag, text);
        show(context, text);

    }

    @Override
    public void onSetTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"设置成功";
        } else {
            text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
        }
        Log.d(LogTag, text);
        show(context, text);

    }

    @Override
    public void onDeleteTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"删除成功";
        } else {
            text = "\"" + tagName + "\"删除失败,错误码：" + errorCode;
        }
        Log.d(LogTag, text);
        show(context, text);

    }

    // 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击
    @Override
    public void onNotifactionClickedResult(Context context,
                                           XGPushClickedResult message) {
        if (context == null || message == null) {
            return;
        }
        String text = "";
        if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
            // 通知在通知栏被点击啦。。。。。
            // APP自己处理点击的相关动作
            // 这个动作可以在activity的onResume也能监听，请看第3点相关内容
            text = "通知被打开 :" + message;
        } else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
            // 通知被清除啦。。。。
            // APP自己处理通知被清除后的相关动作
            text = "通知被清除 :" + message;
        }
//        Toast.makeText(context, "广播接收到通知被点击:" + message.toString(),
//                Toast.LENGTH_SHORT).show();
        // 获取自定义key-value
        String customContent = message.getCustomContent();
        if (customContent != null && customContent.length() != 0) {
            try {
                JSONObject obj = new JSONObject(customContent);
                // key1为前台配置的key
                if (!obj.isNull("key")) {
                    String value = obj.getString("key");
                    Log.d(LogTag, "get custom value:" + value);
                }
                // ...
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // APP自主处理的过程。。。
        Log.d(LogTag, text);
        show(context, text);
    }

    @Override
    public void onRegisterResult(Context context, int errorCode,
                                 XGPushRegisterResult message) {
        // TODO Auto-generated method stub
        if (context == null || message == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = message + "注册成功";
            // 在这里拿token
            String token = message.getToken();
        } else {
            text = message + "注册失败，错误码：" + errorCode;
        }
        Log.d(LogTag, text);
        show(context, text);
    }

    // 消息透传
    @Override
    public void onTextMessage(Context context, XGPushTextMessage message) {
        // TODO Auto-generated method stub
        if (message == null) return;
        String text = "收到消息:" + message.toString();

        // 获取自定义key-value
        String customContent = message.getCustomContent();
        if (!TextUtils.isEmpty(customContent)) {
            try {
                JSONObject jsonObject = new JSONObject(customContent);
                Log.d("pushNotifyreceiver", "[XingeMyReceiver] jsonObject=" + jsonObject);
                Intent updateIntent = null;

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
                        String messageId = jsonObject.optString("messageId");
                        XJNotify notifydb = new Select().from(XJNotify.class).where("messageId = ?", messageId).executeSingle();
                        /// 从db中查询消息
                        if (notifydb != null) {
                            Log.d("XJNotify ", "processingUnreadInfo receiver xinge  repeat return messageId  " + messageId);
                            return;
                        }
                        XJNotify notify = new XJNotify(emobid == null ? "-1" : emobid, cmd_code, jsonObject.optString("title"), jsonObject.optString("content"), jsonObject.optInt("timestamp"), false, "no");
                        notify.save();
                        EventBus.getDefault().post(new NewNotifyEvent(notify, true));
                        break;

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
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // APP自主处理消息的过程...
            Log.d(LogTag,text);

            show(context, text);
        }else{
            Log.d("pushNotifyreceiver", "[XingeMyReceiver] jsonObject= is null " );
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
