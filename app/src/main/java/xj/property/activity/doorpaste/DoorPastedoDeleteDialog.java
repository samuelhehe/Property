package xj.property.activity.doorpaste;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
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
import xj.property.utils.other.PreferencesUtil;


/**
 * 摘门贴
 */
public class DoorPastedoDeleteDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = "DoorPastedoDeleteDialog";
    private final LayoutInflater layoutInflater;
    private final Integer userCharacterVaules;
    private final int totalpasteTimes;

    private Context mContext;

    private TextView dialog_doorpaste_del_notice_tv;
    private TextView dialog_doorpaste_del_chatvalue_tv;
    private TextView tv_cancel;
    private TextView tv_ok;
    private String type;
    private TextView dialog_doorpaste_del_title_tv;
    private LinearLayout share_rootview;

    public interface  DelListener{

        void doOK(String type);

        void doCancel();
    }
    private DelListener delListener;

    public DoorPastedoDeleteDialog(Context context, int totalpasteTimes ,  DelListener delListener) {
        super(context, R.style.Dialog_Fullscreen_fordoorpasteremove);
        this.mContext = context;
        layoutInflater = LayoutInflater.from(context);
        this.delListener = delListener;
        this.totalpasteTimes = totalpasteTimes;
        this.userCharacterVaules = Integer.valueOf(PreferencesUtil.getRPValue(context));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_doorpaste_delete);
        initView();
        initData();
    }

    private void initData() {
//        摘掉此门贴一共需要消耗26人品值，确定要摘除吗？
        if(totalpasteTimes>userCharacterVaules){
           /// 不够
            dialog_doorpaste_del_notice_tv.setText("摘掉此门贴一共需要消耗"+totalpasteTimes+"人品值，你的人品值不够，可以通过发生活圈或评论赚取人品值。");
            if(tv_cancel!=null){
                tv_cancel.setVisibility(View.GONE);
            }
        }else{
          /// 够
            dialog_doorpaste_del_notice_tv.setText("摘掉此门贴一共需要消耗"+totalpasteTimes+"人品值，确定要摘除吗？");
        }
        dialog_doorpaste_del_chatvalue_tv.setText(""+userCharacterVaules);
    }

    private void initView() {

        share_rootview = (LinearLayout) findViewById(R.id.share_rootview);
        share_rootview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        dialog_doorpaste_del_notice_tv = (TextView) findViewById(R.id.dialog_doorpaste_del_notice_tv);
        dialog_doorpaste_del_chatvalue_tv = (TextView) findViewById(R.id.dialog_doorpaste_del_chatvalue_tv);
        dialog_doorpaste_del_title_tv = (TextView) findViewById(R.id.dialog_doorpaste_del_title_tv);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_cancel:
                if(delListener!=null){
                    delListener.doCancel();
                }
                dismiss();
                break;

            case R.id.tv_ok:
                if(delListener!=null){
                    if(totalpasteTimes>userCharacterVaules){
                        delListener.doOK("no");
                    }else if(TextUtils.equals(type,"example")){
                        delListener.doOK("example");
                    }else{
                        delListener.doOK("yes");
                    }
                }
                break;
        }
    }

    @Override
    public void cancel() {
        super.cancel();
    }

}
