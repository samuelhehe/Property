package xj.property.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;


import java.util.List;

import xj.property.R;
import xj.property.beans.ActivityBean;
import xj.property.beans.RepairmentClassBean;
import xj.property.utils.image.cache.MyImageCache;

/**
 * Created by maxwell on 15/3/10.
 */
public class RepairmentClassAdapter extends BaseAdapter{

    /**
     * logger
     */

    private Context mContext;
    private List<RepairmentClassBean> dataList;

    public RepairmentClassAdapter(Context context, List<RepairmentClassBean> dataList)
    {
        this.mContext = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount()
    {
        return dataList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getViewTypeCount()
    {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_repairment_class, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.iv_repairment_class = (ImageView) convertView.findViewById(R.id.iv_repairment_class);
            viewHolder.tv_title_repairment_class = (TextView) convertView.findViewById(R.id.tv_title_repairment_class);
            viewHolder.tv_detail_repairment_class = (TextView) convertView.findViewById(R.id.tv_detail_repairment_class);
            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        /**
         * set values
         */
        viewHolder.tv_title_repairment_class.setText(dataList.get(position).getCatName());
        viewHolder.tv_detail_repairment_class.setText(dataList.get(position).getCatDesc());

        return convertView;
    }

    class ViewHolder
    {
        public ImageView iv_repairment_class;
        public TextView tv_title_repairment_class;
        public TextView tv_detail_repairment_class;
    }
}
