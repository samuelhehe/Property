package xj.property.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import xj.property.R;
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.beans.RPValueBean;
import xj.property.utils.other.Config;

/**
 * Created by che on 2015/6/9.
 *
 * v3 2016/03/21 人品值列表
 *
 */
public class RPValueAdapter extends BaseAdapter{

    private List<RPValueBean> rpValueBeans= new ArrayList<>();
    private Context context;
    private DisplayImageOptions options;

    public RPValueAdapter(Context context,List<RPValueBean> rpValueBeans) {
        this.rpValueBeans = rpValueBeans;
        this.context = context;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.head_portrait_personage)
                .showImageOnFail(R.drawable.head_portrait_personage).showImageOnLoading(R.drawable.head_portrait_personage)
                .build();
    }

    public void addData(List<RPValueBean> list){
        this.rpValueBeans.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return rpValueBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return rpValueBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_praise, null);
            holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            holder.rp_iv_user_type = (ImageView) convertView.findViewById(R.id.rp_iv_user_type);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_rp_value = (TextView) convertView.findViewById(R.id.tv_rp_value);
            holder.item_for_onclick = (LinearLayout) convertView.findViewById(R.id.item_for_onclick);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final RPValueBean    rpValueBean =  rpValueBeans.get(position);
        ImageLoader.getInstance().displayImage(rpValueBean.getAvatar(),holder.iv_avatar,options );

        /// 2015/11/19 添加帮内头衔
        initBangzhuMedal(rpValueBean.getGrade(),holder);

        holder.tv_name.setText(""+rpValueBean.getNickname());
        holder.tv_rp_value.setText(""+rpValueBean.getPraiseSum());

        holder.item_for_onclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "" + rpValueBeans.get(position).getEmobId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, UserGroupInfoActivity.class);
                intent.putExtra(Config.INTENT_PARMAS2,rpValueBean.getEmobId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }


    /**
     * 初始化用户横条奖章图片
     */
    private void initBangzhuMedal(String userType, ViewHolder holder ) {
        if ( holder.rp_iv_user_type != null) {
//            normal , bangzhu, fubangzhu ,zhanglao,bangzhong
            if (TextUtils.equals(userType, Config.USER_TYPE_ZHANGLAO)) {
                holder.rp_iv_user_type.setImageDrawable(context.getResources().getDrawable(R.drawable.life_circle_zhanglao_icon));
                holder.rp_iv_user_type.setVisibility(View.VISIBLE);
            } else if (TextUtils.equals(userType, Config.USER_TYPE_BANGZHU)) {
                holder.rp_iv_user_type.setImageDrawable(context.getResources().getDrawable(R.drawable.life_circle_bangzhu_icon));
                holder.rp_iv_user_type.setVisibility(View.VISIBLE);
            } else if (TextUtils.equals(userType, Config.USER_TYPE_FUBANGZHU)) {
                holder.rp_iv_user_type.setImageDrawable(context.getResources().getDrawable(R.drawable.life_circle_fubangzhu_icon));
                holder.rp_iv_user_type.setVisibility(View.VISIBLE);
            } else {
                holder.rp_iv_user_type.setVisibility(View.GONE);
            }
        }
    }


    private class ViewHolder {
        private ImageView iv_avatar;
        private ImageView rp_iv_user_type;
        private TextView tv_name;
        private TextView tv_rp_value;
        private LinearLayout item_for_onclick;
    }
}
