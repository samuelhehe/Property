package xj.property.utils.image.cache;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class MyImageCache implements ImageCache
{
	private static MyImageCache imageCache;
	private Map<String, SoftReference<Bitmap>> softMap = new HashMap<String, SoftReference<Bitmap>>();
	private LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 8))
	{

		@Override
		protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue)
		{
			if (evicted)
			{
				softMap.put(key, new SoftReference<Bitmap>(oldValue));
			}
		}

		@Override
		protected int sizeOf(String key, Bitmap value)
		{
			return value.getRowBytes() * value.getHeight();
		}

	};

	private MyImageCache()
	{
	}

	public static MyImageCache getInstance()
	{
		if (imageCache == null)
		{
			imageCache = new MyImageCache();
		}
		return imageCache;
	}

	@Override
	public Bitmap getBitmap(String arg0)
	{
		Bitmap bitmap = null;
		if (lruCache.get(arg0) != null)
		{
			bitmap = lruCache.get(arg0);
		} else if (softMap.get(arg0) != null)
		{
			bitmap = softMap.get(arg0).get();
			lruCache.put(arg0, bitmap);
			softMap.remove(arg0);
		}
		return bitmap;
	}

	@Override
	public void putBitmap(String arg0, Bitmap arg1)
	{
		lruCache.put(arg0, arg1);
	}

}
