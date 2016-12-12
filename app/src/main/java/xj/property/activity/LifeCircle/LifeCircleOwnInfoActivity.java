package xj.property.activity.LifeCircle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;
import xj.property.R;
import xj.property.activity.HXBaseActivity.ChatActivity;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseUtils;
import xj.property.utils.ToastUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.LifeCircleBlackListHelper;
import xj.property.utils.other.PreferencesUtil;

public class LifeCircleOwnInfoActivity extends HXBaseActivity {
    private ImageView ivAvatar;

    private TextView tv_name_user, tv_friend_gender, tv_friend_address, tv_friend_signature, tv_person_value, tv_percent, tv_sendmessage;


    private LinearLayout ll_lifecircle, ll_zan, ll_anim, ll_add;


    private RelativeLayout rl_switch_block_notify;
    private ImageView iv_switch_block_notify, iv_switch_unblock_notify;

    private ScrollView sl_scroll;

    Animation in = new AlphaAnimation(0.0f, 1.0f);   //设置透明度变化动画
    Animation stay = new AlphaAnimation(1.0f, 1.0f);   //设置透明度变化动画
    Animation out = new AlphaAnimation(1.0f, 0.0f);   //设置透明度变化动画
    private String emobIdTo;
    private UserInfoDetailBean bean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifecircle_user_info);
        initview();
        initData();
    }

    private void initData() {
       // emobIdTo = getIntent().getStringExtra(Config.INTENT_PARMAS2);
        emobIdTo = "e9c3eefafe5c977a3e04e99d109d378b";
        Log.i("onion", "emobIdTo" + emobIdTo);
        mLdDialog.show();

        NetBaseUtils.extractUserInfo(getmContext(),PreferencesUtil.getCommityId(getmContext()),emobIdTo,new NetBaseUtils.NetRespListener<CommonRespBean<UserInfoDetailBean>>() {
            @Override
            public void successYes(CommonRespBean<UserInfoDetailBean> commonRespBean, Response response) {
                mLdDialog.dismiss();
                bean = commonRespBean.getData();
                initBean();
            }

            @Override
            public void successNo(CommonRespBean<UserInfoDetailBean> commonRespBean, Response response) {
                mLdDialog.dismiss();
                ToastUtils.showToast(LifeCircleOwnInfoActivity.this, "查询对象信息失败:" + commonRespBean.getMessage());
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                showNetErrorToast();
                finish();
            }
        });

    }


    private void initview() {
        initTitle(null, "", null);
        tv_name_user = (TextView) findViewById(R.id.tv_name_user);
        ivAvatar = (ImageView) findViewById(R.id.iv_avatar);

        tv_friend_gender = (TextView) findViewById(R.id.tv_friend_gender);
        tv_friend_address = (TextView) findViewById(R.id.tv_friend_address);
        tv_friend_signature = (TextView) findViewById(R.id.tv_friend_signature);
        ll_lifecircle = (LinearLayout) findViewById(R.id.ll_lifecircle);
        tv_person_value = (TextView) findViewById(R.id.tv_person_value);
        tv_percent = (TextView) findViewById(R.id.tv_percent);
        rl_switch_block_notify = (RelativeLayout) findViewById(R.id.rl_switch_block_notify);
        iv_switch_block_notify = (ImageView) findViewById(R.id.iv_switch_block_notify);
        iv_switch_unblock_notify = (ImageView) findViewById(R.id.iv_switch_unblock_notify);
        tv_sendmessage = (TextView) findViewById(R.id.tv_sendmessage);
        ll_zan = (LinearLayout) findViewById(R.id.ll_zan);
        ll_anim = (LinearLayout) findViewById(R.id.ll_anim);
        sl_scroll = (ScrollView) findViewById(R.id.sl_scroll);
        ll_add = (LinearLayout) findViewById(R.id.ll_add);

        ll_anim.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return sl_scroll.dispatchTouchEvent(event);
            }
        });
        rl_switch_block_notify.setOnClickListener(this);

        ll_lifecircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tv_sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentPush = new Intent();
