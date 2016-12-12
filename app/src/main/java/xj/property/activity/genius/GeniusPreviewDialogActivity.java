package xj.property.activity.genius;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.UserInfoDetailBean;
import xj.property.utils.other.PreferencesUtil;

/**
 * aurth:asia
 * 牛人提交输入Dialog页面
 */
public class GeniusPreviewDialogActivity extends HXBaseActivity {

    private final int CLOSEPROPERTY = 66;
    private final int PROPERTYPAY = 88;

    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;

    private TextView mTv_out;
    private TextView mTv_submit;
    private TextView mTv_dialog_name;

    private String genius;

    private UserInfoDetailBean userbean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genius_cost_dialog);
        genius = getIntent().getStringExtra("genius");
        userbean = PreferencesUtil.getLoginInfo(getApplicationContext());//TODO 默认显示当前用户的地址
        initView();
        if(genius!=null&&!"".equals(genius)){
            mTv_dialog_name.setText(genius);
        }
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
                setResult(CLOSEPROPERTY, intent);
                finish();
                break;
            case R.id.tv_submit:
                Intent intentMessage = new Intent();
                setResult(PROPERTYPAY, intentMessage);
                finish();
                break;

        }
    }

    public void initView() {
        mTv_out = (TextView) findViewById(R.id.tv_out);
        mTv_submit = (TextView) findViewById(R.id.tv_submit);
        mTv_dialog_name = (TextView) findViewById(R.id.tv_dialog_name);
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
