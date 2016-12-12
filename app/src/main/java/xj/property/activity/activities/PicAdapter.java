package xj.property.activity.activities;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import xj.property.R;
import xj.property.beans.ActivityBean;
import xj.property.beans.LifeCircleBean;
import xj.property.utils.DensityUtil;

/**
 * Created by Administrator on 2015/3/13.
 */
public class PicAdapter extends BaseAdapter {
    private Context context;
    private List<String> photos;
    private AbsListView.LayoutParams params;

    public PicAdapter(Context context, List<String> photos) {
        this.context = context;
        this.photos = photos;

        int width = DensityUtil.dip2px(context, 97f);
        params = new AbsListView.LayoutParams(width, width);
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.img_active, null);
            convertView.setLayoutParams(params);
//            ((ImageView)convertView).setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        ImageLoader.getInstance().displayImage(photos.get(position), (ImageView) convertView);
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
