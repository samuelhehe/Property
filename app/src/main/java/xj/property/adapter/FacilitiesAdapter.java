package xj.property.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

import xj.property.R;
import xj.property.beans.CourierListBean;
import xj.property.beans.FacilitiesBean;

/**
 * Created by n on 2015/4/14.
 */
public class FacilitiesAdapter extends BaseAdapter {
    Context context;
    ArrayList<FacilitiesBean> list;
    DisplayImageOptions option_1 = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.default_picture)
            .showImageOnFail(R.drawable.default_picture)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(true).cacheOnDisk(true)
            .build();

    FacilitiesAdapter(){

    }

    public FacilitiesAdapter(Context context, ArrayList<FacilitiesBean> list){
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
    public FacilitiesBean getItem(int position) {
        return list.get(position);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final  FacilitiesBean data=list.get(position);
        ViewHolder vh = null;
        View view;
        if (convertView != null) {
            view = convertView;
            vh = (ViewHolder) view.getTag();
        } else {
            view = View.inflate(context, R.layout.item_facilities, null);
            vh=new ViewHolder();
            vh.iv_circleshop=(ImageView)view.findViewById(R.id.iv_circleshop);
            vh.tv_facilityName=(TextView)view.findViewById(R.id.tv_facilityName);
            vh.tv_distance=(TextView)view.findViewById(R.id.tv_distance);
            vh.tv_businessStartTime=(TextView)view.findViewById(R.id.tv_businessStartTime);
            vh.tv_businessEndTime=(TextView)view.findViewById(R.id.tv_businessEndTime);
            view.setTag(vh);
        }
        if (data.getLogo().length()!=0){
             ImageLoader.getInstance().displayImage(data.getLogo(), vh.iv_circleshop,option_1);
        }
        vh.tv_facilityName.setText(data.getFacilityName());
        vh.tv_distance.setText(data.getDistance()+"m");
        vh.tv_businessStartTime.setText(data.getBusinessStartTime()+"");
        vh.tv_businessEndTime.setText(data.getBusinessEndTime()+"");
        return view;
    }

    public static class ViewHolder {
        public   ImageView iv_circleshop;
        public   TextView tv_facilityName;
        public   TextView tv_distance;
        public  TextView tv_businessStartTime;
        public  TextView tv_businessEndTime;
    }


}
