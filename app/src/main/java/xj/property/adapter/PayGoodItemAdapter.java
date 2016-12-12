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

import java.util.ArrayList;
import java.util.List;

import xj.property.R;
import xj.property.beans.CourierListBean;
import xj.property.beans.OrderDetailBean;
import xj.property.cache.OrderDetailModel;

/**
 * Created by n on 2015/4/23.
 */
public class PayGoodItemAdapter extends BaseAdapter {

    Context context;
    ArrayList<OrderDetailBean> list;
    private AbsListView.LayoutParams params;


    public PayGoodItemAdapter(Context context, ArrayList<OrderDetailBean> list){
        if (list != null)
            this.list = list;
        this.context = context;
        params=new AbsListView.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,context.getResources().getDimensionPixelSize(R.dimen.item_active_pay));

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
    public OrderDetailBean getItem(int position) {
        return list.get(position);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderDetailBean data=list.get(position);
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.welfare_item_pay_good_item, null);
            vh = new ViewHolder(convertView);
        } else {
            vh=(ViewHolder)convertView.getTag();
        }
        convertView.setLayoutParams(params);
        vh.tv_good_name.setText(data.getServiceName());
        vh.tv_single_good_count.setText("X"+data.getCount());
        vh.tv_single_sum_price.setText("￥"+data.getPrice());
        return convertView;
    }

    public static class ViewHolder {
        public TextView tv_good_name;
        public   TextView tv_single_good_count;
        public  TextView tv_single_sum_price;
        ViewHolder(View view){
            tv_good_name=(TextView)view.findViewById(R.id.tv_good_name);
            tv_single_good_count=(TextView)view.findViewById(R.id.tv_single_good_count);
            tv_single_sum_price=(TextView)view.findViewById(R.id.tv_single_sum_price);
            view.setTag(this);
        }
    }
}
