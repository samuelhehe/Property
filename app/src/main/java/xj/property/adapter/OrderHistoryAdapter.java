package xj.property.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import xj.property.R;
import xj.property.beans.OrderRepairHistoryV3Bean;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.StarOnClickListener;

/**
 * Created by Administrator on 2015/4/9.
 */
public class OrderHistoryAdapter extends BaseAdapter {
    Context context;
    List<OrderRepairHistoryV3Bean.OrderRepairHistoryV3DataBean> list;
    CallBack callBack;
    private AbsListView.LayoutParams params;

    public OrderHistoryAdapter(Context context,List<OrderRepairHistoryV3Bean.OrderRepairHistoryV3DataBean> list, CallBack callBack) {
        this.context = context;
        this.list = list;
        this.callBack = callBack;
        params=new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,context.getResources().getDimensionPixelSize(R.dimen.item_repair_his));
    }

    @Override

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_history, null);
            convertView.findViewById(R.id.iv_star0).setVisibility(View.GONE);
            convertView.findViewById(R.id.tv_tochat).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.call(getItem(position));

                }

            });
            vh = new ViewHolder(convertView);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        convertView.setLayoutParams(params);
        OrderRepairHistoryV3Bean.OrderRepairHistoryV3DataBean bean = getItem(position);
        vh.tv_pay.setText("￥"+bean.getOrderPrice());
        vh.tv_type.setText("服务时长" + StrUtils.getTime(Integer.parseInt(bean.getEndTime() + "")));
        vh.tv_serial.setText(bean.getSerial());
        vh.tv_time.setText(StrUtils.getTime4Millions(bean.getEndTime() * 1000L));
        vh.tv_name.setText(bean.getShopName());

        ImageLoader.getInstance().displayImage(bean.getShopLogo(), vh.ivAvatar);
        StarOnClickListener listener = StarOnClickListener.getInstance(vh.ll_eva);
        int i = 2;
        try {
            i = bean.getScore();

        } catch (Exception e) {
            e.printStackTrace();
        }
        listener.changeColor(i);
        return convertView;
    }

    class ViewHolder {
        ImageView ivAvatar;
        TextView tv_name;
        TextView tv_type;
        TextView tv_pay;
        TextView tv_time;
        TextView tv_serial;
        LinearLayout ll_eva;
//        TextView tvStar0;

        ViewHolder(View v) {
            ivAvatar = (ImageView) v.findViewById(R.id.iv_head);
            tv_name = (TextView) v.findViewById(R.id.tv_name);
            tv_type = (TextView) v.findViewById(R.id.tv_type);
            tv_pay = (TextView) v.findViewById(R.id.tv_pay);
            tv_time = (TextView) v.findViewById(R.id.tv_time);
            tv_serial = (TextView) v.findViewById(R.id.tv_serial);
            ll_eva = (LinearLayout) v.findViewById(R.id.ll_eva);
//            tvStar0=(TextView)v.findViewById(R.id.iv_star0);
            v.setTag(this);
        }

    }

    @Override
    public OrderRepairHistoryV3Bean.OrderRepairHistoryV3DataBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public interface CallBack {
        void call(OrderRepairHistoryV3Bean.OrderRepairHistoryV3DataBean bean);
    }
}
