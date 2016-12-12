package xj.property.activity.user;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.util.EMLog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.soundcloud.android.crop.Crop;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.tags.ActivityAddTagsList;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseUtils;
import xj.property.netbase.RetrofitFactory;
import xj.property.netbasebean.UserInfoRegRespBean;
import xj.property.netbasebean.UserRegReqBean;
import xj.property.service.ImageUploadService;
import xj.property.utils.BitmapHelper;
import xj.property.utils.ImageUploadUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;
import xj.property.widget.CircleImageView;
import xj.property.widget.LoadingDialog;
import xj.property.widget.MyPopWindow;

public class UserHeaderIconActivity extends HXBaseActivity {
    private static final int UPLOAD_COMPLETE = 101;
    private static final int IMAGE_SAVE_COMPLETE = 102;

    private Button btn_username;
    private MyPopWindow popWindow;

    private CircleImageView upload_header_icon_civ;

    private TextView user_nickname;
    private View headicon_llay;
    private Uri photoUri;
    private String photoName;

    public static final int requestCode_to_album = 2;
    public static final int requestCode_to_Photo = 3;


    private int action = -1;
    private Uri uri;

    private int CROP_AlBUM = 102;

    private UserInfoDetailBean userInfoDetailBeanTmp;
    private ProgressDialog pd;

