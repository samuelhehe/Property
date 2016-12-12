package xj.property.activity.cooperation;

import android.os.Bundle;
import android.view.View;

import xj.property.R;
import xj.property.XjApplication;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.utils.other.PreferencesUtil;

/**
 * 邻居帮操作提示页
 */
public class CooperationNoticesPagerActivity extends HXBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooperation_optnotices_pager);
        findViewById(R.id.act_opt_notice_iv).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_opt_notice_iv:
                PreferencesUtil.saveIsUnReadCooperationIndex(XjApplication.getInstance(), false);

                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PreferencesUtil.saveIsUnReadCooperationIndex(XjApplication.getInstance(), false);
    }
}
