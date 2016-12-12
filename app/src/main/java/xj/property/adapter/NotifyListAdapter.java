package xj.property.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import xj.property.R;
import xj.property.cache.XJNotify;
import xj.property.utils.image.utils.StrUtils;

/**
 * Created by Administrator on 2015/4/1.
 */
public class NotifyListAdapter extends BaseAdapter {
    List<XJNotify> notifies;
    Context context;

    public NotifyListAdapter(List<XJNotify> notifies, Context context) {
        this.notifies = notifies;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_notify, null);
            vh = new ViewHolder(convertView);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        XJNotify notify = notifies.get(position);
        vh.title.setText(notify.title);
        vh.time.setText(StrUtils.getTime4Millions(notify.timestamp * 1000L));
        vh.content.setText(notify.content);
        if (notify.getRead_status().equals("yes")) {
            vh.ivIsRead.setVisibility(View.GONE);
        } else {
            vh.ivIsRead.setVisibility(View.VISIBLE);
        }
        if (position==notifies.size()-1){
//            vh.line_view.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return notifies.size();
    }


    @Override
    public XJNotify getItem(int position) {
        return notifies.get(position);
    }

    class ViewHolder {
        TextView title, time, content;
        ImageView ivIsRead;
//        View line_view;

        ViewHolder(View v) {
            title = (TextView) v.findViewById(R.id.tv_mine_title);
            time = (TextView) v.findViewById(R.id.tv_time);
            content = (TextView) v.findViewById(R.id.tv_mine_content);
            ivIsRead = (ImageView) v.findViewById(R.id.iv_isread);
//            line_view=(View)v.findViewById(R.id.line_view);
            v.setTag(this);
        }
    }


}
