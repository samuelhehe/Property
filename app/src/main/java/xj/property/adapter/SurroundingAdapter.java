package xj.property.adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import xj.property.R;
import xj.property.beans.FacilityBean;

public class SurroundingAdapter extends BaseAdapter {

    /**
     * logger
     */
    /**
     * context
     */
    private Context context;
    /**
     * infalater
     */
    private LayoutInflater inflater;
    /**
     * image of an item
     */
    private static ImageView iv_icon;
    /**
     * name of an item
     */
    private static TextView tv_name;

    /**
     * facility bean list
     */
    private List<FacilityBean> facilityBeanList = new ArrayList<>();

    AbsListView.LayoutParams params;

    /**
     * constructor
     *
     * @param context
     */
    public SurroundingAdapter(Context context, List<FacilityBean> facilityBeanList) {
        super();
        this.context = context;
        this.facilityBeanList = facilityBeanList;
        inflater = LayoutInflater.from(context);
    }

    public SurroundingAdapter(Context context, List<FacilityBean> facilityBeanList, int width, int high) {
        super();
        this.context = context;
        this.facilityBeanList = facilityBeanList;
        inflater = LayoutInflater.from(context);
        params = new AbsListView.LayoutParams(width, high);
    }

    @Override
    public int getCount() {
        return facilityBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //render view
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_surroundings, null);
            viewHolder = new ViewHolder();
            viewHolder.iv_icon_surrounding = (ImageView) convertView.findViewById(R.id.iv_icon_surrounding);
            viewHolder.tv_name_surrounding = (TextView) convertView.findViewById(R.id.tv_name_surrounding);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        convertView.setLayoutParams(params);

        //set values
        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(facilityBeanList.get(position).getPicName(), "drawable",
                context.getPackageName());
        if (resourceId != 0)
            viewHolder.iv_icon_surrounding.setImageResource(resourceId);
        else
            viewHolder.iv_icon_surrounding.setImageResource(R.drawable.ic_launcher);
        viewHolder.tv_name_surrounding.setText(facilityBeanList.get(position).getFacilitiesClassName());
        return convertView;
    }

    /**
     * view holder
     */
    class ViewHolder {
        public ImageView iv_icon_surrounding;
        public TextView tv_name_surrounding;
    }
}
