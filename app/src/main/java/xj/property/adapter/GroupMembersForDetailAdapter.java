package xj.property.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import xj.property.R;
import xj.property.beans.UserGroupBean;
import xj.property.utils.other.UserUtils;

public class GroupMembersForDetailAdapter extends BaseAdapter {
    Context context;
    private List<UserGroupBean> beans;

    public GroupMembersForDetailAdapter(Context context, List<UserGroupBean> beans) {
        this.context = context;
        this.beans = beans;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return beans.size();
    }


    @Override
    public UserGroupBean getItem(int position) {
        return beans.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final UserGroupBean data = beans.get(position);
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.gruop_user_header, null);
            vh = new ViewHolder(convertView);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        if(TextUtils.equals(data.getEmobId(),"group_details_plus")){
            vh.icon.setImageResource(R.drawable.group_details_plus);
            vh.name.setText("");
        }else if(TextUtils.equals(data.getEmobId(),"group_details_minus")){
            vh.icon.setImageResource(R.drawable.group_details_minus);
            vh.name.setText("");
        }else{
            ImageLoader.getInstance().displayImage(data.getAvatar(), vh.icon, UserUtils.options);
            vh.name.setText(data.getNickname());
        }
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, UserGroupInfoActivity.class);
//                intent.putExtra(Config.INTENT_PARMAS2, data.getEmobId());
//                context.startActivity(intent);
//            }
//        });

        return convertView;
    }


    public static class ViewHolder {
        ImageView icon;
        TextView name;

        ViewHolder(View v) {
            icon = (ImageView) v.findViewById(R.id.icon);
            name = (TextView) v.findViewById(R.id.name);
            v.setTag(this);
        }

    }


}
