package xj.property.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import java.util.List;

import xj.property.R;
import xj.property.beans.TakeoutBean;
import xj.property.utils.image.cache.MyImageCache;

/**
 * Created by maxwell on 15/1/8.
 */
public class TakeoutAdapter extends BaseAdapter {
    final private int TYPE_DATA = 0;
    final private int TYPE_LOADING = 1;

    private MyImageCache myImageCache;
    private ImageLoader volleyImageLoader;

    private Context mContext;
    private List<TakeoutBean> dataList;
    private RequestQueue requestQueue;

    // public LoaderAdapter(int count, Context context, String[] url) {
    public TakeoutAdapter(Context context, List<TakeoutBean> dataList)
    {
        this.mContext = context;
        this.dataList = dataList;
        myImageCache = MyImageCache.getInstance();
        requestQueue = Volley.newRequestQueue(context);
        volleyImageLoader = new ImageLoader(requestQueue, myImageCache);
    }

    @Override
    public int getCount()
    {
        // loading界面
        return dataList.size() + 1;
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder = null;
        ViewHolderLoading viewHolderLoading = null;
        int type = getItemViewType(position);
        if (convertView == null)
        {
            switch (type)
            {
                case TYPE_DATA:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_takeout, parent, false);
                    viewHolder = new ViewHolder();
                    viewHolder.tv_shopname_takeout = (TextView) convertView.findViewById(R.id.tv_shopname_takeout);
                    viewHolder.rb_shop_takeout = (RatingBar) convertView.findViewById(R.id.rb_shop_takeout);
                    viewHolder.tv_billCount_takeout = (TextView) convertView.findViewById(R.id.tv_billCount_takeout);
                    viewHolder.networkImageView = (NetworkImageView) convertView.findViewById(R.id.iv_shop_takeout);
                    convertView.setTag(viewHolder);
                    break;

                case TYPE_LOADING:
                    viewHolderLoading = new ViewHolderLoading();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_loading, parent, false);
                    viewHolderLoading.tv_loading = (TextView) convertView.findViewById(R.id.tv_loading);
                    convertView.setTag(viewHolderLoading);
                    break;
            }
        } else
        {
            switch (type)
            {
                case TYPE_DATA:
                    viewHolder = (ViewHolder) convertView.getTag();
                    break;

                case TYPE_LOADING:
                    viewHolderLoading = (ViewHolderLoading) convertView.getTag();
                    break;
            }
        }

        // 设置资源
        switch (type)
        {
            case TYPE_DATA:

                viewHolder.tv_shopname_takeout.setText(dataList.get(position).getShopName());
                viewHolder.tv_billCount_takeout.setText("月接单:"+dataList.get(position).getOrderSum());
                viewHolder.rb_shop_takeout.setNumStars(5);
                viewHolder.rb_shop_takeout.setRating(dataList.get(position).getScore());

                viewHolder.networkImageView.setDefaultImageResId(R.drawable.ic_launcher);
                viewHolder.networkImageView.setErrorImageResId(R.drawable.loading_01);
//                    viewHolder.networkImageView.setImageUrl(url, volleyImageLoader);
                break;

            case TYPE_LOADING:
                viewHolderLoading.tv_loading.setText("正在加载中");
                break;
        }

        return convertView;
    }

    class ViewHolder
    {
        public NetworkImageView networkImageView;
        public TextView tv_shopname_takeout;
        public RatingBar rb_shop_takeout;
        public TextView tv_billCount_takeout;
    }

    class ViewHolderLoading
    {
        public TextView tv_loading;
    }
}
