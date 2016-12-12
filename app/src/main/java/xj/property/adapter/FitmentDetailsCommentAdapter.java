package xj.property.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import xj.property.R;
import xj.property.beans.FitmentDetailsComment;
import xj.property.widget.CircleImageView;

/**
 * Created by asia on 15/11/20.
 */
public class FitmentDetailsCommentAdapter extends BaseAdapter {

    private Context mContext;

    private List<FitmentDetailsComment> mlist;

    private DisplayImageOptions options;


    public FitmentDetailsCommentAdapter(Context mContext, List<FitmentDetailsComment> decorationComment){
        this.mContext = mContext;
        this.mlist = decorationComment;
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
                    .inflate(R.layout.item_details_comment, parent, false);
            holder.iv_title = (CircleImageView) convertView.findViewById(R.id.iv_title);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_message = (TextView) convertView.findViewById(R.id.tv_message);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FitmentDetailsComment bean = mlist.get(position);
        ImageLoader.getInstance().displayImage(bean.getAvatar(), holder.iv_title, options);
        holder.tv_name.setText(bean.getNickname());
        holder.tv_message.setText(bean.getComment());
        return convertView;
    }

    private static final class ViewHolder {
        private CircleImageView iv_title;
        private TextView tv_name;
        private TextView tv_message;
    }

}
