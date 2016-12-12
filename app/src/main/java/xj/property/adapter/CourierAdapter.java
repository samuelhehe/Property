package xj.property.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

import xj.property.R;
import xj.property.beans.CourierListBean;
import xj.property.beans.ExpressAddressBean;
import xj.property.beans.SendWaterListBean;
import xj.property.utils.image.utils.SimpleImageDisplayer;
import xj.property.utils.image.utils.StrUtils;

/**
 * Created by n on 2015/4/14.
 */
public class CourierAdapter extends BaseAdapter {
    Context context;
    List<ExpressAddressBean.CourierDetailBean> list;
    private AbsListView.LayoutParams params;

    DisplayImageOptions option_1 = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.default_pic_courier)
            .showImageOnFail(R.drawable.default_pic_courier).showImageOnLoading(R.drawable.default_pic_courier)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(true).cacheOnDisk(true)
            .build();

    public CourierAdapter(Context context, ArrayList<ExpressAddressBean.CourierDetailBean> list){
        if (list != null)
            this.list = list;
        this.context = context;
        params=new AbsListView.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,context.getResources().getDimensionPixelSize(R.dimen.item_courier));

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
    public ExpressAddressBean.CourierDetailBean getItem(int position) {
        return list.get(position);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ExpressAddressBean.CourierDetailBean data=list.get(position);
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_courier, null);
            vh = new ViewHolder(convertView);
        } else {
            vh=(ViewHolder)convertView.getTag();
        }
        convertView.setLayoutParams(params);
        ImageLoader.getInstance().displayImage(data.getExpressLogo(), vh.iv_logo,option_1);
        vh.tv_courier_name.setText(data.getExpressName()+"");
        vh.courier_phone_num.setText(data.getExpressPhone()+"");
        String time=(data.getBeginTime() + "-" + data.getEndTime());
        vh.courier_time.setText(StrUtils.replaceBlank(time));


        /**
         *
         *       Intent phoneIntent = new Intent(
         "android.intent.action.CALL", Uri.parse("tel:"
         + couriers.get(position - fixnum).getPhone()));
         startActivity(phoneIntent);
         *
         *
         */
        convertView.setClickable(true);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:"+ data.getExpressPhone()));
                context.startActivity(phoneIntent);
            }
        });

        return convertView;
    }

    public static class ViewHolder {
        public   ImageView iv_logo;
        public   TextView tv_courier_name;
        public   TextView courier_phone_num;
        public  TextView courier_time;
        public  ImageView iv_call_phone;
        ViewHolder(View view){
            iv_logo=(ImageView)view.findViewById(R.id.iv_logo);
            tv_courier_name=(TextView)view.findViewById(R.id.tv_courier_name);
            courier_phone_num=(TextView)view.findViewById(R.id.courier_phone_num);
            courier_time=(TextView)view.findViewById(R.id.courier_time);
            iv_call_phone=(ImageView)view.findViewById(R.id.iv_call_phone);
            view.setTag(this);
        }
    }


}
