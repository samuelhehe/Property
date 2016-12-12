package xj.property.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


import java.util.List;
import java.util.Map;

import xj.property.R;

/**
 * Created by maxwell on 15/1/8.
 */
public class TakeoutMenuAdapter extends BaseAdapter{
    /**
     * logger
     */
    final private int TYPE_DATA = 0;
    final private int TYPE_LOADING = 1;

    private Context mContext;
    private List<Map<Integer,String>> dataList;
    private RequestQueue requestQueue;

    // public LoaderAdapter(int count, Context context, String[] url) {
    public TakeoutMenuAdapter(Context context, List<Map<Integer,String>> dataList)
    {
        this.mContext = context;
        this.dataList = dataList;
        requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public int getCount()
    {
        // loading界面
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
    public int getItemViewType(int position)
    {
        if (position == dataList.size())
        {
            return TYPE_LOADING;
        } else
        {
            return TYPE_DATA;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder = null;
        int type = getItemViewType(position);
        if (convertView == null)
        {
            switch (type)
            {
                case TYPE_DATA:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_takeout_menu, parent, false);
                    viewHolder = new ViewHolder();
                    viewHolder.tv_takeout_menu = (TextView) convertView.findViewById(R.id.tv_takeout_menu_item);
                    convertView.setTag(viewHolder);
                    break;
            }
        } else
        {
            switch (type)
            {
                case TYPE_DATA:
                    viewHolder = (ViewHolder) convertView.getTag();
                    break;
            }
        }

        // 设置资源
        switch (type)
        {
            case TYPE_DATA:
                String valueMenu = "";
                for(Integer key : dataList.get(position).keySet()){
                    valueMenu = dataList.get(position).get(key);
                }
                viewHolder.tv_takeout_menu.setText(valueMenu);
                break;
        }
        return convertView;
    }

    class ViewHolder
    {
        public TextView tv_takeout_menu;
    }

}
