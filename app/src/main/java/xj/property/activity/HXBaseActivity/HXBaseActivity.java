/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xj.property.activity.HXBaseActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.Type;
import com.easemob.util.EasyUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import xj.property.R;
import xj.property.activity.ActivityCommonWebViewPager;
import xj.property.activity.LifeCircle.FriendZoneIndexActivity;
import xj.property.activity.activities.ActivitiesActivity;
import xj.property.activity.call.CourierActivity;
import xj.property.activity.call.EmergencyNumberActivity;
import xj.property.activity.call.SendWaterActivity;
import xj.property.activity.contactphone.FastShopIndexActivity;
import xj.property.activity.cooperation.CooperationIndexActivity;
import xj.property.activity.doorpaste.DoorPasteIndexActivity;
import xj.property.activity.fitmentfinish.FitmentFinishActivity;
import xj.property.activity.genius.GeniusRelationActivity;
import xj.property.activity.genius.GeniusSpecialActivity;
import xj.property.activity.invite.ActivityInviteNeighborsHome;
import xj.property.activity.membership.ActivityMSPCardList;
import xj.property.activity.property.PropertyActivity;
import xj.property.activity.repair.RepairListActivity;
import xj.property.activity.vote.VoteIndexActivity;
import xj.property.activity.welfare.ActivityWelfareIndex;
import xj.property.fragment.IndexFragment;
import xj.property.statistic.EventServiceUtils;
import xj.property.utils.CommonUtils;
import xj.property.utils.ToastUtils;
import xj.property.utils.UpdateUtils;
import xj.property.utils.XJPushManager;
import xj.property.utils.image.utils.ImageLoaderConfig;
import xj.property.utils.other.AppOnForegroundUtils;
import xj.property.utils.other.Config;
import xj.property.widget.LoadingDialog;

public class HXBaseActivity extends FragmentActivity implements View.OnClickListener {

    private static final int notifiId = 11;

    protected NotificationManager notificationManager;
    protected LoadingDialog mLdDialog = null;
    protected Context mContext;
    protected EventServiceUtils eventServiceUtils;

    protected String TAG = "HXBaseActivity";
    protected XJPushManager xjpushManager;
    protected long onbackpress;
    protected ImageView ivBack;
    protected TextView tvLeft;
    protected TextView tvRight;
    protected TextView tvTitle;
    protected ImageView ivRight;
    protected long oncreateTime;

    public String getCurrentPagerUUID() {
        return currentPagerUUID;
    }

    public void setCurrentPagerUUID(String currentPagerUUID) {
        this.currentPagerUUID = currentPagerUUID;
    }

    protected String currentPagerUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

// 在 AndroidManifest style中配置  getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        //        android.os.Debug.startMethodTracing("hxbase");
        this.mContext = this;
        xjpushManager = new XJPushManager(this);
        oncreateTime = System.currentTimeMillis();

        if (!ImageLoader.getInstance().isInited()) {
            ImageLoaderConfig.initImageLoader(this, Config.BASE_IMAGE_CACHE);
        }
        enterClickCall();
        InitDialog();
//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setLogo(R.drawable.ic_launcher);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    }

