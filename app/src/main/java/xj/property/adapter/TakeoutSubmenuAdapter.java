package xj.property.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


import java.util.List;

import de.greenrobot.event.EventBus;
import xj.property.R;
import xj.property.beans.ShopItemDetailBean;
import xj.property.event.ButtonOnClickEvent;

/**
 * Created by maxwell on 15/1/9.
 */
public class TakeoutSubmenuAdapter extends BaseAdapter{
    /**
     * logger
     */

    ViewHolder viewHolder = null;

    private View row;

    /**
     * count of single item
     */
    private int itemCount = 0;

//    private OnClickListener onClickListener;

    final private int TYPE_DATA = 0;
    final private int TYPE_LOADING = 1;

    private Context mContext;
    private List<ShopItemDetailBean> dataList;
    private RequestQueue requestQueue;

    // public LoaderAdapter(int count, Context context, String[] url) {
    public TakeoutSubmenuAdapter(Context context, List<ShopItemDetailBean> dataList)
    {
        this.mContext = context;
        this.dataList = dataList;
        requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public int getCount()
    {
        // loading界面
        return dataList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getViewTypeCount()
    {
        return 2;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (position == dataList.size())
        {
            return TYPE_LOADING;
        } else
        {
            return TYPE_DATA;
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        OnClick leftBtnListener = null;
        OnClick rightBtnListener = null;

        int type = getItemViewType(position);
        if (convertView == null)
        {
            switch (type)
            {
                case TYPE_DATA:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_takeout_submenu, parent, false);
                    viewHolder = new ViewHolder();
                    viewHolder.tv_name_submenu_takeout = (TextView) convertView.findViewById(R.id.tv_name_submenu_takeout);
                    viewHolder.rt_submenu_takeout = (RatingBar) convertView.findViewById(R.id.rt_submenu_takeout);
                    viewHolder.tv_billcount_submenu_takeout = (TextView) convertView.findViewById(R.id.tv_billcount_submenu_takeout);
                    viewHolder.tv_price_submenu_takeout = (TextView) convertView.findViewById(R.id.tv_price_submenu_takeout);
                    viewHolder.tv_count_submenu_takeout = (TextView) convertView.findViewById(R.id.tv_count_submenu_takeout);
                    viewHolder.btn_minus_submenu_takeout = (Button)convertView.findViewById(R.id.btn_minus_submenu_takeout);
                    viewHolder.btn_plus_submenu_takeout = (Button)convertView.findViewById(R.id.btn_plus_submenu_takeout);
                    //set click listener
                    leftBtnListener = new OnClick();//在这里新建监听对象
                    viewHolder.btn_minus_submenu_takeout.setOnClickListener(leftBtnListener);
                    rightBtnListener = new OnClick();
                    viewHolder.btn_plus_submenu_takeout.setOnClickListener(rightBtnListener);

                    convertView.setTag(viewHolder);
                    convertView.setTag(viewHolder.btn_minus_submenu_takeout.getId(), leftBtnListener);//对监听对象保存
                    convertView.setTag(viewHolder.btn_plus_submenu_takeout.getId(), rightBtnListener);//对监听对象保存
                    break;
            }
        } else
        {
            switch (type)
            {
                case TYPE_DATA:
                    viewHolder = (ViewHolder) convertView.getTag();
                    leftBtnListener = (OnClick) convertView.getTag(viewHolder.btn_minus_submenu_takeout.getId());//重新获得监听对象
                    rightBtnListener = (OnClick) convertView.getTag(viewHolder.btn_plus_submenu_takeout.getId());//重新获得监听对象
                    break;
            }
        }



        // 设置资源
        switch (type)
        {
            case TYPE_DATA:
                viewHolder.tv_name_submenu_takeout.setText(dataList.get(position).getServiceName());
//                viewHolder.rt_submenu_takeout.setNumStars(dataList.get(position).getStars());
                viewHolder.tv_count_submenu_takeout.setText(String.valueOf(dataList.get(position).getCount()));
                viewHolder.tv_price_submenu_takeout.setText(String.valueOf(dataList.get(position).getPrice())+"元/份");

                //set button callback
//                viewHolder.btn_minus_submenu_takeout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        onClickListener.onClick(position,"left");
//                        if(itemCount>=1){
//                            itemCount -- ;
//                        }
//                        viewHolder.tv_count_submenu_takeout.setText(itemCount);
//                    }
//                });
//                viewHolder.btn_plus_submenu_takeout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        onClickListener.onClick(position,"right");
//                        logger.info("click right ,item count is :"+itemCount);
//                        itemCount ++;
//                    }
//                });
                leftBtnListener.setPosition(position,0);
                rightBtnListener.setPosition(position,1);
                break;
        }
        return convertView;
    }
    class ViewHolder
    {
        public TextView tv_name_submenu_takeout;
        public RatingBar rt_submenu_takeout;
        public TextView tv_billcount_submenu_takeout;
        public TextView tv_price_submenu_takeout;
        public TextView tv_count_submenu_takeout;
        public Button btn_minus_submenu_takeout;
        public Button btn_plus_submenu_takeout;

    }

//    //click listener
//    public interface OnClickListener {
//        public void onClick(int position, String tag);
//    }
//
//    public void setOnClickListener(OnClickListener onClickListener) {
//        this.onClickListener = onClickListener;
//    }

    class OnClick implements View.OnClickListener {
        int index;
        int position;

        public void setPosition(int position,int index) {
            this.index = index;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(new ButtonOnClickEvent("btn click at :",position,index,0));
        }

    }
}
