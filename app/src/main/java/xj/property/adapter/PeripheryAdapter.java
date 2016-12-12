package xj.property.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import xj.property.R;
import xj.property.beans.PanicBuyingDetailBean;
import xj.property.cache.PanicBuyingItemInfo;
import xj.property.utils.other.PreferencesUtil;

/**
 * Created by n on 2015/8/10.
 */
public class PeripheryAdapter extends BaseAdapter {
    Context context;
    List<PanicBuyingDetailBean> panicBuyingDetailBeanList;
    ListView listView;
    String emobid;

    public PeripheryAdapter(Context context, ListView listView, List<PanicBuyingDetailBean> list) {

        this.panicBuyingDetailBeanList = list;
        this.context = context;
        this.listView = listView;

    }

    public void updateData(List<PanicBuyingDetailBean> panicBuyingDetailBeanList) {
        this.panicBuyingDetailBeanList = panicBuyingDetailBeanList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return panicBuyingDetailBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return panicBuyingDetailBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.fragment_periphery_listview_item, null);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        long time = ((panicBuyingDetailBeanList.get(position).getEndTime()) * 1000l) - (System.currentTimeMillis());
        Log.i("panicBuyingDetailBeanList.get(position).getEndTime()_adapter", time + "");

        if(panicBuyingDetailBeanList.get(position).getDistance()<100){
            if(panicBuyingDetailBeanList.get(position).getDistance()<=0){
                viewHolder.tv_distance.setText("0m");
            }else {
                viewHolder.tv_distance.setText("<"+panicBuyingDetailBeanList.get(position).getDistance()+"m");
            }

        }else {
            if(panicBuyingDetailBeanList.get(position).getDistance()>=1000){
                viewHolder.tv_distance.setText(""+(float)panicBuyingDetailBeanList.get(position).getDistance()/1000+"km");
            }else {
                viewHolder.tv_distance.setText(""+panicBuyingDetailBeanList.get(position).getDistance()+"m");
            }
        }


        if (time < 0) {
            //抢购已过期
            viewHolder.tv_title_of_panic_buying.setText("" + panicBuyingDetailBeanList.get(position).getTitle());
            viewHolder.tv_resttime_of_panic_buying.setText("限时抢购已结束！");
            viewHolder.tv_resttime_of_panic_buying.setTextColor(context.getResources().getColor(R.color.expired_fonts));
            viewHolder.tv_title_of_panic_buying.setTextColor(context.getResources().getColor(R.color.expired_fonts));
//            viewHolder.ll_item_father.setBackgroundResource(context.getResources().getColor(android.R.color.background_light));
            viewHolder.ll_item_father.setBackgroundColor(context.getResources().getColor(android.R.color.background_light));
            ImageLoader.getInstance().displayImage(panicBuyingDetailBeanList.get(position).getIcon(), viewHolder.iv_avatar_of_panic_buying);
            changeDistanceColorEnable(viewHolder, true);

        } else if (time > 0) {
            viewHolder.tv_title_of_panic_buying.setText("" + panicBuyingDetailBeanList.get(position).getTitle());
            Log.i("time111", "" + (panicBuyingDetailBeanList.get(position).getEndTime() - panicBuyingDetailBeanList.get(position).getCreateTime()));
            viewHolder.tv_resttime_of_panic_buying.setText("限时抢购剩余时间：" + formatDuring((panicBuyingDetailBeanList.get(position).getEndTime() * 1000l - System.currentTimeMillis())));
            viewHolder.tv_resttime_of_panic_buying.setTextColor(context.getResources().getColor(R.color.effective_fonts_time));
            viewHolder.tv_title_of_panic_buying.setTextColor(context.getResources().getColor(R.color.effective_fonts_title));
            ImageLoader.getInstance().displayImage(panicBuyingDetailBeanList.get(position).getIcon(), viewHolder.iv_avatar_of_panic_buying);

            if(PreferencesUtil.getLogin(context)){
                emobid = PreferencesUtil.getLoginInfo(context).getEmobId();
            }else {
                emobid = PreferencesUtil.getlogoutEmobId(context);
            }

            PanicBuyingItemInfo panicBuyingItemInfo = new Select().from(PanicBuyingItemInfo.class).where("crazySalesId = ? and emobId = ?",
                    panicBuyingDetailBeanList.get(position).getCrazySalesId(), emobid).executeSingle();
            if (panicBuyingItemInfo != null) {
                viewHolder.ll_item_father.setBackgroundColor(convertView.getResources().getColor(R.color.periphery_item_colors));
                viewHolder.tv_title_of_panic_buying.setTextColor(context.getResources().getColor(R.color.readed_effective_fonts_title));
                changeDistanceColor(viewHolder, false);
            }else{
                viewHolder.ll_item_father.setBackgroundResource(R.drawable.note_the_background_map);
                viewHolder.tv_title_of_panic_buying.setTextColor(context.getResources().getColor(R.color.effective_fonts_title));
                changeDistanceColor(viewHolder, true);
            }
        }
        return convertView;
    }

private void changeDistanceColor(ViewHolder viewHolder, boolean isSelect){
    viewHolder.iv_distance.setSelected(isSelect);
    viewHolder.tv_distance.setSelected(isSelect);
}
    private void changeDistanceColorEnable(ViewHolder viewHolder, boolean isSelect){
        viewHolder.iv_distance.setEnabled(isSelect);
        viewHolder.tv_distance.setEnabled(isSelect);
    }


    
    class ViewHolder {
        public ImageView iv_avatar_of_panic_buying;
        public TextView tv_title_of_panic_buying;
        public TextView tv_resttime_of_panic_buying;
        private RelativeLayout ll_item_father;
        private ImageView iv_distance;
        private TextView tv_distance;

        ViewHolder(View view) {
            iv_avatar_of_panic_buying = (ImageView) view.findViewById(R.id.iv_avatar_of_panic_buying);
            tv_title_of_panic_buying = (TextView) view.findViewById(R.id.tv_title_of_panic_buying);
            tv_resttime_of_panic_buying = (TextView) view.findViewById(R.id.tv_resttime_of_panic_buying);
            ll_item_father = (RelativeLayout) view.findViewById(R.id.ll_item_father);
            iv_distance = (ImageView) view.findViewById(R.id.iv_distance);
            tv_distance = (TextView) view.findViewById(R.id.tv_distance);
            view.setTag(this);
        }
    }

    //    public String getTime(long l) {
//        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
//        String time = formatter.format(new Date(l));
//        return time;
//    }

    //倒计时
    public static String formatDuring(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        return days + "天" + hours + "小时";
    }

}