    protected void enterClickCall() {
        eventServiceUtils = new EventServiceUtils(getmContext());
        if (this instanceof SplashActivity) {
            currentPagerUUID = eventServiceUtils.generateUUID();
            //应用初始的时候 上传当前时间之前的所有事件
            eventServiceUtils.postBeforeTimeEvent();
            eventServiceUtils.postClickEvent(currentPagerUUID, "app");
        } else if (this instanceof MainActivity) {
            currentPagerUUID = eventServiceUtils.generateUUID();
            Log.d(TAG, "oncreate : " + this.getClass().getName() + " currentPagerUUID : " + currentPagerUUID);
            eventServiceUtils.postScheduleClickEvent(currentPagerUUID, "app");

        } else if (this instanceof ChatActivity) {
            ChatActivity chatActivity = (ChatActivity) this;
            //// 这里的统计做的是物业客服
            if (TextUtils.equals(chatActivity.getServantType(), Config.SERVANT_TYPE_BANGBANG) ||
                    TextUtils.equals(chatActivity.getServantType(), Config.SERVANT_TYPE_WEIXIUTOUSU) ||
                    TextUtils.equals(chatActivity.getServantType(), Config.SERVANT_TYPE_SHOPTOUSU) ||
                    TextUtils.equals(chatActivity.getServantType(), Config.SERVANT_TYPE_WUYE)) {

                currentPagerUUID = eventServiceUtils.generateUUID();
                eventServiceUtils.postScheduleClickEvent(currentPagerUUID, "" + IndexFragment.HOME_ITEM_FLAG_CUSTOMRS);
            }
        }
        /**
         * 共 17个模块 不包含客服模块统计.
         */
        else if (this instanceof FriendZoneIndexActivity) {
            currentPagerUUID = eventServiceUtils.generateUUID();
            eventServiceUtils.postScheduleClickEvent(currentPagerUUID, "" + IndexFragment.HOME_ITEM_FLAG_FRIENDZONE);
        } else if (this instanceof SendWaterActivity) {
            currentPagerUUID = eventServiceUtils.generateUUID();
            eventServiceUtils.postScheduleClickEvent(currentPagerUUID, "" + IndexFragment.HOME_ITEM_FLAG_SENDWATER);
        } else if (this instanceof ActivityWelfareIndex) {
            currentPagerUUID = eventServiceUtils.generateUUID();
            eventServiceUtils.postScheduleClickEvent(currentPagerUUID, "" + IndexFragment.HOME_ITEM_FLAG_WELFARE_INDEX);
        } else if (this instanceof ActivityMSPCardList) {
            currentPagerUUID = eventServiceUtils.generateUUID();
            eventServiceUtils.postScheduleClickEvent(currentPagerUUID, "" + IndexFragment.HOME_ITEM_FLAG_MSPCARDLIST);
        } else if (this instanceof EmergencyNumberActivity) {
            currentPagerUUID = eventServiceUtils.generateUUID();
            eventServiceUtils.postScheduleClickEvent(currentPagerUUID, "" + IndexFragment.HOME_ITEM_FLAG_EMERGENCY_NUM);
        } else if (this instanceof CooperationIndexActivity) {
            currentPagerUUID = eventServiceUtils.generateUUID();
            eventServiceUtils.postScheduleClickEvent(currentPagerUUID, "" + IndexFragment.HOME_ITEM_FLAG_COOPERATION);
        } else if (this instanceof ActivityInviteNeighborsHome) {
            currentPagerUUID = eventServiceUtils.generateUUID();
            eventServiceUtils.postScheduleClickEvent(currentPagerUUID, "" + IndexFragment.HOME_ITEM_FLAG_INVITENEGIGHBOR);
        } else if (this instanceof VoteIndexActivity) {
            currentPagerUUID = eventServiceUtils.generateUUID();
            eventServiceUtils.postScheduleClickEvent(currentPagerUUID, "" + IndexFragment.HOME_ITEM_FLAG_VOTE);
        } else if (this instanceof PropertyActivity) {
            currentPagerUUID = eventServiceUtils.generateUUID();
            eventServiceUtils.postScheduleClickEvent(currentPagerUUID, "" + IndexFragment.HOME_ITEM_FLAG_PROPERTY);
        } else if (this instanceof FitmentFinishActivity) {
            currentPagerUUID = eventServiceUtils.generateUUID();
            eventServiceUtils.postScheduleClickEvent(currentPagerUUID, "" + IndexFragment.HOME_ITEM_FLAG_FITMENT);
        } else if (this instanceof GeniusSpecialActivity || this instanceof GeniusRelationActivity) {
            currentPagerUUID = eventServiceUtils.generateUUID();
            eventServiceUtils.postScheduleClickEvent(currentPagerUUID, "" + IndexFragment.HOME_ITEM_FLAG_GENIUS);
        } else if (this instanceof RepairListActivity) {
            currentPagerUUID = eventServiceUtils.generateUUID();
            eventServiceUtils.postScheduleClickEvent(currentPagerUUID, "" + IndexFragment.HOME_ITEM_FLAG_REPAIRER);
        } else if (this instanceof RepairListActivity) {
            currentPagerUUID = eventServiceUtils.generateUUID();
            eventServiceUtils.postScheduleClickEvent(currentPagerUUID, "" + IndexFragment.HOME_ITEM_FLAG_REPAIR);
        } else if (this instanceof FastShopIndexActivity) {
            currentPagerUUID = eventServiceUtils.generateUUID();
            eventServiceUtils.postScheduleClickEvent(currentPagerUUID, "" + IndexFragment.HOME_ITEM_FLAG_FASTSHOP);
        } else if (this instanceof CourierActivity) {
            currentPagerUUID = eventServiceUtils.generateUUID();
            eventServiceUtils.postScheduleClickEvent(currentPagerUUID, "" + IndexFragment.HOME_ITEM_FLAG_COURIER);
        }
//        else if (this instanceof PayListActivity) {
//            currentPagerUUID = eventServiceUtils.generateUUID();
//            eventServiceUtils.postScheduleClickEvent(currentPagerUUID, "" + IndexFragment.HOME_ITEM_FLAG_PAYLIST);
//        }
        else if (this instanceof ActivitiesActivity) {
            currentPagerUUID = eventServiceUtils.generateUUID();
            eventServiceUtils.postScheduleClickEvent(currentPagerUUID, "" + IndexFragment.HOME_ITEM_FLAG_ACTIVITYS);
        } else if (this instanceof ActivityCommonWebViewPager) {
            currentPagerUUID = eventServiceUtils.generateUUID();
            eventServiceUtils.postScheduleClickEvent(currentPagerUUID, "webview");
        } else if (this instanceof DoorPasteIndexActivity) {
            currentPagerUUID = eventServiceUtils.generateUUID();
            eventServiceUtils.postScheduleClickEvent(currentPagerUUID, "" + IndexFragment.HOME_ITEM_FLAG_DOORPASTE);
        }

        Log.d(TAG, "oncreate : " + this.getClass().getName() + " currentPagerUUID : " + currentPagerUUID);


        //// 客服模块暂时无法统计...
//            else if (this instanceof ActivitiesActivity) {
//                eventServiceUtils.postScheduleClickEvent(currentPagerUUID, "" + IndexFragment.HOME_ITEM_FLAG_CUSTOMRS);
//            }
        /**
         /// 客服
         public static final int HOME_ITEM_FLAG_CUSTOMRS = 7;
         */
    }

