package xj.property.activity.vote;

import android.os.Bundle;
import android.view.View;

import xj.property.R;
import xj.property.XjApplication;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.utils.other.PreferencesUtil;

/**
 * vote操作提示页
 */
public class VoteNoticesPagerActivity extends HXBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_optnotices_pager);
        findViewById(R.id.act_opt_notice_iv).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_opt_notice_iv:
                PreferencesUtil.saveIsUnReadVoteIndex(XjApplication.getInstance(), false);
                finish();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PreferencesUtil.saveIsUnReadVoteIndex(XjApplication.getInstance(), false);
    }
}
