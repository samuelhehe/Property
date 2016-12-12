package xj.property.activity.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
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
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
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
import xj.property.Constant;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.NewFriendsInviteActivity;
import xj.property.adapter.ActivityImageGridAdapter;
import xj.property.adapter.ExpressionAdapter;
import xj.property.adapter.ExpressionPagerAdapter;
import xj.property.beans.ActivityBean;
import xj.property.beans.CommonPostResultBean;
import xj.property.beans.QuaryToken;
import xj.property.beans.Token;
import xj.property.beans.UserInfoDetailBean;
import xj.property.fragment.DataPickerFragmentSingleButtonDialog;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseUtils;
import xj.property.netbase.RetrofitFactory;
import xj.property.ums.controller.DownloadServices;
import xj.property.utils.ImageUploadUtils;
import xj.property.utils.ImageUtils;
import xj.property.utils.BimpRotateUtil;
import xj.property.utils.SmileUtils;
import xj.property.utils.TimeUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.MaxLengthWatcher;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.ExpandGridView;
import xj.property.widget.MyPopWindow;
import xj.property.widget.com.viewpagerindicator.CirclePageIndicator;

public class NewActivityActivity extends HXBaseActivity implements DataPickerFragmentSingleButtonDialog.OnDateSetCallBack {
    /**
     * logger
     */

    private FrameLayout fl1;
    private FrameLayout fl2;
    /**
     * gridview for activity image
     */
    private GridView gv_image_activity;

    /**
     * activity title
     */
    private EditText et_activity_name;
    /**
     * activty detail
     */
    private EditText et_activity_detail;
    /**
     * activty time
     */
    private TextView et_activity_time;

    /**
     * activty location
     */
    private EditText et_activity_location;

    /**
     * activty time , int
     */
    private int activityTime;

    /**
     * date picker dialog
     */
    private DataPickerFragmentSingleButtonDialog dataPickerFragmentSingleButtonDialog;

    /**
     * adapter
     */
    private ActivityImageGridAdapter activityImageGridAdapter;
    /**
     * request code
     */
    public static final int requestCode_to_Photo = 1;
    public static final int requestCode_to_album = 2;
    public static final int requestCode_to_imagePager = 3;
    public static final int UPLOAD_COMPLETE = 550;
    public static final int UPLOAD_SUCCESS = 549;
    private String imagePath = "";
    private ScrollView scroll_root; //// 当点击活动内容时,控制滑动到底部
    private InputMethodManager imm;
    private LinearLayout bar_bottom;
    private LinearLayout ll_face_container;
    private ViewPager vPager;
    private CirclePageIndicator vpager_indicator;
    private List<String> reslist;
    private CheckBox send_img_checkbox;
    private RelativeLayout new_activity_joingroup_rlay;
    private ImageView iv_switch_on_notify;
    private ImageView iv_switch_off_notify;
    private UserInfoDetailBean userInfoDetailBean;
    private String lifephotos; /// 将要上传的活动图片,逗号分开
    private Map<String, String> reskeyImagesMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initTitle(null, "发起活动/话题", "");
        initView();
        userInfoDetailBean = PreferencesUtil.getLoginInfo(getmContext());
    }

    private  MyPopWindow pop;
    /**
     * init view
     */
    private void initView() {
        //activity title
        et_activity_name = (EditText) findViewById(R.id.et_activity_name);
        et_activity_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            scroll_root.fullScroll(ScrollView.FOCUS_UP);
//                        }
//                    },100);
                    bar_bottom.setVisibility(View.GONE);
                    ll_face_container.setVisibility(View.GONE);
                }
            }
        });
        et_activity_name.addTextChangedListener(new MaxLengthWatcher(NewActivityActivity.this, 14, et_activity_name, "活动名称最多输入14个字!"));
        //activity detail
        et_activity_detail = (EditText) findViewById(R.id.et_activity_detail);
        et_activity_detail.addTextChangedListener(new MaxLengthWatcher(NewActivityActivity.this, 500, et_activity_detail, "活动详情最多输入500个字!"));

        //activity location
        et_activity_location = (EditText) findViewById(R.id.et_activity_location);
        et_activity_location.addTextChangedListener(new MaxLengthWatcher(NewActivityActivity.this, 12, et_activity_location, "活动地点最多输入12个字!"));
        et_activity_location.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            scroll_root.fullScroll(ScrollView.FOCUS_UP);