    private String show_notice = "请选择要上传的头像";
    private String authCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_headicon);
        initTitle(null, "上传头像", "");
        userInfoDetailBeanTmp = (UserInfoDetailBean) getIntent().getSerializableExtra("userInfoDetailBean");
        authCode = getIntent().getStringExtra("authcode");

        if (userInfoDetailBeanTmp != null && !TextUtils.isEmpty(authCode)) {
            initView();
        } else {
            showToast("数据异常请返回重新注册");
            finish();
        }
    }

    /**
     * init view
     */
    private void initView() {
        pd = new ProgressDialog(UserHeaderIconActivity.this);
        btn_username = (Button) findViewById(R.id.btn_username);
        btn_username.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_btn_disable));
        btn_username.setClickable(false);
        headicon_llay = findViewById(R.id.headicon_llay);
        user_nickname = (TextView) findViewById(R.id.user_nickname);
        if (!TextUtils.isEmpty(userInfoDetailBeanTmp.getNickname())) {
            user_nickname.setText(userInfoDetailBeanTmp.getNickname());
        }
        upload_header_icon_civ = (xj.property.widget.CircleImageView) findViewById(R.id.upload_header_icon_civ);
        addpic();
        upload_header_icon_civ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popWindow != null) {
                    popWindow.showAtLocation(headicon_llay, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            }
        });
        mLdDialog = new LoadingDialog(getmContext());
        mLdDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                dialog.cancel();
                return false;
            }
        });

        btn_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /// 头像选择裁剪完成
                if (userHeadericonChoicedComplete) {
                    doregistProcess();
                } else {
                    //// 提示用户需要选择头像
                    showToast(show_notice);

                }

            }
        });
    }


    ////// 注册流程：  1， 获取token， 2， 注册信息，3， 上传头像，4， 登陆
    private void doregistProcess() {
        upload_header_icon_civ.setBackgroundDrawable(getResources().getDrawable(R.drawable.updoad_face_common_shade));
        ImageLoader.getInstance().displayImage("file://" + photoName, upload_header_icon_civ);

        NetBaseUtils.extractNewToken(getmContext(),new NetBaseUtils.NetRespListener<CommonRespBean<String>>() {
            @Override
            public void successYes(CommonRespBean<String> commonRespBean, Response response) {
                String token = commonRespBean.getData();

                String key = ImageUploadUtils.generateResKey();

                String avatar = Config.QINIU_BASE_URL + key;

                Log.d("doregistProcess ", " token " + token + " key " + key + " avatar " + avatar);

                Intent intent = new Intent(getmContext(), ImageUploadService.class);
                intent.putExtra("path", photoName);
                intent.putExtra("reskey", key);
                intent.putExtra("token", token);
                startService(intent);

                //// 进行判断是否是游客状态注册.
                boolean touristLogin = PreferencesUtil.getTouristLogin(getmContext());
                if (touristLogin) {
                    userRegistByFix(avatar);
                } else {
                    //// 已经登录过的人进行再次注册.
                    userRegistHasLoginedCall(avatar);
                }
            }

            @Override
            public void successNo(CommonRespBean<String> commonRespBean, Response response) {
                showNetErrorToast();
            }

            @Override
            public void failure(RetrofitError error) {
                showNetErrorToast();
            }
        });


    }


    protected void addpic() {
        popWindow = new MyPopWindow(UserHeaderIconActivity.this, R.layout.paizhao_pop,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            // 拍照
                            case R.id.pop_xi:
                                popWindow.dismiss();
                                takePhoto();
                                break;
                            // 从相册中选
                            case R.id.pop_mu:
                                popWindow.dismiss();
                                Intent intents = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intents.setType("image/*");
                                action = requestCode_to_album;
                                startActivityForResult(intents, requestCode_to_album);
                                break;
                            // 取消
                            case R.id.pop_no:
                                popWindow.dismiss();
                                break;
                        }
                    }
                });
    }


    /**
     * service to judge if a user exist
     */
    interface FixUserInfoService {
        ////api/v3/communities/{小区ID}/users/{游客的环信ID}

        @PUT("/api/v3/communities/{communityId}/users/{emobId}")
        void userRegist(@Body UserRegReqBean userBean, @Path("communityId") int communityId, @Path("emobId") String emobId, Callback<CommonRespBean<UserInfoRegRespBean>> cb);


        /////api/v3/communities/1/users/regist

        @POST("/api/v3/communities/{communityId}/users/regist")
        void userRegistHasLogined(@Body UserRegReqBean userBean, @Path("communityId") int communityId, Callback<CommonRespBean<UserInfoRegRespBean>> cb);

    }


    /**
     * check if user valid
     *
     * @param avatar
     */
    private void userRegistHasLoginedCall(String avatar) {
        if (mLdDialog != null) {
            mLdDialog.show();
        }

        final UserRegReqBean userBean = new UserRegReqBean();
        userBean.setUsername(userInfoDetailBeanTmp.getUsername());
        userBean.setNickname(userInfoDetailBeanTmp.getNickname());
        userBean.setPassword(StrUtils.string2md5(userInfoDetailBeanTmp.getPassword()));
        userBean.setUserFloor(userInfoDetailBeanTmp.getUserFloor());
        userBean.setUserUnit(userInfoDetailBeanTmp.getUserUnit());
        userBean.setRoom(userInfoDetailBeanTmp.getRoom());
        userBean.setEquipmentVersion(UserUtils.getVersion(getmContext()));
        userBean.setAuthCode(authCode);
        userBean.setAvatar(avatar);

        /*
        "equipment": "{设备类型:android,ios,mi}",
    "deviceToken": "{iOS设备推送的token(非iOS设备不需要传递)}",
    "avatar": "{头像}",
         */

        FixUserInfoService isUserExistService = RetrofitFactory.getInstance().create(getmContext(),userBean,FixUserInfoService.class);
        Callback<CommonRespBean<UserInfoRegRespBean>> callback = new Callback<CommonRespBean<UserInfoRegRespBean>>() {
            @Override
            public void success(CommonRespBean<UserInfoRegRespBean> commonPostResultBean, Response response) {
                if (commonPostResultBean != null && "yes".equals(commonPostResultBean.getStatus()) && commonPostResultBean.getData() != null) {
                    Log.i("onion", "userRegistHasLoginedCall success " + commonPostResultBean.getData());
                    ///在登陆退出状态下进行注册成功后的emobid
                    if (userInfoDetailBeanTmp != null) {
                        userInfoDetailBeanTmp.setEmobId(commonPostResultBean.getData().getEmobId());
                    }
                    if (mLdDialog != null) {
                        mLdDialog.dismiss();
                    }
                    login();
                } else {
                    showToast("用户信息注册失败");
                    Log.i("onion", "register error");
                    userHeadericonChoicedComplete = true;
                    /// 设置按钮可以点击// 点击上传头像
                    btn_username.setBackgroundDrawable(getResources().getDrawable(R.drawable.darkgreen_btn_disable));
                    btn_username.setClickable(true);

                    if (mLdDialog != null) {
                        mLdDialog.dismiss();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("onion", "RetrofitError error" + error.toString());
                showNetErrorToast();
                userHeadericonChoicedComplete = true;
                /// 设置按钮可以点击// 点击上传头像
                btn_username.setBackgroundDrawable(getResources().getDrawable(R.drawable.darkgreen_btn_disable));
                btn_username.setClickable(true);

                if (mLdDialog != null) {
                    mLdDialog.dismiss();
                }
            }
        };

        isUserExistService.userRegistHasLogined(userBean, PreferencesUtil.getCommityId(getmContext()), callback);
    }


    /**
     * check if user valid
     *
     * @param avatar
     */
    private void userRegistByFix(String avatar) {
        if (mLdDialog != null) {
            mLdDialog.show();
        }
        final UserRegReqBean userBean = new UserRegReqBean();
        userBean.setUsername(userInfoDetailBeanTmp.getUsername());
        userBean.setNickname(userInfoDetailBeanTmp.getNickname());
        userBean.setPassword(StrUtils.string2md5(userInfoDetailBeanTmp.getPassword()));
        userBean.setUserFloor(userInfoDetailBeanTmp.getUserFloor());
        userBean.setUserUnit(userInfoDetailBeanTmp.getUserUnit());
        userBean.setRoom(userInfoDetailBeanTmp.getRoom());
        userBean.setAvatar(avatar);
        userBean.setAuthCode(authCode);
        userBean.setEquipmentVersion(UserUtils.getVersion(getmContext()));

        /**
         {
         "authCode": "{验证码}",
         "username": "{用户手机号}",
         "nickname": "{昵称}",
         "avatar": "{头像}",
         "userFloor": "{楼号}",
         "userUnit": "{单元号}",
         "room": "{房间号}",
         "password": "{加密后的密码}"
         }
         *
         */
        FixUserInfoService isUserExistService = RetrofitFactory.getInstance().create(getmContext(),userBean,FixUserInfoService.class);
        Callback<CommonRespBean<UserInfoRegRespBean>> callback = new Callback<CommonRespBean<UserInfoRegRespBean>>() {
            @Override
            public void success(CommonRespBean<UserInfoRegRespBean> commonPostResultBean, Response response) {
                if (commonPostResultBean != null && "yes".equals(commonPostResultBean.getStatus())) {
                    Log.i("onion", "fix success");
                    UserInfoRegRespBean data = commonPostResultBean.getData();

                    Log.i("onion", "fix success data--->>" + data);
                    if (mLdDialog != null) {
                        mLdDialog.dismiss();
                    }
                    login();
                } else {
                    showToast("用户信息注册失败");
                    Log.i("onion", "register error");
                    userHeadericonChoicedComplete = true;
                    /// 设置按钮可以点击// 点击上传头像
                    btn_username.setBackgroundDrawable(getResources().getDrawable(R.drawable.darkgreen_btn_disable));
                    btn_username.setClickable(true);

                    if (mLdDialog != null) {
                        mLdDialog.dismiss();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("onion", "RetrofitError error" + error.toString());
                showNetErrorToast();
                userHeadericonChoicedComplete = true;
                /// 设置按钮可以点击// 点击上传头像
                btn_username.setBackgroundDrawable(getResources().getDrawable(R.drawable.darkgreen_btn_disable));
                btn_username.setClickable(true);

                if (mLdDialog != null) {
                    mLdDialog.dismiss();
                }
            }
        };

        isUserExistService.userRegist(userBean, PreferencesUtil.getCommityId(getmContext()), userInfoDetailBeanTmp.getEmobId(), callback);
    }

    private void login() {
        final UserInfoDetailBean userInfoDetailBean = userInfoDetailBeanTmp;
        if (pd != null) {
            pd.setMessage("正在登录帮帮..");
            pd.show();
        }
        UserUtils.loginUser(UserHeaderIconActivity.this,
                userInfoDetailBean.getUsername(),
                userInfoDetailBeanTmp.getPassword(), new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        Log.i("onion", "handler running......");
                        switch (msg.what) {
                            case Config.LoginUserComplete:
//                                    PreferencesUtil.saveLogin(RoomSelectActivity.this, userInfoDetailBean);
                                PreferencesUtil.saveFirstLogin(getmContext());

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(userInfoDetailBean.getNickname());
                                        if (!updatenick) {
                                            EMLog.e("LoginActivity", "update current user nick fail");
                                        }
                                    }
                                }).start();

                                if (pd != null && pd.isShowing()) {
                                    pd.dismiss();
                                }
                                /// 跳转至添加标签页
                                Intent intent = new Intent(getmContext(), ActivityAddTagsList.class);
                                intent.putExtra("userInfoDetailBean", userInfoDetailBean);
                                startActivity(intent);
                                finish();

                                break;
                            case Config.LoginUserFailure:
                                if (pd != null && pd.isShowing() && !UserHeaderIconActivity.this.isFinishing())
                                    pd.dismiss();

                                break;
                            case Config.LoginUserSuccess:
                                pd.setMessage("正在登录帮帮..");
                                break;
                            case Config.LogoutTourist:
                                pd.setMessage("正在登录帮帮..");
                                break;
                            case Config.LoginUserUPDATENATIVE:
                                pd.setMessage("正在加载群，联系人信息..");
                                break;

                        }
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pd != null) {
            pd.dismiss();
        }
    }

    private boolean userHeadericonChoicedComplete = false;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case IMAGE_SAVE_COMPLETE:
                    Log.d("IMAGE_SAVE_COMPLETE ", "is true");
                    userHeadericonChoicedComplete = true;
                    /// 设置按钮可以点击// 点击上传头像
                    btn_username.setBackgroundDrawable(getResources().getDrawable(R.drawable.darkgreen_btn_disable));
                    btn_username.setClickable(true);
                    break;

                case UPLOAD_COMPLETE:

                    String token = (String) msg.obj;
                    UploadManager um = new UploadManager();
                    upload_header_icon_civ.setBackgroundDrawable(getResources().getDrawable(R.drawable.updoad_face_common_shade));

                    if (action == requestCode_to_album)
                        ImageLoader.getInstance().displayImage("file://" + photoName, upload_header_icon_civ);
                    else
                        ImageLoader.getInstance().displayImage("file://" + photoName, upload_header_icon_civ);

                    um.put(photoName, null, token, new UpCompletionHandler() {
                        @Override
                        public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
                            if (mLdDialog != null) {
                                mLdDialog.dismiss();
                            }
                            try {
                                String headerIconUrl = jsonObject.optString("key");
                                Toast.makeText(getmContext(), "上传头像成功", Toast.LENGTH_SHORT).show();
//                                PreferencesUtil.saveLogin(getmContext(), headerIconUrl);
                                Log.d("UPLOAD_COMPLETE  ", "headerIconUrl " + headerIconUrl);
                                photoName = "";
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getmContext(), "上传头像失败,请重试", Toast.LENGTH_SHORT).show();
                                Log.d("IMAGE_SAVE_COMPLETE ", "is true");
                                userHeadericonChoicedComplete = true;
                                /// 设置按钮可以点击// 点击上传头像
                                btn_username.setBackgroundDrawable(getResources().getDrawable(R.drawable.darkgreen_btn_disable));
                                btn_username.setClickable(true);
                                return;
                            }
                            //// 登陆 跳转至挑选标签页面
                            login();

                        }
                    }, null);
            }

        }
    };


    ////拍照
    private void takePhoto() {

        //先验证手机是否有sdcard
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {

            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            openCameraIntent.putExtra("return-data", true);

            photoName = getFileName();

            ContentValues values = new ContentValues();

            values.put(MediaStore.Images.Media.TITLE, photoName);

            photoUri = getmContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

//            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photoName)));

            action = requestCode_to_Photo;

            startActivityForResult(openCameraIntent, requestCode_to_Photo);

        }
    }

    private String getFileName() {
        String fileName = String.valueOf(System.currentTimeMillis());
        String path;
        File sdcardPath = Environment.getExternalStorageDirectory();
        path = sdcardPath.getAbsolutePath() + File.separator + Config.BASE_GROUP_CACHE + File.separator + "image";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path + File.separator + fileName + ".jpg";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_CANCELED) return;


        if (requestCode == requestCode_to_album) {
            if (data != null) {
                uri = data.getData();
                beginCrop2(uri);

            }
        }

        if (requestCode == requestCode_to_Photo) {

            if (data != null && data.getData() != null) {
                uri = data.getData();
            }
            // 一些机型无法从getData中获取uri，则需手动指定拍照后存储照片的Uri
            if (uri == null) {

                if (photoUri != null) {
                    uri = photoUri;
                }
            }

            beginCrop(photoUri);
//            beginCrop(Uri.fromFile(new File(photoName)));

        }

        if (requestCode == Crop.REQUEST_CROP) {
            show_notice = "正在处理头像";
            handleCrop(resultCode, data);
        }

        if (requestCode == CROP_AlBUM) {
            show_notice = "正在处理头像";
            handleCrop2(resultCode, data);
        }

    }


    private void beginCrop(Uri source) {

        Uri destination = Uri.fromFile(new File(getmContext().getCacheDir(), "cropped"));

        Crop.of(source, destination).asSquare().start(UserHeaderIconActivity.this, Crop.REQUEST_CROP);

    }

    private void beginCrop2(Uri source) {

        Uri destination = Uri.fromFile(new File(getmContext().getCacheDir(), "cropped"));

        Crop.of(source, destination).asSquare().start(UserHeaderIconActivity.this, CROP_AlBUM);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
            Log.i("debbug getOutput  ", "" + Crop.getOutput(result));

            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getmContext().getContentResolver(), Crop.getOutput(result));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoName != null) {
                bmp = BitmapHelper.rotateBitmapByDegree(bmp, BitmapHelper.getBitmapDegree(photoName));
            }
