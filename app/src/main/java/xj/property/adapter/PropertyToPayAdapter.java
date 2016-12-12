package xj.property.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import xj.property.R;
import xj.property.beans.UserBonusBean;
import xj.property.utils.image.utils.StrUtils;

/**
 * Created by n on 2015/4/24.
 */
public class PropertyToPayAdapter extends BaseAdapter {
    Context context;
    ArrayList<UserBonusBean> userBonusBean;
    // 用来控制CheckBox的选中状况
    public static HashMap<Integer, Boolean> isSelected;
    private AbsListView.LayoutParams params;

    public PropertyToPayAdapter(Context context, ArrayList<UserBonusBean> userBonusBean) {
        this.context = context;
        this.userBonusBean = userBonusBean;
        isSelected = new HashMap<Integer, Boolean>();
        params=new AbsListView.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,context.getResources().getDimensionPixelSize(R.dimen.item_bonus));
        // 初始化数据
        initDate();
    }

    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < userBonusBean.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    @Override
    public int getCount() {
        return userBonusBean.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private String uselist;
    StringBuffer buffer;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        UserBonusBean bean = userBonusBean.get(position);
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_property_topay, null);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        convertView.setLayoutParams(params);
        viewHolder.tv_bonus_name.setText(bean.getBonusName());
        if(bean.getBonusName().contains("物业券")){
            viewHolder.tv_bonus_price.setText("免"+bean.getBonusPrice()+"个月");
        }else{
            viewHolder.tv_bonus_price.setText("￥"+bean.getBonusPrice()+"元");
        }
        viewHolder.tv_bonus_list.setText(bean.getBonusDetail());
        viewHolder.tv_bonus_time.setText(StrUtils.getTime42Millions(bean.getStartTime() * 1000L) + " 至 " + StrUtils.getTime42Millions(bean.getExpireTime() * 1000L));
        viewHolder.ck_bonus.setChecked(getIsSelected().get(position));
        return convertView;
    }

    public class ViewHolder {
        public TextView tv_bonus_name;
        public TextView tv_bonus_price;
        public TextView tv_bonus_list;
        public TextView tv_bonus_time;
        public CheckBox ck_bonus;

        public ViewHolder(View v) {
            tv_bonus_name = (TextView) v.findViewById(R.id.tv_bonus_name);
            tv_bonus_price = (TextView) v.findViewById(R.id.tv_bonus_price);
            tv_bonus_list = (TextView) v.findViewById(R.id.tv_bonus_list);
            tv_bonus_time = (TextView) v.findViewById(R.id.tv_bonus_time);
            ck_bonus = (CheckBox) v.findViewById(R.id.ck_bonus);

            v.setTag(this);
        }
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        PropertyToPayAdapter.isSelected = isSelected;
    }
}
