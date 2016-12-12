package xj.property.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

import xj.property.R;
import xj.property.activity.property.PropertyPayDetailsActivity;
import xj.property.beans.PropertyPayHistoryBean;
import xj.property.utils.TimeUtils;

/**
 * Created by asia on 15/11/20.
 */
public class PropertyHistoryAdapter extends BaseAdapter {

    private Context mContext;

    private List<PropertyPayHistoryBean> mlist;

    private DisplayImageOptions options;

    public PropertyHistoryAdapter(Context mContext, List<PropertyPayHistoryBean> mlist){

        this.mContext = mContext;
        this.mlist = mlist;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_picture)
                .showImageForEmptyUri(R.drawable.default_picture)
                .showImageOnFail(R.drawable.default_picture)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    public void ChangeRefresh(List<PropertyPayHistoryBean> list){
        mlist.clear();
        mlist.addAll(list);
        notifyDataSetChanged();
    }

    public void LoadMoreRefresh(List<PropertyPayHistoryBean> list){
        mlist.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = ((Activity) (mContext)).getLayoutInflater()
                    .inflate(R.layout.item_property_history, parent, false);
            holder.mTv_pay_time_long = (TextView) convertView.findViewById(R.id.tv_pay_time_long);
            holder.mTv_pay_num = (TextView) convertView.findViewById(R.id.tv_pay_num);
            holder.mTv_pay_time = (TextView) convertView.findViewById(R.id.tv_pay_time);
            holder.mTv_address = (TextView) convertView.findViewById(R.id.tv_address);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final PropertyPayHistoryBean propertyPayHistoryBean = mlist.get(position);
        holder.mTv_pay_time_long.setText("已缴费"+propertyPayHistoryBean.getUnitCount()+"个月");

        holder.mTv_pay_time.setText("交费时间："+TimeUtils.fromLongToString2(propertyPayHistoryBean.getCreateTime()+""));

        holder.mTv_address.setText(propertyPayHistoryBean.getAdress());

        holder.mTv_pay_num.setText(propertyPayHistoryBean.getPaymentPrice()+"");
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPayedActivity = new Intent(mContext,PropertyPayDetailsActivity.class);
                intentPayedActivity.putExtra("paymentId",propertyPayHistoryBean.getPaymentId());
                intentPayedActivity.putExtra("type","history");
                intentPayedActivity.putExtra("paymentId",propertyPayHistoryBean.getPaymentId());
                mContext.startActivity(intentPayedActivity);
            }
        });
        return convertView;
    }

    private static final class ViewHolder {
        private TextView mTv_pay_time_long;
        private TextView mTv_pay_num;
        private TextView mTv_pay_time;
        private TextView mTv_address;
    }

}
