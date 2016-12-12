package xj.property.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import xj.property.R;
import xj.property.beans.RepairUncleV3Bean;
import xj.property.utils.other.ShopStatus;

/**
 * Created by Administrator on 2015/3/26.
 */
public class RepairUncleAdapter extends BaseAdapter {
    ArrayList<RepairUncleV3Bean.RepairUncleV3DataBean> list;
    Context context;
    AbsListView.LayoutParams params;
    private static ArrayList<ImageView> ivs = null;
    DisplayImageOptions option_1 = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.head_portrait_personage)
            .showImageOnFail(R.drawable.head_portrait_personage)
            .showImageOnLoading(R.drawable.head_portrait_personage)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(true).cacheOnDisk(true)
            .build();

    public RepairUncleAdapter(Context context, ArrayList<RepairUncleV3Bean.RepairUncleV3DataBean> list) {
        if (list != null)
            this.list = list;
        this.context = context;
        params = new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, context.getResources().getDimensionPixelSize(R.dimen.item_repair_uncle));
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
    public RepairUncleV3Bean.RepairUncleV3DataBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RepairUncleV3Bean.RepairUncleV3DataBean bean = list.get(position);
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_repair_uncle, null);
            vh = new ViewHolder(convertView);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        convertView.setLayoutParams(params);
        ImageLoader.getInstance().displayImage(bean.getLogo(), vh.master_logo, option_1);
        vh.tv_Name.setText(bean.getShopName());
        if (bean.getStatus().equals(ShopStatus.normal.toString())) {
            vh.tv_Status.setText("空闲");
            vh.tv_Status.setTextColor(context.getResources().getColor(R.color.item_fix_free));
            vh.ivStatus.setImageResource(R.drawable.master_list_free);
            vh.go_get.setTextColor(context.getResources().getColor(R.color.item_fix_get));
            vh.go_get.setText("去预约...");
        }
        else if(bean.getStatus().equals(ShopStatus.suspend.toString())) {
            vh.tv_Status.setText("休息中...");
            vh.tv_Status.setTextColor(context.getResources().getColor(R.color.item_fix_suspend));
            vh.ivStatus.setImageResource(R.drawable.master_list_suspend);
            vh.go_get.setTextColor(context.getResources().getColor(R.color.item_fix_sleep));
            vh.go_get.setText("休息中...");
        }
        else {
            vh.tv_Status.setText("服务中...");
            vh.tv_Status.setTextColor(context.getResources().getColor(R.color.item_fix_doing));
            vh.ivStatus.setImageResource(R.drawable.master_list_servicc);
            vh.go_get.setTextColor(context.getResources().getColor(R.color.item_fix_get));
            vh.go_get.setText("去预约...");
        }
        ivs = new ArrayList<ImageView>();
        ivs.clear();
        ivs.add(vh.iv_star1);
        ivs.add(vh.iv_star2);
        ivs.add(vh.iv_star3);
        ivs.add(vh.iv_star4);
        ivs.add(vh.iv_star5);

        int num = 0;
        try {
            num = bean.getShopId();//Integer.parseInt(bean.getScore());
        } catch (Exception e) {
            e.printStackTrace();
        }

        changeColor(num);


//        int i=5;
//        try {
//            i = Integer.parseInt(bean.getScore());
//        }catch ( Exception e){
//            e.printStackTrace();
//        }
//        switch (i){
//            case 0: vh.ivScoer.setImageResource(R.drawable.star0);break;
//            case 1: vh.ivScoer.setImageResource(R.drawable.star1);break;
//            case 2: vh.ivScoer.setImageResource(R.drawable.star2);break;
//            case 3: vh.ivScoer.setImageResource(R.drawable.star3);break;
//            case 4: vh.ivScoer.setImageResource(R.drawable.star4);break;
//            case 5: vh.ivScoer.setImageResource(R.drawable.star5);break;
//
//        }
        return convertView;
    }


    class ViewHolder {
        ImageView master_logo;
        ImageView ivScoer;
        ImageView ivStatus;
        TextView tv_Name;
        TextView tv_Status;
        TextView go_get;

        ImageView iv_star1;
        ImageView iv_star2;
        ImageView iv_star3;
        ImageView iv_star4;
        ImageView iv_star5;
        LinearLayout ll_eva;

        ViewHolder(View v) {
            master_logo = (ImageView) v.findViewById(R.id.master_logo);
            ivScoer = (ImageView) v.findViewById(R.id.iv_star_count);
            tv_Name = (TextView) v.findViewById(R.id.fixer_name);
            tv_Status = (TextView) v.findViewById(R.id.tv_states);
            ivStatus = (ImageView) v.findViewById(R.id.iv_status);
            go_get=(TextView)v.findViewById(R.id.go_get);
            ll_eva = (LinearLayout) v.findViewById(R.id.ll_eva);
            iv_star1 = (ImageView) v.findViewById(R.id.iv_star1);
            iv_star2 = (ImageView) v.findViewById(R.id.iv_star2);
            iv_star3 = (ImageView) v.findViewById(R.id.iv_star3);
            iv_star4 = (ImageView) v.findViewById(R.id.iv_star4);
            iv_star5 = (ImageView) v.findViewById(R.id.iv_star5);
            v.setTag(this);
        }
    }

    public void changeColor(int position) {
        for (int i = 0; i < ivs.size(); i++) {
            if (i < position) ivs.get(i).setImageResource(R.drawable.master_history_star_light);
            else ivs.get(i).setImageResource(R.drawable.master_history_star_dark);
        }
    }
}
