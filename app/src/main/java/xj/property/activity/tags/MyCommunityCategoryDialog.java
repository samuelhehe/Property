package xj.property.activity.tags;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.beans.CommunityCategoryDataRespBean;
import xj.property.beans.SysDefaultTagsBean;
import xj.property.beans.TagsA2BAddReqBean;
import xj.property.beans.TagsA2BAddRespBean;
import xj.property.beans.TagsA2BRespBean;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

public class MyCommunityCategoryDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = "MyCommunityCategoryDialog";
    private final LayoutInflater layoutInflater;

    private List<CommunityCategoryDataRespBean.InfoEntity> info = new ArrayList<>();
    private Context mContext;

    private TextView myc_cname_tv;
    private ImageView myc_category_close_iv;
    private TextView myc_cnames_show_tv;
    private RelativeLayout community_category_rlay;

    public MyCommunityCategoryDialog(Context context, List<CommunityCategoryDataRespBean.InfoEntity> info) {
        super(context, R.style.Dialog_Fullscreen_forCategory);
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

    private void initData() {

        String cname = PreferencesUtil.getCommityName(this.getContext());
        if (TextUtils.isEmpty(cname)) {
            myc_cname_tv.setText("帮帮小区");
        } else {
            myc_cname_tv.setText(cname);
        }
        if (info != null && info.size() > 0) {

            StringBuilder sb = new StringBuilder();
            for (CommunityCategoryDataRespBean.InfoEntity infoEntity : info) {
                sb.append(infoEntity.getRange() + "位" + infoEntity.getLabelContent()).append(";");
            }
            if(!TextUtils.isEmpty(sb.toString())){
                StringBuilder finalSb = new StringBuilder(sb.subSequence(0, sb.length() - 1));
                finalSb.append("...");
                myc_cnames_show_tv.setText(finalSb.toString());
            }else{
                myc_cnames_show_tv.setText("");
            }
        }


    }

    private void initView() {

        community_category_rlay = (RelativeLayout)findViewById(R.id.community_category_rlay);
        community_category_rlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        myc_cname_tv = (TextView) findViewById(R.id.myc_cname_tv);
        myc_cnames_show_tv = (TextView) findViewById(R.id.myc_cnames_show_tv);

        myc_category_close_iv = (ImageView) findViewById(R.id.myc_category_close_iv);
        myc_category_close_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


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
