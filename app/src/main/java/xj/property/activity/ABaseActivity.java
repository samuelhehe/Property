package xj.property.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;

import xj.property.utils.XJPushManager;
import xj.property.widget.LoadingDialog;

/**
 * 作者：asia on 2016/1/11 13:00
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public abstract class ABaseActivity extends Activity {
    protected final int RESULTSUBMIT=88;
    protected final int RESULTEXIT=66;
    protected LoadingDialog mLdDialog = null;
    protected XJPushManager xjpushManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        xjpushManager = new XJPushManager(this);

        InitDialog();
        init(savedInstanceState);
        initView();
        initDate();
        initListenner();
    }
    private void InitDialog() {
        mLdDialog = new LoadingDialog(this);
        mLdDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                dialog.cancel();
                return false;
            }
        });
    }
    protected abstract void init(Bundle savedInstanceState);
    protected abstract void initView();
    protected abstract void initDate();
    protected abstract void initListenner();
}
