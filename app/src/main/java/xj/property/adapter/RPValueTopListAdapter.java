package xj.property.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import xj.property.R;
import xj.property.beans.ActivityBean;
import xj.property.beans.RPListResult;
import xj.property.beans.RPTopListItem;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.UserUtils;

/**
 * Created by maxwell on 15/1/8.
 */
public class RPValueTopListAdapter extends BaseAdapter {

    private Context mContext;
    private List<RPTopListItem> dataList;
    ListView listView;
   int colorYellow=0xfffca700;
   int colorGreen=0xff30df38;
   int colorBlack=0xff333333;

    public RPValueTopListAdapter(Context context, ListView listView, List<RPTopListItem> dataList) {
        this.mContext = context;
        this.dataList = dataList;
        this.listView=listView;
    }

    @Override
    public int getCount() {
        // loading界面
        return dataList.size();
    }

    @Override
    public RPTopListItem getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        RPTopListItem bean = dataList.get(position);
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.rpvalue_top_list_item, null);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
  ImageLoader.getInstance().displayImage(bean.getAvatar(), viewHolder.iv_avatar, UserUtils.options);
        viewHolder.txt_week_rpvalue.setText(bean.getPraiseSum() + "");
        viewHolder.txt_all_rpvalue.setText("("+bean.getCharacterValues()+")");
        viewHolder.txt_my_name.setText(bean.getNickname());
        viewHolder.txt_top_num.setText("NO."+(dataList.indexOf(bean)+1));
            switch (dataList.indexOf(bean)){
                case 0:
                    viewHolder.txt_my_name.setTextColor(colorYellow);
                    viewHolder.txt_top_num.setTextColor(colorYellow);
                    viewHolder.  img_emblem.setVisibility(View.VISIBLE);
                    viewHolder.  img_emblem.setImageResource(R.drawable.the_gold_medal);
                    break;
                case 1:
                    viewHolder.txt_my_name.setTextColor(colorYellow);
                    viewHolder.txt_top_num.setTextColor(colorYellow);
                    viewHolder.  img_emblem.setVisibility(View.VISIBLE);
                    viewHolder.  img_emblem.setImageResource(R.drawable.the_silver_medal);
                    break;
                case 2:
                    viewHolder.txt_my_name.setTextColor(colorYellow);
                    viewHolder.txt_top_num.setTextColor(colorYellow);
                    viewHolder.  img_emblem.setVisibility(View.VISIBLE);
                    viewHolder.  img_emblem.setImageResource(R.drawable.the_bronze_medal);
                    break;
                default:
                    viewHolder.txt_my_name.setTextColor(colorBlack);
                    viewHolder.txt_top_num.setTextColor(colorGreen);
                    viewHolder.  img_emblem.setVisibility(View.GONE);
                    break;
          }
        return convertView;
    }

    class ViewHolder {
        public ImageView iv_avatar;
        public TextView txt_top_num;
        public TextView txt_my_name;
        public TextView txt_week_rpvalue;
        public TextView txt_all_rpvalue;
        public ImageView img_emblem;

        ViewHolder(View v) {
            iv_avatar = (ImageView) v.findViewById(R.id.iv_avatar);
            txt_top_num = (TextView) v.findViewById(R.id.txt_top_num);
            txt_my_name = (TextView) v.findViewById(R.id.txt_my_name);
            txt_week_rpvalue = (TextView) v.findViewById(R.id.txt_week_rpvalue);
            txt_all_rpvalue = (TextView) v.findViewById(R.id.txt_all_rpvalue);
            img_emblem = (ImageView) v.findViewById(R.id.img_emblem);
            v.setTag(this);
        }
    }

}
