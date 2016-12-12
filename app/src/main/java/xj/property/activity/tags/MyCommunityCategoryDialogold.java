package xj.property.activity.tags;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xj.property.R;
import xj.property.beans.CommunityCategoryDataRespBean;

public class MyCommunityCategoryDialogold extends Dialog implements View.OnClickListener {

    private static final String TAG = "MyCommunityCategoryDialog";
    private String fromemobId;
    private String toEmobId;
    private String communityId;

    private List<CommunityCategoryDataRespBean.InfoEntity> info = new ArrayList<>();
    private Context mContext;

    private LayoutInflater layoutInflater;
    private TextView myc_cname_tv;
    private TextView mycc_top_left_tv;
    private TextView mycc_top_right_tv;
    private TextView mycc_moddle_left_tv;
    private TextView mycc_moddle_right_tv;
    private TextView mycc_bottom_left_tv;
    private TextView mycc_bottom_right_tv;
    private ImageView myc_category_close_iv;

    public MyCommunityCategoryDialogold(Context context, String communityId, String fromemobId, String toEmobId) {
        super(context, R.style.Theme_CustomDialog);
        this.mContext = context;
        layoutInflater = LayoutInflater.from(context);
        this.fromemobId = fromemobId;
        this.toEmobId = toEmobId;
        this.communityId = communityId;

    }

    public MyCommunityCategoryDialogold(Context context, List<CommunityCategoryDataRespBean.InfoEntity> info) {
        super(context, R.style.Theme_CustomDialog2);
        this.mContext = context;
        layoutInflater = LayoutInflater.from(context);
        this.info = info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mycommunity_category_mgr);
        initView();
        initData();
    }

    private void initView() {
        myc_cname_tv = (TextView) findViewById(R.id.myc_cname_tv);

        mycc_top_left_tv = (TextView) findViewById(R.id.mycc_top_left_tv);
        mycc_top_right_tv = (TextView) findViewById(R.id.mycc_top_right_tv);

        mycc_moddle_left_tv = (TextView) findViewById(R.id.mycc_moddle_left_tv);
        mycc_moddle_right_tv = (TextView) findViewById(R.id.mycc_moddle_right_tv);

        mycc_bottom_left_tv = (TextView) findViewById(R.id.mycc_bottom_left_tv);
        mycc_bottom_right_tv = (TextView) findViewById(R.id.mycc_bottom_right_tv);

        myc_category_close_iv = (ImageView) findViewById(R.id.myc_category_close_iv);
        myc_category_close_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }


    // / 初始化数据,初始化UI
    private void initData() {
//        getTagsA2BTags();
//
//        getSystemDefaultTags();


        for (int i = 0; i < info.size(); i++) {
            if (info.get(i) == null) {
                break;
            }
            switch (i) {
                case 0:
                    mycc_top_left_tv.setText(info.get(0).getRange() + "位  " + info.get(0).getLabelContent());
                    break;
                case 1:
                    mycc_top_right_tv.setText(info.get(1).getRange() + "位  " + info.get(1).getLabelContent());
                    break;
                case 2:
                    mycc_moddle_left_tv.setText(info.get(2).getRange() + "位  " + info.get(2).getLabelContent());
                    break;
                case 3:
                    mycc_moddle_right_tv.setText(info.get(3).getRange() + "位  " + info.get(3).getLabelContent());
                    break;
                case 4:
                    mycc_bottom_left_tv.setText(info.get(4).getRange() + "位  " + info.get(4).getLabelContent());
                    break;
                case 5:
                    mycc_bottom_right_tv.setText(info.get(5).getRange() + "位  " + info.get(5).getLabelContent());
                    break;
            }
        }
    }











    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.mytags_mgr_inputtags_btn:

                break;

        }
    }

    @Override
    public void cancel() {
        super.cancel();
    }

}
