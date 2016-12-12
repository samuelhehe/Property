package xj.property.activity.LifeCircle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.activity.activities.AlbumActivity;
import xj.property.activity.activities.BitmapHelper;
import xj.property.activity.activities.FileUtils;
import xj.property.activity.activities.ImagePagerActivity;
import xj.property.adapter.ActivityImageGridAdapter;
import xj.property.adapter.ExpressionAdapter;
import xj.property.adapter.ExpressionPagerAdapter;
import xj.property.beans.CircleNewRecord;
import xj.property.beans.CommonPostResultBean;
import xj.property.beans.QuaryToken;
import xj.property.beans.Token;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseUtils;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.BimpRotateUtil;
import xj.property.utils.ImageUploadUtils;
import xj.property.utils.ImageUtils;
import xj.property.utils.SmileUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.video.util.AsyncTask;
import xj.property.widget.ExpandGridView;
import xj.property.widget.MyPopWindow;
import xj.property.widget.com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by Administrator on 2015/6/8.
 */
public class NewRecoreActivity extends HXBaseActivity {

    private GridView gv_image_record;
    private EditText et_record_detail;
    private RelativeLayout rl_switch_block_notify;
    private ImageView iv_switch_block_notify;
    private ImageView iv_switch_unblock_notify;


    public static final int requestCode_to_Photo = 1;
    public static final int requestCode_to_album = 2;
    public static final int requestCode_to_imagePager = 3;
    public static final int UPLOAD_COMPLETE = 550;
    public static final int UPLOAD_SUCCESS = 549;

    private String photoName;

    private ActivityImageGridAdapter activityImageGridAdapter;

    MyPopWindow pop;

    int loadConut;
    private int isCreate = 0;//是否创建群    1 - 创建   2 - 不创建   /// v3 2016/03/02 {是否创建群组：1->创建,0->不创建}
    private TextView tv_right_text;
    private LinearLayout ll_face_container;
    private ViewPager vPager;
    private CirclePageIndicator vpager_indicator;
    private List<String> reslist;
    private PopupWindow popupWindow;
    private CheckBox send_img_checkbox;

    private LinearLayout bar_bottom;
    private InputMethodManager imm;
    private UserInfoDetailBean userDetailsBean;
    private String lifephotos; //// 发起生活圈的图片地址， 逗号隔开， eg: "http://7d9lcl.com2.z0.glb.qiniucdn.com/FpgAe-5Aan5MCL4g0BonwAnYjos9,http://7d9lcl.com2.z0.glb.qiniucdn.com/FpgAe-5Aan5MCL4g0BonwAnYjos9"
    private Map<String, String> reskeyImagesMap; //// 发起生活圈的上传key与path

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BitmapHelper.cleanBitmap();
        setContentView(R.layout.activity_new_record);
//        initTitle(null, "发生活圈", "    发送");
        initTitle(null, "发生活圈", "");
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
        userDetailsBean = PreferencesUtil.getLoginInfo(NewRecoreActivity.this);
    }

    private void initView() {
        rl_switch_block_notify = (RelativeLayout) findViewById(R.id.rl_switch_block_notify);
        iv_switch_block_notify = (ImageView) findViewById(R.id.iv_switch_block_notify);
        iv_switch_unblock_notify = (ImageView) findViewById(R.id.iv_switch_unblock_notify);
        rl_switch_block_notify.setOnClickListener(this);

        tv_right_text = (TextView) findViewById(R.id.tv_right_text);
        tv_right_text.setText("发送");
        tv_right_text.setVisibility(View.VISIBLE);
        tv_right_text.setOnClickListener(this);

        et_record_detail = (EditText) findViewById(R.id.et_record_detail);

        et_record_detail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
//                    boolean bool = imm.showSoftInput(v,InputMethodManager.SHOW_IMPLICIT);
//                    if (bool) {
//                        send_img_checkbox.setButtonDrawable(getResources().getDrawable(R.drawable.chatting_biaoqing_btn_normal));
                    bar_bottom.setVisibility(View.VISIBLE);
                    ll_face_container.setVisibility(View.GONE);
//                    }


                } else {

                    bar_bottom.setVisibility(View.GONE);
                    ll_face_container.setVisibility(View.GONE);
//                    closeBoard();
                }

            }
        });

        et_record_detail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                boolean bool = imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                if (bool) {
                    send_img_checkbox.setButtonDrawable(getResources().getDrawable(R.drawable.chatting_biaoqing_btn_normal));
                    bar_bottom.setVisibility(View.VISIBLE);
                    ll_face_container.setVisibility(View.GONE);

                }

                return false;
            }
        });

        et_record_detail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                StrUtils.lengthFilter(getmContext(), et_record_detail, 1500, "内容最长为1500字");
            }
        });

        bar_bottom = (LinearLayout) findViewById(R.id.bar_bottom);

        ll_face_container = (LinearLayout) findViewById(R.id.ll_face_container);

        vPager = (ViewPager) findViewById(R.id.vPager);

        vpager_indicator = (CirclePageIndicator) findViewById(R.id.vpager_indicator);

        // 表情list 目前表情数量为99个
        reslist = getExpressionRes(99);

        // 初始化表情viewpager
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        View gv3 = getGridChildView(3);
        View gv4 = getGridChildView(4);
        View gv5 = getGridChildView(5);

        views.add(gv1);
        views.add(gv2);
        views.add(gv3);
        views.add(gv4);
        views.add(gv5);

        vPager.setAdapter(new ExpressionPagerAdapter(views));
        vpager_indicator.setViewPager(vPager);

        send_img_checkbox = (CheckBox) findViewById(R.id.send_img_checkbox);
        send_img_checkbox.setOnCheckedChangeListener(new onMyCheckedChangeListener());


