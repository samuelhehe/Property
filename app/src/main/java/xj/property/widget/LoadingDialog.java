package xj.property.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import xj.property.R;
import xj.property.utils.other.BaseUtils;


/**
 * 正在加载提示图
 * @author liyao
 */
public class LoadingDialog extends ProgressDialog {
	private Context mContext;
	private LayoutInflater mLayoutInflater;

	private String mMessage = "";

	public LoadingDialog(Context context) {
		super(context);
		mContext = context;
		mLayoutInflater = LayoutInflater.from(context);

		setProgressStyle(ProgressDialog.STYLE_SPINNER);
		setMessage(mContext.getString(R.string.loading_title));
		setIndeterminate(true);
		setCancelable(false);
	}

	public void setMessage(String msg) {
		mMessage = msg;
	}

	public void show() {
		super.show();

        FrameLayout layout = (FrameLayout) mLayoutInflater.inflate(
				R.layout.loading_dialog, null);
		this.setContentView(layout);
		// WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		// lp.alpha = 0.8f; // 0.0-1.0
		// this.getWindow().setAttributes(lp);

		ImageView iv = (ImageView) layout
				.findViewById(R.id.progress_image_loading);
		BaseUtils.setLoadingImageAnimation(iv);

//		TextView tv = (TextView) layout.findViewById(R.id.loading_message);
//		tv.setText(mMessage);
	}
}
