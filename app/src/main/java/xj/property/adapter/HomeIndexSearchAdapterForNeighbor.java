package xj.property.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import xj.property.R;
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.beans.SearchUserResultRespBean;
import xj.property.utils.other.Config;
import xj.property.utils.other.UserUtils;

public class HomeIndexSearchAdapterForNeighbor extends BaseAdapter {
    Context context;
    List<SearchUserResultRespBean> list;
    LayoutInflater layoutInflater;

    public HomeIndexSearchAdapterForNeighbor(Context context, List<SearchUserResultRespBean> list) {
        if (list != null)
            this.list = list;
        this.context = context;
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
    public SearchUserResultRespBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SearchUserResultRespBean data = list.get(position);
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.common_home_search_neighbor_item, null);
            vh = new ViewHolder(convertView);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        ImageLoader.getInstance().displayImage(data.getAvatar(), vh.search_neighbor_header_iv, UserUtils.options);
        vh.search_neighbor_name_tv.setText(data.getNickname());
        loadingflay(vh, data);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserGroupInfoActivity.class);
                intent.putExtra(Config.INTENT_PARMAS2, data.getEmobId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private void loadingflay(ViewHolder vh, SearchUserResultRespBean data) {
        vh.search_neighbor_flay.removeAllViews();
        vh.search_neighbor_flay.setVisibility(View.GONE);
        List<SearchUserResultRespBean.LabelsEntity> labels = data.getLabels();
        if (labels != null && labels.size() > 0) {
            for (SearchUserResultRespBean.LabelsEntity label : labels) {
                View inflate = layoutInflater.inflate(R.layout.common_tags_item_sresult, null);
                TextView common_tags_name_tv = (TextView) inflate.findViewById(R.id.common_tags_name_tv);
                common_tags_name_tv.setText(label.getLabelContent());
                TextView common_tags_nums_tv = (TextView) inflate.findViewById(R.id.common_tags_nums_tv);
                if (Integer.valueOf(label.getCount()) > 99) {
                    common_tags_nums_tv.setText("99+");
                } else {
                    common_tags_nums_tv.setText(label.getCount());
                }
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
        xj.property.widget.FilterFlowLayout search_neighbor_flay;

        ViewHolder(View v) {
            search_neighbor_header_iv = (ImageView) v.findViewById(R.id.search_neighbor_header_iv);
            search_neighbor_name_tv = (TextView) v.findViewById(R.id.search_neighbor_name_tv);
            search_neighbor_flay = (xj.property.widget.FilterFlowLayout) v.findViewById(R.id.search_neighbor_flay);
            v.setTag(this);
        }

    }


}
