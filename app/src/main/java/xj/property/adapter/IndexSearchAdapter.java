package xj.property.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import xj.property.R;
import xj.property.beans.FacilitiesBean;
import xj.property.beans.FastShopDetailListBean;
import xj.property.beans.IndexSearchDataListBean;

/**
 * Created by n on 2015/4/14.
 */
public class IndexSearchAdapter extends BaseAdapter {
    Context context;
    List<FacilitiesBean> list;

    public IndexSearchAdapter( Context context, ArrayList<FacilitiesBean> list){
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
    public FacilitiesBean  getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FacilitiesBean data=list.get(position);
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_index_search_result, null);
            vh = new ViewHolder(convertView);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(data.getLogo(),vh.iv_logo);
        vh.tv_name.setText(data.getFacilityName());
        vh.tv_distance.setText(data.getDistance()+"");
        vh.tv_businessStartTime.setText(data.getBusinessStartTime());
        vh.businessEndTime.setText(data.getBusinessEndTime());

        return convertView;
    }

    public static class ViewHolder {
        ImageView iv_logo;
        TextView tv_name;
        TextView tv_distance;
        TextView tv_businessStartTime;
        TextView businessEndTime;
        ViewHolder(View v) {
            iv_logo=(ImageView)v.findViewById(R.id.iv_logo);
            tv_name=(TextView)v.findViewById(R.id.tv_name);
            tv_distance=(TextView)v.findViewById(R.id.tv_distance);
            tv_businessStartTime=(TextView)v.findViewById(R.id.tv_businessStartTime);
            businessEndTime=(TextView)v.findViewById(R.id.businessEndTime);
            v.setTag(this);
        }

    }


}