//        initPopupWindow();
        gv_image_record = (GridView) findViewById(R.id.gv_image_record);
        activityImageGridAdapter = new ActivityImageGridAdapter(this, 9);
        gv_image_record.setAdapter(activityImageGridAdapter);

        gv_image_record.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(gv_image_record.getWindowToken(), 0); //强制隐藏键盘

                if (arg2 == BitmapHelper.bitmapListMemory.size()) {
                    // new PopupWindows(NewRecoreActivity.this, gv_image_record);

                    pop = new MyPopWindow(NewRecoreActivity.this, R.layout.paizhao_pop,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    switch (v.getId()) {
                                        // 拍照
                                        case R.id.pop_xi:
                                            takePhoto();
                                            pop.dismiss();
                                            break;
                                        // 从相册中选
                                        case R.id.pop_mu:
                                            pop.dismiss();
                                            Intent intent = new Intent(NewRecoreActivity.this, AlbumActivity.class);
                                            intent.putExtra(Config.INTENT_IMAGECOUNT, 9);
                                            startActivityForResult(intent, requestCode_to_album);
                                            pop.dismiss();

                                            break;
                                        // 取消
                                        case R.id.pop_no:
                                            pop.dismiss();
                                            break;
                                    }
                                }
                            });
                    pop.showAtLocation(gv_image_record, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);


                } else {
                    Intent intentPush = new Intent(NewRecoreActivity.this,
                            ImagePagerActivity.class);
                    intentPush.putExtra("ID", arg2);
                    startActivityForResult(intentPush, requestCode_to_imagePager);
                }
            }
        });

    }


    private class onMyCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
