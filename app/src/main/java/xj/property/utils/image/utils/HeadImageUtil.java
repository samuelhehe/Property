package xj.property.utils.image.utils;

import android.os.Environment;
import android.widget.ImageView;

public class HeadImageUtil
{
	public void displayImage(String url,ImageView imageView)
	{
		FileHelper.fileIsExist(Environment.getExternalStorageDirectory().getAbsolutePath()+"/zzw/HeadImg/");
	}
}