    /**
     * 记录应用是否在前台
     */
    private boolean isActive = true;

    @Override

    protected void onResume() {
        super.onResume();
        Log.d(TAG, "class name: " + this.getClass().getName() + "oncreate  2 onResume  used : " + (System.currentTimeMillis() - oncreateTime) + "ms");
        // onresume时，取消notification显示
        EMChatManager.getInstance().activityResumed();
        // umeng
//        MobclickAgent.onResume(this);

        if (!isActive) {
            //app 从后台唤醒，进入前台
            isActive = true;
            // Log.i("onion","HXBaseActivity  从后台唤醒，进入前台");
            if (this instanceof MainActivity) {
                if (eventServiceUtils != null) {
                    if (TextUtils.isEmpty(currentPagerUUID)) {
                        currentPagerUUID = eventServiceUtils.generateUUID();
                    }
                    Log.d(TAG, "onResume :  postScheduleClickEvent " + this.getClass().getName() + " currentPagerUUID : " + currentPagerUUID);
                    eventServiceUtils.postScheduleClickEvent(currentPagerUUID, "app");
                }
            }
            onBackToForeground();
        }
    }

    protected void InitDialog() {
        mLdDialog = new LoadingDialog(this);
        // mLdDialog.setMessage(this.getString(R.string.dialog_loading_msg));
        mLdDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                dialog.cancel();
                return false;
            }
        });
    }

    //    protected abstract void initView();
    protected void initTitle(String left, String title, String right) {
        if (findViewById(R.id.heaptop) == null) return;
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvLeft = (TextView) findViewById(R.id.tv_left_text);
        tvRight = (TextView) findViewById(R.id.tv_right_text);
        tvRight.setVisibility(View.INVISIBLE);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        if (TextUtils.isEmpty(left) || TextUtils.isEmpty(title)) {
            ivBack.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            //tvLeft.setTextSize(this.getResources().getDimension(R.dimen.titlefonts));
            ivBack.setVisibility(View.GONE);
            tvLeft.setVisibility(View.VISIBLE);
            tvLeft.setText(left);
            tvLeft.setOnClickListener(this);
        }
        if (title != null) {
            tvTitle.setText(title);
        }
        if (right != null) {
            tvRight.setText(right);
            tvRight.setVisibility(View.VISIBLE);
            //tvRight.setTextSize(this.getResources().getDimension(R.dimen.titlefonts));
            tvRight.setOnClickListener(this);
        }
    }

    protected void initTitle(String left, String title, int resId) {
        if (findViewById(R.id.heaptop) == null) return;
        ivRight = (ImageView) findViewById(R.id.iv_right);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(resId);
        ivRight.setOnClickListener(this);
        initTitle(left, title, "");
    }


    protected void showNetErrorToast() {
//        Toast.makeText(this, this.getResources().getString(R.string.netError), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "网络异常，请稍后重试！", Toast.LENGTH_SHORT).show();
//        Toast toast = getToast(this, "网络异常，请稍后重试！");
//        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.show();

        ToastUtils.showToast(this, "网络异常，请稍后重试");
    }

    protected void showDataErrorToast() {
//        Toast.makeText(this, this.getResources().getString(R.string.netError), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "网络异常，请稍后重试！", Toast.LENGTH_SHORT).show();
//        Toast toast = getToast(this, "网络异常，请稍后重试！");
//        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.show();

        ToastUtils.showToast(this, "数据异常，请稍后重试");
    }

    protected void showDataErrorToast(String reason) {
//        Toast.makeText(this, this.getResources().getString(R.string.netError), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "网络异常，请稍后重试！", Toast.LENGTH_SHORT).show();
//        Toast toast = getToast(this, "网络异常，请稍后重试！");
//        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.show();

        if (TextUtils.isEmpty(reason)) {
            ToastUtils.showToast(this, "数据异常");
        } else {
            ToastUtils.showToast(this, "数据异常：" + reason);
        }
    }

    protected void showNoMoreToast() {
//        Toast.makeText(this, this.getResources().getString(R.string.netError), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "没有更多数据了", Toast.LENGTH_SHORT).show();
//        Toast toast = getToast(this, "没有更多数据了");
//        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.show();

        ToastUtils.showToast(this, "没有更多数据了");
    }


    /**
     * 从后台切换至前台
     */
    protected void onBackToForeground() {
        UpdateUtils.initUpdate(getmContext());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!AppOnForegroundUtils.isAppOnForeground(HXBaseActivity.this)) {
            //app 进入后台
            //全局变量isActive = false 记录当前已经进入后台
            isActive = false;
            Log.d(TAG, "onResume FOR BACKGROUND :  postScheduleExitEvent " + this.getClass().getName());
            if (eventServiceUtils != null) {
                if (TextUtils.isEmpty(currentPagerUUID)) {
                    currentPagerUUID = eventServiceUtils.generateUUID();
                }
                Log.d(TAG, "onResume :  postScheduleExitEvent " + this.getClass().getName() + " currentPagerUUID : " + currentPagerUUID);
                eventServiceUtils.postScheduleExitEvent(currentPagerUUID, this.getClass().getName());
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        // umeng
//        MobclickAgent.onPause(this);
    }

    /**
     * 当应用在前台时，如果当前消息不是属于当前会话，在状态栏提示一下
     * 如果不需要，注释掉即可
     *
     * @param message
     */
    protected void notifyNewMessage(EMMessage message) {
        //如果是设置了不提醒只显示数目的群组(这个是app里保存这个数据的，demo里不做判断)
        //以及设置了setShowNotificationInbackgroup:false(设为false后，后台时sdk也发送广播)
        if (!EasyUtils.isAppRunningForeground(this)) {
            return;
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(getApplicationInfo().icon)
                .setSmallIcon(R.drawable.status_bar_icn)
                .setWhen(System.currentTimeMillis()).setAutoCancel(true);

        String ticker = CommonUtils.getMessageDigest(message, this);
        if (message.getType() == Type.TXT) {
            ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");

        }
        //设置状态栏提示
        if (message.getType() == Type.TXT)
            ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
//        if( !   XJMessageHelper.operatNewMessage(message))
//            XJContactHelper.saveContact(message);
//        if(message.getChatType()==EMMessage.ChatType.GroupChat){
//            GroupHeader header = new Select().from(GroupHeader.class).where("group_id = ?", message.getTo()).executeSingle();
//            if(header==null||header.getNum()<10)
//                GroupUtils.getGroupInfo(message.getTo());
//        }
//              operatNewMessage(message);
//              if(message.getIntAttribute(Config.EXPKey_CMD_CODE,0)!=0){
//                  return  ticker;
//              }
        if (message.getType() == Type.LOCATION) {
            mBuilder.setTicker(message.getStringAttribute(Config.EXPKey_nickname, message.getFrom()) + ":[位置]");
        } else if ("通知".equals(message.getStringAttribute(Config.EXPKey_nickname, "通知"))) {
            mBuilder.setTicker("通知" + message.getStringAttribute("content", ""));
        } else
            mBuilder.setTicker(message.getStringAttribute(Config.EXPKey_nickname, "收到通知") + ": " + ticker);

        Notification notification = mBuilder.build();
        notificationManager.notify(notifiId, notification);
        notificationManager.cancel(notifiId);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!(this instanceof SplashActivity)) {
            if (!TextUtils.isEmpty(currentPagerUUID)) {
                Log.d(TAG, "onDestroy : " + this.getClass().getName() + " currentPagerUUID : " + currentPagerUUID);
                if (eventServiceUtils != null) {
                    //// 退出事件
                    eventServiceUtils.postExitEvent(currentPagerUUID, this.getClass().getSimpleName());
                }
            }
        }
        Log.d(TAG, "onBackPressed 2 onDestroy : " + this.getClass().getName() + " used : " + (System.currentTimeMillis() - onbackpress) + "ms");
//        android.os.Debug.stopMethodTracing();
    }


    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        onBackPressed();
    }

    protected void showToast(String str) {
//        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        ToastUtils.showToast(this, str);
    }

    protected void showToast(int res) {
//        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        ToastUtils.showToast(this, res);
    }

    /**
     * 自定义长方形圆角矩形Toast ,内容自定义, 位置上中下居中.
     *
     * @param showT   显示的内容
     * @param gravity 显示的位置
     */
    protected void showCommonToast(String showT, int gravity) {

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.common_welfare_toast_lay, null);
        TextView title = (TextView) layout.findViewById(R.id.toast_title_tv);
        title.setText(showT);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(gravity, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }


    protected void showMiddleToast(String str) {
        showCommonToast(str, Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
    }

    protected void showTopToast(String str) {
        showCommonToast(str, Gravity.TOP | Gravity.CENTER_HORIZONTAL);
    }

    protected void showButtomToast(String str) {
        showCommonToast(str, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
    }

    // 隐藏键盘
    protected void hideKeyboard() {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onbackpress = System.currentTimeMillis();
//        if (!TextUtils.isEmpty(currentPagerUUID)) {
//            Log.d(TAG, "onBackPressed : " + this.getClass().getName() + " currentPagerUUID : " + currentPagerUUID);
//            if (eventServiceUtils != null) {
//                //// 退出事件
//                eventServiceUtils.postScheduleExitEvent(currentPagerUUID, this.getClass().getSimpleName());
//            }
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }


    long userInteractionTime = 0;

    @Override
    public void onUserInteraction() {
        userInteractionTime = System.currentTimeMillis();
        super.onUserInteraction();
//        Log.i("appname", "Interaction");
    }

    @Override
    public void onUserLeaveHint() {
        long uiDelta = (System.currentTimeMillis() - userInteractionTime);
        super.onUserLeaveHint();

        Log.i("bThere", "Last User Interaction = " + userInteractionTime);
        if (uiDelta < 100)
            Log.i("appname", "Home Key Pressed");
        else
            Log.i("appname", "We are leaving, but will probably be back shortly!");
    }

    @Override
    public void onClick(View v) {

    }
}
