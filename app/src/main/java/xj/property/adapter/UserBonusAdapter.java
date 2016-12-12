package xj.property.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import xj.property.R;
import xj.property.beans.UserBonusAdapterBean;
import xj.property.beans.UserBonusBean;
import xj.property.utils.TimeUtils;


/**
 * Created by Administrator on 2015/4/1.
 */
public class UserBonusAdapter extends BaseExpandableListAdapter {
    Context context;
    ArrayList<UserBonusAdapterBean> group;//组列表



    public UserBonusAdapter(Context context,ArrayList<UserBonusAdapterBean> group) {
        this.context = context;
        this.group = group;
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
        //       if (convertView == null) {
        convertView = View.inflate(context, R.layout.item_user_bonus_group, null);
        groupHolder = new GroupHolder();
        groupHolder.tv_status_str = (TextView) convertView.findViewById(R.id.tv_status_str);
        convertView.setTag(groupHolder);
//        } else {
//            groupHolder = (GroupHolder) convertView.getTag();
//        }
        groupHolder.tv_status_str.setText(group.get(groupPosition).getBonusStatusStr());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final UserBonusBean item;
        item = group.get(groupPosition).getChildList().get(childPosition);
        ChildHolder childHolder = null;
        //   if (convertView == null) {
        convertView = View.inflate(context, R.layout.item_user_bonus, null);
        childHolder = new ChildHolder();
        childHolder.rl_bg=(RelativeLayout)convertView.findViewById(R.id.rl_bg);
        childHolder.iv_bonusStatus_used = (ImageView) convertView.findViewById(R.id.iv_bonusStatus_used);
//        childHolder.rl_background=(ImageView)convertView.findViewById(R.id.rl_background);
        childHolder.tv_bonusPar = (TextView) convertView.findViewById(R.id.tv_bonusPar);
        childHolder.tv_bonusName = (TextView) convertView.findViewById(R.id.tv_bonusName);
        childHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
        childHolder.tv1=(TextView)convertView.findViewById(R.id.tv1);
        convertView.setTag(childHolder);
//        } else {
//            childHolder = (ChildHolder) convertView.getTag();
//        }
//        InputStream is=context.getResources().o
//        childHolder.rl_background.setImageBitmap(BitmapFactory.decodeStream());

        childHolder.tv_bonusPar.setText(item.getBonusPrice() + "");
        childHolder.tv_bonusName.setText(item.getBonusName());

        if (item.getUsed().trim().equals("yes")){
            childHolder.iv_bonusStatus_used.setVisibility(View.VISIBLE);
//            childHolder.rl_bg.setBackgroundColor(Color.rgb(Integer.parseInt(item.getBonusR()),Integer.parseInt(item.getBonusG()),Integer.parseInt(item.getBonusB())));
            childHolder.rl_bg.setBackgroundColor(context.getResources().getColor(R.color.item_bonus_gone));
            childHolder.tv_bonusPar.setTextColor(context.getResources().getColor(R.color.item_bonus_gone_text));
            childHolder.tv1.setTextColor(context.getResources().getColor(R.color.item_bonus_gone_text));
        }else if ("no".equals(item.getUsed().trim())){
            if (item.getExpireTime()*1000>System.currentTimeMillis()){
                childHolder.iv_bonusStatus_used.setVisibility(View.INVISIBLE);//
                int r=0;
                int g=0;
                int b=0;
                try {
                   r=Integer.parseInt(item.getBonusR());
                    g=Integer.parseInt(item.getBonusG());
                   b= Integer.parseInt(item.getBonusB());
                }catch (Exception e){

                }
                childHolder.rl_bg.setBackgroundColor(Color.rgb(r,g,b));
            }else {
                childHolder.iv_bonusStatus_used.setVisibility(View.VISIBLE);
                childHolder.rl_bg.setBackgroundColor(context.getResources().getColor(R.color.item_bonus_gone));
                childHolder.tv_bonusPar.setTextColor(context.getResources().getColor(R.color.item_bonus_gone_text));
                childHolder.tv1.setTextColor(context.getResources().getColor(R.color.item_bonus_gone_text));
                childHolder.iv_bonusStatus_used.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ticket_timeout_logo));
            }
        }


        childHolder.tv_time.setText("有效期 "+ TimeUtils.fromLongToString2(""+(item.getStartTime()*1000))+" 至 "+ TimeUtils.fromLongToString2(""+(1000*item.getExpireTime())));
        //ImageLoader.getInstance().displayImage(item.getServiceImg(), childHolder.iv_shop_image);item.getBonusPar()
        return convertView;
    }
    class GroupHolder {
        public TextView tv_status_str;
    }

    class ChildHolder {
        //        public ImageView rl_background;
        public RelativeLayout rl_bg;
        public ImageView iv_bonusStatus_used;
        public TextView tv_bonusPar;
        public TextView tv_bonusName;
        public TextView tv_time;
        public  TextView tv1;
    }

    /**
     * 将彩色图转换为灰度图
     * @param img 位图
     * @return 返回转换好的位图
     */
    public Bitmap convertGreyImg(Bitmap img,UserBonusBean item) {
        int width = img.getWidth();   //获取位图的宽
        int height = img.getHeight();  //获取位图的高

        int []pixels = new int[width * height]; //通过位图的大小创建像素点数组

        img.getPixels(pixels, 0, width, 0, 0, width, height);
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                int red = (Integer.parseInt(item.getBonusR()));
                int green = (Integer.parseInt(item.getBonusG()));

                int blue = (Integer.parseInt(item.getBonusB()));

                grey = (int)((float) red  + (float)green + (float)blue);
                pixels[width * i + j] = grey;
            }
        }
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
    }



}