//                intentPush.putExtra("userId", "tz");
//                intentPush.putExtra("userId", bean.getCommunityId());
                intentPush.putExtra("userId", bean.getEmobId());//tz
                intentPush.putExtra(Config.EXPKey_nickname, bean.getNickname());
                intentPush.putExtra(Config.EXPKey_avatar, bean.getAvatar());
                intentPush.setClass(LifeCircleOwnInfoActivity.this, ChatActivity.class);
                intentPush.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                startActivity(intentPush);
            }
        });

        ll_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_add.clearAnimation();
                ll_add.setVisibility(View.VISIBLE);

                in.setDuration(1000);
                stay.setDuration(1200);
                out.setDuration(800);
                AnimationSet set = new AnimationSet(true);    //创建动画集对象
                set.addAnimation(in);
                set.addAnimation(stay);
                set.addAnimation(out);
                set.setFillAfter(true);                 //停留在最后的位置
                set.setFillEnabled(true);
                ll_add.setAnimation(set);
                set.startNow();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_switch_block_notify:
                if (iv_switch_unblock_notify.getVisibility() == View.VISIBLE) {//加入黑名单
                     mLdDialog.show();
                    iv_switch_unblock_notify.setVisibility(View.INVISIBLE);
                    iv_switch_block_notify.setVisibility(View.VISIBLE);
                    LifeCircleBlackListHelper.addBlack(this, emobIdTo, new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            mLdDialog.dismiss();
                            switch (msg.what) {
                                case Config.ADDBLACKLIST:
                                    List<String> list = PreferencesUtil.getLifeCircleBlackList(LifeCircleOwnInfoActivity.this);
                                    list.add(emobIdTo);
                                    PreferencesUtil.saveLifeCircleBlackList(LifeCircleOwnInfoActivity.this, list);
                                    iv_switch_unblock_notify.setVisibility(View.INVISIBLE);
                                    iv_switch_block_notify.setVisibility(View.VISIBLE);
                                    showToast("已加入生活圈黑名单");
                                    break;
                                case Config.TASKERROR:
                                    showToast("加入生活圈黑名单失败");
                                    break;
                                case Config.NETERROR:
                                    showNetErrorToast();
                                    break;
                            }
                        }
                    });
                } else {//取消黑名单
                    mLdDialog.show();
                    LifeCircleBlackListHelper.removeBlack(this,emobIdTo,new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        mLdDialog.dismiss();
                        switch (msg.what){
                            case Config.REMOVEBLACKLIST:
                                List<String> list= PreferencesUtil.getLifeCircleBlackList(LifeCircleOwnInfoActivity.this);
                                if(list.contains(emobIdTo)) list.remove(emobIdTo);
                                PreferencesUtil.saveLifeCircleBlackList(LifeCircleOwnInfoActivity.this, list);
                                iv_switch_unblock_notify.setVisibility(View.VISIBLE);
                                iv_switch_block_notify.setVisibility(View.INVISIBLE);
                                showToast("移除黑名单");
                                break;
                            case Config.TASKERROR:
                                showToast("移除黑名单失败");
                                break;
                            case Config.NETERROR:
                                showNetErrorToast();
                                break;
                        }
                    }
                });
                }
                break;
            case R.id.tv_right_text:
                break;
        }
    }

    private void initBean() {
        tv_name_user.setText(bean.getNickname() + "");
        ImageLoader.getInstance().displayImage(bean.getAvatar(), ivAvatar, new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .showImageOnFail(R.drawable.head_portrait_personage).showImageForEmptyUri(R.drawable.head_portrait_personage).build());
        tv_friend_gender.setText(getGender(bean.getGender())+"");
        tv_friend_address.setText(bean.getUserFloor()+"");
        tv_friend_signature.setText(bean.getSignature()+"");
        if(PreferencesUtil.getLifeCircleBlackList(this).contains(emobIdTo)){
            iv_switch_block_notify.setVisibility(View.VISIBLE);
            iv_switch_unblock_notify.setVisibility(View.INVISIBLE);
        }
    }

    private String getGender(String gender) {
        System.out.print("new gendr:" + gender);
        String str = null;
        if ("m".equals(gender)) {
            return "男";
        } else if ("f".equals(gender)) {
            return "女";
        } else {
            return "保密";
        }

    }

}
