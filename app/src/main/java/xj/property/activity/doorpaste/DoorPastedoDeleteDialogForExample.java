package xj.property.activity.doorpaste;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import xj.property.R;
import xj.property.utils.other.PreferencesUtil;


/**
 * 摘门贴
 */
public class DoorPastedoDeleteDialogForExample extends Dialog implements View.OnClickListener {

    private static final String TAG = "DoorPastedoDeleteDialog";
    private final LayoutInflater layoutInflater;
    private final Integer userCharacterVaules;
    private final int totalpasteTimes;
    private final boolean isexample;

    private Context mContext;

    private TextView dialog_doorpaste_del_notice_tv;
    private TextView dialog_doorpaste_del_chatvalue_tv;
    private TextView tv_cancel;
    private TextView tv_ok;
    private TextView dialog_doorpaste_del_title_tv;
    private LinearLayout share_rootview;

    public interface DelListener {

        void doOK(String type);

        void doCancel();
    }

    private DelListener delListener;

    public DoorPastedoDeleteDialogForExample(Context context, int totalpasteTimes, boolean isexample, DelListener delListener) {
        super(context, R.style.Dialog_Fullscreen_fordoorpasteremove);
        this.mContext = context;
        layoutInflater = LayoutInflater.from(context);
        this.delListener = delListener;
        this.totalpasteTimes = totalpasteTimes;
        this.isexample = isexample;
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
        dialog_doorpaste_del_notice_tv.setText("此贴只为示例贴，不会消耗你的人品值。");
        if (tv_cancel != null) {
            tv_cancel.setVisibility(View.GONE);
        }
        dialog_doorpaste_del_chatvalue_tv.setVisibility(View.GONE);
        dialog_doorpaste_del_title_tv.setVisibility(View.GONE);
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
                if (delListener != null) {
                    delListener.doCancel();
                }
                dismiss();
                break;

            case R.id.tv_ok:
                if (delListener != null) {
                    if (totalpasteTimes > userCharacterVaules) {
                        delListener.doOK("no");
                    } else if (isexample) {
                        delListener.doOK("example");
                    } else {
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
