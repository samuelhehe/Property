package xj.property.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

import xj.property.R;
import xj.property.beans.LocationVerficationRespBean;


/**
 * 作者：che on 2016/1/13 15:07
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class AddressAdapter extends BaseAdapter {

    private final String TAG = "AddressAdapter";

    private Context mContext;

    private List<LocationVerficationRespBean.InfoEntity> mlist;

    private DisplayImageOptions options;

    public AddressAdapter(Context mContext, List<LocationVerficationRespBean.InfoEntity> mlist){
        this.mContext = mContext;
        this.mlist = mlist;
    }

    public void ChangeRefresh(List<LocationVerficationRespBean.InfoEntity> list){
        mlist.clear();
        mlist.addAll(list);
        notifyDataSetChanged();
    }

    public void LoadMoreRefresh(List<LocationVerficationRespBean.InfoEntity> list){
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
                    .inflate(R.layout.item_address, parent, false);
            holder.mTv_city = (TextView) convertView.findViewById(R.id.tv_city);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final LocationVerficationRespBean.InfoEntity infoEntity = mlist.get(position);
        holder.mTv_city.setText(infoEntity.getCityName() + " " + infoEntity.getCommunityName());
        return convertView;
    }

    private static final class ViewHolder {
        private TextView mTv_city;
    }

}
