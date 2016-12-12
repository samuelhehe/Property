package xj.property.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import xj.property.R;
import xj.property.activity.contactphone.FastShopIndexActivity;
import xj.property.beans.FastShopDetailListBean;
import xj.property.utils.other.FastShopCarDBUtil;


/**
 * Created by Administrator on 2015/4/1.
 */
public class FastShopDetailAdapter extends BaseAdapter {
    List<FastShopDetailListBean.PagerItemBean> list;
    Context context;
    //FastShopDetailListBean.PagerItemBean pagerItemBean=null;
//    private  static ArrayList<ImageView> ivs =null;
    DisplayImageOptions option_2 = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.default_picture)
            .showImageForEmptyUri(R.drawable.default_picture)
            .showImageOnFail(R.drawable.default_picture)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(true).cacheOnDisk(true)
            .build();

    public FastShopDetailAdapter(Context context, List<FastShopDetailListBean.PagerItemBean> list) {
        if (list != null)
            this.list = list;
        this.context = context;
        //  params=new AbsListView.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,context.getResources().getDimensionPixelSize(R.dimen.item_active_car));
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
    public FastShopDetailListBean.PagerItemBean getItem(int position) {
        return list.get(position);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final FastShopDetailListBean.PagerItemBean itembean = list.get(position);
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_gridview_fastshop, null);
            vh = new ViewHolder(convertView);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        // convertView.setLayoutParams(params);
//        ImageLoader.getInstance().displayImage("drawable://"+R.drawable.default_picture, vh.iv_shop_image,option_2);

        if (itembean.getOriginPrice() == null || itembean.getOriginPrice().length() == 0) {
            vh.tv_good_originPrice.setText("");
            vh.tv_good_originPrice.setVisibility(View.GONE);
        } else {
            vh.tv_good_originPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            vh.tv_good_originPrice.setText("￥" + itembean.getOriginPrice());
        }

//        vh.tv_shopname.setText(itembean.getShopName());
        vh.tv_good_name.setText(itembean.getServiceName());
        vh.tv_good_price.setText("￥" + itembean.getCurrentPrice());
        ImageLoader.getInstance().displayImage(itembean.getServiceImg() + "?imageView/1/w/200/h/200", vh.iv_shop_image, option_2);
        if ("1".equals(itembean.getBrandId())) {
            vh.short_value.setVisibility(View.VISIBLE);
        } else {
            vh.short_value.setVisibility(View.GONE);
        }

//        ivs = new ArrayList<ImageView>();
//        ivs.clear();
//        ivs.add(vh.iv_star1);
//        ivs.add(vh.iv_star2);
//        ivs.add(vh.iv_star3);
//        ivs.add(vh.iv_star4);
//        ivs.add(vh.iv_star5);
        if ("yes".equals(itembean.getMultiattribute())) {
            vh.iv_shop_count_add.setImageResource(R.drawable.snacks_style);
            vh.iv_shop_count_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < itembean.getList().size(); i++) {
                        itembean.getList().get(i).setShopName(itembean.getShopName());
                        itembean.getList().get(i).setShopEmobId(itembean.getShopEmobId());
                    }
                    ((FastShopIndexActivity) context).getCurrentItemFragment().showChoicePop(itembean.getList());
                }
            });
        } else {
            vh.iv_shop_count_add.setImageResource(R.drawable.snacks_add);
            vh.iv_shop_count_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                ( (FastShopDetailActivity) context).changePrice(itembean, view);
//                ( (FastShopIndexActivity) context).changePic(itembean.getServiceImg());
//                EventBus.getDefault().post(itembean);
                    if (!FastShopCarDBUtil.addCarAble(itembean.getServiceId())) {
                        Toast.makeText(context, R.string.fast_shop_add_error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ((FastShopIndexActivity) context).getCurrentItemFragment().changePrice(itembean, view);
                    ((FastShopIndexActivity) context).refresh();
                }
            });

        }

        vh.iv_shop_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FastShopIndexActivity) context).changePic(itembean.getServiceImg());
            }
        });


//        vh.  tv_shopname.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ( (FastShopIndexActivity) context).toEva(itembean.getShopEmobId());
//            }
//        });
//            vh.tv_eva.setText(itembean.getScore()+"");
        return convertView;
    }

    public static class ViewHolder {
        ImageView iv_shop_image;
        ImageView iv_shop_count_add;
        ImageView short_value;
        //        TextView tv_shopname;
        TextView tv_good_name;
        TextView tv_good_price;
        TextView tv_good_originPrice;

        //        TextView tv_eva;
//        ImageView iv_star1;
//        ImageView iv_star2;
//        ImageView iv_star3;
//        ImageView iv_star4;
//        ImageView iv_star5;
//        LinearLayout ll_eva;
        ViewHolder(View v) {
            iv_shop_image = (ImageView) v.findViewById(R.id.iv_shop_image);
            iv_shop_count_add = (ImageView) v.findViewById(R.id.iv_shop_count_add);
//            tv_shopname=(TextView)v.findViewById(R.id.tv_shopname);
            tv_good_name = (TextView) v.findViewById(R.id.tv_good_name);
            tv_good_price = (TextView) v.findViewById(R.id.tv_good_price);
            tv_good_originPrice = (TextView) v.findViewById(R.id.tv_good_originPrice);
            short_value = (ImageView) v.findViewById(R.id.short_value);
            v.setTag(this);
        }

    }
}
