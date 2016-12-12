package xj.property.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import xj.property.R;
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.beans.RunForScoreAllV3Bean;
import xj.property.beans.RunForScoreBean;
import xj.property.beans.RunForScoreV3Bean;
import xj.property.utils.other.Config;
import xj.property.widget.CircleImageView;

/**
 * Created by asia on 15/11/20.
 */
public class MyRunFormAdapter extends BaseAdapter {

    private Context mContext;

    private List<RunForScoreV3Bean.RunForScoreV3DataBean> mlist;

    private DisplayImageOptions options;

    public MyRunFormAdapter(Context mContext, List<RunForScoreV3Bean.RunForScoreV3DataBean> mlist){

        this.mContext = mContext;
        this.mlist = mlist;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_picture)
                .showImageForEmptyUri(R.drawable.default_picture)
                .showImageOnFail(R.drawable.default_picture)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    public void ChangeRefresh(List<RunForScoreV3Bean.RunForScoreV3DataBean> list){
        mlist.clear();
        mlist.addAll(list);
        notifyDataSetChanged();
    }

    public void LoadMoreRefresh(List<RunForScoreV3Bean.RunForScoreV3DataBean> list){
        mlist.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = ((Activity) (mContext)).getLayoutInflater()
                    .inflate(R.layout.item_bang_zhu_runfor_my, parent, false);
            holder.mAvatar = (xj.property.widget.CircleImageView) convertView.findViewById(R.id.avatar);
            holder.mTv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.mTv_text = (TextView) convertView.findViewById(R.id.tv_text);
            holder.mTv_score = (TextView) convertView.findViewById(R.id.tv_score);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final RunForScoreV3Bean.RunForScoreV3DataBean scoreBean = mlist.get(position);

        ImageLoader.getInstance().displayImage(scoreBean.getAvatar(),holder.mAvatar,options);

        holder.mTv_name.setText(scoreBean.getNickname());

        holder.mTv_score.setText(scoreBean.getScore()+"");

        holder.mTv_text.setText(scoreBean.getType()==0?"投票得分":"赞人品得分");

        holder.mAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userGroupInfoActivity = new Intent(mContext,UserGroupInfoActivity.class);
                userGroupInfoActivity.putExtra(Config.INTENT_PARMAS2,scoreBean.getEmobIdFrom());
                mContext.startActivity(userGroupInfoActivity);
            }
        });

        return convertView;
    }

    private static final class ViewHolder {
        private CircleImageView mAvatar;
        private TextView mTv_name;
        private TextView mTv_text;
        private TextView mTv_score;
    }


}
