package xj.property.activity.property;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.UserInfoDetailBean;
import xj.property.utils.other.PreferencesUtil;

/**
 * aurth:asia
 * 缴费住址输入Dialog页面
 */
public class PropertyDialogActivity extends HXBaseActivity {

    private final int CLOSEPROPERTY = 66;
    private final int PROPERTYPAY= 88;

    private EditText mEt_house;
    private EditText mEt_house_unit;
    private EditText mEt_house_num;
    private TextView mTv_out;
    private TextView mTv_submit;

    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;

    private UserInfoDetailBean userbean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_pay_cost_dialog);
        userbean = PreferencesUtil.getLoginInfo(getApplicationContext());//TODO 默认显示当前用户的地址
        initView();
        initListenner();
        initDate();
        initAnima();
    }

    private void initDate() {
        if(userbean!=null &&userbean.getUserUnit()!=null&& !userbean.getUserUnit().equals("")){
            mEt_house_unit.setText(userbean.getUserUnit());
        }
        if(userbean!=null &&userbean.getUserFloor()!=null&& !userbean.getUserFloor().equals("")){
            mEt_house.setText(userbean.getUserFloor());
        }
        if(userbean!=null &&userbean.getUserFloor()!=null&& !userbean.getUserFloor().equals("")){
            mEt_house_num.setText(userbean.getRoom());
        }
    }


    private void initListenner() {
        mTv_out.setOnClickListener(this);
        mTv_submit.setOnClickListener(this);
    }

    private void initAnima() {
        TypedArray activityStyle = getTheme().obtainStyledAttributes(new int[] {android.R.attr.windowAnimationStyle});
        int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);
        activityStyle.recycle();
        activityStyle = getTheme().obtainStyledAttributes(windowAnimationStyleResId, new int[] {android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation});
        activityCloseEnterAnimation = activityStyle.getResourceId(0, 0);
        activityCloseExitAnimation = activityStyle.getResourceId(1, 0);
        activityStyle.recycle();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_out:
                Intent intent = new Intent();
                setResult(CLOSEPROPERTY, intent);
                finish();
                break;
            case R.id.tv_submit:
                if("".equals(mEt_house.getText().toString().trim())){
                    Toast.makeText(getApplicationContext(), "请填写楼号", Toast.LENGTH_SHORT).show();
                }else if("".equals(mEt_house_num.getText().toString().trim())){
                    Toast.makeText(getApplicationContext(), "请填写房间号", Toast.LENGTH_SHORT).show();
                }else{
                    userbean.setUserUnit( mEt_house_unit.getText().toString());
                    userbean.setUserFloor(mEt_house.getText().toString());
                    userbean.setRoom(mEt_house_num.getText().toString());
                    PreferencesUtil.saveLogin(getApplicationContext(),userbean);

                    Intent intentMessage = new Intent();
                    intentMessage.putExtra("unit", mEt_house_unit.getText().toString());
                    intentMessage.putExtra("floor",mEt_house.getText().toString());
                    intentMessage.putExtra("room",mEt_house_num.getText().toString());
                    setResult(PROPERTYPAY, intentMessage);
                    finish();
                }
                break;

        }
    }

    public void initView() {
        mEt_house = (EditText) findViewById(R.id.et_house);
        mEt_house_unit = (EditText) findViewById(R.id.et_house_unit);
        mEt_house_num = (EditText) findViewById(R.id.et_house_num);
        mTv_out = (TextView) findViewById(R.id.tv_out);
        mTv_submit = (TextView) findViewById(R.id.tv_submit);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Intent intent = new Intent();
            setResult(CLOSEPROPERTY, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }

}
