package xj.property.activity.fitmentfinish;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;


/**
 * 作者：asia on 2015/12/11 11:56
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class
        FitMentFinishSchemeIndexActivity extends HXBaseActivity {

    private String mTag = "FitMentFinishSchemeIndexActivity";

    private RelativeLayout mTl_experience1;
    private RelativeLayout mTl_experience2;
    private RelativeLayout mTl_experience3;
    private RelativeLayout mTl_experience4;
    private RelativeLayout mTl_experience5;
    private RelativeLayout mTl_experience6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_fitment_scheme_index_header);
        initTitle();
        initView();
        initListenner();
    }

    private void initListenner() {
        mTl_experience1.setOnClickListener(this);
        mTl_experience2.setOnClickListener(this);
        mTl_experience3.setOnClickListener(this);
        mTl_experience4.setOnClickListener(this);
        mTl_experience5.setOnClickListener(this);
        mTl_experience6.setOnClickListener(this);
    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        this.findViewById(R.id.iv_back).setOnClickListener(this);
       ((TextView) this.findViewById(R.id.tv_title)).setText("装修经验之谈");
    }

    private void initView() {
        mTl_experience1 = (RelativeLayout) findViewById(R.id.tl_experience1);
        mTl_experience2 = (RelativeLayout) findViewById(R.id.tl_experience2);
        mTl_experience3 = (RelativeLayout) findViewById(R.id.tl_experience3);
        mTl_experience4 = (RelativeLayout) findViewById(R.id.tl_experience4);
        mTl_experience5 = (RelativeLayout) findViewById(R.id.tl_experience5);
        mTl_experience6 = (RelativeLayout) findViewById(R.id.tl_experience6);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.rl_index:
                startActivity(new Intent(this, FitMentFinishSchemeActivity.class));
                break;

            case R.id.iv_back:
                finish();
                break;

            case R.id.tl_experience1:
                Intent intent1 = new Intent(this, FitMentFinishSchemeActivity.class);
                intent1.putExtra("url","/jsp/app/fitment/fitment_experience_detail.jsp");
                startActivity(intent1);
                break;
            case R.id.tl_experience2:
                Intent intent2 = new Intent(this, FitMentFinishSchemeActivity.class);
                intent2.putExtra("url","/jsp/app/fitment/fitment_experience_detail1.jsp");
                startActivity(intent2);
                break;
            case R.id.tl_experience3:
                Intent intent3 = new Intent(this, FitMentFinishSchemeActivity.class);
                intent3.putExtra("url","/jsp/app/fitment/fitment_experience_detail2.jsp");
                startActivity(intent3);
                break;
            case R.id.tl_experience4:
                Intent intent4 = new Intent(this, FitMentFinishSchemeActivity.class);
                intent4.putExtra("url","/jsp/app/fitment/fitment_experience_detail3.jsp");
                startActivity(intent4);
                break;
            case R.id.tl_experience5:
                Intent intent5 = new Intent(this, FitMentFinishSchemeActivity.class);
                intent5.putExtra("url","/jsp/app/fitment/fitment_experience_detail4.jsp");
                startActivity(intent5);
                break;
            case R.id.tl_experience6:
                Intent intent6 = new Intent(this, FitMentFinishSchemeActivity.class);
                intent6.putExtra("url","/jsp/app/fitment/fitment_experience_detail5.jsp");
                startActivity(intent6);
                break;
        }

    }
}
