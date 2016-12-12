package xj.property.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import xj.property.R;
import xj.property.beans.RepairMenuBeanV3;

/**
 * Created by maxwell on 15/1/8.
 */
public class RepairAdapter extends BaseAdapter {

    private Context mContext;
    private List<RepairMenuBeanV3> dataList;

    public RepairAdapter(Context context, List<RepairMenuBeanV3> dataList) {
        this.mContext = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        // loading界面
        return dataList.size();
    }

    @Override
    public RepairMenuBeanV3 getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        RepairMenuBeanV3 bean = dataList.get(position);
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_repair, null);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        String path=bean.getCatImg();
//        if (path.toString().contains("."))//去掉本地图片后缀
//            path = path.substring(0, path.toString().lastIndexOf("."));
//        ImageLoader.getInstance().displayImage(
//                "drawable://" + mContext.getResources().getIdentifier(path, "drawable",
//                        mContext.getPackageName()),viewHolder.imageView);
        if (bean.getCatImg().equals("repair_strong_electricity_bg.png")){
            viewHolder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.repair_strong_electricity_bg));
        }else if (bean.getCatImg().equals("repair_light_electricity_bg.png")){
            viewHolder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.repair_light_electricity_bg));
        }else if (bean.getCatImg().equals("repair_water_pipe_bg.png")){
            viewHolder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.repair_water_pipe_bg));
        }
        else if (bean.getCatImg().equals("repair_synthesize_bg.png")){
            viewHolder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.repair_synthesize_bg));
        }

        viewHolder.tv_title.setText(bean.getCatName());
        viewHolder.tv_content.setText(bean.getCatDesc());


        return convertView;
    }

    class ViewHolder {
         ImageView imageView;
         TextView tv_title;
         TextView tv_content;
        ViewHolder(View v) {
            imageView = (ImageView) v.findViewById(R.id.iv_pic);
            tv_title = (TextView) v.findViewById(R.id.tv_title);
            tv_content = (TextView) v.findViewById(R.id.tv_content);
            v.setTag(this);
        }
    }

}