//                        }
//                    },100);
                    bar_bottom.setVisibility(View.GONE);
                    ll_face_container.setVisibility(View.GONE);
                }
            }
        });

        //activty time
        et_activity_time = (TextView) findViewById(R.id.et_activity_time);

        et_activity_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dataPickerFragmentSingleButtonDialog == null) {
                    dataPickerFragmentSingleButtonDialog = new DataPickerFragmentSingleButtonDialog(NewActivityActivity.this);
                }
                dataPickerFragmentSingleButtonDialog.show(getFragmentManager(), "NewActivityActivity");
                hideKeyboard();
            }
        });

        new_activity_joingroup_rlay = (RelativeLayout) findViewById(R.id.new_activity_joingroup_rlay);
        new_activity_joingroup_rlay.setOnClickListener(this);

        iv_switch_on_notify = (ImageView) findViewById(R.id.iv_switch_on_notify);
        iv_switch_on_notify.setOnClickListener(this);
        iv_switch_off_notify = (ImageView) findViewById(R.id.iv_switch_off_notify);
        iv_switch_off_notify.setOnClickListener(this);


        scroll_root = (ScrollView) findViewById(R.id.scroll_root);

        et_activity_detail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            scroll_root.fullScroll(ScrollView.FOCUS_DOWN);
