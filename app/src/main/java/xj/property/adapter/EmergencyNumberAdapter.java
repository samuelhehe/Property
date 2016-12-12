package xj.property.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xj.property.R;
import xj.property.beans.SendWaterListBean;

/**
 * Created by n on 2015/4/14.
 */
public class EmergencyNumberAdapter extends BaseAdapter {
    Context context;
    List<SendWaterListBean.DataEntity> list;
    public   EmergencyNumberAdapter(){

    }

    public EmergencyNumberAdapter(Context context, ArrayList<SendWaterListBean.DataEntity> list){
        if (list != null)
            this.list = list;
        this.context = context;

    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public SendWaterListBean.DataEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final  SendWaterListBean.DataEntity data=list.get(position);
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_water_station, null);
            vh = new ViewHolder(convertView);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.water_station_name.setText(data.getName());
        vh.water_station_phone.setText(data.getPhone());
        vh.water_station_time.setVisibility(View.INVISIBLE);
        return convertView;
    }

    public static class ViewHolder {
        TextView water_station_name;
        TextView water_station_phone;
        TextView water_station_time;

        ViewHolder(View v) {
            water_station_name=(TextView)v.findViewById(R.id.water_station_name);
            water_station_phone=(TextView)v.findViewById(R.id.water_station_phone);
            water_station_time=(TextView)v.findViewById(R.id.water_station_time);
            v.setTag(this);
        }

    }


}
