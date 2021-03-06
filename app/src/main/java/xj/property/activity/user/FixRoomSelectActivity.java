package xj.property.activity.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.easemob.util.HanziToPinyin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.PUT;
import retrofit.http.Path;
import xj.property.Constant;
import xj.property.R;
import xj.property.XjApplication;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.Floor;
import xj.property.beans.UpdateUserRoomRequest;
import xj.property.beans.UserInfoDetailBean;
import xj.property.beans.XJUserInfoBean;
import xj.property.domain.User;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseUtils;
import xj.property.netbasebean.ModifyUserAddressReqBean;
import xj.property.ums.common.CommonUtil;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.HomeOwnerUtil;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.wheelview.ArrayWheelAdapter;
import xj.property.widget.wheelview.OnWheelChangedListener;
import xj.property.widget.wheelview.WheelView;

public class FixRoomSelectActivity extends HXBaseActivity {

    private Button btn_room_select;

    private String[] item_floors;
    private String[] item_unit;
    private String[] item_data_rooms;
    private boolean progressShow;
    private ProgressDialog pd;
    List<Floor> floors = new ArrayList<>();


    private EditText select_floor_et, select_unit_et, select_room_et;


    private UserInfoDetailBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_select);
        initTitle(null, "修改真实住址", "");
        bean = PreferencesUtil.getLoginInfo(this);
        initView();
        initData();
        pd = new ProgressDialog(FixRoomSelectActivity.this);


//        mLdDialog.show();
//        HomeOwnerUtil.getHomeList(new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                mLdDialog.dismiss();
//                if (msg.what == Config.TASKERROR) {
//                    showToast("获取小区结构失败");
//                } else {
//                    floors = (List<Floor>) msg.obj;
//                    item_floors = HomeOwnerUtil.getFloor(floors);
//                    item_unit = HomeOwnerUtil.getuserUnit(floors.get(0));
//                    item_data_rooms = HomeOwnerUtil.getuserRoom(floors.get(0).getList().get(0));
//                    initWheelCity(null, null, null);
//                }
//            }
//        });


    }


//
//    private String getRoom(UserInfoDetailBean bean) {
//        if (bean != null && bean.getUserFloor() != null && bean.getUserUnit() != null && bean.getRoom() != null) {
//            return bean.getUserFloor() + bean.getUserUnit() + bean.getRoom();
//        } else {
//            return null;
//        }
//
//    }


    private void initData() {

        if (bean != null) {

            if (select_floor_et != null) {
                select_floor_et.setText(bean.getUserFloor());
                select_floor_et.setSelection(select_floor_et.getText().length());
            }

            if (select_unit_et != null) {
                select_unit_et.setText(bean.getUserUnit());
                select_unit_et.setSelection(select_unit_et.getText().length());
            }

            if (select_room_et != null) {
                select_room_et.setText(bean.getRoom());
                select_room_et.setSelection(select_room_et.getText().length());
            }
        } else {
            Log.d("initData", "UserInfoDetailBean bean is null");
        }

    }

    WheelView mWheelViewPro;
    WheelView mWheelViewCity;
    WheelView mWheelViewTwon;

    private void initWheelCity(String proindex,
                               String cityindex, String townindex) {
        mWheelViewPro.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
//                int proCurrentPosition = v.getCurrentItem();

                item_unit = HomeOwnerUtil.getuserUnit(floors.get(newValue));
                mWheelViewCity.setAdapter(new ArrayWheelAdapter<String>(
                        item_unit));
                mWheelViewCity.setCurrentItem(item_unit.length / 2);
                item_data_rooms = HomeOwnerUtil.getuserRoom(floors.get(newValue).getList().get(0));
                mWheelViewTwon.setAdapter(new ArrayWheelAdapter<String>(
                        item_data_rooms));
                mWheelViewTwon.setCurrentItem(item_data_rooms.length / 2);
            }
        });
        mWheelViewCity.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int proCurrentPosition = mWheelViewPro.getCurrentItem();
                item_data_rooms = HomeOwnerUtil.getuserRoom(floors.get(proCurrentPosition).getList().get(newValue));
                mWheelViewTwon.setAdapter(new ArrayWheelAdapter<String>(
                        item_data_rooms));
                mWheelViewTwon.setCurrentItem(item_data_rooms.length / 2);
            }
        });

        // province
        // 初始化的位置--------------------------------------------------------------
        int proPosition = 0;
        if (proindex != null) {
            proPosition = getArrayPosition(item_floors, proindex);
        }
        // city
        // 初始化的位置--------------------------------------------------------------
        int cityPosition = 0;
        if (cityindex != null) {
            cityPosition = getArrayPosition(item_unit, cityindex);
        }


        // town
        // 初始化的位置--------------------------------------------------------------
        int townPosition = 0;
        if (townindex != null) {
            townPosition = getArrayPosition(item_data_rooms, townindex);
        }

        ArrayWheelAdapter<String> proAdapter = new ArrayWheelAdapter<String>(
                item_floors);
