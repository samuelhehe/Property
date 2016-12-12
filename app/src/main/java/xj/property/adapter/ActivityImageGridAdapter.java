package xj.property.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import xj.property.R;
import xj.property.activity.activities.BitmapHelper;

/**
 * Created by maxwell on 15/2/6.
 */
public class ActivityImageGridAdapter extends BaseAdapter {

    /**
     * logger
     */
    /**
     * inflater
     */
    private LayoutInflater inflater;
    /**
     * context
     */
    private Context context;

    /**
     * selected postion
     */
    private int selectedPosition = -1;

    private boolean shape;
    private int count = 6;

    public boolean isShape() {
        return shape;
    }

    public void setShape(boolean shape) {
        this.shape = shape;
    }

    /**
     * constructor
     *
     * @param context
     */
    public ActivityImageGridAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public ActivityImageGridAdapter(Context context, int count) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.count = count;
    }

    /**
     * number of grid cells  is 1 plus existed images
     *
     * @return
     */
    public int getCount() {
        return (BitmapHelper.bitmapListMemory.size() + 1);
    }

    public Object getItem(int arg0) {

        return null;
    }

    public long getItemId(int arg0) {

        return 0;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    /**
     * get view
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_image_activity,
                    parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView
                    .findViewById(R.id.iv_image_activity);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == BitmapHelper.bitmapListMemory.size()) {
            if (count!=9){
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        context.getResources(), R.drawable.activity_start_camera));
            }else {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        context.getResources(), R.drawable.commodity_picture_add));
            }

            if (position == count) {
                holder.image.setVisibility(View.GONE);
            }
        } else {
            holder.image.setImageBitmap(BitmapHelper.bitmapListMemory.get(position));
        }

        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
    }

}
