package xj.property.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import xj.property.R;
import xj.property.beans.LifeCircleBean;

/**
 * Created by Administrator on 2015/3/13.
 */
public class ImageAdapter extends BaseAdapter {
    private Context context;
    private List<String> lifePhotos;
  private AbsListView.LayoutParams params;
   /* DisplayImageOptions option_1 = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.head_portrait_personage)
            .showImageOnFail(R.drawable.head_portrait_personage).showImageOnLoading(R.drawable.head_portrait_personage)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(true).cacheOnDisk(true)
            .build();*/
    public ImageAdapter(Context context, List<String> lifePhotos) {
        this.context = context;
        this.lifePhotos = lifePhotos;
        params=new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,context.getResources().getDimensionPixelSize(R.dimen.item_life_photo));
    }

    public ImageAdapter(Context context, List<String> lifePhotos,Integer i) {
        this.context = context;
        this.lifePhotos = lifePhotos;
        params=new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,context.getResources().getDimensionPixelSize(R.dimen.item_life_photo_share_goods));
    }

    @Override
    public int getCount() {
        return lifePhotos.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView=View.inflate(context, R.layout.img_active,null);
            convertView.setLayoutParams(params);
//            ((ImageView)convertView).setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        ImageLoader.getInstance().displayImage(lifePhotos.get(position)+"?imageView/1/w/200/h/200",(ImageView)convertView,options);
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return lifePhotos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnFail(ru.truba.touchgallery.R.drawable.default_img)
            .showImageForEmptyUri(ru.truba.touchgallery.R.drawable.default_img)
            .showImageOnLoading(ru.truba.touchgallery.R.drawable.default_img)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();

}
