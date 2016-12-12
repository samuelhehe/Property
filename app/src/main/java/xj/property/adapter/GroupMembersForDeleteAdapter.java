package xj.property.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMGroup;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import xj.property.R;
import xj.property.beans.SearchUserResultRespBean;
import xj.property.beans.UserGroupBean;
import xj.property.beans.UserGroupBeanForDel;
import xj.property.beans.UserInfoDetailBean;
import xj.property.utils.ToastUtils;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

public class GroupMembersForDeleteAdapter extends BaseAdapter {
    private  UserInfoDetailBean userInfoBean;
    private EMGroup emgroup;
    private Map<String, UserGroupBeanForDel> mapLists;
    Context context;
    LayoutInflater layoutInflater;
    List<UserGroupBeanForDel> list;

    public GroupMembersForDeleteAdapter(Context context, List<UserGroupBeanForDel> list, EMGroup emGroup, Map<String, UserGroupBeanForDel> resultLists) {
        if (list != null)
            this.list = list;
        this.context = context;
        userInfoBean = PreferencesUtil.getLoginInfo(context);
        this.mapLists = resultLists;
        this.emgroup = emGroup;
        layoutInflater = LayoutInflater.from(context);
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
    public UserGroupBeanForDel getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final UserGroupBeanForDel data = list.get(position);
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.common_group_member_for_delete_item, null);
            vh = new ViewHolder(convertView);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        ImageLoader.getInstance().displayImage(data.getAvatar(), vh.search_neighbor_header_iv, UserUtils.options);
        vh.search_neighbor_name_tv.setText(data.getNickname());

        vh.search_neighbor_cb.setChecked(data.isChecked);
        if (TextUtils.equals(emgroup.getOwner(), data.getEmobId())) {
            vh.search_neighbor_cb.setVisibility(View.GONE);
            vh.search_neighbor_cb.setEnabled(false);
        }else{
            vh.search_neighbor_cb.setVisibility(View.VISIBLE);
            vh.search_neighbor_cb.setEnabled(true);
        }
        vh.search_neighbor_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (TextUtils.equals(emgroup.getOwner(), data.getEmobId())) {
                    ToastUtils.showToast(context, "群主不能把自己删除");
                    data.isChecked = false;
                    mapLists.remove(data.getEmobId());
                    return;
                }
                data.isChecked = isChecked;
                if (isChecked) {
                    mapLists.put(data.getEmobId(), data);
                } else {
                    mapLists.remove(data.getEmobId());
                }
            }
        });

        loadingflay(vh, data);  /// 用户标签 加载

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

    private void loadingflay(ViewHolder vh, UserGroupBeanForDel data) {
        vh.search_neighbor_flay.removeAllViews();
        vh.search_neighbor_flay.setVisibility(View.GONE);
        List<UserGroupBeanForDel.LabelInfoBean> labels =data.getLabels();
        if (labels != null && labels.size() > 0) {
            for (UserGroupBeanForDel.LabelInfoBean label : labels) {
                View inflate = layoutInflater.inflate(R.layout.common_tags_item_sresult, null);
                TextView common_tags_name_tv = (TextView) inflate.findViewById(R.id.common_tags_name_tv);
                common_tags_name_tv.setText(label.getLabelContent());
                TextView common_tags_nums_tv = (TextView) inflate.findViewById(R.id.common_tags_nums_tv);
                if (label.getCount() > 99) {
                    common_tags_nums_tv.setText("99+");
                } else {
                    common_tags_nums_tv.setText(""+label.getCount());
                }
                common_tags_nums_tv.setVisibility(View.GONE);
                vh.search_neighbor_flay.addView(inflate);
            }
            vh.search_neighbor_flay.setVisibility(View.VISIBLE);
        } else {
            vh.search_neighbor_flay.removeAllViews();
            vh.search_neighbor_flay.setVisibility(View.GONE);
        }
    }

    public static class ViewHolder {
        ImageView search_neighbor_header_iv;
        TextView search_neighbor_name_tv;
        CheckBox search_neighbor_cb;
        xj.property.widget.FilterFlowLayout search_neighbor_flay;

        ViewHolder(View v) {
            search_neighbor_header_iv = (ImageView) v.findViewById(R.id.search_neighbor_header_iv);
            search_neighbor_name_tv = (TextView) v.findViewById(R.id.search_neighbor_name_tv);
            search_neighbor_cb = (CheckBox) v.findViewById(R.id.search_neighbor_cb);
            search_neighbor_flay = (xj.property.widget.FilterFlowLayout) v.findViewById(R.id.search_neighbor_flay);
            v.setTag(this);
        }

    }


}