//            Log.i("debbug",""+getBitmapSize(bmp));

            upload_header_icon_civ.setImageBitmap(bmp);

            final Bitmap finalBmp = bmp;

            if (TextUtils.isEmpty(photoName)) photoName = getFileName();

            new Thread(new Runnable() {
                public void run() {

                    try {

//                        FileUtils.saveBitmapForTakePhoto(finalBmp, photoName);
                        BitmapHelper.saveMyBitmap(photoName, finalBmp);

//                    try {
//
//                        File f = new File(photoName);
//
//                        if (f.exists()) {
//                            f.delete();
//                        }else{
//                            if(!f.getParentFile().exists()){
//                                f.getParentFile().mkdirs();
//                            }
//                        }
//
////                        FileOutputStream out = new FileOutputStream(f);
//                        OutputStream  out = new FileOutputStream(f);
//                        finalBmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
//                        out.flush();
//                        out.close();
//
//                        Log.e("saveBitmapForTakePhoto", "已经保存" +f.getAbsolutePath() );
//                    }catch (IOException e) {
//                        e.printStackTrace();
//                        Log.e("saveBitmapForTakePhoto", "IOException" + e.getMessage());
//
//                        savePhotoError.sendEmptyMessage(1);
//                    }
                        handler.sendEmptyMessage(IMAGE_SAVE_COMPLETE);


                    } catch (IOException e) {

                        e.printStackTrace();

                        savePhotoError.sendEmptyMessage(1);
                    }
                }
            }).start();

