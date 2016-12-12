package xj.property.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import xj.property.R;
import xj.property.activity.contactphone.FastShopOrderActivity;
import xj.property.beans.HistoryFastShopBean;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.FastShopCarDBUtil;

/**
 * Created by Administrator on 2015/4/9.
 *
 * v3 2016/03/17
 */
public class FastGoodHistoryAdapter extends BaseAdapter {
    Context context;
    List<HistoryFastShopBean> list;
    CallBack callBack;

    public FastGoodHistoryAdapter(Context context, List<HistoryFastShopBean> list, CallBack callBack) {
        this.context = context;
        this.list = list;
        this.callBack = callBack;
    }

    @Override

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_fastshop_history, null);
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
        final HistoryFastShopBean bean = getItem(position);

        vh.tv_pay.setText("￥" + bean.getMoney());
        vh.tv_serial.setText("订单:" + bean.getSerial());
        vh.tv_time.setText(StrUtils.getTime4Millions(bean.getEndTime() * 1000L));
        vh.delete_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((FastShopOrderActivity) context).showDeleteDialog(bean.getShopOrderId());
            }
        });
        List<HistoryFastShopBean.ShopBean> list = bean.getShopItems();
//        vh.listView.setAdapter(new XJBaseAdapter(context,R.layout.item_goods,list,new String[]{"shopName","serviceName","currentPrice"}));
        if (list != null) {
            int k = vh.ll_goods.getChildCount();
            int unAvailable = 0;
            for (int i = 0; i < list.size(); i++) {
                final HistoryFastShopBean.ShopBean shopBean = list.get(i);
                View v = null;
                if (i < k) {//足够。复用
                    v = vh.ll_goods.getChildAt(i);
                } else {//不够，inflate
                    v = View.inflate(context, R.layout.singlegood, null);
                    vh.ll_goods.addView(v);
                }
                final ImageView iv_buy_again = (ImageView) v.findViewById(R.id.iv_buy_again);
                if ("available".equals(shopBean.getStatus())) {
                    iv_buy_again.setImageResource(R.drawable.snacks_add);
                    iv_buy_again.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!FastShopCarDBUtil.addCarAble(shopBean.getServiceId())) {
                                Toast.makeText(context, R.string.fast_shop_add_error, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            ;
                            ((FastShopOrderActivity) context).changePrice(shopBean, iv_buy_again);
                        }
                    });
                } else {
                    unAvailable++;
                    iv_buy_again.setImageResource(R.drawable.order_from_shop_icon);
                }


                ImageLoader.getInstance().displayImage(shopBean.getServiceImg(), (ImageView) v.findViewById(R.id.iv_head));
                ((TextView) v.findViewById(R.id.tv_name)).setText(shopBean.getShopName() + "");
                ((TextView) v.findViewById(R.id.tv_title)).setText(shopBean.getServiceName() + "");

                if (shopBean.getOriginPrice() != null && shopBean.getOriginPrice().length() != 0) {
                    ((TextView) v.findViewById(R.id.tv_org)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                    ((TextView) v.findViewById(R.id.tv_org)).setText("原价" + shopBean.getOriginPrice());
                } else {
                    ((TextView) v.findViewById(R.id.tv_org)).setText("");
                }


                ((TextView) v.findViewById(R.id.tv_price)).setText("￥" + shopBean.getCurrentPrice() + "");
              /*  StarOnClickListener listener = StarOnClickListener.getInstance((LinearLayout)v.findViewById(R.id.ll_eva));
                int scoer = 0;
                v.findViewById(R.id.iv_star0).setVisibility(View.GONE);
                try {
                    scoer = Integer.parseInt(shopBean.getScore());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                listener.changeColor(scoer);*/
            }
            if (k > list.size()) {

                for (int i = list.size(); i < k; i++) {
                    View v = vh.ll_goods.getChildAt(i);
                    if (v != null)
                        vh.ll_goods.removeViewAt(i);
                }
            }
            if (unAvailable == 0) {
                vh.tv_tochat.setBackgroundResource(R.drawable.history_order_bt);
                vh.tv_tochat.setClickable(true);
                vh.tv_tochat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((FastShopOrderActivity) context).toCarFromOldOrder(bean);
                    }
                });
            } else {
                vh.tv_tochat.setBackgroundResource(R.drawable.shop_greybt);
                vh.tv_tochat.setClickable(false);
            }

        }

        return convertView;
    }

    class ViewHolder {
        TextView tv_pay;
        TextView tv_time;
        TextView tv_serial;
        TextView tv_tochat;
        LinearLayout ll_goods;
        ImageView delete_order;
//ListView listView;
//        TextView tvStar0;

        ViewHolder(View v) {
            tv_pay = (TextView) v.findViewById(R.id.tv_pay);
            tv_time = (TextView) v.findViewById(R.id.tv_time);
            tv_serial = (TextView) v.findViewById(R.id.tv_serial);
//            listView=(ListView)v.findViewById(R.id.lv_goods);
            ll_goods = (LinearLayout) v.findViewById(R.id.ll_goods);
            tv_tochat = (TextView) v.findViewById(R.id.tv_tochat);
            delete_order = (ImageView) v.findViewById(R.id.delete_order);
//            tvStar0=(TextView)v.findViewById(R.id.iv_star0);
            v.setTag(this);
        }

    }

    @Override
    public HistoryFastShopBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public interface CallBack {
        void call(HistoryFastShopBean bean);
    }
}