//                        }
//                    },100);
                    bar_bottom.setVisibility(View.VISIBLE);
                    ll_face_container.setVisibility(View.GONE);

                } else {

                    bar_bottom.setVisibility(View.GONE);
                    ll_face_container.setVisibility(View.GONE);
//                    closeBoard();
                }

            }
        });

        et_activity_detail.setOnTouchListener(new View.OnTouchListener() {
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


        gv_image_activity = (GridView) findViewById(R.id.gv_activity);
        fl1 = (FrameLayout) findViewById(R.id.fl1);
        fl2 = (FrameLayout) findViewById(R.id.fl2);

        activityImageGridAdapter = new ActivityImageGridAdapter(this);
        gv_image_activity.setAdapter(activityImageGridAdapter);

        gv_image_activity.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(gv_image_activity.getWindowToken(), 0); //强制隐藏键盘

                if (position == BitmapHelper.bitmapListMemory.size()) {
                    //  new PopupWindows(NewActivityActivity.this, gv_image_activity);

                    pop = new MyPopWindow(NewActivityActivity.this, R.layout.paizhao_pop,
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
                                            Intent intent = new Intent(NewActivityActivity.this,
                                                    AlbumActivity.class);
                                            intent.putExtra(Config.INTENT_IMAGECOUNT, 6);
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
                    pop.showAtLocation(gv_image_activity, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);


                } else {
                    Intent intentPush = new Intent(NewActivityActivity.this,
                            ImagePagerActivity.class);
                    intentPush.putExtra("ID", position);
                    startActivityForResult(intentPush, requestCode_to_imagePager);
                }
            }
        });


        //new apply button
        LinearLayout btn_apply_activity = (LinearLayout) findViewById(R.id.btn_apply_activity);
        btn_apply_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput() == false) {
                    showToast("请输入活动名及活动内容!");
                    return;
                } else {
                    showNoticeDialog(NewActivityActivity.this);
                }
            }
        });
    }

    int loadConut;
    /**
     * handler
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    /// 刷新生成photos
                    generatorResKeyPath();
                    gv_image_activity.setAdapter(activityImageGridAdapter);
//                    activityImageGridAdapter.notifyDataSetChanged();
                    break;
                case UPLOAD_COMPLETE:
                    String token = (String) msg.obj;
                    UploadManager um = new UploadManager();
                    if (reskeyImagesMap != null && reskeyImagesMap.size() > 0) {
                        loadConut = reskeyImagesMap.size();
                        Set<Map.Entry<String, String>> entries = reskeyImagesMap.entrySet();
                        for (final Map.Entry<String, String> entry : entries) {
                            um.put(entry.getValue(),entry.getKey(), token, new UpCompletionHandler() {
                                @Override
                                public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
                                    Log.d("NewRecordActivity", " upload complete key: " + entry.getKey() + " isok : " + responseInfo.isOK() + " isServerError " + responseInfo.isServerError() + " error: " + responseInfo.error);
                                    handler.sendEmptyMessage(UPLOAD_SUCCESS);
                                }
                            }, null);
                        }
                    }
                    break; /// v3 2016/03/04
                case UPLOAD_SUCCESS:

                    uploadsuccess();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void uploadsuccess() {
        if (loadConut > 0) {
            loadConut--;
            if(loadConut==0){
                mLdDialog.dismiss();
                BitmapHelper.cleanBitmap();
                ////TODO 跳转选择邻居
                if (infoEntity != null) {
                    Intent intent = new Intent(getmContext(), NewFriendsInviteActivity.class);
                    intent.putExtra("isPublishActivity", true);
                    intent.putExtra("groupId", infoEntity.getEmobGroupId());
                    intent.putExtra("groupowner", infoEntity.getEmobIdOwner());
                    startActivity(intent);
                }
                showToast("已发起活动");
                setResult(RESULT_OK);
                finish();
            }
        } else {
            mLdDialog.dismiss();
            BitmapHelper.cleanBitmap();
            ////TODO 跳转选择邻居
            if (infoEntity != null) {
                Intent intent = new Intent(getmContext(), NewFriendsInviteActivity.class);
                intent.putExtra("isPublishActivity", true);
                intent.putExtra("groupId", infoEntity.getEmobGroupId());
                intent.putExtra("groupowner", infoEntity.getEmobIdOwner());
                startActivity(intent);
            }
            showToast("已发起活动");
            setResult(RESULT_OK);
            finish();
        }
    }


    /**
     * 生成需要上传图片的map
     *
     * @return
     */
    private void generatorResKeyPath() {
        int imageSize = BitmapHelper.imageListStorage.size();
        if (imageSize > 0) {
            StringBuilder sb = new StringBuilder();
            Map<String, String> reskeylist = new HashMap<>(imageSize);
            for (String uploadpath : BitmapHelper.imageListStorage) {
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


    /**
     * refresh gridview
     */
    public void refreshGridView() {

        new Thread(new Runnable() {
            public void run() {
                while (true) {
//                    logger.info("Bimp count is :" + BitmapHelper.imageCount);
//                    logger.info("drr size is :"+ BitmapHelper.imageListStorage.size());

                    if (BitmapHelper.imageCount == BitmapHelper.imageListStorage.size()) {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        break;
                    } else {
                        try {
                            String path = BitmapHelper.imageListStorage.get(BitmapHelper.imageCount);
                            Bitmap bitmap = null;
//                            try {
//                                Log.i("onion","photoName: "+path);
//                                int degree=BimpRotateUtil.getBitmapDegree(path);
//                                Log.i("onion","degree: "+degree);
//                                bitmap = BimpRotateUtil.rotateBitmapByDegree(bitmap,degree);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
                            // bitmap = BitmapHelper.revitionImageSize(path);
                            bitmap = BitmapHelper.revitionImageSizeBydegree(path);
                            BitmapHelper.bitmapListMemory.add(bitmap);

                            BitmapHelper.imageCount++;
//                            Message message = new Message();
//                            message.what = 1;
//                            handler.sendMessage(message);
//                            break;
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
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


                        int selectionStart = et_activity_detail.getSelectionStart();// 获取光标的位置
                        Spannable smiledText = SmileUtils.getSmiledText(getmContext(), (String) field.get(null));
                        Editable editableText = et_activity_detail.getEditableText();
                        editableText.insert(selectionStart, smiledText);

//                        et_activity_detail.append(SmileUtils.getSmiledText(getmContext(), (String) field.get(null)));

                    } else {
                        // 删除文字或者表情
                        if (!TextUtils.isEmpty(et_activity_detail.getText())) {

                            int selectionStart = et_activity_detail.getSelectionStart();// 获取光标的位置

                            if (selectionStart > 0) {

                                String body = et_activity_detail.getText().toString();

                                String tempStr = body.substring(0, selectionStart);

                                int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                if (i != -1) {
                                    /// 截取最后一个表情
                                    CharSequence cs = tempStr.substring(i, selectionStart);

                                    if (SmileUtils.containsKey(cs.toString()))
                                        /// 删除最后一个表情字符串的占位符
                                        et_activity_detail.getEditableText().delete(i, selectionStart);
                                    else
                                        et_activity_detail.getEditableText().delete(selectionStart - 1, selectionStart);
                                } else {
                                    et_activity_detail.getEditableText().delete(selectionStart - 1, selectionStart);
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


    private class onMyCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
//                closeBoard();
                imm.hideSoftInputFromWindow(et_activity_detail.getWindowToken(), 0);
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
                        imm.showSoftInput(et_activity_detail, InputMethodManager.SHOW_IMPLICIT);

                    }
                }, 200);

            }
        }
    }


    public void showNoticeDialog(final Context context) {
        final Dialog noticeDialog = new Dialog(context, R.style.MyDialogStyle);
        noticeDialog.setContentView(R.layout.dialog_newactivity);
        TextView tv_cancle = (TextView) noticeDialog.findViewById(R.id.tv_cancle);
        TextView tv_relogin = (TextView) noticeDialog.findViewById(R.id.tv_relogin);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noticeDialog.dismiss();
            }
        });
        tv_relogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivity();
                noticeDialog.dismiss();
            }
        });
        noticeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        noticeDialog.setCancelable(false);
        noticeDialog.show();
    }


    /**
     * popupwindow to select image from album or camera
     */
    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            View view = View
                    .inflate(mContext, R.layout.item_popupwindow, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
//            LinearLayout ll_popup = (LinearLayout) view
//                    .findViewById(R.id.ll_popup);
//            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
//                    R.anim.push_bottom_in_2));
            // ll_popup.setBackgroundColor(0x70000000);

//            setWidth(ViewGroup.LayoutParams.FILL_PARENT);
//            setHeight(ViewGroup.LayoutParams.FILL_PARENT);
//            setBackgroundDrawable(new BitmapDrawable());
//            setFocusable(true);
//            setOutsideTouchable(true);
            setContentView(view);
            // showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            showAtLocation(parent, Gravity.BOTTOM
                    | Gravity.CENTER_HORIZONTAL, 0, 0);
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
                    Intent intent = new Intent(NewActivityActivity.this,
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
            if (resultCode == RESULT_OK) {
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

                    File picfile = new File(photoName);
                    //// 存在并且文件不是空
                    if (picfile.exists() && picfile.length() > 0) {
                        ImageUtils.rotate(BitmapFactory.decodeFile(photoName), ImageUtils.readPictureDegree(photoName));
                        BitmapHelper.imageListStorage.add(photoName);
                        refreshGridView();
                    }
                }
            }
        }
        if (requestCode == requestCode_to_imagePager) {
            refreshGridView();
        }
    }

    private String photoName;


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

    int getTokencount = 3;

    @Override
    protected void onResume() {
        super.onResume();
        getTokencount = 3;
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


    @Override
    public void onDateSetCallBack(int year, int month, int day, String tag) {
        String date;
        date = "活动时间：" + year + "年  " + (++month) + "月  " + day + "日";
        et_activity_time.setText(date);
        String formatDate = year + "-" + (month) + "-" + day + " " + "00:00:00";
        activityTime = TimeUtils.fromDateStringToInt(formatDate);
    }

    /**
     * new activity service
     */
    interface NewActivityService {
        //        @POST("/api/v1/communities/{communityId}/users/{userId}/activities")
        @POST("/api/v3/activities")
        void newAcitivy(@Body ActivityBean activityBean, Callback<CommonRespBean<ActivityBean>> cb);
    }

    private boolean checkInput() {
        if (et_activity_name.getText().toString().length() == 0) {
            return false;
        }

        /*else  if (et_activity_time.getText().toString().length()==0){
            return false;
        }

        else  if (et_activity_location.getText().toString().length()==0){
            return false;
        }*/
        else if (et_activity_detail.getText().toString().length() == 0) {
            return false;
        } else return true;
    }


    ActivityBean infoEntity;

    /**
     * apply new activity
     */
    private void newActivity() {
        mLdDialog.show();

        /**
         * {
         "activityTitle": "小区篮球社",
         "activityDetail": "本小区内喜欢打球的兄弟们都进来，大家一起玩",
         "emobIdOwner": "d463b16dfc014466a1e441dd685ba505",
         "place": "一号院篮球场",
         "communityId": 2,
         "activityTime": 1456729161,
         "type": "activity",
         "approval": "yes"
         }
         */
        ActivityBean activityBean = new ActivityBean();
        activityBean.setEmobIdOwner(userInfoDetailBean.getEmobId());
        activityBean.setCommunityId(userInfoDetailBean.getCommunityId());
        activityBean.setType("activity");
        activityBean.setActivityTitle(et_activity_name.getText().toString());
        activityBean.setActivityDetail(et_activity_detail.getText().toString());
        activityBean.setActivityTime(activityTime);
        activityBean.setPlace(et_activity_location.getText().toString());
        if (!TextUtils.isEmpty(lifephotos)) {
            activityBean.setPhotoes(lifephotos);
        }
        activityBean.setApproval((iv_switch_on_notify.getVisibility() == View.VISIBLE) ? "yes" : "no");/// 默认是需要群主申请

        NewActivityService newActivityService = RetrofitFactory.getInstance().create(getmContext(),activityBean,NewActivityService.class);
        Callback<CommonRespBean<ActivityBean>> callback = new Callback<CommonRespBean<ActivityBean>>() {
            @Override
            public void success(CommonRespBean<ActivityBean> commonPostResultBean, Response response) {
                if (commonPostResultBean != null && commonPostResultBean.getStatus().equals("yes") && commonPostResultBean.getData() != null) {
                    infoEntity = commonPostResultBean.getData();
                    //获取tokenId
                    if (BitmapHelper.imageListStorage.size() != 0) {
                        mLdDialog.setMessage("正在上传图片");
                        getToken();
                    } else {
                        mLdDialog.dismiss();
                        Intent intent = new Intent(getmContext(), NewFriendsInviteActivity.class);
                        intent.putExtra("isPublishActivity", true);
                        intent.putExtra("groupId", infoEntity.getEmobGroupId());
                        intent.putExtra("groupowner", infoEntity.getEmobIdOwner());

                        startActivity(intent);
                        showToast("已发起活动");
                        setResult(RESULT_OK);
                        finish();
                    }

                    //上传图片
                } else {
                    mLdDialog.dismiss();
                    Toast.makeText(NewActivityActivity.this, "申请活动失败" + commonPostResultBean.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                showNetErrorToast();
            }
        };



        newActivityService.newAcitivy(activityBean, callback);
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
                if (getTokencount-- > 0) {
                    getToken();
                } else {
                    mLdDialog.dismiss();
                    showNetErrorToast();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (getTokencount-- > 0) {
                    getToken();
                } else {
                    mLdDialog.dismiss();
                    showNetErrorToast();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.new_activity_joingroup_rlay:
            case R.id.iv_switch_on_notify:
            case R.id.iv_switch_off_notify:
                if (iv_switch_on_notify.getVisibility() == View.VISIBLE) {
                    iv_switch_on_notify.setVisibility(View.INVISIBLE);
                    iv_switch_off_notify.setVisibility(View.VISIBLE);
                } else {
                    iv_switch_on_notify.setVisibility(View.VISIBLE);
                    iv_switch_off_notify.setVisibility(View.INVISIBLE);
                }
                break;
        }


    }
}
