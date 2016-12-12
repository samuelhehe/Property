package xj.property.adapter;


import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import xj.property.R;
import xj.property.beans.IndexBean;
import xj.property.fragment.IndexFragment;
import xj.property.utils.other.UserUtils;

public class GridViewAdapter extends BaseAdapter {
    private Handler mHandler;
    private Context context;
    private LayoutInflater inflater;
    private static ImageView iv_icon;
    private static TextView tv_name;
    private static TextView tv_num;
    private List<IndexBean> beans;
    AbsListView.LayoutParams params;

    public GridViewAdapter(Context context, List<IndexBean> beans, int width, int high, Handler mHandler) {
        super();
        this.mHandler = mHandler;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.beans = beans;
        params = new AbsListView.LayoutParams(width, high);
    }

    public GridViewAdapter(Context context, List<IndexBean> beans) {
        super();
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.beans = beans;
    }

    @Override
    public int getCount() {
        return beans.size();
    }

    @Override
    public IndexBean getItem(int position) {
        return beans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(R.layout.item_main, null);
        iv_icon = (ImageView) view.findViewById(R.id.iv_icon_main);
        tv_name = (TextView) view.findViewById(R.id.tv_name_main);
        tv_num = (TextView) view.findViewById(R.id.tv_num);
        IndexBean bean = beans.get(position);
        /// 帮帮模块
        if (TextUtils.equals(IndexFragment.HOME_ITEM_MODEL_TYPE_BANGBANG, bean.getType())) {
            iv_icon.setBackgroundResource(context.getResources().getIdentifier(bean.getImgName(), "drawable", "xj.property"));
            tv_name.setText(bean.getServiceName());
            //// 帮帮URL web 页
        } else if (TextUtils.equals(IndexFragment.HOME_ITEM_MODEL_TYPE_URL, bean.getType())) {
            /// 默认显示模块加载图
//            iv_icon.setBackgroundResource(context.getResources().getIdentifier(bean.getImgName(), "drawable","xj.property"));
            ImageLoader.getInstance().displayImage(bean.getImgName(), iv_icon, UserUtils.home_item_loading_options);
            tv_name.setText(bean.getServiceName());
            /// 默认适配旧版本显示缓存图
        } else {
            iv_icon.setBackgroundResource(context.getResources().getIdentifier(bean.getImgName(), "drawable", "xj.property"));
            tv_name.setText(bean.getServiceName());
        }

        if (TextUtils.isEmpty(bean.getServiceName()) && bean.getImgName().equals("empty")) {
            view.findViewById(R.id.tv_complete).setVisibility(View.GONE);
        } else if (TextUtils.isEmpty(bean.getServiceName())) {
            tv_name.setVisibility(View.INVISIBLE);
            view.findViewById(R.id.tv_complete).setVisibility(View.VISIBLE);
            tv_name.setTextColor(context.getResources().getColor(R.color.index_wait_color));
            //// item个数会默认填充12个，不足12则使用不同图片补齐12
            if (position == beans.size() - 1) {
                view.findViewById(R.id.home_wait_label).setVisibility(View.VISIBLE);
                iv_icon.setBackgroundResource(R.drawable.home_item_more);
                iv_icon.setVisibility(View.VISIBLE);
                view.findViewById(R.id.tv_complete).setVisibility(View.GONE);
            }
        }

        if (position >= beans.size() - 1) {
            //// the last one
            mHandler.sendEmptyMessage(IndexFragment.SHOW_LINES_VISIABLE);
            Log.d("the last one ", "getServiceName " + bean.getServiceName());
        }

        if (params != null)
            view.setLayoutParams(params);
        return view;
    }

}
