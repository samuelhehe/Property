package xj.property.activity.fitmentfinish;

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
public class FitmentDialogActivity extends HXBaseActivity {

    private final int MESSAGE= 88;
    private final int CLOSEDIALOG = 66;

    private EditText mEt_success;
    private EditText mEt_message;
    private TextView mTv_out;
    private TextView mTv_submit;

    private UserInfoDetailBean userbean;

    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitment_dialog);
        userbean = PreferencesUtil.getLoginInfo(getApplicationContext());//TODO 默认显示当前用户的地址
        initView();
        initAnima();
        initListenner();
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
                setResult(CLOSEDIALOG, intent);
                finish();
                break;
            case R.id.tv_submit:
                if("".equals(mEt_success.getText().toString().trim())){
                    Toast.makeText(getApplicationContext(), "请填验证码", Toast.LENGTH_SHORT).show();
                }else if("".equals(mEt_message.getText().toString().trim())){
                    Toast.makeText(getApplicationContext(), "请填写评论信息", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intentMessage = new Intent();
                    intentMessage.putExtra("success", mEt_message.getText().toString());
                    intentMessage.putExtra("message",mEt_message.getText().toString());
                    setResult(MESSAGE, intentMessage);
                    finish();
                }
                break;

        }
    }

    public void initView() {
        mEt_success = (EditText) findViewById(R.id.et_success);
        mEt_message = (EditText) findViewById(R.id.et_message);
        mTv_out = (TextView) findViewById(R.id.tv_out);
        mTv_submit = (TextView) findViewById(R.id.tv_submit);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Intent intent = new Intent();
            setResult(CLOSEDIALOG, intent);
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