//		proAdapter.setItemTextResource(Typeface.BOLD);
        mWheelViewPro.setSoundEffectsEnabled(true);
        mWheelViewPro.setCurrentItem(proPosition); // 初始化province的位置----------------------------
        mWheelViewPro.setAdapter(proAdapter);
        mWheelViewPro.setCyclic(false);
        ArrayWheelAdapter<String> cityAdapter = new ArrayWheelAdapter<String>(
                item_unit);
        mWheelViewCity.setAdapter(cityAdapter);

        mWheelViewCity.setCurrentItem(cityPosition); // 初始化city的位置-------------------------------------------

        ArrayWheelAdapter<String> twonAdapter = new ArrayWheelAdapter<String>(
                item_data_rooms);
        mWheelViewTwon.setAdapter(twonAdapter);

        mWheelViewTwon.setCurrentItem(townPosition); // 初始化town的位置------------------------------------------

    }

    private int getArrayPosition(String[] data, String dataIndex) {

        for (int i = 0; i < data.length; i++) {

            if (data[i].equals(dataIndex)) {

                return i;
            }
        }

        return 0;
    }

    private void initView() {
//        findViewById(R.id.ll_btns).setVisibility(View.GONE);
//        mWheelViewPro = (WheelView) findViewById(R.id.provice);
//        mWheelViewCity = (WheelView) findViewById(R.id.city);
//        mWheelViewTwon = (WheelView) findViewById(R.id.area);

        findViewById(R.id.iv_back).setVisibility(View.VISIBLE);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_room_select = (Button) findViewById(R.id.btn_room_select);
        btn_room_select.setOnClickListener(this);
        btn_room_select.setText("保存");
        select_floor_et = (EditText) this.findViewById(R.id.select_floor_et);
        select_unit_et = (EditText) this.findViewById(R.id.select_unit_et);
        select_room_et = (EditText) this.findViewById(R.id.select_room_et);

    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            showToast("请保存地址");
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    /**
     * 修改用户地址信息
     *
     * @param item_room
     * @param item_floors
     * @param item_unit
     */
    private void fixUserInfo(final String item_room, final String item_floors, final String item_unit) {
        mLdDialog.show();
        ModifyUserAddressReqBean modifyUserAddressReqBean = new ModifyUserAddressReqBean();
        modifyUserAddressReqBean.setRoom(item_room);
        modifyUserAddressReqBean.setUserUnit(item_unit);
        modifyUserAddressReqBean.setUserFloor(item_floors);
        NetBaseUtils.modifyUserAddress(getmContext(),
                PreferencesUtil.getCommityId(getmContext()),
                PreferencesUtil.getLoginInfo(getmContext()).getEmobId(),
                modifyUserAddressReqBean, new NetBaseUtils.NetRespListener<CommonRespBean<String>>() {

                    @Override
                    public void successYes(CommonRespBean<String> commonRespBean, Response response) {
                        mLdDialog.dismiss();
                        showToast("修改成功");
                        PreferencesUtil.saveUserAddress(getmContext(), item_floors, item_unit, item_room);
                        finish();
                    }

                    @Override
                    public void successNo(CommonRespBean<String> commonRespBean, Response response) {
                        showToast("修改失败:" + commonRespBean.getMessage());
                        mLdDialog.dismiss();
                    }
                    @Override
                    public void failure(RetrofitError error) {
                        showToast("修改失败，请稍后再试");
                        mLdDialog.dismiss();
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_room_select:
            case R.id.tv_right_text:
                String item_floor = select_floor_et.getText().toString().trim();
                String item_unit = select_unit_et.getText().toString().trim();
                String item_room = select_room_et.getText().toString().trim();

                if (CommonUtil.isNetworkAvailable(getmContext())) {
                    fixUserInfo(item_room, item_floor, item_unit);
                } else {
                    showNetErrorToast();
                }

//                fixUserInfo(item_floor, item_unit, item_room);
                break;

//            case R.id.tv_left_text:
//                return;
        }

    }

    /**
     * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
     *
     * @param username
     * @param user
     */
    protected void setUserHearder(String username, User user) {
        String headerName = null;
        if (!TextUtils.isEmpty(user.getNick())) {
            headerName = user.getNick();
        } else {
            headerName = user.getUsername();
        }
        if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
            user.setHeader("");
        } else if (Character.isDigit(headerName.charAt(0))) {
            user.setHeader("#");
        } else {
            user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase());
            char header = user.getHeader().toLowerCase().charAt(0);
            if (header < 'a' || header > 'z') {
                user.setHeader("#");
            }
        }
    }


    //获取楼信息

//    interface FixUserPasswordService {
//        ///api/v1/communities/{communityId}/homeOwner
//        @GET("/api/v1/communities/{communityId}/homeOwner")
//        void fixUserPassword(@Path("communityId") int communityId, Callback<FloorResult> cb);
//    }
//
//    private void fixUserPassword() {
//        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setEndpoint(Config.NET_BASE2)
//                .build();
//        FixUserPasswordService isUserExistService = restAdapter.create(FixUserPasswordService.class);
//        final UserBean userBean = new UserBean();
//        userBean.setPassword(StrUtils.string2md5(getIntent().getStringExtra(Config.LoginUserPwd)));
//        Callback<FloorResult> callback = new Callback<FloorResult>() {
//            @Override
//            public void success(FloorResult floorResult, Response response) {
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                logger.info("retrofit error is :" + error);
//            }
//        };
//        isUserExistService.fixUserPassword(2, callback);
//    }


    //重新获取用户信息

    interface GetUserService {
        @GET("/api/v1/communities/{communityId}/users/{emobId}")
        void getuser(@Path("communityId") int communityId, @Path("emobId") String emobId, Callback<XJUserInfoBean> cb);
    }

    private void getuser(String emobId) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Config.NET_BASE2)
                .build();
        GetUserService isUserExistService = restAdapter.create(GetUserService.class);
        Callback<XJUserInfoBean> callback = new Callback<XJUserInfoBean>() {
            @Override
            public void success(XJUserInfoBean bean, Response response) {
                PreferencesUtil.Logout(FixRoomSelectActivity.this);
                PreferencesUtil.saveLogin(FixRoomSelectActivity.this, bean.getInfo());
                Map<String, User> map = new HashMap<String, User>();
                XjApplication.getInstance().setContactList(map);
            }

            @Override
            public void failure(RetrofitError error) {
            }
        };
        isUserExistService.getuser(PreferencesUtil.getCommityId(FixRoomSelectActivity.this), emobId, callback);
    }


    private void storeLocation() {
        SharedPreferences sharedPreferences = getSharedPreferences("xj", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("userFloor", userFloor);
//        editor.putString("userUnit ", userUnit);
//        editor.putString("room ", room);
        editor.commit();//提交修改
    }
}