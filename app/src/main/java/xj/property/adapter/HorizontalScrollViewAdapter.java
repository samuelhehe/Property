package xj.property.adapter;

/**
 * Created by Administrator on 2015/10/21.
 */

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import xj.property.R;
import xj.property.activity.surrounding.PanicBuyingActivity;
import xj.property.beans.PublishedBuyingBean;
import xj.property.cache.PanicBuyingItemInfo;
import xj.property.utils.other.PreferencesUtil;

/**
 * 横向滑动适配
 */
public class HorizontalScrollViewAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    private List<PublishedBuyingBean.InfoEntity.PageDataEntity> panicBuyingDetailBeanList;

    public HorizontalScrollViewAdapter(Context context, List<PublishedBuyingBean.InfoEntity.PageDataEntity> beanPageDataEntitys) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.panicBuyingDetailBeanList = beanPageDataEntitys;
    }

    public int getCount() {
        return panicBuyingDetailBeanList.size();
    }

    public Object getItem(int position) {
        return panicBuyingDetailBeanList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.common_srrounding_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final PublishedBuyingBean.InfoEntity.PageDataEntity beanPageDataEntity = panicBuyingDetailBeanList.get(position);

        viewHolder.srrouding_item_desc_tv.setText(beanPageDataEntity.getDescr());
        viewHolder.srrounding_item_shopname_tv.setText(beanPageDataEntity.getTitle());
        viewHolder.srrounding_item_shopdistance_tv.setText(beanPageDataEntity.getDistance() + "m");


        ImageLoader.getInstance().displayImage(beanPageDataEntity.getCrazySalesImg().get(0).getImgUrl(),viewHolder.srrounding_item_icon_iv);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emobid;
                if (PreferencesUtil.getLogin(mContext)) {
                    emobid = PreferencesUtil.getLoginInfo(mContext).getEmobId();
                } else {
                    emobid = PreferencesUtil.getlogoutEmobId(mContext);
                }
                long time = ((beanPageDataEntity.getEndTime()) * 1000l) - (System.currentTimeMillis());
                Log.i("time_test_test1", System.currentTimeMillis() + "");
                Log.i("time_test_test2", (beanPageDataEntity.getEndTime()) + "");
                if (time <= 0) {
                    Toast.makeText(mContext, "抢购活动已过期！", Toast.LENGTH_SHORT).show();
                } else {

                    PanicBuyingItemInfo panicBuyingItemInfo = new Select().from(PanicBuyingItemInfo.class).where("crazySalesId = ? and emobId = ?", beanPageDataEntity.getCrazySalesId(),
                            emobid).executeSingle();
                    if (panicBuyingItemInfo == null) {
                        new PanicBuyingItemInfo(beanPageDataEntity.getCrazySalesId(), emobid).save();
                    }
                    Intent intent = new Intent(mContext, PanicBuyingActivity.class);
                    intent.putExtra("crazySalesId", beanPageDataEntity.getCrazySalesId());
                    Log.i("crazySalesId----", "" + beanPageDataEntity.getCrazySalesId());
                    mContext.startActivity(intent);
                }

            }
        });


        return convertView;
    }

    private class ViewHolder {
        ImageView srrounding_item_icon_iv;
        TextView srrouding_item_desc_tv;
        TextView srrounding_item_shopname_tv;
        TextView srrounding_item_shopdistance_tv;

        ViewHolder(View v) {
            srrounding_item_icon_iv = (ImageView) v.findViewById(R.id.srrounding_item_icon_iv);
            srrouding_item_desc_tv = (TextView) v.findViewById(R.id.srrouding_item_desc_tv);
            srrounding_item_shopname_tv = (TextView) v.findViewById(R.id.srrounding_item_shopname_tv);
            srrounding_item_shopdistance_tv = (TextView) v.findViewById(R.id.srrounding_item_shopdistance_tv);

            v.setTag(this);
        }
    }

}