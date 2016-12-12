package xj.property.activity.runfor;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Date;

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
import xj.property.activity.LifeCircle.FriendZoneIndexActivity;
import xj.property.activity.activities.ActivityExplain;
import xj.property.activity.activities.BitmapHelper;
import xj.property.beans.CircleNewRecord;
import xj.property.beans.CommonPostResultBean;
import xj.property.beans.MottoRunForBean;
import xj.property.beans.RunForAllV3Bean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.CircleImageView;

/**
 * aurth:asia
 * 去拉票页面
 */
public class MottoRunForActivity extends HXBaseActivity {

    //titile
    private RelativeLayout mHeaptop;
    private ImageView mIv_back;
    private TextView mTv_left_text;
    private TextView mTv_title;
    private TextView mTv_right_text;
    private ImageView mIv_right;

    private CircleImageView mAvatar;
    private TextView mTv_name;
    private EditText mEt_text;
    private RadioGroup mRg_radioGroup;
    private RadioButton mRb_0;
    private RadioButton mRb_1;
    private RadioButton mRb_2;
    private RadioButton mRb_3;
    private RadioButton mRb_4;
    private TextView mTv_share;

    private RunForAllV3Bean.RunForDataV3Bean mRunForBean;

    private String emobId;

    private String inputStr;

    private UserInfoDetailBean userBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrunfor_motto);
        initView();
        userBean = PreferencesUtil.getLoginInfo(MottoRunForActivity.this);
        mRunForBean = (RunForAllV3Bean.RunForDataV3Bean) getIntent().getSerializableExtra("runForBean");
        if (userBean != null) {
            emobId = userBean.getEmobId();
        }
        initDate();
        initListenner();
    }

    private void initDate() {
        mTv_title.setText("竞选宣言");
        if (userBean != null) {
            ImageLoader.getInstance().displayImage(userBean.getAvatar(), mAvatar, options);
            mTv_name.setText(userBean.getNickname());
        }
    }

    private void initListenner() {
        mIv_back.setOnClickListener(this);

        mEt_text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mRg_radioGroup.clearCheck();
                return false;
            }
        });

        mTv_share.setOnClickListener(this);
        mRb_0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRg_radioGroup.clearCheck();
//                    mEt_text.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);
                }
            }
        });
        mEt_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRb_0.setChecked(true);
            }
        });
        mEt_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mRb_0.setChecked(true);
            }
        });
        mRg_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == mRb_1.getId()) {
                    mRb_0.setChecked(false);
                    inputStr = mRb_1.getText().toString();
                    mEt_text.setText("");
                } else if (checkedId == mRb_2.getId()) {
                    mRb_0.setChecked(false);
                    inputStr = mRb_2.getText().toString();
                    mEt_text.setText("");
                } else if (checkedId == mRb_3.getId()) {
                    mRb_0.setChecked(false);
                    inputStr = mRb_3.getText().toString();
                    mEt_text.setText("");
                } else if (checkedId == mRb_4.getId()) {
                    mRb_0.setChecked(false);
                    inputStr = mRb_4.getText().toString();
                    mEt_text.setText("");
                }

            }
        });
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right_text:
                startActivity(new Intent(this, ActivityExplain.class));
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_share:
                if (mRg_radioGroup.getCheckedRadioButtonId() == -1 && mEt_text.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "选择填写竞选宣言", Toast.LENGTH_SHORT).show();
                } else {
                    if (!mEt_text.getText().toString().trim().equals("") && mRb_0.isChecked()) {
                        inputStr = mEt_text.getText().toString();
                    }
                    getFriendZone();
                }
                break;

        }
    }

    public void initView() {
        mHeaptop = (RelativeLayout) findViewById(R.id.heaptop);
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mTv_left_text = (TextView) findViewById(R.id.tv_left_text);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mTv_right_text = (TextView) findViewById(R.id.tv_right_text);
        mIv_right = (ImageView) findViewById(R.id.iv_right);

        mAvatar = (CircleImageView) findViewById(R.id.avatar);
        mTv_name = (TextView) findViewById(R.id.tv_name);
        mEt_text = (EditText) findViewById(R.id.et_text);
        mRg_radioGroup = (RadioGroup) findViewById(R.id.rg_radioGroup);
        mRb_0 = (RadioButton) findViewById(R.id.rb_0);
        mRb_1 = (RadioButton) findViewById(R.id.rb_1);
        mRb_2 = (RadioButton) findViewById(R.id.rb_2);
        mRb_3 = (RadioButton) findViewById(R.id.rb_3);
        mRb_4 = (RadioButton) findViewById(R.id.rb_4);
        mTv_share = (TextView) findViewById(R.id.tv_share);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }


    /**
     * 拉票 ， 实际上是发布一个生活圈 type ==23
     */
    private void getFriendZone() {
        mLdDialog.show();
        CircleNewRecord circleNewRecord = new CircleNewRecord();
        circleNewRecord.setEmobId(emobId);
        circleNewRecord.setCommunityId(PreferencesUtil.getCommityId(this));
        circleNewRecord.setLifeContent(inputStr);
        circleNewRecord.setCreateGroup(0); /// 不创建群组
        circleNewRecord.setType(23); /// 帮主竞选部分 -->>> 23

        Log.i("onion", "CircleNewRecord: " + circleNewRecord.toString());
        NetBaseUtils.newCircleRecord(getmContext(), circleNewRecord, new NetBaseUtils.NetRespListener<CommonRespBean<String>>() {
            @Override
            public void successYes(CommonRespBean<String> commonRespBean, Response response) {
                mLdDialog.dismiss();
                Intent intentPush = new Intent(MottoRunForActivity.this, FriendZoneIndexActivity.class);
                startActivity(intentPush);
                finish();
                Log.d("MottoRunForActivity", response.toString());
                setResult(RESULT_OK);
                finish();
            }
            @Override
            public void successNo(CommonRespBean<String> commonRespBean, Response response) {
                mLdDialog.dismiss();
                showToast("亲，每月只能拉一次投票！");
            }
            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                showNetErrorToast();
            }
        });
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
