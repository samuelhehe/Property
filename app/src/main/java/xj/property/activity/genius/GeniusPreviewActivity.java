package xj.property.activity.genius;

import android.app.Activity;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.soundcloud.android.crop.Crop;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.GeniusRequest;
import xj.property.beans.GeniusResult;
import xj.property.beans.QuaryToken;
import xj.property.beans.Token;
import xj.property.beans.UserInfoDetailBean;
import xj.property.event.GeniusEvent;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.BitmapHelper;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.CircleImageView;
import xj.property.widget.LoadingDialog;
import xj.property.widget.MyPopWindow;

/**
 * 作者：asia on 2015/12/17 10:51
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能： 牛人预览
 */
public class GeniusPreviewActivity extends HXBaseActivity {

    private CircleImageView mIv_choose_image;
    private TextView mTv_name;
    private TextView mTv_message;
    private TextView mTv_submit_genius;
    private ScrollView mSv_all;

    private final int PROPERTYPAY= 88;
    private final int CLOSEPROPERTY = 66;

    private MyPopWindow popWindow;
    private String photoName;
    private Uri uri = null;
    private Uri photoUri;
    int action = 1;
    private UserInfoDetailBean bean;
    public static final int requestCode_to_Photo = 1;
    public static final int requestCode_to_album = 2;
    public static final int requestCode_to_upload = 4;
    public static final int requestCode_to_imagePager = 3;
    public static final int UPLOAD_COMPLETE = 750;
    public static final int REVITION_COMPLETE = 751;

    private final int CROP_AlBUM = 100;

    private String input;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case REVITION_COMPLETE:

