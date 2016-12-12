package xj.property.activity.dialog;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
import xj.property.R;
import xj.property.activity.ABaseActivity;
import xj.property.event.DialogEvent;

/**
 * 作者：asia on 2015/12/30 11:01
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：asia
 */
public class DialogActivity extends ABaseActivity implements View.OnClickListener {

    private TextView mTv_text;
    private TextView mTv_out;
    private TextView mTv_submit;
    private boolean isMoved;
    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;
    private String message;
    private int position;
    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.activity_dialog_move);
        message = getIntent().getStringExtra("message");
        position = getIntent().getIntExtra("position",0);
    }

    protected void initListenner() {
        if(!isMoved){
            mTv_out.setOnClickListener(this);
        }
        mTv_submit.setOnClickListener(this);
    }

    protected void initView() {
        mTv_text = (TextView) findViewById(R.id.tv_text);
        mTv_out = (TextView) findViewById(R.id.tv_out);
        mTv_submit = (TextView) findViewById(R.id.tv_submit);
    }

    @Override
    protected void initDate() {
        initAnima();
        if(message!=null&&!"".equals(message)){
            mTv_text.setText(message);
        }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_out:
                setResult(RESULTEXIT);
                finish();
                break;
            case R.id.tv_submit:
                DialogEvent dialog = new DialogEvent();
                dialog.setMessage(""+position);
                EventBus.getDefault().post(dialog);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void finish() {
        EventBus.getDefault().post(new DialogEvent());
        super.finish();
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }
}
