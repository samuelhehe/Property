package xj.property.activity.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.PUT;
import retrofit.http.Path;
import xj.property.R;
import xj.property.XjApplication;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.MainActivity;
import xj.property.beans.UpdateUserGenderRequest;
import xj.property.beans.UserInfoDetailBean;
import xj.property.beans.XJUserInfoBean;
import xj.property.event.ExitEvent;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.XJPushManager;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

/**
 * Created by Administrator on 2015/3/25.
 */
public class UserSettingActivity extends HXBaseActivity {
    UserInfoDetailBean bean;
    private LinearLayout ll_top;
    private LinearLayout contentL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersetting);
        bean = PreferencesUtil.getLoginInfo(this);
        Log.i("onion", bean.getEmobId());
        initTitle(null, "个人设置", "");
        initView();
    }

    private void initView() {
        ll_top = (LinearLayout) findViewById(R.id.ll_top);
        contentL = (LinearLayout) findViewById(R.id.ll_mine);
        updateUI(bean);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PreferencesUtil.getLogin(UserSettingActivity.this)) {
            bean = PreferencesUtil.getLoginInfo(UserSettingActivity.this);
        } else {
            bean = null;
        }
        updateUI(bean);
    }

    private void updateUI(final UserInfoDetailBean bean) {
        contentL.removeAllViews();
        if (bean != null) {

            contentL.addView(addItem("昵称", bean.getNickname(), true, true, FixUserInfoActivity.class, 0, FixUserInfoActivity.UserKey_NikeName));

            View gender = addItem("性别", getGender(bean.getGender()), true, true, null, 0, -1);
            gender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showGenderDialog(bean);
                }
            });
            contentL.addView(gender);
            contentL.addView(addItem("地址", getRoom(bean), true, true, FixRoomSelectActivity.class, 0, FixUserInfoActivity.UserKey_Address));

            contentL.addView(addItem("手机", bean.getUsername(), true, true, null, 25, -1));
            contentL.addView(addItem("密码", "", true, false, FixUserPasswordActivity.class, 0, 5));

            contentL.addView(addItem("通讯录黑名单", "", true, true, BlackListViewActivity.class, 25, 5));
            contentL.addView(addItem("不看他的生活圈", "", true, false, BlackListLifeCircleActivity.class, 0, 5));

            View v = addItem("退出登录", "", true, false, null, 25, -1);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLogoutDialog(UserSettingActivity.this);
                }
            });
            contentL.addView(v);
        }
        // changeUI();
    }


    private LinearLayout addItem(String title, String content, boolean bMore, boolean bline, final Class<?> class1, int topMargin, final int fixNum) {
        LinearLayout ll = (LinearLayout) View.inflate(UserSettingActivity.this,
                R.layout.item_mine_set, null);
        TextView tvTitle = (TextView) ll.findViewById(R.id.tv_mine_title);
        tvTitle.setText(title);
        TextView tvContent = (TextView) ll.findViewById(R.id.tv_mine_content);
        tvContent.setText(content);
        if (!bMore) {
            ll.findViewById(R.id.iv_more).setVisibility(View.GONE);
        }
        if (!bline) {
            ll.findViewById(R.id.mine_line).setVisibility(View.GONE);
        }
        if (class1 != null)
            ll.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UserSettingActivity.this, class1);
                    if (fixNum != -1) {
                        intent.putExtra(Config.UPDATE_USERINFO, fixNum);
                    }
                    startActivity(intent);
                }
            });
        if (topMargin > 0) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = topMargin;
            ll.setLayoutParams(params);
        }
        return ll;
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

    private String getRoom(UserInfoDetailBean bean) {
        if (bean != null && bean.getUserFloor() != null && bean.getUserUnit() != null && bean.getRoom() != null) {
//            return bean.getUserFloor() + bean.getUserUnit() + bean.getRoom();

            StringBuilder roomSb = new StringBuilder(PreferencesUtil.getCommityName(this));

            if (!TextUtils.isEmpty(bean.getUserFloor())) {
                roomSb.append("-").append(bean.getUserFloor());
            } else {
                return "请完善地址";
            }
            if (!TextUtils.isEmpty(bean.getUserUnit())) {
                roomSb.append("-").append(bean.getUserUnit());
            } else {
                return "请完善地址";
            }
            if (!TextUtils.isEmpty(bean.getRoom())) {
                roomSb.append("-").append(bean.getRoom());
            } else {
                return "请完善地址";
            }
            return roomSb.toString();
        } else {
            return "请完善地址";
        }
    }


    /**
     * 性别dialog
     */
    private AlertDialog dialog;

    private void showGenderDialog(UserInfoDetailBean beans) {
        final UserInfoDetailBean userInfoDetailBean = beans;

        AlertDialog.Builder builder = new AlertDialog.Builder(UserSettingActivity.this);
        View view = View.inflate(UserSettingActivity.this, R.layout.dialog_fix_user_gender, null);
        RelativeLayout rl_fix_gender_male = (RelativeLayout) view.findViewById(R.id.rl_fix_gender_man);
        RelativeLayout rl_fix_gender_female = (RelativeLayout) view.findViewById(R.id.rl_fix_gender_woman);
        final ImageView iv_female = (ImageView) view.findViewById(R.id.iv_woman);
        final ImageView iv_male = (ImageView) view.findViewById(R.id.iv_man);
        // iv_female.setVisibility(View.GONE);
        // iv_male.setVisibility(View.GONE);
        if (userInfoDetailBean.getGender().equals("m")) {
            iv_female.setVisibility(View.GONE);
            iv_male.setVisibility(View.VISIBLE);
        } else if (userInfoDetailBean.getGender().equals("f")) {
            iv_female.setVisibility(View.VISIBLE);
            iv_male.setVisibility(View.GONE);
        }

        userInfoDetailBean.setGender("s");
        rl_fix_gender_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfoDetailBean.setGender("m");
                userRegistByFix("m");
                PreferencesUtil.saveLogin(UserSettingActivity.this, userInfoDetailBean);
                bean = PreferencesUtil.getLoginInfo(UserSettingActivity.this);
                updateUI(bean);
                //onResume();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                        } catch (Exception e) {
                        }
                    }
                }).start();
                dialog.dismiss();
            }
        });

        rl_fix_gender_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfoDetailBean.setGender("f");
                userRegistByFix("f");
                PreferencesUtil.saveLogin(UserSettingActivity.this, userInfoDetailBean);
                bean = PreferencesUtil.getLoginInfo(UserSettingActivity.this);
                updateUI(bean);
                //onResume();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                        } catch (Exception e) {
                        }
                    }
                }).start();
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                bean.setGender("s");
            }
        });
        dialog.show();
    }


    private AlertDialog logout_dialog;

    public void showLogoutDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_userlogout, null);
        final TextView cancle = (TextView) view.findViewById(R.id.cancle);
        final TextView submit = (TextView) view.findViewById(R.id.submit);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout_dialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "您已退出", Toast.LENGTH_LONG).show();

                PreferencesUtil.clearBlackList(UserSettingActivity.this);
                logout();
                updateUI(null);

                logout_dialog.dismiss();
            }
        });
        logout_dialog = builder.create();
        logout_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        logout_dialog.setView(view, 0, 0, 0, 0);
        logout_dialog.setCanceledOnTouchOutside(true);
        logout_dialog.show();
    }


    /**
     * service to judge if a user exist
     */
    interface FixUserInfoService {
//        @PUT("/api/v1/communities/{communityId}/users/update/{userName}")
//        void updateUserInfo(@Header("signature") String signature, @Body UpdateUserGenderRequest request, @Path("communityId") long communityId, @Path("userName") String userName, Callback<XJUserInfoBean> cb);
//        @PUT("/api/v1/communities/{communityId}/users/update/{userName}")

        //        /api/v3/communities/{小区ID}/users/{emobId}/gender
        @PUT("/api/v3/communities/{communityId}/users/{emobId}/gender")
        void updateUserInfo(@Body UpdateUserGenderRequest request, @Path("communityId") long communityId, @Path("emobId") String emobId, Callback<CommonRespBean<String>> cb);
    }

    /**
     * 修改用户的性别
     * "gender": "{性别：f->女,m->男,s->保密}"
     */
    private void userRegistByFix(String gender) {
        final UpdateUserGenderRequest request = new UpdateUserGenderRequest();
        request.setGender(gender);
        FixUserInfoService service = RetrofitFactory.getInstance().create(getmContext(), request, FixUserInfoService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> commonPostResultBean, Response response) {
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        };
        service.updateUserInfo(request, bean.getCommunityId(), bean.getEmobId(), callback);
    }


    private void logout() {
        final ProgressDialog pd = new ProgressDialog(UserSettingActivity.this);
        pd.setMessage("正在退出登陆..");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        XjApplication.getInstance().logout(new EMCallBack() {

            @Override
            public void onSuccess() {
                UserSettingActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();

                        if (xjpushManager == null) {
                            xjpushManager = new XJPushManager(getmContext());
                        }
                        xjpushManager.unregisterLoginedPushService();
                        EventBus.getDefault().post(new ExitEvent());
                        PreferencesUtil.Logout(UserSettingActivity.this);
                        startActivity(new Intent(UserSettingActivity.this, MainActivity.class));
                        finish();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.i("onion", "登出错误" + message);
            }
        });
    }
}
