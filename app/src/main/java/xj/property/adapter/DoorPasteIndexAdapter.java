package xj.property.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.activity.doorpaste.DoorPasteDetailActivity;
import xj.property.activity.doorpaste.IwantAddPasteActivity;
import xj.property.activity.doorpaste.IwantPasteActivity;
import xj.property.beans.CanPasteRespBean;
import xj.property.beans.DoorPasteIndexBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.DensityUtil;
import xj.property.utils.ToastUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;


/**
 * Created by Administrator on 2015/4/1.
 */
public class DoorPasteIndexAdapter extends BaseAdapter {
    private final AbsListView.LayoutParams params;
    List<DoorPasteIndexBean> list = new ArrayList<>();
    Activity context;
    private UserInfoDetailBean bean;

    public DoorPasteIndexAdapter(Activity context, List<DoorPasteIndexBean> list, int width, int high) {
        if (list != null) {
            this.list = list;
        }
        this.context = context;
        params = new AbsListView.LayoutParams(DensityUtil.dip2px(context, width), DensityUtil.dip2px(context, high));
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
    public DoorPasteIndexBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final DoorPasteIndexBean itembean = list.get(position);
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.common_doorpaste_item, null);
            vh = new ViewHolder(convertView);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        //// 说明是添加的
        if (itembean.isAddBlock()) {
            vh.doorpaste_index_item_rlay.setVisibility(View.GONE);
            vh.doorpaste_index_create_iv.setVisibility(View.VISIBLE);
            vh.doorpaste_index_create_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bean = PreferencesUtil.getLoginInfo(context);
                    if (bean != null) {
                        context.startActivityForResult(new Intent(context, IwantPasteActivity.class),500);
                    } else {
                        Intent intent = new Intent(context, RegisterLoginActivity.class);
                        context.startActivity(intent);
                    }
                }
            });
        } else if (itembean.isIsexample()) {
            //// 示例
            vh.doorpaste_index_example_iv.setVisibility(View.VISIBLE);
            vh.doorpaste_index_create_iv.setVisibility(View.GONE);
            vh.doorpaste_index_item_rlay.setVisibility(View.VISIBLE);
            vh.doorpaste_index_home_tv.setText(itembean.getAddress());

            if (itembean.getTimes() > 99) {
                vh.doorpaste_index_count_tv.setText("被贴99+次");
            } else {
                vh.doorpaste_index_count_tv.setText("被贴" + itembean.getTimes() + "次");
            }
            vh.doorpaste_index_add_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    bean = PreferencesUtil.getLoginInfo(context);
                    if (bean != null) {
                        gocanAddOrStick(context, itembean.isIsexample(), itembean);
                    } else {
                        Intent intent = new Intent(context, RegisterLoginActivity.class);
                        context.startActivity(intent);
                    }
                }
            });

            vh.doorpaste_index_item_rlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, DoorPasteDetailActivity.class);
                    intent.putExtra("iwantaddpaste", itembean);
                    intent.putExtra("isExample", itembean.isIsexample());
                    context.startActivity(intent);
                }
            });

            /// 正常数据
        } else {
            vh.doorpaste_index_example_iv.setVisibility(View.GONE);
            vh.doorpaste_index_create_iv.setVisibility(View.GONE);
            vh.doorpaste_index_item_rlay.setVisibility(View.VISIBLE);
            vh.doorpaste_index_home_tv.setText(itembean.getAddress());
            if (itembean.getTimes() > 99) {
                vh.doorpaste_index_count_tv.setText("被贴99+次");
            } else {
                vh.doorpaste_index_count_tv.setText("被贴" + itembean.getTimes() + "次");
            }
            vh.doorpaste_index_add_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    bean = PreferencesUtil.getLoginInfo(context);
                    if (bean != null) {
                        gocanAddOrStick(context, itembean.isIsexample(), itembean);
                    } else {
                        Intent intent = new Intent(context, RegisterLoginActivity.class);
                        context.startActivity(intent);
                    }

                }
            });

            vh.doorpaste_index_item_rlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DoorPasteDetailActivity.class);
                    intent.putExtra("iwantaddpaste", itembean);
                    context.startActivityForResult(intent, 500);
                }
            });
        }
        if (params != null) {
            convertView.setLayoutParams(params);
        }
        return convertView;
    }

    public class ViewHolder {
        TextView doorpaste_index_home_tv;
        TextView doorpaste_index_count_tv;
        TextView doorpaste_index_add_tv;
        ImageView doorpaste_index_create_iv;
        ImageView doorpaste_index_example_iv;
        RelativeLayout doorpaste_index_item_rlay;

        ViewHolder(View v) {
            doorpaste_index_home_tv = (TextView) v.findViewById(R.id.doorpaste_index_home_tv);
            doorpaste_index_count_tv = (TextView) v.findViewById(R.id.doorpaste_index_count_tv);
            doorpaste_index_add_tv = (TextView) v.findViewById(R.id.doorpaste_index_add_tv);
            doorpaste_index_create_iv = (ImageView) v.findViewById(R.id.doorpaste_index_create_iv);
            doorpaste_index_example_iv = (ImageView) v.findViewById(R.id.doorpaste_index_example_iv);
            doorpaste_index_item_rlay = (RelativeLayout) v.findViewById(R.id.doorpaste_index_item_rlay);
            v.setTag(this);
        }
    }
    interface DoorPasteIndexService {
//        @GET("/api/v1/doors/{doorId}/canAddOrStick")
//        void canAddOrStick(@Path("doorId") int doorId, @QueryMap Map<String, String> map, Callback<CanPasteRespBean> cb);
        @GET("/api/v3/doors/{doorId}/canAddOrStick")
        void canAddOrStick(@Path("doorId") int doorId, @QueryMap Map<String, String> map, Callback<CommonRespBean<Boolean>> cb);
    }

    private void gocanAddOrStick(final Context context, final boolean isExample, final DoorPasteIndexBean itemBean) {
        if (isExample) {
            Intent intent = new Intent(context, IwantAddPasteActivity.class);
            intent.putExtra("iwantaddpaste", itemBean);
            intent.putExtra("isExample", isExample);
            context.startActivity(intent);
            return;
        }
        HashMap<String, String> option = new HashMap<>();
        option.put("emobId", "" + PreferencesUtil.getLoginInfo(context).getEmobId());
        DoorPasteIndexService service = RetrofitFactory.getInstance().create(context,option,DoorPasteIndexService.class);
        Callback<CommonRespBean<Boolean>> callback = new Callback<CommonRespBean<Boolean>>() {
            @Override
            public void success(CommonRespBean<Boolean> bean, retrofit.client.Response response) {
                if (bean != null && bean.getData()) {
                    Intent intent = new Intent(context, IwantAddPasteActivity.class);
                    intent.putExtra("iwantaddpaste", itemBean);
                    intent.putExtra("isExample", isExample);
                    context.startActivity(intent);
                } else if (bean != null) {
                    ToastUtils.showToast(context, "同一天，同一个门只能贴一次");
                } else {
                    ToastUtils.showToast(context, "数据异常");
                }
            }
            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                ToastUtils.showToast(context, "网络异常，请稍后重试！");
            }
        };
        service.canAddOrStick(itemBean.getDoorId(), option, callback);
    }


}