//                closeBoard();
                imm.hideSoftInputFromWindow(et_record_detail.getWindowToken(), 0);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (ll_face_container != null) {
                            ll_face_container.setVisibility(View.VISIBLE);
                            send_img_checkbox.setButtonDrawable(getResources().getDrawable(R.drawable.chatting_biaoqing_btn_enable));
                        }
                    }
                }, 200);

            } else {
                if (ll_face_container != null) {
                    ll_face_container.setVisibility(View.GONE);
                    send_img_checkbox.setButtonDrawable(getResources().getDrawable(R.drawable.chatting_biaoqing_btn_normal));
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        showKeyBoard();
                        imm.showSoftInput(et_record_detail, InputMethodManager.SHOW_IMPLICIT);
                    }
                }, 200);

            }
        }
    }

    public void showKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void closeBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
        if (imm.isActive())  //一直是true
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 获取表情的gridview的子view
     *
     * @param i
     * @return
     */
    private View getGridChildView(int i) {
        View view = View.inflate(this, R.layout.expression_gridview, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();
        if (i == 1) {
            List<String> list1 = reslist.subList(0, 20);/// 20/27//34
            list.addAll(list1);
        } else if (i == 2) {
            list.addAll(reslist.subList(20, 40));
        } else if (i == 3) {
            list.addAll(reslist.subList(40, 60));
        } else if (i == 4) {
            list.addAll(reslist.subList(60, 80));
        } else if (i == 5) {
            list.addAll(reslist.subList(80, reslist.size()));
        }
        list.add("delete_expression");
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this, 1, list);
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = expressionAdapter.getItem(position);
                try {
                    if (filename != "delete_expression") { // 不是删除键，显示表情
                        // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                        Class clz = Class.forName("xj.property.utils.SmileUtils");

                        Field field = clz.getField(filename);
                        int selectionStart = et_record_detail.getSelectionStart();// 获取光标的位置
                        Spannable smiledText = SmileUtils.getSmiledText(getmContext(), (String) field.get(null));
                        Editable editableText = et_record_detail.getEditableText();
                        editableText.insert(selectionStart, smiledText);

//                        et_record_detail.append(SmileUtils.getSmiledText(getmContext(), (String) field.get(null)));

                    } else {
                        // 删除文字或者表情
                        if (!TextUtils.isEmpty(et_record_detail.getText())) {

                            int selectionStart = et_record_detail.getSelectionStart();// 获取光标的位置

                            if (selectionStart > 0) {

                                String body = et_record_detail.getText().toString();

                                String tempStr = body.substring(0, selectionStart);

                                int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                if (i != -1) {
                                    /// 截取最后一个表情
                                    CharSequence cs = tempStr.substring(i, selectionStart);

                                    if (SmileUtils.containsKey(cs.toString()))
                                        /// 删除最后一个表情字符串的占位符
                                        et_record_detail.getEditableText().delete(i, selectionStart);
                                    else
                                        et_record_detail.getEditableText().delete(selectionStart - 1, selectionStart);
                                } else {
                                    et_record_detail.getEditableText().delete(selectionStart - 1, selectionStart);
                                }
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        return view;
    }

    public List<String> getExpressionRes(int getSum) {
        List<String> reslist = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "ee_" + x;

            reslist.add(filename);

        }
        return reslist;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_switch_block_notify:
                if (iv_switch_unblock_notify.getVisibility() == View.VISIBLE) {//创建群聊
                    iv_switch_unblock_notify.setVisibility(View.INVISIBLE);
                    iv_switch_block_notify.setVisibility(View.VISIBLE);
                    isCreate = 1;
                } else {
                    iv_switch_unblock_notify.setVisibility(View.VISIBLE);
                    iv_switch_block_notify.setVisibility(View.INVISIBLE);
                    isCreate = 0;
                }
                break;
            case R.id.tv_right_text:
                if (!PreferencesUtil.getLogin(NewRecoreActivity.this)) {
                    Intent intent = new Intent(NewRecoreActivity.this, RegisterLoginActivity.class);
                    startActivity(intent);
                    return;
                } else {
                    if (et_record_detail.getText().toString().length() == 0 && BitmapHelper.imageCount == 0) {
                        Toast.makeText(NewRecoreActivity.this, "请填写发起内容或上传图片！", Toast.LENGTH_LONG).show();
                    } else {
                        if (checkInput()) {
                            newRecord();
                        } else {
                            Toast.makeText(NewRecoreActivity.this, "内容最长为1500字", Toast.LENGTH_LONG).show();
                        }
                    }

                }
                break;
        }


    }

    int getTokencount = 3;

    @Override
    protected void onResume() {
        super.onResume();
        getTokencount = 3;
    }

    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            View view = View
                    .inflate(mContext, R.layout.item_popupwindow, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            update();
            TextView bt1 = (TextView) view
                    .findViewById(R.id.item_popupwindows_camera);
            TextView bt2 = (TextView) view
                    .findViewById(R.id.item_popupwindows_Photo);
            TextView bt3 = (TextView) view
                    .findViewById(R.id.item_popupwindows_cancel);

            bt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    takePhoto();
                    dismiss();
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(NewRecoreActivity.this,
                            AlbumActivity.class);
                    startActivityForResult(intent, requestCode_to_album);
                    dismiss();
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    private void takePhoto() {
        //先验证手机是否有sdcard
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            openCameraIntent.putExtra("return-data", true);
            photoName = getFileName();
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photoName)));
            startActivityForResult(openCameraIntent, requestCode_to_Photo);
        }
    }

    private String getFileName() {
        String fileName = String.valueOf(System.currentTimeMillis());
        String path;
        File sdcardPath = Environment.getExternalStorageDirectory();
        path = sdcardPath.getAbsolutePath() + File.separator + Config.BASE_IMAGE_CACHE +
                File.separator + "image";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path + File.separator + fileName + ".jpg";
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //// 生成Path map
                    generatorResKeyPath();
                    gv_image_record.setAdapter(activityImageGridAdapter);
//                    activityImageGridAdapter.notifyDataSetChanged();
                    break;
                case UPLOAD_COMPLETE:
                    String token = (String) msg.obj;
                    UploadManager um = new UploadManager();
                    loadConut = reskeyImagesMap.size();
                    if (reskeyImagesMap != null && !reskeyImagesMap.isEmpty()) {
                        Set<Map.Entry<String, String>> entries = reskeyImagesMap.entrySet();
                        for (final Map.Entry<String, String> set : entries) {
                            um.put(set.getValue(), set.getKey(), token, new UpCompletionHandler() {
                                @Override
                                public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
                                    Log.d("NewRecordActivity", " upload complete key: " + set.getKey() + " isok : " + responseInfo.isOK() + " isServerError " + responseInfo.isServerError() + " error: " + responseInfo.error);
                                    handler.sendEmptyMessage(UPLOAD_SUCCESS);
                                }
                            }, null);
                        }
                    }
