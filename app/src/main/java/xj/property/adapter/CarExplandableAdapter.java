package xj.property.adapter;

/**
 * Created by Administrator on 2015/4/2.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import xj.property.R;
import xj.property.activity.contactphone.FastShopCarActivity;
import xj.property.beans.FastShopCatBean;
import xj.property.beans.FastShopDetailListBean;
import xj.property.cache.FastShopCatModel;
import xj.property.utils.other.Arith;
import xj.property.utils.other.FastShopCarDBUtil;

public class CarExplandableAdapter extends BaseExpandableListAdapter {
    Context context;
    ArrayList<FastShopCatBean> group;//组列表
    String emobId;

    public CarExplandableAdapter(Context context, ArrayList<FastShopCatBean> group,String emobId) {
        this.context = context;
        this.group = group;
        this.emobId=emobId;
    }


    @Override
    public int getGroupCount() {
        return group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return group.get(groupPosition).getChildList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return group.get(groupPosition).getChildList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        GroupHolder groupHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_shoppingcar_group, null);
            groupHolder = new GroupHolder();
            groupHolder.tv_shop_name = (TextView) convertView.findViewById(R.id.tv_shop_name);
            groupHolder.tv_shop_price_num = (TextView) convertView.findViewById(R.id.tv_shop_price_num);
            groupHolder.tv_head=(TextView)convertView.findViewById(R.id.tv_head);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        if (groupPosition!=0){
            groupHolder.tv_head.setVisibility(View.VISIBLE);
        }else {
            groupHolder.tv_head.setVisibility(View.GONE);
        }
        groupHolder.tv_shop_name.setText(group.get(groupPosition).getShopName());
        double sum = group.get(groupPosition).getPrice();
        groupHolder.tv_shop_price_num.setText("￥" + sum);
        return convertView;


    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final FastShopCatModel item = group.get(groupPosition).getChildList().get(childPosition);
        ChildHolder childHolder = null;
//        if (convertView == null) {
        convertView = View.inflate(context, R.layout.item_shoppingcar_child, null);
        childHolder = new ChildHolder();
        childHolder.iv_shop_image = (ImageView) convertView.findViewById(R.id.iv_shop_image);
        childHolder.tv_good_name = (TextView) convertView.findViewById(R.id.tv_goodname);
        childHolder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
        childHolder.tv_sum_price_num = (TextView) convertView.findViewById(R.id.tv_sum_price_num);
        childHolder.cb_child_status = (CheckBox) convertView.findViewById(R.id.cb_child_status);
        childHolder.bt_goodcount_sub = (Button) convertView.findViewById(R.id.bt_goodcount_sub);
        childHolder.bt_goodcount_add = (Button) convertView.findViewById(R.id.bt_goodcount_add);
        convertView.setTag(childHolder);
//        } else {
//            childHolder = (ChildHolder) convertView.getTag();
//        }


        childHolder.tv_good_name.setText(item.getServiceName());
        ImageLoader.getInstance().displayImage(item.getServiceImg(), childHolder.iv_shop_image);
        int count = item.getCount();
        double singleprice = item.getPrice();
        childHolder.tv_count.setText(count + "");
        childHolder.tv_sum_price_num.setText("￥" + Arith.mul(count , singleprice) );
        //  childHolder.cb_child_status.setChecked((item.state.equals("1"))?true:false);

        if(item.state.equals("1")){
            childHolder.cb_child_status.setChecked(true);
            // item.setChecked(true);
            //  childHolder.bt_goodcount_sub.setClickable(true);
            //  childHolder.bt_goodcount_add.setClickable(true);



        }else {
            childHolder.cb_child_status.setChecked(false);
//            item.setChecked(false);
            //   childHolder.bt_goodcount_sub.setClickable(false);
            //  childHolder.bt_goodcount_add.setClickable(false);
        }


        childHolder.cb_child_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

//                FastShopCatModel   items = child.get(groupPosition).get(childPosition);
                int  number = item.getServiceId();
                Boolean  floag = true;
                if("1".equals(item.state)){
                    floag = true;
                }else{
                    floag = false;
                }
                System.out.println(number +"  " +  item.getServiceName() +"  " + !floag);
                FastShopCarDBUtil.updateGoodStateByServiceId(number, !floag,emobId);
                ((FastShopCarActivity) context).changeChildNum();

            }
        });



        childHolder.bt_goodcount_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.state.equals("1")){
                    if (item.getCount() >= 2) {
                        item.setCount(item.getCount() - 1);
                        System.out.println("count:" + item.getCount());
                        //CarExplandableAdapter.this.notifyDataSetChanged();
                        if(item.getShopItemSkuId()!=0){
                            FastShopCarDBUtil.subGoodCountByServiceIdAndSkuId(item.serviceId,item.getShopItemSkuId(),emobId);
                        }else{
                            FastShopCarDBUtil.subGoodCountByServiceId(item.serviceId,emobId);
                        }
                        ((FastShopCarActivity) context).changeChildNum();
                    } else if (item.getCount() == 0) {

                    }
                }

            }
        });
        childHolder.bt_goodcount_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.state.equals("1")){
                    //先判断限购
                    if( ! FastShopCarDBUtil.addCarAble(item.getServiceId())){
                        Toast.makeText(context,R.string.fast_shop_add_error,Toast.LENGTH_SHORT).show();
                        return;
                    };
                    item.setCount(item.getCount() + 1);

                    System.out.println("count:" + item.getCount());
                    if(item.getShopItemSkuId()!=0){
                        FastShopCarDBUtil.updateGoodCountByServiceIdAndSkuId(item.serviceId,item.getShopItemSkuId(),emobId);
                    }else{
                        FastShopCarDBUtil.updateGoodCountByServiceId(item.serviceId,emobId);
                    }
                    ((FastShopCarActivity) context).changeChildNum();
                }
            }
        });


        return convertView;
    }

    class GroupHolder {
        public TextView tv_shop_name;
        public TextView tv_shop_price_num;
        public TextView tv_head;
    }

    class ChildHolder {
        public ImageView iv_shop_image;
        public TextView tv_good_name;
        public TextView tv_count;
        public TextView tv_sum_price_num;
        public CheckBox cb_child_status;
        public Button bt_goodcount_sub;
        public Button bt_goodcount_add;
    }
}
