package xj.property.activity.genius;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;

/**
 * 作者：asia on 2015/12/17 10:51
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能： 牛人信息输入页面
 */
public class GeniusRelationActivity extends HXBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genius_relation);
        findViewById(R.id.iv_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(GeniusRelationActivity.this, GeniusApplyActivity.class);
                intent.putExtra("geniusFirst", true);
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initView() {
    }

    private void initDate() {
    }

    private void initListenner() {
    }

    @Override
    public void onClick(View v) {

    }
}
