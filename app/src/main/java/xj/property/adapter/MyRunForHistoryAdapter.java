package xj.property.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import xj.property.R;
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.beans.RunForScoreHistoryV3Bean;
import xj.property.utils.other.Config;
import xj.property.widget.CircleImageView;

/**
 * Created by asia on 15/11/20.
 */
public class MyRunForHistoryAdapter extends BaseAdapter {

    private Context mContext;

    private List<RunForScoreHistoryV3Bean.RunForScoreHistoryDataV3Bean> mlist;

    private DisplayImageOptions options;

    private int maxNum;

    private int mItemHeight;

    private String MyElectedEmobId;

    public String getMyElectedEmobId() {
        return MyElectedEmobId;
    }

    public void setMyElectedEmobId(String myElectedEmobId) {
        MyElectedEmobId = myElectedEmobId;
    }

    public MyRunForHistoryAdapter(Context mContext, List<RunForScoreHistoryV3Bean.RunForScoreHistoryDataV3Bean> mlist){
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
        maxNum=mlist.get(0).getScore();
        mItemHeight=dip2px(mContext,2);
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
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
                    .inflate(R.layout.item_bang_zhu_runfor_history, parent, false);
            holder.mTv_no = (TextView) convertView.findViewById(R.id.tv_no);
            holder.mAvatar = (CircleImageView) convertView.findViewById(R.id.avatar);
            holder.mTv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.mTtv_ticket = (TextView) convertView.findViewById(R.id.tv_ticket);
            holder.mTv_people= (TextView) convertView.findViewById(R.id.tv_people);
            holder.mTop_left = (LinearLayout) convertView.findViewById(R.id.top_left);
            holder.mTop_right = (LinearLayout) convertView.findViewById(R.id.top_right);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final RunForScoreHistoryV3Bean.RunForScoreHistoryDataV3Bean runForBean = mlist.get(position);
        if(position==0){
            maxNum = runForBean.getScore();
        }
        ImageLoader.getInstance().displayImage(runForBean.getAvatar(), holder.mAvatar, options);
        holder.mTv_name.setText(runForBean.getNickname());
        holder.mTtv_ticket.setText(runForBean.getElectionCount()+"票");
        holder.mTv_people.setText(runForBean.getPraiseCount()+"人品值");
        holder.mTv_no.setText(""+runForBean.getRank());
        holder.mAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userGroupInfoActivity = new Intent(mContext,UserGroupInfoActivity.class);
                userGroupInfoActivity.putExtra(Config.INTENT_PARMAS2,runForBean.getEmobId());
                mContext.startActivity(userGroupInfoActivity);
            }
        });


        float leftPercent = Float.valueOf(runForBean.getScore()) / maxNum;

        leftPercent *= 0.8f;

        if(maxNum==0){
            leftPercent = 0.0f;
        }

        float rightPercent = 1 - leftPercent;

        holder.mTop_left.setLayoutParams(new LinearLayout.LayoutParams(0, mItemHeight, leftPercent));

        holder.mTop_right.setLayoutParams(new LinearLayout.LayoutParams(0, mItemHeight, rightPercent));
        //TODO 百分比进度条
        return convertView;
    }

    private static final class ViewHolder {
        private TextView mTv_no;
        private CircleImageView mAvatar;
        private TextView mTv_name;
        private TextView mTtv_ticket;
        private TextView mTv_people;
        private LinearLayout mTop_left;
        private LinearLayout mTop_right;
    }

}
