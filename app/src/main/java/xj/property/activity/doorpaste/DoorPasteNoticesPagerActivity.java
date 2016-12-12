package xj.property.activity.doorpaste;

import android.os.Bundle;
import android.view.View;

import xj.property.R;
import xj.property.XjApplication;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.utils.other.PreferencesUtil;

/**
 *  门贴操作提示页
 */
public class DoorPasteNoticesPagerActivity extends HXBaseActivity implements  View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doorpaste_optnotices_pager);
        findViewById(R.id.act_opt_notice_llay).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_opt_notice_llay:
                PreferencesUtil.saveIsUnReadDoorPasteIndex(XjApplication.getInstance(), false);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PreferencesUtil.saveIsUnReadDoorPasteIndex(XjApplication.getInstance(), false);
    }
}