//            resultView.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getmContext(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    /**
     * 处理pickfrom album 裁剪结果
     *
     * @param resultCode
     * @param result
     */
    private void handleCrop2(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {

            Log.i("debbug getOutput album  ", "" + Crop.getOutput(result));

            uri = Crop.getOutput(result);

            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getmContext().getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoName != null) {
                bmp = BitmapHelper.rotateBitmapByDegree(bmp, BitmapHelper.getBitmapDegree(photoName));
            }

            final Bitmap finalBmp = bmp;

            handler.post(new Runnable() {
                @Override
                public void run() {
                    upload_header_icon_civ.setImageBitmap(finalBmp);
                }
            });

            if (TextUtils.isEmpty(photoName)) photoName = getFileName();

            new Thread(new Runnable() {
                public void run() {

                    try {
//                        FileUtils.saveBitmapForTakePhoto(finalBmp, photoName);
                        BitmapHelper.saveMyBitmap(photoName, finalBmp);

                        handler.sendEmptyMessage(IMAGE_SAVE_COMPLETE);

                    } catch (IOException e) {

                        e.printStackTrace();
                        savePhotoError.sendEmptyMessage(1);
                    }
                }
            }).start();

//            resultView.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getmContext(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    Handler savePhotoError = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(getmContext(), "保存图片出现异常，请重试！", Toast.LENGTH_SHORT).show();
            photoName = null;
//            iv_open_photo.setImageResource(R.drawable.photo_pictureadd);

//            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.default_avatar, upload_header_icon_civ);

//            ImageLoader.getInstance().displayImage("file://" + photoName, upload_header_icon_civ, UserUtils.bangzhu_election_me_options);

        }
    };

    @Override
    public void onClick(View v) {

    }

}
