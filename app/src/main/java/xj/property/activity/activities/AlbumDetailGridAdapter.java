package xj.property.activity.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xj.property.R;
import xj.property.activity.activities.BitmapHelper;

public class AlbumDetailGridAdapter extends BaseAdapter {
    /**
     * logger
     */

	private TextCallback textcallback = null;

	final String TAG = getClass().getSimpleName();
    /**
     * context
     */
    private Context context;

    /**
     * iamge model list
     */
	private List<ImageModel> imageModelList;

	public Map<String, String> map = new HashMap<String, String>();
    /**
     * bitmap cache
     */
	private BitmapCache cache;
    /**
     * handelr
     */
	private Handler mHandler;
    /**
     * total number of selected image
     */
	private int selectTotal = 0;

    private int imageCount=6;

	BitmapCache.ImageCallback callback = new BitmapCache.ImageCallback() {
		@Override
		public void imageLoad(ImageView imageView, Bitmap bitmap,
				Object... params) {
			if (imageView != null && bitmap != null) {
				String url = (String) params[0];
				if (url != null && url.equals((String) imageView.getTag())) {
					((ImageView) imageView).setImageBitmap(bitmap);
				} else {
					Log.e(TAG, "callback, bitmapList not match");
				}
			} else {
				Log.e(TAG, "callback, bitmapList null");
			}
		}
	};

	public static interface TextCallback {
		public void onListen(int count);
	}

	public void setTextCallback(TextCallback listener) {
		textcallback = listener;
	}

	public AlbumDetailGridAdapter(Context context,int imageCount ,List<ImageModel> list, Handler mHandler) {
		this.context = context;
		imageModelList = list;
		cache = new BitmapCache();
		this.mHandler = mHandler;
        this.imageCount=imageCount;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (imageModelList != null) {
			count = imageModelList.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	class Holder {
		private ImageView iv;
		private ImageView selected;
		private TextView text;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(context, R.layout.item_album_detail, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.image);
			holder.selected = (ImageView) convertView.findViewById(R.id.isselected);
			holder.text = (TextView) convertView.findViewById(R.id.item_image_grid_text);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		final ImageModel item = imageModelList.get(position);

		holder.iv.setTag(item.imagePath);
		cache.displayBmp(holder.iv, item.thumbnailPath, item.imagePath,callback);


		if (item.isSelected) {

			holder.selected.setImageResource(R.drawable.icon_data_select);  
			holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
		} else {
            holder.selected.setImageDrawable(null); /// 2015/12/16
            holder.text.setBackgroundColor(0x00000000);
		}

		holder.iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String path = imageModelList.get(position).imagePath;

				if ((BitmapHelper.imageListStorage.size() + selectTotal) < imageCount) {

						item.isSelected = !item.isSelected;
					if (item.isSelected) {

						holder.selected.setImageResource(R.drawable.icon_data_select);
						holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);

						selectTotal++;

						if (textcallback != null)
							textcallback.onListen(selectTotal);

						map.put(path, path);

					} else if (!item.isSelected) {

						holder.selected.setImageResource(-1);
						holder.text.setBackgroundColor(0x00000000);

                        selectTotal--;

						if (textcallback != null)
							textcallback.onListen(selectTotal);

						map.remove(path);
					}
				} else if ((BitmapHelper.imageListStorage.size() + selectTotal) >= imageCount) {
					if (item.isSelected == true) {
						item.isSelected = !item.isSelected;
						holder.selected.setImageResource(-1);
						selectTotal--;
						map.remove(path);

					} else {
						Message message = Message.obtain(mHandler, 0);
						message.sendToTarget();
					}
				}
			}

		});

		return convertView;
	}
}
