package xj.property.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import xj.property.R;

/**
 * Created by asia on 15/11/20.
 */
public class FitmentDetailsBusinessAdapter extends BaseAdapter {

    private Context mContext;

    private String[] mlist;

    private DisplayImageOptions options;


    public FitmentDetailsBusinessAdapter(Context mContext, String[] mlist){
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


    @Override
    public int getCount() {
        return mlist.length;
    }

    @Override
    public Object getItem(int position) {
        return mlist[position];
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
                    .inflate(R.layout.item_details_business, parent, false);
            holder.mIv_business = (ImageView) convertView.findViewById(R.id.iv_business);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(mlist[position], holder.mIv_business, options);
        return convertView;
    }

    private static final class ViewHolder {
        private ImageView mIv_business;
    }

}