//                    for (int i = 0; i < BitmapHelper.uploadImageStorage.size(); i++) {
//                        final int finalI = i;
//                        um.put(BitmapHelper.uploadImageStorage.get(i), null, token, new UpCompletionHandler() {
//                            @Override
//                            public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
//                                Log.d("NewRecordActivity", " upload complete  i: " + finalI + " isok : " + responseInfo.isOK() + " isServerError " + responseInfo.isServerError() + " error: " + responseInfo.error);
//                                handler.sendEmptyMessage(UPLOAD_SUCCESS);
//                            }
//                        }, null);
//
//                    }
                    break; /// 2015/12/28  break ,,,
                case UPLOAD_SUCCESS:
                    uploadFinish();
                    break;


            }
            super.handleMessage(msg);
        }
    };

    private synchronized void uploadFinish() {
        if (loadConut > 0) {
            loadConut--;
            Log.d("NewRecordActivity", " uploadFinish " + loadConut);

            if (loadConut == 0) {
                mLdDialog.dismiss();
                BitmapHelper.cleanBitmap();
                setResult(RESULT_OK);
                finish();
            }

        } else {
            mLdDialog.dismiss();
            BitmapHelper.cleanBitmap();
            setResult(RESULT_OK);
//                        showToast("发布成功");
            finish();
        }
    }

    /**
     * 生成需要上传图片的map
     *
     * @return
     */
    private void generatorResKeyPath() {
        int imageSize = BitmapHelper.uploadImageStorage.size();
        if (imageSize > 0) {
            StringBuilder sb = new StringBuilder();
            Map<String, String> reskeylist = new HashMap<>(imageSize);
            for (String uploadpath : BitmapHelper.uploadImageStorage) {
                String reskey = ImageUploadUtils.generateResKey();
                String photopath = Config.QINIU_BASE_URL + reskey;
                reskeylist.put(reskey, uploadpath); /// reskey--->>path
                sb.append(photopath).append(",");
            }
            lifephotos = sb.substring(0, sb.length() - 1);
            Log.d(TAG, "generatorResKeyPath -- lifephotos " + lifephotos);
            reskeyImagesMap = reskeylist;
        } else {
            reskeyImagesMap = null;
        }
    }

    public void refreshGridView() {

        new Thread(new Runnable() {
            public void run() {
                dialogHandler.sendEmptyMessage(0);
                while (true) {
                    if (BitmapHelper.imageCount >= BitmapHelper.imageListStorage.size()) {

                        handler.sendEmptyMessage(1);
                        break;
                    } else {
                        try {
                            String path = BitmapHelper.imageListStorage.get(BitmapHelper.imageCount);
                            Bitmap bitmap = null;
                            bitmap = BitmapHelper.revitionImageSizeBydegree(path);
                            if (bitmap != null) {
                                BitmapHelper.bitmapListMemory.add(bitmap);
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showToast("无法加载图片!");
                                    }
                                });
                            }
                            BitmapHelper.imageCount++;
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showToast("加载图片异常!");
                                }
                            });
                        }
                    }
                }
                dialogHandler.sendEmptyMessage(1);
            }
        }).start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AlbumActivity.resultCode_to_NewActivity) {
            if (data != null) {
                refreshGridView();
            }
        } else if (resultCode == ImagePagerActivity.resultCode_to_newAcitivity) {
            refreshGridView();
        }
        if (requestCode == requestCode_to_Photo) {

            if (data != null) { //可能尚未指定intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                //返回有缩略图
                if (data.hasExtra("data")) {
                    Bitmap thumbnail = data.getParcelableExtra("data");
                    //Bitmap thumbnail= null;
                    try {
                        int degree = BimpRotateUtil.getBitmapDegree(photoName);
                        thumbnail = BimpRotateUtil.rotateBitmapByDegree(thumbnail, degree);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //得到bitmap后的操作
                    FileUtils.saveBitmap(thumbnail, photoName);
                    // FileUtils.saveMyBitmap(photoName,thumbnail );
                    File f = new File(photoName);
                    if (f.exists()) {
                        ImageUtils.rotate(BitmapFactory.decodeFile(photoName), ImageUtils.readPictureDegree(photoName));
                        BitmapHelper.imageListStorage.add(photoName);
                        refreshGridView();
                    }
                }
            } else {

                //由于指定了目标uri，存储在目标uri，intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                // 通过目标uri，找到图片
                // 对图片的缩放处理
                // 操作
                ImageUtils.rotate(BitmapFactory.decodeFile(photoName), ImageUtils.readPictureDegree(photoName));
                BitmapHelper.imageListStorage.add(photoName);
                refreshGridView();
            }

        }
        if (requestCode == requestCode_to_imagePager) {
            refreshGridView();
        }
    }

    private void getToken() {
        NetBaseUtils.extractNewToken(getmContext(),new NetBaseUtils.NetRespListener<CommonRespBean<String>>() {
            @Override
            public void successYes(CommonRespBean<String> commonRespBean, Response response) {
                Message msg = Message.obtain();
                msg.obj = commonRespBean.getData();
                msg.what = UPLOAD_COMPLETE;
                handler.sendMessage(msg);
            }

            @Override
            public void successNo(CommonRespBean<String> commonRespBean, Response response) {
                showToast("上传图片出错：" + commonRespBean.getMessage());
                if(mLdDialog!=null){
                    mLdDialog.dismiss();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                if (getTokencount-- > 0) {
                    getToken();
                } else {
                    mLdDialog.dismiss();
                    showNetErrorToast();
                }
            }
        });
    }


    /**
     * new record
     */
    interface NewCircleRecordService {
//        @POST("/api/v1/communities/{communityId}/circles")
//        void newCircleRecord(@Header("signature") String signature, @Body CircleNewRecord circleNewRecord, @Path("communityId") long communityId, Callback<CommonPostResultBean> cb);


        //        @POST("/api/v1/communities/{communityId}/circles")
        @POST("/api/v3/lifeCircles")
        void newCircleRecord(@Body CircleNewRecord circleNewRecord, Callback<CommonRespBean<String>> cb);

    }


    private boolean checkInput() {
        if (et_record_detail.getText().toString().length() >= 1500) {
            return false;
        }
        return true;
    }

    /**
     * apply new activity
     */
    private void newRecord() {
        mLdDialog.show();

        CircleNewRecord circleNewRecord = new CircleNewRecord();
        circleNewRecord.setEmobId(userDetailsBean.getEmobId());
        circleNewRecord.setCommunityId(userDetailsBean.getCommunityId());
        circleNewRecord.setLifeContent(et_record_detail.getText().toString());
        circleNewRecord.setCreateGroup(isCreate);
        if (!TextUtils.isEmpty(lifephotos)) {
            circleNewRecord.setPhotoes(lifephotos);
        }
        Log.i("onion", "CircleNewRecord: " + circleNewRecord.toString());
        NetBaseUtils.newCircleRecord(getmContext(),circleNewRecord,new NetBaseUtils.NetRespListener<CommonRespBean<String>>() {
            @Override
            public void successYes(CommonRespBean<String> commonRespBean, Response response) {
                if (BitmapHelper.imageListStorage.size() > 0) {
                    mLdDialog.setMessage("正在上传图片");
                    getToken();
                } else {
                    mLdDialog.dismiss();
                    showToast("发布成功");
                    setResult(RESULT_OK);
                    finish();
                }
            }
            @Override
            public void successNo(CommonRespBean<String> commonRespBean, Response response) {
                mLdDialog.dismiss();
                showDataErrorToast(commonRespBean.getMessage());
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                showNetErrorToast();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BitmapHelper.cleanBitmap();
    }

    Handler dialogHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mLdDialog.show();
                    break;
                case 1:
                    mLdDialog.dismiss();
                    break;
            }
        }
    };
}
