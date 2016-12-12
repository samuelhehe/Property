package xj.property.activity.activities;

import android.os.Bundle;
import android.view.View;

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.utils.other.PreferencesUtil;

/**
 * 活动操作提示页
 */
public class ActivitiesNoticesPagerActivity extends HXBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_optnotices_pager);
        findViewById(R.id.act_opt_notice_iv).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_opt_notice_iv:

                PreferencesUtil.saveFirstUseActivities(getmContext(),false);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PreferencesUtil.saveFirstUseActivities(getmContext(),false);
    }
}
