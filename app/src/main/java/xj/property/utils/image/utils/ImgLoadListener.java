package xj.property.utils.image.utils;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import xj.property.utils.other.BaseUtils;

public class ImgLoadListener implements ImageLoadingListener {

	public void onLoadingStarted(String imageUri, View view) {
		Log.i("onion", "Started");
		if (view != null && view instanceof ImageView)
			BaseUtils.setLoadingImageAnimation((ImageView) view);
	}

	public void onLoadingFailed(String imageUri, View view,
			FailReason failReason) {
		Log.i("onion", "Failed");
		view.clearAnimation();
	}

	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		view.clearAnimation();
		LayoutParams params = view.getLayoutParams();
		params.height = (int)ImageUtils.getPicHeight(loadedImage.getWidth(), loadedImage.getHeight(),params.width);
		if (view instanceof ImageView) {
			ImageView iv = (ImageView) view;
			iv.setScaleType(ScaleType.FIT_XY);
		}
		view.setLayoutParams(params);
		Log.i("onion", loadedImage.toString()+"/"+loadedImage.getHeight()+"/"+loadedImage.getWidth()+ "Complete"+params.height);
	}

	public void onLoadingCancelled(String imageUri, View view) {
		Log.i("onion", "Cancell");
		if (view != null)
			view.clearAnimation();
	}

}
