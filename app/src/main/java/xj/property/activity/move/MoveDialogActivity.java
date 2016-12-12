package xj.property.activity.move;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import xj.property.R;
import xj.property.activity.ABaseActivity;

/**
 * 作者：asia on 2015/12/30 11:01
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：asia
 */
public class MoveDialogActivity extends ABaseActivity implements View.OnClickListener {

    private TextView mTv_out;
    private TextView mTv_submit;
    private boolean isMoved;
    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;

    @Override
    protected void init(Bundle savedInstanceState) {
        isMoved = getIntent().getBooleanExtra("isMoved", false);
        if(isMoved){
            setContentView(R.layout.activity_dialog_moved);
        }else{
            setContentView(R.layout.activity_dialog_move);
        }
    }

    protected void initListenner() {
        if(!isMoved){
            mTv_out.setOnClickListener(this);
        }
        mTv_submit.setOnClickListener(this);
    }

    protected void initView() {
        mTv_out = (TextView) findViewById(R.id.tv_out);
        mTv_submit = (TextView) findViewById(R.id.tv_submit);
    }

    @Override
    protected void initDate() {
        initAnima();
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
                if(isMoved){
                    setResult(RESULTEXIT);
                }else{
                    setResult(RESULTSUBMIT);
                }
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }
}
