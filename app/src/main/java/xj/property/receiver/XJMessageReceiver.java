package xj.property.receiver;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import org.json.JSONObject;

import xj.property.HXSDKHelper;
import xj.property.activity.HXBaseActivity.MainActivity;
import xj.property.activity.LifeCircle.FriendZoneIndexActivity;
import xj.property.hxlib.model.HXSDKModel;
import xj.property.service.PushPullReqService;
import xj.property.utils.other.Config;

/**
 * Created by n on 2015/6/5.
 */
public class XJMessageReceiver extends PushMessageReceiver {
    
    private static String TAG = "mipushdebug";

    NotifyNotification myNotification;
    Ringtone ringtone = null;
    protected AudioManager audioManager;



    @Override
    public void onReceiveMessage(Context context, MiPushMessage miPushMessage) {
        super.onReceiveMessage(context, miPushMessage);
        Log.v(TAG,"onReceiveMessage is called. " + miPushMessage.toString());
    }

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message){
        Log.v(TAG, "onReceivePassThroughMessage is called. " + message.toString());
//        String log = context.getString(R.string.recv_passthrough_message, message.getContent());
//        MainActivity.logList.add(0, getSimpleDate() + " " + log);
//        Message msg = Message.obtain();
//        if (message.isNotified()) {
//            msg.obj = log;
//        }
//        DemoApplication.getHandler().sendMessage(msg);
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message){

        Log.v(TAG, "onNotificationMessageClicked is called. " + message.toString());
        JSONObject jsonObject=null;
        Intent     updateIntent=null;
        String data = message.getContent();
        try {
            jsonObject=new JSONObject(data);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(jsonObject==null)return;
        int cmd_code= jsonObject.optInt(Config.EXPKey_CMD_CODE);
        switch (cmd_code) {
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
                break;

            case 110:
                break;
            case -1:
                break;
            case 121://评论
            case 122://赞

                updateIntent=new Intent(context, FriendZoneIndexActivity.class);
                updateIntent.putExtra(Config.INTENT_BACKMAIN,true);
        }
        if(updateIntent==null)
            updateIntent   = new Intent(context, MainActivity.class);
        updateIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(updateIntent);
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message){
        Log.v(TAG,
                "onNotificationMessageArrived is called. " + message.toString());

        context.startService(new Intent(context,PushPullReqService.class));


/*        JSONObject jsonObject=null;
        String data = message.getContent();
        try {

            jsonObject=new JSONObject(data);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(jsonObject==null)return;
        if(!  PushUtil.onPushComing(jsonObject, Config.PHONETYPE)){
           MiPushClient.clearNotification(context,message.getNotifyId());
            return;
        }
        int cmd_code= jsonObject.optInt(Config.EXPKey_CMD_CODE);
        switch (cmd_code) {
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
                String emobid = "-1";
                if (PreferencesUtil.getLogin(XjApplication.getInstance()))
                    emobid = PreferencesUtil.getLoginInfo(XjApplication.getInstance()).getEmobId();
//                        else
//                            emobid = "-1";
                XJNotify notify = new XJNotify(emobid == null ? "-1" : emobid, cmd_code, jsonObject.optString("title"), jsonObject.optString("content"), jsonObject.optInt("timestamp"), false,"no");
                notify.save();
                EventBus.getDefault().post(new NewNotifyEvent(notify, true));
                break;
            case 110:
                int count = PreferencesUtil.getUnReadCount(XjApplication.getInstance()) + 1;
                PreferencesUtil.saveUnReadCount(XjApplication.getInstance(), count);
                EventBus.getDefault().post(new NewPushEvent(cmd_code));
                MiPushClient.clearNotification(context,message.getNotifyId());
                break;
            case -1:
                break;
            case 121://评论
            case 122://赞
                int circleCount = PreferencesUtil.getUnReadCircleCount(XjApplication.getInstance()) + 1;
                PreferencesUtil.saveUnReadCircleCount(XjApplication.getInstance(), circleCount);
                EventBus.getDefault().post(new NewPushEvent(cmd_code));
        }*/

    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        Log.v(TAG,
                "onCommandResult is called. " + message.toString());

//        onCommandResult is called. command={set-alias}, resultCode={0}, reason={null}, category={null}, commandArguments={[2011eb792db7b1029341faab3ad65919]}
//        String command = message.getCommand();
//        List<String> arguments = message.getCommandArguments();
//        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
//        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
//        String log = "";
//        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
//            if (message.getResultCode() == ErrorCode.SUCCESS) {
//                log = context.getString(R.string.register_success);
//            } else {
//                log = context.getString(R.string.register_fail);
//            }
//        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
//            if (message.getResultCode() == ErrorCode.SUCCESS) {
//                log = context.getString(R.string.set_alias_success, cmdArg1);
//            } else {
//                log = context.getString(R.string.set_alias_fail, message.getReason());
//            }
//        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
//            if (message.getResultCode() == ErrorCode.SUCCESS) {
//                log = context.getString(R.string.unset_alias_success, cmdArg1);
//            } else {
//                log = context.getString(R.string.unset_alias_fail, message.getReason());
//            }
//        }  else if (MiPushClient.COMMAND_SET_ACCOUNT.equals(command)) {
//            if (message.getResultCode() == ErrorCode.SUCCESS) {
//                log = context.getString(R.string.set_account_success, cmdArg1);
//            } else {
//                log = context.getString(R.string.set_account_fail, message.getReason());
//            }
//        } else if (MiPushClient.COMMAND_UNSET_ACCOUNT.equals(command)) {
//            if (message.getResultCode() == ErrorCode.SUCCESS) {
//                log = context.getString(R.string.unset_account_success, cmdArg1);
//            } else {
//                log = context.getString(R.string.unset_account_fail, message.getReason());
//            }
//        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
//            if (message.getResultCode() == ErrorCode.SUCCESS) {
//                log = context.getString(R.string.subscribe_topic_success, cmdArg1);
//            } else {
//                log = context.getString(R.string.subscribe_topic_fail, message.getReason());
//            }
//        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
//            if (message.getResultCode() == ErrorCode.SUCCESS) {
//                log = context.getString(R.string.unsubscribe_topic_success, cmdArg1);
//            } else {
//                log = context.getString(R.string.unsubscribe_topic_fail, message.getReason());
//            }
//        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
//            if (message.getResultCode() == ErrorCode.SUCCESS) {
//                log = context.getString(R.string.set_accept_time_success, cmdArg1, cmdArg2);
//            } else {
//                log = context.getString(R.string.set_accept_time_fail, message.getReason());
//            }
//        } else {
//            log = message.getReason();
//        }
//        MainActivity.logList.add(0, getSimpleDate() + "    " + log);
//
//        Message msg = Message.obtain();
//        msg.obj = log;
//        DemoApplication.getHandler().sendMessage(msg);


    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message){
        Log.v(TAG,
                "onReceiveRegisterResult is called. " + message.toString());
//        String command = message.getCommand();
//        String log;
//        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
//            if (message.getResultCode() == ErrorCode.SUCCESS) {
//                log = context.getString(R.string.register_success);
//            } else {
//                log = context.getString(R.string.register_fail);
//            }
//        } else {
//            log = message.getReason();
//        }
//
//        Message msg = Message.obtain();
//        msg.obj = log;
//        DemoApplication.getHandler().sendMessage(msg);
    }

//    @SuppressLint("SimpleDateFormat")
//    public static String getSimpleDate() {
//        return new SimpleDateFormat("MM-dd hh:mm:ss").format(new Date());
//    }
//
//    public static class DemoHandler extends Handler {
//
//        private Context context;
//
//        public DemoHandler(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            String s = (String) msg.obj;
//            if (MainActivity.sMainActivity != null) {
//                MainActivity.sMainActivity.refreshLogInfo();
//            }
//            if (!TextUtils.isEmpty(s)) {
//                Toast.makeText(context, s, Toast.LENGTH_LONG).show();
//            }
//        }
//    }
long lastNotifiyTime=0;
    public void viberateAndPlayTone(Context context) {

        HXSDKModel model = HXSDKHelper.getInstance().getModel();
        if(!model.getSettingMsgNotification()){
            return;
        }

        if (System.currentTimeMillis() - lastNotifiyTime < 1000) {
            // received new messages within 2 seconds, skip play ringtone
            return;
        }
        audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        Vibrator  vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        try {
            lastNotifiyTime = System.currentTimeMillis();

            // 判断是否处于静音模式
            if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                return;
            }

            if(model.getSettingMsgVibrate()){
                long[] pattern = new long[] { 0, 180, 80, 120 };
                vibrator.vibrate(pattern, -1);
            }
            //    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            if(model.getSettingMsgSound()){
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