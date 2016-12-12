package xj.property.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import xj.property.R;
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.beans.SearchUserResultRespBean;
import xj.property.utils.DensityUtil;
import xj.property.utils.ToastUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.UserUtils;

public class GroupInviteSearchAdapterForNeighbor extends BaseAdapter {
    private List<String> membersInGroup = new ArrayList<>();
//    private Map<String, Boolean> mapLists;

    public List<String> getMapLists() {
        return mapLists;
    }

    public void setMapLists(List<String> mapLists) {
        this.mapLists = mapLists;
    }

    private List<String> mapLists = new ArrayList<String>();

    private String searchKey;
    private Context context;
    private List<SearchUserResultRespBean> list;
    private LayoutInflater layoutInflater;

    public GroupInviteSearchAdapterForNeighbor(Context context, List<SearchUserResultRespBean> list,
                                               List<String> membersInGroup,
                                               String searchKey) {
        if (list != null)
            this.list = list;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.searchKey = searchKey;
        this.membersInGroup = membersInGroup;
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
    public SearchUserResultRespBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final SearchUserResultRespBean data = list.get(position);
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.common_group_search_neighbor_item, null);
            vh = new ViewHolder(convertView);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        ImageLoader.getInstance().displayImage(data.getAvatar(), vh.search_neighbor_header_iv, UserUtils.options);
        vh.search_neighbor_name_tv.setText(data.getNickname());

        if (mapLists != null && mapLists.contains(data.getEmobId())) {
//            data.isChecked = true;
            vh.search_neighbor_cb.setChecked(true);
        } else {
//            data.isChecked = false;
            vh.search_neighbor_cb.setChecked(false);
        }
//        final ViewHolder finalVh = vh;
//        vh.search_neighbor_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                //// 已经是群成员了.
//                if (membersInGroup != null && membersInGroup.contains(data.getEmobId())) {
//                    ToastUtils.showToast(context, "已经是群成员了");
//                    if (mapLists.contains(data.getEmobId())) {
//                        mapLists.remove(data.getEmobId());
//                    }
//                    finalVh.search_neighbor_cb.setChecked(false);
//                    return;
//                }
//                if (isChecked) {
//                    mapLists.add(data.getEmobId());
//                } else {
//                    mapLists.remove(data.getEmobId());
//                }
//            }
//        });

        loadingflay(vh, data);

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

    private void loadingflay(ViewHolder vh, SearchUserResultRespBean data) {
        vh.search_neighbor_flay.removeAllViews();
        vh.search_neighbor_flay.setVisibility(View.GONE);
        List<SearchUserResultRespBean.LabelsEntity> labels = data.getLabels();
        if (labels != null && labels.size() > 0) {
            labels = processLabels(labels);
            int size = labels.size();
            for (int i = 0; i < size; i++) {
                SearchUserResultRespBean.LabelsEntity label = labels.get(i);
                View inflate = layoutInflater.inflate(R.layout.common_tags_item_sresult, null);
                RelativeLayout common_tags_rlay = (RelativeLayout) inflate.findViewById(R.id.common_tags_rlay);
                TextView common_tags_name_tv = (TextView) inflate.findViewById(R.id.common_tags_name_tv);
                common_tags_name_tv.setText(label.getLabelContent());
                TextView common_tags_nums_tv = (TextView) inflate.findViewById(R.id.common_tags_nums_tv);
                ////
                if (i == 0) {
                    common_tags_rlay.setBackground(context.getResources().getDrawable(R.drawable.search_result_green_circle));
                    int paddingleft = DensityUtil.dip2px(context, 5);
                    common_tags_name_tv.setPadding(paddingleft, 0, paddingleft, 0);
                    common_tags_name_tv.setTextColor(context.getResources().getColor(R.color.sys_green_theme_text_color));
                    common_tags_nums_tv.setTextColor(context.getResources().getColor(R.color.sys_green_theme_text_color));
                }
                if (Integer.valueOf(label.getCount()) > 99) {
                    common_tags_nums_tv.setText("99+");
                } else {
                    common_tags_nums_tv.setText(label.getCount());
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

    private List<SearchUserResultRespBean.LabelsEntity> processLabels(List<SearchUserResultRespBean.LabelsEntity> labels) {
        String searchKey1 = getSearchKey();
        if (TextUtils.isEmpty(searchKey1)) {
            return labels;
        }
        int size = labels.size();
        for (int i = 0; i < size; i++) {
            SearchUserResultRespBean.LabelsEntity labelsEntity = labels.get(i);
            String labelContent = labelsEntity.getLabelContent();
            if (!TextUtils.isEmpty(labelContent)) {
                if (labelContent.contains(searchKey1)) {
                    labels.remove(labelsEntity);
                    //// 将匹配字符添加至第一个位置
                    labels.add(0, labelsEntity);
                }
            }
        }
        return labels;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public class ViewHolder {
        public ImageView search_neighbor_header_iv;
        public TextView search_neighbor_name_tv;
        public CheckBox search_neighbor_cb;
        public xj.property.widget.FilterFlowLayout search_neighbor_flay;

        ViewHolder(View v) {
            search_neighbor_header_iv = (ImageView) v.findViewById(R.id.search_neighbor_header_iv);
            search_neighbor_name_tv = (TextView) v.findViewById(R.id.search_neighbor_name_tv);
            search_neighbor_cb = (CheckBox) v.findViewById(R.id.search_neighbor_cb);
            search_neighbor_flay = (xj.property.widget.FilterFlowLayout) v.findViewById(R.id.search_neighbor_flay);
            v.setTag(this);
        }

    }


}