                    mLdDialog = new LoadingDialog(GeniusPreviewActivity.this);
                    mLdDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        public boolean onKey(DialogInterface dialog, int keyCode,
                                             KeyEvent event) {
                            dialog.cancel();
                            return false;
                        }
                    });
                    getToken();
                    mLdDialog.show();
                    break;

                case UPLOAD_COMPLETE:

                    String token = (String) msg.obj;
                    UploadManager um = new UploadManager();
                    if (action == requestCode_to_album)
                        ImageLoader.getInstance().displayImage("file://" + photoName, mIv_choose_image);
                    else
                        ImageLoader.getInstance().displayImage("file://" + photoName, mIv_choose_image);

                    um.put(photoName, null, token, new UpCompletionHandler() {
                        @Override
                        public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
                            mLdDialog.dismiss();
                            try {
                                bean.setAvatar(jsonObject.getString("key"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(GeniusPreviewActivity.this, "修改头像成功", Toast.LENGTH_SHORT).show();
                            PreferencesUtil.saveLogin(GeniusPreviewActivity.this, bean);
                            photoName = "";
                        }
                    }, null);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genius_preview);
        bean = PreferencesUtil.getLoginInfo(GeniusPreviewActivity.this);
        input=getIntent().getStringExtra("input");
        initView();
        initDate();
        initListenner();
    }

    private void initListenner() {
        mIv_choose_image.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        mTv_submit_genius.setOnClickListener(this);
    }

    private void initDate() {
        ((TextView)findViewById(R.id.tv_title)).setText("我是牛人预览");
        ((TextView)findViewById(R.id.tv_title)).setTextColor(0xff2FCC71);
        if(bean.getAvatar()!=null){
            ImageLoader.getInstance().displayImage(bean.getAvatar(),mIv_choose_image,options);
        }
        mTv_message.setText(input);
        mTv_name.setText(bean.getNickname());
        mSv_all.smoothScrollTo(0, 0);
    }

    private void initView() {
        mIv_choose_image = (CircleImageView) findViewById(R.id.iv_choose_image);
        mTv_name = (TextView) findViewById(R.id.tv_name);
        mTv_message = (TextView) findViewById(R.id.tv_message);
        mTv_submit_genius = (TextView) findViewById(R.id.tv_submit_genius);
        mSv_all = (ScrollView) findViewById(R.id.sv_all);
    }

    protected void addpic() {
        popWindow = new MyPopWindow(GeniusPreviewActivity.this, R.layout.paizhao_pop,
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
                                Intent intents = new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
     * 拍照
     */
    private void takePhoto() {
        //先验证手机是否有sdcard
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {

            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            openCameraIntent.putExtra("return-data", true);

            photoName = getFileName();

            ContentValues values = new ContentValues();

            values.put(MediaStore.Images.Media.TITLE, photoName);

            photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

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
        else if (requestCode == requestCode_to_album) {
            if (data != null) {
                uri = data.getData();
                beginCrop2(uri);
            }
        }

        else if (requestCode == requestCode_to_Photo) {

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

        }

        else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }

        else if (requestCode == CROP_AlBUM) {
            handleCrop2(resultCode, data);
        }

        /// 目前没有使用
        else if (requestCode == requestCode_to_upload) {

            photoName = data.getStringExtra("photoName");

            if (photoName.length() == 0) return;

            new Thread() {
                @Override
                public void run() {
                    try {
                        handler.sendEmptyMessage(REVITION_COMPLETE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        else if(resultCode == PROPERTYPAY){
            postGenius();

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_choose_image:
                addpic();
                popWindow.showAtLocation(v, Gravity.BOTTOM
                        | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_submit_genius:
                Intent intent = new Intent(GeniusPreviewActivity.this,GeniusPreviewDialogActivity.class);
                startActivityForResult(intent, PROPERTYPAY);
                break;
        }
    }

    private void beginCrop(Uri source) {

        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));

        Crop.of(source, destination).asSquare().start(this, Crop.REQUEST_CROP);

    }

    private void beginCrop2(Uri source) {

        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));

        Crop.of(source, destination).asSquare().start( this, CROP_AlBUM);

    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
            Log.i("debbug getOutput  ", "" + Crop.getOutput(result));

            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), Crop.getOutput(result));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoName != null) {
                bmp = BitmapHelper.rotateBitmapByDegree(bmp, BitmapHelper.getBitmapDegree(photoName));
            }

            mIv_choose_image.setImageBitmap(bmp);

            final Bitmap finalBmp = bmp;

            if (TextUtils.isEmpty(photoName)) photoName = getFileName();

            new Thread(new Runnable() {
                public void run() {

                    try {

                        BitmapHelper.saveMyBitmap(photoName, finalBmp);

                        handler.sendEmptyMessage(REVITION_COMPLETE);

                    } catch (IOException e) {

                        e.printStackTrace();

                        savePhotoError.sendEmptyMessage(1);
                    }
                }
            }).start();

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(GeniusPreviewActivity.this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
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
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
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
                    mIv_choose_image.setImageBitmap(finalBmp);
                }
            });

            if (TextUtils.isEmpty(photoName)) photoName = getFileName();

            new Thread(new Runnable() {
                public void run() {

                    try {
                        BitmapHelper.saveMyBitmap(photoName, finalBmp);

                        handler.sendEmptyMessage(REVITION_COMPLETE);

                    } catch (IOException e) {

                        e.printStackTrace();
                        savePhotoError.sendEmptyMessage(1);
                    }
                }
            }).start();

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(GeniusPreviewActivity.this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    Handler savePhotoError = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(GeniusPreviewActivity.this, "保存图片出现异常，请重试！", Toast.LENGTH_SHORT).show();
            photoName = null;
        }
    };

    /**
     * 获取Token部分
     */
    interface TokenService {
        ///api/v1/communities/{communityId}/qiniuToken
        //@POST("/api/v1/communities/getToken")
        @POST("/api/v1/communities/{communityId}/qiniuToken")
        void getToekn(@Header("signature") String signature, @Body QuaryToken qt, @Path("communityId") long communityId, Callback<Token> cb);

        @POST("/api/v1/communities/{communityId}/famousPerson")
        void postGeniusMessage(@Header("signature") String signature, @Body GeniusRequest qt, @Path("communityId") long communityId, Callback<GeniusResult> cb);

        @POST("/api/v3/communities/{communityId}/users/{emobid}/niuren")
        void postGeniusMessageV3(@Body GeniusRequest qt, @Path("communityId") long communityId, @Path("emobid") String emobid, Callback<CommonRespBean<String>> cb);

    }

    private void getToken() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        TokenService service = restAdapter.create(TokenService.class);
        Callback<Token> callback = new Callback<Token>() {
            @Override
            public void success(Token Token, Response response) {
                Message msg = Message.obtain();
                msg.obj = Token.getToken();
                msg.what = UPLOAD_COMPLETE;
                handler.sendMessage(msg);
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.netError), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        };

        QuaryToken quaryToken = new QuaryToken("" + bean.getUserId(), "user");
        service.getToekn(StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(quaryToken)), quaryToken, bean.getCommunityId(), callback);
    }

    private void postGenius() {
//        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
//        TokenService service = restAdapter.create(TokenService.class);
        GeniusRequest quaryToken = new GeniusRequest();
//        quaryToken.setEmobId(bean.getEmobId());
//        quaryToken.setIntro(input);
        quaryToken.setFamousIntroduce(input);
        TokenService service = RetrofitFactory.getInstance().create(getmContext(),quaryToken,TokenService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> Token, Response response) {
               if(Token!=null&&"yes".equals(Token.getStatus())){
                   Intent intent = new Intent(GeniusPreviewActivity.this,GeniusSpecialActivity.class);
                   startActivity(intent);
                   bean.setIdentity("famous");
                   PreferencesUtil.saveLogin(getApplicationContext(),bean);
                   EventBus.getDefault().post(new GeniusEvent());
                   finish();
               }else{
                   Toast.makeText(getApplicationContext(),"牛人信息提交失败，请重新提交",Toast.LENGTH_SHORT).show();
               }
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.netError), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        };


        service.postGeniusMessageV3(quaryToken, bean.getCommunityId(),bean.getEmobId(), callback);
    }

    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.default_picture)
            .showImageForEmptyUri(R.drawable.default_picture)
            .showImageOnFail(R.drawable.default_picture)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();
}
