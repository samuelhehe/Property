package xj.property.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import xj.property.R;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.activity.doorpaste.DoorPasteDetailActivity;
import xj.property.beans.DoorPasteDetailsBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.utils.SmileUtils;
import xj.property.utils.other.PreferencesUtil;

/**
 *
 */
public class DoorPasteDetailAdapter extends BaseAdapter {
    private DoorPasteDetailActivity doorpaste;
    List<DoorPasteDetailsBean> list;
    Context context;
    SimpleDateFormat format = new SimpleDateFormat("MM-dd");

    public DoorPasteDetailAdapter(Context context, List<DoorPasteDetailsBean> list, DoorPasteDetailActivity doorPasteDetailActivity) {
        if (list != null)
            this.list = list;
        this.context = context;
        this.doorpaste = doorPasteDetailActivity;
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
    public DoorPasteDetailsBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final DoorPasteDetailsBean itembean = list.get(position);
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.common_doorpaste_details_item, null);
            vh = new ViewHolder(convertView);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Spannable spanAll = SmileUtils.getSmiledText(context, itembean.getContent());
        vh.doorpaste_details_content_tv.setText(spanAll, TextView.BufferType.SPANNABLE);
        Date tag_time_date = new Date(itembean.getCreateTime() * 1000L);

        vh.doorpaste_details_time_tv.setText(format.format(tag_time_date) +" "+ getWeekOfDate(tag_time_date));
        vh.doorpaste_details_addnum_tv.setText("" + itembean.getTimes());
        vh.doorpaste_details_addnum_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoDetailBean bean = PreferencesUtil.getLoginInfo(context);
                if (bean != null) {
                    doorpaste.doAddPaste(itembean.getDoorId(), itembean.getDoorStickerId());
                } else {
                    Intent intent = new Intent(context, RegisterLoginActivity.class);
                    context.startActivity(intent);
                }
            }
        });


        return convertView;
    }


    /**
     * 获取指定日期是周几
     * 参数为null时表示获取当前日期是周几
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekOfDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekOfDays[w];
    }

    public class ViewHolder {
        TextView doorpaste_details_content_tv;
        TextView doorpaste_details_time_tv;
        TextView doorpaste_details_addnum_tv;

        ViewHolder(View v) {
            doorpaste_details_content_tv = (TextView) v.findViewById(R.id.doorpaste_details_content_tv);
            doorpaste_details_time_tv = (TextView) v.findViewById(R.id.doorpaste_details_time_tv);
            doorpaste_details_addnum_tv = (TextView) v.findViewById(R.id.doorpaste_details_addnum_tv);
            v.setTag(this);
        }

    }
}
