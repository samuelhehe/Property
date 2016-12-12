package xj.property.utils.image.utils;

import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import xj.property.utils.network.NetworkUtils;

public class ImageLoadAsyncTask extends AsyncTask<String, Void, Bitmap>
{

	private String curImageUrl;
	public static HashMap<String, Bitmap> imageCache = new HashMap<String, Bitmap>();

	private ImageCallback imageCallback;

	public ImageLoadAsyncTask(ImageCallback callback)
	{
		this.imageCallback = callback;
	}

	@Override
	protected Bitmap doInBackground(String... params)
	{
		Bitmap bitmap = null;
		curImageUrl = params[0];
		byte[] imgByte = NetworkUtils.getBytes(params[0]);
		if (imgByte.length > 0)
		{
			bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
		}

		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result)
	{
		// this.mImageView.setImageBitmap(result);
		imageCache.put(curImageUrl, result);
		imageCallback.sendImage(curImageUrl, result);
	}

	// ͼƬ������ɺ�ص��ӿ�
	public interface ImageCallback
	{
		/**
		 * ��Activity������������ɵ�ͼƬ��Ϣ
		 * 
		 * @param url
		 *            ͼƬ�������ַ
		 * @param result
		 *            ͼƬ��Bitmap����
		 */
		public void sendImage(String url, Bitmap result);
	}
}
