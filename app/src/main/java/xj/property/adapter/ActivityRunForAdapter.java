package xj.property.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import xj.property.R;
import xj.property.activity.runfor.MyRunForActivity;
import xj.property.activity.runfor.RunForActivity;
import xj.property.activity.runfor.RunForDialogActivity;
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.beans.RunForAllV3Bean;
import xj.property.utils.other.Config;
import xj.property.widget.CircleImageView;

/**
 * Created by asia on 15/11/20.
 */
public class ActivityRunForAdapter extends BaseAdapter {

    private Context mContext;

    private List<RunForAllV3Bean.RunForDataV3Bean> mlist;

    private DisplayImageOptions options;

    private int maxNum=0;

    private int mItemHeight;

    private String MyElectedEmobId;

    private Boolean voted;

    public Boolean getVoted() {
        return voted;
    }

    public void setVoted(Boolean voted) {
        this.voted = voted;
    }

    public String getMyElectedEmobId() {
        return MyElectedEmobId;
    }

    public void setMyElectedEmobId(String myElectedEmobId) {
        MyElectedEmobId = myElectedEmobId;
    }

    public ActivityRunForAdapter(Context mContext,List<RunForAllV3Bean.RunForDataV3Bean> mlist){
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

        mItemHeight=dip2px(mContext,2);
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void ChangeRefresh(List<RunForAllV3Bean.RunForDataV3Bean> list){
        mlist.clear();
        mlist.addAll(list);
        if(list.size()>0){
            maxNum=mlist.get(0).getScore();
        }
        notifyDataSetChanged();
    }

    public void LoadMoreRefresh(List<RunForAllV3Bean.RunForDataV3Bean> list){
        mlist.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public RunForAllV3Bean.RunForDataV3Bean getItem(int position) {
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
                    .inflate(R.layout.item_bang_zhu_runfor, parent, false);
            holder.mTv_no = (TextView) convertView.findViewById(R.id.tv_no);
            holder.mAvatar = (CircleImageView) convertView.findViewById(R.id.avatar);
            holder.mTv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.mTv_score = (TextView) convertView.findViewById(R.id.tv_score);
            holder.mTop_left = (LinearLayout) convertView.findViewById(R.id.top_left);
            holder.mTop_right = (LinearLayout) convertView.findViewById(R.id.top_right);
//            holder.mRight_percent_tv = (TextView) convertView.findViewById(R.id.right_percent_tv);
            holder.iv_direction_up = (ImageView) convertView.findViewById(R.id.iv_direction_up);
            holder.iv_direction_down = (ImageView) convertView.findViewById(R.id.iv_direction_down);
            holder.mTv_playnum = (TextView) convertView.findViewById(R.id.tv_playnum);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final RunForAllV3Bean.RunForDataV3Bean runForBean = mlist.get(position);
        if(position==0){
            maxNum = runForBean.getScore();
        }
        ImageLoader.getInstance().displayImage(runForBean.getAvatar(), holder.mAvatar, options);
        holder.mTv_name.setText(runForBean.getNickname());
        holder.mTv_score.setText(runForBean.getScore()+"分");
        holder.mTv_no.setText(""+runForBean.getRank());
        if(voted){
            if(runForBean.isVoted()){
                holder.mTv_playnum.setVisibility(View.VISIBLE);
                holder.mTv_playnum.setText("已投");
                holder.mTv_playnum.setOnClickListener(null);
            }else{
                holder.mTv_playnum.setVisibility(View.INVISIBLE);
            }
        }else{
            holder.mTv_playnum.setVisibility(View.VISIBLE);
            holder.mTv_playnum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent runForDialogActivity = new Intent(mContext,RunForDialogActivity.class);
                    runForDialogActivity.putExtra("runForBean",runForBean);
                    RunForActivity runforactivity = (RunForActivity) mContext;
                    runForDialogActivity.putExtra("mRunForBean",runforactivity.mRunForBean);
                    runforactivity.startActivityForResult(runForDialogActivity, runforactivity.VOTESUCESS);
                }
            });
        }
        if(runForBean.isArrowUpOrDown()){
            holder.iv_direction_up.setVisibility(View.VISIBLE);
            holder.iv_direction_down.setVisibility(View.GONE);
        }else{
            holder.iv_direction_up.setVisibility(View.GONE);
            holder.iv_direction_down.setVisibility(View.VISIBLE);
        }
        holder.mAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userGroupInfoActivity = new Intent(mContext,UserGroupInfoActivity.class);
                userGroupInfoActivity.putExtra(Config.INTENT_PARMAS2,runForBean.getEmobId());
                mContext.startActivity(userGroupInfoActivity);
            }
        });
        holder.mTv_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myRunForIntent = new Intent(mContext,MyRunForActivity.class);
                myRunForIntent.putExtra("runForBean",runForBean);
                mContext.startActivity(myRunForIntent);
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
        private TextView mTv_score;
        private LinearLayout mTop_left;
        private LinearLayout mTop_right;
//        private TextView mRight_percent_tv;
        private ImageView iv_direction_up;
        private ImageView iv_direction_down;
        private TextView mTv_playnum;
    }

}
